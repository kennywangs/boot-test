package com.xxb.module.identity.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "id_role")
public class Role implements Serializable {

	private static final long serialVersionUID = -5658423224478676522L;
	
	/**
	 * 角色类型，系统角色 值：10
	 */
	public static final Integer ROLE_TYPE_SYS = 10;

	/**
	 * 角色类型，机构角色 值：20
	 */
	public static final Integer ROLE_TYPE_GROUP = 20;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 48)
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String id;
	
	@Column(name = "name", nullable = false, unique=true)
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String name;
	
	@Column(name = "description")
	@org.hibernate.annotations.Type(type = "org.hibernate.type.StringType")
	private String description;
	
	@Column(name = "modify_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDate;

	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	/**
	 * 角色机构uuid（支持机构角色定义)
	 */
	@Column(name = "role_type")
	private Integer roleType = Role.ROLE_TYPE_SYS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "id_role_auth", joinColumns = {@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="auth_id")})
	private Set<Authority> auths;

	public Set<Authority> getAuths() {
		return auths;
	}

	public void setAuths(Set<Authority> auths) {
		this.auths = auths;
	}

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

	public Integer getRoleType() {
		return roleType;
	}

	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}

}
