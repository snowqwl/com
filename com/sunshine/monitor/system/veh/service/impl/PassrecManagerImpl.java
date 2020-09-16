package com.sunshine.monitor.system.veh.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.util.DateUtils;
import com.sunshine.monitor.comm.util.excel.OutputExcelUtils;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.dao.UrlDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.veh.dao.PassrecDao;
import com.sunshine.monitor.system.veh.dao.PassrecOptimizeDao;
import com.sunshine.monitor.system.veh.dao.PassrecOptimizeSCSDao;
import com.sunshine.monitor.system.veh.service.PassrecManager;
import com.sunshine.monitor.system.ws.VehPassrecEntity;
import com.sunshine.monitor.system.ws.inAccess.axis2.InAccessClientImpl;
import com.sunshine.monitor.system.ws.inAccess.axis2.InAccessClientImpl.MappingInvocationHandler;
import com.sunshine.monitor.system.ws.util.NotDefPaserException;
import com.sunshine.monitor.system.ws.util.XmlBuild;
import com.sunshine.monitor.system.ws.util.XmlBuild.InAccessXmlBuild;

@Service("passrecManager")
public class PassrecManagerImpl implements PassrecManager {

	@Autowired
	@Qualifier("passrecOptimizeDao")
	private PassrecOptimizeDao passrecOptimizeDao;
	
	@Autowired
	@Qualifier("passrecOptimizeSCSDao")
	private PassrecOptimizeSCSDao passrecOptimizeSCSDao;
	
	@Autowired
	@Qualifier("passrecDao")
	private PassrecDao passrecDao;

	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	
	@Autowired
	@Qualifier("urlDao")
	private UrlDao urlDao;
	
	private Map<String, String> idxName = null;
	
	private String gptable = "veh_passrec"; // greenplum过车视图
	
	public PassrecManagerImpl(){
		idxName = new HashMap<String, String>(4);
		idxName.put("gcsj", "T_PASS_GCSJ");
		idxName.put("hphm", "T_PASS_HPHM_GCSJ");
		idxName.put("hphm_kdbh", "T_PASS_HPHM_KKBH_GCSJ");
		idxName.put("kdbh", "T_PASS_KKBH_GCSJ");
	}
	
