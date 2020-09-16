package com.sunshine.monitor.system.gate.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.gate.bean.CodeDirect;
import com.sunshine.monitor.system.gate.bean.CodeGateCd;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.dao.DirectDao;

@Repository("directDao")
public class DirectDaoImpl extends BaseDaoImpl implements DirectDao {
	
	@Override
	public String getTableName() {
		return "code_gate_extend";
	}
	
/**
 * 废弃代码？全局检索 无调用
 */
	public Map<String,Object> findDirectForMap(Map filter, CodeDirect direct){
		String sql = "";
		

		List vl=new ArrayList<>();
		if ((direct.getFxbh() != null) && (direct.getFxbh().length() > 0)) {
			sql += " and fxbh = ?";
			vl.add(direct.getFxbh());
		}
		if ((direct.getFxmc() != null) && (direct.getFxmc().length() > 0)) {
			sql += " and fxmc like ?";
			vl.add("%"+direct.getFxbh()+"%");
		}
		if (!sql.equals(""))
			sql = " (" + sql.substring(5, sql.length()) + ") ";
		if (!sql.equals(""))
			sql = " and " + sql;
		sql = "select FXBH,FXLX,FXMC,to_char(GXSJ,'yyyy-mm-dd hh24:mi') gxsj,BZ from code_direct where length(fxbh)!=10 " + sql + "  order by gxsj desc";
		Map<String,Object> map = this.findPageForMap(sql, 
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString())
		);
		
