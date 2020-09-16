package com.sunshine.monitor.system.gate.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.core.util.DateUtil;
import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.context.AbstractEventSource;
import com.sunshine.monitor.comm.util.redis.JedisService;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateCd;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.gate.event.CodeGateAddorUpdateEvent;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.AdministrativeDivision;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Road;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.dao.UrlDao;
import com.sunshine.monitor.system.veh.bean.VehRealpass;
import com.sunshine.monitor.system.veh.service.impl.RealMangerImpl;


@Service("gateManager")
@Transactional
public class GateManagerImpl implements GateManager {
	
	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("urlDao")
	private UrlDao urlDao;
	
	@Autowired
	@Qualifier("codeGateEventSource")
	private AbstractEventSource codeGateEventSource;

	public Map<String, Object> findGatePageForMap(Page page, CodeGate gate,String glbm)
			throws Exception {
		Map<String,Object> map = this.gateDao.findGatePageForMap(page,gate,glbm);
		List<CodeGate> list = (List<CodeGate>) map.get("rows");
		for (CodeGate code:list) {
			code.setDwdmmc(this.systemDao.getDepartmentName(code.getDwdm()));
			code.setKklxmc(this.systemDao.getCodeValue("150000", code.getKklx()));
			code.setKkxzmc(this.systemDao.getCodeValue("156001", code.getKkxz()));
			code.setKkztmc(this.systemDao.getCodeValue("156003", code.getKkzt()));
		}
		map.put("rows", list);
		return map;
	}
	public Map<String, Object> findGatePageRedisForMap(Page page, CodeGate gate,String glbm)
			throws Exception {
		Map<String,Object> map = this.gateDao.findGatePageRedisForMap(page,gate,glbm);
		List<CodeGate> list = (List<CodeGate>) map.get("rows");
		List returnList = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(list!=null){
			VehRealpass vehrealpass = null;
			Map<String,Object> value = null;
			JedisService jedis = new JedisService();
			for (int i = 0; i < list.size(); i++) {
				long sj = 0;
				vehrealpass = new VehRealpass();
				CodeGate ga = list.get(i);
				//获取卡口实时过车数据
				//TODOliuws
				/*430527100010
				431500100029
				430527100015*/
				//Map<String,Object> value = jedis.getVehRealPassRedis(ga.getKdbh());  430527100010
				 value = jedis.getVehRealPassRedis(ga.getKdbh()/*"430104000032"*/);
				 
				 if(value!=null && value.get("isOnline")!=null && !"否".equals(value.get("isOnline"))){
					 vehrealpass = buildVehRrealPass(value);
					 vehrealpass.setFxbh(this.getDirectName(vehrealpass.getFxbh())/*ga.getSxfxbm()*/);
					 //todo 当前时间减去过车时间的差值
					 Date date = new Date();
					 sj = (date.getTime() - sdf.parse(vehrealpass.getGcsj()).getTime()) / 60000L;
					 if (sj > 30.0D) {
							//sj = vehrealpass.getComparedate().intValue();
							String ssj = "";
							int fen = (int)(sj % 60);
							if (fen > 0) {
								ssj = fen + "分钟" + ssj;
							}
							int hour = (int)sj / 60;
							if (hour > 0) {
								ssj = hour + "小时" + ssj;
							}
							if (ssj.length() > 0) {
								ssj = "延时" + ssj;
							}
							vehrealpass.setBy1("1");
							vehrealpass.setZt(ssj);
							
						} else {
							vehrealpass.setBy1("0");
							vehrealpass.setZt("正常");
						}
						if (!"".equals(vehrealpass.getCdbh()) && vehrealpass.getCdbh() != null) {
							vehrealpass.setCdbh(Integer.valueOf(vehrealpass.getCdbh()).toString());
						} else {
							vehrealpass.setCdbh("0");
						}
				 }else {
						vehrealpass.setGcsj("");
						vehrealpass.setBy1("1");
						vehrealpass.setCdbh("");
						vehrealpass.setZt("没有数据上传");
						
						
				}
				 vehrealpass.setKdbh(ga.getKdmc());
				 vehrealpass.setDwdm(this.systemDao.getDepartmentName(ga.getDwdm()));
				 vehrealpass.setSbzt(ga.getKkzt());
				 vehrealpass.setSbztmc(this.systemDao.getCodeValue("156003", ga.getKkzt()==null?"":ga.getKkzt().toString()));
				 returnList.add(vehrealpass);
			}
		}
		map.put("rows", returnList);
		return map;
	}
	public List<CodeGate> getGateList(CodeGate gate)throws Exception{
		gate.setKkzt("1");
		List<CodeGate> list = this.gateDao.getGateList(gate);
		for (CodeGate code:list) {
			code.setDwdmmc(this.systemDao.getDepartmentName(code.getDwdm()));
			code.setKklxmc(this.systemDao.getCodeValue("150000", code.getKklx()));
			code.setKkxzmc(this.systemDao.getCodeValue("156001", code.getKkxz()));
			code.setKkztmc(this.systemDao.getCodeValue("156003", code.getKkzt()));
		}
		return list;
	}
	
