package com.xxb.web.upload;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.xxb.base.BaseController;

@Controller
@RequestMapping("/upload")
public class UploadController extends BaseController {

	@Autowired
	private Environment env;

	@RequestMapping(value = "/singleupload", method = RequestMethod.POST)
	@ResponseBody
	public String upload(HttpServletRequest request, @RequestParam("file") CommonsMultipartFile file)
			throws IOException {
		if (file.getSize() > 0) {
			String uploadPath = env.getProperty("config.upload.path");
			try {
				FileUtils.copyInputStreamToFile(file.getInputStream(),
						new File(uploadPath, file.getOriginalFilename()));
			} catch (Exception e) {
				return handleError("上传失败！", e);
			}
		}
		return handleResult("上传成功！");
	}

	@RequestMapping(value = "/batchupload", method = RequestMethod.POST)
	@ResponseBody
	public String batchUpload(HttpServletRequest request) throws IOException {
		// DiskFileItemFactory 保存文件
//		DiskFileItemFactory factory = new DiskFileItemFactory();
//		ServletFileUpload upload = new ServletFileUpload(factory);
//		upload.setHeaderEncoding("UTF-8");
//		List<FileItem> items;
//		try {
//			items = upload.parseRequest(request);
//			String uploadPath = env.getProperty("upload.path");
//			for (FileItem fileItem:items) {
//				File uploadedDist = new File(uploadPath, fileItem.getName());
//				fileItem.write(uploadedDist);
//			}
//		} catch (Exception e) {
//			return handleError("上传失败！", e);
//		}
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		if (files==null || files.isEmpty()) {
			return handleError("没有上传任何文件！");
		}
		for (MultipartFile file : files) {
			if (file.getSize() > 0) {
				String uploadPath = env.getProperty("config.upload.path");
				try {
					FileUtils.copyInputStreamToFile(file.getInputStream(),
							new File(uploadPath, file.getOriginalFilename()));
				} catch (Exception e) {
					return handleError("上传失败！", e);
				}
			}
		}
		return handleResult("上传成功！");
	}

}