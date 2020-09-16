package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.bean.StatisEntity;
import com.sunshine.monitor.system.analysis.bean.SubjectEntity;
import com.sunshine.monitor.system.analysis.dao.SubjectDao;

@Repository("subjectDao")
public class SubjectDaoImpl extends BaseDaoImpl implements SubjectDao {

	public int saveSubject(SubjectEntity subject,List<Map<String,Object>> statislist)throws Exception {		
		String ztbh = this.jdbcTemplate.queryForObject("select seq_yp_ztxx.nextval from dual", String.class);
		if(subject.getZtbh()!=null){
			ztbh=subject.getZtbh();
		}
		String insert_subject = "";
		if(subject != null) {
			insert_subject = "insert into t_yp_ztxx(zabh,ztbh,fxztmc,fxlx,fxtj,fxtjms,jgms,bz,fxsj) values('"
				+((subject.getZabh()==null)?"1":subject.getZabh())
				+"','"
				+ztbh
				+"','"
				+((subject.getFxztmc()==null)?"":subject.getFxztmc())
				+"','"
				+((subject.getFxlx()==null)?"":subject.getFxlx())
				+"','"
				+((subject.getFxtj()==null)?"":subject.getFxtj().replace("'", "\""))
				+"','"
				+((subject.getFxtjms()==null)?"":subject.getFxtjms())
				+"','"
				+((subject.getJgms()==null)?"":subject.getJgms().replace("'", "\'"))
				+"','"
				+((subject.getBz()==null)?"":subject.getBz())
				+"',sysdate)";
			this.jdbcTemplate.update(insert_subject);
		}
		if( statislist!= null) {
			String insert_statis = "";
			for(Map<String,Object> statis : statislist) {
				insert_statis = "insert into t_yp_zttjxx(ztbh,hphm,hpzl,hpys,kks,xzqs,ts,cs,sfyj,bz) values('"
					+ztbh
					+"','"
					+((statis.get("hphm")==null)?"":statis.get("hphm"))
					+"','"
					+((statis.get("hpzl")==null)?"":statis.get("hpzl"))
					+"','"
					+((statis.get("hpys")==null)?"":statis.get("hpys"))
					+"','"
					+((statis.get("kks")==null)?"":statis.get("kks"))
					+"','"
					+((statis.get("xzqs")==null)?"":statis.get("xzqs"))
					+"','"
					+((statis.get("ts")==null)?"":statis.get("ts"))
					+"',"
					+((statis.get("cs")==null)?"0":statis.get("cs"))
					+",'"
					+((statis.get("sfyj")==null)?"":statis.get("sfyj"))
					+"','"
					+((statis.get("bz")==null)?"":statis.get("bz"))
					+"')";
				this.jdbcTemplate.update(insert_statis);
			}
			return 1;
		}
		return 0;
	}
	
	public Map queryStatisList(Map filter,String ztbh) {
		String sql = "select * from t_yp_zttjxx where 1 = 1 ";
		if(ztbh != null && !"".equals(ztbh)) {
			sql +=" and ztbh = '"+ztbh+"'";
		}
		sql += " order by cs desc ";
		System.out.println(sql);
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}
	
