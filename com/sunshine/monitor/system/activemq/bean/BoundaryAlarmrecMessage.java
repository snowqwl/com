package com.sunshine.monitor.system.activemq.bean;

/**
 * 边界预警消息实体
 * @author OUYANG
 *
 */
public class BoundaryAlarmrecMessage extends TransBean{

	private static final long serialVersionUID = 1L;

	/**
	 * 布控、布控审核审批信息
	 */
	private TransSusp transSusp;
	
	/**
	 * 预警信息
	 */
	private TransAlarm transAlarm;

	public TransSusp getTransSusp() {
		return transSusp;
	}

	public void setTransSusp(TransSusp transSusp) {
		this.transSusp = transSusp;
	}

	public TransAlarm getTransAlarm() {
		return transAlarm;
	}

	public void setTransAlarm(TransAlarm transAlarm) {
		this.transAlarm = transAlarm;
	}
}
