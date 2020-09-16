package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.SuitLicense;
import com.sunshine.monitor.system.analysis.bean.WhilteName;
import com.sunshine.monitor.system.analysis.dao.XySuitLicenseDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Repository("xySuitLicenseDao")
public class XySuitLicenseDaoImpl extends BaseDaoImpl implements XySuitLicenseDao {


	@Override
	public int insertWhite(WhilteName wn) {
		String sql="insert into jm_zyk_xy_white (hphm,hpzl,ctime) values(?,?,sysdate) ";
		int i=this.jdbcTemplate.update(sql, new Object[]{wn.getHphm(),wn.getHpzl()}); 
		return i;
	}

	@Override
	public int isHaveWhite(String hphm,String hpzl) {
		String sql="select hphm from jm_zyk_xy_white where hphm='"+hphm+"' and hpzl ='"+hpzl+"'  ";
		return this.getRecordCounts(sql.toString(),0);
	}

	@Override
	public int deleteWhite(String hphm,String hpzl) {
		String sql="delete from jm_zyk_xy_white where hphm =? and hpzl =? ";
		int i=this.jdbcTemplate.update(sql, new Object[]{hphm,hpzl}); 
		return i;
	}
	
	public List<Map>  queryhpzl() throws Exception{
		List<Map>  list=new ArrayList<Map>();
		String sql="select dmz ,  dmsm1 from frm_code where dmlb='030107' and zt!='2'  ";
		Object[] params = null;
		Map<String, Object> map = findPageForMap(sql.toString(),0, 100);
		list=(List<Map>) map.get("rows");
		return list;
	}
	
