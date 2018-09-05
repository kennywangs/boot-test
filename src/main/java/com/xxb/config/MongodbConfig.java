package com.xxb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
public class MongodbConfig {
	
	@Autowired
	protected Environment env;
	
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory());
	}
	
	@Bean
	public MongoDbFactory mongoDbFactory() {
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.connectionsPerHost(Integer.valueOf(env.getProperty("config.mongodb.option.connections-per-host")));
		builder.minConnectionsPerHost(Integer.valueOf(env.getProperty("config.mongodb.option.min-connections-per-host")));
		builder.threadsAllowedToBlockForConnectionMultiplier(Integer.valueOf(env.getProperty("config.mongodb.option.threads-allowed-to-block-for-connection-multiplier")));
		builder.socketTimeout(Integer.valueOf(env.getProperty("config.mongodb.option.socket-timeout")));
		builder.connectTimeout(Integer.valueOf(env.getProperty("config.mongodb.option.connect-timeout")));
		builder.heartbeatFrequency(Integer.valueOf(env.getProperty("config.mongodb.option.heartbeat-frequency")));
		builder.heartbeatConnectTimeout(Integer.valueOf(env.getProperty("config.mongodb.option.heartbeat-connect-timeout")));
		builder.heartbeatSocketTimeout(Integer.valueOf(env.getProperty("config.mongodb.option.heartbeat-socket-timeout")));
		builder.maxWaitTime(Integer.valueOf(env.getProperty("config.mongodb.option.max-wait-time")));
		
		ServerAddress serverAddress = new ServerAddress(env.getProperty("config.mongodb.host"), Integer.valueOf(env.getProperty("config.mongodb.port")));
		
		String userName = env.getProperty("config.mongodb.userName");
		String password = env.getProperty("config.mongodb.password");
		String dbName = env.getProperty("config.mongodb.dbName");
		MongoCredential credential = MongoCredential.createScramSha1Credential(userName, dbName, password.toCharArray());
		MongoClient client = new MongoClient(serverAddress, credential, builder.build());
		MongoDbFactory factory = new SimpleMongoDbFactory(client, dbName);
		return factory;
	}

}
