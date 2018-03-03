package com.anycc.pmp.slas.service;

import com.anycc.pmp.ptmt.entity.Project;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


public interface ProBrowserService {

    List<Project> initCharts(String orgId);
    
    ResponseEntity<byte[]> exportResourceList(HttpServletRequest request) throws IOException;
}
