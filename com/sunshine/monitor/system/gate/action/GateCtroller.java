package com.sunshine.monitor.system.gate.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.core.util.ClassUtil;
import com.sunshine.monitor.comm.bean.Menu;
import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateCd;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.AdministrativeDivision;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.SystemManager;

@Controller
@RequestMapping(value = "/gate.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GateCtroller {

	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;

	@RequestMapping()
	public String index() {
		return "gate/gatemain";
	}
	
	@RequestMapping
	public ModelAndView administrativeMain(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("manager/administrativeMain");
	}
	
	@RequestMapping()
	public String forwardWin(HttpServletRequest request) {
		String id = request.getParameter("id");
		String xzqh = request.getParameter("xzqh");
		String type = request.getParameter("type");
		request.setAttribute("id", id);
		request.setAttribute("xzqh", xzqh);
		if(type.equals("0"))
			request.setAttribute("type", "0");
		else
			request.setAttribute("type", "1");
		return "manager/administrativeWin";
	}
	
	@RequestMapping()
	@ResponseBody
	public String delete(String kdbh){
		try {
			gateManager.delGate(kdbh);
		} catch (Exception e) {
			e.printStackTrace();
			return String.format("{result:'error',msg:'%1$s'}", e.getMessage()); 
		}
		return String.format("{result:'success',msg:'%1$s'}", "ok"); 
	}

	/**
	 * 分页查询卡口信息列表
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param command
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> findGatePageForMap(HttpServletRequest request,HttpServletResponse response, String page,String rows,CodeGate command){
		Page p = new Page(Integer.parseInt(page),Integer.parseInt(rows));
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
		Department depart = userSession.getDepartment();
		Map<String,Object> result = null;
		try {
			result = this.gateManager.findGatePageForMap(p, command,depart.getGlbm());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 分页查询卡口实时在线情况列表
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param command
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> findGatePageRedisForMap(HttpServletRequest request,HttpServletResponse response, String page,String rows,CodeGate command){
		Page p = new Page(Integer.parseInt(page),Integer.parseInt(rows));
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
		Department depart = userSession.getDepartment();
		Map<String,Object> result = null;
		try {
			result = this.gateManager.findGatePageRedisForMap(p, command,depart.getGlbm());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 保存卡口信息
	 * @param request
	 * @param response
	 * @param gate
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,String> save(HttpServletRequest request,HttpServletResponse response,CodeGate gate){
		Map<String,String> result = null;
		try {
			byte[] kktp = this.getPictureBytes(request);
			this.gateManager.save(gate,kktp);
			result = Common.messageBox("保存成功！", "1");
		} catch (Exception e) {
			result = Common.messageBox("出现异常！", "0");
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(params = "method=forwardDetail",method = RequestMethod.GET)
	public String forwardDetail(HttpServletRequest request,HttpServletResponse response,String kdbh){
		try{
		request.setAttribute("kklx",this.systemManager.getCode("150000"));
        request.setAttribute("kkzt",this.systemManager.getCode("156003"));
        request.setAttribute("kkxz",this.systemManager.getCode("156001"));
        request.setAttribute("kklx2",this.systemManager.getCode("156002"));
        request.setAttribute("sjscms",this.systemManager.getCode("156006"));
        request.setAttribute("dllx",this.systemManager.getCode("156011"));
        request.setAttribute("kkwz",this.systemManager.getCode("156007"));
        request.setAttribute("kdbh",kdbh);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "gate/gatemainWin";
	}
	
	// 查询卡口信息明细
	@RequestMapping(params = "method=getGateInfo")
	@ResponseBody
	public CodeGate getGateInfo(HttpServletRequest request,HttpServletResponse response,String kdbh) {
		CodeGate codeGate = null;
		try {
			codeGate = this.gateManager.getGateInfo(kdbh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codeGate;
	}

	@RequestMapping
	@ResponseBody
	public List<CodeGate> getAllGate() {
		List<CodeGate> list = null;
		try {
			list = this.gateManager.getAllGates();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 部门--卡口关联 城市--卡口关联
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getGateByGlbmorCity(HttpServletRequest request,
			HttpServletResponse response) {
		String glbm = request.getParameter("glbm");
		String city = request.getParameter("city");
		List<CodeGate> list = null;
		CodeGate gate = new CodeGate();
		try {
			if ((glbm != null) && (glbm.length() > 0)) {
				if (glbm.equals("0")) {
					list = this.gateManager.getGateList(gate);
				} else if (glbm.equals("1")) {
					gate.setKklx("1");
					list = this.gateManager.getGateList(gate);
				} else {
					gate.setDwdm(glbm);
					list = this.gateManager.getGateList(gate);
				}
			} else if ((city != null) && (city.length() == 6)) {
				list = this.gateManager.getGateList(gate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据卡口编号获取方向信息列表
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public List<CodeGateExtend> getAllExtend(HttpServletRequest request,
			HttpServletResponse response) {
		List<CodeGateExtend> list = null;
		String kdbh = request.getParameter("kdbh");
		try {
			list = this.gateManager.getDirectList(kdbh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据方向编号获取车道信息列表
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public List<CodeGateCd> getAllRoads(HttpServletRequest request){
		List<CodeGateCd> list = null;
		String fxbh = request.getParameter("fxbh");
		try {
			list = this.gateManager.getRoad(fxbh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	};

     
	@RequestMapping
	public void getGateXls(HttpServletRequest request,HttpServletResponse response,CodeGate gate) {
		String[] columnName = new String[] { "卡口编号", "卡口名称", "卡口类型", "管理部门",
				"卡口地址", "卡口经度", "卡口纬度", "卡口性质", "方向名称", "方向编号", "方向类型", "车道编号",
				"车道类型", "车道IP" };
		short[] columnWidth = new short[] { 5000, 9000, 3000, 4500, 10000,
				3500, 3500, 3500, 9000, 3000, 4000, 2000, 3500, 3500 };
		String[] codeGateMethod = new String[] { "Kdbh", "Kdmc", "Kklxmc",
				"Dwdmmc", "Kkdz", "Kkjd", "Kkwd", "Kkxzmc", "Fxmc", "Fxbh",
				"Fxlx", "Cdbh", "Cdlx", "Ip" };

		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet("sheet1");
		HSSFRow header = sheet.createRow(0);
		header.setHeight((short) 400);
		HSSFCellStyle excelStyle3 = ExcelStyle.excelStyle3(book);
		HSSFCellStyle excelStyle5 = ExcelStyle.excelStyle5(book);
		for (int i = 0; i < columnName.length; i++) {
			sheet.setColumnWidth((short) i, (short) columnWidth[i]);
			sheet.setVerticallyCenter(true);
			HSSFCell cell = header.createCell((short) i);
			cell.setCellValue(columnName[i]);
			cell.setCellStyle(excelStyle3);
		}
		try {
			Map<String,Object> filter = new HashMap<String,Object>();
			filter.put("kkzt", gate.getKkzt());
			filter.put("kdmc", gate.getKdmc());
			filter.put("kklx", gate.getKklx());
			filter.put("dwdm", gate.getDwdm());
			List<Map<String, Object>> gates = this.gateManager.getGateListXls(filter);
			
			short rowIndex = 1;
			short rownum = 1;
			short lastrow = 1;
			for (int j = 0; j < gates.size(); j++) {
				short r_col = 0;
				Map<String, Object> g = gates.get(j);
				HSSFRow r_row = sheet.createRow(rowIndex);
				r_row.setHeight((short) 400);
				if (j == 0) {
					rownum = (short) r_row.getRowNum();
					lastrow = (short) r_row.getRowNum();
				}
				for (int i = 0; i < codeGateMethod.length; i++) {
					Object s = g.get(codeGateMethod[i]);
					HSSFCell cell = r_row.createCell(r_col);
					if (s != null) {
						if (codeGateMethod[i].equals("Cdlx")) {
							s = this.systemManager.getCodeValue("156004", s
									.toString());
						}
						if (codeGateMethod[i].equals("Fxlx")) {
							s = this.systemManager.getCodeValue("155001", s
									.toString());
						}
						cell.setCellValue(s.toString());
					}
					cell.setCellStyle(excelStyle5);
					sheet.addMergedRegion(new Region(rownum, (short) i,
							lastrow, (short) i));
					r_col++;
				}
				rowIndex++;
			}
			// 输出Excel工作薄
           response.setHeader("pragma", "no-cache");
           response.setHeader("cache-control", "no-cache");
           response.setDateHeader("Expires", 0);
			response.setCharacterEncoding("UTF-8");
		    response.reset();
			response.setContentType("application/xml;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;Filename="
					+ new Date().getTime()
					+ new String("卡口基本信息".getBytes("GBK"),"ISO8859-1") + ".xls");
			ServletOutputStream stream = response.getOutputStream();
			book.write(stream);
			stream.flush();
			stream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 上传图片
	 * 
	 * @param request
	 * @return
	 */
	private byte[] getPictureBytes(HttpServletRequest request) throws Exception {
		byte pic[] = (byte[]) null;
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile picobj = mrequest.getFile("file");
		try {
			if (picobj != null && !picobj.isEmpty()) {
				pic = picobj.getBytes();
			} else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pic;
	}
	
	@RequestMapping
	@ResponseBody
	public String getGateName(String kdbh){
		String result = kdbh;
		try {
			result = this.gateManager.getGateName(kdbh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(params = "method=getTollGateTree")
	@ResponseBody
	public List<Map<String,Object>> getTollGateTree(HttpServletRequest request) throws Exception {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		String kd = "";
		 kd = URLDecoder.decode(request.getParameter("kdmc"), "UTF-8");
		////过滤输入的特殊字符//////////
		String regEx="[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";   
        Pattern   p   =   Pattern.compile(regEx);      
        Matcher   mat   =   p.matcher(kd);  
        kd = mat.replaceAll("").trim();		
		String dwdm = request.getParameter("dwdm");
		try {
				list = this.gateManager.getTollGateTree(kd,dwdm);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list.size()>0) {
			
			Iterator<Map<String,Object>> it = list.iterator();
			while(it.hasNext()){
				Map<String,Object> m = it.next();
				
				Map<String,Object> item = new HashMap<String,Object>(); 
				if(m.get("id")!=null) {
					
				  item.put("id", m.get("id").toString());
				  item.put("name", m.get("idname").toString());
				  item.put("pId", m.get("pid").toString());
			      menuNodes.add(item);
			      
				}
			}
		}
		return menuNodes;
	}
	
	@ResponseBody
	@RequestMapping
	public List<CodeGate> getGateByRegion(HttpServletRequest request,
			HttpServletResponse response) {
		String city = request.getParameter("city");
		int jb =Integer.parseInt(request.getParameter("jb"));
		List<CodeGate> list = null;
		CodeGate gate = new CodeGate();
		try {
			switch(jb){
				case 2:gate.setDwdm(city.substring(0,2));break;
				case 3:gate.setDwdm(city.substring(0,4));break;
				case 4:gate.setXzqh(city);break;
			}
			list =this.gateManager.getGateList(gate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@ResponseBody
	@RequestMapping
	public List<CodeGate> getGateByRegionAndJj(HttpServletRequest request,
			HttpServletResponse response) {
		String city = request.getParameter("city");
		int jb =Integer.parseInt(request.getParameter("jb"));
		List<CodeGate> list = null;
		CodeGate gate = new CodeGate();
		try {
			switch(jb){
				case 2:gate.setDwdm(city.substring(0,2));break;
				case 3:gate.setDwdm(city.substring(0,4));break;
				case 4:gate.setXzqh(city);break;
			}
			list =this.gateManager.getGateListAndJj(gate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	@ResponseBody
	@RequestMapping
	public List<CodeGate> getTollGateByRegion(HttpServletRequest request,
			HttpServletResponse response) {
		String city = request.getParameter("city");
		int jb =Integer.parseInt(request.getParameter("jb"));
		List<CodeGate> list = null;
		CodeGate gate = new CodeGate();
		try {
			if(city.length()<=6){
				switch(jb){
					case 2:gate.setDwdm(city.substring(0,2));break;
					case 3:gate.setDwdm(city.substring(0,4));break;
					case 4:gate.setDwdm(city);break;
				}
			}else{
				gate.setDwdm(city);
			}
			list =this.gateManager.getTollGateList(gate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 卡口信息列表查询
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param command
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=queryList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest request,
			HttpServletResponse response, String page, String rows,
			CodeGate command) {
		Map queryMap = null;
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
		Department depart = userSession.getDepartment();
		try {
			Map filter = new HashMap();
			filter.put("curPage", page);
			filter.put("pageSize", rows);
			if (command.getDwdm() != null && command.getDwdm().length() > 1) {
				command.setDwdmmc(this.systemManager.getDepartmentName(command
						.getDwdm()));
			}
			queryMap = this.gateManager.getGates(filter, command, depart.getGlbm());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryMap;
	}
	
	/**
	 * @author huanghaip
	 * @date 2017-3-23
	 * 
	 * 查询行政区划列表
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(params = "method=queryAdminDivisionList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> queryAdminDivisionList(HttpServletRequest request,
			HttpServletResponse response,String page,String rows){
		String xzqh = request.getParameter("xzqh");
		String qhmc = request.getParameter("qhmc");
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,String> filter = new HashMap<String,String>();
		filter.put("curPage", page);
		filter.put("pageSize", rows);
		if(StringUtils.isNotBlank(xzqh))
			filter.put("xzqh",xzqh);
		if(StringUtils.isNotBlank(qhmc))
			filter.put("qhmc",qhmc);
		try{
			map = this.gateManager.getAdministrativeDivisions(filter);
		} catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * @author huanghaip
	 * @date 2017-3-23
	 * 
	 * 获取单条行政区划记录
	 * @param request
	 * @param id
	 * @param xzqh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object queryAdminDivision(HttpServletRequest request,
			@RequestParam String id,@RequestParam String xzqh){
		AdministrativeDivision ad = new AdministrativeDivision();
		try {
			ad = this.gateManager.getAdminDivision(id, xzqh);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ad;
	}
	
	/**
	 * @author huanghaip
	 * @date 2017-3-23
	 * 
	 * 更新行政区划序号记录
	 * @param request
	 * @param ad
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> updateAdminDivision(HttpServletRequest request,
			AdministrativeDivision ad){
		Map<String,Object> map = new HashMap<String,Object>();
		int result = 0;
		try {
			result = this.gateManager.updateAdminDivision(ad);
			if(result != 0){
				map.put("code", "1");
				map.put("msg","更新成功！");
			} else {
				map.put("code", "0");
				map.put("msg","更新失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 批量修改卡口经纬度
	 * @param request
	 * @param response
	 * @param gate
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,String> editGatePointBatch(HttpServletRequest request,HttpServletResponse response){
		Map<String,String> result = null;
		try {
			String gateArray = request.getParameter("gateArray");
			
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			
			String glbm = userSession.getSysuser().getGlbm();
			String ip = userSession.getSysuser().getIp();
			String yhdh = userSession.getSysuser().getYhdh();
			
			this.gateManager.saveGateXY(gateArray,glbm,ip,yhdh);
			result = Common.messageBox("保存成功！", "1");
		} catch (Exception e) {
			result = Common.messageBox("出现异常！", "0");
			e.printStackTrace();
		}
		return result;
	}
}
