package com.sunshine.monitor.system.analysis.service.impl;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.system.analysis.bean.CombineCondition;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.bean.StatisEntity;
import com.sunshine.monitor.system.analysis.bean.SubjectEntity;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.analysis.dao.CaseTouchSCSDao;
import com.sunshine.monitor.system.analysis.dao.CombineAnalysisDao;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.dao.LinkedAnalysisDao;
import com.sunshine.monitor.system.analysis.dao.PeerVehiclesDao;
import com.sunshine.monitor.system.analysis.dao.SubjectDao;
import com.sunshine.monitor.system.analysis.dao.VehicleDao;
import com.sunshine.monitor.system.analysis.service.SubjectManager;
import com.sunshine.monitor.system.analysis.service.TimeSpaceCombineManager;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.query.dao.QueryListDao;

@Service("subjectManager")
@Transactional
public class SubjectManagerImpl implements SubjectManager {

	@Autowired
	private CombineAnalysisDao combineAnalysisDao;
	
	@Autowired
	private SubjectDao subjectDao;
	
	@Autowired
	@Qualifier("queryListDao")
	private QueryListDao queryListDao;
	
	@Autowired
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("vehicleDao")
	private VehicleDao vehicleDao;
	
	@Autowired
	@Qualifier("linkedDao")
	private LinkedAnalysisDao linkedDao;
	
	@Autowired
	@Qualifier("caseTouchSCSDao")
	private CaseTouchSCSDao caseTouchSCSDao;
	
	@Autowired
	@Qualifier("peerDao")
	private PeerVehiclesDao peerDao;
	
	@Autowired
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;
	
	@Autowired
	private TimeSpaceCombineManager timeSpaceCombineManager;

