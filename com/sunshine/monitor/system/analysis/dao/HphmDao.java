package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.HphmEntity;

/**
 * 注册地维护信息表持久层操作接口
 * @author licheng
 *
 */
public interface HphmDao {

	/**
	 * 根据条件查询出所有符合条件的实体记录集合(like 条件)
	 */
	public List<HphmEntity> getHphmList(Map<String,Object> filter);
}
