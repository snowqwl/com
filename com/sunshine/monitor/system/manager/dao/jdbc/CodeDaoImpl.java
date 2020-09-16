package com.sunshine.monitor.system.manager.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.dao.CodeDao;

@Repository("codeDao")
public class CodeDaoImpl extends BaseDaoImpl implements CodeDao {

	public Code getCodeByTable(String dmlb, String dmz)
			throws Exception {
		/**String tmpSql = "Select * from frm_code Where dmlb='" + dmlb 
		+ "' and dmz='" + dmz + "' order by dmz";*/
		List param = new ArrayList<>();
		String tmpSql = "SELECT DMLB, DMZ, DMSM1, DMSM2, DMSM3, DMSM4, DMSX, ZT FROM FRM_CODE " +
				"WHERE DMLB=? AND DMZ=? ";
		param.add(dmlb);
		param.add(dmz);
		//List<Code> list = this.queryList(tmpSql, Code.class);
		List<Code> list = this.queryForList(tmpSql,param.toArray(),Code.class);
		if (list != null && list.size() > 0) {
			return this.queryList(tmpSql, param.toArray(),Code.class).get(0);
		}
		    return null;
	}

	public List<Code> getCodesByTable(String dmlb) {
		/**String tmpSql = "Select * from frm_code Where dmlb='" + dmlb 
		+ "' order by dmz";*/
		String tmpSql = "SELECT DMLB, DMZ, DMSM1, DMSM2, DMSM3, DMSM4, DMSX, ZT FROM FRM_CODE WHERE DMLB=? ORDER BY nlssort(DMSM1, 'NLS_SORT=SCHINESE_PINYIN_M')";
		//List<Code> list = this.queryForList(tmpSql, Code.class);
		List<Code> list = this.queryForList(tmpSql, new Object[]{dmlb}, Code.class);
		return list;
	}
	
	public Map<String, Object> getCodesbyTable(Map filter,String dmlb) {
		String tmpSql = "select * from frm_code where dmlb=? order by dmz";
		Map<String, Object> queryMap = this.findPageForMap(tmpSql,new Object[]{dmlb},
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}

	public int removeCode(Code code) throws Exception {
		List param = new ArrayList<>();
		String tmpSql = "Delete frm_code Where DMLB=? and DMZ=?";
		param.add(code.getDmlb());
		param.add(code.getDmz());
		return this.jdbcTemplate.update(tmpSql,param.toArray());
		
	}

	public int saveCode(Code code) throws Exception {
		List param = new ArrayList<>();
		String sql =
				"Select count(*) icount from frm_code Where dmlb=? and dmz=?";
		param.add(code.getDmlb());
		param.add(code.getDmz());
		String tmpSql = null;
		int count = this.jdbcTemplate.queryForInt(sql,param.toArray());
		List v1 = new ArrayList<>();
		if (count > 1) {
			tmpSql = "Update frm_code set dmsm1=?, dmsm2=?, dmsm3=?, " +
					"dmsm4=?, dmsx=?, zt=? Where dmlb=? and dmz=?";
			v1.add(code.getDmsm1());
			v1.add(code.getDmsm2());
			v1.add(code.getDmsm3());
			v1.add(code.getDmsm4());
			v1.add(code.getDmsx());
			v1.add(code.getZt());
			v1.add(code.getDmlb());
			v1.add(code.getDmz());
			
		} else {
			tmpSql =
					"Insert into frm_code(DMLB,DMZ,DMSM1,DMSM2,DMSM3,DMSM4,DMSX,ZT) values(?,?,?," +
							"?,?,?,?,?)";
			v1.add(code.getDmlb());
			v1.add(code.getDmz());
			v1.add(code.getDmsm1());
			v1.add(code.getDmsm2());
			v1.add(code.getDmsm3());
			v1.add(code.getDmsm4());
			v1.add(code.getDmsx());
			v1.add(code.getZt());
		}
		return this.jdbcTemplate.update(tmpSql,param.toArray());
	}
	
	public List<Code> getCodeDetail(String dmlb,String dmz)throws Exception{
		List param = new ArrayList<>();
		String tmpSql = "SELECT DMLB, DMZ, DMSM1, DMSM2, DMSM3, DMSM4, DMSX, ZT FROM FRM_CODE " +
				"WHERE DMLB=? and dmz=? ";
		param.add(dmlb);
		param.add(dmz);
		List<Code> list = this.queryForList(tmpSql,param.toArray(),Code.class);
		return list;
	}
	
	public int updateCode(Code code) throws Exception {
		List param = new ArrayList<>();
		String tmpSql =
				"update frm_code set dmsm1 = ?,?,?,?,?,? Where DMLB=? and DMZ=?";
		param.add(code.getDmsm1());
		param.add(code.getDmsm2());
		param.add(code.getDmsm3());
		param.add(code.getDmsm4());
		param.add(code.getDmsx());
		param.add(code.getZt());
		param.add(code.getDmlb());
		param.add(code.getDmz());
		return this.jdbcTemplate.update(tmpSql,param.toArray());
		
	}
	
}
