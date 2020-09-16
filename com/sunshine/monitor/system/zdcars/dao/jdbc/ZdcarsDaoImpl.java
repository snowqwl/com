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
		String sql ="insert into FW_CARS_API t values(" +
				""+ArrayIdUtil.getId()+",'" +
						""+car.getArrayId()+"" +
						"','" +
						""+car.getHphm()+"" +
						"','" +
						""+car.getHpzl()+"" +
						"','" +
						""+car.getHpzb()+"" +
						"'," +
						"to_date('"+car.getGcsj()+"','yyyy-mm-dd hh:mi:ss')" +
						",'" +
						""+car.getSbbh()+"" +
						"','" +
						""+car.getKkbh()+"" +
						"','" +
						""+car.getFxbh()+"" +
						"','" +
						""+car.getCdbh()+"" +
						"','" +
						""+car.getZpfx()+"" +
						"','" +
						""+car.getZbz()+"" +
						"','" +
						""+car.getPsly()+"" +
						"','" +
						""+car.getSbly()+"" +
						"','" +
						""+car.getZp1()+"" +
						"','" +
						""+car.getZp2()+"" +
						"','" +
						""+car.getZp3()+"" +
						"','" +
						""+car.getYxdj()+"" +
						"',to_date('"+car.getXrsj()+"','yyyy-mm-dd hh:mi:ss'))";
		int c = this.jdbcTemplate.update(sql);
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
		String sql = " select * from view_cars_result  where "+key+" like '%"+value+"%' ";
		list = this.jdbcTemplate.queryForList(sql);
		
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
