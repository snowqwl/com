package com.sunshine.monitor.system.redlist.bean;

import com.sunshine.monitor.comm.bean.Entity;

/**
 * 
 * @author OUYANG 2013/7/22
 * 
 */
public class RedList extends Entity {
	
	private static final long serialVersionUID = 1L;

	private String id ;
	
	private String hphm ;
	
	private String hpzl ;
	
	private String clxh ;
	
	private String clpp ;
	
	private String  cllx ;
	
	private String hpys ;
	
	private String csys ;
	
	private String clsyr ;
	
	private String syrlxdh ;
	
	private String syrxxdz ;
	
	private String kssj ;
	
	private String jssj ;
	
	/**
	 * 审批状态(0=初始状态、1=审批状态)
	 */
	private String status ;
	
	/**
	 * 审批人
	 */
	private String auditman ;
	
	/**
	 * 审批单位
	 */
	private String auditdept ;
	
	/**
	 * 审批时间
	 */
	private String auditdate ;	
	
	/**
	 * 是否有效
	 */
	private String isvalid ;
	
	//------------------
	public String hpzlmc;
	public String hpysmc;
	public String cllxmc;
	public String csysmc;
	

	public String getHpzlmc() {
		return hpzlmc;
	}

	public void setHpzlmc(String hpzlmc) {
		this.hpzlmc = hpzlmc;
	}

	public String getHpysmc() {
		return hpysmc;
	}

	public void setHpysmc(String hpysmc) {
		this.hpysmc = hpysmc;
	}

	public String getCllxmc() {
		return cllxmc;
	}

	public void setCllxmc(String cllxmc) {
		this.cllxmc = cllxmc;
	}

	public String getCsysmc() {
		return csysmc;
	}

	public void setCsysmc(String csysmc) {
		this.csysmc = csysmc;
	}

	public String getHphm() {
		return hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public String getHpzl() {
		return hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}

	public String getClxh() {
		return clxh;
	}

	public void setClxh(String clxh) {
		this.clxh = clxh;
	}

	public String getClpp() {
		return clpp;
	}

	public void setClpp(String clpp) {
		this.clpp = clpp;
	}

	public String getCllx() {
		return cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public String getHpys() {
		return hpys;
	}

	public void setHpys(String hpys) {
		this.hpys = hpys;
	}

	public String getCsys() {
		return csys;
	}

	public void setCsys(String csys) {
		this.csys = csys;
	}

	public String getClsyr() {
		return clsyr;
	}

	public void setClsyr(String clsyr) {
		this.clsyr = clsyr;
	}

	public String getSyrlxdh() {
		return syrlxdh;
	}

	public void setSyrlxdh(String syrlxdh) {
		this.syrlxdh = syrlxdh;
	}

	public String getSyrxxdz() {
		return syrxxdz;
	}

	public void setSyrxxdz(String syrxxdz) {
		this.syrxxdz = syrxxdz;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAuditman() {
		return auditman;
	}

	public void setAuditman(String auditman) {
		this.auditman = auditman;
	}

	public String getAuditdept() {
		return auditdept;
	}

	public void setAuditdept(String auditdept) {
		this.auditdept = auditdept;
	}

	public String getAuditdate() {
		return auditdate;
	}

	public void setAuditdate(String auditdate) {
		this.auditdate = auditdate;
	}

	public String getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
		    return true;
		}
		if (obj instanceof RedList) {
			RedList rl = (RedList)obj;
			if(this.hphm.equals(rl.getHphm())){
				return true ;
			}
		}
		return false;
	}

	public int hashCode() {
		
		return (this.hphm.hashCode() + this.hpzl.hashCode()) * 123;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
}
