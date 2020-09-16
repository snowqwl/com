package com.sunshine.monitor.comm.maintain.enums;

public enum SmsCodeEnum {
	
	MONEY("100","余额不足"),
	
	ACCOUNT_CLOSE("101","账号关闭"),
	
	MESSAGE_LARGET("102","短信内容超过195字或为空或内容编码格式不正确"),
	
	PHONE_MORE_NULL("103","手机号码超过50个或合法的手机号码为空"),
	
	VISTOR_SEPORATE("104","用户访问时间间隔低于50毫秒"),
	
	RQUEST_ISNOT_POST("105","用户访问方式不是post方式"),
	
	ACCOUNT_NOTEXTIST("106","用户名不存在"),
	
	PASSWORD_ERROR("107","密码错误"),
	
	IP_ERROR("108","指定访问IP错误"),
	
	SUB_PHONE("110","小号不合法"),
	
	MESSAGE_SENSETIVE("111","短信内容内有敏感词"),
	
	OTHERS_ERROR("-100","其他未知错误");
	
	/**
	 * 错误编码
	 */
	private String code;
	
	/**
	 * 错误描述
	 */
	private String desc;
	
	private SmsCodeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	/**
	 * 根据编码，返回描述信息
	 * @param code
	 * @return
	 */
	public static String getDesc(String code){
		SmsCodeEnum[] smsCodes = SmsCodeEnum.values();
		for(SmsCodeEnum smscode : smsCodes){
			String _code = smscode.code;
			if(_code.equals(code)){
				return smscode.desc;
			}
		}
		return "";
	}
	
}
