package com.sunshine.monitor.system.monitor.action;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.birt.report.utility.filename.IFilenameGenerator;


public class TimestampFilenameGenerator implements IFilenameGenerator{
	
	private String datePattern = "yyyyMMdd";
	
	public String getFilename(String baseName, String fileExtension,String outputType,Map options) {
		
		DateFormat dataformat = new SimpleDateFormat(this.datePattern);
		if (fileExtension == null) {
			fileExtension = "";
		}
		try {
			baseName = new String(baseName.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			baseName="BIRTReport";
		}
		return baseName + "_" + dataformat.format(new Date()) + "." + fileExtension;
	}

}
