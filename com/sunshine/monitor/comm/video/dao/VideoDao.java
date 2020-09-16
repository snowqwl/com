package com.sunshine.monitor.comm.video.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;

public interface VideoDao extends BaseDao {
	
	/*
	 * 取摄像机部门结构树
	 */
	public List<Map<String,Object>> getCameraTree()throws Exception;
	
	/*
	 * 取摄像机部门异步结构树
	 */
	public List<Map<String,Object>> getCameraTreeAsync(String id)throws Exception;
	
	/*
	 * 判断节点是否为父节点
	 */
	public boolean getCountFromCamera(String id)throws Exception;
}
