package com.sunshine.monitor.system.analysis.zyk.service.imp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.util.DateUtils;
import com.sunshine.monitor.comm.util.excel.OutputExcelUtils;
import com.sunshine.monitor.system.analysis.zyk.bean.PoliceSituation;
import com.sunshine.monitor.system.analysis.zyk.dao.PoliceSituationDao;
import com.sunshine.monitor.system.analysis.zyk.service.PoliceSituationManager;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.dao.SystemDao;

@Service("policeSituationManager")
public class PoliceSituationManagerImpl implements PoliceSituationManager {

	@Autowired
	private PoliceSituationDao policeSituationDao;
	@Autowired
	private SystemDao systemDao;
	
	
	private Logger log = LoggerFactory.getLogger(PoliceSituationManagerImpl.class);
	private String table_name = "JM_ZTK_POLICE_SITUATION";
	
	public String getSql(PoliceSituation bean,Map<String, Object> filter){
		StringBuffer sql = new StringBuffer("select id,jqh,gabh,ajbh,jqmc,hphm,hpzl,lrr,lrrmc,lrsj,description,tplj,xxly,xzqh from "+table_name+" where 1 = 1");
		if(bean.getHphm()!=null && !"".equals(bean.getHphm())){
			sql.append(" and hphm = '").append(bean.getHphm()).append("'");
		}
		if(bean.getHpzl()!=null && !"".equals(bean.getHpzl())){
			sql.append(" and hpzl = '").append(bean.getHpzl()).append("'");
		}
		if(bean.getKssj()!=null && !"".equals(bean.getKssj())){
			sql.append(" and lrsj >= to_date('").append(bean.getKssj()).append("','yyyy-mm-dd hh24:mi:ss')");
		}
		if(bean.getJssj()!=null && !"".equals(bean.getJssj())){
			sql.append(" and lrsj <= to_date('").append(bean.getJssj()).append("','yyyy-mm-dd hh24:mi:ss')");
		}
		if(bean.getLrr()!=null && !"".equals(bean.getLrr())){
			sql.append(" and lrr = '").append(bean.getLrr()).append("'");
		}
		sql.append(" order by lrsj desc");
		return sql.toString();
	}
	
