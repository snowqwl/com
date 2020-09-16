package com.sunshine.monitor.system.manager.dao.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.Policess;
import com.sunshine.monitor.comm.bean.Role;
import com.sunshine.monitor.comm.bean.UserRole;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.CallableStatementCallbackImpl;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.dao.SysUserDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;

@Repository("sysUserDao")
public class SysUserDaoImpl extends BaseDaoImpl implements SysUserDao {
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	public List getRoleList(Role role) throws Exception {

		String tmpSql = "";
		if ((role != null) && (role.getJsmc() != null)
				&& (!role.getJsmc().equals(""))) {
			tmpSql = " where jsmc like '" + role.getJsmc() + "%'";
		}

		tmpSql = "select * from jm_role " + tmpSql + " order by jsdh";

		List list = this.queryList(tmpSql, Role.class);
		return list;
	}

	public Role getRole(String jsdh) throws Exception {
		String sql = "SELECT JSDH,JSMC,JSSX,JSJB,BZ FROM JM_ROLE WHERE JSDH = ?";
		List<Role> list = this.queryForList(sql, new Object[]{jsdh}, Role.class);
		Role role = null ;
		if (list != null && list.size() > 0){
			role = list.get(0);
		}
		return role;
	}
	
	/**
	 * @author huzhaoj
	 * @date 2017-04-27
	 * 因为公安网环境查询（单条件“已授权”）慢，注释掉这个方法，改用下面的findSysUserForMap方法。
	 * 
	 * */
	/*public Map findSysUserForMap(Map filter, SysUser user) throws Exception {
		String tmpSql = "";
		if (StringUtils.isNotBlank(user.getGlbm())) {
			tmpSql = tmpSql + " and a.glbm='" + user.getGlbm() + "'";
		}
		if (StringUtils.isNotBlank(user.getYhdh())) {
			tmpSql = tmpSql + " and a.yhdh like '%" + user.getYhdh() + "%'";
		}
		if (StringUtils.isNotBlank(user.getYhmc())) {
			tmpSql = tmpSql + " and a.yhmc like '%" + user.getYhmc() + "%'";
		}
		if (StringUtils.isNotBlank(user.getJh())) {
			tmpSql = tmpSql + " and a.jh='" + user.getJh() + "'";
		}
		if (StringUtils.isNotBlank(user.getSfzmhm())) {
			tmpSql = tmpSql + " and a.sfzmhm='" + user.getSfzmhm() + "'";
		}

		if (StringUtils.isNotBlank(user.getRoles())) {
			tmpSql = tmpSql
					+ " and (select count(1) from jm_userrole c where a.yhdh=c.yhdh and c.jsdh in ("
					+ user.getRoles() + ")) > 0 ";
		}
		if ("2".equals(user.getAuthority())) {//有角色
			tmpSql = tmpSql + " and (select count(1) from jm_userrole where yhdh= a.yhdh ) > 0 ";
		}else if("3".equals(user.getAuthority())){//无角色（未授权）
			tmpSql = tmpSql + " and (select count(1) from jm_userrole where yhdh= a.yhdh ) = 0 ";
		}
		String tmpSql0 = "select a.YHDH,a.YHMC,a.MM,a.SFZMHM,a.JH,a.GLBM,a.IPKS,a.IPJS,to_char(a.ZHYXQ,'yyyy-mm-dd') zhyxq,to_char(a.mmyxq,'yyyy-mm-dd') mmyxq,a.ZT,a.QXMS,a.BZ,b.bmmc "
				+ "from frm_sysuser a,frm_department b "
				//+ "where a.glbm=b.glbm and b.glbm in (select xjjg from frm_prefecture where dwdm = '"+user.getGlbm()+"' or dwdm = '"+filter.get("dwdm")+"')"
				+ "where a.glbm=b.glbm and b.glbm in (select xjjg from frm_prefecture where 1 = 1 ";
		String tmpSql1 = tmpSql0;
				if (StringUtils.isNotBlank(user.getGlbm())) {
					tmpSql1 = tmpSql1 + " and dwdm = '"+user.getGlbm()+"'";
				}
		String tmpSql2 = "";	
//	       tmpSql2 = tmpSql1 + tmpSql + " ) order by b.glbm,b.bmmc,a.yhmc,a.ROWID";
		   tmpSql2 = tmpSql1 + tmpSql + " ) order by b.sjbm,nlssort(b.bmmc, 'NLS_SORT=SCHINESE_PINYIN_M'),NLSSORT (A.yhmc,'NLS_SORT=SCHINESE_PINYIN_M'),a.ROWID";
	
		//tmpSql += "select * from (" + tmpSql+") ";
		return this.findPageForMap("select * from (" + tmpSql2 +") ", Integer.parseInt(filter.get(
				"curPage").toString()), Integer.parseInt(filter.get("pageSize")
				.toString()));
	}*/
	
