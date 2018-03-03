
package com.anycc.pmp.ptmt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.anycc.pmp.ptmt.entity.ProjectStage;
import com.anycc.pmp.ptmt.entity.RoleMPermission;

public interface ProjectStageDAO extends JpaRepository<ProjectStage, String>, JpaSpecificationExecutor<ProjectStage> {
	
	List<ProjectStage> findByPid(String pid);
	
	@Query("select a.projectStage from RoleMPermission a, RoleManager b, ProjectMember c where a.rid=b.id and b.id=c.rid and c.pid=?1 and c.uid=?2 ")
	List<ProjectStage> findSidByUser(String id,String uid);
}