package com.sunshine.monitor.comm.util.orm.helper;


public class Pager {
	private Integer pageCount = 0;
	private Integer pageNumber = 1;
	private Integer pageSize = 10;
	
	public Pager(){}
	
	public Pager(int pageNumber, int pageSize){
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}
	
	public Pager setPageCount(Integer pageCount){
		this.pageCount = pageCount;
		return this;
	}

	public int getPageCount() {
		return this.pageCount;
	}

	public int getPageNumber() {
		return this.pageNumber;
	}

	public Pager setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
		return this;
	}

	public int getPageSize() {
		return this.pageSize;
	}
	
	/**
	 * 数据开始行序号
	 * @return
	 */
	public int getStartPage() {
		return Math.min(getEndRow() , (getPageNumber()-1)*getPageSize() + 1);
	}
	/**
	 * 数据结束行序号
	 * @return
	 */
	public int getEndPage() {
		return Math.min(getPageCount(), getEndRow());
	}
	
	public int getStartRow(){
		return  (getPageNumber()-1)*getPageSize() + 1;
	}
	
	public int getEndRow(){
		return getPageNumber()*getPageSize();
	}

	public boolean isFirst() {
		return this.pageNumber == 1;
	}

	public boolean isLast() {
		return  getEndRow() >= getPageCount() ;
	}

	public Pager setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

}
