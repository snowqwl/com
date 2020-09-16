package com.sunshine.monitor.system.manager.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.PageUtil;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.ZgfxBean;
import com.sunshine.monitor.system.manager.dao.ZgfxDao;

@Repository("zgfxDao")
public class ZgfxDaoImpl extends BaseDaoImpl implements ZgfxDao {
	@Override
	public Map<String, Object> queryList(Map<String, Object> filter,ZgfxBean bean) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" select id,name,bz,isshow,ispush,yxsj,status,fileurl,sbr,sbdw,sbdwmc from frm_zgfx c");
		sb.append(" where 1=1 ");
		if(StringUtils.isNotBlank(bean.getName()))
			sb.append(" and name like '%").append(bean.getName()).append("%'");
		
		if(StringUtils.isNotBlank(bean.getIsshow()))
			sb.append(" and isshow = '").append(bean.getIsshow()).append("' ");
		
		if(StringUtils.isNotBlank(bean.getIspush()))
			sb.append(" and ispush = '").append(bean.getIspush()).append("' ");
		
		if(StringUtils.isNotBlank(bean.getKssj()))
			sb.append(" and yxsj >=to_date('").append(bean.getKssj()).append("00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
		
		if(StringUtils.isNotBlank(bean.getJssj()))
			sb.append(" and yxsj <=to_date('").append(bean.getJssj()).append("23:59:59', 'yyyy-mm-dd hh24:mi:ss') ");

		sb.append(" and status = '0' ");//是否有效
		sb.append(" and yxsj>sysdate ");//有效日期
		//sb.append(" and isshow = '0' ");//是否推送到首页
		//sb.append(" and ispush = '0' ");//是否显示
		sb.append(" order by id desc ");
		
		Map<String, Object> queryMap = this.findPageForMap(sb.toString(),
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}
	
	@Override
	public int save(ZgfxBean bean) throws Exception {
		String sql = "insert into frm_zgfx(id,name,bz,isshow,ispush,yxsj,status,fileurl,sbr,sbdw,sbdwmc) "
				+ "values(seq_zgfx_id.nextval,'"+bean.getName()+"','"+bean.getBz()+"','"+bean.getIsshow()+"','"+bean.getIspush()+"',to_date('"+bean.getYxsj()+"23:59:59','yyyy-mm-dd hh24:mi:ss'),'0','"+bean.getFileurl()+"','"+bean.getSbr()+"','"+bean.getSbdw()+"','"+bean.getSbdwmc()+"')";
		return this.jdbcTemplate.update(sql);
	}
	
	@Override
	public int delete(String zgId) throws Exception {
		String tmpSql = "update frm_zgfx set status=1 where id= '"+zgId+"'";
		return this.jdbcTemplate.update(tmpSql);
		
	}
	
	@Override
	public int update(ZgfxBean bean) throws Exception {
		String tmpSql = "update frm_zgfx set name='"+bean.getName()+"',bz='"+bean.getBz()+"',isshow='"+bean.getIsshow()+"',ispush='"+bean.getIspush()+"',yxsj=to_date('"+bean.getYxsj()+" 23:59:59','yyyy-mm-dd hh24:mi:ss'),fileurl='"+bean.getFileurl()+"',sbr='"+bean.getSbr()+"',sbdw='"+bean.getSbdw()+"',sbdwmc='"+bean.getSbdwmc()+"' where id= '"+bean.getId()+"'";
		return this.jdbcTemplate.update(tmpSql);
		
	}

	@Override
	public ZgfxBean getZgfx(String zgId) throws Exception {
		String sb = "select id,name,bz,isshow,ispush,status,fileurl,to_char(yxsj,'yyyy-MM-dd') as yxsj,sbr,sbdw,sbdwmc from frm_zgfx where id='"+zgId+"'";
		return this.queryForObject(sb, ZgfxBean.class);
	}
	
	/**
	 * 首页
	 */
	@Override
	public List<ZgfxBean> queryForMainPage() throws Exception {
		String sql = "select id,name,yxsj from (select id,name,yxsj from frm_zgfx where ispush='0' and isshow='0' and status='0' and yxsj>sysdate ) where rownum<=10 order by id desc";
		return this.queryForList(sql, ZgfxBean.class);
	}

	@Override
	public Map<String, Object> queryForShowPage(Map<String, Object> filter,
			String title) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,name,bz,isshow,ispush,status,fileurl,sbr,sbdw,sbdwmc,to_char(yxsj,'yyyy-MM-dd') as yxsj from frm_zgfx ");
		sb.append(" where 1=1　");
		if(StringUtils.isNotBlank(title)){
			sb.append(" and (name like '%").append(title).append("%'");
			sb.append(" or bz like '%").append(title).append("%' ");
			sb.append(" or sbr like '%").append(title).append("%' ");
			sb.append(" or sbdwmc like '%").append(title).append("%' )");
		}
		sb.append(" and status = '0' ");//是否有效
		sb.append(" and yxsj>sysdate ");//有效日期
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
