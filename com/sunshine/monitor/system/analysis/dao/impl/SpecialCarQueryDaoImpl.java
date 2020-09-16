package com.sunshine.monitor.system.analysis.dao.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.SpecialCarQueryGz;
import com.sunshine.monitor.system.analysis.dao.SpecialCarQueryDao;


@Repository("SpecialCarQueryDao")
public class SpecialCarQueryDaoImpl extends BaseDaoImpl implements SpecialCarQueryDao {
	
	public String saveBdgz(SpecialCarQueryGz information)throws Exception{
			String seSql = "SELECT SEQ_SPECIALCARRULE_XH.nextval from dual";
			String xh = this.jdbcTemplate.queryForObject(seSql, String.class);

			String isExit = "SELECT count(*) from T_YP_SPECIALCARRULE where gzbh = '"
					+ xh + "'";
			int is = this.jdbcTemplate.queryForInt(isExit);
			if (is == 0) {
				String insertSql = "INSERT into T_YP_SPECIALCARRULE(GZBH, BDLX, HPHM, GZKSSJ, GZJSSJ, CITY, KDBHSTR, KDMCSTR, ZT)values"
						+ "('"
						+ xh
						+ "', '"
						+ information.getBdlx()
						+ "', '"
						+ ((information.getHphm() == null) ? "" : information.getHphm())
						+ "',"
						+ ((information.getGzkssj() == null) ? "" :"to_date('" +information.getGzkssj()+"','yyyy-mm-dd hh24:mi:ss')")
						+ ","
						+ ((information.getGzjssj() == null) ? "" :"to_date('" +information.getGzjssj()+"','yyyy-mm-dd hh24:mi:ss')")
						+ ", '"
						+ ((information.getCity() == null) ? "" : information.getCity())
						+ "','"
						+ ((information.getKdbhstr() == null) ? "" : information.getKdbhstr())
						+ "','"
						+ ((information.getKdmcstr() == null) ? "" : information.getKdmcstr())
						+ "','"
						+ information.getZt()
						+ "')";
				int result = this.jdbcTemplate.update(insertSql);
				if (result == 1) {
					return xh;
				}
			}
			return null;
		}
	
	public Map<String, Object> querySpecialCarRuleList(Map<String, Object> conditions) throws Exception {		
		StringBuffer sql = new StringBuffer(50);
		sql.append("select * from T_YP_SPECIALCARRULE where 1=1 ");
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
				
			}
		}
		/*sql.append(" order by ");
		sql.append(conditions.get("sort"));
		sql.append(" ");
		sql.append(conditions.get("order"));*/
		Map<String, Object> map = this.findPageForMap(sql.toString(), Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}
	
	public SpecialCarQueryGz getSpecialCarRule(String gzbh)throws Exception{
		SpecialCarQueryGz car = null;
		String sql = " select * from T_YP_SPECIALCARRULE where gzbh = '"+gzbh+"'";
		List list = this.jdbcTemplate.queryForList(sql);
		if(list.size()>0){
			car = (SpecialCarQueryGz)list.get(0);
		}
		return car;		
	}
	
}
