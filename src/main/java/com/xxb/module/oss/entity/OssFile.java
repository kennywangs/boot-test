package com.xxb.module.oss.entity;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "oss_file")
public class OssFile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public OssFile() {
		super();
	}
	
	@Id
    private String _id;
	private String bucketId;
	private String parantId;
	private String descName;
	private String ext;
	@Indexed(unique = true)
	private String filePath;
	private int type;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getBucketId() {
		return bucketId;
	}
	public void setBucketId(String bucketId) {
		this.bucketId = bucketId;
	}
	public String getParantId() {
		return parantId;
	}
	public void setParantId(String parantId) {
		this.parantId = parantId;
	}
	public String getDescName() {
		return descName;
	}
	public void setDescName(String descName) {
		this.descName = descName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}

}
