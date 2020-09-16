package com.ht.dc.trans.util;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SysparaDao;
import com.sunshine.monitor.system.manager.dao.UrlDao;


public class TransUtil {

	private SysparaDao sysparaDao;

	private UrlDao urlDao;

	public TransUtil() {
		this.urlDao = (UrlDao) SpringApplicationContext
				.getStaticApplicationContext().getBean("urlDao");

		this.sysparaDao = (SysparaDao) SpringApplicationContext
				.getStaticApplicationContext().getBean("sysparaDao");

	}

	private Logger logger = LoggerFactory.getLogger(TransUtil.class);

	public int isLeafNode(String jsdw) {

		Syspara xzqh = null;

		try {
			xzqh = sysparaDao.getSyspara("xzqh", "1", null);
		} catch (Exception e) {
			e.printStackTrace();
			xzqh = null;
		}
		if ((xzqh == null) || (xzqh.getCsz().length() < 1)) {
			return 3;
		}
		if (xzqh.equals("999999999999")) {
			return 1;
		}
		String tmpXzqh = "";
		int endindex = 0;
		for (int i = xzqh.getCsz().length(); i > 0; i--) {
			if (xzqh.getCsz().charAt(i - 1) != '0') {
				endindex = i;
				break;
			}
		}
		tmpXzqh = xzqh.getCsz().substring(0, endindex);
		String sql = "select * from code_url where sjjd like '" + tmpXzqh
				+ "%'";
		int isLeaf = 0;
		try {

			List<CodeUrl> list = this.urlDao.queryList(sql, CodeUrl.class);
			for (Iterator it = list.iterator(); it.hasNext();) {
				CodeUrl tmp = (CodeUrl) it.next();
				if (tmp.getDwdm().equals(jsdw)) {
					isLeaf = 1;
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		/**
		 * DebugLog.debug("trans", "当前行政区划为：" + xzqh.getCsz() + "; 接收单位是：" +
		 * jsdw + "; 是否为子节点：" + isLeaf);
		 **/
		return isLeaf;
	}

	public CodeUrl getNextLeafNodeUrl(String jsdw) {

		Syspara xzqh = null;
		try {
			xzqh = sysparaDao.getSyspara("xzqh", "1", null);
		} catch (Exception e) {
			e.printStackTrace();
			xzqh = null;
		}
		if ((xzqh == null) || (xzqh.getCsz().length() < 1)) {
			return null;
		}

		// UrlDaoJdbc urlDao = new UrlDaoJdbc();
		// urlDao.setJdbcTemplate(jdbcTemplate);
		String curDwdm = new String(jsdw);
		CodeUrl res = null;
		boolean hasFound = false;
		for (int count = 0; count < 10; count++) {
			CodeUrl tmp = null;
			try {
				tmp = urlDao.getUrl(curDwdm);
			} catch (Exception e) {
				e.printStackTrace();
				tmp = null;
			}
			if (tmp == null) {
				res = null;
				break;
			}
			if ((tmp.getSjjd() == null) || (tmp.getSjjd().length() < 1)) {
				res = null;
				break;
			}
			if (tmp.getSjjd().equals(xzqh.getCsz())) {
				res = tmp;
				hasFound = true;
				break;
			}
			curDwdm = tmp.getSjjd();
		}

		if (!hasFound) {
			res = null;
		}
		if (res != null)
			logger.debug("搜索下一个路径，当前单位：" + xzqh.getCsz() + "; 接收单位：" + jsdw
					+ "; 下一个节点：" + res.getDwdm());
		else {
			logger.debug("搜索下一个路径，当前单位：" + xzqh.getCsz() + "; 接收单位：" + jsdw
					+ "; 下一个节点为空(NULL)！");
		}
		return res;
	}

	public CodeUrl getFatherNodeUrl() {
		Syspara xzqh = null;
		try {
			xzqh = sysparaDao.getSyspara("xzqh", "1", null);
		} catch (Exception e) {
			e.printStackTrace();
			xzqh = null;
		}
		if ((xzqh == null) || (xzqh.getCsz().length() < 1)) {
			return null;
		}

		CodeUrl res = null;
		try {
			res = urlDao.getUrl(xzqh.getCsz());
		} catch (Exception e) {
			e.printStackTrace();
			res = null;
		}
		if ((res == null) || (res.getSjjd() == null)
				|| (res.getSjjd().length() < 1))
			return null;
		try {
			res = urlDao.getUrl(res.getSjjd());
		} catch (Exception e) {
			e.printStackTrace();
			res = null;
		}
		if (res != null)
			logger.debug("搜索父节点，当前单位：" + xzqh.getCsz() + "; 父节点："
					+ res.getDwdm());
		else {
			logger.debug("搜索父节点，当前单位：" + xzqh.getCsz() + "; 父节点为空(NULL)！");
		}
		return res;
	}

	public boolean isDwdmExits(String jsdw) {
		boolean isExits = false;

		CodeUrl tmp = null;
		try {
			tmp = urlDao.getUrl(jsdw);
		} catch (Exception e) {
			e.printStackTrace();
			tmp = null;
		}
		if (tmp != null) {
			isExits = true;
		}
		return isExits;
	}

	public boolean isDwdmIPValid(String dwdm, String ip, String csdw,
			String jsdw, String sn) {
		boolean isDwdmIPValid = false;
		boolean isCsdwJsdwValid = false;
		boolean isCsdwJsdwExits = false;

		CodeUrl tmp = null;
		try {
			tmp = urlDao.getUrl(dwdm);
		} catch (Exception e) {
			e.printStackTrace();
			tmp = null;
		}

		if (tmp != null) {
			if ((tmp.getUrl() != null) && (!"".equals(tmp.getUrl()))
					&& (tmp.getUrl().equals(ip))) {
				isDwdmIPValid = true;
			}
		}

		CodeUrl tmpCsdw = null;
		CodeUrl tmpJsdw = null;
		try {
			tmpCsdw = urlDao.getUrl(dwdm);
			tmpJsdw = urlDao.getUrl(jsdw);
		} catch (Exception e) {
			e.printStackTrace();
			tmpCsdw = null;
			tmpJsdw = null;
		}

		if ((tmpCsdw != null) && (tmpJsdw != null)) {
			isCsdwJsdwExits = true;
			if (!csdw.equals(jsdw)) {
				isCsdwJsdwValid = true;
			}
		}

		boolean isValid = false;
		if ((isDwdmIPValid) && (isCsdwJsdwValid) && (isCsdwJsdwExits)) {
			isValid = true;
		}
		return isValid;
	}
}
