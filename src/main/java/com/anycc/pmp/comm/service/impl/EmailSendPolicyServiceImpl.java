package com.anycc.pmp.comm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.FailedResponse;
import com.anycc.common.dto.ObjectResponse;
import com.anycc.common.dto.Response;
import com.anycc.common.dto.SuccessResponse;
import com.anycc.pmp.comm.dao.EmailSendPolicyDAO;
import com.anycc.pmp.comm.entity.EmailSendPolicy;
import com.anycc.pmp.comm.service.EmailSendPolicyService;

@Service("emailSendPolicyService")
@Transactional
public class EmailSendPolicyServiceImpl implements EmailSendPolicyService {

	@Autowired
	private EmailSendPolicyDAO emailSendPolicyDAO;
	@Autowired
	private TempAuxiliaryUtils<EmailSendPolicy> tempAuxiliaryUtils;
	
	@Override
	public Response queryEmailSendPolicy() {
		EmailSendPolicy emailSendPolicy = emailSendPolicyDAO.findOne(1L);
		if (emailSendPolicy != null) {
			return new ObjectResponse<EmailSendPolicy>(emailSendPolicy);
		} else {
			return new FailedResponse("后台查询邮件策略的信息失败！");
		}
	}
	@Override
	public EmailSendPolicy queryEmailSendPolicyInfo() {
		return emailSendPolicyDAO.findOne(1L);
	}
	@Override
	public Response update(EmailSendPolicy emailSendPolicy) {
		Assert.notNull(emailSendPolicy);
		// 保存角色表
		emailSendPolicyDAO.save(emailSendPolicy);
		if (emailSendPolicy != null) {
			return new SuccessResponse();
		} else {
			return new FailedResponse("更新邮件策略的信息失败！");
		}
		
	}

}
