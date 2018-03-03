
package com.anycc.pmp.rsmt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anycc.pmp.rsmt.entity.ResourceDown;

public interface ResourceDownDAO extends JpaRepository<ResourceDown, String>, JpaSpecificationExecutor<ResourceDown> {
}