package com.anycc.pmp.comm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.anycc.common.CommonUtils;
import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.DictionaryColumn;
import com.anycc.pmp.comm.dao.TempDemoDao;
import com.anycc.pmp.comm.entity.TempDemo;
import com.anycc.pmp.comm.service.TempDemoService;
@Service
public class TempDemoServiceImpl implements TempDemoService{
	@Autowired
	private TempDemoDao tempDemoDao;
	@Autowired
	private TempAuxiliaryUtils<TempDemo> tempAuxiliaryUtils;
	@Autowired
	private CommonUtils<TempDemo> commonUtils;
	@Override
	public DTData<TempDemo> findDemos(Specification<TempDemo> specification,DTPager pager) {
		Page<TempDemo> tempPage = tempDemoDao.findAll(specification, tempAuxiliaryUtils.buildPageRequest(pager));
		commonUtils.convertDictionaryPage(tempPage,new DictionaryColumn("stageId","stageName","阶段名称"));
		/* 多个字典字段调用方法
		List<DictionaryColumn> list=new ArrayList<DictionaryColumn>();
		list.add(new DictionaryColumn("stageId1","stageName1","阶段名称1")); 
		list.add(new DictionaryColumn("stageId2","stageName2","阶段名称2")); 
		commonUtils.convertDictionaryPage(tempPage,list);*/
		return new DTData<TempDemo>(tempPage, pager);
	}
}
