package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.comm.util.orm.helper.Pager;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;

public interface ScsVehPassrecService {
	public List<ScsVehPassrec> findList(Cnd cnd, Pager pager) throws Exception;
	
	public Map<String,Object> getVehDetail(String gcxh)throws Exception;

}
