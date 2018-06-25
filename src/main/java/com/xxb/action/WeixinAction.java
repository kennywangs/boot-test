package com.xxb.action;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.xxb.base.BaseController;
import com.xxb.module.identity.entity.Group;
import com.xxb.module.identity.entity.User;
import com.xxb.module.identity.service.AuthService;
import com.xxb.util.Constant;
import com.xxb.util.TokenUtils;
import com.xxb.util.jackson.Djson;
import com.xxb.util.jackson.JacksonJsonUtil;
import com.xxb.util.web.WeixinUtils;

@RestController
@RequestMapping("/weixin")
public class WeixinAction extends BaseController {
	
	private static final String Jscode2session = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code&appid=%s&secret=%s&js_code=%s";
	
	@Autowired
	private AuthService authService;
	
	@Value("${wx.appid}")
	private String appId;
	@Value("${wx.secret}")
	private String secret;
	
	@RequestMapping(value="/getUserData.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getUserData(String code) {
		String url = String.format(Jscode2session, appId, secret, code);
		String res = WeixinUtils.httpsRequestToString(url, "GET", null);
		return handleResult("获取成功", res);
	}
	
	@RequestMapping(value="/login",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String login(String code, String mobile, String token) {
		try {
			String url = String.format(Jscode2session, appId, secret, code);
			String res = WeixinUtils.httpsRequestToString(url, "GET", null);
			JSONObject object = JSONObject.parseObject(res);
			User user = userService.findByOpenId(object.getString("openid"));
			if (user==null) {
				if (StringUtils.isNotEmpty(mobile)) {
					user = userService.findByMobile(mobile);
				}
				if (user==null) {
					user = new User();
				}
				user.setOpenId(object.getString("openid"));
				user.setMobile(mobile==null?"":mobile);
				user.setName(mobile==null?object.getString("openid"):mobile);
				user.setType(User.USER_TYPE_CUSTOM);
				userService.saveUser(user);
			}
			
			String tokenKey = Constant.getTokenkey(user.getId(), token);
			if (!StringUtils.isEmpty(token) && stringRedisTemplate.hasKey(tokenKey)) {
				user.setToken(token);
				return handleResult("登录成功！", user);
			}
			token = TokenUtils.createJWT(user.getName(), user.getId(), user.getType().toString(),
					env.getProperty("jwt.client"), env.getProperty("jwt.name"),
					Integer.valueOf(env.getProperty("jwt.expiresSecond")) * 1000, env.getProperty("jwt.security"));
			tokenKey = Constant.getTokenkey(user.getId(), token);
			stringRedisTemplate.opsForValue().set(tokenKey,getUserInfoJson(user),1,TimeUnit.DAYS);
			user.setToken(token);
			
			return handleResult("登陆成功",user);
		} catch (Exception e) {
			return handleError("登陆失败",e);
		}
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
