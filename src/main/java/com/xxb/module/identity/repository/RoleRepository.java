package com.xxb.module.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xxb.module.identity.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String>,JpaSpecificationExecutor<Role> {

}
