/**  
 * @Title: MyAuthenticator.java 
 * @Package common.method.mail 
 * @Description: TODO 
 * @author 葛钰鹏
 * @date 2014年11月15日 下午10:45:29 
 * @version V1.0  
 */

package com.anycc.pmp.mail;

import javax.mail.*;

/**
 * @ClassName: MyAuthenticator
 * @Description:
 * @author: 葛钰鹏
 * @date 2014年11月15日 下午10:45:29
 * 
 */

public class MyAuthenticator extends Authenticator{
	String userName = null;
	String password = null;

	public MyAuthenticator() {
	}

	public MyAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}
