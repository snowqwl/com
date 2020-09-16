package com.sunshine.monitor.comm.quart.util;

import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * 通OID获取信息(包括主机、路由，交换机)，继承此类
 * 
 * @author OUYANG 2014/3/28
 * 
 */
public abstract class AbstractTabdata {

	protected CommunityTarget target;

	protected Snmp snmp;

	public int maxRepetitions = 100;
	
	public int retries = 2 ;
	
	public int timeout = 1500 ;
	
	/**
	 * 
	 * @param ip
	 * @param community
	 * @param version
	 */
	public AbstractTabdata(String ip, String community, int version){
		try {
			Address targetAddress = GenericAddress.parse("udp:" + ip + "/161");
			TransportMapping transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			// 设置target
			target = new CommunityTarget();
			target.setCommunity(new OctetString(community));
			target.setAddress(targetAddress);
			// 通信息不成功时重试次数
			target.setRetries(getRetries());
			// 超时时间
			target.setTimeout(getTimeout());
			target.setVersion(version);
			transport.listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 默认协议为UDP
	 * 
	 * @param ip
	 * @param community
	 * @param version
	 * @param retries
	 * @param timeout
	 * @throws Exception
	 */
	public AbstractTabdata(String ip, String community, int version,
			int retries, int timeout) {
		try {
			Address targetAddress = GenericAddress.parse("udp:" + ip + "/161");
			TransportMapping transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			// 设置target
			target = new CommunityTarget();
			target.setCommunity(new OctetString(community));
			target.setAddress(targetAddress);
			// 通信息不成功时重试次数
			target.setRetries(retries);
			// 超时时间
			target.setTimeout(timeout);
			target.setVersion(version);
			transport.listen();
			
			this.retries = retries;
			this.timeout = timeout;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过具体标识OID获取信息
	 * 
	 * @param oid
	 * @return
	 */
	public abstract Object queryGetTabData(String... oids);

	/**
	 * 通过OID获取BULK块信息
	 * 
	 * @param oid
	 * @return
	 */
	public abstract Object queryBulkTabData(String... oids);

	/**
	 * 通过OID遍历树
	 * 
	 * @param oid
	 * @return
	 */
	public abstract Object queryWalkTabData(String oid);

	/**
	 * 用于BULK操作查询块大小,如果需修改默认值，子类重写此方法
	 * 
	 * @return
	 */
	public int getMaxRepetitions() {
		return maxRepetitions;
	}

	/**
	 * 重试次数
	 * @return
	 */
	public int getRetries() {
		return retries;
	}

	/**
	 * 超时
	 * @return
	 */
	public int getTimeout() {
		return timeout;
	}
	
}
