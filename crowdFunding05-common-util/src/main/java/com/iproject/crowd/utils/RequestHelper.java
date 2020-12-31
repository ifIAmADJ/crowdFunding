package com.iproject.crowd.utils;

import javax.servlet.http.HttpServletRequest;

public class RequestHelper {

    /**
     * 判断当前请求是否是 Ajax 请求。
     * @param request 拦截请求头。
     * @return 若为 Ajax 请求，则返回 true。
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {

        //1.根据请求头判断是普通请求还是 Ajax 请求。
        String acceptHeader = request.getHeader("Accept");
        String xRequestHeader = request.getHeader("X-Requested-With");

        return (acceptHeader != null && acceptHeader.contains("application/json"))
                ||
                (xRequestHeader != null && xRequestHeader.equals("XMLHttpRequest"));
    }


}
