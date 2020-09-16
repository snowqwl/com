package com.sunshine.monitor.system.veh.action;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sunshine.monitor.comm.util.picws.PicWSInvokeTool;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.service.SysparaManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.manager.service.UrlManager;
import com.sunshine.monitor.system.veh.bean.VehRealpass;
import com.sunshine.monitor.system.veh.service.RealManager;

@Controller
@RequestMapping(value="/realpassCtrl.do",params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RealpassController {
	
	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("realManager")
	private RealManager realManager;
	
	@Autowired
	@Qualifier("urlManager")
	private UrlManager urlManager;
	
	@Autowired
	private SysparaManager sysparaManager;
	
	/**
	 * 进入单卡口实时监控主页
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String findForward(HttpServletRequest request){
		/**
		 try {
			 request.setAttribute("departmentList", this.gateManager
					.getDepartmentsByKdbh());
			List<CodeUrl> list = this.urlManager.getCodeUrls();
			request.setAttribute("urls",list);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("exception", e);
			return "error/showDBError";
		}*/
		return "veh/realSingleGetePass";
	}
	/**
	 * 进入多卡口实时监控主页
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String findList(HttpServletRequest request){
		/**
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
		*/
		return "veh/realListGetePass";
	}
	
	/**
	 * 多卡口实时监测查询
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String realList(HttpServletRequest request){
		String mykds = request.getParameter("jkdss");
		/** 市州行政区划代码  */
		String city = request.getParameter("city");
		request.setAttribute("mykds", mykds);
		try {
			/**
			 * 由City与行政区划判断是否是本地调用,如果是本地调用，city不再传回JSP,
			 * 反之远程调用
			 */
			if(city != null){
				String xzqh = this.sysparaManager.getSyspara(
					"xzqh", "1", null).getCsz();
				if(!city.equals(xzqh))
					request.setAttribute("city", city.substring(0,4)+"00000000");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "veh/realpasslist";
	}
	
	/**
	 * 单卡口实时监测查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public String queryList(HttpServletRequest request){
		String kd = request.getParameter("kdbh");
		String cd = request.getParameter("cd");
		String fx = request.getParameter("fxbh");
		/** 市州行政区划代码  */
		String city = request.getParameter("city");
		request.setAttribute("kd", kd);
		request.setAttribute("cd", cd);
		request.setAttribute("fx", fx);
		try {
			/**
			 * 由City与行政区划判断是否是本地调用,如果是本地调用，city不再传回JSP,
			 * 反之远程调用
			 */
			if(city != null){
				String xzqh = this.sysparaManager.getSyspara(
					"xzqh", "1", null).getCsz();
				if(!city.equals(xzqh))
					request.setAttribute("city", city.substring(0,4)+"00000000");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "veh/realPassDetail";
	}
	
	@RequestMapping
	public String realView(HttpServletRequest request){
		try {
			String gcxh = request.getParameter("gcxh");
			String kdbh = request.getParameter("kd");
			String cd = request.getParameter("cd");
			String fx = request.getParameter("fxbh");
			//VehRealpass vr = this.realManager.getVehRealDetail(gcxh);
			//
			VehRealpass vr = this.realManager.getVehRealPassRedis(kdbh,fx,cd);
			if (vr != null) {
				String fxmc;
				try {
					CodeGateExtend direct = this.gateManager.getDirect(vr
							.getFxbh());
					if (direct == null)
						fxmc = vr.getFxbh();
					else
						//fxmc = URLEncoder.encode(direct.getFxmc(), "UTF-8");
						fxmc = direct.getFxmc();
				} catch (Exception e) {
					fxmc = vr.getFxbh();
				}
				String kdmc;
				try {
					CodeGate gate = this.gateManager.getGate(vr.getKdbh());
					if (gate == null)
						kdmc = vr.getKdbh();
					else
						//kdmc = URLEncoder.encode(gate.getKdmc(), "UTF-8");
						kdmc = gate.getKdmc();
				} catch (Exception e) {
					kdmc = vr.getKdbh();
				}
				vr.setKdbh(kdmc);
				vr.setFxbh(fxmc);
				vr.setCllx(systemManager.getCodeValue("030104",vr.getCllx()));
				vr.setCsys(systemManager.getCodeValue("030108",vr.getCsys()));
				vr.setCwhpys(systemManager.getCodeValue("031001",vr.getCwhpys()));
				vr.setHpys(systemManager.getCodeValue("031001", vr.getHpys()));
				vr.setHpzlmc(systemManager.getCodeValue("030107",vr.getHpzl()));
				vr.setXszt(systemManager.getCodeValue("110005",vr.getXszt()));
				
				// 处理长沙卡口图片
				JSONObject tpJsonObject = PicWSInvokeTool.getPicJsonObject(vr,kdbh);
				vr.setTp1(PicWSInvokeTool.getValue(tpJsonObject,"tp1",vr.getTp1()));
				vr.setTp2(PicWSInvokeTool.getValue(tpJsonObject,"tp2",vr.getTp2()));
				vr.setTp3(PicWSInvokeTool.getValue(tpJsonObject,"tp3",vr.getTp3()));
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				long between = (date.getTime() - sdf.parse(vr.getGcsj())
						.getTime()) / 60000L;
				//long between =this.realManager.getDiffTime(DateUtil.longToTime(Long.parseLong(vr.getGcsj())));
				if (between > 1800L) {
					request.setAttribute("flag", "1");
				}
				// 卡口日过车总量
				long gcTotal = realManager.getVehpassByKdbh(kdbh,fx,cd);
				request.setAttribute("gcTotal", gcTotal);
			}
			request.setAttribute("vehrealpass", vr);
			request.setAttribute("kd", kdbh);
			request.setAttribute("control", request.getParameter("c"));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return "veh/realpassview";
	}
}
