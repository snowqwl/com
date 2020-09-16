package com.sunshine.monitor.system.analysis.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.lf5.PassingLogRecordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.analysis.bean.HideDayOutNightDayBean;
import com.sunshine.monitor.system.analysis.clickhouse.bean.ClickHouseConstant;
import com.sunshine.monitor.system.analysis.clickhouse.repository.ClickHouseRepos;
import com.sunshine.monitor.system.analysis.dao.HideDayOutNightSCSDao;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.service.HideDayOutNightManager;
import com.sunshine.monitor.system.gate.dao.DirectDao;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Service("dayNightManager")
@Transactional
public class HideDayOutNightManagerImpl implements HideDayOutNightManager{
	@Autowired
	@Qualifier("dayNightSCSDao")
	private HideDayOutNightSCSDao dayNightSCSDao;
	
	@Autowired
	@Qualifier("scsVehPassrecDao")
	private ScsVehPassrecDao scsVehPassrecDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;
	
	@Autowired
	@Qualifier("directDao")
	private DirectDao directDao;
	
	private static String pass_view = "veh_passrec"; // 过车视图
	
	@Autowired
	@Qualifier("clickHouseRepos")
	private ClickHouseRepos clickHouseRepos;

	@Override
	public List<Map<String, Object>> findDayNightForPageExt(Map<String, Object> filter, HideDayOutNightDayBean bean)
			throws Exception {
		//List<Map<String,Object>> list=this.dayNightSCSDao.getDayNightListExt(filter, bean);
		List<Map<String,Object>> list = this.clickHouseRepos.getDayNightListExt(filter, bean);
		for(int i=0;i<list.size();i++){
			Map<String,Object> map=(Map<String,Object>)list.get(i);
			map.put("hpzlmc",this.systemDao.getCodeValue("030107",map.get("hpzl")==null?"":map.get("hpzl").toString()));
			map.put("hpysmc",this.systemDao.getCodeValue("031001",map.get("hpys")==null?"":map.get("hpys").toString()));
		}
		return list;		 
	}

	@Override
	public int getForLinkTotal(Map<String, Object> filter,HideDayOutNightDayBean bean) throws Exception {
		try{
			//return dayNightSCSDao.getDayNightTotal(filter, bean);
			return clickHouseRepos.getDayNightTotal(filter, bean);
		} catch(Exception e){
			return 0;
		}
	}
	
	@Override
	public List<Map<String, Object>> queryGjPassrecListExt(VehPassrec veh,
			Map<String, Object> filter) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 拼接sql与参数预编译
		List<String> params = new ArrayList<String>();
		StringBuffer sbsql = new StringBuffer("select hphm, hpzl, hpys, tp1, gcsj, kkbh as kdbh, fxlx as fxbh, concat(kkbh,'|',fxlx,'|',hphm,'|',hpzl,'|',toString(gcsj)) as gcxh from ");
		sbsql.append(ClickHouseConstant.PASS_TABLE_NAME);
		sbsql.append(" where 1 = 1 ");
		if(veh.getKssj()!=null && !"".equals(veh.getKssj().toString())){
			sbsql.append(" and gcsj >= ? ");
			params.add(veh.getKssj().toString().trim() + " 00:00:00");
		}
		if(veh.getJssj()!=null && !"".equals(veh.getJssj().toString())){
			sbsql.append(" and gcsj <= ? ");
			params.add(veh.getJssj().toString().trim() + " 23:59:59");
		}
		if(veh.getHphm()!=null && !"".equals(veh.getHphm().toString())){
			sbsql.append(" and hphm = ? ");
			params.add(veh.getHphm().toString());
		}
		if(veh.getHpzl()!=null && !"".equals(veh.getHpzl().toString())){
			sbsql.append(" and hpzl = ? ");
			params.add(veh.getHpzl().toString());
		}
		if(veh.getHpys()!=null && !"".equals(veh.getHpys().toString())){
			sbsql.append(" and hpys = ? ");
			params.add(veh.getHpys().toString());
		}
		sbsql.append(" order by gcsj desc ");
		if(filter.get("rows")!=null && !"".equals(filter.get("rows").toString())){
			if(filter.get("page")!=null && !"".equals(filter.get("page").toString())){
				int page = Integer.parseInt(filter.get("page").toString());
				int rows = Integer.parseInt(filter.get("rows").toString());
				int offset = (page-1)*rows;
				sbsql.append(" limit " + rows);
				sbsql.append(" offset " + offset);
			}
		}
		
