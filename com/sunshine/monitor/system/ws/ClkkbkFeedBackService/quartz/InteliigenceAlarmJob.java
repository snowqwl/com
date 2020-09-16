package com.sunshine.monitor.system.ws.ClkkbkFeedBackService.quartz;

import java.util.List;

import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.ws.ClkkbkFeedBackService.bean.TransIntelligence;
import com.sunshine.monitor.system.ws.ClkkbkFeedBackService.services.SendInteliigenceService;

public class InteliigenceAlarmJob {
	private static SendInteliigenceService sendService;
	static{
		sendService = SpringApplicationContext.getStaticApplicationContext()
				.getBean("sendInteliigenceService",SendInteliigenceService.class);
	}
	
	public void execute(){
		List<TransIntelligence> transIntelList = sendService.getTransInteliigenceList();
		for(TransIntelligence t : transIntelList){
			try {
				sendService.sendTransIntelligence(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
