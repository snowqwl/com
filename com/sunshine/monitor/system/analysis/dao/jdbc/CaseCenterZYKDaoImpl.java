package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ZykBaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.CaseEntity;
import com.sunshine.monitor.system.analysis.dao.CaseCenterZYKDao;

@Repository("caseCenterZYKDao")
public class CaseCenterZYKDaoImpl extends ZykBaseDaoImpl implements CaseCenterZYKDao{

	public Map<String, Object> findCaseByPage(CaseEntity entity,
			Map<String, Object> filter) throws Exception {
		StringBuffer sb = new StringBuffer(" select jb.anjianleibie as ajlb, jb.anjianbianhao as ajbh, jb.anjianmingcheng as ajmc, jcj.faanshijiankaishi as afsj, jcj.faxiandidian as afdz, jcj.jianyaoanqing as jyaq from QBZHPT.T_ASJ_JBXX_DXF jb, QBZHPT.T_ASJ_JCJXX_DXF jcj where 1=1 ");
		if(entity.getAjlb()!=null&&entity.getAjlb().length()>0){
			sb.append(" and jb.anjianleibie = '").append(entity.getAjlb()).append("'");
		}
		if(entity.getAjmc()!=null&&entity.getAjmc().length()>0){
			sb.append(" and jb.anjianmingcheng like '%").append(entity.getAjmc()).append("%'");
		}
		if(entity.getKssj()!=null&&entity.getKssj().length()>0){
			sb.append(" and jcj.jiejingshijian > '").append(entity.getKssj()).append("'");
		}
		if(entity.getJssj()!=null&&entity.getJssj().length()>0){
			sb.append(" and jcj.jiejingshijian <= '").append(entity.getJssj()).append("'");
		}
		sb.append(" and jb.anjianbianhao = jcj.anjianbianhao order by jb.lianriqi desc");
		Map<String, Object> result = this.findPageForMap(sb.toString(), Integer
				.parseInt(filter.get("page").toString()), Integer
				.parseInt(filter.get("rows").toString()), CaseEntity.class);
		return result;
	}
	
	public Map<String, Object> getCaseDetail(Map<String, Object> filter){
		Map<String, Object> map = new HashMap<String, Object>();
		if(filter==null){
			return map;
		}
		StringBuffer sb = new StringBuffer("select jb.anjianbianhao as ajbh, jb.anjianmingcheng as ajmc, jcj.faanshijiankaishi as afsj, jcj.faxiandidian as afdz, jcj.jianyaoanqing as jyaq from QBZHPT.T_ASJ_JBXX_DXF jb, QBZHPT.T_ASJ_JCJXX_DXF jcj where 1=1");
		if(filter.get("ajbh")!=null && !"".equals(filter.get("ajbh").toString()!="")){
			sb.append(" and jb.anjianbianhao = '").append(filter.get("ajbh").toString()).append("'");
		}
		sb.append(" and jb.anjianbianhao = jcj.anjianbianhao order by jb.lianriqi desc");
		try{
			map = this.jdbcZykTemplate.queryForMap(sb.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
}
