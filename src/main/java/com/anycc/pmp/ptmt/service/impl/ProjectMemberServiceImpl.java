package com.anycc.pmp.ptmt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.pmp.ptmt.dao.ProjectMemberDao;
import com.anycc.pmp.ptmt.entity.ProjectMember;
import com.anycc.pmp.ptmt.service.ProjectMemberService;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {
	@Autowired
	private ProjectMemberDao projectMemberDao;
	@Autowired
	private TempAuxiliaryUtils<ProjectMember> tempAuxiliaryUtils;

	@Override
	public DTData<ProjectMember> queryByPaging(ProjectMember projectMember,
			DTPager pager) {
		String hql = " from ProjectMember p where 1=1";
		if (projectMember.getRid() != null) {
			hql += " and p.rid='" + projectMember.getRid() + "' ";
		}
		if (projectMember.getPid() != null) {
			hql += " and p.pid='" + projectMember.getPid() + "' ";
		}
		Page<ProjectMember> projectMemberPage = tempAuxiliaryUtils
				.findBySearch(hql, tempAuxiliaryUtils.buildPageRequest(pager));
		return new DTData<ProjectMember>(projectMemberPage, pager);

	}

	@Override
	public DTData<ProjectMember> queryByPaging2(ProjectMember projectMember,
			DTPager pager) {
		String hql = " from ProjectMember p where p.rid is null ";
		if (projectMember.getPid() != null) {
			hql += " and p.pid='" + projectMember.getPid() + "' ";
		}
		Page<ProjectMember> projectMemberPage = tempAuxiliaryUtils
				.findBySearch(hql, tempAuxiliaryUtils.buildPageRequest(pager));
		return new DTData<ProjectMember>(projectMemberPage, pager);
	}
}
