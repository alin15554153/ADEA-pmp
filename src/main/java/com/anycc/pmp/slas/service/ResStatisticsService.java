package com.anycc.pmp.slas.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.slas.entity.ResStatistics;

/**
 * Created by Shibin on 2016/3/17.
 */
public interface ResStatisticsService {

    public DTData<ResStatistics> queryByPaging(Project project, DTPager pager);

    Response queryResStatisticsByAreaAndCompany(ResStatistics resStatistics);
    
    ResponseEntity<byte[]> exportResourceList(HttpServletRequest request) throws IOException;
}
