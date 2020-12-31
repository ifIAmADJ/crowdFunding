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
    <title>出错了！</title>
</head>
<body>
<h1>出错了！</h1>
<%--从请求域中取出 Exception 对象 --%>
${requestScope.exception.message}
</body>
</html>
