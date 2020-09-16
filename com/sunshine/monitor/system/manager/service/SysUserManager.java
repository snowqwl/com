package com.sunshine.monitor.system.manager.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Policess;
import com.sunshine.monitor.comm.bean.Role;
import com.sunshine.monitor.comm.bean.UserRole;
import com.sunshine.monitor.system.manager.bean.SysUser;

public interface SysUserManager {

	public List getRoleList(Role role)throws Exception;
	
	public List getRolesList(Role role)throws Exception;
	
	public Map findSysUserForMap(Map filter, SysUser user)throws Exception;
	
	public Map findSysUsersForMap(Map filter, SysUser user) throws Exception;
	
	public SysUser getSysUser(String yhdh)throws Exception;
	/** 
	 * 
	 * 函数功能说明：保存和更新用户表
	 * 修改日期 	2013-6-26
	 * @param user
	 * @param modul
	 * @throws Exception    
	 * @return void
	 */
	public void saveOrUpdateSysUser(SysUser user, String modul,SysUser sessionUser,String Ip) throws Exception;
	
	/**
	 * 
	 * 函数功能说明:删除用户和其角色
	 * 修改日期 	2013-6-26
	 * @param user
	 * @return
	 * @throws Exception    
	 * @return int
	 */
	public void delSysUser(SysUser user) throws Exception;
	
	public List getUserListForGlbm(String glbm)throws Exception;
	
	public List getLeaderUserListForGlbm(String glbm)throws Exception;
	
	public List getLocalUserListForGlbm(String glbm) throws Exception;
	
	public int insertUserRole(UserRole uRole)throws Exception;
	
	public int insertSysUser(SysUser user)throws Exception;
	
	public int updateSysUser(SysUser user)throws Exception;
	
	public int removeSysUser(String yhdh)throws Exception;
	
	public int removeUserRole(String yhdh)throws Exception;
	
	public SysUser getSysuserBySfzmhm(String sfzmhm) throws Exception;
	//获取多个警员信息
	public Map getPoliceInfo(Map filter, SysUser command) throws Exception;
	//获取单个警员信息
	public List getPolice(String USER_NAME, String USER_IDCARD,String USER_REALNAME) throws Exception;
	
	public List<SysUser> getKcPolice(String user_name, String user_idcard,String user_realname) throws Exception;
	
	public SysUser getSysuserBySfzmhm(List<String> args) throws Exception;
	
	public int updatePasswd(SysUser user,String yhdh) throws Exception;
	
	public int syncUser() throws Exception ;
	
	public int reset(String yhdh) throws Exception;
	
	/**
	 * 用户信息导出
	 * @param filter
	 * @param path
	 * @param user
	 * @return
	 */
	public String outPutUserList(Map<String,Object> filter,String path,SysUser user);
//删除警员信息
	public int updatePolice(String syyh) throws Exception;
	
	/**
	 * 根据警号和身份证号查询用户信息
	 * @param jh
	 * @param sfzh
	 * @return
	 * @throws Exception
	 */
	public SysUser findUserByJHAndSfzh(String jh, String sfzh) throws Exception;
}