	public Map<String, Object> whitelist(Map<String, Object> conditions) {
		StringBuffer sql = new StringBuffer();
		sql.append("select  *  from  jm_zyk_xy_white where 1=1 ");
		if(conditions.containsKey("hphm") && conditions.get("hphm")!=null &&  conditions.get("hphm")!=""){
			sql.append("and hphm like '%"+conditions.get("hphm").toString()+"%' ");
		}
		if(conditions.containsKey("hpzl") && conditions.get("hpzl")!=null &&  conditions.get("hpzl")!=""){
			sql.append("and hpzl ='"+conditions.get("hpzl").toString()+"' ");
		}
		sql.append("order by ctime desc ");
		Map<String, Object> map = findPageForMap(sql.toString(), Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}
	
	public Map getHphm(int curPage, int pageSize, String kssj, String jssj) {
		String sql = " select hphm,hpzl from veh_passrec where hphm <> '无牌' ";
		if (kssj != null && !"".equals(kssj)) {
			sql += " and gcsj >= to_date('" + kssj
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}

		if (jssj != null && !"".equals(jssj)) {
			sql += " and gcsj <= to_date('" + jssj
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}
		sql += " group by hphm,hpzl order by hphm ";

		return this.findPageForMap(sql, curPage, pageSize);
	}

	public List getListByHphm(String hphm, String hpzl, String kssj, String jssj) {
		String sql = " select gcxh,hphm,kdbh,gcsj,cllx,hpzl,fxbh,tp1 from veh_passrec where 1 = 1";
		if (hphm != null && !"".equals(hphm)) {
			sql += " and hphm = '" + hphm + "'";
		}

		if (hpzl != null && !"".equals(hpzl)) {
			sql += " and hpzl = '" + hpzl + "'";
		}

		if (kssj != null && !"".equals(kssj)) {
			sql += " and gcsj >= to_date('" + kssj
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}

		if (jssj != null && !"".equals(jssj)) {
			sql += " and gcsj <= to_date('" + jssj
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}
		sql += " order by gcsj desc";
		return this.jdbcTemplate.queryForList(sql);
	}

	public void insertSuitLicense(SuitLicense sl) {
		String sql = " insert into hn_jm_suit_license(gcxh1,gcxh2,hphm,hpzl,hpys,kdbh1,kdbh2,fxbh1,fxbh2,gcsj1,gcsj2,gctp1,gctp2,rksj,zt,zxjl,sjfz,jssd)"
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
			+ "',to_date('"
			+ sl.getGcsj1()
			+ "','yyyy-mm-dd hh24:mi:ss'),to_date('"
			+ sl.getGcsj2()
			+ "','yyyy-mm-dd hh24:mi:ss'),'"
			+ sl.getGctp1()
			+ "','"
			+ sl.getGctp2()
			+ "',trunc(sysdate-1),'" 
			+ sl.getZt() 
			+ "','"
			+ sl.getZxjl()
		    + "','"
		    + sl.getSjfz()
		    + "','"
		    +sl.getJssd()
			+"')";
    //System.out.println(sql);
	this.jdbcTemplate.execute(sql);

}

	public Map getCount(Map filter, String kssj, String jssj) {
		String sql = " select * from (select a.sj as sj,nvl(e.lpxy,0) as lpxy,nvl(f.lptp,0) as lptp,nvl(g.lpbtp,0) as lpbtp, "
				+ " nvl(h.lpwqr,0) as lpwqr,nvl(i.qtxy,0) as qtxy,nvl(j.qttp,0) as qttp,nvl(k.qtbtp,0) as qtbtp,nvl(l.qtwqr,0) as qtwqr, "
				+ " nvl(a.xyzs,0) as xyzs,nvl(b.tpzs,0) as tpzs,nvl(c.btpzs,0) as btpzs,nvl(d.wqrzs,0) as wqrzs from "
				+ " (select sj,count(1) as xyzs from (select trunc(fxsj) as sj,hphm,hpzl from hn_jm_suit_license  group by trunc(fxsj),hphm,hpzl) group by sj) a left join "
				+ " (select sj,count(1) as tpzs from (select trunc(fxsj) as sj,hphm,hpzl from hn_jm_suit_license  where zt ='1' group by trunc(fxsj),hphm,hpzl) group by sj)b on a.sj=b.sj "
				+ " left join (select sj,count(1) as btpzs from (select trunc(fxsj) as sj, hphm,hpzl from hn_jm_suit_license  where zt ='2' group by trunc(fxsj),hphm,hpzl) group by sj)c on a.sj=c.sj "
				+ " left join (select sj,count(1) as wqrzs from (select trunc(fxsj) as sj, hphm,hpzl from hn_jm_suit_license  where zt ='3' group by trunc(fxsj),hphm,hpzl) group by sj)d on a.sj=d.sj "
				+ " left join (select sj,count(1) as lpxy from (select trunc(fxsj) as sj,  hphm,hpzl from hn_jm_suit_license  where hpzl = '02' group by trunc(fxsj),hphm,hpzl) group by sj)e on a.sj=e.sj "
				+ " left join (select sj,count(1) as lptp from (select trunc(fxsj) as sj,  hphm,hpzl from hn_jm_suit_license  where hpzl = '02' and zt ='1'  group by trunc(fxsj),hphm,hpzl) group by sj)f on a.sj=f.sj "
				+ " left join (select sj,count(1) as lpbtp from (select trunc(fxsj) as sj, hphm,hpzl from hn_jm_suit_license  where hpzl = '02' and zt ='2' group by trunc(fxsj),hphm,hpzl) group by sj)g on a.sj=g.sj "
				+ " left join (select sj,count(1) as lpwqr from (select trunc(fxsj) as sj, hphm,hpzl from hn_jm_suit_license  where hpzl = '02' and zt ='3' group by trunc(fxsj),hphm,hpzl) group by sj)h on a.sj=h.sj "
				+ " left join (select sj,count(1) as qtxy from  (select trunc(fxsj) as sj, hphm,hpzl from hn_jm_suit_license  where hpzl <> '02' group by  trunc(fxsj),hphm,hpzl) group by sj)i on a.sj=i.sj "
				+ " left join (select sj,count(1) as qttp from (select trunc(fxsj) as sj, hphm,hpzl from hn_jm_suit_license  where hpzl <> '02' and zt ='1' group by trunc(fxsj),hphm,hpzl) group by sj)j on a.sj =j.sj "
				+ " left join (select sj,count(1) as qtbtp from (select trunc(fxsj) as sj, hphm,hpzl from hn_jm_suit_license  where hpzl <> '02' and zt ='2' group by trunc(fxsj),hphm,hpzl) group by sj)k on a.sj=k.sj "
				+ " left join (select sj,count(1) as qtwqr from (select trunc(fxsj) as sj, hphm,hpzl from hn_jm_suit_license  where hpzl <> '02' and zt ='3' group by trunc(fxsj),hphm,hpzl) group by sj)l on a.sj=l.sj "
				+ " ) where 1=1";
		if (kssj != null && !"".equals(kssj)) {
			sql += " and sj >= to_date('" + kssj
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}
		if (jssj != null && !"".equals(jssj)) {
			sql += " and sj <= to_date('" + jssj
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}		
		sql += " order by sj desc ";
		//System.out.println(sql);
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}

	public Map getSuitCount(Map filter, String kssj, String jssj, String hpzl) {
		String sql = " and zt = '1'";
		if (hpzl != null && !"".equals(hpzl)) {
			if(hpzl.startsWith("!")) {
				sql += " and hpzl <> '02' ";
			} else {
				sql += " and hpzl = '" + hpzl + "' ";
			}
		}
		if (kssj != null && !"".equals(kssj)) {
			sql += " and fxsj >= to_date('" + kssj
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}
		if (jssj != null && !"".equals(jssj)) {
			sql += " and fxsj <= to_date('" + jssj
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}

		sql = "select a.sj ,a.hphm,a.hpzl,a.hpys,b.xzqs,b.kks,a.cs from (select sj, hphm,hpzl,hpys, count(distinct gcxh) as cs from (select distinct gcxh1 as gcxh, trunc(fxsj) as sj, hphm,hpzl,hpys  from hn_jm_suit_license where 1 = 1 "
				+ sql
				+ "union select distinct gcxh2 as gcxh, trunc(fxsj) as sj, hphm,hpzl,hpys from hn_jm_suit_license where 1=1 "
				+ sql
				+ ") group by sj, hphm, hpys, hpzl)a left join  ( select trunc(fxsj) as sj, hphm, hpzl, hpys, count(distinct kdbh) as kks, count(distinct substr(kdbh, 1, 6)) as xzqs  from (select hphm, hpzl, hpys, kdbh1 as kdbh,fxsj,zt from hn_jm_suit_license  union select hphm, hpzl, hpys, kdbh2 as kdbh,fxsj,zt from hn_jm_suit_license) where 1 = 1 "
				+ sql
				+ " group by trunc(fxsj), hphm, hpys, hpzl) b on a.sj = b.sj and a.hphm = b.hphm and a.hpzl = b.hpzl order by a.cs desc ";
		//System.out.println(sql);
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}

	public Map getXySuit(Map filter, String kssj, String jssj, String zt, String hpzl) {
		String consql = "";
		if (hpzl != null && !"".equals(hpzl)) {
			if(hpzl.startsWith("!")) {
				consql += " and hpzl <> '02' ";
			} else {
				consql += " and hpzl = '" + hpzl + "' ";
			}
		}
		
		if (kssj != null && !"".equals(kssj)) {
			consql += " and fxsj >= to_date('" + kssj
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}
		if (jssj != null && !"".equals(jssj)) {
			consql += " and fxsj <= to_date('" + jssj
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}
		if (zt != null && !"".equals(zt) && !"0".equals(zt)) {
			consql += " and zt = '" + zt + "'";
		}
		String sql = "select a.sj,a.hphm,a.hpzl,a.hpys,a.zt,a.kks,a.xzqs,b.xyzs from ( "
			+"select trunc(fxsj) as sj,hphm,hpzl,hpys,zt,count(distinct kdbh) as kks,count(distinct substr(kdbh,1,6)) as xzqs,count(hphm) as xyzs from (select hphm,hpzl,hpys,kdbh1 as kdbh,zt,fxsj from hn_jm_suit_license union select hphm,hpzl,hpys,kdbh2 as kdbh,zt,fxsj from hn_jm_suit_license) where 1=1 "
			+consql
			+" group by trunc(fxsj), hphm, hpys, hpzl, zt) a "
			+" left join (select trunc(fxsj) as sj, hphm, hpzl, hpys, zt, count(*) as xyzs from hn_jm_suit_license where 1 = 1 "
			+consql
			+" group by trunc(fxsj), hphm, hpys, hpzl, zt) b "
			+" on a.sj=b.sj and a.hphm=b.hphm and a.hpzl = b.hpzl and a.hpys = b.hpys "
			+" order by xyzs desc "
			;
		//System.out.println(sql);
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}

	public void updateSuitLicense(Map fifter, String zt) {
		String sql = "update jm_zyk_xy_suit set zt = '" + zt + "'";
		if(fifter.get("ypyj")!=null && !"".equals(fifter.get("ypyj").toString())) {
			sql+=",ypyj='"+fifter.get("ypyj").toString()+"'";
		}
		sql+=" where 1=1";
		if((String)fifter.get("hphm") != null && !"".equals(fifter.get("hphm").toString())) {
			sql += " and hphm = '" + fifter.get("hphm").toString() + "' ";
		}
		if((String)fifter.get("hpzl") != null && !"".equals(fifter.get("hpzl").toString())) {
			sql += " and hpzl = '" + fifter.get("hpzl").toString() +"' ";
		}
//		if((String)fifter.get("tp1") != null && !"".equals(fifter.get("tp1").toString())) {
//			sql += " and tp1 = '" + fifter.get("tp1").toString() +"' ";
//		}
		if((String)fifter.get("hpys") != null && !"".equals(fifter.get("hpys").toString())) {
			sql += " and hpys = '" + fifter.get("hpys").toString() +"' ";
		}
		if((String)fifter.get("gcxh1") != null && !"".equals(fifter.get("gcxh1").toString())) {
			sql += " and gcxh1 = '" + fifter.get("gcxh1").toString() +"' ";
		}
		if((String)fifter.get("gcxh2") != null && !"".equals(fifter.get("gcxh2").toString())) {
			sql += " and gcxh2 = '" + fifter.get("gcxh2").toString() +"' ";
		}
		
		this.jdbcTemplate.update(sql);
		
	}

	public Map getSuitVehList(Map filter, SuitLicense suitLicense) {
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
			sql += " and trunc(fxsj) = to_date('" + suitLicense.getFxsj()
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}

		if (suitLicense.getZt() != null && !"".equals(suitLicense.getZt())) {
			sql += " and zt = '" + suitLicense.getZt() + "' ";
		}
		sql = "select hphm,hpzl,hpys,gcxh1 as gcxh,kdbh1 as kdbh,fxbh1 as fxbh,gcsj1 as gcsj,gctp1 as tp1 from hn_jm_suit_license where 1=1 "
				+ sql
				+ " union select hphm,hpzl,hpys,gcxh2 as gcxh,kdbh2 as kdbh,fxbh2 as fxbh,gcsj2 as gcsj,gctp2 as tp1 from hn_jm_suit_license where 1=1 "
				+ sql + " order by gcsj desc ";
		//System.out.println(sql);
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}

