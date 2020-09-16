package com.sunshine.monitor.system.analysis.dao;

import com.sunshine.monitor.system.veh.bean.VehPassrec;

import java.util.List;
import java.util.Map;

public interface AnotherPlaceDao {
   //根据指定条件查询外地号牌的车辆信息列表
	public Map<String,Object> queryForAnotherPlaceList(VehPassrec veh,Map<String,Object> filter,String local) throws Exception ;
	//根据指定条件查询分组数据(注册地)
	public Map<String ,Object > getList(Map<String,Object> filter) throws Exception ;
	//分页查询分组数据（注册地）
	public Map getListForpage(Map<Object,String> filter) throws Exception ;
	//保存分组的注册地异地牌信息
	public String saveZcd(String taskname,Map<String, Object> filter)throws Exception ;
	//根据条件查询任务信息
	public Map findtask(String xh,Map<String, Object> filter,String taskname) throws Exception ;
	//根据任务编号查出注册地信息
	public Map findZcdList(String xh,Map<String, Object> filter) throws Exception; 
	//查出不同过车类型的号牌统计数据
	public Map queryDetilList(String xh ,Map<String ,Object> filter,String sign) throws Exception;
	//查处不同过车类型的号牌统计数据分页
	public Map queryDetilList(Map filter) throws Exception ;
	//查询不同过车类型的号牌总计数据（高危地区）
	public Map queryDangerousDetailList(String xh ,Map<String ,Object> filter,String sign);
	//查询不同过车类型的号牌总计数据分页（高危地区）
	public Map queryDangerousDetailList(Map<String ,Object> filter);
	//根据好牌号码查询过车轨迹
	public Map queryForDetilList(Map filter) throws Exception ;
	//根据任务序号删除任务
	public String sureDeleteTask(Map filter) throws Exception ;
	//地区树
	public List cityTree() throws Exception ;
	//添加高危地区组
	public int insertDangerousArray(Map filter) throws Exception ;
	//根据查询条件查询高危地区组
	public Map queryDangerous(Map filter) throws Exception ;
	//根据高危地区id删除 高危地区组
	public int deleteById(Map filter) throws Exception ;
	
	/**
	 * 查出不同过车类型的号牌统计数据:过境型，候鸟型，本地型，合计
	 * 改造 2016-9-8 liumeng
	 * @param xh
	 * @param filter
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryDetilListExt(String xh ,Map<String ,Object> filter,String sign) throws Exception;
	
	/**
	 * 查出不同过车类型的号牌统计数据--总和COUNT :过境型，候鸟型，本地型，合计
	 * 改造 2016-9-8 liumeng
	 * @param xh
	 * @param filter
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public int getForDetilTotal(String xh ,Map<String ,Object> filter,String sign) throws Exception;
	
	/**
	 * 查出不同过车类型的号牌统计数据 :预警数
	 * 改造 2016-9-9 liumeng
	 * @param xh
	 * @param filter
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryDangerousDetailListExt(String xh ,Map<String ,Object> filter,String sign) throws Exception;
	/**
	 * 查出不同过车类型的号牌统计数据--总和COUNT :预警数
	 * 改造 2016-9-9 liumeng
	 * @param xh
	 * @param filter
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public int getForDetilDangerousTotal(String xh ,Map<String ,Object> filter,String sign) throws Exception;
	
	/**
	 * 根据指定条件查询分组数据(注册地)-新增页面 liumeng 2016-10-11 
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object>  getListExt(Map<String,Object> filter) throws Exception ;
	/**
	 * 根据指定条件查询分组数据(注册地)总记录数-新增页面 liumeng 2016-10-11 
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public int getListTotal(Map<String,Object> filter) throws Exception ;
	
	
}
