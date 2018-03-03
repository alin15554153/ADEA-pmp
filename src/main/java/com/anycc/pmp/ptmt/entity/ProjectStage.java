package com.anycc.pmp.ptmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "projectstagedivision")
public class ProjectStage implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String sid;

	/**
	 * 阶段名称,现在存入的是字典ID
	 */
	@Column(name = "sname", length = 50)
	private String sname;

	/**
	 * 阶段名称
	 */
	@Transient
	private String stageName;
	
	/**
	 * 阶段类型
	 */
	@Transient
	private String typeName;

	/**
	 * 阶段序号
	 */
	@Column(name = "sseq", length = 11)
	private Integer sseq;

	/**
	 * 阶段预计开始日期
	 */
	@Column(name = "expbegintime", length = 10)
	@Temporal(TemporalType.DATE)
	private Date expbegintime;

	/**
	 * 阶段预计结束日期
	 */
	@Column(name = "expendtime", length = 10)
	@Temporal(TemporalType.DATE)
	private Date expendtime;

	/**
	 * 阶段实际开始日期
	 */
	@Column(name = "actbegintime", length = 10)
	@Temporal(TemporalType.DATE)
	private Date actbegintime;

	/**
	 * 阶段实际结束日期
	 */
	@Column(name = "actendtime", length = 10)
	@Temporal(TemporalType.DATE)
	private Date actendtime;

	/**
	 * 阶段类型
	 */
	@Column(name = "type", length = 11)
	private Integer type;

	/**
	 * 项目编号
	 */
	@Column(name = "pid", length = 11)
	private String pid;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public Integer getSseq() {
		return sseq;
	}

	public void setSseq(Integer sseq) {
		this.sseq = sseq;
	}

	public Date getExpbegintime() {
		return expbegintime;
	}

	public void setExpbegintime(Date expbegintime) {
		this.expbegintime = expbegintime;
	}

	public Date getExpendtime() {
		return expendtime;
	}

	public void setExpendtime(Date expendtime) {
		this.expendtime = expendtime;
	}

	public Date getActbegintime() {
		return actbegintime;
	}

	public void setActbegintime(Date actbegintime) {
		this.actbegintime = actbegintime;
	}

	public Date getActendtime() {
		return actendtime;
	}

	public void setActendtime(Date actendtime) {
		this.actendtime = actendtime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
