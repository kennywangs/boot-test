package com.xxb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongodbTest {

	private static final Logger log = LoggerFactory.getLogger(MongodbTest.class);

	public static void main(String[] args) {
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		MongoCredential credential = MongoCredential.createScramSha1Credential("root", "admin",
				"rootken".toCharArray());
		ServerAddress serverAddress = new ServerAddress("39.108.127.68", 27017);
		MongoClient mongo = new MongoClient(serverAddress, credential, builder.build());
		for (String name : mongo.listDatabaseNames()) {
			log.info("db name:{}", name);
		}
		
		createUser(mongo);
//		insertData(mongo);
//		deleteUser(mongo);
//		deleteDb(mongo);
		mongo.close();
	}

	private static void insertData(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("test");
		for (String name : db.listCollectionNames()) {
			log.info("collection name:{}", name);
		}
		MongoCollection<Document> collection = db.getCollection("ctest");
		Document document = new Document("_id", new ObjectId());
		document.put("name", "Cheung");
		collection.insertOne(document);
		log.info("insert data is {}", document.toJson());
	}

	private static void createUser(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("test");
		Document result;
		BasicDBObject getUsersInfoCommand = new BasicDBObject("usersInfo",
				new BasicDBObject("user", "ken").append("db", "ken"));
		BasicDBObject createUserCommand = new BasicDBObject("createUser", "ken").append("pwd", "ken").append("roles",
				Lists.newArrayList(new BasicDBObject("role", "dbOwner").append("db", "ken")));
		result = db.runCommand(getUsersInfoCommand);
		log.info("userInfo result:{}", result.toJson());
		List users = (List) result.get("users");
		if (users.isEmpty()) {
			result = db.runCommand(createUserCommand);
			log.info("create user result :{}, ok is {}", result, result.get("ok"));
		}
	}
	
	private static void updateUser(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("test");
		BasicDBObject updateUserCommand = new BasicDBObject("updateUser", "ken").append("pwd", "1234");
		Document result = db.runCommand(updateUserCommand);
		log.info("update user result :{}, ok is {}", result, result.get("ok"));
	}

	private static void deleteUser(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("test");
		Document result;
		BasicDBObject getUsersInfoCommand = new BasicDBObject("usersInfo",
				new BasicDBObject("user", "test").append("db", "test"));
		BasicDBObject dropUserCommand = new BasicDBObject("dropUser", "test");
		result = db.runCommand(getUsersInfoCommand);
		log.info("userInfo result:{}", result);
		List users = (List) result.get("users");
		if (!users.isEmpty()) {
			result = db.runCommand(dropUserCommand);
			log.info("drop user result :{}, ok is {}", result, result.get("ok"));
		}
	}

	private static void deleteDb(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("test");
		db.drop();
	}

}
