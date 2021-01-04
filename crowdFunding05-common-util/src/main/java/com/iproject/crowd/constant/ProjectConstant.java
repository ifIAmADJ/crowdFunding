package com.iproject.crowd.constant;

/**
 * 保存一些项目中经常使用的值，将它声明为常量。
 */
public class ProjectConstant {

    // 属性名
    public static final String ATTR_NAME_EXCEPTION = "exception";
    public static final String ATTR_NAME_LOGIN_ADMIN= "loginAdmin";
    public static final String ATTR_NAME_PAGE_INFO= "pageInfo";


    // 登录业务相关
    public static final String MESSAGE_LOGIN_INVALID = "oops! 账号密码错误，请重新输入。";
    public static final String MESSAGE_LOGIN_ACCOUNT_ALREADY_IN_USE = "账号已被使用，请重新输入";
    public static final String MESSAGE_ACCESS_FORBIDDEN = "请登录之后再使用";
    public static final String MESSAGE_STRING_INVALID = "传入的字符串不合法，请确认。";


    public static final String MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE = "账号发生重复，请去数据库系统中确认。";



}
