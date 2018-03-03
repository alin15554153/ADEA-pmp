package com.anycc.pmp.ptmt.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.anycc.common.CommonUtils;
import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.FailedResponse;
import com.anycc.common.dto.ObjectResponse;
import com.anycc.common.dto.Response;
import com.anycc.common.dto.SuccessResponse;
import com.anycc.pmp.ptmt.dao.ProMissionMemberDAO;
import com.anycc.pmp.ptmt.dao.ProjectMemberDao;
import com.anycc.pmp.ptmt.dao.ProjectMissionDAO;
import com.anycc.pmp.ptmt.entity.ProMissionMember;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectMember;
import com.anycc.pmp.ptmt.entity.ProjectMission;
import com.anycc.pmp.ptmt.service.ProjectMissionService;

@Service("projectMissionService")
@Transactional
public class ProjectMissionServiceImpl implements ProjectMissionService {

	@Autowired
	private ProjectMissionDAO projectmissionDAO;
	@Autowired
	private ProjectMemberDao projectMemberDao;
	@Autowired
	private ProMissionMemberDAO proMissionMemberDao;
	@Autowired
	private TempAuxiliaryUtils<ProjectMission> tempAuxiliaryUtils;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private CommonUtils<ProjectMission> commonUtils;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.anycc.pmp.ptmt.service.ProjectService#queryByPaging(com.anycc.pmp
	 * .ptmt.entity.Project,com.anycc.rhip.common.dto.DTPager)
	 */
	@Override
	public DTData<ProjectMission> queryByPaging(Specification specification,
			DTPager pager) {
		Page<ProjectMission> projectPage = projectmissionDAO.findAll(specification,
				tempAuxiliaryUtils.buildPageRequest(pager));
		return new DTData<ProjectMission>(projectPage, pager);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.anycc.pmp.ptmt.service.ProjectService#addProject(com.anycc.pmp.ptmt
	 * .entity.Project)
	 */
	@Override
	public Response addProjectMission(ProjectMission projectmission) {
		Assert.notNull(projectmission);
		projectmissionDAO.save(projectmission);
		if (!"".equals(projectmission.getId())) {
			String mid = projectmission.getId();//编号id
			String pid = projectmission.getPid();//项目id
			String sid = projectmission.getSid();//阶段id
			String perids = projectmission.getMmanager();//参与人员
			if (!"".equals(perids)) {
				String perid[] = perids.split(",");
				for (int i = 0; i < perid.length; i++) {
					ProMissionMember proMissionMember = new ProMissionMember();
					proMissionMember.setMid(mid);
					proMissionMember.setPid(pid);
					proMissionMember.setSid(sid);
					proMissionMember.setUid(Integer.parseInt(perid[i]));
					proMissionMemberDao.save(proMissionMember);
				}
			}
			return new SuccessResponse();
		} else {
			return new FailedResponse("后台保存任务失败!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anycc.pmp.ptmt.service.ProjectService#queryById(java.lang.Long)
	 */
	@Override
	public Response queryById(String id) {
		// Assert.isTrue(id != null);
		Assert.notNull(id);
		ProjectMission projectmission = projectmissionDAO.findOne(id);
		if (projectmission != null && projectmission.getSid() != null) {
			return new ObjectResponse<ProjectMission>(projectmission);
		} else {
			return new FailedResponse("后台查询ID为[" + id + "]的信息失败！");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.anycc.pmp.ptmt.service.ProjectService#updateProject(java.lang.Long)
	 */
	@Override
	public Response updateProjectMission(ProjectMission projectmission) {
		Assert.notNull(projectmission);
		Assert.notNull(projectmission.getSid());
		String perids = projectmission.getMmanager();
		String newper[] = perids.split(",");
		List<String> tempList = Arrays.asList(newper);
		List<String> params = new ArrayList<String>();
		List<ProMissionMember> oldMembers = proMissionMemberDao
				.findByMid(projectmission.getId());
		for (ProMissionMember proMissionMember : oldMembers) {
			params.add(proMissionMember.getUid().toString());
			if (!tempList.contains(proMissionMember.getUid().toString())) {// 需要删除的
				proMissionMemberDao.delete(proMissionMember.getId());
			}
		}
		if(!"".equals(perids)){
			for (int i = 0; i < newper.length; i++) {
				if (!params.contains(newper[i])) {// 需要新增的
					ProMissionMember proMissionMember = new ProMissionMember();
					proMissionMember.setMid(projectmission.getId());
					proMissionMember.setPid(projectmission.getPid());
					proMissionMember.setSid(projectmission.getSid());
					proMissionMember.setUid(Integer.parseInt(newper[i]));
					proMissionMemberDao.save(proMissionMember);
				}
			}	
		}
		projectmissionDAO.save(projectmission);
		return new SuccessResponse();
	}

	/*
	 * 删除
	 * 
	 * @see com.anycc.pmp.ptmt.service.ProjectService#delete(java.lang.Long)
	 */
	@Override
	public Response delete(String l) {
		//删除任务负责人
		List<ProMissionMember> proMissionMembers = proMissionMemberDao
				.findByMid(l);
		if (proMissionMembers.size() > 0 && proMissionMembers != null) {
			for (ProMissionMember proMissionMember : proMissionMembers) {
				proMissionMemberDao.delete(proMissionMember.getId());
			}
		}
		//删除任务
		projectmissionDAO.delete(l);
		return new SuccessResponse();
	}


	@Override
	public List<ProjectMember> findBySid(String sid) {
		// TODO Auto-generated method stub
		return projectMemberDao.findBySid(sid);
	}

}
