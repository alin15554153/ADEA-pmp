/**  
 * @Title: MailSend.java 
 * @Package common.method.mail 
 * @Description: TODO 
 * @author 葛钰鹏
 * @date 2014年11月15日 下午10:57:10 
 * @version V1.0  
 */

package com.anycc.pmp.mail;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anycc.pmp.comm.entity.EmailSendPolicy;
import com.anycc.pmp.comm.service.EmailSendPolicyService;

/**
 * @ClassName: MailSend
 * @Description:
 * @author: 葛钰鹏
 * @date 2014年11月15日 下午10:57:10
 * 
 */
@Component
public class MailSend {
	@Autowired
	private EmailSendPolicyService emailSendPolicyService;
	/**
	 * 发送邮件
	 * @param title  邮件抬头
	 * @param context 邮件内容
	 * @param toAddress 发送人员
     */
	public void mailSend(String title, String context,String  toAddress) {
		//获取邮件发送策略信息
		EmailSendPolicy emailSendPolicy=emailSendPolicyService.queryEmailSendPolicyInfo();
		MailSenderInfo mailInfo = new MailSenderInfo();
		//设置smtp服务器
		mailInfo.setMailServerHost(emailSendPolicy.getSmtpServer());
		//设置端口
		mailInfo.setMailServerPort(emailSendPolicy.getPort());
		mailInfo.setValidate(true);
		//设置邮箱用户
		mailInfo.setUserName(emailSendPolicy.getAccount());
		//设置邮箱密码
		mailInfo.setPassword(emailSendPolicy.getPassword());
		//设置发送邮箱
		mailInfo.setFromAddress(emailSendPolicy.getAccount());
		mailInfo.setToAddressArray(new String []{toAddress});
		try {
			mailInfo.setSubject(MimeUtility.encodeText(title,"utf-8",null).toString());
		} catch (UnsupportedEncodingException e) {
			mailInfo.setSubject(title);
			e.printStackTrace();
		}
		mailInfo.setContent(context);
		SimpleMailSender sms = new SimpleMailSender();
		sms.sendHtmlMail(mailInfo);// 发送html格式
	}


	public void mailSend(String title, String context,List<String> toAddressArray) {
		//获取邮件发送策略信息
		EmailSendPolicy emailSendPolicy=emailSendPolicyService.queryEmailSendPolicyInfo();
		MailSenderInfo mailInfo = new MailSenderInfo();
		//设置smtp服务器
		mailInfo.setMailServerHost(emailSendPolicy.getSmtpServer());
		//设置端口
		mailInfo.setMailServerPort(emailSendPolicy.getPort());
		mailInfo.setValidate(true);
		//设置邮箱用户
		mailInfo.setUserName(emailSendPolicy.getAccount());
		//设置邮箱密码
		mailInfo.setPassword(emailSendPolicy.getPassword());
		//设置发送邮箱
		mailInfo.setFromAddress(emailSendPolicy.getAccount());

		String [] addressArray = new String [toAddressArray.size()];
		for(int index = 0 ; index <toAddressArray.size() ;index++ ){
			addressArray[index] = toAddressArray.get(index);
		}
		mailInfo.setToAddressArray(addressArray);
		try {
			mailInfo.setSubject(MimeUtility.encodeText(title,"utf-8",null).toString());
		} catch (UnsupportedEncodingException e) {
			mailInfo.setSubject(title);
			e.printStackTrace();
		}
		mailInfo.setContent(context);
		SimpleMailSender sms = new SimpleMailSender();
		sms.sendHtmlMail(mailInfo);// 发送html格式
	}
}