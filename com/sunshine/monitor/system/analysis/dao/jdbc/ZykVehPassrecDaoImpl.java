package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.jdbc.ZykBaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.analysis.dao.ZykVehPassrecDao;

@Repository("zykVehPassrecDao")
public class ZykVehPassrecDaoImpl extends ZykBaseDaoImpl implements ZykVehPassrecDao {
	private static int THREAD_NUM = 5;//机动车线程数量最大设置为5个

	Logger log = LoggerFactory.getLogger(ZykVehPassrecDaoImpl.class);
	@SuppressWarnings("unchecked")
	public Map<CarKey,VehicleEntity> getVehicleList(List<CarKey> cars) throws Exception{
		Map<CarKey,VehicleEntity> result = new HashMap<CarKey,VehicleEntity>();
		if(cars == null || cars.size() == 0){
			return null;
		}		
		
		ExecutorService service = null;
		long pstart = System.currentTimeMillis();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(" select /*+index(t I_RHJDC_HPHM) */t.hphm,t.hpzl,t.csys,t.clpp1,t.fzjg from QBZHPT.T_WP_RHJDC t")
			   .append(" where t.hpzl = ? and t.hphm = ? ")
			   .append(" and rownum<=1 order by t.gxsj ");
//			   .append(" and instr(zt, 'G') = 0 and instr(zt, 'E') = 0 and instr(zt, 'B') = 0 ");
			
			if(cars.size()==1){
				CarKey s = cars.get(0);
				List<String> params = new ArrayList<String>();//预编译					
				params.add(s.getCarType());
				params.add(s.getCarNo());
				List<VehicleEntity> data = this.queryForList(sql.toString(),params.toArray(),VehicleEntity.class);
				if(data!=null && data.size()>0){
					result.put(s,data.get(0));
				}
			} else {
				int poolSize = cars.size();
				if(poolSize > 5){
					poolSize=THREAD_NUM;//最大设置5个
				}
				service = Executors.newFixedThreadPool(poolSize);
				List<Future> list = new Vector<Future>();				
				for (CarKey s : cars) {
					List<String> params = new ArrayList<String>();//预编译					
					params.add(s.getCarType());
					params.add(s.getCarNo());
					Future f1 = service.submit(new VehicleResultCall<List>(sql.toString(),params));
					list.add(f1);
				}
				for(Future f1 : list){
					List<VehicleEntity> data = (List)f1.get();
					if(data==null || data.size()==0){
						continue;
					}
					VehicleEntity entity = data.get(0);
					CarKey car = new CarKey();
					car.setCarNo(entity.getHphm());
					car.setCarType(entity.getHpzl());
					result.put(car,entity);	
				}
			}	
			long pend = System.currentTimeMillis();
			log.info("机动车关联查询 "+cars.size()+"条耗时-("+(pend-pstart)+"毫秒) "+sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(service != null){
				service.shutdown();
			}			
		}
		return result;
	}		
		
//		if (cars.size() > 1) {
//			StringBuffer temp = new StringBuffer(" where ");
//			for (CarKey s : cars) {
//				List<String> params = new ArrayList<String>();//预编译					
//				params.add(s.getCarType());
//				params.add(s.getCarNo());
//				temp.append(" hphm = ? and hpzl = ?");
//			}
//			String subSql = temp.substring(0, (temp.length() - 2));
//			StringBuffer sql = new StringBuffer();
//			
//			sql.append(" select csys,clpp1,fzjg from QBZHPT.T_WP_RHJDC ").append(subSql);
//			sql.append(" where hphm = ? and hpzl = ?");
//			sql.append(" and instr(zt, 'G') = 0 and instr(zt, 'E') = 0 and instr(zt, 'B') = 0 order by gxrq");
//			long start = System.currentTimeMillis();
//			List<VehicleEntity> list = this.queryForList(sql.toString(), VehicleEntity.class);
//			
//			long end = System.currentTimeMillis();
//			log.info("机动车关联查询"+list.size()+"条耗时："+(end-start)+"MS.");
//			if (list != null && list.size() > 0)
//				for (VehicleEntity veh : list) {
//					CarKey car = new CarKey();
//					car.setCarNo(veh.getHphm());
//					car.setCarType(veh.getHpzl());
//					result.put(car, veh);
//				}
//		}		
	
	/**
	 * 线程查询机动车信息
	 * @author 
	 * @param <T>
	 */
	class VehicleResultCall<T> implements Callable<T>{
		private String sql ;
		private List<String> params ;
		public VehicleResultCall(String sql, List<String> params){
			this.sql = sql ;
			this.params = params;
		}		
		@SuppressWarnings("unchecked")
		@Override
		public T call() throws Exception {
			return (T) queryVehicleJDC(sql.toString(),params);
			
		}
	}		
	
	public <T> List<? extends Object> queryVehicleJDC(String sql,List<String> params) throws Exception {
		return this.queryForList(sql.toString(),params.toArray(), VehicleEntity.class);
	}
	
	public VehicleEntity getVehicleInfo(VehicleEntity veh) throws Exception {
		VehicleEntity result = null;
		String hphm = veh.getFzjg().substring(0,1) + veh.getHphm();
		String sql = "select * from ( select * from QBZHPT.T_WP_RHJDC where hphm = '"+hphm+"'";
		       sql +=  " and hpzl ='"+veh.getHpzl()+"'";
		       sql += "  order by gxsj desc) where rownum <= 1";
		List<VehicleEntity> list = this.queryForList(sql,VehicleEntity.class);
		if(list.size()>0){
			result = (VehicleEntity) list.get(0);
		}
		return result;
	}

}
