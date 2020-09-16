package com.sunshine.monitor.comm.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.Menu;
import com.sunshine.monitor.comm.dao.MenuDao;
import com.sunshine.monitor.comm.util.MenuTrees;

@Repository
public class MenuDaoImpl extends BaseDaoImpl implements MenuDao {
	
	private Logger log = LoggerFactory.getLogger(MenuDaoImpl.class);
	
	private String optimizeMode_all_rows = "/*+ALL_ROWS*/";
	
	private String optimizeMode_first_rows = "/*+FIRST_ROWS*/";
	
	
	public void init() throws Exception{
		initMenuTree();
	}	
	
	public void initMenuTree() throws Exception {
		MenuTrees.getInstance().clearMenuTrees();
		List<Menu> list = findMenuList();
		for(Menu menu : list){
			MenuTrees.getInstance().addMenu(menu);
		}
		log.debug("菜单加载完毕:"+list.size());
	}
	
	public List<Menu> findMenuList() throws Exception {
		String sql = "SELECT /*+ALL_ROWS*/ a.ID, a.NAME, a.YMDZ, a.SXH, a.CXSX, a.LJ, a.PID, a.BZ, a.LB FROM JM_MENU a";
		return this.queryList(sql, Menu.class);
	}
	
	public List<Menu> findTopMenuList() throws Exception {
		String sql = "SELECT ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB FROM JM_MENU WHERE PID IS NULL ORDER BY SXH ASC";
		return this.queryList(sql, Menu.class);
	}
	
	public List<Menu> findMenuListByRoles(String roles,String id) throws Exception {
		/*String sql = "SELECT ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB " +
		" FROM JM_MENU m,JM_ROLEMENU r " +
		" WHERE 1=1 and  m.ID=r.CXDH AND m.ID!= " + id + " AND r.JSDH IN("+roles+")" +
		" START WITH m.ID=" + id +
		" CONNECT BY PRIOR m.ID = m.PID "+
		" GROUP BY ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB" +
		" ORDER BY BZ,SXH";*/
		String sql = "SELECT ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB, ISPOP  " +
		" FROM JM_MENU m" +
		" WHERE 1=1  AND m.ID!= ? AND m.id  in (select cxdh from JM_ROLEMENU where jsdh in (?))" +
		" START WITH m.ID= ? CONNECT BY PRIOR m.ID = m.PID "+
		" GROUP BY ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB, ISPOP" +
		" ORDER BY BZ,SXH";
		
		return this.jdbcTemplate.query(sql, new JcbkRowMapper<>(Menu.class),id,roles,id);
	}
/*
	public String findMenuUrlsForString(String roleId) throws Exception {
		String sql = "select menuurl from jm_roleurl where jsdh='" + roleId + "'";
		String menuUrl = this.queryForObject(sql, String.class);
		return menuUrl;
	}
*/	
	public Menu findMenu(String id) throws Exception {
		String sql = "SELECT ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB FROM JM_MENU WHERE ID=?";
		return this.queryObject(sql, Menu.class,id);
	}

	public List<Menu> findMenuListByPid(String id) throws Exception {
		String sql = "SELECT ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB FROM JM_MENU WHERE PID=?";
		return this.queryList(sql, Menu.class,id);
	}
	
	public List<Menu> queryMenuDirectory() throws Exception {
		String sql = "SELECT ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB FROM JM_MENU WHERE LB='1'";
		return this.queryList(sql, Menu.class);
	}	
	
	public Map<String, Object> findMenuListByPage(Map<?, ?> condition)
			throws Exception {
		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer(
				"SELECT ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB FROM JM_MENU WHERE 1=1 ");
		if(condition.get("id") != null && !"".equals(condition.get("id"))){
			sql.append(" and id = ?");
			param.add(condition.get("id").toString().trim());
		}
		if (condition.get("name") != null && !"".equals(condition.get("name"))) {
			sql.append(" and name like ?");
			param.add("%"+condition.get("name").toString().trim()+"%");
		}
		sql.append(" order by ? ?");
		param.add(condition.get("sort"));
		param.add(condition.get("order"));
		Object[] array = param.toArray(new Object[param.size()]);
		return findPageForMap(sql.toString(), array,Integer.parseInt(condition.get(
				"page").toString()), Integer.parseInt(condition.get("rows")
				.toString()));
	}

