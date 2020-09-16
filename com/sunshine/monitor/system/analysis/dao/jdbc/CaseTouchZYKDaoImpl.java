package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ZykBaseDaoImpl;
import com.sunshine.monitor.system.analysis.dao.CaseTouchZYKDao;

@Repository("caseTouchZYKDao")
public class CaseTouchZYKDaoImpl extends ZykBaseDaoImpl implements CaseTouchZYKDao {

	protected Logger debugLogger =  LoggerFactory.getLogger(CaseTouchZYKDaoImpl.class);
	
	public List getCaseDm(String ywxtmc,String dmlb,String dmmc) throws Exception{
		String sql = "select dm, dmmc from qbzhpt_dm.t_dm where 1=1   ";
		if(dmlb!=""){
			sql+=" and dmlb like '%"+dmlb+"%' ";
		}
		if(ywxtmc!=""){
			sql+=" and ywxtmc like '%"+ywxtmc+"%' ";
		}
		if(dmmc!=""){
			sql+=" and dmmc like '%"+dmmc+"%' ";
		}
		return this.jdbcZykTemplate.queryForList(sql);
	}
	
	public Map<String,Object> getCaseForList(Map<String,Object> conditions)throws Exception{
		StringBuffer sql = new StringBuffer(50);
		sql.append(" select t1.ajbh,ajmc,ajly,ajlx,fasjcz,fasjzz,zyaq,fadd_qx from ")
		   .append("(select anjianmingcheng as ajmc,anjianlaiyuan as ajly,anjianleibie as ajlx,anjianbianhao as ajbh from QBZHPT.T_ASJ_JBXX_DXF where anjianbianhao is not null)t1, ")
		   .append("(select anjianbianhao as ajbh,faanshijiankaishi  as fasjcz,faanshijianjieshu as fasjzz,jianyaoanqing as zyaq," )
		   .append(" substr(jiejingdanweiid, 0, 7) as fadd_qx from QBZHPT.T_ASJ_JCJXX_DXF where 1=1 ");
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		String ajlxstr = "";
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter = "sort".equalsIgnoreCase(key)
					|| "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key)
					|| "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.equals("afkssj")) {
					value = value.replace("-", "").replace(":", "").replace(" ","");
					sql.append(" and faanshijiankaishi >= ").append(
							"'" + value + "' ");
				} else if (key.equals("afjssj")) {
					value = value.replace("-", "").replace(":", "").replace(" ","");
					sql.append(" and faanshijianjieshu <= ").append(
							" '" + value + "'");
				}else if (key.equals("slfaqh")) {
					sql.append(" and jiejingdanweiid like '").append(value).append("%' ");
							
				}else if (key.equals("ajly")) {
					sql.append(" and anjianlaiyuan = '").append(value).append("' ");
				}
				else if (key.equals("ajlx")) {
					ajlxstr = value;
				}
				else {
					sql.append(" and ");
					sql.append(key);
					sql.append(" = '").append(value).append("'");
				}
			}
		}
		sql.append(" and faanshijiankaishi is not null and faanshijianjieshu is not null and anjianbianhao is not null)t2 where t1.ajbh=t2.ajbh  ");
		if(ajlxstr!=""){
			sql.append(" and ajlx='").append(ajlxstr).append("'");
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
	
}