	/**
	 * 查询过车信息列表
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.INTEGRATION_PASSRC_QUERY, description = "过车信息查询")
	public Map<String, Object> getPassrecList(Map<String, Object> conditions)
			throws Exception {
		return getPassrecForList(conditions);
	}
	
	/**
	 * 过车数据导出
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.INTEGRATION_PASSRC_EXPORT, description = "过车信息导出")
	public Map<String, Object> exportPassrecList(Map<String, Object> conditions)
			throws Exception {
		return getPassrecForList(conditions);
	}

	private Map<String, Object> getPassrecForList(Map<String, Object> conditions)
			throws Exception {
		Map<String, Object> map = this.passrecOptimizeDao.getPassrecList(conditions,
				null);
		List<VehPassrec> list = (List<VehPassrec>) map.get("rows");
		if (list != null) {
			for (Iterator<VehPassrec> it = list.iterator(); it.hasNext();) {
				VehPassrec v = (VehPassrec) it.next();
				v.setHpzlmc(this.systemDao.getCodeValue("030107", v.getHpzl()));
				v.setHpysmc(this.systemDao.getCodeValue("031001", v.getHpys()));
				v.setXsztmc(this.systemDao.getCodeValue("110005", v.getXszt()));
				v.setCwhpysmc(this.systemDao.getCodeValue("031001", v
						.getCwhpys()));
				v.setHpyzmc(this.systemDao.getCodeValue("031003", v.getHpyz()));
				v.setCllxmc(this.systemDao.getCodeValue("030104", v.getCllx()));
				v.setCsysmc(this.systemDao.getCsys(v.getCsys()));
				//v.setKdmc(this.gateDao.getGateName(v.getKdbh()));
				v.setKdmc(this.gateManager.getGateName(v.getKdbh()));
				v.setSbmc("");
				v.setFxmc(this.gateManager.getDirectName(v.getFxbh()));
				v.setTp1(v.getTp1().replaceAll("\\\\", "/"));
				v.setTp2(v.getTp2().replaceAll("\\\\", "/"));
				v.setTp3(v.getTp3().replaceAll("\\\\", "/"));
			}
		}
		return map;
	}
	
	
	/**
	 * 查询过车信息列表(含交警卡口)
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.INTEGRATION_PASSRC_QUERY, description = "过车信息查询")
	public Map<String, Object> getPassrecAndJjList(Map<String, Object> conditions)
			throws Exception {
		Map<String, Object> map = this.passrecOptimizeDao.getPassrecAndJjList(conditions,null);
		return getPassrecAndJjForList(map);
	}
	
    private Map<String, Object> getPassrecForDownload(Map<String, Object> conditions) throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
    	conditions.put("page", "1");
    	map = getPasslistsNoCount(conditions);
    	return map;
    }
	
	
	private Map<String, Object> getPassrecAndJjForList(Map<String, Object> conditions)throws Exception {
		/*Map<String, Object> map = this.passrecOptimizeDao.getPassrecAndJjList(conditions,
				null);*/
		Map<String, Object> map = conditions;
		List<VehPassrec> list = (List<VehPassrec>) map.get("rows");
		if (list != null) {
			for (Iterator<VehPassrec> it = list.iterator(); it.hasNext();) {
				VehPassrec v = (VehPassrec) it.next();
				v.setHpzlmc(this.systemDao.getCodeValue("030107", v.getHpzl()));
				v.setHpysmc(this.systemDao.getCodeValue("031001", v.getHpys()));
				v.setXsztmc(this.systemDao.getCodeValue("110005", v.getXszt()));
				v.setCwhpysmc(this.systemDao.getCodeValue("031001", v.getCwhpys()));
				v.setHpyzmc(this.systemDao.getCodeValue("031003", v.getHpyz()));
				v.setCllxmc(this.systemDao.getCodeValue("030104", v.getCllx()));
				v.setCsysmc(this.systemDao.getCodeValue("030108", v.getCsys()));
				v.setCsys(this.systemDao.getCodeValue("030108", v.getCsys()));
				v.setClpp(this.gateManager.getDirectName(v.getClpp()));
				//v.setKdmc(this.gateDao.getGateName(v.getKdbh()));
				//v.setKdmc(this.gateManager.getGateName(v.getKdbh()));
				if (conditions.get("cityname") == null
							|| conditions.get("cityname").toString().length() < 1) {
				v.setKdmc(this.gateManager.getGateAndJjName(v.getKdbh(),"t_jj_code_gate"));
					}else{
				v.setKdmc(this.gateManager.getGateAndJjName(v.getKdbh(),"code_gate"));	
				}
				v.setSbmc("");
				v.setFxmc(this.gateManager.getDirectName(v.getFxbh()));
				// 处理长沙卡口图片(影响查询速度)
				/*JSONObject tpJsonObject = PicWSInvokeTool.getPicJsonObject(v,v.getKdbh());
				v.setTp1(PicWSInvokeTool.getValue(tpJsonObject,"tp1",v.getTp1()).replaceAll("\\\\", "/"));
				v.setTp2(PicWSInvokeTool.getValue(tpJsonObject,"tp2",v.getTp2()).replaceAll("\\\\", "/"));
				v.setTp3(PicWSInvokeTool.getValue(tpJsonObject,"tp3",v.getTp3()).replaceAll("\\\\", "/"));*/
			}
		}
		return map;
		}
	
	
	
	/**
	 * 根据号牌号码查询过车信息列表(模糊查询)
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPassrecListByHphm(Map<String, Object> conditions) throws Exception {
		Map<String, Object> map =  this.passrecDao.getPassrecListByHphm(conditions);
		List<VehPassrec> list = (List<VehPassrec>) map.get("rows");
		if (list != null) {
			for (Iterator<VehPassrec> it = list.iterator(); it.hasNext();) {
				VehPassrec v = (VehPassrec) it.next();
				v.setHpzlmc(this.systemDao.getCodeValue("030107", v.getHpzl()));
				v.setHpysmc(this.systemDao.getCodeValue("031001", v.getHpys()));
				v.setXsztmc(this.systemDao.getCodeValue("110005", v.getXszt()));
				v.setCwhpysmc(this.systemDao.getCodeValue("031001", v
						.getCwhpys()));
				v.setHpyzmc(this.systemDao.getCodeValue("031003", v.getHpyz()));
				v.setCllxmc(this.systemDao.getCodeValue("030104", v.getCllx()));
				v.setCsysmc(this.systemDao.getCsys(v.getCsys()));
				v.setKdmc(this.gateManager.getGateName(v.getKdbh()));
				v.setSbmc("");
				v.setFxmc(this.gateManager.getDirectName(v.getFxbh()));
				v.setTp1(v.getTp1().replaceAll("\\\\", "/"));
				v.setTp2(v.getTp2().replaceAll("\\\\", "/"));
				v.setTp3(v.getTp3().replaceAll("\\\\", "/"));
			}
		}
		return map;
	}

	public int getAllCount(Map<String,Object> conditions) throws Exception {
		int count=0;
		count=this.passrecOptimizeDao.getAllCount(conditions);
		return count;
	}
	
	public int getPassAndJjAllCount(Map<String,Object> conditions) throws Exception {
		int count=0;
		count=this.passrecOptimizeDao.getPassAndJjAllCount(conditions);
		return count;
	}
	
	
	/**
	 * 查询过车详细信息
	 * @param gcxh
	 * @return
	 * @throws Exception
	 */
	public VehPassrec getVehPassrecDetail(String gcxh) throws Exception {
		VehPassrec v = this.passrecDao.getVehPassrecDetail(gcxh);
		v.setHpzlmc(this.systemDao.getCodeValue("030107", v.getHpzl()));
		v.setHpysmc(this.systemDao.getCodeValue("031001", v.getHpys()));
		v.setXsztmc(this.systemDao.getCodeValue("110005", v.getXszt()));
		v.setCwhpysmc(this.systemDao.getCodeValue("031001", v
				.getCwhpys()));
		v.setHpyzmc(this.systemDao.getCodeValue("031003", v.getHpyz()));
		v.setCllxmc(this.systemDao.getCodeValue("030104", v.getCllx()));
		v.setCsysmc(this.systemDao.getCsys(v.getCsys()));
		v.setKdmc(this.gateManager.getGateName(v.getKdbh()));
		v.setSbmc("");
		v.setFxmc(this.gateManager.getDirectName(v.getFxbh()));
		v.setTp1(v.getTp1().replaceAll("\\\\", "/"));
		v.setTp2(v.getTp2().replaceAll("\\\\", "/"));
		v.setTp3(v.getTp3().replaceAll("\\\\", "/"));
		return v;
	}
	/**
	 * 查询一定数量的过车信息
	 * 功能:全省漫游查询
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<VehPassrecEntity> queryPassrecLimitedList(String sql)
			throws Exception {
		List<VehPassrecEntity> list = this.passrecDao
				.queryPassrecLimitedList(sql);
		return list;
	}
	/**
	 * 使用axis2 对 /dc 系统 进行漫游查询
	 * @return
	 */
	public List<VehPassrecEntity> queryPassrecDataByAxis2(CodeUrl codeUrl,
			VehPassrec vehPassrec) throws AxisFault, DocumentException,
			NotDefPaserException {
		String systemType = "01";
		String businessType = "01";
		String sn = codeUrl.getSn();
		String kssj = StringUtils.defaultIfEmpty(vehPassrec.getKssj(), "");
		String jssj = StringUtils.defaultIfEmpty(vehPassrec.getJssj(), "");
		String hpzl = StringUtils.defaultIfEmpty(vehPassrec.getHpzl(), "");
		String hphm = StringUtils.defaultIfEmpty(vehPassrec.getHphm(), "");
		InAccessXmlBuild build = new XmlBuild.InAccessXmlBuild(kssj, jssj,
				hpzl, hphm);
		InAccessClientImpl inAccessClient = new InAccessClientImpl(codeUrl);
		inAccessClient
				.setMappingInvocationHandler(new MappingInvocationHandler() {

					public VehPassrecEntity invoke(InAccessClientImpl client,
							Element data, VehPassrecEntity entity)
							throws NotDefPaserException {
						client.autoMapping(data, entity);
						try {
							entity.setCity(urlDao.getUrlName(entity.getGcxh()
									.substring(0, 12)));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							entity.setKdmc(gateManager.getGateName(StringUtils
									.defaultIfBlank(entity.getKdbh(), "")));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							entity.setFxmc(gateManager.getDirectName(StringUtils
									.defaultIfBlank(entity.getFxbh(), "")));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							String hpzlmc = systemDao.getCode(
									"030107",
									StringUtils.defaultIfBlank(
											entity.getHpzl(), "")).getDmsm1();
							entity.setHpzlmc(StringUtils.defaultIfBlank(hpzlmc,
									entity.getHpzl()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							entity.setSbmc("");
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (entity.getTp1() != null)
							entity.setTp1(entity.getTp1().replaceAll("\\\\",
									"/"));
						if (entity.getTp2() != null)
							entity.setTp3(entity.getTp2().replaceAll("\\\\",
									"/"));
						if (entity.getTp1() != null)
							entity.setTp3(entity.getTp3().replaceAll("\\\\",
									"/"));
						return entity;
					}
				});

		return new InAccessClientImpl(codeUrl).executes(systemType,
				businessType, sn, build);
	}
	/**
	 * 查询当月中的过车数量
	 * @return
	 */
	public int getPassrecCountInThisMonth() throws Exception {
		return this.passrecDao.getPassrecCountInThisMonth();
	}
	
	/**
	 * 过车查询提示
	 * @param condition
	 * @return
	 */
	@OperationAnnotation(type = OperationType.INTEGRATION_PASSJJ_QUERY, description = "过车信息查询（地市）")
	public int queryTips(Map condition) {
		return this.passrecOptimizeDao.queryTips(condition);
	}
	
	/**
	 * 过车查询提示(含交警过车查询)
	 * @param condition
	 * @return
	 */
	public int queryTipsAndJj(Map condition) {
		return this.passrecOptimizeDao.queryTipsAndJj(condition);
	}
	
	/**
	 * 根据条件选择相应索引策略进行优化
	 * 此优化基于规则
	 * @param condition
	 * @return
	 */
	private String selectIdxHint(Map<String, Object> condition){
		boolean c1 = condition.containsKey("hphm");
		boolean c2 = condition.containsKey("kdbh");
		if(c1 && c2) {
			return idxName.get("hphm_kdbh");
		} else if(c1 && !c2){
			return idxName.get("hphm");
		} else if(!c1 && c2){
			return idxName.get("kdbh");
		} else {
			return idxName.get("gcsj");
		}
	}
	
	/**
	 * <2016-8-15> 车查询基于GreenPlum改造
	 * <2016-7-20> 过车查询优化:指定索引策略,sql预编译处理
	 * 
	 * 过车信息查询提示(含交警过车查询)
	 */
	public int getPassCount(Map<String, Object> condition) throws Exception{
		int total=0;
		StringBuffer sql = new StringBuffer(50);
		try {
			//List<Object> params = new ArrayList<Object>();
			if(condition.get("hphm")!=null && !"".equals(condition.get("hphm"))) {
				sql.append(" and hphm = '"+condition.get("hphm")+"'");
				//params.add(condition.get("hphm"));
			}
			if(condition.get("hpzl")!=null && !"".equals(condition.get("hpzl"))) {
				sql.append(" and hpzl = '"+condition.get("hpzl")+"'");
				//params.add(condition.get("hpzl"));
			}
			if(condition.get("hpys")!=null && !"".equals(condition.get("hpys"))) {
				sql.append(" and hpys = '"+condition.get("hpys")+"'");
				//params.add(condition.get("hpys"));
			}
			if(condition.get("csys")!=null && !"".equals(condition.get("csys"))) {
				sql.append(" and csys = '"+condition.get("csys")+"'");
				//params.add(condition.get("csys"));
			}
			if(condition.get("cllx")!=null && !"".equals(condition.get("cllx"))) {
				sql.append(" and cllx = '"+condition.get("cllx")+"'");
				//params.add(condition.get("cllx"));
			}
			if(condition.get("kdbh")!=null && !"".equals(condition.get("kdbh"))) {
				sql.append(" and kdbh = '"+condition.get("kdbh")+"'");
				//params.add(condition.get("kdbh"));
			}
			if(condition.get("kdbhlike")!=null && !"".equals(condition.get("kdbhlike"))) {
				sql.append(" and kdbh like '"+condition.get("kdbhlike")+"%'");
				//params.add(condition.get("kdbhlike") + "%");
			}
			if(condition.get("fxbh")!=null && !"".equals(condition.get("fxbh"))) {
				sql.append(" and fxbh = '"+condition.get("fxbh")+"'");
				//params.add(condition.get("fxbh"));
			}
			if(condition.get("kssj")!=null && !"".equals(condition.get("kssj"))) {
				sql.append(" and gcsj >= '"+condition.get("kssj")+"'");
				//params.add(condition.get("kssj"));
			}
			if(condition.get("jssj")!=null && !"".equals(condition.get("jssj"))) {
				sql.append(" and gcsj <= '"+condition.get("jssj")+"'");
				//params.add(condition.get("jssj"));
			}
			StringBuffer indexSql=new StringBuffer();
			//String hint = selectIdxHint(condition);
			indexSql.append("select count(1) from "+ gptable +" t where 1=1 ");
			sql=indexSql.append(sql);
			//添加红名单过滤
			//StringBuffer rsql = new StringBuffer(" SELECT count(1) FROM (");	
			//rsql.append(indexSql);
			//rsql.append(" and not exists(select a.hphm from jm_red_namelist a where t.hphm=a.hphm and a.status='1' and a.isvalid='1')");
			sql.append(" limit 1000 offset 0");
			//rsql.append(") where rownum <= 1000 ) totalTable ");
			total = passrecOptimizeSCSDao.queryPassCount(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("过车查询异常!" + e.getMessage() + " Cause:" + e.getCause());
		}
		return total;
	}
	
	/**
	 * <2016-8-15> 车查询基于GreenPlum改造
	 * <2016-7-20> 过车查询优化:指定索引策略,sql预编译处理
	 * 
	 * 查询过车信息列表(含交警卡口)
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.INTEGRATION_PASSRC_QUERY, description = "过车信息查询")
	public Map<String, Object> getPasslists(Map<String, Object> conditions)
			throws Exception {
		String sql = getSql(conditions);
		// 构建分页
		int count = Integer.valueOf(conditions.get("count").toString());
		int cpage = Integer.valueOf(conditions.get("page").toString());
		int psize = Integer.valueOf(conditions.get("rows").toString());
		String psql = passrecOptimizeSCSDao.getPageSqlGp(sql, cpage, psize);
		List<VehPassrec> list = passrecOptimizeSCSDao.queryPassPage(psql, VehPassrec.class);
		Map<String, Object> result = new HashMap<String,Object>();
		// 设置总共有多少条记录
		result.put("total", count);
		// 设置当前页的数据
		result.put("rows", list);		
		
		return getPassrecAndJjForList(result);
	}
	
	/**
	 * <2016-12-19> licheng 精确号牌,查询第一页不计总数 
	 * 
	 * 查询过车信息列表(含交警卡口)
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.INTEGRATION_PASSRC_QUERY, description = "过车信息查询")
	public Map<String, Object> getPasslistsExt(Map<String, Object> conditions)
			throws Exception {
		String sql = getSql(conditions);
		List<VehPassrec> list = passrecOptimizeSCSDao.getPasslistsExt(sql,conditions);
		Map<String, Object> result = new HashMap<String,Object>();
		// 设置当前页的数据
		result.put("rows", list);		
		return getPassrecAndJjForList(result);
	}
	
	public Map<String, Object> getPasslistsExtDS(Map<String, Object> conditions)
			throws Exception {
		String sql = getSql(conditions);
		List<VehPassrec> list = passrecOptimizeSCSDao.getPasslistsExt(sql,conditions);
		Map<String, Object> result = new HashMap<String,Object>();
		// 设置当前页的数据
		result.put("rows", list);		
		return getPassrecAndJjForList(result);
	}
	
	@Override
	public Integer queryPassAndJjlist3Total(Map<String, Object> conditions)
			throws Exception {
		Integer count = 0;
		String sql = getSql(conditions);
		count = passrecOptimizeSCSDao.queryPassAndJjlist3Total(sql);
		return count;
	}
	
	private String getSql(Map<String, Object> conditions){
		StringBuffer sql = new StringBuffer(50);
		//List<Object> params = new ArrayList<Object>();
		if(conditions.get("kdbh")!=null && !"".equals(conditions.get("kdbh"))) {
			sql.append(" and kdbh = '"+conditions.get("kdbh")+"'");
			//params.add(conditions.get("kdbh"));
		}
		
		if(conditions.get("kdbhlike")!=null && !"".equals(conditions.get("kdbhlike"))) {
			sql.append(" and kdbh like '"+conditions.get("kdbhlike")+"%'");
			//params.add(conditions.get("kdbhlike") + "%");
		}
		if(conditions.get("fxbh")!=null && !"".equals(conditions.get("fxbh"))) {
			sql.append(" and fxbh = '"+conditions.get("fxbh")+"'");
			//params.add(conditions.get("fxbh"));
		}
		if(conditions.get("hphm")!=null && !"".equals(conditions.get("hphm"))) {
			sql.append(" and hphm = '"+conditions.get("hphm")+"'");
			//params.add(conditions.get("hphm"));
		}
		if(conditions.get("hpzl")!=null && !"".equals(conditions.get("hpzl"))) {
			sql.append(" and hpzl = '"+conditions.get("hpzl")+"'");
			//params.add(conditions.get("hpzl"));
		}
		if(conditions.get("hpys")!=null && !"".equals(conditions.get("hpys"))) {
			sql.append(" and hpys = '"+conditions.get("hpys")+"'");
			//params.add(conditions.get("hpys"));
		}
		if(conditions.get("csys")!=null && !"".equals(conditions.get("csys"))) {
			sql.append(" and csys = '"+conditions.get("csys")+"'");
			//params.add(conditions.get("csys"));
		}
		if(conditions.get("cllx")!=null && !"".equals(conditions.get("cllx"))) {
			sql.append(" and cllx = '"+conditions.get("cllx")+"'");
			//params.add(conditions.get("cllx"));
		}
		if(conditions.get("kssj")!=null && !"".equals(conditions.get("kssj"))) {
			sql.append(" and gcsj >= '"+conditions.get("kssj")+"'");
			//params.add(conditions.get("kssj"));
		}
		if(conditions.get("jssj")!=null && !"".equals(conditions.get("jssj"))) {
			sql.append(" and gcsj <= '"+conditions.get("jssj")+"'");
			//params.add(conditions.get("jssj"));
		}
		StringBuffer indexSql=new StringBuffer();
		indexSql.append("select gcxh, gcsj, kdbh, hphm, fxbh, clsd, hpzl, cdbh, clpp, hpys, csys, tp1, tp2, tp3, rksj, "
				+ "cwhphm, cwhpys, clwx, hpyz, xszt, cllx from "+ gptable +" t where 1=1 ");
		sql=indexSql.append(sql);
		//添加红名单过滤
		//sql.append(" and not exists(select a.hphm from jm_red_namelist a where t.hphm=a.hphm and a.status='1' and a.isvalid='1')");	
		sql.append(" order by gcsj desc ");
		return sql.toString();
	}
	
	/**
	 * <2016-8-15> 车查询基于GreenPlum改造
	 * <2016-7-20> 过车查询优化:指定索引策略,sql预编译处理,不计算总数
	 * 
	 * 查询过车信息列表(含交警卡口)
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.INTEGRATION_PASSRC_QUERY, description = "过车信息查询")
	public Map<String, Object> getPasslistsNoCount(Map<String, Object> conditions)
			throws Exception {
		StringBuffer sql = new StringBuffer(50);
		//List<Object> params = new ArrayList<Object>();
		if(conditions.get("kdbh")!=null && !"".equals(conditions.get("kdbh"))) {
			sql.append(" and kdbh = '"+conditions.get("kdbh")+"'");
			//params.add(conditions.get("kdbh"));
		}
		if(conditions.get("kdbhlike")!=null && !"".equals(conditions.get("kdbhlike"))) {
			sql.append(" and kdbh like '"+conditions.get("kdbhlike")+"%'");
			//params.add(conditions.get("kdbhlike") + "%");
		}
		if(conditions.get("fxbh")!=null && !"".equals(conditions.get("fxbh"))) {
			sql.append(" and fxbh = '"+conditions.get("fxbh")+"'");
			//params.add(conditions.get("fxbh"));
		}
		if(conditions.get("hphm")!=null && !"".equals(conditions.get("hphm")) && !"undefined".equals(conditions.get("hphm"))) {
			sql.append(" and hphm = '"+conditions.get("hphm")+"'");
			//params.add(conditions.get("hphm"));
		}
		if(conditions.get("hpzl")!=null && !"".equals(conditions.get("hpzl"))) {
			sql.append(" and hpzl = '"+conditions.get("hpzl")+"'");
			//params.add(conditions.get("hpzl"));
		}
		if(conditions.get("hpys")!=null && !"".equals(conditions.get("hpys"))) {
			sql.append(" and hpys = '"+conditions.get("hpys")+"'");
			//params.add(conditions.get("hpys"));
		}
		if(conditions.get("csys")!=null && !"".equals(conditions.get("csys"))) {
			sql.append(" and csys = '"+conditions.get("csys")+"'");
			//params.add(conditions.get("csys"));
		}
		if(conditions.get("cllx")!=null && !"".equals(conditions.get("cllx"))) {
			sql.append(" and cllx = '"+conditions.get("cllx")+"'");
			//params.add(conditions.get("cllx"));
		}
		if(conditions.get("kssj")!=null && !"".equals(conditions.get("kssj"))) {
			sql.append(" and gcsj >= '"+conditions.get("kssj")+"'");
			//params.add(conditions.get("kssj"));
		}
		if(conditions.get("jssj")!=null && !"".equals(conditions.get("jssj"))) {
			sql.append(" and gcsj <= '"+conditions.get("jssj")+"'");
			//params.add(conditions.get("jssj"));
		}
		StringBuffer indexSql=new StringBuffer();
		indexSql.append("select gcxh, gcsj, kdbh, hphm, fxbh, clsd, hpzl, clpp, cdbh, hpys, csys, tp1, tp2, tp3, rksj, "
				+ "cwhphm, cwhpys, clwx, hpyz, xszt, cllx from "+ gptable +" t where 1=1 ");
		sql=indexSql.append(sql);
		//添加红名单过滤
		//sql.append(" and not exists(select a.hphm from jm_red_namelist a where t.hphm=a.hphm and a.status='1' and a.isvalid='1')");	
		sql.append(" order by gcsj desc");
		
		// 构建分页
		int cpage = Integer.valueOf(conditions.get("page").toString());
		int psize = Integer.valueOf(conditions.get("rows").toString());
		String psql = passrecOptimizeSCSDao.getPageSqlGp(sql.toString(), cpage, psize);
		List<VehPassrec> list = passrecOptimizeSCSDao.queryPassPage(psql,VehPassrec.class);
		Map<String, Object> result = new HashMap<String,Object>();
		// 设置当前页的数据
		result.put("rows", list);
		// 获取当前页码
		result.put("curpage",conditions.get("page"));
		return getPassrecAndJjForList(result);
	}
	
	public Map<String, Object> getPasslistsNoCountDS(Map<String, Object> conditions)
			throws Exception {
		StringBuffer sql = new StringBuffer(50);
		//List<Object> params = new ArrayList<Object>();
		if(conditions.get("kdbh")!=null && !"".equals(conditions.get("kdbh"))) {
			sql.append(" and kdbh = '"+conditions.get("kdbh")+"'");
			//params.add(conditions.get("kdbh"));
		}
		if(conditions.get("kdbhlike")!=null && !"".equals(conditions.get("kdbhlike"))) {
			sql.append(" and kdbh like '"+conditions.get("kdbhlike")+"%'");
			//params.add(conditions.get("kdbhlike") + "%");
		}
		if(conditions.get("fxbh")!=null && !"".equals(conditions.get("fxbh"))) {
			sql.append(" and fxbh = '"+conditions.get("fxbh")+"'");
			//params.add(conditions.get("fxbh"));
		}
		if(conditions.get("hphm")!=null && !"".equals(conditions.get("hphm")) && !"undefined".equals(conditions.get("hphm"))) {
			sql.append(" and hphm = '"+conditions.get("hphm")+"'");
			//params.add(conditions.get("hphm"));
		}
		if(conditions.get("hpzl")!=null && !"".equals(conditions.get("hpzl"))) {
			sql.append(" and hpzl = '"+conditions.get("hpzl")+"'");
			//params.add(conditions.get("hpzl"));
		}
		if(conditions.get("hpys")!=null && !"".equals(conditions.get("hpys"))) {
			sql.append(" and hpys = '"+conditions.get("hpys")+"'");
			//params.add(conditions.get("hpys"));
		}
		if(conditions.get("csys")!=null && !"".equals(conditions.get("csys"))) {
			sql.append(" and csys = '"+conditions.get("csys")+"'");
			//params.add(conditions.get("csys"));
		}
		if(conditions.get("cllx")!=null && !"".equals(conditions.get("cllx"))) {
			sql.append(" and cllx = '"+conditions.get("cllx")+"'");
			//params.add(conditions.get("cllx"));
		}
		if(conditions.get("kssj")!=null && !"".equals(conditions.get("kssj"))) {
			sql.append(" and gcsj >= '"+conditions.get("kssj")+"'");
			//params.add(conditions.get("kssj"));
		}
		if(conditions.get("jssj")!=null && !"".equals(conditions.get("jssj"))) {
			sql.append(" and gcsj <= '"+conditions.get("jssj")+"'");
			//params.add(conditions.get("jssj"));
		}
		StringBuffer indexSql=new StringBuffer();
		indexSql.append("select gcxh, gcsj, kdbh, hphm, fxbh, clsd, hpzl, clpp, cdbh, hpys, csys, tp1, tp2, tp3, rksj, "
				+ "cwhphm, cwhpys, clwx, hpyz, xszt, cllx from "+ gptable +" t where 1=1 ");
		sql=indexSql.append(sql);
		//添加红名单过滤
		//sql.append(" and not exists(select a.hphm from jm_red_namelist a where t.hphm=a.hphm and a.status='1' and a.isvalid='1')");	
		sql.append(" order by gcsj desc");
		
		// 构建分页
		int cpage = Integer.valueOf(conditions.get("page").toString());
		int psize = Integer.valueOf(conditions.get("rows").toString());
		String psql = passrecOptimizeSCSDao.getPageSqlGp(sql.toString(), cpage, psize);
		List<VehPassrec> list = passrecOptimizeSCSDao.queryPassPage(psql,VehPassrec.class);
		Map<String, Object> result = new HashMap<String,Object>();
		// 设置当前页的数据
		result.put("rows", list);
		// 获取当前页码
		result.put("curpage",conditions.get("page"));
		return getPassrecAndJjForList(result);
	}
	
	/**
	 * <2016-7-20> 过车查询优化
	 * 
	 * 构造oracle数据库的分页语句,不含总数
	 * @param sql
	 * @param cpage
	 * @param psize
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private String getPageSql(String sql, int cpage, int psize,
			List<Object> params) throws Exception {
		// 起始行数
		int startIndex = (cpage - 1) * psize;
		// 结束行数
		int lastIndex  = cpage * psize;
		// 构造oracle数据库的分页语句
		StringBuffer psql = new StringBuffer(" SELECT /*+FIRST_ROWS(20)*/* FROM ( ");
		psql.append(" SELECT temp.* ,ROWNUM num FROM ( ");
		psql.append(sql);
		if (psize != -1) {
			psql.append(" ) temp where ROWNUM <= ?");
			params.add(lastIndex);
		} else {
			psql.append(" ) temp ");
		}
		psql.append(" ) WHERE num > ?");
		params.add(startIndex);
		return psql.toString();
	}
	
	/**
	 * <2016-7-20> 过车查询优化
	 * 
	 * 构造oracle数据库的分页语句,包含总数
	 * @param count
	 * @param sql
	 * @param cpage
	 * @param psize
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private String getPageSql(int count, String sql, int cpage, int psize,
			List<Object> params) throws Exception {
		// 总记录数
		int totalRows = count;
		// 总页数
		int totalPages;
		if (psize != -1) {
			if (totalRows % psize == 0) {
				totalPages = totalRows / psize;
			} else {
				totalPages = (totalRows / psize) + 1;
			}
		} else {
			totalPages = 1;
		}
		// 起始行数
		int startIndex = (cpage - 1) * psize;
		// 结束行数
		int lastIndex = 0;
		if (totalRows < psize) {
			lastIndex = totalRows;
		} else if ((totalRows % psize == 0)
				|| (totalRows % psize != 0 && cpage < totalPages)) {
			lastIndex = cpage * psize;
		} else if (totalRows % psize != 0 && cpage == totalPages) {// 最后一页
			lastIndex = totalRows;
		}
		// 构造oracle数据库的分页语句
		StringBuffer psql = new StringBuffer(" SELECT /*+FIRST_ROWS(20)*/* FROM ( ");
		psql.append(" SELECT temp.* ,ROWNUM num FROM ( ");
		psql.append(sql);
		if (psize != -1) {
			psql.append(" ) temp where ROWNUM <= ?");
			params.add(lastIndex);
		} else {
			psql.append(" ) temp ");
		}
		psql.append(" ) WHERE num > ?");
		params.add(startIndex);
		return psql.toString();
	}
	
	/**
	 * 导出过车信息列表前200条写入系统目录下
	 * @return url
	 */
	public String PassrecAndJjListFileUrl(Map<String,Object> condition) {
		StringBuffer url = new StringBuffer();
		String filename = "";
		OutputStream output = null;
		Map<String, Object> result = null;
		try {
			String path = condition.get("path").toString();
			condition.remove("path");
			result = getPassrecForDownload(condition);
			List data = (List) result.get("rows");
			// data
			LinkedHashMap<String, List> sheets = new LinkedHashMap<String, List>();
			sheets.put("过车列表", data);
			// Heads
			List<String[]> heads = new ArrayList<String[]>();
			heads.add(new String[]{"卡口名称","号牌号码","过车时间","号牌颜色","号牌种类","过车时间","图片一","图片二","图片三",});
			// Fields
			List<String[]> fields = new ArrayList<String[]>();
			fields.add(new String[]{"kdmc","hphm","gcsj","hpysmc","hpzlmc","gcsj","tp1","tp2","tp3"});
			
			// ====暂时实现=====
			filename = DateUtils.getCurrTimeStr(1)+".xls";
			/*String classPath = Thread.currentThread().getContextClassLoader()
					.getResource("").getPath();
			int pos = classPath.indexOf("WEB-INF");
			String fullPath = classPath.substring(0, pos) + "download/";*/
			String fullPath = path;
			url.append(fullPath).append(filename);
			
			File file = new File(url.toString());
			output = new FileOutputStream(file);  
			OutputExcelUtils.outPutExcel(heads, fields, sheets, new String[]{"车辆通行信息"}, null, output);
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
