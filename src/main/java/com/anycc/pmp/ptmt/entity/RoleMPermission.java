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

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "projectrole_permission")
public class RoleMPermission implements Serializable {
	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	/**
	 * 角色ID
	 */
	@Column(name = "rid", length = 11)
	private String rid;

	/**
	 * 阶段ID
	 */
	@Column(name = "sid", length = 11)
	private String sid;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(referencedColumnName = "sid", name = "sid", unique = false, insertable = false, updatable = false)
	private ProjectStage projectStage;

	/**
	 * 项目ID
	 */
	@Column(name = "pid", length = 11)
	private String pid;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(referencedColumnName = "id", name = "pid", unique = false, insertable = false, updatable = false)
	private Project project;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public ProjectStage getProjectStage() {
		return projectStage;
	}

	public void setProjectStage(ProjectStage projectStage) {
		this.projectStage = projectStage;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
