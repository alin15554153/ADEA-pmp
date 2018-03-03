package com.anycc.pmp.comm.service;

import org.springframework.data.jpa.domain.Specification;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.pmp.comm.entity.TempDemo;

public interface TempDemoService {
	public DTData<TempDemo> findDemos(Specification<TempDemo> specification,DTPager pager);
}
