package com.xxb.test.controller;

import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.xxb.base.BaseController;
import com.xxb.test.entity.TestMg;
import com.xxb.test.entity.TestMgGroup;
import com.xxb.test.repository.TestMgGroupRepository;
import com.xxb.test.repository.TestMgRepository;

@RestController
@RequestMapping("/testmg")
public class TestMgController extends BaseController {
	
	@Autowired
	private TestMgRepository repo;
	
	@Autowired
	private TestMgGroupRepository groupRepo;
	
	@Autowired
	private MongoTemplate template;
	
	@RequestMapping(value = "/save",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method=RequestMethod.POST)
	public String save(@RequestBody TestMg test){
		TestMg e = repo.save(test);
		return handelResult("save succussful.", e);
	}
	
	@RequestMapping(value = "/list",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String list(@PageableDefault(value = 10, sort = { "fid" }, direction = Sort.Direction.DESC) Pageable pageable){
		Page<TestMg> ret = repo.findAll(pageable);
		return handelPageResult("list page succussful.", ret);
	}
	
	@RequestMapping(value = "/get", produces=MediaType.APPLICATION_JSON_UTF8_VALUE, method=RequestMethod.GET)
	public String get(String id){
		TestMg e = repo.findById(new ObjectId(id)).get();
		DateTime startTime=new DateTime(2017, 9, 1, 0, 0,0);
		Aggregation aggregation = Aggregation.newAggregation(
//				Aggregation.project("fid", "fname", "fdate"),
				Aggregation.match(
                        Criteria.where("fdate").gte(startTime.toDate())),
				Aggregation.lookup("test_group", "group_id", "_id", "groupinfo")
//				Aggregation.unwind("groupinfo"),
//				Aggregation.project("fid", "fname", "fdate", "groupinfo.name")
//				Aggregation.match(
//                        Criteria.where("groupinfo.name").is("中国")),
		);
		AggregationResults<TestMg> result = template.aggregate(aggregation, "testmg", TestMg.class);
		return handelResult("get by id succussful."+result.getRawResults().toJson(), result.getMappedResults());
	}
	
	@RequestMapping(value = "/delete", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String delete(){
		repo.deleteAll();
		return handelResult("delete succussful.");
	}
	
	@RequestMapping(value = "/gen", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String gen(){
		groupRepo.deleteAll();
		List<TestMgGroup> list = Lists.newArrayList();
		TestMgGroup group = new TestMgGroup(null,UUID.randomUUID().toString(),"中国");
		list.add(group);
		groupRepo.saveAll(list);
		return handelResult("genarate succussful.",groupRepo.findAll());
	}

}
