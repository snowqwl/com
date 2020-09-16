package com.easymap.dao;

import java.util.List;
import java.util.Map;

import org.jdom.Document;

import com.sunshine.monitor.system.gate.bean.CodeGate;

public interface ISpaceQueryDao {
	
	/**
	 * 检查站画圆查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document jczFindCircleQuery(double double1, double double2,double double3, String layer, int parseInt, int parseInt2);

	
	/**
	 * 卡口画圆查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document kkFindCircleQuery(double double1, double double2,double double3, String layer, int parseInt, int parseInt2);
	/**
	 * 卡口画圆查询 （改造）
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 * @date 2019/10/14
	 * @author hzy
	 */
	public Map kkFindCircleQuery1(double double1, double double2,double double3, String layer, int parseInt, int parseInt2);

	/**
	 * 卡口画圆模糊查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @param kkmcArray
	 * @return
	 */
	public Document kkFindCircleIndistinctQuery(double double1, double double2,double double3, String layer, int parseInt, int parseInt2, String kkmcArray);

	/**
	 * 公安监控画圆查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document gajkFindCircleQuery(double double1, double double2,double double3, String layer, int parseInt, int parseInt2);

	/**
	 * 电子围栏画圆查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document dzwlFindCircleQuery(double double1, double double2,double double3, String layer, int parseInt, int parseInt2);

	/**
	 * pdt画圆查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document pdtFindCircleQuery(double double1, double double2,double double3, String layer, int parseInt, int parseInt2);

	/**
	 * 检查站矩形查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param double4
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document jczFindRectangleQuery(double double1, double double2,double double3, double double4, String layer, int parseInt,int parseInt2);
	
	/**
	 * 卡口矩形查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param double4
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document kkFindRectangleQuery(double double1, double double2,double double3, double double4, String layer, int parseInt,int parseInt2);

	/**
	 * 卡口矩形查询 （改造）
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param double4
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 * @date 2019/10/14
	 * @author hzy
	 */
	public Map kkFindRectangleQuery1(double double1, double double2,double double3, double double4, String layer, int parseInt,int parseInt2);
	
	/**
	 * 卡口矩形模糊查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param double4
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document kkFindRectangleIndistinctQuery(double double1, double double2,double double3, double double4, String layer, int parseInt,int parseInt2, String kkmcArray);

	/**
	 * 公安监控矩形查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param double4
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document gajkFindRectangleQuery(double double1, double double2,double double3, double double4, String layer, int parseInt,int parseInt2);

	/**
	 * 电子围栏矩形查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param double4
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document dzwlFindRectangleQuery(double double1, double double2,double double3, double double4, String layer, int parseInt,int parseInt2);

	/**
	 * pdt矩形查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param double4
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document pdtFindRectangleQuery(double double1, double double2,
			double double3, double double4, String layer, int parseInt,
			int parseInt2);

	/**
	 * 检查站多边形查询
	 * @param parseInt
	 * @param parseInt2
	 * @param layer
	 * @param split
	 * @return
	 */
	public Document jczFindPolygonQuery(int parseInt, int parseInt2,String layer, String[] split);

	/**
	 * 卡口多边形查询
	 * @param parseInt
	 * @param parseInt2
	 * @param layer
	 * @param split
	 * @return
	 */
	public Document kkFindPolygonQuery(int parseInt, int parseInt2,String layer, String[] split);
	/**
	 * 卡口多边形查询 （改造）
	 * @param parseInt
	 * @param parseInt2
	 * @param layer
	 * @param split
	 * @return
	 * @date 2019/10/14
	 * @author hzy
	 */
	public Map kkFindPolygonQuery1(int parseInt, int parseInt2,String layer, String[] split);
	
	/**
	 * 卡口多边形模糊查询
	 * @param parseInt
	 * @param parseInt2
	 * @param layer
	 * @param split
	 * @param kkmcArray
	 * @return
	 */
	public Document kkFindPolygonIndistinctQuery(int parseInt, int parseInt2,String layer, String[] split, String kkmcArray);

	/**
	 * 公安监控多边形查询
	 * @param parseInt
	 * @param parseInt2
	 * @param layer
	 * @param split
	 * @return
	 */
	public Document gajkFindPolygonQuery(int parseInt, int parseInt2,String layer, String[] split);

	/**
	 * 电子围栏多边形查询
	 * @param parseInt
	 * @param parseInt2
	 * @param layer
	 * @param split
	 * @return
	 */
	public Document dzwlFindPolygonQuery(int parseInt, int parseInt2,String layer, String[] split);

	/**
	 * pdt多边形查询
	 * @param parseInt
	 * @param parseInt2
	 * @param layer
	 * @param split
	 * @return
	 */
	public Document pdtFindPolygonQuery(int parseInt, int parseInt2,String layer, String[] split); 
	
	/**
	 * 历史报警查询
	 * @param parseInt
	 * @param parseInt2
	 * @param layer
	 * @param split
	 * @return
	 */
	public Document historyAlarmQuery(Map<String, String> params);
	/**
	 * 历史报警查询
	 * @param parseInt
	 * @param parseInt2
	 * @param layer
	 * @param split
	 * @return
	 * @date 2019/10/14
	 * @author hzy
	 */
	public Map historyAlarmQuery1(Map<String, String> params);
	
	/**
	 * 轨迹预判车辆最后一个轨迹点
	 * @param hphm
	 * @return
	 */
	public List<Map<String,Object>> getPredictionCar(String hphm);
	
	/**
	 * 组织结构卡口查询
	 * @param CodeGate
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document kkPosition(CodeGate gate,  int parseInt, int parseInt2);
	
	/**
	 * 组织结构卡口查询(重写)
	 * @param CodeGate
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 * @date 2019/10/14
	 * @author hzy
	 */
	public Map kkPosition1(CodeGate gate,  int parseInt, int parseInt2);
	
	/**
	 * 组织结构卡口模糊查询
	 * @param gate
	 * @param parseInt
	 * @param parseInt2
	 * @param kkmcArray
	 * @return
	 */
	public Document kkPositionIndistinct(CodeGate gate,  int parseInt, int parseInt2, String kkmcArray);
	
	/**
	 * 卡口模糊查询
	 * @param CodeGate
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document kkSearch(String kkmc,  int parseInt, int parseInt2);
	
	/**
	 * 轨迹预判查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	public Document trackPredictionQuery(double double1, double double2,double double3, String layer, int parseInt, int parseInt2);
	/**
	 * 轨迹预判查询
	 * @param double1
	 * @param double2
	 * @param double3
	 * @param layer
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 * @date 2019/10/14
	 * @author hzy
	 */
	public Map trackPredictionQuery1(double double1, double double2,double double3, String layer, int parseInt, int parseInt2);
	
}
