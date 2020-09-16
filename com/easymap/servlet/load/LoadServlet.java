package com.easymap.servlet.load;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.easymap.dao.load.LoadDao;
import com.easymap.listeners.InitListener;

@SuppressWarnings("serial")
public class LoadServlet extends HttpServlet {
	
	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		PrintWriter print = null;
		try {
			long l1 = System.currentTimeMillis();
			LoadDao loadDao = new LoadDao(InitListener.sqlProperties);
			Document document = loadDao.getResponseDocument();
			response.setContentType("text/xml;charset=UTF-8");
			Format format = Format.getPrettyFormat();
			format.setEncoding("UTF-8");
			XMLOutputter output = new XMLOutputter(format);
			print = response.getWriter();
			long l2 = System.currentTimeMillis();
			if (document != null) {
				output.output(document, print);
				System.out.println("用时:" + (l2 - l1) + "毫秒");
			} else {
				System.out.println("doc为空!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			print.flush();
			print.close();
		}
	}
}