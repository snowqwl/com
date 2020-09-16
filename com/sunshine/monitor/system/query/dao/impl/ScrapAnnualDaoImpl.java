package com.sunshine.monitor.system.query.dao.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.query.bean.ScrapAnnual;
import com.sunshine.monitor.system.query.dao.ScrapAnnualDao;

@Repository
public class ScrapAnnualDaoImpl extends BaseDaoImpl implements ScrapAnnualDao {

	public Map getMapForScrapAnnual(Map map, ScrapAnnual info)throws Exception {
String table="";
table = "jm_annual";
List param = new ArrayList<>();
StringBuffer sql = new StringBuffer(
		"select v.* from ? v where 1=1 ");
param.add(table);

if (StringUtils.isNotBlank(info.getHphm())) {
	sql.append(" and hphm like ? ");
	param.add("%"+URLDecoder.decode( info.getHphm(),"UTF-8")+"%");
}

if (StringUtils.isNotBlank(info.getHpzl())) {
	sql.append(" and hpzl = ? ");
	param.add(info.getHpzl());
}
if (StringUtils.isNotBlank(info.getCllx())) {
	sql.append(" and cllx = ? ");
	param.add(info.getCllx());
};		

Map<String, Object> map1 = this.getSelf().findPageForMap(sql.toString(), param.toArray(),Integer
		.parseInt(map.get("page").toString()), Integer
		.parseInt(map.get("rows").toString()));
return map1;
}
}
