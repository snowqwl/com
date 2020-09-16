package com.sunshine.monitor.system.gate.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.AbstractLobCreatingPreparedStatementCallbackImpl;
import com.sunshine.monitor.comm.util.orm.SqlUtils;
import com.sunshine.monitor.comm.util.orm.bean.PreSqlEntry;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateCd;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.bean.AdministrativeDivision;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.service.LogManager;

@Repository("gateDao")
public class GateDaoImpl extends BaseDaoImpl implements GateDao {
	
	@Autowired
	@Qualifier("logManager")
	private LogManager logManager;
    
	private String getTabName(){
		return "code_gate";
	}
	
	public List<Map<String, Object>> getGateListXls(Map filter) throws Exception{
		StringBuffer sb = new StringBuffer("select g.kdbh, g.kdmc, g.kklx, g.dwdm, g.kkdz, g.kkjd, g.kkxz,c.cdbh, c.cdlx, c.ip, e.fxmc, " +
				"e.fxbh, e.fxlx from code_gate g,code_gate_cd c, code_gate_extend e where c.FXBH = e.FXBH and e.kdbh = g.kdbh ");
		if (filter.get("kkzt")!=null && filter.get("kkzt").toString()!="") {
			sb.append(" and g.kkzt = '").append(filter.get("kkzt").toString()).append("'");
		}
		if (filter.get("kdmc")!=null && filter.get("kdmc").toString()!="") {
			sb.append(" and g.kkmc like '%").append(filter.get("kdmc").toString()).append("%'");
		}
		if (filter.get("kklx")!=null && filter.get("kklx").toString()!="") {
			sb.append(" and g.kklx = '").append(filter.get("kklx").toString()).append("'");
		}
		if (filter.get("dwdm")!=null && filter.get("dwdm").toString()!="") {
			if(filter.get("dwdm").toString().length() == 6) {
				sb.append(" and g.dwdm like '" + filter.get("dwdm").toString() + "%'");
			} else {
				sb.append("  AND g.DWDM IN (select xjjg from frm_prefecture where dwdm = (SELECT csz FROM frm_syspara WHERE gjz='xzqh'))");
			}
		}
		sb.append(" order by g.gxsj desc ");
		return this.jdbcTemplate.queryForList(sb.toString());
	}
	
	public Map<String, Object> findPageGateForMap(Map<String, Object> map,
			Class<?> clazz) {
		int curPage = 1;
		int pageSize = 10;
		StringBuffer sb = new StringBuffer();
		Map<String, Object> result = null;
		if (map != null) {
			Set<String> set = map.keySet();
			for (Object o : set) {
				String val = o.toString();
				if (val.equalsIgnoreCase("dwdm")) {
					String[] deps = map.get(val).toString().split(",");
					for (int i = 0; i < deps.length; i++) {
						if (i == 0) {
							sb.append(" and dwdm = '" + deps[i] + "'");
						} else {
							sb.append(" or dwdm = '" + deps[i] + "'");
						}
					}
				} else if (!(val.equalsIgnoreCase("page") || val.equalsIgnoreCase("rows"))) {
					sb.append(" and " + val + "='" + map.get(val) + "'");
				}
			}
			if (map.containsKey("page")) {
				curPage = Integer.parseInt(map.get("page").toString());
			}
			if (map.containsKey("rows")) {
				pageSize = Integer.parseInt(map.get("rows").toString());
			}
		}

		StringBuffer sql = new StringBuffer(" select * from ");
		sql.append(" code_gate ").append(" where 1=1 ").append(sb);
		sql.append(" order by gxsj");

		result = this.findPageForMap(sql.toString(), curPage, pageSize, clazz);
		return result;
	}
	
