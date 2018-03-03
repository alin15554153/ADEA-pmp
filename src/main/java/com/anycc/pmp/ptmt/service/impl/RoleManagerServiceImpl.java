package com.anycc.pmp.ptmt.service.impl;

import java.util.List;

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
import com.anycc.common.dto.DictionaryColumn;
import com.anycc.common.dto.FailedResponse;
import com.anycc.common.dto.ObjectResponse;
import com.anycc.common.dto.Response;
import com.anycc.common.dto.SuccessResponse;
import com.anycc.pmp.ptmt.dao.ProjectMemberDao;
import com.anycc.pmp.ptmt.dao.RoleMPermissionDAO;
import com.anycc.pmp.ptmt.dao.RoleManagerDAO;
import com.anycc.pmp.ptmt.entity.ProjectMember;
import com.anycc.pmp.ptmt.entity.RoleMPermission;
import com.anycc.pmp.ptmt.entity.RoleManager;
import com.anycc.pmp.ptmt.entity.RoleProject;
import com.anycc.pmp.ptmt.service.RoleManagerService;
import com.anycc.service.DictionaryService;

@Service("roleManagerService")
@Transactional
public class RoleManagerServiceImpl implements RoleManagerService {

	@Autowired
	private CommonUtils<RoleManager> commonUtils;
	@Autowired
	private ProjectMemberDao projectMemberDAO;
	@Autowired
	private RoleManagerDAO roleManagerDAO;
	@Autowired
	private RoleMPermissionDAO rolePermissionDAO;

