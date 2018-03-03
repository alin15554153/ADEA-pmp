
package com.anycc.pmp.ptmt.service;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.pmp.ptmt.entity.ProjectChangeLog;
import com.anycc.pmp.ptmt.entity.ProjectStage;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProjectChangeLogService {
		
	DTData<ProjectChangeLog> queryByPaging(ProjectChangeLog projectChangeLog, DTPager pager);

	DTData<ProjectChangeLog> queryByPaging(Specification specification, DTPager pager);

	Response addProjectChangeLog(ProjectChangeLog projectChangeLog);

	Response updateProjectChangeLog(ProjectChangeLog projectChangeLog);

	Response queryById(String id);

	Response delete(String l);

	Response initStageByProject(String id);
}
