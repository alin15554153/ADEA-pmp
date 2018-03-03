
package com.anycc.pmp.ptmt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anycc.pmp.ptmt.entity.ProjectChangeLog;

public interface ProjectChangeLogDAO extends JpaRepository<ProjectChangeLog, String>, JpaSpecificationExecutor<ProjectChangeLog> {
}