	public List<Map<String,Object>> getStatisList(SubjectEntity subject) {
		String sql="";
		int fxlx = Integer.parseInt(subject.getFxlx());
		Map filter = new HashMap();
		List<Map<String,Object>> list = null;
		Map<String,Object> resultMap = new HashMap();
		JSONObject json  = null;
		try {
		switch(fxlx){
			case 3:	
				json = JSONObject.fromObject(subject.getFxtj());
				CombineCondition cc = new CombineCondition();
				cc.setCondition(json.getString("condition"));
				cc.setCllx(json.getString("cllx"));
				cc.setHpys(json.getString("hpys"));
				cc.setCsys(json.getString("csys"));
				cc.setTjlx("1");
				filter.put("curPage", "1");
				filter.put("pageSize", "40");
				resultMap = this.timeSpaceCombineManager.districtAnalysisByScsDB(filter,cc,"");
				list = (List)resultMap.get("rows");				
			break;
			case 2:
				json = JSONObject.fromObject(subject.getFxtj());
				CombineCondition cc1 = new CombineCondition();
				cc1.setCondition(json.getString("condition"));
				cc1.setHpzl(json.getString("hpzl"));
				cc1.setHpys(json.getString("hpys"));
				cc1.setCsys(json.getString("csys"));
				cc1.setTjlx("1");
				filter.put("curPage", "1");
				filter.put("pageSize", "40");
				resultMap = this.timeSpaceCombineManager.offenInOutByScsDB(filter,cc1,"");
				list = (List)resultMap.get("rows");	
			break;
		}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/*public void setField(SubjectEntity subject,Object bean){
		Class<?>  c = bean.getClass();
		Field[] fields = c.getDeclaredFields();
		JSONObject json  = JSONObject.fromObject(subject.getFxtj());
		Iterator iter = json.keys();
		for(Field field:fields){
			while(iter.hasNext()){
				String key = iter.next().toString();
				if(field.getName().indexOf(key)!=-1){
					try {
						PropertyDescriptor pd = new PropertyDescriptor(field.getName(),c);
						Method method = pd.getWriteMethod();	
						method.invoke(c,new Object[]{json.getString(key)});						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}*/
	
	public int saveSubject(SubjectEntity subject, List<Map<String,Object>> statislist) throws Exception {
		return this.subjectDao.saveSubject(subject, statislist);
	}
	
	public Map queryStatisList(Map filter,String ztbh) {
		Map map = this.subjectDao.queryStatisList(filter, ztbh);
		try {
			List<Map<String, Object>> queryList = (List) map.get("rows");
			if (queryList.size() == 0) {
				return map;
			}
			List<CarKey> cars = new ArrayList<CarKey>();
			for (Map<String, Object> m : queryList) {
				CarKey car = new CarKey();
				car.setCarNo(m.get("HPHM").toString());
				car.setCarType(m.get("HPZL").toString());
				cars.add(car);
			}
			//Map<CarKey, Integer> wfxx = this.queryListDao.getViolationCount(cars);
			Map<CarKey, Integer> wfxx = this.illegalZYKDao.getViolationCount(cars);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (Iterator i = queryList.iterator(); i.hasNext();) {
				Map<String, Object> result = (Map<String, Object>) i.next();
				result.put("HPYSMC", this.systemDao.getCodeValue("031001",
						result.get("HPYS").toString()));
				result.put("HPZLMC", this.systemDao.getCodeValue("030107",
						result.get("HPZL").toString()));
				result.put("WFXX", wfxx.get(new CarKey(result.get("HPHM")
						.toString(), result.get("HPZL").toString())));
				list.add(result);
			}
			map.put("rows", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public SubjectEntity querySubjectDetail(String ztbh) {
		return this.subjectDao.querySubjectDetail(ztbh);
	}

	public String saveAnalysisSubject(SubjectEntity subject) throws Exception {
		return this.subjectDao.saveAnalysisSubject(subject);
	}

	public int[] saveContrailByMap(List<Map<String,Object>> list, String ztbh)throws Exception {
		return this.subjectDao.saveContrail(list, ztbh);
	}
	
	public int[] saveContrail(List<ScsVehPassrec> list, String ztbh)throws Exception {
		//return this.subjectDao.saveContrail(list, ztbh);
		return null;
	}

	public int[] savePeerInfo(List<Map<String, Object>> list, String ztbh)throws Exception {
		return this.subjectDao.savePeerInfo(list, ztbh);
	}

	@Transactional
	public int savePeerAnalysis(SubjectEntity subject,String sessionId) throws Exception {
		final String ztbh = this.subjectDao.saveAnalysisSubject(subject);
		final String fxtj = subject.getFxtj();
		new Thread(){		
			List<Map<String,Object>> contrailList = null;			
			List<Map<String,Object>> peerList = null;
			public void run() {			
				try{
				contrailList = peerDao.getContrailList(fxtj);
				peerList =  peerDao.getPeerList(fxtj);
				saveContrailByMap(contrailList, ztbh);
				savePeerInfo(peerList, ztbh);
				}catch(Exception e){
					e.printStackTrace();
				}
			}			
		}.start();				
		return 0;
	}
	
	@Transactional
	public int saveVehicleContrail(SubjectEntity subject,String sessionId)throws Exception{
		String ztbh = this.subjectDao.saveAnalysisSubject(subject);
		List<Map<String,Object>> contrailList = this.peerDao.getContrailList(sessionId);
		this.saveContrailByMap(contrailList, ztbh);
		return 0;
	}

	public Map<String, Object> loadContrail(String ztbh,
			Map<String, Object> filter) throws Exception {
		Map<String,Object> result =  this.subjectDao.loadContrail(ztbh, filter);
		List<ScsVehPassrec> list = (List<ScsVehPassrec>) result.get("rows");
		for(ScsVehPassrec veh:list){
			veh.setHpysmc(this.systemDao.getCodeValue("031001",
					veh.getHpys()));
			veh.setHpzlmc(this.systemDao.getCodeValue("030107",
					veh.getHpzl()));
			veh.setKdmc(this.systemDao.getField("dc_code_Gate","kdbh","kdmc",veh.getKdbh()).toString());
			veh.setFxmc(this.systemDao.getField("dc_code_direct","fxbh","fxmc",veh.getFxbh()).toString());
			veh.setXzqhmc(this.systemDao.getDistrictNameByXzqh(veh.getKdbh().substring(0,6)));
		}
		result.put("rows",list);
		return result;
	}

	public Map<String, Object> loadPeerInfo(String ztbh,
			Map<String, Object> filter) throws Exception {
		Map<String,Object> result =  this.subjectDao.loadPeerInfo(ztbh, filter);
		List<Map<String,Object>> list =(List<Map<String,Object>>)result.get("rows");
		
        //获取违法关联数
        List<CarKey> cars = new ArrayList<CarKey>();
        for(Map<String,Object> m:list){
        	CarKey car = new CarKey();	
        	car.setCarNo(m.get("hphm").toString());
        	car.setCarType(m.get("hpzl").toString());
        	cars.add(car);
        }
        
        //Map<CarKey,Integer> wfxx=this.queryListDao.getViolationCount(cars);
        Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
		
		for(Map<String,Object> map:list){
			map.put("hphm",map.get("HPHM").toString());
			map.put("hpys",map.get("HPYS").toString());
			map.put("hpzl",map.get("HPZL").toString());
			map.put("cs",map.get("CS").toString());
			map.put("ts",map.get("TS").toString());
			map.put("kks",map.get("KKS").toString());
			map.put("txcs",map.get("TXCS").toString());
			map.put("xzqs",map.get("XZQS").toString());

            map.put("hpysmc",this.systemDao.getCodeValue("031001",map.get("HPYS").toString()));
            map.put("hpzlmc",this.systemDao.getCodeValue("030107",map.get("HPZL").toString()));
			map.put("wfxx", wfxx.get(
					 new CarKey(map.get("HPHM").toString(), map.get("HPZL").toString())));
		}
		result.put("rows",list);
		return result;
	}

	public Map<String, Object> getPeerContrail(String ztbh,
			ScsVehPassrec veh, Map<String, Object> filter) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
        result = this.subjectDao.getPeerContrail(ztbh, veh, filter);
        //翻译字段
		List<ScsVehPassrec> rows = (List<ScsVehPassrec>) result.get("rows");
		for(int i=0;i<rows.size();i++){
			ScsVehPassrec scs = rows.get(i);
			scs.setXzqhmc(this.systemDao.getDistrictNameByXzqh(scs.getKdbh().substring(0,6)));
			scs.setKdmc(this.systemDao.getField("dc_code_Gate","kdbh","kdmc",scs.getKdbh()).toString());
			scs.setFxmc(this.systemDao.getField("dc_code_direct","fxbh","fxmc",scs.getFxbh()).toString());
			scs.setHpysmc(this.systemDao.getCodeValue("031001", scs.getHpys()));
		}
        result.put("rows", rows);
		return result;
	}

	public Map<String, Object> getPeerComparePic(String ztbh, ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		List<ScsVehPassrec[]> list = new ArrayList<ScsVehPassrec[]>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,Object> result = this.subjectDao.getPeerContrailForMap(ztbh, veh, filter);
        //翻译字段
		List<Map<String,Object>> rows = (List<Map<String,Object>>) result.get("rows");
		for(int i=0;i<rows.size();i++){
			ScsVehPassrec[] array = new ScsVehPassrec[2];
			ScsVehPassrec veh1 = new ScsVehPassrec();
			ScsVehPassrec veh2 = new ScsVehPassrec();
            
			Map<String,Object> scs = rows.get(i);
			scs.put("kdmc",this.systemDao.getField("dc_code_Gate","kdbh","kdmc",scs.get("kdbh").toString()));
			scs.put("xzqhmc",this.systemDao.getDistrictNameByXzqh(scs.get("kdbh").toString().substring(0,6)));
			scs.put("fxmc", this.systemDao.getField("dc_code_direct","fxbh","fxmc",scs.get("fxbh").toString()));
			//组装成List<ScsVehPssrec[]>
			veh1.setHphm(scs.get("hphm").toString());
			veh1.setGcsj(scs.get("gcsj").toString());
			veh1.setKdmc(scs.get("kdmc").toString());
			veh1.setFxmc(scs.get("fxmc").toString());
			veh1.setXzqhmc(scs.get("xzqhmc").toString());
			veh1.setTp1(scs.get("tp1").toString());
			
			veh2.setHphm(scs.get("mbhp").toString());
			veh2.setGcsj(scs.get("mbsj").toString());
			veh2.setKdmc(scs.get("kdmc").toString());
			veh2.setFxmc(scs.get("fxmc").toString());
			veh2.setXzqhmc(scs.get("xzqhmc").toString());
			veh2.setTp1(scs.get("mbtp").toString());

			//计算同行时间差
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//Date date1 = format.parse(scs.get("gcsj").toString());
			//Date date2 = format.parse(scs.get("mbsj").toString());
			//long second = (date1.getTime()-date2.getTime())/(1000);
			int sjc = Math.round(Math.abs(sdf.parse(scs.get("gcsj").toString()).getTime()-sdf.parse(scs.get("mbsj").toString()).getTime())/1000);
            veh1.setSjc(String.valueOf(sjc));
            
			array[0]=veh1;
			array[1]=veh2;
			list.add(array);
		}
		result.put("rows", list);
		return result;
	}

	public int[] saveLinkInfo(SubjectEntity subject,String sessionId)
			throws Exception {
		String ztbh = this.subjectDao.saveAnalysisSubject(subject);
//		List<Map<String,Object>> linkList = this.linkedDao.getLinkList(sessionId);
		return null;
	}

	public Map<String, Object> getLinkList(String ztbh,
			Map<String, Object> filter) throws Exception {
		
		Map<String,Object> result = this.subjectDao.getLinkList(ztbh, filter);
		List<Map<String,Object>> list=(List<Map<String,Object>>) result.get("rows");
		
		List<CarKey> cars = new ArrayList<CarKey>();
		for(int i=0;i<list.size();i++){
			Map<String,Object> map=(Map<String,Object>)list.get(i);
			CarKey car = new CarKey();
			car.setCarNo(map.get("hphm").toString());
        	car.setCarType(map.get("hpzl").toString());
        	cars.add(car);
        	map.put("hphm", map.get("HPHM").toString());
        	map.put("hpzl", map.get("HPZL").toString());
        	map.put("hpys", map.get("HPYS").toString());
        	map.put("cs", map.get("CS").toString());
        	map.put("kks", map.get("KKS").toString());
        	map.put("xzqs", map.get("XZQS").toString());
        	map.put("zhgcsj", map.get("ZHGCSJ").toString());
			map.put("hpzlmc",this.systemDao.getCodeValue("030107",map.get("HPZL").toString()));
			map.put("hpysmc",this.systemDao.getCodeValue("031001",map.get("HPYS").toString()));
			//map.put("csysmc",this.systemDao.getCodeValue("030108",map.get("csys")==null?"":map.get("csys").toString()));
		}
		 //获取违法关联数      
		 //Map<CarKey,Integer> wfxx=this.queryListDao.getViolationCount(cars);
		 Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
		 
		 //获取机动车信息
		 Map<CarKey,VehicleEntity> vehInfo = this.vehicleDao.getVehicleList(cars);
		 
	     for(Map<String,Object> map:list){
	    	 String hphm = map.get("HPHM").toString();
	    	 String hpzl = map.get("HPZL").toString();
	    	 VehicleEntity info = vehInfo.get(new CarKey(hphm.substring(1,hphm.length()),hpzl));
	    	 map.put("wfxx", wfxx.get(new CarKey(hphm,hpzl)));
	    	 map.put("clpp1", "");
	    	 map.put("csysmc", "");
	    	 if(info!=null){
	    		 if(info.getFzjg().equals(hphm.substring(0,2))){
	    			 map.put("clpp1", info.getClpp1());
	    			 map.put("csysmc",this.systemDao.getCodeValue("030108",info.getCsys()));
	    		 }
	    	 }
	     }
	     result.put("rows",list);
		 return result;
	}
	
	/**
	 * 保存案件对碰分析
	 * @param subject
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public int[] saveCaseTouch(SubjectEntity subject,String sessionId) throws Exception{
		String ztbh = this.subjectDao.saveAnalysisSubject(subject);
		return null;
//		List<Map<String,Object>> caseTouchList = this.caseTouchSCSDao.getCaseTouchList(subject.getFxtj());
//		return this.subjectDao.savecaseTouchInfo(ztbh,caseTouchList);
		
	}
	

}
