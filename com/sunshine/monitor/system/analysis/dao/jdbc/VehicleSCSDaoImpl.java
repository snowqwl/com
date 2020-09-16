package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.analysis.dao.VehicleSCSDao;

@Repository("vehicleSCSDao")
public class VehicleSCSDaoImpl extends ScsBaseDaoImpl implements VehicleSCSDao{

	
	public Map<CarKey, VehicleEntity> getVehicleList(List<CarKey> cars) throws Exception{
		Map<CarKey, VehicleEntity> result = new HashMap<CarKey, VehicleEntity>();
		if (cars.size() > 1) {
			StringBuffer temp = new StringBuffer(" where ");
			for (CarKey s : cars) {
				temp.append(" (hphm = '").append(s.getCarNo()).append("' and ")
						.append(" hpzl = '").append(s.getCarType()).append(
								"') or");
			}
			String subSql = temp.substring(0, (temp.length() - 2));
			StringBuffer sql = new StringBuffer();

			
			sql.append(" select hpzl,hphm,clpp1,csys,fzjg from jm_zyk_vehicle ").append(subSql);//v_wbzy_bz_jdcxx 此表不存在
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

 }
