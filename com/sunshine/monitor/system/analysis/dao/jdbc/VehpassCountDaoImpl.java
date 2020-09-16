package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.VehpassCount;
import com.sunshine.monitor.system.analysis.dao.VehpassCountDao;

@Repository("vehpassCountDao")
public class VehpassCountDaoImpl extends BaseDaoImpl implements VehpassCountDao {

	public String getVehpassMaxDate() throws Exception{
		String sql = "Select nvl(max(gxsj),sysdate) from monitor.VEHPASSCOUNT_DETAIL";
	    return this.jdbcTemplate.queryForObject(sql, String.class);
	}

	public int saveVehpassCountInfo(List<VehpassCount> vehpassList) throws Exception {
		int []result = null;
		if (vehpassList != null && vehpassList.size() > 0) {
			String []strArray = new String[vehpassList.size()];
			for (int i = 0; i < vehpassList.size(); i++) {
				strArray[i] = "Insert into monitor.VEHPASSCOUNT_DETAIL(tjrq,kdbh,gcs,gxsj)"
				  + "values(to_date('" + vehpassList.get(i).getTjrq() + "','yyyy-MM-dd HH24:mi:ss'),'" + vehpassList.get(i).getKdbh() + "'"
				  + "," + vehpassList.get(i).getGcs() + ",sysdate)";
			}
			result = this.jdbcTemplate.batchUpdate(strArray);
		}
		return result != null && result.length > 0 ? 1 : -1;
	}


}
