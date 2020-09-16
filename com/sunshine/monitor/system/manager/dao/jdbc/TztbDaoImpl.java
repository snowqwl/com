package com.sunshine.monitor.system.manager.dao.jdbc;

import java.util.ArrayList;
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
		List param = new ArrayList<>();
		if(StringUtils.isNotBlank(bean.getTitle())){
			sb.append(" and title like ?");
			param.add("%"+bean.getTitle()+"%");
		}

		if(StringUtils.isNotBlank(bean.getKssj())){
			sb.append(" and cjsj >=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(bean.getKssj()+"00:00:00");
		}

		if(StringUtils.isNotBlank(bean.getJssj())){
			sb.append(" and cjsj <=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(bean.getJssj()+"23:59:59");
		}


		sb.append(" order by id desc ");
		
		Map<String, Object> queryMap = this.findPageForMap(sb.toString(),param.toArray(),
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}
	
	@Override
	public int save(TztbBean bean) throws Exception {
		List param = new ArrayList<>();
		String sql = "insert into frm_tztb(id,title,fileurl,content,cjr,cjsj,showcolor) "
				+ "values(seq_tztb_id.nextval,?,?,?," +
				"?,sysdate,?)";
		param.add(bean.getTitle());
		param.add(bean.getFileurl());
		param.add(bean.getContent());
		param.add(bean.getCjr());
		param.add(bean.getShowcolor());
		return this.jdbcTemplate.update(sql);
	}
	
	@Override
	public int delete(String zgId) throws Exception {
		String tmpSql = "delete from frm_tztb where id= ?";
		return this.jdbcTemplate.update(tmpSql,new Object[]{zgId});
		
	}
	
	@Override
	public int update(TztbBean bean) throws Exception {
		List param = new ArrayList<>();
		String tmpSql =
				"update frm_tztb set title=?,fileurl=?,content=?,showcolor" +
						"=? where id= ?";
		param.add(bean.getTitle());
		param.add(bean.getFileurl());
		param.add(bean.getContent());
		param.add(bean.getShowcolor());
		param.add(bean.getId());
		return this.jdbcTemplate.update(tmpSql);
		
	}

	@Override
	public TztbBean getById(String Id) throws Exception {
		String sb = "select id,title,lb,fileurl,content,cjr,cjsj,showcolor from frm_tztb where " +
				"id=?";
		return this.queryForObject(sb,new Object[]{Id}, TztbBean.class);
	}
	
	@Override
	public Map<String, Object> queryForMainPage(Map<String, Object> filter,String title) throws Exception {
		StringBuffer sb = new StringBuffer();
		List param = new ArrayList<>();
		sb.append("select id,title,fileurl,content,to_char(cjsj,'yyyy-MM-dd') as cjsj,showcolor from frm_tztb ");
		if(StringUtils.isNotBlank(title)){
			sb.append(" where title like ?");
			sb.append(" or content like ?");
			param.add("%"+title+"%");
			param.add("%"+title+"%");
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
}
