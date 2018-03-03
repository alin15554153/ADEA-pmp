package com.anycc.pmp.ptmt.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "projectrole")
public class RoleManager implements Serializable {
	/**
	 * 阶段编号
	 */
	@Transient
	private String sid;

	/**
	 * 角色编号
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String rid;
	/**
	 * 角色名称(现存字典code)
	 */
	@Column(name = "rname", nullable = false, length = 50)
	private String rname;

	/**
	 * 角色名称(现存字典code)
	 */
	@Transient
	private String rnames;

	public String getRnames() {
		return rnames;
	}

	public void setRnames(String rnames) {
		this.rnames = rnames;
	}

	/**
	 * 角色说明
	 */
	@Column(name = "rdescription", nullable = false, length = 255)
	private String rdescription;

	/**
	 * 项目编号
	 */
	@Column(name = "pid", nullable = false, length = 11)
	private String pid;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(referencedColumnName = "id", name = "pid", unique = false, insertable = false, updatable = false)
	private Project project;

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public String getRdescription() {
		return rdescription;
	}

	public void setRdescription(String rdescription) {
		this.rdescription = rdescription;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
