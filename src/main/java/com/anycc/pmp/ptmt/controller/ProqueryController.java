package com.anycc.pmp.ptmt.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.anycc.commmon.web.entity.WebUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
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
import com.anycc.entity.main.User;
import com.anycc.log.Log;
import com.anycc.log.LogMessageObject;
import com.anycc.log.impl.LogUitls;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectStage;
import com.anycc.pmp.ptmt.service.ProjectService;
import com.anycc.pmp.ptmt.service.ProjectStageService;
import com.anycc.pmp.rsmt.entity.Resource;
import com.anycc.pmp.rsmt.service.ResourceService;
import com.anycc.shiro.ShiroUser;
import com.anycc.util.persistence.DynamicSpecifications;
import com.anycc.utils.SecurityUtils;

@Controller
@RequestMapping("/management/pmp/ptmt/proquery")
public class ProqueryController {

	@Autowired
	private ProjectService proqueryService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private ProjectStageService proquerystageService;
	private static final String LIST = "/management/pmp/ptmt/proQuery/list";
	private static final String VIEWLIST = "/management/pmp/ptmt/proQuery/viewList";
	
	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df,
				true));
	}

	/**
	 * 查询proquery List
	 * 
	 * @return
	 */
	@RequiresPermissions("PROQUERY:view")
	@RequestMapping(value = "queryProjectList", method = RequestMethod.POST)
	@ResponseBody
	public List<Project> queryProjectList() {
		return proqueryService.queryProjectList();
	}

	/**
	 * 跳转到proquery管理页面
	 * 
	 * @return
	 */
	@RequiresPermissions("PROQUERY:view")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String index() {
		return LIST;
	}
	
	/**
	 * 跳转到proquery详情页面,不限制阶段展示
	 * 
	 * @return
	 */
	@RequiresPermissions("PROQUERY:view")
	@RequestMapping(value = "{id}/viewlist", method = RequestMethod.GET)
	public String viewindex(@PathVariable("id") String id, Map<String, Object> map) {
		map.put("proid", id);
		map.put("limit", "0");//给隐藏域赋值，用于区分
		return VIEWLIST;
	}
	
	/**
	 * 跳转到proquery详情页面,制阶段展示
	 * 
	 * @return
	 */
	@RequiresPermissions("PROQUERY:view")
	@RequestMapping(value = "{id}/viewlistlimit", method = RequestMethod.GET)
	public String viewindexlimit(@PathVariable("id") String id, Map<String, Object> map) {
		map.put("proid", id);
		map.put("limit", "1");//给隐藏域赋值，用于区分
		return VIEWLIST;
	}

	/**
	 * 查询proquerystage 
	 * 
	 * @return
	 */
	@RequiresPermissions("PROQUERY:view")
	@RequestMapping(value = "queryByPagingWithPid", method = RequestMethod.POST)
	@ResponseBody
	public DTData<ProjectStage> queryByPagingWithPid(ServletRequest request, DTPager pager) {
		Specification<ProjectStage> specification = DynamicSpecifications.bySearchFilter(request, ProjectStage.class);
		return proquerystageService.queryByPaging(specification, pager);
	}	
	
	/**
	 * 查询resource 
	 * 
	 * @return
	 */
	@RequiresPermissions("PROQUERY:view")
	@RequestMapping(value = "{pid}/{stageid}/queryByPagingWithStageId", method = RequestMethod.GET)
	@ResponseBody
	public DTData<Resource> queryByPagingWithStageId(ServletRequest request, DTPager pager,@PathVariable("pid") String pid,@PathVariable("stageid") String stageid) {
		Specification<Resource> specification = DynamicSpecifications.bySearchFilter(request, Resource.class);
		return proquerystageService.queryByPagingWithStageId(specification, pager,pid,stageid);
	}
	
	
	/**
	 * 分页查询proquerystage列表
	 *
	 * @return
	 */
	@RequiresPermissions("PROQUERY:view")
	@RequestMapping(value = "queryuserlist", method = RequestMethod.POST)
	@ResponseBody
	public DTData<WebUser> queryuserlist(ServletRequest request, DTPager pager) {
		Specification<WebUser> specification = DynamicSpecifications
				.bySearchFilter(request, WebUser.class);
		return proqueryService.queryuserlist(specification, pager);
	}
	
	/**
	 * 分页查询user列表
	 *
	 * @return
	 */
	@RequiresPermissions("PROQUERY:view")
	@RequestMapping(value = "queryByPaging", method = RequestMethod.POST)
	@ResponseBody
	public DTData<Project> queryByPaging(ServletRequest request, DTPager pager) {
		Specification<Project> specification = DynamicSpecifications
				.bySearchFilter(request, Project.class);
		return proqueryService.queryByPaging(specification, pager);
	}


	/**
	 * 根据id查询信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("PROQUERY:view")
	@RequestMapping(value = "{id}/view", method = RequestMethod.GET)
	@ResponseBody
	public Response queryById(@PathVariable("id") String id) {
		return proqueryService.queryById(id);
	}
	
	/**
	 * 根据id查询prostage信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("PROQUERY:view")
	@RequestMapping(value = "{id}/viewstage", method = RequestMethod.GET)
	@ResponseBody
	public Response stagequeryById(@PathVariable("id") String id) {
		return proquerystageService.queryById(id);
	}
	
	/**
	 * 根据id查询resource信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("PROQUERY:view")
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
	@RequiresPermissions("PROQUERY:view")
	@RequestMapping(value = "{id}/querytab", method = RequestMethod.GET)
	@ResponseBody
	public List<ProjectStage> querytab(@PathVariable("id") String id) {
		return proquerystageService.findByPid(id);
	}

	/**
	 * EXCEL导出功能
	 */
	@RequestMapping(value = "tableExport", method = RequestMethod.GET)
	public ResponseEntity<byte[]> excelExportDelete(HttpServletRequest request) throws IOException {
		//return resourceDownService.exportResourceList(request);
		return null;
	}

}