	public Map findSysUserForMap(Map filter, SysUser user) throws Exception {
		
		String tmpSql = "";
		
		if (StringUtils.isNotBlank(user.getYhdh())) {
			tmpSql += " AND A.YHDH LIKE '%" + user.getYhdh() + "%'";
		}
		if (StringUtils.isNotBlank(user.getYhmc())) {
			tmpSql += " AND A.YHMC LIKE '%" + user.getYhmc() + "%'";
		}
		if (StringUtils.isNotBlank(user.getJh())) {
			tmpSql += " AND A.JH='" + user.getJh() + "'";
		}
		if (StringUtils.isNotBlank(user.getSfzmhm())) {
			tmpSql += " AND A.SFZMHM='" + user.getSfzmhm() + "'";
		}
		
		String sql = "SELECT A .YHDH, A .YHMC, A .MM, A .SFZMHM, A .JH, A .GLBM, A .IPKS, A .IPJS, TO_CHAR (A .ZHYXQ, 'yyyy-mm-dd') ZHYXQ, TO_CHAR (A .MMYXQ, 'yyyy-mm-dd') MMYXQ, A .ZT, A .QXMS, A .BZ, B.BMMC" +
				" FROM frm_sysuser A, frm_department B WHERE A.GLBM = B.GLBM";
		
		sql += tmpSql;

		String tmpSql0 = "";
		
		if ("2".equals(user.getAuthority()) || StringUtils.isNotBlank(user.getRoles())) {//已授权
			tmpSql0 = " AND EXISTS(SELECT DISTINCT YHDH FROM jm_userrole WHERE YHDH = A.YHDH";
			if (StringUtils.isNotBlank(user.getRoles())) {
				tmpSql0 += " AND JSDH IN (" + user.getRoles() + "))";
			}else{
				tmpSql0 += " )";
			}
		}else if("3".equals(user.getAuthority())){//未授权
			tmpSql0 = " AND NOT EXISTS(SELECT DISTINCT YHDH FROM jm_userrole WHERE YHDH = A.YHDH)";
		}
		
		sql += tmpSql0;

		//bug24:无下级部门的单位无返回数据，应该返回本身
		//String tmpSql1 = " AND B.GLBM IN (SELECT DISTINCT XJJG FROM frm_prefecture WHERE 1=1";
		String tmpSql1 ="";
		if (StringUtils.isNotBlank(user.getGlbm())) {
			 tmpSql1 = " AND A.GLBM IN (select glbm from frm_department t start with glbm = '"+user.getGlbm()+"' connect by prior glbm=sjbm) ";
		}
		/*
		if("".equals(user.getGlbm()) || user.getGlbm()==null){
			tmpSql1 = " AND (A.GLBM = '"+filter.get("dwdm")+"' OR  A.GLBM IN (SELECT DISTINCT XJJG FROM frm_prefecture WHERE DWDM = '"+filter.get("dwdm")+"') ";
		}
		*/
		
		sql += tmpSql1;
		
		String tmpSql2 = "  ORDER BY B.SJBM,NLSSORT( B.BMMC,'NLS_SORT=SCHINESE_PINYIN_M'), NLSSORT( A.YHMC,'NLS_SORT=SCHINESE_PINYIN_M'),A.ROWID";
	
		sql += tmpSql2;
		
		return this.findPageForMap("SELECT * FROM (" + sql +") ", Integer.parseInt(filter.get(
				"curPage").toString()), Integer.parseInt(filter.get("pageSize")
				.toString()));
	}

	public SysUser getSysUserByYHDH(String yhdh) throws Exception {
		List<SysUser> sysuserList = this.queryForList(
				"select u.yhdh, u.yhmc, u.mm, u.sfzmhm, u.jh, u.glbm, u.ipks, u.ipjs, u.zhyxq, u.mmyxq, u.zt, u.qxms, u.sq, u.lxdh1, u.lxdh2, u.lxdh3, u.lxcz, u.dzyx, u.lxdz, u.bgdz, u.syyhdh, u.dzzs, u.dzzsxx, u.gxsj, u.bz,d.bmmc as bmmc from frm_sysuser u,frm_department d where d.glbm=u.glbm and yhdh = ?",
				new Object[]{yhdh}, SysUser.class);
		if ((sysuserList == null) || (sysuserList.size() == 0)) {
			return null;
		}
		SysUser user = sysuserList.get(0);
		return user;
	}

