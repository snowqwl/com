package com.sunshine.monitor.system.analysis.zyk.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.zyk.bean.PoliceSituation;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface PoliceSituationManager {
	/**
	 * list-查询警情库
	 * @param bean
	 * @param filter
	 * @return
	 */
	public Map<String, Object> queryList(PoliceSituation bean, Map<String, Object> filter);
	/**
	 * add-警情库
	 * @param bean
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public int save(PoliceSituation bean,SysUser user) throws Exception;
	/**
	 * update-警情库
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int update(PoliceSituation bean) throws Exception;
	/**
	 * delete-警情库
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int delete(String id) throws Exception;
	/**
	 * 根据主键ID查询警情库
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PoliceSituation queryPoliceSituationById(String id) throws Exception;	
	
	/**
	 * 根据查询条件导出警情库数据
	 * @param filter 导出数据量限制
	 * @param path 本地文件临时存放地址
	 * @param bean
	 * @return filename 文件名称
	 */
	public String outPutPoliceSituationList(Map<String,Object> filter,String path,PoliceSituation bean);
}
