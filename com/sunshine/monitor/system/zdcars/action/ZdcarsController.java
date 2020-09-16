package com.sunshine.monitor.system.zdcars.action;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.ibm.icu.text.SimpleDateFormat;
import com.sunshine.core.util.ClassUtil;
import com.sunshine.monitor.system.zdcars.bean.CarS;
import com.sunshine.monitor.system.zdcars.bean.Carsresult;
import com.sunshine.monitor.system.zdcars.dao.ZdcarsDao;
import com.sunshine.monitor.system.zdcars.service.ZdCarsService;
import com.sunshine.monitor.system.zdcars.util.ArrayIdUtil;
import com.sunshine.monitor.system.zdcars.util.DateJsonValueProcessor;
import com.sunshine.monitor.comm.util.ExcelStyle;


@Controller
@RequestMapping(value="/zdcars.do",params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZdcarsController {
	@Autowired
	private ZdcarsDao zdcarsdao;
	
	@Autowired
	private  ZdCarsService zdcarsservice ; 
	
	/**
	 * 跳转图片数据上传页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forward(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("zdcars/zdcarslist");
	}
	
	
	/**
	 * 把上传的数据做处理
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object addCars(HttpServletRequest request,HttpServletResponse response){
	
		Map<String, Object> result = new HashMap<String, Object>();
		// 用于生成重复的号牌号码信息
		List<CarS> list = new ArrayList<CarS>();
		// 保存最终数据(去掉重复数据)
		@SuppressWarnings("unused")
		Set<CarS> set = new HashSet<CarS>();
		try{
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile excelFile = mrequest.getFile("exfilename");
		if (excelFile != null && !excelFile.isEmpty()) {
				InputStream input = excelFile.getInputStream();
				//获取文件名
				String filename=excelFile.getOriginalFilename();
				//保存临时文件到项目里面，做分页查询用.
				if(input!=null){
					File file=new File(request.getSession().getServletContext().getRealPath("/")+"upload");
					if((!file .exists()  && !file .isDirectory())){
						file.mkdir();
					}
					FileOutputStream fs=new FileOutputStream(request.getSession().getServletContext().getRealPath("/")+"/upload/"+filename);
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
				FileInputStream tempinput=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"/upload/"+filename);
			
				
				HSSFWorkbook book = new HSSFWorkbook(tempinput);
				// 默认sheet
				HSSFSheet sheet = book.getSheetAt(0);
				HSSFRow row = sheet.getRow(sheet.getLastRowNum());
				// 行总数
				int rowNum = sheet.getLastRowNum() + 1;
				// 列总数
				int cellNum = (int)row.getLastCellNum();
				
				System.out.println("test:行总数"+rowNum);
				System.out.println("test:列总数"+cellNum);
				String arryid = ArrayIdUtil.getArrayId();
				String methodNames[]=getMethodName();
				for(int i=1;i<rowNum;i++){
					row = sheet.getRow(i);
					CarS veh=new CarS();
					
					veh.setArrayId(arryid);
					for(short j=0;j<cellNum;j++){
						if(row == null){
							continue;
						}
						HSSFCell cell = row.getCell(j);
						//System.out.println(cell);
						if(methodNames!=null&&j<methodNames.length){
							String cellVal = "";
							cellVal = cell.toString().trim();
							String methodName = "set" + methodNames[j];
							//veh.setId(ArrayIdUtil.getId());
							
							
							ClassUtil.invokeMethod(veh, methodName, cellVal);
						}
						 else {
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
				if(list.size() > 0) {
					
					result.put("rows", list);
					result.put("total", list.size());
				}
			} else {
				// 文件为空
				result.put("msg", "文件名为空!");
			}
		
		}
		catch(java.lang.ClassCastException e){
			System.out.println("转换失败");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	
		
		
		
	}
	
	/**
	 * 把上传的数据持久化到本地
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@ResponseBody
	@RequestMapping
	public Object commitAddCars(HttpServletRequest request,HttpServletResponse response ){
		String obj2=request.getParameter("veh");
		Map<String, Object> result = new HashMap<String, Object>();
        SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd hh:mm:ss");
		 
		try{
			 JSONArray arry = JSONArray.fromObject(obj2);
			 for (int i = 0; i < arry.size(); i++)
		        {
		            JSONObject jsonObject = arry.getJSONObject(i);
		            CarS vehinfo= (CarS) jsonObject.toBean(jsonObject, CarS.class);
		            vehinfo.setXrsj( sdf.format(new Date()));
		        	result = (Map<String, Object>) this.zdcarsservice.saveCars(vehinfo);
		            
		        }
		}
		catch(Exception e){
			e.printStackTrace();
		}


		
		return result;
	}
	
	/**
	 * 跳转识别结果页面
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardResult(){
		return new ModelAndView("zdcars/carResultlist");
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping
	public Object resultCarsList(HttpServletRequest request,HttpServletResponse response,Carsresult carresult) throws Exception{
		Map<String ,Object> resultMap=new HashMap<String,Object>();
		List<Carsresult>  str=(List<Carsresult>) this.zdcarsservice.selectCarsResult("arrayid",carresult.getARRAYID(),request);
		JsonConfig jf = new JsonConfig();  
    	//DateJsonValueProcessor d=new DateJsonValueProcessor();
    	 jf.registerJsonValueProcessor(java.sql.Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd"));  
         jf.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));  
         JSONArray arry = JSONArray.fromObject(str,jf);
		
		resultMap.put("total", arry.size());
		resultMap.put("rows", arry);
		return resultMap;
	}
	
	
	/**
	 * 识别结果集的导出，并且记录导出记录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "deprecation" })
	@ResponseBody
	@RequestMapping(params="method=downloadCarsList",method=RequestMethod.POST)
	public void downloadCarsList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Object ars=new String(request.getParameter("veh").getBytes("UTF-8"),"UTF-8");
		int size=ars.toString().length();
		if(size==2){
			  ars= zdcarsdao.selectCarsResult("arrayid",request.getParameter("aid")).get("data");
		}
		// 创建Excel的工作书册 Workbook,对应到一个excel文档   
	    HSSFWorkbook wb = new HSSFWorkbook();   
	    // 创建Excel的工作sheet,对应到一个excel文档的tab   
	    HSSFSheet sheet = wb.createSheet("sheet1");   
	    try{
	    	JsonConfig jf = new JsonConfig();  
	    	//DateJsonValueProcessor d=new DateJsonValueProcessor();
	    	 jf.registerJsonValueProcessor(java.sql.Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd"));  
	         jf.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));  
	         JSONArray arry = JSONArray.fromObject(ars,jf);
			 String methodNames2[]= getMethodName2();
			 //设置excel标头字段行
			 HSSFRow columnRow = sheet.createRow(0); 
			 columnRow.setHeight((short) 500);// 设定行的高度   
			for(int i=0;i<getColumnNames().length;i++){
				//设置单元格宽度
				sheet.setColumnWidth((short)i, (short)5000);
				 //设置excel的标头字段列
	 	    	HSSFCell ColumnCell =columnRow.createCell((short)i);
	 	    	//设置标头样式
	 	    	ColumnCell.setCellStyle(ExcelStyle.excelStyle3(wb));
	 	    	ColumnCell.setCellValue(String.valueOf(getColumnNames()[i]));
			}			
			 for (int i = 1; i < arry.size()+1; i++)
		        {	
		            JSONObject jsonObject = arry.getJSONObject(i-1);
		            Carsresult vehinfo= (Carsresult) jsonObject.toBean(jsonObject, Carsresult.class);
		           // System.out.println(vehinfo);
		        	    HSSFRow row = sheet.createRow(i);   
		        	    row.setHeight((short) 500);// 设定行的高度   
		        	    for(int p=0;p<getMethodName2().length;p++){
		        	    	// 设置excel每列宽度   
			            	sheet.setColumnWidth((short)p, (short)5000);
		        	    	// 创建Excel的单元格   
			        	    HSSFCell cell = row.createCell((short) p);
			        	 // 给Excel的单元格设置样式和赋值   
			        	    String methodName = "get" + methodNames2[p];
			        	    Object  cellVal = ClassUtil.invokegetMethod(vehinfo, methodName);
			        	    if(p%2==0){
			        	    	 cell.setCellStyle(ExcelStyle.excelStyle(wb));   
			        	    }
			        	    else{
			        	    	cell.setCellStyle(ExcelStyle.excelStyle2(wb));   
			        	    }
			        	    cell.setCellValue(String.valueOf(cellVal));   
		        	    }
		         
		        }
			 //对下载内容记录日志文件
			  this.zdcarsservice.downloadResult();
			  	response.setCharacterEncoding("UTF-8");
			    response.reset();
				response.setContentType("application/msexcel");
				response.setHeader("Content-Disposition", "attachment;Filename="+ArrayIdUtil.getTimeId()+new String("车辆图片识别结果".getBytes(), "ISO_8859_1")+".xls");
				OutputStream os = response.getOutputStream();
				wb.write(os);
				os.flush();
				os.close();
	    }
		catch(Exception e){
			e.printStackTrace();
		}
		

			
		
	}
	
	@ResponseBody
	@RequestMapping
	public void getDownModel(HttpServletRequest request, HttpServletResponse response, String filename){
		try {
			FileInputStream	 fis=new FileInputStream(new File(request.getSession().getServletContext().getRealPath("/")+"/download/"+filename+""));
			byte[] b=new byte[fis.available()];
			fis.read(b);
			fis.close();
			response.setContentType("application;charset=UTF-8");   			
			response.setHeader("Content-Disposition", "attachment;Filename="+new String(filename.getBytes("utf-8"), "utf-8"));
			OutputStream os = response.getOutputStream();
			os.write(b, 0, b.length);  
	        os.flush(); 
	        os.close(); 
		}
		catch(Exception e){
			e.toString();
		}
		
	}
	//上传方法名
	public String [] getMethodName(){
		String []methodNames={"Hphm","Hpzl","Hpzb","Gcsj","Sbbh","Kkbh","Fxbh","Cdbh","Zpfx","Zbz",
			"Psly","Sbly","Zp1","Zp2","Zp3","Yxdj"
		};
				return methodNames;
	}
	//下载方法名
	public String [] getMethodName2(){
		String []methodNames={"ID","ARRAYID","GCSJ","CLPP","CLXH","CLNK","CSYS","HPFL","TPYY","HPHM",
			"SQHM","HPZL","ZP1","ZP1","ZP1","CLLX","KDBH","SBBH","FXBH","HPZB","ZYB","CWDM","SFYZ","FKSJ"
		};
		return methodNames;
	}
	//下载的excel字段
	public String [] getColumnNames(){
		String []columnNames={
				"唯一id","批次号" ,"过车时间","品牌","型号","车辆年款","车辆颜色","号牌分类","套牌原因","CarS识别号牌号码","上传的号牌号码","识别号牌种类","图片1","图片2","图片3","CarS识别车辆类型",
				"卡点编号","设备编号","方向编号","号牌坐标","遮阳板是否放下","CarS识别状态代码","卡口号牌和Cars号牌是否一致","反馈时间"
		};
		return columnNames;
	}
	
	public ZdcarsDao getZdcarsdao() {
		return zdcarsdao;
	}

	public void setZdcarsdao(ZdcarsDao zdcarsdao) {
		this.zdcarsdao = zdcarsdao;
	}
	
	
	
	
	
}