	@Override
	public Map<String, Object> queryList(PoliceSituation bean,Map<String, Object> filter){
		Map<String, Object> result = new HashMap<String, Object>();
		String sql = getSql(bean, filter);
		try {			
		    try {
		    	long start = System.currentTimeMillis();
				result=this.policeSituationDao.findPageForMap(sql,Integer.parseInt(filter.get("page").toString()), 
						Integer.parseInt(filter.get("rows").toString()),PoliceSituation.class);
				long end = System.currentTimeMillis();
				log.info("查询警情资源库耗时："+(end - start)+"MS");
			} catch (Exception e) {
				log.info("查询警情资源库ERROR: "+sql.toString());
				e.printStackTrace();
			}		
			
			List<PoliceSituation> list=(List<PoliceSituation>) result.get("rows");
			if(list!=null && list.size()>0){
				for(PoliceSituation entity : list){				
					entity.setHpzlmc(this.systemDao.getCodeValue("030107",entity.getHpzl()==null?"":entity.getHpzl()));
				}
			}
			result.put("rows",list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public int save(PoliceSituation bean,SysUser user) throws Exception {
		String sql = "INSERT into "+table_name+"(id,jqh,gabh,ajbh,jqmc,hphm,hpzl,description,tplj,xxly,xzqh,by1,by2,by3,lrr,lrrmc,lrsj)" +
				" values(SEQ_JM_ZTK_POLICE_SITUATION.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";
		List<Object> params = new ArrayList<Object>();
		params.add(bean.getJqh()==null?"":bean.getJqh());
		params.add(bean.getGabh()==null?"":bean.getGabh());
		params.add(bean.getAjbh()==null?"":bean.getAjbh());
		params.add(bean.getJqmc()==null?"":bean.getJqmc());
		params.add(bean.getHphm()==null?"":bean.getHphm());
		params.add(bean.getHpzl()==null?"":bean.getHpzl());
		params.add(bean.getDescription()==null?"":bean.getDescription());
		params.add(bean.getTplj()==null?"":bean.getTplj());
		params.add(bean.getXxly()==null?"":bean.getXxly());
		params.add(bean.getXzqh()==null?"":bean.getXzqh());
		params.add(bean.getBy1()==null?"":bean.getBy1());
		params.add(bean.getBy2()==null?"":bean.getBy2());
		params.add(bean.getBy3()==null?"":bean.getBy3());
		params.add(user.getYhdh());
		params.add(user.getYhmc());
		int c = 0;
		try {
			c = this.policeSituationDao.update(sql, params);
		} catch (Exception e) {
			log.info("新增警情资源库ERROR: "+sql);
			throw new SQLException(e.getMessage());
		}
		return c;
	}
	@Override
	public int update(PoliceSituation bean) throws Exception{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(" update "+ table_name +" set ");
		if (StringUtils.isNotBlank(bean.getJqh())){
			sb.append("jqh=?,");
			params.add(bean.getJqh());
		}
		if (StringUtils.isNotBlank(bean.getGabh())){
			sb.append("gabh=?,");
			params.add(bean.getGabh());
		}
		if (StringUtils.isNotBlank(bean.getAjbh())){
			sb.append("ajbh=?,");
			params.add(bean.getAjbh());
		}
		if (StringUtils.isNotBlank(bean.getJqmc())){
			sb.append("jqmc=?,");
			params.add(bean.getJqmc());
		}
		if (StringUtils.isNotBlank(bean.getHphm())){
			sb.append("hphm=?,");
			params.add(bean.getHphm());
		}
		if (StringUtils.isNotBlank(bean.getHpzl())){
			sb.append("hpzl=?,");
			params.add(bean.getHpzl());
		}
		if (StringUtils.isNotBlank(bean.getDescription())){
			sb.append("description=?,");
			params.add(bean.getDescription());
		}
		if (StringUtils.isNotBlank(bean.getTplj())){
			sb.append("tplj=?,");
			params.add(bean.getTplj());
		}
		if (StringUtils.isNotBlank(bean.getXxly())){
			sb.append("xxly=?,");
			params.add(bean.getXxly());
		}
		if (StringUtils.isNotBlank(bean.getXzqh())){
			sb.append("xzqh=?,");
			params.add(bean.getXzqh());
		}
		if (StringUtils.isNotBlank(bean.getBy1())){
			sb.append("by1=?,");
			params.add(bean.getBy1());
		}
		if (StringUtils.isNotBlank(bean.getBy2())){
			sb.append("by2=?,");
			params.add(bean.getBy2());
		}
		if (StringUtils.isNotBlank(bean.getBy3())){
			sb.append("by3=?,");
			params.add(bean.getBy3());
		}
		String sql = sb.substring(0,sb.length()-1);
		sql = sql+" where id = ? ";
		params.add(bean.getId());
		log.info("update警情资源库: "+sql);
		int c = 0;
		try {
			c = this.policeSituationDao.update(sql, params);
		} catch (Exception e) {
			log.info("修改警情资源库ERROR: "+sql.toString());
			throw new SQLException(e.getMessage());
		}
		return c;
	}
	@Override
	public PoliceSituation queryPoliceSituationById(String id) throws Exception {
		PoliceSituation bean = new PoliceSituation();
		String sql = "select id,jqh,gabh,ajbh,jqmc,hphm,hpzl,lrr,lrsj,description,tplj,xxly,xzqh from "+table_name+" where id="+id;
		bean = this.policeSituationDao.queryObject(sql, PoliceSituation.class);
		return bean;
	}
	@Override
	public int delete(String id) throws Exception{
		List<Object> params = new ArrayList<Object>();
		String sql = "delete from "+table_name+" where id=?";
		params.add(id);
		int i = this.policeSituationDao.update(sql,params);
		return i;
	}
	
	public List<PoliceSituation> queryPoliceSituationForExcel(PoliceSituation bean,Map<String, Object> filter){
		List<PoliceSituation> list = new ArrayList<PoliceSituation>();
//		Map<String, Object> sqlMap = getSql(bean, filter);		
		String sql = getSql(bean, filter);
//		List<Object> params = (List<Object>) sqlMap.get("params");
	    try {
	    	long start = System.currentTimeMillis();
	    	list = this.policeSituationDao.queryList(sql, PoliceSituation.class);
			long end = System.currentTimeMillis();
			log.info("查询警情资源库耗时："+(end - start)+"MS");
		} catch (Exception e) {
			log.info("查询警情资源库ERROR: "+sql.toString());
			e.printStackTrace();
		}		
		return list;
	}
	
	@Override
	public String outPutPoliceSituationList(Map<String, Object> filter,String path, PoliceSituation bean) {
		StringBuffer url = new StringBuffer();
		String filename = "";
		OutputStream output = null;		
		try {
			List<PoliceSituation> data = queryPoliceSituationForExcel(bean, filter);
			// data
			LinkedHashMap<String, List> sheets = new LinkedHashMap<String, List>();
			sheets.put("警情信息列表", data);
			// Heads
			List<String[]> heads = new ArrayList<String[]>();
			heads.add(new String[]{"警情号","警情名称","公安编号","案件编号","号牌号码","号牌种类","录入人","录入时间","信息来源",});
			// Fields
			List<String[]> fields = new ArrayList<String[]>();
			fields.add(new String[]{"jqh","jqmc","gabh","ajbh","hphm","hpzl","lrr","lrsj","xxly",});			
			
			filename = DateUtils.getCurrTimeStr(1)+".xls";
			String fullPath = path;
			url.append(fullPath).append(filename);
			
			File file = new File(url.toString());
			output = new FileOutputStream(file);  
			OutputExcelUtils.outPutExcel(heads, fields, sheets, new String[]{"警情信息"}, null, output);
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
}
