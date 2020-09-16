package com.sunshine.monitor.system.manager.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SysparaDao;

@Repository("sysparaDao")
public class SysparaDaoImpl extends BaseDaoImpl implements SysparaDao {

	
	public Syspara getSyspara(String gjz, String sfgy,String glbm) throws Exception {
		String tmpSql = "";
		List<Syspara> list = null;
		if (sfgy.equals("1")) {
			tmpSql = "SELECT * FROM FRM_SYSPARA WHERE GJZ = ? AND SFGY='1'";
			list = this.queryForList(tmpSql, new Object[]{gjz}, Syspara.class);
		} else {
			tmpSql = "SELECT * FROM FRM_SYSPARA WHERE GJZ = ? AND SFGY='0' AND GLBM = ?";
			list = this.queryForList(tmpSql, new Object[]{gjz, glbm}, Syspara.class);
		}
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public Map<String,Object> getSysparas(Map filter) throws Exception {
		String tmpSql = "Select * from frm_syspara Where (sfgy='0' and glbm=?) or (sfgy='1') " +
				"order by xssx";

		//List<Syspara> list = this.jdbcTemplate.queryForList(tmpSql, Syspara.class);
		Map<String,Object> map = this.findPageForMap(tmpSql, new Object[]{filter.get("glbm")},
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
		return map;
	}

	public List getSysparas(String glbm) throws Exception {
		String tmpSql = "select GJZ, CSMC, CSZ, XSSX, SFGY, GLBM, BZ, REG, SHOWABLE from frm_syspara Where (sfgy='0' and glbm=?) Or (sfgy='1') order by xssx";
		List list = this.queryForList(tmpSql, new Object[]{glbm}, Syspara.class);
		return list ;
	}
}
