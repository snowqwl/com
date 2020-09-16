package com.sunshine.monitor.system.ws.GateService;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.ws.GateService.bean.CodeDirectEntity;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.GateService.CodeDirectService", serviceName = "CodeDirectService")
@Component("codeDirectServiceImpl")
public class CodeDirectServiceImpl implements CodeDirectService {
   
	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	public String getDirect(String conditions) {
		Map<String,Object> map=null;
		String result = "";
		if(conditions!=null&&conditions.length()>0){
			map=JSONObject.fromObject(conditions);
		}
		try {
			List<CodeDirectEntity> list = this.gateDao.queryForListByMap(map,"Code_Gate_Extend",CodeDirectEntity.class);
			for(int k = 0;k<list.size();k++){
				CodeDirectEntity code = list.get(k);
				code.setFxlxmc(this.systemDao.getCodeValue("",code.getFxlx()));
				code.setSblxmc(this.systemDao.getCodeValue("",code.getSblx()));
				code.setSbztmc(this.systemDao.getCodeValue("",code.getSbzt()));
				code.setYwdwmc(this.systemDao.getDepartmentName(code.getYwdw()));
				if (code.getZrdw() != null && code.getZrdw().length() > 0){
					JSONObject dw=JSONObject.fromObject(code.getZrdw());
					code.setZrdw_sa(dw.get("1").toString());
					code.setZrdwmc_sa(this.systemDao.getDepartmentName(dw.get("1").toString()));
					code.setZrdw_jt(dw.get("2").toString());
					code.setZrdwmc_jt(this.systemDao.getDepartmentName(dw.get("2").toString()));
				}
				
				if (code.getLjdw() != null && code.getLjdw().length() > 0){
					JSONObject dw=JSONObject.fromObject(code.getLjdw());
					//ljdw 涉案类
					code.setLjdw_sa(dw.get("1").toString());
					String[] ljdw_sa = dw.get("1").toString().split(";");
					StringBuffer ljdwmc_sa = new StringBuffer();
					for(String s:ljdw_sa){
						ljdwmc_sa.append(this.systemDao.getDepartmentName(s)+",");
					}
					code.setLjdwmc_sa(ljdwmc_sa.substring(0,(ljdwmc_sa.length()-1)));
					//ljdw 交通类
					code.setLjdw_jt(dw.get("2").toString());
					String[] ljdw_jt = dw.get("2").toString().split(";");
					StringBuffer ljdwmc_jt = new StringBuffer();
					for(String s:ljdw_jt){
						ljdwmc_jt.append(this.systemDao.getDepartmentName(s)+",");
					}
					code.setLjdwmc_jt(ljdwmc_jt.substring(0,(ljdwmc_jt.length()-1)));
				}
			}
			JSONArray json = JSONArray.fromObject(list);
			result = json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
