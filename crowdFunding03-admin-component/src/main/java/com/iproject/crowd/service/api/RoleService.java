package com.iproject.crowd.service.api;

import com.github.pagehelper.PageInfo;
import com.iproject.crowd.entity.Role;

public interface RoleService {

    PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize,String keyword);

    void saveRole(Role role);
}
