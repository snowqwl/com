package com.easymap.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XMLOutputterUtil {

	public static void response(Document document,HttpServletResponse response) {
		PrintWriter pw = null;
		try {
			response.setContentType("text/xml;charset=UTF-8");
			Format format = Format.getPrettyFormat();
			format.setEncoding("UTF-8");
			XMLOutputter xmlOut = new XMLOutputter(format);
			pw = response.getWriter();
			xmlOut.output(document, pw);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
	}
}
