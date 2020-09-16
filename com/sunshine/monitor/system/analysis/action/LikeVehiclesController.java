package com.sunshine.monitor.system.analysis.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.comm.vehicle.tran.CSYSTranslation;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.analysis.service.LikeVehiclesManager;
import com.sunshine.monitor.system.analysis.service.PeerVehiclesManager;
import com.sunshine.monitor.system.analysis.service.VehicleManager;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Controller
@RequestMapping(value = "/likeVeh.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LikeVehiclesController {

	@Autowired
	private SystemManager systemManager;
	@Autowired
	private LikeVehiclesManager likevehiclesManager;
	
	@Autowired
	@Qualifier("vehicleManager")
	private VehicleManager vehicleManager;
	
	@Autowired
	@Qualifier("peerManager")
	private PeerVehiclesManager peerManager;
	
	@Autowired
	private SystemDao systemDao;
	
	@Autowired
	private GateDao gateDao;
	
	
	@SuppressWarnings("unchecked")
	private static Map getCodeMap = null;
	
	protected Logger debugLogger = LoggerFactory.getLogger(LikeVehiclesController.class);
	/**
	 * 初始化页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public String findForward(HttpServletRequest request,
			HttpServletResponse response) {
		try {
		      
			//号牌颜色集合
			request.setAttribute("hpysList", this.systemManager.getCodes("031001"));
			//车辆颜色集合
			request.setAttribute("csysList", this.systemManager.getCode("030108"));
			//号牌种类集合
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));

		} catch (Exception e) {
			debugLogger.warn("模糊查询主页跳转", e);
			request.setAttribute("exception", e);
		}

		return "analysis/likevehiclesmain";
	}
	
	/**
	 * 初始化页面车辆综合查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public String findCarForward(HttpServletRequest request,
			HttpServletResponse response) {
		try {
		      
			//号牌颜色集合
			request.setAttribute("hpysList", this.systemManager.getCodes("031001"));
			//车辆颜色集合
			request.setAttribute("csysList", this.systemManager.getCode("030108"));
			//号牌种类集合
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));

		} catch (Exception e) {
			debugLogger.warn("车辆综合查询页面跳转", e);
			request.setAttribute("exception", e);
		}
		return "analysis/CarTrackQuery";
	}

	/**
	 * 按条件获取号牌号码和过车数
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param veh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public List<Map<String, Object>> queryForLike(HttpServletRequest request,
			HttpServletResponse response, String page, String rows,
			VehPassrec veh,String sign) {
		List<Map<String, Object>> list = null;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		String hphm = veh.getHphm().replace("?", "_").replace("？", "_");
		veh.setHphm(hphm);
				
		try {
			list = likevehiclesManager.queryForLikeExt(veh, filter);
		} catch (Exception e) {
			debugLogger.warn("根据条件模糊查询", e);
		}
		return list;
	}

	/**
	 * 按条件获取记录总数
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param veh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Integer queryForLikeTotal(HttpServletRequest request,
			HttpServletResponse response, String page, String rows,
			VehPassrec veh) {
		int total = 0;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		String hphm = veh.getHphm().replace("?", "_").replace("？", "_");
		try {
			hphm = URLDecoder.decode(hphm, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		veh.setHphm(hphm);
				
		try {
			total = likevehiclesManager.getForLikeTotal(veh, filter);
		} catch (Exception e) {
			debugLogger.warn("根据条件模糊查询", e);
		}
		return total;
	}

	@RequestMapping
	@ResponseBody
	public Map<String ,Object> queryCarTrack(HttpServletRequest request,HttpServletResponse response,VehPassrec veh,String rows,String page,String sign){
		Map<String, Object> map = null;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("sessionid", request.getSession().getId().replace("-","_"));
		String hphm = veh.getHphm().replace("?", "_").replace("？", "_");
		veh.setHphm(hphm);
		try {
			if(Integer.parseInt(sign)==0){
			map = likevehiclesManager.queryCarTrack(veh, filter);
			map.put("pageSign", "0");
			}
			else{
				map=likevehiclesManager.queryCarTrackPage(filter);
				map.put("pageSign", "1");
			}
		} catch (Exception e) {
			debugLogger.warn("车辆综合查询轨迹查询--天云星", e);
		}
		return map;
	}
	
	/**
	 * 判断跳转到轨迹列表还是轨迹视图
	 * 
	 * @param request
	 * @param response
	 * @param veh
	 * @param method
	 * @return
	 */
	@RequestMapping
	public String queryForLikeList(HttpServletRequest request,
			HttpServletResponse response, VehPassrec veh, String winContrl) {
		String returnWin = "";
		try {
			if (winContrl.equals("scanList")) {
				returnWin = "analysis/likevehiclesList";
			} else if (winContrl.equals("scanPic")) {
				returnWin = "analysis/likevehiclesPic";
			}
			//传转义集合
			request.setAttribute("ByCodeMap", getCodeMap);
			request.setAttribute("page", "1");
			request.setAttribute("rows", "6");
			request.setAttribute("hphm", java.net.URLDecoder.decode(veh
					.getHphm(), "UTF-8"));
			request.setAttribute("hpys", java.net.URLDecoder.decode(veh
					.getHpys(), "UTF-8"));
			request.setAttribute("hpzl", java.net.URLDecoder.decode(veh
					.getHpzl(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			debugLogger.warn("车辆列表页面跳转", e);
		}
		request.setAttribute("kdbh", veh.getKdbh());
		request.setAttribute("kssj", veh.kssj);
		request.setAttribute("jssj", veh.jssj);
		return returnWin;
	}

	/**
	 * //根据号牌号码和部分条件获取过车的轨迹列表
	 * 
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @return
	 */

	@RequestMapping
	@ResponseBody
	public Map<String, Object> queryForLikePageList(HttpServletRequest request,
			HttpServletResponse response, VehPassrec veh, String page,
			String rows) {
		Map<String, Object> map = null;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);

		try {

			veh.setHphm(java.net.URLDecoder.decode(veh.getHphm(), "UTF-8"));
			map = likevehiclesManager.queryForLikeList(veh, filter);
		} catch (Exception e) {
			debugLogger.warn("车辆列表分页查询", e);
		}
		return map;
	}

	/**
	 * 根据号牌号码和部分条件获取过车的轨迹视图
	 * 
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String, Object> queryForLikePagePic(HttpServletRequest request,
			HttpServletResponse response, VehPassrec veh, String page,
			String rows) {
		Map<String, Object> map = null;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		try {
			veh.setHphm(java.net.URLDecoder.decode(veh.getHphm(), "UTF-8"));
			map = likevehiclesManager.queryForLikePic(veh, filter);
		} catch (UnsupportedEncodingException e) {
			debugLogger.warn("车辆视图分页查询", e);
		} catch (Exception e) {
			debugLogger.warn("车辆视图分页查询", e);
		}

		return map;
	}
	/**
	 * 根据号牌号码和部分条件获取最新的一条过车详细
	 * @param request
	 * @param response
	 * @param veh
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping
	@ResponseBody
	public Map queryForLikeLast(HttpServletRequest request,HttpServletResponse response ,VehPassrec veh){
		Map lastPassMap=null;
		//UserSession session = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
		//SysUser user = session.getSysuser();
		//ProvincialRBSPHandle pHandle = new ProvincialRBSPHandle();
		//JSONObject jsonObj =null;
		Map send=new HashMap();
		@SuppressWarnings("unused")
		String hpzl="";
		try {
			//根据条件得到最后的一条过车信息
			 lastPassMap=this.likevehiclesManager.queryForLikeLast(veh);
			 if(lastPassMap!=null){
			 lastPassMap.put("HPYS", systemManager.getCodeValue("031001",(String)lastPassMap.get("HPYS")));
			 lastPassMap.put("KDMC", this.gateDao.getOldGateName(lastPassMap.get("KDBH").toString()));
			 lastPassMap.put("XSZTMC",this.systemDao.getDistrictNameByXzqh(lastPassMap.get("XZQH").toString()));			 			
			 if(lastPassMap.get("HPZL")!=null){
				 hpzl=lastPassMap.get("HPZL").toString();
			 }
			 }
			 send.put("hphm", veh.getHphm());
			 if(veh.getHphm().matches("^[\u4e00-\u9fa5|WJ]{1}[A-Z0-9]{6}$")){
					VehicleEntity cnd = new VehicleEntity();
				 	cnd.setHphm(veh.getHphm().substring(1,veh.getHphm().length()));
				 	cnd.setHpzl(veh.getHpzl());
					cnd.setFzjg(veh.getHphm().substring(0,2));
			        VehicleEntity entity = this.vehicleManager.getVehicleInfoGreenplum(cnd);
			        if(entity!=null){
			        	CSYSTranslation csysTranslation = new CSYSTranslation("030108");
			        	lastPassMap.put("clxh",(entity.getClxh()!=null)?entity.getClxh():"");
			        	lastPassMap.put("syr", (entity.getSyr()!=null)?entity.getSyr():"");
			        	lastPassMap.put("lxdh",(entity.getLxdh()!=null)?entity.getLxdh():"");
			        	lastPassMap.put("clpp",(entity.getClpp1()!=null)?entity.getClpp1():"");		        	
			        	lastPassMap.put("csysmc",csysTranslation.getMC(entity.getCsys()));
			        	if(entity.getZp()!=null&&entity.getZp().length()>0){
			        		lastPassMap.put("zp",entity.getZp());
			        	}
			        	lastPassMap.put("sign","1");
			        }
			 }
			 /**
				jsonObj= pHandle.findJson(user, send, hpzl);
				if(jsonObj.get("result").toString().equals("1")){
					//车辆型号
					lastPassMap.put("clxh",jsonObj.get("clxh").toString() );
					//所有人
					lastPassMap.put("syr", java.net.URLDecoder.decode(jsonObj.get("syr").toString(),"utf-8"));
					//联系电话
					lastPassMap.put("lxdh", jsonObj.get("lxdh").toString());
					//车辆品牌
					lastPassMap.put("clpp", java.net.URLDecoder.decode(jsonObj.get("clpp").toString(),"utf-8"));
					//车神颜色
					lastPassMap.put("csysmc", jsonObj.get("csysmc").toString());
					//标记
					lastPassMap.put("sign", "1");
				}
				**/
				
		} catch (Exception e) {
			debugLogger.warn("查询最后一次过车详情", e);

		}
		return lastPassMap;
	}
	
	/**
	 * pgis结果集合
	 * @param request
	 * @param response
	 * @param veh
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping
	@ResponseBody
	public Map getContrailList(HttpServletRequest request,
			HttpServletResponse response, ScsVehPassrec veh) {
		Map result = null;
		try {
			result = this.peerManager.getContrailList(Constant.SCS_TEMP_PREFIX+"_LikeCarTrack_"+(request.getSession().getId().replace("-","_")));
		} catch (Exception e) {
			debugLogger.warn("pgis结果集合", e);
		}
		return result;
	}
	
	 /** 
	 * 导出模糊车牌分析的Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getCaseInfoXls(HttpServletRequest request,HttpServletResponse response, String page, String rows,VehPassrec veh,String sign) {
		String[] headName = new String[]{"号牌号码", "号牌颜色", "号牌种类", "过车次数", "违法信息关联"};
		short[] columnWidth = new short[]{3000,5000,5000,9000,3000,3000,3000,9000,12000,3000,3000,6000}; 
		String[] method = new String[]{"hphm","hpysmc","hpzlmc","gccs","wfxx"};
		String name = "模糊车牌分析";
		try {			
			List<Map<String,Object>> infos= this.queryForLike(request, response, page, rows, veh, sign);
			exportCommon.exportExecl(infos, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
