package com.sunshine.monitor.system.manager.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.Prefecture;

public interface PrefectureDao extends BaseDao {
		
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
	public int getCountForLowerDepartment(String sjbm);
	
	public List getLeaderDepartmentTree(Map filter);
	
	public List getlocalDepartmentTree(Map filter);
	/**
	 * 获取卡口菜单树（地市级）
	 * @param dwdm
	 * @return
	 */
	public List getGateTree(String dwdm);
	
	/**
	 * 获取旧版卡口树
	 * @param dwdm
	 * @return
	 */
	public List getOldGateTree(String dwdm);
	
	/**
	 * 获取卡口菜单树（地市级）
	 * @param filter
	 * @return
	 */
	public List getMulGateTree(Map filter);
	
	/**
	 * 获取卡口树（省级：广东省）
	 * @return
	 */
	public List getSTGateTree();
	
	public List getOldGateTreeOnlyGate(String dwdm);
	
	public List getSTOldGateTreeOnlyGate();
	
	/**
	 * 旧版卡口
	 * @return
	 */
	public List getSTOldGateTree();
	
	public List getSTGateTreeOnlyGate();
	
	public List<Map<String,Object>> getCityTree();
	
	public boolean delPrefectureBydeptId(String curDept);
	
	public boolean savePrefectureBymenuids(String curDept, String[] menuids);
	
	public List getPrefectures(Prefecture prefecture);
	
	public Prefecture getPrefecture(String paramString);

	public int insertPrefecture(Prefecture p)throws Exception;

	public int insertPrefectureTemp(Prefecture p)throws Exception;

	public int delAllForPrefectureTemp(String glbm)throws Exception;

	public List getPrefectureTemp(String glbm)throws Exception;
	/**
	 * 获取广东行政区划树
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getGdCityTree() throws Exception;
	
	public List<Map<String,Object>> getGdAnHnCityTree() throws Exception;
}
