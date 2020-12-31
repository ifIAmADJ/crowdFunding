package com.iproject.crowd.service.impl;

import com.iproject.crowd.entity.Admin;
import com.iproject.crowd.mapper.AdminMapper;
import com.iproject.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

//  后续解决这个问题:猜测 @MapperScan 和 @Mapper 注解将解决这个问题 ?

    @Autowired
    public AdminServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public void saveAdmin(Admin admin) {
        adminMapper.insert(admin);
    }

    @Override
    public List<Admin> getAll() {
//      该方法传入 null 则相当于进行无条件查询
        return adminMapper.selectByExample(null);
    }

}
