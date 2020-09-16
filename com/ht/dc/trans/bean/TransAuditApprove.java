 package com.ht.dc.trans.bean; 
 import java.lang.reflect.Field;
 
 public class TransAuditApprove extends TransBean
 {
   public static final long serialVersionUID = -1942409761002835045L;
   private String xh;
   private String bkxh;
   private String czr;
   private String czrdw;
   private String czrdwmc;
   private String czsj;
   private String czjg;
   private String ms;
   private String bzw;
   private String by1;
   private String by2;
   private String czrjh;
   private String czrmc;
 
   public String getXh()
   {
     return this.xh;
   }
   public void setXh(String xh) {
     this.xh = xh;
   }
   public String getBkxh() {
     return this.bkxh;
   }
   public void setBkxh(String bkxh) {
     this.bkxh = bkxh;
   }
   public String getCzr() {
     return this.czr;
   }
   public void setCzr(String czr) {
     this.czr = czr;
   }
   public String getCzrdw() {
     return this.czrdw;
   }
   public void setCzrdw(String czrdw) {
     this.czrdw = czrdw;
   }
   public String getCzrdwmc() {
     return this.czrdwmc;
   }
   public void setCzrdwmc(String czrdwmc) {
     this.czrdwmc = czrdwmc;
   }
   public String getCzsj() {
     return this.czsj;
   }
   public void setCzsj(String czsj) {
     this.czsj = czsj;
   }
   public String getCzjg() {
     return this.czjg;
   }
   public void setCzjg(String czjg) {
     this.czjg = czjg;
   }
   public String getMs() {
     return this.ms;
   }
   public void setMs(String ms) {
     this.ms = ms;
   }
   public String getBzw() {
     return this.bzw;
   }
   public void setBzw(String bzw) {
     this.bzw = bzw;
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
   public String getCzrjh() {
     return this.czrjh;
   }
   public void setCzrjh(String czrjh) {
     this.czrjh = czrjh;
   }
   public String getCzrmc() {
     return this.czrmc;
   }
   public void setCzrmc(String czrmc) {
     this.czrmc = czrmc;
   }
 
   public String toXmlString() throws Exception {
     StringBuffer sb = new StringBuffer();
     sb.append("<AUDIT>");
     Class c = getClass();
     Field[] properties = c.getDeclaredFields();
     for (int i = 0; i < properties.length; i++) {
       if (properties[i].getModifiers() == 2) {
         properties[i].setAccessible(true);
         Object tmp = properties[i].get(this);
         String strTmp = "";
         if (tmp != null) {
           if ((tmp instanceof String))
             strTmp = (String)tmp;
           else if ((tmp instanceof Long)) {
             strTmp = String.valueOf(tmp);
           }
         }
         if ((strTmp != null) && (!"".equals(strTmp))) {
           String name = properties[i].getName().toUpperCase();
           sb.append("<").append(name).append(">").append(strTmp).append("</").append(name).append(">");
         }
       }
     }
     sb.append("</AUDIT>");
     return sb.toString();
   }
 }
