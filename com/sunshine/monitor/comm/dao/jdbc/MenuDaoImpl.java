package com.sunshine.monitor.comm.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
		" WHERE 1=1  AND m.ID!= " + id + " AND m.id  in (select cxdh from JM_ROLEMENU where jsdh in ("+roles+"))" +
		" START WITH m.ID=" + id +
		" CONNECT BY PRIOR m.ID = m.PID "+
		" GROUP BY ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB, ISPOP" +
		" ORDER BY BZ,SXH";
		
		return this.queryList(sql, Menu.class);}
/*
	public String findMenuUrlsForString(String roleId) throws Exception {
		String sql = "select menuurl from jm_roleurl where jsdh='" + roleId + "'";
		String menuUrl = this.queryForObject(sql, String.class);
		return menuUrl;
	}
*/	
	public Menu findMenu(String id) throws Exception {
		String sql = "SELECT ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB FROM JM_MENU WHERE ID="
				+ id;
		return this.queryObject(sql, Menu.class);
	}

	public List<Menu> findMenuListByPid(String id) throws Exception {
		String sql = "SELECT ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB FROM JM_MENU WHERE PID="
				+ id;
		return this.queryList(sql, Menu.class);
	}
	
	public List<Menu> queryMenuDirectory() throws Exception {
		String sql = "SELECT ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB FROM JM_MENU WHERE LB='1'";
		return this.queryList(sql, Menu.class);
	}	
	
	public Map<String, Object> findMenuListByPage(Map<?, ?> condition)
			throws Exception {
		StringBuffer sql = new StringBuffer(
				"SELECT ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB FROM JM_MENU WHERE 1=1 ");
		if(condition.get("id") != null && !"".equals(condition.get("id"))){
			sql.append(" and id = '").append(condition.get("id").toString().trim()).append("'");
		}
		if (condition.get("name") != null && !"".equals(condition.get("name"))) {
			sql.append(" and name like '%");
			sql.append(condition.get("name").toString().trim());
			sql.append("%'");
		}
		sql.append(" order by ");
		sql.append(condition.get("sort"));
		sql.append(" ");
		sql.append(condition.get("order"));
		return findPageForMap(sql.toString(), Integer.parseInt(condition.get(
				"page").toString()), Integer.parseInt(condition.get("rows")
				.toString()));
	}

	public boolean insertMenu(Menu menu) throws Exception {
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
		sql.append("INSERT INTO JM_MENU VALUES('"+id_value+"','");
		sql.append(menu.getName()).append("','");
		sql.append((menu.getYmdz() == null) ? "" : menu.getYmdz()).append("',");
		sql.append(menu.getSxh()).append(",");
		sql.append(menu.getCxsx()).append(",'");
		sql.append((menu.getLj() == null) ? "" : menu.getLj()).append("','");
		sql.append(pid).append("','");
		sql.append((menu.getBz() == null) ? "" : menu.getBz()).append("','");
		sql.append(menu.getLb()).append("','");
		sql.append((menu.getIspop() == null) ? "" : menu.getIspop());
		sql.append("')");
		int result = this.jdbcTemplate.update(sql.toString());
		if(result < 1) 
			success = false;
		return success;
		
	}

	public boolean deleteMenu(String id) throws Exception {
		String sql = "DELETE FROM JM_MENU WHERE ID = " + id;
		int result = this.jdbcTemplate.update(sql);
		return (result < 1)? false : true;
	}

	public boolean batchDeleteMenus(String[] ids) throws Exception{
		boolean flag = true ;
		String[] sqlArray = new String[ids.length];
		int i = 0 ;
		for (String id : ids) {
			sqlArray[i]="DELETE FROM JM_MENU WHERE ID = " + id;
			i = i + 1 ;
		}
		int[] result = this.jdbcTemplate.batchUpdate(sqlArray);
		for (int j = 0; i < result.length; j++) {
			if(result[j]<0){
				// 第i+1条数据执行失败，只要有一条数据执行失败，则结果为失败
				flag = false ;
			}
		}
		return flag ;
	}

	public boolean updateMenu(Menu menu) throws Exception {
		StringBuffer sql = new StringBuffer(50);
		sql.append("UPDATE JM_MENU SET ");
		sql.append("NAME = '").append(menu.getName()).append("',");
		sql.append("YMDZ = '").append((menu.getYmdz()==null)?"":menu.getYmdz()).append("',");
		sql.append("SXH = ").append(menu.getSxh()).append(",");
		sql.append("CXSX = '").append(menu.getCxsx()).append("',");
		sql.append("LJ = '").append((menu.getLj()==null)?"":menu.getYmdz()).append("',");
		sql.append("PID = '").append(menu.getPid()).append("',");
		sql.append("BZ = '").append(menu.getBz()).append("',");
		sql.append("LB = '").append(menu.getLb()).append("'");
		sql.append(" WHERE ID = '").append(menu.getId()).append("'");
		int count = this.jdbcTemplate.update(sql.toString()) ;
		return (count<1)?false:true;
	}

	@Override
	public List<Menu> findMenuListByRoles(String roles) throws Exception {		
		String sql = "SELECT ID, NAME, YMDZ, SXH, CXSX, LJ, PID, BZ, LB " +
		" FROM JM_MENU m" +
		" WHERE 1=1  AND m.id  in (select cxdh from JM_ROLEMENU where jsdh in ("+roles+"))";		
		return this.queryList(sql, Menu.class);
	}

	@Override
	public List<Map<String,Object>> getIndexMenuPower(String filter) throws Exception {
		String sql="select menuid,status from frm_index_menu_power order by status";
		return this.jdbcTemplate.queryForList(sql);
	}

	
}