package com.sunshine.monitor.comm.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Ping {
	public static String test(String ip, String type) {
		if ((ip == null) || (ip.length() < 1) || (type == null)
				|| (type.length() < 1)) {
			return "0";
		}
		if (type.equals("1")) {
			Runtime runtime = Runtime.getRuntime();
			try {
				String r = "";
				Process process = null;
				String line = null;
				InputStream is = null;
				InputStreamReader isr = null;
				BufferedReader br = null;
				process = runtime.exec("ping " + ip + " -n 1");
				is = process.getInputStream();
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				while ((line = br.readLine()) != null) {
					r = r + line + "\n";
				}
				is.close();
				isr.close();
				br.close();
				if ((r.indexOf("Request timed out") != -1)
						|| (r.indexOf("请求超时") != -1))
					return "-1";
				if ((r.indexOf("Destination host unreachable") != -1)
						|| (r.indexOf("目标主机无法访问") != -1))
					return "-2";
				if ((r.indexOf("Received = 1") != -1)
						|| (r.indexOf("已接收 = 1") != -1))
					return "1";
				if ((r.indexOf("0% loss") != -1) || (r.indexOf("0% 丢失") != -1)) {
					return "1";
				}
				return "-1";
			} catch (Exception e) {
				runtime.exit(1);
				return "-9";
			}
		}
		if (type.equals("2")) {
			Runtime runtime = Runtime.getRuntime();
			try {
				String r = "";
				Process process = null;
				String line = null;
				InputStream is = null;
				InputStreamReader isr = null;
				BufferedReader br = null;
				process = runtime.exec("ping " + ip + " -c 1");
				is = process.getInputStream();
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				while ((line = br.readLine()) != null) {
					r = r + line + "\n";
				}
				is.close();
				isr.close();
				br.close();
				if (r.indexOf("received, 0% packet loss") != -1) {
					return "1";
				}
				if (r.indexOf("0% packet loss") != -1) {
					return "1";
				}
				return "-1";
			} catch (Exception e) {
				runtime.exit(1);
				return "-9";
			}
		}
		return "0";
	}
}
