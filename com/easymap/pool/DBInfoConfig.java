package com.easymap.pool;

import com.easymap.listeners.InitListener;

public class DBInfoConfig
{
  private static final DBInfoConfig cfg = new DBInfoConfig();

  public static DBInfoConfig getInstance()
  {
    return cfg;
  }

  public String getValue(String paramString)
  {
    return InitListener.configProperties.getProperty(paramString).trim();
  }
}
