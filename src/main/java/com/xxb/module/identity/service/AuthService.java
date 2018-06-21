package com.xxb.module.identity.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.xxb.module.base.service.BaseService;
import com.xxb.module.identity.entity.Authority;
import com.xxb.module.identity.entity.Role;
import com.xxb.module.identity.repository.AuthorityRepository;
import com.xxb.module.identity.repository.RoleRepository;
import com.xxb.module.identity.repository.UserRepository;
import com.xxb.util.Constant;
import com.xxb.util.JsonUtils;
import com.xxb.util.PojoConvertUtil;

@Service
@Transactional(rollbackFor=Throwable.class)
public class AuthService extends BaseService<Authority> {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private AuthorityRepository authRepo;

	public Role saveRole(Role role) {
		Role entity;
		if (StringUtils.isEmpty(role.getId())) {
			entity = role;
			entity.setCreateDate(new Date());
		} else {
			entity = roleRepo.findById(role.getId()).get();
			entity = PojoConvertUtil.convertPojo(role, Role.class, entity);
		}
		role.setModifyDate(new Date());
		roleRepo.save(entity);
		return entity;
	}
	
	public Authority saveAuth(Authority auth) {
		Authority entity;
		if (StringUtils.isEmpty(auth.getId())) {
			entity = auth;
			entity.setCreateDate(new Date());
		} else {
			entity = authRepo.findById(auth.getId()).get();
			entity = PojoConvertUtil.convertPojo(auth, Authority.class, entity);
		}
		auth.setModifyDate(new Date());
		authRepo.save(entity);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public Set<Role> getRolesByUser(String userId){
		return userRepo.findById(userId).get().getRoles();
	}
	
	@Transactional(readOnly = true)
	public Collection<Authority> getAuthsByUser(String userId){
		Map<String,Authority> auths = Maps.newHashMap();
		Set<Role> roles = userRepo.findById(userId).get().getRoles();
		for (Role role:roles) {
			for (Authority auth:role.getAuths()) {
				if (auths.containsKey(auth.getId())) {
					continue;
				}
				auths.put(auth.getId(), auth);
			}
			
		}
		return auths.values();
	}
	
	@Transactional(readOnly = true)
	public Set<Authority> getAuthsByRole(String roleId){
		return roleRepo.findById(roleId).get().getAuths();
	}
	
	@Transactional(readOnly = true)
	public Set<String> getAllAuthId(Set<Role> roles) {
		Map<String,Authority> auths = Maps.newHashMap();
		for (Role role:roles) {
			for (Authority auth:role.getAuths()) {
				if (auths.containsKey(auth.getId())) {
					continue;
				}
				auths.put(auth.getId(), auth);
			}
		}
		return auths.keySet();
	}
	
	@Transactional(readOnly = true)
	public void refreshCacheAuth() {
		List<Authority> auths = authRepo.findAll();
		HashOperations<String, String, String> hops = stringRedisTemplate.opsForHash();
		for (Authority auth : auths){
			String authJson = JsonUtils.dateGson.toJson(auth);
			hops.put(Constant.cacheAuthsKey, auth.getId(), authJson);
		}
	}
}