	public List<CodeGate> getGateListAndJj(CodeGate gate)throws Exception{
		gate.setKkzt("1");
		List<CodeGate> list = this.gateDao.getGateListAndJj(gate);
		for (CodeGate code:list) {
			code.setDwdmmc(this.systemDao.getDepartmentName(code.getDwdm()));
			code.setKklxmc(this.systemDao.getCodeValue("150000", code.getKklx()));
			code.setKkxzmc(this.systemDao.getCodeValue("156001", code.getKkxz()));
			code.setKkztmc(this.systemDao.getCodeValue("156003", code.getKkzt()));
		}
		return list;
	}
	
	public List<Map<String, Object>> getGateListXls(Map filter) throws Exception {
		List<Map<String, Object>> list = this.gateDao.getGateListXls(filter);
		for (Map<String, Object> map:list) {
			map.put("dwdmmc",this.systemDao.getDepartmentName(map.get("dwdm")==null?"":map.get("dwdm").toString()));
			map.put("kklxmc",this.systemDao.getCodeValue("150000", map.get("kklx")==null?"":map.get("kklx").toString()));
			map.put("kkxzmc",this.systemDao.getCodeValue("156001", map.get("kkxz")==null?"":map.get("kkxz").toString()));
			map.put("kkztmc",this.systemDao.getCodeValue("156003", map.get("kkzt")==null?"":map.get("kkzt").toString()));
		}
		return list;
	}
	
	@Transactional
	public void save(CodeGate gate,byte[] kktp)throws Exception{
		//生成卡口编号
		if(StringUtils.isBlank(gate.getKdbh())){
			String kdbh = this.gateDao.getKdbh(gate.getXzqh());
			gate.setKdbh(kdbh);
		}
		//保存卡口基础信息
		this.saveGate(gate);
		//保存卡口图片
		this.gateDao.saveKktp(kktp, gate.getKdbh());
		//保存卡口方向信息
		JSONArray array = JSONArray.fromObject(gate.getDirects());
		List<CodeGateExtend> list = (List<CodeGateExtend>)JSONArray.toCollection(array,CodeGateExtend.class);
		for(CodeGateExtend extend:list){
			extend.setKdbh(gate.getKdbh());
			this.saveGateExtend(extend);
		}
		// Notified the listerner, It's delive kakou information to Boundary
		if (StringUtils.isNotBlank(gate.getBjcs()))
			codeGateEventSource.notifyListers(new CodeGateAddorUpdateEvent(gate,""));
	}

	public void saveGate(CodeGate gate) throws Exception {
		this.gateDao.saveGate(gate);
	}
	
	public void saveGateXY(String gateArray,String glbm,String ip,String yhdh) throws Exception {
		this.gateDao.saveGateXY(gateArray,glbm,ip,yhdh);
	}