		// 查询结果
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			//list = this.scsVehPassrecDao.queryGjPassrecListExt(sbsql.toString(), params.toArray());
			list = this.clickHouseRepos.queryGjPassrecListExt(sbsql.toString(), params.toArray());
			for(int i=0;i<list.size();i++){
				Map<String,Object> map=(Map<String,Object>)list.get(i);
				map.put("hpysmc",this.systemDao.getCodeValue("031001",map.get("hpys")==null?"":map.get("hpys").toString()));
				map.put("kdmc", this.gateDao.getOldGateName(map.get("kdbh").toString()));
				map.put("fxmc", this.directDao.getOldDirectName(map.get("fxbh")==null?"":map.get("fxbh").toString()));
				map.put("xzqhmc", this.systemDao.getDistrictNameByXzqh(map.get("kdbh").toString().substring(0, 6)));
				map.put("gcsj", map.get("gcsj")==null?"":sdf.format(map.get("gcsj")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Integer queryGjPassrecListTotal(VehPassrec veh,
			Map<String, Object> filter) {
		// 拼接sql与参数预编译
		List<String> params = new ArrayList<String>();
		StringBuffer sbsql = new StringBuffer("select count(1) from ");
		sbsql.append(ClickHouseConstant.PASS_TABLE_NAME);
		sbsql.append(" where 1 = 1 ");
		if(veh.getKssj()!=null && !"".equals(veh.getKssj().toString())){
			sbsql.append(" and gcsj >= ? ");
			params.add(veh.getKssj().toString().trim() + " 00:00:00");
		}
		if(veh.getJssj()!=null && !"".equals(veh.getJssj().toString())){
			sbsql.append(" and gcsj <= ? ");
			params.add(veh.getJssj().toString().trim() + " 23:59:59");
		}
		if(veh.getHphm()!=null && !"".equals(veh.getHphm().toString())){
			sbsql.append(" and hphm = ? ");
			params.add(veh.getHphm().toString());
		}
		if(veh.getHpzl()!=null && !"".equals(veh.getHpzl().toString())){
			sbsql.append(" and hpzl = ? ");
			params.add(veh.getHpzl().toString());
		}
		if(veh.getHpys()!=null && !"".equals(veh.getHpys().toString())){
			sbsql.append(" and hpys = ? ");
			params.add(veh.getHpys().toString());
		}
		
		// 查询结果
		Integer total = 0;
		try {
			//total = this.scsVehPassrecDao.queryGjPassrecListTotal(sbsql.toString(), params.toArray());
			total = this.clickHouseRepos.queryGjPassrecListTotal(sbsql.toString(), params.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}
	
	
	
	public List<Map<String, Object>> queryHideDayHotView(VehPassrec veh)throws Exception {
		// 拼接sql与参数预编译
		StringBuffer sbsql = new StringBuffer("select toDate(gcsj) as rowid,toHour(gcsj) as columnid,count(1) as gccs,case when toHour(gcsj) >= 6 and toHour(gcsj) < 18 then -1 when toHour(gcsj) >= 18 and toHour(gcsj) < 23 then 0 else 2 end as value,'' as tllabel,'' as trlabel from "+ClickHouseConstant.PASS_TABLE_NAME+" where 1=1 ");
		if(veh.getHphm()!=null && !"".equals(veh.getHphm().toString())){
			String hphm = veh.getHphm();
			sbsql.append(" and hphm = '"+hphm+"' ");
		}
		if(veh.getHpzl()!=null && !"".equals(veh.getHpzl().toString())){
			sbsql.append(" and hpzl = '"+veh.getHpzl()+"' ");
		}
		if(veh.getHpys()!=null && !"".equals(veh.getHpys().toString())){
			sbsql.append(" and hpys = '"+veh.getHpys()+"' ");
		}
		if(veh.getKssj()!=null && !"".equals(veh.getKssj().toString())){
			sbsql.append(" and gcsj >= '"+veh.getKssj().trim()+" 00:00:00' ");
		}
		if(veh.getJssj()!=null && !"".equals(veh.getJssj().toString())){
			sbsql.append(" and gcsj <= '"+veh.getJssj().trim()+" 23:59:59' group by toDate(gcsj),toHour(gcsj) order by rowid desc,columnid");
		}
		// 查询结果
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listhour = new ArrayList<Map<String, Object>>();
		Map<String,Object> result = null;
		try {
			//list = this.scsVehPassrecDao.queryHideDayHotView(sbsql.toString());
			list = this.clickHouseRepos.findListBySql(sbsql.toString());
			/*Map<String,Object> map=(Map<String,Object>)list.get(0);
			String datestr = map.get("rowid").toString();
			String hourSql = gethoutsql(datestr,veh);
			listhour =  this.scsVehPassrecDao.queryHideDayHotView(hourSql.toString());;*/
			for(int i=23;i>=0;i--){
				/*boolean isFlag = true;
				for(int j=0;j<listhour.size();j++){
					Map<String,Object> hourmap=(Map<String,Object>)listhour.get(j);
					int hour = Integer.parseInt(hourmap.get("columnid").toString());
					if(i==hour){
						isFlag=false;
						break;
					}
				}
				if(isFlag){*/
					result = new HashMap<String, Object>();
					result.put("columnid", i);
					result.put("value", "");
					result.put("tllabel", "");
					result.put("trlabel", "");
					result.put("rowid", "");
					list.add(result);
					
				/*}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Map<String, Object>> queryCarCount(VehPassrec veh) throws Exception{
		// 拼接sql与参数预编译
		StringBuffer sbsql = new StringBuffer("select COUNT (1) cs,toHour(gcsj) AS hours,toDate(gcsj) AS days from " + ClickHouseConstant.PASS_TABLE_NAME + " where 1=1 ");
		if(veh.getHphm()!=null && !"".equals(veh.getHphm().toString())){
			String hphm = veh.getHphm();
			sbsql.append(" and hphm = '"+hphm+"' ");
		}
		if(veh.getHpzl()!=null && !"".equals(veh.getHpzl().toString())){
			sbsql.append(" and hpzl = '"+veh.getHpzl()+"' ");
		}
		if(veh.getHpys()!=null && !"".equals(veh.getHpys().toString())){
			sbsql.append(" and hpys = '"+veh.getHpys()+"' ");
		}
		if(veh.getKssj()!=null && !"".equals(veh.getKssj().toString())){
			sbsql.append(" and gcsj >= '"+veh.getKssj().trim()+" 00:00:00' ");
		}
		if(veh.getJssj()!=null && !"".equals(veh.getJssj().toString())){
			sbsql.append(" and gcsj <= '"+veh.getJssj().trim()+" 23:59:59' GROUP BY toDate(gcsj),toHour(gcsj)");
		}
		// 查询结果
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			//list = this.scsVehPassrecDao.findList(sbsql.toString()); // key为大写
			list = this.clickHouseRepos.findListBySql(sbsql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	private String gethoutsql(String datestr, VehPassrec veh) throws UnsupportedEncodingException {
		StringBuffer sbsql = new StringBuffer("select lhour as columnid from yp_passrec_zfyc_h where 1=1 ");
		if(datestr!=null && !"".equals(datestr)){
			sbsql.append(" and ldate = '"+datestr+"' ");
		}
		if(veh.getHphm()!=null && !"".equals(veh.getHphm().toString())){
			String hphm = veh.getHphm();
			//hphm = new String(hphm.getBytes("ISO-8859-1"),"utf-8");
			hphm= URLDecoder.decode(hphm,"utf-8");
			sbsql.append(" and hphm = '"+hphm+"' ");
		}
		if(veh.getHpzl()!=null && !"".equals(veh.getHpzl().toString())){
			sbsql.append(" and hpzl = '"+veh.getHpzl()+"' ");
		}
		if(veh.getHpys()!=null && !"".equals(veh.getHpys().toString())){
			sbsql.append(" and hpys = '"+veh.getHpys()+"' ");
		}
		if(veh.getKssj()!=null && !"".equals(veh.getKssj().toString())){
			sbsql.append(" and ldate >= '"+veh.getKssj()+"' ");
		}
		if(veh.getJssj()!=null && !"".equals(veh.getJssj().toString())){
			sbsql.append(" and ldate <= '"+veh.getJssj()+"' order by lhour desc ");
		}
		return sbsql.toString();
	}
}
