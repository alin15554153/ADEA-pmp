package com.anycc.pmp.rsmt.service;

import java.util.List;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.pmp.rsmt.entity.ResourceDown;

public interface ResourceDownCheckService {

	DTData<ResourceDown> findUncheckedApplicationListBySql(ResourceDown resourceDown, String myId, String myRoleId, DTPager pager);

	//DTData<ResourceDown> findUncheckedApplicationList(Specification<ResourceDown> specification, DTPager pager);

	//审核资源下载申请
	Response checkApplication(String id, int sid, String status, String remark);

	//查看过往审核记录
	Response showPreRemark(String id);

	public List<ResourceDown> findTopSexList(ResourceDown resourceDown, String myId, String myRoleId);

}
