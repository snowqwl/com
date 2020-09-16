package com.sunshine.monitor.comm.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;

public class AuthenticationFilter extends AbstractCasFilter {
	/**
	 * The URL to the CAS Server login.
	 */
	private String casServerLoginUrl;

	/**
	 * Whether to send the renew request or not.
	 */
	private boolean renew = false;

	/**
	 * Whether to send the gateway request or not.
	 */
	private boolean gateway = false;

	/**
	 * 过滤地址集合
	 */
	private Set<String> exclusionSet = null;

	/**
	 * 过滤地址
	 */
	private String exclusions = null;

	private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected void initInternal(final FilterConfig filterConfig) throws ServletException {
		if (!isIgnoreInitConfiguration()) {
			super.initInternal(filterConfig);
			setCasServerLoginUrl(getPropertyFromInitParams(filterConfig, "casServerLoginUrl", null));
			logger.info("Loaded CasServerLoginUrl parameter: " + this.casServerLoginUrl);
			setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
			logger.info("Loaded renew parameter: " + this.renew);
			setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));
			logger.info("Loaded gateway parameter: " + this.gateway);

			// 获取需要过滤拦截地址
			setExclusions(getPropertyFromInitParams(filterConfig, "exclusions", null));
			if ((exclusions != null) && exclusions.trim().length() > 0) {
				String[] exclusionArray = exclusions.split(",");
				if (exclusionArray != null && exclusionArray.length > 0) {
					exclusionSet = new HashSet<String>();
					for (String exclusionUrl : exclusionArray) {
						exclusionSet.add(exclusionUrl);
					}
				}
			}

			final String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);

			if (gatewayStorageClass != null) {
				try {
					this.gatewayStorage = (GatewayResolver) Class.forName(gatewayStorageClass).newInstance();
				} catch (final Exception e) {
					logger.error(e.toString(), e);
					throw new ServletException(e);
				}
			}
		}
	}

	public void init() {
		super.init();
		CommonUtils.assertNotNull(this.casServerLoginUrl, "casServerLoginUrl cannot be null.");
	}

	public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		final HttpSession session = request.getSession(false);
		final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;

		if (isExclusion(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		String uri = request.getRequestURI();
		String url = uri + "?method=" + request.getParameter("method");
		UserSession userSession = null;
		userSession = (UserSession) WebUtils.getSessionAttribute((HttpServletRequest) request, "userSession");
		if (userSession != null && url.indexOf("comm.do?method=cdlogin") == -1) {
			filterChain.doFilter(request, response);
			return;
		} else if (userSession != null && url.indexOf("comm.do?method=cdlogin") > 0) {
			HttpSession se = request.getSession();
			se.removeAttribute("userSession");
		}

		if (assertion != null) {
			filterChain.doFilter(request, response);
			return;
		}

		final String serviceUrl = constructServiceUrl(request, response);
		final String ticket = CommonUtils.safeGetParameter(request, getArtifactParameterName());
		final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);

		if (CommonUtils.isNotBlank(ticket) || wasGatewayed) {
			filterChain.doFilter(request, response);
			return;
		}

		final String modifiedServiceUrl;

		logger.debug("no ticket and no assertion found");
		if (this.gateway) {
			logger.debug("setting gateway attribute in session");
			modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
		} else {
			modifiedServiceUrl = serviceUrl;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Constructed service url: " + modifiedServiceUrl);
		}

		final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl, getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);

		if (logger.isDebugEnabled()) {
			logger.debug("redirecting to \"" + urlToRedirectTo + "\"");
		}

		response.sendRedirect(urlToRedirectTo);
	}

	/**
	 * 判断请求地址是否拦截
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	private boolean isExclusion(HttpServletRequest request) throws IOException, ServletException {
		// String servletPath = request.getServletPath();
		String uri = request.getRequestURI();
		// 返回true不需要拦截,返回false需要拦截
		if (exclusionSet == null) {
			return false;
		}
		for (String turi : exclusionSet) {
			if (uri.indexOf(turi) != -1)
				return true;
		}
		return false;
	}

	public final void setRenew(final boolean renew) {
		this.renew = renew;
	}

	public final void setGateway(final boolean gateway) {
		this.gateway = gateway;
	}

	public final void setCasServerLoginUrl(final String casServerLoginUrl) {
		this.casServerLoginUrl = casServerLoginUrl;
	}

	public final void setGatewayStorage(final GatewayResolver gatewayStorage) {
		this.gatewayStorage = gatewayStorage;
	}

	public void setExclusions(String exclusions) {
		this.exclusions = exclusions;
	}
}
