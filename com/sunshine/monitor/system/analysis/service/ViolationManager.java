package com.sunshine.monitor.system.analysis.service;

import java.util.List;

public interface ViolationManager {
	public List getViolationList(String wflx,String hphm,String hpzl) throws Exception;
}
