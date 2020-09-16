package com.sunshine.monitor.system.query.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.query.dao.CollectVehDao;
import com.sunshine.monitor.system.query.service.CollectVehManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.ws.PassrecService.bean.PassrecEntity;


@Service
@Transactional
public class CollectVehManagerImpl implements CollectVehManager {
	@Autowired
	private CollectVehDao collectVehDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Override
	@OperationAnnotation(type=OperationType.COLLECTVEH_QUERY,description="关注车辆查询")
	public Map<String, Object> getCollectVehMapForFilter(Page page,VehSuspinfo info,String conSql)
			throws Exception {
		return collectVehDao.getCollectVehList(page, info, conSql);
	}

	@Override
	public int saveCollectVeh(SysUser user,VehSuspinfo info) throws Exception {
		return collectVehDao.insert(user, info);
	}

	@Override
	public int editCollectVeh(String hphm, String hpzl, String status)
			throws Exception {
		return collectVehDao.update(hphm,hpzl,status);
	}

	@Override
	public int checkMaxCount(String jh) throws Exception {
		return collectVehDao.checkMaxCount(jh);
	}

	@Override
	public List<VehPassrec> getCollectVehForMainPage(String conSql) 
			throws Exception {
		List<VehPassrec> list = collectVehDao.queryCollectVehList(conSql);
		for(int i = 0;i<list.size();i++){
			final VehPassrec veh = list.get(i);
			veh.setHpzlmc(this.systemDao.getCodeValue("030107",veh.getHpzl()));
			
			//对已读的进行操作 查询是否有最新的过车时间，如果有，则修改状态为未读
			//
			new Thread(){
				@Override
				public void run() {
					try {
						collectVehDao.checkLastGcsj(veh.getHphm(), veh.getHpzl(), veh.getGcsj());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();				
		}
		return list; 
	}

	@Override
	public int editCollectVehIsRead(String hphm, String hpzl, String isread)
			throws Exception {
		return collectVehDao.updateIsRead(hphm, hpzl, isread);
	}

	@Override
	public int checkHphm(String jh,String hphm, String hpzl) throws Exception {
		return collectVehDao.checkByHphm(jh,hphm, hpzl);
	}

	@Override
	public VehSuspinfo findVehCollect(String jh, String hphm, String hpzl)
			throws Exception {
		return collectVehDao.getVehCollect(jh, hphm, hpzl);
	}

	@Override
	public int update(SysUser user, VehSuspinfo info) throws Exception {
		return collectVehDao.edit(user, info);
	}
	
	
}
