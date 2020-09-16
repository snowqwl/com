package com.sunshine.monitor.comm.util.orm.bean;

public class StaticEntry {
	private String value;
	public StaticEntry(String value){
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return this.value;
	}
}
