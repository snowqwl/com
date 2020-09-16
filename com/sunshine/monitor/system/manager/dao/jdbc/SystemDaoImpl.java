package com.sunshine.monitor.system.manager.dao.jdbc;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.StatSystem;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.AdministrativeDivision;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.Crossing;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.District;
import com.sunshine.monitor.system.manager.bean.Road;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.bean.TrafficDepartment;
import com.sunshine.monitor.system.manager.dao.SystemDao;

@Repository("systemDao")
public class SystemDaoImpl  extends BaseDaoImpl implements SystemDao {
	
	private static List<Map<String,Object>> directList = new Vector<Map<String,Object>>();
	private static List<Map<String,Object>> gateTree = new Vector<Map<String,Object>>();
	private static List<Map<String,Object>> allgate = new Vector<Map<String,Object>>();
	private static List<Map<String,Object>> gateTreeFilterDirect = new Vector<Map<String,Object>>();
	private Logger log = LoggerFactory.getLogger(SystemDaoImpl.class);
	
	private static Map<String, SoftReference<List<Code>>> codeCache = new ConcurrentHashMap<String, SoftReference<List<Code>>>();

	private static Map<String, SoftReference<SysUser>> userCache = new ConcurrentHashMap<String, SoftReference<SysUser>>();

	private static Map<String, SoftReference<Department>> deptCache = new ConcurrentHashMap<String, SoftReference<Department>>();
	
	public List getCode(String dmlb) throws Exception {
		List<Code> list = null;
		SoftReference<List<Code>> sof = codeCache.get(dmlb);
		if(sof!=null){
			list= sof.get();
		}
		if(list == null || list.size()==0){
			String sql = "select dmlb,dmz,dmsm1,dmsm2,dmsm3,dmsm4,dmsx,zt from frm_code where dmlb=? and zt=?";
			if("120004".equals(dmlb)){
				sql+=" and dmz<>'5'";
			}
			String zt = "1";
			list = this.queryForList(sql, new Object[]{dmlb, zt}, Code.class);
			codeCache.put(dmlb, new SoftReference<List<Code>>(list));
		}
		return list;
	}

	public String getDepartmentName(String glbm) throws Exception {
		String bmmc = this.getDepartmentName1(glbm);
		return bmmc;
	}
	
    @Deprecated
	public int loadUser() throws Exception {
		loadAllGateTree();
		return 0;
	}
	
	/**
	 * 将系统新增用户加载到内存中
	 */
	public void addUser(SysUser su) throws Exception {
		if(su != null && !"".equals(su.getYhdh())){
			SoftReference<SysUser> wkr = userCache.get(su.getYhdh());
			SysUser sysUser = null;
			if(wkr!=null){
				sysUser = wkr.get();
				if(sysUser != null){
					userCache.remove(sysUser.getYhdh());
				}
			}
			userCache.put(su.getYhdh(), new SoftReference<SysUser>(su));
		}
		log.info("系统用户加载新增:YHDH=" + su.getYhdh()+ "记录");
	}

	public int loadDepartment() throws Exception {

		return 0;
	}
	
	public void loadGateTree() throws Exception {
		gateTree.clear();
		/*String sql = "select distinct id, idname, nvl(pid, 0) pid from ( "
			+" select c.fxmc as idname, c.fxbh as id, a.kdbh as pid  from dc_code_gate a, dc_code_device b, dc_code_direct c where a.kdbh = b.kdbh and (b.fxbh1 = c.fxbh or b.fxbh2 = c.fxbh) "
			+" union all "
			+" select d.kdmc as idname, d.kdbh as id, nvl(e.xzqhdm, substr(d.kdbh, 1, 4)) as pid  from dc_code_gate d, frm_xzqh e where substr(d.kdbh, 1, 6) = e.xzqhdm "
			+" union all "
			+" select e.xzqhmc as idname, e.xzqhdm as id, f.dwdm as pid  from frm_xzqh e right join code_url f on substr(e.xzqhdm, 1, 4) = substr(f.dwdm, 1, 4)"
			+" union all "
			+" select g.jdmc as idname, g.dwdm as id, h.dwdm as pid from code_url g, code_url h where substr(g.dwdm, 1, 2) = substr(h.dwdm, 1, 2) and h.dwdm like '4300%' and g.dwdm not like '4300%' "
			+" union all "
			+" select jdmc as idname, dwdm as id, '' as pid from code_url where dwdm like '4300%' "
			+" ) t start with t.id like '4300%' connect by prior t.id = t.pid order by id "
			;*/
		String sql =" select distinct id, idname, nvl(pid, 0) pid from ( "  
				   +" select c.fxmc as idname, c.fxbh as id, a.kdbh as pid  from code_gate a, code_gate_extend c where a.kdbh = c.kdbh "   
				   +" union all "  
				   +" select d.kdmc as idname, d.kdbh as id, substr(kdbh,1,6) as pid  from code_gate d where substr(kdbh,0,4) not in('4315','4300') "
				   +" union all "
				   +" select e.xzqhmc as idname, e.xzqhdm as id,  pid  from frm_xzqh e "
				   +" union all "       
				   +" select kdmc as idname,kdbh as id ,'430000870000' as pid from code_gate where substr(kdbh,0,4) in('4315','4300') " 
				   +" ) order by nlssort(idname, 'NLS_SORT=SCHINESE_PINYIN_M') ";
		//System.out.println(sql);
		gateTree = this.jdbcTemplate.queryForList(sql);
		
	}
	
