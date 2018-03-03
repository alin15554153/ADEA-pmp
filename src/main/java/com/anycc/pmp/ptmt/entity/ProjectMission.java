package com.anycc.pmp.ptmt.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "project_mission")
public class ProjectMission implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid")
    private String id;
	
	/**
	 * 任务名称
	 */
    @Column(name="mname",nullable=false, length=255)
    private String mname;
    
    /**
	 * 负责人(编号字符串)
	 */
    @Column(name="mmanager",nullable=false, length=255)
    private String mmanager;
    
    /**
	 * 开始时间
	 */
    @Column(name="mstartdate",nullable=false, length=10)
	@Temporal(TemporalType.DATE)
    private Date mstartdate;
    
    /**
	 * 结束时间
	 */
    @Column(name="menddate",nullable=false, length=10)
	@Temporal(TemporalType.DATE)
    private Date menddate;
    
    /**
	 * 进度
	 */
    @Column(name="mprogress",nullable=false, length=255)
    private Double mprogress;
    
    /**
	 * 项目编号
	 */
    @Column(name="pid",length=32)
    private String pid;
    
    /**
	 * 阶段编号
	 */
    @Column(name="sid",length=32)
    private String sid;
    

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "sid", insertable = false, updatable = false)
    private ProjectStage projectStage;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getMmanager() {
		return mmanager;
	}

	public void setMmanager(String mmanager) {
		this.mmanager = mmanager;
	}

	public Date getMstartdate() {
		return mstartdate;
	}

	public void setMstartdate(Date mstartdate) {
		this.mstartdate = mstartdate;
	}

	public Date getMenddate() {
		return menddate;
	}

	public void setMenddate(Date menddate) {
		this.menddate = menddate;
	}

	public Double getMprogress() {
		return mprogress;
	}

	public void setMprogress(Double mprogress) {
		this.mprogress = mprogress;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public ProjectStage getProjectStage() {
		return projectStage;
	}

	public void setProjectStage(ProjectStage projectStage) {
		this.projectStage = projectStage;
	}
    
}
