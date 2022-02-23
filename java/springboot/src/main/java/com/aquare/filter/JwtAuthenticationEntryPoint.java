package com.aquare.filter;


import com.aquare.common.JSONChange;
import com.aquare.common.ResultCode;
import com.aquare.common.ResultDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * @author: dengtao aquare@163.com
 * createAt: 2019/4/10
 * @category 认证失败切面
 */
@Log4j2
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
	
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        ResultDTO result = ResultDTO.failure(ResultCode.FAILURE_Authentication_failed, authException.getLocalizedMessage());
        printWriter.write(JSONChange.objToJson(result));
        printWriter.flush();
    }
}