	// 翻页查询卡口信息实现方法
	public Map<String, Object> findGatePageForMap(Page page, CodeGate gate,
			String glbm) throws Exception {
		StringBuffer sb = new StringBuffer("select * from code_gate where 1=1 ");
		if (StringUtils.isNotBlank(gate.getKkzt())) {
			sb.append(" and kkzt = '").append(gate.getKkzt()).append("'");
		}
		if (StringUtils.isNotBlank(gate.getKdmc())) {
			sb.append(" and kdmc like '%").append(gate.getKdmc()).append("%'");
		}
		if (StringUtils.isNotBlank(gate.getKklx())) {
			sb.append(" and kklx = '").append(gate.getKklx()).append("'");
		}
		if (StringUtils.isNotBlank(gate.getKkzt())) {
			sb.append(" and kkzt = '").append(gate.getKkzt()).append("'");
		}
		if (StringUtils.isNotBlank(gate.getDwdm())) {
			sb.append(" AND dwdm like '").append(gate.getDwdm().substring(0,4)).append("%'");
		}
		
		if (StringUtils.isNotBlank(glbm)) {
			sb.append(" and dwdm in(select xjjg from frm_prefecture where dwdm = '"+glbm+"')" );
		}
		
	
		
		//sb.append(" order by gxsj desc ");
		return this.findPageForMap(sb.toString(), page.getCurPage(),
				page.getPageSize(), CodeGate.class);
	}
	// 翻页查询卡口信息实现方法
	public Map<String, Object> findGatePageRedisForMap(Page page, CodeGate gate,
			String glbm) throws Exception {
		StringBuffer sb = new StringBuffer("select * from code_gate where 1=1 ");
		if (StringUtils.isNotBlank(gate.getKdmc())) {
			sb.append(" and kdmc like '%").append(gate.getKdmc()).append("%'");
		}
		if (StringUtils.isNotBlank(gate.getKklx())) {
			sb.append(" and kklx = '").append(gate.getKklx()).append("'");
		}
		if (StringUtils.isNotBlank(gate.getDwdm())) {
			sb.append(" AND dwdm like '").append(gate.getDwdm().substring(0,4)).append("%'");
		}
		if (StringUtils.isNotBlank(gate.getKkzt())) {
			sb.append(" AND kkzt = '").append(gate.getKkzt()).append("'");
		}
		/*else{
			if (StringUtils.isNotBlank(glbm)) {
				sb.append(" and dwdm in(select xjjg from frm_prefecture where dwdm = '"+glbm+"')" );
			}
		}*/

		
		//sb.append(" order by gxsj desc ");
		return this.findPageForMap(sb.toString(), page.getCurPage(),
				page.getPageSize(), CodeGate.class);
	}
	
	public List<CodeGate> getGateList(CodeGate gate)throws Exception{
		StringBuffer sb = new StringBuffer("select * from code_gate where 1=1 ");
		if (StringUtils.isNotBlank(gate.getKkzt())) {
			sb.append(" and kkzt = '").append(gate.getKkzt()).append("'");
		}
		if (StringUtils.isNotBlank(gate.getKdmc())) {
			sb.append(" and kkmc like '%").append(gate.getKdmc()).append("%'");
		}
		if (StringUtils.isNotBlank(gate.getKklx())) {
			sb.append(" and kklx = '").append(gate.getKklx()).append("'");
		}
		if (StringUtils.isNotBlank(gate.getDwdm())) {
			sb.append(" and dwdm like '").append(gate.getDwdm()).append("%'");
		}
		if (StringUtils.isNotBlank(gate.getXzqh())) {
			sb.append(" and xzqh like '").append(gate.getXzqh()).append("'");
		}

		sb.append(" order by nlssort(kdmc, 'NLS_SORT=SCHINESE_PINYIN_M') ");
		return this.queryForList(sb.toString(), CodeGate.class);
		
	}
	/**
	 * 获取卡口(含交警卡口)信息列表
	 * @return
	 * @throws Exception
	 */
	public List<CodeGate> getGateListAndJj(CodeGate gate)throws Exception{
		StringBuffer sb = new StringBuffer("select * from t_jj_code_gate where 1=1 ");
		if (StringUtils.isNotBlank(gate.getKkzt())) {
			sb.append(" and kkzt = '").append(gate.getKkzt()).append("'");
		}
		if (StringUtils.isNotBlank(gate.getKdmc())) {
			sb.append(" and kkmc like '%").append(gate.getKdmc()).append("%'");
		}
		if (StringUtils.isNotBlank(gate.getKklx())) {
			sb.append(" and kklx = '").append(gate.getKklx()).append("'");
		}
		if (StringUtils.isNotBlank(gate.getDwdm())) {
			if(gate.getDwdm().length() <= 6) {
				sb.append(" and dwdm like '" + gate.getDwdm() + "%'");
			} else {
				sb.append(" and dwdm = '" + gate.getDwdm() + "'");
			}
		}
		if (StringUtils.isNotBlank(gate.getXzqh())) {
			sb.append(" and xzqh like '").append(gate.getXzqh()).append("'");
		}

		sb.append(" order by nlssort(kdmc, 'NLS_SORT=SCHINESE_PINYIN_M') ");
		System.out.println("sb=" + sb.toString());
		return this.queryForList(sb.toString(), CodeGate.class);
		
	}
	
	

