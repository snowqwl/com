package com.sunshine.monitor.system.query.dao.impl;

import java.util.ArrayList;
import java.util.List;
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
		List param = new ArrayList<>();
	    StringBuffer sb = new StringBuffer(" select * from ");
	    sb.append("? where gcsj > to_date(?,'yyyy-MM-dd " +
				"hh24:mi:ss')")
	      .append(" and gcsj <= to_date(?,'yyyy-MM-dd hh24:mi:ss')");
	    param.add(TABLENAME);
	    param.add(veh.getKssj());
	    param.add(veh.getJssj());
	    if(StringUtils.isNotBlank(veh.getHphm())){
	    	sb.append(" and hphm like ?");
			param.add("%" + veh.getHphm() + "%");
	    }
	    if(StringUtils.isNotBlank(veh.getKdbh())){
	    	sb.append(" and kdbh = ?");
	    	param.add(veh.getKdbh());
	    }else{
	    	//判断城市是否为空
	    	if(StringUtils.isNotBlank(veh.getCity())){
	    		sb.append(" and gcxh like ?");
	    		param.add(veh.getCity()+"%");
	    	}
	    }
	    sb.append(" order by gcsj desc ");
		return this.findPageForMap(sb.toString(), param.toArray(),page.getCurPage(),
				page.getPageSize(),
				TrafficPolicePassrec.class);
	}
}
