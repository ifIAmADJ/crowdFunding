package com.iproject.crowd.service.api;

import com.github.pagehelper.PageInfo;
import com.iproject.crowd.entity.Admin;

import java.util.List;


public interface AdminService {

    void saveAdmin(Admin admin);
    List<Admin> getAll();
    Admin getAdminByLoginAcct(String loginAcct,String loginPlainPwd);
    PageInfo<Admin> getAdminPageInfo(String keyword,Integer pageNum,Integer pageSize);

    void remove(Integer adminId);

    Admin getAdminById(Integer id);

    void update(Admin admin);
}
