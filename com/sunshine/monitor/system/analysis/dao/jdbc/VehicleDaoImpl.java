package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.analysis.dao.VehicleDao;

@Repository("vehicleDao")
public class VehicleDaoImpl extends BaseDaoImpl implements VehicleDao {

	/**
	 * 获取机动车信息
	 */
	public VehicleEntity getVehicleInfo(VehicleEntity veh)
			throws Exception {
		VehicleEntity result = null;
		String sql = "select * from v_wbzy_bz_jdcxx where hphm = '"+veh.getHphm()+"' and fzjg ='"+veh.getFzjg()+"'";
               sql +=  " and hpzl ='"+veh.getHpzl()+"'";
		//this.jdbcTemplate.queryfor
		List<VehicleEntity> list = this.queryForList(sql,VehicleEntity.class);
		if(list.size()>0){
			result = (VehicleEntity) list.get(0);
		}
		return result;
	}

	public Map<CarKey, VehicleEntity> getVehicleList(List<CarKey> cars) throws Exception{
		Map<CarKey, VehicleEntity> result = new HashMap<CarKey, VehicleEntity>();
		if (cars.size() > 1) {
			StringBuffer temp = new StringBuffer(" where ");
			for (CarKey s : cars) {
				temp.append(" (hphm = '").append(s.getCarNo().substring(1,s.getCarNo().length())).append("' and ")
						.append(" hpzl = '").append(s.getCarType()).append(
								"') or");
			}
			String subSql = temp.substring(0, (temp.length() - 2));
			StringBuffer sql = new StringBuffer();

			sql.append(" select * from v_wbzy_bz_jdcxx ").append(subSql);
			List<VehicleEntity> list = this.queryForList(sql
					.toString(), VehicleEntity.class);
			if (list != null && list.size() > 0)
				for (VehicleEntity veh : list) {
					CarKey car = new CarKey();
					car.setCarNo(veh.getHphm());
					car.setCarType(veh.getHpzl());
					result.put(car, veh);
				}
		}
		return result;

	}

	public String getVehiclePic(String xh) throws Exception {
		String result = "";
		String sql = "select zp from v_veh_picture where xh='"+xh+"'";
		//String s = this.queryDetail("v_veh_picture", "zp",xh,String.class);
		List<String> list = this.queryForList(sql,String.class);
		if(list.size()>0){
			result = (String) list.get(0);
		}
		return result;
	}

}
