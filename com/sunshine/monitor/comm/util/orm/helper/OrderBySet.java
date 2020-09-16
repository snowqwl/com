package com.sunshine.monitor.comm.util.orm.helper;

import java.util.ArrayList;
import java.util.List;

public class OrderBySet implements SqlExpression {
	 private List<OrderByItem> list;
	 OrderBySet() {
	        list = new ArrayList<OrderByItem>(3);
	    }
	 
	public String toSql() {
		StringBuilder sb = new StringBuilder();
		if (!list.isEmpty()) {
            sb.append(" ORDER BY ");
            for (OrderByItem obi : list) {
                sb.append(obi.toSql());
                sb.append(", ");
            }
            sb.setCharAt(sb.length() - 2, ' ');
        }  
		return sb.toString();
	}
	public OrderBySet asc(String name) {
		list.add(new OrderByItem(name,"asc"));
		return this;
	}
	public OrderBySet desc(String name) {
		list.add(new OrderByItem(name,"desc"));
		return this;
	}
}
