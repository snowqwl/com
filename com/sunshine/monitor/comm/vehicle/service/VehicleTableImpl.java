package com.sunshine.monitor.comm.vehicle.service;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sunshine.monitor.comm.vehicle.IVehicle;
import com.sunshine.monitor.comm.vehicle.bean.Vehicle;

public class VehicleTableImpl implements IVehicle {

	// private static NutDao appDao;
	/**
	 * 机动车状态说明
	 * A 正常; B 迁走用户; C 被盗抢;D 停驶;E 注销;G 违法处理;GE 违法未处理;I 事故未处理;L 被扣;M 强制注销
	 */
//	private static final String QUERY_HPHM_HPZL = "SELECT a.HPHM,a.HPZL,a.CLLX,a.CLXH,a.SYR,a.CSYS,a.CLPP1,a.LXDH,a.FDJH,a.CLSBDH,a.ZSXXDZ,a.FZJG,a.ZT,a.YXQZ,a.QZBFQZ,m.front,m.back,m.profile FROM SYN_HN_JDC a left join jm_veh_model m  on a.clxh=m.vehmodel WHERE a.HPHM=? AND a.HPZL=? and instr(zt, 'G') = 0 and instr(zt, 'E') = 0  and instr(zt, 'B') = 0 ORDER BY a.gxsj DESC";
//	private static final String QUERY_HPHM = "SELECT a.HPHM,a.HPZL,a.CLLX,a.CLXH,a.SYR,a.CSYS,a.CLPP1,a.LXDH,a.FDJH,a.CLSBDH,a.ZSXXDZ,a.FZJG,a.ZT,a.YXQZ,a.QZBFQZ,m.front,m.back,m.profile FROM SYN_HN_JDC a left join jm_veh_model m  on a.clxh=m.vehmodel  WHERE a.HPHM=? and instr(zt, 'G') = 0 and instr(zt, 'E') = 0  and instr(zt, 'B') = 0 ORDER BY a.gxsj DESC";
	private static final String QUERY_HPHM_HPZL = "SELECT a.HPHM,a.HPZL,a.CLLX,a.CLXH,a.SYR,a.CSYS,a.CLPP1,a.LXDH,a.FDJH,a.CLSBDH,a.ZSXXDZ,a.FZJG,a.ZT,a.YXQZ,a.QZBFQZ,m.front,m.back,m.profile FROM SYN_HN_JDC a left join jm_veh_model m  on a.clxh=m.vehmodel WHERE a.HPHM=? AND a.HPZL=? ORDER BY a.gxsj DESC";

	private static final String QUERY_HPHM = "SELECT a.HPHM,a.HPZL,a.CLLX,a.CLXH,a.SYR,a.CSYS,a.CLPP1,a.LXDH,a.FDJH,a.CLSBDH,a.ZSXXDZ,a.FZJG,a.ZT,a.YXQZ,a.QZBFQZ,m.front,m.back,m.profile FROM SYN_HN_JDC a left join jm_veh_model m  on a.clxh=m.vehmodel  WHERE a.HPHM=? ORDER BY a.gxsj DESC";

	private JdbcTemplate jdbcTemplate;
	
	private BeanPropertyRowMapper<Vehicle> bprm;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public VehicleTableImpl() {
		// appDao = NutzContext.getAppDao();
		bprm = new BeanPropertyRowMapper<Vehicle>(Vehicle.class);
	}

	@Override
	public <K, V> Object query(Map<K, V> map) throws Exception {
		List<Vehicle> list = null;
		if (map.containsKey("HPZL")) {
			list = jdbcTemplate.query(QUERY_HPHM_HPZL,
					new Object[] {map.get("HPHM"),
							map.get("HPZL")},
							bprm);
		} else {
			list =jdbcTemplate.query(QUERY_HPHM, new Object[] { map
					.get("HPHM")}, bprm);
		}
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		/*
		 * Vehicle cb = appDao.fetch(Vehicle.class, Cnd.where("hphm", "=",
		 * map.get("HPHM").toString()) .and("hpzl", "=",
		 * map.get("HPZL").toString()));
		 */
		return null;
	}
}
