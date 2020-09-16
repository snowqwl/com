package com.sunshine.monitor.system.manager.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sunshine.core.util.FileUtil;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.ReCode;
import com.sunshine.monitor.system.manager.service.ReCodeManager;

@Controller
@RequestMapping(value = "/recode.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReCodeCtroller {

	@Autowired
	private GateManager gateManager;
	
	@Autowired
	private ReCodeManager reCodeManager;
	
	@RequestMapping
	public String findForward(HttpServletRequest request){
		
		try {
			request.setAttribute("departmentList", this.gateManager
					.getDepartmentsByKdbh());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "manager/recodelist";
	}
	
	@RequestMapping
	@ResponseBody
	public Map queryList(String page, String rows,ReCode reCode){

		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		
		Map map = null;
		try{
			map = reCodeManager.findReCodeForMap(filter,reCode);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	
	}
	
	@RequestMapping
	@ResponseBody
	public Object getReCode(String yabh){
		ReCode code = null;
		
		try {
			code = reCodeManager.getReCodeForYabh(yabh);
			code.setGlbm("0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}
	
	@RequestMapping
	@ResponseBody
	public Object getDepartments(){
		List list=null;
		try {
			list=this.gateManager.getDepartmentsByKdbh();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping
	@ResponseBody
	public Object getReCodeListForKdbh(String kdbh){
		List list = null;
		
		try {
			list = reCodeManager.getReCodeListForKdbh(kdbh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
		
	}
	
	@RequestMapping
	@ResponseBody
	public Object saveReCode(HttpServletRequest req,ReCode code) throws Exception {
		
		Map map = null;
		
		String realPath = getLocalFileDirRealPath(req);
		
		MultipartHttpServletRequest request = (MultipartHttpServletRequest)req;
		
		FileUtil.createFolderNotExists(realPath);
		
		MultipartFile mFile = request.getFile("file");
		
		try {
			reCodeManager.saveReCodeAndLoadFile(mFile,code,realPath);
			
			map = Common.messageBox("保存成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			map = Common.messageBox("程序异常！", "0");
		}
		return map;
	}
	
	public static String getLocalFileDirRealPath(HttpServletRequest req) throws Exception {
		String home = FileUtil.getFileHomeRealPath();
		return home+"/download/recode";
	}
	@RequestMapping
	@ResponseBody
	public Object updateReCode(HttpServletRequest req,ReCode code) throws Exception{
		Map map = null;
		
		if(StringUtils.isBlank(code.getYabh() )){
			
			return Common.messageBox("参数传输异常！", "0");
		}
		
		;
		String realPath = getLocalFileDirRealPath(req);//req.getSession().getServletContext().getRealPath("download/recode");
		MultipartHttpServletRequest request = (MultipartHttpServletRequest)req;
		
		FileUtil.createFolderNotExists(realPath);
		
		MultipartFile mFile = request.getFile("file");
		try {
			reCodeManager.updateReCode(mFile,code,realPath);
			
			map = Common.messageBox("更新成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			map = Common.messageBox("程序异常！", "0");
		}
		
		return map;
	}
}