	public void loadAllGateTree() throws Exception {/*
		allgate.clear();
		String sql = "select kdbh,kdmc,kkjd,kkwd from dc.code_gate_temp2 t ";
		allgate = this.jdbcTemplate.queryForList(sql);
		if(allgate != null && allgate.size() > 0)
		{
			for (Map gate : allgate) {
				gate.put("X", convertAndCheckToDecimalByString((String)gate.get("KKJD")));
				gate.put("Y", convertAndCheckToDecimalByString((String)gate.get("KKWD")));
			}
		}
	*/}
	
	public void loadGateTreeFilterDirect() throws Exception {
		gateTreeFilterDirect.clear();
		/*String sql = "select distinct id, idname, nvl(pid, 0) pid from ( "
			+" select d.kdmc as idname, d.kdbh as id, nvl(e.xzqhdm, substr(d.kdbh, 1, 4)) as pid  from dc_code_gate d, frm_xzqh e where substr(d.kdbh, 1, 6) = e.xzqhdm "
			+" union all "
			+" select e.xzqhmc as idname, e.xzqhdm as id, f.dwdm as pid  from frm_xzqh e right join code_url f on substr(e.xzqhdm, 1, 4) = substr(f.dwdm, 1, 4)"
			+" union all "
			+" select g.jdmc as idname, g.dwdm as id, h.dwdm as pid from code_url g, code_url h where substr(g.dwdm, 1, 2) = substr(h.dwdm, 1, 2) and h.dwdm like '4300%' and g.dwdm not like '4300%' "
			+" union all "
			+" select jdmc as idname, dwdm as id, '' as pid from code_url where dwdm like '4300%' "
			+" ) t start with t.id like '4300%' connect by prior t.id = t.pid order by id "
			;*/
		String sql =  "select distinct id, idname, nvl(pid, 0) pid from ( "
					+" select d.kdmc as idname, d.kdbh as id, substr(kdbh,1,6) as pid  from code_gate d where substr(kdbh,0,4) not in('4315','4300')" 
					+" union all "
					+" select e.xzqhmc as idname, e.xzqhdm as id,  pid  from frm_xzqh e "
					+" union all "       
					+" select kdmc as idname,kdbh as id ,'430000870000' as pid from code_gate where substr(kdbh,0,4) in('4315','4300') "
					+" ) order by nlssort(idname, 'NLS_SORT=SCHINESE_PINYIN_M') ";
		//System.out.println(sql);
		gateTreeFilterDirect = this.jdbcTemplate.queryForList(sql);
		//loadAllGateTree();
		
	}
	/**
	 * 将系统新增部门加载到内存中
	 */
	public void addDepartment(Department d) throws Exception {
		if(d != null && !"".equals(d.getGlbm())){
			SoftReference<Department> wkr = deptCache.get(d.getGlbm());
			Department dept = null;
			if(wkr!=null){
				dept = wkr.get();
				if(dept != null){
					deptCache.remove(dept.getGlbm());
				}
			}
			deptCache.put(d.getGlbm(), new SoftReference<Department>(d));
		}
		log.info("系统部门加载新增:GLBM=" +d.getGlbm()+ "记录");
	}

	@Deprecated
	public int loadCode() throws Exception {

		return 0;
	}
	
