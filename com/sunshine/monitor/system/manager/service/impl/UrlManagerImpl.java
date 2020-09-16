package com.sunshine.monitor.system.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.dao.UrlDao;
import com.sunshine.monitor.system.manager.service.UrlManager;

@Service("urlManager")
@Transactional
public class UrlManagerImpl implements UrlManager {
	
	@Autowired
	@Qualifier("urlDao")
	private UrlDao urlDao;

	public List<CodeUrl> getUrls(CodeUrl url) throws Exception {
		return this.urlDao.getUrls(url);
	}

	public List<CodeUrl> getCodeUrls() throws Exception {
		return this.urlDao.getCodeUrls();
	}
	
	public List<CodeUrl> getCodeUrls(String jb) throws Exception {
		return this.urlDao.getCodeUrls(jb);
	}

	public CodeUrl getUrl(String dwdm) throws Exception {
		return this.urlDao.getUrl(dwdm);
	}

	public String getUrlName(String dwdm) throws Exception {
		return this.urlDao.getUrlName(dwdm);
	}
}
