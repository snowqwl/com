package com.easymap.dao.org;

import static com.easymap.util.Tools.getSafeObj;
import static com.easymap.util.Tools.isNotEmpty;

import com.easymap.dao.IOrgQueryDao;
import com.easymap.pool.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository("OrgQueryDao")
public class OrgQueryDaoImpl implements IOrgQueryDao {

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	public Document findOrgQuery(String userid) {
		 
		ResultSet rs = null;
		Document document = new Document();
		Element response = new Element("response");
		Map<String,Element> map = new HashMap<String,Element>();
		try {
			
			String sql="select * from (select glbm,bmmc,sjbm from frm_department t  connect by sjbm = prior glbm  start with glbm in(select t2.glbm from frm_sysuser t2 where t2.yhdh = ?))";
			System.out.println("SQL:"+sql);
			List<Map<String,Object>> queryForList = jdbcTemplate.queryForList(sql,new String []{userid});
			if(isNotEmpty(queryForList)){
				for (Map<String, Object> map2 : queryForList) {
					Element item = new Element("item");
					String zzjgName = getSafeObj(map2.get("bmmc"));
					String zzjgID = getSafeObj(map2.get("glbm"));
					item.setAttribute("ORGNAME", zzjgName);
					item.setAttribute("ORGID", zzjgID);
					map.put(zzjgID, item);
					Element items = map.get(getSafeObj(map2.get("sjbm")));
					if (items == null)
						response.addContent(item);
					else
						items.addContent(item);
				}
				document.addContent(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				DBUtils.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return document;
	}
}