package com.sunshine.monitor.comm.bean;

public class Page extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int curPage;
	
	private int pageSize;

	private int total;
	
	public Page(int curPage,int pageSize){
		this.curPage=curPage;
		this.pageSize=pageSize;
	}
	
	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	

}
