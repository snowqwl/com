package com.sunshine.monitor.comm.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sunshine.monitor.comm.util.ValidateCode;

public class ValidateCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if ((request.getParameter("method") != null)
				&& (request.getParameter("method").equals("guess"))) {
			HttpSession s = request.getSession();
			String r = (String) s.getAttribute("codes");
			System.out.println("codes:" + r);
		} else {
			response.setContentType("image/jpeg");

			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0L);
			HttpSession session = request.getSession();
			ValidateCode vCode = new ValidateCode(120, 30, 4, 20);

			session.setAttribute("codes", vCode.getCode());
			
			vCode.write(response.getOutputStream());
		}
	}
}
