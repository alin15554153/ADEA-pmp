package com.anycc.pmp.comm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="email_send_policy")
public class EmailSendPolicy {
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * smtp服务器
	 */
	@Column(name="smtp_server")
	private String smtpServer;
	/**
	 * 端口号
	 */
	private String port;
	/**
	 * 邮箱账户
	 */
	private String account;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 项目信息变更是否发 1:发送 0:不发送
	 */
	@Column(name="is_pjchange_send")
	private long isPjchangeSend;
	/**
	 * 项目成员变更是否发 1:发送 0:不发送
	 */
	@Column(name="is_pjmemchange_send")
	private long isPjmemchangeSend;
	/**
	 * 资源申请是否发 1:发送 0:不发送
	 */
	@Column(name="is_rsapply_send")
	private long isRsapplySend;
	/**
	 * 阶段变更是否发 1:发送 0:不发送
	 */
	@Column(name="is_pjstage_send")
	private long isPjstageSend;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getIsPjchangeSend() {
		return isPjchangeSend;
	}
	public void setIsPjchangeSend(long isPjchangeSend) {
		this.isPjchangeSend = isPjchangeSend;
	}
	public long getIsPjmemchangeSend() {
		return isPjmemchangeSend;
	}
	public void setIsPjmemchangeSend(long isPjmemchangeSend) {
		this.isPjmemchangeSend = isPjmemchangeSend;
	}
	public long getIsRsapplySend() {
		return isRsapplySend;
	}
	public void setIsRsapplySend(long isRsapplySend) {
		this.isRsapplySend = isRsapplySend;
	}
	public long getIsPjstageSend() {
		return isPjstageSend;
	}
	public void setIsPjstageSend(long isPjstageSend) {
		this.isPjstageSend = isPjstageSend;
	}
	
}
