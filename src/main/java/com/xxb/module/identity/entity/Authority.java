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
@Table(name = "id_authority")
public class Authority implements Serializable {

	private static final long serialVersionUID = -3682547834022812570L;
	
	/**
	 * 权限类型，url权限类型
	 */
	public static final Integer AUTH_TYPE_URL = 10;

	/**
	 * 权限类型，方法权限类型
	 */
	public static final Integer AUTH_TYPE_METHOD = 20;

	/**
	 * 权限类型，数据权限类型
	 */
	public static final Integer AUTH_TYPE_DATA = 30;

	/**
	 * 权限类型，功能权限类型
	 */
	public static final Integer AUTH_TYPE_MODULE = 40;

	/**
	 * 权限类型，功能操作类型
	 */
	public static final Integer AUTH_TYPE_OPERATE = 50;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 48)
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String id;
	
	@Column(name = "name", nullable = false, unique = true)
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String name;
	
	@Column(name = "description")
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String description;
	
	/**
	 * 资源类型
	 */
	@Column(name = "auth_type")
	private Integer authType = Authority.AUTH_TYPE_URL;
	
	/**
	 * web层url资源描述
	 */
	private String authUrl;
	
	/**
	 * 开放授权控制
	 */
	private Boolean needAuth = true;
	
	@Column(name = "modify_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDate;

	@Column(name = "create_date")
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getAuthType() {
		return authType;
	}

	public void setAuthType(Integer authType) {
		this.authType = authType;
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	public Boolean getNeedAuth() {
		return needAuth;
	}

	public void setNeedAuth(Boolean needAuth) {
		this.needAuth = needAuth;
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
