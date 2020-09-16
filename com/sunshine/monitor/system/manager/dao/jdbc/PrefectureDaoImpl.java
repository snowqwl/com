package com.sunshine.monitor.system.manager.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.Prefecture;
import com.sunshine.monitor.system.manager.dao.PrefectureDao;

@Repository("prefectureDao")
public class PrefectureDaoImpl extends BaseDaoImpl implements PrefectureDao {
	
	public Map<String,Object> findPrefectureForMap(Map filter, Prefecture prefecture) {
		List param = new ArrayList<>();
		String sql= "";
		if ((prefecture.getDwdm() != null)
				&& (prefecture.getDwdm().length() > 0)) {
			sql = sql + " and dwdm=?";
			param.add(prefecture.getDwdm());
		}
		if(prefecture.getDwdmmc() != null
				&& (prefecture.getDwdmmc().length() > 0)) {
			sql = sql + " and d.bmmc like ?";
			param.add("%"+prefecture.getDwdmmc()+"%");
		}
		if ((prefecture.getXjjg() != null)
				&& (prefecture.getXjjg().length() > 0)) {
			sql = sql + " and xjjg=?";
			param.add(prefecture.getXjjg());
		}
		if ((prefecture.getJglx() != null)
				&& (prefecture.getJglx().length() > 0)) {
			sql = sql + " and jglx=?";
			param.add(prefecture.getJglx());
		}
		if ((prefecture.getJgqz() != null)
				&& (prefecture.getJgqz().length() > 0)) {
			sql = sql + " and jgqz=?";
			param.add(prefecture.getJgqz());
		}
		
		sql = "select t.xjjg,d.bmmc xjjgmc,decode(t.jglx,'0','系统运算','用户配置') jglx,to_char(t.gxsj,'yyyy-mm-dd hh24:mi') gxsj" +
				" from frm_prefecture t,frm_department d where t.xjjg=d.glbm " + sql
				+ " order by dwdm,xjjg";
		Map<String,Object> map = this.findPageForMap(sql,param.toArray(),
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString())
		);
		
