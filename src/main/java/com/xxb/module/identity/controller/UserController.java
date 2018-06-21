package com.xxb.module.identity.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.xxb.base.BaseController;
import com.xxb.module.identity.entity.Group;
import com.xxb.module.identity.entity.User;
import com.xxb.module.identity.repository.UserRepository;
import com.xxb.util.jackson.Djson;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
	
	@Autowired
	private UserRepository repo;
	
	@RequestMapping(value="/search_native.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String searchNativeUser(@RequestBody JSONObject param) {
		Page<User> users = userService.searchNativeUser(param);
		return handlePageResult("成功", users);
	}
	
	@RequestMapping(value="/search.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String searchUser(@RequestBody JSONObject param,@PageableDefault(value = 10, sort = { "name" }, direction = Sort.Direction.ASC) Pageable pageable) {
		Page<User> users = userService.searchUser(param, pageable);
		Djson djson = new Djson(User.class,null,"password,roles,group");
		return handleJsonPageResult("成功", users, djson);
	}
	
	@RequestMapping(value="/save.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveUser(@RequestBody User user) {
		user = userService.saveUser(user);
		Djson ujson = new Djson(User.class,null,"password,roles,group");
		return handleJsonResult("成功", user, ujson);
	}
	
	@RequestMapping(value="/view.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String viewUser(String id) {
		User user = repo.findById(id).get();
		Djson ujson = new Djson(User.class,null,"password,roles");
		Djson gjson = new Djson(Group.class,null,null);
		return handleJsonResult("成功", user, ujson, gjson);
	}
	
	@RequestMapping(value="/list.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listUser(@PageableDefault(value = 10, sort = { "name" }, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<User> users = repo.findAll(pageable);
		Djson djson = new Djson(User.class,null,"password,roles,group");
		return handleJsonPageResult("成功", users, djson);
	}
	
	@RequestMapping(value="/modifypw.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String modifyPw(@RequestBody JSONObject param, HttpServletRequest request) {
		try {
			userService.modifyPw(param, getCurrentUser(request));
			return handleResult("修改成功");
		} catch (Exception e) {
			return handleError("修改失败!",e);
		}
	}
	
	@RequestMapping(value="/logout.do",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		try {
			String reqToken = getReqToken(request);
			Claims claims = getClaims(reqToken);
			String tokenKey = getTokenkey((String) claims.get("userid"), getReqToken(request));
			stringRedisTemplate.delete(tokenKey);
			Cookie cookie = getCookie(request, "access-token");
			cookie.setValue(null);
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			return handleResult("登出成功");
		} catch (Exception e) {
			return handleError("登出失败!",e);
		}
	}

}
