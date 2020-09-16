package com.sunshine.monitor.system.veh.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.veh.bean.VehModelBean;
import com.sunshine.monitor.system.veh.dao.VehModelDao;

@Repository("vehModelDao")
public class VehModelDaoImpl extends BaseDaoImpl implements VehModelDao {

	@Override
	public List<VehModelBean> getVehModelList(Map<String, Object> conditions)
			throws Exception {
		String sql = "select id,brand,style,year,discharge,dischargeval,front,back,profile,chinesename,vehmodel,bz,type from jm_veh_model";
		System.out.println(sql);
		return this.queryForList(sql, VehModelBean.class);
	}

	@Override
	public int insert(Map<String, Object> map) throws Exception {
		int result = 0;
		String sql = "insert into jm_veh_model(id,brand,style,year,discharge,dischargeval,front,back,profile,chinesename,vehmodel,bz,type) values(SEQ_JM_veh_model.Nextval,'"+ map.get("brand") +"',"
				+ "'"+ map.get("style") +"','"+ map.get("year") +"','"+ map.get("discharge") +"','"+ map.get("dischargeval") +"',"
						+ "'"+ map.get("front") +"','"+ map.get("back") +"','"+ map.get("profile") +"','','','"+map.get("bz")+"','"+map.get("type")+"')";
		System.out.println(sql);
		result = this.jdbcTemplate.update(sql);
		return result;
	}

	@Override
	public int updateClxh(VehModelBean bean) throws Exception {
		int result = 0;
		String sql = "update jm_veh_model set chinesename='"+bean.getChinesename()+"',vehmodel='"+bean.getVehmodel()+"' where id = '"+bean.getId()+"'";
		System.out.println(sql);
		result = this.jdbcTemplate.update(sql);
		return result;
	}

}
