package com.sunshine.monitor.comm.vehicle.tran;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.vehicle.ITranslation;
/**
 * 车身颜色翻译
 * 如：AHB三种颜色，翻译结果：A颜色，H颜色，B颜色
 * @author Administrator
 *
 */
public class CSYSTranslation extends ITranslation{

	private Logger log = LoggerFactory.getLogger(CSYSTranslation.class);
	
	public CSYSTranslation(String dmlb) {
		super(dmlb);
	}
	
	@Override
	public String getMC(String dmz) throws Exception {
		String _dmlb = getDmlb();
		if(StringUtils.isBlank(dmz) || StringUtils.isBlank(_dmlb)){
			log.info("代码类别或代码值为空!DMLB="+ _dmlb +",DMZ="+ dmz);
		}
		StringBuffer sb = new StringBuffer();
		if(dmz.length()>1){
			for(int i = 0; i < dmz.length(); i++){
				String csysVal = String.valueOf(dmz.charAt(i));
				String csysMc = getSystemManager().getCodeValue(_dmlb, csysVal);
				if(StringUtils.isNotBlank(csysMc))
					sb.append(csysMc + ",");
			}
			if(sb.length()>0){
				return sb.substring(0, sb.length()-1);
			}
		} else {
			String csysmc = getSystemManager().getCodeValue(_dmlb, dmz);
			return csysmc;
		}
		return "";
	}
}