	// 保存卡口信息实现方法
	public void saveGate(CodeGate gate) throws Exception {
		String tmpSql = "select count(1) from code_Gate where kdbh = ? ";
		int count = this.jdbcTemplate.queryForInt(tmpSql,new Object[]{gate.getKdbh()});
		gate.setDlmc(null);
		gate.setXzqhmc(null);
		gate.setQgkdbh(null);
		//判断隐藏卡口编号是否为空，为空执行插入，不为空则执行更新
		if(count>0){
			Map<String,String> map = new HashMap<String,String>();
			map.put("gxsj","sysdate");
			PreSqlEntry p = SqlUtils.getPreUpdateSqlByObject(getTabName(),gate,map,"kdbh");
			this.jdbcTemplate.update(p.getSql(), p.getValues().toArray());
		}else{
			Map<String,String> map = new HashMap<String,String>();
			map.put("csbj","0");
			map.put("yjczlx","0");
			map.put("bdms","0");
			map.put("bjcsbj","0");
			PreSqlEntry p = SqlUtils.getPreInsertSqlByObject(getTabName(),gate,map);
			this.jdbcTemplate.update(p.getSql(), p.getValues().toArray()); 
		}
	}
	
	// 更新卡口经纬度
	public void saveGateXY(String gateArray,String glbm,String ip,String yhdh) throws Exception {
		String[] gateArr = gateArray.split(";");
		
		Arrays.sort(gateArr);
		
		List<String> list = new ArrayList<String>();
		
		list.add(gateArr[0]);
		for(int i = 1; i < gateArr.length; i++){
			if(!gateArr[i].equals(list.get(list.size()-1))){
				list.add(gateArr[i]);
			}
		}
		
		String[] gateArrs = list.toArray(new String[list.size()]);
		
		for(int i = 0 ; i < gateArrs.length;i++){
			String gateInfo = gateArrs[i];
			String[] gateInfoArr = gateInfo.split(",");
			String kdbh = gateInfoArr[0];
			String jd = gateInfoArr[1];
			String wd = gateInfoArr[2];
			
			String tmpSql = "update code_Gate set kkjd = '"+jd+"',kkwd = '"+wd+"' where kdbh = '"+kdbh+"'";
			this.jdbcTemplate.update(tmpSql);
			
			Log log = new Log();
			log.setGlbm(glbm);
			log.setIp(ip);
			log.setYhdh(yhdh);
		    log.setCzlx("8542");
		    
		    StringBuffer sb=new StringBuffer();
		    sb.append("卡口信息修改，操作条件为：");
			sb.append("卡口编号："+kdbh);
			sb.append(" 修改后的经度为："+jd+"，修改后的纬度："+wd);
		   
		    log.setCznr(sb.toString());
		    
			this.logManager.saveLog(log);
		}
	}

	// 保存卡口方向信息实现方法
	public void saveGateExtend(CodeGateExtend extend) throws Exception {
		extend.setFxbh(null);
		String tmpSql = "select count(1) from code_Gate_extend where kdbh = ? and fxlx = ?";
		int count = this.jdbcTemplate.queryForInt(tmpSql,new Object[]{extend.getKdbh(),extend.getFxlx()});
		//判断方向是否已经存在，存在则进行更新操作，否则进行插入操作
		if(count>0){
			Map<String,String> map = new HashMap<String,String>();
			map.put("gxsj","sysdate");
			PreSqlEntry p = SqlUtils.getPreUpdateSqlByObject("code_Gate_extend",extend,map,"kdbh","fxlx");
			this.jdbcTemplate.update(p.getSql(), p.getValues().toArray()); 
		}else{
			Map<String,String> map = new HashMap<String,String>();
			map.put("gxsj","sysdate");
			map.put("lrsj","sysdate");
			PreSqlEntry p = SqlUtils.getPreInsertSqlByObject("code_Gate_extend",extend,map);
			this.jdbcTemplate.update(p.getSql(), p.getValues().toArray()); 
		}
	}

