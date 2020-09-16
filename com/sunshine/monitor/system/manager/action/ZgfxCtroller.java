package com.sunshine.monitor.system.manager.action;

import java.net.URLDecoder;
import java.util.HashMap;
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
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.ZgfxBean;
import com.sunshine.monitor.system.manager.service.ZgfxManager;

@Controller
@RequestMapping(value = "/zgfx.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZgfxCtroller {

	@Autowired
	@Qualifier("zgfxManager")
	private ZgfxManager zgfxManager;

	@RequestMapping()
	public String index() {
		return "manager/zgfxlist";
	}
	
	@RequestMapping()
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest request,String page, String rows,ZgfxBean bean) {
		Map<String, Object> queryMap = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("curPage", page);
			map.put("pageSize", rows);
			queryMap = this.zgfxManager.queryListZgzf(map, bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryMap;
	}

	@RequestMapping()
	@ResponseBody
	public Object edit(HttpServletRequest request,HttpServletResponse response,ZgfxBean bean) {
		Map<String, String> result = null;
		try {
			int count = this.zgfxManager.updateZgzf(bean);
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
	public Map<String, String> save(HttpServletRequest request,HttpServletResponse response,ZgfxBean bean) {
		Map<String, String> result = null;
		try {
			int count = this.zgfxManager.saveZgzf(bean);
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
	public Object delete(HttpServletRequest request,HttpServletResponse response,String zgId) {
		Map<String, String> result = null;
		try {
			int count = this.zgfxManager.deleteZgzf(zgId);
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
	public Object getZgfxById(HttpServletRequest request,HttpServletResponse response,String zgId) {
		ZgfxBean bean= null;
		try {
			bean = this.zgfxManager.queryDetail(zgId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	@RequestMapping()
	public ModelAndView forwardZgfx(HttpServletRequest request,String Id) {
		ModelAndView mv = new ModelAndView();
		try {
			ZgfxBean bean = this.zgfxManager.queryDetail(Id);
			request.setAttribute("bean",bean);
			mv.setViewName("manager/zgfxDetail");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	@RequestMapping()
	public String view() {
		return "manager/zgfxShowList";
	}
	
	@RequestMapping()
	@ResponseBody
	public Map<String, Object> queryListForShowPage(HttpServletRequest request,String page, String rows) {
		Map<String, Object> queryMap = null;
		try {
			String title = URLDecoder.decode(request.getParameter("title"),"UTF-8");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("curPage", page);
			map.put("pageSize", rows);
			queryMap = this.zgfxManager.queryForShowPage(map, title);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryMap;
	}
}
