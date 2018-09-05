package com.xxb.module.oss.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.xxb.base.ProjectException;
import com.xxb.module.oss.entity.OssBucket;
import com.xxb.module.oss.entity.OssFile;
import com.xxb.module.oss.repository.OssBucketRepository;
import com.xxb.module.oss.repository.OssFileRepository;
import com.xxb.util.PojoConvertUtil;
import com.xxb.util.web.MyPage;

@Service
public class OssFileService {
	
	@Autowired
	private OssBucketRepository bucketRepo;
	
	@Autowired
	private OssFileRepository fileRepo;
	
	@Autowired
	private MongoTemplate template;
	
	@Autowired
	protected Environment env;

	public void createBucket(OssBucket bucket) {
		String uploadPath = env.getProperty("config.upload.ossPath");
		String realFileName = UUID.randomUUID().toString();
		File file = new File(uploadPath, realFileName);
		bucket.setFilePath(file.getAbsolutePath());
		bucket.setRealName(realFileName);
		bucketRepo.save(bucket);
	}

	public void modifyBucket(OssBucket bucket) {
		bucketRepo.findById(new ObjectId(bucket.get_id())).ifPresent(entity->{
			entity = PojoConvertUtil.convertPojo(bucket, OssBucket.class, entity);
			bucketRepo.save(entity);
		});
	}

	public OssBucket getBucket(String id) {
		return bucketRepo.findById(new ObjectId(id)).get();
	}

	public Page<OssBucket> getBuckets(JSONObject params, Pageable pageable) {
		return bucketRepo.findAll(pageable);
	}

	public void createOssFile(OssFile ossFile, MultipartFile file) {
		String bucektId = ossFile.getBucketId();
		if (StringUtils.isEmpty(bucektId)) {
			OssFile parent = fileRepo.findById(new ObjectId(ossFile.getParantId())).get();
			bucektId = parent.getBucketId();
			ossFile.setBucketId(bucektId);
		}
		OssBucket bucket = bucketRepo.findById(new ObjectId(bucektId)).get();
		String realFileName = UUID.randomUUID().toString();
		String oriFileName = file.getOriginalFilename();
		try {
			FileUtils.copyInputStreamToFile(file.getInputStream(),
					new File(bucket.getFilePath(),realFileName));
		} catch (Exception e) {
			throw new ProjectException("copy upload file failed.");
		}
		if (StringUtils.isEmpty(ossFile.getDescName())) {
			ossFile.setDescName(FilenameUtils.getBaseName(oriFileName));
		}
		ossFile.setExt(FilenameUtils.getExtension(oriFileName));
		ossFile.setFilePath(realFileName);
		fileRepo.save(ossFile);
	}

	public void modifyOssFile(OssFile ossFile) {
		fileRepo.findById(new ObjectId(ossFile.get_id())).ifPresent(entity->{
			entity = PojoConvertUtil.convertPojo(ossFile, OssFile.class, entity);
			fileRepo.save(entity);
		});
	}

	public OssFile getFile(String id) {
		return fileRepo.findById(new ObjectId(id)).get();
	}

	public Page<OssFile> getFiles(JSONObject params, Pageable pageable) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(params.getString("parantId"))) {
			query.addCriteria(Criteria.where("parantId").is(params.getString("parantId")));
		}
		if (StringUtils.isNotEmpty(params.getString("bucketId"))) {
			query.addCriteria(Criteria.where("bucketId").is(params.getString("bucketId")));
		}
		long count = template.count(query, OssFile.class);
		query.with(pageable.getSort());
		query.skip(pageable.getOffset());
		query.limit(pageable.getPageSize());
		List<OssFile> content = template.find(query, OssFile.class);
		return new MyPage<OssFile>(content, count);
	}

	public void deleteFile(String id) {
		fileRepo.findById(new ObjectId(id)).ifPresent(ossFile->{
			String uploadPath = env.getProperty("config.upload.ossPath");
			OssBucket bucket = bucketRepo.findById(new ObjectId(ossFile.getBucketId())).get();
			String filePath = bucket.getRealName() + "/" + ossFile.getFilePath();
			File file = new File(uploadPath, filePath);
			if (!file.exists()) {
				throw new ProjectException("文件已经不存在");
			}
			FileUtils.deleteQuietly(file);
			fileRepo.delete(ossFile);
		});
	}

	public void downloadFile(String id, HttpServletResponse response) {
		OssFile ossFile = fileRepo.findById(new ObjectId(id)).get();
		if (ossFile==null) {
			throw new ProjectException("没有这个文件");
		}
		String uploadPath = env.getProperty("config.upload.ossPath");
		OssBucket bucket = bucketRepo.findById(new ObjectId(ossFile.getBucketId())).get();
		String filePath = bucket.getRealName() + "/" + ossFile.getFilePath();
		File file = new File(uploadPath, filePath);
		if (!file.exists()) {
			throw new ProjectException("文件已经不存在");
		}
		String downloadName = ossFile.getDescName()+"."+ossFile.getExt();
//		String mimeType = URLConnection.guessContentTypeFromName(downloadName);
//		if (mimeType == null) {
//			mimeType = "appliction/octet-stream";
//		}
		response.setContentType("appliction/octet-stream");
		response.setHeader("Content-Disposition", "inline; filename=\""+downloadName+"\"");
		response.setContentLength((int) file.length());
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			FileCopyUtils.copy(inputStream, response.getOutputStream());
		} catch (Exception e) {
			throw new ProjectException("下载文件出错",ProjectException.IOEXCEPTION,e);
		} finally {
				try {
					if (inputStream!=null) inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
