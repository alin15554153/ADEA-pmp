
package com.anycc.pmp.ptmt.entity;

import com.anycc.commmon.web.entity.WebOrganization;
import com.anycc.commmon.web.entity.WebUser;
import com.anycc.common.JsonDict;
import com.anycc.pmp.comm.entity.WebArea;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="projectinfo")
public class Project implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid")
    private String id;
	/**
	 * 项目名称
	 */
    @Column(name="name",nullable=false, length=255)
    private String name;
    
	/**
	 * 项目编号
	 */
    @Column(name="code",nullable=false, length=50)
    private String code;
    
	/**
	 * 项目当前阶段
	 */
    /*@Column(name="stage",nullable=false, length=10)
    private String stage;*/
    
    @OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="stage")
	private ProjectStage projectstage;
    
	/**
	 * 项目进度
	 */
    @Column(name="progress",length=18)
    private Double progress;
    
	/**
	 * 项目等级
	 */
    @Column(name="level",nullable=false, length=1)
    private String level;
    
	/**
	 * 项目类别
	 */
    @Column(name="type",nullable=false, length=10)
    private Integer type;
    
	/**
	 * 项目性质
	 */
    @Column(name="nature",nullable=false, length=1)
    private String nature;
    
	/**
	 * 项目经理
	 */
