package com.sunshine.monitor.system.manager.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.Policess;
import com.sunshine.monitor.comm.bean.Role;
import com.sunshine.monitor.comm.bean.UserRole;
import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.util.DateUtils;
import com.sunshine.monitor.comm.util.excel.OutputExcelUtils;
import com.sunshine.monitor.system.manager.aspect.ManagerFilterAnnotation;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.dao.LogDao;
import com.sunshine.monitor.system.manager.dao.SysUserDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.DepartmentManager;
import com.sunshine.monitor.system.manager.service.SysUserManager;

@Transactional
@Service("sysUserManager")
public class SysUserManagerImpl implements SysUserManager {

	@Autowired
	@Qualifier("sysUserDao")
	private SysUserDao sysUserDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("logDao")
	private LogDao logDao;
	
	@Autowired
	private DepartmentManager  departmentManager;
	
	private Logger log = LoggerFactory.getLogger(SysUserManagerImpl.class);
	
	public List getRoleList(Role role) throws Exception {
		
		return sysUserDao.getRoleList(role);
	}
	
	public Map findSysUserForMap(Map filter, SysUser user) throws Exception {
		
		return sysUserDao.findSysUserForMap(filter, user);
	}
	
	public SysUser getSysUser(String yhdh) throws Exception {
		SysUser user = sysUserDao.getSysUserByYHDH(yhdh);
		if (user != null) {
			List roleList = sysUserDao.getUserRole(yhdh);
			String ss = "";
			String rolemc = "";
			for (Object o : roleList) {
				if (ss.length() > 0)
					ss += ",";
				UserRole uRole = (UserRole) o;
				if("100000".equals(uRole.getJsdh())){
					user.setAuthority("0");
				}
				ss += uRole.getJsdh();
				Role role = sysUserDao.getRole(uRole.getJsdh());
				if (role != null) {
					if (rolemc.length() > 0)
						rolemc += ",";
					rolemc += role.getJsmc();

				}

			}
			user.setRoles(ss);
			user.setRolemc(rolemc);
			
			user.setBmmc(departmentManager.getDepartmentName(user.getGlbm()));
		}
		return user;
	}
	
