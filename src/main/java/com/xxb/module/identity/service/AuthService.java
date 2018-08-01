package com.xxb.module.identity.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
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
		if (roles==null) {
			return null;
		}
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

	public Page<Authority> getAuthsPage(JSONObject searchParam, Pageable pageable) {
		Specification<Authority> specification = getAuthWhereClause(searchParam);
		Page<Authority> page = authRepo.findAll(specification, pageable);
		return page;
	}

	public Page<Role> getRolesPage(JSONObject searchParam, Pageable pageable) {
		Specification<Role> specification = getRoleWhereClause(searchParam);
		Page<Role> page = roleRepo.findAll(specification, pageable);
		return page;
	}

	@SuppressWarnings("serial")
	private Specification<Authority> getAuthWhereClause(JSONObject searchParam) {
		return new Specification<Authority>() {
			@Override
            public Predicate toPredicate(Root<Authority> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();
				if (searchParam.containsKey("name")) {
                    predicate.add(cb.like(root.get("name").as(String.class), searchParam.getString("name")));
                }
				if (searchParam.containsKey("authUrl")) {
                    predicate.add(cb.like(root.get("authUrl").as(String.class), searchParam.getString("authUrl")));
                }
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
	}

	@SuppressWarnings("serial")
	private Specification<Role> getRoleWhereClause(JSONObject searchParam) {
		return new Specification<Role>() {
			@Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();
				if (searchParam.containsKey("name")) {
                    predicate.add(cb.like(root.get("name").as(String.class), searchParam.getString("name")));
                }
				if (searchParam.containsKey("description")) {
                    predicate.add(cb.like(root.get("description").as(String.class), searchParam.getString("description")));
                }
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
	}
}
