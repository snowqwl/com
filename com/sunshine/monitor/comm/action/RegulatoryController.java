package com.sunshine.monitor.comm.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/regulatory.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegulatoryController {
	
	/**
	 * 跳转到IT资源综合监管系统
	 * 
	 */
	@RequestMapping
	public ModelAndView findForward(HttpServletRequest request, HttpServletResponse response) {		
		try {
			FileInputStream	 fis=new FileInputStream(new File(RegulatoryController.class.getResource("/").getPath()+"/forwardUrl.properties"));
			InputStream in = new BufferedInputStream(fis);
			Properties p = new Properties();        
			p.load(in);
			String itUrl=p.get("itUrl").toString();
			request.setAttribute("regulatory", itUrl);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ModelAndView("regulatory/temp");
	}
}