	public SubjectEntity querySubjectDetail(String ztbh) {
		String sql = "select * from t_yp_ztxx where 1=1 ";
		if(ztbh != null && !"".equals(ztbh)) {
			sql +=" and ztbh = '"+ztbh+"'";
		}
		//System.out.println(sql);
		List<SubjectEntity> list = this.queryForList(sql, SubjectEntity.class);
		if(list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public String saveAnalysisSubject(SubjectEntity subject) throws Exception {
		String ztbh = this.jdbcTemplate.queryForObject("select seq_yp_ztxx.nextval from dual", String.class);
		String insert_subject = "";
		if(subject != null) {
				insert_subject = "insert into t_yp_ztxx(zabh,ztbh,fxztmc,fxlx,fxtj,fxtjms,jgms,bz,fxsj) values('"
				+((subject.getZabh()==null)?"1":subject.getZabh())
				+"','"
				+ztbh
				+"','"
				+((subject.getFxztmc()==null)?"":subject.getFxztmc())
				+"','"
				+((subject.getFxlx()==null)?"":subject.getFxlx())
				+"','"
				+((subject.getFxtj()==null)?"":subject.getFxtj().replace("'", "\""))
				+"','"
				+((subject.getFxtjms()==null)?"":subject.getFxtjms())
				+"','"
				+((subject.getJgms()==null)?"":subject.getJgms().replace("'", "\'"))
				+"','"
				+((subject.getBz()==null)?"":subject.getBz())
				+"',sysdate)";
			this.jdbcTemplate.update(insert_subject);
		}
		return ztbh;
	}

	public int[] saveContrail(final List<Map<String,Object>> list, final String ztbh)throws Exception {
		String sql = "insert into t_yp_gjxx values(?,seq_yp_gjxx.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BatchPreparedStatementSetter setter =	new BatchPreparedStatementSetter() {
			public int getBatchSize() {
				return list.size();
			}
			public void setValues(PreparedStatement ps, int i)throws SQLException {
				Map<String,Object> veh  = list.get(i);
				ps.setString(1,ztbh);
				ps.setString(2,(veh.get("gcxh")!=null)?veh.get("gcxh").toString():"");
				ps.setString(3,(veh.get("kdbh")!=null)?veh.get("kdbh").toString():"");
				ps.setString(4,(veh.get("fxbh")!=null)?veh.get("fxbh").toString():"");
				ps.setString(5,(veh.get("hpzl")!=null)?veh.get("hpzl").toString():""); 
				ps.setString(6,(veh.get("hphm")!=null)?veh.get("hphm").toString():""); 		
				ps.setString(7,(veh.get("cwhphm")!=null)?veh.get("cwhphm").toString():""); 
				ps.setString(8,(veh.get("cwhpys")!=null)?veh.get("cwhpys").toString():""); 
				ps.setString(9,(veh.get("hpyz")!=null)?veh.get("hpyz").toString():""); 
				
		        java.util.Date date = new java.util.Date();
				try {
					date = formatDate.parse(veh.get("gcsj").toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				ps.setDate(10,new java.sql.Date(date.getTime())); 
				
				ps.setString(11,""); 
				ps.setString(12,""); 
				ps.setString(13,(veh.get("cllx")!=null)?veh.get("cllx").toString():""); 
				ps.setString(14,(veh.get("cdbh")!=null)?veh.get("cdbh").toString():""); 
				ps.setString(15,(veh.get("tp1")!=null)?veh.get("tp1").toString():""); 
				ps.setString(16,""); 
				ps.setString(17,""); 
				ps.setString(18,(veh.get("wfbj")!=null)?veh.get("wfbj").toString():""); 
				ps.setString(19,""); 
			}
		}; 
		return this.jdbcTemplate.batchUpdate(sql, setter);
	}

	public int[] savePeerInfo(final List<Map<String, Object>> list,final String ztbh) {
		String sql = "insert into t_yp_txxx values(?,seq_yp_txxx.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter() {

			public int getBatchSize() {
				return list.size();
			}

			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
			  Map<String,Object> map = list.get(i);
		      java.util.Date gcsj = new java.util.Date();
		      java.util.Date mbsj = new java.util.Date();

			  try {
				  if(map.get("gcsj")!=null)
					gcsj = formatDate.parse(map.get("gcsj").toString());
				  if(map.get("mbsj")!=null)
					mbsj = formatDate.parse(map.get("mbsj").toString());

			  } catch (ParseException e) {
					e.printStackTrace();
			  }
				
			  ps.setString(1,ztbh);
			  ps.setString(2,(map.get("hphm")!=null)?map.get("hphm").toString():"");
			  ps.setString(3,(map.get("hpys")!=null)?map.get("hpys").toString():"");
			  ps.setString(4,(map.get("hpzl")!=null)?map.get("hpzl").toString():"");
			  ps.setString(5,(map.get("gcxh")!=null)?map.get("gcxh").toString():"");
			  ps.setDate(6, new java.sql.Date(gcsj.getTime()));
			  ps.setString(7,(map.get("kdbh")!=null)?map.get("kdbh").toString():"");
			  ps.setString(8,(map.get("fxbh")!=null)?map.get("fxbh").toString():"");
			  if(map.get("tp1")!=null){
				  ps.setString(9,map.get("tp1").toString());
			  }else{
				  ps.setString(9,"");
			  }
			  ps.setString(10,(map.get("mbhp")!=null)?map.get("mbhp").toString():"");
			  ps.setString(11,(map.get("mbxh")!=null)?map.get("mbxh").toString():"");
			  ps.setDate(12,new java.sql.Date(mbsj.getTime()));
			  ps.setString(13,(map.get("mbzl")!=null)?map.get("mbzl").toString():"");
			  if(map.get("mbtp")!=null){
				  ps.setString(14,map.get("mbtp").toString());
			  }else{
				  ps.setString(14,"");
			  }
			  ps.setString(15,(map.get("sjc")!=null)?map.get("sjc").toString():"");
			  ps.setString(16,"");
			}
		};
		return this.jdbcTemplate.batchUpdate(sql,setter);
	}
	
	public Map<String, Object> loadContrail(String ztbh,Map<String,Object> filter) throws Exception {
		int page = Integer.parseInt(filter.get("page").toString());
		int rows = Integer.parseInt(filter.get("rows").toString());
		String sql = "select * from t_yp_gjxx where ztbh = '"+ztbh+"'";
		return this.findPageForMap(sql, page, rows, ScsVehPassrec.class);
	}

	public Map<String, Object> loadPeerInfo(String ztbh,Map<String,Object> filter) throws Exception {
		int page = Integer.parseInt(filter.get("page").toString());
		int rows = Integer.parseInt(filter.get("rows").toString());
		String sql = "select hphm,hpzl,hpys,"
				+ " count(gcxh) as cs,"
				+ " count(distinct kdbh) as kks,"
				+ " count(distinct mbhp) as txcs,"
				+ " count(distinct substr(kdbh, 1, 6)) as xzqs,"
				+ " count(distinct trunc(gcsj, 'DD')) as ts"
				+ " from t_yp_txxx where ztbh = '"+ztbh+"'  group by hphm,hpzl,hpys order by cs desc";

		return this.findPageForMap(sql, page, rows);
	}

	public Map<String, Object> getPeerContrail(String ztbh,ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		int page = Integer.parseInt(filter.get("page").toString());
		int rows = Integer.parseInt(filter.get("rows").toString());
		String sql ="select * from t_yp_txxx where hphm='"+veh.getHphm()+"'"
		           +" and hpzl ='"+veh.getHpzl()+"' and ztbh ='"+ztbh+"'";
		return this.findPageForMap(sql, page, rows,ScsVehPassrec.class);
	}
	
	public Map<String, Object> getPeerContrailForMap(String ztbh,ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		int page = Integer.parseInt(filter.get("page").toString());
		int rows = Integer.parseInt(filter.get("rows").toString());
		String sql ="select * from t_yp_txxx where hphm='"+veh.getHphm()+"'"
		           +" and hpzl ='"+veh.getHpzl()+"' and ztbh ='"+ztbh+"'";
		return this.findPageForMap(sql, page, rows);
	}

	public int[] saveLinkInfo(final String ztbh,final List<Map<String, Object>> list) throws Exception {
		String sql = "insert into t_yp_glxx values(?,seq_yp_glxx.nextval,?,?,?,?,?,?,?,?)";
		final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter() {

			public int getBatchSize() {
				return list.size();
			}

			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				  Map<String,Object> map = list.get(i);
			      java.util.Date zhgcsj = new java.util.Date();
				  try {
						zhgcsj = formatDate.parse(map.get("zhgcsj").toString());
				  } catch (ParseException e) {
						e.printStackTrace();
				  }
				  ps.setString(1,ztbh);
				  ps.setString(2,map.get("hphm").toString());
				  ps.setString(3,map.get("hpzl").toString());
				  ps.setString(4,map.get("hpys").toString());
				  ps.setString(5,map.get("cs").toString());
				  ps.setString(6,map.get("xzqs").toString());
				  ps.setString(7,map.get("kks").toString());
				  ps.setDate(8, new java.sql.Date(zhgcsj.getTime()));
				  ps.setString(9,"");
			}
		};
		return this.jdbcTemplate.batchUpdate(sql,setter);
	}

