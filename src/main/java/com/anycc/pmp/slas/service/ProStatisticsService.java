package com.anycc.pmp.slas.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.pmp.slas.entity.ProStatistics;


public interface ProStatisticsService {

    public DTData<ProStatistics> queryByPaging(ProStatistics proStatistics, DTPager pager);
    
    ResponseEntity<byte[]> exportResourceList(HttpServletRequest request) throws IOException;
}
