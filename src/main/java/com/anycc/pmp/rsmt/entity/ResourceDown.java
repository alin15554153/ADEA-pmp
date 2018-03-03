package com.anycc.pmp.rsmt.entity;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="t_resource_down")//资源申请表
public class ResourceDown implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid")
    @Column(name="aid",nullable=false, length=32)
    private String id;
    
	/**
	 * 资源ID
	 */
    @Column(name="id",nullable=false, length=10)
    private String rid;

    /**
     * 资源实体
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "id", referencedColumnName = "id" , insertable = false, updatable = false)
    private Resource resource;

    /**
	 * 申请人ID
	 */
    @Column(name="uid",nullable=false, length=10)
    private Integer uid;

	/**
	 * 申请时间
	 */
    @Column(name="atime",nullable=false, length=10)
	@Temporal(TemporalType.DATE)
    private Date atime;

    /**
     * 申请时间字符串
     */
    @Transient
    private String atimeString;
    
	/**
	 * 申请原因
	 */
    @Column(name="aremark",nullable=false, length=255)
    private String aremark;
    
	/**
	 * 审核状态 ==> 1:待审核 2:PM通过 3:PM不通过 4:MG通过 5:MG不通过 6:SMG通过 7:SMG不通过
	 */
    @Column(name="status",nullable=false, length=1)
    private String status;
    
	/**
	 * 流程类型：1:pm-mg(项目经理-管理员) 2:pm-mg-smg(项目经理-管理员-超级管理员)
	 */
    @Column(name="process_type",length=10)
    private Integer processType;

    /**
     * 资源名称
     */
    @Transient
    private String resourceName;

    /**
     * 资源类别(字典)
     */
    @Transient
    private String resourceType;

    /**
     * 资源类别(名称)
     */
    @Transient
    private String resourceTypeName;

    /**
     * 项目名称
     */
    @Transient
    private String projectName;

    /**
     * 阶段ID
     */
    @Transient
    private String stageId;

    /**
     * 阶段名称
     */
    @Transient
    private String stageName;

    /**
     * 用户名(真实姓名)
     */
    @Transient
    private String userName;

    /**
     * 机构ID
     */
    @Transient
    private String orgId;

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

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Date getAtime() {
        return atime;
    }

    public void setAtime(Date atime) {
        this.atime = atime;
    }

    public String getAtimeString() {
        return atimeString;
    }

    public void setAtimeString(String atimeString) {
        this.atimeString = atimeString;
    }

    public String getAremark() {
        return aremark;
    }

    public void setAremark(String aremark) {
        this.aremark = aremark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProcessType() {
        return processType;
    }

    public void setProcessType(Integer processType) {
        this.processType = processType;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceTypeName() {
        return resourceTypeName;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
