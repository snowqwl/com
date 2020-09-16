package com.sunshine.monitor.system.manager.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.sunshine.monitor.system.manager.bean.ReCode;

public interface ReCodeManager {

	public Map findReCodeForMap(Map filter, ReCode reCode)throws Exception;

	public ReCode getReCodeForYabh(String yabh)throws Exception;

	public int saveRecode( ReCode code) throws Exception;
	
	public int updateRecode(ReCode code)throws Exception;
	
	public String saveFileForReCode(MultipartFile mFile,String realPath,String name)throws Exception;
	
	public void saveReCodeAndLoadFile(MultipartFile mFile, ReCode code,String realPath) throws Exception;

	public void updateReCode(MultipartFile mFile,ReCode code, String realPath)throws Exception;

	public List getReCodeListForKdbh(String kdbh)throws Exception;

}
