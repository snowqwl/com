package com.sunshine.monitor.system.ws.ClkkbkFeedBackService.services.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.dao.VehAlarmDao;
import com.sunshine.monitor.system.ws.ClkkbkFeedBackService.bean.TransFeedback;
import com.sunshine.monitor.system.ws.ClkkbkFeedBackService.bean.TransIntelligence;
import com.sunshine.monitor.system.ws.ClkkbkFeedBackService.client.ClkkbkFeedBackClient;
import com.sunshine.monitor.system.ws.ClkkbkFeedBackService.dao.TransFeedbackDao;
import com.sunshine.monitor.system.ws.ClkkbkFeedBackService.services.SendInteliigenceService;
import com.sunshine.monitor.system.ws.ClkkbkFeedBackService.utils.ConvertXml;

@Service("sendInteliigenceService")
@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
public class SendInteliigenceServiceImpl implements SendInteliigenceService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ClkkbkFeedBackClient client;
	@Autowired
	private TransFeedbackDao transFeedbackDao;
	@Autowired
	private VehAlarmDao vehAlarmDao;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public List<TransIntelligence> getTransInteliigenceList() {
		 String sql = "select * from trans_intelligence where cszt='0' and csbj<100";
		 return jdbcTemplate.query(sql, new RowMapper<TransIntelligence>(){
			public TransIntelligence mapRow(ResultSet rs, int index) throws SQLException {
				String dateFormat = "yyyy-MM-dd HH:mm:ss";
				TransIntelligence t = new TransIntelligence();
				String cszt = rs.getString("cszt");
				String csxh = rs.getString("csxh");
				Double csbj = rs.getDouble("csbj");
				Date cssj = rs.getDate("cssj");
				String type = rs.getString("type");
				String ywxh = rs.getString("ywxh");
				SimpleDateFormat spDateFormat = new SimpleDateFormat(dateFormat);
				t.setCszt(cszt);
				t.setCsxh(csxh);
				t.setCsbj(csbj);
				t.setCssj(spDateFormat.format(cssj));
				t.setType(type);
				t.setYwxh(ywxh);
				return t;
			}
			 
		 });
	}

	public void sendTransIntelligence(TransIntelligence t) throws Exception {
		String returnXml = "";
		if ("22".equals(t.getType())) {
			VehAlarmrec bean = vehAlarmDao.getVehAlarmForBjxh(t.getYwxh());
			returnXml = client.feedBack(ConvertXml.getAlarmXml(bean));
			
		} else if ("25".equals(t.getType())) {
			VehAlarmHandled bean = vehAlarmDao.getAlarmHandleForFkbh(t.getYwxh());
			returnXml = client.feedBack(ConvertXml.getHandledXml(bean));
		} else {
			throw new RuntimeException("获取类型不正确，不能传输，type="+t.getType());
		}
		TransFeedback back = ConvertXml
				.getTransFeedback(returnXml, t.getType());
		transFeedbackDao.save(back);
		updateTransIntellivece(t,back.getCsjg());
	}

	private void updateTransIntellivece(TransIntelligence bean, String jg) {
		int csbj = bean.getCsbj().intValue() + 1;
	    String sql = "update trans_intelligence set cszt='" + jg + "', csbj=" + csbj + ", cssj=sysdate where csxh='" + bean.getCsxh() + "'";
	    int res = jdbcTemplate.update(sql);
	    if(res != 1) log.error("大情报相关数据发送完成时，更新传输表异常，传输序号为："+ bean.getCsxh());
	    else log.info("大情报相关数据发送完成，更新传输表成功，传输序号为："+ bean.getCsxh());
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public ClkkbkFeedBackClient getClient() {
		return client;
	}

	public void setClient(ClkkbkFeedBackClient client) {
		this.client = client;
	}

	public TransFeedbackDao getTransFeedbackDao() {
		return transFeedbackDao;
	}

	public void setTransFeedbackDao(TransFeedbackDao transFeedbackDao) {
		this.transFeedbackDao = transFeedbackDao;
	}

	public VehAlarmDao getVehAlarmDao() {
		return vehAlarmDao;
	}

	public void setVehAlarmDao(VehAlarmDao vehAlarmDao) {
		this.vehAlarmDao = vehAlarmDao;
	}

}
