package com.easymap.bean;

import java.util.Date;

public class MonitorInfo {
	
//	private static List monitorInfos = new ArrayList();
	private String gpsid = null;
	private Date fromtime = null;
	private Date totime = null;
	private float x = 0.0F;
	private float y = 0.0F;
	private float speed = 0.0F;
	private float dir = 0.0F;
	private String stars = null;
	private Date time = null;
	private String mobileid = null;

//	public static List getMonitorInfo() {
//		return monitorInfos;
//	}

	public String getGpsid() {
		return this.gpsid;
	}

	public void setGpsid(String paramString) {
		this.gpsid = paramString;
	}

	public Date getFromtime() {
		return this.fromtime;
	}

	public void setFromtime(Date paramDate) {
		this.fromtime = paramDate;
	}

	public Date getTotime() {
		return this.totime;
	}

	public void setTotime(Date paramDate) {
		this.totime = paramDate;
	}

	public float getX() {
		return this.x;
	}

	public void setX(float paramFloat) {
		this.x = paramFloat;
	}

	public float getY() {
		return this.y;
	}

	public void setY(float paramFloat) {
		this.y = paramFloat;
	}

	public float getSpeed() {
		return this.speed;
	}

	public void setSpeed(float paramFloat) {
		this.speed = paramFloat;
	}

	public float getDir() {
		return this.dir;
	}

	public void setDir(float paramFloat) {
		this.dir = paramFloat;
	}

	public String getStars() {
		return this.stars;
	}

	public void setStars(String paramString) {
		this.stars = paramString;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date paramDate) {
		this.time = paramDate;
	}

	public String getMobileid() {
		return this.mobileid;
	}

	public void setMobileid(String paramString) {
		this.mobileid = paramString;
	}
}