	public void saveGateExtend(CodeGateExtend extend) throws Exception {
		List<CodeGateCd> list = new ArrayList<CodeGateCd>();
		if (extend.getZrdw() != null && extend.getZrdw().length() > 0) {
			extend.setZrdw(extend.getZrdw().replaceAll("\\[", "'")
					.replaceAll("\\(", "{"));
			extend.setZrdw(extend.getZrdw().replaceAll("\\]", "'")
					.replaceAll("\\)", "}"));
		}
		if (extend.getZrdwlxdh() != null && extend.getZrdwlxdh().length() > 0) {
			extend.setZrdwlxdh(extend.getZrdwlxdh().replaceAll("\\[", "'")
					.replaceAll("\\(", "{"));
			extend.setZrdwlxdh(extend.getZrdwlxdh().replaceAll("\\]", "'")
					.replaceAll("\\)", "}"));
		}
		if (extend.getZrdwlxr() != null && extend.getZrdwlxr().length() > 0) {
			extend.setZrdwlxr(extend.getZrdwlxr().replaceAll("\\[", "'")
					.replaceAll("\\(", "{"));
			extend.setZrdwlxr(extend.getZrdwlxr().replaceAll("\\]", "'")
					.replaceAll("\\)", "}"));
		}
		if (extend.getLjdwlxdh() != null && extend.getLjdwlxdh().length() > 0) {
			extend.setLjdwlxdh(extend.getLjdwlxdh().replaceAll("\\[", "'")
					.replaceAll("\\(", "{"));
			extend.setLjdwlxdh(extend.getLjdwlxdh().replaceAll("\\]", "'")
					.replaceAll("\\)", "}"));
		}
		if (extend.getLjdwlxr() != null && extend.getLjdwlxr().length() > 0) {
			extend.setLjdwlxr(extend.getLjdwlxr().replaceAll("\\[", "'")
					.replaceAll("\\(", "{"));
			extend.setLjdwlxr(extend.getLjdwlxr().replaceAll("\\]", "'")
					.replaceAll("\\)", "}"));
		}
		if (extend.getLjdw() != null && extend.getLjdw().length() > 0) {
			extend.setLjdw(extend.getLjdw().replaceAll("\\[", "'")
					.replaceAll("\\(", "{"));
			extend.setLjdw(extend.getLjdw().replaceAll("\\]", "'")
					.replaceAll("\\)", "}"));
		}
		//车道信息
		if(StringUtils.isNotBlank(extend.getRoads())){
			String roads = extend.getRoads().replaceAll("\'", "\"").replaceAll("\\%\\(", "\\[").replaceAll("\\)\\%", "\\]");
			JSONArray array = JSONArray.fromObject(roads);
			list =(List<CodeGateCd>)JSONArray.toCollection(array,CodeGateCd.class);
		}
		//保存方向信息
		extend.setCdzs(list.size()+0.0);
		this.gateDao.saveGateExtend(extend);

		//移除车道
		if(StringUtils.isNotBlank(extend.getDelRoads())){
		    String[] cdbhs=extend.getDelRoads().split(",");
		    for(String cdbh:cdbhs){
		    	StringBuffer sb = new StringBuffer();
		    	sb.append(" kdbh = '").append(extend.getKdbh()).append("'")
		    	  .append(" fxlx = '").append(extend.getFxlx()).append("'")
		    	  .append(" cdbh = '").append(cdbh).append("'");
		    	this.gateDao.delete(sb.toString());
		    }
		}

		for(CodeGateCd road:list){
			road.setKdbh(extend.getKdbh());
			road.setFxlx(extend.getFxlx());
			this.saveRoad(road);
		}
	}

	public void saveRoad(CodeGateCd road) throws Exception {
		this.gateDao.saveRoad(road);
	}
	
	public void delGate(String kdbh) throws Exception {
			//删除卡口
			this.gateDao.delete("Code_Gate","kdbh",kdbh);
			//删除方向
			this.gateDao.delete("Code_Gate_Extend","kdbh",kdbh);
			//删除车道
			this.gateDao.delete("Code_Gate_Cd","kdbh",kdbh);
	}

	public CodeGate getGateInfo(String kdbh) throws Exception {
		// 获取卡口基本信息
		CodeGate gate = this.getGate(kdbh);
		
		if (gate!=null) {
			if (StringUtils.isNotBlank(gate.getBjcs())) {
				String bjcsmc = "";
				String[] bjcs = gate.getBjcs().split(",");
				for (String s:bjcs) {
					bjcsmc = bjcsmc + this.urlDao.getUrlName(s) + ",";
				}
				gate.setBjcsmc(bjcsmc.substring(0, bjcsmc.length() - 1));
			}
			// 获取卡口方向信息
			List<CodeGateExtend> list = this.getDirectInfo(kdbh);
			JSONArray array = JSONArray.fromObject(list);
			if (array!=null) {
				gate.setDirects(array.toString());
			}
		}
		//翻译字段
		gate.setSybmmc(this.systemDao.getDepartmentName(gate.getSybm()));
		gate.setDwdmmc(this.systemDao.getDepartmentName(gate.getDwdm()));
		gate.setSybmmc(this.systemDao.getDepartmentName(gate.getSybm()));
		gate.setXzqhmc(this.systemDao.getDistrictNameByXzqh(gate.getXzqh()));
		List<Road> road =this.systemDao.queryList(" select * from frm_dl where dldm ='"+gate.getDldm()+"' and xzqh like '%"+gate.getXzqh()+"%'",Road.class);
		gate.setDlmc(road.size()>0?road.get(0).getDlmc():"");
		return gate;
	}
	
