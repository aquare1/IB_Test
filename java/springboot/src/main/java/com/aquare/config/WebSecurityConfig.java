package com.aquare.config;

import com.aquare.filter.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Author: dengtao aquare@163.com
 * createAt: 2018/9/14
 * @category 安全配置
 */
@Log4j2
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    private final RestAuthenticationAccessDeniedHandler accessDeniedHandler;

    private final UserDetailsService CustomUserDetailsService;

    private final JwtAuthenticationTokenFilter authenticationTokenFilter;
    
    //@Autowired
   // private UrlPathFilterInvocationSecurityMetadataSource  urlPathFilterInvocationSecurityMetadataSource;
 
    @Autowired
    private UrlAccessDecisionManager urlAccessDecisionManager;

    @Autowired
    public WebSecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler,
                             @Qualifier("RestAuthenticationAccessDeniedHandler") RestAuthenticationAccessDeniedHandler accessDeniedHandler,
                             @Qualifier("CustomUserDetailsService") UserDetailsService CustomUserDetailsService,
                             JwtAuthenticationTokenFilter authenticationTokenFilter) {
        this.authenticationEntryPoint = unauthorizedHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.CustomUserDetailsService = CustomUserDetailsService;
        this.authenticationTokenFilter = authenticationTokenFilter;
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(this.CustomUserDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 装载BCrypt密码编码器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
                //
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()

                //
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/v1/get-person/**").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.POST,"/v1/post-person").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.DELETE,"/v1/delete-person/**").access("#oauth2.hasScope('write')")
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(urlAccessDecisionManager);
                        return o;
                    }
                })

                .anyRequest().authenticated().and()
                .formLogin().and()
                .httpBasic();
                
        // 禁用缓存
        httpSecurity.headers().cacheControl();

        // 添加JWT filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

       // httpSecurity.addFilterAfter(filter, afterFilter);

    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/swagger-ui.html")
                .antMatchers("**/swagger-ui.html")
                .antMatchers("/favicon.ico")
                .antMatchers("/**/*.css")
                .antMatchers("/**/*.js")
                .antMatchers("/**/*.png")
                .antMatchers("/**/*.gif")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/v2/**")
                .antMatchers("/**/*.ttf")
                .antMatchers("/webjars/**")
                .antMatchers("/csrf")
                .antMatchers("/")
                .antMatchers("/error")
                .antMatchers("/v2/api-docs")
                .antMatchers("**/swagger-resources/configuration/ui")
                .antMatchers("**/swagger-resources")
                .antMatchers("**/swagger-resources/configuration/security")
                .antMatchers("/v1/login")
                .antMatchers("/v1/get-person/**");
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}