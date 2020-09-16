package com.sunshine.monitor.system.analysis.zyk.service;

import java.util.Map;

import com.sunshine.monitor.system.analysis.zyk.bean.PoliceSituation;
import com.sunshine.monitor.system.analysis.zyk.bean.Suspect;

public interface SuspectManager {

	/**
	 * 根据条件查询可疑库
	 * @param susp
	 * @param filter
	 * @return
	 */
	public Map<String, Object> queryList(Suspect susp, Map<String, Object> filter);
	
	/**
	 * 新增可疑库信息
	 * @param suspect
	 */
	public void saveSuspect(Suspect suspect);
	
	/**
	 * 根据ID获取单条可疑库信息
	 * @param suspectId
	 */
	public Suspect getSuspectById(String suspectId);
	
	/**
	 * 根据ID删除可疑库记录
	 * @param id
	 */
	public void deleteSuspect(String id);
	
	/**
	 * 更新可疑库记录
	 * @param suspect
	 */
	public void updateSuspect(Suspect suspect);
	
	/**
	 * 根据查询条件导出可疑库数据
	 * @param filter 导出数据量限制
	 * @param path 本地文件临时存放地址
	 * @param bean
	 * @return filename 文件名称
	 */
	public String outPutSuspectList(Map<String,Object> filter,String path,Suspect bean);
}
