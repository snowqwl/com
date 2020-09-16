package com.sunshine.monitor.comm.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.StatSystem;
import com.sunshine.monitor.comm.dao.PublicDao;
import com.sunshine.monitor.comm.service.PublicManager;

@Transactional
@Service
public class PublicManagerImpl implements PublicManager {

	@Autowired
	private PublicDao publicDao;
	
	public List CityReport() throws Exception {
		List list = this.publicDao.CityReport();
		if ((list != null) && (list.size() > 0)) {
			for (Iterator it = list.iterator(); it.hasNext();) {
				StatSystem ss = (StatSystem) it.next();
				if (ss.getJq().equals("1"))
					ss.setJqmc("<span class=\"green\">正常</span>");
				else if (ss.getJq().equals("0"))
					ss.setJqmc("<span class=\"gray\">目标不存在</span>");
				else if (ss.getJq().equals("-1"))
					ss.setJqmc("<span class=\"red\">无法连接</span>");
				else if (ss.getJq().equals("-2"))
					ss.setJqmc("<span class=\"gray\">主机不正常</span>");
				else if (ss.getJq().equals("-9")) {
					ss.setJqmc("<span class=\"red\">发生异常</span>");
				}
				if (ss.getXt().equals("1"))
					ss.setXtmc("<span class=\"green\">正常</span>");
				else if (ss.getXt().equals("0"))
					ss.setXtmc("<span class=\"gray\">目标不存在</span>");
				else if (ss.getXt().equals("-1"))
					ss.setXtmc("<span class=\"red\">无法连接</span>");
				else if (ss.getXt().equals("-2")) {
					ss.setXtmc("<span class=\"red\">校验错误</span>");
				}
				if (ss.getJr().equals("1"))
					ss.setJrmc("<span class=\"green\">正常</span>");
				else if (ss.getJr().equals("0"))
					ss.setJrmc("<span class=\"gray\">目标不存在</span>");
				else if (ss.getJr().equals("-1"))
					ss.setJrmc("<span class=\"red\">无法连接</span>");
				else if (ss.getJr().equals("-2")) {
					ss.setJrmc("<span class=\"red\">校验错误</span>");
				}
			}
		}
		return list;
	}

	public Map FullTextSearch(String searchText) throws Exception {
		return this.publicDao.FullTextSearch(searchText);
	}

}
