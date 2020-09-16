package com.sunshine.monitor.system.manager.action;

import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.bean.Codetype;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.service.CodeManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.manager.service.UrlManager;

@Controller
@RequestMapping(value = "/code.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CodeCtroller {

	@Autowired
	@Qualifier("codeManager")
	private CodeManager codeManager;

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;

	@Autowired
	@Qualifier("urlManager")
	private UrlManager urlManager;

	public CodeManager getCodeManager() {
		return codeManager;
	}

	public void setCodeManager(CodeManager codeManager) {
		this.codeManager = codeManager;
	}

	@RequestMapping()
	public String index() {
		return "manager/codelist";

	}

	@SuppressWarnings("unchecked")
	@RequestMapping()
	@ResponseBody
	public Map<String, Object> queryList(String page, String rows,
			Codetype command) {
		Codetype codetype = command;
		Map<String, Object> queryMap = null;
		try {
			Map map = new HashMap();
			map.put("curPage", page);
			map.put("pageSize", rows);
			queryMap = this.codeManager.getCodetypesByPageSize(map, codetype);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryMap;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping()
	@ResponseBody
	public Object editOne(String page, String rows, Code code) {
		String dmlb = code.getDmlb();
		if ((dmlb == null) || (dmlb.length() < 1)) {
			return new ModelAndView("");
		}
		Map<String, Object> map = null;
		try {
			Map filter = new HashMap();
			filter.put("curPage", page);
			filter.put("pageSize", rows);
			Codetype codetype = this.codeManager.getCodetype(dmlb);
			if (codetype != null) {
				map = this.codeManager.getCodesByTable(filter, codetype
						.getDmlb());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 初始化方向类型下拉菜单
	 * 
	 * @return
	 */
	@RequestMapping(params = "method=getSelectMenuForCode", method = RequestMethod.GET)
	@ResponseBody
	public List getSelectMenuForCode(String dmlb) {
		List codeList = null;
		try {
			codeList = codeManager.getCodesByTable(dmlb);
			//临时屏蔽一些日志记录
			for(int i=0;i<codeList.size();i++){			
				Code code =(Code) codeList.get(i);
				if(code.getDmsm1().equals("批量数据导出")){
					codeList.remove(i);
				}
				if(code.getDmsm1().equals("布控指挥中心涉案类审批新增")){
					codeList.remove(i);
				}
				if(code.getDmsm1().equals("布控指挥中心交通类审批新增")){
					codeList.remove(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codeList;
	}
    
	@RequestMapping(params = "method=getCodeType", method = RequestMethod.GET)
	@ResponseBody
	public Codetype getCodeType(String dmlb) {
		Codetype codeType = null;
		try {
			codeType = codeManager.getCodetype(dmlb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codeType;
	}

	/**
	 * 初始化报警大类类型下拉菜单
	 * 
	 * @return
	 */
	@RequestMapping(params = "method=getSelectMenuForCodeByDmsm", method = RequestMethod.GET)
	@ResponseBody
	public List getSelectMenuForCodeByDmsm(String dmlb, String dmsm,
			String position) {
		List codeList = null;
		int ps = 0;
		if (position != null)
			ps = Integer.parseInt(position);

		try {
			codeList = systemManager.getCodesByDmsm(dmlb, dmsm, ps);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return codeList;
	}

	@RequestMapping(params = "method=getCodeUrl", method = RequestMethod.GET)
	@ResponseBody
	public List<CodeUrl> getCodeUrl(HttpServletRequest requst,String jb) {
		UserSession userSession = (UserSession) requst.getSession().getAttribute("userSession");
		Department dp = userSession.getDepartment();
		if ("4300".equals(dp.getGlbm().substring(0, 4))) {
			jb = "3 or jb = 2";
		}
		List<CodeUrl> codeUrlList = null;
		try {
			codeUrlList = urlManager.getCodeUrls(jb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codeUrlList;
	}
	
	@RequestMapping(params = "method=getUrl", method = RequestMethod.GET)
	@ResponseBody
	public CodeUrl getUrl(String dwdm){
		CodeUrl codeUrl=null;
		try {
			codeUrl=urlManager.getUrl(dwdm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codeUrl;
	}

	@RequestMapping(params = "method=saveCode", method = RequestMethod.POST)
	@ResponseBody
	public Map saveCode(HttpServletRequest request,
			HttpServletResponse response, Code command) {
		Code code = command;
		Map result = null;
		try {
			Code tmpCode = this.codeManager.getCodeByTable(code.getDmlb(), code
					.getDmz());
			if (tmpCode != null) {
				return Common.messageBox("已经存在" + tmpCode.getDmz() + "代码", "0");
			}
			int count = this.codeManager.saveCode(code);
			if (count > 0) {
				result = Common.messageBox("保存成功！", "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常！", "0");
		}
		return result;
	}

	@RequestMapping(params = "method=saveCodetype", method = RequestMethod.POST)
	@ResponseBody
	public Map saveCodetype(HttpServletRequest request,
			HttpServletResponse response, Codetype command, String modal) {
		Codetype codetype = command;
		Map result = null;
		if (codetype != null) {
			try {
				if (modal.equals("new")) {
					Codetype tmpcodetype = this.codeManager
							.getCodetype(codetype.getDmlb());
					if (tmpcodetype != null) {
						return Common.messageBox("已存在代码为" + codetype.getDmlb()
								+ "的代码类别！", "0");
					}
				}
				int count = this.codeManager.saveCodetype(codetype);
				if (count > 0) {
					result = Common.messageBox("保存成功！", "1");
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = Common.messageBox("出现异常！", "0");
			}
		}
		return result;
	}

	@RequestMapping(params = "method=removeCodetype", method = RequestMethod.POST)
	@ResponseBody
	public Map removeCodetype(HttpServletRequest request,
			HttpServletResponse response, Codetype command) {
		Codetype codetype = command;
		Map result = null;
		int count = 0;
		if (codetype.getDmlb() == null || codetype.getDmlb().length() < 0) {
			return Common.messageBox("参数传输异常！", "0");
		}
		try {
			List list = this.codeManager.getCodesByTable(codetype.getDmlb());
			if (list != null && list.size() > 0) {
				result = Common.messageBox("该代码类别中已存在代码数据，不允许删除！", "0");
			} else {
				count = this.codeManager.removeCodetype(codetype);
			}
			if (count == 1) {
				result = Common.messageBox("删除成功！", "1");
			}
		} catch (Exception e) {
			result = Common.messageBox("出现异常！", "0");
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(params = "method=removecode", method = RequestMethod.POST)
	@ResponseBody
	public Map removecode(HttpServletRequest request,
			HttpServletResponse response, Code command) {
		Code code = command;
		Map result = null;
		if (code.getDmlb() == null || code.getDmlb().length() < 0
				|| code.getDmz() == null || code.getDmz().length() < 0) {
			return Common.messageBox("参数传输异常！", "0");
		}
		try {
			int count = this.codeManager.removeCode(code);
			if (count == 1) {
				result = Common.messageBox("删除成功！", "1");
				this.systemManager.loadCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常！", "0");
		}
		return result;

	}
	
	/**
	 * 根据代码类别和代码值取出相应的code填充（用于code修改回填）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object setValueForCode(HttpServletRequest request,HttpServletResponse response) {
		Code result = new Code();
		String dmlb = request.getParameter("dmlb");
		String dmz = request.getParameter("dmz");
		try {
			List<Code> codeList = this.codeManager.getCodeDetail(dmlb,dmz);
			if(codeList.size()>0){
				result = codeList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
	
	/**
	 * 更新相应的code
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map updateCode(HttpServletRequest request,HttpServletResponse response, Code command) {
		Code code = command;
		Map result = null;
		try {
			/*Code tmpCode = this.codeManager.getCodeByTable(code.getDmlb(), code
					.getDmz());
			if (tmpCode != null) {
				return Common.messageBox("已经存在代码值为" + tmpCode.getDmz() + "的代码", "0");
			}*/
			int count = this.codeManager.updateCode(code);
			if (count == 1) {
				result = Common.messageBox("更新成功！", "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常！", "0");
		}
		return result;
	}
	
	
	/**
	 * 删除相应的code
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map removeCodeByParams(HttpServletRequest request,HttpServletResponse response, Code command) {
		Code code = command;
		Map result = null;
		if (code.getDmlb() == null || code.getDmlb().length() < 0
				|| code.getDmz() == null || code.getDmz().length() < 0) {
			return Common.messageBox("参数传输异常！", "0");
		}
		try {
			int count = this.codeManager.removeCode(code);
			if (count == 1) {
				result = Common.messageBox("删除成功！", "1");
				this.systemManager.loadCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常！", "0");
		}
		return result;

	}
	
	/**
	 * 通过编辑删除相应的code
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map removeCodeByBj(HttpServletRequest request,HttpServletResponse response) {
		String dmlb = request.getParameter("dmlb");
		String dmz = request.getParameter("dmz");
		Code code = new Code();
		code.setDmlb(dmlb);
		code.setDmz(dmz);
		Map result = null;
		if (code.getDmlb() == null || code.getDmlb().length() < 0
				|| code.getDmz() == null || code.getDmz().length() < 0) {
			return Common.messageBox("参数传输异常！", "0");
		}
		try {
			int count = this.codeManager.removeCode(code);
			if (count == 1) {
				result = Common.messageBox("删除成功！", "1");
				this.systemManager.loadCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常！", "0");
		}
		return result;

	}
	
	
	
	
}
