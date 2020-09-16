package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.HphmEntity;

/**
 * 注册地维护信息业务逻辑层操作接口
 * @author licheng
 *
 */
public interface HphmManager {

	/**
	 * 得到符合条件的实体记录集合(like)
	 */
	public List<HphmEntity> getHphmList(Map<String,Object> filter);
}
