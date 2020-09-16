package com.sunshine.monitor.comm.util;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTemplateFactory {
	
	private static JdbcTemplateFactory jdbcTemplateFactory = new JdbcTemplateFactory();
	
	private Logger log = LoggerFactory.getLogger(JdbcTemplateFactory.class);

	private JdbcTemplateFactory(){
		
	}
	
	public static JdbcTemplateFactory getJdbcTemplateFactory(){
		return jdbcTemplateFactory;
	}
	
	public JdbcTemplate getJdbcTemplate() throws Exception {
		ApplicationContext ac = SpringApplicationContext.getStaticApplicationContext();
		JdbcTemplate jdbcTemplate = null ;
		if(ac == null){
			/*load bean from configuration*/
			ac = new ClassPathXmlApplicationContext("spring-db.xml");
		} else {
			boolean is = SpringApplicationContext.containsBean("jdbcTemplate");
			if(is){
				jdbcTemplate = (JdbcTemplate)ac.getBean("jdbcTemplate");
			} else {
				log.info("Spring configuration file is not exist bean name jdbcTemplate!");
			}
		}
		return jdbcTemplate;
	}
}