package com.sunshine.monitor.comm.quart.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

/**
 * 操作系统进程信息监控
 * 
 * @author OUYANG 2014/3/28
 * 
 */
public class SystemProcessTabdata extends AbstractTabdata {


	public SystemProcessTabdata(String ip, String community, int version) throws Exception {
		super(ip, community, version);
	}
	
	public SystemProcessTabdata(String ip, String community, int version,
			int retries, int timeout) throws Exception {
		super(ip, community, version, retries, timeout);
	}

	/**
	 * OID具休某个标识， 最后一位标识为实例标识
	 */
	@Override
	public Object queryGetTabData(String... oids) {
		try {
			PDU pdu = new PDU();
			for (String oid : oids) {
				pdu.addAll(new VariableBinding[] { new VariableBinding(new OID(
						oid)) });
			}
			// 请求类型
			pdu.setType(PDU.GET);
			// 响应
			ResponseEvent respEvnt = snmp.send(pdu, target);
			//
			if (respEvnt != null && respEvnt.getResponse() != null) {
				Vector recVBs = respEvnt.getResponse().getVariableBindings();
				for (int i = 0; i < recVBs.size(); i++) {
					VariableBinding recVB = (VariableBinding) recVBs
							.elementAt(i);
					System.out.println(recVB.getOid() + " : "
							+ recVB.getVariable());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * BULK遍历，必须设置属性MaxRepetitions，默认为零
	 */
	@Override
	public Object queryBulkTabData(String... oids) {
		try {
			PDU pdu = new PDU();
			for (String oid : oids) {
				pdu.addAll(new VariableBinding[] { new VariableBinding(new OID(
						oid)) });
			}
			pdu.setMaxRepetitions(getMaxRepetitions());
			pdu.setNonRepeaters(0);
			pdu.setType(PDU.GETBULK);
			ResponseEvent respEvnt = snmp.send(pdu, target);
			if (respEvnt != null && respEvnt.getResponse() != null) {
				Vector recVBs = respEvnt.getResponse().getVariableBindings();
				for (int i = 0; i < recVBs.size(); i++) {
					VariableBinding recVB = (VariableBinding) recVBs
							.elementAt(i);
					System.out.println(recVB.getOid() + " : "
							+ recVB.getVariable());
				}
			}
		} catch (NullPointerException n) {
			throw n;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * WALK遍历,数量过大避免使用此方法
	 */
	@Override
	public Object queryWalkTabData(String oid) {
		Map<String, List<String>> map = null;
		try {
			PDUFactory pf = new DefaultPDUFactory(PDU.GET);
			TableUtils tu = new TableUtils(snmp, pf);
			OID[] columns = new OID[1];
			columns[0] = new VariableBinding(new OID(oid)).getOid();
			List<TableEvent> list = tu.getTable(target, columns, null, null);
			StringBuffer sb = new StringBuffer(50);
			map = new LinkedHashMap<String, List<String>>();
			for (int i = 0; i < list.size(); i++) {
				TableEvent te = (TableEvent) list.get(i);
				VariableBinding[] vb = te.getColumns();
				if (vb != null) {
					for (int j = 0; j < vb.length; j++) {
						OID toid = vb[j].getOid();
						String val = vb[j].getVariable().toString();
						String processId = getProcessId(toid);
						List<String> dlist = getDataList(map, processId);
						dlist.add(val);
					}
				} else {
					throw new NullPointerException("被监控系统的网络不通或IP或其它相关配置错识！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public Properties getProperties(String str) {
		Properties properies = null;
		try {
			// System.out.println(System.getProperty("file.encoding"));
			// http://hi.baidu.com/sodarfish/item/965a9b3427eaa1d66c15e940
			byte[] buf = str.getBytes("UTF-8");
			ByteArrayInputStream inStream = new ByteArrayInputStream(buf);
			properies = new Properties();
			properies.load(inStream);
			inStream.close();
			System.out.println(properies.toString());
			String value = (String) properies
					.get("1.3.6.1.2.1.25.4.2.1.4.4092");
			System.out.println("key=" + value);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properies;
	}

	/**
	 * 获取进程编号
	 * 
	 * @param oid
	 * @return
	 */
	public String getProcessId(OID oid) {
		String t_oid = oid.toString().trim();
		int pos = t_oid.lastIndexOf(".");
		String processId = t_oid.substring(pos + 1, t_oid.length());
		return processId;
	}

	/**
	 * 获取进程列表信息
	 * 
	 * @param map
	 * @param processId
	 * @return
	 */
	public List<String> getDataList(Map<String, List<String>> map,
			String processId) {
		if (map.containsKey(processId)) {
			return (List) map.get(processId);
		} else {
			List<String> dlist = new LinkedList<String>();
			map.put(processId, dlist);
			return dlist;
		}
	}

	public static void main(String[] args) throws Exception {
		SystemProcessTabdata snmpGet = new SystemProcessTabdata("localhost",
				"jcbk", 1);
		Map map = (Map)snmpGet.queryWalkTabData("1.3.6.1.2.1.25.4.2.1");
		Collection c = map.values();
		for (Object obj : c) {
			System.out.println(obj.toString());
		}
	}
}
