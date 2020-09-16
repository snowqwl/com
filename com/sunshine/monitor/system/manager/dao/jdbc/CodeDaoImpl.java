package com.sunshine.monitor.system.manager.dao.jdbc;

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
		String tmpSql = "SELECT DMLB, DMZ, DMSM1, DMSM2, DMSM3, DMSM4, DMSX, ZT FROM FRM_CODE WHERE DMLB='"+dmlb+"' AND DMZ='"+dmz+"' ";
		//List<Code> list = this.queryList(tmpSql, Code.class);
		List<Code> list = this.queryForList(tmpSql,Code.class);
		if (list != null && list.size() > 0) {
			return this.queryList(tmpSql, Code.class).get(0);
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
		String tmpSql = "select * from frm_code where dmlb='" + dmlb 
		+ "' order by dmz";
		Map<String, Object> queryMap = this.findPageForMap(tmpSql,
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}

	public int removeCode(Code code) throws Exception {
		String tmpSql = "Delete frm_code Where DMLB='" + code.getDmlb() + "' and DMZ='" + code.getDmz() + "'";
		return this.jdbcTemplate.update(tmpSql);
		
	}

	public int saveCode(Code code) throws Exception {
		String sql = "Select count(*) icount from frm_code Where dmlb='" + code.getDmlb() + "' and dmz='" + code.getDmz() + "'";
		String tmpSql = null;
		int count = this.jdbcTemplate.queryForInt(sql);
		if (count > 1) {
			tmpSql = "Update frm_code set dmsm1='" + code.getDmsm1() + "', dmsm2='" + code.getDmsm2() + 
			"', dmsm3='" + code.getDmsm3() + "', dmsm4='" + code.getDmsm4() + "', dmsx='" + code.getDmsx() + "', zt='" + code.getZt() + 
			"' Where dmlb='" + code.getDmlb() + "' and dmz='" + code.getDmz() + "'";
		} else {
			tmpSql = "Insert into frm_code(DMLB,DMZ,DMSM1,DMSM2,DMSM3,DMSM4,DMSX,ZT) values('" + code.getDmlb() + "','" + code.getDmz() + "','" + code.getDmsm1() + "','" 
			+ code.getDmsm2() + "','" + code.getDmsm3() + "','" + code.getDmsm4() + "','" + code.getDmsx() + "', '" + code.getZt() + "')";
		}
		return this.jdbcTemplate.update(tmpSql);
	}
	
	public List<Code> getCodeDetail(String dmlb,String dmz)throws Exception{
		String tmpSql = "SELECT DMLB, DMZ, DMSM1, DMSM2, DMSM3, DMSM4, DMSX, ZT FROM FRM_CODE WHERE DMLB='"+dmlb+"' and dmz='"+dmz+"' ";
		List<Code> list = this.queryForList(tmpSql,Code.class);
		return list;
	}
	
	public int updateCode(Code code) throws Exception {
		String tmpSql = "update frm_code set dmsm1 = '"+code.getDmsm1()+"'," +
						" dmsm2 = '"+code.getDmsm2()+"',dmsm3='"+code.getDmsm3()+"',dmsm4='"+code.getDmsm4()+"'" +
						" ,dmsx='"+code.getDmsx()+"',zt='"+code.getZt()+"' Where DMLB='" + code.getDmlb() + "' and DMZ='" + code.getDmz() + "'";
		return this.jdbcTemplate.update(tmpSql);
		
	}
	
}