	/**
	 * 将系统新增字典加载到内存中
	 */
	public void addCode(Code code) throws Exception {
		SoftReference<List<Code>> sof = codeCache.get(code.getDmlb());
		if(sof != null){
			List<Code> list = sof.get();
			if(list != null && list.size()>0){
				list.remove(code);
				list.add(code);
			} else {
				List<Code> temp = new ArrayList<Code>();
				temp.add(code);
				codeCache.put(code.getDmlb(), new SoftReference<List<Code>>(temp));
			}
		} else {
			List<Code> temp = new ArrayList<Code>();
			temp.add(code);
			codeCache.put(code.getDmlb(), new SoftReference<List<Code>>(temp));
		}
		log.info("系统代码加载新增:DMLB=" + code.getDmlb());
	}
	
	public SysUser getUser(String yhdh) throws Exception {
		SysUser sysUser = null;
		SoftReference<SysUser> sof = userCache.get(yhdh);
		if(sof!=null){
			sysUser= sof.get();
		}
		if(sysUser == null){
			String sql = "select * from frm_SysUser where yhdh=?";
			List<SysUser> list = this.queryForList(sql, new Object[]{yhdh}, SysUser.class);
			if(list.size()>0)
				sysUser = list.get(0);
			userCache.put(yhdh, new SoftReference<SysUser>(sysUser));
		}
		return sysUser;		
	}

	public List<SysUser> getUsers(String glbm) throws Exception {
		String sql = "select * from frm_SysUser where glbm=?";
		List<SysUser> list = this.queryForList(sql, new Object[]{glbm}, SysUser.class);
		Vector<SysUser> vector = new Vector<SysUser>();
		for (Iterator<SysUser> it = list.iterator(); it.hasNext();) {
			SysUser SysUser = it.next();
			vector.add(SysUser);
		}
		return vector;
	}

	public String getUserName(String yhdh) throws Exception {
		SysUser user = getUser(yhdh);
		if (user == null) {
			return yhdh;
		}
		return user.getYhmc();
	}

	public List<Department> getDepartments() throws Exception {
		String sql = "select * from frm_department where zt=?";
		List<Department> list = this.queryForList(sql, new Object[]{"1"}, Department.class);
		return list;
	}

	public Department getDepartment(String glbm) throws Exception {
		Department dept = null;
		SoftReference<Department> wrf = deptCache.get(glbm);
		if(wrf!=null){
			dept = wrf.get();
		}
		if(dept == null){
			String sql = "select * from frm_department where glbm=? and zt=?";
			List<Department> list = this.queryForList(sql, new Object[]{glbm,"1"}, Department.class);
			if(list.size()>0){
				dept = list.get(0);
				deptCache.put(glbm, new SoftReference<Department>(dept));
			}
		}
		return dept;
	}

	public String getDepartmentName1(String glbm) throws Exception {
		Department department = getDepartment(glbm);
		if (department == null) {
			return glbm;
		}
		return department.getBmmc();
	}

	public List<Code> getCodes(String dmlb) throws Exception {
		List<Code> list = getCode(dmlb);
		return list;
	}

	public Code getCode(String dmlb, String dmz) throws Exception {
		List<Code> list = getCode(dmlb);
		Code conCode = new Code(dmlb,dmz);
		Code code = null;
		if (list == null) {
			code = new Code(dmlb, dmz, dmz);
		} else {
			for (int i = 0; i < list.size(); i++) {
				Code tmp = list.get(i);
				if(tmp != null && tmp.equals(conCode)) {
					code = tmp;
					break;
				}
			}
			if (code == null) {
				code = new Code(dmlb, dmz, "");
			}
		}
		return code;
	}

	public String getCodeValuenew(String dmlb, String dmz) throws Exception {
		List<Code> list = null;
		String sql = "select dmlb,dmz,dmsm1,dmsm2,dmsm3,dmsm4,dmsx,zt from frm_code where dmlb=? and zt=? and dmz=?";
		String zt = "1";
		list = this.queryForList(sql, new Object[]{dmlb, zt,dmz}, Code.class);
		if(list.size()>0){
			return list.get(0).getDmsm1();
		}
		return "";
	}
	
	public String getCodeValue(String dmlb, String dmz) throws Exception {
		String r = "";
		List<Code> list =  getCode(dmlb);
		Code temp = new Code(dmlb, dmz);
		Code code = null;
		if (list == null) {
			r = dmz;
		} else {
			for (int i = 0; i < list.size(); i++) {
				code =  list.get(i);
				if (code.equals(temp)) {
					r = code.getDmsm1();
					break;
				}
			}
			if (code == null){
				r = dmz;
			} else if (r.length() < 1) {
				r="";
			}
		}
		return r;
	}
	