	public int insertSysUser(SysUser user) throws Exception {
		// 更新用户表
		StringBuffer sbInsert = new StringBuffer("");
		sbInsert
				.append(
						"INSERT Into frm_sysuser (yhdh,yhmc,mm,sfzmhm,jh,glbm,ipks,ipjs,zhyxq,mmyxq,zt,qxms,bz,sq,")
				.append(
						"lxdh1,lxdh2,lxdh3,lxcz,dzyx,lxdz,bgdz,dzzs,dzzsxx,gxsj) Values ('")
				.append(user.getYhdh()).append("','").append(user.getYhmc())
				.append("','").append(user.getMm()).append("','").append(
						user.getSfzmhm()).append("','").append(user.getJh())
				.append("','").append(user.getGlbm()).append("','").append(
						user.getIpks()).append("','").append(user.getIpjs())
				.append("',to_date('").append(user.getZhyxq()).append(
						"','yyyy-mm-dd hh24:mi:ss'),to_date('").append(
						user.getMmyxq()).append("','yyyy-mm-dd hh24:mi:ss'),'")
				.append(user.getZt()).append("','1','").append(user.getBz())
				.append("','','").append(user.getLxdh1()).append("','").append(user.getLxdh2())
				.append("','").append(user.getLxdh3()).append("','").append(
						user.getLxcz()).append("','").append(user.getDzyx())
				.append("','").append(user.getLxdz()).append("','").append(
						user.getBgdz()).append("','").append(user.getDzzs())
				.append("','").append(user.getDzzsxx()).append("',sysdate )");

		return this.jdbcTemplate.update(sbInsert.toString());
	}

	public int updateSysUser(SysUser user) throws Exception {
		StringBuffer sbUpdate = new StringBuffer("UPDATE frm_sysuser set ");
		sbUpdate.append(" yhmc = '").append(user.getYhmc()).append("'");
		if (StringUtils.isNotBlank(user.getSfzmhm()))
			sbUpdate.append(",sfzmhm='").append(user.getSfzmhm()).append("'");

		if (StringUtils.isNotBlank(user.getJh()))
			sbUpdate.append(",jh='").append(user.getJh()).append("'");

		if (StringUtils.isNotBlank(user.getGlbm()))
			sbUpdate.append(",glbm='").append(user.getGlbm()).append("'");

		if (StringUtils.isNotBlank(user.getIpks()))
			sbUpdate.append(",ipks='").append(user.getIpks()).append("'");

		if (StringUtils.isNotBlank(user.getIpjs()))
			sbUpdate.append(",ipjs='").append(user.getIpjs()).append("'");

		String modual = "";
		if (StringUtils.isNotBlank(user.getZhyxq())) {
			if (user.getZhyxq().length() > 10)
				modual = "yyyy-mm-dd hh24:mi:ss";
			else
				modual = "yyyy-mm-dd";
			sbUpdate.append(",zhyxq=to_date('").append(user.getZhyxq()).append(
					"','" + modual + "' )");

		}

		if (StringUtils.isNotBlank(user.getMmyxq())) {
			if (user.getMmyxq().length() > 10)
				modual = "yyyy-mm-dd hh24:mi:ss";
			else
				modual = "yyyy-mm-dd";
			sbUpdate.append(",mmyxq=to_date('").append(user.getMmyxq()).append(
					"','" + modual + "' )");

		}
		if (StringUtils.isNotBlank(user.getZt()))
			sbUpdate.append(",zt='").append(user.getZt()).append("'");

		//if (StringUtils.isNotBlank(user.getLxdh1()))
			sbUpdate.append(",lxdh1='").append(user.getLxdh1()).append("'");

		//if (StringUtils.isNotBlank(user.getLxdh2()))
			sbUpdate.append(",lxdh2='").append(user.getLxdh2()).append("'");

		//if (StringUtils.isNotBlank(user.getLxdh3()))
			sbUpdate.append(",lxdh3='").append(user.getLxdh3()).append("'");

		if (StringUtils.isNotBlank(user.getLxcz()))
			sbUpdate.append(",lxcz='").append(user.getLxcz()).append("'");

		if (StringUtils.isNotBlank(user.getDzyx()))
			sbUpdate.append(",dzyx='").append(user.getDzyx()).append("'");

		if (StringUtils.isNotBlank(user.getLxdz()))
			sbUpdate.append(",lxdz='").append(user.getLxdz()).append("'");

		if (StringUtils.isNotBlank(user.getBgdz()))
			sbUpdate.append(",bgdz='").append(user.getBgdz()).append("'");

		if (StringUtils.isNotBlank(user.getDzzs()))
			sbUpdate.append(",dzzs='").append(user.getDzzs()).append("'");

		if (StringUtils.isNotBlank(user.getDzzsxx()))
			sbUpdate.append(",dzzsxx='").append(user.getDzzsxx()).append("'");

		sbUpdate.append(",gxsj=sysdate  Where yhdh = '").append(user.getYhdh())
				.append("' ");

		return this.jdbcTemplate.update(sbUpdate.toString());
	}

