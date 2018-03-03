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

import com.anycc.commmon.web.entity.WebUser;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "project_mission_member")
public class ProMissionMember implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 项目编号
     */
    @Column(name = "pid", length = 32)
    private String pid;

    /**
     * 项目阶段
     */
    @Column(name = "sid", length = 32)
    private String sid;

    /**
     * 阶段任务编号
     */
    @Column(name = "mid", length = 32)
    private String mid;

    /**
     * 用户编号
     */
    @Column(name = "uid", length = 32)
    private Integer uid;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "uid", insertable = false, updatable = false)
    private WebUser webUser;

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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }

}