	public Map querySuitList(Map filter,VehPassrec veh) throws Exception {
		String hpys="";
		if(veh.getHpys()!=null){
			if(!veh.getHpys().equals(""))
			hpys=" and t.hpys='"+veh.getHpys()+"'";
		}
		String csys="";
		if(veh.getCsys()!=null){
			if(!veh.getCsys().equals(""))
			csys=" and t.csys='"+veh.getCsys()+"'";
		}
		String hpzl="";
		if(veh.getHpzl()!=null){
			if(!veh.getHpzl().equals(""))
			hpzl=" and t.hpzl='"+veh.getHpzl()+"'";
		}
		String hphm ="";
		String zt=" and zt='1'";
		if(veh.getHphm()!=null){
			if(!veh.getHphm().equals("")){
				hphm=" and t.hphm='"+veh.getHphm()+"'";
				//zt="";
			}
		}
		String beginkssj1 =" and t.gcsj1 > to_date('"+veh.getKssj()+"','yyyy-mm-dd hh24:mi:ss')";
		String endkssj1 =" and t.gcsj1 <= to_date('"+veh.getJssj()+"','yyyy-mm-dd hh24:mi:ss')";
		String beginkssj2 =" and t.gcsj2 > to_date('"+veh.getKssj()+"','yyyy-mm-dd hh24:mi:ss')";
		String endkssj2 =" and t.gcsj2 <= to_date('"+veh.getJssj()+"','yyyy-mm-dd hh24:mi:ss')";
		if(veh.getKssj()==null||veh.getKssj().equals("")){
			beginkssj1="";
			beginkssj2="";
		}
		if(veh.getJssj()==null||veh.getJssj().equals("")){
			endkssj1="";
			endkssj2="";
		}
		
		
		String sql = " select * from hn_jm_suit_license t where 1=1  "
			+hpys
			+csys
			+hpzl
			+hphm
			+zt
			+beginkssj1
		    +endkssj1
		    +beginkssj2
		    +endkssj2
		    +" order by t.fxsj desc"
			  ;
		System.out.println(sql);
		return this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
	}