	public int updatePasswd(SysUser user, String yhdh) throws Exception {
		String tmpSql = "";
		int result = this.checkPasswd(user, yhdh);
		if (result == 1) {
			String mm = this.stringToMD5(yhdh + user.getMm1());
			tmpSql = "update frm_sysuser set mm='" + mm + "' where yhdh='"
					+ yhdh + "'";
			result = this.jdbcTemplate.update(tmpSql);
		}
		return result;
	}

	public int insertUserRole(UserRole uRole) throws Exception {
		StringBuffer sb = new StringBuffer(
				"INSERT INTO JM_USERROLE(YHDH,JSDH) VALUES('");
		sb.append(uRole.getYhdh()).append("','").append(uRole.getJsdh())
				.append("') ");

		return this.jdbcTemplate.update(sb.toString());
	}

	public List getUserListForGlbm(String glbm) throws Exception {
		String ds = glbm.substring(0, 4);

		String sql = "select s.* from frm_department d join frm_sysuser s on d.glbm = s.glbm"
				+ " where d.sjbm > '0' and s.zt='1' "
				+ " and d.glbm like '"
				+ ds
				+ "%' and d.glbm in(select  xjjg from frm_prefecture  where dwdm = '"
				+ glbm + "')" + "order by d.jb, d.glbm";
		return this.queryForList(sql, SysUser.class);
	}
	
	public List getLeaderUserListForGlbm(String glbm) throws Exception {
		String ds = glbm.substring(0, 4);
		String sql = "select s.* from frm_department d join frm_sysuser s on d.glbm = s.glbm"
				+ " where 1=1  "
				//+ " d.sjbm > 0 "
				+ "  and  s.zt='1'     "
				+ " and d.glbm like '"
				+ ds
				+ "%' " 
				+" and d.glbm in(SELECT DWDM FROM FRM_PREFECTURE WHERE XJJG = '"+glbm+"' )"
				+ "order by d.jb, d.glbm";
		return this.queryForList(sql, SysUser.class);
	}
	
	public List getLocalUserListForGlbm(String glbm) throws Exception {
		String ds = glbm.substring(0, 4);
		String sql = "select s.* from frm_department d join frm_sysuser s on d.glbm = s.glbm"
				+ " where 1=1   "
				//+" d.sjbm > 0 "
				+  " and s.zt='1' "
				+ " and d.glbm like '"
				+ ds
				+ "%' " 
				+" and d.glbm = '"+glbm+"' "
				+ "order by d.jb, d.glbm";
		return this.queryForList(sql, SysUser.class);
	}

	public int removeSysUser(String yhdh) throws Exception {
		String sql = "DELETE from frm_sysuser Where yhdh =?";

		return this.jdbcTemplate.update(sql, new Object[] { yhdh });
	}

	public int removeUserRole(String yhdh) throws Exception {
		String sql_role = "DELETE From JM_USERROLE Where yhdh =?";

		return this.jdbcTemplate.update(sql_role, new Object[] { yhdh });
	}

	public List getUserRole(String yhdh) throws Exception {
		String sql = "SELECT A.YHDH,A.JSDH FROM JM_USERROLE A WHERE " +
				"EXISTS(SELECT B.JSDH FROM JM_ROLE B WHERE B.JSDH = A.JSDH) " +
				"AND EXISTS(SELECT M.CXDH FROM JM_ROLEMENU M WHERE M.JSDH=A.JSDH) " +
				"AND YHDH = ?";
		return this.queryForList(sql ,new Object[]{yhdh} , UserRole.class);
	}

