package com.anycc.pmp.ptmt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anycc.pmp.ptmt.entity.RoleMPermission;

public interface RoleMPermissionDAO extends
		JpaRepository<RoleMPermission, String>,
		JpaSpecificationExecutor<RoleMPermission> {
}