	public List<CodeGateExtend> getDirectInfo(String kdbh)throws Exception{
		//获取方向信息
		List<CodeGateExtend> list = this.getDirectList(kdbh);
		for (CodeGateExtend code : list) {
			List<CodeGateCd> rList;
			if (code.getYwdw() != null && code.getYwdw().length() > 0)
				code.setYwdwmc(this.systemDao.getDepartmentName(code.getYwdw()));
			
			if (code.getZrdw() != null && code.getZrdw().length() > 0){
				JSONObject dw=JSONObject.fromObject(code.getZrdw());
				code.setZrdw_sa(dw.get("1").toString());
				code.setZrdwmc_sa(this.systemDao.getDepartmentName(dw.get("1").toString()));
				code.setZrdw_jt(dw.get("2").toString());
				code.setZrdwmc_jt(this.systemDao.getDepartmentName(dw.get("2").toString()));
			}
			
			if(code.getZrdwlxr() != null && code.getZrdwlxr().length()>0){
				JSONObject lxr=JSONObject.fromObject(code.getZrdwlxr());
				code.setZrdwlxr_sa(lxr.get("1").toString());
				code.setZrdwlxr_jt(lxr.get("2").toString());
			}
			
			if(code.getZrdwlxdh() != null && code.getZrdwlxdh().length()>0){
				JSONObject lxdh=JSONObject.fromObject(code.getZrdwlxdh());
				code.setZrdwlxdh_sa(lxdh.get("1").toString());
				code.setZrdwlxdh_jt(lxdh.get("2").toString());
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
			
			if(code.getLjdwlxr() != null && code.getLjdwlxr().length()>0){
				JSONObject lxr=JSONObject.fromObject(code.getLjdwlxr());
				code.setLjdwlxr_sa(lxr.get("1").toString());
				code.setLjdwlxr_jt(lxr.get("2").toString());
			}
			
			if(code.getLjdwlxdh() != null && code.getLjdwlxdh().length()>0){
				JSONObject lxdh=JSONObject.fromObject(code.getLjdwlxdh());
				code.setLjdwlxdh_sa(lxdh.get("1").toString());
				code.setLjdwlxdh_jt(lxdh.get("2").toString());
			}
  
			//获取车道信息
			rList = this.getRoad(code.getFxbh());
			String roadJson ="";
			if(rList.size()>0){
				StringBuffer temp = new StringBuffer();
				for (CodeGateCd road:rList) {
					StringBuffer sb = new StringBuffer();
					sb.append(",{'cdbh':'").append(road.getCdbh())
					.append("','cdlx':'").append(road.getCdlx())
					.append("','xcxs':'").append(road.getXcxs())
					.append("','dcxs':'").append(road.getDcxs())
					.append("','zdxs':'").append(road.getZdxs())
					.append("','xxpz':'").append(road.getXxpz())
					.append("','ip':'").append(road.getIp())
					.append("'}");
					temp.append(sb.toString());
				}
				roadJson = temp.substring(1,temp.length());
			}
			code.setRoads("%("+roadJson+")%");
		}
		return list;
	}
	
	public List<CodeGateExtend> getDirectList(String kdbh)throws Exception{
		StringBuffer sb =new StringBuffer("select * from code_gate_extend where 1=1");
		if(StringUtils.isNotBlank(kdbh)){
			sb.append(" and kdbh = '").append(kdbh).append("'");
		}
		return this.gateDao.queryList(sb.toString(),CodeGateExtend.class);
	}
	
	public List<CodeGateCd> getRoad(String fxbh)throws Exception{
		return this.gateDao.queryList("select * from code_Gate_cd where fxbh = '"+fxbh+"' order by cdbh ",CodeGateCd.class);
	}

	public List<Department> getDepartmentsByKdbh() throws Exception {
		return this.gateDao.getDepartmentsByKdbh();
	}
	
	
	public CodeGate getGateAndJj(String kdbh,String tablename) throws Exception {
		CodeGate gate = this.gateDao.queryDetail(tablename,"kdbh",kdbh,CodeGate.class);
		if(gate!=null){
			gate.setSybmmc(this.systemDao.getDepartmentName(gate.getSybm()));
			gate.setDwdmmc(this.systemDao.getDepartmentName(gate.getDwdm()));
			gate.setSybmmc(this.systemDao.getDepartmentName(gate.getSybm()));
			gate.setXzqhmc(this.systemDao.getDistrictNameByXzqh(gate.getXzqh()));
			List<Road> road =this.systemDao.queryList(" select * from frm_dl where dldm ='"+gate.getDldm()+"' and xzqh like '%"+gate.getXzqh()+"%'",Road.class);
			gate.setDlmc(road.size()>0?road.get(0).getDlmc():"");
		}
		return gate;
	}
	
	
	public CodeGate getGate(String kdbh) throws Exception {
		CodeGate gate = this.gateDao.queryDetail("code_gate","kdbh",kdbh,CodeGate.class);
		if(gate!=null){
			gate.setSybmmc(this.systemDao.getDepartmentName(gate.getSybm()));
			gate.setDwdmmc(this.systemDao.getDepartmentName(gate.getDwdm()));
			gate.setSybmmc(this.systemDao.getDepartmentName(gate.getSybm()));
			gate.setXzqhmc(this.systemDao.getDistrictNameByXzqh(gate.getXzqh()));
			List<Road> road =this.systemDao.queryList(" select * from frm_dl where dldm ='"+gate.getDldm()+"' and xzqh like '%"+gate.getXzqh()+"%'",Road.class);
			gate.setDlmc(road.size()>0?road.get(0).getDlmc():"");
		}
		return gate;
	}
	
	public List<CodeGate> getAllGates()throws Exception {
		return this.getGateList(new CodeGate());
	}
	
	public CodeGateExtend getDirect(String fxbh) throws Exception {
	
		return this.gateDao.getDirect(fxbh);
	}

	public String getDirectName(String fxbh) throws Exception {
		/**代码严重BUG,方法queryList实现自定义Bean解析封装，但对原生类型String, int等无法封装
		 * StringBuffer sb = new StringBuffer();
		sb.append("select fxmc from code_gate_extend where fxbh = '").append(fxbh).append("'");
		List<String> list = this.gateDao.queryList(sb.toString(),String.class);
		if(list.size()>0){
			return list.get(0);
		}
		return "";*/
		CodeGateExtend codeDirect = getDirect(fxbh);
		if(codeDirect != null){
			return codeDirect.getFxmc();
		}
		return fxbh;
	}
	
	public List<Map<String,Object>> getGateTreeByGlbm(String searchText) throws Exception {
		return this.gateDao.getGateTreeByGlbm(searchText);
	}
	
	public List<Map<String,Object>> getGateTreeByGlbmAndSt(String searchText,String dwdm) throws Exception {
		return this.gateDao.getGateTreeByGlbmAndSt(searchText,dwdm);
	}

	public String getGateName(String kdbh) throws Exception {
		CodeGate gate =  this.getGate(kdbh);
		if(gate!=null){
			return gate.getKdmc();
		}else{
			return kdbh;
		}
	}
	/**
	 * 获取卡口(含交警卡口）
	 * @param kdbh
	 * @return
	 * @throws Exception
	 */
	public String getGateAndJjName(String kdbh,String tablename) throws Exception {
		CodeGate gate =  this.getGateAndJj(kdbh,tablename);
		if(gate!=null){
			return gate.getKdmc();
		}else{
			return kdbh;
		}
	}
		
	public List getAllGateExtend(Map filter) throws Exception{
		return gateDao.getCodeGateExtend(filter);
	}

	public void saveBoundaryGate(CodeGate gate) throws Exception {
		this.saveGate(gate);
	}

	public void saveBoundaryGateExtend(List<CodeGateExtend> list,
			String userName, CodeGate gate) throws Exception {
		for(CodeGateExtend extend:list){
			extend.setKdbh(gate.getKdbh());
			this.saveGateExtend(extend);
		}
	}

	public List<Map<String, Object>> getTollGateTree(String searchText,
			String dwdm) throws Exception {
		return this.gateDao.getTollGateTree(searchText, dwdm);
	}

	public List<CodeGate> getTollGateList(CodeGate gate) throws Exception {
		return this.gateDao.getTollGateList(gate);
	}
	
	/**
	 * 查询CODE_GATE表信息
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getGates(Map filter, CodeGate codeGate,String subSql) throws Exception {
		Map<String,Object> map = this.gateDao.getGates(filter, codeGate, subSql);
		List<CodeGate> queryList = (List<CodeGate>) map.get("rows");
		for (int i=0;i<queryList.size();i++) {
			CodeGate gate = queryList.get(i);
			gate.setDwdmmc(this.systemDao.getDepartmentName(gate.getDwdm()));
			gate.setKklxmc(this.systemDao.getCodeValue("150000", gate.getKklx()));
			gate.setKkxzmc(this.systemDao.getCodeValue("156001", gate.getKkxz()));
			gate.setKkztmc(this.systemDao.getCodeValue("156003", gate.getKkzt()));
		}
		map.put("rows", queryList);
		return map;
	}
	
		private VehRealpass buildVehRrealPass(Map vehpass){
		VehRealpass vehrp = new VehRealpass();
		vehrp.setDailyCount(vehpass.get("dailyCount")==null?"":vehpass.get("dailyCount").toString());
		vehrp.setKdbh(vehpass.get("gateId")==null?"":vehpass.get("gateId").toString());
		vehrp.setFxlx(vehpass.get("directionType")==null?"":vehpass.get("directionType").toString());
		vehrp.setFxbh(vehpass.get("gateId").toString()+vehpass.get("directionType").toString());
		vehrp.setCdbh(vehpass.get("driverWayId")==null?"":vehpass.get("driverWayId").toString());;
		vehrp.setHpzl(vehpass.get("licenseType")==null?"":vehpass.get("licenseType").toString());;
		vehrp.setGcsj(vehpass.get("passTime")==null?"":DateUtil.longToTime(Long.parseLong(vehpass.get("passTime").toString())));;
		//vehrp.setClsd(Long.parseLong(vehpass.get("speed")==null?"":vehpass.get("speed").toString()));;
		vehrp.setHpys(vehpass.get("licenseColor")==null?"":vehpass.get("licenseColor").toString());;
		vehrp.setCllx(vehpass.get("carType")==null?"":vehpass.get("carType").toString());;
		vehrp.setHphm(vehpass.get("license")==null?"":vehpass.get("license").toString());;
		vehrp.setCwhphm(vehpass.get("backLicense")==null?"":vehpass.get("backLicense").toString());;
		vehrp.setCwhpys(vehpass.get("backLicenseColor")==null?"":vehpass.get("backLicenseColor").toString());;
		vehrp.setHpyz(vehpass.get("identical")==null?"":vehpass.get("identical").toString());;
		vehrp.setCsys(vehpass.get("carColor")==null?"":vehpass.get("carColor").toString());;
		//vehrp.setClxs(Long.parseLong(vehpass.get("limitSpeed")==null?"":vehpass.get("limitSpeed").toString()));;
		vehrp.setClpp(vehpass.get("carBrand")==null?"":vehpass.get("carBrand").toString());;
		vehrp.setClwx(vehpass.get("carShape")==null?"":vehpass.get("carShape").toString());;
		vehrp.setXszt(vehpass.get("travelStatus")==null?"":vehpass.get("travelStatus").toString());;
		vehrp.setWfbj(vehpass.get("violationFlag")==null?"":vehpass.get("violationFlag").toString());;
		//vehrp.setTp1(vehpass.get("TP1")==null?"":vehpass.get("TP1").toString());;
		//vehrp.setTp2(vehpass.get("TP2")==null?"":vehpass.get("TP2").toString());;
		//vehrp.setTp3(vehpass.get("TP3")==null?"":vehpass.get("TP3").toString());;
		return vehrp;
	}
	
	
	/**
	 * 获取行政区划列表
	 * @return
	 */
	public Map<String,Object> getAdministrativeDivisions(Map<String,String> filter) throws Exception{
		Map<String,Object> map = this.gateDao.getAdministrativeDivisions(filter);
		return map;
	}
	
	public AdministrativeDivision getAdminDivision(String xzqhxh, String xzqh) throws Exception{
		AdministrativeDivision administrative = this.gateDao.getAdminDivision(xzqhxh, xzqh);
		return administrative;
	}
	
	public int updateAdminDivision(AdministrativeDivision ad) throws Exception{
		return this.gateDao.updateAdminDivision(ad);
	}
}
