package com.xxb.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xxb.test.entity.Test;
import com.xxb.test.repository.TestRepository;

@Service
@Transactional(rollbackFor=Throwable.class)
public class TestService {
	
	@Autowired
	private TestRepository repo;
	
	public Test save(Test test) {
		Test entity = repo.findById(test.getFid()).get();
		entity.setFname(test.getFname());
//		entity = repo.save(entity);
//		entity.setFname("final");
		return entity;
//		return repo.saveAndFlush(entity);
	}
	
	public int updateVersion(String id) {
		return repo.updateVersion(id);
	}

}
