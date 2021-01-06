package com.iproject.crowd.mvc.config;

import com.google.gson.Gson;
import com.iproject.crowd.constant.ProjectConstant;
import com.iproject.crowd.exception.AccessForbiddenException;
import com.iproject.crowd.exception.AcctNotUniqueException;
import com.iproject.crowd.exception.AcctNotUniqueForUpdateException;
import com.iproject.crowd.exception.LoginFailureException;
import com.iproject.crowd.utils.RequestHelper;
import com.iproject.crowd.utils.ResultEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一的异常处理组件，通过注解的方式进行配置
 */
@ControllerAdvice
public class CrowdExceptionResolver {

    private ModelAndView commonResolveException(
            Exception ex,
            String viewName,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws IOException {

        //1. 首先判断是普通请求还是 Json 请求
        boolean ajaxRequest = RequestHelper.isAjaxRequest(httpServletRequest);

        //2. 如果是 Ajax 请求
        if (ajaxRequest) {

            //3. 创建一个 ResultEntity
            ResultEntity<Object> resultEntity = ResultEntity.failure(ex.getMessage());

            //4. 创建一个 Gson 对象

            Gson gson = new Gson();

            String s = gson.toJson(resultEntity);


            // 5. 采取原生的方式返回这个 json 对象，因此在此分支中返回的 ModelAndView 为 null;
            httpServletResponse.getWriter().write(s);

            return null;
        }

        // 否则，创建一个 ModelAndView 对象
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject(ProjectConstant.ATTR_NAME_EXCEPTION, ex);

        // 10.设置对应的视图名称
        modelAndView.setViewName(viewName);

        // 11. 返回一个完整的 ModelAndView 对象。
        return modelAndView;
    }

    @ExceptionHandler(value = AcctNotUniqueException.class)
    public ModelAndView resolveAcctNotUniqueException(
            AcctNotUniqueException acctNotUniqueException,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        return commonResolveException(acctNotUniqueException, "admin-add", httpServletRequest, httpServletResponse);
    }

    @ExceptionHandler(value = AcctNotUniqueForUpdateException.class)
    public ModelAndView resolveAcctNotUniqueForUpdateException(
            AcctNotUniqueForUpdateException acctNotUniqueForUpdateException,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        return commonResolveException(acctNotUniqueForUpdateException, "system-error", httpServletRequest, httpServletResponse);
    }


    @ExceptionHandler(value = LoginFailureException.class)
    public ModelAndView resolveLoginFailureException(
            LoginFailureException loginFailureException,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        return commonResolveException(loginFailureException, "admin-login", httpServletRequest, httpServletResponse);
    }


    @ExceptionHandler(value = AccessForbiddenException.class)
    public ModelAndView resolveAccessForbiddenException(
            AccessForbiddenException accessForbiddenException,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        return commonResolveException(accessForbiddenException, "admin-login", httpServletRequest, httpServletResponse);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(
            NullPointerException nullPointException,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        return commonResolveException(nullPointException, "system-error", httpServletRequest, httpServletResponse);
    }
}
