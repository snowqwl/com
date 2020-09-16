package com.easymap.dao.vague;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.easymap.dao.IVagueDao;
import com.easymap.pool.DBUtils;
import com.easymap.util.ElementUtils;
import com.easymap.util.TabName;

@Repository("VagueQueryDao")
public class VagueQueryDaoImpl implements IVagueDao {
	
	
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * ģ���ѯ  
	 * @param paramString1
	 * @param paramString2
	 * @param paramString3
	 * @param paramInt1
	 * @param paramInt2
	 * @return
	 */
	public Document findVagueSearch(String querytype,String querykey) {
		
		ResultSet rs = null;

		int totalRows=-1;//�ܼ�¼ 
		int start = 1;
		int end= 15;
		
		Element items = null;
		
		try {
			
			String whereSql="select * from s_gp_locatehistoryinfo s ";

			if(querytype.equals("callno")){//����
				
			}
			
			if(querytype.equals("carno")){//����
				
			}
			
			if(querytype.equals("orgname")){//�豸�� 
				
			}
			
			String countSql = "SELECT COUNT(*) FROM(" +whereSql+ ")";

			rs = DBUtils.findResultData(countSql);

			if (rs.next())
				totalRows = rs.getInt(1);
			List vl=new ArrayList();
			vl.add(end);
			vl.add(start);
			String orgSql = "select * from (SELECT rownum rn, t.* FROM (" +whereSql+ ") t where rownum <= ?) where rn >= ?";
		    items = ElementUtils.getResultElements(DBUtils.findResultData(orgSql,vl));
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		items.setAttribute("display", totalRows <= 0?"false":"true");
		items.setAttribute("totalnum", Integer.toString(totalRows));
		
		return ElementUtils.responseDocument(items);

	}
	
	public Document findVagueSearch(String querytype,String querykey,String layer,int startpos,int pageSize,String points) {
		
		ResultSet rs = null;
		
		int totalRows=-1;//�ܼ�¼ 
		int pagenum=pageSize;//ÿҳ15����¼  
		
		int start = startpos;//��ǰ��¼��ʼ��
		int end= startpos + pagenum - 1;//��ǰ��¼������
		
		Element items = new Element("items");
		String[] split = points.split(";");
		List vl=new ArrayList();
		try {
			
			StringBuilder sb = new StringBuilder();
			sb.append(" select rownum rn,t.*,d.*,ROUND(TO_NUMBER(t.time - sysdate) * 24 * -60) leavemin ")
			.append(" ,to_char(t.time, 'yyyy-mm-dd hh24:mi:ss') intime ")
			.append(" from view_st_gps t,view_st_police_device d ")
			.append(" where 1 = 1 and t.phone = d.mobileno ");
			if ("circle".equalsIgnoreCase(split[0])) {
				// Բ��
				sb.append("  ");
				sb.append(" and lng is not null and regexp_instr(lng,'[^.[:digit:]]')=0");
				sb.append(" and sqrt(power(lng-(?),2) + power(lat-(?),2))<?");
				vl.add(split[1] );
				vl.add(split[2] );
				vl.add(split[3] );
			}
			else if ("rectangle".equalsIgnoreCase(split[0])) {
				// ����
				sb.append(" and lng is not null and regexp_instr(lng,'[^.[:digit:]]')=0");
				sb.append(" and (to_number(lng) between ? and ?) and (to_number(lat) between ? and ?)");
				vl.add(split[1] );
				vl.add(split[3] );
				vl.add(split[2] );
				vl.add(split[4] );
			}
			else if ("polygon".equalsIgnoreCase(split[0])) {
				// �����
				
			}
			
			String countSql = "SELECT COUNT(*) FROM(" + sb + ")";
			
			rs = DBUtils.findResultData(countSql,vl);
			
			if (rs.next())
				totalRows = rs.getInt(1);
			if(totalRows > 0)
			{
				vl.add(end);
				vl.add(start);
				String orgSql = "select * from (" + sb + " and rownum <= ?) where rn >= ?";
				items = ElementUtils.getResultElements(DBUtils.findResultData(orgSql,vl));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		items.setAttribute("display", totalRows <= 0?"false":"true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("orgid", "");
		items.setAttribute("startnum", start+"");
		items.setAttribute("endnum", end+"");
		
		return ElementUtils.responseDocument(items);
		
	}
	
	public Document findVagueSearch(String querytype,String querykey,String selorg,String layer,int startpos,int pageSize) {
		
		ResultSet rs = null;
		int totalRows=-1;//�ܼ�¼ 
		int pagenum=pageSize;//ÿҳ15����¼  
		
		int start = startpos;//��ǰ��¼��ʼ��
		int end= startpos + pagenum - 1;//��ǰ��¼������
		
		Element items = new Element("items");
		StringBuilder groupKeys = new StringBuilder();//用来分组
		List vl=new ArrayList();
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from (");
			if(layer.contains("A")){
				sb.append(" select t.id jczbh,t.jczmc,t.zdsx zdlx,'' ip,'' port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,d.bmmc,t.ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'A' type ")
				.append(" from T_JCZXX t,frm_department d ")
				.append(" where t.sspcs=d.glbm")
				.append(" and ").append("jczmc").append(" like ? ");
				vl.add("%"+querykey+"%");
				sb.append("union all ");
				groupKeys.append("A");
			}
			if(layer.contains("B")){
				sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ")
				.append(" from ").append(TabName.CODE_Gate).append(" t,frm_department d ")
				.append(" where t.dwdm=d.glbm")
				//.append(" and hczxjd is not null and regexp_instr(hczxjd,'[^.[:digit:]]') = 0 ")
				.append(" and ").append("kdmc").append(" like ? ");
				vl.add("%"+querykey+"%");
				sb.append("union all ");
				groupKeys.append("B");
			}
			
			if(layer.contains("C")){
				sb.append(" select t.bh jczbh,t.mc jczmc,t.sptlx zdlx,t.dvrip ip,t.port,t.dz jczdz,t.lxdh zdlxdh,t.jd jd,t.wd wd,t.lxr zdfzrxm,t.SSJYFQDM sspcs ,t.SSJYFQMC bmmc,t.SSJYFQMC ssfj,to_char(t.gxsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'C' type ")
				.append(" from t_monitor t ")
				.append(" where 1=1")
				//.append(" and hczxjd is not null and regexp_instr(hczxjd,'[^.[:digit:]]') = 0 ")
				.append(" and ").append("mc").append(" like ? ");
				vl.add("%"+querykey+"%");
				sb.append("union all ");
				groupKeys.append("C");
			}
			if(layer.contains("D")){
				sb.append(" select t.dzwlbh jczbh,t.dzwlmc jczmc,t.dzwllx zdlx,'' ip,'' port,t.szdz jczdz,t.lxdh zdlxdh,t.jd,t.wd,t.lxr zdfzrxm,t.glbm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'D' type ")
				.append(" from t_dzwlxx").append(" t,frm_department d ")
				.append(" where t.glbm=d.glbm")
				//.append(" and hczxjd is not null and regexp_instr(hczxjd,'[^.[:digit:]]') = 0 ")
				.append(" and ").append("dzwlmc").append(" like ? ");
				vl.add("%"+querykey+"%");
				sb.append("union all ");
				groupKeys.append("D");
			}
			if(layer.contains("E")){
				sb.append("  select t.dth jczbh,t.pdt_id jczmc,'' zdlx,'' ip,'' port,'' jczdz,'' zdlxdh,to_char(t.jd) jd,to_char(t.wd)wd,'' zdfzrxm,'' sspcs ,'' bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'E' type ")
				.append(" from "+TabName.PDTLOCATIONMTAB+" t")
				.append(" where 1=1")
				.append(" and ").append(" dth").append(" like ? ");
				vl.add("%"+querykey+"%");
				sb.append("union all ");
				groupKeys.append("E");
			}
			sb.delete(sb.lastIndexOf("union all"),sb.length());
			sb.append(")t");
			sb.append(" where 1=1 ");
			if(selorg != null && selorg.trim().length() > 0){
				sb.append(" and t.sspcs ")
				.append(" in  ( ")
				.append(" select glbm ")
				.append(" from frm_department  ")
				.append(" connect by sjbm = prior glbm ")
				.append(" and zt = '1' ")
				.append(" start with glbm in (?) ) ");
				vl.add(selorg );
			}
			String countSql = "SELECT COUNT(1) from ("+sb+")";
			//查询总条数
			totalRows = this.jdbcTemplate.queryForInt(countSql,vl);
			if(totalRows > 0){
				//查询列表
				vl.add(end);
				vl.add(start);
				String orgSql = "select * from (" + sb + " and rownum <= ?) where rn >= ?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,vl);
				items = ElementUtils.getResultElements(queryForList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
		} 
		
		items.setAttribute("display", totalRows <= 0?"false":"true");
		items.setAttribute("num", Integer.toString(totalRows));
//		items.setAttribute("orgid", orgid);
		items.setAttribute("startnum", start+"");
		items.setAttribute("endnum", end+"");
		items.setAttribute("groupKeys", groupKeys.toString());
		
		return ElementUtils.responseDocument(items);
		
	}

	public Document findJczVagueSearch(String querytype, String querykey,
			String selorg, String layer, int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows=-1;//总行数
		int pagenum=pageSize;//页数
		int start = startpos;//开始页
		int end= startpos + pagenum - 1;//结束页
		Element items = new Element("items");
		List vl=new ArrayList<>();
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from (");
			sb.append(" select t.id jczbh,t.jczmc,t.zdsx zdlx,'' ip,'' port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,d.bmmc,t.ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'A' type ")
			.append(" from T_JCZXX t,frm_department d ")
			.append(" where t.sspcs=d.glbm")
			.append(" and ").append("jczmc").append(" like ? ");
			vl.add("%"+querykey+"%");
			sb.append(")t");
			sb.append(" where 1=1 ");
			if(selorg != null && selorg.trim().length() > 0){
				sb.append(" and t.sspcs ")
				.append(" in  ( ")
				.append(" select glbm ")
				.append(" from frm_department  ")
				.append(" connect by sjbm = prior glbm ")
				.append(" and zt = '1' ")
				.append(" start with glbm in (?) ) ");
				vl.add(selorg);
			}
			String countSql = "SELECT COUNT(1) from ("+sb+")";
			//查询总条数
			totalRows = this.jdbcTemplate.queryForInt(countSql,vl);
			if(totalRows > 0){
				//查询列表
				
				String orgSql = "select * from (" + sb + " and rownum <= ?) where rn >= ?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,vl);
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{

		} 
		items.setAttribute("display", totalRows <= 0?"false":"true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("startnum", start+"");
		items.setAttribute("endnum", end+"");

		return ElementUtils.responseDocument(items);
	}

	public Document findkkVagueSearch(String querytype, String querykey,
			String selorg, String layer, int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows=-1;//总行数
		int pagenum=pageSize;//页数
		int start = startpos;//开始页
		int end= startpos + pagenum - 1;//结束页
		Element items = new Element("items");
		List vl=new ArrayList<>();
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from (");
			
			sb.append(" select t.kdbh jczbh,t.kdmc jczmc,t.kklx zdlx,'' ip,'' port,t.kkdz jczdz,t.lxdh zdlxdh,t.kkjd jd,t.kkwd wd,'' zdfzrxm,t.dwdm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'B' type ")
			.append(" from ").append(TabName.CODE_Gate).append(" t,frm_department d ")
			.append(" where t.dwdm=d.glbm")
			.append(" and ").append("kdmc").append(" like ? ");
			vl.add("%"+querykey+"%");
			sb.append(")t");
			sb.append(" where 1=1 ");
			if(selorg != null && selorg.trim().length() > 0){
				sb.append(" and t.sspcs ")
				.append(" in  ( ")
				.append(" select glbm ")
				.append(" from frm_department  ")
				.append(" connect by sjbm = prior glbm ")
				.append(" and zt = '1' ")
				.append(" start with glbm in (?) ) ");
				vl.add(selorg);
			}
			String countSql = "SELECT COUNT(1) from ("+sb+")";
			//查询总条数
			totalRows = this.jdbcTemplate.queryForInt(countSql,vl);
			if(totalRows > 0){
				//查询列表
				String orgSql = "select * from (" + sb + " and rownum <= ?) where rn >= ?";
				vl.add(end);
				vl.add(start);
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,vl);
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{

		} 
		items.setAttribute("display", totalRows <= 0?"false":"true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("startnum", start+"");
		items.setAttribute("endnum", end+"");

		return ElementUtils.responseDocument(items);
	}

	public Document findGajkVagueSearch(String querytype, String querykey,
			String selorg, String layer, int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows=-1;//总行数
		int pagenum=pageSize;//页数
		int start = startpos;//开始页
		int end= startpos + pagenum - 1;//结束页
		Element items = new Element("items");
		List vl=new ArrayList<>();
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from (");

			sb.append(" select t.bh jczbh,t.mc jczmc,t.sptlx zdlx,t.dvrip ip,t.port,t.dz jczdz,t.lxdh zdlxdh,t.jd jd,t.wd wd,t.lxr zdfzrxm,t.SSJYFQDM sspcs ,t.SSJYFQMC bmmc,t.SSJYFQMC ssfj,to_char(t.gxsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'C' type ")
			.append(" from t_monitor t ")
			.append(" where 1=1")
			.append(" and ").append("mc").append(" like ? ");
			vl.add("%"+querykey+"%");
			sb.append("union all ");
			sb.delete(sb.lastIndexOf("union all"),sb.length());
			sb.append(")t");
			sb.append(" where 1=1 ");
			if(selorg != null && selorg.trim().length() > 0){
				sb.append(" and t.sspcs ")
				.append(" in  ( ")
				.append(" select glbm ")
				.append(" from frm_department  ")
				.append(" connect by sjbm = prior glbm ")
				.append(" and zt = '1' ")
				.append(" start with glbm in (?) ) ");
				vl.add(selorg);
			}
			String countSql = "SELECT COUNT(1) from ("+sb+")";
			//查询总条数
			totalRows = this.jdbcTemplate.queryForInt(countSql,vl);
			if(totalRows > 0){
				//查询列表
				vl.add(end);
				vl.add(start);
				String orgSql = "select * from (" + sb + " and rownum <= ?) where rn >= ?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,vl);
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{

		} 
		items.setAttribute("display", totalRows <= 0?"false":"true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("startnum", start+"");
		items.setAttribute("endnum", end+"");
		return ElementUtils.responseDocument(items);
	}

	public Document findDzwlVagueSearch(String querytype, String querykey,
			String selorg, String layer, int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows=-1;//总行数
		int pagenum=pageSize;//页数
		int start = startpos;//开始页
		int end= startpos + pagenum - 1;//结束页
		Element items = new Element("items");
		List vl=new ArrayList<>();
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from (");

			sb.append(" select t.dzwlbh jczbh,t.dzwlmc jczmc,t.dzwllx zdlx,'' ip,'' port,t.szdz jczdz,t.lxdh zdlxdh,t.jd,t.wd,t.lxr zdfzrxm,t.glbm sspcs ,d.bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'D' type ")
			.append(" from t_dzwlxx").append(" t,frm_department d ")
			.append(" where t.glbm=d.glbm")
			.append(" and ").append("dzwlmc").append(" like '%").append(querykey).append("%' ");
			vl.add("%"+querykey+"%");
			sb.append(")t");
			sb.append(" where 1=1 ");
			if(selorg != null && selorg.trim().length() > 0){
				sb.append(" and t.sspcs ")
				.append(" in  ( ")
				.append(" select glbm ")
				.append(" from frm_department  ")
				.append(" connect by sjbm = prior glbm ")
				.append(" and zt = '1' ")
				.append(" start with glbm in (?) ) ");
				vl.add(selorg);
			}
			String countSql = "SELECT COUNT(1) from ("+sb+")";
			//查询总条数
			totalRows = this.jdbcTemplate.queryForInt(countSql,vl);
			if(totalRows > 0){
				//查询列表
				String orgSql = "select * from (" + sb + " and rownum <= ?) where rn >= ?";
				vl.add(end);
				vl.add(start);
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,vl);
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{

		} 
		items.setAttribute("display", totalRows <= 0?"false":"true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("startnum", start+"");
		items.setAttribute("endnum", end+"");
		return ElementUtils.responseDocument(items);
	}

	public Document findPdtVagueSearch(String querytype, String querykey,
			String selorg, String layer, int startpos, int pageSize) {
		ResultSet rs = null;
		int totalRows=-1;//总行数
		int pagenum=pageSize;//页数
		int start = startpos;//开始页
		int end= startpos + pagenum - 1;//结束页
		Element items = new Element("items");
		List vl=new ArrayList<>();
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select rownum rn, t.jczbh,t.jczmc,t.zdlx,t.ip,t.port,t.jczdz,t.zdlxdh,t.jd,t.wd,t.zdfzrxm,t.sspcs ,t.bmmc,t.ssfj,t.gxsj,t.type from (");
			sb.append("  select t.dth jczbh,t.pdt_id jczmc,'' zdlx,'' ip,'' port,'' jczdz,'' zdlxdh,to_char(t.jd) jd,to_char(t.wd)wd,'' zdfzrxm,'' sspcs ,s.bz bmmc,'' ssfj,to_char(t.cjsj,'yyyy-mm-dd hh24:mi:ss')gxsj,'E' type ")
			.append(" from "+TabName.PDTLOCATIONMTAB+" t join t_pdt_sbxx s on t.DTH = s.DTH")
			.append(" where 1=1");
			vl.add("%"+querykey+"%");
			sb.append(" and ").append(" t.dth").append(" like ? ");
			sb.append(")t");
			sb.append(" where 1=1");
			if(selorg != null && selorg.trim().length() > 0){
				sb.append(" and t.sspcs ")
				.append(" in  ( ")
				.append(" select glbm ")
				.append(" from frm_department  ")
				.append(" connect by sjbm = prior glbm ")
				.append(" and zt = '1' ")
				.append(" start with glbm in (?) ) ");
				vl.add(selorg);
			}
			String countSql = "SELECT COUNT(1) from ("+sb+")";
			//查询总条数
			totalRows = this.jdbcTemplate.queryForInt(countSql,vl);
			if(totalRows > 0){
				//查询列表
				vl.add(end);
				vl.add(start);
				String orgSql = "select * from (" + sb + " and rownum <= ?) where rn >= ?";
				List<Map<String,Object>> queryForList = this.jdbcTemplate.queryForList(orgSql,vl);
				items = ElementUtils.getResultElements(queryForList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{

		} 
		items.setAttribute("display", totalRows <= 0?"false":"true");
		items.setAttribute("num", Integer.toString(totalRows));
		items.setAttribute("startnum", start+"");
		items.setAttribute("endnum", end+"");
		return ElementUtils.responseDocument(items);
	}

}

