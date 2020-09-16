package com.sunshine.monitor.system.manager.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.ftp.FtpUtil;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.TztbBean;
import com.sunshine.monitor.system.manager.service.TztbManager;

@Controller
@RequestMapping(value = "/tztb.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TztbCtroller {

	@Autowired
	@Qualifier("tztbManager")
	private TztbManager tztbManager;

	@RequestMapping()
	public String index() {
		return "manager/tztblist";
	}

	@RequestMapping()
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest request,String page, String rows,TztbBean bean) {
		Map<String, Object> queryMap = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("curPage", page);
			map.put("pageSize", rows);
			queryMap = this.tztbManager.queryList(map, bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryMap;
	}

	@RequestMapping()
	@ResponseBody
	public Object edit(HttpServletRequest request,HttpServletResponse response,TztbBean bean) {
		Map<String, String> result = null;
		try {
			int count = this.tztbManager.update(bean);
			if (count > 0) {
				result = Common.messageBox("保存成功！", "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常！", "0");
		}
		return result;
	}

   
	@RequestMapping(params = "method=save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> save(HttpServletRequest request,HttpServletResponse response,TztbBean bean) {
		Map<String, String> result = null;
		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			String yhdh = userSession.getSysuser().getYhdh();
			bean.setCjr(yhdh); 
			int count = this.tztbManager.save(bean);
			if (count > 0) {
				result = Common.messageBox("保存成功！", "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常！", "0");
		}
		return result;
	}

	@RequestMapping()
	@ResponseBody
	public Object delete(HttpServletRequest request,HttpServletResponse response,String Id) {
		Map<String, String> result = null;
		try {
			int count = this.tztbManager.delete(Id);
			if (count > 0) {
				result = Common.messageBox("删除成功！", "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常！", "0");
		}
		return result;
	}
	
	@RequestMapping()
	@ResponseBody
	public Object getTztbById(HttpServletRequest request,HttpServletResponse response,String Id) {
		TztbBean bean = null;
		try {
			bean = this.tztbManager.queryDetail(Id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	@RequestMapping()
	public ModelAndView forwardTztb(HttpServletRequest request,String Id) {
		ModelAndView mv = new ModelAndView();
		try {
			TztbBean bean = this.tztbManager.queryDetail(Id);
			request.setAttribute("bean",bean);
			mv.setViewName("manager/tztbDetail");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}	
}
