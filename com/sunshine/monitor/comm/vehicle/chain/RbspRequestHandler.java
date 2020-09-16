package com.sunshine.monitor.comm.vehicle.chain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.vehicle.IRequestHandler;
import com.sunshine.monitor.comm.vehicle.IVehicle;
import com.sunshine.monitor.comm.vehicle.RbspSpec;

public class RbspRequestHandler extends IRequestHandler implements RbspSpec {

	private Logger log = LoggerFactory.getLogger(RbspRequestHandler.class);

	public RbspRequestHandler() {

	}

	@Override
	public <K, V> Object doHandler(Map<K, V> conditionMap) throws Exception {
		log.debug("进入全国机动车请求服务.......");
		if (validate(conditionMap)) {
			String hphm = conditionMap.get(KEY_HPHM).toString();
			String hpzl = conditionMap.get(KEY_HPZL).toString();
			IVehicle adpter = null;
			if (isHandle(hphm)) {
				log.debug("查询全国机动车库........");
				adpter = getServiceClass();
				Object obj = null;
				try {
					obj = adpter.query(conditionMap);
					if (obj != null) {
						String result = obj.toString();
						if (result.startsWith("SrcReceiver:"))
							return "{result:'全国机动车请求服务未开通!'}";
						else
							return wapper(obj.toString(), hpzl);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (obj == null && !hasNextChain())
					return getIrequestHandler().doHandler(conditionMap);
				return obj;
			}
		}
		return null;
	}

	@Override
	public boolean isHandle(String hphm) throws Exception {

		return true;
	}

	@Override
	public int level() {

		return 1;
	}

	private JSONObject wapper(String res, String hpzl)
			throws DocumentException, UnsupportedEncodingException {
		Document document = DocumentHelper.parseText(res);
		List<Element> valueList = document
				.selectNodes("RBSPMessage/Method/Items/Item/Value");
		if (valueList == null || valueList.isEmpty())
			return null;
		// Row Datas(size=2)
		List<Element> rowList = valueList.get(0).elements("Row");
		if (rowList == null || rowList.size() < 3)
			return null;
		// Empty Datas
		String state = rowList.get(0).element("Data").getTextTrim();
		if (!"000".equals(state))
			return null;
		// Return All of Field Name
		List<Element> keys = rowList.get(1).elements("Data");
		// Return All of Record
		List<Element> values = findRealValues(keys, rowList, hpzl);
		if (values == null) {
			log.error("未找到对应记录!");
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject("{result:1}");
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i).getTextTrim().toLowerCase();
			// 转换字段名
			key = fieldNameConverMap.containsKey(key) ? fieldNameConverMap
					.get(key) : key;
			// 获取字段值
			String value = values.get(i).getTextTrim();
			value = URLEncoder.encode(value, "UTF-8");
			// 添加到结果集
			jsonObj.element(key, value);
			// 翻译
			translationFieldVal(key, jsonObj);
		}
		return jsonObj;
	}

	/**
	 * 返回多条记录返回结果集中的号牌种类与请求条件中的号牌种类相等的一条记录 【注意】考虑返回结果记录有多条情况
	 * 
	 * @param keys
	 * @param rowList
	 * @param hpzl
	 * @return
	 */
	private List<Element> findRealValues(List<Element> keys,
			List<Element> rowList, String hpzl) {
		int hpzlIndex = findHPZLIndex(keys);
		if (hpzlIndex == -1) {
			log.error("省RBSP返回信息解析：无法找到hpzl节点");
			return null;
		}
		for (int i = 2; i < rowList.size(); i++) {
			List<Element> rowValues = rowList.get(i).elements("Data");
			if (rowValues == null || rowValues.size() != keys.size()) {
				log.error("省RBSP返回信息解析：key size != value size");
				return null;
			}
			String hpzlValue = rowValues.get(hpzlIndex).getTextTrim();
			if (hpzl.equals(hpzlValue)) {
				return rowValues;
			}
		}
		return null;
	}

	/**
	 * 查找HPZL 对应的字段序号
	 * 
	 * @param keys
	 * @return
	 */
	private int findHPZLIndex(List<Element> keys) {
		if (keys == null) {
			return -1;
		}
		for (int i = 0; i < keys.size(); i++) {
			if ("HPZL".equals(keys.get(i).getTextTrim()))
				return i;
		}
		return -1;
	}

	private void translationFieldVal(String columnKey, JSONObject jsonObj) {
		String columnValue = jsonObj.get(columnKey).toString();
		try {
			if (fieldValTranslationMap.containsKey(columnKey)) {
				jsonObj.put(columnKey + OPTION_KEY_SUFFIX, translation(
						columnKey, columnValue));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
