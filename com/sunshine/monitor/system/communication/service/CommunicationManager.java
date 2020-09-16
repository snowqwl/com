package com.sunshine.monitor.system.communication.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.communication.bean.Communication;



public interface CommunicationManager {

	/**
	 * 查询短信信息列表
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> querySMSlist(Map<String, Object> conditions) throws Exception;
	
	/**
	 * 查询卡口列表
	 * @param conditions
	 * @return Map
	 * @throws Exception
	 */
	public Map<String, Object> queryGatelist(Map<String, Object> conditions)  throws Exception;

	/**
	 * 查询用户列表
	 * @param conditions
	 * @return Map
	 * @author yudam
	 * @since  2014-11-4
	 * @throws Exception
	 */
	public Map<String, Object> queryUserlist(Map<String, Object> conditions)  throws Exception;

	public int updateGate(Map gate) throws Exception;

	public int updateUserPhone(Map user) throws Exception;
	
	public int updateCommById(Map sms)  throws Exception;
	
	/**
	 * 通过xh查询短信信息列表
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public List getSMSDatesByXh(String xh) throws Exception;
}
