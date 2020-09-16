package com.sunshine.monitor.comm.util;

import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.View;

public class JsonView implements View {
	public static final JsonView instance = new JsonView();
	public static final String JSON_ROOT = "root";

	public String getContentType() {
		return "text/html;charset=UTF-8";
	}

	public void render(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Object jsonStr = model.get("root");
		if (jsonStr == null) {
			return;
		}
		PrintWriter writer = response.getWriter();
		writer.write(jsonStr.toString());
	}
}
