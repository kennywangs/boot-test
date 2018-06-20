package com.xxb.action;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxb.base.BaseController;
import com.xxb.util.web.WeixinUtils;

@RestController
@RequestMapping("/weixin")
public class WeixinAction extends BaseController {
	
	private static final String Jscode2session = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code&appid=%s&secret=%s&js_code=%s";

	@RequestMapping(value="/getUserData",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getUserData(String code) {
		String url = String.format(Jscode2session, "wxc87e524c87d97459", "1b75ec7546450430146889c7a324c450", code);
		String res = WeixinUtils.httpsRequestToString(url, "GET", null);
		return handelResult("获取成功", res);
	}
}
