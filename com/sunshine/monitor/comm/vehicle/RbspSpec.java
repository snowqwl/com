package com.sunshine.monitor.comm.vehicle;

public interface RbspSpec {
	//======================基本配置信息============================
	/**
	 * RBSP Version 2.0
	 */
	public static final String PROFILE_VERSION = "version";

	/**
	 * Included request Server IP, PORT, SenderId
	 */
	public static final String PROFILE_BASE = "base";

	/**
	 * 目前PCI中有生成一个许可证，改许可证是跟请求方系统的IP绑定，因此一个许可证只能针对一个IP
	 */
	public static final String PROFILE_IP = "ip";

	/**
	 * 
	 */
	public static final String PROFILE_PORT = "port";

	/**
	 * 请求方ID,请求方在请求服务系统上的唯一内部标识
	 */
	public static final String PROFILE_SENDERID = "senderid";

	
	/**
	 * Request time out
	 */
	public static final String PROFILE_TIMEOUT = "timeout";
	
	
	// ==========================接收配置===========================
	
	/**
	 * Return attributes configuration
	 */
	public static final String PROFILE_RECEIVERID = "ReceiverID";

	/**
	 * 
	 */
	public static final String PROFILE_DATAOBJECTCODE = "DataObjectCode";

	
	/**
	 * Request return Fields
	 */
	public static final String PROFILE_RETURNS = "returnFields";
}
