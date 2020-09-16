package com.sunshine.monitor.system.monitor.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.monitor.service.KhzbProjectManager;

@Controller
@RequestMapping(value = "/kkzxqkmonitor.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class KkzxqkMonitorCtroller {
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	/**
	 * 跳转卡口在线情况监测页面
	 * @param request
	 * @param response
	 * @return
	 */
	/*@RequestMapping()
	public String findForward(HttpServletRequest request,
			HttpServletResponse response) {
		return "monitor/kkzxqkmonitormain";
	}*/
	@RequestMapping()
	public String findForward(HttpServletRequest request,
			HttpServletResponse response) {
		return "monitor/kkzxqkredismonitormain";
	}
	
	/**
	 * 跳转卡口在线情况监测详细页面
	 * @param request
	 * @param response
	 * @param tjsj
	 * @param dwdm
	 * @return
	 */
	@RequestMapping(params = "method=kkzxqkDetail")
	public String kkzxqkDetail(HttpServletRequest request,
			HttpServletResponse response, String tjsj, String dwdm) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		Department dp = userSession.getDepartment();
		String year = tjsj.substring(0, 4);
		String month = tjsj.substring(5, 7);
		String last_Month = String.valueOf((Integer.parseInt(month) - 1));
		if (last_Month.length() < 2)
			last_Month = "0" + last_Month;
		String kssj = year + "-" + last_Month + "-26 00:00:00";
		String jssj = year + "-" + month + "-25 23:59:59";
		if("00".equals(last_Month)){
			kssj = (Integer.parseInt(year)-1) +"-12-26 00:00:00";
		}
		request.setAttribute("kssj", kssj);
		request.setAttribute("jssj", jssj);

		if (dwdm != null && dwdm.length() > 0)
			request.setAttribute("dwdm", dwdm);
		if(dp.getGlbm().startsWith("4300")){
			return "monitor/st_report/st_kkzxqkmonitordetail";
		}
		return "monitor/kkzxqkmonitordetail";
	}

	@RequestMapping(params = "method=main")
	public String main(HttpServletRequest request,
			HttpServletResponse response, String dwdm) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		Department dp = userSession.getDepartment();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String this_Month = format.format(new Date());

		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		String last_Month = new SimpleDateFormat("yyyy-MM").format(c.getTime());

		String kssj = last_Month + "-26 00:00:00";
		String jssj = this_Month + "-25 23:59:59";

		request.setAttribute("kssj", kssj);
		request.setAttribute("jssj", jssj);
		request.setAttribute("dwdm", dwdm);
		if(dp.getGlbm().startsWith("4300")){
			return "monitor/st_report/st_kkzxqkmonitordetail";
		}
		return "monitor/kkzxqkmonitordetail";
	}
	
	/**
	 * 跳转卡口在线情况监测页面(改造后)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String findForwardKkzx(HttpServletRequest request,
			HttpServletResponse response) {		
		/*String[] ds = {"cd","yy","cz","zz"};
		String basefailedstr = "";	
		String cdflag = "success";
		String yyflag = "success";
		String czflag = "success";
		String zzflag = "success";
		for(int i=0;i<ds.length;i++){
			try {
				basefailedstr = this.systemManager.getBaseConnectionState(ds[i]);
				if("cd".equals(basefailedstr)){
					cdflag = "failure";
				}else if("yy".equals(basefailedstr)){
					yyflag = "failure";
				}
				else if("cz".equals(basefailedstr)){
					czflag = "failure";
				}
				else if("zz".equals(basefailedstr)){
					zzflag = "failure";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("cdflag", cdflag);
		request.setAttribute("yyflag", yyflag);
		request.setAttribute("czflag", czflag);
		request.setAttribute("zzflag", zzflag);*/
		return "monitor/newkkzxmonitormain";
	}
	
}
