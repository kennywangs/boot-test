package com.xxb.base;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.xxb.module.identity.entity.Authority;
import com.xxb.module.identity.entity.User;
import com.xxb.util.Constant;
import com.xxb.util.JsonUtils;
import com.xxb.util.TokenUtils;

import io.jsonwebtoken.Claims;

@Service
//@Transactional(rollbackFor = Throwable.class)
public class SecurityService {
	
	@Autowired
	protected StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	protected Environment env;
	
	private PathMatcher pathMatcher = new AntPathMatcher();
	
	public boolean accessAuthToken(String url, String reqToken) throws IOException, ServletException {
		Claims claims = TokenUtils.parseJWT(reqToken, env.getProperty("jwt.security"));
		String tokenKey = Constant.getTokenkey((String) claims.get("userid"), reqToken);
		String uJson = stringRedisTemplate.opsForValue().get(tokenKey);
		if (claims!=null && StringUtils.isNotEmpty(uJson)) {
			if (url.equals("/user/logout.do")) {
				return true;
			}
			if (checkAuths(uJson,url)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkAuths(String uJson, String url) {
		User user = JsonUtils.parseJson(uJson, User.class);
		if (user.getType()==User.USER_TYPE_SUPER) {
			return true;
		}
		HashOperations<String, String, String> hops = stringRedisTemplate.opsForHash();
		if (user.getAuths()==null) {
			return false;
		}
		List<String> authJlist = hops.multiGet(Constant.cacheAuthsKey, user.getAuths());
		for (String authJson : authJlist) {
			Authority auth = JsonUtils.parseJson(authJson, Authority.class);
			boolean matched = pathMatcher.match(auth.getAuthUrl(), url);
			if (matched) {
				return true;
			}
		}
		return false;
	}

}
