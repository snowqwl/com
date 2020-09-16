package com.sunshine.monitor.system.manager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Prefecture;
import com.sunshine.monitor.system.manager.dao.DepartmentDao;
import com.sunshine.monitor.system.manager.dao.PrefectureDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.PrefectureManager;

@Service("prefectureManager")
@Transactional
public class PrefectureManagerImpl implements PrefectureManager{

	@Autowired
	private PrefectureDao prefectureDao;
	
	@Autowired
	@Qualifier("departmentDao")
	private DepartmentDao departmentDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	public Map<String,Object> findPrefectureForMap(Map filter, Prefecture prefecture){
		return prefectureDao.findPrefectureForMap(filter, prefecture);
	}
	
	public List getDepartmentTree(Map filter){
		return prefectureDao.getDepartmentTree(filter);
	}
	
	public List getDepartmentTreeAsync(String sjbm){
		return prefectureDao.getDepartmentTreeAsync(sjbm);
	}
	public List getPrefectureTreeAsync(String sjbm,String glbmn){
		return prefectureDao.getPrefectureTreeAsync(sjbm,glbmn);
	}
	public boolean getCountForLowerDepartment(String sjbm){
		int r = prefectureDao.getCountForLowerDepartment(sjbm);
		if(r>0){
			return true;
		} else {
			return false;
		} 
	}
	
	public List getLeaderDepartmentTree(Map filter){
		if(filter.get("audit")!=null){
			return prefectureDao.getlocalDepartmentTree(filter);
		}
		return prefectureDao.getLeaderDepartmentTree(filter);
	}
	
