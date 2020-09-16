package com.sunshine.monitor.system.communication.dao.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.dao.page.PagingParameter;
import com.sunshine.monitor.comm.maintain.SmsFacade;
import com.sunshine.monitor.system.communication.dao.CommunicationDao;
@Repository("CommunicationDao")
public class CommunicationDaoImpl extends BaseDaoImpl implements
		CommunicationDao {

	public CommunicationDaoImpl() {

		// super.setTableName("FRM_COMMUNICATION");
	}
	public Map<String, Object> getSMSForList(Map<String, Object> conditions,
			String tableName) throws Exception {
		// TODO Auto-generated method stub
		List vl=new ArrayList<>();
		
		//StringBuffer sql=new StringBuffer("select * from FRM_COMMUNICATION where 1=1");
		StringBuffer sql=new StringBuffer("select id,ywlx,dxjsr,dxjshm,dxnr,dxfssj,zt,fsfs,reason from FRM_COMMUNICATION where 1=1");
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
			String key = entry.getKey();
			String value = (String) entry.getValue();
			if (value!=null&&value!="") {
				if (key.indexOf("kssj") != -1) {
					vl.add(value);
					sql.append(" and dxfssj >=  to_date(?,'yyyy-mm-dd hh24:mi:ss')");
				} else if (key.indexOf("jssj") != -1) {
					vl.add(value);
					sql.append(" and dxfssj <=  to_date(?,'yyyy-mm-dd hh24:mi:ss')");
				} else if ("dxjsr".equalsIgnoreCase(key)) {
					vl.add("%"+value+"%");
					sql.append(" and dxjsr like ?");
				} else if ("dxjshm1".equalsIgnoreCase(key)) {
					vl.add("%"+value+"%");
					sql.append(" and dxjshm like ?");
				} else if ("ywlx".equalsIgnoreCase(key)) {
					vl.add(value);
					sql.append(" and ywlx = ?");
				} else if ("fsfs".equalsIgnoreCase(key)) {
					sql.append(" and fsfs = ?");
				} 
			}
		}
		sql.append(" order by id desc ");
		return queryforMapnew(conditions.get("page").toString(), conditions.get("rows").toString(), sql.toString(), vl);
//		return this.findPageForMap(sql.toString(), Integer.parseInt(conditions.get("page").toString()), Integer.parseInt(conditions.get("rows").toString()));
	}
	public Map<String, Object> getGateForList(Map<String, Object> conditions,
			String tableName) throws Exception {
		

		List vl=new ArrayList<>();
		StringBuffer sql=new StringBuffer("select KDBH,FXBH,YWZRR,YWSJHM,FXLX,FXMC,GXSJ from CODE_GATE_EXTEND  where 1=1");
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
			String key = entry.getKey();
			String value = (String) entry.getValue();
			if(value!=null&&value!=""){
				if ("ywsjhm".equalsIgnoreCase(key)) {
					vl.add("%"+value+"%");
					sql.append(" and ywsjhm like ?");
				} else if ("ywzrr".equalsIgnoreCase(key)) {
					vl.add("%"+value+"%");
						sql.append(" and ywzrr like ?");
				} else if ("kdbh".equalsIgnoreCase(key)) {	
					vl.add(value);
						sql.append(" and kdbh =?");
				} else if ("fxbh".equalsIgnoreCase(key)) {
					vl.add(value);
					sql.append(" and fxbh =?");
				} 
				}
			}
		return queryforMapnew(conditions.get("page").toString(), conditions.get("rows").toString(), sql.toString(), vl);
//		return this.findPageForMap(sql.toString(), Integer.parseInt(conditions.get("page").toString()), Integer.parseInt(conditions.get("rows").toString()));
	
	}
	
	
	public Map<String, Object> getUserForList(Map<String, Object> conditions,
			String tableName) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer("select yhdh,yhmc,jh,lxdh1,lxdh2,lxdh3,gxsj from frm_sysuser  where 1=1");
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
	

		List vl=new ArrayList<>();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
			String key = entry.getKey();
			String value = (String) entry.getValue();
			 if ("yhhm".equalsIgnoreCase(key)) {
				 if(value!=null&&value!="")
					 vl.add("%"+value+"%");
				 vl.add("%"+value+"%");
				 vl.add("%"+value+"%");
					 sql.append(" and (lxdh1 like ? or lxdh2 like ? or lxdh3 like ?)");
			 }
			 else if ("jh".equalsIgnoreCase(key)) {	
					if(value!=null&&value!="")
						vl.add("%"+value+"%");
						sql.append(" and jh like ?");
				} else if ("yhmc".equalsIgnoreCase(key)) {
					if(value!=null&&value!="")
						vl.add("%"+value+"%");
					sql.append(" and yhmc like ?");
				} 
			}
		vl.add(conditions.get("glbm"));
		sql.append(" and glbm = ?");
		return queryforMapnew(conditions.get("page").toString(), conditions.get("rows").toString(), sql.toString(), vl);
