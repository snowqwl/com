package com.sunshine.monitor.system.gate.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateCd;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.manager.bean.AdministrativeDivision;
import com.sunshine.monitor.system.manager.bean.Department;

public interface GateManager{
	
	/**
	 * 分页查询卡口列表
	 * @param page
	 * @param gate
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findGatePageForMap(Page page,CodeGate gate,String glbm)throws Exception;
	/**
	 * 分页查询卡口实时在线情况列表
	 * @param page
	 * @param gate
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findGatePageRedisForMap(Page page,CodeGate gate,String glbm)throws Exception;

	/**
	 * 获取卡口信息列表
	 * @param gate
	 * @param subsql
	 * @return
	 * @throws Exception
	 */
	public List<CodeGate> getGateList(CodeGate gate)throws Exception;
	
	/**
	 * 获取卡口信息(含交警卡口)列表
	 * @param gate
	 * @param subsql
	 * @return
	 * @throws Exception
	 */
	public List<CodeGate> getGateListAndJj(CodeGate gate)throws Exception;
	
	
	
	/**
	 * 保存操作
	 * @param gate
	 * @throws Exception
	 */
	public void save(CodeGate gate,byte[] kktp)throws Exception;
	
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
	 * 删除卡口
	 * @param gate
	 * @throws Exception
	 */
	public void delGate(String kdbh)throws Exception;
	
	
	/**
	 * 查询卡口信息
	 * @param kdbh
	 * @return
	 * @throws Exception
	 */
	public CodeGate getGateInfo(String kdbh) throws Exception;
	
	/**
	 * 根据卡口编号查询详细方向信息（包含车道信息）
	 * @param kdbh
	 * @return
	 * @throws Exception
	 */
	public List<CodeGateExtend> getDirectInfo(String kdbh)throws Exception;
	
	/**
	 * 获取方向信息
	 * @param kdbh
	 * @return
	 * @throws Exception
	 */
	public List<CodeGateExtend> getDirectList(String kdbh)throws Exception;
	
	/**
	 * 根据卡口编号与方向类型查询车道信息
	 * @param kdbh
	 * @param fxlx
	 * @return
	 * @throws Exception
	 */
	public List<CodeGateCd> getRoad(String fxbh)throws Exception;
		
	/**
	 * 根据卡口获取部门列表
	 * @return
	 * @throws Exception
	 */
	public List<Department> getDepartmentsByKdbh() throws Exception;
	
	/**
	 * 获取卡口
	 * @return
	 * @throws Exception
	 */
	public CodeGate getGate(String kdbh)throws Exception;
	
	/**
	 * 获取方向信息
	 * @param kdbh
	 * @param fxlx
	 * @return
	 * @throws Exception
	 */
	public CodeGateExtend getDirect(String fxbh)throws Exception;
	
	/**
	 * 获取全部卡口
	 * @return
	 * @throws Exception
	 */
	public List<CodeGate> getAllGates()throws Exception;
	
	/**
	 * 获取方向名称
	 * @return
	 * @throws Exception
	 */
	public String getDirectName(String fxbh)throws Exception;
	
	/**
	 * 获取卡口名称
	 * @param kdbh
	 * @return
	 * @throws Exception
	 */
	public String getGateName(String kdbh)throws Exception;
	
	/**
	 * 获取卡口(含交警卡口）
	 * @param kdbh
	 * @return
	 * @throws Exception
	 */
	public String getGateAndJjName(String kdbh,String tablename)throws Exception;
	
	
	
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
	 * 交管卡口树,表v_dev_tollgate
	 * @param searchText
	 * @param dwdm
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getTollGateTree(String searchText,String dwdm)throws Exception;
	
	/**
	 * 获取方向
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List getAllGateExtend(Map filter) throws Exception;
	
	/**
	 * 保存边界卡口方向信息
	 * @param codeGate
	 * @return
	 * @throws Exception
	 */
	public void saveBoundaryGate(CodeGate codeGate) throws Exception;
	
	/**
	 * 边界卡口方向信息
	 * @param extend
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public void saveBoundaryGateExtend(List<CodeGateExtend> extend,String userName,CodeGate gate) throws Exception;
	
	/**
	 * 获取交警卡口信息列表
	 * @param gate
	 * @param subsql
	 * @return
	 * @throws Exception
	 */
	public List<CodeGate> getTollGateList(CodeGate gate)throws Exception;
	
	/**
	 * 查询CODE_GATE表信息
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getGates(Map filter, CodeGate paramsCodeGate, String subSql) throws Exception;

	public List<Map<String, Object>> getGateListXls(Map filter) throws Exception;
	
	/**
	 * @author huanghaip
	 * @date 2017-3-23
	 * 获取行政区划列表
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getAdministrativeDivisions(Map<String,String> filter)throws Exception;
	
	/**
	 * @author huanghaip
	 * @date 2017-3-23
	 * 获取单条行政区划记录
	 * @param xzqhxh 行政区划序号
	 * @param xzqh   行政区号
	 * @return
	 * @throws Exception
	 */
	public AdministrativeDivision getAdminDivision(String xzqhxh, String xzqh) throws Exception;
	
	public int updateAdminDivision(AdministrativeDivision ad) throws Exception;
}