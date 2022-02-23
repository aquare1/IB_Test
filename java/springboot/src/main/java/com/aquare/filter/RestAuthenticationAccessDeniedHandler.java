package com.aquare.filter;

import com.aquare.common.JSONChange;
import com.aquare.common.ResultCode;
import com.aquare.common.ResultDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: dengtao aquare@163.com
 * createAt: 2018/9/21
 * @category 权限不足异常处理
 */
@Log4j2
@Component("RestAuthenticationAccessDeniedHandler")
public class RestAuthenticationAccessDeniedHandler implements AccessDeniedHandler {
	
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        //登陆状态下，权限不足执行该方法
    	//logger.info("权限不足： Path:"+httpServletRequest.getRequestURI() +",msg:"+e.getMessage());
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        ResultDTO result = ResultDTO.failure(ResultCode.FAILURE_Insufficient_authority, e.getLocalizedMessage());
        printWriter.write(JSONChange.objToJson(result));
        printWriter.flush();
    }
}