	public boolean insertMenu(Menu menu) throws Exception {
		List param = new ArrayList<>();
		boolean success = true ;
		String _sql = "SELECT MENU_CXDH_SEQ.NEXTVAL as id from dual";
		int id_value = this.jdbcTemplate.queryForInt(_sql);
		String pid = "";
		if("1".equals(menu.getLb()) && (menu.getPid()==null || "".equals(menu.getPid()))){
			pid = "" + id_value;
		}else{
			pid = menu.getPid();
		}
		StringBuffer sql = new StringBuffer(50);
		sql.append("INSERT INTO JM_MENU VALUES(?,?");
		param.add(id_value);
		param.add(menu.getName());
		if(menu.getYmdz()!=null){
			sql.append(",?");
			param.add(menu.getYmdz());
		}
		sql.append(",?,?");
		param.add(menu.getSxh());
		param.add(menu.getCxsx());
		if(menu.getLj()!=null){
			sql.append(",?");
			param.add(menu.getLj());
		}
		sql.append(",?");
		param.add(pid);
		if (menu.getBz()!=null){
			sql.append(",?");
			param.add(menu.getBz());
		}
		sql.append(",?");
		param.add(menu.getLb());
		if(menu.getIspop()!=null){
			sql.append("?");
			param.add(menu.getIspop());
		}
		Object[] array = param.toArray(new Object[param.size()]);
		int result = this.jdbcTemplate.update(sql.toString(),array);
		if(result < 1) 
			success = false;
		return success;
	}

	public boolean deleteMenu(String id) throws Exception {
		String sql = "DELETE FROM JM_MENU WHERE ID = ?";
		int result = this.jdbcTemplate.update(sql,id);
		return (result < 1)? false : true;
	}

	public boolean batchDeleteMenus(final String[] ids) throws Exception{
		boolean flag = true ;
		int i = 0 ;

		try {
			String sql = "DELETE FROM JM_MENU WHERE ID = ?";
			this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, Arrays.asList(ids).get(i));
				}
				@Override
				public int getBatchSize() {
					return ids.length;
				}
			});
		} catch (DataAccessException e) {
			flag=false;
			e.printStackTrace();
		}
		return flag ;
	}

	public boolean updateMenu(Menu menu) throws Exception {
		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer(50);
		sql.append("UPDATE JM_MENU SET ");
		sql.append("NAME = ?,");
		param.add(menu.getName());
		if(menu.getYmdz()!=null){
			sql.append("YMDZ = ?,");
			param.add(menu.getYmdz());
		}

		sql.append("SXH = ?,");
		param.add(menu.getSxh());
		sql.append("CXSX = ?,");
		param.add(menu.getCxsx());
		if(menu.getLj()!=null){
			sql.append("LJ = ?,");
			param.add(menu.getLj());
		}
		sql.append("PID = ?,");
		param.add(menu.getPid());
		sql.append("BZ = ?,");
		param.add(menu.getBz());
		sql.append("LB = ?");
		param.add(menu.getLb());
		sql.append(" WHERE ID = ?");
		param.add(menu.getId());

		Object[] array = param.toArray(new Object[param.size()]);
		int count = this.jdbcTemplate.update(sql.toString(),array);
		return (count<1)?false:true;
	}

	@Override
	public List<Menu> findMenuListByRoles(String roles) throws Exception {		
		String sql = "SELECT ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB " +
		" FROM JM_MENU m" +
		" WHERE 1=1  AND m.id  in (select cxdh from JM_ROLEMENU where jsdh in (?))";
		return this.queryList(sql, Menu.class,roles);
	}

	@Override
	public List<Map<String,Object>> getIndexMenuPower(String filter) throws Exception {
		String sql="select menuid,status from frm_index_menu_power order by status";
		return this.jdbcTemplate.queryForList(sql);
	}

	
}