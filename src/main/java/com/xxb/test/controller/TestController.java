package com.xxb.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xxb.base.BaseController;
import com.xxb.base.ServiceResult;
import com.xxb.test.entity.Test;
import com.xxb.test.repository.TestRepository;

@RestController
@RequestMapping("/test")
public class TestController extends BaseController {
	
	@Autowired
	private TestRepository repo;
	
	@RequestMapping(value = "/hello",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ServiceResult hello(@RequestHeader String appId){
		ServiceResult ret = new ServiceResult();
		ret.setMsg("hello world.");
		ret.setData(appId);
		return ret;
	}
	
	@RequestMapping(value = "/save",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method=RequestMethod.POST)
	public String save(@RequestBody Test test){
		Test e = repo.save(test);
		return handelResult("save succussful.", e);
	}
	
	@RequestMapping(value = "/list",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String list(@PageableDefault(value = 10, sort = { "fid" }, direction = Sort.Direction.DESC) Pageable pageable){
		Page<Test> ret = repo.findAll(pageable);
		return handelPageResult("list page succussful.", ret);
	}
	
	@RequestMapping(value = "/get", produces=MediaType.APPLICATION_JSON_UTF8_VALUE, method=RequestMethod.GET)
	public String get(String id){
		Test e = repo.findById(id).get();
		return handelResult("get by id succussful.", e);
	}
	
	@RequestMapping(value = "/gettest", produces=MediaType.APPLICATION_JSON_UTF8_VALUE, method=RequestMethod.GET)
	public Test getTest(String id){
		Test e = repo.findById(id).get();
		return e;
	}

}