package com.anycc.pmp.ptmt.service;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectFollow;
import org.springframework.data.jpa.domain.Specification;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * Created by DELL1 on 2016/7/15.
 */
public interface ProjectFollowService {
    Response queryByPid(String pid);

    Response addProjectFollow(ProjectFollow projectFollow);

    DTData<ProjectFollow> queryByPaging(Specification specification, DTPager pager);

    Response viewFollow(String id);

    Response deleteFollow(String id);

    Response updateFollow(ProjectFollow projectFollow);

    DTData<ProjectFollow>  queryProjectFollowList(ServletRequest request, DTPager pager);
}
