package com.sunshine.monitor.system.communication.dao;
import java.util.List;
import java.util.Map;
import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.communication.bean.Communication;

public interface CommunicationDao extends BaseDao {
	
	/**
	 * 查询短信列表
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSMSForList(Map<String, Object> conditions, String tableName) throws Exception;

	/**
	 * 查询卡口列表
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getGateForList(Map<String, Object> conditions, String tableName)  throws Exception;

	/**
	 * 查询用户列表
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getUserForList(Map<String, Object> conditions, String tableName) throws Exception;
	
	public int updateGate(Map gate);
	
	public int updateUserPhone(Map user);
	
	public int sendSusTips() throws Exception;
	
	public int sendLjNotCancelSusTips() throws Exception;
	
	//发送短信提醒需要拦截
	public int sendNoLjTips() throws Exception;
	
	public Map getcommunicateByXh (String xh) throws Exception ;
	
	//根据业务序号查询短信记录
	public List getInfoByXh(String xh) throws Exception ;
	
	public int updateCommuniceZt_1(String id);
	
	public int updateCommuniceZt_2(String id,String reason) ;

	public List getSMSDatesByXh(String xh) throws Exception;
}