//    @Column(name="manager",nullable=false, length=10)
//    private String manager;
    @ManyToOne(fetch=FetchType.EAGER)
   	@JoinColumn(name="manager")
   	private WebUser manager;
    
    public WebUser getManager() {
		return manager;
	}

	public void setManager(WebUser manager) {
		this.manager = manager;
	}

	/**
	 * 项目负责人
	 */
    @Column(name="director",nullable=false, length=10)
    private String director;
    
    /**
	 * 项目复核人
	 */
    @Column(name="checker",nullable=false, length=10)
    private String checker;
    
    /**
	 * 项目金额
	 */
    @Column(name="amt",length=12)
    private Double amt;
    
	/**
	 * 1进行中2已中标3未中标3已放弃5完成
	 */
    @Column(name="status",nullable=false, length=1)
    private String status;
    
	/**
	 * 备注
	 */
    @Column(name="remark",length=255)
    private String remark;
    
	/**
	 * 项目所属地区
	 */
    /*@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "area_id", insertable = false, updatable = false)
    private WebArea webArea;*/
    
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="area_id")
	private WebArea webArea;
    
	/**
	 * 项目所属公司
	 */
    /*@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "org_id", insertable = false, updatable = false)
    private WebOrganization webOrganization;*/
    @ManyToOne(fetch=FetchType.EAGER)
   	@JoinColumn(name="org_id")
   	private WebOrganization webOrganization;
    
	public ProjectStage getProjectstage() {
		return projectstage;
	}

	public void setProjectstage(ProjectStage projectstage) {
		this.projectstage = projectstage;
	}

	/**
	 * 操作人
	 */
    /*@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "cid", insertable = false, updatable = false)
    private WebUser webUser;*/
    @ManyToOne(fetch=FetchType.EAGER)
   	@JoinColumn(name="cid")
   	private WebUser webUser;
    
	/**
	 * 建立时间
	 */
    @Column(name="ctime",nullable=false, length=10)
	@Temporal(TemporalType.DATE)
    private Date ctime;
    
    /**
	 * 删除状态(0:可用  1:不可用) 默认0
	 */
    @Column(name="del_tag",length=11)
    private Integer delTag=0;

	/**
	 * 项目来源
	 */
	@Column(name="project_source",length =255)
	private String projectSource;

	/**
	 * 干系人
	 */
	@Column(name="stakeholder",length=255)
	private String stakeholder;

	/**
	 * 签约概率
	 */
	@Column(name="signing_probability",length=18)
	private Double signingProbability;

	@OneToMany(mappedBy = "pid",fetch=FetchType.LAZY,cascade = CascadeType.REFRESH)
    @OrderBy("followTime desc")
	List<ProjectFollow> followList = new ArrayList<>();

	/**
	 * 项目总包
	 */
	@Column(name="total_package")
	private Integer totalPackage;

	@Column(name="sub_package",length=50)
	private String subPackage;

	/**
	 * 预计签约时间
	 */
	@Column(name="expected_time",nullable=false, length=10)
	@Temporal(TemporalType.DATE)
	private Date expectedTime;

	@Column(name="assistance_unit",length=500)
	private String assistanceUnit;

	@Column(name="assistance_content",length=500)
	private String assistanceContent;

	@Column(name="signing_budget",length=12)
	private Double signingBudget;



	public List<ProjectFollow> getFollowList() {
		return followList;
	}

	public void setFollowList(List<ProjectFollow> followList) {
		this.followList = followList;
	}

	@Transient
    private String uupid;
    
    @Transient
    private String perids;
    
    @Transient
    private String pernames;
    
    @Transient
    private String orgids;
    
    @Transient
    private String levelName;
    
    @Transient
    private String statusName;
    
    @Transient
    private String stageName;
    
    @Transient
    private String starttime;
    
    @Transient
    private String endtime;

	@Transient
	private String totalPackageValue;
    
    
    public String getUupid() {
		return uupid;
	}

	public void setUupid(String uupid) {
		this.uupid = uupid;
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
	 * @param code the code to set
	 */
    public void setCode(String code){
       this.code = code;
    }
    
    /**
     * @return the code 
     */
    public String getCode(){
       return this.code;
    }
	

	
	/**
	 * @param progress the progress to set
	 */
    public void setProgress(Double progress){
       this.progress = progress;
    }
    
    /**
     * @return the progress 
     */
    public Double getProgress(){
       return this.progress;
    }
	
	/**
	 * @param level the level to set
	 */
    public void setLevel(String level){
       this.level = level;
    }
    
    /**
     * @return the level 
     */
    public String getLevel(){
       return this.level;
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
	 * @param nature the nature to set
	 */
    public void setNature(String nature){
       this.nature = nature;
    }
    
    /**
     * @return the nature 
     */
    public String getNature(){
       return this.nature;
    }
	
	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
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
	 * @param ctime the ctime to set
	 */
    public void setCtime(Date ctime){
       this.ctime = ctime;
    }
    
    /**
     * @return the ctime 
     */
    public Date getCtime(){
       return this.ctime;
    }

	public WebUser getWebUser() {
		return webUser;
	}

	public void setWebUser(WebUser webUser) {
		this.webUser = webUser;
	}

	public WebArea getWebArea() {
		return webArea;
	}

	public void setWebArea(WebArea webArea) {
		this.webArea = webArea;
	}

	public WebOrganization getWebOrganization() {
		return webOrganization;
	}

	public void setWebOrganization(WebOrganization webOrganization) {
		this.webOrganization = webOrganization;
	}

	public String getPerids() {
		return perids;
	}

	public void setPerids(String perids) {
		this.perids = perids;
	}

	public String getPernames() {
		return pernames;
	}

	public void setPernames(String pernames) {
		this.pernames = pernames;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public Integer getDelTag() {
		return delTag;
	}

	public void setDelTag(Integer delTag) {
		this.delTag = delTag;
	}

	public String getOrgids() {
		return orgids;
	}

	public void setOrgids(String orgids) {
		this.orgids = orgids;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getProjectSource() {
		return projectSource;
	}

	public void setProjectSource(String projectSource) {
		this.projectSource = projectSource;
	}

	public String getStakeholder() {
		return stakeholder;
	}

	public void setStakeholder(String stakeholder) {
		this.stakeholder = stakeholder;
	}

	public Double getSigningProbability() {
		return signingProbability;
	}

	public void setSigningProbability(Double signingProbability) {
		this.signingProbability = signingProbability;
	}

	public Integer getTotalPackage() {
		return totalPackage;
	}

	public void setTotalPackage(Integer totalPackage) {
		this.totalPackage = totalPackage;
	}

	public String getTotalPackageValue() {
		return totalPackageValue;
	}

	public void setTotalPackageValue(String totalPackageValue) {
		this.totalPackageValue = totalPackageValue;
	}

	public String getSubPackage() {
		return subPackage;
	}

	public void setSubPackage(String subPackage) {
		this.subPackage = subPackage;
	}

	public Date getExpectedTime() {
		return expectedTime;
	}

	public void setExpectedTime(Date expectedTime) {
		this.expectedTime = expectedTime;
	}

	public String getAssistanceUnit() {
		return assistanceUnit;
	}

	public void setAssistanceUnit(String assistanceUnit) {
		this.assistanceUnit = assistanceUnit;
	}

	public String getAssistanceContent() {
		return assistanceContent;
	}

	public void setAssistanceContent(String assistanceContent) {
		this.assistanceContent = assistanceContent;
	}

	public Double getSigningBudget() {
		return signingBudget;
	}

	public void setSigningBudget(Double signingBudget) {
		this.signingBudget = signingBudget;
	}
}
