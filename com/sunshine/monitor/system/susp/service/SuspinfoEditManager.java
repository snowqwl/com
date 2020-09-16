package com.sunshine.monitor.system.susp.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;


public interface SuspinfoEditManager {
	/**
	 * 本地布控变更查询
	 * @param map
	 * @param info
	 * @return
	 */
	public Map findSuspinfoForMap(Map map, VehSuspinfo info)throws Exception;
	/**
	 * 查询veh_suspinfo表信息
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo getSuspinfoDetailForBkxh(String bkxh)throws Exception;
	
	/**
	 * 查询各地市veh_suspinfo表信息
	 * @param bkxh
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo getCitySuspinfoDetailForBkxh(String bkxh,String cityname)throws Exception;
	/**
	 * 查询veh_suspinfo表信息
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo getSuspinfoDetail(String bkxh)throws Exception;
	
	/**
	 * 本地布控变更保存
	 * @param suspInfo
	 * @return
	 * @throws Exception
	 */
	public Map updateSuspinfo(VehSuspinfo suspInfo,VehSuspinfopic vspic)throws Exception;
	/**
	 * 本地布控变更删除
	 * @param suspInfo
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public Map delSuspinfo(VehSuspinfo suspInfo, String string)throws Exception;
	/**
	 * (根据参数)查询布控表
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public List getSuspListForHphm(String hphm,String hpzl)throws Exception;
	/**
	 *查询code_url表
	 * @return
	 * @throws Exception
	 */
	public List getBkfwListTree()throws Exception;
	/**
	 *得到布控范围
	 *@param bkfw
	 * @return
	 * @throws Exception
	 */
	public String getBkfw(String bkfw) throws Exception;
	
	/**
	 * autjor hyj 2017/6/22
	 *得到跨省布控范围
	 *@param bkfw
	 * @return
	 * @throws Exception
	 */
	public String getksBkfw(String bkfw) throws Exception;
	/**
	 *得到报警方式
	 * @param bjfs
	 * @return
	 * @throws Exception
	 */
	public String getBjfs(String bjfs) throws Exception ;
	/**
	 *得到车身颜色
	 * @param csys
	 * @return
	 * @throws Exception
	 */
	public String getCsys(String csys) throws Exception;
	/**
	 *特殊字符转义
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public String TextToHtml(String s);
	/**
	 * (根据参数)查询布控表信息数目
	 * @param begin
	 * @param end
	 * @param yhdh
	 * @param bkfwlx
	 * @return
	 * @throws Exception
	 */
	public int getSuspinfoEditCount(String begin, String end, String yhdh, String bkfwlx) throws Exception;
	
	
	/**
	 * 获取审核审批内容
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public AuditApprove getAuditApprove(String bkxh)throws Exception;
	
	
	public Boolean existPic(String bkxh);
	
}
