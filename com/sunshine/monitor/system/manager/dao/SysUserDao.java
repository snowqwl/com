package com.sunshine.monitor.system.manager.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Policess;
import com.sunshine.monitor.comm.bean.Role;
import com.sunshine.monitor.comm.bean.UserRole;
import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.SysUser;

public interface SysUserDao extends BaseDao{

	public List getRoleList(Role role)throws Exception;
	
	public List getRoleList(String subSql)throws Exception;
	
	public List getUserRole(String yhdh)throws Exception;
	
	public Role getRole(String jsdh)throws Exception;
	
	/**
	 * 增加能查看的角色的用户
	 * 市->县->派出所
	 * @param filter
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Map findSysUsersForMap(Map filter,SysUser user)throws Exception;
	
	public Map findSysUserForMap(Map filter,SysUser user)throws Exception;
	
	public SysUser getSysUserByYHDH(String yhdh)throws Exception;
	
	public List getUserListForGlbm(String glbm)throws Exception;
	
	public List getLeaderUserListForGlbm(String glbm)throws Exception;
	
	public List getLocalUserListForGlbm(String glbm) throws Exception;
	
	public int insertUserRole(UserRole uRole)throws Exception;
	
	public int insertSysUser(SysUser user)throws Exception;
	
	public int updateSysUser(SysUser user)throws Exception;
	
	public int removeSysUser(String yhdh)throws Exception;
	
	public int removeUserRole(String yhdh)throws Exception;

	public SysUser getSysuserBySfzmhm(String sfzmhm)throws Exception;
	
	public SysUser getSysuserBySfzmhm(Object... args)throws Exception;

	public String stringToMD5(String mm);
	
	public int updatePasswd(SysUser user,String yhdh) throws Exception;
	
	public int checkPasswd(SysUser user,String yhdh);
	
	public int syncUser() throws Exception;
	
	 /**
     * 同步警综人员
     * @return
     * @throws Exception
     */  
	public int syncQbUser() throws Exception;
	
	/**
	 * ��������
	 * @return
	 * @throws Exception
	 */
	public int reset(String yhdh,String mm) throws Exception;
	
	/**
	 * 主要用于新增到日志记录中，获取角色名称
	 * @param conSql 查询sql
	 * @return
	 * @throws Exception
	 */
	public List<Role> getRoleListByParam(String conSql) throws Exception;
	
	/**
	 * 查询用户到处列表
	 * @param filter
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getUserListForExportExcel(Map<String,Object> filter, SysUser user) throws Exception;
//获取警员信息
	public Map getPoliceInfo(Map filter, SysUser command) throws Exception;
	public List getPolice(String USER_NAME, String USER_IDCARD,String USER_REALNAME) throws Exception;
//删除警员信息
	public int updatePolice(String syyh) throws Exception;
	
	public List getKcPolice(String user_name, String user_idcard,String user_realname ) throws Exception;
	
	public SysUser findUserByJHAndSfzh(String jh, String sfzh) throws Exception;
}
