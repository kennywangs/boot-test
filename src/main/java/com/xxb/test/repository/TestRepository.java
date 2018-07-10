package com.xxb.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.xxb.test.entity.Test;

public interface TestRepository extends JpaRepository<Test,String> {
	
	@Modifying(clearAutomatically=true)
	@Query(value="update xxb_test set version=0 where fid=?1",nativeQuery=true)
	public int updateVersion(@Param(value = "id") String id);

}
