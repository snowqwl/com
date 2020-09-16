/*package com.sunshine.monitor.comm.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.WebUtils;

import cn.com.hnisi.authentication.client.ILoginInfoRegister;

import com.sunshine.monitor.comm.bean.UserSession;

import edu.yale.its.tp.cas.client.CASAuthenticationException;
import edu.yale.its.tp.cas.client.CASReceipt;
import edu.yale.its.tp.cas.client.ProxyTicketValidator;
import edu.yale.its.tp.cas.client.Util;
import edu.yale.its.tp.cas.client.filter.CASFilterRequestWrapper;

public class CASFilterExtend implements Filter {
	
	private static Log log = LogFactory.getLog(CASFilterExtend.class);
	
	public static final String ANONYMOUS_USER_ID = "anonymous";
	public static final String LOGIN_INFO_REGISTER_CLASS_PARAM = "loginInfoRegisterClass";
	public static final String CASSEVERNAME_INIT_PARAM = "CASServerIP";
	public static final String SECOND_SERVICE_INIT_PARAM = "serverIP";
	public static final String CASSERVER_LOGIN_IP = "casserverLoginIP";
	public static final String CASSERVER_VALIDATE_IP = "casserverValidateIP";
	public static final String IGNORE_URL_INIT_PARAM = "ignoreURLS";
	public static final String LOGIN_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.loginUrl";
	public static final String VALIDATE_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.validateUrl";
	public static final String SERVICE_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.serviceUrl";
	public static final String SERVERNAME_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.serverName";
	public static final String RENEW_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.renew";
	public static final String AUTHORIZED_PROXY_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.authorizedProxy";
	public static final String PROXY_CALLBACK_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.proxyCallbackUrl";
	public static final String WRAP_REQUESTS_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.wrapRequest";
	public static final String GATEWAY_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.gateway";
	public static final String CAS_FILTER_USER = "SSOUserId";
	public static final String CAS_FILTER_RECEIPT = "edu.yale.its.tp.cas.client.filter.receipt";
	private static final String CAS_FILTER_GATEWAYED = "edu.yale.its.tp.cas.client.filter.didGateway";
	
	private String loginInfoRegisterClassName;
	private Class logicInfoRegistClass;
	private String casLogin;
	private String casValidate;
	private String casServiceUrl;
	private String casServerName;
	private String casProxyCallbackUrl;
	private boolean casRenew;
	private boolean wrapRequest;
	private boolean casGateway;
	private List authorizedProxies;
	private String[] ignoreUriArray;
	private AntPathMatcher pathMatcher;
	public static String casLogout;

	public CASFilterExtend() {
		this.casGateway = false;
		this.authorizedProxies = new ArrayList();
	}

	public void init(FilterConfig config) throws ServletException {
		this.loginInfoRegisterClassName = config
				.getInitParameter("loginInfoRegisterClass");
		this.casLogin = config
				.getInitParameter("edu.yale.its.tp.cas.client.filter.loginUrl");
		this.casValidate = config
				.getInitParameter("edu.yale.its.tp.cas.client.filter.validateUrl");
		this.casServiceUrl = config
				.getInitParameter("edu.yale.its.tp.cas.client.filter.serviceUrl");
		String casAuthorizedProxy = config
				.getInitParameter("edu.yale.its.tp.cas.client.filter.authorizedProxy");
		this.casRenew = Boolean
				.valueOf(config.getInitParameter("edu.yale.its.tp.cas.client.filter.renew"))
				.booleanValue();
		this.casServerName = config
				.getInitParameter("edu.yale.its.tp.cas.client.filter.serverName");
		this.casProxyCallbackUrl = config
				.getInitParameter("edu.yale.its.tp.cas.client.filter.proxyCallbackUrl");
		this.wrapRequest = Boolean
				.valueOf(config.getInitParameter("edu.yale.its.tp.cas.client.filter.wrapRequest"))
				.booleanValue();
		this.casGateway = Boolean
				.valueOf(config.getInitParameter("edu.yale.its.tp.cas.client.filter.gateway"))
				.booleanValue();
		String tempIgnoreUriStr = config.getInitParameter("ignoreURLS");
		String tempCASServerName = config.getInitParameter("CASServerIP");
		String tempCasserverLoginIP = config
				.getInitParameter("casserverLoginIP");
		String tempCasserverValidateIP = config
				.getInitParameter("casserverValidateIP");
		if ((tempIgnoreUriStr != null) && (!tempIgnoreUriStr.equals(""))) {
			this.ignoreUriArray = tempIgnoreUriStr.split(";");
			this.pathMatcher = new AntPathMatcher();
		}
		if ((this.loginInfoRegisterClassName == null)
				|| (this.loginInfoRegisterClassName.equals("")))
			this.loginInfoRegisterClassName = "cn.com.hnisi.authentication.client.DefaultLoginInfoRegister";
		createLogicInfoRegistClass();
		if ((this.casServerName == null) || (this.casServerName.equals("")))
			this.casServerName = config.getInitParameter("serverIP");
		if (((this.casLogin == null) || (this.casLogin.equals("")))
				&& (tempCasserverLoginIP != null)
				&& (!tempCasserverLoginIP.equals("")))
			this.casLogin = ("http://" + tempCasserverLoginIP + "/casserver/login");
		if (((casLogout == null) || (this.casLogin.equals("")))
				&& (tempCasserverLoginIP != null)
				&& (!tempCasserverLoginIP.equals("")))
			casLogout = "http://" + tempCasserverLoginIP + "/casserver/logout";
		if (((this.casLogin == null) || (this.casLogin.equals("")))
				&& (tempCASServerName != null)
				&& (!tempCASServerName.equals("")))
			this.casLogin = ("http://" + tempCASServerName + "/casserver/login");
		if (((casLogout == null) || (this.casLogin.equals("")))
				&& (tempCASServerName != null)
				&& (!tempCASServerName.equals("")))
			casLogout = "http://" + tempCASServerName + "/casserver/logout";
		if (((this.casValidate == null) || (this.casValidate.equals("")))
				&& (tempCasserverValidateIP != null)
				&& (!tempCasserverValidateIP.equals("")))
			this.casValidate = ("http://" + tempCasserverValidateIP + "/casserver/serviceValidate");
		if (((this.casValidate == null) || (this.casValidate.equals("")))
				&& (tempCASServerName != null)
				&& (!tempCASServerName.equals("")))
			this.casValidate = ("http://" + tempCASServerName + "/casserver/serviceValidate");
		if ((this.casGateway)
				&& (Boolean.valueOf(this.casRenew).booleanValue()))
			throw new ServletException(
					"gateway and renew cannot both be true in filter configuration");
		if ((this.casServerName != null) && (this.casServiceUrl != null))
			throw new ServletException(
					"serverName and serviceUrl cannot both be set: choose one.");
		if ((this.casServerName == null) && (this.casServiceUrl == null))
			throw new ServletException(
					"one of serverName or serviceUrl must be set.");
		if ((this.casServiceUrl != null)
				&& (!this.casServiceUrl.startsWith("https://"))
				&& (!this.casServiceUrl.startsWith("http://")))
			throw new ServletException(
					"service URL must start with http:// or https://; its current value is ["
							+ this.casServiceUrl + "]");
		if (this.casValidate == null)
			throw new ServletException("validateUrl parameter must be set.");
		if (casAuthorizedProxy != null) {
			String anAuthorizedProxy;
			for (StringTokenizer casProxies = new StringTokenizer(
					casAuthorizedProxy); casProxies.hasMoreTokens(); this.authorizedProxies.add(anAuthorizedProxy)) {
				anAuthorizedProxy = casProxies.nextToken();
				if (!anAuthorizedProxy.startsWith("https://")) {
					throw new ServletException(
							"CASFilter initialization parameter for authorized proxies must be a whitespace delimited list of authorized proxies.  Authorized proxies must be secure (https) addresses.  This one wasn't: ["
									+ anAuthorizedProxy + "]");
				}
			}
		}
		if (log.isDebugEnabled())
			log.debug("CASFilter initialized as: [" + toString() + "]");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain fc) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String url = httpRequest.getRequestURI() + "?method="
				+ request.getParameter("method");
		//System.out.println(url);
		if (url.indexOf("vehRealpass.do?method=queryList") > 0) {
			fc.doFilter(request, response);
			return;
		}
		if (url.indexOf("vehRealpass.do?method=realLists") > 0) {
			fc.doFilter(request, response);
			return;
		}
		if (url.indexOf("vehRealpass.do?method=realView") > 0) {
			fc.doFilter(request, response);
			return;
		}
		if (url.indexOf("vehRealalarm.do?method=queryList") > 0) {
			fc.doFilter(request, response);
			return;
		}
		if (url.indexOf("vehRealalarm.do?method=realLists") > 0) {
			fc.doFilter(request, response);
			return;
		}
		if (url.indexOf("vehRealalarm.do?method=realView") > 0) {
			fc.doFilter(request, response);
			return;
		}
		if (url.indexOf("ajax.do") > 0) {
			fc.doFilter(request, response);
			return;
		}
		if (url.indexOf("ajax.do?method=getVehicle")>0){
			fc.doFilter(request, response);
			return;
		}
		if (url.indexOf("main.do?method=toZoomJsp") > 0) {
			fc.doFilter(request, response);
			return;
		}
		if (url.indexOf("public.do") > 0) {
			fc.doFilter(request, response);
			return;
		}
		if (url.indexOf("ajax.do?method=getRealPassList") > 0) {
			fc.doFilter(request, response);
			return;
		}
		if(url.indexOf("realpassCtrl.do?method=queryList")>0){
			fc.doFilter(request,response);
			return;
		}
		if(url.indexOf("realpassCtrl.do?method=realList")>0){
			fc.doFilter(request,response);
			return;
		}
		if(url.indexOf("realpassCtrl.do?method=realView")>0){
			fc.doFilter(request,response);
			return;
		}
		if(url.indexOf("realalarm.do?method=queryList")>0){
			fc.doFilter(request,response);
			return;
		}
		if(url.indexOf("realalarm.do?method=queryRealMultiList")>0){
			fc.doFilter(request, response);
			return;
		}
		if(url.indexOf("realalarm.do?method=realView")>0){
			fc.doFilter(request, response);
			return;
		}
		if(url.indexOf("realalarm.do?method=forwardRealMultiDetail")>0){
			fc.doFilter(request, response);
			return;
		}
		String uri = httpRequest.getRequestURI();
		if (uri.indexOf("login.do") > 0) {
			String sunshine = request.getParameter("sunshine");
			if (("1".equals(sunshine)) || ("2".equals(sunshine))) {
				fc.doFilter(request, response);
				return;
			}
		}

		UserSession userSession = null;
		userSession = (UserSession) WebUtils.getSessionAttribute(
				(HttpServletRequest) request, "userSession");
		if (userSession != null) {
			fc.doFilter(request, response);
			return;
		}
		if (log.isTraceEnabled()) {
			log.trace("entering doFilter()");
		}
		HttpSession session = ((HttpServletRequest) request).getSession();
		String logoutParam = httpRequest.getParameter("logoutParam");
		if ((logoutParam != null) && (!logoutParam.equals(""))) {
			logoutSystem(response, session, logoutParam);
			return;
		}
		if ((this.ignoreUriArray != null)
				&& (isIgnore(getRequestURI(httpRequest)))) {
			fc.doFilter(request, response);
			return;
		}
		if ((!(request instanceof HttpServletRequest))
				|| (!(response instanceof HttpServletResponse))) {
			log
					.error("doFilter() called on a request or response that was not an HttpServletRequest or response.");
			throw new ServletException("CASFilter protects only HTTP resources");
		}
		if ((this.casProxyCallbackUrl != null)
				&& (this.casProxyCallbackUrl
						.endsWith(((HttpServletRequest) request)
								.getRequestURI()))
				&& (request.getParameter("pgtId") != null)
				&& (request.getParameter("pgtIou") != null)) {
			log.trace("passing through what we hope is CAS's request for proxy ticket receptor.");
			fc.doFilter(request, response);
			return;
		}
		if (this.wrapRequest) {
			log.trace("Wrapping request with CASFilterRequestWrapper.");
			request = new CASFilterRequestWrapper((HttpServletRequest) request);
		}
		CASReceipt receipt = (CASReceipt) session
				.getAttribute("edu.yale.its.tp.cas.client.filter.receipt");
		String localeUserId = (String) session.getAttribute("SSOUserId");
		if (((receipt != null) && (isReceiptAcceptable(receipt)))
				|| ((localeUserId != null) && (localeUserId.equals("anonymous")))) {
			log
					.trace("CAS_FILTER_RECEIPT attribute was present and acceptable - passing  request through filter..");
			fc.doFilter(request, response);
			return;
		}
		String SSOuserId = httpRequest.getParameter("SSOuserId");
		if ((SSOuserId != null) && (SSOuserId.equals("anonymous"))) {
			session.setAttribute("SSOUserId", "anonymous");
			session
					.removeAttribute("edu.yale.its.tp.cas.client.filter.didGateway");
			invokeLoginInfoRegist((HttpServletRequest) request,
					(HttpServletResponse) response);
			fc.doFilter(request, response);
			return;
		}
		String ticket = request.getParameter("ticket");
		if ((ticket == null) || (ticket.equals(""))) {
			log.trace("CAS ticket was not present on request.");
			boolean didGateway = Boolean
					.valueOf(
							(String) session
									.getAttribute("edu.yale.its.tp.cas.client.filter.didGateway"))
					.booleanValue();
			if (this.casLogin == null) {
				log
						.fatal("casLogin was not set, so filter cannot redirect request for authentication.");
				throw new ServletException(
						"When CASFilter protects pages that do not receive a 'ticket' parameter, it needs a edu.yale.its.tp.cas.client.filter.loginUrl filter parameter");
			}
			if (!didGateway) {
				if (((HttpServletRequest) request).getHeader("referer") == null) {
					log
							.trace("Did not previously gateway.  Setting session attribute to true.");
					session.setAttribute(
							"edu.yale.its.tp.cas.client.filter.didGateway",
							"true");
					redirectToCAS((HttpServletRequest) request,
							(HttpServletResponse) response);
				} else {
					((HttpServletResponse) response)
							.sendRedirect("./reset.jsp");
				}
				return;
			}
			log.trace("Previously gatewayed.");
			if ((this.casGateway)
					|| (session.getAttribute("SSOUserId") != null)) {
				log.trace("casGateway was true and CAS_FILTER_USER set: passing request along filter chain.");
				fc.doFilter(request, response);
				return;
			}

			session.setAttribute("edu.yale.its.tp.cas.client.filter.didGateway", "true");
			redirectToCAS((HttpServletRequest) request,
					(HttpServletResponse) response);
			return;
		}

		try {
			receipt = getAuthenticatedUser(httpRequest);
		} catch (CASAuthenticationException e) {
			log.error(e);
			throw new ServletException(e);
		}
		if (!isReceiptAcceptable(receipt))
			throw new ServletException(
					"Authentication was technically successful but rejected as a matter of policy. ["
							+ receipt + "]");
		if (session != null) {
			String userID = receipt.getUserName();
			if (userID.endsWith(",PKI")) {
				userID = userID.substring(0, userID.length() - 4);
				session.setAttribute("isca", "true");
			} else {
				session.setAttribute("isca", "false");
			}
			session.setAttribute("SSOUserId", userID);
			session.setAttribute("edu.yale.its.tp.cas.client.filter.receipt",
					receipt);
			session.removeAttribute("edu.yale.its.tp.cas.client.filter.didGateway");
			invokeLoginInfoRegist((HttpServletRequest) request,
					(HttpServletResponse) response);
		}
		if (log.isTraceEnabled())
			log.trace("validated ticket to get authenticated receipt ["
					+ receipt + "], now passing request along filter chain.");
		fc.doFilter(request, response);
		log.trace("returning from doFilter()");
	}

	private void logoutSystem(ServletResponse response, HttpSession session,
			String logoutParam) throws IOException {
		session.invalidate();
		int nextLogoutAddressEndIndex = logoutParam.indexOf(";");
		String nextLogoutAddress = null;
		String newLogoutParam = null;
		if (nextLogoutAddressEndIndex > -1) {
			nextLogoutAddress = logoutParam.substring(0,
					nextLogoutAddressEndIndex);
			newLogoutParam = logoutParam
					.substring(nextLogoutAddressEndIndex + 1);
		} else {
			nextLogoutAddress = logoutParam;
			newLogoutParam = casLogout;
		}
		((HttpServletResponse) response).sendRedirect(nextLogoutAddress
				+ "?logoutParam=" + newLogoutParam);
	}

	private void createLogicInfoRegistClass() {
		try {
			this.logicInfoRegistClass = Class.forName(
					this.loginInfoRegisterClassName, false, Thread
							.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException ignore) {
			try {
				this.logicInfoRegistClass = Class.forName(
						this.loginInfoRegisterClassName, false,
						CASFilterExtend.class.getClassLoader());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("没有找到:"
						+ this.loginInfoRegisterClassName + "这个登录信息注册类");
			}
		}
	}

	private String getRequestURI(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		return servletPath;
	}

	private boolean isIgnore(String uri) {
		if ((this.ignoreUriArray != null) && (this.ignoreUriArray.length > 0)) {
			for (int i = 0; i < this.ignoreUriArray.length; i++)
				if (this.pathMatcher.match(this.ignoreUriArray[i], uri))
					return true;
		}
		return false;
	}

	private void invokeLoginInfoRegist(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ILoginInfoRegister register = (ILoginInfoRegister) this.logicInfoRegistClass
					.newInstance();
			register.regist(request, response);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isReceiptAcceptable(CASReceipt receipt) {
		if (receipt == null)
			throw new IllegalArgumentException(
					"Cannot evaluate a null receipt.");
		return (!this.casRenew) || (receipt.isPrimaryAuthentication());
	}

	private CASReceipt getAuthenticatedUser(HttpServletRequest request)
			throws ServletException, CASAuthenticationException {
		log.trace("entering getAuthenticatedUser()");
		ProxyTicketValidator pv = null;
		pv = new ProxyTicketValidator();
		pv.setCasValidateUrl(this.casValidate);
		pv.setServiceTicket(request.getParameter("ticket"));
		pv.setService(getService(request));
		pv.setRenew(Boolean.valueOf(this.casRenew).booleanValue());
		if (this.casProxyCallbackUrl != null)
			pv.setProxyCallbackUrl(this.casProxyCallbackUrl);
		if (log.isDebugEnabled())
			log.debug("about to validate ProxyTicketValidator: [" + pv + "]");
		return CASReceipt.getReceipt(pv);
	}

	private String getService(HttpServletRequest request)
			throws ServletException {
		log.trace("entering getService()");
		if ((this.casServerName == null) && (this.casServiceUrl == null))
			throw new ServletException(
					"need one of the following configuration parameters: edu.yale.its.tp.cas.client.filter.serviceUrl or edu.yale.its.tp.cas.client.filter.serverName");
		String serviceString;
		if (this.casServiceUrl != null)
			serviceString = URLEncoder.encode(this.casServiceUrl);
		else
			serviceString = Util.getService(request, this.casServerName);
		if (log.isTraceEnabled())
			log.trace("returning from getService() with service ["
					+ serviceString + "]");
		return serviceString;
	}

	private void redirectToCAS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		if (log.isTraceEnabled())
			log.trace("entering redirectToCAS()");
		String casLoginString = this.casLogin + "?service="
				+ getService(request) + (this.casRenew ? "&renew=true" : "")
				+ (this.casGateway ? "&gateway=true" : "");
		if (log.isDebugEnabled())
			log.debug("Redirecting browser to [" + casLoginString + ")");
		response.sendRedirect(casLoginString);
		if (log.isTraceEnabled())
			log.trace("returning from redirectToCAS()");
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[CASFilter:");
		sb.append(" casGateway=");
		sb.append(this.casGateway);
		sb.append(" wrapRequest=");
		sb.append(this.wrapRequest);
		sb.append(" casAuthorizedProxies=[");
		sb.append(this.authorizedProxies);
		sb.append("]");
		if (this.casLogin != null) {
			sb.append(" casLogin=[");
			sb.append(this.casLogin);
			sb.append("]");
		} else {
			sb.append(" casLogin=NULL!!!!!");
		}
		if (this.casProxyCallbackUrl != null) {
			sb.append(" casProxyCallbackUrl=[");
			sb.append(this.casProxyCallbackUrl);
			sb.append("]");
		}
		if (this.casRenew)
			sb.append(" casRenew=true");
		if (this.casServerName != null) {
			sb.append(" casServerName=[");
			sb.append(this.casServerName);
			sb.append("]");
		}
		if (this.casServiceUrl != null) {
			sb.append(" casServiceUrl=[");
			sb.append(this.casServiceUrl);
			sb.append("]");
		}
		if (this.casValidate != null) {
			sb.append(" casValidate=[");
			sb.append(this.casValidate);
			sb.append("]");
		} else {
			sb.append(" casValidate=NULL!!!");
		}
		return sb.toString();
	}

	public void destroy() {
	}
}
*/