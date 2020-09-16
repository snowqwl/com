package com.sunshine.monitor.system.analysis.bean;

import com.sunshine.monitor.comm.dao.annotation.Column;
import com.sunshine.monitor.comm.dao.annotation.DIC;

/**
 * field 的顺序等不能变动，自动创建表字段顺序决定了
 * insert into select ... 的顺序 , 相关代码see
 * {@link com.sunshine.monitor.system.analysis.service.impl.NightCarServiceImpl NightCarServiceImpl}
 * .computeNightCarInOneDay()方法
 * @author lifenghu
 *
 */
public class NightCarTemp {
	@Column(size=2000)
	private String groupId;
	@Column
	private String dateStr;
	@Column
	private String carDate;
	@Column
	private String carNo;
	@Column(size=5)
	@DIC(dicFieldName="licenseColorMc",dicType="031001")
	private String licenseColor;
	private String licenseColorMc;
	@Column(size=5)
	@DIC(dicFieldName="licenseTypeMc",dicType="030107")
	private String licenseType;
	private String licenseTypeMc;
	@Column
	private String passCount;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getLicenseColor() {
		return licenseColor;
	}
	public void setLicenseColor(String licenseColor) {
		this.licenseColor = licenseColor;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getPassCount() {
		return passCount;
	}
	public void setPassCount(String passCount) {
		this.passCount = passCount;
	}
	public String getCarDate() {
		return carDate;
	}
	public void setCarDate(String carDate) {
		this.carDate = carDate;
	}
	public String getLicenseColorMc() {
		return licenseColorMc;
	}
	public void setLicenseColorMc(String licenseColorMc) {
		this.licenseColorMc = licenseColorMc;
	}
	public String getLicenseTypeMc() {
		return licenseTypeMc;
	}
	public void setLicenseTypeMc(String licenseTypeMc) {
		this.licenseTypeMc = licenseTypeMc;
	}
	
}
