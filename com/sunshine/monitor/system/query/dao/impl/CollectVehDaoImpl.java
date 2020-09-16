package com.sunshine.monitor.system.query.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oracle.sql.DATE;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.query.dao.CollectVehDao;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

import org.apache.commons.lang.StringUtils;

@Repository("collectVehDao")
public class CollectVehDaoImpl extends BaseDaoImpl implements CollectVehDao{

	@Override
	public Map<String, Object> getCollectVehList(Page page,VehSuspinfo info,String conSql) throws Exception {
		List param = new ArrayList<>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select jh,hphm,hpzl,xxly,scr,scsj,status,bz,isread,gcsj from veh_collect c");
		sb.append(" where 1=1 ");
		if(StringUtils.isNotBlank(info.getHphm())){
			sb.append(" and hphm like ?");
			param.add("%"+info.getHphm()+"%");
		}

		if(StringUtils.isNotBlank(info.getHpzl())){
			sb.append(" and hpzl = ? ");
			param.add(info.getHpzl());
		}

//		if(StringUtils.isNotBlank(info.getBy1()))
//			sb.append(" and status = '").append(info.getBy1()).append("' ");
		
		if(StringUtils.isNotBlank(info.getXxly())){
			sb.append(" and xxly = ? ");
			param.add(info.getXxly());
		}

		
		if(StringUtils.isNotBlank(info.getKssj())){
			sb.append(" and scsj >=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(info.getKssj());
		}

		if(StringUtils.isNotBlank(info.getJssj())){
			sb.append(" and scsj <=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(info.getJssj());
		}
		sb.append(" and status = 0 and jh= ?");
		param.add(conSql);
		sb.append(" order by scsj desc ");
		
		return this.findPageForMap(sb.toString(), param.toArray(),page.getCurPage(),
				page.getPageSize());
	}
	
	private String queryFirstGcsj(String hphm,String hpzl,String conSql){
		String gcsj = "";
		List param = new ArrayList<>();
		String sql="select gcsj from (select gcsj from veh_passrec where hphm='"+hphm+"' and hpzl='"+hpzl+"' "+ conSql +" order by gcsj desc) where rownum=1";
		try {
			List<VehPassrec> list = this.queryList(sql, VehPassrec.class);
			if(list.size()>0){
				gcsj=list.get(0).getGcsj();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gcsj;
	}
 
	@Override
	public int insert(SysUser user,VehSuspinfo info) throws Exception {
		String gcsj = queryFirstGcsj(info.getHphm(), info.getHpzl(),"");//在过车表中查询最新的过车数据
		if("".equals(gcsj)){
			gcsj="''";
		}else{
			gcsj= "to_date('"+gcsj+"','yyyy-mm-dd hh24:mi:ss')";
		}
		List param = new ArrayList<>();
		String sql = "insert into veh_collect(jh,hphm,hpzl,xxly,scr,scsj,status,bz,isread,gcsj) "
				+ "values(?,?,?,?,?,sysdate,'0',?,'0',"+gcsj+
				")";
		param.add(user.getJh());
		param.add(info.getHphm());
		param.add(info.getHpzl());
		param.add(info.getXxly());
		param.add(user.getYhdh());
		param.add(info.getBz());
		param.add(gcsj);
		return this.jdbcTemplate.update(sql,param.toArray());
	}

	@Override
	public int update(String hphm, String hpzl, String status) throws Exception {
		List param = new ArrayList<>();
		String sql = " update veh_collect set status=? where hphm=? and hpzl=?";
		param.add(status);
		param.add(hphm);
		param.add(hpzl);
		return this.jdbcTemplate.update(sql,param.toArray());
	}
	
	@Override
	public int updateIsRead(String hphm, String hpzl, String isread) throws Exception {
		List param = new ArrayList<>();
		String sql = " update veh_collect set isread=? where hphm=? and hpzl=?";
		param.add(isread);
		param.add(hphm);
		param.add(hpzl);
		return this.jdbcTemplate.update(sql,param.toArray());
	}
	
	@Override
	public int checkMaxCount(String jh) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" select jh from veh_collect  where jh = ? and status=0");
		return this.getRecordCounts(sb.toString(), new Object[]{jh},0);
	}

	@Override
	public List<VehPassrec> queryCollectVehList(String conSql) throws Exception {
		StringBuffer sb = new StringBuffer(" select hphm,hpzl,bz,isread,gcsj from veh_collect ");
		sb.append(" where 1=1 "+conSql);
		sb.append(" and status=0 ");
		sb.append(" order by scsj desc");
		List<VehPassrec> vehList = this.queryForList(sb.toString(), VehPassrec.class);
		return vehList;
	}
	
	@Override
	public int checkLastGcsj(String hphm, String hpzl,String gcsj) throws Exception {
		List param = new ArrayList<>();
		if(!"".equals(gcsj)){
			gcsj = " and gcsj > to_date('"+gcsj+"', 'yyyy-mm-dd hh24:mi:ss')";
		}
		String gcsjTemp = queryFirstGcsj(hphm,hpzl,gcsj);
	
		if("".equals(gcsjTemp)){
			return 0;
		}else{
			gcsjTemp = " to_date('"+gcsjTemp+"','yyyy-mm-dd hh24:mi:ss')";
			String sql = " update veh_collect set isread=0,gcsj="+gcsjTemp+" where hphm='"+hphm+"' and hpzl='"+hpzl+"'";
			return this.jdbcTemplate.update(sql); 
		}
	}

	@Override
	public int checkByHphm(String jh,String hphm, String hpzl)
			throws Exception {
		List param = new ArrayList<>();
		String sql =
				" select hphm from veh_collect where jh=? and hphm=? and hpzl=? and " +
						"status=0 ";
		param.add(jh);
		param.add(hphm);
		param.add(hpzl);
		int count = getRecordCounts(sql, param.toArray(),0);
		return count;
	}

	@Override
	public VehSuspinfo getVehCollect(String jh, String hphm, String hpzl)
			throws Exception {
		List param = new ArrayList<>();
		String sql = " select jh,hphm,hpzl,xxly,scr,scsj,status,bz,isread,gcsj from veh_collect " +
				"where jh=? and hphm=? and hpzl=? ";
		param.add(jh);
		param.add(hphm);
		param.add(hpzl);
		return this.queryForObject(sql, param.toArray(),VehSuspinfo.class);
	}

	@Override
	public int edit(SysUser user, VehSuspinfo info) throws Exception {
		// TODO Auto-generated method stub
		List param = new ArrayList<>();
		String sql =
				" update veh_collect set xxly=?,bz=? where jh=? and hphm=? and" +
						" hpzl=?";
		param.add(info.getXxly());
		param.add(info.getBz());
		param.add(user.getJh());
		param.add(info.getHphm());
		param.add(info.getHpzl());
		return this.jdbcTemplate.update(sql,param.toArray());
	}
}
