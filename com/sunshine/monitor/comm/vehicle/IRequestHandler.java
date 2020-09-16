package com.sunshine.monitor.comm.vehicle;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.vehicle.tran.CSYSTranslation;
import com.sunshine.monitor.comm.vehicle.tran.DefaultTranslation;

/**
 * 
 * @author Administrator
 *
 */
public abstract class IRequestHandler implements KeyConfig {
	
	private IRequestHandler irequestHandler;
	
	private IVehicle serviceClass;

	private Logger log = LoggerFactory.getLogger(IRequestHandler.class);
	
	/**
	 * 字段名转换映射表
	 */
	protected static Map<String, String> fieldNameConverMap;
	
	/**
	 * 字段值翻译
	 */
	protected static Map<String, ITranslation> fieldValTranslationMap;
	
	static{
		fieldNameConverMap = new HashMap<String, String>();
		fieldValTranslationMap = new HashMap<String, ITranslation>();
		
		fieldNameConverMap.put("syrxxdz", "xxdz");
		fieldNameConverMap.put("clpp1", "clpp");
		fieldNameConverMap.put("jdcsyr", "syr");
		fieldNameConverMap.put("sjhm", "lxdh");
		fieldNameConverMap.put("zsxxdz", "xxdz");
		
		fieldValTranslationMap.put("hpzl", new DefaultTranslation("030107"));
		fieldValTranslationMap.put("cllx", new DefaultTranslation("030104"));
		fieldValTranslationMap.put("csys", new CSYSTranslation("030108"));
	}
	
	public IRequestHandler(){
		
	}
	
	/**
	 * 可以手工或通过IOC注入下一个处理
	 * 
	 * @param irequestHandler
	 */
	public void setIrequestHandler(IRequestHandler irequestHandler) {
		this.irequestHandler = irequestHandler;
	}
	
	public IRequestHandler getIrequestHandler() {
		return irequestHandler;
	}
	/**
	 * 执行数据处理器
	 * @return
	 */
	public IVehicle getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(IVehicle serviceClass) {
		this.serviceClass = serviceClass;
	}

	/**
	 * 是否存在下一节点链处理
	 * @return
	 */
	public boolean hasNextChain(){
		
		return getIrequestHandler() == null;
	}
	
	/**
	 * 级别设置，如果下一级别比本级别高，则可以向下一级别提交请求处理，否则放弃
	 * 数字越小，级别越大
	 * 如：1 > 2
	 * 暂时未实现
	 * @return
	 */
	public abstract int level(); 
	
	/**
	 * 具体处理方法
	 * 
	 * @param conditionMap
	 * @return
	 */
	public abstract <K,V> Object doHandler(Map<K, V> conditionMap) throws Exception;

	
	/**
	 * 判断本节点是否能处理 
	 * 
	 * @param hphm
	 * @return
	 * @throws Exception
	 */
	public abstract boolean isHandle(String hphm) throws Exception;
	
	
	public <K,V> boolean validate(Map<K, V> conditionMap) throws Exception{
		boolean flag = true;
		if(conditionMap == null || conditionMap.isEmpty()){
			log.info("查询的条件为空");
			flag = false;
		}
		V hphm = conditionMap.get(KEY_HPHM);
		V hpzl = conditionMap.get(KEY_HPZL);
		if(hphm == null || hpzl == null || 
				"".equals(hpzl) || "".equals(hpzl)){
			log.info("号牌号码或号牌种类为空,HPHM:"+ hphm+",HPZL:"+hpzl);
			flag = false;
		}
		return flag;
	}

	public String translation(String fieldName, String dmz){
		ITranslation translation = null;
		try {
			translation = fieldValTranslationMap.get(fieldName);
			if(translation != null)
				return translation.getMC(dmz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dmz;
	}
}
