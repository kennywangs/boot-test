package com.xxb.module.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xxb.module.identity.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String>,JpaSpecificationExecutor<Authority> {

}
