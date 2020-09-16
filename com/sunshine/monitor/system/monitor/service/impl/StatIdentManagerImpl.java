package com.sunshine.monitor.system.monitor.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.util.FusionChartsXMLGenerator;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.monitor.dao.StatMonitorDao;
import com.sunshine.monitor.system.monitor.service.StatIdentManager;

@Service("statIdentManager")
public class StatIdentManagerImpl implements StatIdentManager {

	@Autowired
	private StatMonitorDao statDao;
	

	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	
	public String getIdentListXML(String kssj, String jssj, String jkdss) throws Exception {
		
		String[]titleName = {"总过车数","蓝牌车","黄牌车","无牌车","其他颜色车","粤港车","军警车"};
		String[]keys = {"GCS","LPC","HPC","WPC","QTYSC","YGC","JJC"}; 
		List<Map<String, Object>> identList = statDao.getIdentList( kssj,  jssj,  jkdss);
		String multiXml = null;
		
		if(identList.size() > 0){
			
			FusionChartsXMLGenerator fsx = FusionChartsXMLGenerator.getInstance();
			List<List<String>>  res  = fsx.getMultiDSXMLData(identList, titleName, keys,"FXMC");
			
			multiXml = fsx.getMultiDSXML(res, "卡口流量统计【统计时间："+kssj+" 至  "+jssj+"】", "","0","",false,"卡口-方向名称", "过车数量", 1, 1, 0, 0);
		}
		
		return multiXml;
	}

	public String getHourIdentListXML(String kssj, String jssj, String kdbh,String fxbh,String cdbh)
			throws Exception {
		
		String kdmc = gateManager.getGateName(kdbh);
		String fxmc = null;
		if(StringUtils.isNotBlank(fxbh)){
			fxmc = gateManager.getDirectName(fxbh);
		}
		String fxmcT = fxmc == null ?"":",方向【"+fxmc+"】";
		String cdCT = StringUtils.isBlank(cdbh) ? "":",【" + cdbh + "】车道";
		
		String[]titleName = {"0时","1时","2时","3时","4时","5时","6时","7时",
				"8时","9时","10时","11时","12时","13时","14时","15时",
				"16时","17时","18时","19时","20时","21时","22时","23时"};
		String[]keys = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14",
						"15","16","17","18","19","20","21","22","23"};
		List<List<String>> list = new ArrayList<List<String>>();
		
		List<Map<String, Object>> identHourList = statDao.getHourIdentList( kssj,  jssj,  kdbh,fxbh,cdbh);
		
		String multiXml = null;
		if(identHourList.size() > 0){
			
			for(int i = 0; i < titleName.length; i ++){
				List<String> res = new ArrayList<String>();
				res.add(titleName[i]);
				String value = "0";
				for(Map<String, Object> mapT : identHourList){
					String hh = mapT.get("HH") == null?"":mapT.get("HH").toString();
					if( keys[i].equals(hh)  ){
						value = mapT.get("GCS_T") == null ? "0" : mapT.get("GCS_T").toString();
						break;
					}
				}
				res.add(value);
				
				list.add(res);
			}
		
			FusionChartsXMLGenerator fsx = FusionChartsXMLGenerator.getInstance();
			multiXml = fsx.getSingleDSXML(list, "卡口【"+kdmc+"】" + fxmcT + cdCT + "小时流量统计【统计时间："+kssj.substring(0, 10)+" 】", "", "0", "", "小时（24小时制）", "小时过车数量", 1, 1, 0, 0);
			//String multiXml = fsx.getMultiDSXML(res, "卡口【"+kdmc+"】小时流量统计【统计时间："+kssj+" 】", "", "方向名称", "小时过车数量", 1, 1, 0, 0);
		}
		
		return multiXml;
	}

}
