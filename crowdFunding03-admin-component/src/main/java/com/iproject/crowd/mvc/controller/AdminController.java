package com.iproject.crowd.mvc.controller;


import com.github.pagehelper.PageInfo;
import com.iproject.crowd.constant.ProjectConstant;
import com.iproject.crowd.entity.Admin;
import com.iproject.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/admin/update.html")
    public String update(
            Admin admin,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword) {

        adminService.update(admin);

        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(
            @RequestParam("adminId") Integer id,
            ModelMap modelMap
    ) {

        Admin admin = adminService.getAdminById(id);

        // 为了将信息回显
        modelMap.addAttribute("admin", admin);

        return "admin-edit";

    }

    @RequestMapping("/admin/save.html")
    public String save(Admin admin) {
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum=" + Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(
            @PathVariable("adminId") Integer adminId,
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("keyword") String keyword
    ) {

        // 执行删除
        adminService.remove(adminId);

        /*
         * 重点是页面跳转部分。
         *
         * 方案1: return "admin-page";
         * 这种方式没有在 admin-page 视图内设置任何数据 ( 参考 getAdminPageInfo ) 方法，因此页面会不显示数据。
         *
         * 方案2: return "forward:/admin/get/page.html"
         * 这种方式会重复做一次删除操作，没有必要。
         *
         * 方案3: return "redirect:/admin/get/page.html"
         *
         * */
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    @RequestMapping("/admin/get/page.html")
    public String getAdminPageInfo(
            // 允许提供空的字符串值。
            @RequestParam(value = "keyword", defaultValue = "") String key,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            ModelMap modelMap
    ) {

        PageInfo<Admin> adminPageInfo = adminService.getAdminPageInfo(key, pageNumber, pageSize);

        // jsp 可以通过 ${RequestScope.pageInfo.list} 获取。
        modelMap.addAttribute(ProjectConstant.ATTR_NAME_PAGE_INFO, adminPageInfo);
        return "admin-page";
    }

    @RequestMapping("/admin/do/logout.html")
    public String Logout(HttpSession session) {

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

        session.setAttribute(ProjectConstant.ATTR_NAME_LOGIN_ADMIN, admin);

        // 重定向到
        return "redirect:/admin/to/main/page.html";
    }


}
