package com.sunshine.monitor.system.redlist.service.impl;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.redlist.bean.RedList;
import com.sunshine.monitor.system.redlist.dao.RedListDao;
import com.sunshine.monitor.system.redlist.service.RedListManager;

@Service
public class RedListManagerImpl implements RedListManager{
	
	@Autowired
	private RedListDao redListDao;
	
	@Autowired
	private SystemDao systemDao;

	@Transactional
	@OperationAnnotation(type=OperationType.RED_LIST_ADD, description="新增红名单")
	public int addRedList(RedList redlist) throws Exception {
		redlist.setStatus("0");
		return redListDao.addRedList(redlist);
	}
	
	@Transactional
	public RedList queryRedList(RedList redlist) throws Exception {
		//String subSql = " and hphm = '" + redlist.getHphm() + "'" ;
		String subSql = " and id = '" + redlist.getId() + "'" ;
		RedList vs = this.redListDao.queryRedList(subSql);
		if(vs != null) {
			if(vs.getHpzl() != null){
				vs.setHpzlmc(this.systemDao.getCodeValue("030107", vs.getHpzl()));
			}
			if ((vs.getCllx() != null) && (!"".equals(vs.getCllx()))) {
				vs.setCllxmc(this.systemDao.getCodeValue("030104", vs.getCllx()));
			}
			if ((vs.getHpys() != null) && (!"".equals(vs.getHpys()))) {
				vs.setHpysmc(this.systemDao.getCodeValue("031001", vs.getHpys()));
			}
			String _result = "";
			if ((vs.getCsys() != null) && (!"".equals(vs.getCsys()))) {
				String[] csys = vs.getCsys().split(",");
				for (int i = 0; i < csys.length; i++) {
					_result = _result
							+ this.systemDao.getCodeValue("030108", csys[i]) + ",";
				}
				if (_result.length() > 0) {
					vs.setCsysmc(_result.substring(0, _result.length() - 1));
				}
			}
		}
		return vs;
	}

	@Transactional
	@OperationAnnotation(type=OperationType.RED_LIST_BATCH_ADD, description="批量导入红名单")
	public int addRedListBatch(Set<RedList> set) throws Exception {
		int success = 0 ;
		for(RedList rl : set) {
			RedList temp = this.queryInvalidRedList(rl);
			if(temp != null) {
				// 优先更新
				if(this.updateinValidRedList(rl) >0 ) {
					success = success + 1;
					continue;
				}
			} else {
				RedList t2 = this.queryRedList(rl) ;
				if(t2 == null){ 
					int result = this.addRedList(rl);
					if(result > 0) {
						success = success + 1 ;
					}
				}
			}
		}
		return success;
	}
	
	public RedList queryRedList(String subSql) throws Exception {
		return this.redListDao.queryRedList(subSql);
	}
	
	@OperationAnnotation(type=OperationType.RED_LIST_QUERY, description="查询未审批红名单")
	public Map<String, Object> queryRedList(Map<String, Object> conditions)
			throws Exception {
		// 未审批
		conditions.put("status", "0");
		// 有效
		conditions.put("isvalid", "1");
		return this.redListDao.queryRedList(conditions);
	}

	public RedList queryInvalidRedList(RedList redlist) throws Exception {
		String subSql = " and hphm = '" + redlist.getHphm() + "' and status='0' and isvalid='0'" ;
		return this.redListDao.queryRedList(subSql);
	}

	@Transactional
	public int updateinValidRedList(RedList redlist) throws Exception {
		
		return this.redListDao.updateinValidRedList(redlist);
	}

	@OperationAnnotation(type=OperationType.RED_LIST_AUDIT, description="红名单审批")
	public int updateRedList(RedList redlist) throws Exception {
		String temp=redlist.getStatus();
		if(redlist.getStatus().equalsIgnoreCase("pass")){
			
			redlist.setStatus("1");
			redlist.setIsvalid("1");
		}
		else{
			redlist.setStatus("1");
			redlist.setIsvalid("0");
		}
		int result = this.redListDao.updateRedList(redlist);
		if(result == 1) {
			this.redListDao.addDatatoCache(redlist.getHphm());
		}
		return result;
	}
	
	@OperationAnnotation(type=OperationType.RED_LIST_AUIDTED_QUERY, description="查询审批通过红名单")
	public Map<String, Object> queryAuidtedRedList(Map<String, Object> conditions) throws Exception {
		// 审批
		conditions.put("status", "1");
		// 有效
		conditions.put("isvalid", "1");
		return this.redListDao.queryRedList(conditions);
	}

	@Transactional
	@OperationAnnotation(type=OperationType.RED_LIST_DEBLOCK, description="解封红名 单")
	public int deblockingRedList(RedList redlist) throws Exception {
		this.redListDao.deblockingRedList(redlist);
		return this.deleteRedList(redlist.getHphm(),redlist.getId());
	}

	@Transactional
	@OperationAnnotation(type=OperationType.RED_LIST_REMOVE, description="移出红名单库")
	public int deleteRedList(String hphm,String id) throws Exception {
		int result = this.redListDao.deleteRedList(id);
		if(result == 1) {
			this.redListDao.deleteCacheData(hphm);
		}
		return result;
	}
	
	public boolean filterRedList(String hphm) throws Exception {
		boolean flag = false ;
		String subSql = " and hphm='" + hphm +"' and status='1' and isvalid='1' and jssj > to_date('"+this.redListDao.getTime()+"','yyyy-mm-dd hh24:mi:ss')";
		RedList redList = this.redListDao.queryRedList(subSql);
		if(redList != null) {
			flag = true;
		}
		return flag;
	}

	public Map<String, Object> queryValidRedlist(Map<String, Object> conditions)
			throws Exception {
		String isvalid = (String) conditions.get("isvalid");
		String status = (String) conditions.get("status");
		if(StringUtils.isNotBlank(isvalid) && "0".equals(isvalid) && "0".equals(status)){
			conditions.put("status", "0");
			conditions.put("isvalid", "0");
		} else if("1".equals(status)&&"0".equals(isvalid)){
			conditions.put("status", "1");
			conditions.put("isvalid", "0");
		}else{
			conditions.put("status", "1");
			conditions.put("isvalid", "1");
		}
		return this.redListDao.queryValidRedlist(conditions);
	}
	
	/**
	 * 初始加载红名单
	 */
	public void initRedList(){
		this.redListDao.initRedList();
	}
	
	
	@Transactional
	public RedList existRedName(RedList redlist) throws Exception {
		String subSql = " and hphm = '" + redlist.getHphm() + "'  and isvalid='1' and jssj > to_date('"+this.redListDao.getTime()+"','yyyy-mm-dd hh24:mi:ss')";
		RedList red = this.redListDao.queryRedList(subSql);
		return red;
	}
	

}
