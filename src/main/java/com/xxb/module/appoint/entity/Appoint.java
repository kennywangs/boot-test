package com.xxb.module.appoint.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.xxb.module.identity.entity.User;

@Entity
@Table(name = "module_appoint")
public class Appoint implements Serializable {

	private static final long serialVersionUID = -6409607970655659281L;
	
	public static final Integer STATUS_APPOINT = 1;
	
	public static final Integer STATUS_COMFIRM = 10;
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")
	@Column(name="id",length=48)
	@Type(type="org.hibernate.type.StringType")
	private String id;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private User customer;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "attendant_id")
	private User attendant;
	
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	/**
	 * 预约开始时间
	 */
	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	/**
	 * 预约结束时间
	 */
	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	/**
	 * 备注
	 */
	@Column(name = "comment",length=10240)
	@Type(type="org.hibernate.type.StringType")
	private String comment;
	
	/**
	 * 附加信息(json)
	 */
	@Column(name = "add_info",length=102400)
	@Type(type="org.hibernate.type.StringType")
	private String info;
	
	private Integer status = STATUS_APPOINT;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	public User getAttendant() {
		return attendant;
	}

	public void setAttendant(User attendant) {
		this.attendant = attendant;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
