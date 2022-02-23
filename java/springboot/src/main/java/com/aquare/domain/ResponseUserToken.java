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
    
    private UserDetail userDetail;
    
    public ResponseUserToken(String token, UserDetail userDetail){
    	this.token = token;
    	this.userDetail = userDetail;
    }
    
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public UserDetail getUserDetail() {
		return userDetail;
	}
	
	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}
    
    
}
