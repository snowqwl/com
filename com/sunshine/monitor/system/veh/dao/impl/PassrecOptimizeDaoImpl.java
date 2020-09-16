package com.sunshine.monitor.system.veh.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.veh.dao.PassrecOptimizeDao;
import com.sunshine.monitor.system.veh.util.OptimizeStrategy;

@Repository("passrecOptimizeDao")
public  class PassrecOptimizeDaoImpl extends BaseDaoImpl implements
PassrecOptimizeDao {

	protected static Logger log = LoggerFactory.getLogger(PassrecOptimizeDaoImpl.class);
	
	public PassrecOptimizeDaoImpl() {
		super.setTableName("VEH_PASSREC");
	}

	public Map<String, Object> getPassrecList(Map<String, Object> conditions,
			String tableName) throws Exception {
		String table = "";
		//索引策略
		OptimizeStrategy opt=new OptimizeStrategy("t");
		if (tableName == null || "".equals(tableName)) {
			table = this.getTableName();
		} else {
			table = tableName;
		}
		StringBuffer sql = new StringBuffer(50);
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		//定义条件预处理数组
		List<String> whereSqlArray=new ArrayList();
		
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter = "sort".equalsIgnoreCase(key)
					|| "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key)
					|| "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.indexOf("kssj") != -1) {
					sql.append(" and gcsj >= ")
					.append(
							"to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					whereSqlArray.add(value);
				} else if (key.indexOf("jssj") != -1) {
					sql.append(" and gcsj <= ")
					.append(
							"to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					whereSqlArray.add(value);
				} else if ("hphm".equalsIgnoreCase(key)) {
					//String temp = Common.changeHphm(value).toUpperCase();
					String temp =value;
					if(temp.length()<7&&temp.length()>0){
						temp="%"+temp+"%";
					}
					//使用号牌号码索引
						/*if(opt.getCode()==3){
							opt.setCode(4);
						}else opt.setCode(2);*/
					  opt.setCode(1);
					if ((temp.endsWith("%")) || (temp.indexOf("_") > 0)) {
						//sql.append(" and hphm like '" + temp + "' ");
						sql.append(" and hphm like ? ");
						whereSqlArray.add(temp) ;
					} else {
						sql.append(" and hphm = ? ");
						whereSqlArray.add(temp) ;
					}
				} else if ("kdbh".equalsIgnoreCase(key)) {
					//sql.append(" and kdbh in ('" + value + "') ");
					sql.append(" and kdbh = ? ");
					whereSqlArray.add(value) ;
					/*if(opt.getCode()==1){
						opt.setCode(4);
					}else opt.setCode(3);*/
					if(opt.check()){
						opt.setCode(3);
					}
				} /*
				 * else if ("glbm".equalsIgnoreCase(key)) { String sonSql =
				 * "select * from code_gate where dwdm='" + value + "'";
				 * List<CodeGate> list = this.queryForList(sonSql,
				 * CodeGate.class); String kdbhsql = ""; if (list.size() > 0) {
				 * for (int i = 0; i < list.size(); i++) { CodeGate cg =
				 * (CodeGate) list.get(i); kdbhsql = kdbhsql + "kdbh='" +
				 * cg.getKdbh() + "' or "; } sql.append(" and (" +
				 * kdbhsql.substring(0, kdbhsql.length() - 3) + ") "); } }
				 */else if ("licesenHeader".equalsIgnoreCase(key)) {
					String[] licesenHeaders = value.trim().split("\\p{Punct}");
					if (licesenHeaders.length <= 0)
						continue;
					sql.append("and ( ");
					for (String licesenHeader : licesenHeaders) {
						sql.append(" hphm like '" + licesenHeader + "%' or ");
					}
					sql.delete(sql.length() - 3, sql.length());
					sql.append(")");
				} else {

					if ("hphm".equalsIgnoreCase(key)
							|| "hpzl".equalsIgnoreCase(key)
							|| "hpys".equalsIgnoreCase(key)
							|| "fxbh".equalsIgnoreCase(key)) {
						sql.append(" and ");
						sql.append(key);
						sql.append(" = '").append(value+"'");
					}

				}
			}
		}
		
		//添加红名单过滤
		sql.append(" and not exists(select a.hphm from jm_red_namelist a where t.hphm=a.hphm and a.status='1' and a.isvalid='1')");
		/*// 过车时间排序
		if(opt.algorithmMix().equals(opt.getIndex().get(1))){
		sql.append(" order by "); sql.append(conditions.get("sort"));
		sql.append(" "); sql.append(conditions.get("order"));
		}*/

		StringBuffer sb = new StringBuffer();
		/**
		if(opt.getCode()==0){
			opt.setCode(2);
			sql.append(" order by gcsj desc ");
		}
		if(opt.getCode()==1){
			sql.append(" order by gcsj desc ");
		}
		**/
		sql.append(" order by gcsj desc ");
		sb.append("select " + opt.algorithm() + " * from " + table
				+ " t where 1=1 ");
		sb.append(sql);
		
		Map<String, Object> map = null;
		// 第一次查询统计总数，点击datagrid分页按钮只查询当页数据，使用分页标记
		if ("0".equals(conditions.get("pageSign"))) {
			if (conditions.get("cityname") == null
					|| conditions.get("cityname").toString().length() < 1) {
				map = this.findPageForMapByIndex(sb.toString(),"1000", Integer.parseInt(conditions.get("page")
						.toString()), Integer.parseInt(conditions.get("rows")
						.toString()), VehPassrec.class,whereSqlArray);
			} else {
				JdbcTemplate jd = SpringApplicationContext.getRemoteSourse(
						conditions.get("cityname").toString(), "jcbk", false);
				map = this.findPageForMapByIndex(sb.toString(),
						"1000",
						Integer.parseInt(conditions.get("page").toString()),
						Integer.parseInt(conditions.get("rows").toString()),
						VehPassrec.class,whereSqlArray,jd);
			}
		} else {
			if (conditions.get("cityname") == null
					|| conditions.get("cityname").toString().length() < 1) {
				map = this.findPageForMapByIndex(sb.toString(),
						"1000",
						Integer.parseInt(conditions.get("page").toString()),
						Integer.parseInt(conditions.get("rows").toString()),
						VehPassrec.class,
						whereSqlArray);
			} else {
				JdbcTemplate jd = SpringApplicationContext.getRemoteSourse(
						conditions.get("cityname").toString(), "jcbk", false);
				map = this.findPageForMapByIndex(sb.toString(),
						"1000",
						Integer.parseInt(conditions.get("page").toString()),
						Integer.parseInt(conditions.get("rows").toString()),
						VehPassrec.class,whereSqlArray,jd);
			}

		}
		return map;
	}
	
	
	public Map<String, Object> getPassrecAndJjList(Map<String, Object> conditions,
			String tableName) throws Exception {
		String table = "";
		boolean flag = false;
		boolean hphmFlag = false;
		//索引策略
		//OptimizeStrategy opt=new OptimizeStrategy("t");
		if (tableName == null || "".equals(tableName)) {
			if (conditions.get("cityname") == null
					|| conditions.get("cityname").toString().length() < 1) {
				table = "T_JJ_VEH_PASSREC";
				flag = true;
			}else{
				table = "veh_passrec";
			}
		} else {
			table = tableName;
		}
		StringBuffer sql = new StringBuffer(50);
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		//定义条件预处理数组
		List<String> whereSqlArray=new ArrayList();
		
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter = "sort".equalsIgnoreCase(key)
					|| "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key)
					|| "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.indexOf("kssj") != -1) {
					sql.append(" and gcsj >= ")
					.append(
							"to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					whereSqlArray.add(value);
				} else if (key.indexOf("jssj") != -1) {
					sql.append(" and gcsj <= ")
					.append(
							"to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					whereSqlArray.add(value);
				} else if ("hphm".equalsIgnoreCase(key)) {
					hphmFlag= true;
					//String temp = Common.changeHphm(value).toUpperCase();
//					String temp =value;
//					if(temp.length()<7&&temp.length()>0){
//						temp=temp+"%";
//					}
//					//使用号牌号码索引
//						/*if(opt.getCode()==3){
//							opt.setCode(4);
//						}else opt.setCode(2);*/
//					  opt.setCode(1);
//					if ((temp.endsWith("%")) || (temp.indexOf("_") > 0)) {
//						//sql.append(" and hphm like '" + temp + "' ");
//						sql.append(" and hphm like ? ");
//						whereSqlArray.add(temp) ;
//					} else {
						sql.append(" and hphm = ? ");
						whereSqlArray.add(value) ;
//					}
				} else if("kdbhlike".equalsIgnoreCase(key)){
					sql.append(" and kdbh like '" + value + "%' ");
				} else if ("kdbh".equalsIgnoreCase(key)) {
					//sql.append(" and kdbh in ('" + value + "') ");
					sql.append(" and kdbh = ? ");
					whereSqlArray.add(value) ;
					/*if(opt.getCode()==1){
						opt.setCode(4);
					}else opt.setCode(3);*/
					//if(opt.check()){
						//opt.setCode(3);
					//}
				}else if ("licesenHeader".equalsIgnoreCase(key)) {
					String[] licesenHeaders = value.trim().split("\\p{Punct}");
					if (licesenHeaders.length <= 0)
						continue;
					sql.append("and ( ");
					for (String licesenHeader : licesenHeaders) {
						sql.append(" hphm like '" + licesenHeader + "%' or ");
					}
					sql.delete(sql.length() - 3, sql.length());
					sql.append(")");
				} else {
					if ("hphm".equalsIgnoreCase(key)
							|| "hpzl".equalsIgnoreCase(key)
							|| "hpys".equalsIgnoreCase(key)
							|| "fxbh".equalsIgnoreCase(key)) {
						sql.append(" and ");
						sql.append(key);
						sql.append(" = '").append(value+"'");
					}

				}
			}
		}
		//添加红名单过滤
		sql.append(" and not exists(select a.hphm from jm_red_namelist a where t.hphm=a.hphm and a.status='1' and a.isvalid='1')");
		StringBuffer sb = new StringBuffer();
		if(hphmFlag){
			sql.append(" order by gcsj desc ");
		}
		sb.append("select * from " + table
				+ " t where 1=1 ");
		sb.append(sql);
		Map<String, Object> map = null;
		// 第一次查询统计总数，点击datagrid分页按钮只查询当页数据，使用分页标记
		if ("0".equals(conditions.get("pageSign"))) {
			if (conditions.get("cityname") == null
					|| conditions.get("cityname").toString().length() < 1) {
				map = this.findPageForMapByIndex(sb.toString(),conditions.get("count").toString(), Integer.parseInt(conditions.get("page")
						.toString()), Integer.parseInt(conditions.get("rows")
						.toString()), VehPassrec.class,whereSqlArray);
			} else {
				JdbcTemplate jd = SpringApplicationContext.getRemoteSourse(
						conditions.get("cityname").toString(), "jcbk", false);
				map = this.findPageForMapByIndex(sb.toString(),
						conditions.get("count").toString(),
						Integer.parseInt(conditions.get("page").toString()),
						Integer.parseInt(conditions.get("rows").toString()),
						VehPassrec.class,whereSqlArray,jd);
			}
		} else {
			if (conditions.get("cityname") == null
					|| conditions.get("cityname").toString().length() < 1) {
				map = this.findPageForMapByIndex(sb.toString(),
						conditions.get("count").toString(),
						Integer.parseInt(conditions.get("page").toString()),
						Integer.parseInt(conditions.get("rows").toString()),
						VehPassrec.class,
						whereSqlArray);
			} else {
				JdbcTemplate jd = SpringApplicationContext.getRemoteSourse(
						conditions.get("cityname").toString(), "jcbk", false);
				map = this.findPageForMapByIndex(sb.toString(),
						conditions.get("count").toString(),
						Integer.parseInt(conditions.get("page").toString()),
						Integer.parseInt(conditions.get("rows").toString()),
						VehPassrec.class,whereSqlArray,jd);
			}

		}
		return map;
	}

	public Map<String, Object> findPageForMapByIndex(String sql, String total,
			int curPage, int pageSize, Class<?> clazz,List<String> whereSqlArray) {
		// 总记录数
		int totalRows = Integer.parseInt(total);
		// 总页数
		int totalPages;
		if (pageSize != -1) {
			if (totalRows % pageSize == 0) {
				totalPages = totalRows / pageSize;
			} else {
				totalPages = (totalRows / pageSize) + 1;
			}
		} else {
			totalPages = 1;
		}
		// 起始行数
		int startIndex = (curPage - 1) * pageSize;
		// 结束行数
		int lastIndex = 0;
		if (totalRows < pageSize) {
			lastIndex = totalRows;
		} else if ((totalRows % pageSize == 0)
				|| (totalRows % pageSize != 0 && curPage < totalPages)) {
			lastIndex = curPage * pageSize;
		} else if (totalRows % pageSize != 0 && curPage == totalPages) {// 最后一页
			lastIndex = totalRows;
		}
		// 分页语句
		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT ");
		paginationSQL.append("temp.* ,ROWNUM num FROM ( ");
		paginationSQL.append(sql);
		if (pageSize != -1) {
			paginationSQL.append("" + ") temp where ROWNUM <= " + lastIndex);
		} else {
			paginationSQL.append("" + ") temp ");
		}
		paginationSQL.append(" ) WHERE" + " num > " + startIndex);
		
		//====================================================================
		System.out.println("分页预处理SQL--"+paginationSQL.toString());
		log.info("分页预处理SQL--"+paginationSQL.toString());
		//还原预处理测试
		Object sqltemp[] =whereSqlArray.toArray();
		String sqlOutPut="";
		sqlOutPut = paginationSQL.toString();
		for(int i =0;i<sqltemp.length;i++){
			sqlOutPut =sqlOutPut.replaceFirst("\\?", "'"+(String) sqltemp[i]+"'");
		}
		System.out.println("分页执行SQL--"+sqlOutPut);
		log.info("分页执行SQL--"+sqlOutPut);
		//====================================================================
		
		List list = this.queryForList(paginationSQL.toString(), whereSqlArray.toArray(),clazz);
		Map<String, Object> map = new HashMap<String, Object>();
		// 设置总共有多少条记录
		map.put("total", totalRows);
		// 设置当前页的数据
		map.put("rows", list);
		return map;
	}

	/**
	 * 用户查询提示
	 * @param condition
	 * @return
	 */
	public int queryTips(Map condition) {
		StringBuffer sql = new StringBuffer("");
		int total=0;
		//索引策略 默认使用过车时间索引
		OptimizeStrategy opt=new OptimizeStrategy("t");
	
		if(condition.get("kdbh")!=null && !"".equals(condition.get("kdbh"))) {
			/*if(opt.check()){
				if(opt.getCode()==1){
					opt.setCode(4);
				}else opt.setCode(3);
			}*/
			if(opt.check()){
				opt.setCode(3);
			}
			sql.append(" and kdbh = '").append(condition.get("kdbh")).append("' ");
		}
		if(condition.get("fxbh")!=null && !"".equals(condition.get("fxbh"))) {
			sql.append(" and fxbh = '").append(condition.get("fxbh")).append("' ");
		}
		if(condition.get("licesenHeader")!=null && !"".equals(condition.get("licesenHeader"))) {
			String[] licesenHeaders = condition.get("licesenHeader").toString().trim().split("\\p{Punct}");
			if (licesenHeaders.length > 0) {
				
			sql.append("and ( ");
			for (String licesenHeader : licesenHeaders) {
				sql.append(" hphm like '" + licesenHeader + "%' or ");
			}
			sql.delete(sql.length() - 3, sql.length());
			sql.append(") ");
			}
		}
		
		if(condition.get("exactselect")!=null && !"".equals(condition.get("exactselect"))){//判断是否是精确查询车牌
			if(condition.get("hphm")!=null && !"".equals(condition.get("hphm"))) {
				//使用号牌号码索引
				opt.setCode(1);
				sql.append(" and hphm ='").append(condition.get("hphm")).append("' ");
			}
		}else{
			if(condition.get("hphm")!=null && !"".equals(condition.get("hphm"))) {
				//使用号牌号码索引
				opt.setCode(1);
				/*if(opt.check()){
					if(opt.getCode()==3){
						opt.setCode(4);
					}else opt.setCode(2);
				}*/
				sql.append(" and hphm  like '%").append(condition.get("hphm")).append("%' ");
			}
		}
		
		
		if(condition.get("hpzl")!=null && !"".equals(condition.get("hpzl"))) {
			sql.append(" and hpzl = '").append(condition.get("hpzl")).append("' ");
		}
		if(condition.get("kssj")!=null && !"".equals(condition.get("kssj"))) {
			sql.append(" and gcsj >= ").append(
					"to_date('" + condition.get("kssj") + "','yyyy-mm-dd hh24:mi:ss')");
		}
		if(condition.get("jssj")!=null && !"".equals(condition.get("jssj"))) {
			sql.append(" and gcsj <= ").append(
					"to_date('" + condition.get("jssj") + "','yyyy-mm-dd hh24:mi:ss')");
		}
		
		String table = this.getTableName();
		StringBuffer indexSql=new StringBuffer();
		if(opt.getCode()==0){
			opt.setCode(2);
		}
		
		//如果查询条件是 过车时间+卡点编号直接返回1000条记录(如果是使用号牌号码为索引就加上过车时间倒序)
		//if(opt.getCode()!=1){
		//	return total = 1000;
		//}
		indexSql.append("select " + opt.algorithm() + " * from " + table
				+ " t where 1=1 ");
		//StringBuffer indexSql=new StringBuffer("select "+opt.algorithm()+"* from veh_passrec t where 1=1 "); 
		sql=indexSql.append(sql);
		Map map = null;
		
		if (condition.get("cityname") == null
				|| condition.get("cityname").toString().length() < 1) {
		    
			total = this.getTotalCount(sql);
			//map = this.getSelf().findPageForMap(sql.toString(), 1, 1, VehPassrec.class);
		} else {
			JdbcTemplate jd =  SpringApplicationContext.getRemoteSourse(condition.get("cityname").toString().toUpperCase(), "jcbk", false);
			map = this.getSelf().findPageForMapByJdbc(sql.toString(), 1, 10,VehPassrec.class, jd);
			total = Integer.parseInt(map.get("total").toString());
		}
		return total;
	}
	
	/**
	 * 用户查询提示（含交警过车信息查询）
	 * @param condition
	 * @return
	 */
	public int queryTipsAndJj(Map condition) {
		StringBuffer sql = new StringBuffer(50);
		int total=0;
		String table ="";
		Map map = null;
		boolean flag = false;
		//索引策略 默认使用过车时间索引
		OptimizeStrategy opt=new OptimizeStrategy("t");
		if(condition.get("kdbh")!=null && !"".equals(condition.get("kdbh"))) {
			if(opt.check()){
				opt.setCode(3);
			}
			sql.append(" and kdbh = '").append(condition.get("kdbh")).append("' ");
		}
		if(StringUtils.isNotBlank((String)condition.get("kdbhlike"))){
			sql.append(" and kdbh like '").append((String)condition.get("kdbhlike")).append("%'");
		}
		if(condition.get("fxbh")!=null && !"".equals(condition.get("fxbh"))) {
			sql.append(" and fxbh = '").append(condition.get("fxbh")).append("' ");
		}
		if(condition.get("licesenHeader")!=null && !"".equals(condition.get("licesenHeader"))) {
			String[] licesenHeaders = condition.get("licesenHeader").toString().trim().split("\\p{Punct}");
			if (licesenHeaders.length > 0) {
				
			sql.append("and ( ");
			for (String licesenHeader : licesenHeaders) {
				sql.append(" hphm like '" + licesenHeader + "%' or ");
			}
			sql.delete(sql.length() - 3, sql.length());
			sql.append(") ");
			}
		}
		if(condition.get("hphm")!=null && !"".equals(condition.get("hphm"))) {
			//使用号牌号码索引
			opt.setCode(1);
			sql.append(" and hphm = '").append(condition.get("hphm")).append("' ");
		}
		if(condition.get("hpzl")!=null && !"".equals(condition.get("hpzl"))) {
			sql.append(" and hpzl = '").append(condition.get("hpzl")).append("' ");
		}
		if(condition.get("kssj")!=null && !"".equals(condition.get("kssj"))) {
			sql.append(" and gcsj >= ").append(
					"to_date('" + condition.get("kssj") + "','yyyy-mm-dd hh24:mi:ss')");
		}
		if(condition.get("jssj")!=null && !"".equals(condition.get("jssj"))) {
			sql.append(" and gcsj <= ").append(
					"to_date('" + condition.get("jssj") + "','yyyy-mm-dd hh24:mi:ss')");
		}
		//更改查询的表名
		if (condition.get("cityname") == null
				|| condition.get("cityname").toString().length() < 1) {
			flag = true;
			table = "T_JJ_VEH_PASSREC";
		}else{
			 table = "VEH_PASSREC";
		}
		StringBuffer indexSql=new StringBuffer();
		if(opt.getCode()==0){
			opt.setCode(2);
		}
		
		//如果查询条件是 过车时间+卡点编号直接返回1000条记录(如果是使用号牌号码为索引就加上过车时间倒序)
		//if(opt.getCode()!=1){
		//	return total = 1000;
		//}
		/*indexSql.append("select " + opt.algorithm() + " * from " + table
				+ " t where 1=1 ");*/
		indexSql.append("select * from " + table
				+ " t where 1=1 ");
		//StringBuffer indexSql=new StringBuffer("select "+opt.algorithm()+"* from veh_passrec t where 1=1 "); 
		sql=indexSql.append(sql);
		if (flag) {
			total = this.getTotalCount(sql);
			//map = this.getSelf().findPageForMap(sql.toString(), 1, 1, VehPassrec.class);
		} else {
			///修改表名，应对应到各地市
			JdbcTemplate jd =  SpringApplicationContext.getRemoteSourse(condition.get("cityname").toString().toUpperCase(), "jcbk", false);
			map = this.getSelf().findPageForMapByJdbc(sql.toString(), 1, 10,VehPassrec.class, jd);
			total = Integer.parseInt(map.get("total").toString());
		}
		return total;
	}
	
    public int getTotalCount(StringBuffer sql) {
    	StringBuffer totalSQL = new StringBuffer(" SELECT count(1) FROM ( select /*+FIRST_ROWS*/* from (");	
    	totalSQL.append(sql);
    	//添加红名单过滤
    	totalSQL.append(" and not exists(select a.hphm from jm_red_namelist a where t.hphm=a.hphm and a.status='1' and a.isvalid='1')");
    	totalSQL.append(") where rownum <= 1000 ) totalTable ");
    	int total=0;
    	try{
    		System.out.println(totalSQL.toString());
    	    total=this.jdbcTemplate.queryForInt(totalSQL.toString());
    	    if(total!=0){
    	    	return total;
    	    }
    	} catch(Exception e){
    		return -1;
    	}
    	return total;
    }
	
	public int getAllCount(Map<String, Object> conditions) throws Exception {
		String table = "";
		table = this.getTableName();
		StringBuffer sql = new StringBuffer(50);
		//索引策略 默认使用过车时间索引
		OptimizeStrategy opt=new OptimizeStrategy("t");
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter = "sort".equalsIgnoreCase(key)
					|| "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key)
					|| "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.indexOf("kssj") != -1) {
					sql.append(" and gcsj >= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else if (key.indexOf("jssj") != -1) {
					sql.append(" and gcsj <= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else if ("hphm".equalsIgnoreCase(key)) {
					String temp = Common.changeHphm(value).toUpperCase();
					//使用号牌号码索引
					if(opt.getCode()==3){
						opt.setCode(4);
					}else opt.setCode(2);
					if ((temp.indexOf("%") > 0) || (temp.indexOf("_") > 0)) {
						sql.append(" and hphm like '" + temp + "' ");
					} else {
						sql.append(" and hphm = '" + temp + "' ");
					}
				} else if ("kdbh".equalsIgnoreCase(key)) {
					if(opt.getCode()==1){
						opt.setCode(4);
					}else opt.setCode(3);
					//sql.append(" and kdbh in ('" + value + "') ");
					sql.append(" and kdbh = '" + value + "' ");

				} else if ("licesenHeader".equalsIgnoreCase(key)) {
					String[] licesenHeaders = value.trim().split("\\p{Punct}");
					if (licesenHeaders.length <= 0)
						continue;
					sql.append("and ( ");
					for (String licesenHeader : licesenHeaders) {
						sql.append(" hphm like '" + licesenHeader + "%' or ");
					}
					sql.delete(sql.length() - 3, sql.length());
					sql.append(")");
				} else {

					if ("hphm".equalsIgnoreCase(key)
							|| "hpzl".equalsIgnoreCase(key)
							|| "hpys".equalsIgnoreCase(key)
							|| "fxbh".equalsIgnoreCase(key)) {
						sql.append(" and ");
						sql.append(key);
						sql.append(" = '").append(value).append("'");
					}

				}
			}
		}
		StringBuffer sb = new StringBuffer();
		if(opt.getCode()==0){
			opt.setCode(2);
		}
		sb.append("select " + opt.algorithm() + " count(1) from " + table
				+ " t where 1=1 ");
		sb.append(sql);
		//添加红名单过滤
		sb.append(" and not exists(select a.hphm from jm_red_namelist a where t.hphm=a.hphm and a.status='1' and a.isvalid='1')");
		
		//System.out.println("异步统计总数的sql--"+sb.toString());
		log.info("异步统计总数的sql--"+sb.toString());
		//return 1000;
		int total = this.jdbcTemplate.queryForInt(sb.toString());
		return total ;
	}
	
	/**
	 * 统计查询总数(含交警卡口)
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public int getPassAndJjAllCount(Map<String, Object> conditions) throws Exception {
		String table = "";
		table = "T_JJ_VEH_PASSREC";
		StringBuffer sql = new StringBuffer(50);
		//索引策略 默认使用过车时间索引
		OptimizeStrategy opt=new OptimizeStrategy("t");
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter = "sort".equalsIgnoreCase(key)
					|| "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key)
					|| "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.indexOf("kssj") != -1) {
					sql.append(" and gcsj >= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else if (key.indexOf("jssj") != -1) {
					sql.append(" and gcsj <= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else if ("hphm".equalsIgnoreCase(key)) {
					String temp = Common.changeHphm(value).toUpperCase();
					//使用号牌号码索引
					if(opt.getCode()==3){
						opt.setCode(4);
					}else opt.setCode(2);
					if ((temp.indexOf("%") > 0) || (temp.indexOf("_") > 0)) {
						sql.append(" and hphm like '" + temp + "' ");
					} else {
						sql.append(" and hphm = '" + temp + "' ");
					}
				} else if ("kdbh".equalsIgnoreCase(key)) {
					if(opt.getCode()==1){
						opt.setCode(4);
					}else opt.setCode(3);
					//sql.append(" and kdbh in ('" + value + "') ");
					sql.append(" and kdbh = '" + value + "' ");

				} else if ("licesenHeader".equalsIgnoreCase(key)) {
					String[] licesenHeaders = value.trim().split("\\p{Punct}");
					if (licesenHeaders.length <= 0)
						continue;
					sql.append("and ( ");
					for (String licesenHeader : licesenHeaders) {
						sql.append(" hphm like '" + licesenHeader + "%' or ");
					}
					sql.delete(sql.length() - 3, sql.length());
					sql.append(")");
				} else {

					if ("hphm".equalsIgnoreCase(key)
							|| "hpzl".equalsIgnoreCase(key)
							|| "hpys".equalsIgnoreCase(key)
							|| "fxbh".equalsIgnoreCase(key)) {
						sql.append(" and ");
						sql.append(key);
						sql.append(" = '").append(value).append("'");
					}

				}
			}
		}
		StringBuffer sb = new StringBuffer();
		if(opt.getCode()==0){
			opt.setCode(2);
		}
		sb.append("select " + opt.algorithm() + " count(1) from " + table
				+ " t where 1=1 ");
		sb.append(sql);
		//添加红名单过滤
		sb.append(" and not exists(select a.hphm from jm_red_namelist a where t.hphm=a.hphm and a.status='1' and a.isvalid='1')");
		
		//System.out.println("异步统计总数的sql--"+sb.toString());
		log.info("异步统计总数的sql--"+sb.toString());
		//return 1000;
		int total = this.jdbcTemplate.queryForInt(sb.toString());
		return total ;
	}
	
	
	
	public Map<String, Object> findPageForMapByIndex(String sql, String total,
			int curPage, int pageSize, Class<?> clazz,List<String> whereSqlArray,JdbcTemplate jd) {

		// 总记录数
		int totalRows = Integer.parseInt(total);
		// 总页数
		int totalPages;
		if (pageSize != -1) {
			if (totalRows % pageSize == 0) {
				totalPages = totalRows / pageSize;
			} else {
				totalPages = (totalRows / pageSize) + 1;
			}
		} else {
			totalPages = 1;
		}
		// 起始行数
		int startIndex = (curPage - 1) * pageSize;
		// 结束行数
		int lastIndex = 0;
		if (totalRows < pageSize) {
			lastIndex = totalRows;
		} else if ((totalRows % pageSize == 0)
				|| (totalRows % pageSize != 0 && curPage < totalPages)) {
			lastIndex = curPage * pageSize;
		} else if (totalRows % pageSize != 0 && curPage == totalPages) {// 最后一页
			lastIndex = totalRows;
		}

		// 构造oracle数据库的分页语句
		StringBuffer cbo_sql = new StringBuffer(20);
		cbo_sql.append(" /*+FIRST_ROWS(").append(pageSize).append(")*/ ");
		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT ");
		paginationSQL.append(cbo_sql.toString());
		paginationSQL.append("temp.* ,ROWNUM num FROM ( ");
		paginationSQL.append(sql);
		//添加红名单过滤
		//paginationSQL.append(" and not exists(select a.hphm from jm_red_namelist a where t.hphm=a.hphm and a.status='1' and a.isvalid='1')");
		//过车时间倒序
		//paginationSQL.append(" ORDER BY GCSJ DESC");
		if (pageSize != -1) {
			paginationSQL.append("" + ") temp where ROWNUM <= " + lastIndex);
		} else {
			paginationSQL.append("" + ") temp ");
		}
		paginationSQL.append(" ) WHERE" + " num > " + startIndex);
		
		System.out.println("根据索引查询的sql(预处理)--"+paginationSQL.toString());
		log.info("根据索引查询的sql(预处理)--"+paginationSQL.toString());
		//还原预处理之前的sql--用于测试
		Object sqltemp[] =whereSqlArray.toArray();
		String sqlOutPut="";
		sqlOutPut = paginationSQL.toString();
		for(int i =0;i<sqltemp.length;i++){
			sqlOutPut =sqlOutPut.replaceFirst("\\?", "'"+(String) sqltemp[i]+"'");
		}
		System.out.println("根据索引查询的sql(还原预处理sql)--"+sqlOutPut);
		log.info("根据索引查询的sql(还原预处理sql)--"+sqlOutPut);
		List list = this.queryForList(paginationSQL.toString(), whereSqlArray.toArray(),clazz,jd);
		Map<String, Object> map = new HashMap<String, Object>();
		// 设置总共有多少条记录
		map.put("total", totalRows);
		// 设置当前页的数据
		map.put("rows", list);

		return map;
	}

	//============================新过车查询改造====================================
	
	public int queryPassCount(String sql, Object... params) throws Exception {
		log.info("JCBKSQL-COUNT:" + sql);
		int result = 0;
		try{
			Object r = this.jdbcTemplate.queryForObject(sql, params, Object.class);
			result = Integer.valueOf(r.toString());
		}catch(EmptyResultDataAccessException e){
			//不做处理
		}
		return result;
	}

	public List queryPassPage(String sql, Object[] params, Class<?> clazz)
			throws Exception {
		long pstart = System.currentTimeMillis();
		List list = this.jdbcTemplate.query(sql, params, new BeanPropertyRowMapper(clazz));
		long pend = System.currentTimeMillis();
		log.info("JCBKSQL-PAGE:(" + (pend-pstart) + "毫秒)" + sql);
		return list;
	}
	
	//=============================================================================
}