	public Map<String,Object> getLinkList(String ztbh,Map<String,Object> filter)throws Exception{
		StringBuffer sb = new StringBuffer();
		int page = Integer.parseInt(filter.get("page").toString());
		int rows = Integer.parseInt(filter.get("rows").toString());
		sb.append("select * from t_yp_glxx where ztbh = '").append(ztbh).append("'");
		return this.findPageForMap(sb.toString(),page,rows);
	}
	
	/**
	 * 保存案件对碰分析结果
	 * @param ztbh
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public int[] savecaseTouchInfo(final String ztbh,final List<Map<String,Object>> list)throws Exception{
		String sql = "insert into t_yp_glxx values(?,seq_yp_glxx.nextval,?,?,?,?,?,?,?,?)";
		final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter() {

			public int getBatchSize() {
				return list.size();
			}

			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				  Map<String,Object> map = list.get(i);
			      java.util.Date zhgcsj = new java.util.Date();
				  try {
						zhgcsj = formatDate.parse(map.get("gcsj").toString());
				  } catch (ParseException e) {
						e.printStackTrace();
				  }
				  ps.setString(1,ztbh);
				  ps.setString(2,map.get("hphm").toString());
				  ps.setString(3,map.get("hpzl").toString());
				  ps.setString(4,map.get("hpys").toString());
				  ps.setString(5,"");
				  ps.setString(6,map.get("xzqs").toString());
				  ps.setString(7,"");
				  ps.setDate(8, new java.sql.Date(zhgcsj.getTime()));
				  ps.setString(9,"");
			}
		};
		return this.jdbcTemplate.batchUpdate(sql,setter);
	}

}
