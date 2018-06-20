package com.xxb.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;

import com.xxb.module.identity.entity.Authority;
import com.xxb.module.identity.repository.AuthorityRepository;
import com.xxb.util.Constant;
import com.xxb.util.JsonUtils;

@Controller
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private AuthorityRepository authRepo;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent e) {
		System.out.println("----------------------startup------------------");
		List<Authority> auths = authRepo.findAll();
		HashOperations<String, String, String> hops = stringRedisTemplate.opsForHash();
		for (Authority auth : auths){
			String authJson = JsonUtils.dateGson.toJson(auth);
			hops.put(Constant.cacheAuthsKey, auth.getId(), authJson);
		}
	}

	
}