	public Map getGateByKDBH(String kdbh)
	{
		if(kdbh == null || kdbh.length() < 1)
			return null;
		for (Map gatem : allgate) {
			if(gatem.get("KDBH").equals(kdbh))
				return gatem;
		}
		return null;
	}
	
	public List getAllGate()
	{
		return allgate;
	}
	
	public String getCodeValue(String dmlb, String dmz, int dmsm)
			throws Exception {
		String r = "";
		List<Code> list = getCode(dmlb);
		Code temp = new Code(dmlb,dmz);
		Code code = null;
		if (list == null) {
			r = dmz;
		} else {
			for (int i = 0; i < list.size(); i++) {
				code = list.get(i);
				if (code.equals(temp)) {
					if (dmsm == 1) {
						r = code.getDmsm1();
						break;
					}
					if (dmsm == 2) {
						r = code.getDmsm2();
						break;
					}
					if (dmsm == 3) {
						r = code.getDmsm3();
						break;
					}
					if (dmsm == 4) {
						r = code.getDmsm4();
						break;
					}
					r = code.getDmsm1();
					break;
				}
			}
			if (code == null)
				r = dmz;
			else if (r.length() < 1) {
				r = dmz;
			}
		}
		return r == null ? "" : r;
	}

	public List<Code> getCodesByDmsm(String dmlb, String dmsm, int position)
			throws Exception {
		List<Code> list = getCode(dmlb);
		Vector<Code> vector = new Vector<Code>();
		Code code = null;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				code = (Code) list.get(i);
				if (position == 1) {
					if (code.getDmsm1().equals(dmsm))
						vector.add(code);
				} else if (position == 2) {
					if (code.getDmsm2().equals(dmsm))
						vector.add(code);
				} else if (position == 3) {
					if (code.getDmsm3().equals(dmsm))
						vector.add(code);
				} else if (position == 4) {
					if (code.getDmsm4().equals(dmsm)){
						vector.add(code);
					}
				} else if (code.getDmz().equals(dmsm)) {
					vector.add(code);
				}
			}

		}

		return vector;
	}

	public String getDepartmentKey(String glbm) throws Exception {
		if (glbm.endsWith("0000000000"))
			return glbm.substring(0, 2);
		if (glbm.endsWith("00000000"))
			return glbm.substring(0, 4);
		if (glbm.endsWith("000000"))
			return glbm.substring(0, 6);
		if (glbm.endsWith("0000"))
			return glbm.substring(0, 8);
		if (glbm.endsWith("00")) {
			return glbm.substring(0, 10);
		}
		return glbm;
	}

	public String getSysDate(String day, boolean isTime) throws Exception {
		String sql = "";
		String timeformat = "yyyy-mm-dd";
		if (isTime) {
			timeformat = timeformat + " hh24:mi:ss";
		}
		if ((day == null) || (day.length() < 1))
			sql = "select to_char(sysdate,'" + timeformat + "') dd from dual";
		else {
			sql = "select to_char(sysdate" + day + ",'" + timeformat
					+ "') dd from dual";
		}
		return (String) this.jdbcTemplate.queryForObject(sql, String.class);
	}

	public String getCsys(String csys) throws Exception {
		try {
			if ((csys == null) || (csys.length() < 1))
				return csys;
			String t = csys;
			if (csys.indexOf(",") != -1)
				t = csys.replaceAll(",", "");
			String ccsys = "";
			for (int i = 0; i < t.length(); i++) {
				ccsys = ccsys
						+ getCodeValue("030108", String.valueOf(t.charAt(i)))
						+ ",";
			}
			if (ccsys.endsWith(","))
				ccsys = ccsys.substring(0, ccsys.length() - 1);
			return ccsys;
		} catch (Exception e) {
		}
		return csys;
	}	

	public int loadPrefecture() throws Exception {
		return 0;
	}
	
	public String getParameter(String gjz, HttpServletRequest request) {
		try {
			String r = null;
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			List<Syspara> list = userSession.getSyspara();
			if (list != null) {
				for (Iterator<Syspara> it = list.iterator(); it.hasNext();) {
					Syspara syspara = (Syspara) it.next();
					if ((syspara.getGjz().equalsIgnoreCase(gjz))
							&& (syspara.getSfgy().equals("1"))) {
						r = syspara.getCsz();
					}
				}
			}
			return r;
		} catch (Exception e) {
		}
		return null;
	}
	
	public String getParameter(String gjz, String glbm,
			HttpServletRequest request) {
		try {
			String r = null;
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			List<Syspara> list = userSession.getSyspara();
			if (list != null) {
				for (Iterator<Syspara> it = list.iterator(); it.hasNext();) {
					Syspara syspara = (Syspara) it.next();
					if ((syspara.getGjz().equalsIgnoreCase(gjz))
							&& (syspara.getGlbm().equalsIgnoreCase(glbm))) {
						r = syspara.getCsz();
					}
				}
			}
			return r;
		} catch (Exception e) {
		}
		return null;
	}
	
	public List<AdministrativeDivision> queryAdministrativeDivisions(
			String subConditions) throws Exception {
		String sql = "select xzqh, xzdm, qhdm, qhmc, zt, sfxs, bz1, bz2 from JM_XZQH_GLBM";
		if(subConditions != null && !"".equals(subConditions)){
			sql += subConditions;
		}
		return queryList(sql, AdministrativeDivision.class);
	}
	
	public List<AdministrativeDivision> queryCityAdministrativeAdvisions(
			String cityname) throws Exception {
		String sql = "select xzqh, xzdm, qhdm, qhmc, zt, sfxs, bz1, bz2 from JM_XZQH_GLBM";
		if(cityname != null) {
			sql += "@"+cityname;
		}
		sql += " where sfxs='1' and zt='1' order by wz asc ";
		System.out.println(sql);
		return queryList(sql, AdministrativeDivision.class);
	}
	
	public List getDistricts(String glbm) throws Exception {
        StringBuffer sb=new StringBuffer();
        sb.append("SELECT * FROM FRM_XZQH WHERE LENGTH(XZQHDM)=6 ");
        if(StringUtils.isNotBlank(glbm)){
        	sb.append(" AND XZQHDM LIKE '%"+glbm+"%'");
        }
        sb.append("ORDER BY XZQHDM");  
		return queryList(sb.toString(),District.class);
	}
	
	public List getDistrictsByCity(String city) throws Exception {
        String sql = "";
        if(city != null && !"".equals(city)) {
        	sql = " where xzqhdm like '" + city.substring(0, 4) + "%' ";
        }
        sql = " SELECT * FROM FRM_XZQH " + sql + " ORDER BY XZQHDM";
		return queryList(sql,District.class);
	}
	
	public String getDistrictNameByXzqh(String xzqhdm) throws Exception {
		String sql = "select xzqhmc from frm_xzqh where xzqhdm = '"+xzqhdm +"'";
		List list=this.jdbcTemplate.queryForList(sql);
				if(list.size()!=0){
					Map map=(Map)list.get(0);
					return map.get("xzqhmc").toString();
				}
		return xzqhdm;
	}
	

	public List getRoadsByFilter(String xzqh) throws Exception {
        StringBuffer sb=new StringBuffer();
        sb.append("SELECT * FROM FRM_DL WHERE XZQH LIKE '%")
          .append(xzqh).append("%' ORDER BY DLDM");
		return queryList(sb.toString(),Road.class);
	}
	
	public List getCrossingByFilter(String xzqh, String dldm) throws Exception {
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT * FROM FRM_LKLD WHERE XZQH = '")
		  .append(xzqh)
		  .append("' AND DLDM = '")
		  .append(dldm)
		  .append("' ORDER BY LDDM");
		return queryList(sb.toString(),Crossing.class);
	}
	
	public List getTrafficDepartment(String xzqh) throws Exception {
        StringBuffer sb=new StringBuffer();
        sb.append("SELECT * FROM FRM_GLBM WHERE GLBM LIKE '").append(xzqh).append("%'");
		return queryList(sb.toString(),TrafficDepartment.class);
	}
	public String getTrafficDepartmentName(String dwdm) throws Exception {
		StringBuffer sb=new StringBuffer();
		String result="";
		sb.append("SELECT BMQC FROM FRM_GLBM WHERE GLBM='").append(dwdm).append("'");
		result=this.jdbcTemplate.queryForObject(sb.toString(),String.class);
		return result;
	}

	public void updateStatSystem(StatSystem ss) {
		//先查询是否存在地市记录
		String select_sql = "select count(1) from stat_system where dwdm = '"+ss.getDwdm()+"'";
		int count = this.jdbcTemplate.queryForInt(select_sql);
		if(count == 0) {
			//插入
			String insert_sql = "insert into stat_system(dwdm,dwmc,jq,xt,jr,gxsj) values('"
				+ss.getDwdm()+"','"
				+ss.getDwmc()+"','"
				+ss.getJq()+"','"
				+ss.getXt()+"','"
				+ss.getJr()+"',"
				+"sysdate)";
			this.jdbcTemplate.update(insert_sql);
		} else {
			String update_sql = "update stat_system set jq = '"
				+ss.getJq()+"' ,xt = '"
				+ss.getXt() +"' ,jr = '"
				+ss.getJr() +"',gxsj = sysdate"+
				" where dwdm = '"+ss.getDwdm()+"'";
			this.jdbcTemplate.update(update_sql);
		}
		
	}
	public List<String> getCityNames(String[] dwdm) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" select jdmc from code_url where ");
		for(int i = 0; i<dwdm.length;i++){
		    if(i==0){
		    	sb.append(" dwdm = '"+dwdm[i]+"'");
		    }else{
		    	sb.append(" or dwdm = '"+dwdm[i]+"'");
		    }
		}
		List<String> list= this.jdbcTemplate.queryForList(sb.toString(),String.class);
		return list;
	}

	public int  loadDirect() throws Exception {
		StringBuffer sb=new StringBuffer();
		sb.append(" select fxbh,fxmc from code_gate_extend ");
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sb.toString());
		directList=list;
		return list.size();
	}

	public List<Map<String,Object>> getDirects() throws Exception {
		return directList;
	}
	
	public List getGateTree(String jb) throws Exception {
		if("gate".equals(jb)) {
			return gateTreeFilterDirect;
		}
		return gateTree;
	}
	
	public double convertAndCheckToDecimalByString(String latlng) {
		if(latlng == null || latlng.length() < 1)
			return 0;
		if(latlng.indexOf("°") > 0)
		{			
			double du = 0;
			double fen = 0;
			double miao = 0;
			if(latlng.indexOf("°")!=-1){
				du = Double.parseDouble(latlng.substring(0, latlng.indexOf("°")));
				if(latlng.indexOf("′")!=-1){
					fen = Double.parseDouble(latlng.substring(latlng.indexOf("°") + 1, latlng.indexOf("′")));
					if(latlng.indexOf("″")!=-1){
						miao = Double.parseDouble(latlng.substring(latlng.indexOf("′") + 1, latlng.indexOf("″")));
					}
				}
			}
			
			if (du < 0)
				return -(Math.abs(du) + (fen + (miao / 60)) / 60);
			return du + (fen + (miao / 60)) / 60;
		}
		return Double.parseDouble(latlng);

    }

	public int updateVisitTotal() throws Exception {
      return  this.jdbcTemplate.update("update  frm_syspara set csz = (select csz+1 from frm_syspara where gjz = 'users') where gjz = 'users'");		
	}

	public List<Map<String, Object>> getXzqhTree(String dwdm) throws Exception {
		StringBuffer sql = new StringBuffer("select xzqhdm as id,xzqhmc as name," +
				"pid as pid,jb as JB from frm_xzqh where 1=1  ");
		if(StringUtils.isNotBlank(dwdm)){
			sql.append(" and pid = '").append(dwdm).append("'");
			sql.append(" or xzqhdm = '").append(dwdm).append("' ");
		}
		sql.append(" order by xzqhxh,xzqhdm ");
		return this.jdbcTemplate.queryForList(sql.toString()); 
	}
	
	public List getCityList() throws Exception{
		String sql = " select substr(dwdm,0,6) as dwdm,jdmc from code_url where dwdm<>'430000000000' ";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	public String getBaseConnectionState(String ds)throws Exception{
		int n = 0;
		String flagstr = "";
		String sql = " select count(1) from veh_realpass@"+ds;
		try{
		n = this.jdbcTemplate.queryForInt(sql);
		
		}catch(Exception e){
			flagstr = ds;
		}
		return flagstr ; 
	}
	
	public List getCodeByDmlb(String dmlb,String subsql)throws Exception{
		List<Code> list = null;		
		if(list == null || list.size()==0){
			String sql = "select dmlb,dmz,dmsm1,dmsm2,dmsm3,dmsm4,dmsx,zt from frm_code where dmlb=? and zt=? "+subsql;
			String zt = "1";
			list = this.queryForList(sql, new Object[]{dmlb, zt}, Code.class);
		}
		return list;
	}

}
