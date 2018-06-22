package com.xxb.module.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.xxb.module.identity.entity.User;

public interface UserRepository extends JpaRepository<User, String>,JpaSpecificationExecutor<User> {
	
	@Query(value="select u.id,u.name,u.mobile,u.password,u.create_date,u.group_id,u.modify_date from id_user u left join id_group g on u.group_id=g.id where g.name like %?1%",nativeQuery=true)
	public List<User> searchByGroupName(String groupName);
	
	public User queryTopEntityByMobileEquals(String mobile);
	
	public User queryTopEntityByNameEquals(String name);
	
	public User queryTopEntityByNameEqualsOrMobileEquals(String name,String mobile);

	public User queryTopEntityByOpenIdEquals(String openId);

}
