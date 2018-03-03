package com.anycc.pmp.ptmt.controller;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectFollow;
import com.anycc.pmp.ptmt.service.ProjectFollowService;
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
import java.util.Date;
import java.util.List;

/**
 * Created by DELL1 on 2016/7/15.
 */
@Controller
@RequestMapping("/management/pmp/ptmt/projectFollow")
public class ProjectFollowController {
    @Autowired
    private ProjectFollowService projectFollowService;

    private static final String LIST = "/management/pmp/ptmt/project/follow/list";

    @InitBinder
    public void dataBinder(WebDataBinder dataBinder) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df,
                true));
    }



    @RequiresPermissions("ProjectFollow:view")
    @RequestMapping(value = "queryProjectFollowList", method = RequestMethod.POST)
    @ResponseBody
    public  DTData<ProjectFollow> queryProjectFollowList(ServletRequest request, DTPager pager) {
        return projectFollowService.queryProjectFollowList(request,pager);
    }

    /**
     * 跳转到project管理页面
     *
     * @return
     */
    @RequiresPermissions("ProjectFollow:view")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String index() {
        return LIST;
    }


    /**
     * 新增项目跟踪
     * @param projectFollow
     * @return
     */
    @RequestMapping(value="addFollow",method = RequestMethod.POST)
    @ResponseBody
    public Response addProjectFollow(ProjectFollow projectFollow){
        projectFollow.setCreateTime(new Date());
        return projectFollowService.addProjectFollow(projectFollow);
    }

    /**
     * 根据项目id查询项目跟踪
     * @param pid
     * @return
     */
    @RequestMapping(value="queryFollowByPid",method = RequestMethod.GET)
    @ResponseBody
    public Response queryProjectFollowList(String pid){
        return projectFollowService.queryByPid(pid);
    }

    /**
     * 分页查询resource列表
     *
     * @return
     */
    @RequestMapping(value = "queryByPaging", method = RequestMethod.POST)
    @ResponseBody
    public DTData<ProjectFollow> queryByPaging(ServletRequest request, DTPager pager) {
        Specification<ProjectFollow> specification = DynamicSpecifications.bySearchFilter(request, ProjectFollow.class);
        return projectFollowService.queryByPaging(specification, pager);

    }

    /**
     * 按id查询跟踪
     * @param id
     * @return
     */
    @RequestMapping(value="{id}/view",method = RequestMethod.GET)
    @ResponseBody
    public Response view(@PathVariable("id") String id){
        return projectFollowService.viewFollow(id);
    }

    /**
     * 修改跟踪
     * @param projectFollow
     * @return
     */
    @RequestMapping(value="{id}/update",method = RequestMethod.POST)
    @ResponseBody
    public Response update(ProjectFollow projectFollow){
        return projectFollowService.updateFollow(projectFollow);
    }

    /**
     * 按id删除跟踪
     * @param id
     * @return
     */
    @RequestMapping(value="{id}/delete",method = RequestMethod.GET)
    @ResponseBody
    public Response delete(@PathVariable("id") String id){
        return projectFollowService.deleteFollow(id);
    }
}
