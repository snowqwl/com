package com.sunshine.monitor.system.alarm.bean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.StringUtils;

import com.sunshine.monitor.comm.bean.Entity;
import com.sunshine.monitor.comm.util.CNCharacterUtils;

public class VehAlarmrec extends Entity{

	private static final long serialVersionUID = -588136819340726533L;
	private String bjxh;
	private String bjdl;
	private String bjlx;
	private String bkxh;
	private String bjsj;
	private String bjdwdm;
	private String bjdwmc;
	private String bjdwlxdh;
	private String hphm;
	private String hpzl;
	private String gcxh;
	private String gcsj;
	private String sbbh;
	private String sbmc;
	private String kdbh;
	private String kdmc;
	
	private String fxbh;
	private String fxlx;
	private String fxmc;
	private String cllx;
	private Long clsd;
	private String hpys;
	private String cwhphm;
	private String cwhpys;
	private String hpyz;
	private String cdbh;
	private String clwx;
	private String csys;
	private String tp1;
	private String tp2;
	private String tp3;
	private String qrr;
	private String qrrjh;
	private String qrrmc;
	private String qrdwdm;
	private String qrdwdmmc;
	private String qrdwlxdh;
	private String qrsj;
	private String qrzt;
	private String qrjg;
	private String gxsj;
	private String jyljtj;
	private String xxly;
	private String by1;
	private String by2;
	private String by3;
	private String by4;
	private String by5;
	private String zldwlxdh;
	private String sfxdzl;
	private String sffk;
	private String sflj;
	public String xdzl;
	public String qrztmc;
	public String ljtjmc;
	public String bjlxmc;
	public String bjdlmc;
	public String hpzlmc;
	public String hpysmc;
	public String csysmc;
	public String bkjb;
	public String bkjg;
	public String sfxdzlmc;
	public String sffkmc;
	public String sfljmc;
	public String xxlymc;
	public String fkbh;
	//---确认人单位警种-----
	public String qrrdwjz;

	public String getQrrdwjz() {
		return qrrdwjz;
	}

	public void setQrrdwjz(String qrrdwjz) {
		this.qrrdwjz = qrrdwjz;
	}
	
	@XmlElement(name="报警序号")
	public String getBjxh() {
		return this.bjxh;
	}

	public void setBjxh(String bjxh) {
		this.bjxh = bjxh;
	}

	public String getBjdl() {
		return this.bjdl;
	}

	public void setBjdl(String bjdl) {
		this.bjdl = bjdl;
	}

	public String getBjlx() {
		return this.bjlx;
	}

	public void setBjlx(String bjlx) {
		this.bjlx = bjlx;
	}
	
	@XmlElement(name="布控序号")
	public String getBkxh() {
		return this.bkxh;
	}

	public void setBkxh(String bkxh) {
		this.bkxh = bkxh;
	}

	public String getBjsj() {
		return this.bjsj;
	}

	public void setBjsj(String bjsj) {
		this.bjsj = bjsj;
	}

	public String getBjdwdm() {
		return this.bjdwdm;
	}

	public void setBjdwdm(String bjdwdm) {
		this.bjdwdm = bjdwdm;
	}

	public String getBjdwmc() {
		return this.bjdwmc;
	}

	public void setBjdwmc(String bjdwmc) {
		this.bjdwmc = bjdwmc;
	}

	public String getBjdwlxdh() {
		return this.bjdwlxdh;
	}

	public void setBjdwlxdh(String bjdwlxdh) {
		this.bjdwlxdh = bjdwlxdh;
	}

	public String getHphm() {
		return this.hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public String getHpzl() {
		return this.hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}

	public String getGcxh() {
		return this.gcxh;
	}

	public void setGcxh(String gcxh) {
		this.gcxh = gcxh;
	}

	public String getGcsj() {
		return this.gcsj;
	}

	public void setGcsj(String gcsj) {
		this.gcsj = gcsj;
	}

	public String getSbbh() {
		return this.sbbh;
	}

	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}

