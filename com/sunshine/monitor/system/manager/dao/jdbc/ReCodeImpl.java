package com.sunshine.monitor.system.manager.dao.jdbc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		List param = new ArrayList<>();
		StringBuffer sb = new StringBuffer("select g.kdmc, t.* from CODE_GATE_RECORD t,code_gate g where t.kdbh = g.kdbh  ");

		if(StringUtils.isNotBlank(reCode.getKdbh())){
			sb.append(" and  t.kdbh = ? ");
			param.add(reCode.getKdbh());
		}
		
		if(StringUtils.isNotBlank(reCode.getFxbh())){
			sb.append(" and t.fxbh = ? ");
			param.add(reCode.getFxbh());
		}
		
		if(StringUtils.isNotBlank(reCode.getYxzt())){
			sb.append(" and t.yxzt = ? ");
			param.add(reCode.getYxzt());
		}
		
		if(StringUtils.isNotBlank(reCode.getYalx())){
			sb.append(" and t.yalx = ? ");
			param.add(reCode.getYalx());
		}
		
		if(StringUtils.isNotBlank(reCode.getGlbm())){
			if(reCode.getGlbm().length() != 1){
				sb.append(" and g.dwdm = ? ");
				param.add(reCode.getGlbm());
			}
		}
		sb.append("order by t.yabh desc");
		return this.findPageForMap(sb.toString(), param.toArray(),
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
		List param = new ArrayList<>();
		
		StringBuffer sb = new StringBuffer("insert into   CODE_GATE_RECORD( kdbh,fxbh,yabh, yanr , yxzt , kssj, jssj , wdlj,bjd,jsdw, yalx)  ");
		sb.append(" values(?,?,?,?,?,?,?,?,?,?)");
		if(code.getKdbh()==null){
			param.add("''");
		}else{
			param.add(code.getKdbh());
		}
		if(code.getFxbh()==null){
			param.add("''");
		}else{
			param.add(code.getFxbh());
		}
		param.add(yabh+"||SEQ_RECODE_XH.NEXTVAL,");
		if(code.getYanr()==null){
			param.add("''");
		}else{
			param.add(code.getYanr());
		}
		if(code.getYxzt()==null){
			param.add("''");
		}else{
			param.add(code.getYxzt());
		}

		
		if(StringUtils.isNotBlank(code.getKssj())){
			sb.append(" to_date(?,'yyyy-mm-dd hh24:mi:ss'),");
			param.add(code.getKssj());
		}else{
			sb.append("null,");
		}
		
		if(StringUtils.isNotBlank(code.getJssj())){
			sb.append("to_date(?,'yyyy-mm-dd hh24:mi:ss'),");
			param.add(code.getJssj());
		}else{
			sb.append("null,");
		}
		if(code.getWdlj()==null){
			param.add("''");
		}else{
			param.add(code.getWdlj());
		}
		if(code.getBjd()==null){
			param.add("''");
		}else{
			param.add(code.getBjd());
		}
		if(code.getJsdw()==null){
			param.add("''");
		}else{
			param.add(code.getJsdw());
		}
		if(code.getYalx()==null){
			param.add("''");
		}else{
			param.add(code.getYalx());
		}

		
		return this.jdbcTemplate.update(sb.toString());
	}

	public int updateReCode(ReCode code) throws Exception {
		List param = new ArrayList<>();
		StringBuffer sb = new StringBuffer("update CODE_GATE_RECORD set ");
		sb.append(" kdbh = ?, fxbh = ?, yalx = ?, yxzt = ?, kssj = to_date(?,'yyyy-mm-dd " +
				"hh24:mi:ss'), ")
		  .append(" jssj = to_date(?,'yyyy-mm-dd hh24:mi:ss'), ")
		  .append(" yanr = ?,");
		param.add(code.getKdbh());
		param.add(code.getFxbh());
		param.add(code.getYalx());
		param.add(code.getYxzt());
		param.add(code.getKssj());
		param.add(code.getJssj());
		param.add(code.getYanr());
		
		if(StringUtils.isNotBlank(code.getWdlj())){
			sb.append(" wdlj = ?, ");
			param.add(code.getWdlj());
		}
		sb.append(" bjd = ?  ");
		if(code.getBjd()==null){
			param.add("''");
		}else{
			param.add(code.getBjd());
		}

		sb.append(" where yabh = ?  ");
		param.add(code.getYabh());
		
		return this.jdbcTemplate.update(sb.toString(),param.toArray());
	}

	public List getReCodeListForKdbh(String kdbh) throws Exception {
		String sql = "select * from code_gate_record where kdbh = ?";
		
		return this.queryForList(sql, new Object[]{kdbh}, ReCode.class);
	}
	
	
}
