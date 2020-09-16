package com.sunshine.monitor.comm.bean;

/**
 * 分页工具类 
 * @author liumeng 2016-6-3
 *
 */
public class PageUtil {
	 /**
	  * 每页显示的条数
	  */
	 private int pageSize;
	 /**
	  * 总共的条数
	  */
	 private int recordCount;
	 /**
	  *  当前页面
	  */
	 private int currentPage=1;
	 public PageUtil(int pageSize, int recordCount, int currentPage) {
		 this.pageSize = pageSize;
		 this.recordCount = recordCount;
		 this.currentPage = currentPage;
		 //setCurrentPage(currentPage);
	 }
	 
	 /**
	  *  构造方法
	  * @param pageSize
	  * @param recordCount
	  */
//	 public PageUtil(int pageSize, int recordCount,int currentPage) {
//		 this(pageSize, recordCount, currentPage);
//	 }
	 
	 /**
	  *  总页数
	  * @return
	  */
	 public int getPageCount() {
		  // 总条数/每页显示的条数=总页数
		  int size = recordCount / pageSize;
		  // 最后一页的条数
		  int mod = recordCount % pageSize;
		  if (mod != 0)
		   size++;
		  return recordCount == 0 ? 1 : size;
	 }
	 
	 /**
	  *  包含，起始索引为0
	  * @return
	  */
	 public int getFromIndex() {
		 return (currentPage - 1) * pageSize;
	 }
	 
	 /**
	  *  不包含
	  * @return
	  */
	 public int getToIndex() {
		 return Math.min(recordCount, currentPage * pageSize);
	 }
	 
	 /**
	  *  得到当前页
	  * @return
	  */
	 public int getCurrentPage() {
		 return currentPage;
	 }
	 
	 /**
	  *  设置当前页
	  * @param currentPage
	  */
	 public void setCurrentPage(int currentPage) {
		 int validPage = currentPage <= 0 ? 1 : currentPage;
		 validPage = validPage > getPageCount() ? getPageCount() : validPage;
		 this.currentPage = validPage;
	 }
	 
	 /**
	  *  得到每页显示的条数
	  * @return
	  */
	 public int getPageSize() {
		 return pageSize;
	 }
	 
	 /**
	  *  设置每页显示的条数
	  * @param pageSize
	  */
	 public void setPageSize(int pageSize) {
		 this.pageSize = pageSize;
	 }
	 
	 /**
	  *  得到总共的条数
	  * @return
	  */
	 public int getRecordCount() {
		 return recordCount;
	 }
	 /**
	  * 设置总共的条数
	  * @param recordCount
	  */
	 public void setRecordCount(int recordCount) {
	  this.recordCount = recordCount;
	 }
}
