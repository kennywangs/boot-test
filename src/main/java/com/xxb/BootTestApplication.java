package com.xxb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = { "com.xxb.**.config","com.xxb.**.service" })
@EnableAutoConfiguration(exclude = { MultipartAutoConfiguration.class})
@EnableTransactionManagement
public class BootTestApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(BootTestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BootTestApplication.class, args);
	}

}
