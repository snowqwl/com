package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.bean.CaseTouchAnalysis;
import com.sunshine.monitor.system.analysis.dao.CaseTouchDao;
import com.sunshine.monitor.system.notice.bean.SysInformation;

@Repository("CaseTouchDao")
public class CaseTouchDaoImpl extends BaseDaoImpl implements CaseTouchDao {
	
	
	
	public String saveCaseTouchGz(CaseTouchAnalysis information)throws Exception{
		String seSql = "SELECT SEQ_CASETOUCHRULE.nextval from dual";
		String xh = this.jdbcTemplate.queryForObject(seSql, String.class);

		String isExit = "SELECT count(*) from T_YP_CASETOUCHRULE where gzbh = '"
				+ xh + "'";
		int is = this.jdbcTemplate.queryForInt(isExit);
		if (is == 0) {
			String insertSql = "INSERT into T_YP_CASETOUCHRULE(GZBH, GZMC, DPGZ, DPGZMC, PL, GXSJ)values"
					+ "('"
					+ xh
					+ "', '"
					+ information.getGzmc()
					+ "', '"
					+ information.getDpgz()
					+ "','"
					+ information.getDpgzmc()
					+ "', '"
					+ information.getPl()
					+ "',"
					+ "sysdate"
					+ ")";
			int result = this.jdbcTemplate.update(insertSql);
			if (result == 1) {
				return xh;
			}
		}
		return null;
	}
	
	public Map<String,Object> queryCaseTouchRuleList(Map<String,Object> conditions)throws Exception{
		StringBuffer sql = new StringBuffer(50);
		sql.append("select * from T_YP_CASETOUCHRULE where 1=1 ");
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
		sql.append(" order by ");
		sql.append(conditions.get("sort"));
		sql.append(" ");
		sql.append(conditions.get("order"));
		Map<String, Object> map = this.findPageForMap(sql.toString(), Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}
	
	public CaseTouchAnalysis getEditInformation(String gzbh)throws Exception{
		CaseTouchAnalysis casetouch = null;
		String sql = " select dpgz,pl from T_YP_CASETOUCHRULE where gzbh = '"+gzbh+"'";
		List<CaseTouchAnalysis> list = this.queryForList(sql, CaseTouchAnalysis.class);
		if(list.size()>0){
			casetouch = list.get(0);
		}
		return casetouch;
	}
	
	public int updateCaseTouchGz(CaseTouchAnalysis information)throws Exception{
		String isExit = "SELECT count(*) from T_YP_CASETOUCHRULE where gzbh = '"
						+ information.getGzbh()+ "'";
	int is = this.jdbcTemplate.queryForInt(isExit);
	if (is > 0) {
		String upSql = "UPDATE T_YP_CASETOUCHRULE set GZMC = '"
				+ information.getGzmc()
				+ "', DPGZ =  '"
				+ information.getDpgz()
				+ "', DPGZMC   = '"
				+ information.getDpgzmc()
				+ "', PL = '"
				+ information.getPl()
				+ "', GXSJ = sysdate "
				+ " where GZBH = '" + information.getGzbh() + "'";

		return this.jdbcTemplate.update(upSql);
	}
	return 0; 
	}
	
	public List getCaseTouchRule() throws Exception{
		String sql = " select gzbh,gzmc,dpgz,pl from T_YP_CASETOUCHRULE ";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	public Map<String,Object> getCaseForListNew(Map<String,Object> conditions)throws Exception{
 		StringBuffer sql = new StringBuffer(50);
		sql.append("SELECT * FROM (");
		sql.append(" SELECT a1.anjianbianhao AS ajbh,a1.anjianmingcheng AS ajmc,a1.anjianlaiyuan AS ajly," +
				" a1.anjianleibie AS ajlx,a2.faanshijiankaishi AS fasjcz,a2.faanshijianjieshu AS fasjzz," +
				" a2.jianyaoanqing AS zyaq,a2.faxiandidiandm AS fadd_qx	FROM jm_zyk_case a1,jm_zyk_jcjxx a2");
		sql.append(" WHERE a1.anjianbianhao = a2.anjianbianhao");
		
//		sql.append(" AND a2.faanshijiankaishi >= '20160801000000'");
//		sql.append(" AND a2.faanshijianjieshu <= '20161019235959'");
		//开始时间
		if (conditions.get("afkssj") != null && !"".equals(conditions.get("afkssj"))) {
			String value = (String) conditions.get("afkssj");
			value = value.replace("-", "").replace(":", "").replace(" ","");
			sql.append(" and a2.faanshijiankaishi >= ").append("'" + value + "' ");
		}
		//结束时间
		if (conditions.get("afjssj") != null && !"".equals(conditions.get("afjssj"))) {
			String value = (String) conditions.get("afjssj");
			value = value.replace("-", "").replace(":", "").replace(" ","");
			sql.append(" and a2.faanshijianjieshu <= ").append(" '" + value + "'");
		}
		//案件来源
		if (conditions.get("ajly") != null && !"".equals(conditions.get("ajly"))) {
			sql.append(" and a1.anjianlaiyuan = '").append(conditions.get("ajly")).append("' ");
		}
		//案件类型
		if (conditions.get("ajlx") != null && !"".equals(conditions.get("ajlx"))) {
			sql.append(" and a1.anjianleibie='").append(conditions.get("ajlx")).append("'");
		}	
		//案件名称
		if (conditions.get("ajmc") != null && !"".equals(conditions.get("ajmc"))) {
			sql.append(" and a1.anjianmingcheng like '%").append(conditions.get("ajmc")).append("%'");
		}
		//案件区域
		if (conditions.get("slfaqh") != null && !"".equals(conditions.get("slfaqh"))) {
			sql.append(" and a2.faxiandidiandm = '").append(conditions.get("slfaqh")).append("'");
		}
		sql.append(" ORDER BY fasjcz DESC");
		sql.append(" ) b");

		Map<String, Object> map = this.findPageForMap(sql.toString(), Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}
	
	
	public Map<String,Object> getCaseForList(Map<String,Object> conditions)throws Exception{
		StringBuffer sql = new StringBuffer(50);
		sql.append("select ajbh,ajmc,fasjcz,fasjzz,fadd_qx,zyaq,ajly from V_DSJCQ_AJ where 1=1 ");
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
				if (key.equals("afkssj")) {
					sql.append(" and FASJCZ >= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else if (key.equals("afjssj")) {
					sql.append(" and FASJZZ <= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				}else if (key.equals("slfaqh")) {
					sql.append(" and FADD_QX = '").append(value).append("' ");
							
				}else {
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
		Map<String, Object> map = this.findPageForMap(sql.toString(), Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}
	
	public String getXzqhName(String xzqhdm)throws Exception{
		String xzqhmc = "";
		String sql = " select xzqhmc from frm_xzqh where xzqhdm = '"+xzqhdm+"'";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql);
		if(list.size()>0){
			xzqhmc = list.get(0).get("xzqhmc").toString();
		}else{
			xzqhmc = xzqhdm;
		}
		return xzqhmc;
	}
}
