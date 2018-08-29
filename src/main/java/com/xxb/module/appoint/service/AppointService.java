package com.xxb.module.appoint.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.xxb.base.ProjectException;
import com.xxb.module.appoint.entity.Appoint;
import com.xxb.module.appoint.repository.AppointRepository;
import com.xxb.module.base.service.BaseService;
import com.xxb.module.identity.entity.User;

@Service
@Transactional(rollbackFor = Throwable.class)
public class AppointService extends BaseService<Appoint> {
	
	@Autowired
	private AppointRepository repo;

	public Appoint startAppoint(Appoint appoint) {
		appoint.setCreateDate(new Date());
		return repo.save(appoint);
	}
	
	public void confirmAppoint(String appointId, User user) {
		Appoint appoint = repo.findById(appointId).get();
		if (appoint==null) {
			throw new ProjectException("没有这个预约");
		}
		appoint.setOperator(user.getId());
		appoint.setStatus(Appoint.STATUS_COMFIRM);
		repo.save(appoint);
	}
	
	public void cancelAppoint(String appointId, User user) {
		Appoint appoint = repo.findById(appointId).get();
		if (appoint==null) {
			throw new ProjectException("没有这个预约");
		}
		appoint.setOperator(user.getId());
		appoint.setStatus(Appoint.STATUS_CANCEL);
		repo.save(appoint);
	}
	
	public void deleteAppoint(String appointId) {
		Appoint appoint = repo.findById(appointId).get();
		if (appoint==null) {
			throw new ProjectException("没有这个预约");
		}
		repo.delete(appoint);
	}
	
	@Transactional(readOnly = true)
	public Page<Appoint> listAppointByDate(String customer, String attendant, Date date, Pageable pageable) {
		DateTime dt = new DateTime(date);
		Date startDate = dt.withTimeAtStartOfDay().toDate();
		Date endDate = dt.withTimeAtStartOfDay().plusDays(1).toDate();
		Specification<Appoint> specification = dateWhereClause(customer,attendant, startDate, endDate);
		Page<Appoint> page = repo.findAll(specification, pageable);
		return page;
	}

	@Transactional(readOnly = true)
	public boolean canAppoint(String attendant, Date startTime, Date endTime) {
		JSONObject searchParam = new JSONObject();
		searchParam.put("startTime", startTime);
		searchParam.put("endTime", endTime);
		searchParam.put("attendant", attendant);

		Specification<Appoint> specification = checkWhereClause(searchParam);
		List<Appoint> list = repo.findAll(specification);
		if (CollectionUtils.isEmpty(list)) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("serial")
	private Specification<Appoint> checkWhereClause(final JSONObject searchParam) {
		return new Specification<Appoint>() {
			@Override
			public Predicate toPredicate(Root<Appoint> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();

				Join<Appoint, User> attendantJoin = root
						.join(root.getModel().getSingularAttribute("attendant", User.class), JoinType.LEFT);
				predicate.add(cb.equal(attendantJoin.get("id").as(String.class), searchParam.getString("attendant")));
				predicate.add(cb.equal(root.get("status").as(Integer.class), Appoint.STATUS_COMFIRM));

				predicate.add(cb.greaterThanOrEqualTo(root.get("startDate").as(Date.class), searchParam.getDate("startTime")));
				predicate.add(cb.lessThan(root.get("startDate").as(Date.class), searchParam.getDate("endTime")));
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
	}
	
	@SuppressWarnings("serial")
	private Specification<Appoint> dateWhereClause(String customer, String attendant, Date startDate, Date endDate) {
		return new Specification<Appoint>() {
			@Override
			public Predicate toPredicate(Root<Appoint> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();

				if (StringUtils.isNotEmpty(customer)) {
					Join<Appoint, User> attendantJoin = root
							.join(root.getModel().getSingularAttribute("customer", User.class), JoinType.LEFT);
					predicate.add(cb.equal(attendantJoin.get("id").as(String.class), customer));
					
				}
				if (StringUtils.isNotEmpty(attendant)) {
					Join<Appoint, User> attendantJoin = root
							.join(root.getModel().getSingularAttribute("attendant", User.class), JoinType.LEFT);
					predicate.add(cb.equal(attendantJoin.get("id").as(String.class), attendant));
					
				}
				if (startDate != null) {
					predicate.add(cb.greaterThanOrEqualTo(root.get("startDate").as(Date.class), startDate));
				}
				if (endDate != null) {
					predicate.add(cb.lessThan(root.get("startDate").as(Date.class), endDate));
				}
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
	}
	
	@Scheduled(cron="5 0 0 * * ?")
	public void cleanTask() {
		DateTime dt = new DateTime();
		Date endDate = dt.minusDays(1).withTimeAtStartOfDay().toDate();
		logger.info("delete expired appoints end with:{}",endDate);
		Specification<Appoint> specification = dateWhereClause(null, null, null, endDate);
		List<Appoint> delAppoints = repo.findAll(specification);
		delAppoints.forEach(item->logger.info(item.getStartDate().toString()));
		repo.deleteInBatch(delAppoints);
	}

}
