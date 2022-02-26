package com.aquare.filter;


import com.aquare.domain.UserDetail;

import com.aquare.exception.MyAuthenticationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 
 * @author: aquare@163.com
 * createAt: 2019/4/14
 */
@Log4j2
@Component
public class UrlAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, AuthenticationException {
		//logger.info("验证权限： Path:"+((FilterInvocation)o).getHttpRequest().getRequestURI());
		if(authentication.isAuthenticated()) {
        	if(authentication.getPrincipal().getClass().getSimpleName().equals("UserDetail")) {
        		UserDetail userDetail = (UserDetail)authentication.getPrincipal();
                String url = ((FilterInvocation)o).getHttpRequest().getRequestURI();

        	}else {
        		log.info("AuthenticationSimpleName:"+authentication.getPrincipal().getClass().getSimpleName());
				log.info("Authentication:"+authentication.getPrincipal());
				log.info("Authentication info not enough : Path:"+((FilterInvocation)o).getHttpRequest().getRequestURI());
				throw new AccessDeniedException("验证权限失败!");
        	}
		}else {
			log.info("Authentication Login invalid: Path:"+((FilterInvocation)o).getHttpRequest().getRequestURI());
			throw new MyAuthenticationException("未登录或登录超时");
		}
    }




    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}