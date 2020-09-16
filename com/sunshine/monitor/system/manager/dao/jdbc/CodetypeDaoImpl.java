package com.sunshine.monitor.system.manager.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.Codetype;
import com.sunshine.monitor.system.manager.dao.CodetypeDao;

@Repository("codetypeDao")
public class CodetypeDaoImpl extends BaseDaoImpl implements CodetypeDao {

	public Codetype getCodetype(String dmlb) throws Exception {
		//String tmpSql = "Select * from frm_codetype Where dmlb='" + dmlb + "'";
		String tmpSql = "select * from frm_codetype where dmlb = ?";
		List<Codetype> list = this.queryForList(tmpSql, new Object[]{dmlb}, Codetype.class);
		if (list == null || list.size() == 0) {
               return null;			
		}
		return list.get(0);
	}

	public List<Codetype> getCodetypes(Codetype codetype) throws Exception {
		String tmpSql = "";
		List param = new ArrayList<>();
		if ((codetype.getDmlb() != null) && (!codetype.getDmlb().equals(""))) {
			tmpSql = " Where dmlb like ?";
			param.add(codetype.getDmlb()+"%");
		}
		if ((codetype.getLbsm() != null) && (!codetype.equals(""))) {
			if (tmpSql != "") {
				tmpSql = tmpSql + "and lbsm like ?";
				param.add("%" + codetype.getLbsm() + "%");
			} else {
				tmpSql = " Where lbsm like ?";
				param.add("%" + codetype.getLbsm() + "%");
			}
		}
		tmpSql = "Select * from frm_codetype " + tmpSql + "order by dmlb";
		
		List<Codetype> list = this.jdbcTemplate.queryForList(tmpSql, param.toArray(),
				Codetype.class);
		return list;
	}

	public Map<String,Object> getCodetypesByPageSize(Map filter,Codetype codetype)
			throws Exception {
		String tmpSql = "";
		List param = new ArrayList<>();
		if (codetype.getDmlb() != null && !codetype.equals("")) {
			tmpSql = " Where dmlb like ?";
			param.add("%"+codetype.getDmlb()+"%");
		} 
		if ((codetype.getLbsm() != null) && (!codetype.getLbsm().equals(""))) {
			if (tmpSql != "") {
				tmpSql = tmpSql + "and lbsm like ?";
				param.add("%"+codetype.getLbsm()+"%");
			} else {
				tmpSql = " Where lbsm like ?";
				param.add("%"+codetype.getLbsm()+"%");
			}
		}
		tmpSql = "Select * from frm_codetype " + tmpSql + " order by dmlb";
		
		Map<String,Object> map = this.findPageForMap(tmpSql, param.toArray(),
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
	   return map;
	}

	public int removeCodetype(Codetype codetype) throws Exception {
		String tmpSql = "Delete frm_codetype Where dmlb=?";
		
		return this.jdbcTemplate.update(tmpSql,codetype.getDmlb());
		
	}

	public int saveCodetype(Codetype codetype) throws Exception {
		List v1 = new ArrayList<>();
		String sql = "Select count(*) icount from frm_codetype Where dmlb=?";
		v1.add(codetype.getDmlb());

		String tmpSql = "";
		int count = this.jdbcTemplate.queryForInt(sql,v1.toArray());
		List param = new ArrayList<>();

		if (count > 0) {
			tmpSql =
					"Update frm_codetype set LBSM=?,DMCD=?,LBSX=?, LBBZ=?,BZ=? Where DMLB=?";
			param.add(codetype.getLbsm());
			param.add(codetype.getDmcd());
			param.add(codetype.getLbsx());
			param.add(codetype.getLbbz());
			param.add(codetype.getBz());
			param.add(codetype.getDmlb());
		} else {
			tmpSql = "Insert Into frm_codetype(DMLB,LBSM,DMCD,LBSX,LBBZ,BZ) values(?,?,?,?,?,?)";
			param.add(codetype.getDmlb());
			param.add(codetype.getLbsm());
			param.add(codetype.getDmcd());
			param.add(codetype.getLbsx());
			param.add(codetype.getLbbz());
			param.add(codetype.getBz());
		}
		return this.jdbcTemplate.update(tmpSql,param.toArray());
	}

}
