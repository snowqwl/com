package com.sunshine.monitor.system.manager.dao.jdbc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.ReCode;
import com.sunshine.monitor.system.manager.dao.ReCodeDao;

@Repository("reCodeDao")
public class ReCodeImpl extends BaseDaoImpl implements ReCodeDao {

	public Map findReCodeForMap(Map filter, ReCode reCode) throws Exception {
		StringBuffer sb = new StringBuffer("select g.kdmc, t.* from CODE_GATE_RECORD t,code_gate g where t.kdbh = g.kdbh  ");

		if(StringUtils.isNotBlank(reCode.getKdbh())){
			sb.append(" and  t.kdbh = '").append(reCode.getKdbh()).append("' ");
		}
		
		if(StringUtils.isNotBlank(reCode.getFxbh())){
			sb.append(" and t.fxbh = '").append(reCode.getFxbh()).append("' ");
		}
		
		if(StringUtils.isNotBlank(reCode.getYxzt())){
			sb.append(" and t.yxzt = '").append(reCode.getYxzt()).append("' ");
		}
		
		if(StringUtils.isNotBlank(reCode.getYalx())){
			sb.append(" and t.yalx = '").append(reCode.getYalx()).append("' ");
		}
		
		if(StringUtils.isNotBlank(reCode.getGlbm())){
			if(reCode.getGlbm().length() != 1){
				sb.append(" and g.dwdm = '").append(reCode.getGlbm()).append("' ");
			}
		}
		sb.append("order by t.yabh desc");
		return this.findPageForMap(sb.toString(), 
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
	}

	public ReCode getReCodeForYabh(String yabh) throws Exception {
		StringBuffer sb = new StringBuffer("select * from code_gate_record where yabh = ?");
		
		List list = this.queryForList(sb.toString(), new Object[]{yabh}, ReCode.class);
		if(list.size() > 0){
			return (ReCode)list.get(0);
		}
		
		return null;
	}

	public int saveReCode(ReCode code) throws Exception {
		
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String yabh = df.format(now);
		
		StringBuffer sb = new StringBuffer("insert into   CODE_GATE_RECORD( kdbh,fxbh,yabh, yanr , yxzt , kssj, jssj , wdlj,bjd,jsdw, yalx)  ");
		sb.append(" values('").append(code.getKdbh()==null?"":code.getKdbh());
		sb.append("','").append(code.getFxbh()==null?"":code.getFxbh());
		sb.append("','").append(yabh).append("'||SEQ_RECODE_XH.NEXTVAL").append(",'")
		.append(code.getYanr()==null?"":code.getYanr())
		.append("','").append(code.getYxzt()==null?"":code.getYxzt())
		.append("',");
		
		if(StringUtils.isNotBlank(code.getKssj())){
			sb.append("to_date('").append(code.getKssj()).append("','yyyy-mm-dd hh24:mi:ss'),");
		}else{
			sb.append("null,");
		}
		
		if(StringUtils.isNotBlank(code.getJssj())){
			sb.append("to_date('").append(code.getJssj()).append("','yyyy-mm-dd hh24:mi:ss'),");
		}else{
			sb.append("null,");
		}
		
		sb.append(" '").append(code.getWdlj()==null?"":code.getWdlj()).append("','")
		.append(code.getBjd()==null?"":code.getBjd()).append("','")
		.append(code.getJsdw()==null?"":code.getJsdw()).append("','")
		.append(code.getYalx() == null?"":code.getYalx()).append("') ");
		
		return this.jdbcTemplate.update(sb.toString());
	}

	public int updateReCode(ReCode code) throws Exception {
		StringBuffer sb = new StringBuffer("update CODE_GATE_RECORD set ");
		sb.append(" kdbh = '").append(code.getKdbh())
		  .append("', fxbh = '").append(code.getFxbh())
		  .append("', yalx = '").append(code.getYalx())
		  .append("', yxzt = '").append(code.getYxzt())
		  .append("', kssj = to_date('").append(code.getKssj()).append("','yyyy-mm-dd hh24:mi:ss'), ")
		  .append(" jssj = to_date('").append(code.getJssj()).append("','yyyy-mm-dd hh24:mi:ss'), ")
		  .append(" yanr = '").append(code.getYanr()).append("',");
		
		if(StringUtils.isNotBlank(code.getWdlj())){
			sb.append(" wdlj = '").append(code.getWdlj()).append("', ");
		}
		sb.append(" bjd = '").append(code.getBjd() == null?"":code.getBjd()).append("'  ");
		
		sb.append(" where yabh = '").append(code.getYabh()).append("'  ");
		
		return this.jdbcTemplate.update(sb.toString());
	}

	public List getReCodeListForKdbh(String kdbh) throws Exception {
		String sql = "select * from code_gate_record where kdbh = ?";
		
		return this.queryForList(sql, new Object[]{kdbh}, ReCode.class);
	}
	
	
}
