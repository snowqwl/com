package com.sunshine.monitor.system.ws.ClkkbkFeedBackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 仅做测试使用
 * @author Administrator
 *
 */
public class ClkkbkFeedBackServiceImpl implements ClkkbkFeedBackService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	public String feedBack(String xml) {
		log.error(xml);
		return "<?xml version=\"1.0\" encoding=\"GBK\"?> " + 
			"<ResultSet><ReturnValue>000</ReturnValue></ResultSet>";
	}

}
