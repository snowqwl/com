package com.sunshine.monitor.system.query.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.service.UrlManager;
import com.sunshine.monitor.system.query.bean.SuspMonitor;
import com.sunshine.monitor.system.query.dao.QueryListDao;
import com.sunshine.monitor.system.query.service.SuspinfoQueryManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

@Service
@Transactional
public class SuspinfoQueryManagerImpl implements SuspinfoQueryManager {

	/**
	 * 布控表索引列表
	 */
	private static final String IDX_BKR="IDX_PK_VEH_SUSPINFO_BKR";
    private static final String IDX_BKJG="IDX_VEH_SUSPINFO_BKJG";
    private static final String IDX_BKSJ="IDX_VEH_SUSPINFO_BKSJ";
    private static final String IDX_HPHM="IDX_VEH_SUSPINFO_HPHM";
    private static final String IDX_BKXH = "IDX_VEH_SUSPINFO_BKXH";
	
	@Autowired
	private QueryListDao queryListDao;
	
	@Autowired
	private UrlManager urlManager;
	/**
	 * 查询布/管控信息
	 * @param filter
	 * @param info
	 * @param conSql
	 * @return
	 */
	public Map getSuspinfoMapForFilter(Map filter, VehSuspinfo info,String conSql)
			throws Exception {
		StringBuffer sb = new StringBuffer("  1=1 ");
		StringBuffer idxSql = new StringBuffer();
		Boolean flag = true;
		if(StringUtils.isNotBlank(conSql)){
			sb.append(conSql);
		}
		
		if(StringUtils.isNotBlank(info.getBkxh())){
			sb.append(" and bkxh = '").append(info.getBkxh()).append("' ");
			if(flag){
				idxSql.append(IDX_BKXH+" ");
				flag=false;
			}
		}
		
		if(StringUtils.isNotBlank(info.getHphm())){
			sb.append("  and  hphm  like '%").append(Common.changeHphm(info.getHphm())).append("%'");
			if(flag){
				idxSql.append(IDX_HPHM+" ");
				flag=false;
			}
		}
		
		if(info.getBkr()!= null && info.getBkr().length() > 0){
			sb.append("  and bkr ='").append(info.getBkr()).append("' ");
			if(flag){
				idxSql.append(IDX_BKR+" ");
				flag=false;
			}
		}
		
		if(StringUtils.isNotBlank(info.getBkjg())){
			sb.append(" and bkjg = '").append(info.getBkjg()).append("' ");
			if(flag){
				idxSql.append(IDX_BKJG+" ");
				flag=false;
			}
		}
		sb.append(" and bkjg in (select xjjg from frm_prefecture where dwdm = '").append(filter.get("glbm")).append("')");
		if(StringUtils.isNotBlank(info.getKssj()))
			sb.append(" and bksj >=to_date('").append(info.getKssj()).append("', 'yyyy-mm-dd hh24:mi:ss') ");
		
		if(StringUtils.isNotBlank(info.getJssj()))
			sb.append(" and bksj <=to_date('").append(info.getJssj()).append("', 'yyyy-mm-dd hh24:mi:ss') ");
		if(flag){
			idxSql.append(IDX_BKSJ+" ");
			flag=false;
		}
		
		if(StringUtils.isNotBlank(info.getBkdl())){
			sb.append(" and bkdl = '").append(info.getBkdl()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBklb())){
			sb.append("  and bklb = '").append(info.getBklb()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getYwzt())){
			sb.append(" and ywzt = '").append(info.getYwzt()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getXxly())){
			sb.append(" and xxly = '").append(info.getXxly()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getHpzl())){
			sb.append(" and hpzl = '").append(info.getHpzl()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBkjb())){
			sb.append(" and bkjb = '").append(info.getBkjb()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getJlzt())){
			sb.append(" and jlzt = '").append(info.getJlzt()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBjya())){
			sb.append(" and bjya = '").append(info.getBjya()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBjzt())){
			sb.append(" and bjzt = '").append(info.getBjzt()).append("' ");
		}
		
		if(filter.get("city")!=null){
			if(StringUtils.isNotBlank(filter.get("city").toString())){
				sb.append(" and bkjg like '").append(filter.get("city")).append("%' ");
			}
		}
		sb.append("  order by bksj desc ");
		
		filter.put("idx_sql","/*+index(veh_suspinfo "+idxSql.toString()+")+*/");
		return queryListDao.getMapForSuspinfoFilter(filter,  sb);
	}
	
