package com.sunshine.monitor.system.analysis.bean;

import com.sunshine.monitor.comm.dao.annotation.DIC;

public class NightCarTotal {
	private String carNo;
	@DIC(dicFieldName="licenseTypeMc",dicType="030107")
	private String licenseType;
	private String licenseTypeMc; 
	@DIC(dicFieldName="licenseColorMc",dicType="031001")
	private String licenseColor;
	private String licenseColorMc;
	private String totalDay;
	private String violationCount;

	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getLicenseColor() {
		return licenseColor;
	}
	public void setLicenseColor(String licenseColor) {
		this.licenseColor = licenseColor;
	}
	
	public String getTotalDay() {
		return totalDay;
	}
	public void setTotalDay(String totalDay) {
		this.totalDay = totalDay;
	}
	public String getLicenseTypeMc() {
		return licenseTypeMc;
	}
	public void setLicenseTypeMc(String licenseTypeMc) {
		this.licenseTypeMc = licenseTypeMc;
	}
	public String getLicenseColorMc() {
		return licenseColorMc;
	}
	public void setLicenseColorMc(String licenseColorMc) {
		this.licenseColorMc = licenseColorMc;
	}
	public String getViolationCount() {
		return violationCount;
	}
	public void setViolationCount(String violationCount) {
		this.violationCount = violationCount;
	}
}
