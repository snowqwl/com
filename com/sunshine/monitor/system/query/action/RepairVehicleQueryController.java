package com.sunshine.monitor.system.query.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.service.RepairVehicleQueryService;

/**
 * 机修车辆查询
 * @author admin
 *
 */
@Controller
@RequestMapping(value="repairVehicleQuery.do",params="method")
public class RepairVehicleQueryController {
	
	private Logger logger=LoggerFactory.getLogger(RepairVehicleQueryController.class);
	
	@Autowired
	private SystemManager systemManager;
	
	@Autowired
	private RepairVehicleQueryService repairVehicleQueryService;
	
	private String repairVehicleQueryMain="repair/repairVehicleList";
	private String repairVehicleDetail="repair/detail";

	/**
	 * 跳转到机修车查询主页
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardMain() {
		ModelAndView model = new ModelAndView();
		Map<String, List<Code>> map = null;
		try {
			map = new HashMap<String, List<Code>>();
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			
			map.put("hpzlList", hpzlList);
			model.addObject("codemap", map);
		} catch (Exception e) {
			logger.error("跳转机修车查询主页时发生异常："+e.getMessage());
			e.printStackTrace();
		}
		model.setViewName(repairVehicleQueryMain);
		return model;
	}
	
	/**
	 * 机修车查询列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Map<String,Object> queryList(HttpServletRequest request){
		Map<String, Object> params=null;
		Map<String, Object> map=null;
		try {
			params = Common.getParamentersMap(request);
			map = repairVehicleQueryService.list(params);
		} catch (Exception e) {
			logger.error("查询机修车列表出错："+e.getMessage());
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 获取机修车详情
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping
	public ModelAndView getDetail(HttpServletRequest request){
		ModelAndView model = new ModelAndView(repairVehicleDetail);
		Map<String, Object> params=null;
		Map<String, Object> repairMap=null;
		Map<String, Object> jdcMap=null;
		String resultMsg="ok";
		try {
			params = Common.getParamentersMap(request);
			String djcode=(String) params.get("djcode");
			if(StringUtils.isBlank(djcode)) {
				throw new Exception("登记编号不能为空！");
			}
			Map<String, Object> map = repairVehicleQueryService.detail(params);
			repairMap =(Map<String, Object>) map.get("repairMap");
			jdcMap =(Map<String, Object>) map.get("jdcMap");
		} catch (Exception e) {
			resultMsg=e.getMessage();
			e.printStackTrace();
			logger.error("获取机修车详情异常："+e.getMessage());
		}
		model.addObject("repairMap",repairMap);
		model.addObject("jdcMap",jdcMap);
		model.addObject("resultMsg",resultMsg);
		return model;
	}
	
	@RequestMapping
	public void exportData2(HttpServletRequest request,HttpServletResponse response) {
		String[] columnName = new String[] { "收车时间", "车牌号码", "送修人", "车主姓名",
				"收车单位", "所属区域" };
		short[] columnWidth = new short[] { 8000,  3000, 3000, 3000,
				10000, 5000 };
		String[] codeGateMethod = new String[] { "JSTIME", "CLPH", "SXRXM",
				"SYR", "DWNAME", "XIAN_GONGAN_NAME" };

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
			Map<String,Object> params = new HashMap<String,Object>();
			params = Common.getParamentersMap(request);
			Map<String, Object> map = repairVehicleQueryService.getListXls(params);
			List<Map<String, Object>> gates=(List<Map<String, Object>>) map.get("rows");
			
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
						cell.setCellValue(s.toString());
					}
					cell.setCellStyle(excelStyle5);
					sheet.addMergedRegion(new Region(rownum, (short) i,lastrow, (short) i));
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
					+ new String("机动车维修信息".getBytes("GBK"),"ISO8859-1") + ".xls");
			ServletOutputStream stream = response.getOutputStream();
			book.write(stream);
			stream.flush();
			stream.close();
		}catch(Exception e){
			logger.error("导出机修车列表异常："+e.getMessage());
			e.printStackTrace();
		}
	}
	
}
