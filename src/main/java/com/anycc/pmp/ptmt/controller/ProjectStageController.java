
package com.anycc.pmp.ptmt.controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.common.dto.SuccessResponse;
import com.anycc.log.Log;
import com.anycc.log.LogMessageObject;
import com.anycc.log.impl.LogUitls;
import com.anycc.util.persistence.DynamicSpecifications;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectStage;
import com.anycc.pmp.ptmt.service.ProjectService;
import com.anycc.pmp.ptmt.service.ProjectStageService;

import javax.servlet.ServletRequest;

@Controller
@RequestMapping("/management/pmp/ptmt/projectstage")
public class ProjectStageController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ProjectStageService projectstageService;
	
	private static final String LIST = "/management/pmp/ptmt/projectstage/List";

	
	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, 
				new CustomDateEditor(df, true));
	}
	
	
	/**
	 * 跳转到project管理页面
	 * 
	 * @return
	 */
	@RequiresPermissions("ProjectStage:view")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String index() {
		return LIST;
	}

	/**
	 * 分页查询ProjectStage列表
	 *
	 * @return
	 */
	@RequiresPermissions("ProjectStage:view")
	@RequestMapping(value = "queryByPaging", method = RequestMethod.POST)
	@ResponseBody
	public DTData<ProjectStage> queryByPaging(ServletRequest request, DTPager pager) {
		Specification<ProjectStage> specification = DynamicSpecifications.bySearchFilter(request, ProjectStage.class);
		return projectstageService.queryByPaging(specification, pager);

	}
	
	/**
	 * 添加ProjectStage
	 * 
	 * @param ProjectStage
	 * @return
	 */
	@Log(message="添加了id={0}的ProjectStage。")
	@RequiresPermissions("ProjectStage:save")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Response add(ProjectStage projectstage) {
		projectstageService.addProjectStage(projectstage);
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{projectstage.getSid()}));
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
	 * 更新ProjectStage
	 * 
	 * @param ProjectStage
	 * @return
	 */
	@Log(message="修改了id={0}的ProjectStage的信息。")
	@RequiresPermissions("ProjectStage:edit")
	@RequestMapping(value = "{id}/update", method = RequestMethod.POST)
	@ResponseBody
	public Response update(ProjectStage projectstage) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{projectstage.getSid()}));
		return projectstageService.updateProjectStage(projectstage);
	}
	
	/**
	 * 删除ProjectStage
	 * 
	 * @param id
	 * @return
	 */
	@Log(message="删除了id={0}的ProjectStage。")
	@RequiresPermissions("ProjectStage:delete")
	@RequestMapping(value="{id}/delete", method=RequestMethod.POST)
	@ResponseBody
	public Response delete(@PathVariable String id) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{id}));
		return projectstageService.delete(id);
	}

	/**
	 * 批量删除ProjectStage
	 * 
	 * @param idArray
	 * @return
	 */
	@Log(message="批量删除了id={0}ProjectStage。")
	@RequiresPermissions("ProjectStage:delete")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Response delete(@RequestBody String[] idArray) {
		for(String id : idArray){
			projectstageService.delete(id);
		}
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{Arrays.toString(idArray)}));
		return new SuccessResponse();
		
	}

}
