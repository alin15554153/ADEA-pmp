package com.anycc.pmp.rsmt.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.anycc.commmon.web.entity.WebUser;
import com.anycc.common.dto.ObjectResponse;
import com.anycc.pmp.comm.service.SearchPersonService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.pmp.rsmt.entity.ResourceDown;
import com.anycc.pmp.rsmt.service.ResourceDownCheckService;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/management/pmp/rsmt/resourceDownCheck")
public class ResourceDownCheckController {

	@Autowired
	private SearchPersonService searchPersonService;
	@Autowired
	private ResourceDownCheckService resourceDownCheckService;

	private static final String LIST = "management/pmp/rsmt/resourceDownCheck/list";

	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));
	}

	/**
	 * 跳转到resourceDownCheck管理页面
	 *
	 * @return
	 */
	@RequiresPermissions("ResourceDownCheck:view")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String index() {
		return LIST;
	}

	/**
	 * 消息点击显示单个未阅读申请
	 * @param resourceDownId 资源申请ID
	 */
	@RequestMapping(value = "{id}/list", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView findOneApplication(@PathVariable("id") String resourceDownId, DTPager pager) {
		return new ModelAndView(LIST, "resourceDownId", resourceDownId);
	}

	/**
	 * 待审核列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "findTopSexList", method = RequestMethod.POST)
	@ResponseBody
	public DTData<ResourceDown> findTopSexList(HttpServletRequest request, ResourceDown resourceDown) {
		String myId = request.getParameter("myId");// 当前登录人ID
		String myRoleId = request.getParameter("myRoleId");// 当前登录人角色ID
		return new DTData<ResourceDown>(resourceDownCheckService.findTopSexList(resourceDown, myId, myRoleId));
	}

	/**
	 * 待审核列表
	 * 
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "findUncheckedApplicationList", method = RequestMethod.POST)
	@ResponseBody
	public DTData<ResourceDown> findUncheckedApplicationList(HttpServletRequest request, DTPager pager) {
		// Specification<ResourceDown> specification = DynamicSpecifications.bySearchFilter(request, ResourceDown.class);
		// return resourceDownCheckService.findUncheckedApplicationList(specification, pager);
		String myId = request.getParameter("myId");// 当前登录人ID
		String myRoleId = request.getParameter("myRoleId");// 当前登录人角色ID
		String resourceType = request.getParameter("resourceType");// 资源类型
		String resourceName = request.getParameter("resourceName");// 资源名称
		String projectStage = request.getParameter("projectStage");// 阶段
		String projectName = request.getParameter("projectName");//项目名称
		String resourceDownId = request.getParameter("resourceDownId");//申请ID
		ResourceDown resourceDown = new ResourceDown();
		if (!"".equals(resourceName)) {
			resourceDown.setResourceName(resourceName);
		}
		if (!"".equals(resourceType)) {
			resourceDown.setResourceType(resourceType);
		}
		if (!"".equals(projectStage)) {
			resourceDown.setStageId(projectStage);
		}
		if (!"".equals(projectName)) {
			resourceDown.setProjectName(projectName);
		}
		if (!"".equals(resourceDownId)) {
			resourceDown.setId(resourceDownId);
		}
		return resourceDownCheckService.findUncheckedApplicationListBySql(resourceDown, myId, myRoleId, pager);
	}

	/**
	 * 审核资源下载申请
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "checkApplication", method = RequestMethod.POST)
	@ResponseBody
	public Response checkApplication(HttpServletRequest request) {
		String id = request.getParameter("id");
		String sid = request.getParameter("sid");
		String status = request.getParameter("status");
		String remark = request.getParameter("remark");
		return resourceDownCheckService.checkApplication(id, Integer.parseInt(sid), status, remark);
	}

	/**
	 * 查看过往审核记录
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "showPreRemark", method = RequestMethod.POST)
	@ResponseBody
	public Response showPreRemark(HttpServletRequest request) {
		String id = request.getParameter("id");
		return resourceDownCheckService.showPreRemark(id);
	}

	/**
	 * 根据机构ID查找该机构所属区域的区域管理员
	 */
	@RequestMapping(value = "findAreaAdmins", method = RequestMethod.POST)
	@ResponseBody
	public Response findAreaAdmins(HttpServletRequest request) {
		String orgId = request.getParameter("orgId");//该机构iD为项目信息的机构ID，不能根据当前人所属机构获取
		List<WebUser> list=searchPersonService.findAreaAdmins(Long.parseLong(orgId));//查出所有该地区管理员
		return new ObjectResponse<List<WebUser>>(list);
	}

	/**
	 * 查找所有超级管理员
	 */
	@RequestMapping(value = "findSuperAdmins", method = RequestMethod.POST)
	@ResponseBody
	public Response findSuperAdmins() {
		List<WebUser> list = searchPersonService.findSuperAdmins();//查找所有超级管理员
		return new ObjectResponse<List<WebUser>>(list);
	}
}
