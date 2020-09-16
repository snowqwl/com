package com.sunshine.monitor.comm.util;

public enum MenuSort {
	
	MENU_DIRECTORY("1","目录"),
	
	MENU_ITEM("0","菜单");
	
	private String code;
	
	private String title ;
	
	private MenuSort(String code, String title){
		this.code = code ;
		this.title = title ;
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
}
