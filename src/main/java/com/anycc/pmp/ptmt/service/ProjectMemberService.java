package com.anycc.pmp.ptmt.service;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.pmp.ptmt.entity.ProjectMember;

public interface ProjectMemberService {

	public DTData<ProjectMember> queryByPaging(ProjectMember projectMember,
			DTPager pager);

	public DTData<ProjectMember> queryByPaging2(ProjectMember projectMember,
			DTPager pager);
}
