package com.anycc.pmp.ptmt.dao;

import com.anycc.pmp.ptmt.entity.ProjectFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by DELL1 on 2016/7/15.
 */
public interface ProjectFollowDao extends JpaRepository<ProjectFollow, String>,
        JpaSpecificationExecutor<ProjectFollow> {
    List<ProjectFollow> findByPidOrderByCreateTimeDesc(String pid);

    ProjectFollow findById(String id);
}
