package com.anycc.pmp.comm.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.pmp.comm.entity.TempDemo;
import com.anycc.pmp.comm.service.TempDemoService;
import com.anycc.util.persistence.DynamicSpecifications;

/**
 * @author fjw
 * @Description: demo类
 * @date 2016年03月02日 上午9:41:11
 */
@Controller
@RequestMapping("/management/pmp/comm/tempDemo")
public class TempDemoController {
	@Autowired
	private TempDemoService tempDemoService;
	private static final String ADDDEMO = "management/index/addTempletTable";
	private static final String ADDIFRAMEDEMO = "management/index/addIframeTempletTable";

	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dataBinder.registerCustomEditor(Date.class,new CustomDateEditor(df, true));
	}
	/**
	 * 查询demos
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequiresPermissions("TEMPDEMO:view")
	@RequestMapping(value = "findDemos", method = RequestMethod.POST)
	@ResponseBody
	public DTData<TempDemo> findDemos(ServletRequest request, DTPager pager){
		Specification<TempDemo> specification = DynamicSpecifications.bySearchFilter(request, TempDemo.class);
		return tempDemoService.findDemos(specification, pager);
	}
	
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequiresPermissions("TEMPDEMO:view")
	@RequestMapping(value = "toAddDemo", method = RequestMethod.GET)
	public String toAddDemo(){
		return ADDDEMO;
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequiresPermissions("TEMPDEMO:view")
	@RequestMapping(value = "toAddIframeDemo", method = RequestMethod.GET)
	public String toAddIframeDemo(){
		return ADDIFRAMEDEMO;
	}
}
