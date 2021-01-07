package com.iproject.crowd.mvc.controller;

import com.github.pagehelper.PageInfo;
import com.iproject.crowd.entity.Role;
import com.iproject.crowd.service.api.RoleService;
import com.iproject.crowd.utils.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ResponseBody
    @RequestMapping("role/save.json")
    public ResultEntity<String> saveRole(Role role){

        roleService.saveRole(role);

        return ResultEntity.successWithoutData();
    }


    @RequestMapping("/role/get/pageInfo.json")
    @ResponseBody
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword", defaultValue = "") String keyword
    ) {

        // 调用 Service 方法获取分页数据
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);
        return ResultEntity.successWithData(pageInfo);
    }

}
