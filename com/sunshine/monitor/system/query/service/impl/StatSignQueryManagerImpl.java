package com.sunshine.monitor.system.query.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.util.FusionChartsXMLGenerator;
import com.sunshine.monitor.system.query.dao.StatSignQueryDao;
import com.sunshine.monitor.system.query.service.StatSignQueryManager;

@Service("statSignQueryManager")
public class StatSignQueryManagerImpl implements StatSignQueryManager {

	@Autowired
	@Qualifier("statSignQueryDao")
	private StatSignQueryDao statSignQueryDao;
	/**
	 * 签收统计处置统计
	 * @param kssj
	 * @param jssj
	 * @param glbm
	 * @param bkdl
	 * @return
	 */
	public List<List<String>> getSignQuery(String kssj, String jssj,
			String glbm, String bkdl, String jb) {
		List<Map<String,Object>> dataSoucreList = this.statSignQueryDao.getSignQuery(kssj, jssj, glbm, bkdl, jb);
		String []titleName = {"实际签收率","确认无效率","下达指令率"};
		String []keys = {"YQSL","QRWL","YXDL"};
		String columnNameKey = "BMMC";
		List<List<String>> listData = FusionChartsXMLGenerator.getInstance().getMultiDSXMLData(dataSoucreList, titleName, keys, columnNameKey);
		return listData;
	}

	public StatSignQueryDao getStatSignQueryDao() {
		return statSignQueryDao;
	}

	public void setStatSignQueryDao(StatSignQueryDao statSignQueryDao) {
		this.statSignQueryDao = statSignQueryDao;
	}
}
