package com.sunshine.monitor.system.query.dao.impl;

import java.util.*;
import java.util.Map.Entry;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.query.dao.SpecialCarDao;

@Repository
public class SpecialCarDaoImpl extends BaseDaoImpl implements SpecialCarDao {

	public Map querySpecialList(Map conditions) throws Exception {
		List param = new ArrayList<>();
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
					sql.append(" and kdbh in (?) ");
					param.add(value);
				} else if (key.equals("kssj")) {
					sql.append(" and gcsj >= ").append(
							"to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					param.add(value);
				} else if (key.equals("jssj")) {
					sql.append(" and gcsj <= ").append(
							"to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					param.add(value);
				} else {
					sql.append(" and ?=?");
					param.add(key);
					param.add(value);
				}
			}
		}
		sql.append(" group by hphm,hpzl,hpys )a,jm_specialcar b where a.hphm = b.hphm and a.hpys = b.hpys and a.hpzl = b.hpzl and b.isvalid = '1' ");
		sql.append(" order by b.rksj desc ");
		System.out.println(sql);
		Map<String, Object> map = findPageForMap(sql.toString(),param.toArray(),Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}

}
