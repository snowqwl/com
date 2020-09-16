package com.sunshine.monitor.comm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FusionChartsXMLGenerator {
	
	public static final int BOOLEAN_TRUE = 0;
	
	public static final int BOOEAN_FALSE = 1;
	
	private static FusionChartsXMLGenerator fx = new FusionChartsXMLGenerator();
	
	private String[] colors = {"AFD8F8", "F6BD0F", "8BBA00", "FF8E46", "008E8E",   
            "D64646", "8E468E", "588526", "B3AA00", "008ED6", "9D080D", "A186BE",
            "0000CD","00688B","00C5CD","00EEEE","00FF7F","8968CD","8B008B",
            "8B6914","A52A2A","B3EE3A","BCD2EE","CD3700"};
	
	public static FusionChartsXMLGenerator getInstance() {
		return fx;
	}
	
	
	private FusionChartsXMLGenerator () {
		
	}
	
	/**
	 * 功能描述：将数据集转换为 getMultiDSXML 方法中符合参数的集合
	 * @param dataSoucreList ： 数据库中查询的数据集，一个MAP为一条数据
	 * @param titleName:  数组keys对应的字段列别名
	 * @param keys ：字段列名
	 * @param columnNameKey ：X轴展示列名
	 * @return
	 */
	public List<List<String>> getMultiDSXMLData(List<Map<String, Object>> dataSoucreList,String[]titleName,String[]keys ,String columnNameKey){
		
		List<String>[]lists = new List[keys.length + 1 ];
		for(int i = 0; i < lists.length; i++){
			lists[i] = new ArrayList<String>();
			if(i != 0)
				lists[i].add(titleName[i-1]);
		}
		
		for(Map<String, Object> map : dataSoucreList){
			for(int i = 0;i<lists.length;i++){
				if(i == 0)
					lists[0].add(map.get(columnNameKey) == null ?"0":map.get(columnNameKey).toString());
				else
					lists[i].add(map.get(keys[i - 1]) == null ?"0":map.get(keys[i - 1]).toString());
			}
		}

		List<List<String>>  res = new ArrayList<List<String>>();
		for(List<String> list : lists){
			res.add(list);
		}
		
		return res;
	}
	
	/**
	 * 
	 * @param dataSourceList
	 * @param keys 格式第一个参数为name字段名,第二个参数为Value字段名
	 * @return
	 */
	public List<List<String>> getSingleDSXMLData(List<Map<String,Object>> dataSourceList,String[] keys) {
		List<String> []lists = new ArrayList[dataSourceList.size()];
		int count = 0;
		for (Map<String,Object> map : dataSourceList) {
				lists[count] = new ArrayList<String>();
				lists[count].add(map.get(keys[0]) == null ? "0" : map.get(keys[0]).toString());
				lists[count].add(map.get(keys[1]) == null ? "0" : map.get(keys[1]).toString());
				count ++;
		}
		
		List<List<String>> listdata = new ArrayList<List<String>>();
		for (List<String> list : lists) {
			listdata.add(list);
		}
		return listdata;
	}
	/**
	 * data形式为
	 * type0 category1，category2，category3...
	 * type1 value1，value2，value3...
	 * type2 value2，value2，value3...
	 * @param data  
	 * @param caption       图表主标题
	 * @param subCaption    图表副标题
	 * @param slantLable    是否旋转45,默认为默认为0(False):横向显示
	 * @param suffix        后缀显示名称
	 * @param xAxisName     横向坐标轴(x轴)名称
	 * @param yAxisName     纵向坐标轴(y轴)名称
	 * @param showNames     是否显示横向坐标轴(x轴)标签名称
	 * @param showValues    是否在图表显示对应的数据值，默认为1(True)
	 * @param decimalPrecision   指定小数位的位数，[0-10]    例如：='0' 取整
	 * @param rotateNames        是否旋转显示标签，默认为0(False):横向显示
	 * @return 
	 */
	public String getMultiDSXML (List<List<String>> data,String caption,String subCaption,String slantLable,String suffix,boolean maxValue,String xAxisName,String yAxisName,int showNames,int showValues,int decimalPrecision,int rotateNames) {
		
		
		double max = -Double.MAX_VALUE, min = Double.MAX_VALUE;
		for (int i = 1 ; i < data.size(); i++) {
			List row = data.get(i);
			for (int j = 1; j < row.size(); j++) {
				String val = (String) row.get(j);
				if (val != null && val.length() > 0) {
					double v = Double.parseDouble(val);
					if (v > max) {
						max = v;
					}
					if (v < min) {
						min = v;
					}
				}
			}
		}
		if (max == -Double.MAX_VALUE) {
			max = 0;
		}
		if (min == Double.MAX_VALUE) {
			min = 0;
		}
		if (min == max && min == 0) {
			min = 0;
			max = 100;
		}
		
		max = Math.abs(max / 10) + max;
		min = min = Math.abs(min / 10);
		
		int valCnt = data.get(0).size() - 1; 
		if (valCnt > 30) {
			showNames = 0;
			showValues = 0;
		}
		StringBuffer strXml = new StringBuffer();
		strXml.append("<graph baseFont='SumSim' baseFontSize='12' caption='" + caption + 
				"' subcaption = '" + subCaption+ "' yAxisMinValue='" + min + 
				"' xAxisName = '" + xAxisName + "' yAxisName = '" + yAxisName + 
				"' hovercapbg='FFECAA'" + " hovercapborder='F47E00' formatNumberScale='0' decimalPrecision='" + decimalPrecision + 
				"' showValues = '" + showValues + "' numdivlines='5' numVdivlines='0' showNames='" + showNames + 
				"' rotateNames='" + rotateNames + "' slantLabels='" + slantLable + "' numberSuffix='" + suffix + "' rotateYAxisName='0' showAlternateHGridColor='1'>");
		if (maxValue) {
			strXml = new StringBuffer(strXml.substring(0, strXml.length() - 1));
			strXml.append(" yAxisMaxValue='100'>");
		}
		strXml.append("<categories>");
		List headerRow = data.get(0);
		for (int i = 0; i < headerRow.size(); i ++) {
			strXml.append("<category name='" + headerRow.get(i) + "'/>");
		}
		strXml.append("</categories>");
		for (int i = 1; i < data.size(); i++) {
			List row = data.get(i);
			String name = (String) row.get(0);
		    String color = colors[((i - 1) % 24)];
			strXml.append("<dataset seriesName='" + name + "' color='" + color + "' anchorBorderColor='" + color + "' anchorBgColor='" + color + "'>");
			for (int j = 1; j < row.size(); j ++) {
				strXml.append("<set value='" + row.get(j) + "'/>");
			}
			strXml.append("</dataset>");
		}
		strXml.append("</graph>");
		String str = strXml.toString();
		return str;
	}
	
	/**
	 * data数据格式为:
	 * name value
	 * name value
	 * @param caption       图表主标题
	 * @param subCaption    图表副标题
	 * @param slantLable    是否旋转45,默认为默认为0(False):横向显示
	 * @param suffix        后缀显示名称
	 * @param xAxisName     横向坐标轴(x轴)名称
	 * @param yAxisName     纵向坐标轴(y轴)名称
	 * @param showNames     是否显示横向坐标轴(x轴)标签名称
	 * @param showValues    是否在图表显示对应的数据值，默认为1(True)
	 * @param decimalPrecision   指定小数位的位数，[0-10]    例如：='0' 取整
	 * @param rotateNames        是否旋转显示标签，默认为0(False):横向显示
	 */
	public String getSingleDSXML(List<List<String>> data,String caption,String subCaption,String slantLable,String suffix,String xAxisName, String yAxisName, int showNames, int showValues, int decimalPrecision, int rotateNames) {
		double max = -Double.MAX_VALUE,min = Double.MAX_VALUE;
		for (int i = 0; i < data.size(); i ++) {
			List<String> row = data.get(i);
			double value = Double.parseDouble(row.get(1));
			if (value > max) {
				max = value;
			}
			if (value < min) {
				min = value;
			}
		}
			
			if (max == -Double.MAX_VALUE) {
				max = 0;
			}
			
			if (min == Double.MIN_VALUE) {
				min = 0;
			}
			
			if (min == max && min == 0) {
				min = 0;
				max = 100;
			}
			
			max = Math.abs(max / 10) + max;
			min = min - Math.abs(min / 10);
			
			int valCnt = data.size() - 1;
			if (valCnt > 30) {
				showNames = 0;
				showValues = 0;
			}
			
			StringBuffer strXml = new StringBuffer();
			strXml.append("<graph baseFont='SunSim' baseFontSize='12' caption='" + caption +
					"' subcaption='" + subCaption + "' yAxisMinValue='" + min + 
					"' yAxisMaxValue='" + max + "' xAxisName='" + xAxisName + "' yAxisName='" + yAxisName + 
					"' hovercapbg='FFECAA' hovercapborder='F47E00' formatNumberScale='0' decimalPrecision='" + decimalPrecision + 
					"' showValues='" + showValues + "' numdivelines='5' numVdivlines='0' showNames='" + showNames + "' slantLabels='" + slantLable + "' numberSuffix='" + suffix + 
					"' rotateNames='" + rotateNames + 
					"' rotateYAxisName='0' showAlternateHGridColor='1'>");
			for (int i = 0; i < data.size(); i++) {
				List<String> row = data.get(i);
				String lable = row.get(0);
				String value = row.get(1);
				String color = colors[i % 24];
				strXml.append("<set name='" + lable + "' value='" + value + "' color='" + color + "'/>");
			}
			strXml.append("</graph>");
			String str = strXml.toString();
		return str;
	}
	
}

