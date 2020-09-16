package com.sunshine.monitor.system.gate.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateCd;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.manager.bean.AdministrativeDivision;
import com.sunshine.monitor.system.manager.bean.Department;

public interface GateDao extends BaseDao {
	
	
	/**
	 * 查询卡口名称
	 * @param paramString
	 * @return
	 * @throws Exception
	 */
	public String getGateName(String paramString) throws Exception;
	
	/**
	 * 查询方向名称
	 * @param paramString
	 * @return
	 * @throws Exception
	 */
	public String getDirectName(String paramString) throws Exception;

	
	/**
	 * websevice查询卡口列表方法
	 * @param map
	 * @param clazz
	 * @return
	 */
	public Map<String, Object> findPageGateForMap(Map<String, Object> map,Class<?> clazz);
	/**
	 * 分页查询卡口列表
	 * @param page
	 * @param gate
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findGatePageForMap(Page page,CodeGate gate,String glbm)throws Exception;
	/**
	 * 分页查询卡口列表
	 * @param page
	 * @param gate
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findGatePageRedisForMap(Page page,CodeGate gate,String glbm)throws Exception;
	
	/**
	 * 查询旧版卡口名称
	 * @param paramString
	 * @return
	 * @throws Exception
	 */
	public String getOldGateName(String paramString) throws Exception;
	
	
	/**
	 * 获取卡口信息列表
	 * @return
	 * @throws Exception
	 */
	public List<CodeGate> getGateList(CodeGate gate)throws Exception;
	
	/**
	 * 获取卡口(含交警卡口)信息列表
	 * @return
	 * @throws Exception
	 */
	public List<CodeGate> getGateListAndJj(CodeGate gate)throws Exception;
	
	/**
	 * 保存/更新卡口信息
	 * @param gate
	 * @throws Exception
	 */
	public void saveGate(CodeGate gate)throws Exception;
	
	/**
	 * 更新卡口经纬度
	 * @param gate
	 * @throws Exception
	 */
	public void saveGateXY(String gateArray,String glbm,String ip,String yhdh)throws Exception;
	
	/**
	 * 保存/更新方向信息
	 * @param extend
	 * @throws Exception
	 */
	public void saveGateExtend(CodeGateExtend extend)throws Exception;
	
	/**
	 * 保存/更新车道信息
	 * @param road
	 * @throws Exception
	 */
	public void saveRoad(CodeGateCd road)throws Exception;
	
	/**
	 * 生成卡口编号
	 * @param xzqh
	 * @return
	 * @throws Exception
	 */
	public String getKdbh(String xzqh)throws Exception;
	
	/**
	 * 根据卡口获取部门列表
	 * @return
	 * @throws Exception
	 */
	public List<Department> getDepartmentsByKdbh() throws Exception;
	
	/**
	 * 卡口树
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getGateTreeByGlbm(String search_text) throws Exception;
	
	/**
	 * 省厅卡口树
	 * @param search_text
	 * @param dwdm
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getGateTreeByGlbmAndSt(String search_text,String dwdm) throws Exception;
	
	/**
	 * 获取方向
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List getCodeGateExtend(Map filter) throws Exception;
	
	public int getGateCount(CodeGate d, String dwdm) throws Exception;
	
	/**
	 * 保存图片
	 * @param kktp
	 * @param kdbh
	 * @throws Exception
	 */
	public void saveKktp(final byte[] kktp, String kdbh) throws Exception;
	
	/**
	 * 交管卡口树，表v_dev_tollgate
	 * @param search_text
	 * @param dwdm
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getTollGateTree(String search_text,String dwdm) throws Exception;
	
	/**
	 * 获取交警卡口信息列表
	 * @param gate
	 * @param subsql
	 * @return
	 * @throws Exception
	 */
	public List<CodeGate> getTollGateList(CodeGate gate)throws Exception;

	/**
	 * 获取方向对象
	 * @param fxbh
	 * @return
	 * @throws Exception
	 */
	public CodeGateExtend getDirect(String fxbh) throws Exception;
	
	public Map<String, Object> getGates(Map filter,CodeGate gate, String params) throws Exception;
	
	public List<Map<String, Object>> getGateListXls(Map filter) throws Exception;
	
	/**
	 * @author huanghaip
	 * @date 2017-3-23
	 * 获取行政区划列表
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getAdministrativeDivisions(Map<String,String> filter) throws Exception;
	
	/**
	 * @author huanghaip
	 * @date 2017-3-23
	 * 获取单条行政区划记录
	 * @param xzqhxh
	 * @param xzqh
	 * @return
	 * @throws Exception
	 */
	public AdministrativeDivision getAdminDivision(String xzqhxh, String xzqh) throws Exception;
	
	/**
	 * @author huanghaip
	 * @date 2017-3-23
	 * 更新某一条行政区划序号
	 * @param ad
	 * @return
	 * @throws Exception
	 */
	public int updateAdminDivision(AdministrativeDivision ad) throws Exception;
}
