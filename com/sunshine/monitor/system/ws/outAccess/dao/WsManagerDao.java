package com.sunshine.monitor.system.ws.outAccess.dao;

import com.sunshine.monitor.comm.bean.Result;
import com.sunshine.monitor.comm.dao.DBaseDao;
import com.sunshine.monitor.system.ws.outAccess.bean.WsManager;

public interface WsManagerDao extends DBaseDao<WsManager> {
	 public Result checkWsValid(String systemType, String businessType, String sn)
	    throws Exception;
	
	 /**
	  * 查找对应处理类
	  * @param systemType
	  * @param businessType
	  * @return
	  * @throws Exception
	  */
	  public WsManager queryDetail(String systemType, String businessType)
	    throws Exception;
	  
	  
}
