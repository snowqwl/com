package com.easymap.dao.track;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.easymap.util.ElementUtils;
import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.maintain.util.FileSystemResource;
import com.sunshine.monitor.system.analysis.clickhouse.jdbc.ClickHouseJDBC;

@Repository("HistoryQuerySCSDao")
public class HistoryQuerySCSDaoImpl extends TrackMapJDBC{
	
	@Autowired
	@Qualifier("jdbcScsTemplate")
	protected JdbcTemplate jdbcScsTemplate;
	
	@Autowired
	@Qualifier("jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
	
	public Document hphmzltrack(String hphm,int startpos,int pagesize,String stime,String etime)
	{
		//int totalRows = -1;
		int totalRows = -1;// 总行数
		
		int pagenum = 15;

		int start = startpos;
		int end = startpos + pagenum - 1;
		
		Element items = new Element("items");
		List vl=new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select ROW_NUMBER() OVER(order by ve.GCSJ asc) as RN,to_char(ve.gcsj, 'yyyy-mm-dd hh24:mi:ss') gcsj, ")
				.append(" ve.GCXH, ")
				.append(" ve.KDBH JCZBH") 
				.append(" from veh_passrec ve ")
				.append(" where 1 = 1 ")
				.append(" and ve.GCSJ > ? ")
				.append(" and ve.GCSJ < ? ")
				.append(" and ve.HPHM = ? ");
		String countSql = "SELECT COUNT(1) " + sql.substring(sql.indexOf("from"));
		vl.add(stime);
		vl.add(etime);
		vl.add(hphm);
		totalRows = this.jdbcScsTemplate.queryForInt(countSql,vl.toArray());
		if (totalRows > 0) {
			List<Map<String,Object>> queryForList = this.jdbcScsTemplate.queryForList(sql.toString(),vl.toArray());
			
			for(int i=0;i<queryForList.size();i++){
				String jczbh = (String)queryForList.get(i).get("jczbh");
				String codeSql = "select kdmc,kkjd,kkwd,lxdh,lxdz,gxsj from code_gate where kdbh = '"+jczbh+"'";
				List<Map<String,Object>> codeForList = this.jdbcTemplate.queryForList(codeSql.toString());
				if(codeForList!=null && codeForList.size()>0 ){
					Map<String,Object> codeMap = codeForList.get(0);
					Map map=(Map)queryForList.get(i);
					map.put("RN", map.get("rn"));
					map.put("GCXH", map.get("gcxh"));
					map.put("JCZBH", map.get("jczbh"));
					map.put("JCZMC",codeMap.get("kdmc")==null?"":codeMap.get("kdmc"));
					map.put("JD",codeMap.get("kkjd")==null?"":codeMap.get("kkjd"));
					map.put("WD",codeMap.get("kkwd")==null?"":codeMap.get("kkwd"));
					map.put("ZDLXDH", codeMap.get("lxdh")==null?"":codeMap.get("lxdh"));
					map.put("JCZDZ", codeMap.get("lxdz")==null?"":codeMap.get("lxdz"));
					map.put("GXSJ", codeMap.get("gxsj")==null?"":codeMap.get("gxsj"));
				}else {
					queryForList.remove(i);
					totalRows--;
				}
			}
			
			items = ElementUtils.getResultElements(queryForList);
		}
		items.setAttribute("display", totalRows <= 0 ? "false" : "true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "orgid");
		items.setAttribute("startnum", start + "");
		items.setAttribute("endnum", end + "");
		items.setAttribute("track", "true");
		return ElementUtils.responseDocument(items);
		
//		List queryForList = this.jdbcTemplate.queryForList(sql.toString());
//		return queryForList;
	}
	
	/**重写
	 * @date 2019/10/14
	 * @author hzy
	 * @throws Exception 
	 */
	public Map hphmzltrack1(String hphm,int startpos,int pagesize,String stime,String etime) throws Exception
	{
		Properties property = FileSystemResource.getProperty("common.properties");
		Map remap = new HashMap();
		//int totalRows = -1;
		int totalRows = -1;// 总行数
		
		int pagenum = 15;

		int start = startpos;
		int end = startpos + pagenum - 1;
		List vl=new ArrayList<>();
		//String hp_zl[] = hphm.split(",");
		StringBuilder sql = new StringBuilder();		
		sql.append(" select ysbh as gcxh,hpzl,hphm,hpys,zkbh as JCZBH,zkmc as kkmc,"
				+ "xsfx as fxbh,gcsj  from " +property.getProperty("track.table"));
		sql.append(" where 1 = 1 ");
		sql.append(" and ydbpartion in(" + getYdbpartion(stime,etime) + ") ");
		sql.append(" and hphm = ? ");
		vl.add(hphm);
		String countSql = "SELECT COUNT(1) as total " + sql.substring(sql.indexOf("from"));
		totalRows = this.getTotal(countSql,vl.toArray());
		if (totalRows > 0) {
			List<Map<String,Object>> queryForList = this.getPageDatas(sql.toString(),vl.toArray(),1,2000);
			
			for(int i=0;i<queryForList.size();i++){
				String jczbh = (String)queryForList.get(i).get("jczbh");
				vl=new ArrayList<>();
				vl.add(jczbh);
				String codeSql = "select kdmc,kkjd,kkwd,lxdh,lxdz,gxsj from code_gate where kdbh =?";
				List<Map<String,Object>> codeForList = this.jdbcTemplate.queryForList(codeSql.toString(),vl.toArray());
				if(codeForList!=null && codeForList.size()>0 ){
					Map<String,Object> codeMap = codeForList.get(0);
					Map map=(Map)queryForList.get(i);
					map.put("RN", map.get("rn"));
					map.put("GCXH", map.get("gcxh"));
					map.put("JCZBH", map.get("jczbh"));
					map.put("JCZMC",codeMap.get("kdmc")==null?"":codeMap.get("kdmc"));
					map.put("JD",codeMap.get("kkjd")==null?"":codeMap.get("kkjd"));
					map.put("WD",codeMap.get("kkwd")==null?"":codeMap.get("kkwd"));
					map.put("ZDLXDH", codeMap.get("lxdh")==null?"":codeMap.get("lxdh"));
					map.put("JCZDZ", codeMap.get("lxdz")==null?"":codeMap.get("lxdz"));
					map.put("GXSJ", codeMap.get("gxsj")==null?"":codeMap.get("gxsj"));
				}else {
					queryForList.remove(i);
					totalRows--;
				}
			}
			
			remap.put("rows", queryForList);
		}
		remap.put("num", Integer.toString(totalRows));
		remap.put("startnum", start);
		remap.put("endnum", end);
		return remap;
	}
	  /**
     * 计算YDB分区
     * @param kssj
     * @param jssj
     * @return
     */
    public static String getYdbpartion(String kssj, String jssj) {
        String partion="";
        try {
            Date ks = new SimpleDateFormat("yyyy-MM").parse(kssj);
            Date js = new SimpleDateFormat("yyyy-MM").parse(jssj);
            Calendar dd = Calendar.getInstance();
            //设置日期起始时间
            dd.setTime(ks);
            //判断是否到结束日期
            while (dd.getTime().before(js)) {
                partion = partion + "'" + new SimpleDateFormat("yyyyMM").format(dd.getTime()) + "',";
                //进行当前日期月份加1
                dd.add(Calendar.MONTH, 1);
            }
            partion = partion + "'" + new SimpleDateFormat("yyyyMM").format(js.getTime()) + "'";
        } catch (ParseException e) {
        }
        return partion;
    }
}