		return map;
	}
	
	public List getDepartmentTree(Map filter) {
		List l = new ArrayList();
		/*String sql = "select glbm,bmmc,jb,nvl(sjbm,0) sjbm ,py " +
				" from frm_department t ";
		if (!(filter.get("jb") == null || filter.get("jb").equals(""))) {
                sql +=" where jb= " +(Integer.parseInt(filter.get("jb").toString())-1); 
		} else {
			if (!(filter.get("glbm") == null || filter.get("glbm").equals(""))) {
				sql += " start with t.glbm like '" + filter.get("glbm")
						+ "%' connect by prior t.glbm=t.sjbm ";
			}
			if (!(filter.get("dep_name") == null || filter.get("dep_name")
					.equals(""))) {
				sql += " and t.dep_name like '%" + filter.get("dep_name")
						+ "%' ";
			}
		}*/
		List param = new ArrayList<>();
		String sql = "select distinct * from ( select xjjg as glbm,t.bmmc,jb,nvl(sjbm,0) sjbm,py from "+
					 " frm_prefecture p ,frm_department t where p.xjjg = t.glbm ";		
		if (!(filter.get("jb") == null || filter.get("jb").equals(""))) {
            sql +=" where jb= ?";
            param.add((Integer.parseInt(filter.get("jb").toString())-1));
		} else {
			if (!(filter.get("glbm") == null || filter.get("glbm").equals(""))) {
				sql += " and p.dwdm like ?";
				param.add(filter.get("glbm")+"%");
			}
			if (!(filter.get("dep_name") == null || filter.get("dep_name")
					.equals(""))) {
				sql += " and t.dep_name like ?";
				param.add("%"+filter.get("dep_name")
						+ "%");
			}
		}
		sql += " ) order by nlssort(bmmc, 'NLS_SORT=SCHINESE_PINYIN_M') ";

		l = this.jdbcTemplate.queryForList(sql,param.toArray());
		return l;
	}
	
	/**
	 * 异步获取部门树
	 */
	public List  getPrefectureTreeAsync(String sjbm,String glbmn){
		List l = new ArrayList();
		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer(" select glbm, bmmc, jb, nvl(sjbm, 0) sjbm, py from frm_department where (1 = 1) ");
		if("0".equals(sjbm)){
			sql.append(" and jb in (1, 2) and glbm in(select xjjg from frm_prefecture where dwdm " +
					"=?) order by nlssort(bmmc, 'NLS_SORT=SCHINESE_PINYIN_M') ");
			param.add(sjbm);
		} else {
			sql.append(" and sjbm like ? and  glbm in (select xjjg from frm_prefecture where dwdm" +
					" =?)  order by nlssort(bmmc, 'NLS_SORT=SCHINESE_PINYIN_M') ");
			param.add(sjbm+"%");
			param.add(glbmn);
		}
		l = this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		return l;
	}
	/**
	 * 异步获取辖区部门树
	 */
	public List getDepartmentTreeAsync(String sjbm){
		List l = new ArrayList();
		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer(" select glbm, bmmc, jb, nvl(sjbm, 0) sjbm, py from frm_department where (1=1) ");
		if("0".equals(sjbm)){
			sql.append("and jb in (1,2) order by nlssort(bmmc, 'NLS_SORT=SCHINESE_PINYIN_M') ");
		} else {
			sql.append("and sjbm like ? order by nlssort(bmmc, 'NLS_SORT=SCHINESE_PINYIN_M') ");
			param.add(sjbm+"%");
		}
		l = this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		return l;
	}
	

	public int getCountForLowerDepartment(String sjbm){
		String sql = "SELECT count(1)  from frm_department where sjbm = ?";
		int r = this.jdbcTemplate.queryForInt(sql,new Object[]{sjbm});
		return r;
	}
	
	public List getLeaderDepartmentTree(Map filter) {
		List l = new ArrayList();
		List param = new ArrayList<>();
		String sql = "select glbm,bmmc,jb,nvl(sjbm,0) sjbm ,py " +
				" from frm_department t ";
		if (!(filter.get("jb") == null || filter.get("jb").equals(""))) {
                sql +=" where jb= ?";
                param.add((Integer.parseInt(filter.get("jb").toString())-1));
		} else {
			if (!(filter.get("glbm") == null || filter.get("glbm").equals(""))) {
				sql += "WHERE glbm IN (SELECT dwdm FROM frm_prefecture WHERE xjjg =?)";
				param.add(filter.get("glbm").toString());
				/*sql += " start with t.glbm like '" + filter.get("glbm")
						+ "%' connect by prior t.glbm=t.sjbm ";*/
			}
			/*if (!(filter.get("dep_name") == null || filter.get("dep_name")
					.equals(""))) {
				sql += " and t.dep_name like '%" + filter.get("dep_name")
						+ "%' ";
			}*/
		}
		sql += " order by nlssort(bmmc, 'NLS_SORT=SCHINESE_PINYIN_M') ";

		l = this.jdbcTemplate.queryForList(sql,param.toArray());
		return l;
	}
	
	public List getlocalDepartmentTree(Map filter) {
		List l = new ArrayList();
		List param = new ArrayList<>();
		String sql = "select glbm,bmmc,jb,nvl(sjbm,0) sjbm ,py " +
				" from frm_department t ";
		if (!(filter.get("jb") == null || filter.get("jb").equals(""))) {
                sql +=" where jb= ?";
                param.add((Integer.parseInt(filter.get("jb").toString())-1));
		} else {
			if (!(filter.get("glbm") == null || filter.get("glbm").equals(""))) {
				sql += "WHERE glbm = ?";
				sql += " start with t.glbm like ? connect by prior t.glbm=t.sjbm ";
				param.add(filter.get("glbm").toString());
				param.add(filter.get("glbm")+"%");
			}
			if (!(filter.get("dep_name") == null || filter.get("dep_name")
					.equals(""))) {
				sql += " and t.dep_name like ? ";
				param.add("%"+filter.get("dep_name")+"%");
			}
		}
		sql += " order by t.jb,glbm";

		l = this.jdbcTemplate.queryForList(sql,param.toArray());
		return l;
	}
	
	
	public List getGateTree(String dwdm) {
		List list = new ArrayList();
		String sql = "select distinct id, idname, nvl(pid,0) pid from (select fxmc as idname, fxbh as id, a.kdbh as pid, '5' as jb "
		+ " from code_gate a left join code_gate_extend b on a.kdbh = b.kdbh union all select kdmc as idname, "
		+ " kdbh as id, nvl(d.xzqhdm, substr(c.kdbh, 1, 4)) as pid,'4' as jb from code_gate c "
		+ " left join frm_xzqh d on substr(c.kdbh, 1, 6) = d.xzqhdm  union all "
		+ " select e.xzqhmc as idname, e.xzqhdm as id, f.dwdm as pid, '3' as jb from frm_xzqh e right join code_url f on substr(e.xzqhdm, 1, 4) = substr(f.dwdm, 1, 4) and f.jdmc <> '本机' "
		+ " union all select jdmc as idname, dwdm as id, '' as pid, '2' as jb from code_url where jdmc <> '本机') t ";	
		 sql += " start with t.id like ? connect by prior t.id = t.pid ";
		sql +=" order by id";
		System.out.println(sql);
		list = this.jdbcTemplate.queryForList(sql,new Object[]{dwdm+"%"});
		return list;
	}
	
	public List getOldGateTree(String dwdm) {
		List list = new ArrayList();
		String sql = "select distinct id, idname, nvl(pid, 0) pid from ( "
			//+" select c.fxmc as idname, c.fxbh as id, a.kdbh as pid from dc_code_gate a, dc_code_device b, dc_code_direct c where a.kdbh = b.kdbh and (b.fxbh1 = c.fxbh or b.fxbh2 = c.fxbh) "
			//+" union all "
			+" select d.kdmc as idname, d.kdbh as id, nvl(e.xzqhdm, substr(d.kdbh, 1, 4)) as pid from code_gate d, frm_xzqh e where substr(d.kdbh, 1, 6) = e.xzqhdm "
			+" union all "
			+" select e.xzqhmc as idname, e.xzqhdm as id, f.dwdm as pid from frm_xzqh e right join code_url f on substr(e.xzqhdm, 1, 4) = substr(f.dwdm, 1, 4) and f.jdmc <> '本机' "
			+" union all "
			+" select jdmc as idname, dwdm as id, '' as pid from code_url where jdmc <> '本机' "
			+" ) t start with t.id like ? connect by prior t.id = t.pid order by nlssort(idname, " +
				"'NLS_SORT=SCHINESE_PINYIN_M') "
			;
		System.out.println(sql);
		list = this.jdbcTemplate.queryForList(sql,new Object[]{dwdm+"%"});
		return list;
	}
	
	public List getOldGateTreeOnlyGate(String dwdm) {
		List list = new ArrayList();
		String sql = "select distinct id, idname, nvl(pid, 0) pid from ( "
			//+" select d.kdmc as idname, d.kdbh as id, nvl(e.xzqhdm, substr(d.kdbh, 1, 4)) as pid from dc_code_gate d, frm_xzqh e where substr(d.kdbh, 1, 6) = e.xzqhdm "
			//+" union all "
			+" select e.xzqhmc as idname, e.xzqhdm as id, f.dwdm as pid from frm_xzqh e right join code_url f on substr(e.xzqhdm, 1, 4) = substr(f.dwdm, 1, 4) and f.jdmc <> '本机' "
			+" union all "
			+" select jdmc as idname, dwdm as id, '' as pid from code_url where jdmc <> '本机' "
			+" ) t start with t.id like ? connect by prior t.id = t.pid order by id "
			;
		list = this.jdbcTemplate.queryForList(sql,new Object[]{dwdm+"%"});
		return list;
	}
	
	public List getSTOldGateTreeOnlyGate() {
		List list = new ArrayList();
		String sql = "select distinct id, idname, nvl(pid, 0) pid from ( "
			//+" select d.kdmc as idname, d.kdbh as id, nvl(e.xzqhdm, substr(d.kdbh, 1, 4)) as pid from dc_code_gate d, frm_xzqh e where substr(d.kdbh, 1, 6) = e.xzqhdm "
			//+" union all "
			+" select e.xzqhmc as idname, e.xzqhdm as id, f.dwdm as pid from frm_xzqh e right join code_url f on substr(e.xzqhdm, 1, 4) = substr(f.dwdm, 1, 4)"
			+" union all "
			+" select g.jdmc as idname, g.dwdm as id, h.dwdm as pid from code_url g, code_url h where substr(g.dwdm, 1, 2) = substr(h.dwdm, 1, 2) and h.dwdm like '4300%' and g.dwdm not like '4300%' "
			+" union all "
			+" select jdmc as idname, dwdm as id, '' as pid from code_url where dwdm like '4300%' "
			+" ) t start with t.id like '4300%' connect by prior t.id = t.pid order by id "
			;
		System.out.println(sql);
		list = this.jdbcTemplate.queryForList(sql);
		return list;
		
	}
	
	public List getMulGateTree(Map filter) {
		List list = new ArrayList();
		List param = new ArrayList<>();
		String sql = "select distinct id, idname, nvl(pid,0) pid from ( select kdmc as idname, "
		+ " kdbh as id, nvl(d.xzqhdm, substr(c.kdbh, 1, 4)) as pid,'4' as jb from code_gate c "
		+ " left join frm_xzqh d on substr(c.kdbh, 1, 6) = d.xzqhdm  union all "
		+ " select e.xzqhmc as idname, e.xzqhdm as id, f.dwdm as pid, '3' as jb from frm_xzqh e right join code_url f on substr(e.xzqhdm, 1, 4) = substr(f.dwdm, 1, 4) "
		+ " union all select jdmc as idname, dwdm as id, '' as pid, '2' as jb from code_url) t ";
		if(filter.get("dwdm")!=null && !"".equals(filter.get("dwdm"))) {
			sql += " start with t.id like ? connect by prior t.id = t.pid ";
			param.add(filter.get("dwdm")+"%");
		}
		sql +=" order by id";
		list = this.jdbcTemplate.queryForList(sql,param.toArray());
		return list;
	}
	public List getSTGateTree() {
		List list  = new ArrayList();
		String sql = " select distinct id, idname, nvl(pid, 0) pid "
			+" from (select fxmc as idname, fxbh as id, a.kdbh as pid "
			+" from code_gate a left join code_gate_extend b on a.kdbh = b.kdbh "
			+" union all select kdmc as idname, kdbh as id, nvl(d.xzqhdm, substr(c.kdbh, 1, 4)) as pid from code_gate c left join frm_xzqh d on substr(c.kdbh, 1, 6) = d.xzqhdm "
			+" union all select e.xzqhmc as idname, e.xzqhdm as id, f.dwdm as pid "
			+" from frm_xzqh e right join code_url f on substr(e.xzqhdm, 1, 4) = substr(f.dwdm, 1, 4) "
			+" union all select g.jdmc as idname, g.dwdm as id, h.dwdm as pid "
			+" from code_url g , code_url h where substr(g.dwdm, 1, 2) =substr(h.dwdm, 1, 2) and h.dwdm like '4300%' and g.dwdm not like '4300%' "
			+" union all select jdmc as idname,dwdm as id,'' as pid from code_url where dwdm like  '4300%' "
			+" ) t start with t.id like '4300%' connect by prior t.id = t.pid order by id ";
		System.out.println(sql);
		list = this.jdbcTemplate.queryForList(sql);
		return list;
	}
	
	public List getSTOldGateTree() {
		List list = new ArrayList();
		String sql = "select distinct id, idname, nvl(pid, 0) pid from ( "
			+" select c.fxmc as idname, c.fxbh as id, a.kdbh as pid from dc_code_gate a, dc_code_device b, dc_code_direct c where a.kdbh = b.kdbh and (b.fxbh1 = c.fxbh or b.fxbh2 = c.fxbh) "
			+" union all "
			+" select d.kdmc as idname, d.kdbh as id, nvl(e.xzqhdm, substr(d.kdbh, 1, 4)) as pid from dc_code_gate d, frm_xzqh e where substr(d.kdbh, 1, 6) = e.xzqhdm "
			+" union all "
			+" select e.xzqhmc as idname, e.xzqhdm as id, f.dwdm as pid from frm_xzqh e right join code_url f on substr(e.xzqhdm, 1, 4) = substr(f.dwdm, 1, 4)"
			+" union all "
			+" select g.jdmc as idname, g.dwdm as id, h.dwdm as pid from code_url g, code_url h where substr(g.dwdm, 1, 2) = substr(h.dwdm, 1, 2) and h.dwdm like '4300%' and g.dwdm not like '4300%' "
			+" union all "
			+" select jdmc as idname, dwdm as id, '' as pid from code_url where dwdm like '4300%' "
			+" ) t start with t.id like '4300%' connect by prior t.id = t.pid order by id "
			;
		System.out.println(sql);
		list = this.jdbcTemplate.queryForList(sql);
		return list;
	}
	
	public List getSTGateTreeOnlyGate() {
		List list  = new ArrayList();
		String sql = " select distinct id, idname, nvl(pid, 0) pid "
			+" from (select kdmc as idname, kdbh as id, nvl(d.xzqhdm, substr(c.kdbh, 1, 4)) as pid from code_gate c left join frm_xzqh d on substr(c.kdbh, 1, 6) = d.xzqhdm "
			+" union all select e.xzqhmc as idname, e.xzqhdm as id, f.dwdm as pid "
			+" from frm_xzqh e right join code_url f on substr(e.xzqhdm, 1, 4) = substr(f.dwdm, 1, 4) "
			+" union all select g.jdmc as idname, g.dwdm as id, h.dwdm as pid "
			+" from code_url g , code_url h where substr(g.dwdm, 1, 2) =substr(h.dwdm, 1, 2) and h.dwdm like '4300%' and g.dwdm not like '4300%' "
			+" union all select jdmc as idname,dwdm as id,'' as pid from code_url where dwdm like  '4300%' "
			+" ) t start with t.id like '4300%' connect by prior t.id = t.pid order by id ";
		list = this.jdbcTemplate.queryForList(sql);
		return list;
	}
	
	public List<Map<String,Object>> getCityTree() {
		List<Map<String,Object>> l = new ArrayList<Map<String,Object>>();
		String sql = "select dwdm,jdmc,jb,nvl(sjjd,0) sjjd " +
				" from code_url t ";
		
		sql += " order by t.dwdm";
		l = this.jdbcTemplate.queryForList(sql);
		
		return l;
	}
	
	public boolean delPrefectureBydeptId(String curDept) {
		boolean flag = true;
		//String sql = "delete from frm_prefecture where dwdm='"+curDept+"' and jglx='1'";
		String sql = "delete from frm_prefecture where dwdm=?";
		int result = this.jdbcTemplate.update(sql, new Object[] {curDept});
		if(result<0){
			flag = false;
		}
		return flag;
	}
	
	public boolean savePrefectureBymenuids(String curDept, String[] ids) {
		boolean flag = true;
		String[] sqlArray = new String[ids.length];
		int i = 0;
		for(String deptId : ids){
			sqlArray[i] =  "INSERT into frm_prefecture (dwdm,xjjg,jglx,jgqz,gxsj,bz) values ('"+curDept+"','"+deptId+"','1','1',sysdate,'')";
			i++;
		}
		int[] result = this.jdbcTemplate.batchUpdate(sqlArray);
		for (int j = 0; i < result.length; j++) {
			if(result[j]<0){
				//只要有一条数据执行失败，则结果为失败
				flag = false ;
			}
		}
		return flag;
	}
	
	public List getPrefectures(Prefecture prefecture) {
		String tmpSql = "";
		List param = new ArrayList<>();
		if ((prefecture.getDwdm() != null)
				&& (prefecture.getDwdm().length() > 0)) {
			tmpSql = tmpSql + " and dwdm=?";
			param.add(prefecture.getDwdm());
		}
		if ((prefecture.getXjjg() != null)
				&& (prefecture.getXjjg().length() > 0)) {
			tmpSql = tmpSql + " and xjjg=?";
			param.add(prefecture.getXjjg());
		}
		if ((prefecture.getJglx() != null)
				&& (prefecture.getJglx().length() > 0)) {
			tmpSql = tmpSql + " and jglx=?";
			param.add(prefecture.getJglx());
		}
		if ((prefecture.getJgqz() != null)
				&& (prefecture.getJgqz().length() > 0)) {
			tmpSql = tmpSql + " and jgqz=?";
			param.add(prefecture.getJgqz());
		}
		if (tmpSql.length() > 4) {
			tmpSql = " where " + tmpSql.substring(4, tmpSql.length());
		}
		tmpSql = "select * from frm_prefecture " + tmpSql
				+ " order by dwdm,xjjg";
		List list = this.queryForList(tmpSql, Prefecture.class,param.toArray());
		
		return list;
	}

	public Prefecture getPrefecture(String glbm) {
		List param = new ArrayList<>();
		String tmpSql = "select * from frm_prefecture where dwdm=?";
		param.add(glbm);
		//List list = this.jdbcTemplate.queryForList(tmpSql, Prefecture.class);
		List list = this.queryForList(tmpSql,param.toArray(), Prefecture.class);
		if ((list == null) || (list.size() == 0)) {
			Prefecture d = new Prefecture();
			d.setDwdm(glbm);
			d.setXjjg(glbm);
			d.setJglx("0");
			d.setJgqz("1");
			return d;
		}
		return (Prefecture) list.get(0);
	}

	public int insertPrefecture(Prefecture p)throws Exception {
		List param = new ArrayList<>();
		StringBuffer sb =
				new StringBuffer("insert into frm_prefecture values(?,?,?,?,sysdate,?)");
		param.add(p.getDwdm());
		param.add(p.getXjjg());
		param.add(p.getJglx());
		param.add(p.getJgqz());
		if (p.getBz()==null){
			param.add("''");
		}else {
			param.add(p.getBz());
		}

		
		return this.jdbcTemplate.update(sb.toString(),param.toArray());
	}

	public int insertPrefectureTemp(Prefecture p) throws Exception{
		List param = new ArrayList<>();
		StringBuffer sb = new StringBuffer("insert into frm_prefecture_temp values(?,?,?,?)");
		param.add(p.getDwdm());
		param.add(p.getXjjg());
		param.add(p.getJglx());
		param.add(p.getJgqz());

		
		return this.jdbcTemplate.update(sb.toString(),param.toArray());
	}

	public int delAllForPrefectureTemp(String glbm) throws Exception {
		String sql = "DELETE from frm_prefecture_temp where dwdm=?";
		
		return this.jdbcTemplate.update(sql, new Object[]{glbm});
	}

	public List getPrefectureTemp(String glbm) throws Exception {
		String sql = "select distinct dwdm,xjjg,jglx,jgqz from frm_prefecture_temp where dwdm= ?";
		
		return this.queryForList(sql,new Object[]{glbm}, Prefecture.class);
	}
	
	
	public List<Map<String, Object>> getGdCityTree() throws Exception {
		String sql = "select dmz || '000000' as dwdm, substr(dmsm1, 0, 3) jdmc from frm_code where " +
				"dmlb = '000033' and substr(dmz, 0, 2) = '44' and dmsm2 = '1' order by dwdm asc";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getGdAnHnCityTree() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select dmz || '000000' as dwdm, substr(dmsm1, 0, 3) jdmc ")
		.append("from frm_code where dmlb = '000033' and substr(dmz, 0, 2) = '43'")
		.append(" and dmsm2 = '1'")
		.append(" union all ")
		.append("select dmz || '000000' as dwdm, substr(dmsm1, 0, 3) jdmc ")
		.append("from frm_code where dmlb = '000033' and substr(dmz, 0, 2) = '44'")
		.append("and dmsm2 = '1' ");
		sql.append(" union all"+
			" select dmz || '000000' as dwdm, substr(dmsm1, 0, 3) jdmc"+
			 " from frm_code"+
			 " where dmlb = '000033'"+
			  " and ( substr(dmz, 0, 2) = '45' or substr(dmz, 0, 2) = '35' or substr(dmz, 0, 2) = '36' or substr(dmz, 0, 2) = '46' )"+
			 "  and dmsm2 = '1'");
//		.append("order by dwdm asc ");
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}
	
}
