package com.sunshine.monitor.comm.util.ftp;

import java.io.File;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.bean.FtpEntity;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.comm.util.http.HttpHelpers;

public abstract class FtpUtil {
	private static Logger log = LoggerFactory.getLogger(FtpUtil.class);
	private static String encoding = System.getProperty("file.encoding");
//	private static String url = "10.142.54.32";
//	private static String username = "ygnet";
//	private static String password = "123456";
//	private static int port = 21;

	public static FTPClient loginFtp() {
		FTPClient ftpClient = null;
		try {
			ftpClient = new FTPClient();
			FtpEntity ftpEntity = SpringApplicationContext.getBean("ftpEntity", FtpEntity.class);
			String url = ftpEntity.getUrl();
			String username = ftpEntity.getUsername();
			String password = ftpEntity.getPassword();
			int port = ftpEntity.getPort();
			ftpClient.connect(url, port);
			boolean isLogin = ftpClient.login(username, password);
			if(!isLogin){
				log.info("FTP Login failure!");
				ftpClient = new FTPClient();
				ftpClient.connect(url, port);
				ftpClient.login(username, password);
				log.info("FTP Reconnect!");
			}
			ftpClient.enterLocalPassiveMode();
			ftpClient.setDefaultTimeout(2000);
			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.setControlEncoding(ftpEntity.getEncoding());//UTF-8
			
			//reply = ftpClient.getReplyCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ftpClient;
	}

	public static boolean uploadFile(String path, String filename,
			InputStream input) {
		boolean result = false;
		FTPClient ftpClient = loginFtp();
		try {
			if (!isValid(ftpClient.getReplyCode()))
				return false;			
			boolean change = ftpClient.changeWorkingDirectory(new String(path.getBytes(), "ISO-8859-1"));
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			if (!change) {
				if (!makeDirectory(path)) {
					ftpClient.changeWorkingDirectory("/");
					System.out.println("创建文件夹失败!");
				} else {
					ftpClient.changeWorkingDirectory(path);
					result = ftpClient.storeFile(filename, input);
				}

			} else {
				result = ftpClient.storeFile(new String(filename.getBytes(), "ISO-8859-1"), input);
			}
			if (result) {
				System.out.println("文件上传成功!");
			} else {
				System.out.println("文件上传失败!");
			}
			input.close();
			ftpClient.logout();
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
	
	public static boolean isValid(int reply) throws IOException {
		if (!FTPReply.isPositiveCompletion(reply)) {
			log.debug("FTP服务器无法连接!");
			return false;
		}
		return true;
	}

	public static boolean downFile(String remotePath, String fileName,
			String localPath) {
		boolean result = false;
		FTPClient ftpClient = loginFtp();
		try {
			if (!isValid(ftpClient.getReplyCode()))
				return result;
			boolean change = ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(), "ISO-8859-1"));
			if (change) {
				FTPFile[] fs = ftpClient.listFiles();
				for (FTPFile ff : fs) {
					if (ff.getName().equals(fileName)) {
						File localFile = new File(localPath + "/" + ff.getName());
						OutputStream is = new FileOutputStream(localFile);
						ftpClient.retrieveFile(new String(ff.getName().getBytes(), "ISO-8859-1"), is);
						is.close();
						break;
					}
				}
				ftpClient.logout();
				result = true;
			} else {
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

	public static List listFtpWd(String pathName) throws IOException {
		List<String> arFiles = new ArrayList<String>();
		FTPClient ftpClient = loginFtp();
		if (!isValid(ftpClient.getReplyCode()))
			return null;
		if (pathName.startsWith("/")) {
			String directory = new String(pathName.getBytes(), "ISO-8859-1");
			log.debug("Curreent Path(Before):" + ftpClient.printWorkingDirectory());
			boolean change = ftpClient.changeWorkingDirectory(directory);
			log.debug("Curreent Path(After):" + ftpClient.printWorkingDirectory() + "--CODE:" +ftpClient.cwd(directory));
			if (change) {
				FTPFile[] files = ftpClient.listFiles();
				for (FTPFile file : files) {
					if (file.isFile()) {
						// System.out.println(file.getName());
						//arFiles.add(new String(file.getName().getBytes("ISO-8859-1"), "GBK" ));
						arFiles.add(file.getName());
					} else if (file.isDirectory()) {
						listFtpWd(directory + "/" + file.getName());
					}
				}
			}
		}
		return arFiles;
	}

	public static boolean makeDirectory(String dir) {

		boolean flag = true;
		try {
			FTPClient ftpClient = loginFtp();
			if (!isValid(ftpClient.getReplyCode()))
				return false;
			flag = ftpClient.makeDirectory(dir);
			if (flag) {
				System.out.println("make Directory " + dir + " succeed");

			} else {

				System.out.println("make Directory " + dir + " false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}	
	/**
	 * 二次识别
	 * @param remotePath
	 * @param fileName
	 * @param localPath
	 * @return
	 */
	public static boolean downFileSecondPic(String remotePath, String fileName) {
		boolean result = false;		
		try {
			InputStream input = HttpHelpers.getInputStream(remotePath);
			if(input==null){
				return false;
			}
			result = uploadFile("/SecondRecognitionLPR/im",  fileName, input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
