package com.sunshine.monitor.system.monitor.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.util.FusionChartsXMLGenerator;
import com.sunshine.monitor.system.monitor.dao.SjcsjcProjectDao;
import com.sunshine.monitor.system.monitor.service.SjcsjcProjectManager;

@Service("sjcsjcProjectManager")
public class SjcsjcProjectManagerImpl implements SjcsjcProjectManager {

	@Autowired
	@Qualifier("sjcsjcProjectDao")
	private SjcsjcProjectDao sjcsjcProjectDao;

	public int getSjcsQueryCount() {
		return this.sjcsjcProjectDao.getSjcsQueryCount();
	}


	public List<List<String>> getSjcsQueryScl(String kssj,String jssj,String jkdss) {
		
		List<Map<String,Object>> list =  this.sjcsjcProjectDao.getSjcsQuery(kssj, jssj, jkdss);
		String[] titleName = {"正常上传率","超时上传率"};
		String[] keys = {"SSMYNZCSCL","YCSCL"};
		String columnKey = "FXMC";
		return FusionChartsXMLGenerator.getInstance().getMultiDSXMLData(list, titleName, keys, columnKey);
		
	}
	

	public List<List<String>> getSjcsQuerySc(String kssj, String jssj,
			String jkdss) {
		List<Map<String,Object>> list = this.sjcsjcProjectDao.getSjcsQuery(kssj, jssj, jkdss);
		String[] titleName = {"上传总数","正常上传数","超时上传数"};
		String[] keys = {"YSC","SSMYNZCSC","YCSC"};
		String columnKey = "FXMC";
		return FusionChartsXMLGenerator.getInstance().getMultiDSXMLData(list, titleName, keys, columnKey);
	}
	
	public List<List<String>> getSjcsQueryCdSc(String kssj,String jssj,String jkdss,String fxbh) {
		List<Map<String,Object>> list = this.sjcsjcProjectDao.getSjcsQueryCd(kssj, jssj, jkdss, fxbh);
		String[] titleName = {"上传总数","正常上传数","超时上传数"};
		String[] keys = {"YSC","SSMYNZCSC","YCSC"};
		String columnKey = "CDBH";
		return FusionChartsXMLGenerator.getInstance().getMultiDSXMLData(list, titleName, keys, columnKey);
	}
	
	public List<List<String>> getSjcsQueryCdScl(String kssj,String jssj,String jkdss,String fxbh) {
		List<Map<String,Object>> list = this.sjcsjcProjectDao.getSjcsQueryCd(kssj, jssj, jkdss, fxbh);
		String[] titleName = {"正常上传率","超时上传率"};
		String[] keys = {"SSMYNZCSCL","YCSCL"};
		String columnKey = "CDBH";
		return FusionChartsXMLGenerator.getInstance().getMultiDSXMLData(list, titleName, keys, columnKey);
	}

	public SjcsjcProjectDao getSjcsjcProjectDao() {
		return sjcsjcProjectDao;
	}

	public void setSjcsjcProjectDao(SjcsjcProjectDao sjcsjcProjectDao) {
		this.sjcsjcProjectDao = sjcsjcProjectDao;
	}



}
