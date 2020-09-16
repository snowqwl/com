package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.analysis.bean.VehpassCount;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Repository("scsVehPassrecDao")
public class ScsVehPassrecDaoImpl extends ScsBaseDaoImpl implements
		ScsVehPassrecDao {

	protected static Logger log = LoggerFactory.getLogger(ScsVehPassrecDaoImpl.class);
	private String vehPassT = "veh_passrec"; // 过车视图
	
	public List<VehpassCount> getVehpasssCountList(String dateString) throws Exception {
		String tempSql = "Select t.kdbh,count(t.*) gcs,t.fxbh,t.cdbh,DATE_FORMAT(t.gcsj,'%Y-%m-%d %h:00:00') tjrq from t("+Constant.SCS_PASS_TABLE+"::gcsj >= '" + dateString + "')"
		+ " group t.kdbh,t.fxbh,t.cdbh,DATE_FORMAT(t.gcsj,'%Y-%m-%d %h:00:00')";
		return this.queryForList(tempSql, VehpassCount.class);
	}

	public List<VehPassrec> getLatestVehPassrec(String hphm, String hpzl) throws Exception {
		String tempSql = "SELECT kdbh, hphm, hpzl, hpys, cllx, csys, clpp, clwx, tp1, tp2, tp3 FROM veh_passrec WHERE hphm = '"+hphm+"' and hpzl = '"+hpzl+"' order by gcsj desc limit 1 offset 0";
		return this.queryForList(tempSql, VehPassrec.class);
	}
	
	public Map<CarKey,Integer> getViolationCount(List<CarKey> cars){
		Map<CarKey,Integer> result = new HashMap<CarKey,Integer>();
		if (cars.size() >= 1) {
			StringBuffer temp = new StringBuffer(" where ");
			for (CarKey s : cars) {
				temp.append(" (hphm = '").append(s.getCarNo()).append("' and ")
						.append(" hpzl = '").append(s.getCarType())
						.append("') or");
			}
		String subSql = temp.substring(0, (temp.length() - 2));
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select hphm as hphm,hpzl as hpzl,count(1) as cs from qbzhpt.T_WP_DZWZ ")
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
	
	public Map<String, Object> getViolationDetail(CarKey car,Map<String, Object> filter) throws Exception {
        StringBuffer condition = new StringBuffer();
        condition.append(" where hphm ='").append(car.getCarNo()).append("' and hpzl ='").append(car.getCarType()).append("'");
        StringBuffer sql = new StringBuffer();
       
        sql.append(" select wfbh as wfbh,'' as xh,'JM_ZYK_ILLEGAL' as tabname,wfsj,hphm,hpzl,wfdz,cljgmc from ")
           .append(" JM_ZYK_ILLEGAL").append(condition);

        sql.append(" order by wfsj desc ");
         
		return this.findPageForMap(sql.toString(),
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
	}
	
	/**
	 * 缺少号牌种类
	 */
	public Map<String, Object> getTrafficCount(List<String> hphms)
			throws Exception {
		Map<String,Object> result = new HashMap<String,Object>();
		StringBuffer temp = new StringBuffer();
		for(String s:hphms){
		    temp.append("'").append(s).append("'").append(",");
		}
		if(!temp.equals("")&&temp.length()>0){
		String hphm = temp.substring(0,(temp.length()-1));
		StringBuffer sql = new StringBuffer();
		sql.append(" select sum(cs) as cs,hphm as hphm from (")
		     .append(" select hphm as hphm,count(1) as cs from JM_ZYK_ILLEGAL ")
		     .append(" where hphm in (").append(hphm).append(")")
		     .append(" group by hphm ")
		   .append(" union all ")
             .append(" select hphm as hphm,count(1) as cs from JM_ZYK_ILLEGAL ")
             .append(" where hphm in (").append(hphm).append(")")
             .append(" group by hphm ")
           .append(" union all ")
             .append(" select hphm as hphm,count(1) as cs from JM_ZYK_ILLEGAL ")
             .append(" where hphm in (").append(hphm).append(")")
             .append(" group by hphm ")
           .append(") group by hphm");
		
		List<Map<String,Object>> list =  this.jdbcScsTemplate.queryForList(sql.toString());
		for(Map<String,Object> m:list){
			result.put(m.get("hphm").toString(),m.get("cs"));		
		}
		}
		return result;
	}
	
	/**
	 * 获取机动车信息
	 */
	public VehicleEntity getVehicleInfo(VehicleEntity veh)
			throws Exception {
		VehicleEntity result = null;
		String hphm = veh.getFzjg().substring(0,1) + veh.getHphm();
		String sql = "select * from JM_ZYK_VEHICLE where hphm = '"+hphm+"'";
               sql +=  " and hpzl ='"+veh.getHpzl()+"'";
               sql += " and position('G' in zt) = 0 and position('E' in zt) = 0 and position('B' in zt) = 0 order by gxrq";
		List<VehicleEntity> list = this.queryForList(sql,VehicleEntity.class);
		if(list.size()>0){
			result = (VehicleEntity) list.get(0);
		}
		return result;
	}
	
	public Map<CarKey,VehicleEntity> getVehicleList(List<CarKey> cars) throws Exception{
		Map<CarKey,VehicleEntity> result = new HashMap<CarKey,VehicleEntity>();
		if (cars.size() > 1) {
			StringBuffer temp = new StringBuffer(" where ");
			for (CarKey s : cars) {
				temp.append(" (hphm = '").append(s.getCarNo()).append("' and ")
						.append(" hpzl = '").append(s.getCarType()).append(
								"') or");
			}
			String subSql = temp.substring(0, (temp.length() - 2));
			StringBuffer sql = new StringBuffer();

			sql.append(" select * from jm_zyk_vehicle ").append(subSql);
			sql.append(" and position('G' in zt) = 0 and position('E' in zt) = 0 and position('B' in zt) = 0 order by gxrq");
			List<VehicleEntity> list = this.queryForList(sql
					.toString(), VehicleEntity.class);
			if (list != null && list.size() > 0)
				for (VehicleEntity veh : list) {
					CarKey car = new CarKey();
					car.setCarNo(veh.getHphm());
					car.setCarType(veh.getHpzl());
					result.put(car, veh);
				}
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> queryGjPassrecListExt(String sql,
			Object[] params) throws Exception {
		long pstart = System.currentTimeMillis();
		List<Map<String, Object>> list = super.jdbcScsTemplate.queryForList(sql, params);
		long pend = System.currentTimeMillis();
		log.info("JCBKSQL-PAGE:(" + (pend-pstart) + "毫秒)" + sql);
		return list;
	}

	@Override
	public Integer queryGjPassrecListTotal(String sql, Object[] params)
			throws Exception {
		log.info("JCBKSQL-COUNT:" + sql);
		int result = 0;
		try{
			Object r = this.jdbcScsTemplate.queryForInt(sql, params);
			result = Integer.valueOf(r.toString());
		}catch(EmptyResultDataAccessException e){
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public List<Map<String, Object>> queryHideDayHotView(String sql) throws Exception {
		log.info("JCBKSQL-HotView:" + sql);
		List<Map<String, Object>> list = super.jdbcScsTemplate.queryForList(sql);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getVehPassrecByGcxh(String colmuns, String gcxh) throws Exception {
		String sql = "select "+ colmuns+" from "+vehPassT+" where gcxh = '"+gcxh+"'";
		long pstart = System.currentTimeMillis();
		List<Map<String, Object>> list = super.jdbcScsTemplate.queryForList(sql);
		long pend = System.currentTimeMillis();
		log.info("JCBKSQL-getVehPassrecByGcxh:(" + (pend-pstart) + "毫秒)" + sql);
		return list;
	}
}
