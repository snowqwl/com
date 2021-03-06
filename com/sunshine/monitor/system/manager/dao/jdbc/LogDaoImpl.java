package com.sunshine.monitor.system.manager.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.BusinessLog;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.dao.LogDao;

@Repository("logDao")
public class LogDaoImpl extends BaseDaoImpl implements LogDao {

	private Logger logger = LoggerFactory.getLogger(LogDaoImpl.class);

	public Map<String, Object> findLogForMap(Map filter) {
		String sql = "";
		List param = new ArrayList<>();
		sql = "select a.czsj,a.czlx,a.cznr,a.ip,b.bmmc,c.yhmc from frm_log  a ,frm_department b,frm_sysuser c Where a.glbm=b.glbm And a.yhdh=c.yhdh "
				+ sql + " Order By a.czsj Desc";
		sql = "select to_char(x.czsj,'yyyy-mm-dd hh24:mi') czsj,x.cznr,x.ip,x.bmmc,x.yhmc,y.czlx from ("
				// sql = "select x.cznr,x.ip,x.bmmc,x.yhmc,y.czlx from ("
				+ sql
				+ ") x, (select dmz,dmsm1 czlx from frm_code where dmlb = '000012') y where x.czlx=y.dmz order by x.czsj desc";

		if (!(filter.get("rolename") == null || filter.get("rolename").equals(
				""))){
			sql += " and t.F_ROLE like ?";
			param.add("%"+filter.get("rolename"+"%"));
		}

		if (!(filter.get("type") == null || filter.get("type").equals(""))){
			sql += " and t.F_type =?";
			param.add(filter.get("type").toString());
		}


		Map<String, Object> map = this.findPageForMap(sql, param.toArray(),Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));

