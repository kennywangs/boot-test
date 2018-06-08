package com.xxb.module.identity.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "id_user")
public class User implements Serializable {

	private static final long serialVersionUID = 4948829448267610079L;
	
	public static final String defaultPassword  = "83aa400af464c76d";
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")
	@Column(name="id",length=48)
	@org.hibernate.annotations.Type(type="org.hibernate.type.StringType")
	private String id;
	
	@Column(name="name", nullable=false, unique=true)
	@org.hibernate.annotations.Type(type="org.hibernate.type.StringType")
	private String name;
	
	@Column(name="mobile", nullable=false)
	@org.hibernate.annotations.Type(type="org.hibernate.type.StringType")
	private String mobile;
	
	@Column(name="password", nullable=false)
	@org.hibernate.annotations.Type(type="org.hibernate.type.StringType")
	private String password;
	
	@Column(name="group_id", length=48)
	@org.hibernate.annotations.Type(type="org.hibernate.type.StringType")
	private String groupId;
	
	@Column(name="modify_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDate;
	
	@Column(name="create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
