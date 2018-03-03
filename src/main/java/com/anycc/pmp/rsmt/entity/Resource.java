
package com.anycc.pmp.rsmt.entity;

import com.anycc.commmon.web.entity.WebUser;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectStage;
import com.anycc.utils.Identities;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name="resource")
public class Resource implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid")
    private String id;
    
	/**
	 * 资源名称
	 */
    @Column(name="name",nullable=false, length=255)
    private String name;
    
	/**
	 * 资源类型
	 */
    @Column(name="type",nullable=false, length=1)
    private String type;

    @Transient
    private String typeName;
    
	/**
	 * 介绍
	 */
    @Column(name="remark",length=255)
    private String remark;
    
	/**
	 * 资源大小
	 */
    @Column(name="size",length=18)
    private Double size;
    
	/**
	 * 资源所属公司
	 */
    @Column(name="belong",length=10)
    private Integer belong;
    
	/**
	 * 路径
	 */
    @Column(name="path",nullable=false, length=255)
    private String path;
    
	/**
	 * 审核状态：默认1未审核,2审核通过，3未通过
	 */
    @Column(name="status",length=1)
    private String status;
    
	/**
	 * 上传人
	 */
    @Column(name="uid",nullable=false, length=10)
    private Integer uid;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "uid", insertable = false, updatable = false)
    private WebUser webUser;

	/**
	 * 上传日期
	 */
    @Column(name="uploaddate",nullable=false, length=10)
	@Temporal(TemporalType.DATE)
    private Date uploaddate;
    
	/**
	 * 下载次数
	 */
    @Column(name="times",length=10)
    private Integer times;
    
	/**
	 * 项目编号
	 */
    @Column(name="pid",length=10)
    private String pid;
    
    /**
	 * 阶段编号
	 */
    @Column(name="sid",length=10)
    private String sid;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "sid", insertable = false, updatable = false)
    private ProjectStage projectStage;

    public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "pid", referencedColumnName = "id" , insertable = false, updatable = false)
    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectStage getProjectStage() {
        return projectStage;
    }

    public void setProjectStage(ProjectStage projectStage) {
        this.projectStage = projectStage;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }

    /**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @param name the name to set
	 */
    public void setName(String name){
       this.name = name;
    }
    
    /**
     * @return the name 
     */
    public String getName(){
       return this.name;
    }
	
	/**
	 * @param type the type to set
	 */
    public void setType(String type){
       this.type = type;
    }
    
    /**
     * @return the type 
     */
    public String getType(){
       return this.type;
    }
	
	/**
	 * @param remark the remark to set
	 */
    public void setRemark(String remark){
       this.remark = remark;
    }
    
    /**
     * @return the remark 
     */
    public String getRemark(){
       return this.remark;
    }
	
	/**
	 * @param size the size to set
	 */
    public void setSize(Double size){
       this.size = size;
    }
    
    /**
     * @return the size 
     */
    public Double getSize(){
       return this.size;
    }
	
	/**
	 * @param belong the belong to set
	 */
    public void setBelong(Integer belong){
       this.belong = belong;
    }
    
    /**
     * @return the belong 
     */
    public Integer getBelong(){
       return this.belong;
    }
	
	/**
	 * @param path the path to set
	 */
    public void setPath(String path){
       this.path = path;
    }
    
    /**
     * @return the path 
     */
    public String getPath(){
       return this.path;
    }
	
	/**
	 * @param status the status to set
	 */
    public void setStatus(String status){
       this.status = status;
    }
    
    /**
     * @return the status 
     */
    public String getStatus(){
       return this.status;
    }
	
	/**
	 * @param uid the uid to set
	 */
    public void setUid(Integer uid){
       this.uid = uid;
    }
    
    /**
     * @return the uid 
     */
    public Integer getUid(){
       return this.uid;
    }
	
	/**
	 * @param uploaddate the uploaddate to set
	 */
    public void setUploaddate(Date uploaddate){
       this.uploaddate = uploaddate;
    }
    
    /**
     * @return the uploaddate 
     */
    public Date getUploaddate(){
       return this.uploaddate;
    }
	
	/**
	 * @param times the times to set
	 */
    public void setTimes(Integer times){
       this.times = times;
    }
    
    /**
     * @return the times 
     */
    public Integer getTimes(){
       return this.times;
    }
	
	/**
	 * @param pid the pid to set
	 */
    public void setPid(String pid){
       this.pid = pid;
    }
    
    /**
     * @return the pid 
     */
    public String getPid(){
       return this.pid;
    }

    @Transient
    private String userName;

    @Transient
    private String dateString;

    @Transient
    private String projectName;

    @Transient
    private String stageName;

    @Transient
    private String areaId;

    @Transient
    private String areaName;

    @Transient
    private String orgName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
