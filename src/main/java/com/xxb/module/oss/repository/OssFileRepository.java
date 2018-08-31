package com.xxb.module.oss.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.xxb.module.oss.entity.OssFile;

public interface OssFileRepository extends MongoRepository<OssFile, ObjectId> {

}
