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

                        if(result === "SUCCESS"){
                            layer.msg("操作成功!");
                            window.pageNum = 999999;
                            generatePage();
                        }

                        if(result === "FAILURE"){
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
        )
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
                    <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button type="button" class="btn btn-primary" id="showModalButton" style="float:right;"><i
                            class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox"></th>
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
</body>
</html>
