package com.sunshine.monitor.system.analysis.bean;

/**
 * 连续规则
 * @author Administrator
 *
 */
public class ContinueNightCarReg extends NightCarRegAbstrat {
 
	// 连续天数
	private int continueDaycount;
	 
	public int getContinueDaycount() {
		return continueDaycount;
	}

	public void setContinueDaycount(int continueDaycount) {
		this.continueDaycount = continueDaycount;
	}

	public String getAnalysisType() {
		
		return NightCarAnalysisType.CON_REG.getCode();
	}
	 
}
 
