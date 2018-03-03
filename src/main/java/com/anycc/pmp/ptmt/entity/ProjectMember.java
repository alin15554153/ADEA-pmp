package com.anycc.pmp.ptmt.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.anycc.commmon.web.entity.WebUser;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "projectmember")
public class ProjectMember {
	/**
	 * 成员编号
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String mid;

	/**
	 * 成员描述
	 */
	@Column(name = "mdescription", length = 50)
	private String mdescription;

	/**
	 * 项目编号
	 */
	@Column(name = "pid", length = 11)
	private String pid;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(referencedColumnName = "id", name = "pid", unique = false, insertable = false, updatable = false)
	private Project project;

	/**
	 * 角色编号
	 */
	@Column(name = "rid", length = 11)
	private String rid;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(referencedColumnName = "rid", name = "rid", unique = false, insertable = false, updatable = false)
	private RoleManager roleManager;

	/**
	 * 用户编号
	 */
	@Column(name = "uid", length = 11)
	private String uid;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(referencedColumnName = "id", name = "uid", unique = false, insertable = false, updatable = false)
	private WebUser webUser;

	public String getMdescription() {
		return mdescription;
	}

	public void setMdescription(String mdescription) {
		this.mdescription = mdescription;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public WebUser getWebUser() {
		return webUser;
	}

	public void setWebUser(WebUser webUser) {
		this.webUser = webUser;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
