
package com.anycc.pmp.ptmt.controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.FailedResponse;
import com.anycc.common.dto.ObjectResponse;
import com.anycc.common.dto.Response;
import com.anycc.common.dto.SuccessResponse;
import com.anycc.log.Log;
import com.anycc.log.LogMessageObject;
import com.anycc.log.impl.LogUitls;
import com.anycc.pmp.ptmt.entity.ProMissionMember;
import com.anycc.pmp.ptmt.entity.ProjectMember;
import com.anycc.pmp.ptmt.entity.ProjectMission;
import com.anycc.pmp.ptmt.service.ProjectMissionService;
import com.anycc.util.persistence.DynamicSpecifications;
import com.anycc.utils.FileUtils;


@Controller
@RequestMapping("/management/pmp/ptmt/mission")
public class ProjectMissionController {

	@Autowired
	private ProjectMissionService projectMissionService;

	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, 
				new CustomDateEditor(df, true));
	}
	
	/**
	 * 分页查询mission列表
	 * 
	 * @return
	 */
	@RequiresPermissions("Project:view")
	@RequestMapping(value = "queryByPaging", method = RequestMethod.POST)
	@ResponseBody
	public DTData<ProjectMission> queryByPaging(ServletRequest request, DTPager pager) {
		Specification<ProjectMission> specification = DynamicSpecifications.bySearchFilter(request, ProjectMission.class);
		return projectMissionService.queryByPaging(specification, pager);
	}

	
	/**
	 * 添加mission
	 * 
	 * @param mission
	 * @return
	 */
	@Log(message="添加了id={0}的任务维护。")
	@RequiresPermissions("Project:save")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Response add(ProjectMission mission) {
		projectMissionService.addProjectMission(mission);
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{mission.getId()}));
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
		return projectMissionService.queryById(id);
	}

	/**
	 * 更新mission
	 * 
	 * @param mission
	 * @return
	 */
	@Log(message="修改了id={0}的任务维护的信息。")
	@RequiresPermissions("Project:edit")
	@RequestMapping(value = "{id}/update", method = RequestMethod.POST)
	@ResponseBody
	public Response update(ProjectMission mission) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{mission.getId()}));
		return projectMissionService.updateProjectMission(mission);
	}
	
	/**
	 * 删除mission
	 * 
	 * @param id
	 * @return
	 */
	@Log(message="删除了id={0}的任务维护。")
	@RequiresPermissions("Project:edit")
	@RequestMapping(value="{id}/delete", method=RequestMethod.POST)
	@ResponseBody
	public Response delete(@PathVariable String id) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{id}));
		return projectMissionService.delete(id);
	}

	/**
	 * 批量删除mission
	 * 
	 * @param idArray
	 * @return
	 */
	@Log(message="批量删除了id={0}任务维护。")
	@RequiresPermissions("Project:edit")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Response delete(@RequestBody String[] idArray) {
		for(String id : idArray){
			projectMissionService.delete(id);
		}
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{Arrays.toString(idArray)}));
		return new SuccessResponse();
		
	}

	/**
	 * 根据阶段查人员
	 * 
	 * @param mission
	 * @return
	 */
	@RequiresPermissions("Project:edit")
	@RequestMapping(value = "{sid}/findBySid", method = RequestMethod.POST)
	@ResponseBody
	public List<ProjectMember> findBySid(@PathVariable("sid") String sid) {
		return projectMissionService.findBySid(sid);
	}

}
