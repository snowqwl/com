package com.sunshine.monitor.comm.video.action;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.maintain.util.FileSystemResource;
import com.sunshine.monitor.comm.video.service.VideoManager;


@Controller
@RequestMapping(value="/video.do",params="method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VideoController {
	
	@Autowired
	private VideoManager videoManager;	
	
	private static String pcServerIP;
	
	private static String pcUserName;
	
	private static String pcPasswd;
	//视频实况
	private String video_main = "video/videomain";
	//历史视频调阅
	private String historical_video_main = "video/historialVideoMain";
	
	static{
		//ClassLoader cl = Thread.currentThread().getContextClassLoader();
		//FileInputStream fis = null;
		try {
			/*fis = new FileInputStream(cl.getResource("video.properties").getPath());
			InputStream in = new BufferedInputStream(fis);
			Properties p = new Properties();        
			p.load(in);
			pcServerIP = p.get("pcServerIP").toString();
			pcUserName = p.get("pcUserName").toString();
			pcPasswd = p.get("pcPasswd").toString();*/
			Properties property = FileSystemResource.getProperty("video.properties");
			Enumeration<Object> enums = property.keys();
			while (enums.hasMoreElements()) {
				String key = (String) enums.nextElement();
				if (key!=null&&key.startsWith("pcServerIP"))
					pcServerIP = property.getProperty(key);
					
				if (key!=null&&key.startsWith("pcUserName"))
					pcUserName = property.getProperty(key);
				
				if (key!=null&&key.startsWith("pcPasswd"))
					pcPasswd = property.getProperty(key);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	  * 进入视频实况的页面
	  * @return
	  */
	@RequestMapping
	 public ModelAndView forwardVideoMain(HttpServletRequest request,HttpServletResponse response){
			ModelAndView mv = new ModelAndView();
			//List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			//List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>(); 
		 try {
			 request.setAttribute("pcServerIP",pcServerIP);
			 request.setAttribute("pcUserName",pcUserName);
			 request.setAttribute("pcPasswd",pcPasswd);
			/* list = this.videoManager.getCameraTree();//取得摄像机上级部门结构，需加入摄像机信息
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
			 request.setAttribute("menuNodes",JSONArray.fromObject(menuNodes).toString());*/
			 mv.setViewName(video_main);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return mv;
	 }
	
	
	/**
	  * 进入历史视频调阅的页面
	  * @return
	  */
	@RequestMapping
	 public ModelAndView forwardHistorialVideoMain(HttpServletRequest request,HttpServletResponse response){
			ModelAndView mv = new ModelAndView();
			//List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			//List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>(); 
		 try {
			 request.setAttribute("pcServerIP",pcServerIP);
			 request.setAttribute("pcUserName",pcUserName);
			 request.setAttribute("pcPasswd",pcPasswd);
			/* list = this.videoManager.getCameraTree();//取得摄像机上级部门结构，需加入摄像机信息
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
			 request.setAttribute("menuNodes",JSONArray.fromObject(menuNodes).toString());*/
			 mv.setViewName(historical_video_main);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return mv;
	 }
	 
	
	
	/**
	 * 根据条件展示机构菜单树_异步
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=getCameraTree")
	@ResponseBody
	public List<Map<String,Object>> getCameraTree(HttpServletRequest request) {
		String id = request.getParameter("id");
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		List list;
		boolean isParent = false;
		try {
			list = this.videoManager.getCameraTreeAsync(id);
			if(list.size()>0) {
				Iterator<Map<String,Object>> it = list.iterator();
				while(it.hasNext()){
					Map<String,Object> m = it.next();					
					Map<String,Object> item = new HashMap<String,Object>(); 					
					if(m.get("id")!=null) {	
					if(m.get("id").toString().length()<=6){
					  isParent = this.videoManager.getCountFromCamera(m.get("id").toString());
					}
					  item.put("id", m.get("id").toString());
					  item.put("name", m.get("idname").toString());
					  item.put("pId", m.get("pid").toString());
					  item.put("isParent", isParent);
				      menuNodes.add(item);
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return menuNodes;
	}
	
	
	/**
	 * 解析HTML文件，将控件返回的错误码翻译
	 */
	@RequestMapping(params = "method=htmlParser")
	@ResponseBody
	public String htmlParser(HttpServletRequest request) {	
		String parseStr = "";
		try {			   
				String errCode = request.getParameter("errCode");
				ClassLoader cl = Thread.currentThread().getContextClassLoader();
				File input = new File(cl.getResource("").getPath()+"/errCode.html");
				Document doc=Jsoup.parse(input, "UTF-8");
			    Elements tr = doc.getElementsByTag("tr");			    
			    for(Element row : tr){
			    	if(errCode.equals(row.child(0).text())){
			    		parseStr = row.child(2).text();
			    	}
			    }
			    if(parseStr==""){
			    	parseStr = "错误码："+errCode;
			    }
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return parseStr;
	}
	
	
	
	  /*@RequestMapping(params = "method=getCameraTree")
		@ResponseBody
		public List<Map<String,Object>> getCameraTree(HttpServletRequest request) throws Exception {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
			String CameraJsonStr = "";
			CameraJsonStr = URLDecoder.decode(request.getParameter("CameraJsonStr"), "UTF-8");
			try {						
					list = this.videoManager.getCameraTree();//取得摄像机上级部门结构，需加入摄像机信息	
					Map<String,Object> jsonmap = null;
					if(CameraJsonStr!=null&&CameraJsonStr!=""){
						JSONArray arry = JSONArray.fromObject(CameraJsonStr);
						for(int i=0;i<arry.size();i++){				
							JSONObject con = arry.getJSONObject(i);
							jsonmap.put("id",con.getString("CameraCode"));
							jsonmap.put("idname",con.getString("CameraName"));
							jsonmap.put("pid",con.getString("CameraCode")!=null?con.getString("CameraCode".substring(0,6).toString()):"");
							list.add(jsonmap);
						}
					}
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
		}*/
}
