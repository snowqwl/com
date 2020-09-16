package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.HphmEntity;
import com.sunshine.monitor.system.analysis.dao.HphmDao;

/**
 * 注册地维护信息表持久层操作实现
 * @author licheng
 *
 */
@Repository("hphmDao")
public class HphmDaoImpl extends BaseDaoImpl implements HphmDao {

	@Override
	public List<HphmEntity> getHphmList(Map<String,Object> filter) {
		List<HphmEntity> hphmList = new ArrayList<HphmEntity>();
		String dwdm = "";
		String hphm = "";
		if(filter.get("dwdm") != null && !("".equals(filter.get("dwdm").toString()))){
			dwdm = " and dwdm like '"+filter.get("dwdm").toString()+"%'";
		}
		if(filter.get("hphm") != null && !("".equals(filter.get("hphm").toString()))){
			dwdm = " and hphm like '"+filter.get("hphm").toString()+"%'";
		}
		String sql = "SELECT * FROM FRM_HPHM WHERE 1 = 1" + dwdm + hphm;
		try {
			hphmList = this.queryList(sql, HphmEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hphmList;
	}

}
