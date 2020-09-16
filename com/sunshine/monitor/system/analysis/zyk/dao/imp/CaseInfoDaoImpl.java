package com.sunshine.monitor.system.analysis.zyk.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.zyk.bean.CaseInfo;
import com.sunshine.monitor.system.analysis.zyk.dao.CaseInfoDao;

@Repository("caseInfoDao")
public class CaseInfoDaoImpl extends BaseDaoImpl implements CaseInfoDao {
	
	String seq = "select SEQ_JM_ZTK_CASE_INFO.nextval from dual";
	@Override
	public void saveCaseInfo(CaseInfo ci) {
		// 生成序列号id
		String id = this.jdbcTemplate.queryForObject(seq, String.class);
		ci.setId(id);
		StringBuffer sql = new StringBuffer("insert into JM_ZTK_CASE_INFO (id,ajbh,gabh,ajly,ajmc,hphm,hpzl,description,tplj,lrr,lrsj,xzqh,xxly,by1,by2,by3) values(");
		sql.append("'"+ci.getId()+"',")
		   .append("'"+(ci.getAjbh()==null?"":ci.getAjbh().toString())+"',")
		   .append("'"+(ci.getGabh()==null?"":ci.getGabh().toString())+"',")
		   .append("'"+(ci.getAjly()==null?"":ci.getAjly().toString())+"',")
		   .append("'"+(ci.getAjmc()==null?"":ci.getAjmc().toString())+"',")
		   .append("'"+(ci.getHphm()==null?"":ci.getHphm().toString())+"',")
		   .append("'"+(ci.getHpzl()==null?"":ci.getHpzl().toString())+"',")
		   .append("'"+(ci.getDescription()==null?"":ci.getDescription().toString())+"',")
		   .append("'"+(ci.getTplj()==null?"":ci.getTplj().toString())+"',")
		   .append("'"+(ci.getLrr()==null?"":ci.getLrr().toString())+"',")
		   .append("to_date('"+(ci.getLrsj()==null?"":ci.getLrsj().toString())+"','yyyy-mm-dd hh24:mi:ss'),")
		   .append("'"+(ci.getXzqh()==null?"":ci.getXzqh().toString())+"',")
		   .append("'"+(ci.getXxly()==null?"":ci.getXxly().toString())+"',")
		   .append("'"+(ci.getBy1()==null?"":ci.getBy1().toString())+"',")
		   .append("'"+(ci.getBy2()==null?"":ci.getBy2().toString())+"',")
		   .append("'"+(ci.getBy3()==null?"":ci.getBy3().toString())+"')");
		   
		this.jdbcTemplate.execute(sql.toString());
	}
	
	@Override
	public CaseInfo getCaseInfoById(String id) throws Exception{
		String sql = "select id,ajbh,gabh,ajly,ajmc,hphm,hpzl,description,tplj,lrr,lrsj,xzqh,xxly from JM_ZTK_CASE_INFO where id = ?";
		List<CaseInfo> list = this.queryForList(sql, new Object[]{id},CaseInfo.class);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	@Override
	public void updateCaseInfo(CaseInfo ci) throws Exception{
		StringBuffer sql = new StringBuffer("update JM_ZTK_CASE_INFO set ");
		sql.append(ci.getHphm()==null?"":" hphm = '"+ci.getHphm()+"',")
		   .append(ci.getHpzl()==null?"":" hpzl = '"+ci.getHpzl()+"',")
		   .append(ci.getLrr()==null?"":" lrr = '"+ci.getLrr()+"',")
		   .append(ci.getLrsj()==null?"":" lrsj = to_date('"+ci.getLrsj()+"','yyyy-mm-dd hh24:mi:ss'),")
		   .append(ci.getAjbh()==null?"":" ajbh = '"+ci.getAjbh()+"',")
		   .append(ci.getGabh()==null?"":" gabh = '"+ci.getGabh()+"',")
		   .append(ci.getAjly()==null?"":" ajly = '"+ci.getAjly()+"',")
		   .append(ci.getAjmc()==null?"":" ajmc = '"+ci.getAjmc()+"',")
		   .append(ci.getDescription()==null?"":" description = '"+ci.getDescription()+"',")
		   .append(ci.getTplj()==null?"":" tplj = '"+ci.getTplj()+"',")
		   .append(ci.getXzqh()==null?"":" xzqh = '"+ci.getXzqh()+"',")
		   .append(ci.getXxly()==null?"":" xxly = '"+ci.getXxly()+"',")
		   .append(ci.getBy1()==null?"":" by1 = '"+ci.getBy1()+"',")
		   .append(ci.getBy2()==null?"":" by2 = '"+ci.getBy2()+"',")
		   .append(ci.getBy3()==null?"":" by3 = '"+ci.getBy3()+"',");
		   
		String bigBossSql = sql.toString().substring(0, sql.toString().length()-1);
		bigBossSql = bigBossSql+" where id = '"+ci.getId()+"'";
		this.jdbcTemplate.execute(bigBossSql);
	}
	
	@Override
	public void deleteCaseInfo(String id) throws Exception{
		String sql = "delete JM_ZTK_CASE_INFO where id = '"+id+"'";
		this.jdbcTemplate.execute(sql);
	}
	
	@Override
	public List<CaseInfo> getAllCaseInfo(){
		List<CaseInfo> caseInfoList = new ArrayList<CaseInfo>();
		String sql = "select lrr,ajbh,gabh,ajly,ajmc,hphm,hpzl,tplj,description,xzqh,xxly,lrsj from JM_ZTK_CASE_INFO";
		caseInfoList = getJdbcTemplate().query(sql,
				new BeanPropertyRowMapper(CaseInfo.class));
		return caseInfoList;
	}
}
