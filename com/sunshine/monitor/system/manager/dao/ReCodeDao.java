package com.sunshine.monitor.system.manager.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.ReCode;

public interface ReCodeDao  extends BaseDao {

	public Map findReCodeForMap(Map filter, ReCode reCode)throws Exception;

	public ReCode getReCodeForYabh(String yabh)throws Exception;

	public int saveReCode(ReCode code)throws Exception;

	public int updateReCode(ReCode code)throws Exception;

	public List getReCodeListForKdbh(String kdbh)throws Exception;

}
