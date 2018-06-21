package com.xxb.config;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.xxb.base.BaseController;
import com.xxb.base.ProjectException;
import com.xxb.module.identity.entity.Authority;
import com.xxb.module.identity.entity.User;
import com.xxb.util.Constant;
import com.xxb.util.JsonUtils;
import com.xxb.util.TokenUtils;

import io.jsonwebtoken.Claims;

public class HTTPCookieJwtAuthorizeFilter extends BaseController implements Filter {
	
	private PathMatcher pathMatcher = new AntPathMatcher();
	
	private String[] excludedArray;
	
	private String[] pageArray;
	
	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}
	
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
			if (accessAuthToken(request, url, reqToken)) {
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
				httpResponse.sendRedirect("/index.html");
				return;
			}
		}
		
		httpResponse.setContentType("application/json; charset=utf-8");
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		ProjectException e = new ProjectException("请您先登录！", ProjectException.NeedLogin);
		httpResponse.getWriter().write(handleError("权限被拒绝！", e));
		return;
	}

	private boolean accessAuthToken(ServletRequest request, String url, String reqToken) throws IOException, ServletException {
		Claims claims = TokenUtils.parseJWT(reqToken, env.getProperty("jwt.security"));
		String tokenKey = getTokenkey((String) claims.get("userid"), reqToken);
		String uJson = stringRedisTemplate.opsForValue().get(tokenKey);
		if (claims!=null && !StringUtils.isEmpty(uJson)) {
			if (url.equals("/user/logout.do")) {
				return true;
			}
			if (checkAuths(uJson,url)) {
				request.setAttribute("userId", claims.get("userid"));
				request.setAttribute("userName", claims.get("unique_name"));
				return true;
			}
		}
		return false;
	}

	private boolean checkAuths(String uJson, String url) {
		User user = JsonUtils.parseJson(uJson, User.class);
		if (user.getType()==User.USER_TYPE_SUPER) {
			return true;
		}
		HashOperations<String, String, String> hops = stringRedisTemplate.opsForHash();
		if (user.getAuths()==null) {
			return false;
		}
		List<String> authJlist = hops.multiGet(Constant.cacheAuthsKey, user.getAuths());
		for (String authJson : authJlist) {
			Authority auth = JsonUtils.parseJson(authJson, Authority.class);
			boolean matched = pathMatcher.match(auth.getAuthUrl(), url);
			if (matched) {
				return true;
			}
		}
		return false;
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
