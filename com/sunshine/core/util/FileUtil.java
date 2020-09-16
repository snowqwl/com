package com.sunshine.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.List;
import org.apache.tools.zip.ZipEntry;  
import org.apache.tools.zip.ZipOutputStream; 
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;

public class FileUtil {

	/**
	 * 删除文件
	 * 
	 * @param file
	 */
	public static void delete(File file) {
		if (file != null && file.exists())
			file.delete();
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists())
			return;
		if (!file.isDirectory())
			return;

		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator))
				temp = new File(path + tempList[i]);
			else
				temp = new File(path + File.separator + tempList[i]);

			if (temp.isFile())
				temp.delete();

			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	/**
	 * 压缩文件 (中文文件名压缩后会变成乱码)
	 * 
	 * @param files
	 *            File集合
	 * @param destFile
	 *            目标文件
	 */
	public static void zip(List<File> files, String destFile) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		InputStream is = null;
		byte[] b = new byte[1024];
		try {
			fos = new FileOutputStream(destFile);
			zos = new ZipOutputStream(fos);
			for (File file : files) {
				is = new FileInputStream(file);
				ZipEntry entry = new ZipEntry(file.getName());
				zos.putNextEntry(entry);

				while (is.read(b) != -1)
					zos.write(b);
				is.close();
			}
			zos.setEncoding("GB2312"); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (zos != null)
					zos.close();
				if (fos != null)
					fos.close();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
	}

	/**
	 * 创建一个带内容的文件
	 * 
	 * @param filePath
	 * @param fileContent
	 * @param encoding
	 * @return
	 */
	public static String createFile(String filePath, String fileContent,
			String encoding) {
		encoding = encoding.trim();
		PrintWriter printWrinter = null;
		try {
			File f = new File(filePath);

			if (!f.exists()) {
				f.createNewFile();
			} else {
				System.out.println(FileUtil.class.toString() + ":存在file"
						+ filePath + "!");
			}

			printWrinter = new PrintWriter(filePath, encoding);
			printWrinter.print(fileContent);
		} catch (Exception e) {
			System.out.println(FileUtil.class.toString() + ":建立file" + filePath
					+ "出错!");
		} finally {
			if (printWrinter != null)
				printWrinter.close();
		}
		return filePath;
	}

	/**
	 * 读取文件内容
	 * 
	 * @param filePath
	 * @param encoding
	 * @return
	 */
	public static String readFile(String filePath, String encoding) {
		encoding = encoding.trim();
		StringBuffer fileContent = new StringBuffer();
		FileInputStream fs = null;
		InputStreamReader ir = null;
		BufferedReader br = null;
		try {
			fs = new FileInputStream(filePath);
			if (encoding.equals("")) {
				ir = new InputStreamReader(fs);
			} else {
				ir = new InputStreamReader(fs, encoding);
			}

			br = new BufferedReader(ir);
			try {
				String data = "";
				while ((data = br.readLine()) != null) {
					fileContent.append(data + " ");
				}
			} catch (Exception e) {
				System.out.println(FileUtil.class.toString() + ":读取file"
						+ filePath + "出错" + e.toString());
				fileContent.append(e.toString());
			}
			return fileContent.toString();
		} catch (Exception e) {
			System.out.println(FileUtil.class.toString() + ":读取file" + filePath
					+ "出错!");
			return null;
		} finally {
			try {
				if (br != null)
					br.close();
				if (ir != null)
					ir.close();
				if (fs != null)
					fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 检查是否存在file
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean checkFile(String filePath) {
		File f = new File(filePath);
		if (f.isFile()) {
			return f.exists();
		} else
			return false;
	}

	/**
	 * 把内容写入file
	 * 
	 * @param filePath
	 * @param fileContent
	 * @return
	 */
	public static boolean writeFile(String filePath, String fileContent,
			String encoding) {
		encoding = encoding.trim();
		PrintWriter printWrinter = null;
		try {
			File f = new File(filePath);

			if (f.exists()) {
				printWrinter = new PrintWriter(f, encoding);
				printWrinter.print(fileContent);
				return true;
			} else {
				System.out.println(FileUtil.class.toString() + ":不存在file"
						+ filePath + "!");
				return false;
			}
		} catch (Exception e) {
			System.out.println(FileUtil.class.toString() + ":写入file" + filePath
					+ "出错!");
			return false;
		} finally {
			if (printWrinter != null)
				printWrinter.close();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 */
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		delete(file);
	}
	
	/**
	 * 复制文件
	 * @param sourceFile	源文件
	 * @param targetFile	目标文件
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		FileInputStream input = null;
		BufferedInputStream inBuff = null;
		FileOutputStream output = null;
		BufferedOutputStream outBuff = null;
		
		try{
			// 新建文件输入流并对它进行缓冲
			input = new FileInputStream(sourceFile);
			inBuff = new BufferedInputStream(input);
	
			// 新建文件输出流并对它进行缓冲
			output = new FileOutputStream(targetFile);
			outBuff = new BufferedOutputStream(output);
	
			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			outBuff.flush();
		}finally{
			// 关闭流
			if(inBuff!=null)
				inBuff.close();
			if(outBuff!=null)
				outBuff.close();
			if(output!=null)
				output.close();
			if(input!=null)
				input.close();
		}

	}
	
	/**
	 * 获取随机文件名
	 * @param filename	原始文件名
	 * @param prefix 	前缀
	 * @param randomLength 	随机数长度
	 * @return
	 * 返回结果格式为：前缀+时间戳+随机数+文件后缀(根据原始文件名获得)<br/>
	 * 时间戳长度为:15,Format表达式为:yyMMddHHmmssSSS
	 */
	public static String getRandomFileName(String filename,String prefix,int randomLength){
		String suffix = getFileSuffix(filename);
		if(suffix==null)
			suffix = "";
		else
			suffix = "." + suffix;
		return SysUtil.getTimeRandomID(prefix, randomLength) + suffix;
	}
	
	/**
	 * 获取文件后缀
	 * @param filename	文件名
	 * @return	如果没有后缀则返回null,否则返回文件后缀
	 */
	public static String getFileSuffix(String filename){
		int p = filename.lastIndexOf(".");
		if(p == -1)
			return null;
		return filename.substring(p + 1,filename.length());
	}
	
	/**
	 * 获取文件大小字符串
	 * @param size	字节数
	 * @return
	 */
	public static String formatFileSize(long size){
		double kb = 1024D;
		double mb = 1048576D;
		double gb = 1073741824D;
		DecimalFormat df = new DecimalFormat("#.##");
		if(size>=gb)
			return df.format(size/gb)+"GB";
		else if(size>=mb)
			return df.format(size/mb)+"MB";
		else if(size>=kb)
			return df.format(size/kb)+"KB";
		else
			return size+"K";
	}
	
	/**
	 * 如果文件夹不存在则创建文件夹
	 * @param folderPath
	 */
	public static void createFolderNotExists(String folderPath){
		File file = new File(folderPath);
		if(!file.exists())
			file.mkdirs();
	}
	
	
	/**
	 * 获取文件根目录
	 * @return
	 * @throws Exception
	 */
	public static String getFileHomeRealPath() throws Exception{
		
		String osName = System.getProperty("os.name");
		
		if(osName.toLowerCase().indexOf("windows")>=0){
			return "C:/jcbk";
		}else if(osName.toLowerCase().indexOf("linux")>=0){
			return "/home/jcbk";
		}else {
			throw new Exception("未知的服务器环境");
		}
	}

}
