package com.anycc.pmp.comm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sys_area")
public class WebArea implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
	/**
	 * 地区名称
	 */
    @Column(nullable=false, length=30)
    private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    
    
}
    