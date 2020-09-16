 package com.ht.dc.trans.bean; 
 import java.io.Serializable;
import java.lang.reflect.Field;
 
 public class TransBean
   implements Serializable
 {
   public static final long serialVersionUID = 4644484416109036694L;
 
   public String toXmlString()
     throws Exception
   {
     StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
     sb.append("<BEAN>");
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
           sb.append("<").append(name).append("><![CDATA[").append(strTmp).append("]]></").append(name).append(">");
         }
       }
     }
     sb.append("</BEAN>");
     return sb.toString();
   }
 
   public String toXmlBean() throws Exception {
     StringBuffer sb = new StringBuffer("");
     sb.append("<BEAN>");
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
           sb.append("<").append(name).append("><![CDATA[").append(strTmp).append("]]></").append(name).append(">");
         }
       }
     }
     sb.append("</BEAN>");
     return sb.toString();
   }
 }
