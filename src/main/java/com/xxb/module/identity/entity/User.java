package com.xxb.module.identity.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "id_user")
public class User implements Serializable {

	private static final long serialVersionUID = 4948829448267610079L;

	/**
	 * 初始密码：123456
	 */
	public static final String defaultPassword = "e10adc3949ba59abbe56e057f20f883e";
	
	/**
	 * 超级管理员
	 */
	public static final int USER_TYPE_SUPER = 1;
	
	/**
	 * 系统管理员
	 */
	public static final int USER_TYPE_SYS = 2;
	
	/**
	 * 机构用户
	 */
	public static final int USER_TYPE_DEPT = 3;
	
	/**
	 * 用戶
	 */
	public static final int USER_TYPE_CUSTOM = 4;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 48)
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String id;

	private Integer type;

	@Column(name = "name", nullable = false, unique = true)
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String name;
	
	@Column(name = "description")
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String description;

	@Column(name = "mobile", nullable = false, unique = true)
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String mobile;

	@Column(name = "password", nullable = false)
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String password;

	// @Column(name="group_id", length=48)
	// @org.hibernate.annotations.Type(type="org.hibernate.type.StringType")
	// private String groupId;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private Group group;

	@Column(name = "modify_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDate;

	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "id_user_role", joinColumns = {@JoinColumn(name="user_id")},inverseJoinColumns={@JoinColumn(name="role_id")})
	private Set<Role> roles;
	
	@Column(name = "open_id")
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String openId;
	
	public void setSource(String source) {
		this.source = source;
	}

	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String source;

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

	// public String getGroupId() {
	// return groupId;
	// }
	//
	// public void setGroupId(String groupId) {
	// this.groupId = groupId;
	// }

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getSource() {
		return source;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	// -------------- domain ------------------
	
	@Transient
	private Set<String> auths;
	
	@Transient
	private String token;
	
	@Transient
	private Long ttl;
	
	public Long getTtl() {
		return ttl;
	}

	public void setTtl(Long ttl) {
		this.ttl = ttl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Set<String> getAuths() {
		return auths;
	}

	public void setAuths(Set<String> auths) {
		this.auths = auths;
	}
	
	public User excludeField(User user) {
		user.setPassword("");
		user.setRoles(null);
		return user;
	}

}
