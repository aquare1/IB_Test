package com.aquare.common;

/**
 * @author dengtao aquare@163.com
 * @category ResultCode
 * */
public enum ResultCode {
	FAILURE_PARAM_ERROR(122, "失败:参数校验失败 ","error.param.vilidata"),
	FAILURE_PARAM_EMPTY(99, "失败:参数不能为空","error.param.empty"),
	FAILURE_Insufficient_authority(120, "Insufficient authority","error.authority.insufficient"),
	FAILURE_Authentication_failed(121, "Authentication failed ","error.login.authentication"),
	FAILURE_ACCOUNT_NOT_EXIST(2,"acount not exist","acount not exist"),
	SUCCESS(1, "success","succeed"),
	FAILURE(0, "falure","falure");


	/**
	 * 编码
	 */
	private Integer code;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 国际化词条
	 */
	private String i18nKey;

	ResultCode(Integer code, String message,String i18nMsg) {
		this.code = code;
		this.message = message;
		this.i18nKey = i18nMsg;
	}

	public String i18nKey() {
		return this.i18nKey;
	}

	public void setI18nKey(String i18nKey) {
		this.i18nKey = i18nKey;
	}

	public Integer code() {
		return this.code;
	}
	
	public String message() {
		return this.message;
	}

	@Override
	public String toString() {
		return this.name();
	}

}