
package com.anycc.pmp.ptmt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.anycc.pmp.ptmt.entity.Project;
//
public interface ProjectDAO extends JpaRepository<Project, String>, JpaSpecificationExecutor<Project> {

    @Query("select max(p.code) from Project p ")
     String queryMaxCode();
}