package com.anycc.pmp.comm.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anycc.common.dto.Response;
import com.anycc.log.Log;
import com.anycc.log.LogMessageObject;
import com.anycc.log.impl.LogUitls;
import com.anycc.pmp.comm.entity.EmailSendPolicy;
import com.anycc.pmp.comm.service.EmailSendPolicyService;

@Controller
@RequestMapping("/management/pmp/comm/email/emailSendingPolicy")
public class EmailSendPolicyController {
	@Autowired
	private EmailSendPolicyService emailSendPolicyService;
	private static final String LIST = "/management/comm/email/emailSendingPolicy";

	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df,
				true));
	}

	/**
	 * 跳转到emailSendingPolicy管理页面
	 * 
	 * @return
	 */
	@RequiresPermissions("EmailSendPolicy:view")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String index() {
		return LIST;
	}
	
	/**
	 * 查询邮件策略信息
	 * @return
	 */
	@RequestMapping(value = "queryEmailSendPolicy", method = RequestMethod.POST)
	@ResponseBody
	public Response queryEmailSendPolicy() {
		return emailSendPolicyService.queryEmailSendPolicy();
	}
	
	/**
	 * 更新邮件策略信息
	 * 
	 * @param roleManager
	 * @return
	 */
	@Log(message = "修改了id={0}的邮件发送策略。")
	@RequiresPermissions("EmailSendPolicy:edit")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Response update(EmailSendPolicy emailSendPolicy) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(
				new Object[] { emailSendPolicy.getId() }));
		return emailSendPolicyService.update(emailSendPolicy);
	}

}
