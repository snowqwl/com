package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.opensymphony.oscache.util.StringUtil;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.CaseGroupEntity;
import com.sunshine.monitor.system.analysis.bean.CaseInfo;
import com.sunshine.monitor.system.analysis.bean.CaseSpecial;
import com.sunshine.monitor.system.analysis.bean.PoliceInfor;
import com.sunshine.monitor.system.analysis.dao.CaseSpecialDao;

@Repository("caseSpecialDao")
public class CaseSpecialDaoImpl extends BaseDaoImpl implements CaseSpecialDao {

	public Map<String, Object> findCaseByPage(Map<String, Object> filter)
			throws Exception {
		StringBuffer sb = new StringBuffer(
				" Select * from T_YP_KYXXZY  where 1=1 and BY2 = 1");
		sb.append(" Order by RKSJ desc ");
		Map<String, Object> result = this.findPageForMap(sb.toString(), Integer
				.parseInt(filter.get("page").toString()), Integer
				.parseInt(filter.get("rows").toString()), CaseSpecial.class);
		return result;
	}

	public Map<String, Object> findPoliceByPage(Map<String, Object> filter)
			throws Exception {
		StringBuffer sb = new StringBuffer(
				"Select y.*,t.jqmc from t_yp_kyxxzy y,(Select kybh,jqmc from T_YP_MIDDLE m ,t_yq_jqxxzy j");
		sb.append(" Where m.ztbh = j.jqh and m.ztlx = 2 ");
		if (filter.get("jqh") != null && !"".equals(filter.get("jqh"))) {
			if (!filter.get("jqh").equals("1")) {
				sb.append(" and jqh = '" + filter.get("jqh").toString() + "'");
			}
		}
		sb.append(" ) t Where y.kyjlxh = t.kybh ");

		sb.append(" Order by RKSJ desc ");
		Map<String, Object> result = this.findPageForMap(sb.toString(), Integer
				.parseInt(filter.get("page").toString()), Integer
				.parseInt(filter.get("rows").toString()), CaseSpecial.class);
		return result;
	}

	public Map<String, Object> findCaseInfoByPage(Map<String, Object> filter)
			throws Exception {
		StringBuffer sb = new StringBuffer(
				"Select y.*,t.ajzymc from t_yp_kyxxzy y,(Select kybh,ajzymc from T_YP_MIDDLE m ,T_YP_AJXXZY a");
		sb.append(" Where m.ztbh = a.ajzyxh and m.ztlx = 3 ");
		if (filter.get("ajzyxh") != null && !"".equals(filter.get("ajzyxh"))) {
			if (!filter.get("ajzyxh").equals("1")) {
				sb.append(" and ajzyxh = '" + filter.get("ajzyxh").toString()
						+ "'");
			}
		}
		sb.append(" ) t Where y.kyjlxh = t.kybh ");

		sb.append(" Order by RKSJ desc ");
		Map<String, Object> result = this.findPageForMap(sb.toString(), Integer
				.parseInt(filter.get("page").toString()), Integer
				.parseInt(filter.get("rows").toString()), CaseSpecial.class);
		return result;
	}

	public Map<String, Object> findExemplaryCaseByPage(
			Map<String, Object> filter) throws Exception {
		StringBuffer sb = new StringBuffer(
				"Select y.*,t.zamc from t_yp_kyxxzy y,(Select kybh,zamc from T_YP_MIDDLE m ,T_YP_ZAXX a");
		sb.append(" Where m.ztbh = a.zabh and m.ztlx = 1");
		if (filter.get("ztzyxh") != null && !"".equals(filter.get("ztzyxh"))) {
			if (!filter.get("ztzyxh").equals("1")) {
				sb.append(" and zabh = '" + filter.get("ztzyxh").toString()
						+ "'");
			}
		}
		sb.append(") t Where y.kyjlxh = t.kybh ");

		sb.append(" Order by RKSJ desc ");
		Map<String, Object> result = this.findPageForMap(sb.toString(), Integer
				.parseInt(filter.get("page").toString()), Integer
				.parseInt(filter.get("rows").toString()), CaseSpecial.class);
		return result;
	}

