package com.sunshine.monitor.system.alarm.action;

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
import com.sunshine.monitor.system.alarm.bean.VehAlarmCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmLivetrace;
import com.sunshine.monitor.system.alarm.service.VehAlarmLiveTraceManager;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.ReCodeManager;
import com.sunshine.monitor.system.manager.service.SystemManager;


@Controller
@RequestMapping(value="/vehAlarmLiveTraceCtrl.do",params="method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VehAlarmLiveTraceController {

	@Autowired
	@Qualifier("vehAlarmLiveTraceManager")
	private VehAlarmLiveTraceManager vehAlarmLiveTraceManager;
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("reCodeManager")
	private ReCodeManager reCodeManager;
	
	
	@RequestMapping
	public String forwardHandled(HttpServletRequest request){
		
		
		UserSession userSession = (UserSession) WebUtils
				.getSessionAttribute(request, "userSession");
		SysUser user = userSession.getSysuser();
		Department dp = userSession.getDepartment();

		try {
			request.setAttribute("yhmc", user.getYhmc());
			request.setAttribute("bmmc", dp.getBmmc());
			request.setAttribute("bjlbList", systemManager.getCodes("120005"));
			request.setAttribute("bjdlList", systemManager.getCodes("120019"));
			request.setAttribute("hpzlList", systemManager.getCodes("030107"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "alarm/alarmlivetrace";
	}
	
	@RequestMapping
	public String forwardLjHandled(HttpServletRequest request){
		
		try {
			request.setAttribute("bjlbList", systemManager.getCodes("120005"));
			request.setAttribute("bjdlList", systemManager.getCodes("120019"));
			request.setAttribute("hpzlList", systemManager.getCodes("030107"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "alarm/alarmLjList";
	}
	
	/**
	 * 省厅(全省)拦截数查询界面
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String forwardStLjHandled(HttpServletRequest request){
		
		try {
			request.setAttribute("bjlbList", systemManager.getCodes("120005"));
			request.setAttribute("bjdlList", systemManager.getCodes("120019"));
			request.setAttribute("hpzlList", systemManager.getCodes("030107"));
			request.setAttribute("citylist", this.systemManager
					.getCodes("105000"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "alarm/alarmStLjList";
	}
	
	@RequestMapping()
	@ResponseBody
	public Map queryList(String page, String rows,VehAlarmCmd info,String gzbj,HttpServletRequest request){
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		
		Map map = null;
		
		try{
			
			UserSession userSession = (UserSession) WebUtils
						.getSessionAttribute(request, "userSession");
			Department dp = userSession.getDepartment();
			
			info.setZldw(dp.getGlbm());
			map = vehAlarmLiveTraceManager.getAlarmLiveTraceForMap(filter, info,gzbj);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
		
	}

	/**
	 * 拦截情况查询
	 * @param page
	 * @param rows
	 * @param info
	 * @param request
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map queryLJList(String page, String rows,VehAlarmCmd info,HttpServletRequest request){
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		
		Map map = null;
		
		try{
			
			UserSession userSession = (UserSession) WebUtils
						.getSessionAttribute(request, "userSession");
			Department dp = userSession.getDepartment();
			
			info.setZldw(dp.getGlbm());
			map = vehAlarmLiveTraceManager.getAlarmLJForMap(filter, info);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
		
	}
	
	
	/**
	 * 全省（省厅）拦截情况查询
	 * @param page
	 * @param rows
	 * @param info
	 * @param request
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map querySTLJList(String page, String rows,VehAlarmCmd info,HttpServletRequest request){
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		
		Map map = null;
		
		try{
			
                
			String cityname = this.systemManager.getCodeValue("105000", request.getParameter("city"));

			UserSession userSession = (UserSession) WebUtils
						.getSessionAttribute(request, "userSession");
			Department dp = userSession.getDepartment();
			
			info.setZldw(dp.getGlbm());
			map = vehAlarmLiveTraceManager.getAlarmSTLJForMap(filter, info,cityname);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
		
	}
	
	
	
	
	
	@RequestMapping
	@ResponseBody
	public void saveLiveTrace(VehAlarmLivetrace liveTrace,HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map m = null;
		
		Department dp = null;
		
		try {
			
			UserSession userSession = (UserSession) WebUtils
						.getSessionAttribute(request, "userSession");
			SysUser user = userSession.getSysuser();
		    dp = userSession.getDepartment();

			if(dp == null)
				m = Common.messageBox("非法操作!", "0");
			else if(StringUtils.isBlank(dp.getSsjz())){
				m = Common.messageBox("您操作的用户所在的部门缺少警种!", "0");
			}else{
		
				if(StringUtils.isBlank(user.getJh())){
					m =  Common.messageBox("当前用户警号为空，请进入警综系统进行设置！", "0");
				}else{
				
					liveTrace.setLrr(user.getYhdh());
					liveTrace.setLrrmc(user.getYhmc());
					liveTrace.setLrdwdm(user.getGlbm());
					liveTrace.setLrrjh(user.getJh());
					liveTrace.setLrdwmc(dp.getBmmc());
					
					m = vehAlarmLiveTraceManager.saveLiveTrace(liveTrace);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			m = Common.messageBox("程序异常!", "0");
		}
		
		
		PrintWriter writer = null;
		
		try {
			writer = response.getWriter();
			writer.print(JSONObject.fromObject(m));
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
	 * （根据报警序号）查询veh_alarm_cmd表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public Object getVehAlarmCmdListForBjxh(String bjxh,String kdbh){
		List list = null ;
		List yaList = null;
		Map<String,Object> result = new HashMap<String, Object>();
		try{
			
			list = vehAlarmLiveTraceManager.getVehAlarmCmdListForBjxh(bjxh);
			yaList = reCodeManager.getReCodeListForKdbh(kdbh);
		}catch(Exception e){
			e.printStackTrace();
		}
		result.put("cmdList", list);
		result.put("recodelist", yaList);
		return result;
	}
	
	public ReCodeManager getReCodeManager() {
		return reCodeManager;
	}

	public void setReCodeManager(ReCodeManager reCodeManager) {
		this.reCodeManager = reCodeManager;
	}
	
}
