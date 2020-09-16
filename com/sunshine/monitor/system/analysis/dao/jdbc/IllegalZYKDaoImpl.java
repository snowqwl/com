package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.jdbc.ZykBaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Repository("illegalZYKDao")
public class IllegalZYKDaoImpl extends ZykBaseDaoImpl implements IllegalZYKDao{
	private static int THREAD_NUM = 10;//机动车线程数量最大设置为10个
	
	public Map queryOneIllegal(Map filter) throws Exception {
		String hphm = "";
		if(filter.get("hphm")!=null&&filter.get("hphm").toString()!=""){
			hphm = filter.get("hphm").toString();
		}
		String hpzl = "";
		if(filter.get("hpzl")!=null&&filter.get("hpzl").toString()!=""){
			hpzl = filter.get("hpzl").toString();
		}
		String sql = "SELECT * FROM qbzhpt.T_WP_DZWZ  WHERE hphm = '"+hphm+"' and hpzl = '"+hpzl+"' order by gxsj desc";
		//System.out.println("查询区域首次车辆违法信息sql："+sql);
		Map map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
 		return map;
	}
	
	@SuppressWarnings("unchecked")
	public Map<CarKey, Integer> getViolationCount(List<CarKey> cars) throws Exception{
		Map<CarKey,Integer> result = new HashMap<CarKey,Integer>();
		if(cars == null || cars.size() == 0){
			return result;
		}		
		ExecutorService service = null;
		long pstart = System.currentTimeMillis();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(" select /*+index(t I_DZWZ_HPHM) */t.hphm,t.hpzl,count(t.wfsj) cs  from qbzhpt.T_WP_DZWZ t ")//T_AP_VIO_VIOLATION 未能找到连接远程数据库的说明
			   .append(" where t.hpzl = ? and t.hphm = ? ")
			   .append(" group by t.hphm,t.hpzl "); 
			if(cars.size()==1){
				CarKey s = cars.get(0);
				List<String> params = new ArrayList<String>();//预编译					
				params.add(s.getCarType());
				params.add(s.getCarNo());
				List<Map<String,Object>> data = this.jdbcZykTemplate.queryForList(sql.toString(),params.toArray());
				if(data!=null && data.size()>0){
					result.put(s, Integer.parseInt(data.get(0).get("CS").toString()));
				}
			} else {
				int poolSize = cars.size();
				if(poolSize > 10){
					poolSize=THREAD_NUM;//最大设置10个
				}
//				System.out.println("违法关联线程池大小 poolSize= "+poolSize);
				service = Executors.newFixedThreadPool(poolSize);
				List<Future> list = new Vector<Future>();				
				for (CarKey s : cars) {
					List<String> params = new ArrayList<String>();//预编译					
					params.add(s.getCarType());
					params.add(s.getCarNo());
					Future f1 = service.submit(new HResultCall<List>(sql.toString(),params));
					list.add(f1);					
				}
				for(Future f1 : list){
					List<Map<String,Object>> data = (List)f1.get();
					if(data==null || data.size()==0){
						continue;
					}
					Map<String,Object> map = data.get(0);
					CarKey car = new CarKey();
					car.setCarNo(map.get("HPHM").toString());
					car.setCarType(map.get("HPZL").toString());
					result.put(car,Integer.parseInt(map.get("CS").toString()));	
				}
			}	
			long pend = System.currentTimeMillis();
			log.info("违法处理时间-(" + (pend-pstart) + "毫秒) "+sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(service != null){
				service.shutdown();
			}			
		}
			
		
		return result;
	}
	
