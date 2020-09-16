package com.sunshine.monitor.system.manager.dao.jdbc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.UserLoginXls;
import com.sunshine.monitor.system.manager.dao.UserLoginDao;

@Repository("userLoginDao")
public class UserLoginDaoImpl extends BaseDaoImpl implements UserLoginDao {

	public Map<String, Object> queryList(Map<String, Object> conditions)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select yhdh,yhmc,jh,glbm,bmmc ,nvl(count(yhdh2),0) as cs from (select  a.*,l.yhdh as yhdh2 from "+  
				   " (select distinct f.yhdh, f.yhmc, f.jh, f.glbm ,d.bmmc from frm_sysuser f, jm_userrole j,frm_department d " +
				   " where f.yhdh = j.yhdh and f.glbm = d.glbm) a "+
				   " left join frm_log l on czlx in ('1001', '1003') and l.yhdh = a.yhdh) group by yhdh,yhmc,jh,glbm,bmmc ");
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
					sql.append(" having ");
					sql.append(key);
					sql.append(" = '").append(value).append("'");				
			}
		}
		sql.append(" order by ");
		sql.append(conditions.get("sort"));
		sql.append(" ");
		sql.append(conditions.get("order"));
		Map<String, Object> map = this.findPageForMap(sql.toString(), Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}

	public Map<String, Object> queryForDetail(Map<String, Object> conditions)
			throws Exception {
	
		StringBuffer sql = new StringBuffer();
		sql.append(" select yhdh,yhmc,czsj,decode(czlx,'1001','普通登录','1003','PKI登录') as czlx,ip from( select a.*,l.czsj,l.czlx,l.ip from "+ 
                  " (select distinct f.yhdh, f.yhmc from frm_sysuser f, jm_userrole j where f.yhdh = j.yhdh ) a left join frm_log l "+
				  " on czlx in ('1001', '1003') and l.yhdh = a.yhdh) where 1 = 1");
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
					sql.append(" and ");
					sql.append(key);
					sql.append(" = '").append(value).append("'");				
			}
		}
		sql.append(" order by ");
		sql.append(conditions.get("sort"));
		sql.append(" ");
		sql.append(conditions.get("order"));
		Map<String, Object> map = this.findPageForMap(sql.toString(), Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}

	public List<UserLoginXls> getUserLoginList(StringBuffer condition)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from (select yhdh,yhmc,jh,glbm,bmmc ,nvl(count(yhdh2),0) as cs from (select  a.*,l.yhdh as yhdh2 from "+  
				   " (select distinct f.yhdh, f.yhmc, f.jh, f.glbm ,d.bmmc from frm_sysuser f, jm_userrole j,frm_department d " +
				   " where f.yhdh = j.yhdh and f.glbm = d.glbm) a "+
				   " left join frm_log l on czlx in ('1001', '1003') and l.yhdh = a.yhdh) group by yhdh,yhmc,jh,glbm,bmmc ) ");
		sql.append("  where  ").append(condition.toString());
		return this.queryList(sql.toString(),UserLoginXls.class);
	}

	
	
	

}
