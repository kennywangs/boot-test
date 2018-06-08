package com.xxb.config;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.xxb.freemarker.MyConver;
import com.xxb.freemarker.MyFreemarkerView;
import com.xxb.util.jackson.JsonReturnHandler;

@Configuration
// @ComponentScan(basePackages = "com.xxb.**.controller", useDefaultFilters =
// false, includeFilters = {
// @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {
// Controller.class, RestController.class }),
// @ComponentScan.Filter(type = FilterType.ANNOTATION, value =
// ControllerAdvice.class) })
public class WebConfig implements WebMvcConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Bean
	public CommandLineRunner customFreemarker(FreeMarkerViewResolver resolver) {
		return new CommandLineRunner() {
			@Override
			public void run(String... strings) throws Exception {
				// 增加视图
				resolver.setViewClass(MyFreemarkerView.class);
				// 添加自定义解析器
				Map<String, Object> map = resolver.getAttributesMap();
				map.put("conver", new MyConver());
			}
		};
	}

	@Bean
	public JsonReturnHandler jsonReturnHandler() {
		return new JsonReturnHandler();
	}

	// @Bean("servletHandlerAdapter")
	// public HandlerAdapter servletHandlerAdapter() {
	// SimpleServletHandlerAdapter adapter = new SimpleServletHandlerAdapter();
	// return adapter;
	// }
	//
	// @Bean(name = "localeResolver")
	// public CookieLocaleResolver cookieLocaleResolver() {
	// CookieLocaleResolver cookie = new CookieLocaleResolver();
	// return cookie;
	// }

	// @Bean(name = "exceptionResolver")
	// public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
	// SimpleMappingExceptionResolver resolver = new
	// SimpleMappingExceptionResolver();
	// resolver.setDefaultStatusCode(200);
	// resolver.setExceptionAttribute("exception");
	// Properties properties = new Properties();
	// properties.setProperty("java.lang.Exception", "common_error");
	// resolver.setExceptionMappings(properties);
	// return resolver;
	// }

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
		logger.info("" + handlers.size());
		handlers.add(jsonReturnHandler());
	}

	// @Override
	// public void addFormatters(FormatterRegistry registry) {
	// JodaTimeFormatterRegistrar reg = new JodaTimeFormatterRegistrar();
	// reg.setUseIsoFormat(true);
	// reg.registerFormatters(registry);
	// }
	//
	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {
	// registry.addResourceHandler("/views/**").addResourceLocations("/views/static/");
	// registry.addResourceHandler("/resource/**").addResourceLocations("/views/resources/");
	// }
	//
	// @Override
	// public void configureMessageConverters(List<HttpMessageConverter<?>>
	// converters) {
	// StringHttpMessageConverter stringConverter = new
	// StringHttpMessageConverter();
	// stringConverter.setWriteAcceptCharset(false);
	//
	// converters.add(new ByteArrayHttpMessageConverter());
	// converters.add(stringConverter);
	// converters.add(new ResourceHttpMessageConverter());
	// converters.add(new SourceHttpMessageConverter<Source>());
	// converters.add(new AllEncompassingFormHttpMessageConverter());
	//
	// logger.info("customize jackson2 ObjectMapper with ISO8601DateFormatEx");
	// ObjectMapper objectMapper =
	// Jackson2ObjectMapperBuilder.json().applicationContext(this.applicationContext)
	// .build();
	// objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	// objectMapper.setDateFormat(new ISO8601DateFormatEx());
	// converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
	// }

}
