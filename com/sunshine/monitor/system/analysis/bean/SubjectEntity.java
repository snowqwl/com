package com.sunshine.monitor.system.analysis.bean;

import com.sunshine.monitor.comm.bean.Entity;

/**
 * 分析主题信息实体类
 *
 */
public class SubjectEntity extends Entity {

	private static final long serialVersionUID = 1L;
	
	//专案编号
	private String zabh;
	//分析主题编号
	private String ztbh;
	//分析主题名称
	private String fxztmc;
	//分析时间
	private String fxsj;
	//分析条件
	private String fxtj;
	//分析条件描述
	private String fxtjms;
	//分析结果简短描述
	private String jgms;
	//分析类型（1:同行分析，2:频繁出入分析，3:时空轨迹分析，4:昼伏夜出分析，5:套牌车辆分析，6:关联分析）
	private String fxlx;
	//备注
	private String bz;
	
	public String getZabh() {
		return zabh;
	}
	public void setZabh(String zabh) {
		this.zabh = zabh;
	}
	public String getZtbh() {
		return ztbh;
	}
	public void setZtbh(String ztbh) {
		this.ztbh = ztbh;
	}
	public String getFxsj() {
		return fxsj;
	}
	public void setFxsj(String fxsj) {
		this.fxsj = fxsj;
	}
	public String getFxtj() {
		return fxtj;
	}
	public void setFxtj(String fxtj) {
		this.fxtj = fxtj;
	}
	public String getJgms() {
		return jgms;
	}
	public void setJgms(String jgms) {
		this.jgms = jgms;
	}
	public String getFxlx() {
		return fxlx;
	}
	public void setFxlx(String fxlx) {
		this.fxlx = fxlx;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getFxtjms() {
		return fxtjms;
	}
	public void setFxtjms(String fxtjms) {
		this.fxtjms = fxtjms;
	}
	public String getFxztmc() {
		return fxztmc;
	}
	public void setFxztmc(String fxztmc) {
		this.fxztmc = fxztmc;
	}

}
