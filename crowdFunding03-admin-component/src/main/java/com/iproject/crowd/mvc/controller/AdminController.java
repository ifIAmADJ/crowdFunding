package com.iproject.crowd.mvc.controller;


import com.github.pagehelper.PageInfo;
import com.iproject.crowd.constant.ProjectConstant;
import com.iproject.crowd.entity.Admin;
import com.iproject.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;

@Controller
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping("/admin/get/page.html")
    public String getAdminPageInfo(
            // 允许提供空的字符串值。
            @RequestParam(value = "keyword",defaultValue = "") String key,
            @RequestParam(value = "pageNumber",defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            ModelMap modelMap
    ){

        PageInfo<Admin> adminPageInfo = adminService.getAdminPageInfo(key, pageNumber, pageSize);

        // jsp 可以通过 ${RequestScope.pageInfo.list} 获取。
        modelMap.addAttribute(ProjectConstant.ATTR_NAME_PAGE_INFO,adminPageInfo);
        return "admin-page";
    }

    @RequestMapping("/admin/do/logout.html")
    public String Logout(HttpSession session){

        //退出登录的原理是：只需要将 sessionScope 内的信息清除即可。

        session.invalidate();

        return "redirect:/admin/to/login/page.html";
    }


    @RequestMapping("/admin/do/login.html")
    public String doLogin(
            @RequestParam("loginAcct") String loginAcct,
            @RequestParam("loginPwd") String loginPwd,
            HttpSession session
            ) {

        Admin admin = adminService.getAdminByLoginAcct(loginAcct, loginPwd);

        session.setAttribute(ProjectConstant.ATTR_NAME_LOGIN_ADMIN,admin);

        // 重定向到
        return "redirect:/admin/to/main/page.html";
    }


}
