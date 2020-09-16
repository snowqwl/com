package com.ht.dc.trans.dao;

import java.util.List;

import com.ht.dc.trans.bean.TransAlarm;
import com.ht.dc.trans.bean.TransAlarmHandled;
import com.ht.dc.trans.bean.TransAuditApprove;
import com.ht.dc.trans.bean.TransCmd;
import com.ht.dc.trans.bean.TransLivetrace;
import com.ht.dc.trans.bean.TransSusp;
import com.sunshine.monitor.comm.dao.BaseDao;

public interface TransDcDao extends BaseDao {
  
	
	public int saveSuspInfo(TransSusp susp)throws Exception;
		
	public int saveSuspinfoApprove(TransAuditApprove info) throws Exception;
	
	public int updateSuspInfoForCancel(TransSusp info)throws Exception;
	
	public int insertAuditApprove(TransAuditApprove info) throws Exception;

	public int saveAlarmLink(TransAlarm bean) throws Exception;
	
	public int saveAlarmHandleLink(TransAlarmHandled handle) throws Exception;
	
	public String saveCmd(TransCmd vehAlarmCmd) throws Exception;
	
	public int saveLiveTrace(TransLivetrace liveTrace) throws Exception;
	
	public TransSusp getTransSuspDetail(String ywxh, int type)throws Exception;
	
	public List getTransList(String tablename, String type, String maxrow);

	public TransAlarm getTransAlarmDetail(String ywxh)throws Exception ;
	
	public TransAlarmHandled getTransAlarmHandledDetail(String ywxh) throws Exception ;

}
