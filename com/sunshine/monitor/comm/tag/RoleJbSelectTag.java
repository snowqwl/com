package com.sunshine.monitor.comm.tag;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.sunshine.monitor.comm.util.RoleJbEnum;

/**
 * 角色级别下拉标签
 * @author YANGYANG
 *
 */
public class RoleJbSelectTag extends SimpleTagSupport {
	
	private String id  ;
	
	private String name ;
	
	private String width ;
	
	// 比对值
	private String vkey ;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getVkey() {
		return vkey;
	}

	public void setVkey(String vkey) {
		this.vkey = vkey;
	}

	@Override
	public void doTag() throws JspException, IOException {
		Map<String,String> map = RoleJbEnum.getRoleJbMap();
		Set<Entry<String,String>> set = map.entrySet();
		Iterator<Entry<String,String>> it = set.iterator();
		StringBuffer selectStr = new StringBuffer(20);
		selectStr.append("<select id=\""+this.id+"\" name=\""+this.name+"\" class=\"easyui-combobox\" style=\"width:"+this.width+"px;\" required=\"true\">");
		while(it.hasNext()){
			Entry<String,String> entry = it.next();
			String flag = (this.vkey.equals(entry.getKey()))?" selected=\"selected\"":"";
			selectStr.append("<option value=\""+entry.getKey()+"\" "+flag+">"+entry.getValue()+"</option>");
		}
		selectStr.append("</select>");
		this.getJspContext().getOut().print(selectStr.toString()) ;
	}
}