	// 保存卡口车道信息实现方法
	public void saveRoad(CodeGateCd road) throws Exception {
		road.setFxbh(null);
		String tmpSql = "select count(1) from code_Gate_cd where kdbh = ? and fxlx = ? and cdbh = ? ";
		int count = this.jdbcTemplate.queryForInt(tmpSql,new Object[]{road.getKdbh(),road.getFxlx(),road.getCdbh()});
		//判断车道是否已经存在，存在则进行更新操作，否则进行插入操作
		if(count>0){
			Map<String,String> map = new HashMap<String,String>();
			map.put("gxsj","sysdate");
			PreSqlEntry p = SqlUtils.getPreUpdateSqlByObject("code_gate_cd",road,map,"kdbh","fxlx","cdbh");
			this.jdbcTemplate.update(p.getSql(), p.getValues().toArray()); 
		}else{
			Map<String,String> map = new HashMap<String,String>();
			map.put("gxsj","sysdate");
			map.put("csbj","0");
			map.put("bjcsbj","0");
			PreSqlEntry p = SqlUtils.getPreInsertSqlByObject("code_gate_cd",road,map);
			this.jdbcTemplate.update(p.getSql(), p.getValues().toArray()); 
		}
	}

	public String getKdbh(String xzqh) throws Exception {
		//生成卡口编号
		String seq = this.jdbcTemplate.queryForObject("select SEQ_GATE_KDBH.NEXTVAL from dual",String.class);
		String kdbh = xzqh+seq;
		return kdbh;
	}
	
	public List<Department> getDepartmentsByKdbh() throws Exception {
		String sql = "SELECT * FROM FRM_DEPARTMENT WHERE GLBM IN (SELECT DISTINCT(GLBM) FROM CODE_GATE) ORDER BY GLBM";
		return this.queryForList(sql, Department.class);
	}
	
	public List<Map<String,Object>> getGateTreeByGlbm(String search_text) throws Exception {
		StringBuffer sb = new StringBuffer();		
		sb.append(" select distinct kdbh as id, kdmc||decode(kkzt,2,'【已报废】',3,'【维修】') as idname,  decode(dwdm,'430000870000',dwdm,substr(dwdm,0,4)) as pid ")
		  .append(" from code_gate where 1=1 ");
		 if(search_text!=null&&search_text.length()>0) {
				sb.append(" and kdmc like '%")
				  .append(search_text)
				  .append("%' or dwdm in (select xzqhdm from frm_xzqh where xzqhdm in (select distinct xzqhdm from frm_xzqh ) and xzqhmc  like '%")
				  .append(search_text)
				  .append("%')");
			}
		 
		sb.append(" union all")
		  .append("  select distinct decode(xzqhdm,'430000870000',xzqhdm,substr(xzqhdm,0,4)) as id,xzqhmc as idname,nvl(substr(pid,0,4),0) as pid  from frm_xzqh where jb in ('2','3') ");
		if(search_text!=null&&search_text.length()>0) {		  
			  sb.append(" and xzqhdm in (select decode(dwdm,'430000870000',dwdm,substr(dwdm,0,4)||'00') from code_gate where  kdmc like '%")
			  .append(search_text)
			  .append("%')");
			}
		return this.getRecordData(sb.toString(),null);
	}
	
