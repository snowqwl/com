package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.bean.SuitLicense;
import com.sunshine.monitor.system.analysis.dao.SuitLicenseScSDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Repository("suitLicenseScSDao")
public class SuitLicenseScSDaoImpl extends ScsBaseDaoImpl implements
		SuitLicenseScSDao {

	/**
	 * 套牌分析，查询一天内通过的车牌
	 * @param curPage
	 * @param total
	 * @param sj
	 * @return
	 */
	public Map getHphm(int curPage, int total, String sj,String tablename) {
		
		if(curPage == 1) {
			this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+tablename+"");
			String create_sql = "create table "+tablename+" ";
			for(int i = 0; i<24; i++) {
				//create_sql += " select hphm,hpzl from "+Constant.SCS_PASS_TABLE+" where hphm not in ('无牌','-','NoPlate','未识别牌','NA','未检测',' ','NOPLATE','*','拒识/无牌','--------','------------','车牌','未识别','11111111','-------','无牌 ','*******') and hphm is not null and hpzl is not null ";
				create_sql += " select hphm,hpzl from "+Constant.SCS_PASS_TABLE+" where hphm not in ("+Constant.SCS_NO_PLATE+") and hphm is not null and hpzl is not null ";
				if(i<10) {
					create_sql += " and gcsj >= '"+sj+" 0"+i+":00:00' and gcsj <= '"+sj+" 0"+i+":59:59'";
					//create_sql += " and gcsj >= '2014-05-01 0"+i+":00:00' and gcsj <= '2014-05-01 0"+i+":59:59'";
				} else {
					//create_sql += " and gcsj >= '2014-05-01 "+i+":00:00' and gcsj <= '2014-05-01 "+i+":59:59'";
					create_sql += " and gcsj >= '"+sj+" "+i+":00:00' and gcsj <= '"+sj+" "+i+":59:59'";
				}
				create_sql += " group by hphm,hpzl ";
				if(i<23) {
					create_sql += " union ";
				}
			}
			this.jdbcScsTemplate.update(create_sql);
		}
		return this.findPageForMap("select t.hphm,t.hpzl from t("+tablename+") ", curPage, total);
	}

	/**
	 * 套牌分析，根据号牌号码查询过车列表信息
	 * @param hphm
	 * @param hpzl
	 * @param kssj
	 * @param jssj
	 * @return
	 * @throws Exception 
	 */
	public List getListByHphm(String hphm, String hpzl, String kssj, String jssj) throws Exception {
		String sql = "select t.gcxh as GCXH,t.hphm as HPHM,t.kdbh as KDBH,t.gcsj as GCSJ,t.cllx as CLLX,t.hpzl as HPZL,t.fxbh as FXBH,t.hpys as HPYS,t.tp1 as TP1 from t("+Constant.SCS_PASS_TABLE+" :: 1 = 1 ";
		if (hphm != null && !"".equals(hphm)) {
			sql += " and hphm = '" + hphm + "'";
		}

		if (hpzl != null && !"".equals(hpzl)) {
			sql += " and hpzl = '" + hpzl + "'";
			 
		}
		if(kssj != null && !"".equals(kssj)) {
			sql += " and gcsj >= str_to_date('"+kssj+"','%Y-%m-%d %H:%i:%s') ";
			//sql += " and gcsj >= str_to_date('2014-08-09 00:00:00','%Y-%m-%d %H:%i:%s') ";
			
			
		}
		
		if(jssj != null && !"".equals(jssj)) {
			sql += " and gcsj <= str_to_date('"+jssj+"','%Y-%m-%d %H:%i:%s') ";
			//sql += " and gcsj <= str_to_date('2014-08-09 23:59:59','%Y-%m-%d %H:%i:%s') ";
		}
		sql += " ) order t.gcsj : desc ";
		//System.out.println(sql);
		return this.queryForList(sql, ScsVehPassrec.class);
	}

	public Map getCount(Map filter, String kssj, String jssj) {
		//获取当前时间戳，毫秒数
		Long current = new Date().getTime();
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+Constant.SCS_TEMP_PREFIX+"_suitcounttemp"+current);	
		String sql = " create table "+Constant.SCS_TEMP_PREFIX+"_suitcounttemp"+current;
		 sql += " select * from (select a.sj as sj,IFNULL(e.lpxy,0) as lpxy,IFNULL(f.lptp,0) as lptp,IFNULL(g.lpbtp,0) as lpbtp, "
			+ " IFNULL(h.lpwqr,0) as lpwqr,IFNULL(i.qtxy,0) as qtxy,IFNULL(j.qttp,0) as qttp,IFNULL(k.qtbtp,0) as qtbtp,IFNULL(l.qtwqr,0) as qtwqr, "
			+ " IFNULL(a.xyzs,0) as xyzs,IFNULL(b.tpzs,0) as tpzs,IFNULL(c.btpzs,0) as btpzs,IFNULL(d.wqrzs,0) as wqrzs from "
			+ " (select sj,count(1) as xyzs from (select fxsj as sj,hphm,hpzl from hn_jm_suit_license  group by fxsj,hphm,hpzl,hpys) ab group by sj) a left join "
			+ " (select sj,count(1) as tpzs from (select fxsj as sj,hphm,hpzl from hn_jm_suit_license  where zt ='1' group by fxsj,hphm,hpzl,hpys) ac group by sj)b on a.sj=b.sj "
			+ " left join (select sj,count(1) as btpzs from (select fxsj as sj, hphm,hpzl from hn_jm_suit_license  where zt ='2' group by fxsj,hphm,hpzl,hpys) ad group by sj)c on a.sj=c.sj "
			+ " left join (select sj,count(1) as wqrzs from (select fxsj as sj, hphm,hpzl from hn_jm_suit_license  where zt ='0' group by fxsj,hphm,hpzl,hpys) ae group by sj)d on a.sj=d.sj "
			+ " left join (select sj,count(1) as lpxy from (select fxsj as sj,  hphm,hpzl from hn_jm_suit_license  where hpzl = '02' group by fxsj,hphm,hpzl,hpys) af group by sj)e on a.sj=e.sj "
			+ " left join (select sj,count(1) as lptp from (select fxsj as sj,  hphm,hpzl from hn_jm_suit_license  where hpzl = '02' and zt ='1'  group by fxsj,hphm,hpzl,hpys)bc group by sj)f on a.sj=f.sj "
			+ " left join (select sj,count(1) as lpbtp from (select fxsj as sj, hphm,hpzl from hn_jm_suit_license  where hpzl = '02' and zt ='2' group by fxsj,hphm,hpzl,hpys) bd group by sj)g on a.sj=g.sj "
			+ " left join (select sj,count(1) as lpwqr from (select fxsj as sj, hphm,hpzl from hn_jm_suit_license  where hpzl = '02' and zt ='0' group by fxsj,hphm,hpzl,hpys) be group by sj)h on a.sj=h.sj "
			+ " left join (select sj,count(1) as qtxy from  (select fxsj as sj, hphm,hpzl from hn_jm_suit_license  where hpzl <> '02' group by  fxsj,hphm,hpzl,hpys) bf group by sj)i on a.sj=i.sj "
			+ " left join (select sj,count(1) as qttp from (select fxsj as sj, hphm,hpzl from hn_jm_suit_license  where hpzl <> '02' and zt ='1' group by fxsj,hphm,hpzl,hpys)cd group by sj)j on a.sj =j.sj "
			+ " left join (select sj,count(1) as qtbtp from (select fxsj as sj, hphm,hpzl from hn_jm_suit_license  where hpzl <> '02' and zt ='2' group by fxsj,hphm,hpzl,hpys) ce group by sj)k on a.sj=k.sj "
			+ " left join (select sj,count(1) as qtwqr from (select fxsj as sj, hphm,hpzl from hn_jm_suit_license  where hpzl <> '02' and zt ='0' group by fxsj,hphm,hpzl,hpys) cf group by sj)l on a.sj=l.sj "
			+ " ) de where 1=1";
	if (kssj != null && !"".equals(kssj)) {
		sql += " and sj >= '" + kssj+"'";
	}
	if (jssj != null && !"".equals(jssj)) {
		sql += " and sj <= '" + jssj+"'";
	}		
	sql += " order by sj desc ";
	this.jdbcScsTemplate.update(sql);
	//String select_sql = " select t.SJ,t.LPXY,t.LPTP,t.LPBTP,t.LPWQR,t.QTXY,t.QTTP,t.QTBTP,t.QTWQR,t.XYZS,t.TPZS,t.BTPZS,t.WQRZS  from t("+Constant.SCS_TEMP_PREFIX+"_suitcounttemp"+current+") order t.sj:desc ";
	String select_sql = " select date(t.SJ) as sj,sum(t.LPXY) as LPXY,sum(t.LPTP) as LPTP,sum(t.LPBTP) as LPBTP,sum(t.LPWQR) as LPWQR,sum(t.QTXY) as QTXY,sum(t.QTTP) as QTTP ,sum(t.QTBTP) as QTBTP,sum(t.QTWQR) as QTWQR,sum(t.XYZS) as XYZS,sum(t.TPZS) as TPZS,sum(t.BTPZS) as BTPZS,sum(t.WQRZS) as WQRZS  from t("+Constant.SCS_TEMP_PREFIX+"_suitcounttemp"+current+") group date(t.sj) order t.sj:desc ";
	Map<String, Object> map = this.findPageForMap(select_sql, Integer
			.parseInt(filter.get("curPage").toString()), Integer
			.parseInt(filter.get("pageSize").toString()));
	this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+Constant.SCS_TEMP_PREFIX+"_suitcounttemp"+current);	
	return map;
	}

	public Map getSuitCount(Map filter, String kssj, String jssj, String hpzl) {
		//获取当前时间戳，毫秒数
		Long current = new Date().getTime();
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+Constant.SCS_TEMP_PREFIX+"_suitcount_"+current);	
		String sql = " and zt = '1'";
		if (hpzl != null && !"".equals(hpzl)) {
			if(hpzl.startsWith("!")) {
				sql += " and hpzl <> '02' ";
			} else {
				sql += " and hpzl = '" + hpzl + "' ";
			}
		}
		if (kssj != null && !"".equals(kssj)) {
			sql += " and fxsj >= '" + kssj+ "'";
		}
		if (jssj != null && !"".equals(jssj)) {
			sql += " and fxsj <= '" + jssj+ "'";
		}
		sql = "create table "+Constant.SCS_TEMP_PREFIX+"_suitcount_"+current
		+ " select sj ,hphm,hpzl,hpys,xzqs,kks,cs from (select sj, hphm,hpzl,hpys, count(distinct gcxh) as cs,count(distinct kdbh) as kks, count(distinct substr(kdbh, 1, 6)) as xzqs from (select distinct gcxh1 as gcxh,kdbh1 as kdbh,fxsj as sj, hphm,hpzl,hpys  from hn_jm_suit_license where 1 = 1 "
				+ sql
				+ "union select distinct gcxh2 as gcxh,kdbh2 as kdbh,fxsj as sj, hphm,hpzl,hpys from hn_jm_suit_license where 1=1 "
				+ sql
				+ ") a group by sj, hphm, hpys, hpzl) aa order by cs desc ";
		//System.out.println(sql);
		
		this.jdbcScsTemplate.update(sql);
		String select_sql = " select t.SJ,t.HPHM,t.HPZL,t.HPYS,t.XZQS,t.KKS,t.CS from t("+Constant.SCS_TEMP_PREFIX+"_suitcount_"+current+") order t.cs:desc ";
		Map<String, Object> map = this.findPageForMap(select_sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+Constant.SCS_TEMP_PREFIX+"_suitcount_"+current);	
		return map;
	}

	public Map getSuitList(Map filter, String hphm, String hpzl, String sj,
			String zt) {
		String sql = "select t.gcxh1,t.gcxh2,t.hphm,t.fxsj,t.zt,t.hpys,t.kdbh1,t.kdbh2,t.fxbh1,t.fxbh2,t.gctp1,t.gctp2,t.gcsj1,t.gcsj2,t.zxjl,t.hpzl,t.sjfz,t.jssd from t(hn_jm_suit_license::1 = 1 ";
		if (hphm != null && !"".equals(hphm)) {
			sql += " and hphm = '" + hphm + "'";
		}
		if (hpzl != null && !"".equals(hpzl)) {
			sql += " and hpzl = '" + hpzl + "'";
		}
		if (sj != null && !"".equals(sj)) {
			sql += " and fxsj = '" + sj + "'";
		}
		if (zt != null && !"".equals(zt) && !"3".equals(zt)) {
			sql += " and zt = '" + zt + "'";
		}
		sql+= ")";
		 //System.out.println(sql);
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}

	public Map getSuitVehList(Map filter, SuitLicense suitLicense) {
		//获取当前时间戳，毫秒数
		Long current = new Date().getTime();
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+Constant.SCS_TEMP_PREFIX+"_suitvehlist_"+current);	
		String sql = "";
		if (suitLicense.getHphm() != null && !"".equals(suitLicense.getHphm())) {
			sql += " and hphm = '" + suitLicense.getHphm() + "' ";
		}

		if (suitLicense.getHpzl() != null && !"".equals(suitLicense.getHpzl())) {
			sql += " and hpzl = '" + suitLicense.getHpzl() + "' ";
		}

		if (suitLicense.getHpys() != null && !"".equals(suitLicense.getHpys())) {
			sql += " and hpys = '" + suitLicense.getHpys() + "' ";
		}

		if (suitLicense.getFxsj() != null && !"".equals(suitLicense.getFxsj())) {
			sql += " and fxsj = date('"+suitLicense.getFxsj()+ "')";
		}

		if (suitLicense.getZt() != null && !"".equals(suitLicense.getZt())) {
			sql += " and zt = '" + suitLicense.getZt() + "' ";
		}
		sql= "create table "+Constant.SCS_TEMP_PREFIX+"_suitvehlist_"+current
		+ " select hphm,hpzl,hpys,gcxh1 as gcxh,kdbh1 as kdbh,fxbh1 as fxbh,gcsj1 as gcsj,gctp1 as tp1 from hn_jm_suit_license where 1=1 "
				+ sql
				+ " union select hphm,hpzl,hpys,gcxh2 as gcxh,kdbh2 as kdbh,fxbh2 as fxbh,gcsj2 as gcsj,gctp2 as tp1 from hn_jm_suit_license where 1=1 "
				+ sql + " order by gcsj desc ";
		System.out.println(sql);
		this.jdbcScsTemplate.update(sql);
		String select_sql = " select t.HPHM,t.HPZL,t.HPYS,t.GCXH,t.KDBH,t.FXBH,t.GCSJ,t.TP1 from t("+Constant.SCS_TEMP_PREFIX+"_suitvehlist_"+current+") order t.gcsj:desc ";
		Map<String, Object> map = this.findPageForMap(select_sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+Constant.SCS_TEMP_PREFIX+"_suitvehlist_"+current);	
		return map;
	}

	public Map getXySuit(Map filter, String kssj, String jssj, String zt,
			String hpzl) {
		//获取当前时间戳，毫秒数
		Long current = new Date().getTime();
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+Constant.SCS_TEMP_PREFIX+"_xysuit_"+current);	
		String consql = "";
		if (hpzl != null && !"".equals(hpzl)) {
			if(hpzl.startsWith("!")) {
				consql += " and hpzl <> '02' ";
			} else {
				consql += " and hpzl = '" + hpzl + "' ";
			}
		}
		
		if (kssj != null && !"".equals(kssj)) {
			consql += " and fxsj >= '" + kssj + "'";
		}
		if (jssj != null && !"".equals(jssj)) {
			consql += " and fxsj <= '" + jssj + "'";
		}
		if (zt != null && !"".equals(zt) && !"3".equals(zt)) {
			consql += " and zt = '" + zt + "'";
		}
		String sql = "create table "+Constant.SCS_TEMP_PREFIX+"_xysuit_"+current
            + " select a.sj,a.hphm,a.hpzl,a.hpys,a.zt,a.kks,a.xzqs,b.xyzs from ( "
			+"select fxsj as sj,hphm,hpzl,hpys,zt,count(distinct kdbh) as kks,count(distinct substr(kdbh,1,6)) as xzqs,count(hphm) as xyzs from (select hphm,hpzl,hpys,kdbh1 as kdbh,zt,fxsj from hn_jm_suit_license union select hphm,hpzl,hpys,kdbh2 as kdbh,zt,fxsj from hn_jm_suit_license ) ab where 1=1 "
			+consql
			+" group by fxsj, hphm, hpys, hpzl, zt) a "
			+" left join (select fxsj as sj, hphm, hpzl, hpys, zt, count(1) as xyzs from hn_jm_suit_license where 1 = 1 "
			+consql
			+" group by date(fxsj), hphm, hpys, hpzl, zt) b "
			+" on a.sj=b.sj and a.hphm=b.hphm and a.hpzl = b.hpzl and a.hpys = b.hpys "
			+" order by xyzs desc "
			;
		System.out.println(sql);
		this.jdbcScsTemplate.update(sql);
		String select_sql = "select t.SJ,t.HPHM,t.HPZL,t.HPYS,t.ZT,t.KKS,t.XZQS,t.XYZS from t("+Constant.SCS_TEMP_PREFIX+"_xysuit_"+current+") order t.xyzs:desc ";
		Map<String, Object> map = this.findPageForMap(select_sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+Constant.SCS_TEMP_PREFIX+"_xysuit_"+current);	
		return map;
	}

	public void insertSuitLicense(SuitLicense sl) {
		String sql = " insert into hn_jm_suit_license(gcxh1,gcxh2,hphm,hpzl,hpys,kdbh1,kdbh2,fxbh1,fxbh2,gcsj1,gcsj2,gctp1,gctp2,fxsj,zt,zxjl,sjfz,jssd)"
			+ " values('"
			+ sl.getGcxh1()
			+ "','"
			+ sl.getGxcxh2()
			+ "','"
			+ sl.getHphm()
			+ "','"
			+ sl.getHpzl()
			+ "','"
			+ sl.getHpys()
			+ "','"
			+ sl.getKdbh1()
			+ "','"
			+ sl.getKdbh2()
			+ "','"
			+ sl.getFxbh1()
			+ "','"
			+ sl.getFxbh2()
			+ "','"
			+ sl.getGcsj1()
			+ "','"
			+ sl.getGcsj2()
			+ "','"
			+ sl.getGctp1()
			+ "','"
			+ sl.getGctp2()
			+ "',date_sub(curdate(),interval 1 day),'" 
			+ sl.getZt() 
			+ "','"
			+ sl.getZxjl()
		    + "','"
		    + sl.getSjfz()
		    + "','"
		    +sl.getJssd()
			+"')";
    System.out.println(sql);
	this.jdbcScsTemplate.execute(sql);
		
	}

	public Map querySuitList(Map filter, VehPassrec veh) throws Exception {
		String sql = "select t.* from t(hn_jm_suit_license :: zt = '1' ";
		if(veh.getHphm()!=null && !"".equals(veh.getHphm())) {
			sql += " and t.hphm='"+veh.getHphm()+"'";
		}
		if(veh.getHpzl()!=null && !"".equals(veh.getHpzl())) {
			sql += " and t.hpzl='"+veh.getHpzl()+"'";
		}
		if(veh.getHpys()!=null && !"".equals(veh.getHpys())) {
			sql += " and t.hpys='"+veh.getHpys()+"'";
		}
		if(veh.getCsys()!=null && !"".equals(veh.getCsys())) {
			sql += " and t.csys='"+veh.getCsys()+"'";
		}
		if(veh.getKssj() != null && !"".equals(veh.getKssj())) {
			sql += " and t.gcsj1 >= '"+veh.getKssj()+"'";
			sql += " and t.gcsj2 >= '"+veh.getKssj()+"'";
		}
		if(veh.getJssj() != null && !"".equals(veh.getJssj())) {
			sql += " and t.gcsj1 <= '"+veh.getJssj()+"'";
			sql += " and t.gcsj2 <= '"+veh.getJssj()+"'";
		}
		sql += ") order t.fxsj:desc";
		Map map = this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}

	public void updateSuitLicense(SuitLicense suitLicense, String zt) {
		// TODO Auto-generated method stub
		String sql = "update t(hn_jm_suit_license::1=1";
		if(suitLicense.getHphm() != null && !"".equals(suitLicense.getHphm())) {
			sql += " and hphm = '" + suitLicense.getHphm() + "' ";
		}
		if(suitLicense.getHpzl() != null && !"".equals(suitLicense.getHpzl())) {
			sql += " and hpzl = '" + suitLicense.getHpzl() +"' ";
		}
		if(suitLicense.getHpys() != null && !"".equals(suitLicense.getHpys())) {
			sql += " and hpys = '" + suitLicense.getHpys() +"' ";
		}
		if(suitLicense.getFxsj() != null && !"".equals(suitLicense.getFxsj())) {
			sql += " and date(fxsj) = '" + suitLicense.getFxsj() +"'";
		}
		if(suitLicense.getZt() != null && !"".equals(suitLicense.getZt())) {
			sql += " and Zt = '" + suitLicense.getZt() +"' ";
		}
		sql+= ") set t.zt = '"+zt+"'";
		//System.out.println(sql);
		this.jdbcScsTemplate.update(sql);
	}

}
