package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.analysis.dao.IllegalSCSDao;

@Repository("illegalSCSDao")
public class IllegalSCSDaoImpl extends ScsBaseDaoImpl implements IllegalSCSDao{

	
	@Override
	public Map<CarKey, Integer> getViolationCount(List<CarKey> cars) throws Exception{
		Map<CarKey,Integer> result = new HashMap<CarKey,Integer>();
		if (cars.size() > 1) {
			StringBuffer temp = new StringBuffer(" where ");
			for (CarKey s : cars) {
				temp.append(" (hphm = '").append(s.getCarNo()).append("' and ")
						.append(" hpzl = '").append(s.getCarType())
						.append("') or");
			}
			String subSql = temp.substring(0, (temp.length() - 2));
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select hpzl,hphm,count(wfsj) cs  from jm_zyk_illegal ")//T_AP_VIO_VIOLATION 未能找到连接远程数据库的说明
		   .append(subSql)
		   .append(" group by hphm,hpzl");
			List<Map<String,Object>> list =  this.jdbcScsTemplate.queryForList(sql.toString());
			if(list!=null && list.size()>0)
				for(Map<String,Object> m:list){
					CarKey car = new CarKey();
					car.setCarNo(m.get("hphm").toString());
					car.setCarType(m.get("hpzl").toString());
					result.put(car,Integer.parseInt(m.get("cs").toString()));		
				}
		}
		return result;
	}

	
 }
