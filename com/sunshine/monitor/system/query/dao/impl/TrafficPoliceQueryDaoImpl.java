package com.sunshine.monitor.system.query.dao.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.query.bean.TrafficPolicePassrec;
import com.sunshine.monitor.system.query.dao.TrafficPoliceQueryDao;

@Repository("trafficPoliceQueryDao")
public class TrafficPoliceQueryDaoImpl extends BaseDaoImpl implements TrafficPoliceQueryDao{

	private final String TABLENAME = "v_dc_veh_passres";
	
	public Map<String, Object> getPassrecList(TrafficPolicePassrec veh, Page page)
			throws Exception {
	    StringBuffer sb = new StringBuffer(" select * from ");
	    sb.append(TABLENAME)
	      .append(" where gcsj > to_date('").append(veh.getKssj()).append("','yyyy-MM-dd hh24:mi:ss')")
	      .append(" and gcsj <= to_date('").append(veh.getJssj()).append("','yyyy-MM-dd hh24:mi:ss')");
	    if(StringUtils.isNotBlank(veh.getHphm())){
	    	sb.append(" and hphm like '%").append(veh.getHphm()).append("%'");
	    }
	    if(StringUtils.isNotBlank(veh.getKdbh())){
	    	sb.append(" and kdbh = '").append(veh.getKdbh()).append("'");
	    }else{
	    	//判断城市是否为空
	    	if(StringUtils.isNotBlank(veh.getCity())){
	    		sb.append(" and gcxh like '").append(veh.getCity()).append("%'");
	    	}
	    }
	    sb.append(" order by gcsj desc ");
		return this.findPageForMap(sb.toString(), page.getCurPage(), page.getPageSize(), TrafficPolicePassrec.class);
	}
}
