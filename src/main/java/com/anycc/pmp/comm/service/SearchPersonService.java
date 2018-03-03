package com.anycc.pmp.comm.service;


import com.anycc.commmon.web.entity.WebUser;

import java.util.List;

public interface SearchPersonService {
	//根据项目编号查出所有参与人员列表
	List<WebUser> findProjectMembers(String projectId);

	//根据项目编号、角色编号查出对应人员列表
	List<WebUser> findProjectMembers(String projectId, long roleId);

	//根据当前人机构查出角色为地区管理员的人员列表
	List<WebUser> findAreaAdmins(long orgId);

	//查出角色为集团管理员的人员列表
	List<WebUser> findSuperAdmins();
}
