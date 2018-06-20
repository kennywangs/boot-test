package com.xxb.module.identity.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxb.base.BaseController;
import com.xxb.module.identity.entity.Authority;
import com.xxb.module.identity.entity.Role;
import com.xxb.module.identity.service.AuthService;
import com.xxb.util.jackson.Djson;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(value="/role/save",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveRole(@RequestBody Role role) {
		role = authService.saveRole(role);
		return handelResult("成功", role);
	}
	
	@RequestMapping(value="/auth/save",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveAuth(@RequestBody Authority auth) {
		auth = authService.saveAuth(auth);
		return handelResult("成功", auth);
	}
	
	@RequestMapping(value="/auth/listbyuser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listAuthByUser(String userId) {
		Collection<Authority> auths = authService.getAuthsByUser(userId);
		return handelResult("成功", auths);
	}
	
	@RequestMapping(value="/auth/listbyrole",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listAuthByRole(String roleId) {
		Collection<Authority> auths = authService.getAuthsByRole(roleId);
		return handelResult("成功", auths);
	}
	
	@RequestMapping(value="/role/listbyuser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listRoleByUser(String userId) {
		Collection<Role> roles = authService.getRolesByUser(userId);
		Djson djson = new Djson(Role.class,null,"auths");
		return handelJsonResult("成功", roles, djson);
	}

}