	public SysUser getSysuserBySfzmhm(String sfzmhm) throws Exception {
		String sql = "select * from frm_sysuser where sfzmhm in("+sfzmhm+")";
		List<SysUser> list = this.queryForList(sql, SysUser.class);
		SysUser user = null;
		if ((list != null) && (list.size() > 0)) {
			user = list.get(0);
		}
		return user;
	}

	public SysUser getSysuserBySfzmhm(Object... args) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT YHDH,YHMC,MM,SFZMHM,JH,GLBM,IPKS,IPJS,ZHYXQ,MMYXQ,ZT,"
				+ "QXMS,SQ,LXDH1,LXDH2,LXDH3,LXCZ,DZYX,LXDZ,BGDZ,SYYHDH,DZZS,DZZSXX,GXSJ,BZ FROM FRM_SYSUSER WHERE SFZMHM IN(");
		if(args.length==0)
			return null;
		for(int i = args.length; i >= 1; i--){
			if(i==1){
				sb.append("?");
			} else {
				sb.append("?,");
			}
		}
		sb.append(")");
		List<SysUser> list = this.queryForList(sb.toString(), args, SysUser.class);
		SysUser user = null;
		if ((list != null) && (list.size() > 0)) {
			user = list.get(0);
		}
		return user;
	}

	public String stringToMD5(String mm) {
		String resultSql = "select JM_SYS_PKG.ENCODE('" + mm
				+ "') as mm  from dual ";

		List list = this.jdbcTemplate.queryForList(resultSql);
		Map map = (Map) list.get(0);

		return map.get("mm").toString();
	}

	public int checkPasswd(SysUser user, String yhdh) {
		SysUser tmpUser;
		String resultSql = "";
		try {
			String mm = yhdh + user.getMm();
			tmpUser = this.getSysUserByYHDH(yhdh);
			resultSql = "select count(1) from dual where JM_SYS_PKG.DECODE('"
					+ tmpUser.getMm() + "') ='" + mm + "'";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.jdbcTemplate.queryForInt(resultSql);
	}

	public int syncUser() throws Exception {
		String callString = "{call DC_SYNCHRONIZATION_PKG.getUsers(?)}";
		CallableStatementCallbackImpl callBack = new CallableStatementCallbackImpl() {
			public Object doInCallableStatement(CallableStatement cstmt)
					throws SQLException, DataAccessException {
				cstmt.registerOutParameter(1, 2);
				cstmt.execute();
				int iResult = cstmt.getInt(1);
				return new Integer(iResult);
			}
		};
		callBack.setParameterObject(null);
		Integer i = (Integer) this.jdbcTemplate.execute(callString, callBack);
		return i.intValue();
	}

	 /**
     * 同步警综人员
     * @return
     * @throws Exception
     */  
	public int syncQbUser() throws Exception {
		String callString = "{call SyncQbUsers(?)}";
		CallableStatementCallbackImpl callBack = new CallableStatementCallbackImpl() {
			public Object doInCallableStatement(CallableStatement cstmt)
					throws SQLException, DataAccessException {
				cstmt.registerOutParameter(1, 2);
				cstmt.execute();
				int iResult = cstmt.getInt(1);
				return new Integer(iResult);
			}
		};
		callBack.setParameterObject(null);
		Integer i = (Integer) this.jdbcTemplate.execute(callString, callBack);
		return i.intValue();
	}
	
	public int reset(String yhdh,String mm) throws Exception {
		String sql = "update frm_sysuser set mm='"+mm+"' where yhdh='" + yhdh
				+ "'";
		return this.jdbcTemplate.update(sql);
	}

	/**
	 * 需要对角色树形显示进行分类，分类之后要针对子级进行排序，所以讲SQL改成这样 liumeng 2016-7-8
	 */
	public List getRoleList(String subSql) throws Exception {
//		String sql = "select * from jm_role where 1=1 " + subSql +" order by jsjb";
		String sql = "select x.jsdh,x.jsmc,x.jssx,x.jsjb,x.bz,x.type from (select jsdh,jsmc,jssx,jsjb,bz,type from jm_role where 1=1 " +subSql+ " and type=100 or jsdh=100 order by jsjb) x"
				+ "	union all"
				+ " select y.jsdh,y.jsmc,y.jssx,y.jsjb,y.bz,y.type from (select jsdh,jsmc,jssx,jsjb,bz,type from jm_role where 1=1 " +subSql+ " and type<>100 and jsdh<>100 order by jsmc) y";
		List list = this.queryList(sql, Role.class);
		return list;
	}

	@Deprecated
	public Map findSysUsersForMap(Map filter, SysUser user) throws Exception {
		String tmpSql = "";
		if (StringUtils.isNotBlank(user.getGlbm())) {
			tmpSql = tmpSql + " and a.glbm='" + user.getGlbm() + "'";
		}
		if (StringUtils.isNotBlank(user.getYhdh())) {
			tmpSql = tmpSql + " and a.yhdh like '%" + user.getYhdh() + "%'";
		}
		if (StringUtils.isNotBlank(user.getYhmc())) {
			tmpSql = tmpSql + " and a.yhmc like '%" + user.getYhmc() + "%'";
		}
		if (StringUtils.isNotBlank(user.getJh())) {
			tmpSql = tmpSql + " and a.jh='" + user.getJh() + "'";
		}
		if (StringUtils.isNotBlank(user.getSfzmhm())) {
			tmpSql = tmpSql + " and a.sfzmhm='" + user.getSfzmhm() + "'";
		}

		// 1:因用户还未分配角色时--查询不出来--
		
		tmpSql = tmpSql + " and (select count(1) from jm_userrole c where a.yhdh=c.yhdh and c.jsdh in("
					+ user.getRoles() + ")) > 0 ";

		// 2:用户还未分配角色时也可以查询用户
		
		tmpSql = "select a.YHDH,a.YHMC,a.MM,a.SFZMHM,a.JH,a.GLBM,a.IPKS,a.IPJS,to_char(a.ZHYXQ,'yyyy-mm-dd') zhyxq,to_char(a.mmyxq,'yyyy-mm-dd') mmyxq,a.ZT,a.QXMS,a.BZ,b.bmmc "
				+ "from frm_sysuser a,frm_department b "
				+ "where a.glbm=b.glbm and b.glbm in (select xjjg from frm_prefecture where dwdm = '"+user.getGlbm()+"' or dwdm = '"+filter.get("dwdm")+"')"
				+ tmpSql + " order by a.yhmc";
		
		return this.findPageForMap(tmpSql, Integer.parseInt(filter.get(
				"curPage").toString()), Integer.parseInt(filter.get("pageSize")
				.toString()));
	}
	
	@Override
	public List<Role> getRoleListByParam(String conSql) throws Exception {
		String sql = "select * from jm_role where jsdh in ("+ conSql +")";
		return this.queryList(sql, Role.class);
	}

	public Map<String,Object> getUserListForExportExcel(Map<String,Object> filter, SysUser user) throws Exception {
		String tmpSql = "";
		/*if (StringUtils.isNotBlank(user.getGlbm())) {
			tmpSql = tmpSql + " and a.glbm='" + user.getGlbm() + "'";
		}*/
		if (StringUtils.isNotBlank(user.getYhdh())) {
			tmpSql = tmpSql + " and a.yhdh like '%" + user.getYhdh() + "%'";
		}
		if (StringUtils.isNotBlank(user.getYhmc())) {
			tmpSql = tmpSql + " and a.yhmc like '%" + user.getYhmc() + "%'";
		}
		if (StringUtils.isNotBlank(user.getJh())) {
			tmpSql = tmpSql + " and a.jh='" + user.getJh() + "'";
		}
		if (StringUtils.isNotBlank(user.getSfzmhm())) {
			tmpSql = tmpSql + " and a.sfzmhm='" + user.getSfzmhm() + "'";
		}

		if (StringUtils.isNotBlank(user.getRoles())) {
			tmpSql = tmpSql
					+ " and (select count(1) from jm_userrole c where a.yhdh=c.yhdh and c.jsdh in ("
					+ user.getRoles() + ")) > 0 ";
		}
		//if ("2".equals(user.getAuthority())) {//有角色
			tmpSql = tmpSql + " and (select count(1) from jm_userrole where yhdh= a.yhdh ) > 0 ";
		//}else if("3".equals(user.getAuthority())){//无角色（未授权）
		//	tmpSql = tmpSql + " and (select count(1) from jm_userrole where yhdh= a.yhdh ) = 0 ";
		//}
		
		StringBuffer psql = new StringBuffer(" SELECT /*+FIRST_ROWS(20)*/* FROM ( ");
//		psql.append(" SELECT temp.* ,ROWNUM num FROM ( ");
		String tmpSql0 = "select dept.glbm,dept.bmmc,a.yhmc,a.jh,a.sfzmhm,wm_concat(srole.jsmc) jsmc from frm_sysuser a" +
				" inner join jm_userrole ur on ur.yhdh=a.yhdh" +
				" left join jm_role srole ON ur.jsdh = srole.jsdh" +
				" left join frm_department dept on a.glbm=dept.glbm" +
				" where dept.glbm in (select xjjg  from frm_prefecture where 1 = 1";
				
		String tmpSql1 = tmpSql0;
				if (StringUtils.isNotBlank(user.getGlbm())) {
					tmpSql1 = tmpSql1 + " and dwdm = '"+user.getGlbm()+"'";
				}
		String tmpSql2 = "";	
				//+ tmpSql + " order by a.yhmc";
		       tmpSql2 = tmpSql1 + tmpSql + " ) " +
				" group by dept.sjbm,dept.glbm,dept.bmmc,a.yhmc,a.jh,a.sfzmhm" +
//				" order by dept.glbm,dept.bmmc,a.yhmc";
				" order by dept.sjbm,nlssort(dept.bmmc, 'NLS_SORT=SCHINESE_PINYIN_M'),NLSSORT (A.yhmc,'NLS_SORT=SCHINESE_PINYIN_M')";

//		psql.append(tmpSql+" ) temp where ROWNUM <= "+filter.get("pageSize").toString());
//		psql.append(" ) WHERE num > 0 ");
		psql.append( tmpSql2 + " ) ");
		
		Connection conn = null;
		DataSource ds = this.jdbcTemplate.getDataSource();
		conn = DataSourceUtils.getConnection(ds);
		PreparedStatement ps2 = conn.prepareStatement(psql.toString());
		ResultSet rs = ps2.executeQuery();
		
		Map<String,Object> map = new HashMap<String, Object>();
		List<SysUser> list = new ArrayList<SysUser>();
		
		while(rs.next()) {
			SysUser bean = new SysUser();
			String xzqh = rs.getString(1);				
			bean.setBmmc(rs.getString(2));
			bean.setYhmc(rs.getString(3));
//			CLOB clob = (CLOB) rs.getClob(4);//角色名称
//			String clob = rs.getString(4);
//			if (clob != null) {
//				StringBuffer sb = new StringBuffer(65535);
//				Reader stream = clob.getCharacterStream();
//				char[]b = new char[60000];
//				int i = 0;
//				while((i=stream.read(b))!=-1){
//					sb.append(b,0,i);
//				}
//				bean.setRolemc(sb.toString());
//			}
			bean.setJh(rs.getString(4));
			bean.setSfzmhm(rs.getString(5));
			bean.setRolemc(rs.getString(6));
			xzqh=xzqh.substring(0,6);	
			bean.setBz(this.systemDao.getDistrictNameByXzqh(xzqh));
			list.add(bean);
		}
		DataSourceUtils.releaseConnection(conn, ds);
		map.put("rows", list);
		return map;
	}
//获取警员数据查询
	@Override
	//public List<Policess> getPoliceInfo(String USER_NAME,String USER_IDCARD, String USER_REALNAME) {
public Map getPoliceInfo(Map filter, SysUser command) throws Exception {	
		StringBuffer sql =new StringBuffer();  
		sql.append("SELECT USER_NAME as YHDH,USER_REALNAME as YHMC ,USER_IDCARD as SFZMHM FROM qbzx_user where LASTUPDATACOLUMN <>99");
			if(command.getJh()!=null && !command.getJh().equals("")){
				sql.append(" and USER_NAME = '"+command.getJh()+"'");
			}else if(command.getSfzmhm()!=null && !command.getSfzmhm().equals("")){
				sql.append("and  USER_IDCARD = '"+command.getSfzmhm()+"'");
			}else if(command.getYhmc()!=null && !command.getYhmc().equals("")){
				sql.append("and USER_REALNAME = '"+command.getYhmc()+"'");
			}
			return this.findPageForMap("SELECT * FROM (" + sql +") ", Integer.parseInt(filter.get(
					"curPage").toString()), Integer.parseInt(filter.get("pageSize")
					.toString()));
	}
	//yaowang单个警员信息查询
	public List getPolice(String USER_NAME, String USER_IDCARD,String USER_REALNAME ) throws Exception{
		StringBuffer sql =new StringBuffer();  
		sql.append("SELECT * FROM qbzx_user where 1=1 and LASTUPDATACOLUMN !='99'");
		int count =0;
		if(USER_NAME != null && ! USER_NAME.equals("")){
				sql.append(" and USER_NAME = '"+USER_NAME+"'");
				count ++;}
		if(USER_IDCARD!=null && ! USER_IDCARD.equals("") ){
				sql.append("and  USER_IDCARD = '"+USER_IDCARD+"'");
				count ++;}
		if(USER_REALNAME!=null && !USER_REALNAME.equals("")){
			sql.append("and  USER_REALNAME like '%"+USER_REALNAME+"%'");
			count ++;}
		List<Policess> list=new ArrayList<Policess>();
		if(count==0){
			return list;
		}else{
		List<Map<String,Object>> l = this.jdbcTemplate.queryForList(sql.toString());
			
			Policess pol=new Policess();
			//String USER_WORKTEL;
			for(Map<String,Object> map : l){
			pol.setID(map.get("ID").toString());
			pol.setUSER_NAME((String)map.get("USER_NAME"));
			pol.setUSER_REALNAME(map.get("USER_REALNAME").toString());
			pol.setUSER_SEX(map.get("USER_SEX").toString());
			if(map.get("USER_WORKTEL")==null){				
				pol.setUSER_WORKTEL("");
			}else{
				pol.setUSER_WORKTEL(map.get("USER_WORKTEL").toString());
			}
			if(map.get("USER_MOBILETEL1")==null){				
				pol.setUSER_MOBILETEL1("");
			}else{
				pol.setUSER_MOBILETEL1(map.get("USER_MOBILETEL1").toString());
			}
			if(map.get("USER_IDCARD")==null){				
				pol.setUSER_IDCARD("");
			}else{
				pol.setUSER_IDCARD(map.get("USER_IDCARD").toString());
			}
			if(map.get("OFFICALRANK")==null){				
				pol.setOFFICALRANK("");
			}else{
				pol.setOFFICALRANK(map.get("OFFICALRANK").toString());
			}
			if(map.get("ORGCODE")==null){				
				pol.setORGCODE("");
			}else{
				pol.setORGCODE(map.get("ORGCODE").toString());
			}
			if(map.get("ORGNAME")==null){				
				pol.setORGNAME("");
			}else{
				pol.setORGNAME(map.get("ORGNAME").toString());
			}
			list.add(pol);
		}
		return list;
		}
		}

    //删除警员信息
	public int updatePolice(String syyh) throws Exception {
		StringBuffer sql=new StringBuffer();
			sql.append("update qbzx_user set LASTUPDATACOLUMN='99'where ID='");
		sql.append(syyh).append("'");
			 int i=this.jdbcTemplate.update(sql.toString());
			 return  i;
	}

	public List getKcPolice(String user_name, String user_idcard,String user_realname ) throws Exception{
		StringBuffer sql =new StringBuffer();  
		sql.append("select t.user_id as yhid,t.user_name as yhdh,t.user_name as jh,t.user_realname "
				+ "as yhmc,t.user_idcard as sfzmhm,t.user_worktel as lxdh1,t.user_mobiletel1 as "
				+ "lxdh2,t.user_email as dzyx,t.user_address as lxdz from v_sm_user t where(1=1) ");
		if(user_name != null && ! user_name.equals("")){
			sql.append(" and t.user_name = '"+user_name+"'");
		}
		if(user_idcard!=null && ! user_idcard.equals("") ){
			sql.append(" and  t.user_idcard = '"+user_idcard+"'");
		}
		if(user_realname!=null && !user_realname.equals("")){
			sql.append(" and t.user_realname like '%"+user_realname+"%'");
		}
		List<SysUser> list = this.queryForList(sql.toString(), SysUser.class);
		return list;
	}
	
	public SysUser findUserByJHAndSfzh(String jh, String sfzh) throws Exception {
		String sql = "select u.yhdh, u.yhmc, u.mm, u.sfzmhm, u.jh, u.glbm, u.ipks, u.ipjs, u.zhyxq, "
				+ "u.mmyxq, u.zt, u.qxms, u.sq, u.lxdh1, u.lxdh2, u.lxdh3, u.lxcz, u.dzyx, u.lxdz, "
				+ "u.bgdz, u.syyhdh, u.dzzs, u.dzzsxx, u.gxsj, u.bz from frm_sysuser u where(1=1) "
				+ "and u.jh = ? and u.sfzmhm = ?";
		List<SysUser> sysuserList = this.queryForList(sql,new Object[]{jh, sfzh}, SysUser.class);
		if ((sysuserList == null) || (sysuserList.size() == 0)) {
			return null;
		}
		SysUser user = sysuserList.get(0);
		return user;
	}
}
