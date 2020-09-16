package com.easymap.dao;

import org.jdom.Document;

public interface IOrgUserQueryDao {
	
	public Document findOrgUserPage(String name, String userid, int startpos,int pageSize);
	
}
