
package com.anycc.pmp.ptmt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anycc.pmp.ptmt.entity.ProMissionMember;

public interface ProMissionMemberDAO extends JpaRepository<ProMissionMember, String>, JpaSpecificationExecutor<ProMissionMember> {
	
	List<ProMissionMember> findByMid(String mid);
	
}