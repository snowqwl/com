package com.sunshine.monitor.comm.dao.page;

import java.util.HashMap;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Entity;

/**
 * 封装响应的分页数据(数据模型)
 * 线程不安全
 * @author OYANG
 * @version v1.0.6
 */
public class DataStore extends Entity {
	
	private static final long serialVersionUID = -4959755652836494752L;
	
	/** 存储数据 */
	private Map<String, Object> data = new HashMap<String,Object>();
	
	/** 对应data中总记录数的KEY */
	private static final String TOTAL_RECORD_KEY = "total";
	
	/** 对应data中总记录列表的KEY */
	private static final String TOTAL_DATAS_KEY = "rows";
	
	/**
	 * 无参构造方法
	 */
	public DataStore() {
	}
	
	/**
	 * 
	 * @param records 总记录
	 */
	public DataStore(int records){
		data.put(TOTAL_RECORD_KEY, records);
	}
	
	/**
	 * @param datas 数据
	 */
	public DataStore(Object datas) {
		data.put(TOTAL_DATAS_KEY, datas);
	}
	/**
	 * 
	 * @param records 总记录
	 * @param obj     数据
	 */
	public DataStore(int records, Object datas) {
		this(records);
		data.put(TOTAL_DATAS_KEY, datas);
	}
	
	/**
	 * 获取数据映射实体
	 * @return
	 */
	public Map<String, Object> getEntity(){
		
		return data;
	}
	
	/**
	 * 获取总记录数
	 * @return
	 */
	public int getRecords(){
		
		return (Integer)data.get(TOTAL_RECORD_KEY);
	}
	
	/**
	 * 设置总记录数
	 * @param records
	 */
	public void setRecords(int records){
		
		data.put(TOTAL_RECORD_KEY, records);
	}
	
	/**
	 * 获取数据
	 * @return
	 */
	public int getDatas(){
		
		return (Integer)data.get(TOTAL_DATAS_KEY);
	}
	
	/**
	 * 设置数据
	 * @param datas
	 */
	public void setDatas( Object datas){
		
		data.put(TOTAL_RECORD_KEY, datas);
	}
}