	public String getSbmc() {
		return this.sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public String getKdbh() {
		return kdbh;
	}

	public void setKdbh(String kdbh) {
		this.kdbh = kdbh;
	}

	public String getFxlx() {
		return fxlx;
	}

	public void setFxlx(String fxlx) {
		this.fxlx = fxlx;
	}

	public String getKdmc() {
		return this.kdmc;
	}

	public void setKdmc(String kdmc) {
		this.kdmc = kdmc;
	}




	public String getFxmc() {
		return this.fxmc;
	}

	public void setFxmc(String fxmc) {
		this.fxmc = fxmc;
	}

	public String getCllx() {
		return this.cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public Long getClsd() {
		return this.clsd;
	}

	public void setClsd(Long clsd) {
		this.clsd = clsd;
	}

	public String getHpys() {
		return this.hpys;
	}

	public void setHpys(String hpys) {
		this.hpys = hpys;
	}

	public String getCwhphm() {
		return this.cwhphm;
	}

	public void setCwhphm(String cwhphm) {
		this.cwhphm = cwhphm;
	}

	public String getCwhpys() {
		return this.cwhpys;
	}

	public void setCwhpys(String cwhpys) {
		this.cwhpys = cwhpys;
	}

	public String getHpyz() {
		return this.hpyz;
	}

	public void setHpyz(String hpyz) {
		this.hpyz = hpyz;
	}

	public String getCdbh() {
		return this.cdbh;
	}

	public void setCdbh(String cdbh) {
		this.cdbh = cdbh;
	}

	public String getClwx() {
		return this.clwx;
	}

	public void setClwx(String clwx) {
		this.clwx = clwx;
	}

	public String getCsys() {
		return this.csys;
	}

	public void setCsys(String csys) {
		this.csys = csys;
	}
	
	private String ftpURLtoGBK(String url){
		try {
			if(StringUtils.isNotBlank(url)
					&& url.startsWith("ftp")){
				return CNCharacterUtils.encodeChineseChar(url,"GBK");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}

	public String getTp1() {
		 return ftpURLtoGBK(this.tp1);
	}
	
	
	public void setTp1(String tp1) {
		this.tp1 = tp1;
	}

	public String getTp2() {
		return ftpURLtoGBK(this.tp2);
	}

	public void setTp2(String tp2) {
		this.tp2 = tp2;
	}

	public String getTp3() {
		return ftpURLtoGBK(this.tp3);
	}

	public void setTp3(String tp3) {
		this.tp3 = tp3;
	}

	public String getQrr() {
		return this.qrr;
	}

	public void setQrr(String qrr) {
		this.qrr = qrr;
	}

	public String getQrrjh() {
		return this.qrrjh;
	}

	public void setQrrjh(String qrrjh) {
		this.qrrjh = qrrjh;
	}

	public String getQrdwdm() {
		return this.qrdwdm;
	}

	public void setQrdwdm(String qrdwdm) {
		this.qrdwdm = qrdwdm;
	}

	public String getQrdwdmmc() {
		return this.qrdwdmmc;
	}

	public void setQrdwdmmc(String qrdwdmmc) {
		this.qrdwdmmc = qrdwdmmc;
	}

	public String getQrdwlxdh() {
		return this.qrdwlxdh;
	}

	public void setQrdwlxdh(String qrdwlxdh) {
		this.qrdwlxdh = qrdwlxdh;
	}

	public String getQrsj() {
		return this.qrsj;
	}

	public void setQrsj(String qrsj) {
		this.qrsj = qrsj;
	}

	public String getQrzt() {
		return this.qrzt;
	}

	public void setQrzt(String qrzt) {
		this.qrzt = qrzt;
	}

	public String getQrjg() {
		return this.qrjg;
	}

	public void setQrjg(String qrjg) {
		this.qrjg = qrjg;
	}

	public String getGxsj() {
		return this.gxsj;
	}

	public void setGxsj(String gxsj) {
		this.gxsj = gxsj;
	}

	public String getJyljtj() {
		return this.jyljtj;
	}

	public void setJyljtj(String jyljtj) {
		this.jyljtj = jyljtj;
	}

	public String getXxly() {
		return this.xxly;
	}

	public void setXxly(String xxly) {
		this.xxly = xxly;
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

	public String getBy5() {
		return this.by5;
	}

	public void setBy5(String by5) {
		this.by5 = by5;
	}

	public String getZldwlxdh() {
		return this.zldwlxdh;
	}

	public void setZldwlxdh(String zldwlxdh) {
		this.zldwlxdh = zldwlxdh;
	}

	public String getXdzl() {
		return this.xdzl;
	}

	public void setXdzl(String xdzl) {
		this.xdzl = xdzl;
	}

	public String getQrztmc() {
		return this.qrztmc;
	}

	public void setQrztmc(String qrztmc) {
		this.qrztmc = qrztmc;
	}

	public String getLjtjmc() {
		return this.ljtjmc;
	}

	public void setLjtjmc(String ljtjmc) {
		this.ljtjmc = ljtjmc;
	}

	public String getBjlxmc() {
		return this.bjlxmc;
	}

	public void setBjlxmc(String bjlxmc) {
		this.bjlxmc = bjlxmc;
	}

	public String getBjdlmc() {
		return this.bjdlmc;
	}

	public void setBjdlmc(String bjdlmc) {
		this.bjdlmc = bjdlmc;
	}

	public String getHpzlmc() {
		return this.hpzlmc;
	}

	public void setHpzlmc(String hpzlmc) {
		this.hpzlmc = hpzlmc;
	}

	public String getHpysmc() {
		return this.hpysmc;
	}

	public void setHpysmc(String hpysmc) {
		this.hpysmc = hpysmc;
	}

	public String getCsysmc() {
		return this.csysmc;
	}

	public void setCsysmc(String csysmc) {
		this.csysmc = csysmc;
	}

	public String getBkjb() {
		return this.bkjb;
	}

	public void setBkjb(String bkjb) {
		this.bkjb = bkjb;
	}

	public String getBkjg() {
		return this.bkjg;
	}

	public void setBkjg(String bkjg) {
		this.bkjg = bkjg;
	}

	public String getSfxdzl() {
		return this.sfxdzl;
	}

	public void setSfxdzl(String sfxdzl) {
		this.sfxdzl = sfxdzl;
	}

	public String getSffk() {
		return this.sffk;
	}

	public void setSffk(String sffk) {
		this.sffk = sffk;
	}

	public String getSflj() {
		return this.sflj;
	}

	public void setSflj(String sflj) {
		this.sflj = sflj;
	}

	public String getSfxdzlmc() {
		return this.sfxdzlmc;
	}

	public void setSfxdzlmc(String sfxdzlmc) {
		this.sfxdzlmc = sfxdzlmc;
	}

	public String getSffkmc() {
		return this.sffkmc;
	}

	public void setSffkmc(String sffkmc) {
		this.sffkmc = sffkmc;
	}

	public String getSfljmc() {
		return this.sfljmc;
	}

	public void setSfljmc(String sfljmc) {
		this.sfljmc = sfljmc;
	}

	public String getQrrmc() {
		return this.qrrmc;
	}

	public void setQrrmc(String qrrmc) {
		this.qrrmc = qrrmc;
	}

	public String getXxlymc() {
		return this.xxlymc;
	}

	public void setXxlymc(String xxlymc) {
		this.xxlymc = xxlymc;
	}
	
	
	public String getFkbh() {
		return fkbh;
	}

	public void setFkbh(String fkbh) {
		this.fkbh = fkbh;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String gbkStr = "ftp://abc:abc@abc/img-粤H00001.jpg";
		gbkStr = CNCharacterUtils.encodeChineseChar(gbkStr,"GBK");
		System.out.println(gbkStr);
	}

	public String getFxbh() {
		return fxbh;
	}

	public void setFxbh(String fxbh) {
		this.fxbh = fxbh;
	}	

}
