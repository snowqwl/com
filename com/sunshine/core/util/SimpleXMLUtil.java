package com.sunshine.core.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.xml.sax.InputSource;

public final class SimpleXMLUtil { 
	
   private SimpleXMLUtil(){	   
   }

   public static Document file2Doc(String xmlPath){
       return file2Doc(xmlPath,false);       
   }
   
   private synchronized static InputStream readFromJar(JarFile jar,String path){
	   InputStream is = null;
	   int p = path.indexOf(".jar");
	   int s = 6;
	   if(!path.startsWith("file:"))
		   s = 0;
	   String jpath = path.substring(s,p)+".jar";
	   int s2 = 6;
	   if(!path.contains(".jar!"))
		   s2 = 5;
	   String fpath = path.substring(p+s2);
	   try{
		   jar = new JarFile(jpath);
		   Enumeration<JarEntry> e = jar.entries();
			while(e.hasMoreElements()){
				JarEntry je = e.nextElement();
				if(je.getName().equals(fpath)){
					is = jar.getInputStream(je);
				}
			}
	   }catch(IOException e){
		   e.printStackTrace();
	   }
	   return is;
   }

   public static Document file2Doc(String xmlPath,boolean validate){
	   SAXBuilder builder = new SAXBuilder(validate);
	   Document doc = null;
	   JarFile jar = null;
	   try{
		   xmlPath = xmlPath.replace("%20", " ");
		   if(xmlPath.contains(".jar")){
			   doc = builder.build(readFromJar(jar,xmlPath));
		   }else{
			   doc = builder.build(new File(xmlPath));		   
		   }
       }catch(Exception e){
    	   e.printStackTrace();
       }finally{
    	   try {
				if(jar!=null)
					jar.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
       }
       return doc;       
   }

   public static Document file2Doc(InputStream stream,boolean validate){
	   SAXBuilder builder = new SAXBuilder(validate);
	   Document doc = null;
	   try{
		   doc = builder.build(stream);
       }catch(Exception e){
    	   e.printStackTrace();
       }
       return doc;       
   } 

   public static Document file2Doc(InputStream stream){
	   SAXBuilder builder = new SAXBuilder(false);
	   Document doc = null;
	   try{
		   doc = builder.build(stream);
       }catch(Exception e){
    	   e.printStackTrace();
       }
       return doc;       
   } 

   public static String doc2String(final Document doc){
	   try {   
		   ByteArrayOutputStream baos = new ByteArrayOutputStream();   
           PrintWriter pw = new PrintWriter(baos);   
           Format format = Format.getCompactFormat();   
           format.setEncoding("UTF-8");   
           XMLOutputter xmlop = new XMLOutputter();   
           xmlop.setFormat(format);   
           xmlop.output(doc, pw);
           
           return baos.toString();
       } catch (Exception e) {      
           e.printStackTrace();   
       } 
       return null;
   }
   
   public static String element2String(final Element element){
	   try {   
		   ByteArrayOutputStream baos = new ByteArrayOutputStream();   
           PrintWriter pw = new PrintWriter(baos);   
           Format format = Format.getCompactFormat();   
           format.setEncoding("UTF-8");   
           XMLOutputter xmlop = new XMLOutputter();   
           xmlop.setFormat(format);   
           xmlop.output(element, pw);
           return baos.toString();
       } catch (Exception e) {      
           e.printStackTrace();   
       } 
       return null;
   }
   
   public static Document string2Doc(final String xml){
	   Document doc = null;
	   try{
			StringReader sr = new StringReader(xml);
			InputSource is = new InputSource(sr);			
			doc = (new SAXBuilder()).build(is);
	   }catch(Exception e){
			e.printStackTrace();
	   }
	   return doc;
   }
   
   public static void updateXML(String xmlPath,Document doc){
	   try{		
		   xmlPath = xmlPath.replace("%20", " ");
		   Format format = Format.getCompactFormat();
		   format.setEncoding("UTF-8");
		   format.setIndent("	");
		   XMLOutputter serializer = new XMLOutputter(format);
		   FileOutputStream fos = new FileOutputStream(xmlPath);		  
		   serializer.output(doc, fos);
		   fos.close();
	   }catch(Exception e){
		   e.printStackTrace();
	   }      
   } 
   
   public static List<Element> getElements(String xmlPath,String xpathString){
	   return getElements(file2Doc(xmlPath).getRootElement(),xpathString);
   }
   
   /**
    * 根据节点XPATH查找节点列表
    * @param root
    * @param path
    * @return
    */
   public static List<Element> getElements(Element root,String path){
	   try {
		   return XPath.selectNodes(root, path);
	   } catch (JDOMException e) {
		   e.printStackTrace();
	   }
	   return null;
   }
   
   /**
    * 根据节点XPATH查找节点
    * @param root
    * @param path
    * @return
    */
   public static Element getElement(Element root,String path){
	   try {
	   	   return (Element)XPath.selectSingleNode(root, path);
	   } catch (JDOMException e) {
		   e.printStackTrace();
	   }
	   return null;
   }
   
   /**
    * 删除节点
    * @param xml=xml文本
    * @param xpathString=xpath条件；
    * @return
    */
   public static Document removeNode(String xml,String xpathString){
	   return removeNode(string2Doc(xml),xpathString);
   } 
   
   /**
    * 根据节点XPATH删除节点
    * @param doc
    * @param xpathString
    * @return
    */
   public static Document removeNode(Document doc,String xpathString){
	   Element anode=null;
		try {
			XPath   xpath=XPath.newInstance(xpathString); 
			anode = (Element)xpath.selectSingleNode(doc);
		} catch (JDOMException e) {
			e.printStackTrace();
		} 
		anode.getParentElement().removeContent(anode); 
		return doc;
   }
   
   /**
    * XML文件是否存在
    * @param xmlPath
    * @return
    */
   public static boolean isXMLExists(String xmlPath) {
		File file = new File(xmlPath);
		return file.exists();
	}
   
   /**
    * 把xml的所有子节点转化为Map
    * @param path
    * @param xpathString
    * @return
    */
	public static Map<String,String> getChildrenMap(String path,String xpathString){
		Document doc=file2Doc(path);
		List<Element> list=getElement(doc.getRootElement(),xpathString).getChildren();
		Map<String,String> map=null;
		if(null==list||0==list.size())
			return map;
		map=new HashMap<String,String>();
		for (Element element:list) {
			map.put(element.getName(), element.getText());
		}
		return map;
	}
}