		return map;
		
	}

	public List getDirects() {
		
		/*String sql = "select * from code_direct where length(fxbh)!=10 order by fxmc";
		return this.queryForList(sql, CodeDirect.class);*/
		
		List<CodeDirect> listDirect = new ArrayList<CodeDirect>();
		
		String sql = "select *  from code_gate_extend where fxmc is not null ";
		List<CodeGateExtend> listExtend = this.queryForList(sql, CodeGateExtend.class);
		for(CodeGateExtend extend : listExtend){
			CodeDirect direct = new CodeDirect();
			direct.setFxbh(extend.getFxbh());
			direct.setFxmc(extend.getFxmc());
			direct.setFxlx(extend.getFxlx());
			
			listDirect.add(direct);
		}
		
		return listDirect;
	}
	
	public List<CodeDirect> getDirectsByfxbh(String fxbh) {
		/*String sql = "select * from code_direct where fxbh='" + fxbh + "'";
		return this.queryForList(sql, CodeDirect.class);*/
		
		String sql = "select *  from code_gate_extend where fxbh=?";
		
		List<CodeDirect> listDirect = new ArrayList<CodeDirect>();
		List<CodeGateExtend> listExtend = this.queryForList(sql, new Object[]{fxbh}, CodeGateExtend.class);
		for(CodeGateExtend extend : listExtend){
			CodeDirect direct = new CodeDirect();
			direct.setFxbh(extend.getFxbh());
			direct.setFxmc(extend.getFxmc());
			direct.setFxlx(extend.getFxlx());
			
			listDirect.add(direct);
		}
		
		return listDirect;
		
	}

	public CodeDirect getDirect(String fxbh) throws Exception {
		/*String sql = "select * from code_direct where fxbh='" + fxbh + "'";
		List<CodeDirect> list = this.queryList(sql, CodeDirect.class);
		if(list.size()>0){
			return list.get(0);
		}
		return null;*/
		
		
		String sql = "select *  from code_gate_extend where fxbh=?";
		
		CodeDirect direct = null;
		List<CodeGateExtend> listExtend = this.queryForList(sql, new Object[]{fxbh}, CodeGateExtend.class);
		for(CodeGateExtend extend : listExtend){
			direct = new CodeDirect();
			direct.setFxbh(extend.getFxbh());
			direct.setFxmc(extend.getFxmc());
			direct.setFxlx(extend.getFxlx());
			
		}
		
		
		return direct;
	}
	
	public boolean saveDirect(CodeDirect direct, String fxbh) {
		String sql = "";
		String seq = "";
		Object[] obj = null;
		 List vl=new ArrayList<>();
		if(fxbh.equals("")||fxbh.equals(null)){
			/*
			obj = new Object[] { 
					direct.getFxbh(),
					direct.getFxlx(),
					direct.getFxmc(),
					direct.getBz()
				};
	 			*/
		    seq = this.jdbcTemplate.queryForObject("select SEQ_FXBH_XH.nextval from dual",String.class);
            for(int i=seq.length();i<5;i++){
            	seq="0"+seq;
            }        
           
            direct.setFxbh(direct.getFxbh()+direct.getXlh()+direct.getFxxh());
//            vl.add(direct.getFxbh());
            vl.add(direct.getFxlx());
            vl.add(direct.getFxmc());
            vl.add(direct.getBz());
            direct.setXlh(seq);
          
			sql = "insert into Code_direct(fxbh,fxlx,fxmc,bz,gxsj) " +
			" values(?,?,?,?,sysdate)";
		}else {
          vl.add(direct.getFxbh());
          vl.add(direct.getFxlx());
          vl.add(direct.getFxmc());
          vl.add(direct.getBz());
           vl.add(fxbh);
			sql = "update Code_direct set fxbh=? ,fxlx='? ,fxmc=? ,bz=? ,gxsj=sysdate where fxbh=?";
		}
		return this.jdbcTemplate.update(sql,vl.toArray())>0?true:false;
	}

	public List getDirectBySbbh(String sbbh) {
		String sql = "select * from code_direct where fxbh in (select fxbh1 from code_device where sbbh=? union select fxbh2 from code_device where sbbh=?)";
		return this.queryForList(sql,new String[]{sbbh,sbbh}, CodeDirect.class);
	}

	public List getDirectByKdbh(String kdbh) {
		
	
		String sql = "select *  from code_gate_extend where kdbh=?";
		List<CodeDirect> listDirect = new ArrayList<CodeDirect>();
		List<CodeGateExtend> listExtend = this.queryForList(sql, new Object[]{kdbh},CodeGateExtend.class);
		for(CodeGateExtend extend : listExtend){
			CodeDirect direct = new CodeDirect();
			direct.setFxbh(extend.getFxbh());
			direct.setFxmc(extend.getFxmc());
			direct.setFxlx(extend.getFxlx());
			
			listDirect.add(direct);
		}
		
		return listDirect;
		
		
	}
	
	/**
	 * 旧版卡口方向
	 */
    public List getOldDirectByKdbh(String kdbh) {
		
		
		String sql = "Select d.* from dc_code_direct d,dc_code_device c Where (d.fxbh=c.fxbh1 or d.fxbh = c.fxbh2) and kdbh=?";
		
		List<CodeDirect> listDirect = new ArrayList<CodeDirect>();
		List<CodeGateExtend> listExtend = this.queryForList(sql,new String[]{kdbh}, CodeGateExtend.class);
		for(CodeGateExtend extend : listExtend){
			CodeDirect direct = new CodeDirect();
			direct.setFxbh(extend.getFxbh());
			direct.setFxmc(extend.getFxmc());
			direct.setFxlx(extend.getFxlx());
			
			listDirect.add(direct);
		}
		
		return listDirect;
		
		
	}

	public String getDirectName(String fxbh) {
		 

		//String sql = "select *  from code_gate_extend where fxbh='" + fxbh + "'";
		String sql = "select *  from code_gate_extend where fxbh=?";
		List<CodeGateExtend> list = this.queryForList(sql, new Object[]{fxbh}, CodeGateExtend.class);
		if (list.size()>0) {
			return list.get(0).getFxmc();
		}
		return fxbh;
		
	}
	
	/**
	 * 旧版卡口方向名称
	 * @param fxbh
	 * @return
	 * @throws Exception
	 */
	public String getOldDirectName(String fxbh) {
		
		//String sql = "select *  from dc_code_direct where fxbh='" + fxbh + "'";
		try {
			//String sql = "select *  from v_gate_direct where fxbh=?";
			String sql = "select *  from code_gate_extend where fxbh=?";
			List<CodeGateExtend> list = this.queryForList(sql, new Object[]{fxbh}, CodeGateExtend.class);
			if (list.size()>0) {
				return list.get(0).getFxmc();
			}
		} catch (Exception e) {
			log.warn("方向翻译："+e);
		}
		return fxbh;
		
	}

	public boolean saveDirect(CodeDirect direct) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<CodeGateCd> getGateCdListByFxbh(String fxbh) throws Exception {
		
		//String sql = "select *  from code_gate_cd where fxbh='" + fxbh + "'  order by cdbh ";
		String sql = "select *  from code_gate_cd where fxbh=?  order by cdbh ";
		List<CodeGateCd> list = this.queryForList(sql, new Object[]{fxbh},CodeGateCd.class);
		
		return list;
	}

}