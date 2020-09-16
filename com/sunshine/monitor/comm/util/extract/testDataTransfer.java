package com.sunshine.monitor.comm.util.extract;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;


public class testDataTransfer {


	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("结束");
		System.out.println(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date()));
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("开始");
		System.out.println(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date()));
	}

	@Test
	public void test() throws ClassNotFoundException, SQLException {
		DataTransfer dataTransfer=new DataTransfer();
		dataTransfer.doDataTransfer();
	}

}
