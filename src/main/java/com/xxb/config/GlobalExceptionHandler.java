package com.xxb.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.xxb.base.BaseController;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	public static final String ERROR_VIEW = "/error/error";
	
	public static final String DEFAULT_ERROR_VIEW = "/error";
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> throwableErrorHandler(HttpServletRequest req, Throwable e) throws Exception {
		logger.info("自定义异常处理-RuntimeException");
		String body = handleError(req.getRequestURL().toString()+"出错了：", e);
		ResponseEntity<String> ret = new ResponseEntity<String>(body, HttpStatus.INTERNAL_SERVER_ERROR);
		
		return ret;
	}

}
