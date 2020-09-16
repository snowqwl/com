package com.sunshine.monitor.comm.action;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sunshine.monitor.comm.dao.PicDao;

public class PicServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1315644173557114568L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = request.getParameter("method");
		String xh = request.getParameter("xh");
		if ((methodName == null) || (xh == null)) {
			return;
		}
		ApplicationContext ctx = null;
		if (ctx == null)
			ctx = WebApplicationContextUtils
					.getRequiredWebApplicationContext(getServletContext());
		PicDao picDao = (PicDao) ctx.getBean("picDao");
		byte[] pic = (byte[]) null;
		try {
			if ("getPicture".equals(methodName))
				pic = picDao.getPic2(xh);
			else if ("getLjPictureForBkxh".equals(methodName)) 
				pic = picDao.getLjPicForBkxh(xh);
			else if ("getKktpForKdbh".equals(methodName))
				pic = picDao.getKktpForKdbh(xh);
			else if ("getClbksqbPic".equals(methodName))
				pic = picDao.getClbksqb(xh);
			else if ("getLajdsPic".equals(methodName))
				pic = picDao.getLajds(xh);
			else if ("getYjcnsPic".equals(methodName))
				pic = picDao.getYjcns(xh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (pic != null) {
			response.reset();

			response.setContentType("image/*;charset=GBK");
			response.setHeader("Content-Disposition", "x");
			OutputStream sos = response.getOutputStream();
			sos.write(pic);
			sos.flush();
		}
	}
}
