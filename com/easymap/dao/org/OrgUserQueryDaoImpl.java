package com.easymap.dao.org;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.easymap.pool.DBUtils;
import com.easymap.util.ElementUtils;

public class OrgUserQueryDaoImpl {

	public Document findOrgUserPage(String orgid, String userid, int startpos,int pageSize) {

		ResultSet rs = null;

		int totalRows=-1;//�ܼ�¼ 
		int pagenum=pageSize;//ÿҳ15����¼  
		
		int start = startpos;//��ǰ��¼��ʼ��
		int end= startpos + pagenum - 1;//��ǰ��¼������
		List vl=new ArrayList<>();
		Element items = null;
		try {
			
			StringBuilder sqlb = new StringBuilder();
			sqlb.append("select rownum rn,t.systemid,t.yxzqc,t.hczxjd,t.hczxwd,t.hcqrr,t.hcbhpcsdm,t.zdlxdh,t.hcbhpcsmc,t.ypcsdm,t.ypcsmc,to_char(t.hcqrsj,'yyyy-mm-dd hh24:mi:ss')hcqrsj,t.hcxxdz ")
			.append(" from b_dz_hc_xz t ")
			.append(" where ")
			.append(" 1 = 1  and t.ypcsdm ")
			.append(" in  ( ")
			.append(" select zzjgdm ")
			.append(" from t_zzjg1  ")
			.append(" connect by ssjgdm = prior zzjgdm ")
			.append(" and sfyx = '1' ")
			.append(" start with zzjgdm in (?) ) ");
			vl.add(orgid);
			
			String countSql = "SELECT COUNT(1) "+sqlb.substring(sqlb.indexOf("from"));

			rs = DBUtils.findResultData(countSql,vl);

			if (rs.next()){
				totalRows = rs.getInt(1);
				vl.add(end);
				vl.add(start);
				String orgSql = "select * from (" + sqlb + " and rownum <=?) where rn >= ?";
				items = ElementUtils.getResultElements(DBUtils.findResultData(orgSql,vl));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (SQLException e5) {
				e5.printStackTrace();
			}
		}
		
	
		items.setAttribute("display", totalRows <= 0?"false":"true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", orgid);
		items.setAttribute("startnum", start+"");
		items.setAttribute("endnum", end+"");
		items.setAttribute("orgsel", "true");
		
		return ElementUtils.responseDocument(items);

	}
}