package com.xxb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Controller;

import com.xxb.module.identity.service.AuthService;

@Controller
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private AuthService authService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent e) {
		System.out.println("----------------------startup------------------");
		authService.refreshCacheAuth();
	}

	
}
