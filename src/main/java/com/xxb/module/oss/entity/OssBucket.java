package com.xxb.module.oss.entity;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "oss_bucket")
public class OssBucket implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public OssBucket() {
		super();
	}
	
	@Id
    private String _id;
	private String name;
	private String filePath;

	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
