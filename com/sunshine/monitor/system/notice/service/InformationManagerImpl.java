package com.sunshine.monitor.system.notice.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.notice.bean.SysInformation;
import com.sunshine.monitor.system.notice.bean.SysInformationreceive;
import com.sunshine.monitor.system.notice.dao.InformationDao;

@Service
public class InformationManagerImpl implements InformationManager {

	@Autowired
	private InformationDao infoDao;

	@Autowired
	private SystemDao systemDao;

	@OperationAnnotation(type=OperationType.NOTICE_QUE, description="公告查询")
	public Map<String, Object> getInformations(Map<String, Object> conditions)
			throws Exception {
		Map<String, Object> result = this.infoDao.getSysInformations(conditions);
		List<SysInformation> list = (List<SysInformation>) result.get("rows");
		for (SysInformation info : list) {
			info.setXxlb(this.systemDao.getCodeValue("100002", info
					.getXxlb()));
			info.setNr(this.systemDao
					.getCodeValue("100005", info.getSfgk()));
			List<SysInformationreceive> rlist = infoDao.getReceivesById(info.getXh());
			int c = 0;
			int t = 0;
			for (Iterator<SysInformationreceive> iter = rlist.iterator(); iter.hasNext();) {
				if (((SysInformationreceive) iter.next()).getSfjs().equals(
						"1"))
					c++;
				t++;
			}
			info.setBz(String.valueOf(c) + "/" + String.valueOf(t));
		}
		return result;
	}

	public List<Department> getDepartmentByUser() throws Exception {
		
		return this.infoDao.getDepartmentByUser();
	}
	public List<Department> getDepartmentByUser(String glbm) throws Exception {
		
		return this.infoDao.getDepartmentByUser(glbm);
	}
	
	@OperationAnnotation(type=OperationType.NOTICE_PUB, description="公告发布")
	public String saveInformation(SysInformation information)
			throws Exception {
		
		return this.infoDao.saveInformation(information);
	}
	
	@OperationAnnotation(type=OperationType.NOTICE_MOD, description="公告修改")
	public int updateInformation(SysInformation information) throws Exception {
		
		return this.infoDao.updateInformation(information);
	}
	
	@OperationAnnotation(type=OperationType.NOTICE_MOD, description="公告删除")
	public int removeInformation(SysInformation information) throws Exception {
		
		return this.infoDao.removeInformation(information);
	}
	
	public int removeReceive(String xh) throws Exception {
		
		return this.infoDao.removeReceive(xh);
	}

	public int saveReceive(SysInformationreceive sysInformationreceive)
			throws Exception {
		
		return this.infoDao.saveReceive(sysInformationreceive);
	}

	public Map<String, Object> getSysInfos(Map<String, Object> condition)
			throws Exception {
		Map<String, Object> result = this.infoDao.getSysInfos(condition);
		List<SysInformation> list = (List<SysInformation>) result.get("rows");
		for (SysInformation info : list) {
			info.setXxlb(this.systemDao.getCodeValue("100002", info
					.getXxlb()));
			info.setNr(this.systemDao
					.getCodeValue("100005", info.getSfgk()));
			info.setCzdwmc(this.systemDao.getDepartmentName(info.getCzdw()));
		}
		return result;
	}
	public List<SysInformation> getZxggInfos(Map<String, Object> condition)
	throws Exception {
		List<SysInformation> result = this.infoDao.getZxggInfos(condition);
		//List<SysInformation> list = (List<SysInformation>) result.get("rows");
		for (SysInformation info : result) {
			info.setXxlb(this.systemDao.getCodeValue("100002", info
					.getXxlb()));
			info.setNr(this.systemDao
					.getCodeValue("100005", info.getSfgk()));
			info.setCzdwmc(this.systemDao.getDepartmentName(info.getCzdw()));
		}
return result;
}
	public SysInformation getInformation(String xh, String username)
			throws Exception {
		
		return this.infoDao.getInformation(xh, username);
	}
	public List  getYhdh(String xh)
	throws Exception {
			return this.infoDao.getYhdh(xh);
	}
	public SysInformation getEditInformation(String xh)
		throws Exception {
		return this.infoDao.getEditInformation(xh);
}

	public List getReceivesForSublist(String xh) throws Exception {
		return this.infoDao.getReceivesForSublist(xh);
	}

	public SysInformationreceive getReceives(String paramString1,
			String paramString2) throws Exception {
		return this.infoDao.getReceives(paramString1, paramString2);
	
	}
	
}
