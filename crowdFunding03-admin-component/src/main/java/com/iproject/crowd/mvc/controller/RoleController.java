package com.iproject.crowd.mvc.controller;

import com.github.pagehelper.PageInfo;
import com.iproject.crowd.entity.Role;
import com.iproject.crowd.service.api.RoleService;
import com.iproject.crowd.utils.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoleController {

    private final RoleService roleService;

    /**
     * @param roleIds 这里带上了 @RequestBody 注解，意味着前端发送时会将数据携带到请求体内，并且是以 json 字符串的格式。
     * @return 若成功返回 success;
     */
    @ResponseBody
    @RequestMapping("/role/remove/by/id/array.json")
    public ResultEntity<String> removeByRoleIdArray(@RequestBody List<Integer> roleIds){

        if(roleIds.size() == 0) return ResultEntity.failure("空对象");

        roleService.removeRole(roleIds);
        return  ResultEntity.successWithoutData();
    }


    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @ResponseBody
    @RequestMapping("/role/update.json")
    public ResultEntity<String> updateRole(Role role){

        roleService.updateRole(role);

        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/role/save.json")
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