	@Override
	public Map getXySuit(Map filter, String kssj, String jssj, String zt,
			String hphm, String hpzl, String hpys) {
		String sql = "select hphm, hpys, hpzl, zt from jm_zyk_xy_suit where 1=1 ";
		if (kssj != null && !"".equals(kssj)) {
			sql += " and rksj >= to_date('" + kssj+ "','yyyy-mm-dd,hh24:mi:ss')";
		}
		if (jssj != null && !"".equals(jssj)) {
			sql += " and rksj <= to_date('" + jssj+ "','yyyy-mm-dd,hh24:mi:ss')";
		}
		if(zt != null && !"".equals(zt)){
			sql += " and zt = '" + zt+ "'";
		}
		if(hphm != null && !"".equals(hphm)){
			sql += " and hphm = '" + hphm+ "'";
		}
		if(hpzl != null && !"".equals(hpzl)){
			sql += " and hpzl = '" + hpzl+ "'";
		}
		if(hpys != null && !"".equals(hpys)){
			sql += " and hpys = '" + hpys+ "'";
		}
		sql += " order by rksj";
		Map<String,Object> map = this.findPageForMap(sql.toString(),
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return map;
	}

	public Map getSuitList(Map filter, String hphm, String hpzl, String kssj, String jssj, String zt) {
		String sql = "select * from jm_zyk_xy_suit where 1 = 1 ";
		if (hphm != null && !"".equals(hphm)) {
			sql += " and hphm = '" + hphm + "'";
		}
		if (hpzl != null && !"".equals(hpzl)) {
			sql += " and hpzl = '" + hpzl + "'";
		}
		if (kssj != null && !"".equals(kssj)) {
			sql += " and trunc(rksj) >= to_date('" + kssj + "','yyyy-mm-dd')";
		}
		if (jssj != null && !"".equals(jssj)) {
			sql += " and trunc(rksj) <= to_date('" + jssj + "','yyyy-mm-dd')";
		}
		if (zt != null && !"".equals(zt)) {
			sql += " and zt = '" + zt + "'";
		}
		 System.out.println("语句"+sql);
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}
	
	public static String change(String [] sa) {
		StringBuffer ss=new StringBuffer();
		ss.append(" sbjg in (");
		for (int i = 0; i < sa.length; i++) {
			if(i==0){
				ss.append("'"+sa[i]+"'");
			}else{
				ss.append(",'"+sa[i]+"'");
			}
		}
		ss.append(" )");
		return ss.toString();
	}
	
	private  PropertiesConfiguration config;
	
	public String getXySql(Map<String, Object> filter, String kssj, String jssj, String zt,
			String hphm, String hpzl, String hpys) throws Exception{
		//从配置里读取查询识别结果种类
		config = new PropertiesConfiguration("ipport.properties");
		String Picswitch=config.getString("deck.show");
		String s1="";
		if(Picswitch!=null && !"".equals(Picswitch)){
			if(Picswitch.indexOf(",")>-1){
				String [] sa=Picswitch.split(",");
				s1=change(sa);
			}else{
				s1=" and sbjg='"+Picswitch+"' ";
			}
		}
		
		String sql = "select  /*+index(t INDX_JM_ZYK_XY_SUIT_GCSJ) */hphm, hpys, hpzl,zt ,rksj,minkm ,gcxh1,gcxh2,xzqh1,xzqh2,sbjg,ypyj from jm_zyk_xy_suit where 1=1 ";
		if (kssj != null && !"".equals(kssj)) {
			sql += " and rksj >= to_date('" + kssj+ "','yyyy-mm-dd,hh24:mi:ss')";
		}
		if (jssj != null && !"".equals(jssj)) {
			sql += " and rksj <= to_date('" + jssj+ "','yyyy-mm-dd,hh24:mi:ss')";
		}
		if(zt != null && !"".equals(zt)){
			sql += " and zt = '" + zt+ "'";
		}
		if(hphm != null && !"".equals(hphm)){
			sql += " and hphm = '" + hphm+ "'";
		}
		if(hpzl != null && !"".equals(hpzl)){
			sql += " and hpzl = '" + hpzl+ "'";
		}
		if(hpys != null && !"".equals(hpys)){
			sql += " and hpys = '" + hpys+ "'";
		}
		if(filter.get("sfks") != null){
			//跨市
			if(filter.get("sfks").equals("0"))
				sql += " and substr(kdbh1, 0, 4) <> substr(kdbh2, 0, 4)";
			//非跨市
			if(filter.get("sfks").equals("1"))
				sql += " and substr(kdbh1, 0, 4) = substr(kdbh2, 0, 4)";
		}
		if(filter.get("szsx")!=null && !"".equals(filter.get("szsx").toString())) {
			sql+=" and (xzqh1 = '"+filter.get("szsx").toString().substring(0, 4)+"' or xzqh2 = '"+filter.get("szsx").toString().substring(0, 4)+"')";
		}
		sql +=s1;
		sql +=" and length(hphm)<8 ";

		/**
		 * 2019年7月3日 by chujiem ,注释掉sql后半段条件
		 */

		//sql +=" and ( (hpys1 is null and hpys2 is null) or (hpys1='0' or hpys2='0' ) or (hpys1=hpys2 and hpys1!=null ) ) ";

		sql += " and hphm not in(select  hphm from jm_zyk_xy_white) order by rksj desc";
		return sql;
	}

	@Override
	public List<Map<String,Object>> getXySuitExt(Map<String, Object> filter,
			String kssj, String jssj, String zt, String hphm, String hpzl,
			String hpys) throws Exception {
		String sql = getXySql(filter, kssj, jssj, zt, hphm, hpzl, hpys);
		List<Map<String,Object>> list = this.getPageDatas(sql,Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return list;
	}

	@Override
	public int getXySuitTotal(Map<String, Object> filter, String kssj,
			String jssj, String zt, String hphm, String hpzl, String hpys)
			throws Exception {
		String sql = getXySql(filter, kssj, jssj, zt, hphm, hpzl, hpys);
		return super.getTotal(sql);
	}

	@Override
	public Map<String, Object> getXzqhmc(String xzqh) {
		String sql="select xzqhmc from frm_xzqh where 1=1 and xzqhdm=?";
		List<String> list=new ArrayList<String>();
		if(xzqh.length()==4) {
			list.add(xzqh+"00");
		}
		if(xzqh.length()>4) {
			list.add(xzqh);
		}
		Map<String, Object> map = this.jdbcTemplate.queryForMap(sql, list.toArray());
		return map;
	}

	@Override
	public List<Map<String, Object>> getSz() {
		String sql="select xzqhdm,xzqhmc from frm_xzqh where 1=1 and length(xzqhdm)=6 and jb='3'";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;
	}

	@Override
	public Map<String, Object> getYpyj(Map<String, Object> fifter) {
		Map<String, Object> map=new HashMap<String,Object>();
		String sql = "select ypyj from jm_zyk_xy_suit where 1=1 ";
		if((String)fifter.get("hphm") != null && !"".equals(fifter.get("hphm").toString())) {
			sql += " and hphm = '" + fifter.get("hphm").toString() + "' ";
		}
		if((String)fifter.get("hpzl") != null && !"".equals(fifter.get("hpzl").toString())) {
			sql += " and hpzl = '" + fifter.get("hpzl").toString() +"' ";
		}
		if((String)fifter.get("hpys") != null && !"".equals(fifter.get("hpys").toString())) {
			sql += " and hpys = '" + fifter.get("hpys").toString() +"' ";
		}
		if((String)fifter.get("gcxh1") != null && !"".equals(fifter.get("gcxh1").toString())) {
			sql += " and gcxh1 = '" + fifter.get("gcxh1").toString() +"' ";
		}
		if((String)fifter.get("gcxh2") != null && !"".equals(fifter.get("gcxh2").toString())) {
			sql += " and gcxh2 = '" + fifter.get("gcxh2").toString() +"' ";
		}
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		if(list.size()>0) {
			map=list.get(0);
		}
		return map;
	}

}
