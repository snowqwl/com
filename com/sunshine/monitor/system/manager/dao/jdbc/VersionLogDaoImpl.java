package com.sunshine.monitor.system.manager.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.PageUtil;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.VersionLogBean;
import com.sunshine.monitor.system.manager.dao.VersionLogDao;

@Repository("versionLogDao")
public class VersionLogDaoImpl extends BaseDaoImpl implements VersionLogDao {

	@Override
	public int delete(String versionId) throws Exception {
		String tmpSql = "delete from frm_versionlog where id= ?";
		return this.jdbcTemplate.update(tmpSql,new Object[]{versionId});
	}	

	@Override
	public VersionLogBean getVersionById(String versionId) throws Exception {
		String sb = " select id,name,content,cjr,cjsj,fbr,fbsj from frm_versionlog where id=?";
		return this.queryForObject(sb,new Object[]{versionId}, VersionLogBean.class);
	}

	@Override
	public Map<String, Object> queryForShowPage(Map<String, Object> filter,
			String name) throws Exception {
		StringBuffer sb = new StringBuffer();
		List param = new ArrayList<>();
		sb.append("select id,name,content,to_char(cjsj,'yyyy-MM-dd') as cjsj from frm_versionlog ");
		if(StringUtils.isNotBlank(name)){
			sb.append(" where name like ?");
			sb.append(" or content like ?");
			param.add("%"+name+"%");
			param.add("%"+name+"%");
		}
		sb.append(" order by id desc ");		
		Map<String, Object> queryMap = this.findPageForMap(sb.toString(),param.toArray(),
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));		
		int recordCount = this.getRecordCounts(sb.toString(), 0);//总记录数
		PageUtil page = new PageUtil(Integer.parseInt((String) filter.get("pageSize")), recordCount,Integer.parseInt((String) filter.get("curPage")));
		queryMap.put("page", page);
		return queryMap;
	}

	@Override
	public Map<String, Object> queryList(Map<String, Object> filter,
			VersionLogBean bean) throws Exception {
		StringBuffer sb = new StringBuffer();
		List param = new ArrayList<>();
		sb.append(" select id,name,content,cjr,cjsj,fbr,fbsj from frm_versionlog ");
		sb.append(" where 1=1 ");
		if(StringUtils.isNotBlank(bean.getName())){
			sb.append(" and name like ?");
			param.add("%"+bean.getName()+"%");
		}

		if(StringUtils.isNotBlank(bean.getKssj())){
			sb.append(" and cjsj >=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(bean.getKssj());
		}

		if(StringUtils.isNotBlank(bean.getJssj())){
			sb.append(" and cjsj <=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(bean.getJssj());
		}

		if(StringUtils.isNotBlank(bean.getCjr())){
			sb.append(" and cjr like ?");
			param.add("%"+bean.getCjr()+"%");
		}

		if(StringUtils.isNotBlank(bean.getFbr())){
			sb.append(" and fbr like ?");
			param.add("%"+bean.getFbr()+"%");
		}
		sb.append(" order by id desc ");
		
		Map<String, Object> queryMap = this.findPageForMap(sb.toString(),param.toArray(),
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}

	@Override
	public int save(VersionLogBean bean) throws Exception {
		List<String> params = new ArrayList<String>();
		params.add(bean.getName());
		params.add(bean.getContent());
		params.add(bean.getCjr());
		String sql = "insert into frm_versionlog(id,name,content,cjr,cjsj) values(SEQ_VERSION_LOG_ID.nextval,?,?,?,sysdate)";
		return this.jdbcTemplate.update(sql,params.toArray());
	}

	@Override
	public int update(VersionLogBean bean) throws Exception {
		List param = new ArrayList<>();
		String tmpSql = "update frm_versionlog set name=?,content=? where " +
				"id= ?";
		param.add(bean.getName());
		param.add(bean.getContent());
		param.add(bean.getId());
		return this.jdbcTemplate.update(tmpSql,param.toArray());
	}
}
