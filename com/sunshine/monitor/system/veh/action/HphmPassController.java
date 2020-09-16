package com.sunshine.monitor.system.veh.action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.manager.service.UrlManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.veh.service.HphmpassManager;


@Controller
@RequestMapping(value="/hmhmpassctr.do" ,params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HphmPassController {
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;

	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;

	@Autowired
	@Qualifier("hphmpassManager")
	private HphmpassManager hphmpassManager;
	
	private Logger log = LoggerFactory.getLogger(PassrecController.class);

	/**
	 * 进入过车信息查询主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardPassForHphmMain(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setAttribute("hpzllist", this.systemManager
					.getCodes("030107"));
			request.setAttribute("hpyslist", this.systemManager
					.getCodes("031001"));
			request.setAttribute("fzjg", this.systemManager.getParameter(
					"fzjg", request));

		} catch (Exception e) {
			e.printStackTrace();
		}

		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		if (userSession.getDepartment().getGlbm().startsWith("4300")) {
			return new ModelAndView("query/querypassbyhphmlist");
		} else
			return new ModelAndView("query/querypassmain");
	}
	
	/**
	 * 查询提示：提示用户查询数量过大是否继续查询
	 * @param request
	 * @param response
	 * @param condition
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryTips(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> conditions = Common.getParamentersMap(request);
		if("[]".equals(conditions.get("hphm"))){
			conditions.remove("hphm");
		}
		putValue(request, conditions);
		return this.hphmpassManager.queryTips(conditions);
	}
	

	private void putValue(HttpServletRequest request,Map<String, Object> conditions) throws Exception{
		String cityDwdm = request.getParameter("city");
		String _kdbh = (String)conditions.get("kdbh");
		if (StringUtils.isNotBlank(cityDwdm)) {
			/**DBLINK配置*/
			String cityname = this.systemManager.getCodeValue("105000",cityDwdm);
			if(StringUtils.isNotBlank(cityname)){
				/**DMSM3本地查询标志*/
				String flag = this.systemManager.getCodeValue("105000",cityDwdm, 3);
				/**如果FLAG不为空则按本地库查询*/
				if("1".equals(flag)){
					/**以及选择确定卡口，则不在模糊查询*/
					if(StringUtils.isBlank(_kdbh)){
						conditions.put("kdbhlike", cityDwdm);
					}
					cityname = "";
				}
			}
			conditions.put("cityname", cityname);
		}
	}
	
	

	
	
	/**
	 * 
	 * 查询多号牌过车详细信息
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@RequestMapping
	public ModelAndView lister(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		ModelAndView mv = null;
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		try {
			
			mv = new ModelAndView("query/hphmpassdetail");
			mv.addObject("glbmlist", this.gateManager.getDepartmentsByKdbh());
			mv.addObject("hpzllist", this.systemManager.getCodes("030107"));
			mv.addObject("hpyslist", this.systemManager.getCodes("031001"));
			mv.addObject("fzjg", this.systemManager.getParameter("fzjg",
					request));
			Map<String, Object> conditions = Common.getParamentersMap(request);
			Set<String> keyset = conditions.keySet();
			Iterator iter = keyset.iterator();
			String key = "";
			String hphmjsonstr = "";
			String hphmjsonarray = "";
			while(iter.hasNext()){
				 key =iter.next().toString();
				if(key.indexOf("hphm")!=-1){
					hphmjsonstr +="{"+key+":"+conditions.get(key)+"},";
				}
			}
			if(hphmjsonstr.trim().length()>0){
			hphmjsonarray = "["+hphmjsonstr.substring(0,hphmjsonstr.length()-1)+"]";
			conditions.put("hphm",hphmjsonarray);}
			mv.addObject("cds", conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 查询过车列表
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryPasslist(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			conditions.remove("kdmc");
			putValue(request, conditions);

			// ======================
			// 查询限制时间(数据量大)
			// ======================
			result = this.hphmpassManager.getPassrecList(conditions);
			List<VehPassrec> list = (List<VehPassrec>) result.get("rows");
			// 是否有数据判断
			if (list == null || list.size() == 0) {
				result.put("counts", "0");
			} else {
				result.put("counts", list.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
