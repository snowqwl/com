package com.sunshine.monitor.comm.maintain.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.MaintainHandle;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.maintain.dao.MaintainDao;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.manager.bean.Department;

@Repository("maintainDao")
public class MaintainDaoImpl extends BaseDaoImpl implements MaintainDao {

	public Map findMaintainForMap(Map filter,String bzlx)throws Exception {
		StringBuffer sql = null;
		
		if(bzlx != null && bzlx.equals("1")){
			StringBuffer sb = new StringBuffer(" Select CSTR_COL02 as KDMC,CSTR_COL01 as KDBH, CSTR_COL03 as SBMC, PK1 as SBBH,to_char(DATE_COL01,'yyyy-mm-dd hh24:mi:ss') as GCSJ,CSTR_COL05 as zdcxsj");
			sb.append(" from monitor.T_MONITOR_JG_INTEGRATE_DETAIL Where KHFA_ID = '36' and workItem_ID in (");
			sb.append(" Select max(to_number(workItem_ID)) from monitor.T_MONITOR_JG_INTEGRATE_DETAIL ");
			sb.append(" Where KHFA_ID='36' group by date_col02,CSTR_COL06)");
			
			sql = new StringBuffer(" select  t.*   from  (").append(sb).append(")t, CODE_GATE_EXTEND extend   ");
			sql.append("  where t.kdbh = extend.kdbh  and  (extend.bz1 is null  or  (sysdate - to_date(extend.bz1,'yyyy-mm-dd hh24:mi:ss'))*24 > 24)  ");
			sql.append("  order by  zdcxsj asc");
		}else{
			
			sql = new StringBuffer(" select * from jm_maintain_handle ");
			
			if(!"0".equals(bzlx)){
				sql.append("   where gzlx = '").append(bzlx).append("' ");
			}
			sql.append("  order by  gzsj ");
			
		}
		
		return this.findPageForMap(sql.toString(), 
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
	}

	public void setby1ForGate(String kdbh)throws Exception {
		
		
		StringBuffer sb = new StringBuffer(" update CODE_GATE_EXTEND  set bz1 = '")
							.append(Common.getNow()).append("' ");
		
		sb.append(" where kdbh = '").append(kdbh).append("' ");
		
		this.jdbcTemplate.update(sb.toString());
	}
	
	public void setCodeGateState(String kdbh, String fxbh, String kkzt,
			String sbzt) throws Exception {
		List<CodeGateExtend> list = null;
		StringBuffer sb = new StringBuffer(
				"update CODE_GATE_EXTEND set sbzt = ").append(sbzt).append(
				" Where KDBH='").append(kdbh).append("' and FXBH='").append(
				fxbh).append("'");
		this.jdbcTemplate.update(sb.toString());
		if (sbzt != null && sbzt.equals("1")) {
			sb = new StringBuffer(
					"Select * from CODE_GATE_EXTEND t Where KDBH='").append(
					kdbh).append("'");
			list = this.queryForList(sb.toString(), CodeGateExtend.class);
		}
		boolean flag = true;
		if (list != null) {
			for (CodeGateExtend codeGate : list) {
				if (codeGate.getSbzt() != null
						&& codeGate.getSbzt().equals("0")) {
					flag = false;
				}
			}
		}

		if (flag) {
			sb = new StringBuffer("Update CODE_GATE set kkzt = ")
					.append(kkzt).append(" Where KDBH='").append(kdbh).append(
							"'");
			this.jdbcTemplate.update(sb.toString());
		}
	}

	public Map findMaintainHandleForMap(Map filter,MaintainHandle handle,Department department)throws Exception {
		/*
		StringBuffer sb = new StringBuffer("select  a.*  from JM_MAINTAIN_HANDLE a,CODE_GATE g  where 1=1  ");
		sb.append(" and a.kdbh=g.kdbh ")
		  .append(" and g.dwdm in (select xjjg from frm_prefecture where dwdm = '").append(department.getGlbm()).append("')");
		  */
		StringBuffer sb = new StringBuffer("select a.* from JM_MAINTAIN_HANDLE a where exists (select b.kdbh from CODE_GATE b where a.kdbh = b.kdbh ");
		if(StringUtils.isNotBlank(handle.getKkbm())) {
			sb.append(" and b.dwdm = '").append(handle.getKkbm()).append("')");
		} else {
			sb.append("and exists (select c.xjjg from frm_prefecture c where c.dwdm = '").append(department.getGlbm()).append("' and b.dwdm = c.xjjg)) ");
		}
		if(StringUtils.isNotBlank(handle.getSfcl())){
			sb.append("  and  sfcl = '").append(handle.getSfcl()).append("' ");
		}
		
		/*else{
			sb.append(" and  sfcl = '0' ");
		}*/
		if(StringUtils.isNotBlank(handle.getGzlx())){
			sb.append("  and  gzlx = '").append(handle.getGzlx()).append("'  ");
		}
		
		if(StringUtils.isNotBlank(handle.getKssj())){
			String reg = "yyyy-mm-dd";
			if(handle.getKssj().length() > 13 ){
				reg += " hh24:mi:ss";
			}
			
			sb.append("  and clsj >= to_date('").append(handle.getKssj()).append("','").append(reg).append("')  ");
		}
		
		if(StringUtils.isNotBlank(handle.getJssj())){
			String reg = "yyyy-mm-dd";
			if(handle.getJssj().length() > 13 ){
				reg += " hh24:mi:ss";
			}
			
			sb.append("  and clsj <= to_date('").append(handle.getJssj()).append("','").append(reg).append("')  ");
		}
		sb.append("  order by jcsj desc ");
		return this.findPageForMap(sb.toString(), 
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
	}

	public MaintainHandle getMaintainHandleForId(String id)throws Exception {
		String sql = "select *  from jm_maintain_handle  where xh = '"+ id +"'";
		
		List<MaintainHandle> list = this.queryForList(sql, MaintainHandle.class);
		if(list != null && list.size() > 0 )
			return list.get(0);
		else
			return null;
		
	}

	public String saveMaintainHandle(MaintainHandle handle)throws Exception {
		StringBuffer sb  = null;
		
		String seqString="''";
		if(StringUtils.isBlank(handle.getXh())){
			String seq="SELECT  SEQ_MAINTAINHANDLE_XH.NEXTVAL seq FROM dual ";
			List seqlist=this.jdbcTemplate.queryForList(seq);
			if(seqlist.size()>0){
				Map map=(Map)seqlist.get(0);
				seqString=map.get("seq").toString();
			}
			sb = new StringBuffer("insert into jm_maintain_handle(xh,gzlx,gzsj,gzqk,sbr,sbrmc,sbrdwdm,jcsj,ywdh,kdbh,fxbh)  values('"+seqString+"','");
			sb.append(handle.getGzlx()).append("',to_date('").append(handle.getGzsj()).append("','yyyy-mm-dd hh24:mi:ss'),'")
			.append(handle.getGzqk()).append("','").append(handle.getSbr()).append("','").append(handle.getSbrmc())
			.append("','").append(handle.getSbrdwdm())
			.append("',sysdate,'")
			.append(""+handle.getYwdh()+"','")
			.append(""+handle.getKdbh()+"','")
			.append(""+handle.getFxbh()+"')");
		
		}else{
			sb = new StringBuffer(" update jm_maintain_handle set fksj = sysdate, clsj = to_date('");
			sb.append(handle.getClsj()).append("','yyyy-mm-dd hh24:mi:ss'),clqk = '").append(handle.getClqk()).append("'  ");
			sb.append(" ,fkr = '").append(handle.getSbr()).append("',fkrmc = '").append(handle.getSbrmc()).append("'  ");
			sb.append(",fkrdwdm = '").append(handle.getFkrdwdm()).append("' ");
			
			if(StringUtils.isNotBlank(handle.getSfcl())){
				sb.append(",sfcl = '").append(handle.getSfcl()).append("'  ");
			}
			sb.append(" where xh = '").append(handle.getXh()).append("'  ");
		}
		this.jdbcTemplate.update(sb.toString());
		return seqString;
	}
	
	
	public int saveDxsj(String value,String values)
	throws Exception {
		String sql ="insert into frm_communication("+value+") values ("+values+")" 
			;
		return this.jdbcTemplate.update(sql);
	}
	
	public List<CodeGateExtend> getGzCodeGateInfo() throws Exception {
		List<CodeGateExtend> listResult = null;
		StringBuffer sb = new StringBuffer(
				"Select PK1 as kdbh,CSTR_COL05 as fxbh from Monitor.T_MONITOR_JG_INTEGRATE_DETAIL")
				.append(" Where WORKITEM_ID = (")
				.append(
						" Select max(WORKITEM_ID) from Monitor.T_MONITOR_JG_INTEGRATE_DETAIL")
				.append(" Where KHFA_ID='39')");
		listResult = this.queryForList(sb.toString(), CodeGateExtend.class);
		return listResult;
	}

}
