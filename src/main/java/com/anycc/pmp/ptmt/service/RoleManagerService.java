package com.anycc.pmp.ptmt.service;

import org.springframework.data.jpa.domain.Specification;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.pmp.ptmt.entity.RoleManager;
import com.anycc.pmp.ptmt.entity.RoleProject;

public interface RoleManagerService {

	DTData<RoleManager> queryByPaging(Specification specification, DTPager pager);

	Response addRoleManager(RoleManager roleManager);

	Response updateRoleManager(RoleManager roleManager);

	Response queryById(String id);

	Response delete(String l);

	Response selectMember(String id, String rid);

	Response deleteLinkedUser(String mid);

	DTData<RoleProject> queryList(Specification<RoleProject> specification,
			DTPager pager, Long mid, String pid);

}
