package com.xxb.config;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class LoadAdditionalProperties implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
	private ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		try {
			Resource[] resources = (Resource[]) resolver.getResources("classpath:project-*.properties");
			for (Resource resource:resources) {
				List<PropertySource<?>> propertySources = new PropertiesPropertySourceLoader().load(resource.getFilename(), resource);
				for (PropertySource<?> ps:propertySources) {
					event.getEnvironment().getPropertySources().addLast(ps);
				}
			}
			Resource[] yamlResources = (Resource[]) resolver.getResources("classpath:project-*.yaml");
			for (Resource resource:yamlResources) {
				List<PropertySource<?>> propertySources = new YamlPropertySourceLoader().load(resource.getFilename(), resource);
				for (PropertySource<?> ps:propertySources) {
					event.getEnvironment().getPropertySources().addLast(ps);
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
