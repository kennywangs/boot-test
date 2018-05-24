package com.xxb.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

@ControllerAdvice(basePackages="com.xxb.web.jsonp.controller")
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
	
	public JsonpAdvice() {
		super("callback","jsonp");
	}
	
	@Override
	protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
			MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
		super.beforeBodyWriteInternal(bodyContainer, contentType, returnType, request, response);
	}

}
