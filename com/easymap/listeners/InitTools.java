//package com.easymap.listeners;
//
//package com.easymap.listeners;
//
//import com.easymap.dbconnection.SqlProperties;
//import com.easymap.guardregionalsettings.dao.EzMapServiceClient;
//import com.easymap.rulesmanager.dao.BaseDao;
//import com.easymap.rulesmanager.dao.RegionDao;
//import com.easymap.rulesmanager.model.Layer;
//import com.easymap.rulesmanager.model.MapServiceConfig;
//import com.easymap.rulesmanager.service.EzXmlService;
//import com.easymap.version.Version;
//import java.io.StringReader;
//import java.io.Writer;
//import java.lang.reflect.Method;
//import java.sql.Clob;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//import oracle.sql.CLOB;
//import org.apache.log4j.Logger;
//import org.jdom.Attribute;
//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.input.SAXBuilder;
//import org.xml.sax.InputSource;
//
//public class InitTools
//{
//  private Logger logger = Logger.getLogger(InitTools.class);
//  private Connection conn;
//  private BaseDao dao;
//
//  public void init()
//  {
//    this.dao = new BaseDao();
//    this.conn = this.dao.getConnection();
//    if (this.conn == null)
//    {
//      this.logger.error("数据库连接不存在，请检查数据库连接的配置是否正确。");
//      this.logger.info("退出系统初始化。");
//      return;
//    }
//    String str1 = getVersion();
//    String str2 = str1.substring(0, 5);
//    String str3 = Version.getVersion().substring(1);
//    if (str1 == null)
//    {
//      this.logger.error("读取数据库版本信息发生错误！");
//      this.logger.info("结束初始化！");
//      this.dao.close();
//      return;
//    }
//    if (str3.equals(str1))
//    {
//      this.logger.info("当前系统版本为:V" + str3 + "，数据库版本为：" + str1);
//      this.logger.info("结束初始化！");
//      this.dao.close();
//      return;
//    }
//    if (!"1.3.4".equals(str2))
//    {
//      this.logger.info("当前系统版本为:V" + str3 + "，数据库版本为：" + str1);
//      if (setVersion(str3))
//        this.logger.info("把数据库版本更新为：" + str3);
//      else
//        this.logger.error("数据库版本更新失败！");
//      this.logger.info("结束初始化！");
//      this.dao.close();
//      return;
//    }
//    int i = this.dao.queryForInt("SELECT * FROM T_GPS_REGION", null);
//    if (i > 0)
//    {
//      this.logger.info("当前系统运行的数据库不正确，不能完成数据库版本信息的设置以及区域信息的初始化。");
//      this.logger.info("退出系统初始化。");
//      this.dao.close();
//      return;
//    }
//    this.logger.info("当前系统版本为:V" + str3 + "，数据库版本为：" + str1);
//    this.logger.info("进行区域数据信息的初始化。");
//    RegionDao localRegionDao = new RegionDao();
//    List localList1 = localRegionDao.getRegions();
//    if (localList1 == null)
//    {
//      this.logger.error("初始化区域数据失败！");
//      this.logger.info("退出系统初始化。");
//      this.dao.close();
//      return;
//    }
//    if (localList1.size() == 0)
//    {
//      this.logger.info("没有需要进行初始化的区域数据。");
//      if (setVersion(str3))
//        this.logger.info("把数据库版本更新为：" + str3);
//      else
//        this.logger.error("数据库版本更新失败！");
//      this.logger.info("结束初始化！");
//      this.dao.close();
//      return;
//    }
//    StringBuffer localStringBuffer = new StringBuffer();
//    for (int j = 0; j < localList1.size(); ++j)
//    {
//      if (localStringBuffer.length() > 0)
//        localStringBuffer.append(",");
//      localStringBuffer.append("'");
//      localStringBuffer.append(localList1.get(j).toString());
//      localStringBuffer.append("'");
//    }
//    List localList2 = getLinkedRegions(localStringBuffer.toString(), 0, localList1.size() + 1);
//    if (localList2 == null)
//    {
//      this.logger.error("从MapService获取需要初始化的区域信息时发生异常。");
//      this.logger.info("初始化区域将中断，请尝试重新启动服务解决此问题。");
//      this.logger.info("退出系统初始化。");
//      this.dao.close();
//      return;
//    }
//    this.logger.info("需要初始化" + localList1.size() + "条，已从MapService中获取" + localList2.size() + "条。");
//    String[] arrayOfString = new String[2];
//    String str4 = null;
//    Element localElement = null;
//    boolean bool = false;
//    String str5 = InitListener.sqlProperties.getSql("init", "updateRegion");
//    String str6 = InitListener.sqlProperties.getSql("CommitPage", "insertRegion");
//    String str7 = InitListener.sqlProperties.getSql("Region", "selectRegionClob");
//    for (int k = 0; k < localList1.size(); ++k)
//      for (int l = 0; l < localList2.size(); ++l)
//      {
//        localElement = (Element)localList2.get(l);
//        if (!localElement.getAttributeValue("id").equals((String)localList1.get(k)))
//          continue;
//        str4 = this.dao.getSequences("HIBERNATE_SEQUENCE");
//        arrayOfString[0] = str4;
//        arrayOfString[1] = localElement.getAttribute("id").getValue();
//        bool = insertRegion(str6, str7, str4, localElement.getAttributeValue("name"), localElement.getAttributeValue("shape"), localElement.getAttributeValue("geotype"), "系统初始化导入的数据！", str5, localElement.getAttributeValue("id"));
//        if (!bool)
//          this.logger.error("当前初始化第 " + (k + 1) + "条，信息：ID为" + arrayOfString[1] + "的区域同步失败！");
//        else
//          this.logger.info("当前初始化第 " + (k + 1) + "条，信息：ID为" + arrayOfString[1] + "的区域同步成功！");
//        localList2.remove(l);
//        break;
//      }
//    bool = setVersion(str3);
//    if (bool == true)
//      this.logger.info("设置数据库版本信息为：" + str3);
//    else
//      this.logger.error("设置数据库版本信息失败！");
//    this.logger.info("系统初始化结束。");
//    this.dao.close();
//  }
//
//  public boolean setVersion(String paramString)
//  {
//    String str = InitListener.sqlProperties.getSql("init", "setVersion");
//    String[] arrayOfString = { paramString };
//    try
//    {
//      return this.dao.commit(str, arrayOfString);
//    }
//    catch (SQLException localSQLException)
//    {
//      this.logger.error(localSQLException.getMessage());
//    }
//    return false;
//  }
//
//  public String getVersion()
//  {
//    String str1 = InitListener.sqlProperties.getSql("init", "getVersion");
//    Statement localStatement = null;
//    ResultSet localResultSet = null;
//    String str2 = null;
//    String str3;
//    try
//    {
//      localStatement = this.conn.createStatement();
//      localResultSet = localStatement.executeQuery(str1);
//      int i = 0;
//      while (localResultSet.next())
//      {
//        ++i;
//        str2 = localResultSet.getString("VERSION");
//      }
//      if (i > 1)
//        this.logger.warn("数据库版本信息有误，总条数：" + i + "，请检查并删除没用的版本信息");
//      str3 = str2;
//      return str3;
//    }
//    catch (Exception localException)
//    {
//      this.logger.error("发生错误：" + localException.getMessage());
//      str3 = str2;
//      return str3;
//    }
//    finally
//    {
//      if (localResultSet != null)
//        try
//        {
//          localResultSet.close();
//        }
//        catch (SQLException localSQLException5)
//        {
//        }
//      if (localStatement != null)
//        try
//        {
//          localStatement.close();
//        }
//        catch (SQLException localSQLException6)
//        {
//        }
//    }
//  }
//
//  public List getLinkedRegions(String paramString, int paramInt1, int paramInt2)
//  {
//    ArrayList localArrayList = new ArrayList();
//    List localList1 = InitListener.mapServiceConfig.getLayers();
//    EzXmlService localEzXmlService = new EzXmlService();
//    for (int i = 0; i < localList1.size(); ++i)
//    {
//      Layer localLayer = (Layer)localList1.get(i);
//      StringBuffer localStringBuffer = new StringBuffer();
//      localStringBuffer.append(localLayer.getCode());
//      localStringBuffer.append(",");
//      localStringBuffer.append(localLayer.getName());
//      localStringBuffer.append(",");
//      localStringBuffer.append("SHAPE");
//      String str1 = paramString;
//      str1 = localLayer.getCode() + " IN (" + paramString + ")";
//      String str2 = localEzXmlService.getRequestXmlByWhereClause(paramInt1, paramInt2, localLayer.getTable(), localStringBuffer.toString(), str1);
//      try
//      {
//        EzMapServiceClient localEzMapServiceClient = new EzMapServiceClient();
//        String str3 = localEzMapServiceClient.doQuery(localLayer.getUrl() + "/DirectPort", str2);
//        StringReader localStringReader = new StringReader(str3);
//        InputSource localInputSource = new InputSource(localStringReader);
//        SAXBuilder localSAXBuilder = new SAXBuilder();
//        Document localDocument = localSAXBuilder.build(localInputSource);
//        Element localElement1 = localDocument.getRootElement();
//        Element localElement2 = (Element)localElement1.getChildren().get(0);
//        Element localElement3 = (Element)localElement2.getChildren().get(0);
//        Element localElement4 = (Element)localElement3.getChildren().get(0);
//        String str4 = localElement4.getAttributeValue("geotype");
//        Element localElement5 = (Element)localElement3.getChildren().get(1);
//        List localList2 = localElement5.getChildren();
//        for (int j = 0; j < localList2.size(); ++j)
//        {
//          Element localElement6 = new Element("region");
//          Element localElement7 = (Element)localList2.get(j);
//          localElement6.setAttribute("geotype", str4);
//          Element localElement8 = (Element)localElement7.getChildren().get(1);
//          List localList3 = localElement8.getChildren();
//          for (int k = 0; k < localList3.size(); ++k)
//          {
//            Element localElement9 = (Element)localList3.get(k);
//            if (localElement9.getAttributeValue("name").equals(localLayer.getCode().toUpperCase()))
//            {
//              localElement6.setAttribute("id", localElement9.getAttributeValue("value"));
//            }
//            else if (localElement9.getAttributeValue("name").equals(localLayer.getName().toUpperCase()))
//            {
//              localElement6.setAttribute("name", localElement9.getAttributeValue("value"));
//            }
//            else
//            {
//              if (!localElement9.getAttributeValue("name").equals("SHAPE"))
//                continue;
//              localElement6.setAttribute("shape", localElement9.getAttributeValue("value"));
//            }
//          }
//          localArrayList.add(localElement6);
//        }
//      }
//      catch (Exception localException)
//      {
//        this.logger.error(localException);
//        return null;
//      }
//    }
//    return localArrayList;
//  }
//
//  public boolean insertRegion(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9)
//  {
//    PreparedStatement localPreparedStatement = null;
//    ResultSet localResultSet = null;
//    try
//    {
//      this.conn.setAutoCommit(false);
//      localPreparedStatement = this.conn.prepareStatement(paramString1);
//      localPreparedStatement.setString(1, paramString3);
//      localPreparedStatement.setString(2, paramString4);
//      localPreparedStatement.setString(3, paramString6);
//      localPreparedStatement.setString(4, paramString7);
//      localPreparedStatement.executeUpdate();
//      localPreparedStatement.close();
//      localPreparedStatement = this.conn.prepareStatement(paramString2);
//      localPreparedStatement.setString(1, paramString3);
//      localResultSet = localPreparedStatement.executeQuery();
//      Writer localWriter = null;
//      Clob localClob = null;
//      localResultSet.next();
//      localClob = localResultSet.getClob("SHAPE");
//      Object localObject1;
//      if ("oracle.sql.CLOB".equals(localClob.getClass().getName()))
//      {
//        localWriter = ((CLOB)localClob).getCharacterOutputStream();
//        localObject1 = paramString5.toCharArray();
//        localWriter.write(localObject1, 0, localObject1.length);
//      }
//      else if ("weblogic.jdbc.wrapper.Clob_oracle_sql_CLOB".equals(localClob.getClass().getName()))
//      {
//        localObject1 = localClob.getClass().getMethod("getVendorObj", new Class[0]);
//        CLOB localCLOB = (CLOB)((Method)localObject1).invoke(localClob, new Object[0]);
//        localWriter = localCLOB.getCharacterOutputStream();
//        char[] arrayOfChar = paramString5.toCharArray();
//        localWriter.write(arrayOfChar, 0, arrayOfChar.length);
//      }
//      if (localWriter != null)
//      {
//        localWriter.flush();
//        localWriter.close();
//      }
//      localPreparedStatement = this.conn.prepareStatement(paramString8);
//      localPreparedStatement.setString(1, paramString3);
//      localPreparedStatement.setString(2, paramString9);
//      localPreparedStatement.execute();
//      this.conn.commit();
//      this.conn.setAutoCommit(true);
//    }
//    catch (Exception localException)
//    {
//      this.logger.error(localException.getMessage());
//      try
//      {
//        this.conn.rollback();
//      }
//      catch (SQLException localSQLException3)
//      {
//      }
//      int i = 0;
//      return i;
//    }
//    finally
//    {
//      if (localResultSet != null)
//        try
//        {
//          localResultSet.close();
//        }
//        catch (SQLException localSQLException6)
//        {
//        }
//      if (localPreparedStatement != null)
//        try
//        {
//          localPreparedStatement.close();
//        }
//        catch (SQLException localSQLException7)
//        {
//        }
//    }
//    return true;
//  }
// }