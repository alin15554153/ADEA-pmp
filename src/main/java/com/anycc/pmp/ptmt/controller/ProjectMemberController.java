package com.anycc.pmp.ptmt.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.anycc.pmp.ptmt.dao.ProjectMemberDao;
import com.anycc.pmp.ptmt.entity.ProjectMember;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.anycc.pmp.ptmt.service.ProjectMemberService;

/**
 * @author luqing
 * @Description: ProjectMember类
 * @date 2016年03月16日 上午16:04:11
 */
@Controller
@RequestMapping("/management/pmp/comm/projectMember")
public class ProjectMemberController {
	@Autowired
	private ProjectMemberService projectMemberService;
	@Autowired
	private ProjectMemberDao projectMemberDao;
	private static final String ADDDEMO = "management/index/addTempletTable";
	private static final String ADDIFRAMEDEMO = "management/index/addIframeTempletTable";

	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df,
				true));

	}

	/**
	 * 查询demos
	 * 
	 * @param request
	 * @param pager
	 * @return
	 */
	/*
	 * @RequiresPermissions("TEMPDEMO:view")
	 * 
	 * @RequestMapping(value = "queryByPaging", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public DTData<ProjectMember> queryByPaging(ServletRequest
	 * request, DTPager pager) { Specification<ProjectMember> specification =
	 * DynamicSpecifications .bySearchFilter(request, ProjectMember.class);
	 * return projectMemberService.queryByPaging(specification, pager); }
	 */

	/**
	 * 跳转到添加页面
	 * 
	 * @return
	 */
	@RequiresPermissions("TEMPDEMO:view")
	@RequestMapping(value = "toAddDemo", method = RequestMethod.GET)
	public String toAddDemo() {
		return ADDDEMO;
	}

	/**
	 * 跳转到添加页面
	 * 
	 * @return
	 */
	@RequiresPermissions("TEMPDEMO:view")
	@RequestMapping(value = "toAddIframeDemo", method = RequestMethod.GET)
	public String toAddIframeDemo() {
		return ADDIFRAMEDEMO;
	}

	/**
	 * 根据项目id查人员
	 * @return
	 */
	@RequiresPermissions("Project:edit")
	@RequestMapping(value = "{pid}/findByPid", method = RequestMethod.POST)
	@ResponseBody
	public List<ProjectMember> findByPid(@PathVariable("pid") String pid){
		return projectMemberDao.findByPid(pid);
	}
}
