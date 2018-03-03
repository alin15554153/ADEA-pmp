package com.anycc.pmp.slas.controller;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.ObjectResponse;
import com.anycc.common.dto.Response;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.slas.entity.ResStatistics;
import com.anycc.pmp.slas.service.ResStatisticsService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Shibin on 2016/3/17.
 */
@Controller
@RequestMapping("/management/pmp/slas/resStatistics")
public class ResStatisticsController {


    @Autowired
    private ResStatisticsService resStatisticsService;

    private static final String LIST = "management/pmp/slas/resStatistics/list";

    @InitBinder
    public void dataBinder(WebDataBinder dataBinder) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dataBinder.registerCustomEditor(Date.class,
                new CustomDateEditor(df, true));
    }


    /**
     * 跳转到resource管理页面
     *
     * @return
     */
    @RequiresPermissions("PMP_SLAS_ResStatistics:view")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String index() {
        return LIST;
    }

    /**
     * 分页查询resource列表
     *
     * @return
     */
    @RequiresPermissions("PMP_SLAS_ResStatistics:view")
    @RequestMapping(value = "queryByPaging", method = RequestMethod.POST)
    @ResponseBody
    public DTData<ResStatistics> queryByPaging(Project project, DTPager pager) {
        return  resStatisticsService.queryByPaging(project, pager);

    }

    @RequiresPermissions("PMP_SLAS_ResStatistics:view")
    @RequestMapping(value = "queryResStatisticsByAreaAndCompany", method = RequestMethod.POST)
    @ResponseBody
    public Response queryResStatisticsByAreaAndCompany(ResStatistics resStatistics) {

        return  resStatisticsService.queryResStatisticsByAreaAndCompany(resStatistics);
    }
    
    /**
	 * EXCEL导出功能
	 */
	@RequestMapping(value = "tableExport", method = RequestMethod.GET)
	public ResponseEntity<byte[]> excelExportDelete(HttpServletRequest request) throws IOException {
		return resStatisticsService.exportResourceList(request);
	}

}