	public List<Map<String,Object>> getGateTree(String dwdm){
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();

		List list = null;
		try {
		if(dwdm.startsWith("4300")){
			//list =  this.prefectureDao.getSTGateTree();
			//list = this.prefectureDao.getSTOldGateTree();
			list = this.systemDao.getGateTree("direct");
			
		} else {
			//list = this.prefectureDao.getGateTree(dwdm);
			list = this.prefectureDao.getOldGateTree(dwdm);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(list.size()>0) {
			Iterator<Object> it = list.iterator();
			while(it.hasNext()){
				Map m = (Map)it.next();
				if(m.get("id")!=null && m.get("idname")!=null) {
				Map<String,Object> item = new HashMap<String,Object>(); 
				item.put("id", m.get("id").toString());
				item.put("name", m.get("idname").toString());
				item.put("pId", m.get("pid").toString());
				if("430000000000".equals(m.get("id").toString())) {
					item.put("nocheck", true);
				}
				menuNodes.add(item);
				}
			}
		}
	    return menuNodes;
	}
	/**
	 * 获取卡口树，不显示方向
	 * @param dwdm
	 * @return
	 */
	public List getOldGateTreeOnlyGate(String dwdm) {
		
		List list = null;
		try {
		if(dwdm.startsWith("4300")){
			//list = this.prefectureDao.getSTOldGateTreeOnlyGate();
			
				list = this.systemDao.getGateTree("gate");
			
		} else {
			list = this.prefectureDao.getOldGateTree(dwdm);
		}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
		
	}
	
	public List getMulGateTree(Map filter) {
		return this.prefectureDao.getMulGateTree(filter);
	}

	public List getSTGateTreeOnlyGate() {
		return this.prefectureDao.getSTGateTreeOnlyGate();
	}
	
	
	public List getCityTree(){
		return prefectureDao.getCityTree();
	}
	

	
	public boolean savePrefectureTree(String curDept, String menuids){
		boolean flag = true; boolean flag1=true; boolean flag2 = true;
		String[] ids = menuids.split(",");
		flag1 = prefectureDao.delPrefectureBydeptId(curDept);
		flag2 = prefectureDao.savePrefectureBymenuids(curDept, ids);
		if(!flag1 || !flag2){
			flag = false;
		}
		return flag;
	}
	
	public void setPrefectureDao(PrefectureDao paramPrefectureDao){
		
	}

	public List findPrefectureList(Prefecture prefecture){
		List list = this.prefectureDao.getPrefectures(prefecture);
		return list;
	}

	public Prefecture getPrefecture(String paramString){
		
		return null;
	}

	public List getRemainder(String paramString) {
		
		return null;
	}

	public int insertPrefecture(Prefecture p) throws Exception{
		return prefectureDao.insertPrefecture(p);
	}

	public int insertPrefectureTemp(Prefecture p) throws Exception{
		return prefectureDao.insertPrefectureTemp(p);
	}

	public int delAllForPrefectureTemp(String glbm) throws Exception {
		
		return prefectureDao.delAllForPrefectureTemp(glbm);
	}

	public void loadPrefecture(String glbm) throws Exception {

		Department dp = departmentDao.getDepartment(glbm);
		if(dp != null && "1".equals(dp.getZt())){
			Prefecture pre = new Prefecture();
			pre.setDwdm(glbm);
			List preList = prefectureDao.getPrefectures(pre);
			int count = 0;
			for(Object o : preList){
				Prefecture p = (Prefecture)o;
				if("0".equals(p.getJglx())) count++;
			}
			
			if(pre != null && count == 0){
				
				String key = null;

	            if( "0000000000".equals(glbm.substring(2)) ) {

	            	key = glbm.substring(0, 2) ;

	            } else if("00000000".equals(glbm.substring(4))) {

	            	key = glbm.substring( 0, 4 );

	            } else if("000000".equals(glbm.substring(6))  ){

	            	key = glbm.substring(0, 6 );

	            } else if("0000".equals(glbm.substring(8))) {

	            	key = glbm.substring( 0, 8 );

	            } else if("00".equals(glbm.substring(10)) ) {

	            	key = glbm.substring( 0, 10 );

	            }else{
	            	key = glbm;
	            }
	            
	            //处理一般部门单位的下级辖区
	            List listDepartment01 = departmentDao.getDepartmentListForGlbm(key,null);
	            for(Object o : listDepartment01){
	            	Department department = (Department)o;
	            	
	            	Prefecture prefecture = new Prefecture();
	            	prefecture.setDwdm(glbm);
	            	prefecture.setXjjg(department.getGlbm());
	            	prefecture.setJglx("0");
	            	prefecture.setJgqz("1");
	            	
	            	this.insertPrefectureTemp(prefecture);
	            }
	            
	            //处理直属部门的下级辖区
	            if(StringUtils.isNotBlank(dp.getSsjz())){
	            	if("0000".equals(glbm.substring(3,7)) && "0000".equals(glbm.substring(8))){
	            		key = glbm.substring(0, 2);
	            		
	            		String sqlCon = " and and ssjz like'"+dp.getSsjz().substring(0, 2)+"%'  ";
	            		List listDepartment02 = departmentDao.getDepartmentListForGlbm(key,sqlCon);
	     	            for(Object o : listDepartment02){
	     	            	Department department = (Department)o;
	     	            	
	     	            	Prefecture prefecture = new Prefecture();
	     	            	prefecture.setDwdm(glbm);
	     	            	prefecture.setXjjg(department.getGlbm());
	     	            	prefecture.setJglx("0");
	     	            	prefecture.setJgqz("1");
	     	            	
	     	            	this.insertPrefectureTemp(prefecture);
	     	            }
		            }else if ("00".equals(glbm.substring(5, 7)) && "0000".equals(glbm.substring(8))){
		            	key = glbm.substring(0, 4);
		            	
		            	String sqlCon = " and ssjz like'"+dp.getSsjz().substring(0, 2)+"%'  ";
	            		List listDepartment02 = departmentDao.getDepartmentListForGlbm(key,sqlCon);
	     	            for(Object o : listDepartment02){
	     	            	Department department = (Department)o;
	     	            	
	     	            	Prefecture prefecture = new Prefecture();
	     	            	prefecture.setDwdm(glbm);
	     	            	prefecture.setXjjg(department.getGlbm());
	     	            	prefecture.setJglx("0");
	     	            	prefecture.setJgqz("1");
	     	            	
	     	            	this.insertPrefectureTemp(prefecture);
	     	            }
		            }
	            }
	            
	            List preTemp = prefectureDao.getPrefectureTemp(glbm);
	            for(Object o : preTemp){
	            	Prefecture prefecture = (Prefecture)o;
	            	
	            	//插入到辖区表中
	            	this.insertPrefecture(prefecture);
	            }
	            
	            //删除临时辖区表中的记录
	            this.delAllForPrefectureTemp(glbm);
			}
		}
	}
	
	public String getCityName(String dwdm){
		String dwdmmc = "";
		if(dwdm.equals("446600000000")) {
			dwdmmc = "顺德";
			return dwdmmc;
		} 
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = this.prefectureDao.getGdAnHnCityTree();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Iterator<Map<String,Object>> it = list.iterator();
		while(it.hasNext()){
			Map<String,Object> m = it.next();
			if(m.get("dwdm").toString().equals(dwdm)){
				dwdmmc = m.get("jdmc").toString();
				break;
			} else {
				dwdmmc = dwdm;
			}
		}
		return dwdmmc;
	}
	
	
	@Override
	public List<Map<String, Object>> getGdCityTree() throws Exception {
		List<Map<String, Object>> list = prefectureDao.getGdAnHnCityTree();
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("JDMC", "顺德区");
		m.put("DWDM", "446600000000");
		list.add(m);
		for(Map<String,Object> map : list){
			if(map.get("DWDM").equals("440000000000"))
				map.put("SJJD", "999999999999");
			map.put("SJJD", "440000000000");
		}
		return list;
	}
}
