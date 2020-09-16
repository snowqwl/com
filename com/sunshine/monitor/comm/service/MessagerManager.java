package com.sunshine.monitor.comm.service;

import java.util.List;

import com.sunshine.monitor.comm.bean.Messager;

public interface MessagerManager {
	
     public List<Messager> getMessagerInfo() throws Exception;

     public List getStatHbInfo() throws Exception;
}
