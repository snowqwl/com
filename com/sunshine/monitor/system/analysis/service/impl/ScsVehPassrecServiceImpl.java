package com.sunshine.monitor.system.analysis.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.comm.util.orm.helper.Pager;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.clickhouse.repository.ClickHouseRepos;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.service.ScsVehPassrecService;
import com.sunshine.monitor.system.manager.dao.SystemDao;

@Service("scsVehPassrecService")
public class ScsVehPassrecServiceImpl implements ScsVehPassrecService {
	@Autowired
	@Qualifier("scsVehPassrecDao")
	private ScsVehPassrecDao scsVehPassrecDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("clickHouseRepos")
	private ClickHouseRepos clickHouseRepos;
	
	public List<ScsVehPassrec> findList(Cnd cnd, Pager pager) throws Exception {
		return scsVehPassrecDao.query(Constant.SCS_PASS_TABLE, ScsVehPassrec.class, cnd, pager);
	}
	
	public Map<String,Object> getVehDetail(String gcxh)throws Exception{
		Map<String,Object> veh = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//List<Map<String,Object>> list= scsVehPassrecDao.findList("select gcxh,kdbh,fxbh,cdbh,cdlx,hpzl,gcsj,clsd,hpys,cllx,hphm,cwhphm,cwhpys,hpyz,csys,clxs,clpp,xszt,tp1,tp2,tp3,rksj  from veh_passrec t where gcxh='"+gcxh+"'");
		List<Map<String,Object>> list= clickHouseRepos.findList(gcxh);
		if(list.size()>0){
		  veh=list.get(0);
		  veh.put("GCSJ", sdf.format(veh.get("GCSJ")));
		  veh.put("RKSJ", sdf.format(veh.get("RKSJ")));
		  veh.put("HPYSMC",(veh.get("HPYS")!=null)?this.systemDao.getCodeValue("031001",veh.get("HPYS").toString()):"");
		  veh.put("HPZLMC",(veh.get("HPZL")!=null)?this.systemDao.getCodeValue("030107",veh.get("HPZL").toString()):"");
		  veh.put("CLLXMC",(veh.get("CLLX")!=null)?this.systemDao.getCodeValue("030104",veh.get("CLLX").toString()):"");
		  veh.put("CSYSMC",(veh.get("CSYS")!=null)?this.systemDao.getCodeValue("030108",veh.get("CSYS").toString()):"");
		  veh.put("XSZTMC",(veh.get("XSZT")!=null)?this.systemDao.getCodeValue("110005",veh.get("XSZT").toString()):"");
		  veh.put("KDMC",  (veh.get("KDBH")!=null)?this.systemDao.getField("code_Gate","kdbh","kdmc",veh.get("KDBH").toString()):"");
		  veh.put("FXMC",  (veh.get("FXBH")!=null)?this.systemDao.getField("code_gate_extend","fxbh","fxmc",veh.get("FXBH").toString()):"");	
		}
		return veh;
	}
	
}
