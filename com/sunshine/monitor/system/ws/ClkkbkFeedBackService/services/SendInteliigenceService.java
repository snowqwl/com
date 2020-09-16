package com.sunshine.monitor.system.ws.ClkkbkFeedBackService.services;

import java.util.List;

import com.sunshine.monitor.system.ws.ClkkbkFeedBackService.bean.TransIntelligence;

public interface SendInteliigenceService {
	public List<TransIntelligence> getTransInteliigenceList();
	
	public void sendTransIntelligence(TransIntelligence t) throws Exception ;
}
