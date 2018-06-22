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

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
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
		return repo.save(appoint);
	}
	
	public Appoint confirmAppoint(String appointId) {
		Appoint appoint = repo.findById(appointId).get();
		appoint.setStatus(Appoint.STATUS_COMFIRM);
		return repo.save(appoint);
	}
	
	public Page<Appoint> listAppointByDate(String attendant, Date date, Pageable pageable) {
		DateTime dt = new DateTime(date);
		Date startDate = dt.withTimeAtStartOfDay().toDate();
		Date endDate = dt.withTimeAtStartOfDay().plusDays(1).toDate();
		Specification<Appoint> specification = dateWhereClause(attendant, startDate, endDate);
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
	private Specification<Appoint> dateWhereClause(String attendant, Date startDate, Date endDate) {
		return new Specification<Appoint>() {
			@Override
			public Predicate toPredicate(Root<Appoint> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();

				Join<Appoint, User> attendantJoin = root
						.join(root.getModel().getSingularAttribute("attendant", User.class), JoinType.LEFT);
				predicate.add(cb.equal(attendantJoin.get("id").as(String.class), attendant));

				predicate.add(cb.greaterThanOrEqualTo(root.get("startDate").as(Date.class), startDate));
				predicate.add(cb.lessThan(root.get("startDate").as(Date.class), endDate));
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
	}

}
