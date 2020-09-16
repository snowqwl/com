package com.sunshine.monitor.system.susp.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.sunshine.core.util.ClassUtil;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.BusinessType;
import com.sunshine.monitor.comm.util.DispatchedRangeType;
import com.sunshine.monitor.comm.util.FuzzySusp;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.PrefectureManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;

@Controller
@RequestMapping(value="/suspinfoAuto.do",params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuspinfoAutoController {
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	
	@Autowired
	private SuspinfoManager suspinManager;
	
	@Autowired
	private PrefectureManager prefectureManager;
	
	// 一键布控主页
	private String mainPage = "susp/autoSusplist";
	
	private Logger log = LoggerFactory.getLogger(SuspinfoAutoController.class);
	
	/**
	 * 一键布控跳转页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forward(HttpServletRequest request,HttpServletResponse response ){
		
		
		return new ModelAndView(this.mainPage);
	}
	
	/**
	 * 上传布控模版
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object addSusplist(HttpServletRequest request,HttpServletResponse response){
		String bklx = request.getParameter("bkbz");
		Map<String, Object> result = new HashMap<String, Object>();
		// 用于生成重复的号牌号码信息
		List<VehSuspinfo> list = new ArrayList<VehSuspinfo>();
		try{
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile excelFile = mrequest.getFile("exfilename");
		if (excelFile != null && !excelFile.isEmpty()) {
				InputStream input = excelFile.getInputStream();
				//保存临时文件到项目里面，做分页查询用.
				if(input!=null){
					File file=new File(request.getSession().getServletContext().getRealPath("/")+"upload");
					if((!file .exists()  && !file .isDirectory())){
						file.mkdir();
					}
					//FileOutputStream fs=new FileOutputStream(request.getSession().getServletContext().getRealPath("/")+"/upload/"+filename);
					FileOutputStream fs=new FileOutputStream(request.getSession().getServletContext().getRealPath("/")+"/upload/"+"/autoSusplistlistmodule.xls");
					
					byte[] buffer =new byte[1024*1024];
			        int bytesum = 0;
			        int byteread = 0; 
			        while ((byteread=input.read(buffer))!=-1){
			           bytesum+=byteread;
			           fs.write(buffer,0,byteread);
			           fs.flush();
			        } 
			        fs.close();
				};
				//FileInputStream tempinput=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"/upload/"+filename);
				FileInputStream tempinput=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"/upload/"+"/autoSusplistlistmodule.xls");
				HSSFWorkbook book = new HSSFWorkbook(tempinput);
				// 默认sheet
				HSSFSheet sheet = book.getSheetAt(0);
				HSSFRow row = sheet.getRow(sheet.getLastRowNum());
				// 行总数
				int rowNum = sheet.getLastRowNum() + 1;
				// 列总数
				int cellNum = (int)row.getLastCellNum();
				HSSFCell c_cell = row.getCell((short) 22);
				String c_cellVal = c_cell.toString().trim();
				if(!c_cellVal.equals("本地")){
					bklx = "1";
				}
				String checkMessage = checkExcelFormal(sheet, bklx);
				if(checkMessage != ""){//校验excel数据
					result.put("msg", checkMessage);
				}
				else{
					String methodNames[] = getMethodName();
					for(int i = 1; i < rowNum; i++){
						row = sheet.getRow(i);
						VehSuspinfo veh=new VehSuspinfo();
						for(short j = 0; j < cellNum; j++){
							if(row == null){
								continue;
						}
						HSSFCell cell = row.getCell(j);
						if(methodNames != null && j < methodNames.length){
							if(cell != null){
								String cellVal = "";
								cellVal = cell.toString().trim();
								String methodName = "set" + methodNames[j];
								if(methodNames[j].equals("bkfw")){
									String bkfw = getCityName(cellVal);
									String bkfwmc = "setBkfwmc";
									ClassUtil.invokeMethod(veh, bkfwmc, bkfw);
								}
								ClassUtil.invokeMethod(veh, methodName, cellVal);
							}
						}else {
		                	// Excel字段长度与字段描述文件不匹配
		                	result.put("msg", "Excel字段长度与字段描述文件不匹配!");
		                	break;	                
						}
					}
					// 放入set
					list.add(veh);
					
				}
				
			}
			input.close();
				// 数据分析
			if(list.size() > 0) {
					
				result.put("rows", list);
				result.put("total", list.size());
			}
		} else {
				// 文件为空
				result.put("msg", "文件名为空!");
		}
		
	}catch(java.lang.ClassCastException e){
		System.out.println("转换失败");
	}
	catch (Exception e) {
		e.printStackTrace();
	}
		return result;
	}
	
	/*
	 * 函数功能：根据城市的 
	 * */
	private String getCityName(String cityName){
		String dwdmmc = "";
		if(cityName == null || cityName.length() < 1)
			return cityName;
		String bkfws[] = cityName.split(",");
		for(int i = 0; i < bkfws.length; i++){
			dwdmmc += prefectureManager.getCityName(bkfws[i]) + ",";
		}
		if(dwdmmc.endsWith(","))
			dwdmmc = dwdmmc.substring(0, dwdmmc.length() - 1);
		
 		return dwdmmc;
	}
	
	
	@SuppressWarnings("deprecation")
	private String checkExcelFormal(HSSFSheet sheet, String bklx) {
		// 属性名称
		String[] sxmc = { "号牌号码", "号牌种类", "车辆型号", "车辆品牌", "车辆类型", "号牌颜色",
				"车身颜色", "车辆所属人", "所属人联系电话", "所属人详细地址", "立案人", "布控大类", "布控类别",
				"布控级别", "报警预案", "报警方式", "短信接收号码", "涉抢涉暴", "立案单位", "立案单位电话",
				"布控有效开始时间", "布控有效结束时间", "联动布控范围", "简要案情", "布控人手机号码" };
		// 是否必填
		String[] sfbt = { "1", "1", "0", "0", "0", "0", "0", "0", "0", "0",
				"1", "1", "1", "1", "1", "1", "0", "1", "1", "1", "1", "1", "0", "1", "1" };

		// 号牌种类 2
		String[] hpzl = { "01", "02", "03", "04", "05", "06", "07", "08", "09",
				"10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
				"20", "21", "22", "23", "24", "99" };
		// 车辆类型 5
		String[] cllx = { "0", "1", "2", "3", "9", "B11", "B12", "B13", "B14",
				"B15", "B16", "B17", "B21", "B22", "B23", "B24", "B25", "B26",
				"B27", "B31", "B32", "B33", "B34", "B35", "D11", "D12", "G11",
				"G13", "G14", "G15", "G16", "G21", "G22", "G23", "G24", "G25",
				"G26", "G31", "G32", "G33", "G34", "G35", "H11", "H12", "H13",
				"H14", "H15", "H16", "H17", "H18", "H21", "H22", "H23", "H24",
				"H25", "H26", "H27", "H28", "H31", "H32", "H33", "H34", "H35",
				"H37", "H38", "H41", "H42", "H43", "H44", "H45", "H46", "H51",
				"H52", "H53", "H54", "J11", "J12", "J13", "K11", "K12", "K13",
				"K14", "K15", "K21", "K22", "K23", "K24", "K25", "K31", "K32",
				"K33", "K41", "K42", "K43", "M11", "M12", "M13", "M14", "M15",
				"M21", "M22", "N11", "Q11", "Q21", "Q31", "T11", "T21", "T22",
				"T23", "X99", "Z11", "Z21", "Z31", "Z41", "Z51", "Z71" };
		// 号牌颜色 6
		String[] hpys = { "0", "1", "2", "3", "4" };
		// 车身颜色 7
		String[] csys = { "A", "B", "C", "D", "E", "F", "H", "I", "J" };
		// 布控大类 12
		String[] bkdl = { "1", "2", "3" };
		// 布控类别 13
		String[] bklb = { "00", "01", "02", "03", "04", "05", "06", "08", "11", "12" };
		// 布控级别 14
		String[] bkjb = { "1", "2", "3", "4" };
		// 报警预案 15
		String[] bjya = { "0", "1" };
		// 报警方式 16
		// String []bjfs={"0001","0010","0100","1000"
		// };
		// 涉抢涉暴18
		String[] sqsb = { "0", "1", "2" };

		Map<String, String[]> coll = new HashMap<String, String[]>();
		coll.put("2", hpzl);
		coll.put("5", cllx);
		coll.put("6", hpys);
		coll.put("7", csys);
		coll.put("12", bkdl);
		coll.put("13", bklb);
		coll.put("14", bkjb);
		coll.put("15", bjya);
		// coll.put("16",bjfs);
		coll.put("18", sqsb);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);

		String checkMessage = "";
		HSSFRow row = sheet.getRow(sheet.getLastRowNum());
		// 行总数
		int rowNum = sheet.getLastRowNum() + 1;
		// 列总数
		int cellNum = (int) row.getLastCellNum();
		int flag1 = 0;
		for (int i = 1; i < rowNum; i++) {// 行
			row = sheet.getRow(i);
			String bkdlTemp = "";// 存储这一列的布控大类
		    boolean dxtzTemp = false;// 存储这一列的是否要短信通知
			for (short j = 0; j < cellNum; j++) {// 列
				if (row == null) {
					continue;
				}
				if (sfbt[j].equals("1"))// 判断是否必填
					if (row.getCell(j) == null || row.getCell(j).toString() == "")
						checkMessage += "第" + (i + 1) + "行," + sxmc[j] + "，为必填数据，请修改！ <br>";

				if ((row.getCell(j) != null) && (j == 20 || j == 21)){// 判断时间格式
					try {
						dateFormat.parse(row.getCell(j).toString());
					} catch (Exception e) {
						// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
						checkMessage += "第" + (i + 1) + "行," + sxmc[j] + "，时间格式不对，请修改！ <br>";
					}
				}
				
				if(bklx.equals("1")){
					if((row.getCell(j) != null) && j == 22){
						String temp = row.getCell(j).toString();
						if(temp.length() <= 0)
							checkMessage += "第" + (i + 1) + "行," + sxmc[j] + "，为必填数据，请修改 <br>";
						if(temp.length() > 12){
							String str[] = temp.split(",");
							for(int s = 0; s < str.length; s++){
								if(str[s].length() != 12){
									checkMessage += "第" + (i + 1) + "行," + sxmc[j] + "，"+ str[s] +"必须为12位，请修改！ <br>";
								}
							}
						}
					}
				}
				
				if((row.getCell(j) != null) &&j == 15) {//报警方式
					String temp2 = row.getCell(j).toString();
					dxtzTemp = false;
					boolean flag = true;//暂存报警方式的格式是否正确
					if (temp2.length() == 4) {
						if(temp2.equals("0000")){
							flag = false;
							checkMessage += "第" + (i + 1) + "行," + sxmc[j] + "格式错误，为1和0组成<br>";
						}
						for (int m = 0; m < temp2.length(); m++) {
							if (!(temp2.charAt(m) == '0' || temp2.charAt(m) == '1')) {
								flag = false;
								checkMessage += "第" + (i + 1) + "行," + sxmc[j] + "为1和0组成<br>";
								break;
							}
						}
					} else{
						flag = false;
						checkMessage += "第" + (i + 1) + "行," + sxmc[j]+ "一定要4位<br>";
					}
					if(flag){
						if(temp2.charAt(0)=='1')//获取头一位
							dxtzTemp=true;		
					}
				} 
				
				if( dxtzTemp && j == 16) {//报警方式选择了短信通知，所以短信通知号码不能为空
					if(row.getCell(j) != null&&row.getCell(j).toString()!=""){
					}
					else
						checkMessage += "第" + (i + 1) + "行," + sxmc[j]+ "报警方式选择了短信通知，所以短信通知号码不能为空<br>";
				}
				if (j == 15 || coll.get(String.valueOf(j + 1)) != null && row.getCell(j) != null)// 存在该列类型
					if (j == 15 || coll.get(String.valueOf(j + 1)).length != 0 ){// 获取数组
						if (j == 6 && row.getCell(j).toString() != "") {// 车身颜色：由1-3中颜色组成
							String[] temp1 = row.getCell(j).toString().split(",");
							flag1 = 1;
							for (int temp = 0; temp < temp1.length; temp++) {
								for (int k = 0; k < coll.get(String.valueOf(j + 1)).length; k++) {
									if (temp1[temp].equalsIgnoreCase(coll.get(String.valueOf(j + 1))[k])) {
										flag1 = 0;
										break;
									}
								}
								if (flag1 == 1) {// 如果某一种颜色不在车身颜色库里，结束
									break;
								} else if (temp != temp1.length - 1) {// 如果还没验证完输入的颜色，继续
									flag1 = 1;
								}
							}
							if (row.getCell(j).toString().length() > 5) {// 车身颜色超过3种
								flag1 = 1;
							}
						}
						/*
						 * 涉案类 : 布控级别 只有1-2级,并且只能拦截  交通违法类 : 属于3级,可拦截可不拦截 管控类: 属于4级
						 * */
						else if (j == 11 || j == 13 || j==14 || j==15) {
							if (j == 11){
								bkdlTemp = row.getCell(j).toString();
								if(bklx.equals("1")){
									log.info("批量布控导入-布控类型：" + bkdlTemp);
									if (bkdlTemp.equals("2")) {// 交通违法类
										checkMessage += "第" + (i + 1) + "行," + sxmc[j] +" 联动布控大类只有涉案类和管控类，请修改！<br>";
										break;
									} 
								}
							} else      //布控大类和布控级别的联系
								if(j==13){
									if (bkdlTemp.equalsIgnoreCase("1")) {// 涉案类
										if (!(row.getCell(j).toString().equalsIgnoreCase("1") || row.getCell(j).toString().equalsIgnoreCase("2")))
											flag1 = 3;
									} else if (bkdlTemp.equalsIgnoreCase("2")) {// 交通违法类
										if (!row.getCell(j).toString().equalsIgnoreCase("3"))
											flag1 = 4;
									} else if (bkdlTemp.equalsIgnoreCase("3")) {// 管控类
										if (!row.getCell(j).toString().equalsIgnoreCase("4"))
											flag1 = 5;
									}
							
								} else
									if(j == 14){//布控大类和报警预案的联系
										if(bkdlTemp.equalsIgnoreCase("1")) {// 涉案类
											if(!(row.getCell(j).toString().equalsIgnoreCase("1")))
												flag1 = 6;
										} else if(bkdlTemp.equalsIgnoreCase("3")) {// 管控类
											if(!row.getCell(j).toString().equalsIgnoreCase("0"))
												flag1 = 7;
										}
									} else{
									//	if(j==15)//布控大类和报警方式的联系
										if(bkdlTemp.equalsIgnoreCase("3")) {// 管控类
											if(!row.getCell(j).toString().equalsIgnoreCase("0001"))
												flag1 = 8;
										} 
									}
						}else {
							if( j == 4 || j == 5 || j == 6){
								flag1 = 0;
							} else {
								flag1 = 2;
							}
							/*for (int k = 0; k < coll.get(String.valueOf(j + 1)).length; k++) {
								if (row.getCell(j).toString().equalsIgnoreCase(
									coll.get(String.valueOf(j + 1))[k])) {
									flag1 = 0;
									break;
								}
							}*/
							List<String> list = Arrays.asList(coll.get(String.valueOf(j + 1)));
							String v_val = String.valueOf(row.getCell(j)).toUpperCase();
							if(StringUtils.isNotBlank(v_val)){
								if(list.contains(v_val)){
									flag1 = 0;
								} else {
									flag1 = 2;
								}
							}
						}
						if (flag1 != 0) {
							switch (flag1) {
							case 1:
								checkMessage += "第" + (i + 1) + "行," + sxmc[j]
										+ "，出错，请修改 <br>";
								break;
							case 2:
								checkMessage += "第" + (i + 1) + "行," + sxmc[j]
										+ "，出错，请修改 <br>";
								break;
							case 3:
								checkMessage += "第" + (i + 1) + "行," + sxmc[j]
										+ "，出错，涉案类的布控级别只能为1或2<br>";
								break;
							case 4:
								checkMessage += "第" + (i + 1) + "行," + sxmc[j]
										+ "，出错，交通违法类的布控级别只能为3<br>";
								break;
							case 5:
								checkMessage += "第" + (i + 1) + "行," + sxmc[j]
										+ "，出错，管控类的布控级别只能为4(四级)<br>";
								break;
							case 6:
								checkMessage += "第" + (i + 1) + "行," + sxmc[j]
										+ "，出错，涉案类的报警预案必须为1(拦截)<br>";
								break;
							case 7:
								checkMessage += "第" + (i + 1) + "行," + sxmc[j]
										+ "，出错，管控类的报警预案必须为0(不拦截)<br>";
								break;
							case 8:
								checkMessage += "第" + (i + 1) + "行," + sxmc[j]
										+ "出错，管控类只能短信通知<br>";
								break;
							default:
								checkMessage += "第" + (i + 1) + "行," + sxmc[j]
										+ "，出错，请修改 <br>";
								break;
							}
							flag1 = 0;
						}
					}
			}
		}
		return checkMessage;

	}

	
	/**
	 * 分页显示
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping
	public Object pageAddSusplist(HttpServletRequest request,HttpServletResponse response) throws Exception {
		File file = new File(request.getSession().getServletContext().getRealPath("/")+"/upload"+"/autoSusplistlistmodule.xls");
		// 当前的页码
		int page = Integer.parseInt(request.getParameter("page")) ;
		int rows = Integer.parseInt(request.getParameter("rows"));
		//System.out.println(page);
		//System.out.println(rows);
		Map<String, Object> result = new HashMap<String, Object>();
		// 用于生成重复的号牌号码信息
		List<VehSuspinfo> list = new ArrayList<VehSuspinfo>();
		if (file.exists()) {
			try {
				FileInputStream input = new FileInputStream(file);
				HSSFWorkbook book = new HSSFWorkbook(input);
				// 默认sheet
				HSSFSheet sheet = book.getSheetAt(0);
				HSSFRow row = sheet.getRow(sheet.getLastRowNum());
				// 行总数
				int rowNum = sheet.getLastRowNum() + 1;
				// 列总数
				int cellNum = (int) row.getLastCellNum();

				//System.out.println("test:行总数" + rowNum);
				//System.out.println("test:列总数" + cellNum);
				String methodNames[] = getMethodName();
				
				for (int i = (page - 1) * rows+1; i < (page-1) * rows + 1 + rows; i++) {
					row = sheet.getRow(i);
					VehSuspinfo veh = new VehSuspinfo();
					for (short j = 0; j < cellNum; j++) {
						if (row == null) {
							continue;
						}
						HSSFCell cell = row.getCell(j);
						if (methodNames != null && j < methodNames.length) {
							if(cell!=null){
								String cellVal = "";
								cellVal = cell.toString().trim();
								String methodName = "set" + methodNames[j];
								ClassUtil.invokeMethod(veh, methodName, cellVal);
							}
						} else {
							// Excel字段长度与字段描述文件不匹配
							result.put("msg", "Excel字段长度与字段描述文件不匹配!");
							break;
						}
					}
					// 放入set
					list.add(veh);

				}
				input.close();
				// 数据分析
				if (list.size() > 0) {

					result.put("rows", list);
					result.put("total",rowNum-1);
				} else {
					// 文件为空
					result.put("msg", "文件名为空!");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return result;
	}
	
	/**
	 * 确认布控
	 * @param request
	 * @param response
	 * @param veh
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object commitSusplist(HttpServletRequest request,HttpServletResponse response,VehSuspinfo veh){
		String obj2=request.getParameter("veh");
		//用于区分是否为本地布控还是联动布控
		String bz = request.getParameter("bz");
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			JSONArray arry = JSONArray.fromObject(obj2);
			// 统计失败记录数和具体原因
			int count = 0;
			String flag[] = new String[arry.size()];
			for (int i = 0; i < arry.size(); i++) {
				JSONObject jsonObject = arry.getJSONObject(i);
				VehSuspinfo vehinfo = (VehSuspinfo) jsonObject.toBean(jsonObject, VehSuspinfo.class);
				VehSuspinfo dveh = null;
				String bkfw = vehinfo.getBkfw();
				String bkfwlx = "";
				if (bkfw.equals("本地") || bkfw.length() < 12)
					bz = "0";
				else {
					if(bkfw.indexOf("44") != -1)
						bkfwlx = "3";
					bz = "1";
				}
				if (bz.equals("0")) // 批量本地布控
					dveh = this.getVehSuspinfoFromParameters(vehinfo, request,
							false, false, bkfwlx);
				else if (bz.equals("1")) // 批量联动布控
					dveh = this.getVehSuspinfoFromParameters(vehinfo, request,
							false, true, bkfwlx);
				if (dveh.getBkdl() != null) {
					if (dveh.getBkdl().equals("3")) {
						dveh.setBkxz("0");
					} else
						dveh.setBkxz("1");
					if (dveh.getBkjglxdh() == null
							|| dveh.getBkjglxdh().equals("")) {
						dveh.setBkjglxdh("");
					}
				}
				flag[i] = this.suspinManager.checkSuspinfo(dveh.getHphm(), dveh.getBkdl());
				if (flag[i].equals("")) {
					result = (Map<String, Object>) this.suspinManager.saveAutoVehSuspinfo(dveh, null);
				} else {
					count++;
				}
			}
			result.put("faile", count);
			result.put("faileMsg", flag);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String [] getMethodName(){
		String []methodNames={"Hphm","hpzl","clxh","clpp","cllx","hpys","csys","clsyr","syrlxdh","syrxxdz",
			"lar","bkdl","bklb","bkjb","bjya","bjfs","dxjshm","sqsb","ladw","ladwlxdh","bkqssj","bkjzsj","bkfw","jyaq","bkjglxdh"
		};
		return methodNames;
	}
	
	@ResponseBody
	@RequestMapping
	public void getDownModel(HttpServletResponse response,String filename,HttpServletRequest request){
		try {
			FileInputStream	 fis=new FileInputStream(new File(request.getSession().getServletContext().getRealPath("/")+"/download/"+filename+""));
			byte[] b=new byte[fis.available()];
			fis.read(b);
			fis.close();
			response.setContentType("application;charset=UTF-8");   			
			response.setHeader("Content-Disposition", "attachment;Filename="+new String(filename.getBytes("utf-8"), "ISO_8859_1"));
			OutputStream os = response.getOutputStream();
			os.write(b, 0, b.length);  
	        os.flush(); 
	        os.close(); 
		}
		catch(Exception e){
			e.toString();
		}
		
	}
	/**
	 * 保存布控参数值封装
	 * @param request
	 * @param flag 模糊布控标志
	 * @param isldbk 联动布控标志
	 * @return
	 * @throws Exception
	 */
	private VehSuspinfo getVehSuspinfoFromParameters(VehSuspinfo veh, HttpServletRequest request, 
			boolean flag, boolean isldbk, String bkfwlx) throws Exception {
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
		if(!isldbk){
			String xzqh = getXzqh(userSession);
			veh.setBkfw(xzqh);
			veh.setBkfwlx(DispatchedRangeType.LOCAL_DISPATCHED.getCode());
		} else {
			/**
			 * 新增跨省联动布控
			 * add by huanghaip 2017-7-7
			 */
			if(bkfwlx.equals("3"))
				veh.setBkfwlx(DispatchedRangeType.KLINKAGE_DISPATCHED.getCode());
			else
				veh.setBkfwlx(DispatchedRangeType.LINKAGE_DISPATCHED.getCode());
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

	
	
	public SystemManager getSystemManager() {
		return systemManager;
	}

	public void setSystemManager(SystemManager systemManager) {
		this.systemManager = systemManager;
	}

	public SuspinfoManager getSuspinManager() {
		return suspinManager;
	}

	public void setSuspinManager(SuspinfoManager suspinManager) {
		this.suspinManager = suspinManager;
	}
}
