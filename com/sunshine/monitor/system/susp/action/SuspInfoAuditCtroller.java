package com.sunshine.monitor.system.susp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoAuditApproveManager;

@Controller
@RequestMapping(value="/suspinfoAuditCtrl.do",params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuspInfoAuditCtroller {

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("suspinfoAuditApproveManager")
	private SuspinfoAuditApproveManager suspinfoAuditApproveManager;
	
	@Autowired
	@Qualifier("logManager")
	private LogManager logManager;
	/**
	 * 跳转布控审核主页
	 * @param response
	 * @param request
	 * @param begin
	 * @param param
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request,String begin, String end, String param,String isFromIndex){
		try{
			if("".equals(isFromIndex) || isFromIndex==null){
				isFromIndex="0";//默认为0，表示号牌号码默认为"湘"字，1表示从首页传过来的，页面不显示"湘"
			}
			if (param != null && param.equals("count")) {
				 end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				 begin = this.systemManager.getSysDate("-180", false) + " 00:00:00";
			}
			if (param != null && param.equals("overtimecount")) {
				end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				begin = this.systemManager.getSysDate("-180", false)
						+ " 00:00:00";
				request.setAttribute("aduitovertime", "1");
				List list = systemManager.getCode("120019");
				list.remove(0);
				list.remove(0);
				request.setAttribute("bkdlList", list);
			}
			else
			request.setAttribute("bkdlList", systemManager.getCode("120019"));
			request.setAttribute("hpzlList", systemManager.getCode("030107"));
			request.setAttribute("auditList", systemManager.getCode("190002"));
			request.setAttribute("bklbList", systemManager.getCode("120005"));
			
			UserSession userSession = (UserSession) WebUtils
									.getSessionAttribute(request, "userSession");
			SysUser user = userSession.getSysuser();
			Department dp = userSession.getDepartment();
			
			request.setAttribute("yhmc", user.getYhmc());
			request.setAttribute("bmmc", dp.getBmmc());
			request.setAttribute("begin", begin);
			request.setAttribute("end", end);
			request.setAttribute("isFromIndex", isFromIndex);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "engineer/suspinfoaudit";
	}
	
	/**
	 * 跳转布控审核(领导审核)主页
	 * @param request
	 * @param bkxh
	 * @return
	 */
	@RequestMapping()
	public String findForwardWin(HttpServletRequest request,String bkxh){
		
		try {
			request.setAttribute("auditList", systemManager.getCode("190002"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UserSession userSession = (UserSession) WebUtils
							.getSessionAttribute(request, "userSession");
		SysUser user = userSession.getSysuser();
		Department dp = userSession.getDepartment();
		
		request.setAttribute("bkxh", bkxh);
		request.setAttribute("yhmc", user.getYhmc());
		request.setAttribute("bmmc", dp.getBmmc());

		return "engineer/suspauditwin";
	}
	
	/**
	 * 审核数据列表
	 * @param page
	 * @param rows
	 * @param request
	 * @param info
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map queryList(String page, String rows,VehSuspinfo info,HttpServletRequest request){
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		Map map = null;
		try{
			UserSession userSession = (UserSession) WebUtils
										.getSessionAttribute(request, "userSession");
			Department dp = userSession.getDepartment();
			String glbm = dp.getGlbm();
			if(request.getParameter("overtime")!=null){
				if(request.getParameter("overtime").equals("1")){
					map = suspinfoAuditApproveManager.findSuspinfoAuditOverTimeForMap(filter,info,glbm);
				}
			}
			else
			map = suspinfoAuditApproveManager.findSuspinfoAuditForMap(filter,info,glbm);
			Log log = new Log();
			log.setGlbm(userSession.getSysuser().getGlbm());
			log.setIp(userSession.getSysuser().getIp());
			log.setYhdh(userSession.getSysuser().getYhdh());
		    log.setCzlx("2210");
		    StringBuffer sb=new StringBuffer();
		    sb.append("布控领导审核查询成功，操作条件： ");
		    if (info.getHphm() != null && info.getHphm().length() > 0) {
				sb.append("，号牌号码："+ info.getHphm());
			}
			if (info.getKssj() != null && info.getKssj().length() > 0) {
				sb.append("，开始时间："
						+ info.getKssj().substring(0,10));
			}
			if (info.getJssj() != null && info.getJssj().length() > 0) {
				sb.append("，结束时间："
						+ info.getJssj().substring(0,10));
			}
			String hpzl = systemManager.getCodeValue("030107", info.getHpzl());
			if (hpzl != null && hpzl.length() > 0) {
				sb.append("，号牌种类："
						+ hpzl);
			}
			String bkdl = systemManager.getCodeValue("120019", info.getBkdl());
			if (bkdl != null && bkdl.length() > 0) {
				sb.append("，布控类型："
						+ bkdl);
			}
			String bkzl = systemManager.getCodeValue("120005", info.getBklb());
			if (bkzl != null && bkzl.length() > 0) {
				sb.append("，布控子类："
						+ bkzl);
			}
		    log.setCznr(sb.toString());
		    
			this.logManager.saveLog(log);
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping()
	@ResponseBody
	public void saveAudit(AuditApprove info,HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map v = null;
		String users=request.getParameter("dxjsr");
		String lxdh=request.getParameter("lxdh");
		if(!StringUtils.isBlank(users)){
			info.setUser(users);
		}
		if(!StringUtils.isBlank(lxdh)){
			info.setLxdh(lxdh);
		}
		if(StringUtils.isBlank(info.getBkxh()) || StringUtils.isBlank(info.getCzjg())){
			v = Common.messageBox("参数传输异常！","0");
		}else{
			/***************************************/
				UserSession userSession = (UserSession) WebUtils
											.getSessionAttribute(request, "userSession");
				SysUser user = userSession.getSysuser();
		
				Department dp = userSession.getDepartment();
			/***************************************/
			
			info.setCzr(user.getYhdh());
			info.setCzrdw(user.getGlbm());
			info.setCzrdwmc(dp.getBmmc());
			info.setCzrjh(user.getJh());
			info.setCzrmc(user.getYhmc());
			
			try {
				
				v = suspinfoAuditApproveManager.saveAudit(info);
			} catch (Exception e) {
				e.printStackTrace();
				v = Common.messageBox("程序异常！","0");
			}
		}
		
		PrintWriter writer = null;
		
		try {
			writer = response.getWriter();
			writer.print(JSONObject.fromObject(v));
		} catch (IOException e) {
			throw e;
		}finally{
			if(writer != null){
				writer.flush();
				writer.close();
			}
		}
		
	}
	
	/**
	 * 批量审核
	 * @param info
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping()
	@ResponseBody
	public Object batchSaveAudit(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
		SysUser user = userSession.getSysuser();
		Department dp = userSession.getDepartment();
		int count = 0;
		int fale = 0;
		try {
			List<Map<String,Object>> list = suspinfoAuditApproveManager.getVehSuspinfoList();
			if(list.size() > 0){
				for(Map<String,Object> resMap : list){
					AuditApprove info = new AuditApprove();
					info.setBkxh(String.valueOf(resMap.get("BKXH")));
					info.setCzjg("1");
					info.setMs("同意");
					info.setCzr(user.getYhdh());
					info.setCzrdw(user.getGlbm());
					info.setCzrdwmc(dp.getBmmc());
					info.setCzrjh(user.getJh());
					info.setCzrmc(user.getYhmc());
					map = suspinfoAuditApproveManager.saveAudit(info);
					if(map.get("code").equals("1"))
						count ++;
					else 
						fale ++;
				}
			}
		} catch (Exception e) {
			fale ++;
			e.printStackTrace();
			map = Common.messageBox("程序异常！","0");
		}
		/*String xhs = request.getParameter("bkxhs");
		String[] bkxhs = xhs.split(",");
		for(int i = 0; i < bkxhs.length; i++){
			AuditApprove info = new AuditApprove();
			info.setBkxh(bkxhs[i]);
			info.setCzjg("1");
			info.setMs("同意");
			info.setCzr(user.getYhdh());
			info.setCzrdw(user.getGlbm());
			info.setCzrdwmc(dp.getBmmc());
			info.setCzrjh(user.getJh());
			info.setCzrmc(user.getYhmc());
			try {
				map = suspinfoAuditApproveManager.saveAudit(info);
				if(map.get("code").equals("1"))
					count ++;
				else 
					fale ++;
			} catch (Exception e) {
				fale ++;
				e.printStackTrace();
				map = Common.messageBox("程序异常！","0");
			}
		}*/
		String msg = "成功审核" + count + "条，失败" + fale + "条。";
		map.put("msg", msg);
		return map;
	}
}
