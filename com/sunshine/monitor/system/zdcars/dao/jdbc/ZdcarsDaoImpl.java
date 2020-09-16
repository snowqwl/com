package com.sunshine.monitor.system.zdcars.dao.jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.zdcars.bean.CarS;
import com.sunshine.monitor.system.zdcars.dao.ZdcarsDao;
import com.sunshine.monitor.system.zdcars.util.ArrayIdUtil;

@Repository("zdcarsDao")
public class ZdcarsDaoImpl extends BaseDaoImpl implements ZdcarsDao {

	public Map<String, Object> saveZdCars(CarS car) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List param = new ArrayList<>();
		String sql ="insert into FW_CARS_API t values(?,?,?,?,?,to_date(?,'yyyy-mm-dd hh:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh:mi:ss'))";
		param.add(ArrayIdUtil.getId());
		param.add(car.getArrayId());
		param.add(car.getHphm());
		param.add(car.getHpzl());
		param.add(car.getHpzb());
		param.add(car.getGcsj());
		param.add(car.getSbbh());
		param.add(car.getKkbh());
		param.add(car.getFxbh());
		param.add(car.getCdbh());
		param.add(car.getZpfx());
		param.add(car.getZbz());
		param.add(car.getPsly());
		param.add(car.getSbly());
		param.add(car.getZp1());
		param.add(car.getZp2());
		param.add(car.getZp3());
		param.add(car.getYxdj());
		param.add(car.getXrsj());

		Object[] array = param.toArray(new Object[param.size()]);
		int c = this.jdbcTemplate.update(sql,array);
		if (c > 0) {
			resultMap.put("code", "1");
			resultMap.put("msg", "导入图像分析数据成功!");
			resultMap.put("carsinfo", car);
		}
		return resultMap;
	}

	public Map<String , Object> selectCarsResult(String key,String value) throws Exception {
		List list=new ArrayList();
		if(value==null||value.equals("")){
			value= "%";
		}
		//String test = value==null||value.equals("")?"%":value ;
		String sql = " select * from view_cars_result  where ? like ? ";
		list = this.jdbcTemplate.queryForList(sql,key,new String[]{"%"+value+"%"});
		
		Map<String , Object> map=new HashMap<String,Object>();
		/*String ars[] = sql.split("'");
		StringBuffer sqllog = new StringBuffer();
		for(int i =0 ; i<ars.length;i++){
			  sqllog.append(ars[i]);
		}
		System.out.println( Arrays.toString(ars));*/
		map.put("sql", sql.toString());
		map.put("data", list);
		
		return map;
	}
			
	
}
