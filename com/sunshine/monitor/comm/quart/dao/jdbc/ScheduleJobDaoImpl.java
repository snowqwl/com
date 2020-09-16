package com.sunshine.monitor.comm.quart.dao.jdbc;

import java.sql.Types;
import java.util.*;
import java.util.Map.Entry;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.quart.bean.JobEntity;
import com.sunshine.monitor.comm.quart.dao.ScheduleJobDao;
import com.sunshine.monitor.comm.util.Common;

@Repository("scheduleJobDao")
public class ScheduleJobDaoImpl extends BaseDaoImpl implements ScheduleJobDao {

	/**
	 * 初始表名
	 */
	public ScheduleJobDaoImpl() {
		super.setTableName("JM_TRANS_SCHEDULE");
	}

	public Map<String, Object> queryScheduleList(
			Map<String, Object> conditions, String tableName) throws Exception {
		String table = "";
		List param = new ArrayList<>();
		if (tableName == null || "".equals(tableName)) {
			table = this.getTableName();
		} else {
			table = tableName;
		}
		StringBuffer sql = new StringBuffer(50);
		sql.append("SELECT RWBH,RWMC,RWZT,ZXSJ,RWZQ,CWNR,CCGC,SXL,SXLFF,BZ FROM ? where 1=1 ");
		param.add(table);
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter = "sort".equalsIgnoreCase(key)
					|| "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key)
					|| "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				// 模糊查询
				if ("rwmc".equalsIgnoreCase(key)) {
					sql.append(" and rwmc like ?");
					param.add(new String[]{"%"+value+"%"});
				} else {
					sql.append(" and ? = ?");
					param.add(key);
					param.add(value);
				}
			}
		}
		sql.append(" order by ? ?");
		param.add(conditions.get("sort"));
		param.add(conditions.get("order"));

		Object[] array = param.toArray(new Object[param.size()]);

		Map<String, Object> map = findPageForMap(sql.toString(),array, Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}

	public String addSchedule(JobEntity jobEntity) throws Exception {
		List param = new ArrayList<>();
		String x_sql = "SELECT SEQ_TRANS_SCHEDULE_RWBH.Nextval FROM dual";
		String t_rwbh = (String) this.jdbcTemplate.queryForObject(x_sql,
				String.class);
		String rwbh = "A" + Common.addZeroStr(t_rwbh, 4);
		String sql = "INSERT INTO JM_TRANS_SCHEDULE(RWBH,RWMC,RWZT,ZXSJ,RWZQ,CCGC,SXL,SXLFF)VALUES('"
				+ rwbh + "',?,'1',sysdate,?,?,?,?)";
		param.add(jobEntity.getRwmc());
		param.add(jobEntity.getRwzq());
		param.add(jobEntity.getCcgc());
		param.add(jobEntity.getSxl());
		param.add(jobEntity.getSxlff());

		Object[] array = param.toArray(new Object[param.size()]);
		int result = this.jdbcTemplate.update(sql,array);
		if (result == 1) {
			return rwbh;
		}
		return null;
	}

	public int deleteById(String rwbh) throws Exception {
		String sql = "delete from JM_TRANS_SCHEDULE where rwbh = ?";
		return this.jdbcTemplate.update(sql,rwbh);
	}

	public JobEntity getJobEntityByRwbh(String rwbh) throws Exception {
		/**String sql = "select * from JM_TRANS_SCHEDULE where RWBH='" + rwbh
				+ "'";*/
		String sql = "SELECT RWBH,RWMC,RWZT,ZXSJ,RWZQ,CWNR,CCGC,SXL,SXLFF,BZ FROM JM_TRANS_SCHEDULE WHERE RWBH=?";
		List<JobEntity> list = this.queryForList(sql, new Object[]{rwbh}, JobEntity.class);
		//List<JobEntity> list = this.queryForList(sql, JobEntity.class);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public List<JobEntity> getAllJobEntitys(boolean isLoadAll) throws Exception {
		String sql = null;
		if (isLoadAll) {
			sql = "SELECT RWBH,RWMC,RWZT,ZXSJ,RWZQ,CWNR,CCGC,SXL,SXLFF,BZ FROM JM_TRANS_SCHEDULE";
		} else {
			sql = "SELECT RWBH,RWMC,RWZT,ZXSJ,RWZQ,CWNR,CCGC,SXL,SXLFF,BZ FROM JM_TRANS_SCHEDULE WHERE RWZT = '1'";
		}
		return queryForList(sql, JobEntity.class);
	}

	public int updateJobEntity(JobEntity jobEntity) throws Exception {
		List param = new ArrayList<>();
		String sql =
				"update JM_TRANS_SCHEDULE set rwmc=?,rwzq= ?,sxl=?,sxlff=?,ccgc=?,rwzt='1' where rwbh = ?";

		param.add(jobEntity.getRwmc());
		param.add(jobEntity.getRwzq());
		param.add(jobEntity.getSxl());
		param.add(jobEntity.getSxlff());
		param.add(jobEntity.getCcgc());
		param.add(jobEntity.getRwbh());

		Object[] array = param.toArray(new Object[param.size()]);
		int result = this.jdbcTemplate.update(sql,array);
		return result;
	}

	public int updateJobStatu(String rwbh, String rwzt) throws Exception {
		String sql = "UPDATE JM_TRANS_SCHEDULE SET RWZT=? WHERE RWBH = ?";
		int result = this.jdbcTemplate.update(sql, new Object[] { rwzt, rwbh },
				new int[] { Types.VARCHAR, Types.VARCHAR });
		return result;
	}

	public int updateJobErrordesc(String rwbh, String cwnr) throws Exception {
		cwnr = cwnr.replaceAll("'", "''");
		String sql = "UPDATE JM_TRANS_SCHEDULE SET CWNR = ? WHERE RWBH = ?";

		int result = this.jdbcTemplate.update(sql,new Object[]{cwnr,rwbh});
		// this.jdbcTemplate.update(sql, new Object[] { cwnr, rwbh },
		// new int[] { Types.VARCHAR, Types.VARCHAR });
		return result;
	}

	public String getTime() throws Exception {
		String sql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual";
		return (String) this.jdbcTemplate.queryForObject(sql, String.class);
	}

	public int updateJobDate(String rwbh) throws Exception {
		String sql = "UPDATE JM_TRANS_SCHEDULE set rwzt='2',zxsj=sysdate where rwbh=?";
		int result = this.jdbcTemplate.update(sql, new Object[] { rwbh },
				new int[] { Types.VARCHAR });
		return result;
	}

	public int saveLog(String rwbh, String zxsj, String czlx, String czjg,
			String cwnr) throws Exception {
		List param = new ArrayList<>();
		String sql =
				"INSERT INTO JM_TRANS_SCHEDULE_LOG(xh,rwbh,zxsj,jssj,czlx,czjg,cwnr) values" +
						"(SEQ_SCHEDULE_LOG.Nextval,?, to_date(?,'yyyy-mm-dd hh24:mi:ss'), sysdate" +
						" ,?, ?, ?)";

		param.add(rwbh);
		param.add(zxsj);
		param.add(czlx);
		param.add(czjg);
		param.add(cwnr);

		Object[] array = param.toArray(new Object[param.size()]);

		int result = this.jdbcTemplate.update(sql,param);
		return result;
	}

	public boolean checkTaskip() throws Exception {
		String sql = "select INSTR(sys_context('userenv', 'ip_address'), v.csz) result,sys_context('userenv', 'ip_address') address,v.csz from frm_syspara v where v.gjz = 'taskip'";
		Map<String, Object> map = this.jdbcTemplate.queryForMap(sql);
		if ((map != null) && (map.size() > 0)) {
			if ((map.get("address").equals("127.0.0.1"))
					|| (map.get("csz").equals("127.0.0.1"))) {
				return true;
			}
			return String.valueOf(map.get("result")).equals("1");
		}
		return false;
	}

	public void initJobData(boolean flag) throws Exception {
		String sql = null;
		if (flag) {
			sql = "update JM_TRANS_SCHEDULE set RWZT='1',CWNR=''";
		} else {
			sql = "update JM_TRANS_SCHEDULE set CWNR='' where rwzt='1'";
		}
		this.jdbcTemplate.update(sql);
	}

	public void updateJob(JobEntity jobEntity) throws Exception {
		List param = new ArrayList<>();
		String sql = "UPDATE JM_TRANS_SCHEDULE set rwzt=?,cwnr=? where rwbh=?";
		param.add(jobEntity.getRwzt());
		param.add(jobEntity.getCwnr().replaceAll("'", "''"));
		param.add(jobEntity.getRwbh());
		Object[] array = param.toArray(new Object[param.size()]);
		this.jdbcTemplate.update(sql,array);
	}
}
