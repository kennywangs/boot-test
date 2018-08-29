package com.xxb.module.identity.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.xxb.base.BaseController;
import com.xxb.module.identity.entity.Authority;
import com.xxb.module.identity.entity.Role;
import com.xxb.module.identity.entity.User;
import com.xxb.module.identity.service.AuthService;
import com.xxb.util.jackson.Djson;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(value="/role/save.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveRole(@RequestBody Role role) {
		role = authService.saveRole(role);
		return handleResult("成功", role);
	}
	
	@RequestMapping(value="/auth/save.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveAuth(@RequestBody Authority auth) {
		auth = authService.saveAuth(auth);
		return handleResult("成功", auth);
	}
	
	@RequestMapping(value="/role/view.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String viewRole(@RequestParam String id) {
		Role role = authService.getRoleById(id);
		Djson djson = new Djson(Role.class,null,"auths");
		return handleJsonResult("成功", role, djson);
	}
	
	@RequestMapping(value="/auth/view.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String viewAuth(@RequestParam String id) {
		Authority auth = authService.getAuthById(id);
		return handleResult("成功", auth);
	}
	
	@RequestMapping(value="/auth/listbyuser.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listAuthByUser(String userId) {
		Collection<Authority> auths = authService.getAuthsByUser(userId);
		return handleResult("成功", auths);
	}
	
	@RequestMapping(value="/auth/listbyrole.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listAuthByRole(String roleId) {
		Collection<Authority> auths = authService.getAuthsByRole(roleId);
		return handleResult("成功", auths);
	}
	
	@RequestMapping(value="/role/listbyuser.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listRoleByUser(String userId) {
		Collection<Role> roles = authService.getRolesByUser(userId);
		Djson djson = new Djson(Role.class,null,"auths");
		return handleJsonResult("成功", roles, djson);
	}
	
	@RequestMapping(value="/auth/list.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listAuths(@RequestBody JSONObject param,@PageableDefault(value = 10, sort = { "createDate" }, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Authority> auths = authService.getAuthsPage(param,pageable);
		return handlePageResult("成功", auths);
	}
	
	@RequestMapping(value="/role/list.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listRoles(@RequestBody JSONObject param,@PageableDefault(value = 10, sort = { "createDate" }, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Role> roles = authService.getRolesPage(param,pageable);
		Djson djson = new Djson(Role.class,null,"auths");
		return handleJsonPageResult("成功", roles, djson);
	}
	
	@RequestMapping(value="/auth/refreshcache.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String refreshCache(HttpServletRequest request) {
		if (getCurrentUser(request).getType()==User.USER_TYPE_SUPER) {
			authService.refreshCacheAuth();
			return handleResult("刷新成功");
		}
		return handleError("无权操作");
	}

}
