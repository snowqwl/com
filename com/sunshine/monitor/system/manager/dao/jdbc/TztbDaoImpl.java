package com.sunshine.monitor.system.manager.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.PageUtil;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.TztbBean;
import com.sunshine.monitor.system.manager.dao.TztbDao;

@Repository("tztbDao")
public class TztbDaoImpl extends BaseDaoImpl implements TztbDao {
	@Override
	public Map<String, Object> queryList(Map<String, Object> filter,TztbBean bean) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" select id,title,lb,fileurl,content,cjr,cjsj,showcolor from frm_tztb c");
		sb.append(" where 1=1 ");
		if(StringUtils.isNotBlank(bean.getTitle()))
			sb.append(" and title like '%").append(bean.getTitle()).append("%'");
		
		if(StringUtils.isNotBlank(bean.getKssj()))
			sb.append(" and cjsj >=to_date('").append(bean.getKssj()).append("00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
		
		if(StringUtils.isNotBlank(bean.getJssj()))
			sb.append(" and cjsj <=to_date('").append(bean.getJssj()).append("23:59:59', 'yyyy-mm-dd hh24:mi:ss') ");

		sb.append(" order by id desc ");
		
		Map<String, Object> queryMap = this.findPageForMap(sb.toString(),
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}
	
	@Override
	public int save(TztbBean bean) throws Exception {
		String sql = "insert into frm_tztb(id,title,fileurl,content,cjr,cjsj,showcolor) "
				+ "values(seq_tztb_id.nextval,'"+bean.getTitle()+"','"+bean.getFileurl()+"','"+bean.getContent()+"','"+bean.getCjr()+"',sysdate,'"+bean.getShowcolor()+"')";
		return this.jdbcTemplate.update(sql);
	}
	
	@Override
	public int delete(String zgId) throws Exception {
		String tmpSql = "delete from frm_tztb where id= '"+zgId+"'";
		return this.jdbcTemplate.update(tmpSql);
		
	}
	
	@Override
	public int update(TztbBean bean) throws Exception {
		String tmpSql = "update frm_tztb set title='"+bean.getTitle()+"',fileurl='"+bean.getFileurl()+"',content='"+bean.getContent()+"',showcolor='"+bean.getShowcolor()+"' where id= '"+bean.getId()+"'";
		return this.jdbcTemplate.update(tmpSql);
		
	}

	@Override
	public TztbBean getById(String Id) throws Exception {
		String sb = "select id,title,lb,fileurl,content,cjr,cjsj,showcolor from frm_tztb where id='"+Id+"'";
		return this.queryForObject(sb, TztbBean.class);
	}
	
	@Override
	public Map<String, Object> queryForMainPage(Map<String, Object> filter,String title) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,title,fileurl,content,to_char(cjsj,'yyyy-MM-dd') as cjsj,showcolor from frm_tztb ");
		if(StringUtils.isNotBlank(title)){
			sb.append(" where title like '%").append(title).append("%'");
			sb.append(" or content like '%").append(title).append("%'");
		}
		sb.append(" order by id desc ");
		
		Map<String, Object> queryMap = this.findPageForMap(sb.toString(),
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
		
		int recordCount = this.getRecordCounts(sb.toString(), 0);//总记录数
		PageUtil page = new PageUtil(Integer.parseInt((String) filter.get("pageSize")), recordCount,Integer.parseInt((String) filter.get("curPage")));
		queryMap.put("page", page);
		return queryMap;
	}
}
