package com.sunshine.monitor.system.analysis.bean;

public class NightCar {
	private String id ;
	private String carNo;
	private String carType;
	private String passTime;
	private Integer continueNum;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getPassTime() {
		return passTime;
	}
	public void setPassTime(String passTime) {
		this.passTime = passTime;
	}
	public Integer getContinueNum() {
		return continueNum;
	}
	public void setContinueNum(Integer continueNum) {
		this.continueNum = continueNum;
	}
	
}
