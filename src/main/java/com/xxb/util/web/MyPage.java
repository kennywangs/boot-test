package com.xxb.util.web;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class MyPage<T> implements Page<T> {
	
	private List<T> content;
	private long totalElement;
	
	public MyPage(List<T> content, long totalElement) {
		this.content = content;
		this.totalElement = totalElement;
	}

	@Override
	public List<T> getContent() {
		return content;
	}
	
	public void getContent(List<T> content) {
		this.content = content;
	}

	@Override
	public int getNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfElements() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Sort getSort() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasContent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFirst() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLast() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Pageable nextPageable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pageable previousPageable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTotalElements() {
		return totalElement;
	}

	@Override
	public int getTotalPages() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <U> Page<U> map(Function<? super T, ? extends U> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
