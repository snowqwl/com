package com.sunshine.monitor.system.ws.outAccess.transform;

import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.ws.outAccess.bean.IntelCSuspBean;

public class IntelCSuspBeanTransform {
	public static VehSuspinfo  transformVehSuspinfoCancle(IntelCSuspBean bean ){
		VehSuspinfo info = new VehSuspinfo();
		info.setJlzt("2");
		info.setYwzt("99");
		info.setBkxh(bean.getBkxh());
		info.setCxsqr(bean.getCxsqr());
		info.setCxsqrjh(bean.getCxsqrjh());
		info.setCxsqdw(bean.getCxsqdw());
		info.setCxsqdwmc(bean.getCxsqdwmc());
		info.setCxsqsj(bean.getCxsqsj());
		info.setCkyydm(bean.getCkyydm());
		info.setCkyyms(bean.getCkyyms());
		return info;
	}
	
	public static AuditApprove transformAuditApprove (IntelCSuspBean bean ){
		AuditApprove auditAp = new AuditApprove();
		auditAp.setCzr(bean.getCzr());
		auditAp.setCzrdw(bean.getCzrdw());
		auditAp.setCzrdwmc(bean.getCzrdw());
		auditAp.setCzsj(bean.getCzsj());
		auditAp.setCzjg("1");
		auditAp.setMs(bean.getMs());
		auditAp.setBzw("4");
		auditAp.setBy1("");
		auditAp.setBy2("");
		auditAp.setCzrjh(bean.getCzrjh());
		auditAp.setCzrmc(bean.getCzr());
		return auditAp;
	}
}
