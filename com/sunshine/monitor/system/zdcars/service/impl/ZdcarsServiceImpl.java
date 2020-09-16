package com.sunshine.monitor.system.zdcars.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.zdcars.bean.CarS;
import com.sunshine.monitor.system.zdcars.dao.ZdcarsDao;
import com.sunshine.monitor.system.zdcars.service.ZdCarsService;



@Service
@Transactional(readOnly = true)
public class ZdcarsServiceImpl implements ZdCarsService {
	@Autowired
	private ZdcarsDao zdcarsDao;
	@Autowired
	private LogManager logManager;
	
	/**
	 * 车辆识别数据上传
	 */
	@OperationAnnotation(type = OperationType.BATCH_DATA_INPUT, description = "车辆图片数据上传")
	public Map<String, Object> saveCars(CarS car) throws Exception {
		
		return this.saveCarsInfo(car);
	}
	
	public Map<String, Object> saveCarsInfo(CarS car) throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		map= this.zdcarsDao.saveZdCars(car);
		Log log=new Log();
		
		return map;
	}
	
	/**
	 * 查询车俩识别结果集
	 * @return
	 * @throws Exception 
	 */
	//@OperationAnnotation(type = OperationType.DOWNLOAD_RESULT, description = "车辆识别结果数据查询")
	public Object selectCarsResult(String key,String value,HttpServletRequest request) throws Exception{
		Map<String , Object> map= this.zdcarsDao.selectCarsResult(key, value);
		Log log=new Log();
		log = getLog(request);
		log.setCznr("车辆识别结果数据查询"+"操作sql："+map.get("sql"));
		this.logManager.saveLog(log);
		return map.get("data");
	}
	
	/**
	 * 车辆识别结果数据下载
	 * @return
	 */
	@OperationAnnotation(type=OperationType.DOWNLOAD_RESULT,description = "车辆识别结果数据导出")
	public void downloadResult(){
		
	}
	
	
	private Log getLog(HttpServletRequest request){
		Log log=new Log();
		 //文档下载日志记录
        UserSession userSession = (UserSession) WebUtils
		.getSessionAttribute(request, "userSession");
        String yhdh = userSession.getSysuser().getYhdh();
        String glbm = userSession.getDepartment().getGlbm();
        String ip = request.getHeader("x-forwarded-for");        
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {        
           ip = request.getHeader("Proxy-Client-IP");        
       }        
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {        
           ip = request.getHeader("WL-Proxy-Client-IP");        
        }        
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {        
            ip = request.getRemoteAddr();        
       }  
		log.setGlbm(glbm);
		log.setIp(ip);
		log.setYhdh(yhdh);
		log.setCzlx("10000");
		return log ;
	}
	
	public ZdcarsDao getZdcarsDao() {
		return zdcarsDao;
	}

	public void setZdcarsDao(ZdcarsDao zdcarsDao) {
		this.zdcarsDao = zdcarsDao;
	}






	public LogManager getLogManager() {
		return logManager;
	}






	public void setLogManager(LogManager logManager) {
		this.logManager = logManager;
	}

	
	
	
	
}
