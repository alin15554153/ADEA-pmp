
package com.anycc.pmp.ptmt.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.anycc.commmon.web.entity.WebUser;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="projectinfo")
public class RoleProject implements Serializable {
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
	 * 项目经理
	 */
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
	 * 删除状态(0:可用  1:不可用) 默认0
	 */
    @Column(name="del_tag",length=11)
    private Integer delTag=0;
    
    /**
   	 * 存储角色名
   	 */
    @Transient
    private String rnames;
    
    @OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="pid")
	private List<RoleManager> roleList=new ArrayList<RoleManager>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDelTag() {
		return delTag;
	}

	public void setDelTag(Integer delTag) {
		this.delTag = delTag;
	}

	public List<RoleManager> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleManager> roleList) {
		this.roleList = roleList;
	}
	
	public String getRnames() {
		return rnames;
	}

	public void setRnames(String rnames) {
		this.rnames = rnames;
	}

	
}
