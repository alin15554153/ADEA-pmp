package com.anycc.pmp.ptmt.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.common.dto.SuccessResponse;
import com.anycc.log.Log;
import com.anycc.log.LogMessageObject;
import com.anycc.log.impl.LogUitls;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectMember;
import com.anycc.pmp.ptmt.entity.ProjectStage;
import com.anycc.pmp.ptmt.entity.RoleManager;
import com.anycc.pmp.ptmt.entity.RoleProject;
import com.anycc.pmp.ptmt.service.ProjectMemberService;
import com.anycc.pmp.ptmt.service.ProjectService;
import com.anycc.pmp.ptmt.service.ProjectStageService;
import com.anycc.pmp.ptmt.service.RoleManagerService;
import com.anycc.util.persistence.DynamicSpecifications;

@Controller
@RequestMapping("/management/pmp/ptmt/project/rolemanager")
public class RoleManagerController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectStageService projectstageService;

	@Autowired
	private RoleManagerService roleManagerService;

	@Autowired
	private ProjectMemberService projectMemberService;
	private static final String LIST = "/management/pmp/ptmt/project/rolemanager/list";
	private static final String SECLIST = "/management/pmp/ptmt/project/rolemanager/secList";

	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df,
				true));
	}

	/**
	 * 跳转到RoleManager管理页面
	 * 
	 * @return
	 */
	@RequiresPermissions("RoleManager:view")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String index() {
		return LIST;
	}

	/**
	 * 跳转到RoleManager查看详情页面
	 * 
	 * @return
	 */
	/**
	 * @return
	 */
	@RequiresPermissions("RoleManager:view")
	@RequestMapping(value = "{pid}/secList", method = RequestMethod.GET)
	public String secList(Map<String, Object> map,
			@PathVariable("pid") String pid) {
		map.put("pid", pid);
		return SECLIST;
	}

	/**
	 * 分页查询RoleManager列表
	 *
	 * @return
	 */
	@RequiresPermissions("RoleManager:view")
	@RequestMapping(value = "queryList", method = RequestMethod.POST)
	@ResponseBody
	public DTData<RoleProject> queryList(ServletRequest request, DTPager pager) {
		Specification<RoleProject> specification = DynamicSpecifications
				.bySearchFilter(request, RoleProject.class);
		String midStr=request.getParameter("managerId");
		long mid=0;
		if(midStr!=null)
			mid=Long.parseLong(midStr);
		String pid=request.getParameter("pname");
		return roleManagerService.queryList(specification, pager,mid,pid);
	}

	/**
	 * 分页查询RoleManager列表
	 *
	 * @return
	 */
	@RequiresPermissions("RoleManager:view")
	@RequestMapping(value = "queryByPaging", method = RequestMethod.POST)
	@ResponseBody
	public DTData<RoleManager> queryByPaging(ServletRequest request,
			DTPager pager) {
		Specification<RoleManager> specification = DynamicSpecifications
				.bySearchFilter(request, RoleManager.class);
		return roleManagerService.queryByPaging(specification, pager);

	}

	/*	*//**
	 * 分页查询WebUser列表
	 *
	 * @return
	 */
	/*
	 * @RequiresPermissions("RoleManager:view")
	 * 
	 * @RequestMapping(value = "query", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public DTData<ProjectMember>
	 * userQueryByPaging(ServletRequest request, DTPager pager) {
	 * Specification<ProjectMember> specification = DynamicSpecifications
	 * .bySearchFilter(request, ProjectMember.class); return
	 * projectMemberService.queryByPaging(specification, pager);
	 * 
	 * }
	 */

	/**
	 * 分页查询WebUser列表
	 *
	 * @return
	 */
	@RequiresPermissions("RoleManager:view")
	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ResponseBody
	public DTData<ProjectMember> query(ProjectMember projectMember,
			DTPager pager) {
		return projectMemberService.queryByPaging(projectMember, pager);

	}

	/**
	 * 分页查询WebUser列表
	 *
	 * @return
	 */
	@RequiresPermissions("RoleManager:view")
	@RequestMapping(value = "userQueryByPaging2", method = RequestMethod.POST)
	@ResponseBody
	public DTData<ProjectMember> userQueryByPaging2(
			ProjectMember projectMember, DTPager pager) {
		return projectMemberService.queryByPaging2(projectMember, pager);

	}

	/**
	 * 查询project List luqing(已使用，后续方法勿改)
	 * 
	 * @return
	 */
	@RequiresPermissions("ProjectStage:view")
	@RequestMapping(value = "{myId}/{pid}/queryProjectList2", method = RequestMethod.POST)
	@ResponseBody
	public List<Project> queryProjectList(@PathVariable("myId") String myId,@PathVariable("pid") String pid) {
		return projectService.queryProjectList2(myId,pid);
	}

	/**
	 * 查询project List luqing(已使用，后续方法勿改)
	 * 
	 * @return
	 */
	@RequiresPermissions("ProjectStage:view")
	@RequestMapping(value = "{pid}/findByPid", method = RequestMethod.POST)
	@ResponseBody
	public List<ProjectStage> findByPid(@PathVariable("pid") String pid) {
		return projectstageService.findByPid(pid);
	}

	/**
	 * 添加RoleManager
	 * 
	 * @param RoleManager
	 * @return
	 */
	@Log(message = "添加了id={0}的角色。")
	@RequiresPermissions("RoleManager:save")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Response add(RoleManager roleManager) {
		roleManagerService.addRoleManager(roleManager);
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(
				new Object[] { roleManager.getRid() }));
		return new SuccessResponse();
	}

	/**
	 * 根据id查询信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("RoleManager:view")
	@RequestMapping(value = "{id}/view", method = RequestMethod.GET)
	@ResponseBody
	public Response queryById(@PathVariable("id") String id) {
		return roleManagerService.queryById(id);
	}

	/**
	 * 选择角色成员
	 * 
	 * @param id
	 * @param rid
	 * @return
	 */
	@RequiresPermissions("RoleManager:view")
	@RequestMapping(value = "{id}/{rid}/selectMember", method = RequestMethod.GET)
	@ResponseBody
	public Response queryById(@PathVariable("id") String id,
			@PathVariable("rid") String rid) {
		return roleManagerService.selectMember(id, rid);
	}

	/**
	 * 删除成员和角色关系
	 * 
	 * @param mid
	 * @return
	 */
	@RequiresPermissions("RoleManager:view")
	@RequestMapping(value = "{mid}/deleteLinkedUser", method = RequestMethod.GET)
	@ResponseBody
	public Response deleteLinkedUser(@PathVariable("mid") String mid) {
		return roleManagerService.deleteLinkedUser(mid);
	}

	/**
	 * 更新roleManager
	 * 
	 * @param roleManager
	 * @return
	 */
	@Log(message = "修改了id={0}的角色。")
	@RequiresPermissions("RoleManager:edit")
	@RequestMapping(value = "{id}/update", method = RequestMethod.POST)
	@ResponseBody
	public Response update(RoleManager roleManager) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(
				new Object[] { roleManager.getRid() }));
		return roleManagerService.updateRoleManager(roleManager);
	}

	/**
	 * 删除roleManager
	 * 
	 * @param id
	 * @return
	 */
	@Log(message = "删除了id={0}的角色。")
	@RequiresPermissions("RoleManager:delete")
	@RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
	@ResponseBody
	public Response delete(@PathVariable String id) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(
				new Object[] { id }));
		return roleManagerService.delete(id);
	}

	/**
	 * 批量删除roleManager
	 * 
	 * @param idArray
	 * @return
	 */
	@Log(message = "批量删除了id={0}的角色。")
	@RequiresPermissions("RoleManager:delete")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Response delete(@RequestBody String[] idArray) {
		for (String id : idArray) {
			roleManagerService.delete(id);
		}
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(
				new Object[] { Arrays.toString(idArray) }));
		return new SuccessResponse();

	}

}
