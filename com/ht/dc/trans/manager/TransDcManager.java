package com.ht.dc.trans.manager;

import com.ht.dc.trans.bean.TransObj;
import com.sunshine.monitor.comm.bean.Result;

public interface TransDcManager {
  
	public Result doReceive(TransObj obj)throws Exception;
	
}
