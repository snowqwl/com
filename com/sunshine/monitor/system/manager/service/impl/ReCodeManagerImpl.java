package com.sunshine.monitor.system.manager.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sunshine.core.util.FileUtil;
import com.sunshine.monitor.system.manager.bean.ReCode;
import com.sunshine.monitor.system.manager.dao.ReCodeDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.ReCodeManager;

@Transactional
@Service("reCodeManager")
public class ReCodeManagerImpl implements ReCodeManager {

	@Autowired
	private ReCodeDao reCodeDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	public Map findReCodeForMap(Map filter, ReCode reCode) throws Exception {
		
		
		return reCodeDao.findReCodeForMap(filter,reCode);
	}

	public ReCode getReCodeForYabh(String yabh) throws Exception {
		String bjdmc="";
		ReCode recode=reCodeDao.getReCodeForYabh(yabh);
		String[] bjd=recode.getBjd().split(",");
		for(int i=0;i<bjd.length;i++){
		  bjdmc+=this.systemDao.getDepartmentName(bjd[i]);
		  if(i!=bjd.length-1){
			bjdmc+=",";
		  }
		}
		recode.setBjdmc(bjdmc);
		return recode;
	}

	public int saveRecode( ReCode code) throws Exception{
		
		return reCodeDao.saveReCode(code);
	}
	
	/**
	 * 
	 * 函数功能说明：保存上传文件
	 * 修改日期 	2013-7-24
	 * @param request
	 * @param tempPath :临时文件目录
	 * @param realPath :实际上传目录
	 * @return
	 * @throws Exception    
	 * @return String
	 */
	public String saveFileForReCode(MultipartFile mFile,String realPath,String name)throws Exception{
		
		String fileEnd = mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf(".")+1).toLowerCase();
		
		
		File rFile = new File(realPath + File.separator +name + "." + fileEnd);
		
		FileOutputStream out = null;
		
		try{
			out = new FileOutputStream(rFile);
			out.write(mFile.getBytes());
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(out != null){
				out.close();
			}
		}
		
		rFile.getAbsolutePath();
		
		return "/download/recode/" +name + "." + fileEnd;
		
	}
	public void saveReCodeAndLoadFile(MultipartFile mFile, ReCode code,String realPath)
			throws Exception {
		String path = "";
		
		if(!mFile.isEmpty()){
			
			String name = code.getKdbh()+ "_" + code.getYalx();
			path = saveFileForReCode(mFile,realPath,name);
		}
		
		code.setWdlj(path);
		
		saveRecode(code);
	}

	public int updateRecode(ReCode code)throws Exception{
		
		return reCodeDao.updateReCode(code);
	}
	
	public void updateReCode(MultipartFile mFile,ReCode code, String realPath) throws Exception {
		String path = "";
		if(!mFile.isEmpty()){
			
			String name = code.getKdbh()+ "_" + code.getYalx();
			path = saveFileForReCode(mFile,realPath,name);
		}
		code.setWdlj(path);
		updateRecode(code);
	}

	public List getReCodeListForKdbh(String kdbh) throws Exception {
		
		return reCodeDao.getReCodeListForKdbh(kdbh);
	}

}
