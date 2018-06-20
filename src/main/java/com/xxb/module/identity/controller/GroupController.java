package com.xxb.module.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxb.base.BaseController;
import com.xxb.module.identity.entity.Group;
import com.xxb.module.identity.repository.GroupRepository;

@RestController
@RequestMapping("/group")
public class GroupController extends BaseController {
	
	@Autowired
	private GroupRepository repo;
	
	@RequestMapping(value="/save")
	public String saveGroup(@RequestBody Group group) {
		group = repo.save(group);
		return handleResult("成功", group);
	}
	
	@RequestMapping(value="/view")
	public String viewGroup(String id) {
		Group group = repo.findById(id).get();
		return handleResult("成功", group);
	}
	
	@RequestMapping(value="/list")
	public String listGroup(@PageableDefault(value = 10, sort = { "name" }, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Group> groups = repo.findAll(pageable);
		return handleResult("成功", groups);
	}

}
