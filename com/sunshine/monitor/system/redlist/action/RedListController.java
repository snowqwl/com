package com.sunshine.monitor.system.redlist.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.redlist.bean.RedList;
import com.sunshine.monitor.system.redlist.service.RedListManager;
import com.sunshine.monitor.system.redlist.util.ConfigRedlistFieldsFactory;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;


@Controller
@RequestMapping(value = "/redlist.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RedListController {
	
	@Autowired
	private RedListManager redListManager ;
	
	@Autowired
	private SystemManager systemManager;
	
	/**
	 * 跳转录入红名单主页
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView forward(HttpServletRequest request, HttpServletResponse response) {
		this.getCodes(request);
		return new ModelAndView("redlist/addredlist");
	}
	
	/**
	 * 表单提交
	 * @param request
	 * @param redList
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object addRedlist(HttpServletRequest request, RedList redList) {
		Map<String, Object> result = null;
		try {
			result = new HashMap<String, Object>();
			if(null!=redList){
				
			
//			RedList rdlist = this.redListManager.queryRedList(redList);
//			int count = 0;
//			if(rdlist == null){
//				count = this.redListManager.addRedList(redList);
//				if(count == 1){
//					result.put("msg", "红名单录入成功!");
//				}
//			} else {
//				result.put("msg", "红名单库已经存在记录!");
//			}
			RedList rdlist = this.redListManager.existRedName(redList);
			if(rdlist!=null){
				if("1".equals(rdlist.getStatus()))
					result.put("msg", "该号牌已在红名单，并起效中");
				else
					result.put("msg", "该号牌正在红名单审核中");
			}
			else
			{
			 result.put("msg", "红名单录入成功!");
			 this.redListManager.addRedList(redList);
			}
				
				
				
			}
			else{
				result.put("msg", "红名单录入提交信息不合法");
			}
		} catch (Exception e) {
			result.put("msg", "红名单录入失败!");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Excel 批量导入
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping
	public Object addBatchRedlist(HttpServletRequest request) throws Exception {
		Map<String, Object> result = this.getRedLists(request);
		return result ;
	}
	
	/**
	 * 转入审批主页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardlist(HttpServletRequest request, HttpServletResponse response) {
		this.getCodes(request);
		return new ModelAndView("redlist/auidtredlistmain");
	}
	
	/**
	 * 查询列表(未审批)
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryAllRedlist(HttpServletRequest request) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			result = this.redListManager.queryRedList(conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询红名单
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryRedlist(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		//String _hphm = request.getParameter("hphm");
		String _id = request.getParameter("id");
		UserSession userSession = (UserSession)WebUtils.getSessionAttribute(request, "userSession");
		try {
			//String hphm = URLDecoder.decode(_hphm, "UTF-8");
			RedList condition = new RedList();
			//condition.setHphm(hphm);
			condition.setId(_id);
			RedList rl = this.redListManager.queryRedList(condition);
			result.put("rl", rl) ;
			result.put("ckman", userSession.getSysuser().getYhmc());
			result.put("ckdept", userSession.getDepartment().getBmmc());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 审批
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object updateRedlist(HttpServletRequest request, RedList condition) {
		Map<String, Object> result = null ;
		try {
			result = new HashMap<String, Object>();
			UserSession userSession = (UserSession)WebUtils.getSessionAttribute(request, "userSession");
			if(userSession != null) {
				condition.setAuditman(userSession.getSysuser().getYhdh());
				condition.setAuditdept(userSession.getDepartment().getGlbm());
				int success = this.redListManager.updateRedList(condition);
				if(success == 1) {
					result.put("msg", "审批成功!");
				}
			}
		} catch (Exception e) {
			result.put("msg", "审批失败!");
			e.printStackTrace();
		}
		return result;
	}	
	
	/**
	 * 转入已审批主页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardAuidted(HttpServletRequest request, HttpServletResponse response) {
		this.getCodes(request);
		return new ModelAndView("redlist/queryredlistmain");
	}	
	
	
	/**
	 * 查询列表(已审批)
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryAllAuidtedRedlist(HttpServletRequest request) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			result = this.redListManager.queryAuidtedRedList(conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	
	/**
	 * 解封
	 * @param request
	 * @param condition
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object deBlockRedlist(HttpServletRequest request, RedList condition) {
		Map<String, Object> result = null ;
		try {
			result = new HashMap<String, Object>();
			UserSession userSession = (UserSession)WebUtils.getSessionAttribute(request, "userSession");
			if(userSession != null) {
				int success = this.redListManager.deblockingRedList(condition);
				if(success == 1) {
					result.put("msg", "解封成功!");
				}
			}
		} catch (Exception e) {
			result.put("msg", "解封失败!");
			e.printStackTrace();
		}
		return result;
	}	
	/**
	 * 转入查询红名单主页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardValidRedlist(HttpServletRequest request, HttpServletResponse response) {
		this.getCodes(request);
		getCodes(request);
		ModelAndView veiw = new ModelAndView("redlist/queryvalidredlist");
		veiw.addObject("isvalid", request.getParameter("isvalid"));
		return new ModelAndView("redlist/queryvalidredlist");
	}	
	/**
	 * 
	 * 函数功能说明：查询jm_red_namelist表数据
	 * @param condition
	 * @param request
	 * @return    
	 * @return 
	 */
	@ResponseBody
	@RequestMapping
	public Object queryValidRedlist(HttpServletRequest request, RedList condition) {
		Map<String, Object> result = null ;
		try {
			result = new HashMap<String, Object>();
			UserSession userSession = (UserSession)WebUtils.getSessionAttribute(request, "userSession");
			Map<String,Object> paras = Common.getParamentersMap(request);
			if(userSession != null) {
				result = this.redListManager.queryValidRedlist(paras);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
	public Object pageAddRedlist(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		File file = new File(request.getSession().getServletContext().getRealPath("/")+"/upload"+"/redlistmodule.xls");
		// 当前的页码
		int page = Integer.parseInt(request.getParameter("page")) ;
		int rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> result = new HashMap<String, Object>();
		// 用于生成重复的号牌号码信息
		List<RedList> list = new ArrayList<RedList>();
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

				System.out.println("test:行总数" + rowNum);
				System.out.println("test:列总数" + cellNum);
				String methodNames[] = getMethodName();
				
				for (int i = (page-1)*rows+1; i < (page-1)*rows+1+rows; i++) {
					row = sheet.getRow(i);
					RedList red = new RedList();
					for (short j = 0; j < cellNum; j++) {
						if (row == null) {
							continue;
						}
						HSSFCell cell = row.getCell(j);
						if(cell!=null){
						// System.out.println(cell);
						if (methodNames != null && j < methodNames.length) {						
							String cellVal = "";
							cellVal = cell.toString().trim();
							String methodName = "set" + methodNames[j];
							ClassUtil.invokeMethod(red, methodName, cellVal);							
						} else {
							// Excel字段长度与字段描述文件不匹配
							result.put("msg", "Excel字段长度与字段描述文件不匹配!");
							break;
						}
						}
					}
					// 放入set
					list.add(red);
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
	
	public String [] getMethodName(){
		String [] methodNames={"Hphm","hpzl","clxh","clpp","cllx","hpys","csys","clsyr","syrlxdh","syrxxdz"
		};
		return methodNames;
	}
	
	/**
	 * Excel 批量处理
	 * @param request
	 * @return
	 */
	private Map<String, Object> getRedLists(HttpServletRequest request){
 		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile excelFile = mrequest.getFile("exfilename");
		Map<String, Object> result = new HashMap<String, Object>();
		String cfcs = "" ;		
		// 用于生成重复的号牌号码信息
		List<String> list = new ArrayList<String>();
		// 保存最终数据(去掉重复数据)
		Set<RedList> set = new HashSet<RedList>();
		try {
			if (excelFile != null && !excelFile.isEmpty()) {
				
				InputStream input = excelFile.getInputStream();
				String filename=excelFile.getOriginalFilename();
				//保存临时文件到项目里面，做分页查询用.
				if(input!=null){
					File file=new File(request.getSession().getServletContext().getRealPath("/")+"upload");
					if((!file .exists()  && !file .isDirectory())){
						file.mkdir();
					}
					//FileOutputStream fs=new FileOutputStream(request.getSession().getServletContext().getRealPath("/")+"/upload/"+filename);
					FileOutputStream fs=new FileOutputStream(request.getSession().getServletContext().getRealPath("/")+"/upload/"+"/redlistmodule.xls");
					
					byte[] buffer =new byte[1024*1024];
			        int bytesum = 0;
			        int byteread = 0; 
			        while ((byteread=input.read(buffer))!=-1)
			       {
			           bytesum+=byteread;
			           fs.write(buffer,0,byteread);
			           fs.flush();
			        } 
			        fs.close();
				};
				FileInputStream tempinput=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"/upload/"+"/redlistmodule.xls");
				HSSFWorkbook book = new HSSFWorkbook(tempinput);
				// 默认sheet
				HSSFSheet sheet = book.getSheetAt(0);
				HSSFRow row = sheet.getRow(sheet.getLastRowNum());
				// 行总数
				int rowNum = sheet.getLastRowNum() + 1;
				// 列总数
				int cellNum = (int)row.getLastCellNum();
				
				String checkMessage = validateExcelFormat(sheet);
				if(checkMessage!="")//校验excel数据
				{
					result.put("msg", checkMessage);
				} else {
					String[] fields = ConfigRedlistFieldsFactory.getConfigRedlist().getFieldsArray();
					for(int i = 1; i < rowNum; i++) {
						row = sheet.getRow(i);
						RedList redlist = new RedList();
						for(short j = 0; j < cellNum; j++) {
							if(row == null){
								continue;
							}
							HSSFCell cell = row.getCell(j);
							if(fields !=null && j <= fields.length) {
		                		String methodName = "set" + fields[j];
		                		String cellVal = "";
		                		if (cell != null) {
		                			cellVal = cell.toString().trim();
		                		}
		                		ClassUtil.invokeMethod(redlist, methodName, cellVal);
		                	} else {
		                		// Excel字段长度与字段描述文件不匹配
		                		result.put("msg", "Excel字段长度与字段描述文件不匹配!");
		                		break;
		                	}
						}
						// 放入set
						set.add(redlist);
						list.add(redlist.getHphm());
					}
				}
				input.close();
				// 数据分析
				if(set.size() > 0) {
					/**int success = this.redListManager.addRedListBatch(set);
					if (success > 0) {
						StringBuilder strResult = new StringBuilder(20);
						cfcs = getAnlysisResult(list);
						if(!"".equals(cfcs)) {
							strResult.append("Excel数据分析结果:");
							strResult.append(cfcs);
							strResult.append(",");
						}
						result.put("cfcs",strResult.toString() + "成功导入数据:" + success + "条");**/
						result.put("rows", set);
						result.put("total", set.size());
					}
				//}
			} else {
				// 文件为空
				result.put("msg", "文件名为空!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 确认录入
	 * @param request
	 * @param response
	 * @param red
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	@ResponseBody
	@RequestMapping
	public Object commitRedlist(HttpServletRequest request,HttpServletResponse response,RedList red,Map<Object,Object> map){
		String obj2=request.getParameter("red");	
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			 JSONArray arry = JSONArray.fromObject(obj2);
			 //统计失败记录数和具体原因
			 int count =0;
			 String flag[] =new String[arry.size()];
			 for (int i = 0; i < arry.size(); i++)
		        {
		            JSONObject jsonObject = arry.getJSONObject(i);		           		          
		            RedList redinfo= (RedList) jsonObject.toBean(jsonObject, RedList.class);		        
		        	int lr = this.redListManager.addRedList(redinfo);
		        	if(lr == 0){count++;}
		        }
			 if(count == 0){
				 result.put("msg", "已成功录入"+arry.size()+"条数据"); 
			 }
			 result.put("faile", count);
		}
		
		catch(Exception e){
			e.printStackTrace();
		}


		
		return result;
	}
	
	
	/**
	 *红名单excel表校验
	 * 
	 */
	private String  validateExcelFormat(HSSFSheet sheet) {
		
		//表列名
		String[] fieldName = { "号牌号码" , "号牌种类" , "车辆型号", 
				"车辆品牌" , "车辆类型" , "号牌颜色" , "车辆颜色" , 
				"车辆所属人" , "所属人联系电话" , "所属人详细地址" };
		/*
		 * 是否必填，与模板表对应
		 * 1：必填，0：可选
		 */
		String[] isRequired = { "1", "1", "0", "0", "0", "0", "0", "0", "0", "0" };
		
		//号牌种类
		String[] licencePlateType = { "01", "02", "03", "04", "05", "06", "07", "08", "09",
				"10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
				"20", "21", "22", "23", "24", "99" };
		
		// 车辆类型
		String[] vehicleType = { "0", "1", "2", "3", "9", "B11", "B12", "B13", "B14",
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
		
		// 号牌颜色 
		String[] licencePlateColor = { "0", "1", "2", "3", "4" };
		
		// 车身颜色 
		String[] vehicleColor = { "A", "B", "C", "D", "E", "F", "H", "I", "J" };
		
		String checkMessage = "";
		HSSFRow row = sheet.getRow(sheet.getLastRowNum());
		// 行总数
		int rowNum = sheet.getLastRowNum() + 1;
		// 列总数
		int cellNum = (int) row.getLastCellNum();
		for (int i = 1; i < rowNum; i++) {// 行
			row = sheet.getRow(i);
			for (short j = 0; j < cellNum; j++) {// 列
				if (row == null) {
					continue;
				}
				// 判断是否必填
				if (isRequired[j].equalsIgnoreCase("1")) {
					if (row.getCell(j) == null
							|| row.getCell(j).toString() == "") {
						checkMessage += "第" + (i + 1) + "行," + "第" + (j + 1) + "列"
								+ "，为必填数据，请修改 <br>";
					} else {
						if( j == 0) {
							try {
								RedList redList = 
									this.redListManager.queryRedList(" and hphm=" + "'" + row.getCell(j).toString() + "'  and isvalid='1'");
								if (redList != null) {
									if( Integer.valueOf(redList.getStatus()) == 0)
										checkMessage += "第" + (i + 1) + "行," + "第1列"
											+ "，" + row.getCell(j).toString() + "已添加到红名单审批表中，请修改 <br>";
									else if(Integer.valueOf(redList.getStatus()) == 1) 
										checkMessage += "第" + (i + 1) + "行," + "第1列"
										+ "，" + row.getCell(j).toString() + "已添加到查询红名单表中，请修改 <br>";
								}
							} catch (Exception e) {
								checkMessage += "异常，请重试！";
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		return checkMessage;
	}
	
	/**
	 * 获取重复分析结果
	 * @param set
	 * @param list
	 * @return
	 */
	private String getAnlysisResult(List<String> list){
		Set<String> set = new HashSet<String>(list);
		StringBuffer msg = new StringBuffer(20);
		for(String _hphm : set){
			int _count = Collections.frequency(list, _hphm) ;
			if((_count - 1) > 0 ) {
				msg.append(" "+_hphm).append(",重复").append(_count-1).append("次");
			}
		}
		return msg.toString() ;
	}
	/**
	 * 获取hpzl、cllx、csys、hpys
	 * @param request
	 * @return
	 */
	private void getCodes(HttpServletRequest request){
		Map<String, List<Code>> map = null ;
		try {
			List<Code> hpzlList = this.systemManager.getCodes("030107");

			List<Code> cllxList = this.systemManager.getCodes("030104");
			List<Code> csysList = this.systemManager.getCodes("030108");
			List<Code> hpysList = this.systemManager.getCodes("031001");

			map = new HashMap<String, List<Code>>();
			map.put("hpzlList", hpzlList);
			map.put("cllxList", cllxList);
			map.put("csysList", csysList);
			map.put("hpysList", hpysList);
			request.setAttribute("codemap", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
