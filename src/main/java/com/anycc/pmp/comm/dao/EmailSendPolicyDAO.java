package com.anycc.pmp.comm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anycc.pmp.comm.entity.EmailSendPolicy;


public interface EmailSendPolicyDAO extends
		JpaRepository<EmailSendPolicy, Long>,
		JpaSpecificationExecutor<EmailSendPolicy> {
}