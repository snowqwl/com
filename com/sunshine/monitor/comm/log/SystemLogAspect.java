package com.sunshine.monitor.comm.log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlElement;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.Entity;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.LogManager;

/**
 * 系统日志
 * 
 * @author OUYANG 2013/7/18
 * 
 */
@Aspect
@Component
public class SystemLogAspect implements Ordered {

	@Autowired
	private LogManager logManager;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	/**
	 * 本次请求参数有效
	 */
	@Autowired(required=false)
	private HttpServletRequest request;

	private Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

	public int getOrder() {
		return 1;
	}

	/**
	 * 对所有服务层注解，方法含有OperationAnnotation注解进行切入
	 */
	@Pointcut("execution(* com.sunshine.monitor..*.*(..)) && @annotation(com.sunshine.monitor.comm.log.OperationAnnotation)")
	public void syslogpcut() {

	}

	/**
	 * 连接点方法正常执行完成执行此方法
	 * 
	 * @param jp
	 * @param operationAnnotation
	 */
	@AfterReturning(pointcut = "syslogpcut() && @annotation(operationAnnotation)",returning="result")
	public void doAfterReturning(JoinPoint jp,
			OperationAnnotation operationAnnotation,Object result) {
		String methodName = jp.getSignature().getName();
		if (!"".equals(methodName) && methodName != null) {
			try {
				Object[] args = jp.getArgs();
				if(result==null){
					result = "";
				}
				String cznr = this.getCznr(args,result);
				UserSession userSession = (UserSession) WebUtils
						.getSessionAttribute(request, "userSession");
				String yhdh = userSession.getSysuser().getYhdh();
				String glbm = userSession.getDepartment().getGlbm();
				String ip = getRemortIP(request);
				String description = operationAnnotation.description();
				OperationType operationType = operationAnnotation.type();
				String czlx = operationType.getType();
				Log log = new Log();
				log.setGlbm(glbm);
				log.setIp(ip);
				log.setYhdh(yhdh);
				log.setCzlx(czlx);
				if (cznr != null && cznr.length() > 0) {
					cznr = "，操作条件：" + cznr;
				}
				log.setCznr(description + "成功 " + cznr);
				this.addLog(log);
				logger.info(description + "成功 " + cznr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 连接点方法执行出现异常时执行此方法
	 * 
	 * @param jp
	 * @param operationAnnotation
	 * @param ex
	 */
	@AfterThrowing(pointcut = "syslogpcut() && @annotation(operationAnnotation)", throwing = "ex")
	public void doAfterthrowing(JoinPoint jp,
			OperationAnnotation operationAnnotation, Exception ex) {
		String methodName = jp.getSignature().getName();
		if (!"".equals(methodName) && methodName != null) {
			try {
				UserSession userSession = (UserSession) WebUtils
						.getSessionAttribute(request, "userSession");
				String yhdh = userSession.getSysuser().getYhdh();
				String glbm = userSession.getDepartment().getGlbm();
				String ip = getRemortIP(request);
				String description = operationAnnotation.description();
				OperationType operationType = operationAnnotation.type();
				String czlx = operationType.getType();
				Log log = new Log();
				log.setGlbm(glbm);
				log.setIp(ip);
				log.setYhdh(yhdh);
				log.setCzlx(czlx);
				String emsg = ex.getMessage();
				if (emsg != null && !"".equals(emsg))
					emsg = emsg.replaceAll("\'", "").replaceAll("\"", "");
				log.setCznr(description + "失败,异常:" + emsg);
				this.addLog(log);
				logger.info(description + "失败,异常:" + ex.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void addLog(Log log) throws Exception {

		this.logManager.saveLog(log);
	}

	public String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getRemoteAddr();
	}

	/**
	 * 操作内容
	 * 
	 * @param args
	 * @return
	 */
	private String getCznr(Object[] args,Object result) {
		String cznr = "";
		Object obj;
		Method method;
		try {
			for (Object o : args) {
				// 参数类型为Entity
				if (o != null) {
					if (o.getClass().getGenericSuperclass()
							.equals(Entity.class)) {
						Method[] methods = o.getClass().getMethods();
						for (Method m : methods) {
							Annotation[] methodAnnotations = m.getAnnotations();
							for(Annotation me : methodAnnotations){
							
								XmlElement xmlElement = (XmlElement)me;
								
								// 获取号牌号牌
								if (m.getName().equals("getHphm")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals(""))
										cznr +=  xmlElement.name() + "：" + obj.toString() + " ";
								}
								// 获取查询开始时间
								if (m.getName().equals("getKssj")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals(""))
										cznr +=  xmlElement.name() + "：" + obj.toString() + " ";
								}
								// 获取查询结束时间
								if (m.getName().equals("getJssj")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals(""))
										cznr += xmlElement.name() + "：" + obj.toString() + " ";
								}
								// 获取布控序号
								if (m.getName().equals("getBkxh")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals(""))
										cznr += xmlElement.name() + "：" + obj.toString() + " ";
								}
								// 获取报警序号
								if (m.getName().equals("getBjxh")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals(""))
										cznr += xmlElement.name() + "：" + obj.toString() + " ";
								}
								// 获取布控大类
								if (m.getName().equals("getBkdl")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals(""))
										cznr += xmlElement.name() + "：" + systemDao.getCodeValue("120019",obj.toString()) + " ";
								}
								// 获取布控类别
								if (m.getName().equals("getBklb")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals(""))
										cznr += xmlElement.name() + "：" + systemDao.getCodeValue("120005",obj.toString()) + " ";
								}
								// 获取号牌种类
								if (m.getName().equals("getHpzl")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals(""))
										cznr += xmlElement.name() + "：" + systemDao.getCodeValue("030107",obj.toString()) + " ";
								}
								// 获取布控范围类型
								if (m.getName().equals("getBkfwlx")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals(""))
										cznr += xmlElement.name() + "：" + systemDao.getCodeValue("120004",obj.toString()) + " ";
								}
								// 获取核查结果
								if (m.getName().equals("getHcjg")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals("")){
										String hcjg = systemDao.getCodeValue("100010",obj.toString());
											if(null != hcjg && !"".equals(hcjg)){
												cznr += xmlElement.name() + "：" + hcjg + " ";
											}else{
												cznr +=xmlElement.name() + "：未审核";
											}
									}
								}
								
								// 获取报警大类
								if (m.getName().equals("getBjdl")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals(""))
										cznr += xmlElement.name() + "：" + systemDao.getCodeValue("120019",obj.toString()) + " ";
								}
								// 获取报警类别
								if (m.getName().equals("getBjlx")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals(""))
										cznr += xmlElement.name() + "：" + systemDao.getCodeValue("120005",obj.toString()) + " ";
								}
								// 获取卡口名称
								if (m.getName().equals("getKdbh")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals(""))
										cznr += xmlElement.name() + "：" + gateManager.getGateName(obj.toString()) + " ";
								}
								
								// 获取是否反馈
								if (m.getName().equals("getSffk")) {
									obj = m.invoke(o);
									if (obj != null&&!obj.equals("")){
										StringBuffer sffk = new StringBuffer();
										if(obj.toString().equals("0")){
											sffk.append("未反馈");
										}else if(obj.toString().equals("1")){
											sffk.append("已反馈");
										}
										cznr += xmlElement.name() + "：" + sffk.toString() + " ";
									}
										
								}
								
								
							}
						// 参数类型为Map
						}
					} else if (o.getClass().equals(HashMap.class)) {
						method = o.getClass().getMethod("get", Object.class);
						// 获取号牌号牌
						obj = method.invoke(o, "hphm");
						if (obj != null)
							cznr += "号牌号码：" + obj.toString() + " ";

						// 获取开始时间
						obj = method.invoke(o, "kssj");
						if (obj != null&&!obj.equals(""))
							cznr += "开始时间：" + obj.toString() + " ";

						// 获取结束时间
						obj = method.invoke(o, "jssj");
						if (obj != null&&!obj.equals(""))
							cznr += "结束时间：" + obj.toString() + " ";
						
						// 获取布控序号
						obj = method.invoke(o, "bkxh");
						if (obj != null&&!obj.equals(""))
							cznr += "布控序号：" + obj.toString() + " ";
						// 获取报警序号
						obj = method.invoke(o, "bjxh");
						if (obj != null&&!obj.equals(""))
							cznr += "报警序号：" + obj.toString() + " ";
						// 获取布控大类
						obj = method.invoke(o, "bkdl");
						if (obj != null&&!obj.equals(""))
							cznr += "布控大类：" + systemDao.getCodeValue("120019",obj.toString()) + " ";
						// 获取布控类别
						obj = method.invoke(o, "bklb");
						if (obj != null&&!obj.equals(""))
							cznr += "布控类别：" + systemDao.getCodeValue("120005",obj.toString()) + " ";
						// 获取号牌种类
						obj = method.invoke(o, "hpzl");
						if (obj != null&&!obj.equals(""))
							cznr += "号牌种类：" + systemDao.getCodeValue("030107",obj.toString()) + " ";
						// 获取布控范围类型
						obj = method.invoke(o, "bkfwlx");
						if (obj != null&&!obj.equals(""))
							cznr += "布控范围类型：" + systemDao.getCodeValue("120004",obj.toString()) + " ";
						// 获取确认状态
						obj = method.invoke(o, "qrzt");
						if (obj != null&&!obj.equals(""))
							cznr += "确认状态：" + systemDao.getCodeValue("120014",obj.toString()) + " ";
						// 获取核查结果
						obj = method.invoke(o, "hcjg");
						if (obj != null&&!obj.equals("")){
							if (obj != null&&!obj.equals("")){
								String hcjg = systemDao.getCodeValue("100010",obj.toString());
									if(null != hcjg && !"".equals(hcjg)){
										cznr += "核查结果：" + hcjg + " ";
									}else{
										cznr += "核查结果：未审核 ";
									}
							}
						}
						
						// 获取报警大类
						obj = method.invoke(o, "bjdl");
						if (obj != null&&!obj.equals(""))
							cznr += "报警大类：" + systemDao.getCodeValue("120019",obj.toString()) + " ";
						// 获取报警类别
						obj = method.invoke(o, "bjlx");
						if (obj != null&&!obj.equals(""))
							cznr += "报警类别：" + systemDao.getCodeValue("120005",obj.toString()) + " ";
						// 获取报警类别
						obj = method.invoke(o, "kssj_bjsj");
						if (obj != null&&!obj.equals(""))
							cznr += "报警开始时间：" + obj.toString() + " ";
						// 获取报警类别
						obj = method.invoke(o, "jssj_bjsj");
						if (obj != null&&!obj.equals(""))
							cznr += "报警结束时间：" + obj.toString() + " ";
						// 获取确认开始时间
						obj = method.invoke(o, "jssj_qrsj");
						if (obj != null&&!obj.equals(""))
							cznr += "确认开始时间：" + obj.toString() + " ";
						// 获取确认结束时间
						obj = method.invoke(o, "kssj_qrsj");
						if (obj != null&&!obj.equals(""))
							cznr += "确认结束时间：" + obj.toString() + " ";
						// 获取反馈签收开始时间
						obj = method.invoke(o, "kssj_lrsj");
						if (obj != null&&!obj.equals(""))
							cznr += "开始时间：" + obj.toString() + " ";
						// 获取反馈签收开结束时间
						obj = method.invoke(o, "jssj_lrsj");
						if (obj != null&&!obj.equals(""))
							cznr += "结束时间：" + obj.toString() + " ";
						// 获取卡点所在地
						obj = method.invoke(o, "city");
						if (obj != null&&!obj.equals("")){
							String city = systemDao.getCodeValue("105000",obj.toString());
							if(null != city && !"".equals(city)){
								cznr += "卡口所在地：" + city + " ";
							}else{
								cznr += "卡口所在地：" + systemDao.getCodeValue("000033",obj.toString()) + " ";
							}
						}
						// 获取卡点名称
						obj = method.invoke(o, "kdbh");
						if (obj != null&&!obj.equals(""))
							cznr += "卡口名称：" + gateManager.getGateName(obj.toString()) + " ";
						// 获取指令状态
						obj = method.invoke(o, "sfxdzl");
						if (obj != null&&!obj.equals("")){
							StringBuffer sfxdzl = new StringBuffer();
							if(obj.toString().equals("0")){
								sfxdzl.append("未下达");
							}else if(obj.toString().equals("1")){
								sfxdzl.append("已下达");
							}
							cznr += "指令状态：" + sfxdzl.toString() + " ";
						}
						// 获取反馈状态
						obj = method.invoke(o, "by1");
						if (obj != null&&!obj.equals("")){
							StringBuffer sfxdzl = new StringBuffer();
							if(obj.toString().equals("0")){
								sfxdzl.append("未反馈");
							}else if(obj.toString().equals("1")){
								sfxdzl.append("已反馈");
							}
							cznr += "是否反馈：" + sfxdzl.toString() + " ";
						}
						obj = method.invoke(o, "sffk");
						if (obj != null&&!obj.equals("")){
							StringBuffer sfxdzl = new StringBuffer();
							if(obj.toString().equals("0")){
								sfxdzl.append("未反馈");
							}else if(obj.toString().equals("1")){
								sfxdzl.append("已反馈");
							}
							cznr += "是否反馈：" + sfxdzl.toString() + " ";
						}
						
						// 获取拦截状态
						obj = method.invoke(o, "sflj");
						if (obj != null&&!obj.equals("")){
							StringBuffer sfxdzl = new StringBuffer();
							if(obj.toString().equals("0")){
								sfxdzl.append("未拦截到");
							}else if(obj.toString().equals("1")){
								sfxdzl.append("已拦截到");
							}
							cznr += "指令状态：" + sfxdzl.toString() + " ";
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cznr;
	}
}
