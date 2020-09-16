package com.sunshine.monitor.system.query.action;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.BusinessType;
import com.sunshine.monitor.comm.util.DispatchedRangeType;
import com.sunshine.monitor.comm.util.FuzzySusp;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.bean.ScrapAnnual;

import com.sunshine.monitor.system.query.service.ScrapAnnualQueryManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;



@Controller
@RequestMapping(value="/scrapAnnualQueryCtrl.do",params="method")
public class ScrapAnnualQueryCtrl {

	@Autowired
	private SystemManager systemManager;	
	
	@Autowired
	private SuspinfoManager suspinManager;
	
	@Autowired(required=true)
	private ScrapAnnualQueryManager scrapAnnualQueryManager;
	
	/**
	 * 跳转报废未年检查询主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request){
		
		try {			
			request.setAttribute("hpzlList", this.systemManager
					.getCodes("030107"));
			request.setAttribute("cllxList", this.systemManager
					.getCodes("030104"));
			request.setAttribute("bkdlList", this.systemManager
					.getCodes("120019"));
			request.setAttribute("bklbList", this.systemManager
					.getCodes("120005"));
			request.setAttribute("ywztList", this.systemManager
					.getCodes("120008"));
			request.setAttribute("bjfsList", this.systemManager
					.getCodes("130000"));
			
			request.setAttribute("hpysList", this.systemManager
					.getCodes("031001"));
			
			request.setAttribute("csysList", this.systemManager
					.getCodes("030108"));						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "query/scrapannualmain";
	}
	
	/**
	 * 
	 * 函数功能说明：报废、未年检车辆查询
	 * @param page
	 * @param rows
	 * @param info
	 * @param request
	 * @return    
	 * @return Map
	 */
	@RequestMapping
	@ResponseBody
	public Map queryList(String page, String rows,ScrapAnnual info,HttpServletRequest request){
		Map map  = null;
		Map filter = new HashMap();
		filter.put("page", page);	//页数
		filter.put("rows", rows);	//每页显示行数
		try {
		
			map = scrapAnnualQueryManager.getMapForScrapAnnual(filter, info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map ;
		
	}
	
	
	/**
	 * 报废、未年检车辆确认布控
	 * @param request
	 * @param response
	 * @param veh
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object commitScrapAnnualList(HttpServletRequest request,HttpServletResponse response,VehSuspinfo veh,Map<Object,Object> map,ScrapAnnual info){
		Map<String, Object> result = new HashMap<String, Object>();
		VehSuspinfopic vspic=null;
		try {
			 vspic = this.getPictureBytes(request);
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile picObj = mrequest.getFile("picVal");
		double picSize = (double)200/(double)1024;
			if(!this.isOverRange(picObj, picSize)){
				result.put("code", "0");
				result.put("msg", "案情图片大小请勿超过200KB!");
				return result;
			}
		
		
		MultipartFile clbksqbObj = mrequest.getFile("clbksqb");
		double clbksqbSize = 1;
		if(!this.isOverRange(clbksqbObj, clbksqbSize)){
			result.put("code", "0");
			result.put("msg", "车辆布控申请表大小请勿超过1M!");
			return result;
		}
		
		MultipartFile lajdsObj = mrequest.getFile("lajds");
		double lajdsSize = 1;
		if(!this.isOverRange(lajdsObj, lajdsSize)){
			result.put("code", "0");
			result.put("msg", "立案决定书大小请勿超过1M!");
			return result;
		}
		
		MultipartFile yjcnsObj = mrequest.getFile("yjcns");
		double yjcnsSize = 1;
		if(!this.isOverRange(yjcnsObj, yjcnsSize)){
			result.put("code", "0");
			result.put("msg", "移交承诺书大小请勿超过1M!");
			return result;
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String obj2=request.getParameter("veh");
		String conditions=request.getParameter("conditions");
		 //System.out.println("json字符串内容如下");
	     //System.out.println(obj2);
		try{
			 JSONArray arry = JSONArray.fromObject(obj2);
			 JSONArray cond = JSONArray.fromObject(conditions);
			 //统计失败记录数和具体原因
			 int count =0;
			 String flag[] =new String[arry.size()];
			 for (int i = 0; i < arry.size(); i++)
		        {
		            JSONObject jsonObject = arry.getJSONObject(i);	//hphm信息		          		         
		            for(int j = 0;j<cond.size();j++){
		 			   JSONObject condjsonObject = cond.getJSONObject(j);
		 			   jsonObject.put(condjsonObject.getString("name"),condjsonObject.getString("value"));			   			   
		 			   }
		            JSONObject jsonObject1 = new JSONObject();	
		            Iterator iter = jsonObject.keys();
		            while(iter.hasNext()){
			            String keystr = iter.next().toString();
			            String key	= keystr.toLowerCase();
			            String value = jsonObject.getString(keystr);
			            jsonObject1.put(key, value);
		            } 
	
					VehSuspinfo vehinfo= (VehSuspinfo) jsonObject.toBean(jsonObject1, VehSuspinfo.class);	
					boolean b=false;
					if(jsonObject1.get("isldbk")!=null&&jsonObject1.get("isldbk").toString().equals("1")){
						b=true;
					}
		        	VehSuspinfo dveh = this.getVehSuspinfoFromParameters(vehinfo, request,false, b);
		        	if(dveh.getBkdl()!=null){
		        		if(dveh.getBkdl().equals("3")){
		        			dveh.setBkxz("0");
		        		}
		        		else
		        			dveh.setBkxz("1");
		        		if(dveh.getBkjglxdh()==null||dveh.getBkjglxdh().equals("")){
		        			dveh.setBkjglxdh("");
		        		}
		        	}
		        	flag[i]=this.suspinManager.checkSuspinfo(dveh.getHphm(), dveh.getBkdl());
		        	if(flag[i].equals("")){
		        	result = (Map<String, Object>) this.suspinManager.saveAutoVehSuspinfo(dveh, vspic);
		        	}
		        	else{
		        		count ++; 
		        	}
		        	
		        }
			 result.put("faile", count);
			 result.put("faileMsg", flag);
		}
		
		catch(Exception e){
			e.printStackTrace();
		}


		
		return result;
	}
	/**
	 * 保存布控参数值封装
	 * @param request
	 * @param flag 模糊布控标志
	 * @param isldbk 联动布控标志
	 * @return
	 * @throws Exception
	 */
	private VehSuspinfo getVehSuspinfoFromParameters(VehSuspinfo veh,
			HttpServletRequest request, boolean flag, boolean isldbk) throws Exception {
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
			boolean sfks = false; //是否为跨省联动布控
			String[] bkfws = veh.getBkfw().split(",");
			for(String bkfw : bkfws){
				if(bkfw.startsWith("44")) {
					sfks = true;
					break;
				}
			}
			if(sfks)
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
	
	/**
	 * 验证图片大小
	 */
	private boolean isOverRange(MultipartFile picobj, double size) throws Exception{
		boolean isOverRange = true;
		if(picobj != null && !picobj.isEmpty()){
			if(picobj.getSize()>size*1024*1024){
				isOverRange = false;
			}
		}
		return isOverRange;
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
		VehSuspinfopic vspic = new VehSuspinfopic();
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile picobj = mrequest.getFile("picVal");
		MultipartFile clbksqbobj = mrequest.getFile("clbksqb");
		MultipartFile lajdsobj = mrequest.getFile("lajds");
		MultipartFile yjcnsobj = mrequest.getFile("yjcns");
		try {
			if (picobj != null && !picobj.isEmpty()){
				pic = picobj.getBytes();
				String fileName = picobj.getOriginalFilename();
				vspic.setZjwj(pic);
				vspic.setZjlx(fileName);
			}
			if(clbksqbobj != null && !clbksqbobj.isEmpty()){
				clbksqb = clbksqbobj.getBytes();
				String bksqbName = clbksqbobj.getOriginalFilename();
				vspic.setClbksqb(clbksqb);
				vspic.setClbksqblj(bksqbName);
			}
			if(lajdsobj != null && !lajdsobj.isEmpty()){
				lajds = lajdsobj.getBytes();
				String lajdsName = lajdsobj.getOriginalFilename();
				vspic.setLajds(lajds);
				vspic.setLajdslj(lajdsName);
			}
			if(yjcnsobj != null && !yjcnsobj.isEmpty()){
				yjcns = yjcnsobj.getBytes();
				String yjcnsName = yjcnsobj.getOriginalFilename();
				vspic.setYjcns(yjcns);
				vspic.setYjcnslj(yjcnsName);
			}
			if(picobj == null && clbksqbobj == null && lajdsobj == null && yjcnsobj == null){
				return null ;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vspic;
	}

}
