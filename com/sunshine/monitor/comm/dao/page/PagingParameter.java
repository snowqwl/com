package com.sunshine.monitor.comm.dao.page;

import java.util.HashMap;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Entity;

/**
 * 封装分页参数
 */
public class PagingParameter extends Entity {
	
	private static final long serialVersionUID = -5871263750693828476L;

	/** 分页起始行，默认为-1，表示不分页，查询全部记录 */
	private int curPage = -1;
	/** 每页显示行数，默认为0，表示不分页，查询全部记录 */
	private int pageSize = 0;
	/** 总记录*/
	private int totalRows = 0;
	
	/**
	 * 构造方法，不指定分页起始行和每页显示行数，默认不分页，查询全部记录
	 */
	public PagingParameter(){
	}
	
	/**
	 * 构造方法
	 * @param start
	 * @param pageSize
	 */
	public PagingParameter(int curPage,int pageSize, int totalRows){
		this.curPage = curPage;
		this.pageSize = pageSize;
		this.totalRows = totalRows;
	}
	
	public PagingParameter(int curPage, int pageSize){
		this.curPage = curPage;
		this.pageSize = pageSize;
	}
	
	public int getTotalRows() {
		return totalRows;
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

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	/**
	 * 判断分页参数是否无效，如果返回true(表示分页参数无效)则不分页，查询全部的记录
	 *
	 * @return
	 */
	public boolean isInvalid() {
		return curPage < 0 || pageSize <= 0;
	}

	/**
	 * 构造开始行与结束行
	 * @return
	 */
	public Map<String,Integer> getStartAndEndRow(){
		// 总页数
		int totalPages = totalPage();
		Map<String,Integer> map = null;
		// 起始行数
		int start = (curPage - 1) * pageSize;
		// 结束行数
		int end = 0;
		if (totalRows < pageSize) {
			end = totalRows;
		} else if ((totalRows % pageSize == 0)
				|| (totalRows % pageSize != 0 && curPage < totalPages)) {
			end = curPage * pageSize;
		} else if (totalRows % pageSize != 0 && curPage == totalPages) {// 最后一页
			end = totalRows;
		}
		map = new HashMap<String,Integer>();
		map.put("start", start);
		map.put("end", end);
		return map ;
	}
	
	public Map<String,Integer> getStartAndEndRowWithoutTotal(){
		Map<String, Integer> map = null;
		// 起始行
		int start = (this.curPage - 1) * this.pageSize;
		// 结束行
		int end = this.curPage * this.pageSize;
		map = new HashMap<String,Integer>();
		map.put("start", start);
		map.put("end", end);
		return map ;
	}
	
	/**
	 * 总页数
	 * @return
	 */
	public int totalPage(){
		int totalPages = 0;
		if (pageSize != -1) {
			if (totalRows % pageSize == 0) {
				totalPages = totalRows / pageSize;
			} else {
				totalPages = (totalRows / pageSize) + 1;
			}
		} else {
			totalPages = 1;
		}
		return totalPages;
	}
}