	public List<CaseInfo> getCaseInfoList() throws Exception {
		StringBuffer sb = new StringBuffer(
				"Select ajzyxh,ajzymc from T_YP_AJXXZY");
		List<CaseInfo> list = this.queryForList(sb.toString(), CaseInfo.class);
		return list;
	}

	public List<CaseGroupEntity> getExemplaryCaseList() throws Exception {
		StringBuffer sb = new StringBuffer("Select zabh,zamc from T_YP_ZAXX");
		List<CaseGroupEntity> list = this.queryForList(sb.toString(),
				CaseGroupEntity.class);
		return list;
	}

	public List<PoliceInfor> getPoliceList() throws Exception {
		StringBuffer sb = new StringBuffer("Select jqh,jqmc from T_YQ_JQXXZY");
		List<PoliceInfor> list = this.queryForList(sb.toString(),
				PoliceInfor.class);
		return list;
	}

	public int saveCaseDoubtfulExceplary(String[] zazyxh, String[] ajzyxh,
			String[] jqh, String[] kyzyxh) {
		int result = 0;
		int zazyResult = this.saveCaseInfo(zazyxh, kyzyxh, "1");
		int kyzyResult = this.saveCaseInfo(ajzyxh, kyzyxh, "3");
		int jqhResult = this.saveCaseInfo(jqh, kyzyxh, "2");
		if (zazyResult > 0 || kyzyResult > 0 || jqhResult > 0) {
			result = 1;
		}
		this.updateCaseInfo(kyzyxh);
		return result;
	}

	public int saveCaseInfo(CaseSpecial caseSpecial) {
		StringBuffer sb = new StringBuffer(
				"Insert into T_YP_KYXXZY(KYJLXH,ZYLX,RKSJ,TP1,BY1) values('4400' || SEQ_TP_KYXXZY.nextval,1,sysdate,'ftp://10.47.254.71/yp/' || SEQ_TP_KYXXZY.currval || '.jpg','"
						+ caseSpecial.getBy1() + "')");
		return this.jdbcTemplate.update(sb.toString());
	}

