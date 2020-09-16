package com.easymap.servlet.track;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easymap.dao.track.HistoryQuerySCSDaoImpl;
import com.easymap.util.Tools;
import com.easymap.util.XMLOutputterUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@SuppressWarnings("all")
@RequestMapping(value="/historyCtrl.do",params="method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HistoryControler  {
	
	@Autowired
	@Qualifier("HistoryQuerySCSDao")
	private HistoryQuerySCSDaoImpl historyQuerySCSDaoImpl; 
	
	Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
	
	@RequestMapping()
	@ResponseBody
	public void histrack(HttpServletRequest request,HttpServletResponse response){
		String res = "empty";
		String querykind = request.getParameter("querykind");
		String queryKey = request.getParameter("queryKey");
		String sp = request.getParameter("startpos");
		String num = request.getParameter("num");
		String stime = request.getParameter("startTime");
		String etime = request.getParameter("endTime");
		int stp = Tools.isEmpty(sp)? 1:Integer.parseInt(sp);
		int snum = Tools.isEmpty(num)? 10:Integer.parseInt(num);
		if(Tools.isNotEmpty(querykind) && Tools.isNotEmpty(queryKey))
		{
				Document document = this.historyQuerySCSDaoImpl.hphmzltrack(queryKey, stp, snum, stime, etime);
				XMLOutputterUtil.response(document, response);
		}
	}
	
	@RequestMapping()
	@ResponseBody
	public Map histrack1(HttpServletRequest request, HttpServletResponse response){
		Map remap = new HashMap();
		String res = "empty";
		String querykind = request.getParameter("querykind");
		String queryKey = request.getParameter("queryKey");
		String sp = request.getParameter("startpos");
		String num = request.getParameter("num");
		String stime = request.getParameter("startTime");
		String etime = request.getParameter("endTime");
		int stp = Tools.isEmpty(sp)? 1:Integer.parseInt(sp);
		int snum = Tools.isEmpty(num)? 10:Integer.parseInt(num);
		if(Tools.isNotEmpty(querykind) && Tools.isNotEmpty(queryKey))
		{
			try {
				remap = this.historyQuerySCSDaoImpl.hphmzltrack1(queryKey, stp, snum, stime, etime);
			} catch (Exception e) {
			remap.put("msg", "ydb查询接口报错，错误原因"+e.getMessage());
			}
			
		}

		return remap;
	}
	
}
