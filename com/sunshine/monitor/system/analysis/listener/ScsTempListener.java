package com.sunshine.monitor.system.analysis.listener;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.analysis.Constant;

/**
 * session监听器，session销毁时删除天云星临时表
 * 
 * @author Administrator
 * 
 */
public class ScsTempListener implements HttpSessionListener,ServletContextListener {
    
	protected Logger log = LoggerFactory.getLogger(BaseDaoImpl.class);

	public void sessionCreated(HttpSessionEvent event) {
      log.info("session创建："+event.getSession().getId());
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		String sessionId = event.getSession().getId().replaceAll("-","_");
		JdbcTemplate template = (JdbcTemplate) SpringApplicationContext
				.getStaticApplicationContext().getBean("jdbcScsTemplate");
		List<Map<String, Object>> list = template
				.queryForList("SHOW TABLES LIKE '"+Constant.SCS_TEMP_PREFIX+"_%"+sessionId+"%'");
		Long start = System.currentTimeMillis();
		log.info("session销毁，临时表开始删除......");
		for (Map<String, Object> m : list) {
			StringBuffer sb = new StringBuffer("DROP TABLE IF EXISTS ");
			sb.append(m.get("Tables").toString());
			template.update(sb.toString());
		}
		Long end = System.currentTimeMillis();
		log.info("session销毁，临时表删除成功，耗时："+(end-start));
	}

	public void contextDestroyed(ServletContextEvent event) {
		
	}

	public void contextInitialized(ServletContextEvent event) {
		
		JdbcTemplate template = (JdbcTemplate) SpringApplicationContext
				.getStaticApplicationContext().getBean("jdbcScsTemplate");
		//删除临时表
		List<Map<String, Object>> list = template
				.queryForList("SHOW TABLES LIKE '"+Constant.SCS_TEMP_PREFIX+"_%'");
		Long start = System.currentTimeMillis();
		log.info("应用启动，临时表开始删除......");
		for (Map<String, Object> m : list) {
			StringBuffer sb = new StringBuffer("DROP TABLE IF EXISTS ");
			sb.append(m.get("Tables").toString());
			log.info("删除临时表："+m.get("Tables").toString());
			template.update(sb.toString());
		}
		Long end = System.currentTimeMillis();
		log.info("应用启动临时表删除成功，耗时："+(end-start));
        //设置特殊号牌列表
		List<String> hphms = template.queryForList("show special value on veh_passrec",String.class);
		Constant.SCS_NO_PLATE="'"+StringUtils.join(hphms, "','")+"'";
	}
}
