package com.anycc.pmp.rsmt.entity;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="resource_down_check")//资源申请表
public class ResourceDownCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid")
    private String id;
    
	/**
	 * 审核原因
	 */
    @Column(name="sremark",length=255)
    private String sremark;
    
	/**
	 * 审核人ID
	 */
    @Column(name="sid",length=10)
    private Integer sid;

    /**
     * 审核人姓名
     */
    @Transient
    private String sname;
    
	/**
	 * 审核时间
	 */
    @Column(name="stime",length=10)
	@Temporal(TemporalType.DATE)
    private Date stime;
    
	/**
	 * 资源ID
	 */
    @Column(name="rid",length=10)
    private String rid;
    

    public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
    public void setSremark(String sremark){
       this.sremark = sremark;
    }
    
    public String getSremark(){
       return this.sremark;
    }
	
    public void setSid(Integer sid){
       this.sid = sid;
    }
    
    public Integer getSid(){
       return this.sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public void setStime(Date stime){
       this.stime = stime;
    }
    
    public Date getStime(){
       return this.stime;
    }
	
    public void setRid(String rid){
       this.rid = rid;
    }
    
    public String getRid(){
       return this.rid;
    }
}
