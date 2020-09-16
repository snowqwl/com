package com.easymap.servlet.org;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;

import com.easymap.dao.org.OrgUserQueryDaoImpl;
import com.easymap.util.XMLOutputterUtil;

@SuppressWarnings("serial")
public class OrgUserQueryServlet extends HttpServlet {
	
	
	public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		responseServer(request, response);
	}

	public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		request.setCharacterEncoding("GBK");
		responseServer(request, response);
	}

	private void responseServer(HttpServletRequest request,HttpServletResponse response) {
		
		try {
			
			String orgIDstr = request.getParameter("name");
			String userid = request.getParameter("userid");
			String num = request.getParameter("num");
			String startpos = request.getParameter("startnum");//当前记录数 startpos
			String isStat = request.getParameter("isStat");
			String order = request.getParameter("order");
			
			System.out.println("======================点击组织架构的操作:"+orgIDstr+"==========================");
			System.out.println("======================点击组织架构的操作:"+userid+"==========================");
			System.out.println("======================点击组织架构的操作:"+num+"==========================");
			System.out.println("======================点击组织架构的操作:"+startpos+"==========================");
			System.out.println("======================点击组织架构的操作:"+isStat+"==========================");
			System.out.println("======================点击组织架构的操作:"+order+"==========================");

			long start = System.currentTimeMillis();

			if ((orgIDstr == null) || (orgIDstr == "null") || orgIDstr.equals(""))
				orgIDstr = "''";
			if (orgIDstr.length() > 0) {
				OrgUserQueryDaoImpl dao = new OrgUserQueryDaoImpl();
				Document document = dao.findOrgUserPage(orgIDstr, userid, Integer.parseInt(startpos), Integer.parseInt(num));//点击节点  
			    XMLOutputterUtil.response(document, response);
			}
			
			long end = System.currentTimeMillis();
			
			System.out.println("====================查询耗时:" + (end - start) + "秒========================");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}