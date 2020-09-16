package com.easymap.servlet.vague;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easymap.dao.IVagueDao;
import com.easymap.dao.vague.VagueQueryDaoImpl;
import com.easymap.util.XMLOutputterUtil;

@SuppressWarnings("serial")
public class VagueQueryServlet extends HttpServlet {

	public void destroy() {
		super.destroy();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		try {

			String querykey = request.getParameter("querykey");
			String querytype = request.getParameter("querytype");
			String layer=request.getParameter("layer");
			int startpos = Integer.parseInt(request.getParameter("startnum"));
			int num = Integer.parseInt(request.getParameter("num"));
			String points = request.getParameter("points");
			String vaguesel = request.getParameter("vaguesel");
			String selorg = request.getParameter("selorg");
			System.out.println("querykey:" + querykey);
			System.out.println("querytype:" + querytype);

			long start = System.currentTimeMillis();

			IVagueDao dao = new VagueQueryDaoImpl();
			if(vaguesel == null)
				XMLOutputterUtil.response(dao.findVagueSearch(querytype,querykey,layer, startpos, num, points), response);
			else
				XMLOutputterUtil.response(dao.findVagueSearch(querytype,querykey,selorg,layer, startpos, num), response);

			long end = System.currentTimeMillis();

			System.out.println("====================查询耗时:" + (end - start) + "毫秒========================");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
