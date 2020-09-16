package com.sunshine.monitor.system.analysis.action;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.bean.CombineCondition;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.analysis.service.TimeSpaceCombineManager;
import com.sunshine.monitor.system.analysis.service.VehicleManager;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.service.SystemManager;

@Controller
@RequestMapping(value = "/tsb.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TimeSpaceCombineController {
		
	// 时空组合条件主页
	private String mainPage = "analysis/timespacecombinemain";
	
	@Autowired
	private GateManager gateManager;
	
	@Autowired
	private SystemManager systemManager;
	
	@Autowired
	private TimeSpaceCombineManager timeSpaceCombineManager;
	
	@Autowired
	@Qualifier("vehicleManager")
	private VehicleManager vehicleManager;
	
	/**
	 * 跳转到时空组合条件主页主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forward(HttpServletRequest request,HttpServletResponse response ){
		
		try {
			request.setAttribute("departmentList", this.gateManager
					.getDepartmentsByKdbh());
			request.setAttribute("hpysList", this.systemManager.getCodes("031001"));
			request.setAttribute("csysList", this.systemManager.getCode("030108"));
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(this.mainPage);
	}
	
	/**
	 * 跳转到假设轨迹分析主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardSuppose(HttpServletRequest request,HttpServletResponse response ){
		
		try {
			request.setAttribute("departmentList", this.gateManager
					.getDepartmentsByKdbh());
			request.setAttribute("hpysList", this.systemManager.getCodes("031001"));
			request.setAttribute("csysList", this.systemManager.getCode("030108"));
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
			request.setAttribute("cllxList", this.systemManager.getCode("030104"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("analysis/supposemain");
	}
	
	/**
	 * 跳转到频繁出入分析主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardOffenInOut(HttpServletRequest request,HttpServletResponse response ){
		
		try {
			//request.setAttribute("departmentList", this.gateManager.getDepartmentsByKdbh());
			request.setAttribute("hpysList", this.systemManager.getCodes("031001"));
			request.setAttribute("csysList", this.systemManager.getCode("030108"));
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
			request.setAttribute("cllxList", this.systemManager.getCode("030104"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("analysis/offenInOutmain");
	}
	
	/**
	 * 跳转到区域碰撞分析主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardDistrict(HttpServletRequest request,HttpServletResponse response ){
		
		try {
			//request.setAttribute("departmentList", this.gateManager.getDepartmentsByKdbh());
			request.setAttribute("hpysList", this.systemManager.getCodes("031001"));
			request.setAttribute("csysList", this.systemManager.getCode("030108"));
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
			request.setAttribute("cllxList", this.systemManager.getCode("030104"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("analysis/districtmain");
	}
	
	/**
	 * 跳转到特殊车辆对比主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardSpecialVehicle(HttpServletRequest request,HttpServletResponse response ){
		
		try {
			//request.setAttribute("departmentList", this.gateManager.getDepartmentsByKdbh());
			request.setAttribute("hpysList", this.systemManager.getCodes("031001"));
			request.setAttribute("csysList", this.systemManager.getCode("030108"));
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
			request.setAttribute("cllxList", this.systemManager.getCode("030104"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("analysis/specialVehicleMain");
	}
	
	/**
	 * 跳转到轨迹过车列表
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardGjPage(HttpServletRequest request) {
		String hphm = request.getParameter("g_hphm");
		String hpzl = request.getParameter("g_hpzl");
		String hpys = request.getParameter("g_hpys");
		String jsontext = request.getParameter("g_jsontext");
		String fxlx = request.getParameter("g_fxlx");
		request.setAttribute("hphm", hphm);
		request.setAttribute("hpzl", hpzl);
		request.setAttribute("hpys", hpys);
		request.setAttribute("jsontext", jsontext);
		request.setAttribute("fxlx", fxlx);
		String queryType =request.getParameter("g_queryType");
		if("list".equals(queryType)) {
			return new ModelAndView("analysis/gjPassrecList");
		}
		if("picture".equals(queryType)) {
			request.setAttribute("page", "1");
			request.setAttribute("rows", "8");
			return new ModelAndView("analysis/gjPassrecPic");
		}
		return null;
	}
	
	/**
	 * 假设轨迹分析
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping
	@ResponseBody
	public Map<String , Object> supposeAnalysis(HttpServletRequest request,String page, String rows, CombineCondition cc ) throws Exception {

		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		String sessionId = request.getSession().getId().replaceAll("-", "_");
		String tablename = Constant.SCS_TEMP_PREFIX+"_jscounttemp"+sessionId;
		//Map map = this.timeSpaceCombineManager.supposeAnalysisByOracle(filter, cc);
		//Map map = this.timeSpaceCombineManager.supposeAnalysisByScsDB(filter, cc);
		Map map = this.timeSpaceCombineManager.districtAnalysisByScsDB(filter, cc, tablename);
		return map;
	}
	
	/**
	 * 频繁出入分析
	 * @param request
	 * @param page
	 * @param rows
	 * @param cc
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping
	@ResponseBody
	public Object offenInOut(HttpServletRequest request,String page, String rows, CombineCondition cc ) throws Exception {
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数		
		Map map = this.timeSpaceCombineManager.offenInOutByScsDB(filter, cc,"");
		//预警规则
		String mgkk = request.getParameter("mgkk");
		String mgsd = request.getParameter("mgsd");
		String mgcs = request.getParameter("mgcs");
		if("".equals(mgkk) && "".equals(mgsd) && "".equals(mgcs)) {
			return map;
		} else {
			List<Map<String,Object>> list = (List) map.get("rows");
			if(list == null) {
				return map;
			}
			List resultList = new ArrayList();
			
				for(Map<String,Object> m: list) {
					if("".equals(mgkk) && "".equals(mgsd)) {
					if(Integer.parseInt(m.get("CS").toString())>=Integer.parseInt(mgcs)) {
						m.put("SFYJ", "1");
					}
					resultList.add(m);
					} else {
						Map ruleparam = new HashMap();
						ruleparam.put("hphm", m.get("HPHM"));
						ruleparam.put("hpzl", m.get("HPZL"));
						ruleparam.put("hpys", m.get("HPYS"));
						ruleparam.put("condition", cc.getCondition());
						if(!"".equals(mgkk)) {
							ruleparam.put("mgkk", mgkk);
						}
						if(!"".equals(mgsd)) {
							ruleparam.put("mgsd", mgsd);
						}
						int count = this.timeSpaceCombineManager.alarmRule(ruleparam);
						if(!"".equals(mgcs)) {
							if(count>=Integer.parseInt(mgcs)) {
								m.put("SFYJ", "1");
							} 
						} else if(count>0) {
							m.put("SFYJ", "1");
						}
						resultList.add(m);
					}
				}
				map.put("rows", resultList);
			
		}
		return map;
	}*/
	@RequestMapping
	@ResponseBody
	public Object offenInOut(HttpServletRequest request,String page, String rows, CombineCondition cc ) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);	//页数
		filter.put("rows", rows);	//每页显示行数		
		List<Map<String, Object>> list = this.timeSpaceCombineManager.offenInOutByScsDBExt(filter, cc);
		return list;
	}

	@RequestMapping
	@ResponseBody
	public Integer queryTotals(HttpServletRequest request,
			HttpServletResponse response, String page, String rows,
			CombineCondition cc) {
		int total = 0;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		/*String hphm = cc.getHphm().replace("?", "_").replace("？", "_");
		try {
			hphm = new String(hphm.getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		cc.setHphm(hphm);*/
				
		try {
			total = timeSpaceCombineManager.queryTotals(cc, filter);
		} catch (Exception e) {
			//debugLogger.warn("根据条件模糊查询", e);
			e.printStackTrace();
		}
		return total;
	}
	
	/**
	 * 区域碰撞分析
	 * @param request
	 * @param page
	 * @param rows
	 * @param cc
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public Object districtAnalysis(HttpServletRequest request,String page, String rows, CombineCondition cc ) throws Exception {
		Map filter = new HashMap();
		filter.put("page", page);	//页数
		filter.put("rows", rows);	//每页显示行数
		String sessionId = request.getSession().getId().replaceAll("-", "_");
		String tablename = Constant.SCS_TEMP_PREFIX+"_qycounttemp"+sessionId;
		//System.out.println("sessionId:"+sessionId);
		List list = this.timeSpaceCombineManager.districtAnalysis2ByScsDBExt(filter, cc, tablename);
		return list;
	}

	@RequestMapping
	@ResponseBody
	public Integer getTotal2(HttpServletRequest request,String page, String rows, CombineCondition cc) {
		int total = 0;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		String sessionId = request.getSession().getId().replaceAll("-", "_");
		String tablename = Constant.SCS_TEMP_PREFIX+"_qycounttemp"+sessionId;
		/*String hphm = veh.getHphm().replace("?", "_").replace("？", "_");
		try {
			hphm = new String(hphm.getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		veh.setHphm(hphm);*/
				
		try {
			total = timeSpaceCombineManager.getTotal2(filter, cc, tablename);
		} catch (Exception e) {
			//debugLogger.warn("根据条件模糊查询", e);
			e.printStackTrace();
		}
		return total;
	}
	
	@RequestMapping
	@ResponseBody
	public Object queryGjPassrecList(HttpServletRequest request, String page, String rows, CombineCondition cc) throws Exception {
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		String tablename = "";
		if("1".equals(cc.getFxlx())) {
			//return this.timeSpaceCombineManager.supposeAnalysisByScsDB(filter, cc);
			return this.timeSpaceCombineManager.districtAnalysisByScsDB(filter, cc, null);
		}
		if("2".equals(cc.getFxlx())) {
			return this.timeSpaceCombineManager.offenInOutByScsDB(filter, cc, null);
		}
		if("3".equals(cc.getFxlx())) {
			//return this.timeSpaceCombineManager.supposeAnalysisByScsDB(filter, cc);
			return this.timeSpaceCombineManager.districtAnalysis2ByScsDB(filter, cc, null);
		}
		return null;
	}
	
	@RequestMapping
	@ResponseBody
	public Object queryPgisGjList(HttpServletRequest request, CombineCondition cc) throws Exception {
		if("1".equals(cc.getFxlx()) || "3".equals(cc.getFxlx())) {
			//return this.timeSpaceCombineManager.supposeAnalysisByScsDB(filter, cc);
			cc.setTjlx("3");
			return this.timeSpaceCombineManager.districtAnalysisByScsDB(null, cc, null);
		}
		return null;
	}
	
	/**
	 * 最后一次过车轨迹与机动车库登记信息
	 * @param request
	 * @param page
	 * @param rows
	 * @param cc
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public Object queryLastVeh(HttpServletRequest request,String page,
			String rows, CombineCondition cc) {
		Map lastPassMap=null;
		Map filter = new HashMap();
		filter.put("curPage", page); // 页数
		filter.put("pageSize", rows); // 每页显示行数
		try {	
		if ("1".equals(cc.getFxlx()) || "3".equals(cc.getFxlx())) {
			lastPassMap = ((List<Map<String,Object>>)this.timeSpaceCombineManager.districtAnalysisByScsDB(filter,
					cc, null).get("rows")).get(0);
		}
		if ("2".equals(cc.getFxlx())) {
			lastPassMap = ((List<Map<String,Object>>)this.timeSpaceCombineManager.offenInOutByScsDB(filter, cc,
					null).get("rows")).get(0);
		}
		//UserSession session = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
		//SysUser user = session.getSysuser();
		//ProvincialRBSPHandle pHandle = new ProvincialRBSPHandle();
		//JSONObject jsonObj =null;
		//Map send=new HashMap();
		//send.put("hphm", cc.getHphm());
		
		 if(cc.getHphm().matches("^[\u4e00-\u9fa5|WJ]{1}[A-Z0-9]{6}$")){
				VehicleEntity cnd = new VehicleEntity();
			 	cnd.setHphm(cc.getHphm().substring(1,cc.getHphm().length()));
			 	cnd.setHpzl(cc.getHpzl());
				cnd.setFzjg(cc.getHphm().substring(0,2));
		        VehicleEntity entity = this.vehicleManager.getVehicleInfoGreenplum(cnd);
		        if(entity!=null){
		        	lastPassMap.put("clxh",(entity.getClxh()!=null)?entity.getClxh():"");
		        	lastPassMap.put("syr", (entity.getSyr()!=null)?entity.getSyr():"");
		        	lastPassMap.put("lxdh",(entity.getLxdh()!=null)?entity.getLxdh():"");
		        	lastPassMap.put("clpp",(entity.getClpp1()!=null)?entity.getClpp1():"");
		        	lastPassMap.put("csysmc",this.systemManager.getCodeValue("030108", entity.getCsys()));
		        	/*if(entity.getZp()!=null&&entity.getZp().length()>0){
		        		lastPassMap.put("zp",entity.getZp());
		        	}*/
		        	lastPassMap.put("sign","1");
		        }
		 }
        /**
		jsonObj= pHandle.findJson(user, send, cc.getHpzl());
		if(jsonObj.get("result").toString().equals("1")){
			//车辆型号
			lastPassMap.put("clxh",jsonObj.get("clxh").toString() );
			//所有人
			lastPassMap.put("syr", java.net.URLDecoder.decode(jsonObj.get("syr").toString(),"utf-8"));
			//联系电话
			lastPassMap.put("lxdh", jsonObj.get("lxdh").toString());
			//车辆品牌
			lastPassMap.put("clpp", java.net.URLDecoder.decode(jsonObj.get("clpp").toString(),"utf-8"));
			//车神颜色
			lastPassMap.put("csysmc", jsonObj.get("csysmc").toString());

			//标记
			lastPassMap.put("sign", "1");
		}
		**/
		} catch(Exception e) {
			e.printStackTrace();
		}
		return lastPassMap;
	}
	
	/**
	 * 
	 * 导出频繁出入Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getCaseInfoXls(HttpServletRequest request,HttpServletResponse response,String page, String rows, CombineCondition cc) {
		String[] headName = new String[]{"号牌号码", "号牌种类", "卡口数", "天数", "次数"};
		short[] columnWidth = new short[]{5000,5000,5000,9000,3000,3000,3000,6000}; 
		String[] method = new String[]{"hphm","hpzlmc","kks","ts","cs"};
	    String name = "频繁出入分析";
	    if (StringUtils.isNotBlank(cc.getPcgz()) && "5".equals(cc.getPcgz())) {
	    	name = "特殊车辆对比";
	    }
	    
		try {			
			List<Map<String, Object>> infos = (List<Map<String, Object>>)this.offenInOut(request, page, rows, cc);
			exportCommon.exportExecl(infos, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * 导出区域碰撞分析Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getdistrictXls(HttpServletRequest request,HttpServletResponse response,String page, String rows, CombineCondition cc) {
		String[] headName = new String[]{"号牌号码", "号牌种类", "区域1", "区域2"};
		short[] columnWidth = new short[]{5000,5000,5000,9000,3000,3000,3000,6000}; 
		String[] method = new String[]{"hphm","hpzlmc","area1","area2"};
		String name = "区域碰撞分析";
		try {			
			List<Map<String, Object>> infos =(List<Map<String, Object>>) this.districtAnalysis(request, page, rows, cc);
			exportCommon.exportExecl(infos, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 
	 * 导出时空轨迹分析Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getXyscrpXls(HttpServletRequest request,HttpServletResponse response,String page, String rows, CombineCondition cc ) {
		String[] headName = new String[]{"号牌号码", "号牌颜色", "车牌种类", "区划数","卡口数","天数","违法信息","次数"};
		short[] columnWidth = new short[]{6000,5000,5000,9000,3000,3000,3000,9000,3000,6000}; 
		String[] method = new String[]{"hphm","hphmys","hpzlmc","xzqs","kks","ts","wfxx","cs"};
		String name ="";
		try {			
			List<Map<String,Object>> infos=(List<Map<String,Object>>)this.supposeAnalysis(request, page, rows, cc).get("rows");
			exportCommon.exportExecl(infos, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
