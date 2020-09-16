package com.sunshine.monitor.system.manager.service;

import java.util.List;

import com.sunshine.monitor.system.manager.bean.CodeUrl;

public interface UrlManager {
	
	public List<CodeUrl> getUrls(CodeUrl paramCodeUrl) throws Exception;

	public List<CodeUrl> getCodeUrls() throws Exception;
	
	public List<CodeUrl> getCodeUrls(String jb) throws Exception;

	public CodeUrl getUrl(String paramString) throws Exception;

	public String getUrlName(String paramString) throws Exception;
}
