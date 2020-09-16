package com.sunshine.monitor.system.ws.ClkkbkFeedBackService.dao.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.DBaseDaoImpl;
import com.sunshine.monitor.system.ws.ClkkbkFeedBackService.bean.TransFeedback;
import com.sunshine.monitor.system.ws.ClkkbkFeedBackService.dao.TransFeedbackDao;

@Repository("transFeedbackDao")
public class TransFeedbackDaoImpl extends DBaseDaoImpl<TransFeedback> implements TransFeedbackDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public TransFeedback save(TransFeedback back) throws Exception {
		Map<String,String> userDefSql = new HashMap<String,String>();
		userDefSql.put("xh","SEQ_TRANS_FEEDBACK_XH.nextval");
		userDefSql.put("FKSJ", "sysdate");
		return super.save(back, userDefSql);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public String getTableName() {
		return "trans_feedback";
	}
	
}
