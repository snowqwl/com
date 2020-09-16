package com.sunshine.monitor.system.susp.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.activemq.bean.TransAuditApprove;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

public interface SuspinfoApproveDao extends BaseDao {
	
	public Object getApprpvesDetailForBkxh111(String bkxh);
	
	public List getBkfwListTreenew() throws Exception ;
	
    public Map<String, Object> getSusinfoApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
    
    public Map<String, Object> getSusinfoApprovesOverTime(Map filter, VehSuspinfo info, String glbm) throws Exception;
    
    public Object getApprpvesDetailForBkxh(String bkxh, String glbm) throws Exception;
    
    public Map<String, Object> getSusinfoClassApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
    
    
    public Map<String, Object> getSusinfoTrafficApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
    
    public List getBkfwListTree() throws Exception;
    
    
    public List getAuditApproves(AuditApprove aa) throws Exception;
 
    
    public int saveSuspinfoApprove(AuditApprove info) throws Exception;
    
    public int saveSuspinfoApprove(TransAuditApprove info) throws Exception;
    
    public int updateSuspinfoApprove(String ywzt, String ljzt, String bkxh) throws Exception;
    
    public int saveTranSusp(String csqh, String jsdw, String bkxh, String type) throws Exception;
    
    public int saveSuspinfoApproveLog(AuditApprove info,String type, VehSuspinfo vehInfo,String ssjz) throws Exception;
    
    public int getSusupinfoApproveCount(String begin, String end, String glbm) throws Exception;
    
    public int getSusupinfoApproveOverTimeCount(String begin, String end, String glbm) throws Exception;

    
    public int getExpireCancelSuspinfoCount(String begin, String end, String glbm,String yhmc) throws Exception;
    
    public int getSuspinfoOuttimeCountCount(String begin, String end, String glbm,String yhmc) throws Exception;
    
    public Map getSuspinfoOuttime(int page,int pagesize) throws Exception;
    
    public List <Code> getUserSuspinfo(String begin, String end, String yhbh) throws Exception;
    
    public int getSuspinfoBkfwlxCount(String begin, String end, String yhbh) throws Exception;
    
    public int getSuspinfoNoBkfwlxCount(String begin, String end, String yhbh) throws Exception;
    
    public String getBkshrmc(String bkxh)throws Exception;
    
    /**
     * 批量审批-查询需要批量审批的跨省的布控数据
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> queryBatchApproveList() throws Exception; 
}
