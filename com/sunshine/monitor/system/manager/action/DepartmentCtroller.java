package com.sunshine.monitor.system.manager.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.service.DepartmentManager;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.manager.service.PrefectureManager;
import com.sunshine.monitor.system.manager.service.SystemManager;

@Controller
@RequestMapping(value = "/departmentCtrl.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DepartmentCtroller {

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;

	@Autowired
	@Qualifier("prefectureManager")
	private PrefectureManager prefectureManager;

	@Autowired
	@Qualifier("departmentManager")
	private DepartmentManager departmentManager;
	
	@Autowired
	@Qualifier("logManager")
	private LogManager logManager;

	@RequestMapping()
	public String findForward(HttpServletRequest request) {
		try {
			request.setAttribute("jblist", systemManager.getCode("000001"));
			request.setAttribute("lsjglist", systemManager.getCode("000005"));
			request.setAttribute("bmlxlist", systemManager.getCode("000006"));
			request.setAttribute("ssjzlist", systemManager.getCode("000008"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "manager/departmentMain";
	}

	@RequestMapping()
	public ModelAndView newAdd(HttpServletRequest request,
			HttpServletResponse response) {
		try {

			request.setAttribute("jblist", systemManager.getCode("000001"));
			request.setAttribute("lsjglist", systemManager.getCode("000005"));
			request.setAttribute("bmlxlist", systemManager.getCode("000006"));
			request.setAttribute("ssjzlist", systemManager.getCode("000008"));

			request.setAttribute("modal", "new");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("manager/departmentEdit");
	}

	@RequestMapping()
	public ModelAndView forwardWin(HttpServletRequest request,
			HttpServletResponse response, String glbm, String modal) {
		Department department = null;
		try {
			if ("edit".equals(modal)) {
				department = this.departmentManager.getDepartment(glbm);
				request.setAttribute("department", department);
			}
			request.setAttribute("jblist", systemManager.getCode("000001"));
			request.setAttribute("lsjglist", systemManager.getCode("000005"));
			request.setAttribute("bmlxlist", systemManager.getCode("000006"));
			request.setAttribute("ssjzlist", systemManager.getCode("000008"));

			request.setAttribute("modal", modal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("manager/departmentMainWin");
	}

	@RequestMapping()
	@ResponseBody
	public Object saveUnit(HttpServletRequest request,
			HttpServletResponse response, Department command) {

		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			
			Department d = departmentManager.getDepartment(command.getGlbm());
			
			String modal = request.getParameter("modal");
			if (modal.equals("new")) {
				Department tmpdepartment = this.departmentManager
						.getDepartment(command.getGlbm());
				if ((tmpdepartment != null)) {
					return Common.messageBox("已存在部门代码为" + command.getGlbm()
							+ "的部门！", "0");
				}
			}
			/*
			 * else { Department tmpdepartment = this.departmentManager
			 * .getDepartment(command.getGlbm());
			 * command.setJb(tmpdepartment.getJb());
			 * command.setSjbm(tmpdepartment.getSjbm()); }
			 */
			this.departmentManager.saveUnit(command, modal);
			
			Log log = new Log();
			log.setGlbm(userSession.getSysuser().getGlbm());
			log.setIp(userSession.getSysuser().getIp());
			log.setYhdh(userSession.getSysuser().getYhdh());
		    log.setCzlx("9032");
		    StringBuffer sb=new StringBuffer();
		    if(modal.equals("new")){
		    	 sb.append("部门新增成功，操作条件为：");
				 sb.append("部门名称："+command.getBmmc());
		    }else{
		    	sb.append("部门修改成功，操作条件为：");
			    if (StringUtils.isNotBlank(command.getBmmc()) && !d.getBmmc().equals(command.getBmmc())) {
			    	sb.append(" 修改前部门名称："+d.getBmmc()+"，修改后部门名称："+command.getBmmc());
				}
			    if (StringUtils.isNotBlank(command.getGlbm()) && !d.getGlbm().equals(command.getGlbm())) {
			    	sb.append(" 修改前部门代码："+d.getGlbm()+"，修改后部门代码："+command.getGlbm());
				}
			    if (StringUtils.isNotBlank(command.getJb()) && !d.getJb().equals(command.getJb())) {
			    	sb.append(" 修改前部门级别："+
			    				systemManager.getCodeValue("000001", d.getJb())+
			    				" 修改后部门级别："+
			    				systemManager.getCodeValue("000001", command.getJb()));
				}
			    if (StringUtils.isNotBlank(command.getSjbm()) && !d.getSjbm().equals(command.getSjbm())) {
			    	sb.append(" 修改前上级部门："+departmentManager.getDepartmentName(d.getSjbm())+
			    			" 修改后上级部门："+departmentManager.getDepartmentName(command.getSjbm()));
				}
			    if (StringUtils.isNotBlank(command.getBmlx()) && !d.getBmlx().equals(command.getBmlx())) {
			    	sb.append(" 修改前部门类型："+
		    				systemManager.getCodeValue("000006", d.getBmlx())+
		    				" 修改后部门类型："+
		    				systemManager.getCodeValue("000006", command.getBmlx()));
				}
			    if (StringUtils.isNotBlank(command.getZt())  && !d.getZt().equals(command.getZt())) {
			    	if(d.getZt().equals("0")){
			    		sb.append(" 修改前上级部门：无效，修改后上级部门：有效");
			    	}else if(d.getZt().equals("1")){
			    		sb.append(" 修改前上级部门：有效，修改后上级部门：无效");
			    	}
				}
			    if (StringUtils.isNotBlank(command.getYzmc()) && !d.getSjbm().equals(command.getYzmc())) {
			    	sb.append(" 修改前印章名称："+d.getYzmc()+"，修改后印章名称："+command.getYzmc());
				}
			    if (StringUtils.isNotBlank(command.getLxdz()) && !d.getSjbm().equals(command.getLxdz())) {
			    	sb.append(" 修改前联系地址："+d.getLxdz()+"，修改后联系地址："+command.getLxdz());
				}
			    if (StringUtils.isNotBlank(command.getLxdh1()) && !d.getSjbm().equals(command.getLxdh1())) {
			    	sb.append(" 修改前联系电话1："+d.getLxdh1()+"，修改后联系电话1："+command.getLxdh1());
				}
			    if (StringUtils.isNotBlank(command.getLxdh2()) && !d.getSjbm().equals(command.getLxdh2())) {
			    	sb.append(" 修改前联系电话2："+d.getLxdh2()+"，修改后联系电话1："+command.getLxdh2());
				}
			    if (StringUtils.isNotBlank(command.getLxdh3()) && !d.getSjbm().equals(command.getLxdh3())) {
			    	sb.append(" 修改前联系电话3："+d.getLxdh3()+"，修改后联系电话3："+command.getLxdh3());
				}
			    if (StringUtils.isNotBlank(command.getLxdh4()) && !d.getSjbm().equals(command.getLxdh4())) {
			    	sb.append(" 修改前联系电话4："+d.getLxdh4()+"，修改后联系电话4："+command.getLxdh4());
				}
			    if (StringUtils.isNotBlank(command.getLxcz()) && !d.getSjbm().equals(command.getLxcz())) {
			    	sb.append(" 修改前传真号码："+d.getLxcz()+"，修改后传真号码："+command.getLxcz());
				}
			    if (StringUtils.isNotBlank(command.getYzbm()) && !d.getSjbm().equals(command.getYzbm())) {
			    	sb.append(" 修改前邮政编码："+d.getYzbm()+"，修改后邮政编码："+command.getYzbm());
				}
			    if (StringUtils.isNotBlank(command.getJyrs()) && !d.getSjbm().equals(command.getJyrs())) {
			    	sb.append(" 修改前警员人数："+d.getJyrs()+"，修改后警员人数："+command.getJyrs());
				}
			    if (StringUtils.isNotBlank(command.getXjrs()) && !d.getSjbm().equals(command.getXjrs())) {
			    	sb.append(" 修改前协警人数："+d.getXjrs()+"，修改后协警人数："+command.getXjrs());
				}
			    if (StringUtils.isNotBlank(command.getSsjz()) && !d.getBmlx().equals(command.getSsjz())) {
			    	sb.append(" 修改前所属警种"+
		    				systemManager.getCodeValue("000008", d.getSsjz())+
		    				" 修改后所属警种："+
		    				systemManager.getCodeValue("000008", command.getSsjz()));
				}
			    if (StringUtils.isNotBlank(command.getSflsjg()) && !d.getBmlx().equals(command.getSflsjg())) {
			    	sb.append(" 修改前临时机关"+
		    				systemManager.getCodeValue("000005", d.getSflsjg())+
		    				" 修改后临时机关："+
		    				systemManager.getCodeValue("000005", command.getSflsjg()));
				}
		    }
		   
		    log.setCznr(sb.toString());
		    
			this.logManager.saveLog(log);
			
			return Common.messageBox("保存成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			String msg = e.getMessage();
			if ((msg == null) || (msg.equals(""))) {
				msg = "NullPointerException";
			}
			return Common.messageBox(msg, "0");
		}

	}

	@RequestMapping()
	@ResponseBody
	public Object editOne(HttpServletRequest request, String glbm) {
		Department department = null;
		try {

			department = this.departmentManager.getDepartment(glbm);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return department;
	}

	@RequestMapping()
	@ResponseBody
	public Object removeUnit(HttpServletRequest request,
			HttpServletResponse response, Department command) {
		UserSession userSession = (UserSession) WebUtils
				.getSessionAttribute(request, "userSession");
		try {
			if (StringUtils.isBlank(command.getGlbm())) {
				return Common.messageBox("参数传输异常！", "0");
			}

			int i = departmentManager.getUserCountForDepartment(command
					.getGlbm());
			if (i > 0) {

				return Common.messageBox("该部门下尚存在" + i
						+ "个用户，不能操作！\\n在删除该部门前，请先调整或删除该部门下的用户。", "0");
			}

			this.departmentManager.removeDepartment(command.getGlbm());
			systemManager.loadDepartment();

			Log log = new Log();
			log.setGlbm(userSession.getSysuser().getGlbm());
			log.setIp(userSession.getSysuser().getIp());
			log.setYhdh(userSession.getSysuser().getYhdh());
			StringBuffer sb=new StringBuffer();
			
			log.setCzlx("9033");
			sb.append("部门删除，操作条件：");
				
			if (command.getGlbm() != null && command.getGlbm().length() > 0) {
				sb.append(" 部门代码：" + command.getGlbm());
			}
			if (command.getBmmc() != null && command.getBmmc().length() > 0) {
				sb.append(" 部门名称：" + command.getBmmc());
			}
			
			log.setCznr(sb.toString());
		    
			this.logManager.saveLog(log);
			return Common.messageBox("删除成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			String msg = e.getMessage();
			if (StringUtils.isBlank(msg)) {
				msg = "NullPointerException";
			}
			return Common.messageBox(msg, "0");
		}

	}

	@RequestMapping()
	@ResponseBody
	public Map queryList(HttpServletRequest request,
			HttpServletResponse response, String page, String rows,
			Department department) {
		Map<String,Object> filter = new HashMap<String,Object>();
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		String dwdm = userSession.getSysuser().getGlbm();
		filter.put("curPage", page); // 页数
		filter.put("pageSize", rows); // 每页显示行数
		filter.put("dwdm",dwdm);//当前用户所属部门
		Map map = null;

		try {
			map = departmentManager.findDepartmentForMap(filter, department);
			
			Log log = new Log();
			log.setGlbm(userSession.getSysuser().getGlbm());
			log.setIp(userSession.getSysuser().getIp());
			log.setYhdh(userSession.getSysuser().getYhdh());
			StringBuffer sb=new StringBuffer();
			
			log.setCzlx("9031");
			sb.append("部门查询，操作条件：");
				
			if (department.getJb() != null && department.getJb().length() > 0) {
				sb.append(" 显示级别："
						+ systemManager.getCodeValue("000001", department.getJb()));
			}
			if (department.getGlbm() != null && department.getGlbm().length() > 0) {
				sb.append(" 部门代码：" + department.getGlbm());
			}
			if (department.getBmmc() != null && department.getBmmc().length() > 0) {
				sb.append(" 部门名称：" + department.getBmmc());
			}
			
			log.setCznr(sb.toString());
		    
			this.logManager.saveLog(log);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	@RequestMapping()
	@ResponseBody
	public Object getDepartmentForJb(HttpServletRequest request,
			HttpServletResponse response) {

		List listResult = new ArrayList();
		try {
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			Department dp = userSession.getDepartment();

			String jb = request.getParameter("jb");
			String glbm = request.getParameter("glbm");
			jb = String.valueOf((Integer.parseInt(jb) - 1));

			dp.setGlbm(glbm);
			List list = departmentManager.getPrefecture(dp);

			for (Object o : list) {
				Department department = (Department) o;
				if (department.getJb().equals(String.valueOf(jb))) {
					listResult.add(department);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listResult;
	}

	/**
	 * 根据条件展示机构菜单树
	 */
	@RequestMapping(params = "method=getDepartmentTreeByFilter")
	@ResponseBody
	public List<Map<String, Object>> getDepartmentTreeByFilter(
			@RequestParam("glbm") String glbm) {
		// String dep_id = request.getParameter("depId");
		Map map = new HashMap();
		map.put("glbm", glbm);
		List<Map<String, Object>> menuNodes = new ArrayList<Map<String, Object>>();
		List list = this.prefectureManager.getDepartmentTree(map);
		if (list.size() > 0) {
			Iterator<Object> it = list.iterator();
			while (it.hasNext()) {
				Map m = (Map) it.next();
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("id", m.get("glbm").toString());
				item.put("name", m.get("bmmc").toString().replaceAll(" ", ""));
				item.put("pId", m.get("sjbm").toString());
				menuNodes.add(item);
			}
		}
		return menuNodes;
	}

	/**
	 * 根据条件展示部门下拉菜单
	 */
	@RequestMapping(params = "method=getPrefectureForGlbm", method = RequestMethod.GET)
	@ResponseBody
	public List getPrefectureForGlbm(@RequestParam("glbm") String glbm) {

		List list = null;
		try {
			Department dp = new Department();
			dp.setGlbm(glbm);
			list = departmentManager.getPrefecture(dp);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	@RequestMapping(params = "method=syncDepartment", method = RequestMethod.POST)
	@ResponseBody
	public Map syncDepartment(HttpServletRequest request,
			HttpServletResponse response) {
		Map result = null;
		try {
			int i = this.departmentManager.SyncDepartment();
			if (i < 0) {
				result = Common.messageBox("同步失败！", "0");
			} else {
				i = this.systemManager.loadDepartment();
				if (i > 0)
					i = this.systemManager.loadPrefecture();
				if (i < 0)
					result = Common.messageBox("同步失败！", "0");
				else {
					result = Common.messageBox("同步成功！", "1");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
