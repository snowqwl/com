package com.sunshine.monitor.system.manager.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.manager.bean.Prefecture;
import com.sunshine.monitor.system.manager.dao.PrefectureDao;

public interface PrefectureManager {
	
	public Map<String,Object> findPrefectureForMap(Map filter, Prefecture prefecture);
	
	public List getDepartmentTree(Map filter);
	
	/**
	 * 异步获取部门树
	 */
	public List getDepartmentTreeAsync(String sjbm);
	/**
	 * 异步获取辖区部门树
	 */
	public List getPrefectureTreeAsync(String sjbm,String glbmn);
	
	/**
	 * 获取下级单位个数
	 */
	public boolean getCountForLowerDepartment(String xjbm);
	
	public List getLeaderDepartmentTree(Map filter);
	
	/**
	 * 获取卡口菜单树（递归查询）
	 * @param filter
	 * @return
	 */
	public List<Map<String,Object>> getGateTree(String dwdm);
	
	/**
	 * 获取卡口菜单树（递归查询）
	 * @param filter
	 * @return
	 */
	public List getMulGateTree(Map filter);
	
	/**
	 * 获取卡口树，不显示方向
	 * @param dwdm
	 * @return
	 */
	public List getOldGateTreeOnlyGate(String dwdm);
	
	public List getSTGateTreeOnlyGate();
	
	public List getCityTree();
	
	public boolean savePrefectureTree(String curDept, String menuids);
	
	public void setPrefectureDao(PrefectureDao paramPrefectureDao);
	
	public List findPrefectureList(Prefecture paramPrefecture);
	
	public Prefecture getPrefecture(String paramString);
	
	public List getRemainder(String paramString);
	
	/**
	 * 
	 * 函数功能说明:插入一条记录到辖区临时表
	 * 修改日期 	2013-6-25
	 * @param p
	 * @return
	 * @throws Exception    
	 * @return int
	 */
	public int insertPrefectureTemp(Prefecture p)throws Exception;
	
	/**
	 * 
	 * 函数功能说明:插入一条记录到辖区表
	 * 修改日期 	2013-6-25
	 * @param p
	 * @return
	 * @throws Exception    
	 * @return int
	 */
	public int insertPrefecture(Prefecture p)throws Exception;
	
	/**
	 * 
	 * 函数功能说明:删除辖区临时表记录
	 * 修改日期 	2013-6-25
	 * @return
	 * @throws Exception    
	 * @return int
	 */
	public int delAllForPrefectureTemp(String glbm)throws Exception;
	/**
	 * 
	 * 函数功能说明:新增部门【glbm】时需要联动更新Prefecture表
	 * 修改日期 	2013-6-25
	 * @param glbm
	 * @throws Exception    
	 * @return void
	 */
	public void loadPrefecture(String glbm)throws Exception;
	
	/**
	 * 函数功能：根据城市的 dwdm 获取城市的名称
	 * add by huanghaip
	 * @date 2017-6-23
	 * @param dwdm
	 * @return
	 */
	public String getCityName(String dwdm);
	
	/**
	 * 查询广东的行政区划树
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getGdCityTree() throws Exception;
}