	public Map getCitySuspinfoMapForFilter(Map filter, VehSuspinfo info, String cityname)throws Exception {
        StringBuffer sb = new StringBuffer("  1=1 ");
        
		StringBuffer idxSql = new StringBuffer();
		Boolean flag = true;
		
		if(StringUtils.isNotBlank(info.getBkxh())){
			sb.append(" and bkxh = '").append(info.getBkxh()).append("' ");
			if(flag){
				idxSql.append(IDX_BKXH+" ");
				flag=false;
			}
		}
		
		if(StringUtils.isNotBlank(info.getHphm())){
			sb.append("  and  hphm  like '%").append(Common.changeHphm(info.getHphm())).append("%'");
			if(flag){
				idxSql.append(IDX_HPHM+" ");
				flag=false;
			}
		}
		
		if(info.getBkr()!= null && info.getBkr().length() > 0){
			sb.append("  and bkr ='").append(info.getBkr()).append("' ");
			if(flag){
				idxSql.append(IDX_BKR+" ");
				flag=false;
			}
		}
		
		if(StringUtils.isNotBlank(info.getBkjg())){
			sb.append(" and bkjg = '").append(info.getBkjg()).append("' ");
			if(flag){
				idxSql.append(IDX_BKJG+" ");
				flag=false;
			}
		}
		
		if(StringUtils.isNotBlank(info.getKssj()))
			sb.append(" and bksj >=to_date('").append(info.getKssj()).append("', 'yyyy-mm-dd hh24:mi:ss') ");
		
		if(StringUtils.isNotBlank(info.getJssj()))
			sb.append(" and bksj <=to_date('").append(info.getJssj()).append("', 'yyyy-mm-dd hh24:mi:ss') ");
		if(flag){
			idxSql.append(IDX_BKSJ+" ");
			flag=false;
		}
		
		if(StringUtils.isNotBlank(info.getBkdl())){
			sb.append(" and bkdl = '").append(info.getBkdl()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBklb())){
			sb.append("  and bklb = '").append(info.getBklb()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getYwzt())){
			sb.append(" and ywzt = '").append(info.getYwzt()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getXxly())){
			sb.append(" and xxly = '").append(info.getXxly()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getHpzl())){
			sb.append(" and hpzl = '").append(info.getHpzl()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBkjb())){
			sb.append(" and bkjb = '").append(info.getBkjb()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getJlzt())){
			sb.append(" and jlzt = '").append(info.getJlzt()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBjya())){
			sb.append(" and bjya = '").append(info.getBjya()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBjzt())){
			sb.append(" and bjzt = '").append(info.getBjzt()).append("' ");
		}
		
		sb.append("  order by bksj desc ");
		
		filter.put("idx_sql","/*+index(veh_suspinfo "+idxSql.toString()+")+*/");
		return this.queryListDao.getMapForCitySuspinfoFilter(filter, sb, cityname);
	}
	
	/**
	 * 查询JM_SUSPINFO_MONITOR表信息
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public List<SuspMonitor> getSuspMonitorListForBkxh(String bkxh) throws Exception {
		List<SuspMonitor> list = queryListDao.getSuspMonitorListForBkxh(bkxh);
		List<CodeUrl> urlList = urlManager.getCodeUrls();
		
		for(Object o : list){
			SuspMonitor spMonitor = (SuspMonitor)o;
			for(CodeUrl cUrl : urlList){
				if(spMonitor.getDwdm().equals(cUrl.getDwdm()))
					spMonitor.setBmmc(cUrl.getJdmc());
			}
		}
		
		return list;
	}
	/**
	 * (根据参数)查询veh_suspinfo表信息
	 * @param filter
	 * @param hphm 号牌号码
	 * @param bkjg 布控申请机关
	 * @return
	 * @throws Exception
	 */
	public Map getSuspinfoMapForFilerByHphm(Map filter, String hphm,String bkjg)
			throws Exception {
		return this.queryListDao.getSuspinfoMapForFilerByHphm(filter, hphm, bkjg);
	}
	
	/**
	 * 查询省厅KK库中是否存在当前地市的dblink
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public int getAllDbLink(String cityname)throws Exception{
		return this.queryListDao.getAllDbLink(cityname);
	}
	/**
	 * 布撤控信息查询中增加布控审批人
	 */
	public String getBksprByBkxh(String bkxh) throws Exception{
		return this.queryListDao.getBksprByBkxh(bkxh);
	}
	
	public List<SuspMonitor> getSuspMonitorList(String bkxh, String bkjg)throws Exception{
		return this.queryListDao.getSuspMonitorList(bkxh, bkjg);
	}
}
