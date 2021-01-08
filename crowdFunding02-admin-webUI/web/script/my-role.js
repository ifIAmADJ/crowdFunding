function showConfirmModal(roleArray){

    $("#confirmModal").modal("show");
    $("#roleNameDiv").empty();

    // 在全局变量范围创建数组存放角色 id
    window.roleIdArray = [];


    // 遍历 roleArray 数组
    for(var i = 0 ; i< roleArray.length; i++){
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName + "<br/>");

        var roleId = role.roleId;
        window.roleIdArray.push(roleId)
    }

}

// 生成分页效果的函数，生成页面效果，是脚本的函数入口
function generatePage() {

    // 1.获取分页数据
    var pageInfo = getPageInfoRemote();

    fillTBody(pageInfo);

}

// 从远程获取数据
function getPageInfoRemote() {

    // 这个请求是 form data 的形式，后端不需要 @RequestBody 形式接受
    // 调用 $.ajax()
    var ajaxResult = $.ajax({
        "url": "role/get/pageInfo.json",
        "type": "post",
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "async": false,
        // 接受 json 格式的信息
        "dataType": "json"
    });
    console.log(ajaxResult);

    var statusCode = ajaxResult.status;


    // 如果当前响应状态码不是 200，则发生了错误情况
    if (statusCode !== 200) {
        layer.msg("FAILURE=" + statusCode + " msg=" + ajaxResult.statusText);
        return null;
    }

    // 如果响应码是200，说明请求成功。
    var resultEntity = ajaxResult.responseJSON;

    // 提取 result 属性

    var result = resultEntity.result;

    if (result === "FAILURE") {
        layer.msg(resultEntity.message);
        return null;
    }

    //确认 result 为成功之后获取 pageInfo
    var pageInfo = resultEntity.data;

    return pageInfo;


}

// 填充表格的主体
function fillTBody(pageInfo) {

    $("#rolePageBody").empty();
    $("#Pagination").empty();

    // 判断 pageInfo 是否有效
    if (pageInfo == null ||
        pageInfo === undefined ||
        pageInfo.list == null ||
        pageInfo.list.length === 0
    ) {
        $("#rolePageBody").append("<tr><td colspan='4'>no record</td></tr>");
    }


    //使用 pageInfo 的 list 属性填充 tbody 部分
    for (var i = 0; i < pageInfo.list.length; i++) {

        var role = pageInfo.list[i];

        var roleId = role.id;

        var roleName = role.name;

        var numberId = "<td>" + (i + 1) + "</td>";

        var checkboxTd = " <td><input class='itemBox' id='" + roleId + "' type='checkbox'></td>";

        var roleNameTd = "<td>" + roleName + "</td>";

        var checkBtn = "<button type='button' class='btn btn-success btn-xs'><i class=  ' glyphicon glyphicon-check'></i></button>";
        var pencilBtn = "<button type='button' id='" + roleId +"' class='btn btn-primary btn-xs pencilBtn'><i class=  ' glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button type='button' id='" + roleId +"' class='btn btn-danger btn-xs removeBtn'><i class=  ' glyphicon glyphicon-remove'></i></button>";

        var buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + " " + "</td>"

        var tr = "<tr>" + numberId + checkboxTd + roleNameTd + buttonTd +"</tr>";


        $("#rolePageBody").append(tr)

    }

    generateNavigator(pageInfo)
}

// 生成分页的导航栏
function generateNavigator(pageInfo) {

    // 获取总记录数
    var totalRecord = pageInfo.total;

    // 声明相关属性
    var properties = {
        "num_edge_entries": 3,								// 边缘页数
        "num_display_entries": 5,								// 主体页数
        "callback": paginationCallback,						// 指定用户点击“翻页”的按钮时跳转页面的回调函数
        "items_per_page": pageInfo.pageSize,	// 每页要显示的数据的数量
        "current_page": pageInfo.pageNum -1 ,	// Pagination内部使用pageIndex来管理页码，pageIndex从0开始，pageNum从1开始，所以要减一
        "prev_text": "last",									// 上一页按钮上显示的文本
        "next_text": "next"
    };

    // 调用 pagination() 函数
    $("#Pagination").pagination(totalRecord,properties);
}

function paginationCallback(pageIndex, jQuery) {

    // 修改全局变量的 pageNumber
    window.pageNum = pageIndex + 1;

    //调用问也函数
    generatePage();


    return false;
}