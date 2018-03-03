package com.anycc.pmp.ptmt.service.impl;

import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.*;
import com.anycc.pmp.ptmt.dao.ProjectFollowDao;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectFollow;
import com.anycc.pmp.ptmt.service.ProjectFollowService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletRequest;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by DELL1 on 2016/7/15.
 */
@Service("projectFollowService")
@Transactional
public class ProjectFollowServiceImpl implements ProjectFollowService {
    @Autowired
    private ProjectFollowDao projectFollowDao;
    @Autowired
    private TempAuxiliaryUtils<ProjectFollow> tempAuxiliaryUtils;
    @PersistenceContext
    private EntityManager em;

    @Override
    public DTData<ProjectFollow> queryByPaging(Specification specification, DTPager pager) {
        return new DTData<ProjectFollow>(projectFollowDao.findAll(specification,
                tempAuxiliaryUtils.buildPageRequest(pager,new Sort(Sort.Direction.DESC, "followTime"))), pager);
    }

    @Override
    public Response queryByPid(String pid) {
        List<ProjectFollow> followList = projectFollowDao.findByPidOrderByCreateTimeDesc(pid);
        if(followList!=null){
            return new ListResponse<ProjectFollow>(followList);
        }
        return new FailedResponse("查询项目跟踪失败!");
    }

    @Override
    public Response addProjectFollow(ProjectFollow projectFollow) {
        projectFollowDao.save(projectFollow);
        return new SuccessResponse();
    }

    @Override
    public Response viewFollow(String id) {
        ProjectFollow projectFollow = projectFollowDao.findById(id);
        if(projectFollow!=null){
            return new ObjectResponse<ProjectFollow>(projectFollow);
        }
        return new FailedResponse("查询跟踪失败!");
    }

    @Override
    public Response deleteFollow(String id) {
        projectFollowDao.delete(id);
        return new SuccessResponse();
    }

    @Override
    public Response updateFollow(ProjectFollow projectFollow) {
        Assert.notNull(projectFollow.getId());
        ProjectFollow oldProjectFollow = projectFollowDao.findById(projectFollow.getId());
        if(oldProjectFollow==null){
            return new FailedResponse("修改跟踪失败");
        }
        projectFollow.setCreateTime(oldProjectFollow.getCreateTime());
        projectFollowDao.save(projectFollow);
        return new SuccessResponse();
    }

    @Override
    public  DTData<ProjectFollow>  queryProjectFollowList(ServletRequest request, DTPager pager) {
        String userName = request.getParameter("userName");
        String pname=request.getParameter("pname");
        String sql = "SELECT p.name,f.content,f.follow_time,f.username from project_follow as f,projectinfo as p WHERE f.pid=p.id ";
        if(StringUtils.isNotBlank(pname)){
            sql+=" and p.name like '%"+pname+"%' ";
        }
        if(StringUtils.isNotBlank(userName)){
            sql+=" and f.username like '%"+userName+"%' ";
        }
        sql+=" ORDER BY f.follow_time desc ";
        System.err.println(sql);
        Query query = em.createNativeQuery(sql);
        query.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setFirstResult(pager.getStart());
        query.setMaxResults(pager.getLength());
        String countsql = "select count(1) from (" + sql + ") as resultNum";
        System.err.println(sql);
        Query queryCnt = em.createNativeQuery(countsql);
        BigInteger count = (BigInteger) queryCnt.getSingleResult();
        PageImpl qmaPage = new PageImpl<>(query.getResultList(), buildPageRequest(pager), count.longValue());
        return new DTData<>(qmaPage, pager);
    }

    /**
     * 创建分页
     *
     * @param pager
     * @return
     */
    private PageRequest buildPageRequest(DTPager pager) {
        return new PageRequest((int) pager.getStart() / pager.getLength(), pager.getLength());
    }
}
