package com.sunshine.monitor.system.analysis.bean;

import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;
import com.sunshine.core.util.DateUtil;
import com.sunshine.monitor.comm.bean.Entity;
import com.sunshine.monitor.system.analysis.dto.TrafficFlowDTO;

/**
 * 流通流量分析:流量模型
 * 
 * @author OUYANG
 * 
 */
public class TrafficFlow extends Entity {

	private static final long serialVersionUID = 5795786726528063110L;

	private String kdbh;

	private String staticType;

	private String analysisType;

	private String kssj;

	private String jssj;

	private String hpys;

	private String cllx;

	private String csys;

	private String displayType;

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat monthFormat = new SimpleDateFormat(
			"yyyy-MM");

	public TrafficFlow() {
	}

	public TrafficFlow(TrafficFlowDTO dto) {
		this.kdbh = dto.getKdbh();
		this.analysisType = dto.getAnalysisType();
		this.staticType = dto.getStaticType();
		this.kssj = dto.getKssj();
		this.jssj = dto.getJssj();
		this.cllx = dto.getCllx();
		this.csys = dto.getCsys();
		this.hpys = dto.getHpys();
		this.displayType = dto.getDisplayType();
	}

	public String getKdbh() {
		return kdbh;
	}

	public void setKdbh(String kdbh) {
		this.kdbh = kdbh;
	}

	public String getStaticType() {
		return staticType;
	}

	public void setStaticType(String staticType) {
		this.staticType = staticType;
	}

	public String getAnalysisType() {
		return analysisType;
	}

	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}

	public String getKssj() {
		return kssj;
	}

	public void setKssj(String kssj) {
		this.kssj = kssj;
	}

	public String getJssj() {
		return jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getHpys() {
		return hpys;
	}

	public void setHpys(String hpys) {
		this.hpys = hpys;
	}

	public String getCllx() {
		return cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public String getCsys() {
		return csys;
	}

	public void setCsys(String csys) {
		this.csys = csys;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	/**
	 * 判断统计类型
	 * 
	 * @return
	 */
	public boolean isHourDisplay() {
		if ("H".equals(staticType.toUpperCase())) {
			return true;
		}
		return false;
	}

	/**
	 * 按总量显示
	 * 
	 * @return
	 */
	public boolean displayType() {
		if ("0".equals(displayType) || StringUtils.isBlank(displayType))
			return true;
		return false;
	}

	/**
	 * 上一天
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countRelativeDay() throws Exception {
		
		return DateUtil.countBeforeDay(kssj);
	}

	/**
	 * 上一月
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countRelativeMonth() throws Exception {
		
		return DateUtil.countBeforeMonth(kssj);
	}
	
	/**
	 * 上一月(含天)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countRelativeMonthDay() throws Exception {
		
		return DateUtil.countBeforeMonthDay(kssj);
	}
	
	/**
	 * 浅复制
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
