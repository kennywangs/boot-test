package com.xxb.config;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisMessageListener implements MessageListener {

	public final static String LISTENER_PATTERN = "__keyevent@0__:*";
	
	private StringRedisTemplate redisTemplate;
	
	public RedisMessageListener(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void onMessage(Message message, byte[] bytes) {
		byte[] body = message.getBody();// 建议使用: valueSerializer
		byte[] channel = message.getChannel();
		System.out.print("onMessage >> ");
		System.out.println(String.format("channel: %s, body: %s, bytes: %s", new String(channel), new String(body),
				new String(bytes)));
	}

}
