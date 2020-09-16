package com.sunshine.monitor.system.monitor.service;

import java.util.List;
import java.util.Map;

public interface SjcsjcProjectManager {
	
	public int getSjcsQueryCount();
	
	/**
	 * 卡口上传率统计（关联方向）
	 * 
	 */
	public List<List<String>> getSjcsQueryScl(String kssj,String jssj,String jkdss);
	
	/** 
	 * 卡口上传数据统计（关联方向）
	 * @return
	 */
	public List<List<String>> getSjcsQuerySc(String kssj,String jssj,String jkdss);

	/** 
	 * 卡口上传数据统计（关联车道）
	 * @return
	 */
	public List<List<String>> getSjcsQueryCdSc(String kssj,String jssj,String jkdss,String fxbh);
	
	/** 
	 * 卡口上传率统计（关联车道）
	 * @return
	 */
	public List<List<String>> getSjcsQueryCdScl(String kssj,String jssj,String jkdss,String fxbh);
}
