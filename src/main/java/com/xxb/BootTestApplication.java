package com.xxb;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.xxb.config.LoadAdditionalProperties;

@SpringBootApplication
// @ComponentScan(basePackages = { "com.xxb.**.config","com.xxb.**.service" })
@ComponentScan(basePackages = { "com.xxb" })
// @EnableAutoConfiguration(exclude = { MultipartAutoConfiguration.class})
@EnableTransactionManagement
public class BootTestApplication {

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(BootTestApplication.class).listeners(new LoadAdditionalProperties()).run(args);
	}

}
