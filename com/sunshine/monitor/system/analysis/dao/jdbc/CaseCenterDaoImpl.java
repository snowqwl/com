package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.CaseEntity;
import com.sunshine.monitor.system.analysis.bean.CaseGroupEntity;
import com.sunshine.monitor.system.analysis.dao.CaseCenterDao;

@Repository("caseCenterDao")
public class CaseCenterDaoImpl extends BaseDaoImpl implements CaseCenterDao {
	
	public Map<String, Object> findCaseByPage(CaseEntity entity,
			Map<String, Object> filter) throws Exception {
		StringBuffer sb = new StringBuffer(" select * from t_yp_ajxx where 1=1 ");
		if(entity.getLar() != null && entity.getLar().length()>0) {
			sb.append(" and lar = '").append(entity.getLar()).append("'");
		}
		if(entity.getAjlb()!=null&&entity.getAjlb().length()>0){
			sb.append(" and ajlb = '").append(entity.getAjlb()).append("'");
		}
		if(entity.getAjmc()!=null&&entity.getAjmc().length()>0){
			sb.append(" and ajmc like '%").append(entity.getAjmc()).append("%'");
		}
		if(entity.getKssj()!=null&&entity.getKssj().length()>0){
			sb.append(" and afsj >　to_date('").append(entity.getKssj()).append("','yyyy-MM-dd hh24:mi:ss')");
		}
		if(entity.getJssj()!=null&&entity.getJssj().length()>0){
			sb.append(" and afsj <=　to_date('").append(entity.getJssj()).append("','yyyy-MM-dd hh24:mi:ss')");
		}
		sb.append(" order by lasj desc");
		Map<String, Object> result = this.findPageForMap(sb.toString(), Integer
				.parseInt(filter.get("page").toString()), Integer
				.parseInt(filter.get("rows").toString()), CaseEntity.class);
		return result;
	}

	public List<Map<String, Object>> queryAnalysisProjectList(
			CaseGroupEntity caseEntity) throws Exception {
		String zabh="";
		if(StringUtils.isNotBlank(caseEntity.getZabh())){
			zabh = " and zabh ='"+caseEntity.getZabh()+"'";
		}
		String sql=" select * from t_yp_ztxx where 1=1"
			+zabh
			+" order by fxsj desc"
			;
		List result=this.jdbcTemplate.queryForList(sql);
		return result;
	}

	public CaseEntity getCaseDetail(CaseEntity entity) throws Exception {
		return this.queryDetail("t_yp_ajxx","ajbh",entity.getAjbh(),CaseEntity.class);
	}

	public Map<String,Object> findCaseGroupByPage(CaseGroupEntity entity,
			Map<String, Object> filter) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from t_yp_zaxx where 1=1");
		if(entity!=null){
			if(entity.getFzrjh() != null && entity.getFzrjh().length()>0) {
				sb.append(" and fzrjh = '").append(entity.getFzrjh()).append("'");
			}
			if(entity.getZabh()!=null&&entity.getZabh().length()>0){
				sb.append(" and zabh = '").append(entity.getZabh()).append("'");
			}
			if(entity.getZamc()!=null&&entity.getZamc().length()>0){
				sb.append(" and zamc like '%").append(entity.getZamc()).append("%'");
			}
			if(entity.getZalx()!=null&&entity.getZalx().length()>0){
				sb.append(" and zalx = '").append(entity.getZalx()).append("'");
			}
			if(entity.getKssj()!=null&&entity.getKssj().length()>0){
				sb.append(" and lrsj > to_date('").append(entity.getKssj()).append("','yyyy-MM-dd hh24:mi:ss')");
			}
			if(entity.getJssj()!=null&&entity.getJssj().length()>0){
				sb.append(" and lrsj <= to_date('").append(entity.getJssj()).append("','yyyy-MM-dd hh24:mi:ss')");
			}
			sb.append(" order by zabh desc ");
		}
		
		return this.findPageForMap(sb.toString(),Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()),CaseGroupEntity.class);
	}

	public List<CaseEntity> getCaseList(String zabh) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select ajbh from t_yp_zaglxx where zabh = '")
		  .append(zabh)
		  .append("' order by ajbh ");
		return this.queryForList(sb.toString(),CaseEntity.class);
	}

	public String saveCaseGroup(CaseGroupEntity entity) throws Exception {
		StringBuffer sb = new StringBuffer();
		String zabh = "";
		if(entity.getZabh()==null){
			String seq = this.jdbcTemplate.queryForObject("SELECT SEQ_YP_ZAXX.NEXTVAL FROM DUAL", String.class);
			zabh = entity.getXzqh()+seq;
			sb.append("insert into t_yp_zaxx(zabh,zamc,zakssj,fzrjh,fzrxm,lrsj,xzqh,aqms)values('")
			  .append(zabh).append("','")
			  .append(entity.getZamc()).append("',")
			  .append("sysdate").append(",'")
			  .append(entity.getFzrjh()).append("','")
			  .append(entity.getFzrxm()).append("',")
			  .append("sysdate").append(",'")
			  .append(entity.getXzqh()).append("','")
			  .append(entity.getAqms()).append("')");
		}else{
			sb.append("update t_yp_zaxx set ");
			if(entity.getZamc()!=null&&entity.getZamc().length()>0){
				sb.append(" and zamc = '").append(entity.getZamc()).append("'");
			}
			if(entity.getFzrjh()!=null&&entity.getFzrjh().length()>0){
				sb.append(" and fzrjh = '").append(entity.getFzrjh()).append("'");
			}
			if(entity.getFzrxm()!=null&&entity.getFzrxm().length()>0){
				sb.append(" and fzrxm = '").append(entity.getFzrxm()).append("'");
			}
			if(entity.getAqms()!=null&&entity.getAqms().length()>0){
				sb.append(" and aqms = '").append(entity.getAqms()).append("'");
			}
			sb.append(" where zabh = '").append(entity.getZabh()).append("'");
		}
		 this.jdbcTemplate.update(sb.toString());
		 return zabh;
	}
	
	public void saveAssociateInfo(String zabh, String ajbh) throws Exception {
	    String sql = "select count(1) from t_yp_zaglxx where zabh='"+zabh+"' and ajbh='"+ajbh+"'";
		int count = this.jdbcTemplate.queryForInt(sql);
		if(count==0){
			String seq =  this.jdbcTemplate.queryForObject("SELECT SEQ_YP_ZAGLXX.NEXTVAL FROM DUAL", String.class);
			StringBuffer sb = new StringBuffer();
			sb.append("insert into t_yp_zaglxx(xh,zabh,ajbh)values('")
			  .append(seq).append("','")
			  .append(zabh).append("','")
			  .append(ajbh).append("')");
			 this.jdbcTemplate.update(sb.toString());
		}
	}
}
