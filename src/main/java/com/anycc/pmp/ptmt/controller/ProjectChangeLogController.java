
package com.anycc.pmp.ptmt.controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.common.dto.SuccessResponse;
import com.anycc.log.Log;
import com.anycc.log.LogMessageObject;
import com.anycc.log.impl.LogUitls;
import com.anycc.pmp.mail.MailSend;
import com.anycc.pmp.ptmt.entity.ProjectStage;
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

import com.anycc.pmp.ptmt.entity.ProjectChangeLog;
import com.anycc.pmp.ptmt.service.ProjectChangeLogService;

import javax.servlet.ServletRequest;


@Controller
@RequestMapping("/management/pmp/ptmt/projectChangeLog")
public class ProjectChangeLogController {

	@Autowired
	private ProjectChangeLogService projectChangeLogService;

	
	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, 
				new CustomDateEditor(df, true));
	}
	
	/**
	 * 分页查询projectChangeLog列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "queryByPaging", method = RequestMethod.POST)
	@ResponseBody
	public DTData<ProjectChangeLog> queryByPaging(ServletRequest request, DTPager pager) {
		Specification<ProjectChangeLog> specification = DynamicSpecifications.bySearchFilter(request, ProjectChangeLog.class);
		return projectChangeLogService.queryByPaging(specification, pager);
	}
	/*public DTData<ProjectChangeLog> queryByPaging(ProjectChangeLog projectChangeLog, DTPager pager) {
		return projectChangeLogService.queryByPaging(projectChangeLog, pager);
	}*/
	
	/**
	 * 添加projectChangeLog
	 * 
	 * @param projectChangeLog
	 * @return
	 */
	@Log(message="添加了id={0}的ProjectChangeLog。")
	@RequiresPermissions("ProjectChangeLog:save")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Response add(ProjectChangeLog projectChangeLog) {
		projectChangeLogService.addProjectChangeLog(projectChangeLog);
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{projectChangeLog.getId()}));

//		MailSend mailSend = new MailSend();
//		mailSend.mailSend("测试邮件210","这是我的邮件哦",new String[]{"evangoe@163.com","evangoe@hotmail.com"});


		return new SuccessResponse();
	}
	/**
	 * 根据id查询信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("ProjectChangeLog:view")
	@RequestMapping(value = "{id}/view", method = RequestMethod.GET)
	@ResponseBody
	public Response queryById(@PathVariable("id") String id) {
		return projectChangeLogService.queryById(id);
	}
	
	/**
	 * 更新projectChangeLog
	 * 
	 * @param projectChangeLog
	 * @return
	 */
	@Log(message="修改了id={0}的ProjectChangeLog的信息。")
	@RequiresPermissions("ProjectChangeLog:edit")
	@RequestMapping(value = "{id}/update", method = RequestMethod.POST)
	@ResponseBody
	public Response update(ProjectChangeLog projectChangeLog) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{projectChangeLog.getId()}));
		return projectChangeLogService.updateProjectChangeLog(projectChangeLog);
	}
	
	/**
	 * 删除projectChangeLog
	 * 
	 * @param id
	 * @return
	 */
	@Log(message="删除了id={0}的ProjectChangeLog。")
	@RequiresPermissions("ProjectChangeLog:delete")
	@RequestMapping(value="{id}/delete", method=RequestMethod.POST)
	@ResponseBody
	public Response delete(@PathVariable String id) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{id}));
		return projectChangeLogService.delete(id);
	}

	/**
	 * 批量删除projectChangeLog
	 * 
	 * @param idArray
	 * @return
	 */
	@Log(message="批量删除了id={0}ProjectChangeLog。")
	@RequiresPermissions("ProjectChangeLog:delete")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Response delete(@RequestBody String[] idArray) {
		for(String id : idArray){
			projectChangeLogService.delete(id);
		}
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{Arrays.toString(idArray)}));
		return new SuccessResponse();
		
	}

	@RequestMapping(value = "{id}/initStageByProject", method = RequestMethod.POST)
	@ResponseBody
	public Response initStageByProject(@PathVariable("id") String id) {

		return projectChangeLogService.initStageByProject(id);
	}

}
