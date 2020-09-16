package com.sunshine.monitor.system.activemq;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConverter;

public class DefaultMessageConverter implements MessageConverter {
	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(DefaultMessageConverter.class);

	public Message toMessage(Object obj, Session session) throws JMSException {
		if (log.isDebugEnabled()) {
			log.debug("toMessage(Object, Session) - start");
		}

		// check Type
		//if (obj instanceof Order) { 
			ActiveMQObjectMessage objMsg = (ActiveMQObjectMessage) session.createObjectMessage();
			HashMap<String, byte[]> map = new HashMap<String, byte[]>();
			try {
				// POJO must implements Seralizable
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos);
				oos.writeObject(obj);
				map.put("POJO", bos.toByteArray());
				objMsg.setObjectProperty("Map", map);
	
			} catch (IOException e) {
				log.error("toMessage(Object, Session)", e);
			}
			return objMsg;
		//} else {   
		//    throw new JMSException("Object:[" + obj + "] is not Order");   
		//}  
		
	}

	public Object fromMessage(Message msg) throws JMSException {
		if (log.isDebugEnabled()) {
			log.debug("fromMessage(Message) - start");
		}

		if (msg instanceof ObjectMessage) {
			HashMap<String, byte[]> map = (HashMap<String, byte[]>) ((ObjectMessage) msg).getObjectProperty("Map");
			try {
				// POJO must implements Seralizable
				ByteArrayInputStream bis = new ByteArrayInputStream(map.get("POJO"));
				ObjectInputStream ois = new ObjectInputStream(bis);
				Object returnObject = ois.readObject();
				return returnObject;
			} catch (IOException e) {
				log.error("fromMessage(Message)", e);

			} catch (ClassNotFoundException e) {
				log.error("fromMessage(Message)", e);
			}

			return null;
		} else {
			throw new JMSException("Msg:[" + msg + "] is not Map");
		}
	}
}
