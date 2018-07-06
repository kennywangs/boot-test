package com.xxb.config;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisChannelListener implements MessageListener {
	
	public final static String SYSTEM_PATTERN = "system*";
	
	private StringRedisTemplate redisTemplate;
	
	public RedisChannelListener(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void onMessage(Message message, byte[] bytes) {
		String body = new String(message.getBody());// 建议使用: valueSerializer
		String channel = new String(message.getChannel());
		String pattern = new String(bytes);
		System.out.print("onMessage system >> ");
		System.out.println(String.format("channel: %s, body: %s, bytes: %s", channel, body, pattern));
	}

}
