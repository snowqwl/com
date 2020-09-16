package com.sunshine.monitor.comm.util;

public enum VehCardType {
	
	LARGE_VEH("01", "大型汽车", "1", "黄"),
	
	LITTLE_VEH("02", "小型汽车", "2", "蓝"),
	
	EMBASSY_VEH("03", "使馆汽车", "3", "黑"),
	
	CONSULATE_VEH("04", "领馆汽车", "3", "黑"),
	
	OUTSIDE_VEH("05", "境外汽车", "3", "黑"),
	
	FOREIGN_VEH("06", "外籍汽车", "3", "黑"),
	
	WHEELED_MOTOR("07", "两、三轮摩托车", "1", "黄"),
	
	LIGHT_MOTOR("08", "轻便摩托车", "2", "蓝"),
	
	EMBASSY_MOTOR("09", "使馆摩托车", "3", "黑"),
	
	CONSULATE_MOTOR("10", "领馆摩托车", "3", "黑"),
	
	OUTSIDE_MOTOR("11", "境外摩托车", "3", "黑"),
	
	FOREIGN_MOTOR("12", "外籍摩托车", "3", "黑"),
	
	FARM_VEH("13", "农用运输车", "1", "黄"),
	
	TRACTOR_VEH("14", "拖拉机", "1", "黄"),
	
	MOTOR_VEH("15", "挂车", "1", "黄"),
	
	COACH_VEH("16", "教练汽车", "1", "黄"),
	
	COACH_MOTOR("17", "教练摩托车", "1", "黄"),
	
	TEST_VEH("18", "试验汽车", "1", "黄"),
	
	TEST_MOTOR("19", "试验摩托车", "1", "黄"),
	
	TEMP_INNER_VEH("20", "临时入境汽车", "0", "白"),
	
	TEMP_INNER_MOTOR("21", "临时入境摩托车", "0", "白"),
	
	TEMP_VEH("22", "临时行驶车", "0", "白"),
	
	POLICE_VEH("23", "警用汽车", "0", "白"),
	
	POLICE_MOTOR("24", "警用摩托", "0", "白"),
	
	MILITARY("25", "军牌", "0", "白"),
	
	POLICE_WJ("26", "武警", "0", "白"),
	
	OTHER("99", "其他", "4", "其它颜色");
	
	private String code;
	
	private String title;
	
	private String colorCode;
	
	private String colorDesc;
	
	private VehCardType(String code, String title, String colorCode, String colorDesc) {
		this.code = code;
		this.title = title;
		this.colorCode = colorCode ;
		this.colorDesc = colorDesc ;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getColorDesc() {
		return colorDesc;
	}

	public void setColorDesc(String colorDesc) {
		this.colorDesc = colorDesc;
	}
	
	public static VehCardType[] getVehCardTypes(){
		
		return VehCardType.values() ;
	}
	
	public static String getColorByCartype(String code){
		VehCardType[] arry = getVehCardTypes();
		for(VehCardType vc : arry){
			String _code = vc.getCode();
			if(_code.equals(code)){
				return vc.getColorCode();
			}
		}
		return null;
	}
	
}
