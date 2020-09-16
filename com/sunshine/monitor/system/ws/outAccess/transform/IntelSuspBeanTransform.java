package com.sunshine.monitor.system.ws.outAccess.transform;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.ws.outAccess.bean.IntelSuspBean;

public class IntelSuspBeanTransform {
	
	public static VehSuspinfo transformVehSuspinfo(IntelSuspBean bean){
		VehSuspinfo info = new VehSuspinfo();
		try {
			BeanUtils.copyProperties(info,bean);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		info.setYsbh(bean.getXh());
		info.setBkrmc(bean.getBkr());
		info.setYwzt("14");
		info.setJlzt("1");
		if(info.getXxly()==null) info.setXxly("5");
		info.setBjzt("0");
		info.setMhbkbj("0");
		info.setBkfw(BKFWToNew.transform(bean.getBkfw()));
		return info;
	}
	
	public static AuditApprove transformAuditApprove(IntelSuspBean bean){
		AuditApprove auditAp = new AuditApprove();
		auditAp.setCzr(bean.getCzr());
		auditAp.setCzrdw(bean.getCzrdw());
		auditAp.setCzrdwmc(bean.getCzrdw());
		auditAp.setCzsj(bean.getCzsj());
		auditAp.setCzjg("1");
		auditAp.setMs(bean.getMs());
		auditAp.setBzw("2");
		auditAp.setBy1("");
		auditAp.setBy2("");
		auditAp.setCzrjh(bean.getCzrjh());
		auditAp.setCzrmc(bean.getCzr());
		return auditAp;
	}

}
