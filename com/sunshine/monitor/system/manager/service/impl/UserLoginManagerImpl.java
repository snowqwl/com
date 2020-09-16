package com.sunshine.monitor.system.manager.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.manager.bean.UserLoginXls;
import com.sunshine.monitor.system.manager.dao.UserLoginDao;
import com.sunshine.monitor.system.manager.service.UserLoginManager;
@Transactional
@Service("userLoginManager")
public class UserLoginManagerImpl implements UserLoginManager {
	
	@Autowired
	@Qualifier("userLoginDao")
	private UserLoginDao userLoginDao;

	public Map<String, Object> queryList(Map<String, Object> conditions)
			throws Exception {
		return this.userLoginDao.queryList(conditions);
	}
	
	public Map<String, Object> queryForDetail(Map<String, Object> conditions)
			throws Exception {		
		Map<String, Object>  map =   this.userLoginDao.queryForDetail(conditions);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String,Object>> list = (List<Map<String,Object>>)map.get("rows");
		if(list.size()>0){
		for(int i=0;i<list.size();i++){
			Map temp = (Map)list.get(i);
			if(temp.get("czsj")!=null)
			temp.put("CZSJ",sdf.format(temp.get("czsj")));
			}
		}
		return map;
	}
	
	public List<UserLoginXls> getUserLoginList(UserLoginXls info)throws Exception{
		StringBuffer sb = new StringBuffer("  1=1 ");		
		if(StringUtils.isNotBlank(info.getJh())){
			sb.append(" and jh = '").append(info.getJh()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getGlbm())){
			sb.append("  and  glbm ='").append(info.getGlbm()).append("' ");
		}
		
		
		
		if(StringUtils.isNotBlank(info.getYhdh())){
			sb.append(" and yhdh = '").append(info.getYhdh()).append("' ");
		}
		return this.userLoginDao.getUserLoginList(sb);
	}
}
