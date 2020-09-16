package com.sunshine.monitor.comm.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.bean.Messager;
import com.sunshine.monitor.comm.dao.MessagerDao;
import com.sunshine.monitor.comm.service.MessagerManager;

@Service("messagerManager")
public class MessagerManagerImp implements MessagerManager {

	@Autowired
	@Qualifier("messagerDao")
	private MessagerDao messagerDao;
	
	
	public List<Messager> getMessagerInfo() throws Exception {
		return this.messagerDao.getMessagerInfo();
	}


	public MessagerDao getMessageDao() {
		return messagerDao;
	}


	public void setMessageDao(MessagerDao messagerDao) {
		this.messagerDao = messagerDao;
	}


	public List getStatHbInfo() throws Exception {
		List list =  this.messagerDao.getStatHbInfo();
		for(int i = list.size() -1 ; i >= 0; i--){
			Map map = (Map)list.get(i);
			Object tjrq = map.get("TJRQ");
			if(tjrq == null || tjrq.toString().length() == 0){
				
				list.remove(i);
			}else{
				
				BigDecimal tbtsbsl = new BigDecimal(map.get("TBTSBSL").toString());
				BigDecimal tywsbsl = new BigDecimal(map.get("TYWSBSL").toString());
				BigDecimal tbtsbzs = new BigDecimal(map.get("TBTSBZS").toString());
				BigDecimal tywsbzs = new BigDecimal(map.get("TYWSBZS").toString());
				BigDecimal y_btsbsl = new BigDecimal(map.get("Y_BTSBSL").toString());
				BigDecimal y_ywsbsl = new BigDecimal(map.get("Y_YWSBSL").toString());
				BigDecimal y_btsbzs = new BigDecimal(map.get("Y_BTSBZS").toString());
				BigDecimal y_ywsbzs = new BigDecimal(map.get("Y_YWSBZS").toString());
				
				BigDecimal T_BTSBL = new BigDecimal(0);
				if(tbtsbzs.intValue() != 0){
					T_BTSBL = tbtsbsl.divide(tbtsbzs,4, RoundingMode.HALF_DOWN);
				}
				
				BigDecimal T_YWSBL = new BigDecimal(0);
				if(tywsbzs.intValue() != 0){
					T_YWSBL = tywsbsl.divide(tywsbzs,4, RoundingMode.HALF_DOWN);
				}
				
				BigDecimal Y_BTSBL = new BigDecimal(0);
				if(y_btsbzs.intValue() != 0){
					Y_BTSBL = y_btsbsl.divide(y_btsbzs,4, RoundingMode.HALF_DOWN);
				}
				
				BigDecimal Y_YWSBL = new BigDecimal(0);
				if(y_ywsbzs.intValue() != 0){
					Y_YWSBL = y_ywsbsl.divide(y_ywsbzs,4, RoundingMode.HALF_DOWN);
				}
				
				//白天环比识别率
				BigDecimal THBSBL = new BigDecimal(0);
				if(T_BTSBL.intValue() != 0){
					THBSBL = (T_BTSBL.subtract(Y_BTSBL)).divide(T_BTSBL,4, RoundingMode.HALF_DOWN);
				}
				
				//夜晚环比识别率
				BigDecimal YHBSBL = new BigDecimal(0);
				if(T_YWSBL.intValue() != 0){
					YHBSBL = (T_YWSBL.subtract(Y_YWSBL)).divide(T_YWSBL,4, RoundingMode.HALF_DOWN);
				}
				
				if(THBSBL.abs().doubleValue() >= 0.3D || YHBSBL.abs().doubleValue() >= 0.3D){
					map.put("GZMS", "卡口号牌识别率环比变化率大于30%");
				}else {
					
					list.remove(i);
				}
				
			}
			
		} 
		 
		return list;
	}
}
