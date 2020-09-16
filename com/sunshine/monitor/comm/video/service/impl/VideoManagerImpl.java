package com.sunshine.monitor.comm.video.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.video.dao.VideoDao;
import com.sunshine.monitor.comm.video.service.VideoManager;
@Service("videoManager")
public class VideoManagerImpl implements VideoManager {
	@Autowired
	private VideoDao videodao;
	
	/*
	 * 取摄像机部门结构树
	 */
	public List<Map<String,Object>> getCameraTree()throws Exception{
		return this.videodao.getCameraTree();
	}
	
	/*
	 * 取摄像机部门异步结构树
	 */
	public List<Map<String,Object>> getCameraTreeAsync(String id)throws Exception{
		return this.videodao.getCameraTreeAsync(id);
	}
	
	/*
	 * 判断节点是否为父节点
	 */
	public boolean getCountFromCamera(String id)throws Exception{
		return this.videodao.getCountFromCamera(id);
	}
}
