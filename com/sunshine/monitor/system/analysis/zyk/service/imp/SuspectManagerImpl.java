package com.sunshine.monitor.system.analysis.zyk.service.imp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.util.DateUtils;
import com.sunshine.monitor.comm.util.excel.OutputExcelUtils;
import com.sunshine.monitor.system.analysis.zyk.bean.PoliceSituation;
import com.sunshine.monitor.system.analysis.zyk.bean.Suspect;
import com.sunshine.monitor.system.analysis.zyk.dao.SuspectDao;
import com.sunshine.monitor.system.analysis.zyk.service.SuspectManager;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;

@Service("suspectManager")
public class SuspectManagerImpl implements SuspectManager {

	@Autowired
	private SuspectDao suspectDao;
	@Autowired
	private SystemDao systemDao;
	@Autowired
	private GateDao gateDao;
	
	private String table = "JM_ZTK_SUSPECT";
	
	@Override
	public Map<String, Object> queryList(Suspect susp,
			Map<String, Object> filter){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			map = this.suspectDao.findPageForMap(susp, filter);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		list =  (List<Map<String, Object>>) map.get("rows");
		if(list!=null && list.size()>0){
			for (Iterator i = list.iterator(); i.hasNext();) {
				Map<String,Object> temp = (Map<String, Object>) i.next();
				String hpzl = "";
				String kkmc = "";
				try {
					hpzl = this.systemDao.getCodeValue("030107", temp.get("hpzl")==null?"":temp.get("hpzl").toString());
					kkmc =  this.gateDao.getOldGateName(temp.get("kkbh")==null?"":temp.get("kkbh").toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				temp.put("LRSJ", sdf.format(temp.get("lrsj")));
				temp.put("HPZL", hpzl);
				temp.put("KKMC", kkmc);
			}
		}
		map.put("rows", list);
		return map;
	}
	
	@Override
	public void saveSuspect(Suspect suspect) {
		try {
			this.suspectDao.saveSuspect(suspect);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Suspect getSuspectById(String suspectId) {
		Suspect suspect = new Suspect();
		try {
			suspect = this.suspectDao.getSuspectById(suspectId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return suspect;
	}
	
	@Override
	public void deleteSuspect(String id){
		try {
			this.suspectDao.deleteSuspect(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateSuspect(Suspect suspect){
		try {
			this.suspectDao.updateSuspect(suspect);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Suspect> querySuspectForOut(Suspect susp,Map<String, Object> filter){
		List<Suspect> list = new ArrayList<Suspect>();
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select id,hphm,hpzl,lrr,lrsj,kkbh,address,description,tplj from "+table+" where 1 = 1");
		if(susp.getHphm()!=null && !"".equals(susp.getHphm())){
			sql.append(" and hphm = ?");
			params.add(susp.getHphm());
		}
		if(susp.getHpzl()!=null && !"".equals(susp.getHpzl())){
			sql.append(" and hpzl = ?");
			params.add(susp.getHpzl());
		}
		if(susp.getKkbh()!=null && !"".equals(susp.getKkbh()) && !"null".equals(susp.getKkbh())){
			sql.append(" and kkbh = ?");
			params.add(susp.getKkbh());
		}
		if(susp.getKssj()!=null && !"".equals(susp.getKssj())){
			sql.append(" and lrsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
			params.add(susp.getKssj());
		}
		if(susp.getJssj()!=null && !"".equals(susp.getJssj())){
			sql.append(" and lrsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
			params.add(susp.getJssj());
		}
		if(susp.getLrr()!=null && !"".equals(susp.getLrr())){
			sql.append(" and lrr = ?");
			params.add(susp.getLrr());
		}
		if(filter.get("rows")!=null && !"".equals(filter.get("rows")) && filter.get("page")!=null && !"".equals(filter.get("page"))){
			Integer rows = Integer.parseInt(filter.get("rows").toString());
			Integer page = Integer.parseInt(filter.get("page").toString());
			sql.append(" and rownum <= "+rows*page);
			sql.append(" and rownum >= "+rows*(page-1));
		}
		
	    try {
	    	long start = System.currentTimeMillis();
			list = this.suspectDao.queryForList(sql.toString(), params.toArray(), Suspect.class);
			long end = System.currentTimeMillis();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return list;
	}
	
	@Override
	public String outPutSuspectList(Map<String, Object> filter,String path, Suspect bean) {
		StringBuffer url = new StringBuffer();
		String filename = "";
		OutputStream output = null;		
		try {
			List<Suspect> data = querySuspectForOut(bean, filter);
			LinkedHashMap<String, List> sheets = new LinkedHashMap<String, List>();
			sheets.put("可疑信息列表", data);
			// Heads
			List<String[]> heads = new ArrayList<String[]>();
			heads.add(new String[]{"录入人","录入时间","号牌号码","号牌种类","卡口编号","地点","描述","图片路径",});
			// Fields
			List<String[]> fields = new ArrayList<String[]>();
			fields.add(new String[]{"lrr","lrsj","hphm","hpzl","kkbh","address","description","tplj",});			
			
			filename = DateUtils.getCurrTimeStr(1)+".xls";
			String fullPath = path;
			url.append(fullPath).append(filename);
			File file = new File(url.toString());
			output = new FileOutputStream(file);  
			OutputExcelUtils.outPutExcel(heads, fields, sheets, new String[]{"可疑信息"}, null, output);
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
