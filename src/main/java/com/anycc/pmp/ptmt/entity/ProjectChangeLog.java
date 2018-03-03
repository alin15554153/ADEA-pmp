
package com.anycc.pmp.ptmt.entity;


import com.anycc.commmon.web.entity.WebUser;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name="projectsn_change_log")
public class ProjectChangeLog implements Serializable {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid")
    private String id;
    
	/**
	 * 阶段编号
	 */
    @Column(name="sid")
    private String sid;
    
	/**
	 * 阶段字典值
	 */
    @Column(name="sname",length=50)
    private String sname;
    
    /**
	 * 阶段名称
	 */
	@Transient
	private String stageName;
    
	/**
	 * 阶段序号 从1开始
	 */
    @Column(name="sseq",length=10)
    private Integer sseq;
    
	/**
	 * 阶段预计开始日期
	 */
    @Column(name="expbegintime",length=10)
	@Temporal(TemporalType.DATE)
    private Date expbegintime;
    
	/**
	 * 阶段预计结束日期
	 */
    @Column(name="expendtime",length=10)
	@Temporal(TemporalType.DATE)
    private Date expendtime;
    
	/**
	 * 阶段实际开始日期
	 */
    @Column(name="actbegintime",length=10)
	@Temporal(TemporalType.DATE)
    private Date actbegintime;
    
	/**
	 * 阶段实际结束日期
	 */
    @Column(name="actendtime",length=10)
	@Temporal(TemporalType.DATE)
    private Date actendtime;
    
	/**
	 * 1:阶段 2:里程碑
	 */
    @Column(name="type",length=10)
    private Integer type;
    
	/**
	 * 项目编号
	 */
    @Column(name="pid",length=10)
    private String pid;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "pid", referencedColumnName = "id", insertable = false, updatable = false)
    private Project project;
    
	/**
	 * 变更原因
	 */
    @Column(name="change_reason",nullable=false, length=500)
    private String changeReason;
    
	/**
	 * 变更人
	 */
    @Column(name="change_userid",nullable=false, length=10)
    private Integer changeUserid;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "change_userid", insertable = false, updatable = false)
    private WebUser webUser;
    
	/**
	 * 变更时间
	 */
    @Column(name="change_time",nullable=false, length=10)
	@Temporal(TemporalType.DATE)
    private Date changeTime;

    /**
     * 变更前阶段字典值

     */
    @Column(name="bfchange_name", length=50)
    private String bfchangeType;
    
    /**
     * 变更前阶段名称

     */
    @Transient
    private String bfchangeTypeName;
    
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
	 * @param sid the sid to set
	 */
    public void setSid(String sid){
       this.sid = sid;
    }
    
    /**
     * @return the sid 
     */
    public String getSid(){
       return this.sid;
    }
	
	/**
	 * @param sname the sname to set
	 */
    public void setSname(String sname){
       this.sname = sname;
    }
    
    /**
     * @return the sname 
     */
    public String getSname(){
       return this.sname;
    }
	
	/**
	 * @param sseq the sseq to set
	 */
    public void setSseq(Integer sseq){
       this.sseq = sseq;
    }
    
    /**
     * @return the sseq 
     */
    public Integer getSseq(){
       return this.sseq;
    }
	
	/**
	 * @param expbegintime the expbegintime to set
	 */
    public void setExpbegintime(Date expbegintime){
       this.expbegintime = expbegintime;
    }
    
    /**
     * @return the expbegintime 
     */
    public Date getExpbegintime(){
       return this.expbegintime;
    }
	
	/**
	 * @param expendtime the expendtime to set
	 */
    public void setExpendtime(Date expendtime){
       this.expendtime = expendtime;
    }
    
    /**
     * @return the expendtime 
     */
    public Date getExpendtime(){
       return this.expendtime;
    }
	
	/**
	 * @param actbegintime the actbegintime to set
	 */
    public void setActbegintime(Date actbegintime){
       this.actbegintime = actbegintime;
    }
    
    /**
     * @return the actbegintime 
     */
    public Date getActbegintime(){
       return this.actbegintime;
    }
	
	/**
	 * @param actendtime the actendtime to set
	 */
    public void setActendtime(Date actendtime){
       this.actendtime = actendtime;
    }
    
    /**
     * @return the actendtime 
     */
    public Date getActendtime(){
       return this.actendtime;
    }
	
	/**
	 * @param type the type to set
	 */
    public void setType(Integer type){
       this.type = type;
    }
    
    /**
     * @return the type 
     */
    public Integer getType(){
       return this.type;
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
	
	/**
	 * @param changeReason the changeReason to set
	 */
    public void setChangeReason(String changeReason){
       this.changeReason = changeReason;
    }
    
    /**
     * @return the changeReason 
     */
    public String getChangeReason(){
       return this.changeReason;
    }
	
	/**
	 * @param changeUserid the changeUserid to set
	 */
    public void setChangeUserid(Integer changeUserid){
       this.changeUserid = changeUserid;
    }
    
    /**
     * @return the changeUserid 
     */
    public Integer getChangeUserid(){
       return this.changeUserid;
    }
	
	/**
	 * @param changeTime the changeTime to set
	 */
    public void setChangeTime(Date changeTime){
       this.changeTime = changeTime;
    }
    
    /**
     * @return the changeTime 
     */
    public Date getChangeTime(){
       return this.changeTime;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }

    public String getBfchangeType() {
        return bfchangeType;
    }

    public void setBfchangeType(String bfchangeType) {
        this.bfchangeType = bfchangeType;
    }
    

    public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public String getBfchangeTypeName() {
		return bfchangeTypeName;
	}

	public void setBfchangeTypeName(String bfchangeTypeName) {
		this.bfchangeTypeName = bfchangeTypeName;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
