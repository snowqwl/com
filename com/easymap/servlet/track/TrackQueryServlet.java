package com.easymap.servlet.track;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;

import com.easymap.dao.ITrackDao;
import com.easymap.dao.track.TrackQueryDaoImpl;
import com.easymap.util.XMLOutputterUtil;

@SuppressWarnings("serial")
public class TrackQueryServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			String querykind = request.getParameter("querykind");
			String queryKey = request.getParameter("queryKey");
			
			long start = System.currentTimeMillis();

			Document document = null;
			ITrackDao dao = new TrackQueryDaoImpl();
			document = dao.findVagueQueryPage(querykind, queryKey, Integer.parseInt(request.getParameter("startpos")), Integer.parseInt(request.getParameter("num")));
		
			
			XMLOutputterUtil.response(document, response);

			long end = System.currentTimeMillis();
			
			System.out.println("====================查询耗时:" + (end - start) + "秒========================");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void destroy() {
		super.destroy();
	}
}
