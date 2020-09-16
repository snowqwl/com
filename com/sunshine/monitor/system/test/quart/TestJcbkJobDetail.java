package com.sunshine.monitor.system.test.quart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.quart.AbstractJobDetail;

/**
 * 测试实现后台任务
 * 
 * @author YANGYANG
 * 
 */
@Component("testJcbkJobDetail")
public class TestJcbkJobDetail extends BaseDaoImpl implements AbstractJobDetail {

	private Logger logger = LoggerFactory.getLogger(TestJcbkJobDetail.class);

	/**
	 * 任务方法
	 */
	public void doTask() {
		logger.info("......后台任务执行完毕.....");
	}
}
