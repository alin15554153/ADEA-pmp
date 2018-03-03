package com.anycc.pmp.rsmt.controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;

import com.anycc.common.dto.*;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectStage;
import com.anycc.pmp.rsmt.dao.ResourceDAO;
import com.anycc.pmp.rsmt.entity.Resource;
import com.anycc.pmp.rsmt.entity.ResourceDownCheck;
import com.anycc.util.persistence.DynamicSpecifications;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anycc.pmp.rsmt.entity.ResourceDown;
import com.anycc.pmp.rsmt.service.ResourceDownService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/management/pmp/rsmt/resourceDown")
public class ResourceDownController {

	@Autowired
	private ResourceDownService resourceDownService;
	@Autowired
	private ResourceDAO resourceDAO;
	private static final String RESOURCELIST = "management/pmp/rsmt/resourceDown/resourceList";
	private static final String APPLICATIONLIST = "management/pmp/rsmt/resourceDown/applicationList";

	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	//资源申请页面
	///////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 跳转到可申请资源列表页面
	 */
	@RequiresPermissions("Resource:view")
	@RequestMapping(value = "resourceList", method = RequestMethod.GET)
	public String resourceList() {
		return RESOURCELIST;
	}

	/**
	 * 分页查询可申请资源列表(废弃)
	 */
	@RequiresPermissions("Resource:view")
	@RequestMapping(value = "findResourceList", method = RequestMethod.POST)
	@ResponseBody
	public DTData<Resource> findResourceList(ServletRequest request, DTPager pager) {
		Specification<Resource> specification = DynamicSpecifications.bySearchFilter(request, Resource.class);
		return resourceDownService.findResourceList(specification, pager);
	}

	/**
	 * 分页查询可申请资源列表
	 */
	@RequiresPermissions("Resource:view")
	@RequestMapping(value = "findResourceListBySql", method = RequestMethod.POST)
	@ResponseBody
	public DTData<Resource> findResourceListBySql(HttpServletRequest request, DTPager pager) {

		String resourceName = request.getParameter("resourceName");//资源名称
		String resourceType = request.getParameter("resourceType");//资源类型
		String stageId = request.getParameter("projectStage");//阶段ID
		String projectName = request.getParameter("projectName");//项目名称
		String uid = request.getParameter("uid");//当前用户ID

		ResourceDown resourceDown = new ResourceDown();
		if (!"".equals(resourceName) && resourceName != null) {
			resourceDown.setResourceName(resourceName);
		}
		if (!"".equals(resourceType) && resourceType != null) {
			resourceDown.setResourceType(resourceType);
		}
		if (!"".equals(stageId) && stageId != null) {
			resourceDown.setStageId(stageId);
		}
		if (!"".equals(projectName) && projectName != null) {
			resourceDown.setProjectName(projectName);
		}
		if (!"".equals(uid) && uid != null) {
			resourceDown.setUid(Integer.parseInt(uid));
		}
		return resourceDownService.findResourceListBySql(resourceDown, pager);
	}

	/**
	 * 根据当前用户ID获取所参与的项目ID列表
	 */
	@RequestMapping(value = "getMyProjectIds", method = RequestMethod.POST)
	@ResponseBody
	public Response getMyProjectIds(HttpServletRequest request){
		String userid = request.getParameter("userid");
		List<Object> list = resourceDownService.getMyProjectIds(userid);
		return new ObjectResponse<List<Object>>(list);
	}

	/**
	 * 根据当前用户ID获取所在地区ID
	 */
	@RequestMapping(value = "getMyAreaId", method = RequestMethod.POST)
	@ResponseBody
	public Response getMyAreaId(HttpServletRequest request){
		String userid = request.getParameter("userid");
		String getMyAreaId = resourceDownService.getMyAreaId(userid);
		return new ObjectResponse<String>(getMyAreaId);
	}

	/**
	 * 提交资源下载申请
	 */
	@RequestMapping(value = "sendApplication", method = RequestMethod.POST)
	@ResponseBody
	public Response sendApplication(HttpServletRequest request) {
		String rid = request.getParameter("rid");//资源ID
		String uid = request.getParameter("uid");//用户ID
		String remark = request.getParameter("remark");//申请原因
		String processType = request.getParameter("processType");//流程类型
		return resourceDownService.sendApplication(rid, Integer.parseInt(uid), remark, Integer.parseInt(processType));
	}



	///////////////////////////////////////////////////////////////////////////////////////////////
	//已经申请资源列表页面
	///////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 跳转到已申请资源管理页面
	 */
	@RequiresPermissions("ResourceDown:view")
	@RequestMapping(value = "applicationList", method = RequestMethod.GET)
	public String applicationList() {
		return APPLICATIONLIST;
	}

