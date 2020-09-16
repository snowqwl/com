package com.sunshine.monitor.system.veh.service;

import javax.servlet.http.HttpServletRequest;

public interface FalseLicensePlateManager {

	/**
	 * 功能：检查单个车牌是否为假车牌
	 * @param request
	 * @param hpzl 号牌种类
	 * @param hphm 号牌号码
	 * @return true:判定为假车牌；false:判定为真车牌
	 */
	public boolean isFalseLicensePalteForHphm(HttpServletRequest request,String hpzl,String hphm);
}
