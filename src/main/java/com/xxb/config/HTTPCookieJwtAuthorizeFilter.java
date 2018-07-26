package com.xxb.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.xxb.base.BaseController;
import com.xxb.base.ProjectException;
import com.xxb.base.SecurityService;

public class HTTPCookieJwtAuthorizeFilter extends BaseController implements Filter {
	
	private PathMatcher pathMatcher = new AntPathMatcher();
	
	private String[] excludedArray;
	
	private String[] pageArray;
	
	@Autowired
	private SecurityService securityService;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String url = httpRequest.getServletPath();
		
		//判断是否在过滤url之外
		for (String excludedUrl : excludedArray)
		{
			boolean matched = pathMatcher.match(excludedUrl, url);
			if (matched){
				chain.doFilter(request, response);
				return;
			}
		}
		
		// token鉴权
		String reqToken = getReqToken(httpRequest);
		if (StringUtils.isNotEmpty(reqToken)) {
			if (securityService.accessAuthToken(url, reqToken)) {
				chain.doFilter(request, response);
				return;
			}
		}
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setCharacterEncoding("UTF-8");
		
		for (String pageUrl : pageArray)
		{
			boolean matched = pathMatcher.match(pageUrl, url);
			if (matched){
				httpResponse.sendRedirect("/login.html");
				return;
			}
		}
		
		httpResponse.setContentType("application/json; charset=utf-8");
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		ProjectException e = new ProjectException("请您先登录！", ProjectException.NeedLogin);
		httpResponse.getWriter().write(handleError("权限被拒绝！", e));
		return;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
		String excluded = env.getProperty("jwt.cookie.auth.excluded");
		if (!StringUtils.isEmpty(excluded)) {
			excludedArray = excluded.split(",");
		}
		String pages = env.getProperty("jwt.cookie.auth.page");
		if (!StringUtils.isEmpty(pages)) {
			pageArray = pages.split(",");
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
