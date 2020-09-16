package com.sunshine.monitor.system.analysis.dao;

import java.util.Date;
import java.util.List;

import com.sunshine.monitor.comm.dao.ScsBaseDao;
import com.sunshine.monitor.comm.util.orm.helper.Pager;
import com.sunshine.monitor.system.analysis.bean.NightCarTemp;
import com.sunshine.monitor.system.analysis.bean.NightCarTotal;

public interface NightCarTempDao extends ScsBaseDao {
	/**
	 * 
	 * @param sessionId
	 * @param groupId
	 * @param pager
	 * @param startTime
	 * @param endTime
	 * @param thresholds 阀值，出现次数thresholds次以下过滤掉
	 * @return
	 */
	public List<NightCarTotal> nightCarTempTotalList(String sessionId,String groupId,
			Pager pager, String startTime, String endTime, Integer thresholds);
	
	public List<NightCarTemp> nightCarTempListShowByFieldDistinct(String[] fields);
	
	public List<NightCarTotal> nightCarTempTotalList(String sessionId,String groupId,
			Pager pager, Date[] dates,  Integer thresholds) ;
}
