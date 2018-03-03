package com.anycc.pmp.comm.service;

import com.anycc.common.dto.Response;
import com.anycc.pmp.comm.entity.EmailSendPolicy;

public interface EmailSendPolicyService {

	/**查询邮件策略信息
	 * @return Response
	 */
	Response queryEmailSendPolicy();
	
	/**查询邮件策略信息(获取stmp服务器、邮箱等信息)
	 * @return EmailSendPolicy
	 */
	EmailSendPolicy queryEmailSendPolicyInfo();

	/**更新邮件策略信息(更新stmp服务器、邮箱等信息)
	 * @return Response
	 */
	Response update(EmailSendPolicy emailSendPolicy);

}
