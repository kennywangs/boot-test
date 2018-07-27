package com.xxb.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xxb.base.BaseController;
import com.xxb.base.ServiceResult;
import com.xxb.test.entity.Test;
import com.xxb.test.repository.TestRepository;
import com.xxb.test.service.TestService;
import com.xxb.util.jackson.Djson;
import com.xxb.util.jackson.JacksonJsonUtil;

@RestController
@RequestMapping("/test")
public class TestController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private TestRepository repo;
	
	@Autowired
	private TestService testService;
	
	@RequestMapping(value = "/hello",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ServiceResult hello(@RequestHeader String appId){
		ServiceResult ret = new ServiceResult();
		ret.setMsg("hello world.");
		ret.setData(appId);
		return ret;
	}
	
	@RequestMapping(value = "/testresp",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> testResp(){
		logger.info("log info test");
		logger.error("log error test");
		return new ResponseEntity<String>("manage/index", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/save",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method=RequestMethod.POST)
	public String save(@RequestBody Test test){
		test.setFname(String.valueOf(System.currentTimeMillis()));
		Test e = testService.save(test);
		test.setFname("final");
//		e = testService.save(test);
		int ret = testService.updateVersion(test.getFid());
		if (ret<1) {
			return handleError("更新不成功");
		}
//		repo.save(e);
		return handleResult("save succussful.", e);
	}
	
	@RequestMapping(value = "/list",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String list(@PageableDefault(value = 10, sort = { "fid" }, direction = Sort.Direction.DESC) Pageable pageable){
		Page<Test> ret = repo.findAll(pageable);
		return handlePageResult("list page succussful.", ret);
	}
	
	@RequestMapping(value = "/get", produces=MediaType.APPLICATION_JSON_UTF8_VALUE, method=RequestMethod.GET)
	public String get(String id){
		Test e = repo.findById(id).get();
		return handleResult("get by id succussful.", e);
	}
	
	@RequestMapping(value = "/getview", produces=MediaType.APPLICATION_JSON_UTF8_VALUE, method=RequestMethod.GET)
	public String getview(String id) throws JsonProcessingException{
		Test e = repo.findById(id).get();
		return JacksonJsonUtil.toJson(e, new Djson(Test.class,null,"fid"));
	}
	
	@RequestMapping(value = "/gettest", produces=MediaType.APPLICATION_JSON_UTF8_VALUE, method=RequestMethod.GET)
	public Test getTest(String id){
		Test e = repo.findById(id).get();
		return e;
	}
	
	@RequestMapping(value = "/systempub", method=RequestMethod.GET)
	public void testRedisPub(String id){
		stringRedisTemplate.convertAndSend("systemchat",String.valueOf(Math.random()));
	}

}
