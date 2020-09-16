package com.sunshine.monitor.system.notice.dao.jdbc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.notice.bean.SysInformation;
import com.sunshine.monitor.system.notice.bean.SysInformationreceive;
import com.sunshine.monitor.system.notice.dao.InformationDao;

@Repository
public class InformationDaoImpl extends BaseDaoImpl implements InformationDao {

	public List<SysInformationreceive> getReceivesById(String xh)
			throws Exception {
		String sql = "select * from sys_informationreceive where xh='" + xh
				+ "' order by yhdh";
		return this.queryForList(sql, SysInformationreceive.class);
	}

	public Map<String, Object> getSysInformations(Map<String, Object> conditions)
			throws Exception {
		StringBuffer sql = new StringBuffer(50);
		sql.append("select * from sys_information where 1=1 ");
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter = "sort".equalsIgnoreCase(key)
					|| "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key)
					|| "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.equals("kssj")) {
					sql.append(" and fbsj >= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else if (key.equals("jssj")) {
					sql.append(" and fbsj <= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else {
					sql.append(" and ");
					sql.append(key);
					sql.append(" = '").append(value).append("'");
				}
			}
		}
		sql.append(" order by ");
		sql.append(conditions.get("sort"));
		sql.append(" ");
		sql.append(conditions.get("order"));
		Map<String, Object> map = this.queryForMap(sql.toString(), Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()),
				SysInformation.class,2000);
		return map;
	}

	public List<Department> getDepartmentByUser() throws Exception {
		String sql = "select * from frm_department where glbm in (select distinct(glbm) from frm_sysuser) order by nlssort(bmmc, 'NLS_SORT=SCHINESE_PINYIN_M') ";
		return this.queryForList(sql, Department.class);
	}
	
	public List<Department> getDepartmentByUser(String glbm) throws Exception {
		String sql = "select * from frm_department where glbm in (select distinct(glbm) from frm_sysuser) and glbm ='" 
					+glbm
					+"'"
					+"order by nlssort(bmmc, 'NLS_SORT=SCHINESE_PINYIN_M')";
		return this.queryForList(sql, Department.class);
	}
	
	public String saveInformation(SysInformation information) throws Exception {

		String seSql = "SELECT seq_information_xh.nextval from dual";
		String xh = this.jdbcTemplate.queryForObject(seSql, String.class);

		String isExit = "SELECT count(*) from sys_information where xh = '"
				+ xh + "'";
		int is = this.jdbcTemplate.queryForInt(isExit);
		if (is == 0) {
			String insertSql = "INSERT into SYS_INFORMATION(XH, XXLB, BT, NR, CZR, CZDW, FBSJ, JZSJ, SFYX, SFGK, BZ)values"
					+ "('"
					+ xh
					+ "', '"
					+ information.getXxlb()
					+ "', '"
					+ information.getBt()
					+ "', '"
					+ information.getNr()
					+ "','"
					+ information.getCzr()
					+ "', '"
					+ information.getCzdw()
					+ "',sysdate, to_date('"
					+ information.getJzsj()
					+ "', 'yyyy-mm-dd hh24:mi:ss'), '"
					+ information.getSfyx()
					+ "','"
					+ information.getSfgk()
					+ "','"
					+ ((information.getBz() == null) ? "" : information.getBz())
					+ "')";
			int result = this.jdbcTemplate.update(insertSql);
			if (result == 1) {
				return xh;
			}
		}
		return null;
	}

	public int updateInformation(SysInformation information) throws Exception {
		String isExit = "SELECT count(*) from sys_information where xh = '"
				+ information.getXh() + "'";
		int is = this.jdbcTemplate.queryForInt(isExit);
		if (is > 0) {
			String upSql = "UPDATE SYS_INFORMATION set XXLB = '"
					+ information.getXxlb()
					+ "', BT =  '"
					+ information.getBt()
					+ "', NR   = '"
					+ information.getNr()
					+ "', CZDW = '"
					+ information.getCzdw()
					+ "', FBSJ = sysdate, JZSJ = to_date('"
					+ information.getJzsj()
					+ "', 'yyyy-mm-dd hh24:mi:ss'),SFYX = '"
					+ information.getSfyx()
					+ "',SFGK = '"
					+ information.getSfgk()
					+ "', BZ   = '"
					+ ((information.getBz() == null) ? "" : information.getBz())
					+ "' where XH = '" + information.getXh() + "' and CZR = '"
					+ information.getCzr() + "'";
			return this.jdbcTemplate.update(upSql);
		}
		return 0;
	}

	public int removeInformation(SysInformation information) throws Exception {
		String isExit = "SELECT count(*) from sys_information where xh = '"
				+ information.getXh() + "'";
		int is = this.jdbcTemplate.queryForInt(isExit);
		if (is > 0) {
			String upSql = "DELETE from SYS_INFORMATION "					
					+ "where XH = '" + information.getXh() + "' and CZR = '"
					+ information.getCzr() + "'";
			return this.jdbcTemplate.update(upSql);
		}
		return 0;
	}
	
	
	public int removeReceive(String xh) throws Exception {
		int result = 0;
		String reSql = "DELETE from SYS_INFORMATIONRECEIVE where xh = " + xh;
		result = this.jdbcTemplate.update(reSql);
		return result;
	}

	public int saveReceive(SysInformationreceive sysInformationreceive)
			throws Exception {
		String jSql = " SELECT count(*) from sys_informationreceive where xh = '"
				+ sysInformationreceive.getXh()
				+ "' and yhdh = '"
				+ sysInformationreceive.getYhdh() + "'";
		int isExit = this.jdbcTemplate.queryForInt(jSql);
		int result = 0;
		if (isExit == 0) {
			String iSql = "INSERT into SYS_INFORMATIONRECEIVE(YHDH, XH, SFJS, JSSJ, JSNR, BZ)values('"
					+ sysInformationreceive.getYhdh()
					+ "', '"
					+ sysInformationreceive.getXh()
					+ "', '"
					+ sysInformationreceive.getSfjs()
					+ "', sysdate, '"
					+ ((sysInformationreceive.getJsnr() == null) ? ""
							: sysInformationreceive.getJsnr())
					+ "', '"
					+ ((sysInformationreceive.getBz() == null) ? ""
							: sysInformationreceive.getBz()) + "')";
			result = this.jdbcTemplate.update(iSql);
		} else {
			String uSql = "UPDATE SYS_INFORMATIONRECEIVE set SFJS = '"
					+ sysInformationreceive.getSfjs()
					+ "', JSSJ = sysdate, JSNR = '"
					+ ((sysInformationreceive.getJsnr() == null) ? ""
							: sysInformationreceive.getJsnr())
					+ "', BZ = '"
					+ ((sysInformationreceive.getBz() == null) ? ""
							: sysInformationreceive.getBz())
					+ "' where YHDH = '" + sysInformationreceive.getYhdh()
					+ "' and XH = '" + sysInformationreceive.getXh() + "'";
			result = this.jdbcTemplate.update(uSql);
		}
		return result;
	}
	
	
	public List getReceivesForSublist(String id) throws Exception {
		String sql = "select * from sys_informationreceive where xh='" + id
				+ "' and sfjs='1' and jsnr is not null order by jssj desc";
		return this.jdbcTemplate.queryForList(sql, SysInformationreceive.class);
	}
	
	
	public SysInformationreceive getReceives(String id, String username)
	    throws Exception {
		SysInformationreceive sysInformationreceive = null;
      String sql = "select * from sys_informationreceive where xh='" + id
		+ "' and yhdh='" + username + "'";
      List list = this.queryForList(sql,SysInformationreceive.class);
      if (list == null && list.size() > 0) {
    	  sysInformationreceive = (SysInformationreceive)list.get(0);
      }
      return sysInformationreceive;
}
	
	public Map<String, Object> getSysInfos(Map<String, Object> condition)
			throws Exception {
		String sql = "";
		if (condition.containsKey("xxlb")) {
			sql = " and xxlb='" + condition.get("xxlb") + "'" + sql;
		}
		if (condition.containsKey("czdw")) {
			sql = " and czdw='" + condition.get("czdw") + "'" + sql;
		}
		if (condition.containsKey("bt")) {
			sql = " and bt like '%" + condition.get("bt") + "%'" + sql;
		}
		if (condition.containsKey("kssj") && condition.containsKey("jssj")) {
			sql = " and (fbsj>=to_date('" + condition.get("kssj")
					+ "','yyyy-mm-dd hh24:mi:ss') and fbsj<=to_date('"
					+ condition.get("jzsj") + "','yyyy-mm-dd hh24:mi:ss'))"
					+ sql;
		}
		if (!sql.equals(""))
			sql = " and (" + sql.substring(5, sql.length()) + ") ";
		sql = "(select xh,xxlb,bt,fbsj,jzsj,sfgk,czdw,sfyx from sys_information where sfyx='1' and (sfgk='1' or (sfgk='0' and xh in (select xh from sys_informationreceive where yhdh='"
				+ condition.get("czr")
				+ "'))) and jzsj>sysdate"
				+ sql
				+ " order by fbsj desc) a,";
		String tmpSql = "";
		tmpSql = tmpSql + "select a.*,b.sfjs,b.jsnr bz from ";
		tmpSql = tmpSql + sql;
		tmpSql = tmpSql
				+ "(select xh,sfjs,jsnr from sys_informationreceive where yhdh='"
				+ condition.get("czr") + "') b ";
		tmpSql = tmpSql + " where a.xh=b.xh(+)";
		if (!condition.containsKey("zt"))
			tmpSql = tmpSql + " order by a.fbsj desc";
		else {
			tmpSql = tmpSql + " and b.sfjs='" + condition.get("zt")
					+ "' order by a.fbsj desc";
		}
		return this.queryForMap(tmpSql, Integer.parseInt(condition.get(
				"page").toString()), Integer.parseInt(condition.get("rows")
				.toString()), SysInformation.class, 2000);
	}
	public List<SysInformation> getZxggInfos(Map<String, Object> condition)
	throws Exception {
		String sql = "";
		if (condition.containsKey("xxlb")) {
			sql = " and xxlb='" + condition.get("xxlb") + "'" + sql;
		}
		if (condition.containsKey("czdw")) {
			sql = " and czdw='" + condition.get("czdw") + "'" + sql;
		}	
		if (condition.containsKey("bt")) {
			sql = " and bt like '%" + condition.get("bt") + "%'" + sql;
		}
		if (condition.containsKey("kssj") && condition.containsKey("jssj")) {
			sql = " and (fbsj>=to_date('" + condition.get("kssj")
			+ "','yyyy-mm-dd hh24:mi:ss') and fbsj<=to_date('"
			+ condition.get("jzsj") + "','yyyy-mm-dd hh24:mi:ss'))"
			+ sql;
		}
		if (!sql.equals(""))
			sql = " and (" + sql.substring(5, sql.length()) + ") ";
		sql = "(select xh,xxlb,bt,fbsj,jzsj,sfgk,czdw,sfyx from sys_information where sfyx='1' and (sfgk='1' or (sfgk='0' and xh in (select xh from sys_informationreceive where yhdh='"
			+ condition.get("czr")
			+ "'))) and jzsj>sysdate"
			+ sql
			+ " order by fbsj desc) a,";
		String tmpSql = "";
		tmpSql = tmpSql + "select a.*,b.sfjs,b.jsnr bz from ";
		tmpSql = tmpSql + sql;
		tmpSql = tmpSql
		+ "(select xh,sfjs,jsnr from sys_informationreceive where yhdh='"
		+ condition.get("czr") + "') b ";
		tmpSql = tmpSql + " where a.xh=b.xh(+)";
		if (!condition.containsKey("zt"))
			tmpSql = tmpSql + " order by a.fbsj desc";
		else {
			tmpSql = tmpSql + " and b.sfjs='" + condition.get("zt")
			+ "' order by a.fbsj desc";
		}
		tmpSql = "select * from ("+tmpSql+") e where rownum <=10";
		return this.queryForList(tmpSql,SysInformation.class);
		}
	public SysInformation getInformation(String xh, String username)
			throws Exception {
		SysInformation sysInformation = null;
		String sql = "select * from sys_information where xh='"
				+ xh
				+ "' and sfyx='1' and (sfgk='1' or (sfgk='0' and xh in (select xh from sys_informationreceive where yhdh='"
				+ username + "'))) and jzsj>sysdate-1";
		List<SysInformation> list = this.queryForList(sql, SysInformation.class);
		if(list.size() > 0){
			sysInformation = list.get(0);
		}
		return sysInformation;
		}
	public List getYhdh(String xh)throws Exception {
		String sql = "select yhdh from sys_informationreceive where xh="+ xh;
		return	this.queryForList(sql, SysInformationreceive.class);				
}
	
	
	
	
	public SysInformation getEditInformation(String xh)
	throws Exception {
		SysInformation sysInformation = null;
		String sql = "select * from sys_information where xh="
		+ xh;
		List<SysInformation> list = this.queryForList(sql, SysInformation.class);
		if(list.size() > 0){
			sysInformation = list.get(0);
		}
		return sysInformation;
		}
}