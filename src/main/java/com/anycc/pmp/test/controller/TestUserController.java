package com.anycc.pmp.test.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/management/pmp/test/user")
public class TestUserController {

    private static final String LIST = "/management/pmp/test/user/userList";
    /**
     * 跳转到test管理页面
     *
     * @return
     */
    @RequiresPermissions("User:view")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String index() {
        return LIST;
    }
}
