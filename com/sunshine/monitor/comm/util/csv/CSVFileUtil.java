package com.sunshine.monitor.comm.util.csv;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取过车图片二次识别的信息
 * CSV文件
 * 总共3列：
 * filename 文件名称（过车序号+图片后缀名）
 * platecolor 号牌颜色
 * plateno 号牌号码
 * @author Administrator
 *
 */
public class CSVFileUtil {  
	    // CSV文件编码  
	    public static final String ENCODE = "gbk";  
	  
	    public static Map<String, Object> recognitionFalsePlate(String fileUrl,String fileName,String gcxh) throws Exception {  
	    	Map<String, Object>  map = new HashMap<String, Object>();
	    	try {
				Class.forName("org.relique.jdbc.csv.CsvDriver");
			    Properties props = new Properties();
				props.put(fileName, ".csv");
				props.put("charset", ENCODE);  
				Connection conn = DriverManager.getConnection("jdbc:relique:csv:"+fileUrl, props);
			    Statement stmt = (Statement) conn.createStatement();
//			    String filename  = "430000001057585661.jpg";
			    ResultSet results = (ResultSet) stmt.executeQuery("SELECT filename,platecolor,plateno FROM result where filename='"+gcxh+"'");
//			    boolean append = true;
//			    CsvDriver.writeToCsv(results, System.out, append);
//			    System.out.println(results);
			    //循环得到list，最后获取最后（最新）一个识别结果
			    while(results.next()){	
			    	String hphm=results.getString("plateno");
			    	if(hphm.equals("识别失败")){
			    		map.put("flag", "0");
			    		continue;
			    	}			    		
			    	map.put("hphm",hphm);			    	
			    	map.put("hpys",checkHpys(results.getString("platecolor")));
			    	map.put("flag", "1");
			    }			    
			    conn.close();		
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return map;
		}
	    
	    public static String checkHpys(String colorStr){
	    	 int color = Integer.parseInt(colorStr);
			 String colorName = "";
			 if(color==0){
				 colorName = "黄色";
			 }else if(color==1){
				 colorName = "蓝色";
			 }else if(color==2){
				 colorName = "黑色";
			 }else if(color==3){
				 colorName = "白色";
			 }else{
				 colorName = "无";
			 }
			 return colorName;
	    }
	    
	    public static void main(String[] args) {
			try {		
				String filename  = "430000001057585661.jpg";
				String endName = filename.substring(filename.lastIndexOf(".") + 1);//图片后缀名
				System.out.println(endName);
				Map<String, Object>  map = recognitionFalsePlate("F://FTP/LPR/out", "result", "430000001057585661.jpg");
				System.out.println("map:"+map.get("hpys")+"------"+map.get("hphm"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	
	} 
