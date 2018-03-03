
package com.anycc.pmp.ptmt.service;

import java.util.List;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.pmp.ptmt.entity.ProMissionMember;
import com.anycc.pmp.ptmt.entity.ProjectMember;
import com.anycc.pmp.ptmt.entity.ProjectMission;

import org.springframework.data.jpa.domain.Specification;

public interface ProjectMissionService {
		
	//DTData<ProjectMission> queryByPaging(ProjectMission projectMission, DTPager pager);

	DTData<ProjectMission> queryByPaging(Specification specification, DTPager pager);
	
	Response addProjectMission(ProjectMission resource);

	Response updateProjectMission(ProjectMission resource);

	Response queryById(String id);

	Response delete(String l);
	
	List<ProjectMember> findBySid(String sid);
		
}
