package com.xxb.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AuthorizeConfig {
	@Autowired
	protected Environment env;
	
	@Bean
	public FilterRegistrationBean jwtFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		HTTPCookieJwtAuthorizeFilter httpCookieFilter = new HTTPCookieJwtAuthorizeFilter();
		registrationBean.setFilter(httpCookieFilter);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("*.do");
//		urlPatterns.add("*.jhtml");
		registrationBean.setUrlPatterns(urlPatterns);
//		registrationBean.setInitParameters(params);
		return registrationBean;
	}
}
