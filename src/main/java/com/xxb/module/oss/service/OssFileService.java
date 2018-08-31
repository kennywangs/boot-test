package com.xxb.module.oss.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.xxb.base.ProjectException;
import com.xxb.module.oss.entity.OssBucket;
import com.xxb.module.oss.entity.OssFile;
import com.xxb.module.oss.repository.OssBucketRepository;
import com.xxb.module.oss.repository.OssFileRepository;
import com.xxb.util.PojoConvertUtil;

@Service
public class OssFileService {
	
	@Autowired
	private OssBucketRepository bucketRepo;
	
	@Autowired
	private OssFileRepository fileRepo;
	
	@Autowired
	protected Environment env;

	public void createBucket(OssBucket bucket) {
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

	public void createOssFile(OssFile ossFile, CommonsMultipartFile file) {
		String uploadPath = env.getProperty("project.upload.path");
		try {
			FileUtils.copyInputStreamToFile(file.getInputStream(),
					new File(uploadPath, file.getOriginalFilename()));
		} catch (Exception e) {
			
		}
	}

	public void modifyOssFile(OssFile ossFile) {
		// TODO Auto-generated method stub
		
	}

	public OssFile getFile(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<OssFile> getFiles(JSONObject params, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteFile(String id) {
		// TODO Auto-generated method stub
		
	}

	public void downloadFile(String id, HttpServletResponse response) {
		OssFile ossFile = fileRepo.findById(new ObjectId(id)).get();
		if (ossFile==null) {
			throw new ProjectException("没有这个文件");
		}
		String uploadPath = env.getProperty("project.upload.path");
		OssBucket bucket = bucketRepo.findById(new ObjectId(ossFile.getBucketId())).get();
		String filePath = uploadPath + bucket.getFilePath() + ossFile.getFilePath();
		File file = new File(filePath);
		if (!file.exists()) {
			throw new ProjectException("文件已经不存在");
		}
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			mimeType = "appliction/octet-stream";
		}
		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", "inline; filename=\""+file.getName()+"\"");
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
