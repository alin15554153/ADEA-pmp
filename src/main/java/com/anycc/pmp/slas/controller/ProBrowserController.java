package com.anycc.pmp.slas.controller;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.slas.entity.ProBrowser;
import com.anycc.pmp.slas.entity.ProStatistics;
import com.anycc.pmp.slas.service.ProBrowserService;
import com.anycc.pmp.slas.service.ProStatisticsService;
import com.anycc.util.persistence.DynamicSpecifications;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/management/pmp/slas/proBrowser")
public class ProBrowserController {


    @Autowired
    private ProBrowserService proBrowserService;

    private static final String LIST = "management/pmp/slas/proBrowser/list";

    @InitBinder
    public void dataBinder(WebDataBinder dataBinder) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dataBinder.registerCustomEditor(Date.class,
                new CustomDateEditor(df, true));
    }


    /**
     * 跳转到管理页面
     *
     * @return
     */
    @RequiresPermissions("PMP_SLAS_ProBrowser:view")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String index(@RequestParam(value = "p", required = false) String p, Model model) {
        model.addAttribute("p", p);
        return LIST;
    }

    /**
     * 分页查询列表
     *
     * @return
     */
    @RequiresPermissions("PMP_SLAS_ProBrowser:view")
    @RequestMapping(value = "initCharts", method = RequestMethod.POST)
    @ResponseBody
    public List<Project> initCharts(String orgId) {
        return  proBrowserService.initCharts(orgId);

    }
    
    /**
	 * EXCEL导出功能
	 */
	@RequestMapping(value = "tableExport", method = RequestMethod.GET)
	public ResponseEntity<byte[]> excelExportDelete(HttpServletRequest request) throws IOException {
		return proBrowserService.exportResourceList(request);
	}


}
