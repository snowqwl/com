package com.easymap.dao.track;

import com.easymap.dao.ITrackDao;
import com.easymap.pool.DBUtils;
import com.easymap.util.ElementUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

public class TrackQueryDaoImpl implements ITrackDao {

	public Document findVagueQueryPage(String querytype,String querykey, int startpos, int pageSize) {

		ResultSet rs = null;

		int totalRows = -1;// 总记录
		int pagenum = 15;// 每页15条记录

		int start = startpos;// 当前记录起始数
		int end = startpos + pagenum - 1;// 当前记录结束数
		List vl=new ArrayList<>();
		Element items = null;
		try {

			StringBuilder whereSql = new StringBuilder();
			whereSql
			.append(" select * from b_dz_hc_xz t where 1=1 ");
			
			if(querykey != null && !querykey.equals(""))
			{
				vl.add("%"+querykey+"%");
				whereSql.append(" and ").append(querytype).append(" like ? ");
			}
			
			String countSql = " SELECT COUNT(1) FROM(" + whereSql + ") ";

			rs = DBUtils.findResultData(countSql,vl);
			if (rs.next()) totalRows = rs.getInt(1);
			vl.add(end);
			vl.add(start);
			String orgSql = "select * from (SELECT rownum rn, t.* FROM (" +whereSql+ ") t where rownum <= ?) where rn >= ?";
		    items = ElementUtils.getResultElements(DBUtils.findResultData(orgSql));

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

		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("startpos", Integer.toString(start));
		items.setAttribute("endpos", Integer.toString(end));
		items.setAttribute("pagenum", Integer.toString(pagenum));
		items.setAttribute("totalnum", Integer.toString(totalRows));
		
		return ElementUtils.responseDocument(items);
	}

	/**
	 * 历史轨迹查询  
	 */
	public Document findTrackQuery(String userid, String queryKey,String starttime,String endtime) 
	{
		int totalRows = -1;// 总记录
		int pagenum = 15;// 每页15条记录
		List vl=new ArrayList<>();
		Element items = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append(" select rownum rn,h.*,d.*,to_char(time, 'yyyy-mm-dd hh24:mi:ss') intime ")
			.append(" from view_st_gps_history h, view_st_police_device d ")
			.append(" where 1 = 1 ")
			.append(" and phone = mobileno ")
			.append(" and phone = ? ")
			.append(" and time between ").append(" to_date(? ,'yyyy-mm-dd hh24:mi:ss') ")
			.append(" and ").append(" to_date(? ,'yyyy-mm-dd hh24:mi:ss') ")
			.append(" order by time ");
			vl.add( queryKey );
			vl.add( starttime );
			vl.add( endtime );
			String countSql = " SELECT COUNT(*) FROM(" + sql + ") ";

			ResultSet rs = DBUtils.findResultData(countSql,vl);
			if (rs.next()) totalRows = rs.getInt(1);
		    items = ElementUtils.getResultElements(DBUtils.findResultData(sql.toString()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
//		items.setAttribute("pagenum", Integer.toString(pagenum));
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("track", "true");
		
		return ElementUtils.responseDocument(items);
	}
}
