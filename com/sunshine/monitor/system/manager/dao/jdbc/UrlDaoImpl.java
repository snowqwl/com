package com.sunshine.monitor.system.manager.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.dao.UrlDao;

@Repository("urlDao")
public class UrlDaoImpl extends BaseDaoImpl implements UrlDao {
	
	public String getUrlName(String dwdm) throws Exception {
		if ((dwdm == null) || (dwdm.length() != 12)) {
			return dwdm;
		}
		//String sql = "select * from code_url where dwdm='" + dwdm + "'";
		String sql = "SELECT DWDM, URL, PORT, CONTEXT, JB, JDMC, SN, SJJD, BZ FROM CODE_URL WHERE DWDM = ?";
		List<CodeUrl> list = this.queryForList(sql, new Object[]{dwdm}, CodeUrl.class);
		if (list.size() == 0) {
			return dwdm ;
		}
		return list.get(0).getJdmc();
	}

	public List<CodeUrl> getUrls(CodeUrl d) throws Exception {
		String sql = "";
		if ((d != null) && (d.getJb() != null) && (d.getJb().length() > 0)) {
			sql = sql + " or jb='" + d.getJb() + "'";
		}
		if ((d != null) && (d.getSjjd() != null) && (d.getSjjd().length() > 0)) {
			sql = sql + " or sjjd='" + d.getSjjd() + "'";
		}
		if (sql.length() > 1) {
			sql = " WHERE " + sql.substring(3, sql.length());
		}
		sql = "SELECT DWDM, URL, PORT, CONTEXT, JB, JDMC, SN, SJJD, BZ FROM CODE_URL " + sql + " order by dwdm asc";
		
		return this.queryForList(sql, CodeUrl.class);
	}

	public List<CodeUrl> getCodeUrls() throws Exception {
		String sql = "SELECT DWDM, URL, PORT, CONTEXT, JB, JDMC, SN, SJJD, BZ FROM CODE_URL ORDER BY DWDM,SN ASC";
		return this.queryForList(sql, CodeUrl.class);
	}
	
	public List<CodeUrl> getCodeUrls(String jb) throws Exception {
		//String sql = "select * from code_url where jb='"+jb+"' order by dwdm";
		String sql = "SELECT DWDM, URL, PORT, CONTEXT, JB, JDMC, SN, SJJD, BZ FROM CODE_URL WHERE jb = " + jb + " order by nlssort(JDMC, 'NLS_SORT=SCHINESE_PINYIN_M') ";
		return this.queryForList(sql, CodeUrl.class);
	}

	public CodeUrl getUrl(String dwdm) throws Exception {
		//String sql = "select * from code_url where dwdm='" + dwdm + "'";
		String sql = "SELECT DWDM, URL, PORT, CONTEXT, JB, JDMC, SN, SJJD, BZ FROM CODE_URL WHERE dwdm = ? ";
		List<CodeUrl> list = this.queryForList(sql, new Object[]{dwdm},CodeUrl.class);
		if (list.size() == 0) {
			return null ;
		}
		return list.get(0);
	}
}