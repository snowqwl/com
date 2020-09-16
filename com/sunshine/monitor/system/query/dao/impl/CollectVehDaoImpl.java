package com.sunshine.monitor.system.query.dao.impl;

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
		StringBuffer sb = new StringBuffer();
		sb.append(" select jh,hphm,hpzl,xxly,scr,scsj,status,bz,isread,gcsj from veh_collect c");
		sb.append(" where 1=1 ");
		if(StringUtils.isNotBlank(info.getHphm()))
			sb.append(" and hphm like '%").append(info.getHphm()).append("%'");
		
		if(StringUtils.isNotBlank(info.getHpzl()))
			sb.append(" and hpzl = '").append(info.getHpzl()).append("' ");
		
//		if(StringUtils.isNotBlank(info.getBy1()))
//			sb.append(" and status = '").append(info.getBy1()).append("' ");
		
		if(StringUtils.isNotBlank(info.getXxly()))
			sb.append(" and xxly = '").append(info.getXxly()).append("' ");
		
		if(StringUtils.isNotBlank(info.getKssj()))
			sb.append(" and scsj >=to_date('").append(info.getKssj()).append("', 'yyyy-mm-dd hh24:mi:ss') ");
		
		if(StringUtils.isNotBlank(info.getJssj()))
			sb.append(" and scsj <=to_date('").append(info.getJssj()).append("', 'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and status = 0 ");
		sb.append(conSql);
		sb.append(" order by scsj desc ");
		
		return this.findPageForMap(sb.toString(), page.getCurPage(), page.getPageSize());
	}
	
	private String queryFirstGcsj(String hphm,String hpzl,String conSql){
		String gcsj = "";
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
		String sql = "insert into veh_collect(jh,hphm,hpzl,xxly,scr,scsj,status,bz,isread,gcsj) "
				+ "values('"+user.getJh()+"','"+info.getHphm()+"','"+info.getHpzl()+"',"
						+ "'"+info.getXxly()+"','"+user.getYhdh()+"',sysdate,'0','"+info.getBz()+"','0',"+gcsj+")";
		return this.jdbcTemplate.update(sql); 
	}

	@Override
	public int update(String hphm, String hpzl, String status) throws Exception {
		String sql = " update veh_collect set status='"+status+"' where hphm='"+hphm+"' and hpzl='"+hpzl+"'";
		return this.jdbcTemplate.update(sql); 
	}
	
	@Override
	public int updateIsRead(String hphm, String hpzl, String isread) throws Exception {
		String sql = " update veh_collect set isread='"+isread+"' where hphm='"+hphm+"' and hpzl='"+hpzl+"'";
		return this.jdbcTemplate.update(sql); 
	}
	
	@Override
	public int checkMaxCount(String jh) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" select jh from veh_collect  where jh = '"+jh+"' and status=0");
		return this.getRecordCounts(sb.toString(), 0);
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
		String sql = " select hphm from veh_collect where jh='"+jh+"' and hphm='"+hphm+"' and hpzl='"+hpzl+"' and status=0 ";
		int count = getRecordCounts(sql, 0);
		return count;
	}

	@Override
	public VehSuspinfo getVehCollect(String jh, String hphm, String hpzl)
			throws Exception {
		String sql = " select jh,hphm,hpzl,xxly,scr,scsj,status,bz,isread,gcsj from veh_collect where jh='"+jh+"' and hphm='"+hphm+"' and hpzl='"+hpzl+"' ";
		return this.queryForObject(sql, VehSuspinfo.class);
	}

	@Override
	public int edit(SysUser user, VehSuspinfo info) throws Exception {
		// TODO Auto-generated method stub
		String sql = " update veh_collect set xxly='"+info.getXxly()+"',bz='"+info.getBz()+"' where jh='"+user.getJh()+"' and hphm='"+info.getHphm()+"' and hpzl='"+info.getHpzl()+"'";
		return this.jdbcTemplate.update(sql); 
	}
}
