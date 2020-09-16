package com.sunshine.monitor.system.monitor.dao;


import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;

public interface SjcsjcProjectDao extends BaseDao{
	
	/**
	 * 统计数据传输情况,在Monitor用户抽取数据
	 * @return
	 */
	public int getSjcsQueryCount();
	
	/**
	 * 统计卡口上传情况（关联方向）,在Monitor用户抽取数据
	 * @return
	 */
	
	public List<Map<String, Object>> getSjcsQuery(String kssj,String jssj,String jkdss);

	/**
	 * 统计卡口上传情况（关联车道）,在Monitor用户抽取数据
	 * @return
	 */
	public List<Map<String, Object>> getSjcsQueryCd(String kssj,String jssj,String jkdss,String fxbh);

}