	public Map<String, Object> getViolationDetail(CarKey car,Map<String, Object> filter) throws Exception {
        StringBuffer condition = new StringBuffer();
        condition.append(" where hphm ='").append(car.getCarNo()).append("' and hpzl ='").append(car.getCarType()).append("'");
        StringBuffer sql = new StringBuffer();
       
        sql.append(" select wfbh as wfbh,'' as xh,'qbzhpt.T_WP_DZWZ ' as tabname,wfsj,hphm,hpzl,wfdz,cljgmc from ")
           .append(" qbzhpt.T_WP_DZWZ ").append(condition);

        sql.append(" order by wfsj desc ");
         
		return this.findPageForMap(sql.toString(),
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
	}
	
	public Map<String, Object> getViolationDetail2(CarKey car,
			Map<String, Object> filter) throws Exception {
		 StringBuffer condition = new StringBuffer();
	        condition.append(" where hphm ='").append(car.getCarNo()).append("' and hpzl ='").append(car.getCarType()).append("'");
	      
	        StringBuffer sql = new StringBuffer("select wfsj,dzwzid,"
					+ " cjjg,cjjgmc,clfl,hpzl,hphm,fzjg,wfdd,wfdz,wfxw,fkje,cljgmc,cljg,clsj,wfbh,dsr,zsxxdz,zsxzqh,"
					+ " jdsbh,jdcsyr,lrr,zqmj,lrsj"
					+ " FROM qbzhpt.T_WP_DZWZ   ").append(condition).append(" order by wfsj desc");
	        
			return this.findPageForMap(sql.toString(), Integer.parseInt(filter.get("curPage").toString()),
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
		     .append(" select hphm as hphm,count(1) as cs from qbzhpt.T_WP_DZWZ  ")
		     .append(" where hphm in (").append(hphm).append(")")
		     .append(" group by hphm ")
		   .append(" union all ")
             .append(" select hphm as hphm,count(1) as cs from qbzhpt.T_WP_DZWZ  ")
             .append(" where hphm in (").append(hphm).append(")")
             .append(" group by hphm ")
           .append(" union all ")
             .append(" select hphm as hphm,count(1) as cs from qbzhpt.T_WP_DZWZ  ")
             .append(" where hphm in (").append(hphm).append(")")
             .append(" group by hphm ")
           .append(") group by hphm");
		
		List<Map<String,Object>> list =  this.jdbcZykTemplate.queryForList(sql.toString());
		for(Map<String,Object> m:list){
			result.put(m.get("hphm").toString(),m.get("cs"));		
		}
		}
		return result;
	}
	
	public Map<String,Object> getMapForIntegrateTraffic(Map<String,Object> filter, VehPassrec info,
			String citys, String wflxTab) throws Exception {
		long pstart = System.currentTimeMillis();
//		StringBuffer sql = new StringBuffer("select /*+index(t I_DZWZ_HPHM) */t.wfsj,t.dzwzid,"
//				+ " t.cjjg,t.cjjgmc,t.clfl,t.hpzl,t.hphm,t.fzjg,t.wfdd,t.wfdz,t.wfxw,t.fkje,t.cljgmc,t.cljg,t.clsj,t.wfbh,t.dsr,t.zsxxdz,t.zsxzqh,"
//				+ " t.jdsbh,t.jdcsyr,t.lrr,t.zqmj,t.lrsj"
//				+ " FROM qbzhpt.T_WP_DZWZ t where 1=1 "); 
		StringBuffer sql = new StringBuffer("select /*+index(t I_DZWZ_HPHM) */to_date(t.wfsj,'yyyy-mm-dd hh24:mi:ss') wfsj,")
				.append("t.dzwzid,t.dsr,t.hpzl,t.hphm,t.wfdz,t.cljgmc,t.cljg FROM qbzhpt.T_WP_DZWZ t")
				.append("  where 1=1 ");		
		
		if (StringUtils.isNotBlank(info.getHphm())) {
			sql.append(" and t.hphm = '").append(info.getHphm()).append("' ");
		}
		
		//if(StringUtils.isNotBlank(citys) && !"T_AP_VIO_FORCE".equals(wflxTab)){
//		if(StringUtils.isNotBlank(citys)){
//			sql.append("  and  (").append(getSqlconForCity("cjjg",citys)).append(")  ") ;
//		}
		
		if (StringUtils.isNotBlank(info.getHpzl())) {
			sql.append("  and t.hpzl = '").append(info.getHpzl()).append("' ");
		}
		
		if (StringUtils.isNotBlank(info.getKssj())) {
			sql.append(" and to_date(t.wfsj,'yyyy-mm-dd hh24:mi:ss') >= to_date('").append(info.getKssj()).append("','yyyy-mm-dd hh24:mi:ss')  ");
		}

		if (StringUtils.isNotBlank(info.getJssj())) {
//			sql.append(" and wfsj <= to_date('").append(info.getJssj()).append("','").append(temp).append("')  ");
			sql.append(" and to_date(t.wfsj,'yyyy-mm-dd hh24:mi:ss') <= to_date('").append(info.getJssj()).append("','yyyy-mm-dd hh24:mi:ss')  ");
		}
		
		sql.append(" order by t.wfsj desc"); 		
		Map<String,Object> map =  this.findPageForMap(sql.toString(), Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
		long pend = System.currentTimeMillis();
		log.info("综合-交通违法查询耗时-(" + (pend-pstart) + "毫秒)"+sql.toString());
		return map;
	}
	
//	private String getSqlconForCity(String parmp,String citys){
//		StringBuffer sb = new StringBuffer("");
//		if(StringUtils.isNotBlank(citys)){
//			String[]cityList = citys.split(",");
//			for(String city : cityList){
//				if( sb.length() > 2 ){
//					sb.append(" or ").append(parmp).append(" like '").append(city).append("%'  ");
//				}else{
//					sb.append("   ").append(parmp).append(" like '").append(city).append("%'  ");
//				}
//			}
//			
//		}
//		return sb.toString();
//	}
	
	public List<Map<String, Object>> getViolationForWfbh(String dzwzId)
			throws Exception {
		String sql = "select wfsj,dzwzid,clsj,"
				+ " cjjg,cjjgmc,clfl,hpzl,hphm,fzjg,wfdd,wfdz,wfxw,fkje,cljgmc,cljg,wfbh,dsr,zsxxdz,zsxzqh,"
				+ " jdsbh,jdcsyr,lrr,zqmj,lrsj"
				+ " FROM qbzhpt.T_WP_DZWZ  where dzwzid = '"+dzwzId+"'";
		return this.jdbcZykTemplate.queryForList(sql);
	}
	
	public VehicleEntity getVehicleInfo(VehicleEntity veh)
			throws Exception {
		VehicleEntity result = null;
		String hphm = veh.getFzjg().substring(0,1) + veh.getHphm();
		String sql = "select * from qbzhpt.T_WP_DZWZ where hphm = '"+hphm+"'";
               sql +=  " and hpzl ='"+veh.getHpzl()+"'";
               sql += " and position('G' in zt) = 0 and position('E' in zt) = 0 and position('B' in zt) = 0 order by gxrq";
		List<VehicleEntity> list = this.queryForList(sql,VehicleEntity.class);
		if(list.size()>0){
			result = (VehicleEntity) list.get(0);
		}
		return result;
	}
	
	/**
	 * 线程查询违法信息
	 * @author 
	 * @param <T>
	 */
	class HResultCall<T> implements Callable<T>{
		private String sql ;
		private List<String> params ;
		public HResultCall(String sql, List<String> params){
			this.sql = sql ;
			this.params = params;
		}		
		@SuppressWarnings("unchecked")
		@Override
		public T call() throws Exception {
			return (T) queryViolationWF(sql.toString(),params);
			
		}
	}	
	
	
	public <T> List<? extends Object> queryViolationWF(String sql,List<String> params) throws Exception {
		return this.jdbcZykTemplate.queryForList(sql,params.toArray());
	}
	
	/**
	 * 获取当前页的违法次数 2016-10-10 liumeng  
	 * @param cars
	 * @return
	 * @throws Exception
	 */
//	public Map<CarKey, Integer> getViolationCountWF(List<CarKey> cars) throws Exception{
//		Map<CarKey,Integer> result = new HashMap<CarKey,Integer>();
//		ExecutorService service = null;
//		long pstart = System.currentTimeMillis();
//		try {
//			if (cars.size() > 1) {
//				List<Future> list = new Vector<Future>();
//				service = Executors.newFixedThreadPool(10);
//				for (CarKey s : cars) {
//					StringBuffer sql = new StringBuffer();
//					List<String> params = new ArrayList<String>();//预编译					
//					params.add(s.getCarType());
//					params.add(s.getCarNo());
//					sql.append(" select /*+index(t I_DZWZ_HPHM) */hphm,hpzl,count(wfsj) cs  from qbzhpt.T_WP_DZWZ  ")//T_AP_VIO_VIOLATION 未能找到连接远程数据库的说明
//					   .append(" where hpzl = ? and hphm = ? ")
//					   .append(" group by hphm,hpzl");
//					
//					Future f1 = service.submit(new HResultCall<List>(sql.toString(),params));
//					list.add(f1);
//					
//				}
//				for(Future f1 : list){
//					List<Map<String,Object>> data = (List)f1.get();
//					if(data==null || data.size()==0){
//						continue;
//					}
//					Map<String,Object> map = data.get(0);
//					CarKey car = new CarKey();
//					car.setCarNo(map.get("HPHM").toString());
//					car.setCarType(map.get("HPZL").toString());
//					result.put(car,Integer.parseInt(map.get("CS").toString()));	
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally{
//			service.shutdown();
//		}
//			
//		long pend = System.currentTimeMillis();
//		log.info("违法处理时间-(" + (pend-pstart) + "毫秒)");
//		return result;
//	}
}
