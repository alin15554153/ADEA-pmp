package com.anycc.pmp.api.controller;

import com.anycc.common.dto.ObjectResponse;
import com.anycc.common.dto.Response;
import com.anycc.pmp.api.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by DELL on 2016/11/1.
 */
@Controller
@RequestMapping("/home/api")
public class ApiController {

    @Autowired
    ApiService apiService;

    /**
     * 大屏首页的项目进度
     * @return
     */
    @RequestMapping(value = "{md5}/projectProgress")
    @ResponseBody
    @CrossOrigin
    public Response projectProgress(@PathVariable("md5") String md5,String param) {
        Map<String,Object> result = apiService.check(param,md5);
        if((int)result.get("code")!=200){
            return  new ObjectResponse<>(result);
        }
        result.put("result",apiService.projectProgress()) ;
        return  new ObjectResponse<>(result);
    }


}
