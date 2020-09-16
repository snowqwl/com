package com.sunshine.monitor.system.query.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import com.sunshine.monitor.comm.util.FusionChartsXMLGenerator;
import com.sunshine.monitor.system.query.dao.QueryListDao;
import com.sunshine.monitor.system.query.service.StatQueryManager;


@Service
@Transactional
public class StatQueryManagerImpl implements StatQueryManager {

	@Autowired
	private QueryListDao queryListDao;
	/**
	 * 四两统计/二数统计
	 * @param dwdm
	 * @param kssj
	 * @param jssj
	 * @return
	 * @throws Exception
	 */
	public String[] getShowStat2Rate4XML(String dwdm, String kssj, String jssj)
			throws Exception {
		
		String[]titleName1 = {"签收率","及时签收处置率","反馈率","拦截率"};
		String[]titleName2 = {"有效签收数","成功拦截数"};
		
		String[]keys1 = {"ZQSL","JSQSL","FKL","LJL"};
		String[]keys2 = {"YXQS","CGLJ_TOTAL"};
		
		List<Map<String,Object>> list = queryListDao.getstat4Rate2NumList(dwdm, kssj, jssj);
		for(Map<String,Object> map : list){
			
			BigDecimal yqs_total = new BigDecimal(map.get("YQS_TOTAL").toString());
			BigDecimal csqs_total = new BigDecimal(map.get("CSQS_TOTAL").toString());
			BigDecimal wqs_total = new BigDecimal(map.get("WQS_TOTAL").toString());
			
			BigDecimal yxqs = new BigDecimal(map.get("YXQS").toString());
			
			BigDecimal yfk = new BigDecimal(map.get("YFK").toString());
			BigDecimal jsfk = new BigDecimal(map.get("JSFK").toString());
			BigDecimal csfk = new BigDecimal(map.get("CSFK").toString());
			
			BigDecimal ylj_total = new BigDecimal(map.get("YLJ_TOTAL").toString());
			BigDecimal cglj_total = new BigDecimal(map.get("CGLJ_TOTAL").toString());
			
			if(yqs_total.intValue() == 0){
				map.put("ZQSL",0);
				map.put("JSQSL",0);
				
			}else{
				map.put("ZQSL", ((csqs_total.add(yxqs)).divide(yqs_total, 2, BigDecimal.ROUND_HALF_UP).doubleValue()) * 100);
				map.put("JSQSL",(yxqs.divide(yqs_total, 4, BigDecimal.ROUND_HALF_UP).doubleValue()) * 100);
			}	
			
			if(yfk.intValue() == 0)
				map.put("FKL", 0);
			else
				map.put("FKL",  ((jsfk.add(csfk)).divide(yfk, 4, BigDecimal.ROUND_HALF_UP).doubleValue()) * 100);
			
			if(ylj_total.intValue() == 0)
				map.put("LJL",0);
			else
				map.put("LJL", (cglj_total.divide(ylj_total, 4, BigDecimal.ROUND_HALF_UP).doubleValue()) * 100);
		
		}
		
		String multiXml01 = null;
		String multiXml02 = null;
		
		if(list.size() > 0){
			
			FusionChartsXMLGenerator fsx = FusionChartsXMLGenerator.getInstance();
			
			List<List<String>>  res1  = fsx.getMultiDSXMLData(list, titleName1, keys1,"BMMC");
			List<List<String>>  res2  = fsx.getMultiDSXMLData(list, titleName2, keys2,"BMMC");
			
			multiXml01 = fsx.getMultiDSXML(res1, "四率统计【统计时间："+kssj+" 至  "+jssj+"】", "","0","%25",false, "管理部门", "百分比", 1, 1, 0, 0);
			multiXml02 = fsx.getMultiDSXML(res2, "两数统计【统计时间："+kssj+" 至  "+jssj+"】", "","0","", false, "管理部门", "数值", 1, 1, 0, 0);
		}
		
		
		String[]result = {multiXml01,multiXml02};
		return result;
	}

}