//		return this.findPageForMap(sql.toString(), Integer.parseInt(conditions.get("page").toString()), Integer.parseInt(conditions.get("rows").toString()));
	
	}
	
	
	
	public int updateGate(Map gate) {
		// TODO Auto-generated method stub
		
		List vl=new ArrayList<>();
		vl.add(gate.get("ywsjhm"));
		vl.add(gate.get("kdbh"));
		vl.add(gate.get("fxbh"));
		String sql= "update CODE_GATE_EXTEND set ywsjhm=?  where kdbh=? and fxbh=?";
		return this.jdbcTemplate.update(sql,vl.toArray());
	}
	
	public int updateUserPhone(Map user){
		List vl=new ArrayList<>();
		vl.add(user.get("lxdh1"));
		vl.add(user.get("lxdh2"));
		vl.add(user.get("lxdh3"));
		vl.add(user.get("yhdh"));
		String sql= "update frm_sysuser set lxdh1=? ,lxdh2=? ,lxdh3=?  ,gxsj=sysdate  where yhdh=?";
		return this.jdbcTemplate.update(sql);
	}
	
	
	public int sendSusTips() throws Exception {
		StringBuffer sb = new StringBuffer();
		boolean flag=false;
		SmsFacade smsFacade = new SmsFacade();
		sb.append(" select * from frm_communication where 1=1");
		sb.append(" and zt='0' ");
		sb.append(" and bz='1' ");
		sb.append(" and (sysdate-ywclsj)*24*3600>7200");
		List list = this.jdbcTemplate.queryForList(sb.toString());
		String YWLX = "";
		String BKXH = "";
		int updateCount = 0;
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if(!StringUtils.isBlank(map.get("YWLX").toString())){
				YWLX=map.get("YWLX").toString();
			}
			if(!StringUtils.isBlank(map.get("XH").toString())){
				BKXH=map.get("XH").toString();
			}
			List ztlist=this.jdbcTemplate.queryForList("SELECT bkxh FROM veh_suspinfo WHERE ywzt=? AND  bkxh =?",new Object[]{YWLX,BKXH});
			if(ztlist.size()>0){
				if(map.get("DXJSHM")!=null){
					if(!map.get("DXJSHM").toString().equals("")){
					 flag=smsFacade.sendAndPostSms(map.get("DXJSHM").toString(), map.get("DXNR").toString(), null);
					if(!flag){
						updateCount += this.updateCommuniceZt_1(map.get("ID").toString());
					}
					else
						this.updateCommuniceZt_2(map.get("ID").toString(),"短信发送失败");
					}
					else
						this.updateCommuniceZt_2(map.get("ID").toString(),"手机号码为空，短信自动作废");
				}
				this.updateCommuniceZt_2(map.get("ID").toString(),"手机号码为空，短信自动作废");
			}
			else{
				this.updateCommuniceZt_2(map.get("ID").toString(),"业务已经处理完，无需发送短信提醒");
			}
			
		
			
		}
		return updateCount;
	}
  //发送状态 (标记为已发送，并且记录发送时间)
	public int updateCommuniceZt_1(String id) {
		String sql = " update frm_communication set "
				+ " zt='1' ,DXFSSJ=sysdate " + " where id =?  ";
		int i = this.jdbcTemplate.update(sql,new String[]{id});
		return i;
	}
	//业务已经处理完，无需发送短信，状态改为待删除
	public int updateCommuniceZt_2(String id,String reason) {
		String sql = " update frm_communication set "
				+ " zt='2' , reason=?   where id =?  ";
		int i = this.jdbcTemplate.update(sql,new String[]{reason,id});
		return i;
	}
	
	public Map getcommunicateByXh (String xh){
		Map map=new HashMap();
		String sql="select * from frm_communication where xh=?  and zt='1'";
		List list=this.jdbcTemplate.queryForList(sql,new String[]{xh});
		for(int i=0;i<list.size();i++){
			 map=(Map)list.get(i);
			return map;
		}
			return map;
	}
	
	public List getInfoByXh(String xh){
		String sql="  select * from frm_communication where xh =? ";
		return this.jdbcTemplate.queryForList(sql,new String[]{xh});
	}
	
	
	public int sendNoLjTips() throws Exception {
		String sql =" select * from frm_communication where   zt='0' and bz='2'";
		List list=this.jdbcTemplate.queryForList(sql);
		SmsFacade smsFacade = new SmsFacade();
		int updateCount = 0;
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			if(map.get("DXJSHM")!=null){
				if(!map.get("DXJSHM").toString().equals("")){
					boolean flag= smsFacade.sendAndPostSms(map.get("DXJSHM").toString(), map.get("DXNR").toString(), null);
					if(!flag){
						this.updateCommuniceZt_2(map.get("ID").toString(),"短信发送失败");
					}
					else
					updateCount += this.updateCommuniceZt_1(map.get("ID").toString());
				}
				else
					this.updateCommuniceZt_2(map.get("ID").toString(),"手机号码为空，短信自动作废");
			}
			else{
				this.updateCommuniceZt_2(map.get("ID").toString(),"手机号码为空，短信自动作废");
			}
			
			
		}
		return updateCount;
	}
	
	public int sendLjNotCancelSusTips() throws Exception {
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		SmsFacade smsFacade = new SmsFacade();
		sb.append(" select * from frm_communication where 1=1");
		sb.append(" and zt='0' ");
		sb.append(" and bz='3' ");
		sb.append(" and fsfs='2' ");
		sb.append(" and (sysdate-ywclsj)>1");
		List list = this.jdbcTemplate.queryForList(sb.toString());
		String YWLX = "";
		String BKXH = "";
		int updateCount = 0;
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if(!StringUtils.isBlank(map.get("YWLX").toString())){
				YWLX=map.get("YWLX").toString();
			}
			if(!StringUtils.isBlank(map.get("XH").toString())){
				BKXH=map.get("XH").toString();
			}
			List ztlist=this.jdbcTemplate.queryForList("SELECT bkxh FROM veh_suspinfo WHERE ywzt=? AND  bkxh =?",new String[]{YWLX,BKXH});
			if(ztlist.size()>0){
				if(map.get("DXJSHM")!=null){
					if(!map.get("DXJSHM").toString().equals("")){
					 flag = smsFacade.sendAndPostSms(map.get("DXJSHM").toString(), map.get("DXNR").toString(), null);
					if(!flag){
						this.updateCommuniceZt_2(map.get("ID").toString(),"短信发送失败");
					}
					else
						updateCount += this.updateCommuniceZt_1(map.get("ID").toString());
					}
					else
						this.updateCommuniceZt_2(map.get("ID").toString(),"手机号码为空，短信自动作废");
				}
				this.updateCommuniceZt_2(map.get("ID").toString(),"手机号码为空，短信自动作废");
			}
			else{
				this.updateCommuniceZt_2(map.get("ID").toString(),"业务已经处理完，无需发送短信提醒");
			}
		}
		return updateCount;
	}
	
	public List getSMSDatesByXh(String xh) throws Exception{
		String sql = "select * from frm_communication where ywlx in (51,52)";
		String tempsql="";
		if(xh!=null||xh.length()>0){
			 tempsql = "and xh = ?";						
		}
		sql =sql+ tempsql;	
		return this.jdbcTemplate.queryForList(sql,new String[]{xh});
	}
	public Map queryforMapnew(String page, String rows, String sql, List vl) {
		String countsql = "SELECT COUNT(1) from (" + sql + ")s";
		int totalRows = -1;
		Map<String, Object> map = new HashMap<>();
		totalRows = this.jdbcTemplate.queryForInt(countsql.toString(), vl.toArray());
		List<Map<String, Object>> queryForList = new ArrayList<>();
		if (totalRows > 0) {
			String orgSql = "select * from (" + sql + " where rownum <= ?) where rn >= ?";
			PagingParameter pagingParameter = new PagingParameter(Integer.parseInt(page), Integer.parseInt(rows),
					totalRows);
			Map<String, Integer> map1 = pagingParameter.getStartAndEndRow();
			int start = map1.get("start");
			int end = map1.get("end");
			vl.add(end);
			vl.add(start);
			queryForList = this.jdbcTemplate.queryForList(orgSql, vl.toArray());
		}
		map.put("rows", queryForList);
		map.put("total", totalRows);

		return map;
	}
}
