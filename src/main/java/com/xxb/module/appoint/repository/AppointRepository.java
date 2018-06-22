package com.xxb.module.appoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xxb.module.appoint.entity.Appoint;

public interface AppointRepository extends JpaRepository<Appoint, String>,JpaSpecificationExecutor<Appoint> {

}
