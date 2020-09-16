 package com.ht.dc.trans.send.business;
 
 import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.ht.dc.trans.bean.TransObj;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
 
 public class SendData
 {
   public static String[] send(TransObj data, CodeUrl codeurl)
   {
     Object servResObj = null;
     try {
       StringBuffer strurl = new StringBuffer("http://");
       strurl.append(codeurl.getUrl());
       if ((codeurl.getPort() != null) && (!"".equals(codeurl.getPort()))) {
         strurl.append(":").append(codeurl.getPort());
       }
       if ((codeurl.getContext() != null) && (!"".equals(codeurl.getContext()))) {
         strurl.append("/").append(codeurl.getContext());
       }
       strurl.append("/servlet/TransReceiveServlet");
       URL url = new URL(strurl.toString());
 
       HttpURLConnection con = (HttpURLConnection)url.openConnection();
 
       con.setDoOutput(true);
 
       con.setUseCaches(false);
 
       OutputStream outs = con.getOutputStream();
 
       ObjectOutputStream objout = new ObjectOutputStream(outs);
 
       objout.writeObject(data);
 
       InputStream in = con.getInputStream();
 
       ObjectInputStream objStream = new ObjectInputStream(in);
 
       servResObj = objStream.readObject();
     } catch (Exception e) {
       e.printStackTrace();
       servResObj = e.getMessage();
     }
     String[] res = (String[])null;
     if (servResObj == null) {
       res = new String[3];
       res[0] = data.getCsxh();
       res[1] = "0";
       res[2] = "接收方返回结果为Null";
     }
     else if ((servResObj instanceof String[])) {
       res = (String[])servResObj;
     } else if ((servResObj instanceof String)) {
       res = new String[3];
       res[0] = data.getCsxh();
       res[1] = "0";
       res[2] = ((String)servResObj);
     } else {
       res = new String[3];
       res[0] = data.getCsxh();
       res[1] = "0";
       res[2] = "接收方返回结果不是String数组";
     }
 
     return res;
   }
 }
