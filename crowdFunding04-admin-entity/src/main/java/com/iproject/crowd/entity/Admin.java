package com.iproject.crowd.entity;

import java.util.Date;

public class Admin {
    private Integer id;

    private String account;

    private String pwd;

    private String email;

    private String name;

    private Date createTime;

    public Admin(Integer id, String account, String pwd, String email, String name, Date createTime) {
        this.id = id;
        this.account = account;
        this.pwd = pwd;
        this.email = email;
        this.name = name;
        this.createTime = createTime;
    }

    public Admin() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}