	public int saveCaseInfo(String[] paramStr1, String[] paramStr2,
			String paramStr3) {
		String[] strSql = null;
		int result = 0;
		if (paramStr1 != null && paramStr2 != null && !"".equals(paramStr1)
				&& !"".equals(paramStr2)) {
			strSql = new String[paramStr1.length * paramStr2.length];
			int c = 0;
			for (int i = 0; i < paramStr1.length; i++) {
				for (int j = 0; j < paramStr2.length; j++) {
					StringBuffer sb = new StringBuffer(
							"Insert into T_YP_MIDDLE(ZTBH,KYBH,ZTLX) values('"
									+ paramStr1[i] + "','" + paramStr2[j]
									+ "','" + paramStr3 + "')");
					strSql[c] = sb.toString();
					c++;
				}
			}
			try {
				this.jdbcTemplate.batchUpdate(strSql);
				result = 1;
			} catch (Exception e) {
				e.printStackTrace();
				result = -1;
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public int saveExemplaryCaseInfo(Map filter) {
		int result = 0;
		List<String> list = null;
		String kyxxzy = "";
		String zabh = null;
		String[] strKyxxSQL = null;
		String[] strSQL = null;
		if (filter != null && filter.size() > 0) {
			zabh = (String) filter.get("zabh");
			list = (List<String>) filter.get("pic");
			if (list != null && list.size() > 0 && zabh != null
					&& !"".equals(zabh)) {
			}
			strKyxxSQL = new String[list.size()];
			strSQL = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				kyxxzy = this.jdbcTemplate.queryForObject(
						"Select SEQ_TP_KYXXZY.nextVal kyxxzy from dual",
						String.class);
				StringBuffer sb = new StringBuffer(
						"Insert into T_YP_KYXXZY(KYJLXH,TP1,RKSJ,BY2) values(" + kyxxzy + ",'"
								 + list.get(i) + "',sysdate,0)");
				StringBuffer sbStr = new StringBuffer(
						"Insert into T_YP_MIDDLE(KYBH,ZTBH,ZTLX) values("
								+ kyxxzy + ",'" + zabh + "',1)");
				strKyxxSQL[i] = sb.toString();
				strSQL[i] = sbStr.toString();
			}
			try {
				this.jdbcTemplate.batchUpdate(strKyxxSQL);
				this.jdbcTemplate.batchUpdate(strSQL);
				result = 1;
			}catch(Exception e) {
				e.printStackTrace();
				result = -1;
			}
		}
		return result;

	}

	public int deleteCaseInfo(String[] kyzyxh) {
		int result = 0;
		String[] strSql = new String[kyzyxh.length];
		if (kyzyxh.length > 0) {
			for (int i = 0; i < kyzyxh.length; i++) {
				StringBuffer sb = new StringBuffer(
						"Update T_YP_KYXXZY set by2 = '0' Where KYJLXH = '"
								+ kyzyxh[i] + "'");
				strSql[i] = sb.toString();
			}
		}
		try {
			this.jdbcTemplate.batchUpdate(strSql);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
		}
		return result;

	}

	public int updateCaseInfo(String[] kyzyxh) {
		int result = 0;
		String[] strSql = new String[kyzyxh.length];
		if (kyzyxh != null && !"".equals(kyzyxh)) {
			for (int i = 0; i < strSql.length; i++) {
				StringBuffer sb = new StringBuffer(
						"Update T_YP_KYXXZY set BY2='0' Where KYJLXH='"
								+ kyzyxh[i] + "'");
				strSql[i] = sb.toString();
			}
			try {
				this.jdbcTemplate.batchUpdate(strSql);
				result = 1;
			} catch (Exception e) {
				e.printStackTrace();
				result = -1;
			}
		}
		return result;
	}

	
	public Map<String, Object> quicksearchListByPage(Map<String, Object> filter)
			throws Exception {
		String by2=String.valueOf(filter.get("zslx"));
		StringBuffer sb = new StringBuffer(" select t.*,'"+by2+"' as tp2 from ( ");
		
//		可疑
		sb.append("  Select t.KYJLXH,t.RKSJ,t.TP1,'1' as by1 from T_YP_KYXXZY t  where 1=1 and BY2 = 1");
		sb.append(" UNION ");
		
//		警情
		sb.append(" Select y.KYJLXH,y.RKSJ,y.TP1,'2' as by1 from t_yp_kyxxzy y,(Select kybh,jqmc from T_YP_MIDDLE m ,t_yq_jqxxzy j");
		sb.append(" Where m.ztbh = j.jqh and m.ztlx = 2 )");
		sb.append(" UNION ");
		
//	专案
		sb.append(" Select y.KYJLXH,y.RKSJ,y.TP1,'3' as by1 from t_yp_kyxxzy y,(Select kybh,ajzymc from T_YP_MIDDLE m ,T_YP_AJXXZY a");
		sb.append(" Where m.ztbh = a.ajzyxh and m.ztlx = 3 )");
		sb.append(" UNION ");
		sb.append(	"Select y.KYJLXH,y.RKSJ,y.TP1,'4' as by1 from t_yp_kyxxzy y,(Select kybh,zamc from T_YP_MIDDLE m ,T_YP_ZAXX a");
		sb.append(" Where m.ztbh = a.zabh and m.ztlx = 1 ) ");
		sb.append("    ) t  where 1=1 ");
		
		String lrlx=String.valueOf(filter.get("lrlx"));
		String lrsj=String.valueOf(filter.get("lrsj"));
//		条件查询
		if(""!=lrlx && null!=lrlx && "null"!=lrlx){
			sb.append("    and by1= '"+lrlx+"'");
		}
		if(""!=lrsj && null!=lrsj && "null"!=lrsj){
			sb.append(" and rksj >= to_date('"+lrsj+"','yyyy-mm-dd hh24:mi:ss')");
		}
		sb.append(" Order by t. RKSJ desc ");
		Map<String, Object> result = this.findPageForMap(sb.toString(), Integer
				.parseInt(filter.get("page").toString()), Integer
				.parseInt(filter.get("rows").toString()), CaseSpecial.class);
		return result;
	}
}
