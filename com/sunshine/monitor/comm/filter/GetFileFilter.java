package com.sunshine.monitor.comm.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.sunshine.core.util.FileUtil;

public class GetFileFilter implements Filter{

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		String realPath;
		try {
			realPath = FileUtil.getFileHomeRealPath()+((HttpServletRequest)req).getServletPath();
		} catch (Exception e) {
			e.printStackTrace();
			return ;
		}
		File file = new File(realPath);
		InputStream in = new FileInputStream(file);
		((HttpServletResponse)resp).addHeader("Content-Disposition", "attachment;filename="+file.getName());
		((HttpServletResponse)resp).addHeader("Content-length", ""+file.length());
		resp.setContentType("application/stream");
		OutputStream out = resp.getOutputStream();
		IOUtils.copy(in, out);
		IOUtils.closeQuietly(in);
		out.flush();
		out.close();
		return ;
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
