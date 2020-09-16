package com.easymap.servlet.space;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easymap.dao.ISpaceQueryDao;
import com.easymap.util.ElementUtils;
import com.easymap.util.XMLOutputterUtil;
import com.sunshine.monitor.system.gate.bean.CodeGate;


@Controller
@SuppressWarnings("serial")
@RequestMapping(value="/spaceQueryCtrl.do",params="method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SpaceQueryControler { //extends HttpServlet {
	
	@Autowired
	@Qualifier("SpaceQueryDao")
	private ISpaceQueryDao spaceQueryDao;
	
	/**
	 * 检查站空间查询
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping()
	@ResponseBody
	public void jczSpaceQuery(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		try {
			String querytype = request.getParameter("type");
			String layer = request.getParameter("layer");
			long start = System.currentTimeMillis();

			Document document = null;
			
			//圆形查询
			if (querytype.equals("circle")) {
				String x = request.getParameter("x");
				String y = request.getParameter("y");
				String radius = request.getParameter("radius");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.jczFindCircleQuery(getDouble(x),getDouble(y),getDouble(radius),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			} 
			
			//矩形查询
			if (querytype.equals("rectangle")) {
				String minx = request.getParameter("minx");
				String miny = request.getParameter("miny");
				String maxx = request.getParameter("maxx");
				String maxy = request.getParameter("maxy");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.jczFindRectangleQuery(getDouble(minx),getDouble(miny),getDouble(maxx),getDouble(maxy),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			}
			
			//多边形查询
			if (querytype.equals("polygon")) {
				String strCoords = request.getParameter("strCoords");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.jczFindPolygonQuery(Integer.parseInt(startpos), Integer.parseInt(num),layer,strCoords.split(","));
			}
			
			XMLOutputterUtil.response(document, response);

			long end = System.currentTimeMillis();	
			
			System.out.println("====================查询耗时:" + (end - start) + "========================");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 卡口空间查询
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping()
	@ResponseBody
	public void kkSpaceQuery(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		try {
			String querytype = request.getParameter("type");
			String layer = request.getParameter("layer");
			long start = System.currentTimeMillis();

			Document document = null;
			
			//圆形查询
			if (querytype.equals("circle")) {
				String x = request.getParameter("x");
				String y = request.getParameter("y");
				String radius = request.getParameter("radius");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.kkFindCircleQuery(getDouble(x),getDouble(y),getDouble(radius),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			} 
			
			//矩形查询
			if (querytype.equals("rectangle")) {
				String minx = request.getParameter("minx");
				String miny = request.getParameter("miny");
				String maxx = request.getParameter("maxx");
				String maxy = request.getParameter("maxy");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.kkFindRectangleQuery(getDouble(minx),getDouble(miny),getDouble(maxx),getDouble(maxy),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			}
			
			//多边形查询
			if (querytype.equals("polygon")) {
				String strCoords = request.getParameter("strCoords");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.kkFindPolygonQuery(Integer.parseInt(startpos), Integer.parseInt(num),layer,strCoords.split(","));
			}
			
			XMLOutputterUtil.response(document, response);

			long end = System.currentTimeMillis();	
			
			System.out.println("====================查询耗时:" + (end - start) + "========================");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * 卡口空间查询 （改造）
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @date 2019/10/14
	 * @author hzy
	 */
	@RequestMapping()
	@ResponseBody
	public Map kkSpaceQuery1(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		Map remap = new HashMap();
		try {
			String querytype = request.getParameter("type");
			String layer = request.getParameter("layer");
			long start = System.currentTimeMillis();
			
			//圆形查询
			if (querytype.equals("circle")) {
				String x = request.getParameter("x");
				String y = request.getParameter("y");
				String radius = request.getParameter("radius");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				remap = spaceQueryDao.kkFindCircleQuery1(getDouble(x),getDouble(y),getDouble(radius),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			} 
			
			//矩形查询
			if (querytype.equals("rectangle")) {
				String minx = request.getParameter("minx");
				String miny = request.getParameter("miny");
				String maxx = request.getParameter("maxx");
				String maxy = request.getParameter("maxy");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				remap = spaceQueryDao.kkFindRectangleQuery1(getDouble(minx),getDouble(miny),getDouble(maxx),getDouble(maxy),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			}
			
			//多边形查询
			if (querytype.equals("polygon")) {
				String strCoords = request.getParameter("strCoords");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				remap = spaceQueryDao.kkFindPolygonQuery1(Integer.parseInt(startpos), Integer.parseInt(num),layer,strCoords.split(","));
			}		

			long end = System.currentTimeMillis();	
			
			System.out.println("====================查询耗时:" + (end - start) + "========================");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return remap;
	}
	
	
	
	/**
	 * 公安监控空间查询
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping()
	@ResponseBody
	public void gajkSpaceQuery(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		try {
			String querytype = request.getParameter("type");
			String layer = request.getParameter("layer");
			long start = System.currentTimeMillis();

			Document document = null;
			
			//圆形查询
			if (querytype.equals("circle")) {
				String x = request.getParameter("x");
				String y = request.getParameter("y");
				String radius = request.getParameter("radius");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.gajkFindCircleQuery(getDouble(x),getDouble(y),getDouble(radius),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			} 
			
			//矩形查询
			if (querytype.equals("rectangle")) {
				String minx = request.getParameter("minx");
				String miny = request.getParameter("miny");
				String maxx = request.getParameter("maxx");
				String maxy = request.getParameter("maxy");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.gajkFindRectangleQuery(getDouble(minx),getDouble(miny),getDouble(maxx),getDouble(maxy),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			}
			
			//多边形查询
			if (querytype.equals("polygon")) {
				String strCoords = request.getParameter("strCoords");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.gajkFindPolygonQuery(Integer.parseInt(startpos), Integer.parseInt(num),layer,strCoords.split(","));
			}
			
			XMLOutputterUtil.response(document, response);

			long end = System.currentTimeMillis();	
			
			System.out.println("====================查询耗时:" + (end - start) + "========================");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 电子围栏空间查询
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping()
	@ResponseBody
	public void dzwlSpaceQuery(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		try {
			String querytype = request.getParameter("type");
			String layer = request.getParameter("layer");
			long start = System.currentTimeMillis();

			Document document = null;
			
			//圆形查询
			if (querytype.equals("circle")) {
				String x = request.getParameter("x");
				String y = request.getParameter("y");
				String radius = request.getParameter("radius");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.dzwlFindCircleQuery(getDouble(x),getDouble(y),getDouble(radius),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			} 
			
			//矩形查询
			if (querytype.equals("rectangle")) {
				String minx = request.getParameter("minx");
				String miny = request.getParameter("miny");
				String maxx = request.getParameter("maxx");
				String maxy = request.getParameter("maxy");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.dzwlFindRectangleQuery(getDouble(minx),getDouble(miny),getDouble(maxx),getDouble(maxy),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			}
			
			//多边形查询
			if (querytype.equals("polygon")) {
				String strCoords = request.getParameter("strCoords");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.dzwlFindPolygonQuery(Integer.parseInt(startpos), Integer.parseInt(num),layer,strCoords.split(","));
			}
			
			XMLOutputterUtil.response(document, response);

			long end = System.currentTimeMillis();	
			
			System.out.println("====================查询耗时:" + (end - start) + "========================");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * pdt空间查询
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping()
	@ResponseBody
	public void pdtSpaceQuery(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		try {
			String querytype = request.getParameter("type");
			String layer = request.getParameter("layer");
			long start = System.currentTimeMillis();

			Document document = null;
			
			//圆形查询
			if (querytype.equals("circle")) {
				String x = request.getParameter("x");
				String y = request.getParameter("y");
				String radius = request.getParameter("radius");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.pdtFindCircleQuery(getDouble(x),getDouble(y),getDouble(radius),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			}
			
			//矩形查询
			if (querytype.equals("rectangle")) {
				String minx = request.getParameter("minx");
				String miny = request.getParameter("miny");
				String maxx = request.getParameter("maxx");
				String maxy = request.getParameter("maxy");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.pdtFindRectangleQuery(getDouble(minx),getDouble(miny),getDouble(maxx),getDouble(maxy),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			}
			
			//多边形查询
			if (querytype.equals("polygon")) {
				String strCoords = request.getParameter("strCoords");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.pdtFindPolygonQuery(Integer.parseInt(startpos), Integer.parseInt(num),layer,strCoords.split(","));
			}
			
			XMLOutputterUtil.response(document, response);

			long end = System.currentTimeMillis();	
			
			System.out.println("====================查询耗时:" + (end - start) + "========================");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 历史报警空间查询
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping()
	@ResponseBody
	public void historyAlarmQuery(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		try {
			Map<String, String> params = new HashMap<String, String>();
			
			String querytype = request.getParameter("type");
			String layer = request.getParameter("layer");
			String num = request.getParameter("num");
			String startpos = request.getParameter("startnum");
			String hphm = request.getParameter("hphm");
			String kdbh = request.getParameter("kdbh");
			String bjdl = request.getParameter("bjdl");
			String bjlx = request.getParameter("bjlx");
			String bjBeginTime = request.getParameter("bjBeginTime");
			String bjEndTime = request.getParameter("bjEndTime");
			
			params.put("querytype", querytype);
			params.put("layer", layer);
			params.put("num", num);
			params.put("startpos", startpos);
			params.put("hphm", hphm);
			params.put("kdbh", kdbh);
			params.put("bjdl", bjdl);
			params.put("bjlx", bjlx);
			params.put("bjBeginTime", bjBeginTime);
			params.put("bjEndTime", bjEndTime);
			
			long start = System.currentTimeMillis();

			Document document = null;
			
			document = spaceQueryDao.historyAlarmQuery(params);
		
			XMLOutputterUtil.response(document, response);

			long end = System.currentTimeMillis();	
			
			System.out.println("====================查询耗时:" + (end - start) + "========================");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * 历史报警空间查询(重写)
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @date 2019/10/14
	 * @author hzy
	 */
	@RequestMapping()
	@ResponseBody
	public Map historyAlarmQuery1(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		Map remap = new HashMap();
		try {
			Map<String, String> params = new HashMap<String, String>();

			String querytype = request.getParameter("type");
			String layer = request.getParameter("layer");
			String num = request.getParameter("num");
			String startpos = request.getParameter("startnum");
			String hphm = request.getParameter("hphm");
			String kdbh = request.getParameter("kdbh");
			String bjdl = request.getParameter("bjdl");
			String bjlx = request.getParameter("bjlx");
			String bjBeginTime = request.getParameter("bjBeginTime");
			String bjEndTime = request.getParameter("bjEndTime");

			params.put("querytype", querytype);
			params.put("layer", layer);
			params.put("num", num);
			params.put("startpos", startpos);
			params.put("hphm", hphm);
			params.put("kdbh", kdbh);
			params.put("bjdl", bjdl);
			params.put("bjlx", bjlx);
			params.put("bjBeginTime", bjBeginTime);
			params.put("bjEndTime", bjEndTime);

			long start = System.currentTimeMillis();


			remap = spaceQueryDao.historyAlarmQuery1(params);

			long end = System.currentTimeMillis();

			System.out.println("====================查询耗时:" + (end - start) + "========================");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return remap;
	}
	
	/**
	 * 轨迹预判查询
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping()
	@ResponseBody
	public void trackPredictionQuery(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		try {
			
			String hphm = request.getParameter("hphm");
			String layer = request.getParameter("layer");
			String x = "";
			String y = "";
			Document document = null;
			
			long start = System.currentTimeMillis();
			
			List<Map<String,Object>> predictionCar = spaceQueryDao.getPredictionCar(hphm);
			if (predictionCar.size()>0){
				for(Map<String,Object> m:predictionCar){
					if(!(m.get("KKJD") == null || "".equals(m.get("KKJD")))){
						x = m.get("KKJD").toString();
					} else {
						x = "0";
					}
					if(!(m.get("KKWD") == null || "".equals(m.get("KKWD")))){
						y = m.get("KKWD").toString();
					} else {
						y = "0";
					}
				}
				
				
				String radius = request.getParameter("radius");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.trackPredictionQuery(getDouble(x),getDouble(y),getDouble(radius),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			} else {
				Element items = new Element("items");
				items.setAttribute("display", "此号牌不存在!");
				document = ElementUtils.responseDocument(items);
			}
			
			XMLOutputterUtil.response(document, response);
			
			long end = System.currentTimeMillis();	
			
			System.out.println("====================查询耗时:" + (end - start) + "========================");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 轨迹预判查询(重写)
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @date 2019/10/14
	 * @author hzy
	 */
	@RequestMapping()
	@ResponseBody
	public Map trackPredictionQuery1(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		Map remap = new HashMap();
		try {
			String hphm = request.getParameter("hphm");
			String layer = request.getParameter("layer");
			String x = "";
			String y = "";

			long start = System.currentTimeMillis();

			List<Map<String,Object>> predictionCar = spaceQueryDao.getPredictionCar(hphm);
			if (predictionCar.size()>0){
				for(Map<String,Object> m:predictionCar){
					if(!(m.get("KKJD") == null || "".equals(m.get("KKJD")))){
						x = m.get("KKJD").toString();
					} else {
						x = "0";
					}
					if(!(m.get("KKWD") == null || "".equals(m.get("KKWD")))){
						y = m.get("KKWD").toString();
					} else {
						y = "0";
					}
				}


				String radius = request.getParameter("radius");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				remap = spaceQueryDao.trackPredictionQuery1(getDouble(x),getDouble(y),getDouble(radius),layer,Integer.parseInt(startpos), Integer.parseInt(num));
			} else {
				remap.put("display", "此号牌不存在!");
			}

			long end = System.currentTimeMillis();
			System.out.println("====================查询耗时:" + (end - start) + "========================");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return remap;
	}
	
	/**
	 * 组织机构卡口查询
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping()
	@ResponseBody
	public void kkPosition(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		try {
			String xzqhdm = request.getParameter("xzqhdm");
			int jb =Integer.parseInt(request.getParameter("jb"));
			CodeGate gate = new CodeGate();
			switch(jb){
				case 2:gate.setDwdm(xzqhdm.substring(0,2));break;
				case 3:gate.setDwdm(xzqhdm.substring(0,4));break;
				case 4:gate.setXzqh(xzqhdm);break;
			}
				
			String num = request.getParameter("num");
			String startpos = request.getParameter("startnum");
			
			long start = System.currentTimeMillis();

			Document document = null;
			document = spaceQueryDao.kkPosition(gate, Integer.parseInt(startpos), Integer.parseInt(num));
			
			XMLOutputterUtil.response(document, response);

			long end = System.currentTimeMillis();	
			
			System.out.println("====================查询耗时:" + (end - start) + "========================");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 组织机构卡口查询(重写)
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @date 2019/10/14
	 * @author hzy
	 */
	@RequestMapping()
	@ResponseBody
	public Map kkPosition1(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		Map remap = null;
		try {
			String xzqhdm = request.getParameter("xzqhdm");
			int jb =Integer.parseInt(request.getParameter("jb"));
			CodeGate gate = new CodeGate();
			switch(jb){
				case 2:gate.setDwdm(xzqhdm.substring(0,2));break;
				case 3:gate.setDwdm(xzqhdm.substring(0,4));break;
				case 4:gate.setXzqh(xzqhdm);break;
			}

			String num = request.getParameter("num");
			String startpos = request.getParameter("startnum");

			long start = System.currentTimeMillis();

			remap = spaceQueryDao.kkPosition1(gate, Integer.parseInt(startpos), Integer.parseInt(num));

			long end = System.currentTimeMillis();

			System.out.println("====================查询耗时:" + (end - start) + "========================");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return remap;
	}
	
	/**
	 * 卡口名称模糊查询
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping()
	@ResponseBody
	public void kkSearch(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String kkmcArray = request.getParameter("kkmcArray");
			String querytype = request.getParameter("type");
			String layer = request.getParameter("layer");
			long start = System.currentTimeMillis();

			Document document = null;
			
			if("null".equals(querytype)||"".equals(querytype)||querytype==null){// 直接模糊查询
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.kkSearch(kkmcArray,Integer.parseInt(startpos), Integer.parseInt(num));
			}else if (querytype.equals("circle")) {// 圆形查询
				String x = request.getParameter("x");
				String y = request.getParameter("y");
				String radius = request.getParameter("radius");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.kkFindCircleIndistinctQuery(getDouble(x),
														   getDouble(y), 
														   getDouble(radius), 
														   layer,
														   Integer.parseInt(startpos), 
														   Integer.parseInt(num),
														   kkmcArray);
			}else if (querytype.equals("rectangle")) {// 矩形查询
				String minx = request.getParameter("minx");
				String miny = request.getParameter("miny");
				String maxx = request.getParameter("maxx");
				String maxy = request.getParameter("maxy");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.kkFindRectangleIndistinctQuery(getDouble(minx),
															  getDouble(miny), 
															  getDouble(maxx), 
															  getDouble(maxy),
															  layer, 
															  Integer.parseInt(startpos),
															  Integer.parseInt(num),
															  kkmcArray);
			}else if (querytype.equals("polygon")) {// 多边形查询
				String strCoords = request.getParameter("strCoords");
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.kkFindPolygonIndistinctQuery(Integer.parseInt(startpos), 
															Integer.parseInt(num),
															layer, 
															strCoords.split(","),
															kkmcArray);
			}else if (querytype.equals("kkPosition")) {// 卡口机构树查询
				String xzqhdm = request.getParameter("xzqhdm");
				int jb =Integer.parseInt(request.getParameter("jb"));
				CodeGate gate = new CodeGate();
				switch(jb){
					case 2:gate.setDwdm(xzqhdm.substring(0,2));break;
					case 3:gate.setDwdm(xzqhdm.substring(0,4));break;
					case 4:gate.setXzqh(xzqhdm);break;
				}
					
				String num = request.getParameter("num");
				String startpos = request.getParameter("startnum");
				document = spaceQueryDao.kkPositionIndistinct(gate, Integer.parseInt(startpos), Integer.parseInt(num),kkmcArray);
			}
			XMLOutputterUtil.response(document, response);

			long end = System.currentTimeMillis();

			System.out.println("====================查询耗时:" + (end - start) + "========================");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private double  getDouble(String str){
		 return Double.parseDouble(str);
	}
}
