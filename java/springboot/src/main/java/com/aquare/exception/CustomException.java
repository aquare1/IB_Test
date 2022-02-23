package com.aquare.exception;

import com.aquare.common.ResultDTO;
import lombok.extern.log4j.Log4j2;

//import lombok.Getter;

/**
 * @author: dengtao aquare@163.com
 * createAt: 2019/4/10
 * @category 返回通用异常结果异常类
 */
@Log4j2
public class CustomException extends RuntimeException{
	
    private ResultDTO<Object> result;

    public CustomException(ResultDTO<Object> result) {
        this.result = result;
    }

	public ResultDTO<Object> getResult() {
		return result;
	}

	public void setResult(ResultDTO<Object> result) {
		this.result = result;
	}
    
}