	public List<Map<String,Object>> getGateTreeByGlbmAndSt(String search_text,String dwdm) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from (select kdbh as id,  kdmc||decode(kkzt,2,'【已报废】',3,'【维修】') as idname, dwdm as pid")
		  .append(" from code_gate where 1=1 ");
		 if(dwdm!=null&&dwdm.length()>0) {
			 sb.append("and substr(dwdm,0,4) = ")
			 	.append("substr(")
			 		.append(dwdm)
			 		.append(",0,4)");
		 }
		  if(search_text!=null&&search_text.length()>0) {
			sb.append(" and kdmc like '%")
			  .append(search_text)
			  .append("%' or dwdm in (select glbm from frm_department where glbm in (select distinct dwdm from code_gate ) and bmmc  like '%")
			  .append(search_text)
			  .append("%')");
		}
		  sb.append(" union all")
		    .append(" select fd.glbm as id, fd.bmmc as idname,  substr(cu.dwdm, 0, 4) as pid ")
		    .append("from frm_department fd,code_url cu")
		    .append("  where fd.glbm in (select distinct dwdm from code_gate) and fd.glbm in (  select dwdm from code_gate ) and substr(fd.glbm, 0, 4) = substr(cu.dwdm, 0, 4)");
		  if(dwdm!=null&&dwdm.length()>0) {
				 sb.append("and substr(cu.dwdm, 0, 4) = ")
					.append("substr(")
			 		.append(dwdm)
			 		.append(",0,4)");
			 }
		 if(search_text!=null&&search_text.length()>0) {		  
			  sb.append(" and fd.glbm in (select dwdm from code_gate where  kdmc like '%")
			  .append(search_text)
			  .append("%')");
			}
		  sb.append(" union all")
		    .append(" select  substr(g.dwdm, 0, 4) as id,g.jdmc as idname,   substr(h.dwdm, 0, 4)as pid ")
		     .append("from code_url g, code_url h where substr(g.dwdm, 1, 2) = substr(h.dwdm, 1, 2) and h.dwdm like '4300%' and g.dwdm not like '4300%' ");
		  if(dwdm!=null&&dwdm.length()>0) {
				 sb.append("and substr(h.dwdm, 0, 4) = ")
					.append("substr(")
			 		.append(dwdm)
			 		.append(",0,4)");
			 }
		  if(search_text!=null&&search_text.length()>0) {
				sb.append(" and g.jdmc  like '%")
				  .append(search_text)
				  .append("%'");
				  
			}
		  sb.append(" ) order by nlssort(idname, 'NLS_SORT=SCHINESE_PINYIN_M') ");
		  //sb.append(" union all")
		   //sb .append(" select  substr(dwdm, 0, 4) as id,jdmc as idname, '0' as pid  from code_url where dwdm like '4300%'") ;
	
		  
		 /* sb.append(" union all")
		  .append(" select kdbh as id, kdmc as idname, '1' as pid")
		  .append(" from code_gate where kkzt='1' and kklx=1");		  
		  if(search_text!=null&&search_text.length()>0) {
			sb.append(" and kdmc like '%")
			  .append(search_text)
			  .append("%'");
		}
		  sb.append(" union all")
		    .append(" select distinct '1' as id, '省际卡口' as idname, '0' as pid ")
		    .append("from frm_department")
		    .append("  where glbm in (select distinct dwdm from code_gate)");
		  if(search_text!=null&&search_text.length()>0) {
				sb.append(" and '省际卡口' like '%")
				  .append(search_text)
				  .append("%'")
				  .append("or glbm in (select dwdm from code_gate where kkzt='1' and kklx=1 and kdmc like '%")
				  .append(search_text)
				  .append("%')");
			}*/
		return this.jdbcTemplate.queryForList(sb.toString());
	}
	
	public List getCodeGateExtend(Map filter) throws Exception {
		StringBuffer sb = new StringBuffer(" SELECT * FROM CODE_GATE_EXTEND T WHERE 1=1");
		if (filter != null) {
			if (filter.get("kdbh") != null
					&& filter.get("kdbh").toString().length() > 0)
				sb.append(" AND T.kdbh = '").append(
						filter.get("kdbh").toString()).append("'");
			if (filter.get("fxlx") != null
					&& filter.get("fxlx").toString().length() > 0)
				sb.append(" AND T.fxlx = '").append(
						filter.get("fxlx").toString()).append("'");
			if (filter.get("ywdw") != null
					&& filter.get("ywdw").toString().length() > 0)
				sb.append(" AND T.YWDW = '").append(
						filter.get("ywdw").toString()).append("'");
			if (filter.get("kdbhs") != null && filter.get("kdbhs").toString().length() > 0) 
				sb.append(" AND T.kdbh in (").append(
						filter.get("kdbhs").toString()).append(")");
		}
		sb.append(" ORDER BY kdbh,fxlx ");
		return this.queryForList(sb.toString(), CodeGateExtend.class);
	}
	
