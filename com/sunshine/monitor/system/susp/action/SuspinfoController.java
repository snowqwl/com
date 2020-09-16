package com.sunshine.monitor.system.susp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.core.util.Assert;
import com.sunshine.monitor.comm.bean.Role;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.maintain.SmsFacade;
import com.sunshine.monitor.comm.service.RoleManager;
import com.sunshine.monitor.comm.util.BusinessType;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.DispatchedRangeType;
import com.sunshine.monitor.comm.util.FuzzySusp;
import com.sunshine.monitor.comm.util.InformationSource;
import com.sunshine.monitor.system.alarm.bean.VehAlarmCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmLivetrace;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.service.VehAlarmManager;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.SysUserManager;
import com.sunshine.monitor.system.manager.service.SysparaManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.service.ReSuspinfoQueryManager;
import com.sunshine.monitor.system.susp.bean.GkSuspinfoApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;
import com.sunshine.monitor.system.susp.service.SuspinfoCancelApproveManager;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;



@Controller
@RequestMapping(value = "/suspinfo.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuspinfoController {

	@Autowired
	private SuspinfoManager suspinManager;
	
	@Autowired
	@Qualifier("suspinfoCancelApproveManager")
	private SuspinfoCancelApproveManager suspinfoCancelApproveManager;
	
	@Autowired
	private SystemManager systemManager;
	
	@Autowired
	private VehAlarmManager alarmManager;
	
	@Autowired
	private RoleManager roleManager;
	
	@Autowired
	private SysUserManager sysUserManager;
	
	@Autowired
	@Qualifier("sysparaManager")
	private SysparaManager sysparaManager;
	
	// 本市布控主页
	private String mainPage = "susp/susplist";

	// 本市模糊布控主页
	private String mhmainPage = "susp/msusplist";

	// 联动布控主页
	private String ldmainPage = "susp/lsusplist";
	
	private String listPage = "susp/suspListMain";
	
	//跨省布控主页
	private String ksmainPage = "susp/ksusplist";
	
	@Autowired
	private ReSuspinfoQueryManager ksspqkService;

	public Map<String, Object> callKsss (HttpServletRequest request,
			HttpServletResponse response,String bkxh,String bkfw) throws Exception{ 
		Map<String, Object> result=new HashMap<String, Object>();
		Map<String, Object> map=new HashMap<String, Object>();
//		VehSuspinfo v=(VehSuspinfo) this.suspinfoApproveManager.getApproveDetailForBkxh111(bkxh);
//		String bkfw=v.getBkfw();
//		map=packMap(v, request);
		List<Map<String, Object>> list=getsfBybkfw(bkfw);
		List<Map<String, Object>> listrows=new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> temp=new HashMap<String, Object>();
			temp.put("bkxh", bkxh);
			temp.put("fqsj", "");
			temp.put("sf", list.get(i).get("sf"));
			temp.put("ds", list.get(i).get("ds"));
			temp.put("fksj", "");
			String res="等待审批后反馈";
			temp.put("fkjg", res);
			listrows.add(temp);
		}
		try {
			ksspqkService.insert(listrows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  result;
	}
	
	public List<Map<String, Object>> getsfBybkfw(String bkfw) throws Exception{
		List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
		String[] sarr=bkfw.split(",");
		List<String> temp=new ArrayList<String>();
		for (int i = 0; i < sarr.length; i++) {
			temp.add(sarr[i].substring(0, 2));
		}
		temp=removeDuplicate(temp);
		temp.remove("43");
		for (int i = 0; i < temp.size(); i++) {
			Map<String, Object> m=new HashMap<String, Object>();
			String sf=systemManager.getCodeValue("000033", temp.get(i)+"0000" );
//			if(temp.get(i).equals("43")){
//				sf="福南省";
//			}else if(temp.get(i).equals("44")){
//				sf="广东省";
//			}else if(temp.get(i).equals("45")){
//				sf="广西壮族自治区";
//			}else if(temp.get(i).equals("46")){
//				sf="海南省";
//			}else if(temp.get(i).equals("35")){
//				sf="福建省";
//			}else if(temp.get(i).equals("36")){
//				sf="江西省";
//			}
			m.put("sf", sf);
			m.put("sfdm", temp.get(i));
			String ds="";
			for (int j = 0; j < sarr.length; j++) {
				String s=sarr[j].substring(0, 6);
				if(s.startsWith(temp.get(i)) ){
					String cityValue=systemManager.getCodeValue("000033",s );
					if(j==sarr.length-1){
						ds+=cityValue+" ";
					}else{
						ds+=cityValue+",";
					}
				}
			}
			m.put("ds", ds);
			list.add(m);
		}
		return list;
	}
	
	public static List removeDuplicate(List list) {   
	    HashSet h = new HashSet(list);   
	    list.clear();   
	    list.addAll(h);   
	    return list;   
	} 
	
	/**
	 * 跳转到本市布控页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView localForward(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			Map<String, List<Code>> map = this.getCodeList();
			mv.setViewName(this.mainPage);
			mv.addObject("codemap", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 跳转布控申请主页
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public String forward(HttpServletRequest request,String module) {
			String end = "";
			String begin = "";
			try {
				end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				begin = this.systemManager.getSysDate("-180", false)
					+ " 00:00:00";
				request.setAttribute("begin",begin);
				request.setAttribute("end",end);
				if (module != null) {					
					request.setAttribute("ywzt","13");
					if("local".equals(module)){
						//本地布控
						request.setAttribute("bkfwlx",1);
					}else if("link".equals(module)){
						//联动布控
						request.setAttribute("bkfwlx",2);
					}
				}
				request.setAttribute("bkdlList", systemManager.getCode("120019"));
				request.setAttribute("bklbList", systemManager.getCode("120005"));
				request.setAttribute("hpzlList", systemManager.getCode("030107"));
				request.setAttribute("ywztList", systemManager.getCode("120008"));

				request.setAttribute("cllxList", systemManager.getCode("030104"));
				request.setAttribute("csysList", systemManager.getCode("030108"));
				request.setAttribute("hpysList", systemManager.getCode("031001"));
				request.setAttribute("bjfsList", systemManager.getCode("130000"));
				Syspara syspara = sysparaManager.getSyspara("xzqh", "1", "");
				request.setAttribute("xzqh", syspara.getCsz());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return listPage;
	}

	/**
	 * 跳转到本市模糊布控主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardtomh(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			
			
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			String glbm = userSession.getDepartment().getGlbm();

			if (StringUtils.isNotBlank(glbm) && glbm.indexOf("430000") == 0) {//保存身份  省厅还是地市
				mv.addObject("identity", "shengting");
			}
			else{
				mv.addObject("identity", "dishi");
			}
			
			
			Map<String, List<Code>> map = this.getCodeList();
			mv.setViewName(this.mhmainPage);
			mv.addObject("codemap", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 跳转到跨市联动布控主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView ldforward(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			Map<String, List<Code>> map = this.getCodeList();
			mv.setViewName(this.ldmainPage);
			mv.addObject("codemap", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	
	
	/**
	 * 跳转到跨省联动布控主页
	 * author huyaj  2017/6/21
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView ksforward(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			Map<String, List<Code>> map = this.getCodeList();
			mv.setViewName(this.ksmainPage);
			mv.addObject("codemap", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 查询本市布控信息
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object querySuspinfoList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> paraMap = this.getParamentersMap(request,
					false, DispatchedRangeType.LOCAL_DISPATCHED.getCode());
			result = this.suspinManager.queryLocalSuspinfoByPage(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询模糊布控信息
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object querymSuspinfoList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> paraMap = this.getParamentersMap(request, true,
					DispatchedRangeType.LOCAL_DISPATCHED.getCode());
			result = this.suspinManager.queryLocalMhSuspinfoByPage(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询当前登录用户的联动布控信息
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryldSuspinfoList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> paraMap = this.getParamentersMap(request, false,
					DispatchedRangeType.LINKAGE_DISPATCHED.getCode());
			result = this.suspinManager.queryLinkSuspinfoByPage(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 新增本市布控申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping
	public Object saveSuspinfo(HttpServletRequest request,HttpServletResponse response, VehSuspinfo veh) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			VehSuspinfo dveh = this.getVehSuspinfoFromParameters(veh, request,false, false);
			dveh.setUser(request.getParameter("dxjsr"));
			dveh.setLxdh(request.getParameter("lxdh"));
			if (dveh.getBkrjh() == null || dveh.getBkrjh().length() < 1) {
				result.put("code", "0");
				result.put("msg", "您操作的用户缺少警号,操作失败!");
				return result;
			}
			VehSuspinfopic vspic = this.getPictureBytes(request);
			result = (Map<String, Object>) this.suspinManager.saveLocalVehSuspinfo(dveh, vspic);
		} catch (Exception e) {
			Map<String, Object> emap = new HashMap<String, Object>();
			emap.put("code", "0");
			emap.put("msg", "新增布控失败!");
			e.printStackTrace();
			return emap;
		}
		return result;
	}

	/**
	 * 新增本市模糊布控申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping
	public Object savemSuspinfo(HttpServletRequest request,
			HttpServletResponse response,VehSuspinfo veh) throws Exception{
		Map<String, Object> result = null;
		
		try {
			VehSuspinfo dveh = this.getVehSuspinfoFromParameters(veh, request, true, false);
			dveh.setUser(request.getParameter("dxjsr"));
			dveh.setLxdh(request.getParameter("lxdh"));
			String hphm = dveh.getHphm().replace("？", "?");
			dveh.setHphm(hphm);
			VehSuspinfopic vspic = this.getPictureBytes(request);
			result = (Map<String, Object>) this.suspinManager.saveLocalMhVehSuspinfo(
					dveh, vspic);
		} catch (Exception e) {
			Map<String, Object> emap = new HashMap<String,Object>();
			emap.put("code", "0");
			emap.put("msg", "新增布控失败" );
			return emap;
		}
		return result;
	}

	/**
	 * 新增跨市联动布控
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object saveldSuspinfo(HttpServletRequest request,
			HttpServletResponse response,VehSuspinfo veh) {
		Map<String, Object> result = null;
		Map<String, Object> emap = new HashMap<String,Object>();
		String ywzt = request.getParameter("issave");
		VehSuspinfo dveh = new VehSuspinfo();
		try {
			String bkfw = veh.getBkfw();
			
			if(bkfw.contains("44") || bkfw.contains("45") || bkfw.contains("46") || bkfw.contains("35") || bkfw.contains("36")) {
				dveh = this.getVehSuspinfoFromParameters(veh, request, false, false);
			}else {
				dveh = this.getVehSuspinfoFromParameters(veh, request, false, true);
			}
			
			if(ywzt.equals("13")){
				veh.setYwzt(ywzt);//如果业务状态是13，表示保存为待修改状态
			}
			dveh.setUser(request.getParameter("dxjsr"));
			dveh.setLxdh(request.getParameter("lxdh"));
			VehSuspinfopic vspic = this.getPictureBytes(request);
			
			MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
			
			MultipartFile picObj = mrequest.getFile("picVal");
			double picSize = (double)200/(double)1024;
			if(!this.isOverRange(picObj, picSize)){
				emap.put("code", "0");
				emap.put("msg", "案情图片大小请勿超过200KB!");
				return emap;
			}
			
			MultipartFile clbksqbObj = mrequest.getFile("clbksqb");
			double clbksqbSize = 1;
			if(!this.isOverRange(clbksqbObj, clbksqbSize)){
				emap.put("code", "0");
				emap.put("msg", "车辆布控申请表大小请勿超过1M!");
				return emap;
			}
			
			MultipartFile lajdsObj = mrequest.getFile("lajds");
			double lajdsSize = 1;
			if(!this.isOverRange(lajdsObj, lajdsSize)){
				emap.put("code", "0");
				emap.put("msg", "立案决定书大小请勿超过1M!");
				return emap;
			}
			
			MultipartFile yjcnsObj = mrequest.getFile("yjcns");
			double yjcnsSize = 1;
			if(!this.isOverRange(yjcnsObj, yjcnsSize)){
				emap.put("code", "0");
				emap.put("msg", "移交承诺书大小请勿超过1M!");
				return emap;
			}
			result = (Map<String, Object>) this.suspinManager.saveLinkVehSuspinfo(
					dveh, vspic);
			if(result.containsKey("bkxh")){
				String bkxh=result.get("bkxh").toString();
				Map m=callKsss(mrequest, response, bkxh,dveh.getBkfw());
			}
		} catch (Exception e) {
			emap.put("code", "0");
			emap.put("msg", "新增布控失败!");
			e.printStackTrace();
			return emap;
		}
		return result;
	}
	
	
	/**
	 * 新增跨省联动布控
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object saveksldSuspinfo(HttpServletRequest request,
			HttpServletResponse response,VehSuspinfo veh) {
		Map<String, Object> result = null;
		Map<String, Object> emap = new HashMap<String,Object>();
		String ywzt = request.getParameter("issave");
		try {
			
			VehSuspinfo dveh = this.getVehSuspinfoFromParameters(veh, request, false, true);
			
			if(ywzt.equals("13")){
				veh.setYwzt(ywzt);//如果业务状态是13，表示保存为待修改状态
			}
			dveh.setUser(request.getParameter("dxjsr"));
			dveh.setLxdh(request.getParameter("lxdh"));
			dveh.setBkfwlx("3");
			VehSuspinfopic vspic = this.getPictureBytes(request);
			
			MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
			
			MultipartFile picObj = mrequest.getFile("picVal");
			double picSize = (double)200/(double)1024;
			if(!this.isOverRange(picObj, picSize)){
				emap.put("code", "0");
				emap.put("msg", "案情图片大小请勿超过200KB!");
				return emap;
			}
			
			MultipartFile clbksqbObj = mrequest.getFile("clbksqb");
			double clbksqbSize = 1;
			if(!this.isOverRange(clbksqbObj, clbksqbSize)){
				emap.put("code", "0");
				emap.put("msg", "车辆布控申请表大小请勿超过1M!");
				return emap;
			}
			
			MultipartFile lajdsObj = mrequest.getFile("lajds");
			double lajdsSize = 1;
			if(!this.isOverRange(lajdsObj, lajdsSize)){
				emap.put("code", "0");
				emap.put("msg", "立案决定书大小请勿超过1M!");
				return emap;
			}
			
			MultipartFile yjcnsObj = mrequest.getFile("yjcns");
			double yjcnsSize = 1;
			if(!this.isOverRange(yjcnsObj, yjcnsSize)){
				emap.put("code", "0");
				emap.put("msg", "移交承诺书大小请勿超过1M!");
				return emap;
			}
			
			result = (Map<String, Object>) this.suspinManager.saveLinkVehSuspinfo(
					dveh, vspic);
		} catch (Exception e) {
			emap.put("code", "0");
			emap.put("msg", "新增布控失败!");
			e.printStackTrace();
			return emap;
		}
		return result;
	}
	
	/**
	 * 保存布控参数值封装
	 * @param request
	 * @param flag 模糊布控标志
	 * @param isldbk 联动布控标志
	 * @return
	 * @throws Exception
	 */
	private VehSuspinfo getVehSuspinfoFromParameters(VehSuspinfo veh,
			HttpServletRequest request, boolean flag, boolean isldbk) throws Exception {
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		String jh = userSession.getSysuser().getJh();
		String yhdh = userSession.getSysuser().getYhdh();
		String yhmc = userSession.getSysuser().getYhmc();
		String bmmc = userSession.getDepartment().getBmmc();
		String glbm = userSession.getDepartment().getGlbm();
		veh.setBkrjh(jh);
		veh.setBkjg(glbm);
		veh.setBkjgmc(bmmc);
		veh.setBkrmc(yhmc);
		veh.setBkr(yhdh);
		
		// 是否是联动布控，如果不是联动布控，则布控范围为行政区划
//		if(!isldbk){
//			String xzqh = getXzqh(userSession);
//			veh.setBkfw(xzqh);
//			veh.setBkfwlx(DispatchedRangeType.LOCAL_DISPATCHED.getCode());
//		} else {
//			veh.setBkfwlx(DispatchedRangeType.LINKAGE_DISPATCHED.getCode());
//		}
		
		//2019/02/19   wangwd
		//  是否为本省布控,如果不是本省布控,则布控范围为跨省布控
		if(isldbk){
			veh.setBkfwlx(DispatchedRangeType.LINKAGE_DISPATCHED.getCode());
		} else {
			veh.setBkfwlx(DispatchedRangeType.KLINKAGE_DISPATCHED.getCode());
		}
		
		// 是否是模糊布控
		if (flag) {
			veh.setMhbkbj(FuzzySusp.FUZZY_TRUE.getCode());
		} else {
			veh.setMhbkbj(FuzzySusp.FUZZY_FALSE.getCode());
		}
		// 业务状态
		veh.setYwzt(BusinessType.DISPATCHED_CHECK.getCode());
		return veh;
	}

	/**
	 * 上传图片
	 * 
	 * @param request
	 * @return
	 */
	private VehSuspinfopic getPictureBytes(HttpServletRequest request)
			throws Exception {
		byte pic[] = (byte[]) null;
		byte clbksqb[] = (byte[]) null;
		byte lajds[] = (byte[]) null;
		byte yjcns[] = (byte[]) null;
		VehSuspinfopic vspic = new VehSuspinfopic();
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile picobj = mrequest.getFile("picVal");
		MultipartFile clbksqbobj = mrequest.getFile("clbksqb");
		MultipartFile lajdsobj = mrequest.getFile("lajds");
		MultipartFile yjcnsobj = mrequest.getFile("yjcns");
		try {
			if (picobj != null && !picobj.isEmpty()){
				pic = picobj.getBytes();
				String fileName = picobj.getOriginalFilename();
				vspic.setZjwj(pic);
				vspic.setZjlx(fileName);
			}
			if(clbksqbobj != null && !clbksqbobj.isEmpty()){
				clbksqb = clbksqbobj.getBytes();
				String bksqbName = clbksqbobj.getOriginalFilename();
				vspic.setClbksqb(clbksqb);
				vspic.setClbksqblj(bksqbName);
			}
			if(lajdsobj != null && !lajdsobj.isEmpty()){
				lajds = lajdsobj.getBytes();
				String lajdsName = lajdsobj.getOriginalFilename();
				vspic.setLajds(lajds);
				vspic.setLajdslj(lajdsName);
			}
			if(yjcnsobj != null && !yjcnsobj.isEmpty()){
				yjcns = yjcnsobj.getBytes();
				String yjcnsName = yjcnsobj.getOriginalFilename();
				vspic.setYjcns(yjcns);
				vspic.setYjcnslj(yjcnsName);
			}
			if(picobj == null && clbksqbobj == null && lajdsobj == null && yjcnsobj == null){
				return null ;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vspic;
	}

	/**
	 * 验证图片大小
	 */
	private boolean isOverRange(MultipartFile picobj, double size) throws Exception{
		boolean isOverRange = true;
		if(picobj != null && !picobj.isEmpty()){
			if(picobj.getSize()>size*1024*1024){
				isOverRange = false;
			}
		}
		return isOverRange;
	}
	/**
	 * 代码列表
	 * 
	 * @return
	 */
	private Map<String, List<Code>> getCodeList() throws Exception {
		Map<String, List<Code>> map = null;
		try {
			// 布控列表
			List<Code> bkdlList = this.systemManager.getCodes("120019");
			List<Code> bklbList = this.systemManager.getCodes("120005");
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			List<Code> ywztList = this.systemManager.getCodes("120008");

			// 新增布控
			List<Code> cllxList = this.systemManager.getCodes("030104");
			List<Code> csysList = this.systemManager.getCodes("030108");
			List<Code> hpysList = this.systemManager.getCodes("031001");
			List<Code> bjfsList = this.systemManager.getCodes("130000");

			map = new HashMap<String, List<Code>>();
			map.put("bkdlList", bkdlList);
			map.put("bklbList", bklbList);
			map.put("hpzlList", hpzlList);
			map.put("ywztList", ywztList);
			map.put("cllxList", cllxList);
			map.put("csysList", csysList);
			map.put("hpysList", hpysList);
			map.put("bjfsList", bjfsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/***
	 * 获取行政区划
	 * 
	 * @param userSession
	 * @return
	 */
	private String getXzqh(UserSession userSession) throws Exception {
		List<Syspara> list = userSession.getSyspara();
		String xzqh = "xzqh";
		for (Iterator<Syspara> it = list.iterator(); it.hasNext();) {
			Syspara sp = (Syspara) it.next();
			if (xzqh.equals(sp.getGjz())) {
				xzqh = sp.getCsz();
				break;
			}
		}
		return xzqh;
	}

	/**
	 * 查询参数值封装
	 * @param request
	 * @param flag 模糊布控标志
	 * @param bkfwlx 布控范围类型
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getParamentersMap(HttpServletRequest request,
			boolean flag, String bkfwlx) throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		String yhdh = userSession.getSysuser().getYhdh();
		Enumeration<String> enumes = request.getParameterNames();
		// 布控人
		paraMap.put("BKR", yhdh);

		// 布控范围
		if (bkfwlx.equals(DispatchedRangeType.LOCAL_DISPATCHED.getCode())) {
			paraMap.put("BKFWLX", DispatchedRangeType.LOCAL_DISPATCHED
					.getCode());
		} else {
			paraMap.put("BKFWLX", DispatchedRangeType.LINKAGE_DISPATCHED
					.getCode());
		}
		// 信息来源
		paraMap.put("XXLY", InformationSource.LOCAL_WRITER.getCode());
		// 模糊布控标志
		if (flag) {
			paraMap.put("MHBKBJ", FuzzySusp.FUZZY_TRUE.getCode());
		} else {
			paraMap.put("MHBKBJ", FuzzySusp.FUZZY_FALSE.getCode());
		}
		while (enumes.hasMoreElements()) {
			String param = enumes.nextElement();
			if (param != null && param.length() > 0
					&& !"method".equalsIgnoreCase(param)) {
				Object value = request.getParameter(param);
				if (value != null && !"".equals(value)) {
					paraMap.put(param, value);
				}
			}
		}
		return paraMap;
	}

	/**
	 * 布范围列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getBkfwScope(HttpServletRequest request,
			HttpServletResponse response) {
		StringBuffer sb = new StringBuffer(50);
		try {
			List<CodeUrl> list = this.suspinManager.getBkfwListTree();
			if (list != null) {
				int size = list.size();
				if (size > 0) {
					sb.append("<ul id=\"browser\" class=\"filetree\">");
					for (int index = 0; index < size; index++) {
						CodeUrl curCode = (CodeUrl) list.get(index);
						if ((!curCode.getJb().equals("2"))
								&& (!curCode.getJb().equals("1"))) {
							sb
									.append(
											"<li><input type=\"checkbox\" id=\"dm")
									.append(curCode.getDwdm())
									.append(
											"\" name=\"dm\"")
									.append(
											" value=\"").append(
											curCode.getDwdm()).append(
											"\"/><span id=\"mc").append(
											curCode.getDwdm()).append("\">")
									.append(curCode.getDwdm()).append(":")
									.append(curCode.getJdmc())
									.append("</span>");
							if (curCode.getJb().equals("1"))
								sb.append("\n<ul>");
							else {
								sb.append("</li>\n");
							}
						}
					}
				}
			}
			sb.append("</ul></li>\n");
			sb.append("</ul>\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	//--------------------------------------撤控确认-------------------------------------------
	
	/**
	 * 跳转拦截确认主页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardSuspinfoCancelsure(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		Map<String, List<Code>> map = null;
		try {
			map = new HashMap<String, List<Code>>();
			List<Code> bkdlList = this.systemManager.getCodes("120019");
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			List<Code> bklbList = this.systemManager.getCodes("120005");
			List<Code> ywztList = this.systemManager.getCodes("120008");
			map.put("bkdlList", bkdlList);
			map.put("hpzlList", hpzlList);
			map.put("bklbList", bklbList);
			map.put("ywztList", ywztList);
			mv.addObject("codemap", map);
			mv.setViewName("susp/suspcancellist");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 查询撤控确认列表
	 * 2013/8/19更新:
	 * 1) 访操作交于指挥中心操作
	 *    根据报警单位查询
	 *    (仅供市局指挥中心和布控审批单位查询)
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object querySuspinfoCancelList(HttpServletRequest request) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			Department dp = userSession.getDepartment();
			// 地市版已修改  统一按布控机关  进行撤控拦截确认   不判断级别
			String tip = "0";
			/*if("3".equals(dp.getJb()) && "1".equals(dp.getBmlx()))
				tip = "1";//指挥中心登录*/
			
			//conditions.put("BKR", userSession.getSysuser().getYhdh());
			conditions.put("BMDWDM", userSession.getSysuser().getGlbm());
			result = this.suspinManager.querySuspinfoCancelList(conditions,tip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	


	/**
	 * 查询撤控确认详细信息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object querySuspinfoCancelDetail(HttpServletRequest request) {
		Map<String, Object> result = null;
		try { 
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			String bjxh = request.getParameter("bjxh");
			String fkbh = request.getParameter("fkbh");
			VehAlarmrec alarm = this.alarmManager.getAlarmsignDetail(bjxh);
			VehSuspinfo susp = null;
			SysUser sysuser = null;
			List<VehAlarmCmd> historyCmdList = null;
			if (alarm != null) {
				susp = this.suspinManager.getSuspDetail(alarm.getBkxh());
				historyCmdList = this.alarmManager.getCmdlist(alarm.getBjxh(), true);
			}
			if(susp!=null){
			 sysuser = this.sysUserManager.getSysUser(susp.getBkr());}
			List<VehAlarmLivetrace> livetraceList = this.alarmManager.getLivetraceList(bjxh);
			List<VehAlarmHandled> handled = this.alarmManager.queryAlarmHandleList(bjxh);
			VehAlarmHandled feedback = this.alarmManager.queryAlarmHandled(fkbh);
			result = new HashMap<String, Object>();
			result.put("susp", susp);
			result.put("sysuser", sysuser);
			result.put("alarm", alarm);
			result.put("historyCmdList",historyCmdList);
			result.put("livetraceList", livetraceList);
			result.put("handled", handled);
			result.put("feedback", feedback);
			result.put("user", userSession.getSysuser());
			result.put("dep", userSession.getDepartment());
			result.put("bkxh", feedback.getBkxh());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}		
	
	/**
	 * 查询请求Map参数
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	private Map<String, Object> getParamentersMap2(HttpServletRequest request)
			throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		Enumeration<String> enumes = request.getParameterNames();
		while (enumes.hasMoreElements()) {
			String param = enumes.nextElement();
			if (param != null && param.length() > 0
					&& !"method".equalsIgnoreCase(param)) {
				Object value = request.getParameter(param);
				if (value != null && !"".equals(value)) {
					paraMap.put(param, value);
				}
			}
		}
		return paraMap;
	}
	
	/**
	 * 保存撤控确认
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object saveSuspinfoCancel(HttpServletRequest request,VehSuspinfo vehSuspinfo) {
		Map<String, String> result = null;
		String fsdxhm="";
		
		try {
			result = new HashMap<String,String>();
			List<VehSuspinfo>  fsdxhmlist = null;
			String hphm = request.getParameter("fkhphm");
			
			//SmsFacade smsFacade = new SmsFacade();
			VehSuspinfo info = this.suspinManager.getSuspDetail(vehSuspinfo.getBkxh());
			String msg = "布控类别为【"+info.getBkdlmc()+"】,车辆类型为【"+info.getHpzlmc()+"】,号牌号码【"+info.getHphm()+"】的车辆已拦截成功，系统已自动撤控！";
			int success = this.suspinManager.saveSuspinfoCancelsign(vehSuspinfo);
			if(success == 1){
				result.put("code", "1");
				result.put("msg", "撤控确认成功");
				if(hphm!=""){
					fsdxhmlist = this.suspinManager.getDxhmByHpdm(hphm);			
				}
				for(int i=0;i<fsdxhmlist.size();i++){
					VehSuspinfo fsdxsj = (VehSuspinfo)fsdxhmlist.get(i);
					if(fsdxsj.getBkjglxdh().length()>0){
					fsdxhm+=fsdxsj.getBkjglxdh()+",";					
						}
				}
				
				if("1".equals(vehSuspinfo.getBy3())){
					// 自动撤控
					int successAutomaticCancel = this.suspinManager.saveAutomaticCancel(vehSuspinfo);
					if(successAutomaticCancel == 1) {
						//发送短信
						this.suspinManager.saveLjNotCancalSj( vehSuspinfo,fsdxhmlist);
					}
				}
				
				if(StringUtils.isNotBlank(fsdxhm))
					SmsFacade.sendAndPostSms(fsdxhm, msg, null);
				}
			
		} catch (Exception e) {
			result.put("code", "0");
			result.put("msg", "撤控确认失败,异常:" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping
	public Object queryValidGKSuspinfo(HttpServletRequest request,VehSuspinfo vehSuspinfo) {
		Map<String, Object> result = null;
		try {
			result = this.suspinManager.queryValidGKSuspinfo(vehSuspinfo, true);
			if(result != null){
				String code = (String)result.get("code");
				if("1".equals(code)){
					UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
					String roles = userSession.getSysuser().getRoles();
					List<Role> list = this.roleManager.queryRoles(roles);
					// 默认值
					result.put("force", "false");
					// 级别为二,指挥中心
					for(Role role : list){
						String jsjb = role.getJsjb();
						String jsmc = role.getJsmc();
						if("".equals(jsmc) || jsmc == null){
							continue ;
						}
						if("2".equals(jsjb) && jsmc.indexOf("指挥中心") != -1){
							result.put("force", "true");
							break;
						}
					}
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	

	/**
	 * 跳转进入管控核查主页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardHCMain(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("engineer/approvegkmain");
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Code> bkdlList = this.systemManager.getCodes("120019");
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			List<Code> bklbList = this.systemManager.getCodes("120005");
			map.put("bkdlList", bkdlList);
			map.put("hpzlList", hpzlList);
			map.put("bklbList", bklbList);
			
			mv.addObject("codeMap", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 核查管控信息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryHCSuspinfo(HttpServletRequest request){
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			Department department = userSession.getDepartment();
			Assert.isNull(department, "未获取到相关管理部信息，请重新登录！");
			conditions.put("bkdw", department.getGlbm());
			return this.suspinManager.queryHCSuspinfo(conditions);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 管控核查详细信息(布控、报警、签收)
	 * @param request
	 * @param bjxh
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryHCAlarmSuspinfoDetail(HttpServletRequest request, 
			@RequestParam(required = true) String bjxh){
		VehAlarmrec alarm = null;
		VehSuspinfo susp  = null ;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			alarm = this.alarmManager.getAlarmsignDetail(bjxh);
			Assert.isNull(alarm, "未找到相应的预警信息！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try{
			susp = this.suspinManager.getSuspDetail(alarm.getBkxh());
			Assert.isNull(susp, "未找到相应的布控信息!");
		} catch(Exception e){
			e.printStackTrace();
		}
		List<Code> qrztList = null;
		List<Code> ljtjList = null;
		try {
			qrztList = this.systemManager.getCodesByDmsm("120014", "1", 2);
			ljtjList = this.systemManager.getCodes("130004");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		result.put("susp", susp);
		result.put("qrztList", qrztList);
		result.put("ljtjList", ljtjList);
		result.put("alarm", alarm);
		return result;
	}
	
	/**
	 * 保存管控信息核查结果
	 * @param request
	 * @param approveSuspinfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object saveHCGkSusp(HttpServletRequest request,GkSuspinfoApprove approveSuspinfo) {
		UserSession userSession = (UserSession) WebUtils
				.getSessionAttribute(request, "userSession");
		Map<String, Object> emap = new HashMap<String,Object>();
		try {
			Assert.isNullOrEmpty(approveSuspinfo, "管控核查信息提交后台出错！");
			String bjxh = approveSuspinfo.getBjxh();
			Assert.isNullOrEmpty(bjxh, "管控核查预警序号为空!");
			String bkxh = approveSuspinfo.getBkxh();
			Assert.isNullOrEmpty(bkxh, "管控核查布控序号为空!");
			String hcnr = approveSuspinfo.getHcnr();
			Assert.isNullOrEmpty(hcnr, "管控核查内容为空！");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			emap.put("msg", e.getMessage());
			return emap;
		}
		SysUser sysUser = userSession.getSysuser();
		approveSuspinfo.setHcr(sysUser.getYhdh());
		approveSuspinfo.setHcdw(userSession.getDepartment().getGlbm());
		try {
			this.suspinManager.insertHCsuspinfo(approveSuspinfo);
			emap.put("msg", "管控核查提交成功!");
		} catch (Exception e) {
			e.printStackTrace();
			emap.put("msg", "管控核查提交失败!");
			return emap;
		}
		return emap;
	}
	
	/**
	 * 核查管控详细信息
	 * @param request
	 * @param bjxh
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryHCGkResult(HttpServletRequest request, @RequestParam(required = true) String bjxh){
		Map<String, Object> emap = null;
		GkSuspinfoApprove gkSuspinfoApprove=null;
		try {
			emap = new HashMap<String,Object>();
			Assert.isNullOrEmpty(bjxh, "预警序号为空！");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			emap.put("msg", e.getMessage());
			return emap;
		}
		try {
			gkSuspinfoApprove = this.suspinManager.queryGkSuspinfoApprove(bjxh);
		} catch (Exception e) {
			e.printStackTrace();
			emap.put("msg", "找不到管控核查信息！");
			return emap;
		}
		return gkSuspinfoApprove;
	}
}