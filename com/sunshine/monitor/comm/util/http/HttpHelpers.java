package com.sunshine.monitor.comm.util.http;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import sun.net.www.protocol.ftp.FtpURLConnection;

/**
 * 获取请求流
 * 
 * @author OY
 */
public abstract class HttpHelpers {

	private static final String KEY = "file.encoding";

	private static final String ENCODING = "GBK";

	public static InputStream getInputStream(String url) throws Exception{
		URLConnection con = null;
		HttpURLConnection httpCon = null;
		FtpURLConnection ftpCon= null;
		try {
			System.setProperty(KEY, ENCODING);
			URL _url = new URL(url);
			con = _url.openConnection();
			con.setConnectTimeout(3000);
			con.setUseCaches(false);// 不缓存
			con.setDefaultUseCaches(false);
			if (con instanceof HttpURLConnection) {
				httpCon = (HttpURLConnection) con;
				httpCon.setInstanceFollowRedirects(true);
				//httpCon.setRequestProperty("Accept-Charset", "utf-8");
				if (httpCon.getResponseCode() >= 300) {
					System.out.println("URL:" + url
							+ ",HTTP Request is not success, Response code is "
							+ httpCon.getResponseCode());
				} else {
					return httpCon.getInputStream();
				}
			} else if(con instanceof FtpURLConnection){
				ftpCon = (FtpURLConnection)con;
				return ftpCon.getInputStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static void main(String[] args) {
		
		//  本地测试
		try {
			InputStream input = getInputStream("http://10.143.73.55:8305/20160105/14519730000380.jpg");
			byte[] bys = new byte[1024*5];
			int rb =0;
			OutputStream ii = new FileOutputStream("f:\\1.jpg");
			while((rb= input.read(bys))!=-1){
				ii.write(bys,0,rb);
			}
			input.close();
			ii.flush();
			ii.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 是否可构建图片
		try {
			InputStream input = getInputStream("http://10.143.73.55:8305/20160105/14519730000380.jpg");
			ImageInputStream iinput = ImageIO.createImageInputStream(input);
			if (iinput != null) {
				Iterator<ImageReader> it = ImageIO.getImageReaders(iinput);
				ImageReader ir = it.next();
				System.out.print(ir);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		
	}
}
