package com.sunshine.monitor.system.analysis.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.analysis.bean.CaseEntity;
import com.sunshine.monitor.system.analysis.bean.CaseGroupEntity;
import com.sunshine.monitor.system.analysis.service.CaseCenterManager;
import com.sunshine.monitor.system.analysis.service.CaseTouchManager;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.SystemManager;

/**
 * 案事件中心controller
 *
 */
@Controller
@RequestMapping(value = "/caseCenter.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CaseCenterController {

	private static final String MAIN = "analysis/caseListMain";
	private static final String CENTER = "analysis/caseCenterMain";
	
	@Autowired
	@Qualifier("caseCenterManager")
	private CaseCenterManager caseCenterManger;
	
	@Autowired(required=true)
	private CaseTouchManager caseTouchManager;
	
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	/**
	 * 跳转到新增专案主页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
    public String forwardMain(HttpServletRequest request,HttpServletResponse response){
		try {
			//request.setAttribute("ajlbList",this.systemManager.getCodes("157001"));
			List ajlxList = getCaseDm("","案件类别","车");
			request.setAttribute("ajlxList",ajlxList);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return MAIN;
    }
	
	/**
	 * 跳转到专案中心页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public String forwardCenter(HttpServletRequest request,HttpServletResponse response){
		return CENTER;
	}
	
	/**
	 * 分页查询：案件信息
	 * @param request
	 * @param response
	 * @param entity
	 * @param page 页码
	 * @param rows 每页展示行数
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String, Object> findCaseByPage(HttpServletRequest request,
			HttpServletResponse response, CaseEntity entity, String page,
			String rows) {
		Map<String,Object> result = null;
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("page",page);
		filter.put("rows", rows);
        try {
        	result = this.caseCenterManger.findCaseByPage(entity, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 分页查询：案件信息
	 * @param request
	 * @param response
	 * @param entity
	 * @param page 页码
	 * @param rows 每页展示行数
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public CaseEntity getCaseDetail(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> entity = new HashMap<String,Object>();
		CaseEntity result = null;
		String ajbh = "";
		if(request.getParameter("ajbh")!=null&&request.getParameter("ajbh").toString()!=""){
			ajbh = request.getParameter("ajbh").toString();
		}
		entity.put("ajbh",ajbh);
		try {
			result = this.caseCenterManger.getCaseDetail(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

    /**
     * 保存专案信息
     * @param request
     * @param response
     * @param entity
     * @return
     */
	@RequestMapping
	@ResponseBody
	public Map<String,String> saveCaseGroup(HttpServletRequest request,HttpServletResponse response,CaseGroupEntity entity){
		Map<String,String> result = new HashMap<String,String>();
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		SysUser sysuser = userSession.getSysuser();
		entity.setFzrjh(sysuser.getYhdh());
		entity.setFzrxm(sysuser.getYhmc());
		entity.setXzqh(sysuser.getGlbm().substring(0,6));
		try {
		    this.caseCenterManger.saveCaseGroup(entity);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code","0");
			result.put("msg","保存失败！");
			return result;
		}
		result.put("code","1");
		result.put("msg","保存成功！");
		return result;
	}
	
	
	
	/**
	 * 根据专案id查出分析工程的结果集
	 * @param request
	 * @param response
	 * @param caseEntity 专案实体
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public List<Map<String,Object>> queryAnalysisProjectList(HttpServletRequest request,HttpServletResponse response,CaseGroupEntity caseEntity){
		List<Map<String,Object>> result =null;
		try {
			//caseEntity.setZabh("1");
			result=this.caseCenterManger.queryAnalysisProjectList(caseEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 分页查询专案信息
	 * @param request
	 * @param response
	 * @param entity
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> findCaseGroupByPage(HttpServletRequest request,HttpServletResponse response,
			CaseGroupEntity entity,String page,String rows,String saveanalysis){
		Map<String,Object> result = null;
		Map<String,Object> filter = new HashMap<String,Object>();
		if(entity.getZamc()!=null && !"".equals(entity.getZamc().toString())){
			String zamc = entity.getZamc();
			try {
				entity.setZamc(URLDecoder.decode(URLDecoder.decode(zamc,"UTF-8"),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		filter.put("page",page);
		filter.put("rows", rows);
		filter.put("saveanalysis", saveanalysis); // 保存分析标志，当saveanalysis有值且为1时则只需要查询专案编号和专案名称
		try {
			result = this.caseCenterManger.findCaseGroupByPage(entity, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List getCaseDm(String ywxtmc,String dmlb,String dmmc){
		List dmlist = new ArrayList();
		try {
			dmlist = this.caseTouchManager.getCaseDm(ywxtmc,dmlb,dmmc);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return dmlist;
	}
}
