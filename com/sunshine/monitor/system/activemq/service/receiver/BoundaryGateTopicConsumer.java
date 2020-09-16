package com.sunshine.monitor.system.activemq.service.receiver;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sunshine.monitor.system.activemq.bean.GateMessage;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.SysparaManager;

/**
 * 接收边界卡信息
 * @author OUYANG 2014/4/17
 *
 */
@Transactional
public class BoundaryGateTopicConsumer {
	
	private static final Log log = LogFactory.getLog(BoundaryGateTopicConsumer.class);
	
	@Autowired
	@Qualifier("sysparaManager")
	private SysparaManager sysparaManager;
	
	@Autowired
	private GateManager gateManager;
	
	private ExecutorService exec = Executors.newFixedThreadPool(2);
	
	public void receiveBoundary(final TransObj message){
		exec.submit(new Runnable() {
			public void run() {
				try {
					GateMessage gateMessage = (GateMessage)message.getObj();
					String csz = getLocalXzqh();
					/** Boundary range */
					String[] bjfw = message.getJsdw().split(",");
					List<String> bjfwList = Arrays.asList(bjfw);
					if(bjfwList.contains(csz) && !message.getCsdw().equals(csz)){
						/** Basic gate information */
						CodeGate basicGate = gateMessage.getCodeGate();
						Assert.notNull(basicGate, "Basic Gate must not be null");
						log.info(csz + "边界地市接收来自"+ message.getCsdw() +"边界卡口信息: " + basicGate.getKdbh());
						//-------------------------------
						System.out.println(csz + "边界地市接收来自"+ message.getCsdw() +"边界卡口信息: " + basicGate.getKdbh());
						//-------------------------------
						gateManager.saveBoundaryGate(basicGate);
						List<CodeGateExtend> extend = gateMessage.getCodeGateExtend();
						String operator = gateMessage.getOperator();
						gateManager.saveBoundaryGateExtend(extend, operator,basicGate);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Query xzqh
	 * @return 
	 * @throws Exception
	 */
	private String getLocalXzqh() throws Exception{
		Syspara sysparam = sysparaManager.getSyspara("xzqh", "1", "");
		return sysparam.getCsz();	
	}
}
