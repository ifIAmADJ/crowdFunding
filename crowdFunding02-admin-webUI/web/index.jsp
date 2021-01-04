<%--
  Created by IntelliJ IDEA.
  User: liJunhu
  Date: 2020/12/29
  Time: 15:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="layer/layer.js"></script>
<script type="text/javascript">
    $(function () {
            $("#btn1").click(
                function () {
                    $.ajax({
                        "url": "send/array.html",    // 请求目标资源的地址
                        "type": "post",              // 请求方式
                        "data": {
                            "array": [1, 2, 3]
                        },             // 发送的数据类型
                        "dataType": "text",          // 如何处理服务器端返回的数据
                        "success": function (response) {
                            alert(response)
                        },                          // 响应成功时的回调函数
                        "error": function (response) {
                            alert(response)
                        }                           // 响应失败时的回调函数
                    })
                }
            );

            var array = [1, 2, 3];

            var requestBody = JSON.stringify(array);

            console.log(requestBody);


            $("#btn2").click(
                function () {
                    $.ajax({
                        "url": "send/array2.html",    // 请求目标资源的地址
                        "type": "post",               // 请求方式
                        "data": requestBody,  // 发送的数据类型
                        "contentType": "application/json;charset=utf-8",
                        "dataType": "text",          // 如何处理服务器端返回的数据
                        "success": function (response) {
                            alert(response)
                        },                          // 响应成功时的回调函数
                        "error": function (response) {
                            alert(response)
                        }                           // 响应失败时的回调函数
                    })
                }
            );

            var student = {

                "name":"Fang Wang",
                "age":20,
                "major":{
                    "name":"CS"
                }
            };

            var studentJson = JSON.stringify(student);

            $("#btn3").click(
                function () {
                    $.ajax({
                        "url": "send/student.json",    // 请求目标资源的地址
                        "type": "post",               // 请求方式
                        "data": studentJson,  // 发送的数据类型
                        "contentType": "application/json;charset=utf-8",
                        "dataType": "json",          // 如何处理服务器端返回的数据
                        "success": function (response) {
                            console.log(response)
                        },                          // 响应成功时的回调函数
                        "error": function (response) {
                            console.log(response)
                        }                           // 响应失败时的回调函数
                    })
                }
            )

        $("#btn4").click(function(){
                layer.msg("aaa!!")
            }
        )

        }
    );
</script>
<head>
    <title>$Title$</title>
</head>
<body>
$END$


<a href="test/ssm.html">测试</a>
<a href="admin/to/login/page.html">进入登录页面</a>
<br>
<button id="btn1">Send [1,2,3] to back-end;1</button>
<br>
<button id="btn2">Send [1,2,3] to back-end;2</button>
<br>
<button id="btn3">Send Student;3</button>
<br>
<button id="btn4">点我测试 layer 弹窗</button>



</body>
</html>
