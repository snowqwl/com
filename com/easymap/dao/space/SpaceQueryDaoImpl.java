package com.easymap.dao.space;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.easymap.dao.ISpaceQueryDao;
import com.easymap.pool.DBUtils;
import com.easymap.util.ElementUtils;
import com.easymap.util.MapDistance;
import com.easymap.util.TabName;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.manager.dao.SystemDao;

@Repository("SpaceQueryDao")
public class SpaceQueryDaoImpl implements ISpaceQueryDao {

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("jdbcScsTemplate")
	private JdbcTemplate jdbcScsTemplate;
	
	@Autowired
	private SystemDao systemDao;

	public Document jczFindCircleQuery(double x, double y, double radius,String layer,
			int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows = -1;// 总行数
		int pagenum = pageSize;// 页数
		int start = startpos;// 开始
		int end = startpos + pagenum - 1;//结束
		Element items = new Element("items");
		StringBuffer sb = new StringBuffer();;
		sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
		sb.append("(");
		try {
			sb.append(" select t.id jczbh,t.jczmc,t.zdsx zdlx,'' ip,'' port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,d.bmmc,t.ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'A' type ");
			sb.append(" from T_JCZXX t,frm_department d ").append(" where t.sspcs=d.glbm ");
			sb.append(")t ");
			sb.append(" where 1=1");
			sb.append(" and JD is not null and regexp_instr(JD,'[^.[:digit:]]')=0");
			sb.append(" and sqrt(power(JD-(?),2) + power(WD-(?),2)) < ?");
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new double[]{x,y,radius});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{x,y,radius,start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {

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
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("circleQuery", "true");
		return ElementUtils.responseDocument(items);
	}

	/**
	 * 卡口圆形查询
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 * @param startpos
	 * @param num
	 * @return
	 */
	public Document kkFindCircleQuery(double x, double y, double radius,String layer,
			int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows = -1;// 总行数
		int pagenum = pageSize;// 页数
		int start = startpos;// 开始
		int end = startpos + pagenum - 1;//结束
		Element items = new Element("items");
		StringBuffer sb = new StringBuffer();;
		sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
		sb.append("(");
		try {
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ");
			sb.append(" from ").append(TabName.CODE_Gate).append(" t,frm_department d ").append(" where t.dwdm=d.glbm ");
			sb.append(")t ");
			sb.append(" where 1=1");
			sb.append(" and JD is not null and regexp_instr(JD,'[^.[:digit:]]')=0");
			sb.append(" and sqrt(power(JD-(?),2) + power(WD-(?),2)) < ?");
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new double[]{x,y,radius});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new double[]{x,y,radius,start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {

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
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("circleQuery", "true");
		return ElementUtils.responseDocument(items);
	}
	
	/**
	 * 卡口圆形查询（改造）
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 * @param startpos
	 * @param num
	 * @return
	 * @date 2019/10/14
	 * @author hzy
	 */
	public Map kkFindCircleQuery1(double x, double y, double radius,String layer,
			int startpos, int pageSize) {
		Map reMap = new HashMap();	//返回结果
		ResultSet rs = null;
		int totalRows = -1;// 总行数
		int pagenum = pageSize;// 页数
		int start = startpos;// 开始
		int end = startpos + pagenum - 1;//结束
		StringBuffer sb = new StringBuffer();;
		sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
		sb.append("(");
		try {
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ");
			sb.append(" from ").append(TabName.CODE_Gate).append(" t,frm_department d ").append(" where t.dwdm=d.glbm ");
			sb.append(")t ");
			sb.append(" where 1=1");
			sb.append(" and JD is not null and regexp_instr(JD,'[^.[:digit:]]')=0");
			sb.append(" and sqrt(power(JD-(?),2) + power(WD-(?),2)) < ?");
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new double[]{x,y,radius});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new double[]{x,y,radius,start});
				reMap.put("rows",queryForList);
			}
		} catch (Exception e) {

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
		reMap.put("num", Integer.toString(totalRows));
		reMap.put("startnum", start + "");
		reMap.put("endnum", end + "");
		return reMap;
	}
	
	
	public Document gajkFindCircleQuery(double x, double y, double radius,String layer,
			int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows = -1;// 总行数
		int pagenum = pageSize;// 页数
		int start = startpos;// 开始
		int end = startpos + pagenum - 1;//结束
		Element items = new Element("items");
		StringBuffer sb = new StringBuffer();;
		sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
		sb.append("(");
		try {
			sb.append(" select t.bh jczbh,t.mc jczmc,t.sptlx zdlx,t.dvrip ip,t.port,t.dz jczdz,t.lxdh zdlxdh,t.jd jd,t.wd wd,t.lxr zdfzrxm,t.SSJYFQDM sspcs ,t.SSJYFQMC bmmc,t.SSJYFQMC ssfj,to_char(t.gxsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'C' type ");
			sb.append(" from t_monitor t ");
			sb.append(")t ");
			sb.append(" where 1=1");
			sb.append(" and JD is not null and regexp_instr(JD,'[^.[:digit:]]')=0");
			sb.append(" and sqrt(power(JD-(?),2) + power(WD-(?),2)) < ?");
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new double[]{x,y,radius});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >=?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new double[]{x,y,radius,start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {

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
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("circleQuery", "true");
		return ElementUtils.responseDocument(items);
	}

	public Document dzwlFindCircleQuery(double x, double y, double radius,String layer,
			int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows = -1;// 总行数
		int pagenum = pageSize;// 页数
		int start = startpos;// 开始
		int end = startpos + pagenum - 1;//结束
		Element items = new Element("items");
		StringBuffer sb = new StringBuffer();;
		sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
		sb.append("(");
		try {
			sb.append(" select t.dzwlbh jczbh,t.dzwlmc jczmc,t.dzwllx zdlx,'' ip,'' port,t.szdz jczdz,t.lxdh zdlxdh,t.jd,t.wd,t.lxr zdfzrxm,t.glbm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'D' type ");
			sb.append(" from t_dzwlxx").append(" t,frm_department d ").append(" where t.glbm=d.glbm");
			sb.append(")t ");
			sb.append(" where 1=1");
			sb.append(" and JD is not null and regexp_instr(JD,'[^.[:digit:]]')=0");
			sb.append(" and sqrt(power(JD-(?),2) + power(WD-(?),2)) < ?" );
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new double[]{x,y,radius});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?"  ;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{x,y,radius,start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {

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
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("circleQuery", "true");
		return ElementUtils.responseDocument(items);
	}
	
	/**
	 * pdt圆形查询
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 * @param startpos
	 * @param num
	 * @return
	 */
	public Document pdtFindCircleQuery(double x, double y, double radius,String layer,
			int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows = -1;// 总行数
		int pagenum = pageSize;// 页数
		int start = startpos;// 开始
		int end = startpos + pagenum - 1;//结束
		Element items = new Element("items");
		StringBuffer sb = new StringBuffer();;
		sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
		sb.append("(");
		try {
			sb.append(" select t.dth jczbh,t.pdt_id jczmc,'' zdlx,'' ip,'' port,'' jczdz,'' zdlxdh,to_char(t.jd) jd,to_char(t.wd)wd,'' zdfzrxm,'' sspcs ,s.bz bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'E' type ");
			sb.append(" from "+TabName.PDTLOCATIONMTAB+" t join t_pdt_sbxx s on t.DTH = s.DTH");
			sb.append(" )t ");
			sb.append(" where 1=1");
			sb.append(" and JD is not null and regexp_instr(JD,'[^.[:digit:]]')=0");
			sb.append(" and sqrt(power(JD-(?),2) + power(WD-(?),2)) < ?");
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new Object[]{x,y,radius});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{x,y,radius,start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {

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
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("circleQuery", "true");
		return ElementUtils.responseDocument(items);
	}
	
	public Document jczFindRectangleQuery(double minx, double miny, double maxx,
			double maxy, String layer,int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页
		int end = startpos + pagenum - 1;//结束页
		Element items = new Element("items");
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.id jczbh,t.jczmc,t.zdsx zdlx,'' ip,'' port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,d.bmmc,t.ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'A' type ");
			sb.append(" from T_JCZXX t,frm_department d ");
			sb.append(" where t.sspcs=d.glbm ");
			sb.append(")t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between  and ) and (to_number(wd) between ? and ?)");
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new Object[]{minx,maxx,miny,maxy});
			if (totalRows > 0) {        
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{minx,maxx,miny,maxy,start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}
		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("rectQuery", "true");
		return ElementUtils.responseDocument(items);
	}

	/**
	 * 卡口矩形查询
	 * 
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 * @return
	 */
	public Document kkFindRectangleQuery(double minx, double miny, double maxx,
			double maxy, String layer,int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页
		int end = startpos + pagenum - 1;//结束页
		Element items = new Element("items");
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ");
			sb.append(" from ").append(TabName.CODE_Gate).append(" t ,frm_department d ");
			sb.append(" where t.dwdm=d.glbm ");
			sb.append(")t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between ? and ?) and (to_number(wd) between ? and ?)");
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new Object[]{minx,maxx,miny,maxy});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?" ;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{minx,maxx,miny,maxy,start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}
		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("rectQuery", "true");
		return ElementUtils.responseDocument(items);
	}
	
	
	/**
	 * 卡口矩形查询（改造）
	 * 
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 * @return
	 * @date 2019/10/14
	 * @author hzy
	 */
	public Map kkFindRectangleQuery1(double minx, double miny, double maxx,
			double maxy, String layer,int startpos, int pageSize) {
		Map reMap = new HashMap();
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页
		int end = startpos + pagenum - 1;//结束页
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ");
			sb.append(" from ").append(TabName.CODE_Gate).append(" t ,frm_department d ");
			sb.append(" where t.dwdm=d.glbm ");
			sb.append(")t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between ? and ?) and (to_number(wd) between ? and ?)");
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new Object[]{minx,maxx,miny,maxy});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?"  ;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{minx,maxx,miny,maxy,start});
				reMap.put("rows", queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}
		reMap.put("num", Integer.toString(totalRows));
		reMap.put("startnum", start + "");
		reMap.put("endnum", end + "");
		return reMap;
	}
	
	public Document gajkFindRectangleQuery(double minx, double miny, double maxx,
			double maxy, String layer,int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页
		int end = startpos + pagenum - 1;//结束页
		Element items = new Element("items");
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.bh jczbh,t.mc jczmc,t.sptlx zdlx,t.dvrip ip,t.port,t.dz jczdz,t.lxdh zdlxdh,t.jd jd,t.wd wd,t.lxr zdfzrxm,t.SSJYFQDM sspcs ,t.SSJYFQMC bmmc,t.SSJYFQMC ssfj,to_char(t.gxsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'C' type ");
			sb.append(" from t_monitor t ");
			sb.append(")t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between ? and ?) and (to_number(wd) between ? and ?)");
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new Object[]{minx,maxx,miny,maxy});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?" ;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{minx,maxx,miny,maxy,start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}
		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("rectQuery", "true");
		return ElementUtils.responseDocument(items);
	}


	public Document dzwlFindRectangleQuery(double minx, double miny, double maxx,
			double maxy, String layer,int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页
		int end = startpos + pagenum - 1;//结束页
		Element items = new Element("items");
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.dzwlbh jczbh,t.dzwlmc jczmc,t.dzwllx zdlx,'' ip,'' port,t.szdz jczdz,t.lxdh zdlxdh,t.jd,t.wd,t.lxr zdfzrxm,t.glbm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'D' type ");
			sb.append(" from t_dzwlxx").append(" t,frm_department d ").append(" where t.glbm=d.glbm");
			sb.append(")t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between ? and ?) and (to_number(wd) between ? and ?)");
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new Object[]{minx,maxx,miny,maxy});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{minx,maxx,miny,maxy,start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}
		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("rectQuery", "true");
		return ElementUtils.responseDocument(items);
	}

	/**
	 * pdt矩形查询
	 * 
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 * @return
	 */
	public Document pdtFindRectangleQuery(double minx, double miny, double maxx,
			double maxy, String layer,int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页
		int end = startpos + pagenum - 1;//结束页
		Element items = new Element("items");
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.dth jczbh,t.pdt_id jczmc,'' zdlx,'' ip,'' port,'' jczdz,'' zdlxdh,to_char(t.jd) jd,to_char(t.wd)wd,'' zdfzrxm,'' sspcs ,s.bz bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'E' type ");
			sb.append(" from "+TabName.PDTLOCATIONMTAB+" t join t_pdt_sbxx s on t.DTH = s.DTH");
			sb.append(" )t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between ? and ?) and (to_number(wd) between ? and ?)");
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new Object[]{minx,maxx,miny,maxy});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?"  ;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{minx,maxx,miny,maxy,start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}
		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("rectQuery", "true");
		return ElementUtils.responseDocument(items);
	}
	
	public Document jczFindPolygonQuery(int startpos, int pageSize,String layer,
			String... strCoords) {
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页数
		int end = startpos + pagenum - 1;// 结束页数
		Element items = new Element("items");
		try {
			StringBuffer sb = new StringBuffer();
			double[] pointsX = new double[strCoords.length / 2];
			double[] pointsY = new double[strCoords.length / 2];
			int xi=0,yi=0;
			for (int i = 0; i < strCoords.length; i++) {
				if(i%2 == 0){
					pointsX[xi] = Double.parseDouble(strCoords[i]);
					xi++;
				}else{
					pointsY[yi] = Double.parseDouble(strCoords[i]);
					yi++;
				}
			}
			Arrays.sort(pointsX);
			Arrays.sort(pointsY);
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.id jczbh,t.jczmc,t.zdsx zdlx, '' ip,'' port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,d.bmmc,t.ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'A' type ");
			sb.append(" from T_JCZXX t,frm_department d ");
			sb.append(" where t.sspcs=d.glbm ");
			sb.append(" )t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between ? and ?) and (to_number(wd) between ? and )");
			
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new Object[]{ pointsX[0],pointsX[xi-1],pointsY[0] ,pointsY[yi-1]});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?" ;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{ pointsX[0],pointsX[xi-1],pointsY[0] ,pointsY[yi-1],start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}

		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("polygonQuery", "true");
		return ElementUtils.responseDocument(items);
	}

	/**
	 * 卡口多边形查询
	 */
	public Document kkFindPolygonQuery(int startpos, int pageSize,String layer,
			String... strCoords) {
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页数
		int end = startpos + pagenum - 1;// 结束页数
		Element items = new Element("items");
		try {
			StringBuffer sb = new StringBuffer();
			double[] pointsX = new double[strCoords.length / 2];
			double[] pointsY = new double[strCoords.length / 2];
			int xi=0,yi=0;
			for (int i = 0; i < strCoords.length; i++) {
				if(i%2 == 0){
					pointsX[xi] = Double.parseDouble(strCoords[i]);
					xi++;
				}else{
					pointsY[yi] = Double.parseDouble(strCoords[i]);
					yi++;
				}
			}
			Arrays.sort(pointsX);
			Arrays.sort(pointsY);
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ")
			.append(" from ").append(TabName.CODE_Gate).append(" t,frm_department d ")
			.append(" where t.dwdm=d.glbm ");
			sb.append(" )t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between ? and ?) and (to_number(wd) between ? and ?)");

			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new Object[]{ pointsX[0],pointsX[xi-1],pointsY[0] ,pointsY[yi-1]});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?"  ;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{ pointsX[0],pointsX[xi-1],pointsY[0] ,pointsY[yi-1],start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}

		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("polygonQuery", "true");
		return ElementUtils.responseDocument(items);
	}
	
	/**
	 * 卡口多边形查询 （改造）
	 * @date 2019/10/14
	 * @author hzy
	 */
	public Map kkFindPolygonQuery1(int startpos, int pageSize,String layer,
			String... strCoords) {
		Map reMap = new HashMap();
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页数
		int end = startpos + pagenum - 1;// 结束页数
		try {
			StringBuffer sb = new StringBuffer();
			double[] pointsX = new double[strCoords.length / 2];
			double[] pointsY = new double[strCoords.length / 2];
			int xi=0,yi=0;
			for (int i = 0; i < strCoords.length; i++) {
				if(i%2 == 0){
					pointsX[xi] = Double.parseDouble(strCoords[i]);
					xi++;
				}else{
					pointsY[yi] = Double.parseDouble(strCoords[i]);
					yi++;
				}
			}
			Arrays.sort(pointsX);
			Arrays.sort(pointsY);
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ")
			.append(" from ").append(TabName.CODE_Gate).append(" t,frm_department d ")
			.append(" where t.dwdm=d.glbm ");
			sb.append(" )t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between ? and ?) and (to_number(wd) between ? and ?)");

			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new Object[]{ pointsX[0],pointsX[xi-1],pointsY[0] ,pointsY[yi-1]});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?" ;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{ pointsX[0],pointsX[xi-1],pointsY[0] ,pointsY[yi-1],start});
				reMap.put("rows", queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}

		reMap.put("num", Integer.toString(totalRows));
		reMap.put("startnum", start + "");
		reMap.put("endnum", end + "");
		return reMap;
	}
	
	public Document gajkFindPolygonQuery(int startpos, int pageSize,String layer,
			String... strCoords) {
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页数
		int end = startpos + pagenum - 1;// 结束页数
		Element items = new Element("items");
		try {
			StringBuffer sb = new StringBuffer();
			double[] pointsX = new double[strCoords.length / 2];
			double[] pointsY = new double[strCoords.length / 2];
			int xi=0,yi=0;
			for (int i = 0; i < strCoords.length; i++) {
				if(i%2 == 0){
					pointsX[xi] = Double.parseDouble(strCoords[i]);
					xi++;
				}else{
					pointsY[yi] = Double.parseDouble(strCoords[i]);
					yi++;
				}
			}
			Arrays.sort(pointsX);
			Arrays.sort(pointsY);
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.bh jczbh,t.mc jczmc,t.sptlx zdlx,t.dvrip ip,t.port,t.dz jczdz,t.lxdh zdlxdh,t.jd jd,t.wd wd,t.lxr zdfzrxm,t.SSJYFQDM sspcs ,t.SSJYFQMC bmmc,t.SSJYFQMC ssfj,to_char(t.gxsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'C' type ");
			sb.append(" from t_monitor t ");
			sb.append(" )t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between ? and ?) and (to_number(wd) between ? and ?)");

			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new Object[]{ pointsX[0],pointsX[xi-1],pointsY[0] ,pointsY[yi-1]});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?"  ;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{ pointsX[0],pointsX[xi-1],pointsY[0] ,pointsY[yi-1],start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}

		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("polygonQuery", "true");
		return ElementUtils.responseDocument(items);
	}


	public Document dzwlFindPolygonQuery(int startpos, int pageSize,String layer,
			String... strCoords) {
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页数
		int end = startpos + pagenum - 1;// 结束页数
		Element items = new Element("items");
		try {
			StringBuffer sb = new StringBuffer();
			double[] pointsX = new double[strCoords.length / 2];
			double[] pointsY = new double[strCoords.length / 2];
			int xi=0,yi=0;
			for (int i = 0; i < strCoords.length; i++) {
				if(i%2 == 0){
					pointsX[xi] = Double.parseDouble(strCoords[i]);
					xi++;
				}else{
					pointsY[yi] = Double.parseDouble(strCoords[i]);
					yi++;
				}
			}
			Arrays.sort(pointsX);
			Arrays.sort(pointsY);
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.dzwlbh jczbh,t.dzwlmc jczmc,t.dzwllx zdlx,'' ip,'' port,t.szdz jczdz,t.lxdh zdlxdh,t.jd,t.wd,t.lxr zdfzrxm,t.glbm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'D' type ");
			sb.append(" from t_dzwlxx").append(" t,frm_department d ").append(" where t.glbm=d.glbm");
			sb.append(" )t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between ? and ?) and (to_number(wd) between ? and ?)");

			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new Object[]{ pointsX[0],pointsX[xi-1],pointsY[0] ,pointsY[yi-1]});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?" ;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{ pointsX[0],pointsX[xi-1],pointsY[0] ,pointsY[yi-1],start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}

		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("polygonQuery", "true");
		return ElementUtils.responseDocument(items);
	}
	
	/**
	 * pdt多边形查询
	 */
	public Document pdtFindPolygonQuery(int startpos, int pageSize,String layer,
			String... strCoords) {
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页数
		int end = startpos + pagenum - 1;// 结束页数
		Element items = new Element("items");
		try {
			StringBuffer sb = new StringBuffer();
			double[] pointsX = new double[strCoords.length / 2];
			double[] pointsY = new double[strCoords.length / 2];
			int xi=0,yi=0;
			for (int i = 0; i < strCoords.length; i++) {
				if(i%2 == 0){
					pointsX[xi] = Double.parseDouble(strCoords[i]);
					xi++;
				}else{
					pointsY[yi] = Double.parseDouble(strCoords[i]);
					yi++;
				}
			}
			Arrays.sort(pointsX);
			Arrays.sort(pointsY);
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.dth jczbh,t.pdt_id jczmc,'' zdlx,'' ip,'' port,'' jczdz,'' zdlxdh,to_char(t.jd) jd,to_char(t.wd)wd,'' zdfzrxm,'' sspcs ,s.bz bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'E' type ");
			sb.append(" from "+TabName.PDTLOCATIONMTAB+" t join t_pdt_sbxx s on t.DTH = s.DTH");
			sb.append(" )t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between ? and ?) and (to_number(wd) between ? and ?)");

			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,new Object[]{ pointsX[0],pointsX[xi-1],pointsY[0] ,pointsY[yi-1],start});
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?"  ;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,new Object[]{ pointsX[0],pointsX[xi-1],pointsY[0] ,pointsY[yi-1],start});
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}

		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("polygonQuery", "true");
		return ElementUtils.responseDocument(items);
	}

	public Document historyAlarmQuery(Map<String, String> params) {
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = Integer.parseInt(params.get("num"));//页数
		int start = Integer.parseInt(params.get("startpos"));//开始页数
		int end = start + pagenum - 1;// 结束页数
		Element items = new Element("items");
		try {
			StringBuffer sb = new StringBuffer(" select rownum rn, a.hphm HPHM, a.hpzl HPZL, a.bjsj BJSJ, a.bjdl BJDL, a.tp1 TP1, s.bkjb BKJB, t.kkjd KKJD, t.kkwd KKWD, t.kdmc KDMC,t.sxfxbm SXFXBM, c.dmsm1 DMSM "
					+ "from VEH_ALARMREC a left join VEH_SUSPINFO s on a.bkxh = s.bkxh left join code_gate t on a.kdbh = t.kdbh "
					+ "left join frm_code c on c.dmz = a.bjlx where c.dmlb = '130034'");
			String hphm = params.get("hphm");
			String kdbh = params.get("kdbh");
			String bjdl = params.get("bjdl");
			String bjlx = params.get("bjlx");
			String bjBeginTime = params.get("bjBeginTime");
			String bjEndTime = params.get("bjEndTime");
			List newv=new ArrayList();
			if(hphm!=null && !("".equals(hphm))){
				sb.append(" and a.hphm like ?");
				newv.add(hphm+"%");
			}
			if(kdbh!=null && !("".equals(kdbh))){
				sb.append(" and a.kdbh = ?");
				newv.add(kdbh);
			}
			if(bjdl!=null && !("".equals(bjdl))){
				sb.append(" and a.bjdl = ?");
				newv.add(bjdl);
			}
			if(bjlx!=null && !("".equals(bjlx))){
				sb.append(" and a.bjlx = ?");
				newv.add(bjlx);
			}
 		    if(bjBeginTime!=null && !("".equals(bjBeginTime))){
				sb.append(" and a.bjsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
				newv.add(bjBeginTime);
			}
			if(bjEndTime!=null && !("".equals(bjEndTime))){
				sb.append(" and a.bjsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
				newv.add(bjEndTime);
			}
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,newv.toArray());
			if (totalRows > 0) {
				newv.add(start);
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?"  ;
				List<Map<String,Object>> historyAlarmList = this.jdbcTemplate.queryForList(orgSql,newv.toArray());
				//翻译字段
				for(Map<String,Object> m:historyAlarmList){
					if(m.get("HPZL")!=null&&!StringUtils.isEmpty(m.get("HPZL").toString())){
						m.put("HPZLMC",this.systemDao.getCodeValue("030107",m.get("HPZL").toString()));
					}else{
						m.put("HPZLMC","未知");
					}
					String bkjbmc = "";
					int s =Integer.parseInt(m.get("BKJB")==null ? "":m.get("BKJB").toString());
					switch(s){
						case 1:bkjbmc = "一级";break;
						case 2:bkjbmc = "二级";break;
						case 3:bkjbmc = "三级";break;
						case 4:bkjbmc = "四级";break;
					}
					m.put("BKJBMC",bkjbmc);
				}
				items = ElementUtils.getResultElements(historyAlarmList);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}
		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("historyAlarmQuery", "true");
		return ElementUtils.responseDocument(items);
	}
	
	/**
	 * 轨迹预判车辆最后一个轨迹点
	 * @param hphm
	 * @return
	 */
	public List<Map<String,Object>> getPredictionCar(String hphm){
		ResultSet rs = null;
		List<Map<String,Object>> predictionCar = null;  
		try {
			StringBuffer sb = new StringBuffer(" select a.hphm HPHM, a.hpzl HPZL, a.gcsj GCSJ, a.tp1 TP1, a.kkbh "
					+ "from JM_VEH_PASSREC a "
					+ "where a.hphm = ? order by a.gcsj desc LIMIT 1 OFFSET 0");
			predictionCar = this.jdbcScsTemplate.queryForList(sb.toString(),new String[]{hphm});
			Map<String, Object> lastGc = predictionCar.get(0);
			lastGc.put("HPHM", lastGc.get("hphm"));
			lastGc.put("HPZL", lastGc.get("hpzl"));
			lastGc.put("GCSJ", lastGc.get("gcsj"));
			lastGc.put("TP1", lastGc.get("tp1"));
			String kkbh = (String)lastGc.get("kkbh");
			StringBuffer codeSql = new StringBuffer("select t.kkjd, t.kkwd, t.kkmc kdmc, t.sxfxbm from jm_code_gate t where t.kkbh = ?");
			// List<Map<String,Object>> codeGateList = this.jdbcScsTemplate.queryForList(codeSql.toString());
			List<Map<String,Object>> codeGateList = this.jdbcTemplate.queryForList(codeSql.toString(),new String[]{kkbh});
			if (codeGateList != null && codeGateList.size() > 0) {
				Map<String, Object> codeGateMap = codeGateList.get(0);
				lastGc.put("KKJD", codeGateMap.get("kkjd"));
				lastGc.put("KKWD", codeGateMap.get("kkwd"));
				lastGc.put("SXFXBM", codeGateMap.get("sxfxbm"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}
		return predictionCar;
	}
	
	/**
	 * 组织机构--卡口分布
	 */
	@Override
	public Document kkPosition(CodeGate gate, int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows = -1;// 总行数
		int pagenum = pageSize;// 每页条数
		int start = startpos;// 开始
		int end = startpos + pagenum - 1;//结束
		Element items = new Element("items");
		StringBuffer sb = new StringBuffer();
		sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
		sb.append("(");
		try {
			List vl=new ArrayList<>();
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ");
			sb.append(" from ").append(TabName.CODE_Gate).append(" t,frm_department d ").append(" where t.dwdm=d.glbm ");
			if (StringUtils.isNotBlank(gate.getDwdm())) {
				if(gate.getDwdm().length() <= 6) {
					sb.append(" and t.dwdm like ?");
					vl.add(gate.getDwdm()+"%");
				} else {
					sb.append(" and t.dwdm = ?");
					vl.add(gate.getDwdm());
				}
			}
			if (StringUtils.isNotBlank(gate.getXzqh())) {
				sb.append(" and t.xzqh like ");
				vl.add(gate.getXzqh());
			}
			sb.append(")t ");
			sb.append("where 1=1");

			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,vl.toArray());
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?"  ;
				vl.add(start);
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,vl.toArray());
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {

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
		items.setAttribute("kkPosition", "true");
		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");

		return ElementUtils.responseDocument(items);
	}
	
	/**
	 * 组织机构--卡口分布(重写)
	 * @date 2019/10/14
	 * @author hzy
	 */
	public Map kkPosition1(CodeGate gate, int startpos, int pageSize) {
		Map remap = new HashMap();
		ResultSet rs = null;
		int totalRows = -1;// 总行数
		int pagenum = pageSize;// 每页条数
		int start = startpos;// 开始
		int end = startpos + pagenum - 1;//结束
		StringBuffer sb = new StringBuffer();
		sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
		sb.append("(");
		List vl=new ArrayList<>();
		try {
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ");
			sb.append(" from ").append(TabName.CODE_Gate).append(" t,frm_department d ").append(" where t.dwdm=d.glbm ");
			if (StringUtils.isNotBlank(gate.getDwdm())) {
				if(gate.getDwdm().length() <= 6) {
					sb.append(" and t.dwdm like ?");
					vl.add(gate.getDwdm() + "%");
				} else {
					sb.append(" and t.dwdm = ?");
					vl.add(gate.getDwdm()  );
				}
			}
			if (StringUtils.isNotBlank(gate.getXzqh())) {
				sb.append(" and t.xzqh like ?");
				vl.add(gate.getXzqh()  );
			}
			sb.append(")t ");
			sb.append("where 1=1");

			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql);
			if (totalRows > 0) {
				vl.add(start);
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,vl.toArray());
				remap.put("rows", queryForList);
			}
		} catch (Exception e) {

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

		remap.put("num", Integer.toString(totalRows));
		remap.put("startnum", start);
		remap.put("endnum", end);

		return remap;
	}
	
	/**历史报警（重写）
	 *
	 * @param params
	 * @return
	 * @date 2019/10/14
	 * @author hzy
	 */
	public Map historyAlarmQuery1(Map<String, String> params) {
		Map remap = new HashMap();
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = Integer.parseInt(params.get("num"));//页数
		int start = Integer.parseInt(params.get("startpos"));//开始页数
		int end = start + pagenum - 1;// 结束页数
		try {
			StringBuffer sb = new StringBuffer(" select rownum rn,a.bjxh  BJXH, a.kdbh   KDBH,  a.hphm HPHM, a.hpzl HPZL, a.bjsj BJSJ, a.bjdl BJDL, a.tp1 TP1, s.bkjb BKJB, t.kkjd KKJD, t.kkwd KKWD, t.kdmc KDMC,t.sxfxbm SXFXBM, c.dmsm1 DMSM "
					+ "from VEH_ALARMREC a left join VEH_SUSPINFO s on a.bkxh = s.bkxh left join code_gate t on a.kdbh = t.kdbh "
					+ "left join frm_code c on c.dmz = a.bjlx where c.dmlb = '130034'");
			String hphm = params.get("hphm");
			String kdbh = params.get("kdbh");
			String bjdl = params.get("bjdl");
			String bjlx = params.get("bjlx");
			String bjBeginTime = params.get("bjBeginTime");
			String bjEndTime = params.get("bjEndTime");
			List vl=new ArrayList<>();
			if(hphm!=null && !("".equals(hphm))){
				sb.append(" and a.hphm like ?");
				vl.add(hphm+"%");
			}
			if(kdbh!=null && !("".equals(kdbh))){
				sb.append(" and a.kdbh = ?");
				vl.add(kdbh);
			}
			if(bjdl!=null && !("".equals(bjdl))){
				sb.append(" and a.bjdl = ?");
				vl.add(bjdl);
			}
			if(bjlx!=null && !("".equals(bjlx))){
				sb.append(" and a.bjlx = ?");
				vl.add(bjlx);
			}
			if(bjBeginTime!=null && !("".equals(bjBeginTime))){
				sb.append(" and a.bjsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
				vl.add(bjBeginTime);
			}
			if(bjEndTime!=null && !("".equals(bjEndTime))){
				vl.add(bjEndTime);
				sb.append(" and a.bjsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
			}
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,vl.toArray());
			if (totalRows > 0) {
				vl.add(start);
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?" ;
				List<Map<String,Object>> historyAlarmList = this.jdbcTemplate.queryForList(orgSql,vl.toArray());
				System.out.println(orgSql);
				//翻译字段
				for(Map<String,Object> m:historyAlarmList){
					if(m.get("HPZL")!=null&&!StringUtils.isEmpty(m.get("HPZL").toString())){
						m.put("HPZLMC",this.systemDao.getCodeValue("030107",m.get("HPZL").toString()));
					}else{
						m.put("HPZLMC","未知");
					}
					String bkjbmc = "";
					int s =Integer.parseInt(m.get("BKJB")==null?"1":m.get("BKJB").toString());
					switch(s){
						case 1:bkjbmc = "一级";break;
						case 2:bkjbmc = "二级";break;
						case 3:bkjbmc = "三级";break;
						case 4:bkjbmc = "四级";break;
					}
					m.put("BKJBMC",bkjbmc);
				}

				remap.put("rows", historyAlarmList);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}

		remap.put("num", Integer.toString(totalRows));
		remap.put("startnum", start);
		remap.put("endnum", end);
		return remap;
	}
	
	/**
	 * 轨迹预判
	 */
	public Document trackPredictionQuery(double x, double y, double radius,String layer,
			int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows = -1;// 总行数
		int pagenum = pageSize;// 页数
		int start = startpos;// 开始
		int end = startpos + pagenum - 1;//结束
		Element items = new Element("items");
		StringBuffer sb = new StringBuffer();
		List vl=new ArrayList<>();
		sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
		sb.append("(");
		try {
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ");
			sb.append(" from ").append(TabName.CODE_Gate).append(" t,frm_department d ").append(" where t.dwdm=d.glbm ");
			sb.append(")t ");
			sb.append(" where 1=1");
			sb.append(" and JD is not null and regexp_instr(JD,'[^.[:digit:]]')=0");
			vl.add(x);
			vl.add(y);
			vl.add(radius);
			
			sb.append(" and sqrt(power(JD-(?),2) + power(WD-(?),2)) < " + radius);
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,vl.toArray());
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= ?) where rn >= ?";
				vl.add(end);
				vl.add(start);
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,vl.toArray());
				for(Map<String,Object> m:queryForList){
					if(m.get("jd")!="0" && m.get("wd")!="0"){
						String distance = MapDistance.getDistance(x+"", y+"", m.get("jd").toString(), m.get("wd").toString());
						DecimalFormat df2  = new DecimalFormat("###.00000");//这样为保持3位
						/*double powX = new Double(df2.format(Math.pow(x-Double.parseDouble(m.get("jd").toString()),2)));
						double powY = new Double(df2.format(Math.pow(y-Double.parseDouble(m.get("wd").toString()),2)));*/
						double fastestTime = new Double(df2.format(Double.parseDouble(distance)/80*60));
						m.put("fastestTime",fastestTime);
					} else {
						m.put("fastestTime",0);
					}

				}
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {

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
		items.setAttribute("display", totalRows <= 0 ? "此号牌车辆尚未经过任何卡口!" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("trackPredictionQuery", "true");
		items.setAttribute("centreX",x+"");
		items.setAttribute("centreY",y+"");
		return ElementUtils.responseDocument(items);
	}
	
	/**
	 * 轨迹预判(重写)
	 * @date 2019/10/14
	 * @author hzy
	 */
	public Map trackPredictionQuery1(double x, double y, double radius,String layer,
										 int startpos, int pageSize) {
		Map remap = new HashMap();
		ResultSet rs = null;
		int totalRows = -1;// 总行数
		int pagenum = pageSize;// 页数
		int start = startpos;// 开始
		int end = startpos + pagenum - 1;//结束
		List vl=new ArrayList<>();
		Element items = new Element("items");
		StringBuffer sb = new StringBuffer();
		sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
		sb.append("(");
		try {
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ");
			sb.append(" from ").append(TabName.CODE_Gate).append(" t,frm_department d ").append(" where t.dwdm=d.glbm ");
			sb.append(")t ");
			sb.append(" where 1=1");
			sb.append(" and JD is not null and regexp_instr(JD,'[^.[:digit:]]')=0");
			sb.append(" and sqrt(power(JD-(?),2) + power(WD-(?),2)) < ?");
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			vl.add(x);
			vl.add(y);
			vl.add(radius);
			totalRows = this.jdbcTemplate.queryForInt(countSql,vl.toArray());
			if (totalRows > 0) {
				vl.add(end);
				vl.add(start);
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= " + start;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,vl.toArray());
				for(Map<String,Object> m:queryForList){
					if(m.get("jd")!="0" && m.get("wd")!="0"){
						String distance = MapDistance.getDistance(x+"", y+"", m.get("jd").toString(), m.get("wd").toString());
						DecimalFormat df2  = new DecimalFormat("###.00000");//这样为保持3位

						double fastestTime = new Double(df2.format(Double.parseDouble(distance)/80*60));
						m.put("fastestTime",fastestTime);
					} else {
						m.put("fastestTime",0);
					}
				}

				remap.put("rows", queryForList);
			}
		} catch (Exception e) {

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

		remap.put("centreX",x+"");
		remap.put("centreY",y+"");
		remap.put("num", Integer.toString(totalRows));
		remap.put("startnum", start);
		remap.put("endnum", end);
		return remap;
	}
	
	/**
	 * ��װXML��ʽ
	 * 
	 * @param orgSql
	 * @return
	 */
	private Document getDocumentData(String orgSql) {

		Element items = ElementUtils.getResultElements(DBUtils
				.findResultData(orgSql));

		items.setAttribute("display", items.getChildren().size() <= 0 ? "false"
				: "true");
		items.setAttribute("count", Integer
				.toString(items.getChildren().size()));

		return ElementUtils.responseDocument(items);

	}
	
	/**
	 * 组织机构--卡口分布
	 */
	@Override
	public Document kkSearch(String kkmcArray, int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows = -1;// 总行数
		int pagenum = pageSize;// 每页条数
		int start = startpos;// 开始
		int end = startpos + pagenum - 1;//结束
		Element items = new Element("items");
		StringBuffer sb = new StringBuffer();
		Map m=parseKkmcArray(kkmcArray);
		List sqlv=(List) m.get("sqlv");
		sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.ssfj,t.gxsj,t.type from ");
		sb.append("(");
		try {
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ");
			sb.append(" from ").append(TabName.CODE_Gate).append(" t ").append(" where 1 = 1 ");
			sb.append(m.get("sql"));
			sb.append(")t ");
			sb.append("where 1=1");

			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql);
			if (totalRows > 0) {
				String orgSql = "select * from (" + sb + " and rownum <= "
						+ end + ") where rn >= ?";
				sqlv.add(start);
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,sqlv.toArray());
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {

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
		items.setAttribute("kkSearch", "true");
		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");

		return ElementUtils.responseDocument(items);
	}
	
	public Document kkFindCircleIndistinctQuery(double x, double y, double radius,String layer,
			int startpos, int pageSize, String kkmcArray) {
		ResultSet rs = null;
		int totalRows = -1;// 总行数
		int pagenum = pageSize;// 页数
		int start = startpos;// 开始
		int end = startpos + pagenum - 1;//结束
		 
		Element items = new Element("items");
		StringBuffer sb = new StringBuffer();
		Map m=parseKkmcArray(kkmcArray);
		List sqlv=(List) m.get("sqlv");
		sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
		sb.append("(");
		try {
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ");
			sb.append(" from ").append(TabName.CODE_Gate).append(" t,frm_department d ").append(" where t.dwdm=d.glbm ");
			sb.append(m.get("sql"));
			sb.append(")t ");
			sb.append(" where 1=1");
			sb.append(" and JD is not null and regexp_instr(JD,'[^.[:digit:]]')=0");
			sb.append(" and sqrt(power(JD-(?),2) + power(WD-(?),2)) < ?");
			sqlv.add(x);
			sqlv.add(y);
			sqlv.add(radius);
			
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,sqlv.toArray());
			if (totalRows > 0) {
				sqlv.add(end);
				sqlv.add(start);
				String orgSql = "select * from (" + sb + " and rownum <= ?) where rn >= ?" ;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,sqlv.toArray());
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {

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
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("kkSearch", "true");
		return ElementUtils.responseDocument(items);
	}
	
	@Override
	public Document kkFindRectangleIndistinctQuery(double minx, double miny, double maxx,
			double maxy, String layer,int startpos, int pageSize, String kkmcArray) {
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页
		int end = startpos + pagenum - 1;//结束页
		Element items = new Element("items");
		Map m=parseKkmcArray(kkmcArray);
		List sqlv=(List) m.get("sqlv");
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ");
			sb.append(" from ").append(TabName.CODE_Gate).append(" t ,frm_department d ");
			sb.append(" where t.dwdm=d.glbm ");
			sb.append(m.get("sql"));
			sb.append(")t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between ? and ?) and (to_number(wd) between ? and ?)");
			sqlv.add(minx);
			sqlv.add(maxx);
			sqlv.add(miny);
			sqlv.add(maxy);
			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,sqlv.toArray());
			if (totalRows > 0) {
				sqlv.add(end);
				sqlv.add(start);
				String orgSql = "select * from (" + sb + " and rownum <= ?) where rn >= ?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,sqlv.toArray());
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}
		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("kkSearch", "true");
		return ElementUtils.responseDocument(items);
	}

	@Override
	public Document kkFindPolygonIndistinctQuery(int startpos, int pageSize,String layer,String[] strCoords,String kkmcArray) {
		ResultSet rs = null;
		int totalRows = -1;//总数
		int pagenum = pageSize;//页数
		int start = startpos;//开始页数
		int end = startpos + pagenum - 1;// 结束页数
		List vl=new ArrayList<>();
		Element items = new Element("items");
		try {
			StringBuffer sb = new StringBuffer();
			double[] pointsX = new double[strCoords.length / 2];
			double[] pointsY = new double[strCoords.length / 2];
			int xi=0,yi=0;
			for (int i = 0; i < strCoords.length; i++) {
				if(i%2 == 0){
					pointsX[xi] = Double.parseDouble(strCoords[i]);
					xi++;
				}else{
					pointsY[yi] = Double.parseDouble(strCoords[i]);
					yi++;
				}
			}
			Map m=parseKkmcArray(kkmcArray);
			List sqlv=(List) m.get("sqlv");
			 vl.addAll(sqlv);
			Arrays.sort(pointsX);
			Arrays.sort(pointsY);
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
			sb.append(" ( ");
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ")
			.append(" from ").append(TabName.CODE_Gate).append(" t,frm_department d ")
			.append(" where t.dwdm=d.glbm ");
			sb.append(m.get("sql"));
			sb.append(" )t ");
			sb.append("where 1=1");
			sb.append(" and jd is not null and regexp_instr(jd,'[^.[:digit:]]')=0");
			sb.append(" and (to_number(jd) between ? and ?) and (to_number(wd) between ? and ?)");
 vl.add(pointsX[0]);vl.add(pointsX[xi-1]);vl.add(pointsY[0]);vl.add(pointsY[yi-1]);

			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,vl.toArray());
			if (totalRows > 0) {
				vl.add(end);
				vl.add(start);
				String orgSql = "select * from (" + sb + " and rownum <= ?) where rn >=? "  ;
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,vl.toArray());
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e2) {
			}
		}

		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("kkSearch", "true");
		return ElementUtils.responseDocument(items);
	}
	
	@Override
	public Document kkPositionIndistinct(CodeGate gate, int startpos, int pageSize, String kkmcArray){
		ResultSet rs = null;
		int totalRows = -1;// 总行数
		int pagenum = pageSize;// 每页条数
		int start = startpos;// 开始
		int end = startpos + pagenum - 1;//结束
		List vl=new ArrayList<>();
		Map m=parseKkmcArray(kkmcArray);
		List sqlv=(List) m.get("sqlv");
		 vl.addAll(sqlv);
		Element items = new Element("items");
		StringBuffer sb = new StringBuffer();
		sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from ");
		sb.append("(");
		try {
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ");
			sb.append(" from ").append(TabName.CODE_Gate).append(" t,frm_department d ").append(" where t.dwdm=d.glbm ");
			sb.append(m.get("sql"));
			if (StringUtils.isNotBlank(gate.getDwdm())) {
				if(gate.getDwdm().length() <= 6) {
					sb.append(" and t.dwdm like ?");
					vl.add( gate.getDwdm() + "%");
				} else {
					sb.append(" and t.dwdm = '" + gate.getDwdm() + "'");
					vl.add( gate.getDwdm()  );
				}
			}
			if (StringUtils.isNotBlank(gate.getXzqh())) {
				sb.append(" and t.xzqh like ?");
				vl.add( gate.getXzqh()  );
			}
			sb.append(")t ");
			sb.append("where 1=1");

			String countSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("from"));
			totalRows = this.jdbcTemplate.queryForInt(countSql,vl.toArray());
			if (totalRows > 0) {
				vl.add(end);
				vl.add(start);
				String orgSql = "select * from (" + sb + " and rownum <= ?) where rn >= ?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,vl.toArray());
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {

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
		items.setAttribute("kkSearch", "true");
		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");

		return ElementUtils.responseDocument(items);
	}

	private Map parseKkmcArray(String kkmcArray){
		String[] kkmc = kkmcArray.split(",");
		StringBuffer sb = new StringBuffer("");
		List vl=new ArrayList<>();
		for(int i = 0; i < kkmc.length; i++){
			sb.append(" and t.kdmc like ?");
			vl.add("%"+kkmc[i]+"%");
		}
		Map m=new HashMap<>();
		m.put("sql", String.valueOf(sb));
		m.put("sqlv", vl);
		return m;
	}
}
