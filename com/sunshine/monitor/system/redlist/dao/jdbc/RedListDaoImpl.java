package com.sunshine.monitor.system.redlist.dao.jdbc;

import java.io.Serializable;
import java.security.Key;
import java.util.*;
import java.util.Map.Entry;

import javax.annotation.Resource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.redlist.bean.RedList;
import com.sunshine.monitor.system.redlist.dao.RedListDao;

@Repository
public class RedListDaoImpl extends BaseDaoImpl implements RedListDao {

	@Resource(name = "redlistCache")
	private Cache cache;

	private Logger logger = LoggerFactory.getLogger(RedListDaoImpl.class);
	
	/**
	 * 初始化缓存
	 */
	@Deprecated
	public void initRedList() {
		List<RedList> list = null;
		try {
			list = queryAllRedList();
			Set<String> set = new HashSet<String>();
			for(RedList redList : list) {
				String _hphm = redList.getHphm();
				set.add(_hphm);
			}
			Element element = new Element("redlist",(Serializable)set);
			cache.put(element);
			logger.info("系统有效红名单 " + set.size() + "条加入缓存!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Deprecated
	public void deleteCacheData(String hphm) throws Exception{
		synchronized (this) {
			Element element = this.cache.get("redlist");
			Set<String> set = (Set<String>)element.getObjectValue();
			set.remove(hphm);
			this.cache.replace(new Element("redlist",(Serializable)set));
			logger.info(hphm + "已经从缓存中清除!");
		}
	}
	
	@Deprecated
	public void addDatatoCache(String hphm) throws Exception{
		synchronized (this) {
			Element element = this.cache.get("redlist");
			Set<String> set = (Set<String>)element.getObjectValue();
			set.add(hphm);
			this.cache.replace(new Element("redlist",(Serializable)set));
			logger.info(hphm + "加入缓存!");
		}
	}

	public List<RedList> queryAllRedList() throws Exception {
		String sql = "select id,hphm, hpzl, clxh, clpp, cllx, hpys, csys, clsyr, syrlxdh, syrxxdz, kssj, jssj, status, auditman, auditdept, auditdate, isvalid from jm_red_namelist where isvalid = '1' and status = '1' and sysdate < jssj" ;
		List<RedList> list = this.queryForList(sql, RedList.class);
		return list;
	}

	public int addRedList(RedList redlist) throws Exception {
		List param = new ArrayList<>();
		String id = this.jdbcTemplate.queryForObject("select SEQ_JM_RED_NAMELIST.NEXTVAL from dual", String.class);
		String sql = "insert into jm_red_namelist(id,hphm, hpzl, clxh, clpp, cllx, hpys, csys, " +
				"clsyr, syrlxdh, syrxxdz,kssj, jssj, status, isvalid) values (?,?,?,?,?,?,?,?,?,?,?,sysdate,add_months(sysdate,12),?," + "'1')";
		param.add(id);
		param.add(redlist.getHphm());
		if(redlist.getHpzl() == null){
			param.add("''");
		}else{
			param.add(redlist.getHpzl());
		}

		if(redlist.getClxh() == null){
			param.add("''");
		}else{
			param.add(redlist.getClxh());
		}

		if(redlist.getClpp() == null){
			param.add("''");
		}else{
			param.add(redlist.getClpp());
		}

		if(redlist.getCllx() == null){
			param.add("''");
		}else{
			param.add(redlist.getCllx());
		}

		if(redlist.getHpys() == null){
			param.add("''");
		}else{
			param.add(redlist.getHpys());
		}
		if(redlist.getCsys() == null){
			param.add("''");
		}else{
			param.add(redlist.getCsys());
		}

		if(redlist.getClsyr() == null){
			param.add("''");
		}else{
			param.add(redlist.getClsyr());
		}

		if(redlist.getSyrlxdh() == null){
			param.add("''");
		}else{
			param.add(redlist.getSyrlxdh());
		}


		if(redlist.getSyrxxdz() == null){
			param.add("''");
		}else{
			param.add(redlist.getSyrxxdz());
		}

		param.add(redlist.getStatus());


		//addDatatoCache(redlist.getHphm());
		return this.jdbcTemplate.update(sql,param.toArray());
	}
	
	public int updateinValidRedList(RedList redlist) throws Exception {
		String sql = "update jm_red_namelist set status='0', isvalid='1', auditman='', " +
				"auditdept='', auditdate='' where id = ?";
		return this.jdbcTemplate.update(sql,redlist.getId()) ;
	}

	public RedList queryRedList(String subSql) throws Exception {
		RedList redList = null;
		String sql = "select * from jm_red_namelist where 1 = 1";
		if (!"".equals(subSql) && subSql.length() > 0) {
			sql = sql + subSql;
		}
		List<RedList> list = this.queryForList(sql, RedList.class);
		if (list.size() > 0) {
			redList = list.get(0);
		}
		return redList;
	}

	public Map<String, Object> queryRedList(Map<String, Object> conditions)
			throws Exception {
		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer(20);
		sql.append("select * from jm_red_namelist where 1=1 ");
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter = "sort".equalsIgnoreCase(key)
					|| "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key)
					|| "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.equals("kssj")) {
					sql.append(" and kssj >= ").append(
							"to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					param.add(value);
				} else if (key.equals("jssj")) {
					sql.append(" and kssj <= ").append(
							"to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					param.add(value);
				} else {
					sql.append(" and ?=?");
					param.add(key);
					param.add(value);
				}
			}
		}
		sql.append(" order by ? ?");
		param.add(conditions.get("sort"));
		param.add(conditions.get("order"));
		Map<String, Object> map = findPageForMap(sql.toString(), param.toArray(),Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}

	public int updateRedList(RedList redlist) throws Exception {
		List param = new ArrayList<>();
		String sql = "update jm_red_namelist set status=?, isvalid=?, " +
				"auditman=?,auditdept=?,auditdate=sysdate where id=?";
		param.add(redlist.getStatus());
		param.add(redlist.getIsvalid());
		param.add(redlist.getAuditman());
		param.add(redlist.getAuditdept());
		param.add(redlist.getId());


		if(redlist.getStatus().equalsIgnoreCase("1")&&redlist.getIsvalid().equalsIgnoreCase("1")){
			addDatatoCache(redlist.getHphm());
		}
		return this.jdbcTemplate.update(sql, param.toArray());
	}

	public int deblockingRedList(RedList redlist) throws Exception {
		String sql = "update jm_red_namelist set status='0' where id=?";
		return this.jdbcTemplate.update(sql,redlist.getId());
	}

	public int deleteRedList(String id) throws Exception {
		String sql = "update jm_red_namelist set isvalid = '0' ,status = '0' where id=?";
		return this.jdbcTemplate.update(sql,id);
	}

	public String getTime() throws Exception {
		String sql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual";
		return (String) this.jdbcTemplate.queryForObject(sql, String.class);
	}

	public Map<String, Object> queryValidRedlist(Map<String, Object> conditions)
			throws Exception {
		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer(20);
		sql.append("select * from jm_red_namelist where 1=1  ");
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter = "sort".equalsIgnoreCase(key)
					|| "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key)
					|| "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.equals("kssj")) {
					sql.append(" and kssj >= ").append(
							"to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					param.add(value);
				} else if (key.equals("jssj")) {
					sql.append(" and kssj <= ").append(
							"to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					param.add(value);
				} else {
					sql.append(" and ?=?");
					param.add(key);
					param.add(value);
				}
			}
		}
		sql.append(" order by ? ?");
		param.add(conditions.get("sort"));
		param.add(conditions.get("order"));

		Map<String, Object> map = findPageForMap(sql.toString(), param.toArray(),Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}
}
