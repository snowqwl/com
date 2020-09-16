package com.sunshine.monitor.system.analysis.zyk.dao.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.zyk.bean.Suspect;
import com.sunshine.monitor.system.analysis.zyk.dao.SuspectDao;

@Repository("suspectDao")
public class SuspectDaoImpl extends BaseDaoImpl implements SuspectDao {

	private String seq = "select SEQ_JM_ZTK_SUSPECT.NEXTVAL from dual";
	private String table = "JM_ZTK_SUSPECT";
	@Override
	public void saveSuspect(Suspect suspect) {
		// 生成序列号id
		String id = this.jdbcTemplate.queryForObject(seq, String.class);
		suspect.setId(id);
		StringBuffer sql = new StringBuffer("insert into "+table+" (id,hphm,hpzl,lrr,lrsj,kkbh,address,description,tplj,by1,by2,by3,by4,by5,xzqh,fid,xxly) values(");
		sql.append("'"+suspect.getId()+"',")
		   .append("'"+(suspect.getHphm()==null?"":suspect.getHphm().toString())+"',")
		   .append("'"+(suspect.getHpzl()==null?"":suspect.getHpzl().toString())+"',")
		   .append("'"+(suspect.getLrr()==null?"":suspect.getLrr().toString())+"',")
		   .append("to_date('"+(suspect.getLrsj()==null?"":suspect.getLrsj().toString())+"','yyyy-mm-dd hh24:mi:ss'),")
		   .append("'"+(suspect.getKkbh()==null?"":suspect.getKkbh().toString())+"',")
		   .append("'"+(suspect.getAddress()==null?"":suspect.getAddress().toString())+"',")
		   .append("'"+(suspect.getDescription()==null?"":suspect.getDescription().toString())+"',")
		   .append("'"+(suspect.getTplj()==null?"":suspect.getDescription().toString())+"',")
		   .append("'"+(suspect.getBy1()==null?"":suspect.getBy1().toString())+"',")
		   .append("'"+(suspect.getBy2()==null?"":suspect.getBy2().toString())+"',")
		   .append("'"+(suspect.getBy3()==null?"":suspect.getBy3().toString())+"',")
		   .append("'"+(suspect.getBy4()==null?"":suspect.getBy4().toString())+"',")
		   .append("'"+(suspect.getBy5()==null?"":suspect.getBy5().toString())+"',")
		   .append("'"+(suspect.getXzqh()==null?"":suspect.getXzqh().toString())+"',")
		   .append("'"+(suspect.getFid()==null?"":suspect.getFid().toString())+"',")
		   .append("'"+(suspect.getXxly()==null?"":suspect.getXxly().toString())+"')");
		this.jdbcTemplate.execute(sql.toString());
	}
	
	@Override
	public Suspect getSuspectById(String suspectId) throws Exception{
		String sql = "select id,kkbh,hphm,hpzl,lrr,lrsj,address,description,tplj from "+table+" where id = ?";
		List<Suspect> list = this.queryForList(sql, new Object[]{suspectId},Suspect.class);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	@Override
	public void deleteSuspect(String id) throws Exception{
		String sql = "delete "+table+" where id = '"+id+"'";
		this.jdbcTemplate.execute(sql);
	}
	
	@Override
	public void updateSuspect(Suspect suspect) throws Exception{
		StringBuffer sql = new StringBuffer("update "+table+" set ");
		sql.append(suspect.getHphm()==null?"":" hphm = '"+suspect.getHphm()+"',")
		   .append(suspect.getHpzl()==null?"":" hpzl = '"+suspect.getHpzl()+"',")
		   .append(suspect.getLrr()==null?"":" lrr = '"+suspect.getLrr()+"',")
		   .append(suspect.getLrsj()==null?"":" lrsj = to_date('"+suspect.getLrsj()+"','yyyy-mm-dd hh24:mi:ss'),")
		   .append(suspect.getKkbh()==null?"":" kkbh = '"+suspect.getKkbh()+"',")
		   .append(suspect.getAddress()==null?"":" address = '"+suspect.getAddress()+"',")
		   .append(suspect.getDescription()==null?"":" description = '"+suspect.getDescription()+"',")
		   .append(suspect.getTplj()==null?"":" tplj = '"+suspect.getTplj()+"',")
		   .append(suspect.getBy1()==null?"":" by1 = '"+suspect.getBy1()+"',")
		   .append(suspect.getBy2()==null?"":" by2 = '"+suspect.getBy2()+"',")
		   .append(suspect.getBy3()==null?"":" by3 = '"+suspect.getBy3()+"',")
		   .append(suspect.getBy4()==null?"":" by4 = '"+suspect.getBy4()+"',")
		   .append(suspect.getBy5()==null?"":" by5 = '"+suspect.getBy5()+"',")
		   .append(suspect.getXzqh()==null?"":" xzqh = '"+suspect.getXzqh()+"',")
		   .append(suspect.getFid()==null?"":" fid = '"+suspect.getFid()+"',")
		   .append(suspect.getXxly()==null?"":" xxly = '"+suspect.getXxly()+"',");
		String bigBossSql = sql.toString().substring(0, sql.toString().length()-1);
		bigBossSql = bigBossSql+" where id = '"+suspect.getId()+"'";
		this.jdbcTemplate.execute(bigBossSql);
	}

	@Override
	public Map<String, Object> findPageForMap(Suspect susp,
			Map<String, Object> filter) throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer("select id,hphm,hpzl,lrr,lrsj,kkbh,address,description,tplj from "+table+" where 1 = 1");
		if(susp.getHphm()!=null && !"".equals(susp.getHphm())){
			sql.append(" and hphm like '%"+susp.getHphm()+"%'");
		}
		if(susp.getHpzl()!=null && !"".equals(susp.getHpzl())){
			sql.append(" and hpzl = '"+susp.getHpzl()+"'");
		}
		if(susp.getKkbh()!=null && !"".equals(susp.getKkbh()) && !"null".equals(susp.getKkbh())){
			sql.append(" and kkbh = '"+susp.getKkbh()+"'");
		}
		if(susp.getKssj()!=null && !"".equals(susp.getKssj())){
			sql.append(" and lrsj >= to_date('"+susp.getKssj()+"','yyyy-mm-dd hh24:mi:ss')");
		}
		if(susp.getJssj()!=null && !"".equals(susp.getJssj())){
			sql.append(" and lrsj <= to_date('"+susp.getJssj()+"','yyyy-mm-dd hh24:mi:ss')");
		}
		if(susp.getLrr()!=null && !"".equals(susp.getLrr())){
			sql.append(" and lrr = '"+susp.getLrr()+"'");
		}
		sql.append(" order by lrsj desc");
	    try {
	    	long start = System.currentTimeMillis();
			map = this.findPageForMap(sql.toString(), Integer
					.parseInt(filter.get("page").toString()), Integer
					.parseInt(filter.get("rows").toString()));
			long end = System.currentTimeMillis();
			log.info("查询可疑资源库耗时："+(end - start)+"MS");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
