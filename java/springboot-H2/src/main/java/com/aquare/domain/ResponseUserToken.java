package com.aquare.domain;

//import lombok.Data;

/**
 * @author dengtao aquare@163.com
 * createAt: 2019/4/17
 */
//@Data
//@AllArgsConstructor
public class ResponseUserToken {
	
    private String token;

    
    public ResponseUserToken(String token){
    	this.token = token;
    }
    
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

    
}
