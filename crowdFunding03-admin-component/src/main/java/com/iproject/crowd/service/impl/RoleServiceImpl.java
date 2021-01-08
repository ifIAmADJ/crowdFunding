package com.iproject.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iproject.crowd.entity.Role;
import com.iproject.crowd.entity.RoleExample;
import com.iproject.crowd.mapper.RoleMapper;
import com.iproject.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {


    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {

        // 1. 开启分页功能
        PageHelper.startPage(pageNum,pageSize);

        List<Role> roles = roleMapper.selectRoleByKeyword(keyword);

        return new PageInfo<>(roles);
    }

    @Override
    public void saveRole(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    @Override
    public void removeRole(List<Integer> roleIdList) {

        // 新将一个 RoleExample
        RoleExample roleExample = new RoleExample();

        // 定义 Criteria
        RoleExample.Criteria criteria = roleExample.createCriteria();
        // 意为 "满足在该列表的所有 id " 都是被执行对象
        criteria.andIdIn(roleIdList);

        roleMapper.deleteByExample(roleExample);
    }
}
