package com.sunshine.monitor.system.alarm.quartz;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.sunshine.monitor.comm.util.JdbcTemplateFactory;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.dao.jdbc.AlarmDaoImpl;

/**
 * 定时删除临时预警签收信息
 * @author OUYANG
 *
 */
public class DeleteTempAlarmsign {
	/**
	 * 10分钟之内可以修改
	 */
	private final int timeOut = 10;
	
	public String checkDeleteTempAlarmsign() throws Exception{
		AlarmDaoImpl ad = new AlarmDaoImpl();
		boolean success = true ;
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplateFactory().getJdbcTemplate();
		List<VehAlarmrec> list = null ;
		if(jdbcTemplate != null){
			ad.setJdbcTemplate(jdbcTemplate);
			list = ad.queryTimeoutTempAlarmsign(timeOut);
			for(VehAlarmrec vehAlarmrec:list){
				try {
					ad.deleteTimeoutTempAlarmsign(vehAlarmrec.getBjxh());
				} catch (Exception e) {
					success = false ;
					e.printStackTrace();
					throw e;
				}
			}
			if((list.size() == 0) && (success == true)){
				return "";
			}
		}
		return (success)?"删除签收超时预警副本,执行成功 "+list.size()+" 条":"失败";
	}
	
}
