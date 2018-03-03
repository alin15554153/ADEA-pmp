
package com.anycc.pmp.ptmt.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.anycc.pmp.ptmt.entity.ProjectMission;

public interface ProjectMissionDAO extends JpaRepository<ProjectMission, String>, JpaSpecificationExecutor<ProjectMission> {
	
}