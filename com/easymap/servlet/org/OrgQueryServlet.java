package com.easymap.servlet.org;

import com.easymap.dao.IOrgQueryDao;
import com.easymap.dao.org.OrgQueryDaoImpl;
import com.easymap.util.XMLOutputterUtil;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class OrgQueryServlet extends HttpServlet {
	
	public void destroy() {
		super.destroy();
	}

	public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		
		try {
			String userid = request.getParameter("userid");
			
			System.out.println("==================userid:"+userid);
			
			long start = System.currentTimeMillis();
			
			if (userid != null) {
				IOrgQueryDao dao = new OrgQueryDaoImpl();
				XMLOutputterUtil.response(dao.findOrgQuery(userid), response);
			}
			
			long end = System.currentTimeMillis();
			
			System.out.println("====================查询耗时:" + (end - start) + "秒========================");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
