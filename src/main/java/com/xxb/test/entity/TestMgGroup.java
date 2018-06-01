package com.xxb.test.entity;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "test_group")
public class TestMgGroup implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    private String _id;
	private String uuid;
    private String name;
    
    public TestMgGroup(String _id, String uuid, String name) {
    	this._id = _id;
        this.setUuid(uuid);
        this.name = name;
    }
    
    public TestMgGroup() {
    	super();
    }
    
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
