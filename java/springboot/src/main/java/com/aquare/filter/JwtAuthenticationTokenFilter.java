package com.aquare.filter;


import com.aquare.common.JwtUtils;
import com.aquare.domain.UserDetail;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @category token
 * 
 * @author: aquare@163.com createAt: 2018/9/14
 */
@Log4j2
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	@Value("${jwt.header}")
	private String token_header;

	@Resource
	private JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		//log.info("JwtAuthenticationTokenFilter Path:"+request.getRequestURI());

		String auth_token = request.getHeader(this.token_header);// header里的token
		//log.info("JwtAuthenticationTokenFilter token:"+auth_token);
		try {
			// 凡是纳入鉴权范围的请求token不允许为空
			String username = "";
			if (auth_token != null && !auth_token.isEmpty()) {

				username = jwtUtils.getUsernameFromToken(auth_token);
			}

			if (jwtUtils.containToken(username, auth_token) && username != null
					&& SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetail userDetail = jwtUtils.getUserFromToken(auth_token);
				if (jwtUtils.validateToken(auth_token, userDetail)) {

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetail, null, userDetail.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}catch (Exception e) {
				Map<String, Object> map = JwtUtils.parseJwtPayload(auth_token);
				String userid = (String) map.get("user_id");
				String username = (String) map.get("sub");
				String scope = map.get("scope").toString();

				UserDetail jwtUser = new UserDetail();
				jwtUser.setId(userid);
				jwtUser.setUsername(username);

				String token = jwtUtils.generateAccessToken(jwtUser);
				jwtUtils.putToken(username, token);
				if (token != null && !token.isEmpty()) {
					if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
						UserDetail userDetail = jwtUtils.getUserFromToken(token);
						if (jwtUtils.validateToken(token, userDetail)) {
							UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
							authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
							SecurityContextHolder.getContext().setAuthentication(authentication);
						}
					}
				}
				response.setHeader("newToken", token);
				response.addHeader("Access-Control-Expose-Headers", "newToken");
				response.setContentType("application/json;charset=utf-8");
				response.setCharacterEncoding("UTF-8");

		}finally {
			try {
				chain.doFilter(request, response);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ServletException e1) {
				e1.printStackTrace();
			}
		}

	}
}
