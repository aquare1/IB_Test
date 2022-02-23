package com.aquare.exception;


import com.aquare.common.ResultCode;
import com.aquare.common.ResultDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author: dengtao aquare@163.com
 * createAt: 2019/4/10
 * @category 全局异常处理类
 */
@Log4j2
@RestControllerAdvice
public class DefaultExceptionHandler {

    /**
     * 处理所有常用异常
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public ResultDTO<Object> handleCustomException(CustomException e){
    	//logger.error(e.getResult().toString());
        return e.getResult();
    }
    
    /**
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultDTO<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        //log.error(ResultDTO.getErrer(e) );
        return ResultDTO.failure(ResultCode.FAILURE_PARAM_EMPTY, e);
    }
    

    @ExceptionHandler(BindException.class)
    public ResultDTO<Object> handleBindException(BindException ex) {
        // ex.getFieldError():随机返回一个对象属性的异常信息。如果要一次性返回所有对象属性异常信息，则调用ex.getAllErrors()
        FieldError fieldError = ex.getFieldError();
                    StringBuilder sb = new StringBuilder();
            sb.append(fieldError.getField()).append("=[").append(fieldError.getRejectedValue()).append("]")
                    .append(fieldError.getDefaultMessage());
        // 生成返回结果   
        //log.error(sb.toString());
        return ResultDTO.failure(ResultCode.FAILURE_PARAM_ERROR, sb.toString()+" "+ ResultDTO.getErrer(ex) );
    }

    /**
     * 参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultDTO<Object> handleBindException(ConstraintViolationException e) {
        // ex.getFieldError():随机返回一个对象属性的异常信息。如果要一次性返回所有对象属性异常信息，则调用ex.getAllErrors()
		String message = "";
		String objectName = "";
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		objectName = violations.toString();
		ArrayList<String>  msgList = new ArrayList<String>();
		ArrayList<String>  propertyPList = new  ArrayList<String>();
		for (ConstraintViolation<?> violation : violations) {
			msgList.add(violation.getMessage());
			propertyPList.add(violation.getPropertyPath().toString());
		}
		if(msgList.size()>0) {
			message = msgList.get(0);
		}
		
		if(propertyPList.size()>0) {
			objectName = propertyPList.get(0);
		}
        //log.error(objectName+message);
        //log.error(ResultDTO.getErrer(e) );
        return ResultDTO.failure( ResultCode.FAILURE_PARAM_ERROR,"param:"+objectName+" "+message + " "+e.getStackTrace()[0].getFileName() +": "+ e.getStackTrace()[0].getLineNumber()  );
    }

}
