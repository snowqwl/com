package com.sunshine.monitor.comm.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Common {
	public static final String success = "1";
	public static final String failed = "0";
	public static final String SuccessMessage = "操作成功！";
	public static final String FailedMessage = "操作失败！";
	public static final String ErrorPost = "非法的请求！";
	public static final String NoInput = "请输入必填项！";

	public static Map<String, String> messageBox(String message, String cd) {

		Map<String, String> map = new HashMap<String, String>();
		try {

			map.put("code", cd);
			map.put("message", URLEncoder.encode(message, "UTF-8"));

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		return map;

	}

	public static String getNow() {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(now);
	}

	public static String ScriptToHtml(String strJava) {
		String r = null;
		if (strJava == null) {
			r = "";
		} else {
			r = strJava.replaceAll("'", "’");

			r = r.replaceAll("\r", "");
			r = r.replaceAll("\n", "");
			r = r.replaceAll("\\(", "（");
			r = r.replaceAll("\\)", "）");
		}
		return r;
	}

	public static String replace(String strOriginal, String strOld,
			String strNew) {
		int i = 0;
		StringBuffer strBuffer = new StringBuffer(strOriginal);
		while ((i = strOriginal.indexOf(strOld, i)) >= 0) {
			strBuffer.delete(i, i + strOld.length());
			strBuffer.insert(i, strNew);
			i += strNew.length();
			strOriginal = strBuffer.toString();
		}
		return strOriginal;
	}

	public static String changeHphm(String hphm) {
		String Chphm = "";
		String[] tmp = (String[]) null;
		if (!hphm.equals("")) {
			tmp = new String[hphm.length()];
			for (int i = 0; i < hphm.length(); i++) {
				tmp[i] = hphm.substring(i, i + 1);
				if (tmp[i].equals("?"))
					tmp[i] = "_";
				else if (tmp[i].equals("*")) {
					tmp[i] = "%";
				}
				Chphm = Chphm + tmp[i];
			}
		} else {
			Chphm = hphm;
		}
		return Chphm;
	}

	public static String changeSqlChar(String value) {
		char[] cs = (char[]) null;
		if ((value != null) && (!"".equals(value))) {
			cs = value.toCharArray();
			for (int i = 0; i < cs.length; i++) {
				if (cs[i] == '?')
					cs[i] = '_';
				else if (cs[i] == '*') {
					cs[i] = '%';
				}
			}
		}
		if (cs == null) {
			return "";
		}
		return String.valueOf(cs);
	}

	/**
	 * 将数字字符串格式成标准长度(字符之前加零)
	 * @param str 原参数
	 * @param len 格式化位数
	 * @return
	 * @author OUYANG
	 */
	public static String addZeroStr(String str, int len) {
		// 初始化
		String result = str;
		if (str == null || "".equals(str)) {
			return null;
		} else if (str.length() > 0 && str.length() < len) {
			int temp = len - (str.length());
			if (temp > 0) {
				for (int i = 0; i < temp; i++) {
					result = "0" + result;
				}
			}
		}
		return result;
	}

	/**
	 * 查询请求Map参数
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getParamentersMap(HttpServletRequest request)
			throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		Enumeration<String> enumes = request.getParameterNames();
		while (enumes.hasMoreElements()) {
			String param = enumes.nextElement();
			if (param != null && param.length() > 0
					&& !"method".equalsIgnoreCase(param)) {
				Object value = request.getParameter(param);
				if (value != null && !"".equals(value) && !"null".equalsIgnoreCase(value.toString())) {
					paraMap.put(param, value);
				}
			}
		}
		return paraMap;
	}
	
	/**
	 * 字段首字母大写
	 * @param colName
	 * @return
	 */
	public static String formatColName(String colName) {
		return colName.substring(0, 1).toUpperCase()
				+ colName.substring(1).toLowerCase();
	}
	
	public static void setAlerts(String message, HttpServletRequest request) {
		String msg = message;
		if ((message == null) || (message.length() < 1)) {
			msg = "NullPointerException";
		}
		request.setAttribute("error", "<script defer>alert('"
				+ ScriptToHtml(msg) + "');</script>");
	}
	
	
	
	
	
	public static String UBB(String source) {
		if ((source == null) || ("null".equals(source)))
			return source;
		String[] UBBReg = new String[28];
		String[] HTMLTags = new String[28];
		UBBReg[0] = "\\[img\\](.+?)\\[/img\\]";
		HTMLTags[0] = "<a href=$1 target=_blank><img src=$1 border=0 alt='点击在新窗口浏览图片' onload=\"javascript:if(this.width > 760)this.width = 760\"></a>";
		UBBReg[1] = "\\[wmv=*([0-9]*),*([0-9]*),*([0-9]*)\\](.*?)\\[/wmv\\]";
		HTMLTags[1] = "<br><object align=middle classid=clsid:22d6f312-b0f6-11d0-94ab-0080c74c7e95 class=object id=mediaplayer width=$1 height=$2 ><param name=showstatusbar value=-1><param name=filename value=$4><param name=AutoStart value=$3><embed type=application/x-oleobject codebase=http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#version=5,1,52,701 flename=mp src=$4  width=$1 height=$2 autostart=$3 ></embed></object><br>";
		UBBReg[2] = "\\[rm=*([0-9]*),*([0-9]*),*([0-9]*)\\](.*?)\\[/rm\\]";
		HTMLTags[2] = "<br><object classid=clsid:cfcdaa03-8be4-11cf-b84b-0020afbbccfa class=object id=raocx width=$1 height=$2><param name=src value=$4><param name=console value=clip1><param name=controls value=imagewindow><param name=autostart value=$3></object><br><object classid=clsid:cfcdaa03-8be4-11cf-b84b-0020afbbccfa height=32 id=video2 width=$1><param name=src value=$4><param name=autostart value=$3><param name=controls value=controlpanel><param name=console value=clip1></object><br>";
		UBBReg[3] = "\\[flash=*([0-9]*),*([0-9]*)\\](.*?)\\[/flash\\]";
		HTMLTags[3] = "<a href=$3 target=_blank><img src=image/swf.gif border=0 alt=点击开新窗口欣赏该flash动画!> [全屏欣赏]</a><br><object codebase=http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0 classid=clsid:d27cdb6e-ae6d-11cf-96b8-444553540000 width=$1 height=$2><param name=movie value=$3><param name=quality value=high><embed src=$3 quality=high pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' width=$1 height=$2>$3</embed></object>";
		UBBReg[4] = "\\[img=*([0-9]*),*([0-9]*)\\](.+?)\\[/img\\]";
		HTMLTags[4] = "<a href=$3 target=_blank><img src=$3 border=0 alt='点击在新窗口浏览图片' width=$1 height=$2></a>";
		UBBReg[5] = "\\[upload\\](.*?)\\[/upload\\]";
		HTMLTags[5] = "<a href=$1 target=_blank>点击下载该文件</a>";
		UBBReg[6] = "\\[upload=(.*?)\\](.*?)\\[/upload\\]";
		HTMLTags[6] = "<a href=$2 target=_blank>点击下载该文件</a>";
		UBBReg[7] = "\\[url\\](.*?)\\[/url\\]";
		HTMLTags[7] = "<a href=$1 target=_blank>$1</a>";
		UBBReg[8] = "\\[url=(.*?)\\](.*?)\\[/url\\]";
		HTMLTags[8] = "<a href=$1 target=_blank>$2</a>";
		UBBReg[9] = "\\[color=(.*?)\\]";
		HTMLTags[9] = "<font color=$1>";
		UBBReg[10] = "\\[/color\\]";
		HTMLTags[10] = "</font>";
		UBBReg[11] = "\\[font=(.*?)\\](.*?)";
		HTMLTags[11] = "<font face=$1>";
		UBBReg[12] = "\\[/font\\]";
		HTMLTags[12] = "</font>";
		UBBReg[13] = "\\[size=*([1-7]*)\\]";
		HTMLTags[13] = "<font size=$1>";
		UBBReg[14] = "\\[/size\\]";
		HTMLTags[14] = "</font>";
		UBBReg[15] = "\\[i\\](.*?)\\[/i\\]";
		HTMLTags[15] = "<i>$1</i>";
		UBBReg[16] = "\\[u\\](.*?)\\[/u\\]";
		HTMLTags[16] = "<u>$1</u>";
		UBBReg[17] = "\\[b\\](.*?)\\[/b\\]";
		HTMLTags[17] = "<b>$1</b>";
		UBBReg[18] = "\\[s\\](.*?)\\[/s\\]";
		HTMLTags[18] = "<s>$1</s>";
		UBBReg[19] = "\\[email\\s*=\\s*([^\\]\\s]+?)\\s*\\]\\s*([\\s\\S]+?)\\s*\\[\\/email\\]";
		HTMLTags[19] = "<a href=\"mailto:$1\">$2</a>";

		UBBReg[20] = "\\[move=(down|up|left|right)\\](.*?)\\[/move\\]";
		HTMLTags[20] = "<marquee direction=$1 scrollamount='3'>$2</marquee>";
		UBBReg[21] = "\\[glow=*([#0-9a-zA-Z]*),*([0-9]*)\\](.*?)\\[/glow\\]";
		HTMLTags[21] = "<table style=\"filter:glow(color=$1, strength=$2)\"><tr><td>$3</td></tr></table>";
		UBBReg[22] = "\\[table=(.[^\\[]*)\\]";
		HTMLTags[22] = "<table width=\"$1\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">";
		UBBReg[23] = "\\[td\\](.*?)\\[/td\\]";
		HTMLTags[23] = "<td>$1</td>";
		UBBReg[24] = "\\[tr\\](.*?)\\[/tr\\]";
		HTMLTags[24] = "<tr>$1</tr>";
		UBBReg[25] = "\\[/table\\]";
		HTMLTags[25] = "</table>";
		UBBReg[26] = "\\[align=(left|right|center)\\](.*?)\\[/align\\]";
		HTMLTags[26] = "<div align=$1>$2</div>";
		UBBReg[27] = "\\[quote\\](.*?)\\[/quote\\]";
		HTMLTags[27] = "<center><table width='90%' border='0' cellspacing='0' cellpadding='0'><tr><td align='left'>引用：</td></tr><tr><td align='left'><table width='100%' border='1' cellpadding='10' cellspacing='0' bordercolor='#CCCCCC' bgcolor='#99FFFF'><tr><td>$1</td></tr></table></td></tr></table></center>";
		source = TextToHtml(source);
		for (int i = 0; i < UBBReg.length; i++) {
			if (UBBReg[i].length() > 0) {
				if (i == 27)
					while (source.indexOf("[quote]") != -1)
						source = source.replaceAll(UBBReg[27], HTMLTags[27]);
				else {
					source = source.replaceAll(UBBReg[i], HTMLTags[i]);
				}
			}
		}
		return source;
	}
	
	
	/**
	 * 
	 * 函数功能说明：身份证15位变18位
	 * 修改日期 	2013-8-21
	 * @param sfzhm
	 * @return    
	 * @return String
	 */
	public static String sfzUpto18(String sfzhm) {
		int[] wi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
		String[] vi = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		int[] ai = new int[18];
		String eightcardid = sfzhm.substring(0, 6);
		eightcardid = eightcardid + "19";
		eightcardid = eightcardid + sfzhm.substring(6, 15);
		int remaining = 0;
		int sum = 0;
		for (int i = 0; i < 17; i++) {
			String k = eightcardid.substring(i, i + 1);
			ai[i] = Integer.parseInt(k);
		}
		for (int i = 0; i < 17; i++) {
			sum += wi[i] * ai[i];
		}
		remaining = sum % 11;
		eightcardid = eightcardid + vi[remaining];
		return eightcardid;
	}
	
	
	public static String TextToHtml(String s) {
		char[] c = s.toCharArray();
		StringBuffer buf = new StringBuffer();
		int i = 0;
		for (int size = c.length; i < size; i++) {
			char ch = c[i];
			if (ch == '"')
				buf.append("&quot;");
			else if (ch == '&')
				buf.append("&amp;");
			else if (ch == '<')
				buf.append("&lt;");
			else if (ch == '>')
				buf.append("&gt;");
			else if (ch == '\r')
				buf.append("");
			else if (ch == '\n')
				buf.append("<br>");
			else if (ch == ' ')
				buf.append("&nbsp;");
			else {
				buf.append(ch);
			}
		}
		c = (char[]) null;
		return buf.toString();
	}
	
	public static String ScriptToString(String strJava) {
		String r = null;
		if (strJava == null) {
			r = "";
		} else {
			r = strJava.replaceAll("'", "\"");
			r = r.replaceAll("\"", "\\\"");

			r = r.replaceAll("\r", "");
			r = r.replaceAll("\n", "");
		}
		return r;
	}
	
	public static void setErrors(Exception e, HttpServletRequest request) {
		e.printStackTrace();
		String msg = e.getMessage();
		if ((msg == null) || (msg.length() < 1)) {
			msg = "NullPointerException";
		}
		request.setAttribute("error", "<script defer>alert('"
				+ ScriptToHtml(msg) + "');</script>");
	}
	
	public static String getPingResult(String s) {
		if (s == null)
			return "";
		if (s.equals("1"))
			return "正常";
		if (s.equals("0"))
			return "错误";
		if (s.equals("-1"))
			return "断网";
		if (s.equals("-2"))
			return "无网";
		if (s.equals("-9")) {
			return "异常";
		}
		return "";
	}
	
	
	/**
	 * 
	 * @param zrdw 责任单位 {'1':'440100000','2':'44010000'}
	 * @author OUYANG
	 * @return
	 */
	public static String getAlarmDwdm(String zrdw, String bjdl) {
		Map<String, String> result = null;
		try {
			JSONObject jsonObject = new JSONObject(zrdw);
			Iterator iterator = jsonObject.keys();
			String key = null;
			String value = null;
			result = new HashMap<String, String>();
			while (iterator.hasNext()) {
				key = (String) iterator.next();
				value = jsonObject.getString(key);
				result.put(key, value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result.get(bjdl);
	}
	
	/**
	 * 加零
	 * @param num
	 * @return
	 */
	public static String addZero(int num){
		if(num < 10) {
			String temp = "0" + num;
			return temp;
		}
		return String.valueOf(num);
	}
	
	 /**
	  * 按违法次数倒叙排序
	  * wfParam map中设置的违法次数的参数名称
	  *  liumeng 2016-10-12
	  * 
	  */
	public static List<Map<String,Object>> orderListByWfcs(List<Map<String,Object>> queryList,String wfParam){		
		 /**按违法次数倒叙排序**/
		 for(int n =1;n<queryList.size();n++){
			 for(int m =0;m<queryList.size();m++){
				 int a = 0;
				 int b = 0;
				 if(queryList.get(m).get(wfParam)!=null){
					a = Integer.parseInt(queryList.get(m).get(wfParam).toString());
				 }
				 if(queryList.get(n).get(wfParam)!=null){
					 b = Integer.parseInt(queryList.get(n).get(wfParam).toString());
				 }
				 if(a<b){
					 Map<String,Object> copywjmap = queryList.get(n);
					 queryList.set(n, queryList.get(m));
					 queryList.set(m, copywjmap);
				 }
			 }
		 }
		 return queryList;
	}

	/**
	 * 通过HttpClient接口
	 */
	public static Map<String, Object> sendHttpClientNew(String url, List<BasicNameValuePair> params,String ipHn,String portHn){
		Map<String, Object> result = new HashMap<String, Object>();
		String sendPostResult = "";
		try {

			//POST的URL String url = "http://" + ipHn + ":" + portHn + "/api/service/vehicle/passRecService/query";
			url = "http://" + ipHn + ":" + portHn + url;
			//建立HttpPost对象
			HttpPost httppost = new HttpPost(url);
			System.out.println("url "+url);
			HttpResponse responseSend;
			if (params == null){
				params = new ArrayList<BasicNameValuePair>();
			}
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			responseSend = new DefaultHttpClient().execute(httppost);
			sendPostResult = EntityUtils.toString(responseSend.getEntity());
			result = (Map<String, Object>) JSON.parse(sendPostResult);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().contains("cannot be cast to java.util.Map")){
				result.put("data", sendPostResult);
			}else {
				e.printStackTrace();
				result.put("code", "接口请求失败");
				result.put("state", "error");
			}
		}
		return result;
	}

	

	/**
	 * 通过HttpClient接口
	 */
	public static Map<String, Object> sendHttpClient(String url, List<BasicNameValuePair> params){
		Map<String, Object> result = new HashMap<String, Object>();
		String sendPostResult = "";
		try {
			PropertiesConfiguration config;config = new PropertiesConfiguration("ipport.properties");
			String ipHn = config.getString("ipgd");
			String portHn = config.getString("portgd");
			String httpUrl = config.getString("gd.pass.url");
			if(StringUtils.isBlank(url)){
				url = httpUrl;
			} else
				url = "http://" + ipHn + ":" + portHn + url;
			//建立HttpPost对象
			HttpPost httppost = new HttpPost(url);
			HttpResponse responseSend;
			if (params == null){
				params = new ArrayList<BasicNameValuePair>();
			}
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			responseSend = new DefaultHttpClient().execute(httppost);
			sendPostResult = EntityUtils.toString(responseSend.getEntity());
			result = (Map<String, Object>) JSON.parse(sendPostResult);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().contains("cannot be cast to java.util.Map")){
				result.put("data", sendPostResult);
			}else {
				e.printStackTrace();
				result.put("code", "接口请求失败");
				result.put("state", "error");
			}
		}
		return result;
	}

	/**
	 * 将requsst值放入
	 */
	public static List<BasicNameValuePair> putReParamToHttpP(Map<String, Object> conditions){
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		for (String key : conditions.keySet()) {
			System.out.println("key= "+ key + " and value= " + conditions.get(key));
			params.add(new BasicNameValuePair(key, conditions.get(key).toString()));
		}
		return params;
	}
	
}
