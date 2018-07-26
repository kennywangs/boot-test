package com.xxb.action;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.xxb.base.BaseController;
import com.xxb.module.identity.entity.Group;
import com.xxb.module.identity.entity.User;
import com.xxb.module.identity.repository.UserRepository;
import com.xxb.module.identity.service.AuthService;
import com.xxb.util.Constant;
import com.xxb.util.TokenUtils;
import com.xxb.util.jackson.Djson;
import com.xxb.util.jackson.JacksonJsonUtil;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/system")
public class SystemAction extends BaseController {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(value="/register_w",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String registerUser(@RequestBody User user) {
		if (StringUtils.isNoneEmpty(user.getId())) {
			return handleError("注册失败,用户id不为空");
		}
		if (repo.queryTopEntityByNameEqualsOrMobileEquals(user.getName(), user.getMobile())!=null) {
			return handleError("注册失败,用户已经存在");
		}
		try {
			user = userService.saveUser(user);
			return handleResult("注册成功", user);
		} catch (Exception e) {
			return handleError("注册失败",e);
		}
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String userLogin(@RequestBody JSONObject param,HttpServletRequest request,HttpServletResponse response) {
		try {
			User user = userService.login(param);
			String reqToken = getReqToken(request);
			String tokenKey = Constant.getTokenkey(user.getId(), reqToken);
			if (!StringUtils.isEmpty(reqToken) && stringRedisTemplate.hasKey(tokenKey)) {
				user.setToken(reqToken);
				return handleResult("登录成功！", user);
			}
//			if (reqToken!=null) {
//				stringRedisTemplate.delete(reqToken);
//			}
			String token = TokenUtils.createJWT(user.getName(), user.getId(), user.getType().toString(),
					env.getProperty("jwt.client"), env.getProperty("jwt.name"),
					Integer.valueOf(env.getProperty("jwt.expiresSecond")) * 1000, env.getProperty("jwt.security"));
			Cookie cookie = new Cookie("access-token", token);
			cookie.setMaxAge(2 * 24 * 60 * 60);
			cookie.setPath("/");
			cookie.setHttpOnly(true);
			response.addCookie(cookie);
			tokenKey = Constant.getTokenkey(user.getId(), token);
			
			stringRedisTemplate.opsForValue().set(tokenKey,getUserInfoJson(user),1,TimeUnit.DAYS);
			user.setToken(token);
			return handleResult("登陆成功",user);
		} catch (Exception e) {
			return handleError("登陆失败",e);
		}
	}
	
	@RequestMapping(value = "/gettoken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getToken(@RequestBody JSONObject param, HttpServletRequest request, HttpServletResponse response) {
		try {
			User user = userService.login(param);
			String reqToken = getCookieToken(request);
			String tokenKey = Constant.getTokenkey(user.getId(), reqToken);
			if (!StringUtils.isEmpty(reqToken) && stringRedisTemplate.hasKey(tokenKey)) {
				return checkToken(reqToken,param.getString("name"));
			}
			return handleError("获取失败！");
//			String auth = request.getHeader("Authorization");
//			String headToken = auth.replace("bearer ", "");
//			if (!StringUtils.isEmpty(headToken) && !StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(headToken))) {
//				return checkToken(headToken,identity);
//			}
//			String token = TokenUtils.createJWT(user.getName(), user.getUuid(), user.getType().toString(),
//					env.getProperty("jwt.client"), env.getProperty("jwt.name"),
//					Integer.valueOf(env.getProperty("jwt.expiresSecond")) * 1000, env.getProperty("jwt.security"));
//			return handleResult("获取成功！", token);
		} catch (Exception e) {
			return handleError("获取失败！", e);
		}
	}
	
	@RequestMapping(value = "/getuser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getUser(HttpServletRequest request) {
		try {
			User user = getCurrentUser(request);
			user.setPassword(null);
			user.setCreateDate(null);
			user.setModifyDate(null);
			return handleResult("获取成功！", user);
		} catch (Exception e) {
			return handleError("获取失败！", e);
		}
	}
	
	private String checkToken(String token, String identity) {
		Claims claims = TokenUtils.parseJWT(token, env.getProperty("jwt.security"));
		if (identity.equals(claims.get("unique_name"))) {
			return handleResult("获取成功！", token);
		}
		return handleError("获取失败！用户授权验签失败！");
	}
	
	private String getUserInfoJson(User user) throws JsonProcessingException {
		user.setAuths(authService.getAllAuthId(user.getRoles()));
		user.setTtl(System.currentTimeMillis()+24*60*60*1000);
		Djson ujson = new Djson(User.class,null,"roles");
		Djson gjson = new Djson(Group.class,null,null);
		List<Djson> djsons = Lists.newArrayList();
		djsons.add(gjson);
		djsons.add(ujson);
		return JacksonJsonUtil.toJson(user, djsons);
	}
	
}
