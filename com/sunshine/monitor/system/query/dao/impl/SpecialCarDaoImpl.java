package com.sunshine.monitor.system.query.dao.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.query.dao.SpecialCarDao;

@Repository
public class SpecialCarDaoImpl extends BaseDaoImpl implements SpecialCarDao {

	public Map querySpecialList(Map conditions) throws Exception {
		StringBuffer sql = new StringBuffer(20);
		//sql.append("select * from jm_specialcar where 1=1 and status='1' and isvalid='1' ");
		sql.append("select b.* from (select hphm,hpzl,hpys from veh_passrec where 1 = 1 ");
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter =  "page".equalsIgnoreCase(key)
					|| "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.equals("kdbh")) {
					sql.append(" and kdbh in (").append(value).append(") "); 
				} else if (key.equals("kssj")) {
					sql.append(" and gcsj >= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else if (key.equals("jssj")) {
					sql.append(" and gcsj <= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else {
					sql.append(" and ");
					sql.append(key);
					sql.append(" = '").append(value).append("'");
				}
			}
		}
		sql.append(" group by hphm,hpzl,hpys )a,jm_specialcar b where a.hphm = b.hphm and a.hpys = b.hpys and a.hpzl = b.hpzl and b.isvalid = '1' ");
		sql.append(" order by b.rksj desc ");
		System.out.println(sql);
		Map<String, Object> map = findPageForMap(sql.toString(), Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}

}
