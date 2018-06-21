package com.xxb.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xxb.base.BaseController;

@Controller
@RequestMapping("/manage")
public class ManageAction extends BaseController {
	
	@RequestMapping(value = "/{folderName}/{tplName}")
	public String processTpl(HttpServletRequest request, ModelMap model) throws Exception {
		Map<String, String> pathVariables = resolvePathVariables(request);
		String folderName = pathVariables.get("folderName");
		String tplName = pathVariables.get("tplName");
		return "manage/"+folderName+"/"+tplName;
	}
	
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, ModelMap model) throws Exception {
		return "manage/index";
	}

}
