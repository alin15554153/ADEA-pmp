package com.anycc.pmp.ptmt.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by DELL1 on 2016/7/15.
 */
@Entity
@Table(name="project_follow")
public class ProjectFollow implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid")
    private String id;

    @Column(name="pid",length=32)
    private String pid;

    @Column(name="content",length=5000)
    private String content;

    @Column(name="follow_time",nullable=false, length=10)
    @Temporal(TemporalType.DATE)
    private Date followTime;

    @Column(name="username",length=255)
    private String username;

    @Column(name="create_time",nullable=false, length=10)
    @Temporal(TemporalType.DATE)
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getFollowTime() {
        return followTime;
    }

    public void setFollowTime(Date followTime) {
        this.followTime = followTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
