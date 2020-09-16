package com.sunshine.monitor.system.manager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.util.StringUtils;
import com.sunshine.monitor.system.manager.aspect.ManagerFilterAnnotation;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Prefecture;
import com.sunshine.monitor.system.manager.dao.DepartmentDao;
import com.sunshine.monitor.system.manager.dao.LogDao;
import com.sunshine.monitor.system.manager.dao.SysUserDao;
import com.sunshine.monitor.system.manager.service.DepartmentManager;
import com.sunshine.monitor.system.manager.service.PrefectureManager;

@Transactional
@Service("departmentManager")
public class DepartmentManagerImpl implements DepartmentManager {

	@Autowired
	@Qualifier("departmentDao")
	private DepartmentDao departmentDao;
	
	@Autowired
	private PrefectureManager prefectureManager;
	
	@Autowired
	@Qualifier("sysUserDao")
	private SysUserDao sysUserDao;
	
	@Autowired
	private LogDao logDao;
	
	private Logger log = LoggerFactory.getLogger(DepartmentManagerImpl.class);
	
	public Map<String, Object> findDepartmentForMap(Map filter,Department department) throws Exception {
		
		return departmentDao.findDepartmentForMap(filter,department);
	}

	public List getPrefecture(Department department) throws Exception {
		
		return departmentDao.getPrefecture(department);
	}

	public Department getDepartment(String glbm) throws Exception {
		
		return departmentDao.getDepartment(glbm);
	}
    
	public int saveDepartment(Department department) throws Exception {
		return departmentDao.saveDepartment(department);
	}

	public int getUserCountForDepartment(String glbm) throws Exception {
		
		return departmentDao.getUserCountForDepartment(glbm);
	}

	public int removeDepartment(String glbm) throws Exception {
		
		return departmentDao.removeDepartment(glbm);
	}

	@ManagerFilterAnnotation(method = "addDepartment", clazz = Department.class)
	public void saveUnit(Department department, String modul) throws Exception {
		saveDepartment(department);
		if("new".equals(modul)){
			//根据部门代码插入下级辖区表
			prefectureManager.loadPrefecture(department.getGlbm()); 
			
			//设置上级部门到辖区表
			if(!StringUtils.isNullBlank(department.getSjbm())){
				Prefecture p = new Prefecture();
				p.setDwdm(department.getSjbm());
				p.setXjjg(department.getGlbm());
				p.setJglx("0");
				p.setJgqz("1");
				prefectureManager.insertPrefecture(p);
			}
		}
	}

	public String getDepartmentName(String glbm) throws Exception {
		Department d = this.departmentDao.getDepartment(glbm);
		if (d == null) {
			return glbm;
		}
		return d.getBmmc();
	}
	
    @OperationAnnotation(type=OperationType.POLICE_DEPARTMENT_SYNC,description="警综部门同步")
	public int SyncDepartment() throws Exception {
		return this.departmentDao.SyncDepartment();
	}
    
	@Override
	public List<Department> getKcBmmc(String userId) throws Exception {
		return departmentDao.getKcBmmc(userId);
	}
}
