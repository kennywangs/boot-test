package com.xxb.test.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.xxb.test.entity.TestMgGroup;

public interface TestMgGroupRepository extends MongoRepository<TestMgGroup, ObjectId>{ 
	
}
