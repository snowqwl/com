package com.ht.dc.trans.receive;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ht.dc.trans.bean.TransObj;
import com.ht.dc.trans.bean.TransReceive;
import com.ht.dc.trans.util.TransUtil;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.manager.dao.SystemDao;


public class TransReceiveServlet extends HttpServlet {
	private static final long serialVersionUID = -425066596178857664L;
	public static int type = 1;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if ((method != null) && ("test".equals(method))) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<HTML>");
			out.println("  <HEAD>");
			out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\" />");
			out.println("<TITLE>TMRI.HT DATA COMMUNICATION</TITLE></HEAD>");
			out.println("  <BODY>");
			out.print("    Hello. Welcome to 'TMRI.HT DATA COMMUNICATION' system.");
			out.println("  </BODY>");
			out.println("</HTML>");
			out.flush();
			out.close();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ObjectOutputStream out = new ObjectOutputStream(
				response.getOutputStream());

		ObjectInputStream in = new ObjectInputStream(request.getInputStream());

		TransObj obj = null;
		String[] result = (String[]) null;
		try {
			obj = (TransObj) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			result = new String[3];
			result[1] = "0";
			result[2] = ("读取对象失败！" + e.getMessage());
			out.writeObject(result);
			out.flush();
			out.close();
			return;
		}
		if (obj == null) {
			result = new String[3];
			result[1] = "0";
			result[2] = "读取的对象为空";
			out.writeObject(result);
			out.flush();
			out.close();
			return;
		}
		
		if (type == 1)
			result = dynamicReceive(obj, request, response);
		else if (type == 2) {
			result = semiautomaticReceive(obj, request, response);
		}
		out.writeObject(result);
		out.flush();
		out.close();
	}

	private String[] dynamicReceive(TransObj obj, HttpServletRequest request,
			HttpServletResponse response) {
		ReceiveBusiness business = null;
		String[] result = (String[]) null;

		boolean isValid = false;
		TransUtil util = new TransUtil();
		//验证发送端与接收端(测试时先注释)
		/*isValid = util.isDwdmIPValid(obj.getCsxh().substring(0, 12),
				request.getRemoteAddr(), obj.getCsdw(), obj.getJsdw(),
				obj.getSn());
			if (!isValid) {
			result = new String[3];
			result[0] = obj.getCsxh();
			result[1] = "0";
			result[2] = "传输单位不合法，请检查以下项：1-传输序号是否为12位行政区划+10位序列值；2-系统单位和系统IP是否已在省厅注册；3-SN设置是否正确。";
			return result;
		}*/
		
		//布控接收接口(TRANS_RECEIVE_JOB表)
		TransReceive transReceive = null;
		try {
			/**
			TransReceiveDaoJdbc transReceiveDao = (TransReceiveDaoJdbc) ctx
					.getBean("transReceiveDao");
			transReceive = (TransReceive) transReceiveDao.queryDetail(String
					.valueOf(obj.getType()));
			**/
		  SystemDao systemDao = (SystemDao) SpringApplicationContext.getStaticApplicationContext().getBean("systemDao");
		  transReceive = (TransReceive)systemDao.queryDetail("TRANS_RECEIVE_JOB", "ywbh",obj.getType(),TransReceive.class);
		  obj.getObj().toXmlString();
		} catch (Exception e) {
			result = new String[3];
			result[0] = obj.getCsxh();
			result[1] = "0";
			result[2] = ("获取业务失败：" + obj.getType() + "，原因：" + e.getMessage());
			return result;
		}

		if (transReceive == null) {
			result = new String[3];
			result[0] = obj.getCsxh();
			result[1] = "0";
			result[2] = ("业务类未正确配置：" + obj.getType());
			return result;
		}
		try {
			//接收数据具体类（布控SuspReceive）
			business = (ReceiveBusiness) Class.forName(
					transReceive.getYwclass()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			result = new String[3];
			result[0] = obj.getCsxh();
			result[1] = "0";
			result[2] = ("动态获取Java类异常：" + transReceive.getYwclass() + ":"
					+ transReceive.getYwbh() + "。" + e.getMessage());
			return result;
		}
		if (business == null) {
			result = new String[3];
			result[0] = obj.getCsxh();
			result[1] = "0";
			result[2] = "动态获取到的Java类为空";
		} else {
			
			result = business.doReceive(obj, request, response);
		}
		return result;
	}

	private String[] semiautomaticReceive(TransObj obj,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
}
