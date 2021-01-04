package com.iproject.crowd.mvc.interceptor;

import com.iproject.crowd.constant.ProjectConstant;
import com.iproject.crowd.entity.Admin;
import com.iproject.crowd.exception.AccessForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 尝试通过 Request 获取 Session 对象。
        Admin admin = (Admin)request.getSession().getAttribute(
                ProjectConstant.ATTR_NAME_LOGIN_ADMIN
        );

        // 2. 这个拦截器针对受保护的资源，因此一旦发现没有登录就返回 false.
        // 这个异常我们将抛给 com.iproject.crowd.mvc.config 下的 ExceptionResolver 来处理。
        if(admin == null) throw new AccessForbiddenException(ProjectConstant.MESSAGE_ACCESS_FORBIDDEN);

        // 返回 true 表示允许 (不拦截) 。
        return true;
    }
}
