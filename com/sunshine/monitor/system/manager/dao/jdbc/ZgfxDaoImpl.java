package com.sunshine.monitor.system.manager.dao.jdbc;

import java.util.ArrayList;
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
		List param = new ArrayList<>();
		sb.append(" select id,name,bz,isshow,ispush,yxsj,status,fileurl,sbr,sbdw,sbdwmc from frm_zgfx c");
		sb.append(" where 1=1 ");
		if(StringUtils.isNotBlank(bean.getName())){
			sb.append(" and name like ？");
			param.add("%"+bean.getName()+"%");
		}

		if(StringUtils.isNotBlank(bean.getIsshow())){
			sb.append(" and isshow = ? ");
			param.add(bean.getIsshow());
		}

		if(StringUtils.isNotBlank(bean.getIspush())){
			sb.append(" and ispush = ? ");
			param.add(bean.getIspush());
		}

		if(StringUtils.isNotBlank(bean.getKssj())){
			sb.append(" and yxsj >=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(bean.getKssj()+"00:00:00");
		}

		if(StringUtils.isNotBlank(bean.getJssj())){
			sb.append(" and yxsj <=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(bean.getJssj()+"23:59:59");
		}

		sb.append(" and status = '0' ");//是否有效
		sb.append(" and yxsj>sysdate ");//有效日期
		//sb.append(" and isshow = '0' ");//是否推送到首页
		//sb.append(" and ispush = '0' ");//是否显示
		sb.append(" order by id desc ");
		
		Map<String, Object> queryMap = this.findPageForMap(sb.toString(),param.toArray(),
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}
	
	@Override
	public int save(ZgfxBean bean) throws Exception {
		List param = new ArrayList<>();
		String sql = "insert into frm_zgfx(id,name,bz,isshow,ispush,yxsj,status,fileurl,sbr,sbdw,sbdwmc) "
				+ "values(seq_zgfx_id.nextval,?,?,?,?," +
				"to_date(?,'yyyy-mm-dd hh24:mi:ss'),'0',?,?,?,?)";
		param.add(bean.getName());
		param.add(bean.getBz());
		param.add(bean.getIsshow());
		param.add(bean.getIspush());
		param.add(bean.getYxsj()+"23:59:59");
		param.add(bean.getFileurl());
		param.add(bean.getSbr());
		param.add(bean.getSbdw());
		param.add(bean.getSbdwmc());
		return this.jdbcTemplate.update(sql,param.toArray());
	}
	
	@Override
	public int delete(String zgId) throws Exception {
		String tmpSql = "update frm_zgfx set status=1 where id= ?";
		return this.jdbcTemplate.update(tmpSql,new Object[]{zgId});
		
	}
	
	@Override
	public int update(ZgfxBean bean) throws Exception {
		List param = new ArrayList<>();
		String tmpSql =
				"update frm_zgfx set name=?,bz=?,isshow=?,ispush=?," +
						"yxsj=to_date(?,'yyyy-mm-dd hh24:mi:ss'),fileurl=?," +
						"sbr=?,sbdw=?,sbdwmc=? where id= ?";
		param.add(bean.getName());
		param.add(bean.getBz());
		param.add(bean.getIsshow());
		param.add(bean.getIspush());
		param.add(bean.getYxsj()+"23:59:59");
		param.add(bean.getFileurl());
		param.add(bean.getSbr());
		param.add(bean.getSbdw());
		param.add(bean.getSbdwmc());
		param.add(bean.getId());
		return this.jdbcTemplate.update(tmpSql,param.toArray());
		
	}

	@Override
	public ZgfxBean getZgfx(String zgId) throws Exception {
		String sb = "select id,name,bz,isshow,ispush,status,fileurl,to_char(yxsj,'yyyy-MM-dd') as" +
				" yxsj,sbr,sbdw,sbdwmc from frm_zgfx where id=?";
		return this.queryForObject(sb, new Object[]{zgId},ZgfxBean.class);
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
		List param = new ArrayList<>();
		sb.append("select id,name,bz,isshow,ispush,status,fileurl,sbr,sbdw,sbdwmc,to_char(yxsj,'yyyy-MM-dd') as yxsj from frm_zgfx ");
		sb.append(" where 1=1　");
		if(StringUtils.isNotBlank(title)){
			sb.append(" and (name like ?");
			param.add("%"+title+"%");
			sb.append(" or bz like ? ");
			param.add("%"+title+"%");
			sb.append(" or sbr like ? ");
			param.add("%"+title+"%");
			sb.append(" or sbdwmc like ? )");
			param.add("%"+title+"%");
		}
		sb.append(" and status = '0' ");//是否有效
		sb.append(" and yxsj>sysdate ");//有效日期
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
