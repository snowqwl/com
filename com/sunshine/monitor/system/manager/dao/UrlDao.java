package com.sunshine.monitor.system.manager.dao;

import java.util.List;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
/**
 * 
 * @author OUYANG 2013/6/11
 *
 */
public interface UrlDao extends BaseDao{
	
	/**
	 * 根据单位代码查询节点名称
	 * @param dwdm
	 * @return
	 * @throws Exception
	 */
	public String getUrlName(String dwdm) throws Exception;

	/**
	 * 查询所有CodeUrl对象列表
	 * @return
	 * @throws Exception
	 */
	public List<CodeUrl> getCodeUrls() throws Exception;
	
	public List<CodeUrl> getCodeUrls(String jb) throws Exception;

	/**
	 * 根据单位单码查询CodeUrl对象
	 * @param paramString
	 * @return
	 * @throws Exception
	 */
	public CodeUrl getUrl(String dwdm) throws Exception;

	/**
	 * 根据CodeUrl对象参数查询列表
	 * @param paramCodeUrl
	 * @return
	 * @throws Exception
	 */
	public List<CodeUrl> getUrls(CodeUrl paramCodeUrl) throws Exception;

}