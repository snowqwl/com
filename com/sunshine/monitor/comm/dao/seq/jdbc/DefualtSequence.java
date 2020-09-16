package com.sunshine.monitor.comm.dao.seq.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.seq.Sequence;

@Repository("defualtSequence")
public class DefualtSequence implements Sequence{
	private static Map<String,String> seqNames = new HashMap<String,String>();
	static {
		seqNames.put("VEH_PASSREC", "SEQ_PASSREC_XH");
		seqNames.put("FRM_NIGHT_CAR ", "SEQ_NIGHTCAR_ID");
	}
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public String nextVal(String seqName){
		return jdbcTemplate.queryForObject("select CSZ||to_char(?.NEXTVAL," +
				"'FM0999999999') from frm_syspara where GJZ='xzqh'",String.class,seqName);
	}
	
	public String nextValByTable(String tableName){
		String seqName = seqNames.get(tableName.toUpperCase());
		if(StringUtils.isBlank(seqName)) new RuntimeException("请求的表，无序列存在在配置中。");
		return nextVal(seqName);
	}
}
