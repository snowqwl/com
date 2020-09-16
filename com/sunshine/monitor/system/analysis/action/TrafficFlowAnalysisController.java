package com.sunshine.monitor.system.analysis.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.xml.XMLSerializer;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.core.util.DateUtil;
import com.sunshine.monitor.comm.util.KeyNumberComparator;
import com.sunshine.monitor.system.analysis.bean.TrafficFlow;
import com.sunshine.monitor.system.analysis.dto.TrafficFlowDTO;
import com.sunshine.monitor.system.analysis.service.TrafficFlowManager;
import com.sunshine.monitor.system.manager.service.SystemManager;

@Controller
@RequestMapping(value = "/trafficFlow.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TrafficFlowAnalysisController {

	private String mainPage = "analysis/trafficFlowMain";

	@Autowired
	private SystemManager systemManager;

	@Autowired
	private TrafficFlowManager trafficFlowManager;

	@RequestMapping
	public ModelAndView forward(HttpServletRequest request) {
		try {
			request.setAttribute("hpyslist", this.systemManager
					.getCodes("031001"));
			request.setAttribute("cllxlist", this.systemManager
					.getCodes("030104"));
			request.setAttribute("csyslist", this.systemManager
					.getCodes("030108"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(this.mainPage);
	}

	@RequestMapping
	@ResponseBody
	public Object getAnalysisFlow(HttpServletRequest request,
			TrafficFlowDTO dto, HttpServletResponse response) {
		List<List<? extends Object>> datas = null;
		String xmlString = null;
		try {
			List<String> titleType = new ArrayList<String>();
			TrafficFlow trafficFlow = new TrafficFlow(dto);
			// 按小时或天统计
			boolean flag = trafficFlow.isHourDisplay();
			// 存取字段KEY
			List<String> keys = new ArrayList<String>();
			if (flag) {
				// 生成X坐标值
				int maxHours = DateUtil.getMaxHour();
				// X=24小时
				for (int i = 0; i <= maxHours; i++) {
					//String key = Common.addZero(i);
					keys.add(String.valueOf(i));
				}
				titleType.add(trafficFlow.getKssj());
				titleType.add(trafficFlow.countRelativeDay());
				List<? extends Object> originalList = this.trafficFlowManager
						.queryFlowPeerHour(trafficFlow);
				List<List<? extends Object>> hours = handleResultAdapter(
						originalList, titleType);
				datas = this.genericDisplayDatas(hours, keys, new String[] {
						"rt", "total" });
				xmlString = this.genericXML(datas, titleType);

			} else {
				int n1 = DateUtil.getMaxDayOfMonth(trafficFlow.getKssj());
				int n2 = DateUtil.getMaxDayOfMonth(trafficFlow.countRelativeMonthDay());
				// 同比月中最大天数
				int max = (n1 > n2) ? n1 : n2;
				// X=整个月
				for (int j = 1; j <= max; j++) {
					//String key = Common.addZero(j);
					keys.add(String.valueOf(j));
				}
				titleType.add(DateUtil.formatByMonth(trafficFlow.getKssj()));
				titleType.add(DateUtil.formatByMonth(trafficFlow
						.countRelativeMonthDay()));
				//List<? extends Object> originalList = this.trafficFlowManager
				//		.queryFlowPeerDay(trafficFlow);
				
				List<List<? extends Object>> days = this.trafficFlowManager.queryTotalFlowPeerDay(trafficFlow);
				
				//List<List<? extends Object>> days = handleResultAdapter(
				//		originalList, titleType);
				
				datas = this.genericDisplayDatas(days, keys, new String[] {
						"rt", "total" });
				xmlString = this.genericXML(datas, titleType);
			}
			datas.add(titleType);
			List<String> chartXml = new ArrayList<String>();
			// System.out.println(xml2Json(xmlString));
			chartXml.add(xml2Json(xmlString));
			datas.add(chartXml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}

	private String xml2Json(String xml) {
		String _temp = new XMLSerializer().read(xml).toString();
		return _temp.replaceAll("@", "");
	}

	/**
	 * 适配器(用后台数据与前端数据适配)
	 * @param list
	 * @param titleType
	 * @return
	 */
	private List<List<? extends Object>> handleResultAdapter(
			List<? extends Object> list, List<String> titleType) {
		List<List<? extends Object>> temp = new ArrayList<List<? extends Object>>();
		List<Map<String, Object>> t1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> t2 = new ArrayList<Map<String, Object>>();
		temp.add(t1);
		temp.add(t2);
		String c = titleType.get(0);
		String b = titleType.get(1);
		Map<String, Object> e = null;
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) list.get(i);
			String rp = (String) map.get("RP");
			if (c.equalsIgnoreCase(rp)) {
				e = new HashMap<String, Object>();
				e.putAll(map);
				t1.add(e);
			} else if (b.equalsIgnoreCase(rp)) {
				e = new HashMap<String, Object>();
				e.putAll(map);
				t2.add(e);
			}
		}
		return temp;
	}

	/**
	 * 数据转化成MSLine图需要XML字符串
	 * 将XML返回到前端前需要自定义转化为JSON
	 * @param list
	 * @param titleType
	 * @return
	 * @throws Exception
	 */
	private String genericXML(List<List<? extends Object>> list,
			List<String> titleType) throws Exception {
		List<String> grapStr = new ArrayList<String>();
		grapStr.add("<graph baseFont='SunSim' baseFontSize='15' caption='流量分析' subcaption=''" 
			    + " yAxisMinValue='0' yAxisMaxValue='100' xaxisname='时间 ' yaxisname='过车量'" 
				+ " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumberScale='0' " 
				+ "decimalPrecision='0' showvalues='1' numdivlines='10' numVdivlines='0' " 
				+ "shownames='1' rotateNames='0' bgColor='#E6F0FF' alternateHGridColor='#DEE8FD'>");
		grapStr
				.add("<categories font='Arial' fontSize='11' fontColor='000000'>");
		List<String> xs = (List<String>) list.get(0);
		for (String key : xs) {
			grapStr.add("<category name='");
			grapStr.add(key);
			grapStr.add("'/>");
		}
		grapStr.add("</categories>");
		List<Map<String, Object>> data = (List<Map<String, Object>>) list
				.get(1);
		String[] colors = { "FDC12E", "56B9F9" };
		for (int i = 0; i < data.size(); i++) {
			grapStr.add("<dataset seriesname='");
			grapStr.add(titleType.get(i));
			grapStr.add("' color='");
			grapStr.add(colors[i]);
			grapStr.add("' showValues='1' alpha='100'>");
			Map<String, Object> map = data.get(i);
			for (Entry<String, Object> c : map.entrySet()) {
				String j = String.valueOf(c.getValue());
				grapStr.add("<set value='");
				grapStr.add(j);
				grapStr.add("'/>");
			}
			grapStr.add("</dataset>");
		}
		grapStr.add("</graph>");
		return StringUtils.join(grapStr, "");
	}

	/**
	 * 预处理数据
	 * 1、X坐标及值预处理，默认初始化为零
	 * 2、后台数据格式封装
	 * @param datas
	 * @param keys
	 * @param args
	 * @return
	 * @throws Exception
	 */
	private List<List<? extends Object>> genericDisplayDatas(
			List<List<? extends Object>> datas, List<String> keys,
			String... args) throws Exception {
		List<List<? extends Object>> result = new ArrayList<List<? extends Object>>();
		// 生成坐标值列表
		Map[] container = { new TreeMap<String, Object>(new KeyNumberComparator()),
				new TreeMap<String, Object>(new KeyNumberComparator()) };
		// 初始化
		for (String key : keys) {
			for (Map m : container) {
				m.put(key, "0");
			}
		}
		// 赋值
		for (int i = 0; i < datas.size(); i++) {
			List<Map<String, Object>> li = (List<Map<String, Object>>) datas.get(i);
			for (Map<String, Object> m : li) {
				container[i].put(String.valueOf(m.get(args[0])), m.get(args[1]));
			}
		}
		List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
		result.add(keys);
		for (Map map : container) {
			temp.add(map);
		}
		result.add(temp);
		return result;
	}
}
