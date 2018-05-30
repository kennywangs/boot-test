package com.xxb.config;

import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xxb.util.ISO8601DateFormatEx;

@Configuration
@ComponentScan(basePackages = "com.xxb.**.controller", useDefaultFilters = false, includeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = { Controller.class, RestController.class }),
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = ControllerAdvice.class) })
public class WebConfig implements WebMvcConfigurer  {

	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	RequestMappingHandlerAdapter mappingHandlerAdapter;
	
	@PostConstruct
	public void init() {
		ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) mappingHandlerAdapter.getWebBindingInitializer();
		MyPropertyEditorRegistrar register = new MyPropertyEditorRegistrar();
		initializer.setPropertyEditorRegistrar(register);
		mappingHandlerAdapter.setWebBindingInitializer(initializer);
	}
	
//	@Bean("configurableWebBindingInitializer")
//	public ConfigurableWebBindingInitializer configurableWebBindingInitializer() {
//		logger.info("configurableWebBindingInitializer init-------");
//		ConfigurableWebBindingInitializer initializer = applicationContext.getBean(ConfigurableWebBindingInitializer.class);
//		if (initializer==null) {
//			initializer = new ConfigurableWebBindingInitializer();
//		}
//		MyPropertyEditorRegistrar register = new MyPropertyEditorRegistrar();
//		initializer.setPropertyEditorRegistrar(register);
//		return initializer;
//	}
	
	@Bean("servletHandlerAdapter")
	public HandlerAdapter servletHandlerAdapter() {
		SimpleServletHandlerAdapter adapter = new SimpleServletHandlerAdapter();
		return adapter;
	}
	
	@Bean(name = "localeResolver")
	public CookieLocaleResolver cookieLocaleResolver() {
		CookieLocaleResolver cookie = new CookieLocaleResolver();
		return cookie;
	}
	
	@Bean(name = "exceptionResolver")
	public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
		SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
		resolver.setDefaultStatusCode(200);
		resolver.setExceptionAttribute("exception");
		Properties properties = new Properties();
		properties.setProperty("java.lang.Exception", "common_error");
		resolver.setExceptionMappings(properties);
		return resolver;
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		JodaTimeFormatterRegistrar reg = new JodaTimeFormatterRegistrar();
		reg.setUseIsoFormat(true);
		reg.registerFormatters(registry);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/views/**").addResourceLocations("/views/static/");
		registry.addResourceHandler("/resource/**").addResourceLocations("/views/resources/");
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
		stringConverter.setWriteAcceptCharset(false);

		converters.add(new ByteArrayHttpMessageConverter());
		converters.add(stringConverter);
		converters.add(new ResourceHttpMessageConverter());
		converters.add(new SourceHttpMessageConverter<Source>());
		converters.add(new AllEncompassingFormHttpMessageConverter());

		logger.info("customize jackson2 ObjectMapper with ISO8601DateFormatEx");
		ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().applicationContext(this.applicationContext)
				.build();
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setDateFormat(new ISO8601DateFormatEx());
		converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
	}

}
