package com.sunshine.monitor.system.susp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
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
import com.sunshine.monitor.comm.maintain.dao.MaintainDao;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.DepartmentManager;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.manager.service.SysparaManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;
import com.sunshine.monitor.system.susp.service.SuspinfoEditManager;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;

@Controller
@RequestMapping(value = "/suspinfoEditCtrl.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuspinfoEditCtroller {

	@Autowired
	@Qualifier("suspinfoEditManager")
	private SuspinfoEditManager suspinfoEditManager;

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;

	@Autowired
	@Qualifier("sysparaManager")
	private SysparaManager sysparaManager;
	
	@Autowired
	@Qualifier("logManager")
	private LogManager logManager;
	/**
	 * 跳转到布控变更(本市布控申请变更联动布控申请变更)主页
	 * 
	 * @param request
	 * @param begin
	 * @param end
	 * @param param
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request, String begin,
			String end, String param) {

		try {
			// String ldbkFlag = request.getParameter("ldbkFlag");
			if (param != null && param.equals("count")) {
				end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				begin = this.systemManager.getSysDate("-180", false)
						+ " 00:00:00";
			}
			request.setAttribute("ldbkFlag", "0");
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
			request.setAttribute("begin", begin);
			request.setAttribute("end", end);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "engineer/suspinfoeditmain";
	}
	/**
	 * 跳转到布控变更详细页面
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
		return "engineer/suspeditwin";
	}

	/**
	 * 已合并到布控申请中
	 * @param request
	 * @param begin
	 * @param end
	 * @param param
	 * @return
	 */
	@Deprecated
	@RequestMapping()
	public String findForwardLdbk(HttpServletRequest request, String begin,
			String end, String param) {

		try {
			// String ldbkFlag = request.getParameter("ldbkFlag");
			if (param != null && param.equals("count")) {
				end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				begin = this.systemManager.getSysDate("-180", false)
						+ " 00:00:00";
			}
			request.setAttribute("ldbkFlag", "1");
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
			request.setAttribute("begin", begin);
			request.setAttribute("end", end);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "engineer/suspinfoeditmain";
	}
	
	/**
	 * 本地布控变更查询
	 * 
	 * @param request
	 * @param page
	 * @param rows
	 * @param info
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map queryList(String page, String rows, VehSuspinfo info,
			HttpServletRequest request) {
		Map filter = new HashMap();
		filter.put("curPage", page); // 页数
		filter.put("pageSize", rows); // 每页显示行数
		
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		SysUser user = userSession.getSysuser();

		info.setBkr(user.getYhdh());

		Map map = null;
		try {
			map = (Map) suspinfoEditManager.findSuspinfoForMap(filter, info).get("data");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping()
	@ResponseBody
	public void updateSuspinfo(HttpServletRequest request,
			HttpServletResponse response, VehSuspinfo suspInfo)
			throws IOException {
		Map result = null;
		try {
			Log log = new Log();
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			
			if (StringUtils.isBlank(suspInfo.getBkxh())) {
				result = Common.messageBox("参数传输异常！", "0");
			} else {
				suspInfo.setYwzt("11");
				
				VehSuspinfo vehSuspinfo = suspinfoEditManager.getSuspinfoDetail(suspInfo.getBkxh());
				
				VehSuspinfopic vspic = this.getPictureBytes(request);
				result = suspinfoEditManager.updateSuspinfo(suspInfo,vspic);
				
				log.setGlbm(userSession.getSysuser().getGlbm());
				log.setIp(userSession.getSysuser().getIp());
				log.setYhdh(userSession.getSysuser().getYhdh());
			    log.setCzlx("2112");
			    StringBuffer sb=new StringBuffer();
			    sb.append("联动布控变更保存成功，操作条件为：");
			    
			    if (StringUtils.isNotBlank(suspInfo.getHphm()) && !vehSuspinfo.getHphm().equals(suspInfo.getHphm())) {
			    	sb.append("，修改前号牌号码："+vehSuspinfo.getHphm()+"，修改后号牌号码："+suspInfo.getHphm());
				}
				if (StringUtils.isNotBlank(suspInfo.getHpzl()) && !vehSuspinfo.getHpzl().equals(suspInfo.getHpzl())){
					sb.append("，修改前号牌种类："+
								systemManager.getCodeValue("030107", vehSuspinfo.getHpzl())+
								"，修改后号牌种类："+
								systemManager.getCodeValue("030107", suspInfo.getHpzl()));
				}
				if (StringUtils.isNotBlank(suspInfo.getBkdl()) && !vehSuspinfo.getBkdl().equals(suspInfo.getBkdl())) {
					sb.append("，修改前布控大类："+
								systemManager.getCodeValue("120019", vehSuspinfo.getBkdl())+
								"，修改后布控大类："+
								systemManager.getCodeValue("120019", suspInfo.getBkdl()));
				}
				if (StringUtils.isNotBlank(suspInfo.getBklb()) && !vehSuspinfo.getBklb().equals(suspInfo.getBklb())) {
					sb.append("，修改前布控类别："+
								systemManager.getCodeValue("120005", vehSuspinfo.getBklb())+
								"，修改后布控类别："+
								systemManager.getCodeValue("120005", suspInfo.getBklb()));
				}
				if (StringUtils.isNotBlank(suspInfo.getClxh()) && !vehSuspinfo.getClxh().equals(suspInfo.getClxh())) {
					sb.append("，修改前车辆型号："+vehSuspinfo.getClxh()+"，修改后车辆型号："+suspInfo.getClxh());
				}
				if (StringUtils.isNotBlank(suspInfo.getFdjh()) && !vehSuspinfo.getFdjh().equals(suspInfo.getFdjh())) {
					sb.append("，修改前发动机号："+vehSuspinfo.getFdjh()+"，修改后发动机号："+suspInfo.getFdjh());
				}
				if (StringUtils.isNotBlank(suspInfo.getClsyr()) && !vehSuspinfo.getClsyr().equals(suspInfo.getClsyr())) {
					sb.append("，修改前车辆所有人："+vehSuspinfo.getClsyr()+"，修改后车辆所有人："+suspInfo.getClsyr());
				}
				if (StringUtils.isNotBlank(suspInfo.getSyrlxdh()) && !vehSuspinfo.getSyrlxdh().equals(suspInfo.getSyrlxdh())) {
					sb.append("，修改前号车辆所有人电话："+vehSuspinfo.getSyrlxdh()+"，修改后车辆所有人电话："+suspInfo.getSyrlxdh());
				}
				if (StringUtils.isNotBlank(suspInfo.getSyrxxdz()) && !vehSuspinfo.getSyrxxdz().equals(suspInfo.getSyrxxdz())) {
					sb.append("，修改前所有人详细地址："+vehSuspinfo.getSyrxxdz()+"，修改后所有人详细地址："+suspInfo.getSyrxxdz());
				}
				if (StringUtils.isNotBlank(suspInfo.getBkqssj()) && !vehSuspinfo.getBkqssj().equals(suspInfo.getBkqssj())) {
					sb.append("，修改前布控启始时间："+vehSuspinfo.getBkqssj()+"，修改后布控启始时间："+suspInfo.getBkqssj());
				}
				if (StringUtils.isNotBlank(suspInfo.getBkjzsj()) && !vehSuspinfo.getBkjzsj().equals(suspInfo.getBkjzsj())) {
					sb.append("，修改前布控结束时间："+vehSuspinfo.getBkjzsj()+"，修改后布控结束时间："+suspInfo.getBkjzsj());
				}
				if (StringUtils.isNotBlank(suspInfo.getBkjglxdh()) && !vehSuspinfo.getBkjglxdh().equals(suspInfo.getBkjglxdh())) {
					sb.append("，修改前布控申请机关联系电话："+vehSuspinfo.getBkjglxdh()+"，修改后布控申请机关联系电话："+suspInfo.getBkjglxdh());
				}
				if (StringUtils.isNotBlank(suspInfo.getBkfw()) && !vehSuspinfo.getBkfw().equals(suspInfo.getBkfw())) {
					sb.append("，修改前布控范围："+
								systemManager.getCodeValue("000033", vehSuspinfo.getBkfw().substring(0, 6))+
								"，修改后布控范围："+
								systemManager.getCodeValue("000033", suspInfo.getBkfw().substring(0, 6)));
				}
				if (StringUtils.isNotBlank(suspInfo.getJyaq()) && !vehSuspinfo.getJyaq().equals(suspInfo.getJyaq())) {
					sb.append("，修改前简要案情："+vehSuspinfo.getJyaq()+"，修改后简要案情："+suspInfo.getJyaq());
				}
				if (StringUtils.isNotBlank(suspInfo.getClpp()) && !vehSuspinfo.getClpp().equals(suspInfo.getClpp())) {
					sb.append("，修改前车辆品牌："+vehSuspinfo.getClpp()+"，修改后车辆品牌："+suspInfo.getClpp());
				}
				if (StringUtils.isNotBlank(suspInfo.getCsys()) && !vehSuspinfo.getCsys().equals(suspInfo.getCsys())) {
					sb.append("，修改前车身颜色："+
								systemManager.getCodeValue("030108", vehSuspinfo.getCsys())+
								"，修改后车身颜色："+
								systemManager.getCodeValue("030108", suspInfo.getCsys()));
				}
				if (StringUtils.isNotBlank(suspInfo.getBjfs()) && !vehSuspinfo.getBjfs().equals(suspInfo.getBjfs())) {
					sb.append("，修改前报警方式："+
								systemManager.getCodeValue("130000", vehSuspinfo.getBjfs())+
								"，修改后报警方式："+
								systemManager.getCodeValue("130000", suspInfo.getBjfs()));
				}
				if (StringUtils.isNotBlank(suspInfo.getBkjb()) && !vehSuspinfo.getBkjb().equals(suspInfo.getBkjb())) {
					sb.append("，修改前布控级别："+
								systemManager.getCodeValue("120020", vehSuspinfo.getBkjb())+
								"，修改后布控级别："+
								systemManager.getCodeValue("120020", suspInfo.getBkjb()));
				}
				if (StringUtils.isNotBlank(suspInfo.getDxjshm()) && !vehSuspinfo.getDxjshm().equals(suspInfo.getDxjshm())) {
					sb.append("，修改前短信接收号码："+vehSuspinfo.getDxjshm()+"，修改后短信接收号码："+suspInfo.getDxjshm());
				}
				if (StringUtils.isNotBlank(suspInfo.getBkxz()) && !vehSuspinfo.getBkxz().equals(suspInfo.getBkxz())) {
					sb.append("，修改前布控性质："+
								systemManager.getCodeValue("120021", vehSuspinfo.getBkxz())+
								"，修改后布控性质："+
								systemManager.getCodeValue("120021", suspInfo.getBkxz()));
				}
				if (StringUtils.isNotBlank(suspInfo.getBjya()) && !vehSuspinfo.getBjya().equals(suspInfo.getBjya())) {
					sb.append("，修改前报警预案："+
								systemManager.getCodeValue("130022", vehSuspinfo.getBjya())+
								"，修改后报警预案："+
								systemManager.getCodeValue("130022", suspInfo.getBjya()));
				}
				if (StringUtils.isNotBlank(suspInfo.getHpys()) && !vehSuspinfo.getHpys().equals(suspInfo.getHpys())) {
					sb.append("，修改前号牌颜色："+
								systemManager.getCodeValue("031001", vehSuspinfo.getHpys())+
								"，修改后号牌颜色："+
								systemManager.getCodeValue("031001", suspInfo.getHpys()));
				}
				if (StringUtils.isNotBlank(suspInfo.getSqsb()) && !vehSuspinfo.getSqsb().equals(suspInfo.getSqsb())) {
					sb.append("，修改前涉枪涉爆："+
								systemManager.getCodeValue("190001", vehSuspinfo.getSqsb())+
								"，修改后涉枪涉爆："+
								systemManager.getCodeValue("190001", suspInfo.getSqsb()));
				}
				if (StringUtils.isNotBlank(suspInfo.getLadw()) && !vehSuspinfo.getLadw().equals(suspInfo.getLadw())) {
					sb.append("，修改前申请单位："+vehSuspinfo.getLadw()+"，修改后申请单位："+suspInfo.getLadw());
				}
				if (StringUtils.isNotBlank(suspInfo.getLadwlxdh()) && !vehSuspinfo.getLadwlxdh().equals(suspInfo.getLadwlxdh())) {
					sb.append("，修改前申请单位联系电话："+vehSuspinfo.getLadwlxdh()+"，修改后申请单位联系电话："+suspInfo.getLadwlxdh());
				}
				if (StringUtils.isNotBlank(suspInfo.getLar()) && !vehSuspinfo.getLar().equals(suspInfo.getLar())) {
					sb.append("，修改前申请人："+vehSuspinfo.getLar()+"，修改后申请人："+suspInfo.getLar());
				}
				if (StringUtils.isNotBlank(suspInfo.getCllx()) && !vehSuspinfo.getCllx().equals(suspInfo.getCllx())) {
					sb.append("，修改前车辆类型："+
								systemManager.getCodeValue("030104", vehSuspinfo.getCllx())+
								"，修改后车辆类型："+
								systemManager.getCodeValue("030104", suspInfo.getCllx()));
				}
				if (StringUtils.isNotBlank(suspInfo.getYwzt()) && !vehSuspinfo.getYwzt().equals(suspInfo.getYwzt())) {
					sb.append("，修改前业务状态："+
								systemManager.getCodeValue("120008", vehSuspinfo.getYwzt())+
								"，修改后业务状态："+
								systemManager.getCodeValue("120008", suspInfo.getYwzt()));
				}
				if (StringUtils.isNotBlank(suspInfo.getCltz()) && !vehSuspinfo.getCltz().equals(suspInfo.getCltz())) {
					sb.append("，修改前车辆特征："+vehSuspinfo.getCltz()+"，修改后车辆特征："+suspInfo.getCltz());
				}
				if (StringUtils.isNotBlank(suspInfo.getClsbdh()) && !vehSuspinfo.getClsbdh().equals(suspInfo.getClsbdh())) {
					sb.append("，修改前车辆识别代号："+vehSuspinfo.getClsbdh()+"，修改后车辆识别代号："+suspInfo.getClsbdh());
				}
			    
			    log.setCznr(sb.toString());
			    
				this.logManager.saveLog(log);
			}

		} catch (Exception e) {
			result = Common.messageBox("保存失败！", "0");
			e.printStackTrace();
		}

		PrintWriter writer = null;

		try {
			writer = response.getWriter();
			writer.print(JSONObject.fromObject(result));
		} catch (IOException e) {
			throw e;
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}

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
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile picobj = mrequest.getFile("picVal");
		MultipartFile clbksqbobj = mrequest.getFile("clbksqb");
		MultipartFile lajdsobj = mrequest.getFile("lajds");
		MultipartFile yjcnsobj = mrequest.getFile("yjcns");
		try {
			if (picobj != null && !picobj.isEmpty()){
				pic = picobj.getBytes();
			}
			if(clbksqbobj != null && !clbksqbobj.isEmpty()){
				clbksqb = clbksqbobj.getBytes();
			}
			if(lajdsobj != null && !lajdsobj.isEmpty()){
				lajds = lajdsobj.getBytes();
			}
			if(yjcnsobj != null && !yjcnsobj.isEmpty()){
				yjcns = yjcnsobj.getBytes();
			}
			if(picobj == null && clbksqbobj == null && lajdsobj == null && yjcnsobj == null){
				return null ;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileName = picobj.getOriginalFilename();
		String bksqbName = clbksqbobj.getOriginalFilename();
		String lajdsName = lajdsobj.getOriginalFilename();
		String yjcnsName = yjcnsobj.getOriginalFilename();
		VehSuspinfopic vspic = new VehSuspinfopic();
		vspic.setZjwj(pic);
		vspic.setZjlx(fileName);
		vspic.setClbksqb(clbksqb);
		vspic.setClbksqblj(bksqbName);
		vspic.setLajds(lajds);
		vspic.setLajdslj(lajdsName);
		vspic.setYjcns(yjcns);
		vspic.setYjcnslj(yjcnsName);
		return vspic;
	}
	
	
	
	@RequestMapping()
	@ResponseBody
	public Object delSuspinfo(HttpServletRequest request, VehSuspinfo info) {
		Map value = null;
		try {
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			SysUser user = userSession.getSysuser();

			String _bkxh = request.getParameter("bkxh");
			if (StringUtils.isBlank(_bkxh)) {
				return Common.messageBox("参数传输异常!", "0");
			}

			Department dp = userSession.getDepartment();
			if (dp == null)
				return Common.messageBox("非法操作!", "0");
			else if (StringUtils.isBlank(dp.getSsjz())) {
				return Common.messageBox("您操作的用户所在的部门缺少警种!", "0");
			}

			info.setBkxh(_bkxh);
			info.setCkyyms("用户删除");
			info.setCxsqr(user.getYhdh());
			info.setCxsqrjh(user.getJh());
			info.setCxsqrmc(user.getYhmc());
			info.setCxsqdw(user.getGlbm());
			info.setCxsqdwmc(dp.getBmmc());

			value = suspinfoEditManager.delSuspinfo(info, dp.getSsjz());
		} catch (Exception e) {
			e.printStackTrace();
			value = Common.messageBox("程序异常！", "0");
		}
		return value;
	}

	@RequestMapping()
	@ResponseBody
	public Object getSuspinfoDetail(HttpServletRequest request) {
		String bkxh = request.getParameter("bkxh");
		VehSuspinfo sb = null;

		try {
			sb = this.suspinfoEditManager.getSuspinfoDetailForBkxh(bkxh);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb;
	}

	// 对象中代码未转换成名称(号牌种类除外)，长字符未替换换行符
	@RequestMapping()
	@ResponseBody
	public Object getSuspinfo(HttpServletRequest request) {
		String bkxh = request.getParameter("bkxh");
		VehSuspinfo sb = null;

		try {
			sb = this.suspinfoEditManager.getSuspinfoDetail(bkxh);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb;
	}

	// 加载布控范围
	@RequestMapping()
	@ResponseBody
	public Object getBkfwScope() {
		try {
			List list = this.suspinfoEditManager.getBkfwListTree();
			StringBuffer sb = new StringBuffer(
					"<ul id=\"browser\" class=\"filetree\">");
			if (list != null) {
				int size = list.size();
				if (size > 0) {
					for (int index = 0; index < size; index++) {
						CodeUrl curCode = (CodeUrl) list.get(index);
						if ((!curCode.getJb().equals("2"))
								&& (!curCode.getJb().equals("1"))) {
							sb
									.append(
											"<li><input type=\"checkbox\" id=\"dm")
									.append(curCode.getDwdm())
									.append(
											"\" name=\"dm\"  onclick=\"selectAll('")
									.append(curCode.getJb()).append(
											"')\" value=\"").append(
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

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// 根据报警大类加载报警类别下拉框
	@RequestMapping()
	@ResponseBody
	public Object getBjlx(HttpServletRequest request,
			HttpServletResponse response) {

		String bjdl = request.getParameter("bjdl");
		StringBuffer sb = new StringBuffer();
		try {
			List list = this.systemManager.getCodesByDmsm("120005", bjdl, 4);
			if ((list == null) || (list.size() == 0)) {
				sb.append("[]");
			} else {
				sb.append("[");
				for (Iterator it = list.iterator(); it.hasNext();) {
					Code code = (Code) it.next();
					sb.append("{'dmz':'").append(code.getDmz()).append(
							"','dmsm1':'").append(
							URLEncoder.encode(code.getDmsm1(), "UTF-8"))
							.append("'},");
				}
				if (sb.length() == 1)
					sb.append(']');
				else
					sb.setCharAt(sb.length() - 1, ']');
			}
		} catch (Exception e) {
			e.printStackTrace();
			sb.delete(0, sb.length());
			sb.append("{}");
		}
		return sb.toString();
	}

	// 根据报警大类加载报警级别下拉框
	@RequestMapping()
	@ResponseBody
	public Object getBkjb(HttpServletRequest request,
			HttpServletResponse response) {
		String bkdl = request.getParameter("bkdl");
		StringBuffer sb = new StringBuffer();
		try {
			if ((bkdl == null) || (bkdl.equals(""))) {
				sb.append("[]");

			}

			List list = this.systemManager.getCodesByDmsm("120020", bkdl, 2);
			if ((list == null) || (list.size() == 0)) {
				sb.append("[]");
			} else {
				sb.append("[");
				for (Iterator it = list.iterator(); it.hasNext();) {
					Code code = (Code) it.next();
					sb.append("{'dmz':'").append(code.getDmz()).append(
							"','dmsm1':'").append(
							URLEncoder.encode(code.getDmsm1(), "UTF-8"))
							.append("'},");
				}
				if (sb.length() == 1)
					sb.append(']');
				else
					sb.setCharAt(sb.length() - 1, ']');
			}
		} catch (Exception e) {
			e.printStackTrace();
			sb.delete(0, sb.length());
			sb.append("{}");
		}
		return sb.toString();
	}

	// 根据报警大类加载报警预案
	@RequestMapping()
	@ResponseBody
	public Object getBjya(HttpServletRequest request,
			HttpServletResponse response) {
		String bkdl = request.getParameter("bkdl");
		StringBuffer sb = new StringBuffer();
		try {
			if ((bkdl == null) || (bkdl.equals(""))) {
				sb.append("[]");
			}
			List list = null;
			if ("1".equals(bkdl))
				list = this.systemManager.getCodesByDmsm("130022", "1", 2);
			else {
				list = this.systemManager.getCode("130022");
			}
			if ((list == null) || (list.size() == 0)) {
				sb.append("[]");
			} else {
				sb.append("[");
				for (Iterator it = list.iterator(); it.hasNext();) {
					Code code = (Code) it.next();
					sb.append("{'dmz':'").append(code.getDmz()).append(
							"','dmsm1':'").append(
							URLEncoder.encode(code.getDmsm1(), "UTF-8"))
							.append("'},");
				}
				if (sb.length() == 1)
					sb.append(']');
				else
					sb.setCharAt(sb.length() - 1, ']');
			}
		} catch (Exception e) {
			e.printStackTrace();
			sb.delete(0, sb.length());
			sb.append("{}");
		}
		return sb.toString();
	}

	// 根据报警大类加载布控性质
	@RequestMapping()
	@ResponseBody
	public Object getBkxz(HttpServletRequest request,
			HttpServletResponse response) {
		String bkdl = request.getParameter("bkdl");
		StringBuffer sb = new StringBuffer();
		try {
			if ((bkdl == null) || (bkdl.equals(""))) {
				sb.append("{}");
			}
			List list = null;

			if ("3".equals(bkdl))
				list = this.systemManager.getCodesByDmsm("120021", "1", 3);
			else {
				list = this.systemManager.getCodesByDmsm("120021", "1", 2);
			}

			if ((list == null) || (list.size() == 0)) {
				sb.append("{}");
			} else {
				sb.append("{");
				for (Iterator it = list.iterator(); it.hasNext();) {
					Code code = (Code) it.next();
					sb.append("'dmz':'").append(code.getDmz()).append(
							"','dmsm1':'").append(
							URLEncoder.encode(code.getDmsm1(), "UTF-8"))
							.append("'");
				}

				sb.append("}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			sb.delete(0, sb.length());
			sb.append("{}");
		}
		return sb.toString();
	}
	
	@RequestMapping
	@ResponseBody
	public AuditApprove getAuditApprove(String bkxh){
		AuditApprove au = null;
		try {
			au = this.suspinfoEditManager.getAuditApprove(bkxh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return au;
	}

}
