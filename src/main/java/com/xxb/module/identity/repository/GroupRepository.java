package com.xxb.module.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xxb.module.identity.entity.Group;

public interface GroupRepository extends JpaRepository<Group, String> {

}
