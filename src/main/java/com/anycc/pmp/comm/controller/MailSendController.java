package com.anycc.pmp.comm.controller;

import java.util.ArrayList;
import java.util.List;

import com.anycc.commmon.web.entity.WebUser;
import com.anycc.commmon.web.service.WebUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anycc.common.dto.FailedResponse;
import com.anycc.common.dto.Response;
import com.anycc.common.dto.SuccessResponse;
import com.anycc.pmp.comm.entity.EmailSendPolicy;
import com.anycc.pmp.comm.entity.Mail;
import com.anycc.pmp.comm.service.EmailSendPolicyService;
import com.anycc.pmp.comm.service.SearchPersonService;
import com.anycc.pmp.mail.MailSend;

/**
 * @author 方锦文
 * @Description: 邮件发送类
 * @date 2016年04月21日 下午14:00:00
 */
@Controller
@RequestMapping("/management/pmp/comm/mailSend")
public class MailSendController {
	@Autowired
	private MailSend mailSend;
	@Autowired
	private SearchPersonService searchPersonService;
	@Autowired
	private WebUserService webUserService;
	@Autowired
	private EmailSendPolicyService emailSendPolicyService;
	
	/**
	 * 给单人发送邮件
	 * @return
	 */
	@RequestMapping(value = "sendPTP", method = RequestMethod.POST)
	@ResponseBody
	public Response sendPTP(Mail mail) {
		//获取邮件发送策略信息
		EmailSendPolicy emailSendPolicy =emailSendPolicyService.queryEmailSendPolicyInfo();
		//项目信息变更设置为不发送 直接返回
		if(mail.getType()==1 && emailSendPolicy.getIsPjchangeSend()==0)
			return new SuccessResponse();
		//项目成员信息变更设置为不发送 直接返回
		if(mail.getType()==2 && emailSendPolicy.getIsPjmemchangeSend()==0)
			return new SuccessResponse();
		//项目阶段信息变更设置为不发送 直接返回
		if(mail.getType()==3 && emailSendPolicy.getIsPjstageSend()==0)
			return new SuccessResponse();
		//资源申请信息设置为不发送 直接返回
		if(mail.getType()==4 && emailSendPolicy.getIsRsapplySend()==0)
			return new SuccessResponse();
		//根据用户编号查出用户信息
		WebUser user=webUserService.queryWebUser(mail.getUserId());
		//发送邮件
		try{
			mailSend.mailSend(mail.getTitle(), mail.getContent(), user.getEmail());
		}catch(Exception e){
			return new FailedResponse("邮箱发送失败！");
		}
		return new SuccessResponse();
	}
	/**
	 * 根据项目给成员发送邮件
	 * @return
	 */
	@RequestMapping(value = "sendAccToProject", method = RequestMethod.POST)
	@ResponseBody
	public Response sendAccToProject(Mail mail) {
		//获取邮件发送策略信息
		EmailSendPolicy emailSendPolicy =emailSendPolicyService.queryEmailSendPolicyInfo();
		//项目信息变更设置为不发送 直接返回
		if(mail.getType()==1 && emailSendPolicy.getIsPjchangeSend()==0)
			return new SuccessResponse();
		//项目成员信息变更设置为不发送 直接返回
		if(mail.getType()==2 && emailSendPolicy.getIsPjmemchangeSend()==0)
			return new SuccessResponse();
		//项目阶段信息变更设置为不发送 直接返回
		if(mail.getType()==3 && emailSendPolicy.getIsPjstageSend()==0)
			return new SuccessResponse();
		//资源申请信息设置为不发送 直接返回
		if(mail.getType()==4 && emailSendPolicy.getIsRsapplySend()==0)
			return new SuccessResponse();
		//根据项目编号查出参与人员
		List<WebUser> list=searchPersonService.findProjectMembers(mail.getProjectId());
		//拼接发送邮件地址数组 begin
		List<String> toAddressArray=new ArrayList<String>();
		for (WebUser webUser : list) {
			if(webUser.getEmail()!=null && !"".equals(webUser.getEmail()))
				toAddressArray.add(webUser.getEmail());
		}
		//拼接发送邮件地址数组 end
		//发送邮件
		try{
			mailSend.mailSend(mail.getTitle(), mail.getContent(), toAddressArray);
		}catch(Exception e){
			return new FailedResponse("邮箱发送失败！");
		}
		return new SuccessResponse();
	}
	
	/**
	 * 给当前机构的地区管理员发送邮件
	 * @return
	 */
	@RequestMapping(value = "sendAreaAdmins", method = RequestMethod.POST)
	@ResponseBody
	public Response sendAreaAdmins(Mail mail) {
		//获取邮件发送策略信息
		EmailSendPolicy emailSendPolicy =emailSendPolicyService.queryEmailSendPolicyInfo();
		//资源申请设置为不发送 直接返回
		if(emailSendPolicy.getIsRsapplySend()==0)
			return new SuccessResponse();
		//查出所有该地区管理员
		List<WebUser> list=searchPersonService.findAreaAdmins(mail.getOrgId());
		//拼接发送邮件地址数组 begin
		List<String> toAddressArray=new ArrayList<String>();
		for (WebUser webUser : list) {
			if(webUser.getEmail()!=null && !"".equals(webUser.getEmail()))
				toAddressArray.add(webUser.getEmail());
		}
		//拼接发送邮件地址数组 end
		//发送邮件
		try{
			mailSend.mailSend(mail.getTitle(), mail.getContent(), toAddressArray);
		}catch(Exception e){
			return new FailedResponse("邮箱发送失败！");
		}
		return new SuccessResponse();
	}
	
	/**
	 * 给超级管理员发送邮件
	 * @return
	 */
	@RequestMapping(value = "sendSuperAdmins", method = RequestMethod.POST)
	@ResponseBody
	public Response sendSuperAdmins(Mail mail) {
		//获取邮件发送策略信息
		EmailSendPolicy emailSendPolicy =emailSendPolicyService.queryEmailSendPolicyInfo();
		//资源申请设置为不发送 直接返回
		if(emailSendPolicy.getIsRsapplySend()==0)
			return new SuccessResponse();
		//查出所有集团管理员
		List<WebUser> list=searchPersonService.findSuperAdmins();
		//拼接发送邮件地址数组 begin
		List<String> toAddressArray=new ArrayList<String>();
		for (WebUser webUser : list) {
			if(webUser.getEmail()!=null && !"".equals(webUser.getEmail()))
				toAddressArray.add(webUser.getEmail());
		}
		//拼接发送邮件地址数组 end
		//发送邮件
		try{
			mailSend.mailSend(mail.getTitle(), mail.getContent(), toAddressArray);
		}catch(Exception e){
			return new FailedResponse("邮箱发送失败！");
		}
		return new SuccessResponse();
	}
}
