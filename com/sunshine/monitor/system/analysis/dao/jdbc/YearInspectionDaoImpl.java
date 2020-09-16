package com.sunshine.monitor.system.analysis.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.JmYearInspection;
import com.sunshine.monitor.system.analysis.dao.YearInspectionDao;

@Repository("yearInspectionDao")
public class YearInspectionDaoImpl extends BaseDaoImpl implements
		YearInspectionDao {

	/**
	 * 保存到报废未年检黑名单库
	 * @param JmYearInspection 
	 * @return
	 */
	public int saveYearInspection(JmYearInspection bean) throws Exception {
		//先查询是否已存在记录
		String select_sql = "select count(1) from jm_year_inspection where hphm = '"+bean.getHphm() +"'";
		int count = this.jdbcTemplate.queryForInt(select_sql);
		//如果不存在，插入保存
		if(count == 0) {
			String insert_sql = "INSERT INTO JM_YEAR_INSPECTION(CLLX,FDJH,ZZCMC,SFZJHM,ZHZXZQH,ZZZ,ZZXZQH,DJRQ,SFZJZL,ZZXZ,CSYS,DJZSBH,HPHM,HPZL,HPYS,CLPP,CLXH,CLSBDH,JDCSYR,CCDJRQ,FPJG,JDCZT,GCJK,TP1,TP2,TP3,TZTP) VALUES ('"
				+((bean.getCllx() == null) ? "" : bean.getCllx())
				+"','"
				+((bean.getFdjh() == null) ? "" : bean.getFdjh())
				+"','"
				+((bean.getZzcmc() == null) ? "" : bean.getZzcmc())
				+"','"
				+((bean.getSfzjhm() == null) ? "" : bean.getSfzjhm())
				+"','"
				+((bean.getZhzxzqh() == null) ? "" : bean.getZhzxzqh())
				+"','"
				+((bean.getZzz() == null) ? "" : bean.getZzz())
				+"','"
				+((bean.getZzxzqh() == null) ? "" : bean.getZzxzqh())
				+"',"
				+"to_date('"+ bean.getDjrq()+ "','YYYY-MM-DD HH24:MI:SS'), '"
				+((bean.getSfzjzl() == null) ? "" : bean.getSfzjzl())
				+"','"
				+((bean.getZzxz() == null) ? "" : bean.getZzxz())
				+"','"
				+((bean.getCsys() == null)? "" : bean.getCsys())
				+"','"
				+((bean.getDjzsbh() == null) ? "" : bean.getDjzsbh())
				+"','"
				+ bean.getHphm()
				+"','"
				+ bean.getHpzl()
				+"','"
				+((bean.getHpys() == null)? "" : bean.getHpys())
				+"','"
				+((bean.getClpp() == null)? "" : bean.getClpp())
				+"','"
				+((bean.getClxh() == null)? "" : bean.getClxh())
				+"','"
				+((bean.getClsbdh() == null)? "" : bean.getClsbdh())
				+"','"
				+((bean.getJdcsyr() == null)? "" : bean.getJdcsyr())
				+"',"
				+"to_date('"+ bean.getCcdjrq()+ "','YYYY-MM-DD HH24:MI:SS'), '"
				+((bean.getFpjg() == null)? "" :bean.getFpjg())
				+"','"
				+((bean.getJdczt() == null)? "" : bean.getJdczt())
				+"','"
				+((bean.getGcjk() == null)? "" :bean.getGcjk())
				+"','"
				+((bean.getTp1() == null)? "" : bean.getTp1())
				+"','"
				+((bean.getTp2() == null)? "" : bean.getTp2())
				+"','"
				+((bean.getTp3() == null)? "" : bean.getTp3())
				+"','"
				+((bean.getTztp() == null)? "" : bean.getTztp())
				+"')";	
			this.jdbcTemplate.update(insert_sql);
		} else {
			//如果存在，更新
			String update_sql = "update JM_YEAR_INSPECTION set ";
			update_sql += "JDCZT = '"+((bean.getJdczt() == null)? "" : bean.getJdczt())+"'";
			update_sql += "where hphm = '" +bean.getHphm() +"'";
			this.jdbcTemplate.update(update_sql);
		}
		return 1;
	}

}
