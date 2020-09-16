 package com.sunshine.monitor.system.activemq.bean;
 
 import java.io.Serializable;
 
 public class TransObj implements Serializable {
   private static final long serialVersionUID = 9019911020391688211L;
   private String type;
   private TransBean obj;
   private String csxh;
   private String csdw;
   private String jsdw;
   private Double csbj;
   private String cssj;
   private String ywxh;
   private String sn;
   private Double ddbj;
   private String ddsj;
 
   public String getType()
   {
     return this.type;
   }
 
   public void setType(String type)
   {
     this.type = type;
   }
 
   public TransBean getObj() {
     return this.obj;
   }
 
   public void setObj(TransBean obj)
   {
     this.obj = obj;
   }
 
   public String getSn() {
     return this.sn;
   }
 
   public void setSn(String sn)
   {
     this.sn = sn;
   }
 
   public String getCsxh() {
     return this.csxh;
   }
 
   public void setCsxh(String csxh)
   {
     this.csxh = csxh;
   }
 
   public String getCsdw() {
     return this.csdw;
   }
 
   public void setCsdw(String csdw)
   {
     this.csdw = csdw;
   }
 
   public String getJsdw() {
     return this.jsdw;
   }
 
   public void setJsdw(String jsdw)
   {
     this.jsdw = jsdw;
   }
 
   public Double getCsbj() {
     return this.csbj;
   }
 
   public void setCsbj(Double csbj)
   {
     this.csbj = csbj;
   }
 
   public String getCssj() {
     return this.cssj;
   }
 
   public void setCssj(String cssj)
   {
     this.cssj = cssj;
   }
   public String getYwxh() {
     return this.ywxh;
   }
   public void setYwxh(String ywxh) {
     this.ywxh = ywxh;
   }

	public Double getDdbj() {
		return ddbj;
	}

	public void setDdbj(Double ddbj) {
		this.ddbj = ddbj;
	}

	public String getDdsj() {
		return ddsj;
	}
	
	public void setDdsj(String ddsj) {
		this.ddsj = ddsj;
	}
   
 }

