package com.ht.dc.trans.receive;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ht.dc.trans.bean.TransObj;


public abstract interface ReceiveBusiness
{
  public abstract String[] doReceive(TransObj paramTransObj, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
}

