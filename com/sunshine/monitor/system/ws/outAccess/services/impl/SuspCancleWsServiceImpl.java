package com.sunshine.monitor.system.ws.outAccess.services.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspInfoAuditDao;
import com.sunshine.monitor.system.susp.dao.SuspInfoCancelDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoDao;
import com.sunshine.monitor.system.susp.service.ExternalSuspinfoManager;
import com.sunshine.monitor.system.ws.outAccess.bean.IntelApplyResult;
import com.sunshine.monitor.system.ws.outAccess.bean.IntelCSuspBean;
import com.sunshine.monitor.system.ws.outAccess.dao.IntelCSuspDao;
import com.sunshine.monitor.system.ws.outAccess.dao.IntelSuspDao;
import com.sunshine.monitor.system.ws.outAccess.exception.OutAccessWSException;
import com.sunshine.monitor.system.ws.outAccess.services.WsService;
import com.sunshine.monitor.system.ws.outAccess.transform.DataXMLToBean;
import com.sunshine.monitor.system.ws.outAccess.transform.IntelCSuspBeanTransform;

@Service("SuspCancleWsServiceImpl")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor={Exception.class})
public class SuspCancleWsServiceImpl implements WsService {
	@Autowired
	@Qualifier("intelCSuspDao")
	private IntelCSuspDao intelCSuspDao;
	
	@Autowired
	private SuspInfoAuditDao suspInfoAuditDao;
	
	@Autowired
	private SuspinfoDao suspinfoDao;
	
	@Autowired
	private ExternalSuspinfoManager externalSuspinfoManager;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	public void chechData() throws OutAccessWSException {
		// TODO Auto-generated method stub

	}

	public void checkAuthority() throws OutAccessWSException {
		// TODO Auto-generated method stub

	}

	public void execute(String systemType, String businessType,
			String sn, String xml, HttpServletRequest request)
			throws OutAccessWSException {
		Object obj = null;
		try {
			obj = DataXMLToBean.transform(xml, IntelCSuspBean.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OutAccessWSException("102",e.getMessage(),"解析XML异常；" );
		}
		if (!(obj instanceof IntelCSuspBean)) {
			throw new OutAccessWSException("100","","请求类型不正确！现请求功能为：布控。" );
		}
	    IntelCSuspBean bean = (IntelCSuspBean)obj;
	    bean.setCzr(bean.getCxsqr());
	    bean.setCzrjh(bean.getCxsqrjh());
	    bean.setCzrdw(bean.getCzrdw());
	    bean.setMs(bean.getCkyyms());
	    try {
			bean.setCzsj(this.systemDao.getSysDate("", true));
		} catch (Exception e) {
			e.printStackTrace();
		}

		exeSuspCancel(bean);
	}
	
	/**
	 * 撤控业务执行主体
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	private void exeSuspCancel(IntelCSuspBean bean) throws OutAccessWSException{
		//1.把IntelCSuspBean类记录起来
		
		try {
			intelCSuspDao.save(bean);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OutAccessWSException("202",e.getMessage(),"传输信息保存异常");
		}
		
		VehSuspinfo info;
		try {
			info = IntelCSuspBeanTransform.transformVehSuspinfoCancle(bean);
			info.setGxsj(this.systemDao.getSysDate("", true));
			info.setCxsqsj(this.systemDao.getSysDate("", true));
			externalSuspinfoManager.suspinfoCancle(info, 
					IntelCSuspBeanTransform.transformAuditApprove(bean));
		} catch (Exception e) {
			e.printStackTrace();
			throw new OutAccessWSException("202",e.getMessage(),"撤控审批失败，发生异常 ！");
		}
		// 插入传输表
		try {
			VehSuspinfo localVehSuspinfo = getLocalVehSuspinfo(info.getBkxh());
			suspInfoAuditDao.insertTransSusp("441200000000", localVehSuspinfo.getBkfw(), info.getBkxh(),
					"11");
		} catch (Exception e) {
			e.printStackTrace();
			throw new OutAccessWSException("202",e.getMessage(),"撤控传输任务执行异常");
		}
	}
	
	private VehSuspinfo getLocalVehSuspinfo(String bkxh) {
		try {
			return suspinfoDao.getVehsuspinfo(bkxh);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
