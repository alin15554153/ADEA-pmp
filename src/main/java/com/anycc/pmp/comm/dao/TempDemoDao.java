package com.anycc.pmp.comm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anycc.pmp.comm.entity.TempDemo;

public interface TempDemoDao extends JpaRepository<TempDemo, Long>, JpaSpecificationExecutor<TempDemo> {

}
