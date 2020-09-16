package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.dao.StudyDao;

@Repository("studyDao")
public class StudyDaoImpl extends BaseDaoImpl implements StudyDao {
	public Map queryGateGroupList(Map filter, String gname) {
		String sql = "select gid,gname from jm_sensitivegate_group where 1 = 1";
		if (gname != null && !"".equals(gname)) {
			sql += " and gname like '%" + gname + "%'";
		}
		if(filter == null) {
			Map result = new HashMap();
			result.put("total", 0);
			result.put("rows", this.jdbcTemplate.queryForList(sql));
			return result;
		}
		
		Map<String,Object> map = this.findPageForMap(sql, 
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString())
		);
		
		return map;
	}
	
	public int insertGateGroup(String gname,String kdbhs) {		
		if(gname != null && !"".equals(gname)) {
			String gid = this.jdbcTemplate.queryForObject("select SEQ_JM_GATEGROUP.nextval from dual", String.class);
			StringBuffer group_sql = new StringBuffer("insert into jm_sensitivegate_group(gid,gname) values('");
			group_sql.append(gid);
			group_sql.append("','");
			group_sql.append(gname);
			group_sql.append("')");
			this.jdbcTemplate.update(group_sql.toString());
			if(kdbhs!=null && !"".equals(kdbhs)) {
				String[] kdbh = kdbhs.split(",");
				for(int i = 0 ;i<kdbh.length;i++) {
					this.jdbcTemplate.update("insert into jm_sensitivegate(gid,kdbh) values('"+gid+"','"+kdbh[i]+"')");
				}
			}
			return 1;
		}
		return 0;
	}
	
	public int updateGateGroup(String gid,String gname,String kdbhs) {
		String group_sql = "update jm_sensitivegate_group set gname = '"+gname+"' where gid = '"+gid+"'";
		String delete_sql = "delete from jm_sensitivegate where gid = '"+gid+"'";
		this.jdbcTemplate.update(group_sql);
		this.jdbcTemplate.update(delete_sql);
		if(kdbhs!=null && !"".equals(kdbhs)) {
			String[] kdbh = kdbhs.split(",");
			for(int i = 0 ;i<kdbh.length;i++) {
				this.jdbcTemplate.update("insert into jm_sensitivegate(gid,kdbh) values('"+gid+"','"+kdbh[i]+"')");
			}
		}
		return 1;
	}
	
	public void deleteGateGroup(String gid) {
		String delete_group_sql = "delete from jm_sensitivegate_group where gid = '"+gid+"'";
		String delete_group_gate_sql = "delete from jm_sensitivegate where gid = '"+gid+"'";
		this.jdbcTemplate.update(delete_group_sql);
		this.jdbcTemplate.update(delete_group_gate_sql);
	}
	
	public List queryGateListByGid(String gid) {
		String sql = "select a.kdbh,b.gname from jm_sensitivegate a,jm_sensitivegate_group b where a.gid=b.gid and b.gid = '"+gid+"'";
		List list = this.jdbcTemplate.queryForList(sql);
		return list;
	}
	
	public Map queryTimeGroupList(Map filter, String tname) {
		String sql = "select tid,tname,timegroup from jm_sensitive_time where 1 = 1";
		if (tname != null && !"".equals(tname)) {
			sql += " and tname like '%" + tname + "%'";
		}
		if(filter == null) {
			Map result = new HashMap();
			result.put("total", 0);
			result.put("rows", this.jdbcTemplate.queryForList(sql));
			return result;
		}		

		Map<String,Object> map = this.findPageForMap(sql, 
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString())
		);
		
		return map;
	}
	
	public int insertTimeGroup(Map params) {
		String select_sql = "select count(1) from jm_sensitive_time where  tname ='"+params.get("tname")+"'";
		int count = this.jdbcTemplate.queryForObject(select_sql, Integer.class);
		if(count>0) {
			return 0;
		} else {
			String insert_sql = "insert into jm_sensitive_time(tid,tname,timegroup) values(SEQ_JM_SENSITIVE_TIME.Nextval,'"
				+params.get("tname")
				+"','"
				+params.get("timegroup")
				+"')";
			this.jdbcTemplate.update(insert_sql);
			return 1;
		}
	}
	
	public int updateTimeGroup(Map params) {
		String select_sql = "select count(1) from jm_sensitive_time where  tname ='"+params.get("tname")+"' and tid <> '"+params.get("tid")+"'";
		int count = this.jdbcTemplate.queryForObject(select_sql, Integer.class);
		if(count>0) {
			return 0;
		} else {
			String update_sql = "update jm_sensitive_time set tname ='"
				+params.get("tname")
				+"',timegroup='"
				+params.get("timegroup")
				+"' where tid = '"
				+params.get("tid")+"'";
			this.jdbcTemplate.update(update_sql);
			return 1;
		}
	}
	
	public void deleteTimeGroup(String tid) {
		this.jdbcTemplate.update("delete from jm_sensitive_time where tid = '"+tid+"'");
	}
	
	public List getTimeGroupByTid(String tid) {
		String sql = "select tid,tname,timegroup from jm_sensitive_time where 1=1";
		if (tid != null && !"".equals(tid)) {
			sql += " and tid = '" + tid+ "'";
		}
		//System.out.println(sql);
		return this.jdbcTemplate.queryForList(sql);
	}
	
	public Map  queryRuleList(Map filter, Map param) {
		String sql = "select rid,rulename,ruletype,by1,by2 from jm_raterule where 1 = 1";
		if (param != null) {
			if (param.get("ruletype") != null
					&& !"".equals(param.get("ruletype"))) {
				sql += " and ruletype = '" + param.get("ruletype") + "'";
			}
			if (param.get("rulename") != null
					&& !"".equals(param.get("rulename"))) {
				sql += " and rulename like '%" + param.get("rulename") + "%'";
			}
		}
		sql += " order by gxsj desc ";
		if(filter == null) {
			Map result = new HashMap();
			result.put("total", 0);
			result.put("rows", this.jdbcTemplate.queryForList(sql));
			return result;
		}		

		Map<String,Object> map = this.findPageForMap(sql, 
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString())
		);
		
		return map;
	}
	
	public int updateRule(Map param){
		String select_sql = "select count(1) from jm_raterule where 1 = 1 ";
		if (param.get("rulename") != null && !"".equals(param.get("rulename"))) {
			select_sql += " and rulename = '"+param.get("rulename")+"'";
		}
		if (param.get("rid") != null && !"".equals(param.get("rid"))) {
			select_sql += " and rid <> '"+param.get("rid")+"'";
		}
		int count = this.jdbcTemplate.queryForObject(select_sql, Integer.class);
		if(count>0) {
			return 0;
		} else {
			String update_sql = null;
			if(param.get("opration") != null && "new".equals(param.get("opration"))) {
				update_sql = "insert into jm_raterule(rid,ruletype,rulename,by1,by2,gxsj) values(seq_jm_raterule.nextval,'"
					+ param.get("ruletype")
					+"','"
					+param.get("rulename")
					+"','"
					+param.get("by1")
					+"','"
					+((param.get("by2")==null)?"":param.get("by2"))
					+"',sysdate)";
			} else if(param.get("opration") != null && "edit".equals(param.get("opration"))) {
				update_sql = "update jm_raterule set ruletype = '"
					+ param.get("ruletype")
					+"',rulename='"
					+ param.get("rulename")
					+"',by1='"
					+ param.get("by1")
					+"',by2='"
					+((param.get("by2")==null)?"":param.get("by2"))
					+"',gxsj=sysdate where rid = '"
					+param.get("rid")+"'";
			}
			//System.out.println(update_sql);
			if(update_sql != null) {
				this.jdbcTemplate.update(update_sql);
			}
			return 1;
		}
	}
	
	public int deleteRule(String rid) {
		String select_sql = "select count(1) from jm_raterule where rid = '"+rid+"'";
		int count = this.jdbcTemplate.queryForObject(select_sql, Integer.class);
		if(count == 0) {
			return 0;
		} else {
			this.jdbcTemplate.update("delete from jm_raterule where rid = '"+rid+"'");
			return 1;
		}
	}

}
