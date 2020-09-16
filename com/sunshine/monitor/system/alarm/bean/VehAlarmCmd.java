package com.sunshine.monitor.system.alarm.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.sunshine.monitor.comm.bean.Entity;


public class VehAlarmCmd  extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String zlxh;
	private String bjxh;
	private String zlr;
	private String zlrjh;
	private String zldw;
	private String zldwmc;
	private String zlsj;
	private String zlfs;
	private String zlrmc;
	private String zlnr;
	private String by1;
	private String by2;
	private String xh;
	private String zljsdw;
	private String zldwlxdh;
	private String sffk;
	private String sfqs;
	private String by3;
	private String by4;
	private String zljsdwmc;
	private String xxly;
	private String gxsj;
	public String hpzl;
	public String hphm;
	public String bjsj;
	/**
	 * kdbh->kkbh
	 */
	public String kdbh;
	public String bjdl;
	public String bjlx;
	public String zlfw;
	public List<VehAlarmCmd> zlfwList;
	public String kdmc;
	public String sffkmc;
	public String sfqsmc;
	public String zlfsmc;
	public String kssj;
	public String jssj;
	public String city;
	
	@XmlElement(name="卡口名称")
	public String getKdbh() {
		return kdbh;
	}

	public void setKdbh(String kdbh) {
		this.kdbh = kdbh;
	}
	
	@XmlElement(name="开始时间")
	public String getKssj() {
		return kssj;
	}

	public void setKssj(String kssj) {
		this.kssj = kssj;
	}
	
	
	@XmlElement(name="结束时间")
	public String getJssj() {
		return jssj;
	}
	
	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getZlfsmc() {
		return this.zlfsmc;
	}

	public void setZlfsmc(String zlfsmc) {
		this.zlfsmc = zlfsmc;
	}

	public String getSfqsmc() {
		return this.sfqsmc;
	}

	public void setSfqsmc(String sfqsmc) {
		this.sfqsmc = sfqsmc;
	}

	public String getSffkmc() {
		return this.sffkmc;
	}

	public void setSffkmc(String sffkmc) {
		this.sffkmc = sffkmc;
	}

	public String getKdmc() {
		return this.kdmc;
	}

	public void setKdmc(String kdmc) {
		this.kdmc = kdmc;
	}
	
	@XmlElement(name="报警大类")
	public String getBjdl() {
		return this.bjdl;
	}

	public void setBjdl(String bjdl) {
		this.bjdl = bjdl;
	}
	
	@XmlElement(name="报警类别")
	public String getBjlx() {
		return this.bjlx;
	}

	public void setBjlx(String bjlx) {
		this.bjlx = bjlx;
	}

	public String getZlxh() {
		return this.zlxh;
	}

	public void setZlxh(String zlxh) {
		this.zlxh = zlxh;
	}
	
	@XmlElement(name="报警序号")
	public String getBjxh() {
		return this.bjxh;
	}

	public void setBjxh(String bjxh) {
		this.bjxh = bjxh;
	}

	public String getZlr() {
		return this.zlr;
	}

	public void setZlr(String zlr) {
		this.zlr = zlr;
	}

	public String getZldw() {
		return this.zldw;
	}

	public void setZldw(String zldw) {
		this.zldw = zldw;
	}

	public String getZldwmc() {
		return this.zldwmc;
	}

	public void setZldwdmc(String zldwmc) {
		this.zldwmc = zldwmc;
	}

	public String getZlsj() {
		return this.zlsj;
	}

	public void setZlsj(String zlsj) {
		this.zlsj = zlsj;
	}

	public String getZlnr() {
		return this.zlnr;
	}

	public void setZlnr(String zlnr) {
		this.zlnr = zlnr;
	}

	public String getBy1() {
		return this.by1;
	}

	public void setBy1(String by1) {
		this.by1 = by1;
	}

	public String getBy2() {
		return this.by2;
	}

	public void setBy2(String by2) {
		this.by2 = by2;
	}

	public String getXh() {
		return this.xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getZljsdw() {
		return this.zljsdw;
	}

	public void setZljsdw(String zljsdw) {
		this.zljsdw = zljsdw;
	}
	
	@XmlElement(name="是否反馈")
	public String getSffk() {
		return this.sffk;
	}

	public void setSffk(String sffk) {
		this.sffk = sffk;
	}

	public String getZljsdwmc() {
		return this.zljsdwmc;
	}

	public void setZljsdwmc(String zljsdwmc) {
		this.zljsdwmc = zljsdwmc;
	}

	public String getBy3() {
		return this.by3;
	}

	public void setBy3(String by3) {
		this.by3 = by3;
	}

	public String getBy4() {
		return this.by4;
	}

	public void setBy4(String by4) {
		this.by4 = by4;
	}
	
	@XmlElement(name="号码种类")
	public String getHpzl() {
		return this.hpzl;
	}
	
	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}
	
	@XmlElement(name="号牌号码")
	public String getHphm() {
		return this.hphm;
	}
	
	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public String getBjsj() {
		return this.bjsj;
	}

	public void setBjsj(String bjsj) {
		this.bjsj = bjsj;
	}

	

	public void setZldwmc(String zldwmc) {
		this.zldwmc = zldwmc;
	}

	public void setZlfwList(List list) {
		this.zlfwList = list;
	}

	public String getZlfw() {
		StringBuffer sb = new StringBuffer();
		if (this.zlfwList != null) {
			int size = this.zlfwList.size();
			if (size > 0) {
				VehAlarmCmd bean = (VehAlarmCmd) this.zlfwList.get(0);
				sb.append(bean.getZljsdwmc());
				for (int i = 1; i < size; i++) {
					bean = (VehAlarmCmd) this.zlfwList.get(i);
					sb.append(",").append(bean.getZljsdw());
				}
			}
		}
		return sb.toString();
	}

	public String getZldwlxdh() {
		return this.zldwlxdh;
	}

	public void setZldwlxdh(String zldwlxdh) {
		this.zldwlxdh = zldwlxdh;
	}

	public String getZlrjh() {
		return this.zlrjh;
	}

	public void setZlrjh(String zlrjh) {
		this.zlrjh = zlrjh;
	}

	public String getXxly() {
		return this.xxly;
	}

	public void setXxly(String xxly) {
		this.xxly = xxly;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSfqs() {
		return this.sfqs;
	}

	public void setSfqs(String sfqs) {
		this.sfqs = sfqs;
	}

	public String getZlfs() {
		return this.zlfs;
	}

	public void setZlfs(String zlfs) {
		this.zlfs = zlfs;
	}

	public String getZlrmc() {
		return this.zlrmc;
	}

	public void setZlrmc(String zlrmc) {
		this.zlrmc = zlrmc;
	}

	public String getGxsj() {
		return this.gxsj;
	}

	public void setGxsj(String gxsj) {
		this.gxsj = gxsj;
	}

}
