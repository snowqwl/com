package com.sunshine.monitor.comm.vehicle.tran;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.vehicle.ITranslation;
/**
 * 默认翻译实现
 * @author Administrator
 *
 */
public class DefaultTranslation extends ITranslation {

	private Logger log = LoggerFactory.getLogger(DefaultTranslation.class);
	
	public DefaultTranslation(String dmlb) {
		super(dmlb);
	}

	@Override
	public String getMC(String dmz) throws Exception{
		try {
			String dmmc = getSystemManager().getCodeValue(getDmlb(), dmz);
			if(dmmc==null || "".equals(dmmc))
				log.info("代码类别："+ getDmlb() + ", 代码值：" + dmz +" 无法翻译，请检查常量表!");
			return dmmc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
