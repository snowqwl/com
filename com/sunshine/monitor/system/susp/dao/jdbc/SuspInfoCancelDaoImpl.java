package com.sunshine.monitor.system.susp.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.orm.SqlUtils;
import com.sunshine.monitor.comm.util.orm.bean.PreSqlEntry;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspInfoCancelDao;

@Repository("suspInfoCancelDao")
public class SuspInfoCancelDaoImpl extends BaseDaoImpl implements SuspInfoCancelDao {

	public Map findSuspinfoCancelForMap(Map filter, VehSuspinfo info,
			String glbm,String modul) throws Exception {
		StringBuffer sb = new StringBuffer(" select BKXH, HPZL,hphm,BKDL, BKLB ,bkrmc,to_char(BKQSSJ,'yyyy-mm-dd') as BKQSSJ ")
		.append(",to_char(BKJZSJ,'yyyy-mm-dd') as BKJZSJ,bjzt, bjsj  from  ");
		
		if("new".equals(modul)){			
			sb.append(" (select * from VEH_SUSPINFO  where YWZT = '14'   and ( XXLY = '0' or xxly = '2' ) ");
			sb.append(" and ((bkdl = '1' or bkdl = '2' or bkdl = '3') and BKJG  in (SELECT xjjg FROM frm_prefecture WHERE dwdm='").append(glbm)
					.append("' OR xjjg='").append(glbm).append("')) ");
		}else{
			sb.append(" (select * from VEH_SUSPINFO  where YWZT = '41'   and XXLY = '0' ");
			sb.append(" and bkjg in (Select xjjg from frm_prefecture Where dwdm='").append(glbm).append("')");
//			sb.append(" and BKJG  in (SELECT glbm FROM frm_department WHERE glbm='").append(glbm).append("' OR sjbm='").append(glbm).append("')");
//			sb.append(" and BKJG = '").append(glbm).append("'");
		}
		
		if(StringUtils.isNotBlank(info.getBkdl())){
			sb.append(" and bkdl = '").append(info.getBkdl()).append("'  ");
		}
		
		if(StringUtils.isNotBlank(info.getBklb())){
			sb.append(" and bklb = '").append(info.getBklb()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getHpzl())){
			sb.append("  and hpzl = '").append(info.getHpzl()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getHphm())){
			sb.append(" and hphm like '%").append(info.getHphm()).append("%' ");		
		}
		
		if(StringUtils.isNotBlank(info.getKssj())){
			sb.append(" and bksj >= to_date('").append(info.getKssj()).append("','yyyy-mm-dd HH24:mi:ss')  ");
		}
		
		if(StringUtils.isNotBlank(info.getJssj())){
			sb.append(" and bksj <= to_date('").append(info.getJssj()).append("','yyyy-mm-dd HH24:mi:ss') ");
		}
		/*
		if(StringUtils.isBlank(info.getKssj()) && StringUtils.isBlank(info.getJssj())){
			sb.append(" and bksj >= sysdate - 365  ");
		}
		*/
		if(StringUtils.isNotBlank(info.getBjzt())){
			sb.append(" and bjzt = '").append(info.getBjzt()).append("' ");
		}
		
		sb.append(")v ");
		sb.append("order by bksj desc ");
		
//		System.out.println("new审核:"+sb.toString());
		return this.getSelf().findPageForMap(sb.toString(),
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
	}
	
	public Map findSuspinfoCancelOverTimeForMap(Map filter, VehSuspinfo info,
			String glbm,String modul) throws Exception {
		StringBuffer sb = new StringBuffer(" select BKXH, HPZL,hphm,BKDL, BKLB ,bkrmc,to_char(BKQSSJ,'yyyy-mm-dd') as BKQSSJ ")
		.append(",to_char(BKJZSJ,'yyyy-mm-dd') as BKJZSJ,bjzt, bjsj  from  ");
		
		if("new".equals(modul)){
			
			sb.append(" VEH_SUSPINFO  where YWZT = '14'   and XXLY = '0' ");
			sb.append(" and (((bkdl = '1' or bkdl = '2') and BKJG = '").append(glbm)
					.append("') or  (bkdl = '3' and bkr = '").append(info.getBkr())
			.append("' )) ");
		}else{
			sb.append(" VEH_SUSPINFO  where YWZT = '41'   and XXLY = '0' ");
			sb.append(" and BKJG = '").append(glbm).append("' ");
		}
		
		sb.append(" and bkdl='3'");
		
		if(StringUtils.isNotBlank(info.getBklb())){
			sb.append(" and bklb = '").append(info.getBklb()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getHpzl())){
			sb.append("  and hpzl = '").append(info.getHpzl()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getHphm())){
			sb.append(" and hphm like '%").append(info.getHphm()).append("%' ");		
		}
		
		if(StringUtils.isNotBlank(info.getKssj())){
			sb.append(" and bksj >= to_date('").append(info.getKssj()).append("','yyyy-mm-dd HH24:mi:ss')  ");
		}
		
		if(StringUtils.isNotBlank(info.getJssj())){
			sb.append(" and bksj <= to_date('").append(info.getJssj()).append("','yyyy-mm-dd HH24:mi:ss') ");
		}
		
		if(StringUtils.isBlank(info.getKssj()) && StringUtils.isBlank(info.getJssj())){
			sb.append(" and bksj >= sysdate - 365  ");
		}
		
		if(StringUtils.isNotBlank(info.getBjzt())){
			sb.append(" and bjzt = '").append(info.getBjzt()).append("' ");
		}
		sb.append(" and (sysdate-bksj)*24*3600>7200 ");
		//sb.append(")v ");
		System.out.println("timeout:"+sb.toString());
		return this.getSelf().findPageForMap(sb.toString(), 
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
	}

	//更新布控表(针对撤控申请)
	public int updateSuspInfoForCancel(VehSuspinfo info)throws Exception{
		String userdw = info.getCxsqdw();
		String bkjg = info.getBkjg();
		StringBuffer sqlCancel = null;
		String bkr = info.getBkrjh();
		String ckr = info.getCxsqr();
		if(!userdw.equals(bkjg)){//上级的人撤控申请，不走审核审批流程
			sqlCancel = new StringBuffer("UPDATE veh_suspinfo  set ywzt = '99' ,gxsj = sysdate ");
		}else{
			if(!bkr.equals(ckr)){
				sqlCancel = new StringBuffer("UPDATE veh_suspinfo  set ywzt = '99' ,gxsj = sysdate ");
			}else{
				sqlCancel = new StringBuffer("UPDATE veh_suspinfo  set ywzt = '41' ,gxsj = sysdate ");
			}
		}
		if(StringUtils.isNotBlank(info.getCkyydm()))
			sqlCancel.append(",ckyydm = '").append(info.getCkyydm()).append("' ");
		
		if(StringUtils.isNotBlank(info.getCkyyms()))
			sqlCancel.append(",ckyyms = '").append(info.getCkyyms()).append("' ");
		
		if(StringUtils.isNotBlank(info.getCxsqr()))
			sqlCancel.append(", cxsqr = '").append(info.getCxsqr()).append("' ");
		
		if(StringUtils.isNotBlank(info.getCxsqrjh()))
			sqlCancel.append(",cxsqrjh = '").append(info.getCxsqrjh()).append("' ");
		
		if(StringUtils.isNotBlank(info.getCxsqrmc()))
			sqlCancel.append(",cxsqrmc = '").append(info.getCxsqrmc()).append("' ");
		
		if(StringUtils.isNotBlank(info.getCxsqdw()))
			sqlCancel.append(", cxsqdw = '").append(info.getCxsqdw()).append("' ");
		
		if(StringUtils.isNotBlank(info.getCxsqdwmc()))
			sqlCancel.append(", cxsqdwmc = '").append(info.getCxsqdwmc()).append("' ");
		
		if(StringUtils.isBlank(info.getBy1())){
			sqlCancel.append(", by1 = to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')");
		}
		
		/**一键撤控的申请人的yhdh**/
		if(StringUtils.isNotBlank(info.getBy5())){
			sqlCancel.append(", by5 = '").append(info.getBy5()).append("'");
		}
		
		sqlCancel.append(", cxsqsj = sysdate  where bkxh = '").append(info.getBkxh())
		.append("' ");
		
		
		int i = this.jdbcTemplate.update(sqlCancel.toString());
		
		return i;
	}
	
	//写入操作日志
	public int insertBusinessLog(VehSuspinfo info,String jz)throws Exception{
		
		StringBuffer sqlLog = new StringBuffer("INSERT into BUSINESS_LOG (xh, ywxh, ywlb, ywjb, czrdh, czrjh, czrdwdm, czrdwjz) ");
		sqlLog.append(" values (seq_business_log_xh.nextval,'").append(info.getBkxh()).append("','20','")
		.append(info.getBkjb()).append("','").append(info.getCxsqr()).append("','").append(info.getCxsqrjh()).append("','")
		.append(info.getCxsqdw()).append("','").append(jz).append("' )");
		
		int i = this.jdbcTemplate.update(sqlLog.toString());
		
		return i;
	}
	
	public List getAlarmList(String bkxh) throws Exception {
		String sql = "select * from veh_alarmrec where bkxh = ?";
		
		List list = this.queryForList(sql, new Object[]{bkxh}, VehAlarmrec.class);
		return list;
	}

	public List getAuditApproves(AuditApprove aa) throws Exception {
		String sql = "";
		List<String> list = new ArrayList<String>(); 
		if ((aa.getBkxh() != null) && (aa.getBkxh().length() > 0)) {
			//sql = sql + " and bkxh='" + aa.getBkxh() + "'";
			sql = sql + " and bkxh = ?";
			list.add(aa.getBkxh());
		}
		if ((aa.getBzw() != null) && (aa.getBzw().length() > 0)) {
			if (aa.getBzw().length() == 1){
				//sql = sql + " and bzw='" + aa.getBzw() + "'";
				sql = sql + " and bzw = ?";
				list.add(aa.getBzw());
			}else if (aa.getBzw().equals("12")){
				sql = sql + " and (bzw='1' or bzw='2')";
			}else if (aa.getBzw().equals("34")) {
				sql = sql + " and (bzw='3' or bzw='4')";
			}
		}
		if (sql.length() > 1)
			sql = " where " + sql.substring(5, sql.length()) + " ";
		sql = "select XH, BKXH, CZR, CZRDW, CZRDWMC, CZSJ, CZJG, MS, BZW, BY1, BY2, CZRJH, CZRMC from audit_approve " + sql + " order by czsj desc";
		return this.queryForList(sql, list.toArray(), AuditApprove.class);
	}

	//获取自动撤控的信息
	public List<VehSuspinfo> getAutoCsuspinfoList() throws Exception {
		String sql = "select * from veh_suspinfo where bkjzsj < sysdate "
				+ "and ywzt <> '99' and jlzt <> '2' and rownum <= 1000";
		List<VehSuspinfo> list = this.queryList(sql, VehSuspinfo.class);
		return list;
	}

	public Map findSuspinfoCancelTimeoutForMap(Map filter, VehSuspinfo info,
			String glbm) throws Exception {
		StringBuffer sb = new StringBuffer(" select BKXH, HPZL,hphm, BKLB ,bkrmc,to_char(BKQSSJ,'yyyy-mm-dd') as BKQSSJ ")
		.append(",to_char(BKJZSJ,'yyyy-mm-dd') as BKJZSJ,bjzt, bjsj  from  ");
		
			sb.append(" (select * from VEH_SUSPINFO  where  XXLY = '0' ");
			sb.append(" and (((bkdl = '1' or bkdl = '2') and bkr = '").append(info.getBkr())
					.append("') or  (bkdl = '3' and bkr = '").append(info.getBkr())
			.append("' )) ");
			sb.append(" and YWZT='14' and cxsqsj is null and by2 is not null and ROUND(TO_NUMBER(sysdate - by2) * 24 * 60 * 60) >= 24 * 60 * 60 ");
		
		if(StringUtils.isNotBlank(info.getBkdl())){
			sb.append(" and bkdl = '").append(info.getBkdl()).append("'  ");
		}
		
		if(StringUtils.isNotBlank(info.getBklb())){
			sb.append(" and bklb = '").append(info.getBklb()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getHpzl())){
			sb.append("  and hpzl = '").append(info.getHpzl()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getHphm())){
			sb.append(" and hphm like '%").append(info.getHphm()).append("%' ");		
		}
		
		if(StringUtils.isNotBlank(info.getKssj())){
			sb.append(" and bksj >= to_date('").append(info.getKssj()).append("','yyyy-mm-dd HH24:mi:ss')  ");
		}
		
		if(StringUtils.isNotBlank(info.getJssj())){
			sb.append(" and bksj <= to_date('").append(info.getJssj()).append("','yyyy-mm-dd HH24:mi:ss') ");
		}
		
		if(StringUtils.isBlank(info.getKssj()) && StringUtils.isBlank(info.getJssj())){
			sb.append(" and bksj >= sysdate - 365  ");
		}
		
		if(StringUtils.isNotBlank(info.getBjzt())){
			sb.append(" and bjzt = '").append(info.getBjzt()).append("' ");
		}
		
		sb.append(")v ");
		
		//System.out.println(sb.toString());
		return this.getSelf().findPageForMap(sb.toString(), 
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
	}
	
	/**
	 * 二次同步警综布控撤控
	 */
	public void synchronizationJz() throws Exception {
		//1-同步开始时间
		//String x_sql = "select bz from trans_schedule where rwbh='A4010'";    //警综被盗被抢同步
		String x_sql = "select bz from JM_trans_schedule where rwbh='A4010'";   //警综被盗被抢同步
		String t_kssj = (String) this.jdbcTemplate.queryForObject(x_sql, String.class);
		if(t_kssj.equals("") || t_kssj==null){
			t_kssj = "20130101000001";
		}
		String t_jyaq="";
		String t_clsbdh="";
		String t_fdjh="";
		String y_sql = "select csz from frm_syspara where gjz='xzqh'";
		String t_bkfw = this.jdbcTemplate.queryForObject(y_sql, String.class);
					
		//2-需要同步的数据(查询一个月内)
		String v_sql = " SELECT bkxh,ysbh,hphm,hpzl,bkdl,bklb,to_char(bkqssj,'yyyy-mm-dd hh24:mi:ss') bkqssj," +
				"to_char(bkjzsj,'yyyy-mm-dd hh24:mi:ss') bkjzsj,jyaq,bkfwlx,bkfw,bkjb," +
				"bkxz,sqsb,bjya,mhbkbj,bjfs,dxjshm,lar,ladw,ladwlxdh,clpp,hpys,clxh,cllx,csys,clsbdh," +
				"fdjh,cltz,clsyr,syrlxdh,syrxxdz,bkr,bkrjh,bkrmc,bkjg,bkjgmc,bkjglxdh,to_char(bksj,'yyyy-mm-dd hh24:mi:ss') bksj,czr,czrjh," +
				"czrmc,czrdw,czrdwmc,to_char(czsj,'yyyy-mm-dd hh24:mi:ss') czsj,czjg,ms,cxsqr,cxsqrjh,cxsqrmc,cxsqdw,cxsqdwmc,to_char(cxsqsj,'yyyy-mm-dd hh24:mi:ss') cxsqsj,ckyydm," +
				"ckyyms,ckczr,ckczrjh,ckczrmc,ckczrdw,ckczrdwmc,to_char(ckczsj,'yyyy-mm-dd hh24:mi:ss') ckczsj,ckczjg,ckms,ywzt,jlzt,gxsj,xxly," +
				"bkpt,bjzt,bjsj,by1,by2,by3,by4,by5 " +
				" FROM jz_veh_suspinfo " +
				" WHERE gxsj>to_date('"+t_kssj+"','yyyymmddhh24miss')-30 and " +
				" gxsj<to_date('"+t_kssj+"','yyyymmddhh24miss') order by gxsj";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(v_sql);
		if(list.size()>0){
			for (Iterator<Map<String, Object>> it = list.iterator(); it.hasNext();) {
				Map<String, Object> obj = (Map<String, Object>) it.next();
				if(obj.get("jyaq").toString().length()>512){
					t_jyaq = obj.get("jyaq").toString().substring(0, 512)+"...";
				}else {
					t_jyaq = obj.get("jyaq").toString();
				}
				if(obj.get("hpzl").toString().length()!=2){
					continue;
				}
				if(obj.get("hphm").toString().length()<6 || obj.get("hphm").toString().length()>10){
					continue;
				}
				if(obj.get("clsbdh").toString().length()>28){
					t_clsbdh = obj.get("clsbdh").toString().substring(0, 28)+"...";
				}else {
					t_clsbdh = obj.get("clsbdh").toString();
				}
				if(obj.get("fdjh").toString().length()>28){
					t_fdjh = obj.get("fdjh").toString().substring(0, 28)+"...";
				}else {
					t_fdjh = obj.get("fdjh").toString();
				}
				//判断是否需要在系统布控(漏掉的布控记录)
				if(obj.get("jlzt").toString().equals("1")){
					String c_sql = "select count(1) from veh_suspinfo " +
							" where ysbh='"+obj.get("ysbh").toString()+"' and hphm='"+obj.get("hphm").toString()+"'";
					int count = this.jdbcTemplate.queryForInt(c_sql);
					if(count==0){
						String seq_sql = "select '"+t_bkfw+"'||seq_suspinfo_xh.nextval from dual";
						String t_bkxh = (String) this.jdbcTemplate.queryForObject(seq_sql, String.class);
						
						String i_sql1 = "Insert Into veh_suspinfo (bkxh,ysbh,hphm,hpzl,bkdl,bklb,bkqssj,bkjzsj,jyaq,bkfwlx,bkfw,bkjb," +
								"bkxz,sqsb,bjya,bjfs,dxjshm,lar,ladw,ladwlxdh,clpp,hpys,clxh,cllx,csys,clsbdh,fdjh,cltz,clsyr,syrlxdh," +
								"syrxxdz,bkr,bkjg,bkjgmc,bkjglxdh,bksj,cxsqr,cxsqdw,cxsqdwmc,cxsqsj,ckyydm,ckyyms,ywzt,jlzt,gxsj,xxly," +
								"bkpt,by1,by2,by3,by4,by5,bjzt,mhbkbj,cxsqrjh,bkrjh,bjsj,cxsqrmc,bkrmc) Values " +
								"('"+t_bkxh+"','"+obj.get("ysbh").toString()+"','"+obj.get("hphm").toString()+"','"+obj.get("hpzl").toString()+"','1','06',to_date('"+obj.get("bkqssj").toString()+"','yyyy-mm-dd hh24:mi:ss'),to_date('"+obj.get("bkjzsj")+"','yyyy-mm-dd hh24:mi:ss'),'"+t_jyaq+"','1','"+t_bkfw+"'," +
								"'2','1','0','1','0111','','"+obj.get("lar").toString()+"','"+obj.get("ladw").toString()+"','"+obj.get("ladwlxdh").toString()+"','"+obj.get("clpp").toString()+"','"+((obj.get("hpys") == null) ? "" : obj.get("hpys"))+"','"+obj.get("clxh").toString()+"'," +
								"'"+obj.get("cllx").toString()+"','"+obj.get("csys").toString()+"','"+t_clsbdh+"','"+t_fdjh+"','"+obj.get("cltz").toString()+"','"+obj.get("clsyr").toString()+"','"+obj.get("syrlxdh").toString()+"','"+obj.get("syrxxdz").toString()+"'," +
								"'"+obj.get("bkr").toString()+"','"+obj.get("bkjg").toString()+"','"+obj.get("bkjgmc").toString()+"','"+obj.get("bkjglxdh").toString()+"',to_date('"+obj.get("bksj").toString()+"','yyyy-mm-dd hh24:mi:ss'),'"+((obj.get("cxsqr") == null) ? "" : obj.get("cxsqr"))+"','"+((obj.get("cxsqdw") == null) ? "" : obj.get("cxsqdw"))+"','"+((obj.get("cxsqdwmc") == null) ? "" : obj.get("cxsqdwmc"))+"'," +
								"'"+((obj.get("cxsqsj") == null) ? "" : obj.get("cxsqsj"))+"','"+((obj.get("ckyydm") == null) ? "" : obj.get("ckyydm"))+"','"+((obj.get("ckyyms") == null) ? "" : obj.get("ckyyms"))+"','14','1',sysdate,'6','"+t_bkfw+"','"+((obj.get("by1") == null) ? "" : obj.get("by1"))+"','"+((obj.get("by2") == null) ? "" : obj.get("by2"))+"','"+((obj.get("by3") == null) ? "" : obj.get("by3"))+"','"+((obj.get("by4") == null) ? "" : obj.get("by4"))+"','"+((obj.get("by5") == null) ? "" : obj.get("by5"))+"'," +
								"'0','0','"+((obj.get("cxsqrjh") == null) ? "" : obj.get("cxsqrjh"))+"','"+obj.get("bkrjh").toString()+"','"+((obj.get("bjsj") == null) ? "" : obj.get("bjsj"))+"','"+((obj.get("cxsqrmc") == null) ? "" : obj.get("cxsqrmc"))+"','"+obj.get("bkrmc").toString()+"')";
						this.jdbcTemplate.update(i_sql1);
						
					    String i_sql2 = "Insert Into audit_approve (xh,bkxh,czr,czrdw,czrdwmc,czsj,czjg,ms,bzw,by1,by2,czrjh,czrmc) Values " +
					       "('"+t_bkfw+"'||seq_audit_xh.nextval,'"+t_bkxh+"','"+obj.get("czr").toString()+"','"+obj.get("czrdw").toString()+"','"+obj.get("czrdwmc").toString()+"',to_date('"+obj.get("czsj").toString()+"','yyyy-mm-dd hh24:mi:ss'),'1','"+obj.get("ms").toString()+"','2','"+((obj.get("by1") == null) ? "" : obj.get("by1"))+"','"+((obj.get("by2") == null) ? "" : obj.get("by2"))+"','"+obj.get("czrjh").toString()+"','"+obj.get("czrmc").toString()+"')";
					    this.jdbcTemplate.update(i_sql2);
					}
				}
				//判断是否需要在系统撤控(漏掉的撤控记录)
				if(obj.get("jlzt").toString().equals("2")){
					String c_sql = "select count(1) from veh_suspinfo " +
							" where ysbh='"+obj.get("ysbh").toString()+"' and hphm='"+obj.get("hphm").toString()+"' and jlzt!='2'";
					int count = this.jdbcTemplate.queryForInt(c_sql);
					if(count==1){
						String i_sql1 = "update veh_suspinfo set cxsqr='"+((obj.get("cxsqr") == null) ? "" : obj.get("cxsqr"))+"',cxsqrjh='"+((obj.get("cxsqrjh") == null) ? "" : obj.get("cxsqrjh"))+"',cxsqrmc='"+obj.get("cxsqrmc").toString()+"',cxsqdw='"+obj.get("cxsqdw").toString()+"',cxsqdwmc='"+((obj.get("cxsqdwmc") == null) ? "" : obj.get("cxsqdwmc"))+"',cxsqsj=to_date('"+obj.get("cxsqsj").toString()+"','yyyy-mm-dd hh24:mi:ss'),ckyydm='99',ywzt='99',jlzt='2',gxsj=sysdate "+
										" where ysbh='"+obj.get("ysbh").toString()+"' and hphm='"+obj.get("hphm").toString()+"'";
						this.jdbcTemplate.update(i_sql1);
						
						String b_sql = "select bkxh  from veh_suspinfo where ysbh='"+obj.get("ysbh").toString()+"' and hphm='"+obj.get("hphm").toString()+"'";
						String t_bkxh = this.jdbcTemplate.queryForObject(b_sql, String.class);
						String i_sql2 = "Insert Into audit_approve (xh,bkxh,czr,czrdw,czrdwmc,czsj,czjg,ms,bzw,by1,by2,czrjh,czrmc) Values " +
					       "('"+t_bkfw+"'||seq_audit_xh.nextval,'"+t_bkxh+"','"+obj.get("ckczr").toString()+"','"+((obj.get("ckczrdw") == null) ? "" : obj.get("ckczrdw"))+"','"+obj.get("ckczrdwmc").toString()+"',to_date('"+obj.get("ckczsj").toString()+"','yyyy-mm-dd hh24:mi:ss'),'1','"+obj.get("ckms").toString()+"','4','"+((obj.get("by1") == null) ? "" : obj.get("by1"))+"','"+((obj.get("by2") == null) ? "" : obj.get("by2"))+"','"+obj.get("ckczrjh").toString()+"','"+obj.get("ckczrmc").toString()+"')";
					    this.jdbcTemplate.update(i_sql2);
					}
					
				}
			}
		}
		
		String i_sql3 = "update JM_trans_schedule set bz=to_char(sysdate,'yyyymmddhh24miss') where rwbh='A4010'";
		this.jdbcTemplate.update(i_sql3);
		
	}

	public void updateSuspInfo(VehSuspinfo info) throws Exception {
		Map<String,String> defaultMap = new HashMap<String,String>();
		defaultMap.put("gxsj", "sysdate");
		PreSqlEntry preEntity = SqlUtils.getPreUpdateSqlByObject("veh_suspinfo", info, new HashMap<String,String>(), "bkxh");
		this.jdbcTemplate.update(preEntity.getSql(), preEntity.getValues().toArray());
	}
	
	/*
	 * yaowang
	 * 
	 */
	//更新布控表(针对撤控申请)
		public int updateSuspInfoForCancelcksq(VehSuspinfo info)throws Exception{
			String userdw = info.getCxsqdw();
			String bkjg = info.getBkjg();
			StringBuffer sqlCancel = null;
			if(!userdw.equals(bkjg)){//上级的人撤控申请，不走审核审批流程
				sqlCancel = new StringBuffer("UPDATE veh_suspinfo  set ywzt = '99' ,gxsj = sysdate ");
			}else{
					sqlCancel = new StringBuffer("UPDATE veh_suspinfo  set ywzt = '41' ,gxsj = sysdate ");
			}
			if(StringUtils.isNotBlank(info.getCkyydm()))
				sqlCancel.append(",ckyydm = '").append(info.getCkyydm()).append("' ");
			
			if(StringUtils.isNotBlank(info.getCkyyms()))
				sqlCancel.append(",ckyyms = '").append(info.getCkyyms()).append("' ");
			
			if(StringUtils.isNotBlank(info.getCxsqr()))
				sqlCancel.append(", cxsqr = '").append(info.getCxsqr()).append("' ");
			
			if(StringUtils.isNotBlank(info.getCxsqrjh()))
				sqlCancel.append(",cxsqrjh = '").append(info.getCxsqrjh()).append("' ");
			
			if(StringUtils.isNotBlank(info.getCxsqrmc()))
				sqlCancel.append(",cxsqrmc = '").append(info.getCxsqrmc()).append("' ");
			
			if(StringUtils.isNotBlank(info.getCxsqdw()))
				sqlCancel.append(", cxsqdw = '").append(info.getCxsqdw()).append("' ");
			
			if(StringUtils.isNotBlank(info.getCxsqdwmc()))
				sqlCancel.append(", cxsqdwmc = '").append(info.getCxsqdwmc()).append("' ");
			
			if(StringUtils.isBlank(info.getBy1())){
				sqlCancel.append(", by1 = to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')");
			}
			
			/**一键撤控的申请人的yhdh**/
			if(StringUtils.isNotBlank(info.getBy5())){
				sqlCancel.append(", by5 = '").append(info.getBy5()).append("'");
			}
			
			sqlCancel.append(", cxsqsj = sysdate  where bkxh = '").append(info.getBkxh())
			.append("' ");
			
			
			int i = this.jdbcTemplate.update(sqlCancel.toString());
			
			return i;
		}
	
}
