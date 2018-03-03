
package com.anycc.pmp.rsmt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anycc.pmp.rsmt.entity.Resource;

public interface ResourceDAO extends JpaRepository<Resource, String>, JpaSpecificationExecutor<Resource> {
}