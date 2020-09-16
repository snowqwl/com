
package com.sunshine.monitor.comm.util.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
/**
 * 该类作废 
 * @author 2016-10-24 liumeng
 *
 */
public class FtpUtilTool {
    private static FTPClient ftpClient = new FTPClient();
    private static String encoding = System.getProperty("file.encoding");
    private static String url = "10.142.54.32";
    private static String username = "ygnet";
    private static String password = "123456";		
    private static int port = 21;   
    public static int loginFtp(){
    	int reply = 0;
    	try{
                
        ftpClient.connect(url, port);
        ftpClient.login(username, password);
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftpClient.setControlEncoding(encoding); 
        reply = ftpClient.getReplyCode();
       
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return reply;
    }
    

    public static boolean uploadFile(String path, String filename, InputStream input) {
        boolean result = false;

        try {      
            boolean change = ftpClient.changeWorkingDirectory(path);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            if (!change) {
            	if (!makeDirectory(path)) { 
           		 	ftpClient.changeWorkingDirectory("/");
                    System.out.println("创建文件夹失败!");  
                }else{
                	ftpClient.changeWorkingDirectory(path);
                	result = ftpClient.storeFile(filename, input);
                }  
                
            }else{
            	result = ftpClient.storeFile(filename, input);
            }           
            if (result) {
                System.out.println("文件上传成功!");
            }else{
            	System.out.println("文件上传失败!");
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

   

    public static boolean downFile(String remotePath, String fileName,String localPath) {
    	 int reply;
    	 boolean result = false;
    	 try{
    		 reply = loginFtp();  
	         if (!FTPReply.isPositiveCompletion(reply)) {
	             ftpClient.disconnect();
	             System.err.println("FTP server refused connection.");
	             return result;
	         }
	         boolean change = ftpClient.changeWorkingDirectory(remotePath);
	         if(change){
	         FTPFile[] fs = ftpClient.listFiles();
	         for (FTPFile ff : fs) {
	             if (ff.getName().equals(fileName)) {
	                 File localFile = new File(localPath + "/" + ff.getName());
	                 OutputStream is = new FileOutputStream(localFile);
	                 ftpClient.retrieveFile(ff.getName(), is);
	                 /**
	                  * 下载之后，直接上传到二次识别的图片目录中去
	                  */
	                 InputStream input = new FileInputStream(localFile);
	                 uploadFile("/development/SecondRecognitionLPR/im",  fileName, input);                 
	                 is.close();
	             }
	         }
	         ftpClient.logout();
	         result = true;
         }else{
        	 return result;
         }
     } catch (IOException e) {
         e.printStackTrace();
     } finally {
         if (ftpClient.isConnected()) {
             try {
                 ftpClient.disconnect();
             	} catch (IOException ioe) {
             }
         }
     }
     return result;
    }
 
    public static List listFtpWd(String pathName) throws IOException{  
         ArrayList<String> arFiles= new ArrayList<String>(); 
         int reply = loginFtp();           
         if (!FTPReply.isPositiveCompletion(reply)) {
             ftpClient.disconnect();
             System.err.println("FTP server refused connection.");
             return null;
         }
        if(pathName.startsWith("/")){  
            String directory = pathName;  
            boolean change = ftpClient.changeWorkingDirectory(directory);
            if(change){
            FTPFile[] files = ftpClient.listFiles();  
            for(FTPFile file:files){  
                if(file.isFile()){                 	
                	//System.out.println(file.getName());
                    arFiles.add(file.getName());  
                }else if(file.isDirectory()){  
                	listFtpWd(directory+"/"+file.getName());  
                }  
            } 
            }
        }
		return arFiles;  
    }  
    

    public static boolean makeDirectory(String dir) {          
       
        boolean flag = true;  
        try {        	
            flag = ftpClient.makeDirectory(dir);  
            if (flag) {  
                System.out.println("make Directory " +dir +" succeed");  
  
            } else {  
  
                System.out.println("make Directory " +dir+ " false");  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return flag;  
    }
    

}


