package com.anycc.pmp.ptmt.service;

import java.util.List;

import com.anycc.commmon.web.entity.WebUser;
import org.springframework.data.jpa.domain.Specification;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.pmp.ptmt.entity.Project;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ProjectService {

	DTData<Project> queryByPaging(Specification specification, DTPager pager);

	DTData<WebUser> queryuserlist(Specification specification, DTPager pager);

	Response addProject(Project project);

	Response updateProject(Project project);

	Response queryById(String id);

	Response delete(String l);

	List<Project> queryProjectList();
	
	Project get(String id);

	// 查询project List luqing(已使用，后续方法勿改)
	List<Project> queryProjectList2(String myId,String pid);

    void export(ServletRequest request,HttpServletResponse response);
}
