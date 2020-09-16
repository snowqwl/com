package com.sunshine.monitor.system.monitor.service;

import java.util.List;
import java.util.Map;

public interface IdentProjectManager {
	
	public List<List<String>> getIdentProjectInfo(String kssj,String jssj,String kdbhs);
	
	public List<List<String>> getIdentcdProjectInfo(String kssj,String jssj,String kdbh,String fxbh);
}