	public int getGateCount(CodeGate d, String dwdm) throws Exception {
		StringBuffer sql = new StringBuffer(
				"select * from code_gate where 1=1 ");
		if ((d.getKkzt() != null) && (d.getKkzt().length() > 0)) {
			sql.append(" AND KKZT = '").append(d.getKkzt()).append("'");
		}
		if ((d.getKdbh() != null) && (d.getKdbh().length() > 0)) {
			sql.append(" AND KDBH = '").append(d.getKdbh()).append("'");
		}
		if ((d.getKdmc() != null) && (d.getKdmc().length() > 0)) {
			sql.append(" AND KDMC LIKE '%").append(d.getKdmc()).append("%'");
		}
		if ((d.getKklx() != null) && (d.getKklx().length() > 0)) {
			sql.append(" AND KKLX = '").append(d.getKklx()).append("'");
		}
		if ((d.getDwdm() != null) && (d.getDwdm().length() > 0)) {
			sql.append(" AND DWDM = '").append(d.getDwdm()).append("'");
		}
		/*if ((dwdm != null) && (dwdm.length() > 1)) {
			sql.append(" AND DWDM IN (").append("select xjjg from frm_prefecture where dwdm ='").append(dwdm).append("')");
		}*/
		return this.getRecordCounts(sql.toString(),0);
	}
	
	public void saveKktp(final byte[] kktp, String kdbh) throws Exception {
		String u_sql = "";
		// 更新操作
		int count = this.jdbcTemplate.queryForInt("SELECT COUNT(1) FROM CODE_GATE_PIC WHERE KDBH = '"+ kdbh + "'");
		if (count > 0) {
			u_sql = "UPDATE CODE_GATE_PIC SET KKTP = ? WHERE KDBH = '" + kdbh+ "'";
		} else {
			u_sql = "INSERT INTO CODE_GATE_PIC (KDBH,KKTP)VALUES('" + kdbh+ "',?)";
		}
		AbstractLobCreatingPreparedStatementCallbackImpl callBack = new AbstractLobCreatingPreparedStatementCallbackImpl(
				this.lobHandler) {
			public void setValues(PreparedStatement pstmt, LobCreator lobCreator)throws SQLException, DataAccessException {
				lobCreator.setBlobAsBytes(pstmt, 1, kktp);
			}
		};
		this.jdbcTemplate.execute(u_sql, callBack);
	}

