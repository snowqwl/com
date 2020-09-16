package com.sunshine.monitor.system.veh.service.impl;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.vehicle.VehQuery;
import com.sunshine.monitor.system.veh.service.FalseLicensePlateManager;

@Service("falseLicensePlateManager")
public class FalseLicensePlateManagerImpl implements FalseLicensePlateManager {
	/**
	 * 功能：检查单个车牌是否为假车牌
	 * @param request
	 * @param hpzl 号牌种类
	 * @param hphm 号牌号码
	 * @return true:判定为假车牌；false:判定为真车牌
	 */
	public boolean isFalseLicensePalteForHphm(HttpServletRequest request, String hpzl, String hphm) {
		VehQuery vq = new VehQuery();
		
		String res = vq.getVehilceInfo(request, hpzl, hphm);
		JSONObject json = JSONObject.fromObject(res);
		String result = json.getString("result");
		
		if(StringUtils.isNotBlank(result) && result.equals("1"))
			return false;
		else
			return true;
	}

}
