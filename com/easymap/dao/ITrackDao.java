package com.easymap.dao;

import org.jdom.Document;

public interface ITrackDao {
	
	public Document findVagueQueryPage(String querytype,String querykey, int startpos, int pageSize);

	public Document findTrackQuery(String userid, String queryKey,String starttime,String endtime);
	 
}