	@Autowired
	private TempAuxiliaryUtils<RoleManager> tempAuxiliaryUtils;
	@Autowired
	private TempAuxiliaryUtils<ProjectMember> projectMemberTempAuxiliaryUtils;
	@Autowired
	private TempAuxiliaryUtils<RoleMPermission> rolePermissionTempAuxiliaryUtils;
	@Autowired
	private DictionaryService dictionaryService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.anycc.pmp.ptmt.service.RoleManagerService#queryByPaging(com.anycc
	 * .pmp.ptmt.entity.RoleManager,com.anycc.rhip.common.dto.DTPager)
	 */
	@Override
	public DTData<RoleManager> queryByPaging(Specification specification,
			DTPager pager) {
		Page<RoleManager> roleManagerPage = roleManagerDAO.findAll(
				specification, tempAuxiliaryUtils.buildPageRequest(pager));
		commonUtils.convertDictionaryPage(roleManagerPage,
				new DictionaryColumn("rname", "rnames", "项目角色"));
		return new DTData<RoleManager>(roleManagerPage, pager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.anycc.pmp.ptmt.service.RoleManagerService#addRoleManager(com.anycc
	 * .pmp.ptmt.entity.RoleManager)
	 */
	@Override
	public Response addRoleManager(RoleManager roleManager) {
		Assert.notNull(roleManager);
		// 保存角色表
		roleManagerDAO.save(roleManager);
		// 保存中间表
		String sid[] = roleManager.getSid().split(",");
		for (int i = 0; i < sid.length; i++) {
			RoleMPermission rolePermission = new RoleMPermission();
			rolePermission.setSid(sid[i]);
			rolePermission.setRid(roleManager.getRid());
			rolePermission.setPid(roleManager.getPid());
			Assert.notNull(rolePermission);
			rolePermissionDAO.save(rolePermission);
		}
		if (roleManager.getRid() != null) {
			return new SuccessResponse();
		} else {
			return new FailedResponse("后台保存roleManager失败!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.anycc.pmp.ptmt.service.RoleManagerService#queryById(java.lang.Long)
	 */
	@Override
	public Response queryById(String rid) {
		Assert.notNull(rid);
		RoleManager roleManager = roleManagerDAO.findOne(rid);
		String pid = roleManager.getPid();
		String hqlString = "from RoleMPermission where pid='" + pid
				+ "' and rid='" + rid + "'";
		List<RoleMPermission> list = rolePermissionTempAuxiliaryUtils
				.findBySearch(hqlString);
		String sid = "";
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				sid += list.get(i).getSid() + ",";
			}
			sid = sid.substring(0, sid.length() - 1);
		}
		roleManager.setSid(sid);
		if (roleManager != null && roleManager.getRid() != null) {
			return new ObjectResponse<RoleManager>(roleManager);
		} else {
			return new FailedResponse("后台查询ID为[" + rid + "]的信息失败！");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.anycc.pmp.ptmt.service.RoleManagerService#updateRoleManager(java.
	 * lang.Long)
	 */
	@Override
	public Response updateRoleManager(RoleManager roleManager) {
		Assert.notNull(roleManager);
		// 删除中间表中数据
		String hqlString = "from RoleMPermission where pid='"
				+ roleManager.getPid() + "' and rid='" + roleManager.getRid()
				+ "'";
		List<RoleMPermission> list = rolePermissionTempAuxiliaryUtils
				.findBySearch(hqlString);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				rolePermissionDAO.delete(list.get(i));
			}
		}

		// 保存角色表
		roleManagerDAO.save(roleManager);
		// 保存中间表
		String sid[] = roleManager.getSid().split(",");
		for (int i = 0; i < sid.length; i++) {
			RoleMPermission rolePermission = new RoleMPermission();
			rolePermission.setSid(sid[i]);
			rolePermission.setRid(roleManager.getRid());
			rolePermission.setPid(roleManager.getPid());
			Assert.notNull(rolePermission);
			rolePermissionDAO.save(rolePermission);
		}
		if (roleManager.getRid() != null) {
			return new SuccessResponse();
		} else {
			return new FailedResponse("后台保存roleManager失败!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anycc.pmp.ptmt.service.RoleManagerService#delete(java.lang.Long)
	 */
	@Override
	public Response delete(String l) {
		// TODO Auto-generated method stub
		RoleManager roleManager = roleManagerDAO.findOne(l);
		String hqlString = "from RoleMPermission where pid='"
				+ roleManager.getPid() + "' and rid='" + roleManager.getRid()
				+ "'";
		List<RoleMPermission> list = rolePermissionTempAuxiliaryUtils
				.findBySearch(hqlString);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				rolePermissionDAO.delete(list.get(i));
			}
		}
		String hqlString1 = "from ProjectMember where pid='"
				+ roleManager.getPid() + "' and rid='" + roleManager.getRid()
				+ "'";
		List<ProjectMember> list1 = projectMemberTempAuxiliaryUtils
				.findBySearch(hqlString1);
		if (list1.size() > 0) {
			for (int i = 0; i < list1.size(); i++) {
				ProjectMember projectMember = projectMemberDAO.findOne(list1
						.get(i).getMid());
				projectMember.setRid(null);
				projectMemberDAO.save(projectMember);
			}
		}

		roleManagerDAO.delete(l);
		return new SuccessResponse();
	}

	@Override
	public Response selectMember(String id, String rid) {
		// TODO Auto-generated method stub
		Assert.notNull(id);
		ProjectMember projectMember = projectMemberDAO.findOne(id);
		Assert.notNull(rid);
		if (rid != "")
			projectMember.setRid(rid);
		projectMemberDAO.save(projectMember);
		return new SuccessResponse();
	}

	@Override
	public Response deleteLinkedUser(String mid) {
		// TODO Auto-generated method stub
		Assert.notNull(mid);
		ProjectMember projectMember = projectMemberDAO.findOne(mid);
		projectMember.setRid(null);
		projectMemberDAO.save(projectMember);
		return new SuccessResponse();
	}

	@Override
	public DTData<RoleProject> queryList(
			Specification<RoleProject> specification, DTPager pager, Long mid,
			String pid) {
		// TODO Auto-generated method stub
		StringBuffer hql = new StringBuffer();
		hql.append(" from RoleProject r");
		hql.append(" where r.delTag=0");
		if (!pid.equals("") && pid != null) {
			hql.append(" and r.name like '%" + pid + "%'");
		}
		hql.append(" and r.manager.id='" + mid + "' ");
		Page<RoleProject> roleProjectPage = tempAuxiliaryUtils.findBySearch(
				hql.toString(), tempAuxiliaryUtils.buildPageRequest(pager));
		String tags = "";
		for (int m = 0; m < roleProjectPage.getContent().size(); m++) {
			RoleProject roleProject = roleProjectPage.getContent().get(m);
			List<RoleManager> list = roleProject.getRoleList();
			for (int i = 0; i < list.size(); i++) {
				if (!"".equals(list.get(i).getRname())) {
					String rname = list.get(i).getRname();
					String dictName = dictionaryService.getNameByThemeAndValue(
							"项目角色", rname);
					tags += dictName + ",";
				}
			}
			if (!"".equals(tags)) {
				tags = tags.substring(0, tags.length() - 1);
			}
			roleProjectPage.getContent().get(m).setRnames(tags);
			;
			tags = "";
		}
		return new DTData<RoleProject>(roleProjectPage, pager);
	}

}
