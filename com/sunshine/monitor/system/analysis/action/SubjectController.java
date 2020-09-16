package com.sunshine.monitor.system.analysis.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.bean.StatisEntity;
import com.sunshine.monitor.system.analysis.bean.SubjectEntity;
import com.sunshine.monitor.system.analysis.service.PeerVehiclesManager;
import com.sunshine.monitor.system.analysis.service.SubjectManager;
import com.sunshine.monitor.system.analysis.service.TimeSpaceCombineManager;

/**
 * 分析主题controller
 * 
 */
@Controller
@RequestMapping(value = "/subject.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SubjectController {

	@Autowired
	private SubjectManager subjectManager;

	@Autowired
	private TimeSpaceCombineManager timeSpaceCombineManager;

	@RequestMapping
	@ResponseBody
	public Object saveSubject(HttpServletRequest request, SubjectEntity subject) {
		Map<String, Object> result = new HashMap<String, Object>();
		String msg = "";
		// 统计信息保存的临时表
		String tablename = "";
		String sessionId = request.getSession().getId().replaceAll("-", "_");
		List<Map<String,Object>> statislist = null;
		try {
			// 区域碰撞，频繁出入，时空轨迹分析保存
			if ("3".equals(subject.getFxlx())||"2".equals(subject.getFxlx())||"10".equals(subject.getFxlx())) {				
				statislist = this.subjectManager.getStatisList(subject);
				this.subjectManager.saveSubject(subject, statislist);
				msg = "保存成功";
			}
			if ("10".equals(subject.getFxlx())) {				
				this.subjectManager.saveAnalysisSubject(subject);
				msg = "保存成功";
			}
			// 套牌主题分析保存
			if ("5".equals(subject.getFxlx())) {
				this.subjectManager.saveSubject(subject, null);
				msg = "保存成功";
			}
			// 同行车主题分析保存
			if ("1".equals(subject.getFxlx())) {
				this.subjectManager.savePeerAnalysis(subject, sessionId);
				msg = "保存成功";
			}
			
			//二次识别分析保存
			if("7".equals(subject.getFxlx())){
				subject.setZtbh(request.getParameter("ztbh"));
				this.subjectManager.saveSubject(subject, null);
				msg = "保存成功";
			}
			
			// 昼伏夜出分析保存
			if("4".equals(subject.getFxlx())) {
				this.subjectManager.saveSubject(subject, null);
				msg = "保存成功";
			}			
			// 关联主题分析保存
			if ("6".equals(subject.getFxlx())){
				this.subjectManager.saveLinkInfo(subject, sessionId);
				msg = "保存成功";
			}
			// 车辆轨迹查询结果保存
			if("8".equals(subject.getFxlx())){
				this.subjectManager.saveVehicleContrail(subject, sessionId);
				msg = "保存成功";
			}
			
			// 案件对碰分析保存
			if("9".equals(subject.getFxlx())){
				this.subjectManager.saveCaseTouch(subject, sessionId);
				msg = "保存成功";
			}
			

		} catch(Exception e) {
			e.printStackTrace();
			msg = "发生异常";
		}
		result.put("msg", msg);
		return result;
	}

	/**
	 * 分析工程统计信息列表
	 * 
	 * @param request
	 * @param page
	 * @param rows
	 * @param ztbh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object queryStatisList(HttpServletRequest request, String page,
			String rows, String ztbh) {
		Map filter = new HashMap();
		filter.put("curPage", page); // 页数
		filter.put("pageSize", rows); // 每页显示行数
		return this.subjectManager.queryStatisList(filter, ztbh);
	}

	@RequestMapping
	@ResponseBody
	public Object querySubjectDetail(String ztbh) {
		return this.subjectManager.querySubjectDetail(ztbh);
	}
}
