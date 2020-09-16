package com.sunshine.monitor.system.monitor.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.veh.bean.VehRealpass;
import com.sunshine.monitor.system.veh.service.RealManager;

@Controller
@RequestMapping(value="/devicemonitor.do", params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeviceMonitorCtroller {
	
	@Autowired
	private GateManager gateManager;
	
	@Autowired
	private SystemManager systemManager;
	
	@Autowired
	private RealManager realManager;
	
	
	/**
	 * 进入过车实时监测主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String findForward(HttpServletRequest request){
		
		try {
			request.setAttribute("flag", "list");
			if (this.systemManager.getParameter("xzqh", request).equals(
					"430000000000")) {
				request.setAttribute("departmentList", this.gateManager
						.getDepartmentsByKdbh());
				request.setAttribute("sign", "true");
			} else {
				request.setAttribute("departmentList", this.gateManager
						.getDepartmentsByKdbh());
				request.setAttribute("sign", "false");
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("exception", e);
			
			return "error/showDBError";
		}
		
		return "manager/deviceTestmain";
	}
	
	
	/**
	 * 跳转卡口状态监控页面
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public ModelAndView deviceTest(HttpServletRequest request,
			HttpServletResponse response,String jkdss) {
		
		try {
//			List deviceList = this.deviceManager.getAllDevices();
			Map<String,Object> filter = new HashMap<String, Object>();
			if (jkdss != null && jkdss.length() > 0) {
				filter.put("kdbhs", jkdss);
			}
			List gateExtendList = gateManager.getAllGateExtend(filter);
			List returnList = new ArrayList();
			if (gateExtendList != null) {
				String tpUrl = "";
				VehRealpass vehrealpass = null;
				VehRealpass vr = null;
				for (int i = 0; i < gateExtendList.size(); i++) {
					vehrealpass = new VehRealpass();
					int sj = 0;
					CodeGateExtend cd = (CodeGateExtend) gateExtendList.get(i);
					CodeGate ga = this.gateManager.getGate(cd.getKdbh());
					if(ga!=null){
					vehrealpass.setKdbh(ga.getKdmc());
					vehrealpass.setFxbh(cd.getFxmc());
					vehrealpass.setSblx(this.systemManager.getCodeValue(
							"120002", cd.getSblx()));
					vehrealpass.setDwdm(ga.getDwdmmc());
					vehrealpass.setSbzt(cd.getSbzt());
					vehrealpass.setSbztmc(this.systemManager.getCodeValue(
							"140015", cd.getSbzt()));
					
					vr = this.realManager.getJianKongInfo(cd.getKdbh(),cd.getFxbh());
					if (vr != null) {
						
						/*tpUrl = vr.getTp1();
						if (!"".equals(tpUrl)) {
							URL url = new URL(tpUrl);
							vehrealpass.setSjip(url.getHost());
						}*/
						
						if (vr.getComparedate().doubleValue() > 30.0D) {
							sj = vr.getComparedate().intValue();
							String ssj = "";
							int fen = sj % 60;
							if (fen > 0) {
								ssj = fen + "分钟" + ssj;
							}
							int hour = sj / 60;
							if (hour > 0) {
								ssj = hour + "小时" + ssj;
							}
							if (ssj.length() > 0) {
								ssj = "延时" + ssj;
							}
							vehrealpass.setBy1("1");
							vehrealpass.setZt(ssj);
							
						} else {
							vehrealpass.setBy1("0");
							vehrealpass.setZt("正常");
						}
						if (!"".equals(vr.getCdbh()) && vr.getCdbh() != null) {
							vehrealpass.setCdbh(Integer.valueOf(vr.getCdbh()).toString());
						} else {
							vehrealpass.setCdbh("0");
						}
						vehrealpass.setGcsj(vr.getGcsj());
					} else {
						vehrealpass.setGcsj("");
						vehrealpass.setBy1("1");
						vehrealpass.setZt("没有数据上传");
					}
					returnList.add(vehrealpass);
					}
				}
			}
			request.setAttribute("realJianKongList", returnList);
			request.setAttribute("jkdss", jkdss);
			
		} catch (Exception e) {
			Common.setErrors(e, request);
		}
		
		return new ModelAndView("manager/deviceTestDetail"); 
	}
	
	@RequestMapping()
	@ResponseBody()
	public Object deviceTestObject(HttpServletRequest request,
			HttpServletResponse response,String jkdss) {
		Map<String,Object> map = new HashMap<String, Object>();
		List returnList = new ArrayList();
		try {
//			List deviceList = this.deviceManager.getAllDevices();
			Map<String,Object> filter = new HashMap<String, Object>();
			if (jkdss != null && jkdss.length() > 0) {
				filter.put("kdbhs", jkdss);
			}
			List gateExtendList = gateManager.getAllGateExtend(filter);
			if (gateExtendList != null) {
				String tpUrl = "";
				VehRealpass vehrealpass = null;
				VehRealpass vr = null;
				for (int i = 0; i < gateExtendList.size(); i++) {
					vehrealpass = new VehRealpass();
					int sj = 0;
					CodeGateExtend cd = (CodeGateExtend) gateExtendList.get(i);
					CodeGate ga = this.gateManager.getGate(cd.getKdbh());
					if(ga!=null){
					vehrealpass.setKdbh(ga.getKdmc());
					vehrealpass.setFxbh(cd.getFxmc());
					vehrealpass.setSblx(this.systemManager.getCodeValue(
							"120002", cd.getSblx()));
					vehrealpass.setDwdm(ga.getDwdmmc());
					vehrealpass.setSbzt(cd.getSbzt());
					vehrealpass.setSbztmc(this.systemManager.getCodeValue(
							"140015", cd.getSbzt()));
					
					vr = this.realManager.getJianKongInfo(cd.getKdbh(),cd.getFxbh());
					if (vr != null) {
						
						/*tpUrl = vr.getTp1();
						if (!"".equals(tpUrl)) {
							URL url = new URL(tpUrl);
							vehrealpass.setSjip(url.getHost());
						}*/
						
						if (vr.getComparedate().doubleValue() > 30.0D) {
							sj = vr.getComparedate().intValue();
							String ssj = "";
							int fen = sj % 60;
							if (fen > 0) {
								ssj = fen + "分钟" + ssj;
							}
							int hour = sj / 60;
							if (hour > 0) {
								ssj = hour + "小时" + ssj;
							}
							if (ssj.length() > 0) {
								ssj = "延时" + ssj;
							}
							vehrealpass.setBy1("1");
							vehrealpass.setZt(ssj);
							
						} else {
							vehrealpass.setBy1("0");
							vehrealpass.setZt("正常");
						}
						if (!"".equals(vr.getCdbh()) && vr.getCdbh() != null) {
							vehrealpass.setCdbh(Integer.valueOf(vr.getCdbh()).toString());
						} else {
							vehrealpass.setCdbh("0");
						}
						vehrealpass.setGcsj(vr.getGcsj());
					} else {
						vehrealpass.setGcsj("");
						vehrealpass.setBy1("1");
						vehrealpass.setCdbh("");
						vehrealpass.setZt("没有数据上传");
					}
					returnList.add(vehrealpass);
					}
				}
			}
			map.put("realJianKongList", returnList);
			
		} catch (Exception e) {
			Common.setErrors(e, request);
		}
		
		return map;
	}
}