	public List<Map<String, Object>> getTollGateTree(String searchText,String dwdm) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" select kdbh as id,  kdmc||decode(kkzt,2,'【已报废】',3,'【维修】') as idname, dwdm as pid")
		  .append(" from v_dev_tollgate where 1=1 and dwdm in (select glbm from frm_department)");
		 if(dwdm!=null&&dwdm.length()>0) {
			 sb.append("and substr(dwdm,0,4) = ")
			 	.append("substr(")
			 		.append(dwdm)
			 		.append(",0,4)");
		 }
		  if(searchText!=null&&searchText.length()>0) {
			sb.append(" and kdmc like '%")
			  .append(searchText)
			  .append("%' or dwdm in (select glbm from frm_department where glbm in (select distinct dwdm from v_dev_tollgate ) and bmmc  like '%")
			  .append(searchText)
			  .append("%')");
		}
		  sb.append(" union all")
		    .append(" select fd.glbm as id, fd.bmmc as idname,  substr(cu.dwdm, 0, 4) as pid ")
		    .append(" from frm_department fd,code_url cu")
		    .append(" where fd.glbm in (select distinct dwdm from v_dev_tollgate) and fd.glbm in (  select dwdm from v_dev_tollgate ) and substr(fd.glbm, 0, 4) = substr(cu.dwdm, 0, 4)");
		  if(dwdm!=null&&dwdm.length()>0) {
				 sb.append("and substr(cu.dwdm, 0, 4) = ")
					.append("substr(")
			 		.append(dwdm)
			 		.append(",0,4)");
			 }
		 if(searchText!=null&&searchText.length()>0) {		  
			  sb.append(" and fd.glbm in (select dwdm from v_dev_tollgate where  kdmc like '%")
			  .append(searchText)
			  .append("%')");
			}
		  sb.append(" union all")
		    .append(" select  substr(g.dwdm, 0, 4) as id,g.jdmc as idname,   substr(h.dwdm, 0, 4)as pid ")
		     .append("from code_url g, code_url h where substr(g.dwdm, 1, 2) = substr(h.dwdm, 1, 2) and h.dwdm like '4300%' and g.dwdm not like '4300%' ");
		  if(dwdm!=null&&dwdm.length()>0) {
				 sb.append("and substr(h.dwdm, 0, 4) = ")
					.append("substr(")
			 		.append(dwdm)
			 		.append(",0,4)");
			 }
		  if(searchText!=null&&searchText.length()>0) {
				sb.append(" and g.jdmc  like '%")
				  .append(searchText)
				  .append("%'");
				  
			}
		return this.jdbcTemplate.queryForList(sb.toString());
	}
	
	public CodeGateExtend getDirect(String fxbh) throws Exception {
		String sql = "select * from code_Gate_extend where fxbh = ?"; 
		List<CodeGateExtend> list =  this.queryForList(sql , new Object[]{fxbh}, CodeGateExtend.class);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public List<CodeGate> getTollGateList(CodeGate gate) throws Exception {
		StringBuffer sb = new StringBuffer("select * from v_dev_tollgate where 1=1 ");
		if (StringUtils.isNotBlank(gate.getKkzt())) {
			sb.append(" and kkzt = '").append(gate.getKkzt()).append("'");
		}
		if (StringUtils.isNotBlank(gate.getKdmc())) {
			sb.append(" and kkmc like '%").append(gate.getKdmc()).append("%'");
		}
		if (StringUtils.isNotBlank(gate.getKklx())) {
			sb.append(" and kklx = '").append(gate.getKklx()).append("'");
		}
		if (StringUtils.isNotBlank(gate.getDwdm())) {
			if(gate.getDwdm().length() <= 6) {
				sb.append(" and dwdm like '" + gate.getDwdm() + "%'");
			} else {
				sb.append(" and dwdm = '" + gate.getDwdm() + "'");
			}
		}

		sb.append(" order by gxsj desc ");
		return this.queryForList(sb.toString(), CodeGate.class);
	}

	public String getOldGateName(String kdbh)  {
		try {
			/*
			StringBuffer sb = new StringBuffer(
					"SELECT KDMC FROM dc.CODE_GATE WHERE KDBH = '");
					*/
			StringBuffer sb = new StringBuffer(
			"SELECT KDMC FROM code_gate WHERE KDBH = '");
		//"SELECT KDMC FROM v_gate WHERE KDBH = '");
			sb.append(kdbh).append("'");
			List<CodeGate> list = this.queryForList(sb.toString(), CodeGate.class);
			if (list != null && list.size() > 0) {
				return list.get(0).getKdmc();
			}
		} catch (Exception e) {
			log.warn("卡口翻译",e);
		}
		return kdbh;
	}

	public <T> List<T> queryForListByMap(Map<String, Object> map,
			String tablename, Class<T> classz) throws Exception {
		return null;
	}

	public String getDirectName(String fxbh) throws Exception {
		/**StringBuffer sb = new StringBuffer(
				"SELECT FXMC FROM CODE_GATE_EXTEND WHERE FXBH = '");
		sb.append(fxbh).append("'");*/
		StringBuffer sb = new StringBuffer("SELECT FXMC FROM CODE_GATE_EXTEND WHERE FXBH = ?");
		List<CodeGateExtend> list = this.queryForList(sb.toString(), new Object[]{fxbh},CodeGateExtend.class);
		if (list != null && list.size() > 0) {
			return list.get(0).getFxmc();
		}
		return fxbh;
	}

	public String getGateName(String kdbh) throws Exception {
		/**StringBuffer sb = new StringBuffer(
				"SELECT KDMC FROM CODE_GATE WHERE KDBH = '");
		sb.append(kdbh).append("'");*/
		StringBuffer sb = new StringBuffer("SELECT KDMC FROM CODE_GATE WHERE KDBH = ?");
		List<CodeGate> list = this.queryForList(sb.toString(), new Object[]{kdbh},CodeGate.class);
		if (list != null && list.size() > 0) {
			return list.get(0).getKdmc();
		}
		return kdbh;
	}
	

	public Map<String, Object> getGates(Map filter, CodeGate d, String subsql)
			throws Exception {
		StringBuffer tmpSql = new StringBuffer(
				" SELECT * FROM CODE_GATE WHERE 1=1 ");
		if ((d.getKkzt() != null) && (d.getKkzt().length() > 0)) {
			tmpSql.append(" AND KKZT = '").append(d.getKkzt()).append("'");
		}
		if ((d.getKdbh() != null) && (d.getKdbh().length() > 0)) {
			tmpSql.append(" AND KDBH = '").append(d.getKdbh()).append("'");
		}
		if ((d.getKdmc() != null) && (d.getKdmc().length() > 0)) {
			tmpSql.append(" AND KDMC LIKE '%").append(d.getKdmc()).append("%'");
		}
		if ((d.getKklx() != null) && (d.getKklx().length() > 0)) {
			tmpSql.append(" AND KKLX = '").append(d.getKklx()).append("'");
		}
		if ((d.getGabh() != null) && (d.getGabh().length() > 0)) {
			tmpSql.append(" AND GABH LIKE '%").append(d.getGabh()).append("%'");
		}
		if ((d.getDwdm() != null) && (d.getDwdm().length() > 0)) {
			tmpSql.append(" AND DWDM = '").append(d.getDwdm()).append("'");
		}
		/*if ((subsql != null) && (subsql.length() > 1)) {
			tmpSql.append(" AND DWDM IN (").append("select xjjg from frm_prefecture where dwdm ='").append(subsql).append("')");
		}*/
		tmpSql.append(" ORDER BY GXSJ DESC");

		Map<String, Object> map = this.findPageForMapNoLimit(tmpSql.toString(),
				Integer.parseInt(filter.get("curPage").toString()), Integer
						.parseInt(filter.get("pageSize").toString()),
				CodeGate.class);
		return map;
	}

	@Override
	public Map<String, Object> getAdministrativeDivisions(Map<String,String> filter) throws Exception{
		StringBuffer sql = new StringBuffer("select t.xzqhxh, t.xzqhdm as xzqh, t.xzqhmc as qhmc, t.xzqhqc, t.pid " +
				"from frm_xzqh t where 1=1 and t.jb='3'");
		if(filter.get("xzqh") != null){
			sql.append(" and t.xzqhdm like '%" + filter.get("xzqh").toString() + "%'");
		}
		if(filter.get("qhmc") != null){
			sql.append(" and t.xzqhmc like '%" + filter.get("qhmc").toString() + "%'");
		}
		sql.append(" order by t.xzqhxh asc");
		Map<String, Object> map = this.findPageForMapNoLimit(sql.toString(),
				Integer.parseInt(filter.get("curPage").toString()), Integer
						.parseInt(filter.get("pageSize").toString()),
						AdministrativeDivision.class);
		return map;
	}
	
	public List<AdministrativeDivision> AdminDivisionsList() throws Exception{
		String sql = "select xzqhdm as xzqh,xzqhmc as qhmc from frm_xzqh where jb = '3' order by xzqhdm asc";
		List<AdministrativeDivision> list = this.queryForList(sql, AdministrativeDivision.class);
		if(list == null || list.size() <= 0){
			return null;
		}
		return list;
	}
	
	public AdministrativeDivision getAdminDivision(String xzqhxh, String xzqh) throws Exception{
		String sql = "select xzqhxh, xzqhdm as xzqh " +
				"from frm_xzqh where xzqhxh = " + xzqhxh + " and xzqhdm = '" + xzqh + "'";
		AdministrativeDivision ad = this.queryObject(sql, AdministrativeDivision.class);
		if(ad == null)
			return null;
		return ad;
	}
	
	public int updateAdminDivision(AdministrativeDivision ad) throws Exception{
		StringBuffer sql = new StringBuffer("update frm_xzqh set xzqhxh = ");
		if(StringUtils.isNotBlank(ad.getXzqhxh())){
			sql.append(ad.getXzqhxh());
		}
		sql.append(" where xzqhdm = '" + ad.getXzqh() + "'");
		int res = this.jdbcTemplate.update(sql.toString());
		return res;
	}
}