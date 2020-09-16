package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.dao.WarningModelSCSDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
@Repository("warningSCSDao")
public class WarningModelSCSDaoImpl extends ScsBaseDaoImpl implements WarningModelSCSDao {
	
	private String getSql(Map filter, VehPassrec veh) throws Exception {
		String kssj = "";
		if(veh.getKssj()!=null&&veh.getKssj()!=""){
			kssj = " and gcsj >= to_date('"+veh.getKssj()+"','yyyy-mm-dd hh24:mi:ss')";
		}
		String jssj = "";
		if(veh.getJssj()!=null&&veh.getJssj()!=""){
			jssj = " and gcsj < to_date('"+veh.getJssj()+"','yyyy-mm-dd hh24:mi:ss')";
		}
		String xzqhdm = "";
		if(filter.get("xzqhdm").toString()!=null&&filter.get("xzqhdm").toString()!=""){
			xzqhdm = "and position('"+filter.get("xzqhdm").toString()+"' in kdbh)>0";
		};
		String sql = "select * from (SELECT hphm,hpzl,gcxh,gcsj,hpys,kdbh,COUNT (1) OVER (PARTITION BY hphm, hpzl)  AS total FROM veh_passrec WHERE 1=1 "+
		xzqhdm+kssj+jssj+") b where b.total=1";
		//String sql2 = "select * from jm_veh_passrec where gcxh='4401000000001158585541' or gcxh='4401000000001158585347' or gcxh='4401000000001158584601'";
		return sql;
	}
	
	public Map queryWarningList(Map filter, VehPassrec veh) throws Exception {
		String sql = getSql(filter, veh);
		//String sql2 = "select * from jm_veh_passrec where gcxh='4401000000001158585541' or gcxh='4401000000001158585347' or gcxh='4401000000001158584601'";
		System.out.println("区域首次分析sql："+sql);
		Map map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
 		return map;
	}
	
	public List<Map<String, Object>> queryWarningListExt(Map filter, VehPassrec veh) throws Exception {
		String sql = getSql(filter, veh);
		System.out.println("区域首次分析sql："+sql);
		List<Map<String, Object>> list =super.getPageDatas(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
 		return list;
	}
	
	public int queryWarningListTotal(Map filter, VehPassrec veh) throws Exception {
		int total = 0;
		String kssj = "";
		if(veh.getKssj()!=null&&veh.getKssj()!=""){
			kssj = " and gcsj >= to_date('"+veh.getKssj()+"','yyyy-mm-dd hh24:mi:ss')";
		}
		String jssj = "";
		if(veh.getJssj()!=null&&veh.getJssj()!=""){
			jssj = " and gcsj < to_date('"+veh.getJssj()+"','yyyy-mm-dd hh24:mi:ss')";
		}
		String xzqhdm = "";
		if(filter.get("xzqhdm").toString()!=null&&filter.get("xzqhdm").toString()!=""){
			xzqhdm = "and position('"+filter.get("xzqhdm").toString()+"' in kdbh)>0";
		};
		String sql = "select * from (SELECT gcxh,COUNT (1) OVER (PARTITION BY hphm, hpzl)  AS total FROM veh_passrec WHERE 1=1 "+
		xzqhdm+kssj+jssj+") b where b.total=1";
		total =super.getTotal(sql);
 		return total;
	}

	public Map queryPass(Map filter) throws Exception {
		String gcxh = "";
		if(filter.get("gcxh").toString()!=null&&filter.get("gcxh").toString()!=""){
			gcxh = " and gcxh='"+filter.get("gcxh").toString()+"'";
		}
		String sql = "select gcxh,kdbh,cdbh,fxbh,hphm,hpzl,hpys,csys,gcsj,tp1,tp2,tp3 from veh_passrec where 1=1"+gcxh;
		System.out.println("查询区域首次车辆信息sql："+sql);
		Map map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
 		return map;
	}
	
	public Map queryOneIllegal(Map filter) throws Exception {
		String hphm = "";
		if(filter.get("hphm")!=null&&filter.get("hphm").toString()!=""){
			hphm = filter.get("hphm").toString();
		}
		String hpzl = "";
		if(filter.get("hpzl")!=null&&filter.get("hpzl").toString()!=""){
			hpzl = filter.get("hpzl").toString();
		}
		String sql = "SELECT dzwzid,wfbh,wfdz,wfsj,cljg,clsj,hphm,hpzl,jdcsyr,dsr,zsxxdz,cjjg,fkje,jdsbh,zqmj,lrr,lrsj FROM jm_zyk_illegal WHERE hphm = '"+hphm+"' and hpzl = '"+hpzl+"' order by gxsj desc";
		//System.out.println("查询区域首次车辆违法信息sql："+sql);
		Map map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
 		return map;
	}
	
	public Map queryKksbl(Map filter) throws Exception {
		String kdbh = "";
		if(filter.get("kdbh")!=null&&filter.get("kdbh").toString()!=""){
			kdbh = filter.get("kdbh").toString();
		}
		
		String sql = "SELECT SUM(NVL(CSTR_COL03, 0)) GCSL,SUM(NVL(CSTR_COL04, 0)) SBSL FROM monitor.T_MONITOR_JG_INTEGRATE_DETAIL  WHERE KHFA_ID = '35' and CSTR_COL01 ='"+kdbh+"'";
		Map map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
 		return map;
	}

}
