package com.sunshine.monitor.system.susp.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

public interface SuspinfoCancelApproveDao extends BaseDao {
	
	
    public Map<String, Object> getSusinfoCancelApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
   
    public Map<String, Object> getSusinfoCancelApprovesOverTime(Map filter, VehSuspinfo info, String glbm) throws Exception;

    public Object getCancelApprpvesDetailForBkxh(String bkxh, String glbm) throws Exception;
    
    public Map<String, Object> getSuspinfoCancelClassApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
    
    public Map<String, Object> getSuspinfoCancelTrafficApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
    
    public List getBkfwListTree() throws Exception;
    
    
    public List getAuditApproves(AuditApprove aa) throws Exception;
    
    public int saveSuspinfoCancelApprove(AuditApprove info) throws Exception;
    
    public int updateSuspinfoCancelApprove(String ywzt, String ljzt, String bkxh);
    
    public int saveTranSusp(String csqh, String jsdw, String bkxh, String type);
    
    public int saveSuspinfoCancelApproveLog(AuditApprove info,String type, VehSuspinfo vehInfo,String ssjz) throws Exception;
    
    public List getCAlarmHistory(String bkxh) throws Exception;
    
    public int getSuspinfoCancelApproveCount(String begin, String end, String glbm);
    public int getSuspinfoCancelApproveOverTimeCount(String begin, String end, String glbm);
    
   
    
}
