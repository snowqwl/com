package com.sunshine.monitor.system.monitor.dao.jdbc;

import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.monitor.bean.GateProject;
import com.sunshine.monitor.system.monitor.dao.GateProjectDao;

@Repository("gateProjectDao")
public class GateProjectDaoImpl extends BaseDaoImpl implements GateProjectDao {

	public Map<String, Object> findGateProjectForMap(Map filter,
			GateProject project) throws DataAccessException {
		String sql = "";
		if ((project.getRq() != null) && (project.getRq().length() > 0)) {
			sql += " and a.rq = '" + project.getRq() + "'";
		}
		if ((project.getDwdm() != null) && (project.getDwdm().length() > 0)) {
			sql += " and a.dwdm ='" + project.getDwdm() + "'";
		}
		sql = "select a.rq,a.dwdm,b.jdmc dwdmmc,a.ghjrs,a.sjjrs from JM_GATE_PROJECT a,code_url b " +
				"where a.dwdm=b.dwdm " + sql + " order by a.inputtime desc";
		if(filter == null) {
			List list= this.jdbcTemplate.queryForList(sql);
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("rows", list);
			resultMap.put("total", 0);
		}
		Map<String,Object> map = this.findPageForMap(sql, 
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString())
		);
		
		return map;
	}

	public GateProject getGateProject(String rq, String dwdm) {
		String sql = "select a.rq,a.dwdm,b.jdmc dwdmmc,a.ghjrs,a.sjjrs,a.inputtime from JM_GATE_PROJECT a,code_url b " +
				"where a.rq='" + rq + "' and a.dwdm='"+dwdm+"' and a.dwdm=b.dwdm";
		List<GateProject> list = this.queryForList(sql, GateProject.class);
		if(list.size() > 0)
			return (GateProject)list.get(0);
		else
			return null;
	}

	public List<GateProject> getGateProjects() throws DataAccessException {
		String sql = "select * from JM_GATE_PROJECT order by dwdm,rq";
		return this.queryForList(sql, GateProject.class);
	}

	public List<GateProject> getGateProjectsByrq(String rq)
			throws DataAccessException {
		String sql = "select * from JM_GATE_PROJECT where rq='" + rq + "'";
		return this.queryForList(sql, GateProject.class);
	}

	public boolean saveGateProject(GateProject bean)
			throws DataAccessException {
		String sql ="" ;
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sql = "select * from JM_GATE_PROJECT where rq='"+bean.getRq()+"' and dwdm='"+bean.getDwdm()+"'";
		List<GateProject> list = this.queryForList(sql, GateProject.class);
		if(list.size()>0){
			sql = "update JM_GATE_PROJECT set ghjrs="+bean.getGhjrs() +" ,inputtime=to_date('"+sdf.format(new Date())+"','yyyy-mm-dd hh24:mi:ss')"
				+" where rq='"+bean.getRq()+"' and dwdm='"+bean.getDwdm()+"'";
		}else {
			sql = "insert into JM_GATE_PROJECT(rq, dwdm, ghjrs) values('"+bean.getRq()+"','"+bean.getDwdm()
				+"','"+bean.getGhjrs()+"')";
		}
		return this.jdbcTemplate.update(sql)>0?true:false;
	}

	public boolean batchDeleteProject(String[] rqhs)
			throws DataAccessException {
		
		boolean flag = true ;
		String[] sqlArray = new String[rqhs.length];
		int i = 0 ;
		for (String rq : rqhs) {
			sqlArray[i]="DELETE FROM JM_GATE_PROJECT WHERE RQ = " + rq;
			i = i + 1 ;
		}
		int[] result = this.jdbcTemplate.batchUpdate(sqlArray);
		for (int j = 0; i < result.length; j++) {
			if(result[j]<0){
				// 第i+1条数据执行失败，只要有一条数据执行失败，则结果为失败
				flag = false ;
			}
		}
		return flag ;
	}

	
	public List getCode_Url() throws Exception {
		String sql=" select substr(dwdm,0,4) DWDM,JDMC from code_url "
			+" where jb='3' "
			;
		return this.jdbcTemplate.queryForList(sql);
	}
	
	public List getGateInfo() throws Exception {
		String sql =  "" 
		+"	select c2.dwdm, c2.jdmc,  nvl(c1.total,0) total"
		+"	  from (select substr(kdbh, 0, 4) || '00000000' as kdbh, count(kdbh) TOTAL"
		+"	          from code_gate"
		+"	         group by substr(kdbh, 0, 4)) c1"
		+"	  right join (select dwdm, jdmc from code_url where jb='3' order by sort ) c2"
		+"	    on c2.dwdm = c1.kdbh"
		;
		return this.jdbcTemplate.queryForList(sql);
	}
	
	public List getGateInfoByCity(String glbm) throws Exception {
		String sql= ""
		+"	select * from "
		+"	(select count(1) TOTAL from code_gate where kdbh like '"+glbm+"%' ) c1 ,  "
		+"	        (select count(1) ON_LINE from code_gate where kkzt='1' and kdbh like '"+glbm+"%' ) c2 ,"
		+"	        (select count(1) BREAK from code_gate where kkzt='2' and kdbh like '"+glbm+"%' ) c3 ,"
		+"	        (select count(1) FIX from code_gate where kkzt='3' and kdbh like '"+glbm+"%' ) c4 "
		;
		
		return this.jdbcTemplate.queryForList(sql);
		}
	
	
	public Map getGateConnectCount() throws Exception {
		Map resultMap=new HashMap();
		String sql=" select substr(dwdm,0,4) DWDM,JDMC from code_url "
			+" where jb='3' "
			;
		List list=this.jdbcTemplate.queryForList(sql);
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			//统计接入情况sql
			String connectSql=" select * from "
				+" (select count(1) TOTAL from code_gate where kdbh like '"+map.get("DWDM")+"%' ) c1 ,"
				+"  (select count(1) ON_LINE from code_gate where kkzt='1' and kdbh like '"+map.get("DWDM")+"%' ) c2"
				;
			List connectList=this.jdbcTemplate.queryForList(connectSql);
			if(connectList.size()>0){
				Map connectMap=(Map)connectList.get(0);
				if(!connectMap.get("TOTAL").equals("0")){
					resultMap.put(map.get("JDMC"),connectMap);
				}
			}
		}
		
		return resultMap;
	}



}
