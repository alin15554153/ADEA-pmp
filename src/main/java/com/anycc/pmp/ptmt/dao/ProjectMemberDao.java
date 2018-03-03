package com.anycc.pmp.ptmt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.anycc.pmp.ptmt.entity.ProjectMember;

public interface ProjectMemberDao extends JpaRepository<ProjectMember, String>,
		JpaSpecificationExecutor<ProjectMember> {
	List<ProjectMember> findByPid(String pid);
	
	//@Query("select new ProjectMember(p.webUser)  from ProjectMember p , RoleManager r , RoleMPermission m where p.rid=r.rid and r.rid = m.rid and m.sid =?")
	@Query("select p.webUser from ProjectMember p , RoleManager r , RoleMPermission m where p.rid=r.rid and r.rid = m.rid and m.sid =?")
	List<ProjectMember> findBySid(String sid);
}
