package com.sunshine.monitor.comm.util.orm.helper;

import java.util.ArrayList;
import java.util.List;

public class OrderByGroup implements OrderBy {
	private List<OrderByItem> obis = new ArrayList<OrderByItem>();
	
	
	public String toSql() {
		if(obis.size() == 0) return "";
		StringBuilder sb = new StringBuilder();
		sb.append("ORDER BY ") ;
		for(OrderByItem obi : obis){
			sb.append(obi.toSql());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	public OrderByGroup ASC(String name){
		obis.add(new OrderByItem(name, "ASC"));
		return this;
	}
	
	public OrderByGroup DESC(String name){
		obis.add(new OrderByItem(name, "DESC"));
		return this;
	}
	
	public List<OrderByItem> getObis(){
		return this.obis;
	}
}
