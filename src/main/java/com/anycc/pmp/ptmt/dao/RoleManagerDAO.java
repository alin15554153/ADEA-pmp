package com.anycc.pmp.ptmt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anycc.pmp.ptmt.entity.RoleManager;

public interface RoleManagerDAO extends JpaRepository<RoleManager, String>,
		JpaSpecificationExecutor<RoleManager> {
}