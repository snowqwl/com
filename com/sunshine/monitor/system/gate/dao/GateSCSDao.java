package com.sunshine.monitor.system.gate.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.comm.dao.ScsBaseDao;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateCd;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.manager.bean.Department;

public interface GateSCSDao extends ScsBaseDao {
		
	/**
	 * 根据条件查询卡口信息  不分页
	 * @param 封装好的gate查询信息    组织机构：gabh
	 * **/
	public List<CodeGate> getGatesForMap(CodeGate gate, String gabh) throws Exception;
	/**
	 * 根据卡点编号查询卡点的卡点扩展信息
	 * @param KDBHs   卡点编号以逗号分隔
	 * **/
	public List<CodeGateExtend> getGatesExtend(String KDBHs) throws Exception;
}
