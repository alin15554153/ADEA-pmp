
package com.anycc.pmp.ptmt.controller;


import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.common.dto.SuccessResponse;
import com.anycc.log.Log;
import com.anycc.log.LogMessageObject;
import com.anycc.log.impl.LogUitls;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.service.ProjectService;
import com.anycc.util.persistence.DynamicSpecifications;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/management/pmp/ptmt/change")
public class ProjectChangeController {

	@Autowired
	private ProjectService projectService;
	private static final String LIST = "management/pmp/ptmt/change/list";
	private static final String CP = "management/pmp/ptmt/change/changePage";

	
	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, 
				new CustomDateEditor(df, true));
	}
	
	
	/**
	 * 跳转到项目变更管理页面
	 * 
	 * @return
	 */
	@RequiresPermissions("PMP_PTMT_ProjectChange:view")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index() {
		return LIST;
	}

	/**
	 * 跳转到项目变更管理页面
	 *
	 * @return
	 */
	@RequiresPermissions("PMP_PTMT_ProjectChange:view")
	@RequestMapping(value = "{id}/changePage", method = RequestMethod.GET)
	public String changePage(@PathVariable("id")String id, Map<String, Object> map) {
		map.put("id", id);
		return CP;
	}

	/**
	 * 分页查询resource列表
	 *
	 * @return
	 */
	@RequiresPermissions("PMP_PTMT_ProjectChange:view")
	@RequestMapping(value = "queryByPaging", method = RequestMethod.POST)
	@ResponseBody
	public DTData<Project> queryByPaging(ServletRequest request, DTPager pager) {
		Specification<Project> specification = DynamicSpecifications.bySearchFilter(request, Project.class);
		return projectService.queryByPaging(specification, pager);

	}


}
