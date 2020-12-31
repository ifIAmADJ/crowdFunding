package com.iproject.crowd.service.api;

import com.iproject.crowd.entity.Admin;

import java.util.List;


public interface AdminService {

    void saveAdmin(Admin admin);

    List<Admin> getAll();
}
