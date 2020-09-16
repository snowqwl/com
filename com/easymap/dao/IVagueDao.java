package com.easymap.dao;

import org.jdom.Document;

public interface IVagueDao {
	
	public Document findVagueSearch(String querytype,String querykey,String layer,int startpos,int pageSize,String points);
	
	public Document findVagueSearch(String querytype,String querykey,String selorg,String layer,int startpos,int pageSize);

	public Document findJczVagueSearch(String querytype,String querykey,String selorg,String layer,int startpos,int pageSize);

	public Document findkkVagueSearch(String querytype,String querykey,String selorg,String layer,int startpos,int pageSize);

	public Document findGajkVagueSearch(String querytype,String querykey,String selorg,String layer,int startpos,int pageSize);

	public Document findDzwlVagueSearch(String querytype,String querykey,String selorg,String layer,int startpos,int pageSize);

	public Document findPdtVagueSearch(String querytype,String querykey,String selorg,String layer,int startpos,int pageSize);

}
