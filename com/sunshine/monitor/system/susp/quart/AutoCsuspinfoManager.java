package com.sunshine.monitor.system.susp.quart;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.comm.util.SuspBussinessStatu;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspInfoAuditDao;
import com.sunshine.monitor.system.susp.dao.SuspInfoCancelDao;

//@Component("autoCsuspinfoManager")
public class AutoCsuspinfoManager extends BaseDaoImpl {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/*@Autowired
	@Qualifier("suspInfoCancelDao")
	private SuspInfoCancelDao suspInfoCancelDao;
	
	@Autowired
	@Qualifier("suspinfoAuditDao")
	private SuspInfoAuditDao suspinfoAuditDao;	
	*/	
	public void doTask() {
		logger.info("到期自动撤控-启动");
		//获取自动撤控的信息
		try {
			SuspInfoCancelDao suspInfoCancelDao = (SuspInfoCancelDao)SpringApplicationContext.getStaticApplicationContext().getBean("suspInfoCancelDao");
			List<VehSuspinfo> list = suspInfoCancelDao.getAutoCsuspinfoList();
			while(list.size() > 0) {
				for(VehSuspinfo veh : list){
					this.updateSuspInfo(SuspBussinessStatu.DISPATCHED_WITHDRAWED.getDmz(), "2", veh.getBkxh(), "01");
					//不知是否要考核，所以暂时不插
					//AuditApprove  info = new  AuditApprove();
					//this.insertBusinessLog(veh, info, "系统",BusinessType.WITHDRAW_AUTO.getCode());
				}
				logger.info("到期自动撤控-结束,到期撤控记录数：{}"+list.size());
				//如果存在很多到期未撤控记录，则执行多次循环进行撤控
				if (list.size()==1000){
					list = suspInfoCancelDao.getAutoCsuspinfoList();
				} else {
					break;
				}
			}
		}catch (Exception e) {
			//logger.error(e);
			e.printStackTrace();
			
		}
		
	}

	/**
	 * 
	 * @param T_YWZT
	 * @param T_JLZT
	 * @param bkxh
	 * @param ckyydm    撤控原因代码   01已过期   02已处理  03无效信息
	 * @return
	 * @throws Exception
	 */
	
	public int updateSuspInfo(String T_YWZT, String T_JLZT, String bkxh, String ckyydm)
			throws Exception {
		
		SuspInfoAuditDao suspinfoAuditDao = (SuspInfoAuditDao)SpringApplicationContext.getStaticApplicationContext().getBean("suspinfoAuditDao");
		return suspinfoAuditDao.updateSuspInfo(T_YWZT, T_JLZT, bkxh, ckyydm);
	}


}
