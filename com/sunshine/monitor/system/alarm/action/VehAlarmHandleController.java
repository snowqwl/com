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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.alarm.bean.VehAlarmCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.service.VehAlarmHandleManager;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.ReCodeManager;
import com.sunshine.monitor.system.manager.service.SysparaManager;
import com.sunshine.monitor.system.manager.service.SystemManager;

@Controller
@RequestMapping(value="/vehAlarmHandleCtrl.do",params="method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VehAlarmHandleController {

	@Autowired
	@Qualifier("vehAlarmHandleManager")
	private VehAlarmHandleManager vehAlarmHandleManager;
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("sysparaManager")
	private SysparaManager sysparaManager;
	
	@Autowired
	@Qualifier("reCodeManager")
	private ReCodeManager reCodeManager;
	
	
	/**
	 * 进入出警反馈录入主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public String forwardHandled(HttpServletRequest request,String sffk,String begin,String end,String param){
		
		UserSession userSession = (UserSession) WebUtils
								.getSessionAttribute(request, "userSession");
		SysUser user = userSession.getSysuser();
		Department dp = userSession.getDepartment();
		
		request.setAttribute("yhmc", user.getYhmc());
		request.setAttribute("bmmc", dp.getBmmc());
		
		try {
			if (param != null && param.equals("count")) {
				 end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				 begin = this.systemManager.getSysDate("-180", false) + " 00:00:00";
			}
			//List sfljList = systemManager.getCodes("130032");
			//List wljdyyList = systemManager.getCodes("130003");
			
			//request.setAttribute("sfljList", sfljList);
			//request.setAttribute("wljdyyList", wljdyyList);
			request.setAttribute("bjlbList", systemManager.getCodes("120005"));
			// 报警大类
			request.setAttribute("bjdlList", systemManager.getCodes("120019"));
			// 号牌种类
			request.setAttribute("hpzlList", systemManager.getCodes("030107"));
			
			request.setAttribute("sffkList", systemManager.getCodes("130006"));
			request.setAttribute("begin", begin);
			request.setAttribute("end", end);
			
			if (sffk != null && sffk.length() > 0) {
				request.setAttribute("sffk", sffk);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "alarm/alarmhandle";
	}
	/**
	 * 查询出警反馈录入列表
	 * 
	 * @param request
	 * @param response
	 * @param info
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map queryList(String page, String rows,VehAlarmCmd info,HttpServletRequest request){
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		
		Map map = null;
		
		try{
			UserSession userSession = (UserSession) WebUtils
						.getSessionAttribute(request, "userSession");
			Department dp = userSession.getDepartment();
			
			info.setZljsdw(dp.getGlbm());
			map = vehAlarmHandleManager.getAlarmHandleForMap(filter, info);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
		
	}
	
     /**
      * 获取卡口预案信息
      * @param kdbh
      * @return
      */
	@RequestMapping()
	@ResponseBody
	public Object getKKYaInfo(String kdbh) {
		List yaList = null;
		try {
			yaList = reCodeManager.getReCodeListForKdbh(kdbh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return yaList;
	}
	
	@RequestMapping
	@ResponseBody
	public void setHandle(VehAlarmHandled handle,HttpServletRequest request,HttpServletResponse response) throws IOException{
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
			}else if(StringUtils.isBlank(user.getJh())){
				m = Common.messageBox("您操作的用户缺少警号,操作失败!","0");
			}else{
				
				Syspara syspara = sysparaManager.getSyspara("xzqh", "1", "");
				String xzqh = syspara.getCsz();
				
				handle.setLrr(user.getYhdh());
				handle.setLrdwdm(user.getGlbm());
				handle.setLrrjh(user.getJh());
				handle.setLrrmc(user.getYhmc());
				handle.setLrdwmc(dp.getBmmc());
				
				byte pic[] = (byte[]) null;
				MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
				MultipartFile picobj = mrequest.getFile("picVal");
				try {
					if (picobj != null && !picobj.isEmpty()){
						pic = picobj.getBytes();
						handle.setTp(pic);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				m = vehAlarmHandleManager.setHandle(handle, xzqh, dp.getSsjz());
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
	 * （根据报警序号）查询veh_alarmrec表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public VehAlarmrec getVehAlarmrec( String bjxh){
		VehAlarmrec alarm = null;
		try{
			alarm = vehAlarmHandleManager.getVehAlarmrecForBjxh(bjxh);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return alarm ;
	}
	/**
	 * （根据指令序号）查询veh_alarm_cmd表信息
	 * @param zlxh 指令序号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public VehAlarmCmd getVehAlarmCmd(String zlxh){
		VehAlarmCmd cmd = null;
		try{
			cmd = this.vehAlarmHandleManager.getVehAlarmCmdForZlxh(zlxh);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return cmd;
		
	}
	/**
	 * （根据报警序号）查询veh_alarm_cmd表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public Object getVehAlarmCmdXml(String bjxh){
		StringBuffer sb = new StringBuffer();
		
		try{
			List list = this.vehAlarmHandleManager.getAlarmCmdListForBjxh(bjxh);
			
			if(list.size() > 0){
				
				/*sb.append("<tr ><td width=\"11%\">下达指令人</td><td width=\"22%\">下达指令时间</td><td width=\"34%\">下达指令内容</td><td width=\"33%\">指令接收单位</td></tr>");
				
				for(Object o : list){
					VehAlarmCmd cmd = (VehAlarmCmd)o;
					sb.append("<tr class=\"out\" onMouseOver=\"this.className='over'\" onMouseOut=\"this.className='out'\" title=\"下达指令单位：").append(cmd.getZldwmc()).append("\">");
					sb.append("<td>").append(cmd.getZlrmc()).append("</td>");
					sb.append("<td>").append(cmd.getZlsj()).append("</td>");
					sb.append("<td>").append(cmd.getZlnr()).append("</td>");
					sb.append("<td>").append(cmd.getBy2()).append("</td>");
					sb.append("</tr>");
				}*/
			
				return list;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	/**
	 * （根据报警序号）查询veh_alarm_livetrace表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public Object getVehAlarmLivetraceXml(String bjxh){
		
		try {
			List list = this.vehAlarmHandleManager.getVehAlarmLivetraceForBjxh(bjxh);
			if(list.size() > 0){
				
				return list;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	/**
	 * （根据报警序号）查询veh_alarm_handled表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public Object getAlarmHandXml(String bjxh){
		
		try{
			List list = this.vehAlarmHandleManager.getVehAlarmHandleForBjxh(bjxh);

			if(list.size() > 0){
				
				return list;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	/**
	 * （根据反馈编号）查询veh_alarm_handled表信息
	 * @param fkbh 反馈编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public VehAlarmHandled getAlarmHandleForFkbh(String fkbh){
		VehAlarmHandled handle = null;
		
		try {
			handle = vehAlarmHandleManager.getAlarmHandleForFkbh(fkbh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return handle;
	}
}
