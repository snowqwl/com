package com.sunshine.monitor.system.analysis.bean;

import com.sunshine.monitor.comm.bean.Entity;
/**
 * 
 * @author huyaj  2017/9/13
 *
 */
public class ModelDetail extends Entity{
	
	private static final long serialVersionUID = 1L;
	//模型编号
	private String id;
	
	//模型标题
	private String title;
	
	//研判模型详细介绍
	private String content;
	
	//创建时间
	private String cjsj;
	
	//创建人
	private String cjr;
	
	//备用字段
	private String by1;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getCjr() {
		return cjr;
	}

	public void setCjr(String cjr) {
		this.cjr = cjr;
	}

	public String getBy1() {
		return by1;
	}

	public void setBy1(String by1) {
		this.by1 = by1;
	}
}