		return map;
	}

	public Map<String, Object> findLogForMap(Map filter, Log log) {
		String sql = "";
		List param = new ArrayList<>();
		if ((log.getGlbm() != null) && (!log.getGlbm().equals(""))) {
			sql = " and a.glbm  like ?";
			param.add(log.getGlbm() + "%");
		}
		if ((log.getBmmc() != null) && (!log.getBmmc().equals(""))) {
			sql = " and b.bmmc=?";
			param.add(log.getBmmc());
		}
		if ((log.getYhdh() != null) && (!log.getYhdh().equals(""))) {
			sql = sql + " and a.yhdh =?";
			param.add(log.getYhdh());
		}
		if ((log.getYhmc() != null) && (!log.getYhmc().equals(""))) {
			sql = sql + " and c.yhmc like ?";
			param.add("%" + log.getYhmc() + "%");
		}
//		if ((log.getCzlx() != null) && (!log.getCzlx().equals(""))) {
//			if("9094".equals(log.getCzlx())){
//				sql = sql + " and a.czlx in ('1001','1003')";
//			}else if("9095".equals(log.getCzlx())){
//				sql = sql + " and a.czlx  in ('1002','1004')";
//			}else{
//				sql = sql + " and a.czlx ='" + log.getCzlx() + "'";
//			}
//		}
		if ((log.getCzlx() != null) && (!log.getCzlx().equals(""))) {
			String logid= queryByParent(log.getCzlx());
			if(logid.equals("") || logid==null){
				logid = "'"+log.getCzlx()+"'";
			}
			sql = sql + " and a.czlx in (?) ";
			param.add(logid);
		}
		if ((log.getKssj() != null) && !log.getKssj().equals("")) {
			sql = sql + " and a.czsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(log.getKssj());
		}
		if ((log.getJssj() != null) && !log.getJssj().equals("")) {
			sql = sql + " and a.czsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(log.getJssj());
		}
		
		if(filter.get("hphm")!=null&&!filter.get("hphm").equals("")){
			sql = sql + " and a.cznr like ?";
			param.add("%"+filter.get("hphm")+"%");
		}
		
		sql = "select a.czsj,a.czlx,a.cznr,a.ip,b.bmmc,c.yhmc from frm_log  a ,frm_department b,frm_sysuser c Where a.glbm=b.glbm And a.yhdh=c.yhdh "
				+ sql + " Order By a.czsj Desc";
		sql = "select to_char(x.czsj,'yyyy-mm-dd hh24:mi') czsj,x.cznr,x.ip,x.bmmc,x.yhmc,y.czlx from ("
				+ sql
				+ ") x, (select dmz,dmsm1 czlx from frm_code where dmlb = '000012') y where x.czlx=y.dmz order by x.czsj desc";
		System.out.println(sql);
		Map<String, Object> map = this.findPageForMap(sql, param.toArray(),Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()),Log.class);

		return map;
	}

	/**
	 * 按用户统计
	 * 
	 * @param log
	 * @return
	 */
	public Map<String, Object> findLogCountForMapByYH(Map filter, Log log)
			throws Exception {
		String sql = "";
		List param = new ArrayList<>();
		if ((log.getGlbm() != null) && (!log.getGlbm().equals(""))) {
			if(log.getGlbm().length()==12) {
				sql = " and glbm = ?";
				param.add(log.getGlbm());
			} else {
				sql = " and glbm like ?";
				param.add(log.getGlbm() + "%");
			}
		}
		if ((log.getYhdh() != null) && (!log.getYhdh().equals(""))) {
			sql = sql + " and yhdh =?";
			param.add(log.getYhdh());
		}
		if ((log.getCzlx() != null) && (!log.getCzlx().equals(""))) {
			sql = sql + " and czlx =?";
			param.add(log.getCzlx());
		}
		if ((log.getKssj() != null) && !log.getKssj().equals("")) {
			sql = sql + " and czsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(log.getKssj());
		}
		if ((log.getJssj() != null) && !log.getJssj().equals("")) {
			sql = sql + " and czsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(log.getJssj());
		}
		String subsql = "";
		if ((log.getYhmc() != null) && (!log.getYhmc().equals(""))) {
			subsql = " and c.yhmc like ?";
			param.add("%" + log.getYhmc() + "%");
		}
		sql = "select e.jdmc,d.xzqhmc, z.bmmc, z.glbm, z.yhmc, z.yhdh, z.czlx, z.czlxmc,z.czcs from( select x.bmmc,x.glbm,x.yhmc, x.yhdh, x.czlx,y.czlxmc,x.czcs from (select b.bmmc,c.yhmc,a.glbm, a.yhdh, a.czlx,a.czcs from (select glbm, yhdh, czlx, count(1) czcs from frm_log where 1 = 1 "
				+ sql
				+ " group by glbm, yhdh, czlx) a,frm_department b,frm_sysuser c where a.glbm = b.glbm and a.yhdh = c.yhdh "+subsql+" ) x,(select dmz, dmsm1 czlxmc from frm_code where dmlb = '000012') y where x.czlx = y.dmz) z,frm_xzqh d,code_url e where d.xzqhdm = SUBSTR(z.glbm,1,6) and SUBSTR(d.xzqhdm, 1, 4) = SUBSTR(e.dwdm, 1, 4) order by d.xzqhdm, z.czcs desc";

		//System.out.println(sql);
		if(filter == null) {
			List queryList = this.jdbcTemplate.queryForList(sql,param.toArray());
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("rows", queryList);
			return result;
		}
		
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));

		return map;
	}
	
	/**
	 * 日志行为分析
	 * 按部门统计
	 * 
	 * @param log
	 * @return
	 */
	public Map<String, Object> findLogCountForMapByBM(Map filter, Log log)
			throws Exception {
		String sql = "";
		List param = new ArrayList<>();
		if ((log.getGlbm() != null) && (!log.getGlbm().equals(""))) {
			if(log.getGlbm().length()==12) {
				sql = " and glbm = ?";
				param.add(log.getGlbm());
			} else {
				sql = " and glbm like ?";
				param.add(log.getGlbm()+"%");
			}
		}
		if ((log.getCzlx() != null) && (!log.getCzlx().equals(""))) {
			sql = sql + " and czlx =?";
			param.add(log.getCzlx());
		}
		if ((log.getKssj() != null) && !log.getKssj().equals("")) {
			sql = sql + " and czsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(log.getKssj());
		}
		if ((log.getJssj() != null) && !log.getJssj().equals("")) {
			sql = sql + " and czsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(log.getJssj());
		}
		sql = " select x.jdmc, x.xzqhmc, x.xzqhdm, x.glbm, x.bmmc, x.czlx, x.czcs, y.czlxmc from (select c.xzqhmc, c.xzqhdm, a.glbm, b.bmmc, a.czlx, a.czcs,d.jdmc from (select glbm, czlx, count(1) czcs from frm_log where 1 = 1 "
				+ sql
				+ "group by glbm, czlx) a,frm_department b,frm_xzqh c,code_url d where a.glbm = b.glbm and c.xzqhdm = SUBSTR(a.glbm, 1, 6)  and SUBSTR(c.xzqhdm, 1, 4) = SUBSTR(d.dwdm, 1, 4)) x, (select dmz, dmsm1 czlxmc from frm_code where dmlb = '000012') y where x.czlx = y.dmz order by x.xzqhdm, x.czcs desc";
		Map<String, Object> map = this.findPageForMap(sql,param.toArray(), Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));

		return map;
	}
	
	/**
	 * 日志行为分析
	 * 按区划统计
	 * @param log
	 * @return
	 */
	public Map<String,Object> findLogCountForMapByQH(Map filter, Log log) throws Exception {
		String sql = "";
		List param = new ArrayList<>();
		if ((log.getGlbm() != null) && (!log.getGlbm().equals(""))) {
			sql = " and glbm like ?";
			param.add(log.getGlbm()+"%");
		}
		if ((log.getCzlx() != null) && (!log.getCzlx().equals(""))) {
			sql = sql + " and czlx =?";
			param.add(log.getCzlx());
		}
		if ((log.getKssj() != null) && !log.getKssj().equals("")) {
			sql = sql + " and czsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(log.getKssj());
		}
		if ((log.getJssj() != null) && !log.getJssj().equals("")) {
			sql = sql + " and czsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(log.getJssj());
		}
		sql = " select x.jdmc,x.xzqhmc, x.xzqhdm, x.czlx, x.czcs, y.czlxmc from (select c.jdmc,b.xzqhmc, b.xzqhdm,a.czlx, a.czcs from (select SUBSTR(glbm, 1, 6) xzqhdm, czlx, count(1) czcs from frm_log where 1 = 1"
				+ sql
				+ "group by SUBSTR(glbm, 1, 6), czlx) a,frm_xzqh b,code_url c where a.xzqhdm = b.xzqhdm and SUBSTR(b.xzqhdm, 1, 4) = SUBSTR(c.dwdm, 1, 4)) x,(select dmz, dmsm1 czlxmc from frm_code where dmlb = '000012') y where x.czlx = y.dmz order by x.xzqhdm, x.czcs desc";

		Map<String, Object> map = this.findPageForMap(sql, param.toArray(),Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));

		return map;
	}

	public List<Log> findLogList(Log log) {
		List param = new ArrayList<>();
		String tmpSql = "";
		if ((log.getGlbm() != null) && (!log.getGlbm().equals(""))) {
			tmpSql = " and a.glbm=?";
			param.add(log.getGlbm());
		}
		if ((log.getYhdh() != null) && (!log.getYhdh().equals(""))) {
			tmpSql = tmpSql + " and a.yhdh =?";
			param.add(log.getYhdh());
		}
		if ((log.getCzlx() != null) && (!log.getCzlx().equals(""))) {
			tmpSql = tmpSql + " and a.czlx =?";
			param.add(log.getCzlx());
		}
		if (!log.getKssj().equals("")) {
			tmpSql = tmpSql + " and a.czsj >= to_date(?,'yyyy-mm-dd hh24:mi')";
			param.add(log.getKssj()+ " 00:00");
		}
		if (!log.getJssj().equals("")) {
			tmpSql = tmpSql + " and a.czsj <= to_date(?,'yyyy-mm-dd hh24:mi')";
			param.add(log.getJssj() + " 23:59");
		}
		tmpSql = "select a.czsj,a.czlx,a.cznr,a.ip,b.bmmc,c.yhmc from frm_log  a ,frm_department b,frm_sysuser c Where a.glbm=b.glbm And a.yhdh=c.yhdh "
				+ tmpSql + " Order By a.czsj Desc";
		tmpSql = "select x.czsj,x.cznr,x.ip,x.bmmc,x.yhmc,y.czlx from ("
				+ tmpSql
				+ ") x, (select dmz,dmsm1 czlx from frm_code where dmlb = '000012') y where x.czlx=y.dmz order by x.czsj desc";

		return this.queryForList(tmpSql, param.toArray(),Log.class);
	}

	public int saveLogs(String paramString1, String paramString2,
			String paramString3, String paramString4, String paramString5) {

		return 0;
	}

	public int saveBusinessLog(BusinessLog log) throws Exception {
		String sql = "";
		List param = new ArrayList<>();
		if (log.getBzsj() == null) {
			sql = "INSERT into BUSINESS_LOG(xh, ywxh, ywlb, ywjb, czrdh, czrjh, czrdwdm, czrdwjz)" +
					"values(seq_business_log_xh.nextval,?,?,?,?,?,?,?)";
			param.add(log.getYwxh());
			param.add(log.getYwlb());
			param.add(log.getYwjb());
			param.add(log.getCzrdh());
			param.add(log.getCzrjh());
			param.add(log.getCzrdwdm());
			param.add(log.getCzrdwjz());
		} else {
			sql = "INSERT into BUSINESS_LOG(xh, ywxh, ywlb, ywjb, BZSJ, czrdh, czrjh, czrdwdm, " +
					"czrdwjz)values(seq_business_log_xh.nextval,?,?,?,to_date(?,'yyyy-mm-dd " +
					"hh24:mi:ss),?,?,?,?)";
			param.add(log.getYwxh());
			param.add(log.getBzsj());
			param.add(log.getYwlb());
			param.add(log.getYwjb());
			param.add(log.getCzrdh());
			param.add(log.getCzrjh());
			param.add(log.getCzrdwdm());
			param.add(log.getCzrdwjz());
		}
		return this.jdbcTemplate.update(sql,param.toArray());
	}

	public int saveLog(Log log) throws Exception {
		List param = new ArrayList<>();
		String sql = "insert into frm_log(GLBM, YHDH, CZSJ, CZLX, CZNR, IP,JH,BSQR,BSQRJH,SQJS," +
				"SFSQ,SQR,SFGCCX,HPHM,CXJG,KSSJ,JSSJ) VALUES (?,?,sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		param.add(log.getGlbm());
		param.add(log.getYhdh());
		param.add(log.getCzlx());
		param.add(log.getCznr().replace("'", "''"));
		param.add(log.getIp());
		if(log.getJh()==null){
			param.add("''");
		}else{
			param.add(log.getJh());
		}
		if(log.getBsqr()==null){
			param.add("''");
		}else{
			param.add(log.getBsqr());
		}
		if(log.getSfgccx()==null){
			param.add("''");
		}else{
			param.add(log.getSfgccx());
		}
		if(log.getSqjs()==null){
			param.add("''");
		}else{
			param.add(log.getSqjs());
		}
		if(log.getSfsq()==null){
			param.add("''");
		}else{
			param.add(log.getSfsq());
		}
		if(log.getSqr()==null){
			param.add("''");
		}else{
			param.add(log.getSqr());
		}
		if(log.getHphm()==null){
			param.add("''");
		}else{
			param.add(log.getHphm());
		}
		if(log.getCxjg()==null){
			param.add("''");
		}else{
			param.add(log.getCxjg());
		}
		if(log.getBsqr()==null){
			param.add("''");
		}else{
			param.add(log.getBsqr());
		}
		if(log.getKssj()==null){
			param.add("''");
		}else{
			param.add(log.getKssj());
		}
		if(log.getJssj()==null){
			param.add("''");
		}else{
			param.add(log.getJssj());
		}

		return this.jdbcTemplate.update(sql);
	}
	
	public Map getPoliceRatio(Map condition) throws Exception {
		List param = new ArrayList<>();
		String sql = "select ? as ls,? as le,? as ts,? as te,"
			+"a.luser,b.tuser,c.lquery,d.tquery,e.lsusp,f.tsusp from "
			+ " (select count(distinct yhdh) as luser from frm_log "
			+" where czlx in (1001,1003,1005) "
			+" and czsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss') "
			+" and czsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss') "
			+" )a,("
			+" select count(distinct yhdh) as tuser from frm_log "
			+" where czlx in (1001,1003,1005) "
			+" and czsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss') "
			+" and czsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss') "
			+" )b,("
			+" select count(1) as lquery from frm_log "
			+" where czlx in (select dmz from frm_code where dmlb = '000012' and dmsm1 like '%查询') "
			+" and czsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss') "
			+" and czsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss') "
			+" )c,("
			+" select count(1) as tquery from frm_log "
			+" where czlx in (select dmz from frm_code where dmlb = '000012' and dmsm1 like '%查询') "
			+" and czsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss') "
			+" and czsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss') "
			+" )d,("
			+" select count(1) as lsusp from frm_log "
			+" where czlx in (2011,2021,2031) "
			+" and czsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss') "
			+" and czsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss') "
			+" )e,("
			+" select count(1) as tsusp from frm_log "
			+" where czlx in (2011,2021,2031) "
			+" and czsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss') "
			+" and czsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss') "
			+" )f"
			;
		param.add(condition.get("lastMonth_start"));
		param.add(condition.get("lastMonth_end"));
		param.add(condition.get("thisMonth_start"));
		param.add(condition.get("thisMonth_end"));
		param.add(condition.get("lastMonth_start"));
		param.add(condition.get("lastMonth_end"));
		param.add(condition.get("thisMonth_start"));
		param.add(condition.get("thisMonth_end"));
		param.add(condition.get("lastMonth_start"));
		param.add(condition.get("lastMonth_end"));
		param.add(condition.get("thisMonth_start"));
		param.add(condition.get("thisMonth_end"));
		param.add(condition.get("lastMonth_start"));
		param.add(condition.get("lastMonth_end"));
		param.add(condition.get("thisMonth_start"));
		param.add(condition.get("thisMonth_end"));


		Map map = this.jdbcTemplate.queryForMap(sql);
		return map;
	}

	@Override
	public List<Map<String,Object>> queryLogCategoryTree(String filter)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select c.dmz as id,c.dmsm1 as name,l.pid from frm_log_category l");
		sql.append(" left join  frm_code c on c.dmz=l.logid");
		sql.append(" where c.dmlb='000012' order by name");
		List<Map<String,Object>> map = this.jdbcTemplate.queryForList(sql.toString());	
		return map;
	}
	
	@Override
	public boolean countLogCategoryParent(String filter)	throws Exception {
		boolean b = false;
		String sql=" select logid from frm_log_category  where pid=?";
		int count = this.getRecordCounts(sql, new Object[]{filter},1000);
		if(count>0){
			b=true;
		}
		return b;
	}
	
	public String queryByParent(String pid) {
		String sql=" select logid from frm_log_category  where pid=?";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql,new Object[]{pid});
		String result = "";
		if(list.size()>0){
			for(Map<String,Object> map : list){
				result += "'"+ map.get("LOGID")+"',";
			}
			result=result.substring(0,result.length()-1);
		}	
		return result;
	}
	
	public int saveSynchLog(Map<String,Object> map) throws Exception {
		List param = new ArrayList<>();
		String sql = "insert into synchronization_log(rwbh, tbsj, ywbh, czjg, tbnr) VALUES (?,"
				+ "sysdate,?,?,?)";
		param.add(map.get("rwbh"));
		param.add(map.get("ywbh"));
		param.add(map.get("czjg"));
		param.add(map.get("tbnr"));
				
		return this.jdbcTemplate.update(sql,param.toArray());
	}

	@Override
	public Map<String, Object> getLogForMap(Map<String, String> params, Log log,String type) {
		List param = new ArrayList<>();
		String sql="SELECT GLBM,JH,SQR,CZSJ,BSQRJH,BSQR,SQJS,IP,KSSJ,JSSJ,HPHM,CXJG FROM FRM_LOG WHERE 1=1 ";
		if(log.getGlbm() !=null && !"".equals(log.getGlbm())) {
			sql=sql+" AND GLBM=? ";
			param.add(log.getGlbm());
		}
		if(log.getSqr() !=null && !"".equals(log.getSqr())) {
			sql=sql+" AND SQR=? ";
			param.add(log.getSqr());
		}
		if(log.getJh() !=null && !"".equals(log.getJh())) {
			sql=sql+" AND JH=? ";
			param.add(log.getJh());
		}
		if(log.getBsqr() !=null && !"".equals(log.getBsqr())) {
			sql=sql+" AND BSQR=? ";
			param.add(log.getBsqr());
		}
		if(log.getBsqrjh() !=null && !"".equals(log.getBsqrjh())) {
			sql=sql+" AND BSQRJH=? ";
			param.add(log.getBsqr());
		}
		if(log.getSqjs() !=null && !"".equals(log.getSqjs())) {
			sql=sql+" AND SQJS = ? ";
			param.add(log.getSqjs());
		}
		//2018-9-30修改，查询条件开始时间、结束时间改为操作时间
		if(log.getKssj()!=null && !"".equals(log.getKssj())) {
			sql+=" AND czsj>to_date(?,'yyyy-mm-dd hh24:mi:ss') ";
			param.add(log.getKssj());
		}
		if(log.getJssj()!=null && !"".equals(log.getJssj())) {
			sql+=" AND czsj<to_date(?,'yyyy-mm-dd hh24:mi:ss') ";
			param.add(log.getJssj());
		}
		if(log.getHphm()!=null && !"".equals(log.getHphm())) {
			sql+=" AND HPHM=? ";
			param.add(log.getHphm());
		}
		if("yhsq".equals(type)) {
			sql+=" AND SFSQ='1' ";
		}else if("clcx".equals(type)) {
			sql+=" AND SFGCCX='1' ";
		}
		sql+=" ORDER BY CZSJ DESC ";
		
		Map<String, Object> map = this.findPageForMap(sql,
				param.toArray(),Integer.parseInt(params.get("curPage")), Integer.parseInt(params.get(
						"pageSize")), Log.class);
		//获取管理部门名称
		List<Log> list= (List<Log>) map.get("rows");
		if(list.size()>0) {
			for (Log l : list) {
				String glbm = l.getGlbm();
				String sql2="SELECT BMMC FROM FRM_DEPARTMENT WHERE GLBM=? ";
				String dept=this.jdbcTemplate.queryForObject(sql2,new Object[]{glbm}, String.class);
				l.setBmmc(dept);
			}
		}
		return map;
	}
		
}
