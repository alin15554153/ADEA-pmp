package com.anycc.pmp.comm.service.impl;

import com.anycc.commmon.web.dao.WebUserDAO;
import com.anycc.commmon.web.entity.WebUser;
import com.anycc.pmp.comm.service.SearchPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchPersonServiceImpl implements SearchPersonService {

	@Autowired
	private WebUserDAO webUserDAO;
	@PersistenceContext
	private EntityManager em;

	@Override
	//根据项目编号查出所有参与人员列表
	public List<WebUser> findProjectMembers(String projectId){
		String sql = "select distinct pm.uid from projectmember pm where pm.pid = '"+projectId+"'";
		Query query = null;
		List<Object> uidList = new ArrayList<Object>();
		List<WebUser> userList = new ArrayList<WebUser>();
		try {
			query = em.createNativeQuery(sql);
			uidList = query.getResultList();
			for (Object o : uidList) {
				WebUser webUser = webUserDAO.findOne(Long.parseLong(o.toString()));
				userList.add(webUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	//根据项目编号、角色编号查出对应人员列表
	public List<WebUser> findProjectMembers(String projectId, long roleId){
		String sql = "select distinct pm.uid from projectmember pm where pm.pid = '"+projectId+"' and pm.rid = "+roleId;
		Query query = null;
		List<Object> uidList = new ArrayList<Object>();
		List<WebUser> userList = new ArrayList<WebUser>();
		try {
			query = em.createNativeQuery(sql);
			uidList = query.getResultList();
			for (Object o : uidList) {
				WebUser webUser = webUserDAO.findOne(Long.parseLong(o.toString()));
				userList.add(webUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	//根据当前人机构查出角色为地区管理员的人员列表
	public List<WebUser> findAreaAdmins(long orgId){
		String sql = "SELECT distinct su.ID FROM sys_user su, sys_user_role sur WHERE su.ID = sur.user_id AND sur.role_id = '14003' AND su.ORGANIZATION_ID IN ( SELECT so.id FROM sys_organization so WHERE so.area_id = (SELECT so.area_id FROM sys_area sa, sys_organization so WHERE sa.id = so.area_id AND so.id = "+orgId+"))";
		Query query = null;
		List<Object> uidList = new ArrayList<Object>();
		List<WebUser> userList = new ArrayList<WebUser>();
		try {
			query = em.createNativeQuery(sql);
			uidList = query.getResultList();
			for (Object o : uidList) {
				WebUser webUser = webUserDAO.findOne(Long.parseLong(o.toString()));
				userList.add(webUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	//查出角色为集团管理员的人员列表
	public List<WebUser> findSuperAdmins(){
		String sql = "SELECT distinct su.ID FROM sys_user su, sys_user_role sur WHERE su.ID = sur.user_id AND sur.role_id = '14004'";
		Query query = null;
		List<Object> uidList = new ArrayList<Object>();
		List<WebUser> userList = new ArrayList<WebUser>();
		try {
			query = em.createNativeQuery(sql);
			uidList = query.getResultList();
			for (Object o : uidList) {
				WebUser webUser = webUserDAO.findOne(Long.parseLong(o.toString()));
				userList.add(webUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}
}
