package com.easymap.servlet.org;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easymap.dao.IOrgQueryDao;
import com.easymap.util.XMLOutputterUtil;

@Controller
@RequestMapping(value="/orgQueryCtrl.do",params="method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrgQueryControler  {
	
	@Autowired
	@Qualifier("OrgQueryDao")
	private IOrgQueryDao OrgQueryDaoImpl;
	

	@RequestMapping()
	@ResponseBody
	public void orgQuery(HttpServletRequest request,HttpServletResponse response,String userid){
		String userids = request.getParameter("userid");
		try {
			
			
			System.out.println("==================userid:"+userid);
			
			long start = System.currentTimeMillis();
			
			if (userid != null) {
				
				XMLOutputterUtil.response(OrgQueryDaoImpl.findOrgQuery(userid), response);
			}
			
			long end = System.currentTimeMillis();
			
			System.out.println("====================查询耗时:" + (end - start) + "========================");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
