package com.anycc.pmp.comm.entity;

public class Mail {
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 项目编号
	 */
	private String projectId;
	/**
	 * 角色编号
	 */
	private Long roleId;

	/**
	 * 用户编号
	 */
	private Long userId;
	/**
	 * 机构编号
	 */
	private Long orgId;
	
	/**
	 * 类型 1：项目信息变更  2：人员信息变更  3：项目阶段信息变更
	 */
	private Long type;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
}
