package com.sunshine.monitor.system.activemq.service.send.impl;

import java.util.Date;
import java.util.List;

import javax.jms.Destination;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.context.AbstractApplicationEvent;
import com.sunshine.monitor.comm.util.DateUtils;
import com.sunshine.monitor.system.activemq.bean.GateMessage;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.activemq.service.send.BoundaryGateQueueProducerManager;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.event.CodeGateAddorUpdateEvent;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.SysparaManager;
/**
 * 
 * @author OUYANG 2014/4/17
 *
 */
@Transactional
@Service("alarmPrearrangQueueProducerManager")
public class BoundaryGateQueueProducerManagerImpl implements 
		BoundaryGateQueueProducerManager {
	
	@Autowired
    private JmsTemplate jmsTemplate;
	
	@Autowired
	@Qualifier("boundarygateQueueDestination")
	private Destination boundarygateQueueDestination;
	
	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	
	@Autowired
	@Qualifier("sysparaManager")
	private SysparaManager sysparaManager;
	
	private Log log = LogFactory.getLog(BoundaryGateQueueProducerManagerImpl.class);
	
	public void send(TransObj message) throws Exception {
		/** P2P module */
		this.jmsTemplate.setPubSubDomain(false);
		this.jmsTemplate.convertAndSend(boundarygateQueueDestination, message);
	}

	public void actionPerformed(AbstractApplicationEvent e)  {
		
		/** Get event object */
		CodeGateAddorUpdateEvent  event = null ;
		if(e instanceof CodeGateAddorUpdateEvent){
			event = (CodeGateAddorUpdateEvent)e;
		} else {
			log.debug("Object instance is not CodeGateAddorUpdateEvent");
			return;
		}
		long timestamp = event.getTimestamp();
		log.info(timestamp+"--excuting delive boundary gate to boundary city .....");
		
		/** Get source of gate extend information */
		CodeGate gate = (CodeGate)event.getSource();
		String operator = event.getUserName();
		String kdbh = gate.getKdbh();
		List<CodeGateExtend> extend = null;
		TransObj transObj = null ;
		try {
			/** Get gate information via parameter kdbh */
			extend = getCodeGateExtends(kdbh);
			
			/** Package message object */
			GateMessage message = new GateMessage(); 
			message.setCodeGate(gate);
			message.setCodeGateExtend(extend);
			message.setOperator(operator);
			
			/** Packageing transmission object */
			String xzqh = getLocalXzqh();
			transObj = new TransObj();
			transObj.setObj(message);
			transObj.setType("28"); 
			transObj.setCsdw(xzqh);
			transObj.setJsdw(gate.getBjcs());
			transObj.setYwxh(gate.getKdbh());
			transObj.setDdbj(1D);
			transObj.setCsbj(0D);
			transObj.setDdsj(DateUtils.getTimeStr(new Date(), 2));
			transObj.setCssj(DateUtils.getTimeStr(new Date(), 2));
			transObj.setCsxh(xzqh+String.valueOf(timestamp));
			
			send(transObj);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
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
	
	
	/**
	 * Query direct info
	 * @param kdbh
	 * @return
	 * @throws Exception
	 */
	private List<CodeGateExtend> getCodeGateExtends(String kdbh) throws Exception{
		return this.gateManager.getDirectList(kdbh);
	}
	
}
