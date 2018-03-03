package com.anycc.pmp.slas.entity;

import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.rsmt.entity.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shibin on 2016/3/17.
 */
public class ResStatistics implements Serializable {

    private Long areaId;
    private Long companyId;
    private String area;
    private String company;
    private String name;
    private String resCount;
    private String totalTimes;
    private String id;

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResCount() {
        return resCount;
    }

    public void setResCount(String resCount) {
        this.resCount = resCount;
    }

    public String getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(String totalTimes) {
        this.totalTimes = totalTimes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
