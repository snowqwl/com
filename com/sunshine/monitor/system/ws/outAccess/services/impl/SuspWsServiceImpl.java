package com.sunshine.monitor.system.ws.outAccess.services.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspInfoAuditDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoDao;
import com.sunshine.monitor.system.susp.exception.SuspinfoValidateException;
import com.sunshine.monitor.system.susp.service.ExternalSuspinfoManager;
import com.sunshine.monitor.system.ws.outAccess.bean.IntelApplyResult;
import com.sunshine.monitor.system.ws.outAccess.bean.IntelSuspBean;
import com.sunshine.monitor.system.ws.outAccess.dao.IntelSuspDao;
import com.sunshine.monitor.system.ws.outAccess.exception.OutAccessWSException;
import com.sunshine.monitor.system.ws.outAccess.services.WsService;
import com.sunshine.monitor.system.ws.outAccess.transform.DataXMLToBean;
import com.sunshine.monitor.system.ws.outAccess.transform.IntelSuspBeanTransform;

@Service("SuspWsServiceImpl")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor={Exception.class})
public class SuspWsServiceImpl implements WsService{
	@Autowired
	private SuspinfoDao suspinfoDao;

	@Autowired
	private SuspInfoAuditDao suspInfoAuditDao;

	@Autowired
	private IntelSuspDao intelSuspDao;
	
	@Autowired
	private ExternalSuspinfoManager externalSuspinfoManager;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	public void execute(String systemType, String businessType,
			String sn, String xml, HttpServletRequest request)
			throws OutAccessWSException {
		IntelApplyResult rets = new IntelApplyResult();
		// 解析xml
		Object obj = null;
		try {
			obj = DataXMLToBean.transform(xml, IntelSuspBean.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OutAccessWSException("102",e.getMessage(),"解析XML异常；" );
		}
		// 检查是否为处理对象
		if (!(obj instanceof IntelSuspBean)) {
			throw new OutAccessWSException("100","","请求类型不正确！现请求功能为：布控。" );
		}
		// 效验数据完整性
		IntelSuspBean bean = (IntelSuspBean) obj;
		bean.setBkrjh(bean.getBkr());
		bean.setCzr(bean.getBkr());
		bean.setCzrjh(bean.getBkrjh());
		bean.setCzrdw(bean.getBkjg());		
		try {
			bean.setCzsj(this.systemDao.getSysDate("", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		 exeSuspApply(bean);
		
	}
	
	/**
	 * 布控业务执行主体
	 * @param bean
	 *            IntelSuspBean
	 * @return IntelApplyResult
	 * @throws Exception
	 */
	private void exeSuspApply(IntelSuspBean bean) throws OutAccessWSException {
		// 保存入 INTEL_SUSPINFO 表传输信息
		
		try {
			intelSuspDao.save(bean);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OutAccessWSException("202",e.getMessage(),"传输信息保存异常");
		}
		VehSuspinfo info ;
		try {
			info = IntelSuspBeanTransform.transformVehSuspinfo(bean);
			info.setGxsj(this.systemDao.getSysDate("", true));
			externalSuspinfoManager.suspinfo( info, 
					IntelSuspBeanTransform.transformAuditApprove(bean));
			
		} catch (SuspinfoValidateException e){
			System.out.println(e.getMessage());
			throw new OutAccessWSException("202",e.getMessage(),"布控异常,布控数据不完整，"+e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new OutAccessWSException("202",e.getMessage(),"布控异常");
		}
		
		// 插入传输表
		try {
			suspInfoAuditDao.insertTransSusp("441200000000", info.getBkfw(), info.getBkxh(), "11");
		} catch (Exception e) {
			e.printStackTrace();
			throw new OutAccessWSException("202",e.getMessage(),"布控传输任务执行异常");
		}
	}

	public void checkAuthority() throws OutAccessWSException {
		//不效验
		if(false){
			new OutAccessWSException("无权限访问对应方法");
		}
	}

	public void chechData() throws OutAccessWSException {
		//不效验
		if(false){
			new OutAccessWSException("数据完整性效验不通过");
		}
	}

	public SuspinfoDao getSuspinfoDao() {
		return suspinfoDao;
	}

	public void setSuspinfoDao(SuspinfoDao suspinfoDao) {
		this.suspinfoDao = suspinfoDao;
	}

	public SuspInfoAuditDao getSuspInfoAuditDao() {
		return suspInfoAuditDao;
	}

	public void setSuspInfoAuditDao(SuspInfoAuditDao suspInfoAuditDao) {
		this.suspInfoAuditDao = suspInfoAuditDao;
	}

	public IntelSuspDao getIntelSuspDao() {
		return intelSuspDao;
	}

	public void setIntelSuspDao(IntelSuspDao intelSuspDao) {
		this.intelSuspDao = intelSuspDao;
	}
	
}
