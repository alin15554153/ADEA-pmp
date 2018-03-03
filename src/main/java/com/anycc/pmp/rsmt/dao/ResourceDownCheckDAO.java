package com.anycc.pmp.rsmt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anycc.pmp.rsmt.entity.ResourceDownCheck;

public interface ResourceDownCheckDAO extends JpaRepository<ResourceDownCheck, String>, JpaSpecificationExecutor<ResourceDownCheck> {
}