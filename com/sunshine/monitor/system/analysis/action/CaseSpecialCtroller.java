package com.sunshine.monitor.system.analysis.action;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunshine.monitor.system.analysis.bean.CaseGroupEntity;
import com.sunshine.monitor.system.analysis.bean.CaseInfo;
import com.sunshine.monitor.system.analysis.bean.CaseSpecial;
import com.sunshine.monitor.system.analysis.bean.ExemplaryCaseInfor;
import com.sunshine.monitor.system.analysis.bean.PoliceInfor;
import com.sunshine.monitor.system.analysis.service.CaseSpecialManager;


/**
 * （专题库）研判分析
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/caseSpecial.do",params="method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CaseSpecialCtroller {
	
	  private static final String DOUBTFULINFO = "analysis/doubtfulInfoMain";       	//可疑信息库主页面
	  private static final String POLICEINFO = "analysis/policeInfoMain";           	//警情信息库主页面
	  private static final String CASEINFO = "analysis/caseInfoMain";               	//案件信息库主页面
	  private static final String EXEMPLARCASEINFO = "analysis/exemplaryCaseMain";   	//专案信息库主页面
	  private static final String quicksearch = "analysis/quicksearch";   	    //快速查询主页面	 

	  @Autowired
	  @Qualifier("caseSpecialManager")
	  private CaseSpecialManager caseSpecialManager;
	  
    /**
     * 跳转可疑信息库主页面
     * @param request
     * @param response
     * @return
     */
	@RequestMapping()
	public String forwardDoubtfulInfor(HttpServletRequest request,HttpServletResponse response) {
		return  DOUBTFULINFO;
	}
	
	
	/**
	 * 分页查询:可疑信息库
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map<String,Object> queryDoubtfulInforList(HttpServletRequest request,
			HttpServletResponse response,String page,String rows) {
		Map<String,Object> result = null;
		Map<String,Object> fitler = new HashMap<String,Object>();
		fitler.put("page",page);
		fitler.put("rows",rows);
		try {
			result = this.caseSpecialManager.findCaseByPage(fitler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 分页查询:警情信息库
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map<String,Object> queryPoliceInforList(HttpServletRequest request,
			HttpServletResponse response,String page,String rows,String jqh){
		Map<String,Object> result = null;
		Map<String,Object> fitler = new HashMap<String,Object>();
		fitler.put("page",page);
		fitler.put("rows",rows);
		fitler.put("jqh", jqh);
		try {
			result = this.caseSpecialManager.findPoliceByPage(fitler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 分页查询:案件信息库
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map<String,Object> queryCaseInforList(HttpServletRequest request,
			HttpServletResponse response,String page,String rows,String ajzyxh){
		Map<String,Object> result = null;
		Map<String,Object> fitler = new HashMap<String,Object>();
		fitler.put("page",page);
		fitler.put("rows",rows);
		fitler.put("ajzyxh", ajzyxh);
		try {
			result = this.caseSpecialManager.findCaseInfoByPage(fitler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 分页查询:专案信息库
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map<String,Object> queryExemplaryCaseList(HttpServletRequest request,
			HttpServletResponse response,String page,String rows,String ztzyxh){
		Map<String,Object> result = null;
		Map<String,Object> fitler = new HashMap<String,Object>();
		fitler.put("page",page);
		fitler.put("rows",rows);
		fitler.put("ztzyxh", ztzyxh);
		try {
			result = this.caseSpecialManager.findExemplaryCaseByPage(fitler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String,Object> getCaseInfoDoubtfulExemplaryInfo(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		List<PoliceInfor> policeList = null;
		List<CaseInfo> caseList = null;
		List<CaseGroupEntity> exemplaryList = null;
		try {
			policeList = this.caseSpecialManager.getPoliceList();
			caseList = this.caseSpecialManager.getCaseInfoList();
			exemplaryList = this.caseSpecialManager.getExemplaryCaseList();
			result.put("policeInfoList", policeList);
			result.put("caseInfoList", caseList);
			result.put("exemplaryInfoList", exemplaryList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	     return result;
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String,Object> saveCaseInfoDoubtFulExceplaryInfo(HttpServletRequest request,
			HttpServletResponse response,String zazyxh,String ajzyxh,String jqh,String kyzyxh) {
		Map<String,Object> result = new HashMap<String, Object>();
		String []zazyxhArr = null;
		String []ajzyxhArr = null;
		String []jqhArr = null;
		String []kyzyxhArr = null;
		int code = 0;
		if (zazyxh != null && !"".equals(zazyxh)) {
			zazyxhArr = zazyxh.split(",");
		}
		if (ajzyxh != null && !"".equals(ajzyxh)) {
			ajzyxhArr = ajzyxh.split(",");
		}
		if (jqh != null && !"".equals(jqh)) {
			jqhArr = jqh.split(",");
		}
		if (kyzyxh != null && !"".equals(kyzyxh)) {
			kyzyxhArr = kyzyxh.split(",");
		}
		try {
			code = this.caseSpecialManager.saveCaseDoubtfulExceplary(zazyxhArr, ajzyxhArr, jqhArr, kyzyxhArr);
			if (code > 0) {
				result.put("msg", "保存成功!");
			} else {
				result.put("msg", "保存失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	@RequestMapping
	@ResponseBody
	public Map<String,Object>  deleteCaseInfo(HttpServletRequest request,
			HttpServletResponse response,String kyzyxh) {
		Map<String,Object> result = new HashMap<String, Object>();
		int code = 0;
		String []kyzyxhArr = null;
		if (kyzyxh != null && !"".equals(kyzyxh)) {
			kyzyxhArr = kyzyxh.split(",");
		}
		try {
			code = this.caseSpecialManager.deleteCaseInfo(kyzyxhArr);
			if (code > 0) {
				result.put("msg", "保存成功!");
			} else {
				result.put("msg", "保存失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	@RequestMapping
	@ResponseBody
	public Map<String,Object>  saveCaseInfo(HttpServletRequest request,
			HttpServletResponse response,CaseSpecial caseSpecial) {
		Map<String,Object> result = new HashMap<String, Object>();
		int code = 0;
		try {
			caseSpecial.setBy1(URLDecoder.decode(caseSpecial.getBy1(),"UTF-8"));
			code = this.caseSpecialManager.saveCaseInfo(caseSpecial);
			if (code > 0) {
				result.put("msg", "保存成功!");
			} else {
				result.put("msg", "保存失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 跳转警情信息库主页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String forwardPoliceInfor(HttpServletRequest request,HttpServletResponse response) {
		List<PoliceInfor> policeInforList = null;
		try {
			policeInforList = this.caseSpecialManager.getPoliceList();
			request.setAttribute("policeInfoList", policeInforList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return POLICEINFO;
	}
	
	
	/**
	 * 跳转案件信息库主页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String forwardCaseInfor(HttpServletRequest request,HttpServletResponse response) {
		List<CaseInfo> CaseInfoList = null;
		try {
			CaseInfoList = this.caseSpecialManager.getCaseInfoList();
			request.setAttribute("CaseInfoList", CaseInfoList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CASEINFO;
	}
	
	
	/**
	 * 跳转快速查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String forwardExemplaryCaseInforQuicksearch(HttpServletRequest request,HttpServletResponse response) {
		try {
			Map m=new HashMap<>();
//			m.put("page", 1);
//			m.put("rows", 10);
//			Map res = this.caseSpecialManager.quicksearchList(m);
//			request.setAttribute("exemplaryInfoList", res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quicksearch;
	}
	/**
	 * 分页查询:快速查询
	 * @param
	 * @param request
	 * @param response
	 * @param page 分页
	 * @param rows 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping()
	public Map<String,Object> queryPoliceInforQuicksearchList(HttpServletRequest request,HttpServletResponse response,
			String rows, String page,
			String lrsj,String lrlx,String zslx){
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("rows", rows);
		filter.put("page", page);
		filter.put("lrsj",lrsj);
		filter.put("lrlx",lrlx );
		filter.put("zslx",zslx );
		try {
			map = caseSpecialManager.quicksearchList(filter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 跳转可疑信息库主页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String forwardExemplaryCaseInfor(HttpServletRequest request,HttpServletResponse response) {
	    List<CaseGroupEntity> exemplaryCaseList = null;
		try {
			exemplaryCaseList = this.caseSpecialManager.getExemplaryCaseList();
			request.setAttribute("exemplaryInfoList", exemplaryCaseList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EXEMPLARCASEINFO;
	}

	/**
	 * 保存专案图片到专案信息库
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> saveCasePic(HttpServletRequest request,HttpServletResponse response,String json){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> result = new HashMap<String,Object>();

		int i = 0;
		JSONObject obj = JSONObject.fromObject(json);
		String zabh = obj.getString("zabh");
		JSONArray array = obj.getJSONArray("pic");
		List<String> list = JSONArray.toList(array,String.class);
		map.put("zabh",zabh);
		map.put("pic",list);
		try {
			i = this.caseSpecialManager.saveCasePic(map);
		} catch (Exception e) {
			result.put("code",0);
			result.put("msg","保存失败");
			e.printStackTrace();
		}
		if(i>=0){
			result.put("code", 1);
		   result.put("msg","保存成功");
		}
		return result;
	}

	public CaseSpecialManager getCaseSpecialManager() {
		return caseSpecialManager;
	}


	public void setCaseSpecialManager(CaseSpecialManager caseSpecialManager) {
		this.caseSpecialManager = caseSpecialManager;
	}
	
	
}
