package com.sunshine.monitor.system.query.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.BusinessType;
import com.sunshine.monitor.comm.util.DispatchedRangeType;
import com.sunshine.monitor.comm.util.FuzzySusp;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.manager.service.SysUserManager;
import com.sunshine.monitor.system.manager.service.SysparaManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.service.ReSuspinfoQueryManager;
import com.sunshine.monitor.system.query.service.SuspinfoQueryManager;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;
import com.sunshine.monitor.system.susp.service.SuspinfoAuditApproveManager;
import com.sunshine.monitor.system.susp.service.SuspinfoEditManager;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;

@Controller
@RequestMapping(value = "/reSuspinfoCtrl.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReSuspinfoCtroller {

	@Autowired
	private SystemManager systemManager;

	@Autowired
	private ReSuspinfoQueryManager reSuspinfoQueryManager;
	
	@Autowired
	@Qualifier("sysparaManager")
	private SysparaManager sysparaManager;
	
	
	
	@Autowired
	private SuspinfoQueryManager suspinfoQueryManager;
	
	@Autowired
	private SuspinfoAuditApproveManager suspinfoAuditApproveManager;

	@Autowired
	private SuspinfoManager suspinfoManager;

	@Autowired
	@Qualifier("suspinfoEditManager")
	private SuspinfoEditManager suspinfoEditManager;

	@Autowired
	@Qualifier("logManager")
	private LogManager logManager;
	
	@Autowired
	@Qualifier("sysUserManager")
	private SysUserManager sysUserManager;
	
	/**
	 * 跳转布控信息查询主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request) {

		try {
			String end = this.systemManager.getSysDate(null, false) + " 23:59:59";
			List bkdlList = this.systemManager.getCodesByDmsm("120019", "1", 4);

			List bklbList = this.systemManager.getCodes("120005");
			List list = new LinkedList();
			if (bklbList != null) {
				for (int i = 0; i < bklbList.size(); i++) {
					Code code = (Code) bklbList.get(i);
					if (!code.getDmsm4().equals("3")) {
						list.add(code);
					}
				}
			}
			List hpzlList = this.systemManager.getCodes("030107");

			List bkxzList = this.systemManager.getCodes("120021");

			List yadmList = this.systemManager.getCodes("130022");

			List bkjbList = this.systemManager.getCodes("120020");

			List xxlyList = this.systemManager.getCodes("120012");

			List ywztList = this.systemManager.getCodes("120008");

			List jlztList = this.systemManager.getCodes("120002");

			List bjztList = this.systemManager.getCodes("130009");

			request.setAttribute("tip", "query");
			request.setAttribute("end", end);
			request.setAttribute("bkdlList", bkdlList);
			request.setAttribute("yadmList", yadmList);
			request.setAttribute("bkxzList", bkxzList);
			request.setAttribute("bkjbList", bkjbList);
			request.setAttribute("hpzlList", hpzlList);
			request.setAttribute("bklbList", list);
			request.setAttribute("xxlyList", xxlyList);
			request.setAttribute("ywztList", ywztList);
			request.setAttribute("jlztList", jlztList);
			request.setAttribute("bjztList", bjztList);
			request.setAttribute("ckyyList", systemManager.getCodes("120007"));
			request.setAttribute("citylist", this.systemManager
					.getCodes("105000"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		String glbm = userSession.getDepartment().getGlbm();

		
		return "query/resuspinfostquery";
		
	}
	
	/**
	 * 布信息查询列表
	 * @param page
	 * @param rows
	 * @param request
	 * @param info
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map queryList(String page, String rows, VehSuspinfo info,
			HttpServletRequest request) {
		Map filter = new HashMap();
		String city = request.getParameter("city");
		filter.put("curPage", page); // 页数
		filter.put("pageSize", rows); // 每页显示行数
		filter.put("city", city);
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		//String tip = request.getParameter("tip");
		Map map = null;
		try {
			// 只查询涉案类和交通违法类 <> '3
			String conSql = "";
			map = reSuspinfoQueryManager.getSuspinfoMapForFilter(filter,info, conSql);
			List<VehSuspinfo> queryList = (List<VehSuspinfo>) map.get("rows");
			for (int i=0;i<queryList.size();i++) {
				Map  temp = (Map) queryList.get(i);
				if(temp.get("BY5")!=null){
					temp.put("CKRMC",this.sysUserManager.getSysUser(temp.get("BY5").toString()).getYhmc());
				}else{
					if(temp.get("CXSQRMC")!=null){
						temp.put("CKRMC",temp.get("CXSQRMC"));
					}else{
						temp.put("CKRMC","--");
					}
				}
				
			}
			map.put("rows", queryList);
			
			Log log = new Log();
			log.setGlbm(userSession.getSysuser().getGlbm());
			log.setIp(userSession.getSysuser().getIp());
			log.setYhdh(userSession.getSysuser().getYhdh());
			StringBuffer sb=new StringBuffer();
			
			log.setCzlx("2310");
			sb.append("续控信息查询成功，操作条件：");
			
			if (info.getKssj() != null && info.getKssj().length() > 0) {
				sb.append("，开始时间："
						+ info.getKssj().substring(0,10));
			}
			if (info.getJssj() != null && info.getJssj().length() > 0) {
				sb.append("，结束时间："
						+ info.getJssj().substring(0,10));
			}
			if (city != null && city.length() > 0){
				String kkszd = systemManager.getCodeValue("000033", city);
				if(null != kkszd && !"".equals(kkszd)){
					sb.append("，卡口所在地：" + kkszd);
				}else{
					sb.append("，卡口所在地：" + "高速卡口");
				}
			}
			if (info.getHphm() != null && info.getHphm().length() > 0) {
				sb.append("，号牌号码："
						+ info.getHphm());
			}
			if (info.getBkxh() != null && info.getBkxh().length() > 0) {
				sb.append("，布控序号："
						+ info.getBkxh());
			}
			if (info.getBkdl() != null && info.getBkdl().length() > 0) {
				sb.append("，布控类型："
						+ systemManager.getCodeValue("120019", info.getBkdl()));
			}
			if (info.getBklb() != null && info.getBklb().length() > 0) {
				sb.append("，布控子类："
						+ systemManager.getCodeValue("120005", info.getBklb()));
			}
			if (info.getHpzl() != null && info.getHpzl().length() > 0) {
				sb.append("，号牌种类："
						+ systemManager.getCodeValue("030107", info.getHpzl()));
			}
			if (info.getBkjb() != null && info.getBkjb().length() > 0) {
				sb.append("，布控级别："
						+ systemManager.getCodeValue("120020", info.getBkjb()));
			}
			if (info.getYwzt() != null && info.getYwzt().length() > 0) {
				sb.append("，业务状态："
						+ systemManager.getCodeValue("120008", info.getYwzt()));
			}
			if (info.getBjya() != null && info.getBjya().length() > 0) {
				sb.append("，报警预案："
						+ systemManager.getCodeValue("130022", info.getBjya()));
			}
			if (info.getXxly() != null && info.getXxly().length() > 0) {
				sb.append("，信息来源："
						+ systemManager.getCodeValue("120012", info.getXxly()));
			}
			if (info.getBjzt() != null && info.getBjzt().length() > 0) {
				sb.append("，报警状态："
						+ systemManager.getCodeValue("130009", info.getBjzt()));
			}
			log.setCznr(sb.toString());
			this.logManager.saveLog(log);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	
	/**
	 * 跳转到续控详细页面
	 * 
	 * @param request
	 * @param begin
	 * @param end
	 * @param param
	 * @return
	 */
	@RequestMapping()
	public String forwardWin(HttpServletRequest request) {
		String ldbkFlag = request.getParameter("ldbkFlag");
		String bkxh = request.getParameter("bkxh");
		try {
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
			request.setAttribute("bkxh", bkxh);

			request.setAttribute("ldbkFlag", ldbkFlag);
			request.setAttribute("existPic",this.suspinfoEditManager.existPic(bkxh));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "query/resuspinfoedit";
	}
	
	/**
	 * 新增续控跨市联动布控
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
		try {
			VehSuspinfo dveh = this.getVehSuspinfoFromParameters(veh, request, false, true);
			dveh.setUser(request.getParameter("dxjsr"));
			dveh.setLxdh(request.getParameter("lxdh"));
			// 业务状态
			veh.setYwzt(BusinessType.DISPATCHED_APPROVAL.getCode());
			VehSuspinfopic vspic = this.getPictureBytes(request);
			
			MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
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
			
			/*String wsdx = this.isBigtp(request);
			if("10".equals(wsdx)){
				//Map<String, Object> emap = new HashMap<String,Object>();
				emap.put("code", "0");
				emap.put("msg", "文书大小请勿超过10MB!");
				return emap;
			}*/
			
			result = (Map<String, Object>) this.reSuspinfoQueryManager.saveLinkVehSuspinfo(dveh, vspic);
		} catch (Exception e) {
			//Map<String, Object> emap = new HashMap<String,Object>();
			emap.put("code", "0");
			emap.put("msg", "新增布控失败!");
			e.printStackTrace();
			return emap;
		}
		return result;
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
	 * 保存布控参数值封装
	 * @param request
	 * @param flag 模糊布控标志
	 * @param 
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
		// 联动布控
		veh.setBkfwlx(DispatchedRangeType.LINKAGE_DISPATCHED.getCode());
		
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
	 * 上传文书
	 * 
	 * @param request
	 * @return
	 */
	private VehSuspinfopic getPictureBytes(HttpServletRequest request)
			throws Exception {
		//byte pic[] = (byte[]) null;
		byte clbksqb[] = (byte[]) null;
		byte lajds[] = (byte[]) null;
		byte yjcns[] = (byte[]) null;
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		//MultipartFile picobj = mrequest.getFile("picVal");
		MultipartFile clbksqbobj = mrequest.getFile("clbksqb");
		MultipartFile lajdsobj = mrequest.getFile("lajds");
		MultipartFile yjcnsobj = mrequest.getFile("yjcns");
		try {
			/*if (picobj != null && !picobj.isEmpty()){
				pic = picobj.getBytes();
			}*/
			if(clbksqbobj != null && !clbksqbobj.isEmpty()){
				clbksqb = clbksqbobj.getBytes();
			}
			if(lajdsobj != null && !lajdsobj.isEmpty()){
				lajds = lajdsobj.getBytes();
			}
			if(yjcnsobj != null && !yjcnsobj.isEmpty()){
				yjcns = yjcnsobj.getBytes();
			}
			if(/*picobj == null && */clbksqbobj == null && lajdsobj == null && yjcnsobj == null){
				return null ;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//String fileName = picobj.getOriginalFilename();
		String bksqbName = clbksqbobj.getOriginalFilename();
		String lajdsName = lajdsobj.getOriginalFilename();
		String yjcnsName = yjcnsobj.getOriginalFilename();
		VehSuspinfopic vspic = new VehSuspinfopic();
		//vspic.setZjwj(pic);
		//vspic.setZjlx(fileName);
		vspic.setClbksqb(clbksqb);
		vspic.setClbksqblj(bksqbName);
		vspic.setLajds(lajds);
		vspic.setLajdslj(lajdsName);
		vspic.setYjcns(yjcns);
		vspic.setYjcnslj(yjcnsName);
		return vspic;
	}
	
	private String isBigtp(HttpServletRequest request)
		throws Exception {
		byte pic[] = (byte[]) null;
		VehSuspinfopic vspic = new VehSuspinfopic();
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile picobj = mrequest.getFile("picVal");
		if(picobj != null && !picobj.isEmpty()){
			if(picobj.getSize()>10*1024*1024){
				return "10";
			}
		}else{
			return "1";
		}
		return "1";
	}
	
	@RequestMapping()
	@ResponseBody
	public Map getAllDbLink(HttpServletRequest request) {
		Map map = new HashMap();
		int n = 0;
		try {
			String cityname = this.systemManager.getCodeValue("105000", request.getParameter("city"));
			n = this.suspinfoQueryManager.getAllDbLink(cityname);
			map.put("key",n); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	
	/**
	 * 各地市布控信息查询
	 * @param page
	 * @param rows
	 * @param info
	 * @param request
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map queryCitySuspList(String page, String rows, VehSuspinfo info,
			HttpServletRequest request) {
		Map filter = new HashMap();
		filter.put("curPage", page); // 页数
		filter.put("pageSize", rows); // 每页显示行数
		Map map = null;
		try {
			String cityname = this.systemManager.getCodeValue("105000", request.getParameter("city"));			
			map = this.suspinfoQueryManager.getCitySuspinfoMapForFilter(filter, info, cityname);
			
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			Log log = new Log();
			log.setGlbm(userSession.getSysuser().getGlbm());
			log.setIp(userSession.getSysuser().getIp());
			log.setYhdh(userSession.getSysuser().getYhdh());
			StringBuffer sb=new StringBuffer();
			log.setCzlx("5520");
			sb.append("布控信息查询成功  ");
			if (info.getHphm() != null && info.getHphm().length() > 0) {
				sb.append("操作条件:,号牌号码:"
						+ info.getHphm());
			}
			log.setCznr(sb.toString());
			this.logManager.saveLog(log);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**根据号牌号码查询该号牌号码相关的所有布控信息和本单位布控的管控信息
	 * 查询布控信息(审核、审批)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map queryListByHphm(String page, String rows, VehSuspinfo info,
			HttpServletRequest request) {
		Map filter = new HashMap();
		filter.put("curPage", page); // 页数
		filter.put("pageSize", rows); // 每页显示行数
		
		Map map = null;

		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		
		String glbm = userSession.getDepartment().getGlbm();
		try {
			map=this.suspinfoQueryManager.getSuspinfoMapForFilerByHphm(filter, info.getHphm(), glbm);
			Log bklog = new Log();
			bklog.setGlbm(userSession.getSysuser().getGlbm());
			bklog.setIp(userSession.getSysuser().getIp());
			bklog.setYhdh(userSession.getSysuser().getYhdh());
			bklog.setCzlx("5520");
			bklog.setCznr("布控信息查询成功 操作条件:,号牌号码:"+info.getHphm());
				
			this.logManager.saveLog(bklog);
			
			Log gklog = new Log();
			gklog.setGlbm(userSession.getSysuser().getGlbm());
			gklog.setIp(userSession.getSysuser().getIp());
			gklog.setYhdh(userSession.getSysuser().getYhdh());
			gklog.setCzlx("5520");
			gklog.setCznr("布控信息查询成功 操作条件:,号牌号码:"+info.getHphm());
				
			this.logManager.saveLog(gklog);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		return map;
	}
	/**
	 * 
	 * 函数功能说明:获取审核审批表记录集合
	 * 修改日期 	2013-6-19
	 * @param aa
	 * @return
	 * @throws Exception    
	 * @return List
	 */
	@RequestMapping
	@ResponseBody
	public List getAuditApproveList(String bkxh) {
		List list = null;

		AuditApprove aa = new AuditApprove();
		aa.setBkxh(bkxh);
		try {
			list = suspinfoAuditApproveManager.getAuditApproves(aa);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 查询布控信息(审核、审批)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @author OUYANG
	 */
	@RequestMapping
	public ModelAndView queryUnlimitDetail(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String bkxh = request.getParameter("bkxh");
			AuditApprove auditApprove = new AuditApprove();
			auditApprove.setBkxh(bkxh);
			VehSuspinfo vs = this.suspinfoManager.getSuspDetail(bkxh);
			if ((vs != null) && (!vs.getBkdl().equals("3"))) {
				request.setAttribute("vs", vs);
				auditApprove.setBzw("1");
				request.setAttribute("auditList", suspinfoAuditApproveManager
						.getAuditApproves(auditApprove));
				auditApprove.setBzw("2");
				request.setAttribute("approveList", suspinfoAuditApproveManager
						.getAuditApproves(auditApprove));
				auditApprove.setBzw("3");
				request.setAttribute("cAuditList", suspinfoAuditApproveManager
						.getAuditApproves(auditApprove));
				auditApprove.setBzw("4");
				request.setAttribute("cApproveList",
						suspinfoAuditApproveManager
								.getAuditApproves(auditApprove));
			} else {
				request.setAttribute("vs", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("query/quesuspdetail");
	}
	/**
	 * 查询veh_suspinfo表信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping()
	@ResponseBody
	public Object getSuspinfoDetail(HttpServletRequest request) {
		String bkxh = request.getParameter("bkxh");
		String city = request.getParameter("city");
		VehSuspinfo sb = null;

		try {
		//if(city == null || city.length()<1 || "430000".equals(city)) {
			sb = this.suspinfoEditManager.getSuspinfoDetailForBkxh(bkxh);
	/*	} else {
			//String cityname = this.systemManager.getCodeValue("105000", city);
			sb = this.suspinfoEditManager.getCitySuspinfoDetailForBkxh(bkxh, city);
		}*/
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb;
	}
	/**
	 * 查询JM_SUSPINFO_MONITOR表信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public Object getSuspMonitorList(HttpServletRequest request) {
		String bkxh = request.getParameter("bkxh");
		List list = null;

		if (bkxh != null) {
			try {
				list = suspinfoQueryManager.getSuspMonitorListForBkxh(bkxh);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
