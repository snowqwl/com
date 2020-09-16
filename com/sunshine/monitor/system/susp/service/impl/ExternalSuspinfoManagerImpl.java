package com.sunshine.monitor.system.susp.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspInfoAuditDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoDao;
import com.sunshine.monitor.system.susp.exception.SuspDaoAccessException;
import com.sunshine.monitor.system.susp.exception.SuspinfoValidateException;
import com.sunshine.monitor.system.susp.service.ExternalSuspinfoManager;
import com.sunshine.monitor.system.ws.outAccess.exception.OutAccessWSException;

@Service("externalSuspinfoManager")
@Transactional(propagation=Propagation.REQUIRED)
public class ExternalSuspinfoManagerImpl implements ExternalSuspinfoManager {

	@Autowired
	private SuspinfoDao suspinfoDao;
	@Autowired
	private SuspInfoAuditDao suspInfoAuditDao;
	/**
	 * 撤控
	 */
	public void suspinfoCancle(VehSuspinfo suspinfo, AuditApprove audit)
			throws SuspinfoValidateException, SuspDaoAccessException {
		VehSuspinfo info = new VehSuspinfo();
		info.setJlzt("2");
		info.setYwzt("99");
		info.setBkxh(suspinfo.getBkxh());
		info.setCxsqr(suspinfo.getCxsqr());
		info.setCxsqrjh(suspinfo.getCxsqrjh());
		info.setCxsqdw(suspinfo.getCxsqdw());
		info.setCxsqdwmc(suspinfo.getCxsqdwmc());
		info.setCxsqsj(suspinfo.getCxsqsj());
		info.setCkyydm(suspinfo.getCkyydm());
		info.setCkyyms(suspinfo.getCkyyms());
		audit.setBkxh(info.getBkxh());
		audit.setCzrjh(suspinfo.getBkrjh());
		if (suspinfo.getCxsqr() == null || suspinfo.getCxsqr().equals("")) {
			audit.setCzr(suspinfo.getBkr());
		} else {
			audit.setCzr(suspinfo.getCxsqr());
		}
		if (audit.getCzrmc() == null || audit.getCzrmc().equals("")) {
			audit.setCzrmc(suspinfo.getCxsqrmc());
		}
		if (audit.getCzrdw() == null || audit.getCzrdw().equals("")) {
			audit.setCzrdw(suspinfo.getCxsqdw());
		}
		if (suspinfo.getBkjgmc()== null || suspinfo.getBkjgmc().equals("")) {
			audit.setCzrdwmc(suspinfo.getBkjgmc());
		} 
		
		validateSuspinfoCancle(suspinfo, audit);
		try {
			suspinfoDao.update(info);
			suspInfoAuditDao.insertAuditApprove(audit);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SuspDaoAccessException(e);
		}
	}
	/**
	 * 布控
	 */
	public void suspinfo(VehSuspinfo suspinfo, AuditApprove audit)
			throws SuspinfoValidateException, SuspDaoAccessException {
		validateSuspinfo(suspinfo, audit);
		VehSuspinfo info;
		try {
			info = suspinfoDao.save(suspinfo);
			audit.setBkxh(info.getBkxh());
			suspInfoAuditDao.insertAuditApprove(audit);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SuspDaoAccessException(e);
		}
	}

	/**
	 * 布控数据完整性检查
	 * @param suspinfo
	 * @param audit
	 * @throws SuspinfoValidateException
	 * @throws SuspDaoAccessException
	 */
	private void validateSuspinfo(VehSuspinfo suspinfo, AuditApprove audit)
			throws SuspinfoValidateException, SuspDaoAccessException {
		String czrdw = audit.getCzrdw();
		//if(StringUtils.isBlank(czrdw)) throw new SuspinfoValidateException("操作单位 为空 ！("+czrdw+")");
		//if(! czrdw.matches("\\d{12}"))  throw new SuspinfoValidateException("操作单位编号格式不正确 ！("+czrdw+")");
		
	}
	
	/**
	 * 撤控数据完整性检查
	 * @param suspinfo
	 * @param audit
	 * @throws SuspinfoValidateException
	 * @throws SuspDaoAccessException
	 */
	private void validateSuspinfoCancle(VehSuspinfo suspinfo, AuditApprove audit)
			throws SuspinfoValidateException, SuspDaoAccessException {
		
			VehSuspinfo info = findSuspinfoByBkxh(suspinfo.getBkxh());
			if(info == null) throw new SuspinfoValidateException(suspinfo.getBkxh()+"：无布控信息，无法撤控");
			
			String czrdw = audit.getCzrdw();
			//if(StringUtils.isBlank(czrdw)) throw new SuspinfoValidateException("操作单位 为空 ！");
			
			/**
			if(!info.getBkjg().equals(audit.getCzrdw())){
				throw new SuspinfoValidateException("不可以撤控其他机关布控信息！" );
			}
			*/
			
			//if(! czrdw.matches("\\d{12}"))  throw new SuspinfoValidateException("操作单位编号格式不正确 ！");
			
		}
	
	/**
	 * 根据布控序号查找布控信息
	 * @param bkxh
	 * @return
	 * @throws SuspDaoAccessException
	 */
	private VehSuspinfo findSuspinfoByBkxh(String bkxh) throws SuspDaoAccessException{
		try {
			return suspinfoDao.getVehsuspinfo(bkxh);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SuspDaoAccessException(e);
		}
	}
	
}
