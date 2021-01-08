<%--
  Created by IntelliJ IDEA.
  User: liJunhu
  Date: 2021/1/3
  Time: 19:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css"/>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script type="text/javascript" charset="UTF-8" src="script/my-role.js"></script>
<script type="text/javascript">
    $(function () {

        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";
        generatePage();

        $("#search").click(function () {

            window.keyword = $("#keywordInput").val();

            generatePage();

        })

        $("#showModalButton").click(
            function () {

                $("#addModal").modal("show");

            });

        $("#saveRoleBtn").click(
            function () {

                // 用户在文本框中输入的角色名称
                var roleName = $.trim($("#addModal [name=roleName]").val());

                $.ajax({
                    "url": "role/save.json",
                    "type": "post",
                    "data": {
                        "name": roleName
                    },
                    "dataType": "json",
                    "success": function (response) {
                        var result = response.result;

                        if (result === "SUCCESS") {
                            layer.msg("操作成功!");
                            window.pageNum = 999999;
                            generatePage();
                        }

                        if (result === "FAILURE") {
                            layer.msg("操作失败!" + response.message)
                        }
                    },
                    "error": function (response) {
                        layer.msg(response.statusText)
                    }

                });

                // 关闭模态框
                $("#addModal").modal("hide");
                $("#addModal [name=roleName]").val("");


            }
        );

        // 使用 jQuery 对象的 on() 函数
        $("#rolePageBody").on("click", ".pencilBtn", function () {

            // 打开编辑页面的模态框
            $("#editModal").modal("show");

            // 获取当前行的值，不去后端
            var roleName = $(this).parent().prev().text();

            // 获取当前角色的id
            window.roleId = this.id;

            $("#editModal [name=roleName]").val(roleName);
        });


        // 给更新模态框的按钮绑定单机响应函数
        $("#updateRoleBtn").click(
            function () {

                // 从文本框中获取新的角色名称
                var roleName = $("#editModal [name=roleName]").val();

                // 发送 ajax 请求
                $.ajax({
                    "url": "role/update.json",
                    "type": "post",
                    "data": {
                        "id": window.roleId,
                        "name": roleName
                    },
                    "dataType": "json",
                    "success": function (response) {
                        var result = response.result;

                        if (result === "SUCCESS") {
                            layer.msg("操作成功!");
                            generatePage();
                        }

                        if (result === "FAILURE") {
                            layer.msg("操作失败!" + response.message)
                        }
                    },
                    "error": function (response) {
                        layer.msg(response.statusText)
                    }
                });

                $("#editModal").modal("hide");

            }
        );

        $("#removeRoleBtn").click(
            function(){
                var requestBody = JSON.stringify(window.roleIdArray);

                $.ajax({
                    "url": "role/remove/by/id/array.json",
                    "type": "post",
                    "data": requestBody,
                    "contentType": "application/json;charset=UTF-8",
                    "dataType":"json",
                    "success": function (response) {
                        var result = response.result;

                        if (result === "SUCCESS") {
                            layer.msg("操作成功!");
                            generatePage();
                        }

                        if (result === "FAILURE") {
                            layer.msg("操作失败!" + response.message)
                        }
                    },
                    "error": function (response) {
                        layer.msg(response.statusText)
                    }
                });

                $("#confirmModal").modal("hide");


            });

        // 使用 jQuery 对象的 on() 函数
        $("#rolePageBody").on("click", ".removeBtn", function () {

            var roleName = $(this).parent().prev().text();

            // 准备 roleArray
            // 创建一个 role 对象
            var roleArray = [{
                roleId:this.id,
                roleName:roleName
            }];

            showConfirmModal(roleArray)
        });

        $("#summaryBox").click(function () {

            // 获取当前多选框自身的状态
            var currentStatus = this.checked;

            $(".itemBox").prop("checked",currentStatus);

            // 全选，全不选的反向操作
            $("#rolePageBody").on("click", ".itemBox", function () {

                // 获取当前已经选中的 itemBox 的数量
                var checkedBoxCount =$(".itemBox:checked").length;

                // 获取全部 .itemBox 的数量
                var totalBoxCount =$(".itemBox").length;

                $("#summaryBox").prop("checked", checkedBoxCount == totalBoxCount)

            });

        });

        $("#batchRemoveBtn").click(function(){

            // 创建一个数组对象，用来存放后面获取到的角色对象
            var roleArray = [];

            $(".itemBox:checked").each(function(){
                var roleId = this.id;

                var roleName = $(this).parent().next().text();

                console.log(roleId + "::::" + roleName);

                roleArray.push({
                    "roleId":roleId,
                    "roleName":roleName
                });
            });


            // 检查 roleArray 的长度是否为 0
            if(roleArray.length == 0){
                layer.msg("请至少选择删除一个元素");
                return;
            }

            showConfirmModal(roleArray)

        });



    });
</script>
<body>
<%@include file="include-navigator.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="search" type="button" class="btn btn-warning"><i
                                class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button type="button" class="btn btn-primary" id="showModalButton" style="float:right;"><i
                            class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <%--<button type="button" class="btn btn-primary" id="showEditModal" style="float:right;"><i--%>
                    <%--class="glyphicon glyphicon-plus"></i> 更新--%>
                    <%--</button>--%>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody"></tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="modal-role-add.jsp" %>
<%@include file="modal-role-edit.jsp" %>
<%@include file="modal-role-confirm.jsp" %>
</body>
</html>
