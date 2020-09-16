package com.easymap.listeners;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/** @deprecated */
public class EncodingFilter
  implements Filter
{
  private FilterConfig config = null;
  private String targetEncoding = "UTF-8";

  public void destroy()
  {
    this.config = null;
    this.targetEncoding = null;
  }

  public void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain paramFilterChain)
    throws IOException, ServletException
  {
    paramServletRequest.setCharacterEncoding(this.targetEncoding);
    paramFilterChain.doFilter(paramServletRequest, paramServletResponse);
  }

  public void init(FilterConfig paramFilterConfig)
    throws ServletException
  {
    this.config = paramFilterConfig;
    this.targetEncoding = this.config.getInitParameter("encoding");
  }
}

/* Location:           C:\Users\Administrator\Desktop\gps产品\gpsServer.ear\gpsServer.war\WEB-INF\lib\gpsServer.V2.0.5.201108301200\
 * Qualified Name:     com.easymap.listeners.EncodingFilter
 * JD-Core Version:    0.5.4
 */