package com.sunshine.monitor.comm.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract event source
 * @author OUYANG 2014/4/17
 *
 */
public abstract class AbstractEventSource {
	
	private List<ApplicationEventListerner> listerners = null ;
	
	public AbstractEventSource(){
		listerners = new ArrayList<ApplicationEventListerner>();
	}
	/**
	 * Add listerner
	 * @param listerner
	 */
	public void addListerner(ApplicationEventListerner listerner){
		listerners.add(listerner);
	}
	
	public void removeListerner(ApplicationEventListerner listerner){
		listerners.remove(listerner);
	}
	
	/**
	 * Event trigger
	 * @param event
	 * @see com.sunshine.monitor.system.gate.event.CodeGateAddorUpdateEvent
	 */
	public void notifyListers(AbstractApplicationEvent event){
		// Adding Thread safe
		List<ApplicationEventListerner> t_listerners = Collections.synchronizedList(listerners);
		for(ApplicationEventListerner listerner : t_listerners){
			listerner.actionPerformed(event);
		}
	}
}
