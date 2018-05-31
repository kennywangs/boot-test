package com.xxb.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import com.xxb.util.JsonUtils;

public abstract class BaseController {
	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	public <T> String handelPageResult(String msg, Page<T> page) {
		ServiceResult result = new ServiceResult();
		result.setTotalNumber(page.getTotalElements());
		result.setTotalPage(page.getTotalPages());
		result.setMsg(msg);
		result.setData(page.getContent());
		return JsonUtils.toJson(result);
	}

	public String handelResult(String msg, Object props) {
		ServiceResult result = new ServiceResult();
		result.setMsg(msg);
		result.setData(props);
		return JsonUtils.toJson(result);
	}

	public String handelResult(String msg) {
		ServiceResult result = new ServiceResult();
		result.setMsg(msg);
		return JsonUtils.toJson(result);
	}

	public String handelError(String msg, Throwable e, Object props) {
		ServiceResult result = new ServiceResult();
		setExResult(msg, e, result);
		result.setData(props);
		return JsonUtils.toJson(result);
	}

	public String handelError(String msg, Throwable e) {
		ServiceResult result = new ServiceResult();
		setExResult(msg, e, result);
		return JsonUtils.toJson(result);
	}
	
	public String handelError(String msg) {
		ServiceResult result = new ServiceResult();
		setExResult(msg, null, result);
		return JsonUtils.toJson(result);
	}

	private void setExResult(String msg, Throwable e, ServiceResult result) {
		result.setSuccess(false);
		if (e instanceof ProjectException) {
			ProjectException pe = (ProjectException) e;
			result.setMsg(msg + e.getMessage());
			result.setErrorCode(pe.getErrorCode());
			logger.debug(e.getMessage(), e);
		} else if (e!=null) {
			result.setMsg(msg);
			result.setErrorCode("UNKNOWN");
			logger.error(e.getMessage(), e);
		} else {
			result.setMsg(msg);
			result.setErrorCode("UNKNOWN");
			logger.error(msg);
		}
	}
}