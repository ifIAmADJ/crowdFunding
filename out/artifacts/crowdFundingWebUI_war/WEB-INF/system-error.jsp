<%--
  Created by IntelliJ IDEA.
  User: liJunhu
  Date: 2020/12/31
  Time: 16:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <%--该标签还保证了web目录下的css,fonts,img 等资源的加载。--%>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/login.css">
    <script src="jquery/jquery-2.1.1.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <script>
        $(function () {
                $("button").click(function () {
                    //相当于浏览器的后退按钮
                    window.history.back();
                });
            }
        );

    </script>
    <style>
    </style>
</head>

<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div><a class="navbar-brand" href="index.html" style="font-size:32px;">众筹网-创意产品众筹平台</a></div>
        </div>
    </div>
</nav>

<div class="container" style="text-align: center">

    <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in"></i> 系统消息</h2>
    <%--
        输出错误信息，对应 CrowdExceptionResolver.java 代码中的:
        modelAndView.addObject(ProjectConstant.ATTR_NAME_EXCEPTION, ex);

        ${requestScope.exception.message} 相当于 requestScope.getAttribute("exception").getMessage();
    --%>
    <h3 style="text-align: center">${requestScope.exception.message}</h3>

    <%-- 通过 js 脚本返回到上一个页面--%>
    <button class="btn btn-lg btn-success btn-block"
            style="width: 150px;margin: 50px auto 0 auto"
    >点我返回上一步
    </button>

</div>

</body>
