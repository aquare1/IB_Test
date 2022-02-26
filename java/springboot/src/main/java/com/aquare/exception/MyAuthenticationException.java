package com.aquare.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;

/**
 * @author: dengtao aquare@163.com
 * createAt: 2019/4/10
 */
@Log4j2
public class MyAuthenticationException extends AuthenticationException {

	public MyAuthenticationException(String msg) {
		super(msg);
	}

}
