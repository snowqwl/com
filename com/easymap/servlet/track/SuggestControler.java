package com.easymap.servlet.track;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easymap.dao.track.SuggestQueryDaoImpl;
import com.easymap.util.Tools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@SuppressWarnings("all")
@RequestMapping(value="/suggestCtrl.do",params="method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuggestControler  {
	
	@Autowired
	@Qualifier("SuggestQueryDao")
	private SuggestQueryDaoImpl suggestQueryDaoImpl;
	
	Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
	
	@RequestMapping()
	@ResponseBody
	public void suggest(HttpServletRequest request,HttpServletResponse response){
		String res = "empty";
		String querykind = request.getParameter("querykind");
		String queryKey = request.getParameter("queryKey");
		String sp = request.getParameter("startpos");
		String num = request.getParameter("num");
		int stp = Tools.isEmpty(sp)? 1:Integer.parseInt(sp);
		int snum = Tools.isEmpty(num)? 10:Integer.parseInt(num);
		if(Tools.isNotEmpty(querykind) && Tools.isNotEmpty(queryKey))
		{
//			if("blackphone".equalsIgnoreCase(querykind))
			if(querykind.startsWith("hphm"))
			{
				List suggestBlackPhone = this.suggestQueryDaoImpl.suggestHphm(querykind, queryKey, stp, snum);
				if(Tools.isNotEmpty(suggestBlackPhone))
					res = g.toJson(suggestBlackPhone);
			}
			try {
				//System.err.println("suggest return : " + res);
				response.getWriter().write(res);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
