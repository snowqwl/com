package com.sunshine.monitor.comm.service;

import java.util.List;
import java.util.Map;

public interface PublicManager {

	public abstract List CityReport() throws Exception;
	
	public Map FullTextSearch(String search_text) throws Exception;
}