	@ManagerFilterAnnotation(method = "addUser", clazz = SysUser.class)
	@OperationAnnotation(type=OperationType.USER_MODIFY,description="用户修改")
	public void saveOrUpdateSysUser(SysUser user, String modul,SysUser sessionUser,String Ip) throws Exception {
		
		try {
			if("new".equals(modul)){
				String mm = user.getYhdh() + user.getMm();
				String md5MM = sysUserDao.stringToMD5(mm);
				
				user.setMm(md5MM);
				this.insertSysUser(user);
			}else{
				this.updateSysUser(user);
			}
			
			//更新角色表
			StringBuffer logContent = new StringBuffer();
			//1.日志新增-获得用户删除前的角色
			List<Role> roleListBefore = this.sysUserDao.getRoleListByParam("select jsdh From JM_USERROLE Where yhdh ='"+user.getYhdh()+"'");
			String cznr = "";
			for(Role role : roleListBefore){
				cznr += role.getJsmc()+",";
			}
			if(!cznr.equals("")){
				cznr = cznr.substring(0,cznr.length()-1);
			}
			logContent.append(sessionUser.getYhmc()+"对"+user.getYhmc()+"进行操作：\n");				
			logContent.append(OperationType.ROLE_USER_BATCH_ADD.getDesc()+"前: "+ cznr +"\n");
			
			//先删除原有的角色
			this.removeUserRole(user.getYhdh());
			
			//插入新更改的角色
			if(StringUtils.isNotBlank(user.getRoles())){//判断是否存在
				String[]roles = user.getRoles().split(",");
				for(String role : roles){
					UserRole uRole = new UserRole();
					uRole.setYhdh(user.getYhdh());
					uRole.setJsdh(role);
					this.insertUserRole(uRole);
				}
			}
			
			//2.日志新增-获得用户删除后的角色
			cznr = "";
			if(StringUtils.isNotBlank(user.getRoles())){//判断是否存在
				List<Role> roleListAfter = this.sysUserDao.getRoleListByParam(user.getRoles());						
				for(Role role : roleListAfter){
					cznr += role.getJsmc()+",";
				}
				if(!cznr.equals("")){
					cznr = cznr.substring(0,cznr.length()-1);
				}
			}
			logContent.append(OperationType.ROLE_USER_BATCH_ADD.getDesc()+"后: "+ cznr);
			//3.新增日志到数据库
			this.saveRoleRecordToLog(sessionUser, Ip, logContent.toString(), OperationType.ROLE_USER_BATCH_ADD.getType());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 用户授权添加日志
	 * @param roleList
	 * @param userName
	 * @param curUser
	 * @param Ip
	 * @param description
	 * @param logCzlx
	 * @throws Exception
	 */
	public void saveRoleRecordToLog(SysUser curUser,String Ip,String description,String logCzlx) throws Exception {
		String yhdh = curUser.getYhdh();
		String glbm = curUser.getGlbm();
		Log log = new Log();
		log.setGlbm(glbm);
		log.setIp(Ip);
		log.setYhdh(yhdh);
		log.setCzlx(logCzlx);		
		log.setCznr(description);
		//System.out.println(cznr);
		logDao.saveLog(log);
	}
	
	public void delSysUser(SysUser user) throws Exception {
		
		this.removeUserRole(user.getYhdh());
		this.removeSysUser(user.getYhdh());
		
	}
	
	public List getUserListForGlbm(String glbm) throws Exception {
		
		return sysUserDao.getUserListForGlbm(glbm);
	}
	

	public List getLeaderUserListForGlbm(String glbm) throws Exception {
		return sysUserDao.getLeaderUserListForGlbm(glbm);
	}
	
	public List getLocalUserListForGlbm(String glbm) throws Exception {
		return sysUserDao.getLocalUserListForGlbm(glbm);
	}
	
	public int insertUserRole(UserRole uRole) throws Exception {
		
		return sysUserDao.insertUserRole(uRole);
	}
	
    @OperationAnnotation(type=OperationType.USER_ADD,description="用户增加")
	public int insertSysUser(SysUser user) throws Exception {
		
		return sysUserDao.insertSysUser(user);
	}
	
    @OperationAnnotation(type=OperationType.USER_MODIFY,description="用户修改")
	public int updateSysUser(SysUser user) throws Exception {
		
		return sysUserDao.updateSysUser(user);
	}
    
    @OperationAnnotation(type=OperationType.USER_DELETE,description="用户删除")
	public int removeSysUser(String yhdh) throws Exception {
		
		return sysUserDao.removeSysUser(yhdh);
	}
    
    public int updatePasswd(SysUser user,String yhdh) throws Exception{
        	return sysUserDao.updatePasswd(user,yhdh);
    }

	public int removeUserRole(String yhdh) throws Exception {

		return sysUserDao.removeUserRole(yhdh);
	}

	public SysUser getSysuserBySfzmhm(String sfzmhm) throws Exception {
		SysUser user = sysUserDao.getSysuserBySfzmhm(sfzmhm);
		if (user != null) {
			List roleList = sysUserDao.getUserRole(user.getYhdh());
			String ss = "";
			String rolemc = "";
			for (Object o : roleList) {
				if (ss.length() > 0)
					ss += ",";

				UserRole uRole = (UserRole) o;
				ss += uRole.getJsdh();

				Role role = sysUserDao.getRole(uRole.getJsdh());
				if (role != null) {
					if (rolemc.length() > 0)
						rolemc += ",";
					rolemc += role.getJsmc();

				}

			}
			user.setRoles(ss);
			user.setRolemc(rolemc);
			
			user.setBmmc(departmentManager.getDepartmentName(user.getGlbm()));
		}
		return user;
	}
	
 
	public SysUser getSysuserBySfzmhm(List<String> args) throws Exception {
		
		return this.sysUserDao.getSysuserBySfzmhm(args.toArray());
	}

    @OperationAnnotation(type=OperationType.POLICE_USER_SYNC,description="警综用户同步")
	public int syncUser() throws Exception {
		return this.sysUserDao.syncQbUser();
	}

	public int reset(String yhdh) throws Exception {
		String mm = yhdh+"888888";
		String md5MM = sysUserDao.stringToMD5(mm);
		return this.sysUserDao.reset(yhdh,md5MM);
	}

	/**
	 * 暂时实现
	 */
	public List getRolesList(Role role) throws Exception {
		String jsJb = "0";
		String jsSx = null;
		String subSql =  "";
		if(role!=null){
			jsJb = role.getJsjb();
			jsSx = role.getJssx();
			// 上级可以查下级别角色
			/**
			if(!StringUtils.isBlank(jsJb))
				subSql = " and jsjb>='"+jsJb+"'";
			*/
			// 控制管理员[从县以下有区别]
			boolean is = (Integer.parseInt(jsJb) >= 3)? true :false ;
			if(is){
				if(!StringUtils.isBlank(jsSx)){
					if(!"0".equals(jsSx)){
						//subSql += " and jssx = '0'";
						subSql +=" and (jsjb='"+jsJb+"' and jssx='0') or (jsjb>'"+jsJb+"')";
					}else{
						subSql += " and jsdh = '"+ role.getJsdh() +"'";
					}
				}
			} else {
				// 上级可以查下级别角色
				if(!StringUtils.isBlank(jsJb))
					subSql = " and jsjb>='"+jsJb+"'";
			}
		}
		return this.sysUserDao.getRoleList(subSql);
	}

	public Map findSysUsersForMap(Map filter, SysUser user) throws Exception {
		
		return sysUserDao.findSysUsersForMap(filter, user);
	}
	
	 private Map<String, Object> getUserAuthorityList(Map<String,Object> filter, SysUser user) throws Exception {
	    	Map<String, Object> map = new HashMap<String, Object>();
	    	filter.put("pageSize", "1000");
	    	map = sysUserDao.getUserListForExportExcel(filter, user);
	    	return map;
	    }
	
	public String outPutUserList(Map<String,Object> filter,String path,SysUser user) {
		StringBuffer url = new StringBuffer();
		String filename = "";
		OutputStream output = null;
		Map<String, Object> result = null;
		try {
			result = getUserAuthorityList(filter,user);
			List<SysUser> data = (List<SysUser>) result.get("rows");
			// data
			LinkedHashMap<String, List> sheets = new LinkedHashMap<String, List>();
			sheets.put("用户授权信息列表", data);
			// Heads
			List<String[]> heads = new ArrayList<String[]>();
			heads.add(new String[]{"行政区划","单位","姓名","警号","身份证号码","角色",});
			// Fields
			List<String[]> fields = new ArrayList<String[]>();
			fields.add(new String[]{"bz","bmmc","yhmc","jh","sfzmhm","rolemc",});
			
			
			// ====暂时实现=====
			filename = DateUtils.getCurrTimeStr(1)+".xls";
			String fullPath = path;
			url.append(fullPath).append(filename);
			
			File file = new File(url.toString());
			output = new FileOutputStream(file);  
			OutputExcelUtils.outPutExcel(heads, fields, sheets, new String[]{"用户授权信息"}, null, output);
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(output != null)
					output.close(); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filename;
	}
	//yaowang 警员信息查询
	public List<Policess> getPolice(String USER_NAME,String USER_IDCARD,String USER_REALNAME) throws Exception {
		List<Policess> policess = sysUserDao.getPolice(USER_NAME,USER_IDCARD,USER_REALNAME);
		return policess;
	}
	
	public List<SysUser> getKcPolice(String user_name, String user_idcard,String user_realname) throws Exception {
		List<SysUser> policess = sysUserDao.getKcPolice(user_name, user_idcard, user_realname);
		for(SysUser u : policess) {
			List<Department> depList = departmentManager.getKcBmmc(u.getJh());
			if(depList.size() > 0) {
				u.setGlbm(depList.get(0).getGlbm().trim());
				u.setBmmc(depList.get(0).getBmmc());
			}
		}
		return policess;
	}
	
	public Map getPoliceInfo(Map filter, SysUser command) throws Exception {
			return sysUserDao.getPoliceInfo(filter, command);
		}
	//删除警员信息
	public int updatePolice(String syyh) throws Exception {
		return sysUserDao.updatePolice(syyh);
	}

	@Override
	public SysUser findUserByJHAndSfzh(String jh, String sfzh) throws Exception {
		return sysUserDao.findUserByJHAndSfzh(jh, sfzh);
	}
}