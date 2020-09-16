package com.sunshine.monitor.comm.vehicle.chain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.bean.CommonEntity;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.comm.vehicle.IRequestHandler;
import com.sunshine.monitor.comm.vehicle.IVehicle;
import com.sunshine.monitor.comm.vehicle.bean.Vehicle;
import com.sunshine.monitor.comm.vehicle.tran.CSYSTranslation;

public class TableRequestHandler extends IRequestHandler {

	private Logger log = LoggerFactory.getLogger(TableRequestHandler.class);
	
	private static JsonConfig jsonConfig = new JsonConfig();
	
	public TableRequestHandler(){
		
	}

	@Override
	public <K,V> Object doHandler(Map<K, V> conditionMap) throws Exception {
		log.debug("进入数据库表查询........");
		Vehicle cb = null;
		JSONObject jsonObj = null;
		if(validate(conditionMap)){
			String hphm = conditionMap.get(KEY_HPHM).toString();
			if (isHandle(hphm)) {
				log.debug("查询本省车辆信息........");
				IVehicle iv = getServiceClass();
				cb = (Vehicle) iv.query(conditionMap);
				if(cb != null){
					CSYSTranslation code = new CSYSTranslation("030117");
					String ztmc = code.getMC(cb.getZt());
					cb.setZtmc(ztmc);
					
					/* 拼凑机动车模型车辆图片的访问地址  */
					if(StringUtils.isNotBlank(cb.getFront())){
						CommonEntity vehModelPic = SpringApplicationContext.getBean("vehModelPic", CommonEntity.class);					
						String url = "ftp://"+vehModelPic.getUsername()+":"+vehModelPic.getPassword()+"@"+vehModelPic.getIp()+"/"+vehModelPic.getFilename()+"/";
						cb.setFront(url+cb.getFront());//正面
						cb.setBack(url+cb.getBack());//背面
						cb.setProfile(url+cb.getProfile());//侧面
					}
					jsonObj = converAndTranslation(cb);
					return jsonObj;
				}
			} 
			if(cb == null && !hasNextChain()){
				// 转入下一个节点处
				log.debug("请求下一个节点处理.......");
				return getIrequestHandler().doHandler(conditionMap);
			} else {
				log.debug("本节点处理结果为空或者未设置下一节点链");
			}
			jsonObj = JSONObject.fromObject("{result:-1}");
		}
		return jsonObj;
	}
	
	@Override
	public int level() {
		
		return 2;
	}

	@Override
	public boolean isHandle(String hphm) throws Exception {
		if (hphm.indexOf(HPHM_PRE) != -1)
			return true;
		return false;
	}
	
	/**
	 * 添加字段
	 */
	private void addField(JSONObject jsonObj, Map<String, String> map){
		jsonObj.element("result", "1");
		jsonObj.element("resfrom", "PTABLE");
		Set<Map.Entry<String, String>> set = map.entrySet();
		Iterator<Map.Entry<String, String>> it = set.iterator();
		while(it.hasNext()){
			Map.Entry<String, String> entry = it.next();
			jsonObj.element(entry.getKey(), entry.getValue());
		}
	}
	
	
	/**
	 * 字段转换及翻译
	 * @param cb
	 * @return
	 */
	private JSONObject converAndTranslation(Vehicle cb){
		final Map<String, String> map = new HashMap<String, String>();
		jsonConfig.setJsonPropertyFilter(new PropertyFilter(){
			@Override
			public boolean apply(Object arg0, String arg1, Object arg2) {
				String val = "";
				if(arg2 != null)
					val = arg2.toString();
				// 转换字段,则用新字段替换，过滤旧字段
				String ncKey = fieldNameConverMap.get(arg1);
				if(StringUtils.isNotBlank(ncKey))//{
					map.put(ncKey, val);
					//return true;
				//}
				// 翻译字段,不过滤
				if(fieldValTranslationMap.containsKey(arg1))
					map.put(arg1+OPTION_KEY_SUFFIX, translation(arg1, val));
				return false;
			}
		});
		JSONObject jsonObj = JSONObject.fromObject(cb, jsonConfig);
		// 修改结果集
		addField(jsonObj, map);
		
		return jsonObj;
	}
}
