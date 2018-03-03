package com.anycc.pmp.ptmt.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.pmp.ptmt.entity.ProjectStage;
import com.anycc.pmp.rsmt.entity.Resource;

public interface ProjectStageService {

	DTData<ProjectStage> queryByPaging(Specification specification,
			DTPager pager);

	DTData<ProjectStage> queryByPagingWithPid(Specification specification,
			DTPager pager, String pid);
	
	DTData<Resource> queryByPagingWithStageId(Specification specification,
			DTPager pager, String pid,String stageid);

	Response addProjectStage(ProjectStage projectstage);

	Response updateProjectStage(ProjectStage projectstage);

	Response queryById(String id);

	Response delete(String l);

	// 查询projectstage List luqing(已使用，后续方法勿改)
	List<ProjectStage> findByPid(String pid);
	
	List<ProjectStage> findTabByPid(String pid);
	
	List<ProjectStage> findSidByUser(String id,String uid);

}
