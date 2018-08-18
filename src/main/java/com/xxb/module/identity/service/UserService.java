package com.xxb.module.identity.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.xxb.base.ProjectException;
import com.xxb.module.base.service.BaseService;
import com.xxb.module.identity.entity.Group;
import com.xxb.module.identity.entity.Role;
import com.xxb.module.identity.entity.User;
import com.xxb.module.identity.repository.UserRepository;
import com.xxb.util.PojoConvertUtil;

@Service
@Transactional(rollbackFor=Throwable.class)
public class UserService extends BaseService<User> {
	
	@Autowired
	private UserRepository repo;
	
	@Transactional(readOnly = true)
	public Page<User> searchNativeUser(JSONObject searchParam) {
		StringBuffer sb = new StringBuffer();
		String select = "select u.id,u.type,u.description,u.name,u.mobile,u.password,u.create_date,u.group_id,u.modify_date ";
		JSONObject queryParam = new JSONObject();
		sb.append("from id_user u left join id_group g on u.group_id=g.id where 1=1");
		if (searchParam.containsKey("groupName")) {
			appendSql(sb, "g.name", "like", queryParam, "groupName", searchParam.getString("groupName"), "and");
		}
		if (searchParam.containsKey("startDate")) {
			appendSql(sb, "u.create_date", ">=", queryParam, "createDate", searchParam.getDate("startDate"), "and");
		}
		String whereSql = sb.toString();
		sb.append(getOrder(searchParam));
		
		Page<User> page = findPage(select+sb.toString(), whereSql, queryParam, User.class);
		
		return page;
	}
	
	@Transactional(readOnly = true)
	public Page<User> searchUser(JSONObject searchParam, Pageable pageable) {
		Specification<User> specification = getWhereClause(searchParam);
		Page<User> page = repo.findAll(specification, pageable);
		
		return page;
	}
	
	public User saveUser(User user) {
		User entity;
		if (StringUtils.isEmpty(user.getId())) {
			entity = user;
			if (StringUtils.isEmpty(entity.getPassword())) {
				entity.setCreateDate(new Date());
				entity.setPassword(User.defaultPassword);
			}else {
//				String pw = Md5Crypt.apr1Crypt(entity.getPassword());
				String pw = DigestUtils.md5Hex(entity.getPassword());
				entity.setPassword(pw);
			}
			entity.setCreateDate(new Date());
		}else {
			entity = repo.findById(user.getId()).get();
			entity = PojoConvertUtil.convertPojo(user, User.class, entity);
			if (StringUtils.isNotEmpty(user.getPassword())) {
				String pw = DigestUtils.md5Hex(user.getPassword());
				entity.setPassword(pw);
			}
		}
		if (user.getRoles()==null || user.getRoles().isEmpty()) {
			entity.setModifyDate(new Date());
		}
		user = repo.save(entity);
		return user;
	}
	
	/**
	 * 动态搜索
	 * @param searchParam
	 * @return
	 */
	@SuppressWarnings("serial")
	private Specification<User> getWhereClause(final JSONObject searchParam){
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (searchParam.containsKey("groupName")) {
                	Join<User,Group> groupJoin = root.join(root.getModel().getSingularAttribute("group",Group.class),JoinType.LEFT);
                    predicate.add(cb.like(groupJoin.get("name").as(String.class), searchParam.getString("groupName")));
                }
                if (searchParam.containsKey("startDate")) {
                    predicate.add(cb.greaterThanOrEqualTo(root.get("createDate").as(Date.class), searchParam.getDate("startDate")));
                }
                if (searchParam.containsKey("roleName")) {
                	Join<User,Role> roleJoin = root.join("roles",JoinType.LEFT);
                	predicate.add(cb.like(roleJoin.get("name").as(String.class), searchParam.getString("roleName")));
                }
                if (searchParam.containsKey("namesearch")) {
                	Predicate p1 = cb.like(root.get("name").as(String.class), "%"+searchParam.getString("namesearch")+"%");
                	Predicate p2 = cb.like(root.get("description").as(String.class), "%"+searchParam.getString("namesearch")+"%");
                	
                    predicate.add(cb.or(p1,p2));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

	public User login(JSONObject param) {
		String name = param.getString("name");
		User user = repo.queryTopEntityByNameEqualsOrMobileEquals(name, name);
		if (user==null) {
			throw new ProjectException("没有这个用户");
		}
		String md5 =  DigestUtils.md5Hex(param.getString("password"));
		if (!user.getPassword().equals(md5)) {
			throw new ProjectException("密码不正确");
		}
		return user;
	}
	
	public User findByOpenId(String openId) {
		User user = repo.queryTopEntityByOpenIdEquals(openId);
		return user;
	}
	
	public User findByMobile(String mobile) {
		User user = repo.queryTopEntityByMobileEquals(mobile);
		return user;
	}

	public void modifyPw(JSONObject param, User curUser) {
		User entity;
		String userId = param.getString("userId");
		String password = param.getString("password");
		if (curUser.getType().equals(User.USER_TYPE_SUPER)) {
			entity = repo.findById(userId).get();
		} else if (userId.equals(curUser.getId())) {
			entity = repo.findById(curUser.getId()).get();
		} else {
			throw new ProjectException("无权修改密码");
		}
		String md5 = DigestUtils.md5Hex(password);
		entity.setPassword(md5);
		entity.setModifyDate(new Date());
		repo.save(entity);
	}

}
