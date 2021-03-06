 package com.ht.dc.trans.receive.business;
 
 import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ht.dc.trans.bean.TransAlarmHandled;
import com.ht.dc.trans.bean.TransObj;
import com.ht.dc.trans.manager.TransDcManager;
import com.ht.dc.trans.receive.ReceiveBusiness;
import com.sunshine.monitor.comm.bean.Result;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
 
 public class AlarmHandledReceive
   implements ReceiveBusiness
 {
   public String[] doReceive(TransObj obj, HttpServletRequest request, HttpServletResponse response)
   {
     String[] res = new String[3];
     if (obj == null) {
       res[0] = "";
       res[1] = "0";
       res[2] = "处警传输对象为空";
       return res;
     }
     res[0] = obj.getCsxh();
     Object data = obj.getObj();
     if (data == null) {
       res[1] = "0";
       res[2] = "处警传输业务对象为空";
       return res;
     }
     if (!(data instanceof TransAlarmHandled)) {
       res[1] = "0";
       res[2] = "处警传输对象错误";
       return res;
     }
     TransDcManager transDcManager = (TransDcManager)SpringApplicationContext.getStaticApplicationContext().getBean("transDcManager"); 
     if(transDcManager == null){
         res[1] = "0";
         res[2] = "获取数据源失败";
         return res;
     }
     Result result = null;
     try {
       result = transDcManager.doReceive(obj);
     } catch (Exception e) {
       result = null;
       e.printStackTrace();
     }
     if (result == null) {
       res[1] = "0";
       res[2] = "数据库操作无返回结果（处警）";
     }
     else if (result.getJg() == 1) {
       res[1] = "1";
       res[2] = "操作成功（处警）";
     } else {
       res[1] = "0";
       res[2] = result.getInfo();
     }
 
     return res;
   }
 }
