package com.sunshine.monitor.comm.startup;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.comm.util.SpringApplicationContext;
/**
 * Spring容器初始化完成，启动调度器
 * @author ouyang
 *
 */
//@Component
@Deprecated
public class ContinerInitCompletedEvent implements ApplicationListener<ContextRefreshedEvent> {

	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null){
			System.out.println("**********启动调度器**********");
			ApplicationContext act = event.getApplicationContext();
			SpringApplicationContext.initApplicationContext(act);
			Scheduler scheduler = (Scheduler) act.getBean("quarzScheduler");
			try {
				scheduler.start();
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}
}
