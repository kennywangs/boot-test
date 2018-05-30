package com.xxb.test.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@javax.persistence.Table(name = "xxb_test")
public class Test implements Serializable {

	private static final long serialVersionUID = -115738491068685754L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "fid")
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String fid;

	@Column(name = "fname")
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String fname;

	@Column(name = "fdate")
	@javax.persistence.Temporal(TemporalType.TIMESTAMP)
	private java.util.Date fdate;

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
		if (fdate != null) {
			java.util.Date ret = new java.util.Date(fdate.getTime());
			return ret;
		} else {
			return null;
		}

	}

	public void setFdate(Date fdate) {
		this.fdate = fdate;
	}
}