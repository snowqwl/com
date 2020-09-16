package com.sunshine.monitor.comm.startup;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sunshine.monitor.comm.dao.jdbc.MenuDaoImpl;
import com.sunshine.monitor.system.manager.dao.jdbc.SystemDaoImpl;

/**
 * 初始化类 使用方法：为初始化功能建立方法，并在init方法中调用即可
 * 
 * @author YANGYANG
 * 
 */
@Deprecated
public class InitializeConfig extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ApplicationContext ac ;
	
	private Logger log = Logger.getLogger(InitializeConfig.class);

	@Override
	public void init() throws ServletException {
		ac = WebApplicationContextUtils
				.getRequiredWebApplicationContext(this.getServletContext());
		JdbcTemplate jdbcTemplate = (JdbcTemplate) ac.getBean("jdbcTemplate");
		initMenuTrees(jdbcTemplate);
		loadSystemCode(jdbcTemplate);
		loadDepartmentCode(jdbcTemplate);
		loadUserCode(jdbcTemplate);
	}
	
	/**
	 * @param beanName
	 * @return
	 */
	private Object getBean(String beanName){
		return this.ac.getBean(beanName);
	}
	
	/**
	 * 构建菜单树
	 * 
	 * @param jdbcTemplate
	 */
	private void initMenuTrees(JdbcTemplate jdbcTemplate) {
		MenuDaoImpl md = (MenuDaoImpl)this.getBean("menuDaoImpl");
		md.setJdbcTemplate(jdbcTemplate);
		try {
			md.initMenuTree();
			log.info("构建菜单树初始化成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加载系统代码
	 * @param jdbcTemplate
	 */
	private void loadSystemCode(JdbcTemplate jdbcTemplate) {
		SystemDaoImpl systemDaoImpl = (SystemDaoImpl)this.getBean("systemDao");
		systemDaoImpl.setJdbcTemplate(jdbcTemplate);
		try {
			int countCodes = systemDaoImpl.loadCode();
			log.info("系统代码值加载成功！共加载:" + countCodes + "条记录");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void loadDepartmentCode(JdbcTemplate jdbcTemplate) {
		SystemDaoImpl systemDaoImpl = (SystemDaoImpl)this.getBean("systemDao");
		systemDaoImpl.setJdbcTemplate(jdbcTemplate);
		try {
			int countCodes = systemDaoImpl.loadDepartment();
			log.info("系统部门加载成功！共加载:" + countCodes + "条记录");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void loadUserCode(JdbcTemplate jdbcTemplate) {
		SystemDaoImpl systemDaoImpl = (SystemDaoImpl)this.getBean("systemDao");
		systemDaoImpl.setJdbcTemplate(jdbcTemplate);
		try {
			int countCodes = systemDaoImpl.loadUser();
			log.info("系统用户加载成功！共加载:" + countCodes + "条记录");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// *******************扩展***********************
}
