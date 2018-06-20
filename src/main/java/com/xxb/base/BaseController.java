package com.xxb.base;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xxb.module.identity.service.UserService;
import com.xxb.util.JsonUtils;
import com.xxb.util.TokenUtils;
import com.xxb.util.jackson.Djson;
import com.xxb.util.jackson.JacksonJsonUtil;

import io.jsonwebtoken.Claims;

public abstract class BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	protected Environment env;

	public <T> String handlePageResult(String msg, Page<T> page) {
		ServiceResult result = new ServiceResult();
		result.setTotalNumber(page.getTotalElements());
		result.setTotalPage(page.getTotalPages());
		result.setMsg(msg);
		result.setData(page.getContent());
		return JsonUtils.toJson(result);
	}

	public String handleResult(String msg, Object props) {
		ServiceResult result = new ServiceResult();
		result.setMsg(msg);
		result.setData(props);
		return JsonUtils.toJson(result);
	}

	public String handleResult(String msg) {
		ServiceResult result = new ServiceResult();
		result.setMsg(msg);
		return JsonUtils.toJson(result);
	}

	public String handleError(String msg, Throwable e, Object props) {
		ServiceResult result = new ServiceResult();
		setExResult(msg, e, result);
		result.setData(props);
		return JsonUtils.toJson(result);
	}

	public String handleError(String msg, Throwable e) {
		ServiceResult result = new ServiceResult();
		setExResult(msg, e, result);
		return JsonUtils.toJson(result);
	}
	
	public String handleError(String msg) {
		ServiceResult result = new ServiceResult();
		setExResult(msg, null, result);
		return JsonUtils.toJson(result);
	}

	private void setExResult(String msg, Throwable e, ServiceResult result) {
		result.setSuccess(false);
		if (e instanceof ProjectException) {
			ProjectException pe = (ProjectException) e;
			result.setMsg(msg + e.getMessage());
			result.setErrorCode(pe.getErrorCode());
			logger.debug(e.getMessage(), e);
		} else if (e!=null) {
			result.setMsg(msg);
			result.setErrorCode("UNKNOWN");
			logger.error(e.getMessage(), e);
		} else {
			result.setMsg(msg);
			result.setErrorCode("UNKNOWN");
			logger.error(msg);
		}
	}
	
	public <T> String handleJsonPageResult(String msg, Page<T> page,Djson... djson) {
		ServiceResult result = new ServiceResult();
		result.setTotalNumber(page.getTotalElements());
		result.setTotalPage(page.getTotalPages());
		result.setMsg(msg);
		result.setData(page.getContent());
		try {
			return JacksonJsonUtil.toJson(result, Arrays.asList(djson));
		} catch (JsonProcessingException e) {
			return handleError("获取失败!",e);
		}
	}
	
	public String handleJsonResult(String msg, Object props,Djson... djson) {
		ServiceResult result = new ServiceResult();
		result.setMsg(msg);
		result.setData(props);
		try {
			return JacksonJsonUtil.toJson(result, Arrays.asList(djson));
		} catch (JsonProcessingException e) {
			return handleError("获取失败!",e);
		}
	}
	
	protected String getCookieToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (null == cookies) {
			return null;
		} else {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("access-token")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	protected Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (null == cookies) {
			return null;
		} else {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}
	
	protected Claims getClaims(HttpServletRequest request) {
		Claims claims = null;
		Cookie[] cookies = request.getCookies();
		if (null == cookies) {
			throw new ProjectException("您还没有登录！", ProjectException.NeedLogin);
		} else {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("access-token")) {
					String token = cookie.getValue();
					String ts = stringRedisTemplate.opsForValue().get(token);
					if (ts==null) {
						throw new ProjectException("您需要重新登录！", ProjectException.NeedLogin);
					}
					claims = TokenUtils.parseJWT(token, env.getProperty("jwt.security"));
					if (claims==null) {
						throw new ProjectException("您需要重新登录！", ProjectException.NeedLogin);
					}
					System.out.println("claims:" + claims.toString());
				}
			}
		}
		return claims;
	}
	
	protected String getTokenkey(String userId, String token){
		return "Token:"+userId+":"+token;
	}
}
