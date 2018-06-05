package com.xxb.config;

import java.util.Date;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;

import com.xxb.util.Date.ISO8601DateEditor;

public class MyPropertyEditorRegistrar implements PropertyEditorRegistrar, WebBindingInitializer {

	public void registerCustomEditors(PropertyEditorRegistry registry) {
		// 1.将string类型的日期字符串初始化为date类型;
		registry.registerCustomEditor(Date.class, new ISO8601DateEditor(false));
		// 2.去除参数两边的空格;
		registry.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}

	@Override
	public void initBinder(WebDataBinder binder) {
		// 1.将string类型的日期字符串初始化为date类型;
		binder.registerCustomEditor(Date.class, new ISO8601DateEditor(false));
		// 2.去除参数两边的空格;
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}

}