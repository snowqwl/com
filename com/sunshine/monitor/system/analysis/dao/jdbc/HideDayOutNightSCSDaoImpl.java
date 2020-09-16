package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.HideDayOutNightDayBean;
import com.sunshine.monitor.system.analysis.dao.HideDayOutNightSCSDao;

@Repository("dayNightSCSDao")
public class HideDayOutNightSCSDaoImpl extends ScsBaseDaoImpl implements HideDayOutNightSCSDao{

	@Override
	public List<Map<String, Object>> getDayNightListExt(Map<String, Object> filter, HideDayOutNightDayBean bean) throws Exception {
		String sql = getSql(filter,bean);
		List<Map<String, Object>> list = this.getPageDatas(sql,Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return list;
	}

	@Override
	public int getDayNightTotal(Map<String, Object> filter,
			HideDayOutNightDayBean bean) throws Exception {
		String sql = getSql(filter,bean);
		return super.getTotal(sql);
	}
	
	public String getSql(Map<String, Object> filter, HideDayOutNightDayBean bean) throws Exception{
		// 条件整理
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT t.hphm,t.hpzl,t.hpys,t.cs from (");
		sb.append(" SELECT hphm,hpzl,hpys,count(1) cs from yp_passrec_zfyc_d ");
		sb.append(" where 1=1 ");
		sb.append(" and lfz >=4 ");		
		if(StringUtils.isNotBlank(bean.getKssj()))			
			sb.append(" and ldate >= '").append(bean.getKssj()).append("'");
		if(StringUtils.isNotBlank(bean.getJssj()))			
			sb.append(" and ldate <= '").append(bean.getJssj()).append("'");
		if(StringUtils.isNotBlank(bean.getHphm()))
			sb.append(" and hphm like '").append(bean.getHphm()).append("'");
		if(StringUtils.isNotBlank(bean.getHpys()))
			sb.append(" and hpys = '").append(bean.getHpys()).append("'");		
		if(StringUtils.isNotBlank(bean.getHpzl()))
			sb.append(" and hpzl = '").append(bean.getHpzl()).append("'");			
		sb.append(" group by hphm,hpzl,hpys ");
		sb.append(" ) t ");
		sb.append(" where t.cs ").append(bean.getComple()).append(bean.getCs()).append("");
		sb.append(" order by t.cs desc");		
		return sb.toString();
	}

}