	/**
	 * 分页查询已申请资源列表
	 */
	@RequiresPermissions("ResourceDown:view")
	@RequestMapping(value = "findApplicationList", method = RequestMethod.POST)
	@ResponseBody
	/*public DTData<ResourceDown> findApplicationList(ResourceDown resourceDown, DTPager pager) {
		return resourceDownService.queryByPaging(resourceDown, pager);
	}*/
	public DTData<ResourceDown> findApplicationList(ServletRequest request, DTPager pager) {
		Specification<ResourceDown> specification = DynamicSpecifications.bySearchFilter(request, ResourceDown.class);
		return resourceDownService.findApplicationList(specification, pager);
	}

	/**
	 * 获取最近一条的审核记录
     */
	@RequestMapping(value = "findCheckById", method = RequestMethod.POST)
	@ResponseBody
	public Response findCheckById(HttpServletRequest request){
		String id = request.getParameter("id");
		ResourceDownCheck ResourceDownCheck = resourceDownService.findCheckById(id);
		return new ObjectResponse<ResourceDownCheck>(ResourceDownCheck);
	}

	/**
	 * 重新提交资源下载申请
	 */
	@RequestMapping(value = "reSendApplication", method = RequestMethod.POST)
	@ResponseBody
	public Response reSendApplication(HttpServletRequest request) {
		String id = request.getParameter("id");//资源ID
		String remark = request.getParameter("remark");//申请原因
		String processType = request.getParameter("processType");//流程类型
		return resourceDownService.reSendApplication(id, remark);
	}


	///////////////////////////////////////////////////////////////////////////////////////////////
	//公用
	///////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 下载功能
     */
	/*@RequestMapping(value = "{filename}/download", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> download(@PathVariable("filename") String filename, HttpServletRequest request) throws IOException {
		String webAppPath = request.getSession().getServletContext().getRealPath("/");
		//webAppPath = new File(webAppPath).getParent();
		File file = new File(webAppPath + File.separator + "upload" + File.separator + filename);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentDispositionFormData("attachment",new String(filename.getBytes("UTF-8"),"iso-8859-1"));
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		if(file.exists()){
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), httpHeaders, HttpStatus.OK);
		} else {
			return null;
		}
	}*/
	@RequestMapping(value = "{company}/{procode}/{filename}/download", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> download(@PathVariable("filename") String filename,@PathVariable("company") String company,@PathVariable("procode") String procode, HttpServletRequest request) throws IOException {
		String webAppPath = request.getSession().getServletContext().getRealPath("/");
		//webAppPath = new File(webAppPath).getParent();
		File file = new File(webAppPath + File.separator + "upload" + File.separator + company + File.separator + procode + File.separator + filename);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentDispositionFormData("attachment",new String(filename.getBytes("UTF-8"),"iso-8859-1"));
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		if(file.exists()){
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), httpHeaders, HttpStatus.OK);
		} else {
			return null;
		}
	}

	/**
	 * 下载功能2
	 */
	@RequestMapping(value = "{rid}/dl", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> downloadRealFileName(@PathVariable("rid") String rid, HttpServletRequest request) throws IOException {
		Resource resource = resourceDAO.findOne(rid);
		Project project = resourceDownService.findProjectByResourceId(rid);
		String company_id = project.getWebOrganization().getId().toString();//公司ID
		String project_code = project.getCode();//项目CODE,非ID
		String webAppPath = request.getSession().getServletContext().getRealPath("/");
		//webAppPath = new File(webAppPath).getParent();
		File file = new File(webAppPath + File.separator + "upload" + File.separator + company_id + File.separator + project_code + File.separator + resource.getPath());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentDispositionFormData("attachment",new String(resource.getName().getBytes("UTF-8"),"iso-8859-1"));
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		if(file.exists()){
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), httpHeaders, HttpStatus.OK);
		} else {
			return null;
		}
	}

	/**
	 * 增加下载次数
	 */
	@RequestMapping(value = "addDownloadTimes", method = RequestMethod.POST)
	@ResponseBody
	public Response addDownloadTimes(HttpServletRequest request) {
		String rid = request.getParameter("rid");//资源ID
		return resourceDownService.addDownloadTimes(rid);
	}

	/**
	 * 项目阶段下拉列表(废弃)
     */
	@RequestMapping(value = "findProjectStageList", method = RequestMethod.POST)
	@ResponseBody
	public Response findProjectStageList(){
		List<ProjectStage> list = resourceDownService.findProjectStageList();
		return new ObjectResponse<List<ProjectStage>>(list);
	}

	/**
	 * EXCEL导出功能
	 */
	@RequestMapping(value = "tableExport", method = RequestMethod.GET)
	public ResponseEntity<byte[]> excelExportDelete(HttpServletRequest request) throws IOException {
		return resourceDownService.exportResourceList(request);
	}

}
