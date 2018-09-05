package com.xxb.module.oss.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.xxb.base.BaseController;
import com.xxb.module.oss.entity.OssBucket;
import com.xxb.module.oss.entity.OssFile;
import com.xxb.module.oss.service.OssFileService;
import com.xxb.util.JsonUtils;

@RestController
@RequestMapping("/oss")
public class OssFileController extends BaseController {
	
	@Autowired
	private OssFileService ossService;
	
	@PostMapping(value="/bucket.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> createBucket(@RequestBody OssBucket bucket){
		ossService.createBucket(bucket);
		return new ResponseEntity<String>(handleResult("成功"), HttpStatus.OK);
	}
	
	@PutMapping(value="/bucket.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> modifyBucket(@RequestBody OssBucket bucket){
		ossService.modifyBucket(bucket);
		return new ResponseEntity<String>(handleResult("成功"), HttpStatus.OK);
	}
	
	@GetMapping(value="/bucket.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> viewBucket(@RequestParam String id){
		OssBucket bucket = ossService.getBucket(id);
		return new ResponseEntity<String>(handleResult("成功", bucket), HttpStatus.OK);
	}
	
	@PostMapping(value="/bucket/search.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> listBucket(@RequestBody JSONObject params, @PageableDefault(value=10, sort={"name"}, direction=Sort.Direction.ASC) Pageable pageable){
		Page<OssBucket> page = ossService.getBuckets(params, pageable);
		return new ResponseEntity<String>(handlePageResult("成功", page), HttpStatus.OK);
	}
	
	@PostMapping(value="/file.do")
	public ResponseEntity<String> createFile(HttpServletRequest request){
		MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
		String params = ((MultipartHttpServletRequest) request).getParameter("params");
		OssFile ossFile = JsonUtils.parseJson(params, OssFile.class);
		ossService.createOssFile(ossFile, file);
		return new ResponseEntity<String>(handleResult("成功"), HttpStatus.OK);
	}
	
	@PutMapping(value="/file.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> modifyFile(@RequestBody OssFile ossFile){
		ossService.modifyOssFile(ossFile);
		return new ResponseEntity<String>(handleResult("成功"), HttpStatus.OK);
	}
	
	@GetMapping(value="/file.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> viewFile(@RequestParam String id){
		OssFile ossFile = ossService.getFile(id);
		return new ResponseEntity<String>(handleResult("成功", ossFile), HttpStatus.OK);
	}
	
	@DeleteMapping(value="/file.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> deleteFile(@RequestParam String id){
		ossService.deleteFile(id);
		return new ResponseEntity<String>(handleResult("成功"), HttpStatus.OK);
	}
	
	@PostMapping(value="/file/search.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> listFile(@RequestBody JSONObject params, @PageableDefault(value=10, sort={"name"}, direction=Sort.Direction.ASC) Pageable pageable){
		Page<OssFile> page = ossService.getFiles(params, pageable);
		return new ResponseEntity<String>(handlePageResult("成功", page), HttpStatus.OK);
	}
	
	@GetMapping(value="/file/download.do")
	public ResponseEntity<String> downloadFile(@RequestParam String id, HttpServletResponse response){
		ossService.downloadFile(id, response);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
}
