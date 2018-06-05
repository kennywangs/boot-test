package com.xxb.test.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "testmg")
public class TestMg implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    private String _id;
    private String fid;
    private String fname;
    private Date fdate;
    private String group_id;
    private TestMgGroup groupinfo;
    
	public TestMgGroup getGroupinfo() {
		return groupinfo;
	}

	public void setGroupinfo(TestMgGroup groupinfo) {
		this.groupinfo = groupinfo;
	}

	public TestMg(String _id, String fid, String fname, Date fdate) {
    	this._id = _id;
        this.fid = fid;
        this.fname = fname;
        this.fdate = fdate;
    }
    
    public TestMg() {
    	super();
    }
    
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public Date getFdate() {
		return fdate;
	}

	public void setFdate(Date fdate) {
		this.fdate = fdate;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

}
