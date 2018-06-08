package com.xxb.module.identity.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.xxb.module.base.service.BaseService;
import com.xxb.module.identity.entity.User;
import com.xxb.module.identity.repository.UserRepository;
import com.xxb.util.PojoConvertUtil;

@Service
@Transactional
public class UserService extends BaseService<User> {
	
	@Autowired
	private UserRepository repo;
	
	@Transactional(readOnly = true)
	public Page<User> searchUser(JSONObject searchParam) {
		StringBuffer sb = new StringBuffer();
		String select = "select u.id,u.name,u.mobile,u.password,u.create_date,u.group_id,u.modify_date ";
		JSONObject queryParam = new JSONObject();
		sb.append("from id_user u left join id_group g on u.group_id=g.id where 1=1");
		if (searchParam.containsKey("groupName")) {
			appendSql(sb, "g.name", "like", queryParam, "groupName", searchParam.getString("groupName"), "and");
		}
		if (searchParam.containsKey("createDate")) {
			appendSql(sb, "u.create_date", ">=", queryParam, "createDate", searchParam.getDate("createDate"), "and");
		}
		String whereSql = sb.toString();
		sb.append(getOrder(searchParam));
		
		Page<User> page = findPage(select+sb.toString(), whereSql, queryParam, User.class);
		
		return page;
	}
	
	public User saveUser(User user) {
		User entity;
		if (StringUtils.isEmpty(user.getId())) {
			entity = user;
			if (StringUtils.isEmpty(entity.getPassword())) {
				entity.setCreateDate(new Date());
				entity.setPassword(User.defaultPassword);
			}
		}else {
			entity = repo.findById(user.getId()).get();
			entity = PojoConvertUtil.convertPojo(user, User.class, entity);
		}
		return repo.save(entity);
	}
	
}
