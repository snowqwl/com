/**
 * 
 */
package com.sunshine.monitor.system.manager.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.Prefecture;
import com.sunshine.monitor.system.manager.service.PrefectureManager;
import com.sunshine.monitor.system.manager.service.SystemManager;

@Controller
@RequestMapping(value="/prefecture.do",params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrefectureCtroller {

	@Autowired
	private PrefectureManager prefectureManager;
	
	@Autowired
	private SystemManager systemManager;
	
	@RequestMapping(params = "method=index")
	public String index() {
		return "manager/prefecturemain";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=queryPrefectureForPage", method = RequestMethod.POST)
	@ResponseBody
	public Map queryPrefectureForPage(String page, String rows, Prefecture prefecture) {	 
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		return this.prefectureManager.findPrefectureForMap(filter, prefecture);
	}
	
	/**
	 * 根据条件展示机构菜单树
	 */
	@RequestMapping(params = "method=getDepartmentTreeByFilter")
	@ResponseBody
	public List<Map<String,Object>> getDepartmentTreeByFilter(@RequestParam("glbm") String glbm) {
		//String dep_id = request.getParameter("depId");
		Map map = new HashMap();
		map.put("glbm", glbm);
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		long begin = System.currentTimeMillis();
		List list = this.prefectureManager.getDepartmentTree(map);
		System.out.println("从数据库请求所用时间:"+(System.currentTimeMillis() - begin));
		if(list.size()>0) {
			Iterator<Object> it = list.iterator();
			while(it.hasNext()){
				Map m = (Map)it.next();
				Map<String,Object> item = new HashMap<String,Object>(); 
				item.put("id", m.get("glbm").toString());
				item.put("name", m.get("bmmc").toString().replaceAll(" ",""));
				item.put("pId", m.get("sjbm").toString());
				menuNodes.add(item);
			}
		}
		return menuNodes;
	}
	
	/**
	 * 异步加载组织机构代码
	 */
	@RequestMapping(params = "method=getDepartmentTreeAsync")
	@ResponseBody
	public List<Map<String,Object>> getDepartmentTreeAsync(HttpServletRequest request) {
		String sjbm = request.getParameter("id");
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		long begin = System.currentTimeMillis();
		List list = this.prefectureManager.getDepartmentTreeAsync(sjbm);
		if(list.size()>0) {
			Iterator<Object> it = list.iterator();
			while(it.hasNext()){
				Map m = (Map)it.next();
				Map<String,Object> item = new HashMap<String,Object>(); 
				String glbm = m.get("glbm").toString();
				boolean isParent = this.prefectureManager.getCountForLowerDepartment(glbm);
				item.put("id", glbm);
				item.put("name", m.get("bmmc").toString().replaceAll(" ",""));
				item.put("pId", m.get("sjbm").toString());
				item.put("isParent", isParent);
				menuNodes.add(item);
			}
		}
		return menuNodes;
	}
	
	@RequestMapping
	public Object query(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			
			Prefecture p = new Prefecture();
			
			p.setDwdm(userSession.getDepartment().getGlbm());
			List<Prefecture> lists = prefectureManager.findPrefectureList(p);
			int counts = 0;
			if (lists == null) {
				counts = 0;
			} else {
				
				for(Prefecture pre : lists){
					pre.setXjjgmc(systemManager.getDepartmentName(pre.getXjjg()));
				}
				counts = lists.size();
				request.setAttribute("queryList", lists);
				request.setAttribute("glbm", userSession.getDepartment()
						.getGlbm());
				request.setAttribute("bmmc", userSession.getDepartment()
						.getBmmc());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "query/prefectureQuery";
	}
	
	/**
	 * 根据条件展示地市菜单树
	 */
	@RequestMapping(params = "method=getCityTreeByFilter")
	@ResponseBody
	public List<Map<String,Object>> getCityTreeByFilter() {
		
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		List list = this.prefectureManager.getCityTree();
		if(list.size()>0) {
			Iterator<Object> it = list.iterator();
			while(it.hasNext()){
				Map m = (Map)it.next();
				Map<String,Object> item = new HashMap<String,Object>(); 
				item.put("id", m.get("dwdm").toString());
				item.put("name", m.get("jdmc").toString().replaceAll(" ",""));
				item.put("pId", m.get("sjjd").toString());
				menuNodes.add(item);
			}
		}
		return menuNodes;
	}
	
	/**
	 * 根据选择的机构展示辖区菜单树,并自动勾选
	 * cityId   地市机构ID
	 * deptId   所选机构ID
	 */
	@RequestMapping(params = "method=getDepartmentBydeptId")
	@ResponseBody
	public List<Map<String,Object>> getDepartmentBydeptId(String cityId, String deptId) {
		String v_deptId = deptId;
		Prefecture pre = new Prefecture();
		pre.setDwdm(deptId);
		List<Prefecture> l = this.prefectureManager.findPrefectureList(pre);          //本辖区所有机构(自己的)
		
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		Map filter = new HashMap();
		//filter.put("glbm", deptId);
		//List list = this.prefectureManager.getDepartmentTree(filter);     //所有的下级部门(所有的)！存在问题：只有几个
		//此处暂显示本地市所有的机构
		filter.put("glbm", cityId);
		List list = this .prefectureManager.getDepartmentTree(filter); 
		if(list.size()>0) {
			Iterator<Object> it = list.iterator();
			while(it.hasNext()){
				Map m = (Map)it.next();
				Map<String,Object> item = new HashMap<String,Object>(); 
				item.put("id", m.get("glbm").toString());
				item.put("name", m.get("bmmc").toString().replaceAll(" ",""));
				item.put("pId", m.get("sjbm").toString());
				
				if(l.size()>0) {
					Iterator<Prefecture> ito = l.iterator();
					while(ito.hasNext()){
						Prefecture mo = (Prefecture)ito.next();
						//System.out.println(mo.getXjjg()+"   "+mo.getXjjgmc());
						if(mo.getXjjg().equals(m.get("glbm").toString())){
							item.put("checked", true);
							break;
						}
					}
				}
				menuNodes.add(item);
			}
		}
		return menuNodes;
		
	}
	
	/**
	 * 保存所选辖区内容
	 * curDept  所选部门
	 * menuids  已存在的下级辖区(含自己)
	 */
	@RequestMapping(params = "method=savePrefectureTree", method = RequestMethod.POST)
	@ResponseBody
	public String savePrefectureTree(String curDept, String menuids) {
		//menuids格式如：445300040000,445302040000,445322040000,445323030000,445321010200

		boolean flag = this.prefectureManager.savePrefectureTree(curDept, menuids);
		if(flag){
			return "辖区维护保存成功!";
		} else {
			return "辖区维护保存失败!";
		}
		
	}

	
	@RequestMapping
	@ResponseBody
	public List<Prefecture> queryPrefectureList(Prefecture prefecture, String page, String rows) {
		return this.prefectureManager.findPrefectureList(prefecture);

	}
	
}
