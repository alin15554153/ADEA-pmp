package com.anycc.pmp.ptmt.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anycc.commmon.web.entity.WebUser;
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
import com.anycc.common.dto.FailedResponse;
import com.anycc.common.dto.ObjectResponse;
import com.anycc.common.dto.Response;
import com.anycc.common.dto.SuccessResponse;
import com.anycc.entity.main.User;
import com.anycc.log.Log;
import com.anycc.log.LogMessageObject;
import com.anycc.log.impl.LogUitls;
import com.anycc.pmp.ptmt.dao.ProjectStageDAO;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectStage;
import com.anycc.pmp.ptmt.service.ProjectService;
import com.anycc.pmp.ptmt.service.ProjectStageService;
import com.anycc.pmp.rsmt.entity.Resource;
import com.anycc.pmp.rsmt.service.ResourceService;
import com.anycc.service.DictionaryService;
import com.anycc.shiro.ShiroUser;
import com.anycc.util.persistence.DynamicSpecifications;
import com.anycc.utils.SecurityUtils;

@Controller
@RequestMapping("/management/pmp/ptmt/project")
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private ProjectStageDAO projectstageDAO;
	@Autowired
	private ProjectStageService projectstageService;
	@Autowired
	private DictionaryService dictionaryService;
	
	
	private static final String LIST = "/management/pmp/ptmt/project/projectList";
	
	private static final String ADDLIST = "/management/pmp/ptmt/project/addList";
	
	private static final String EDITLIST = "/management/pmp/ptmt/project/editList";

	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df,
				true));
	}

	/**
	 * 查询project List luqing(已使用，后续方法勿改)
	 * 
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "queryProjectList", method = RequestMethod.POST)
	@ResponseBody
	public List<Project> queryProjectList() {
		return projectService.queryProjectList();
	}

	/**
	 * 跳转到project管理页面
	 * 
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String index() {
		return LIST;
	}

	/**
	 * 跳转到新增页面
	 * 
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "addinfoTab", method = RequestMethod.GET)
	public String addindex() {
		return ADDLIST;
	}
	
	/**
	 * 跳转到编辑页面
	 * 
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "{id}/editinfoTab", method = RequestMethod.GET)
	public String editindex(@PathVariable("id") String id, Map<String, Object> map) {
		map.put("proid", id);
		return EDITLIST;
	}
	
	/**
	 * 查询projectstage 
	 * 
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "queryByPagingWithPid", method = RequestMethod.POST)
	@ResponseBody
	public DTData<ProjectStage> queryByPagingWithPid(ServletRequest request, DTPager pager) {
		Specification<ProjectStage> specification = DynamicSpecifications.bySearchFilter(request, ProjectStage.class);
		return projectstageService.queryByPaging(specification, pager);
	}	
	
	/**
	 * 查询resource 
	 * 
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "{pid}/{stageid}/queryByPagingWithStageId", method = RequestMethod.GET)
	@ResponseBody
	public DTData<Resource> queryByPagingWithStageId(ServletRequest request, DTPager pager,@PathVariable("pid") String pid,@PathVariable("stageid") String stageid) {
		Specification<Resource> specification = DynamicSpecifications.bySearchFilter(request, Resource.class);
		return projectstageService.queryByPagingWithStageId(specification, pager,pid,stageid);
	}
	
	
	/**
	 * 分页查询projectstage列表
	 *
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "queryuserlist", method = RequestMethod.POST)
	@ResponseBody
	public DTData<WebUser> queryuserlist(ServletRequest request, DTPager pager) {
		Specification<WebUser> specification = DynamicSpecifications
				.bySearchFilter(request, WebUser.class);
		return projectService.queryuserlist(specification, pager);
	}
	
	/**
	 * 分页查询project列表
	 *
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "queryByPaging", method = RequestMethod.POST)
	@ResponseBody
	public DTData<Project> queryByPaging(ServletRequest request, DTPager pager) {
		Specification<Project> specification = DynamicSpecifications
				.bySearchFilter(request, Project.class);
		return projectService.queryByPaging(specification, pager);
	}

	/**
	 * 添加project
	 * 
	 * @param project
	 * @return
	 */
	@Log(message = "添加了名称为{0}的项目。")
	@RequiresPermissions("Project:save")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Response add(Project project) {
		/*ShiroUser shiroUser = SecurityUtils.getShiroUser();
		project.setCid(shiroUser.getUser().getId());
		project.setCompany(shiroUser.getUser().getOrganization().getId());
		project.setArea(shiroUser.getUser().getOrganization().getId());*/
		/*ProjectStage stage = new ProjectStage();
		project.setProjectstage(stage);*/
		projectService.addProject(project);
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(
				new Object[] { project.getName() }));
		return new SuccessResponse();
	}
	/**
	 * 添加projectstage
	 * 
	 * @param projectstage
	 * @return
	 */
	@Log(message="添加了项目阶段{0}。")
	@RequiresPermissions("Project:save")
	@RequestMapping(value = "addstage", method = RequestMethod.POST)
	@ResponseBody
	public Response addstage(ProjectStage projectstage) {
		String sname = projectstage.getSname();//阶段字典编号
		Integer sseq = projectstage.getSseq();//阶段序号
		String pid = projectstage.getPid();
		//查出名称 给日志用
		String dictnameString = dictionaryService.getNameByThemeAndValue("阶段名称", sname);
		List<ProjectStage> list = projectstageDAO.findByPid(pid);
		for(ProjectStage savedStage : list ){
			if(sname.equals(savedStage.getSname())){
				return new FailedResponse("阶段名称重复，请重新选择阶段名称!");
			}
			if(sseq == savedStage.getSseq() ){
				return new FailedResponse("阶段序号重复，请重新选择阶段序号!");
			}
		}
		projectstageService.addProjectStage(projectstage);
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{dictnameString}));
		return new SuccessResponse();
	}
	
	/**
	 * 添加projectresource
	 * 
	 * @param projectresource
	 * @return
	 */
	@Log(message="添加了名称为{0}的项目资源。")
	@RequiresPermissions("Project:save")
	@RequestMapping(value = "addresource", method = RequestMethod.POST)
	@ResponseBody
	public Response addresource(Resource resource) {
		ShiroUser shiroUser = SecurityUtils.getShiroUser();
		resource.setUid(Integer.parseInt(shiroUser.getUser().getId().toString()));
		resource.setBelong(Integer.parseInt(shiroUser.getUser().getOrganization().getId().toString()));
		resource.setStatus("0");
		resource.setTimes(0);
		resource.setUploaddate(new Date());
		resourceService.addResource(resource);
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{resource.getName()}));
		return new SuccessResponse();
	}
	
	
	/**
	 * 根据id查询信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "{id}/view", method = RequestMethod.GET)
	@ResponseBody
	public Response queryById(@PathVariable("id") String id) {
		return projectService.queryById(id);
	}
	
	/**
	 * 根据id查询prostage信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "{id}/viewstage", method = RequestMethod.GET)
	@ResponseBody
	public Response stagequeryById(@PathVariable("id") String id) {
		return projectstageService.queryById(id);
	}
	
	/**
	 * 根据id查询resource信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "{id}/viewresource", method = RequestMethod.GET)
	@ResponseBody
	public Response resourcequeryById(@PathVariable("id") String id) {
		return resourceService.queryById(id);
	}
	
	/**
	 * 根据pid查询stagetab信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "{id}/querytab", method = RequestMethod.GET)
	@ResponseBody
	public List<ProjectStage> querytab(@PathVariable("id") String id) {
		return projectstageService.findByPid(id);
	}
	
	/**
	 * 根据pid查询stagetab信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "{id}/querytablimit", method = RequestMethod.GET)
	@ResponseBody
	public List<ProjectStage> querytablimit(@PathVariable("id") String id) {
		ShiroUser shiroUser = SecurityUtils.getShiroUser();
		String uid = shiroUser.getUser().getId().toString();
		return projectstageService.findSidByUser(id,uid);
	}

	/**
	 * 更新project
	 * 
	 * @param project
	 * @return
	 */
	@Log(message = "修改了名称为{0}的项目的信息。")
	@RequiresPermissions("Project:edit")
	@RequestMapping(value = "{id}/update", method = RequestMethod.POST)
	@ResponseBody
	public Response update(Project project) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(
				new Object[] { project.getName() }));
		return projectService.updateProject(project);
	}
	
	/**
	 * 更新projectstage
	 * 
	 * @param projectstage
	 * @return
	 */
	@Log(message = "修改了id={0}的项目阶段信息。")
	@RequiresPermissions("Project:edit")
	@RequestMapping(value = "{id}/updatestage", method = RequestMethod.POST)
	@ResponseBody
	public Response updatestage(ProjectStage projectstage) {
		ProjectStage oldprojectstage = projectstageDAO.findOne(projectstage.getSid());
		String sname = projectstage.getSname();//阶段字典编号
		Integer sseq = projectstage.getSseq();//阶段序号
		String pid = projectstage.getPid();
		//查出名称 给日志用
		String dictnameString = dictionaryService.getNameByThemeAndValue("阶段名称", sname);
		List<ProjectStage> list = projectstageDAO.findByPid(pid);
		for(ProjectStage savedStage : list ){
			if(sname.equals(savedStage.getSname()) && !sname.equals(oldprojectstage.getSname())){
				return new FailedResponse("阶段名称重复，请重新选择阶段名称!");
			}
			if(sseq == savedStage.getSseq() && sseq != oldprojectstage.getSseq() ){
				return new FailedResponse("阶段序号重复，请重新选择阶段序号!");
			}
		}
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(
				new Object[] { dictnameString }));
		return projectstageService.updateProjectStage(projectstage);
	}
	
	/**
	 * 更新projectresource
	 * 
	 * @param projectresource
	 * @return
	 */
	@Log(message = "修改了名称为{0}的资源的信息。")
	@RequiresPermissions("Project:edit")
	@RequestMapping(value = "{id}/updateresource", method = RequestMethod.POST)
	@ResponseBody
	public Response updateresource(Resource resource) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(
				new Object[] { resource.getName() }));
		ShiroUser shiroUser = SecurityUtils.getShiroUser();
		resource.setUid(Integer.parseInt(shiroUser.getUser().getId().toString()));
		resource.setBelong(Integer.parseInt(shiroUser.getUser().getOrganization().getId().toString()));
		resource.setStatus("1");
		resource.setUploaddate(new Date());
		return resourceService.updateResource(resource);
	}

	/**
	 * 删除project
	 * 
	 * @param id
	 * @return
	 */
	@Log(message = "删除了名称为{0}的项目。")
	@RequiresPermissions("Project:delete")
	@RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
	@ResponseBody
	public Response delete(@PathVariable String id) {
		Project project = projectService.get(id);
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(
				new Object[] { project.getName() }));
		return projectService.delete(id);
	}
	
	/**
	 * 删除resource
	 * 
	 * @param id
	 * @return
	 */
	@Log(message = "删除了名称为{0}项目资源。")
	@RequiresPermissions("Project:delete")
	@RequestMapping(value = "{id}/deleteresource", method = RequestMethod.POST)
	@ResponseBody
	public Response deleteresource(@PathVariable String id) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(
				new Object[] {id}));
		return resourceService.delete(id);
	}

	/**
	 * 批量删除project
	 * 
	 * @param idArray
	 * @return
	 */
	@Log(message = "批量删除了{0}项目。")
	@RequiresPermissions("Project:delete")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Response delete(@RequestBody String[] idArray) {
		String projectnames = "";
		for (String id : idArray) {
			projectnames += projectService.get(id).getName()+",";
			projectService.delete(id);
		}
		if(!"".equals(projectnames)){
			projectnames = projectnames.substring(0, projectnames.length()-1);
		}
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(
				new Object[] { projectnames }));
		return new SuccessResponse();

	}
	
	/**
	 * 根据当前用户角色获取项目，for 项目查看
	 */
//	@RequestMapping(value = "getMyRoleProject", method = RequestMethod.POST)
//	@ResponseBody
//	public Response getMyRoleProject(HttpServletRequest request){
//		String userid = request.getParameter("userid");
//		List<Object> list = projectService.getMyRoleProject(userid);
//		return new ObjectResponse<List<Object>>(list);
//	}

	/**
	 * 导出项目的 excel列表
	 *
	 * @return
	 */
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void export(ServletRequest request, HttpServletResponse response) {
		projectService.export(request,response);
	}

}
