package com.iproject.crowd.mvc.controller;

import com.iproject.crowd.entity.Admin;
import com.iproject.crowd.entity.Student;
import com.iproject.crowd.service.api.AdminService;
import com.iproject.crowd.utils.ResultEntity;
import com.iproject.crowd.utils.RequestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@Controller
public class TestController {

    private final AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping("test/ssm.html")
    public String testSsm(ModelMap modelMap, HttpServletRequest request) {

        logger.info(RequestHelper.isAjaxRequest(request) + "");

        String s = null;
        System.out.println(s.length());

        List<Admin> adminList = adminService.getAll();

        modelMap.addAttribute("adminList", adminList);

        return "target";
    }

    @RequestMapping("send/array.html")
    public String testReceiveArray1(@RequestParam("array[]") List<Integer> list) {

        list.forEach(System.out::println);

        return "target";
    }

    @ResponseBody
    @RequestMapping(value = "send/array2.html")
    public String testReceiveArray2(@RequestBody List<Integer> array) {

        System.out.println(array);
        return "target";
    }

    @ResponseBody
    @RequestMapping(value = "send/student.json")
    public ResultEntity<Student> testReceiveStudent(@RequestBody Student student,HttpServletRequest request) {

        logger.info(RequestHelper.isAjaxRequest(request) + "");
        logger.info(student.toString());

        return ResultEntity.successWithData(student);
    }


}
