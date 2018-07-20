package com.xxb;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.xxb.config.LoadAdditionalProperties;

@SpringBootApplication
//@ComponentScan(basePackages = { "com.xxb.**.config","com.xxb.**.service" })
@ComponentScan(basePackages = { "com.xxb" })
//@EnableAutoConfiguration(exclude = { MultipartAutoConfiguration.class})
@EnableTransactionManagement
public class BootTestApplication {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(BootTestApplication.class).listeners(new LoadAdditionalProperties()).run(args);
	}

}
