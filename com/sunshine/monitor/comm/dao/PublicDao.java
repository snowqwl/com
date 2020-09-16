package com.sunshine.monitor.comm.dao;

import java.util.List;
import java.util.Map;

public interface PublicDao extends BaseDao{

	public  List CityReport() throws Exception;
	
	public Map FullTextSearch(String search_text) throws Exception;
}
