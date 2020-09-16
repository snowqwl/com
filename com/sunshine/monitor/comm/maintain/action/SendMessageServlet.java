package com.sunshine.monitor.comm.maintain.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunshine.monitor.comm.maintain.SmsFacade;
import com.sunshine.monitor.comm.maintain.ISmsTransmitter.SmsTransmitterCallBack;

/**
 * 发送消息的servlet接口
 * 
 * @author liumeng 2015-8-17
 */
public class SendMessageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 发送短信的统一接口
	 */
	private SmsFacade smsFacade = new SmsFacade();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * 调用统一接口发送消息
	 * 
	 * @param mobile
	 *            手机号码
	 * @param content
	 *            信息内容
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String mobile = request.getParameter("mobile");
		String content = request.getParameter("content");
		String msg = "";
		if (mobile == null || mobile.equals("")) {
			msg = "手机号码为空";
		} else if (content == null || content.equals("")) {
			msg = "短信内容为空";
		} else {
			boolean b = false;// 返回ture或者false提示信息是否发送成功
			try {
				b = smsFacade.sendAndPostSms(mobile, content, new SmsTransmitterCallBack() {
					@Override
					public void doCallBack(Map map) throws Exception {
						String status = (String) map.get("status");
						System.out.println("doGet-status " + status);
					}
				});
				msg = "" + b;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		out.write(msg);
		out.flush();
		out.close();
	}
}