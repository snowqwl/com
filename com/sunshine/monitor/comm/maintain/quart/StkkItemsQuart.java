package com.sunshine.monitor.comm.maintain.quart;

import java.util.List;

import com.sunshine.monitor.comm.maintain.MaintainBean;
import com.sunshine.monitor.comm.maintain.StkkItsmTransmitter;
import com.sunshine.monitor.system.gate.bean.CodeGate;

public class StkkItemsQuart {

	/**
	 * 
	 * 函数功能说明：卡口不在线自动发送到运维平台
	 * 修改日期 	2013-8-20
	 * @return    
	 * @return boolean
	 */
	public boolean setGayw(){
		boolean tip = true;
		
		//不在线卡口集合；暂未获取
		List<CodeGate> list = null;
		
		if(list != null && !list.isEmpty()){
			for(CodeGate gate : list){
				MaintainBean bean = new MaintainBean();
				//bean.setLinkMan();
				//bean.setMobilePhone();
				bean.setContent("卡口【" +gate.getKdmc() + "】不在线，请修复！");
				
				new StkkItsmTransmitter().send(null, bean);
			}
		}
		return tip;
	}
}
