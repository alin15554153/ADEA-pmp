package com.anycc.pmp.rsmt.service;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectStage;
import com.anycc.pmp.rsmt.entity.Resource;
import com.anycc.pmp.rsmt.entity.ResourceDown;
import com.anycc.pmp.rsmt.entity.ResourceDownCheck;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface ResourceDownService {
		
	//DTData<ResourceDown> queryByPaging(ResourceDown resourceDown, DTPager pager);

	DTData<Resource> findResourceList(Specification specification, DTPager pager);
	DTData<Resource> findResourceListBySql(ResourceDown resourceDown, DTPager pager);
	List<Object> getMyProjectIds(String userid);
	String getMyAreaId(String userId);
	Response sendApplication(String rid, int uid, String remark, int processType);

	DTData<ResourceDown> findApplicationList(Specification<ResourceDown> specification, DTPager pager);
	ResourceDownCheck findCheckById(String id);

	Response reSendApplication(String id, String remark);

	Response addDownloadTimes(String rid);

	List<ProjectStage> findProjectStageList();
	ResponseEntity<byte[]> exportResourceList(HttpServletRequest request) throws IOException;

	Project findProjectByResourceId(String rid);
}
