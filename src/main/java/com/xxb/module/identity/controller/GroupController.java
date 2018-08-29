package com.xxb.module.identity.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxb.base.BaseController;
import com.xxb.module.identity.entity.Group;
import com.xxb.module.identity.repository.GroupRepository;
import com.xxb.util.PojoConvertUtil;

@RestController
@RequestMapping("/group")
public class GroupController extends BaseController {
	
	@Autowired
	private GroupRepository repo;
	
	@RequestMapping(value="/save.do")
	public String saveGroup(@RequestBody Group group) {
		if (StringUtils.isEmpty(group.getId())) {
			group.setCreateDate(new Date());
			group.setModifyDate(new Date());
			group = repo.save(group);
		} else {
			Group entity = repo.findById(group.getId()).get();
			entity = PojoConvertUtil.convertPojo(group, Group.class, entity);
			entity.setModifyDate(new Date());
			group = repo.save(entity);
		}
		return handleResult("成功", group);
	}
	
	@RequestMapping(value="/view.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String viewGroup(String id) {
		Group group = repo.findById(id).get();
		return handleResult("获取机构成功", group);
	}
	
	@RequestMapping(value="/list.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listGroup(@PageableDefault(value = 10, sort = { "name" }, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Group> groups = repo.findAll(pageable);
		return handlePageResult("获取机构成功", groups);
	}

}
