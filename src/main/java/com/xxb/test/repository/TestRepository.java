package com.xxb.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xxb.test.entity.Test;

public interface TestRepository extends JpaRepository<Test,String> {

}
