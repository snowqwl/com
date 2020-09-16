package com.sunshine.monitor.comm.bean;

public class Result {
  private int jg = 0;
  private String info = "";

  public Result() {
	  
  }

  public Result(int jg, String info) {
    this.jg = jg;
    this.info = info;
  }

  public int getJg() {
    return this.jg;
  }

  public String getInfo() {
    return this.info;
  }

  /**
   * 文字信息
   * @param info
   */
  public void setInfo(String info) {
    this.info = info;
  }

  /**
   * 1-成功；0-失败
   * @param jg
   */
  public void setJg(int jg) {
    this.jg = jg;
  }
}