package com.easymap.dao.track;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.easymap.util.Tools;

@Repository("SuggestQueryDao")
public class SuggestQueryDaoImpl {
	
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public List suggestBlackPhone(String querykind, String querykey, int startpos, int pageSize)
	{
		//int totalRows = -1;
		int pagenum = 15;

		int start = startpos;
		int end = startpos + pagenum - 1;
		
		StringBuilder sb = new StringBuilder();
		String type = "";
		
		if(querykind.equalsIgnoreCase("blackphonenum"))
			type = "phonenum";
		else if(querykind.equalsIgnoreCase("blackphoneimei"))
			type = "imei";
		else if(querykind.equalsIgnoreCase("blackphoneimsi"))
			type = "imsi";
		sb.append(" select rownum rn,id,").append(type).append(" TEXTKEY,phonename from t_phoneblack where 1=1  ");
		
		if(querykey != null && !querykey.equals(""))
		{
			sb.append(" and ").append(type).append(" like ? ");
		}
		
		
			String orgSql = "select * from (" + sb + " and rownum <= ?) where rn >= ?"  ;
			List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{"%"+querykey+"%",end,start});
			return queryForList;

	}
	
	public List suggestHphm(String querykind, String querykey, int startpos, int pageSize)
	{
		if(Tools.isEmpty(querykey))
			return null;
		querykey = Tools.getSafeStr(querykey);
		int pagenum = 15;
		
		int start = startpos;
		int end = startpos + pagenum - 1;
		querykey = Tools.getSafeSqlStr(querykey);
		int row = 10000;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select rownum rn,hphm,hpzl,hpzlmc from ");
		sb.append(" ( ");
		sb.append(" select hphm,hpzl,(select dmsm1 from frm_code where dmlb='030107' and dmz = rec.HPZL) hpzlmc ");
		sb.append(" from veh_passrec rec where rec.hphm like ? and rownum <= ? group by hphm,hpzl  ");
		sb.append(" ) ");                                       
		
		String orgSql = "select * from (" + sb + " where 1=1 and rownum <= ?) where rn >=?";
		List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{querykey+"%",row,end,start});
		return queryForList;
	}
}

