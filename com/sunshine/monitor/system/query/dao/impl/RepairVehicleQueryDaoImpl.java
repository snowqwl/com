package com.sunshine.monitor.system.query.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.query.dao.RepairVehicleQueryDao;

@Repository("repairVehicleQueryDao")
public class RepairVehicleQueryDaoImpl extends BaseDaoImpl implements RepairVehicleQueryDao {
	
	private Logger logger=LoggerFactory.getLogger(RepairVehicleQueryDaoImpl.class);

	@Override
	public Map<String, Object> getList(Map<String, Object> params) {
		List param = new ArrayList<>();
		StringBuffer sb = new StringBuffer();
		sb.append("select w.djcode,w.dwcode,w.jstime,w.clph,w.sxrxm,s.syr,q.dwname,q.xian_gongan_name from t_ct_cl_wxxx w,t_ct_cl_wxxx_qyxx q,SYN_HN_JDC s"
				+ " where 1=1 and w.dwcode=q.dwcode and w.hpzl=s.hpzl and w.clph=s.hphm");
		
		if(StringUtils.isNotBlank((String)params.get("clph"))) { //号牌号码
			sb.append(" and w.clph = ?");
			param.add((String)params.get("clph"));
		}
		if(StringUtils.isNotBlank((String)params.get("kssj"))) { //开始时间
			sb.append(" and to_char(cast(cast(w.jstime as timestamp) as date),'yyyy-mm-dd " +
					"hh24:mi:ss')>?");
			param.add((String)params.get("kssj"));
		}
		if(StringUtils.isNotBlank((String)params.get("jssj"))) { //结束时间
			sb.append(" and to_char(cast(cast(w.jstime as timestamp) as date),'yyyy-mm-dd " +
					"hh24:mi:ss')<?");
			param.add((String)params.get("jssj"));
		}
		if(StringUtils.isNotBlank((String)params.get("hpzl"))) { //号牌种类
			sb.append(" and w.hpzl = ?");
			param.add((String)params.get("hpzl"));
		}
		if(StringUtils.isNotBlank((String)params.get("citys"))) { //所属地市
			String citys=(String)params.get("citys");
			if(citys.endsWith("0000") && citys.length()<7) {
				sb.append(" and substr(q.xian_gongan_code,0,2) = ?");
				param.add(citys.substring(0, 2));
			}else if(citys.endsWith("00")) {
				sb.append(" and substr(q.xian_gongan_code,0,4) = ?");
				param.add(citys.substring(0, 4));
			}else {
				sb.append(" and substr(q.xian_gongan_code,0,6) = ?");
				param.add((String)params.get("citys"));
			}
		}
		if(StringUtils.isNotBlank((String)params.get("dwname"))) { //维修企业
			sb.append(" and q.dwname like ?");
			param.add("%"+(String)params.get("dwname")+"%");
		}
		if(StringUtils.isNotBlank((String)params.get("syr"))) { //车主姓名
			sb.append(" and s.syr = ?");
			param.add((String)params.get("syr"));
		}
		if(StringUtils.isNotBlank((String)params.get("sfzmhm"))) { //车主身份证
			sb.append(" and s.sfzmhm = ?");
			param.add((String)params.get("sfzmhm"));
		}
		logger.info("执行机修车列表查询："+sb.toString());
		int curPage=(int) params.get("curPage");
		int pageSize=(int) params.get("pageSize");
		Map<String, Object> map = this.findPageForMap(sb.toString(),param.toArray(),curPage,
				pageSize);
		return map;
	}

	@Override
	public Map<String, Object> getDetail(Map<String, Object> params) {
		Map<String, Object> resultMap=null;
		StringBuffer sb = new StringBuffer();
		List param = new ArrayList<>();
		sb.append("select w.djcode,w.hpzl,w.clph,w.CLFDJH,w.CLCJH,w.YWZLMS,w.SXTIME,w.SXRXM,w.SXRDH,w.SXRZJH,"
				+ "q.dwname,w.SCYXM,"
				+ "w.qctime,w.QCRXM,w.QCRDH,w.QCRZJH,q.DWDZ,q.FZR,q.FZRDH,q.JJXZ,q.DWDJ,q.DWZZBH,"
				+ "q.XIAN_GONGAN_NAME,q.GONGAN_NAME from t_ct_cl_wxxx w,t_ct_cl_wxxx_qyxx q,SYN_HN_JDC s"
				+ " where 1=1 and w.dwcode=q.dwcode and w.hpzl=s.hpzl and w.clph=s.hphm");
		sb.append(" and w.djcode=?");
		param.add((String)params.get("djcode"));
		logger.info("执行机修车详情查询："+sb.toString());
		resultMap=this.getJdbcTemplate().queryForMap(sb.toString(),param.toArray());
		return resultMap;
	}

	@Override
	public Map<String, Object> getJdcDetail(Map<String, Object> params) {
		Map<String, Object> resultMap=null;
		StringBuffer sb = new StringBuffer();
		List param = new ArrayList<>();
		sb.append("select t.hpzl,t.hphm,t.CLSBDH,t.CLPP1,t.FDJH,t.CLLX,t.syr,t.SFZMMC,t.SFZMHM,t.CSYS,t.SYXZ,t.CCDJRQ"
				+ " from syn_hn_jdc t where 1=1");
		sb.append(" and t.hpzl=? and t.hphm=?");
		param.add((String)params.get("hpzl"));
		param.add((String)params.get("hphm"));
		logger.info("执行机动车详情查询："+sb.toString());
		resultMap=this.getJdbcTemplate().queryForMap(sb.toString(),param.toArray());
		return resultMap;
	}

}
