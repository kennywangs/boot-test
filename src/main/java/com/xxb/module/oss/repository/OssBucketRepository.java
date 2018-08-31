package com.xxb.module.oss.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.xxb.module.oss.entity.OssBucket;

public interface OssBucketRepository extends MongoRepository<OssBucket, ObjectId> {

}
