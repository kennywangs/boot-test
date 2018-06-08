package com.xxb.test.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestFtlPageController {
	
	@RequestMapping("/test/test.jhtml")
	public String testFtl(ModelMap model){
	    model.addAttribute("name","FreeMarker 模版引擎 ");
	    model.addAttribute("today", new Date());
	    model.addAttribute("number", new Double(54321));
	    model.addAttribute("double", new Double(54321.62345));
	    return "test";
	}

}
