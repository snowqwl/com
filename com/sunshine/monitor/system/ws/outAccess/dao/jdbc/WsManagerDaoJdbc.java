package com.sunshine.monitor.system.ws.outAccess.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.Result;
import com.sunshine.monitor.comm.dao.jdbc.DBaseDaoImpl;
import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.comm.util.orm.helper.QueryCondition;
import com.sunshine.monitor.comm.util.orm.helper.QueryCondition.Op;
import com.sunshine.monitor.system.ws.outAccess.bean.WsManager;
import com.sunshine.monitor.system.ws.outAccess.dao.WsManagerDao;

@Repository("wsManagerDao")
public class WsManagerDaoJdbc extends DBaseDaoImpl<WsManager> implements WsManagerDao {

	public WsManager queryDetail(String systemType, String businessType)
			throws Exception {
		WsManager bean = queryForBean(WsManager.class,
				Cnd.where().and("SYSTEMTYPE",Op.EQ,systemType).
				and("BUSINESSTYPE",Op.EQ, businessType)
			);
		return bean;
	}

	/**
	 * 未测试实现，未使用调用，检查WS数据规范
	 * @deprecated
	 */
	public Result checkWsValid(String systemType, String businessType, String sn)
			throws Exception {
		Result res = new Result();
		res.setJg(0);
		if ((systemType == null) || ("".equals(systemType))
				|| (businessType == null) || ("".equals(businessType))
				|| (sn == null) || ("".equals(sn))) {
			res.setJg(0);
			res.setInfo("参数信息不正确");
			return res;
		}
		WsManager bean = queryDetail(systemType, businessType);
		if (bean == null) {
			res.setJg(0);
			res.setInfo("没有找到接口");
		} else if ("1".equals(bean.getZt())) {
			if (sn.equals(bean.getSn())) {
				res.setJg(1);
				res.setInfo("");
			} else {
				res.setJg(0);
				res.setInfo("该接口暂未开放");
			}
		} else {
			res.setJg(0);
			res.setInfo("该接口暂未开放");
		}

		return res;
	}

	@Override
	public String getTableName() {
		return "ws_manager";
	}

}
