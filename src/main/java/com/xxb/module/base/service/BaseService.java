package com.xxb.module.base.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xxb.module.identity.entity.User;
import com.xxb.util.web.MyPage;

public abstract class BaseService<T> {
	
	protected final Logger logger = LoggerFactory.getLogger(BaseService.class);
	
	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	protected Page<T> findPage(String sql, String whereSql, JSONObject queryParam, Class<T> entityClass){
		long start = System.currentTimeMillis();
		Set<String> keySet = queryParam.keySet();
//		NativeQuery<T> query = (NativeQuery<T>) em.createNativeQuery(sql, entityClass);
		Query query = em.createNativeQuery(sql, entityClass);
		for (String key : keySet) {
			query.setParameter(key, queryParam.get(key));
		}
		Page<T> page = new MyPage<T>(query.getResultList(),getCountByWhere(whereSql, queryParam, entityClass));
		logger.info("用时： {}",System.currentTimeMillis()-start);
		return page;
	}
	
	protected long getCountByWhere(String whereSql, JSONObject queryParam, Class<T> entityClass) {
		Set<String> keySet = queryParam.keySet();
        StringBuffer sb = new StringBuffer("SELECT COUNT(*) ").append(whereSql);
        Query query = em.createNativeQuery(sb.toString());
        for (String key : keySet) {
			query.setParameter(key, queryParam.get(key));
		}
        Number ret = (Number) query.getSingleResult();
        return ret.longValue();
    }
	
	protected String getOrder(JSONObject searchParam) {
		StringBuffer sb = new StringBuffer();
		
		if (searchParam.containsKey("orderBy")) {
			JSONArray orders = searchParam.getJSONArray("orderBy");
			for (int i=0;i<orders.size();i++) {
				JSONObject item = orders.getJSONObject(i);
				if (i>0) {
					sb.append(", "+item.getString("field")+" "+item.getString("dir"));
				}else {
					sb.append(" order by "+item.getString("field")+" "+item.getString("dir"));
				}
			}
		}
		
		if (searchParam.containsKey("page")) {
			Integer page = searchParam.getInteger("page");
			Integer size = searchParam.getInteger("size");
			
			Integer start = page*size;
			sb.append(" limit "+size+" offset "+start);
		} else {
			sb.append(" limit 10 offset 0");
		}
		
		return sb.toString();
	}

	protected void appendSql(StringBuffer sb, String field, String eval, JSONObject queryParam, String key, Object value, String opt) {
		String blank = " ";
		sb.append(blank+opt+blank+field+blank+eval+blank+":"+key);
		if (value instanceof Date) {
			Date date = (Date)value;
			value = new Timestamp(date.getTime());
		}
		queryParam.put(key, value);
	}
	
}
