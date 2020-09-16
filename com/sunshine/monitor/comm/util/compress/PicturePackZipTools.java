package com.sunshine.monitor.comm.util.compress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.sunshine.monitor.comm.util.http.HttpHelpers;
/**
 * 图片压缩成ZIP,支持并发多线程;
 * java.util.ZipOutputStream中文乱码 
 * 方法一、JDK1.7可以设置编码 
 * 方法二、换成Apache ant
 * @author OY
 * 
 */
public class PicturePackZipTools {

private static String DEFAULT_COMPRESS_ENCODE = "GBK";
	
	private static ZipOutputStream getZipStreamEncode(OutputStream output,
			String encode) {
		ZipOutputStream zipStream = new ZipOutputStream(output);
		if (encode == null || "".equals(encode)) {
			zipStream.setEncoding(DEFAULT_COMPRESS_ENCODE);
		} else {
			zipStream.setEncoding(encode);
		}
		return zipStream;
	}

	/**
	 * 访问本地路径下的所有文件
	 * 
	 * @param path
	 * @return
	 */
	public static List<File> loadFiles(String path) {
		List<File> list = null;
		try {
			File fold = new File(path);
			if (fold.isDirectory()) {
				File[] files = fold.listFiles();
				list = Arrays.asList(files);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 读取本地系统路径下的所有图片打成ZIP
	 * 
	 * @param path
	 * @param output
	 * @param compress
	 */
	public static void compressZip(String path, OutputStream output,
			String encode, boolean compress) {
		List<File> listfiles = null;
		ZipOutputStream zipStream = null;
		try {
			zipStream = getZipStreamEncode(output, encode);
			listfiles = loadFiles(path);
			for (File file : listfiles) {
				compressZip(file, zipStream, compress);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (zipStream != null) {
					zipStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取网络图片打成打成ZIP
	 * @param urls
	 *            key = 图片名, value = 图片URL
	 * @param output 
	 * @param encode 编码
	 * @param compress 是否压缩
	 */
	public static void compressZip(Map<String, String> urls,
			OutputStream output, String encode, boolean compress) {
		ZipOutputStream zipStream = null;
		try {
			zipStream = getZipStreamEncode(output, encode);
			//Map<String, String> synUrls = Collections.synchronizedMap(urls);
			Set<Entry<String, String>> set = urls.entrySet();
			Iterator<Entry<String, String>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				compressZip(entry.getValue(), zipStream, entry.getKey(),
						compress);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (zipStream != null) {
					zipStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 压缩单个文件为ZIP
	 * @param file
	 * @param output
	 * @param encode
	 * @param compress
	 */
	public static void compressZip(File file, OutputStream output,
			String encode, boolean compress) throws Exception{
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
			compressZip(input,file.getName(),output,encode,compress);
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * 压缩单个文件为ZIP
	 * @param input
	 * @param fileName
	 * @param output
	 * @param encode
	 * @param compress
	 */
	public static void compressZip(InputStream input, String fileName,
			OutputStream output, String encode, boolean compress) throws Exception {
		ZipOutputStream zipStream = null;
		try {
			zipStream = getZipStreamEncode(output, encode);
			zip(input, zipStream, fileName, compress);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (zipStream != null)
					zipStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 本地图片
	 */
	private static void compressZip(File file, ZipOutputStream zipStream,
			boolean compress) throws Exception{
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
			zip(input, zipStream, file.getName(), compress);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 网络图片
	 * 
	 * @param url
	 * @param zipStream
	 * @param compress
	 */
	private static void compressZip(String url, ZipOutputStream zipStream,
			String fileName, boolean compress) throws Exception{
		InputStream input = null;
		try {
			input = HttpHelpers.getInputStream(url);
			zip(input, zipStream, fileName, compress);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param input
	 * @param zipStream
	 * @param zipEntryName
	 * @param compress
	 */
	private static void zip(InputStream input, ZipOutputStream zipStream,
			String zipEntryName, boolean compress) throws Exception{
		byte[] bytes = null;
		BufferedInputStream bufferStream = null;
		try {
			if(input == null)
				throw new Exception("获取压缩的数据项失败! 数据项名为：" + zipEntryName);
			// 压缩条目不是具体独立的文件，而是压缩包文件列表中的列表项，称为条目，就像索引一样
			ZipEntry zipEntry = new ZipEntry(zipEntryName);
			// 定位到该压缩条目位置，开始写入文件到压缩包中
			zipStream.putNextEntry(zipEntry);
			if (compress) {
				bytes = CompressPictureTools.compressOfQuality(input, 0);
				zipStream.write(bytes, 0, bytes.length);
			} else {
				bytes = new byte[1024 * 10];// 读写缓冲区
				bufferStream = new BufferedInputStream(input);// 输入缓冲流
				int read = 0;
				while ((read = bufferStream.read(bytes)) != -1) {
					zipStream.write(bytes, 0, read);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != bufferStream)
					bufferStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
