package com.sunshine.monitor.system.monitor.dao;

import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.monitor.bean.KhzbProject;

public interface KhzbProjectDao extends BaseDao {
	
	/**
	 * 考核指标信息查询
	 * @param paramString1
	 * @param paramString2
	 * @param city
	 * @return
	 */
	public KhzbProject getKhzbProjectInfo(String paramString1,String paramString2, String city) throws Exception;
	
	
	/**
	 * 考核指标信息保存
	 * @param khzbProject
	 * @return
	 */
	public int saveKhzb(KhzbProject khzbProject) throws Exception;
	
	
	/**
	 * 统计指标信息记录
	 * @param khzbProject
	 * @return
	 */
	public Map getKhzbProjectList(Map filter,KhzbProject khzbProject);
	
	
	public KhzbProject getKhzbProjectforObject(KhzbProject khzbProject) throws Exception;
	
	/**
	 * 年度规化率
	 * @return
	 * @throws Exception
	 */
	public float queryNdghRate(String kssj, String jssj) throws Exception;
	
	/**
	 * 拦获数
	 * @return
	 * @throws Exception
	 */
	public float queryLHS(String kssj, String jssj) throws Exception;
	
	/**
	 * 超时撤控数
	 * @return
	 * @throws Exception
	 */
	public float queryCSCKS(String kssj, String jssj)throws Exception;
	
	/**
	 * 卡口在线率
	 * @return
	 * @throws Exception
	 */
	public float queryKkzxRate(String kssj, String jssj)throws Exception;
	
	/**
	 * 故障及时率
	 * @return
	 * @throws Exception
	 */
	public float queryGzjsRate(String kssj, String jssj) throws Exception;
	
}
