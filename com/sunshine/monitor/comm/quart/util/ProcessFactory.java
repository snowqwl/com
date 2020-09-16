package com.sunshine.monitor.comm.quart.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 进程列表信息工厂
 * 
 * @author OUYANG 2014/3/28
 * 
 */
public class ProcessFactory extends AbstractDataFactory {

	private final static String PROCESS_OID = "1.3.6.1.2.1.25.4.2.1";

	public static ProcessFactory processFactory;

	public Map<String, AbstractTabdata> map = null;

	private ProcessFactory() {
		map = new ConcurrentHashMap<String, AbstractTabdata>();
	}

	public static ProcessFactory getProcessFactory() {
		if (processFactory == null) {
			processFactory = new ProcessFactory();
		}
		return processFactory;
	}

	/**
	 * IP+PORT=KEY
	 * 
	 */
	public Object getProcessTabdata(String ip, int port, String community,
			int version) {
		AbstractTabdata tabdata = null;
		Object obj = null;
		try {
			String key = ip + port;
			if (map.containsKey(key)) {
				tabdata = map.get(key);
			} else {
				tabdata = new SystemProcessTabdata(ip, community, version);
			}
			obj = tabdata.queryWalkTabData(PROCESS_OID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 验证是否存在指定线程
	 * 
	 * @return
	 */
	public boolean isExistProcess(Object obj) {
		Map map = (Map) obj;
		if (map.isEmpty())
			return true;
		Collection<List<String>> c = map.values();
		for (Object o : c) {
			List<String> list = (List<String>) o;
			System.out.println(list.toString());
			if (list.size() > 4) {
				String processName = list.get(1).trim();
				String processPara = list.get(4).trim();
				if (processPara != null && !"".equals(processPara)) {
					if ("java.exe".equals(processName)
							&& (processPara.indexOf("DKETTLE_HOME")) != -1) {
						System.out.println(processName + "_" + processPara);
					}
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		ProcessFactory p = ProcessFactory.getProcessFactory();
		Map map = (Map) p.getProcessTabdata("localhost", 161, "public", 1);
		p.isExistProcess(map);
		//Collection c = map.values();
		//for (Object obj : c) {
		//	System.out.println(obj.toString());
		//}
	}

}
