package com.sunshine.monitor.comm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LocalFileUtils {
	private static String systemFileSeparator = System.getProperty("file.separator");
	private static String filePathFormat = "^[/\\\\]{1}\\S+$";
	
	private LocalFileUtils(){}
	
	public static String getLocalFileHomePath() throws UnknownOSException{
		String osName = System.getProperty("os.name");
		if(osName.toLowerCase().indexOf("windows")>=0){
			return "D:\\jcbk\\localFile";
		}else if(osName.toLowerCase().indexOf("linux")>=0){
			return "\\home\\jcbk\\localFile";
		}else {
			throw new UnknownOSException();
		}
	}

	public static Boolean saveFile(InputStream in, String path)
			throws PathFormatExceptino, UnknownOSException, IOException {
		validatePathFormat(path);
		path = transformPath(path);
		File file = new File(getLocalFileHomePath()+path);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		if(!file.exists()) file.createNewFile();
		FileOutputStream out = new FileOutputStream(file);
		byte[] b = new byte[1024];
		int l;
		while((l = in.read(b))>0){
			out.write(b,0,l);
		}
		out.flush();
		out.close();
		return true;
	}
	
	private static String transformPath(String path) {
		path = path.replace("/", systemFileSeparator);
		path = path.replace("\\", systemFileSeparator);
		return path;
	}

	private static void validatePathFormat(String path) throws PathFormatExceptino{
		if(!path.matches(filePathFormat)){
			throw new PathFormatExceptino("path="+path+" , format error ! path format = "
					+filePathFormat);
		}
	}
	
	private static class PathFormatExceptino extends Exception {
		private static final long serialVersionUID = 2964610495893079651L;
		public PathFormatExceptino(){}
		public PathFormatExceptino(String msg){
			super(msg);
		}
	}
	
	private static class UnknownOSException extends Exception{
		private static final long serialVersionUID = 9172787142236212184L;
		public UnknownOSException(){
			super("unknown OS Name By System.getProperty(\"os.name\")");
		}
	}
	
}

