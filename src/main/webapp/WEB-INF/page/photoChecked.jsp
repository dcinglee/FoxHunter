<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>猎狐图片系统</title>
    <meta charset="UTF-8"/>
    <base href="<%=request.getContextPath()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/easy-ui/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/easy-ui/themes/icon-lib.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/easy-ui/themes/bootstrap/easyui.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/easy-ui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/easy-ui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/script/util.js"></script>
    <style type="text/css">
        .overflowDiv {
            overflow: hidden
        }
    </style>
    <script type="text/javascript">

        var contentUrl = "<%=request.getContextPath()%>";

        // 字典值。
        var checkStates = {1: "待审核", 2: "审核通过", 3: "审核未通过"};
        var types = {1: "一般", 2: "危险", 3: "高危"};
        var orgTypes = {1: "待确认", 2: "个人", 3: "团伙"};
        var states = {1: "灰名", 2: "黑名未验证", 3: "黑名已验证"};
        var fromTypes = {1: "地推", 2: "网络", 3: "推算", 4: "举报"};

        function init() {
            // 定义表格列/列表数据获取
            var gridColumns = [[
                {field: 'photoId', title: 'ID', hidden: true},
                {field: 'fileName', title: '图片名称', width: 200, align: 'center'},
                {
                    field: 'createCustom', title: '上传客户', width: 100, align: 'center',
                    formatter: function (value) {
                        return (value && value.name) || "";
                    }
                },
                {
                    field: 'checkState', title: '审核状态', width: 100, align: 'center',
                    formatter: function (value) {
                        return checkStates[value] || "";
                    }
                },
                {field: 'auditDate', title: '审核时间', width: 100, align: 'center'},
                {field: 'auditInfo', title: '审核备注', width: 100, align: 'center'},
                {
                    field: 'auditManager', title: '审核人', width: 100, align: 'center',
                    formatter: function (value) {
                        return (value && value.name) || "";
                    }
                },
                {field: 'height', title: '高度', width: 100, align: 'center'},
                {field: 'width', title: '宽度', width: 100, align: 'center'},
                {field: 'latitude', title: '纬度', width: 100, align: 'center'},
                {field: 'longitude', title: '经度', width: 100, align: 'center'},
                {field: 'createDate', title: '上传时间', width: 100, align: 'center'},
                {field: 'path', title: '存储地址', width: 200, hidden: true}
            ]];
            // 定义表格工具栏
            var gridToolbar = [{
                text: "查询",
                iconCls: 'icon-search',
                handler: function () {
                    $('#dataGrid').datagrid("reload");
                }
            }, '-', {
                text: "查看图片",
                iconCls: 'icon-picture',
                handler: function () {
                    var data = $("#dataGrid").datagrid("getSelected");
                    if (data && data.photoId) {
                        openAuditDialog({title: "查看图片", action: "audit", rowData: data});
                    }
                }
            }];

            // 初始化表格
            $('#dataGrid').datagrid({
                url: contentUrl + "/photo/list",//获取图片列表数据地址
                fit: true,
                fitColumns: true,
                rownumbers: true,
                singleSelect: true,
                striped: true,
                idField: "id",
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 50, 100],
                loadMsg: "正在加载中···",
                columns: gridColumns,
                toolbar: gridToolbar,
                onDblClickRow: function (index, data) {
                    if (data && data.photoId) {
                        openAuditDialog({title: "查看图片", action: "audit", rowData: data});
                    }
                },
                onBeforeLoad: function (param) {
                    param.customName = $("#customNameInput").val() || "";
                    param.checkState = 2;
                }
            });

            // 定义已录入的表格列
            var addedColumns = [[
                {field: 'blacklistId', title: 'ID', hidden: true},
                {field: 'name', title: '名称', width: 200, align: 'left'},
                {field: 'nickName', title: '昵称', width: 200, align: 'left'},
                {field: 'enName', title: '英文名', width: 200, align: 'left'},
                {field: 'phoneNo', title: '电话号码', width: 200, align: 'left'},
                {field: 'qqNo', title: 'QQ号码', width: 200, align: 'left'},
                {field: 'microNo', title: '微信号码', width: 200, align: 'left'},
                {field: 'email', title: '邮箱', width: 200, align: 'left'},
                {field: 'homeUrl', title: '主页地址', width: 200, align: 'left'}
            ]];

            // 初始化已录入表格
            $('#addedGrid').datagrid({
                url: contentUrl + "/blacklist/listOfPhoto",
                fit: true,
                fitColumns: true,
                rownumbers: true,
                singleSelect: true,
                striped: true,
                loadMsg: "正在加载中···",
                columns: addedColumns,
                onBeforeLoad: function (param) {
                    param.photoId = $("#photoId").val() || "";
                    if (!param.photoId) return false;
                }
            });
        }

        // 打开图片审核对话框
        function openAuditDialog(data) {
            $("#auditDialog").dialog({
                title: data.title,
                closed: false,
                onClose: function () {
                    $("#auditForm").form("reset");
                    $("#photoImg").attr("src", "#");
                    $("#addedGrid").datagrid("loadData", []);
                },
                buttons: [{
                    text: "下一张",
                    width: 100,
                    iconCls: 'icon-next_green',
                    handler: function () {
                        // 查看下一张图片
                        nextPhoto();
                    }
                }, {
                    text: "返回",
                    width: 100,
                    iconCls: 'icon-arrow_undo',
                    handler: function () {
                        $("#auditDialog").window("close");
                    }
                }]
            });
            $("#auditForm").form("load", data.rowData);
            $("#addedGrid").datagrid("reload");
            $("#photoImg").attr("src", contentUrl + data.rowData.path);
        }

        // 继续查看下一张图片
        function nextPhoto() {
            var photoId = $("#photoId").val();
            if (!photoId) {
                return false;
            }
            Util.request(contentUrl + "/photo/next", {photoId: photoId, nextState: 2}, function (result) {
                if (result.success) {
                    $("#auditForm").form("load", result.data);
                    $("#photoImg").attr("src", contentUrl + result.data.path);
                    $("#addedGrid").datagrid("reload");
                } else {
                    $("#auditDialog").window("close");
                }
            });
        }

    </script>
</head>
<body class="easyui-layout" onload="init();">

<div data-options="region:'north',iconCls:'icon-home',border:false"
     style="height: 50px; padding: 8px">
    <table>
        <tr>
            <td><input id="customNameInput" class="easyui-textbox"
                       data-options="prompt:'上传客户名称'" style="width: 300px"></td>
            <td></td>
        </tr>
    </table>
</div>

<div data-options="region:'center',iconCls:'icon-user',border:false">
    <div id="dataGrid" data-options="border:false"></div>
</div>

<div id="auditDialog" class="easyui-dialog" style="width:800px; height: 600px; padding: 0px"
     data-options="closed:true,bodyCls:'overflowDiv',iconCls:'icon-picture',resizable:true,maximizable:true,modal:true,title:'查看图片'">
    <div class="easyui-layout" data-options="border:false,fit:true">
        <div data-options="region:'north',height:130,title:'当前图片已录入的黑名单',iconCls:'icon-flag_checked'">
            <div id="addedGrid" data-options="border:false">
            </div>
        </div>
        <div id="photoPanel" data-options="region:'center',iconCls:'icon-photo'" style="padding:10px">
            <form id="auditForm" method="post">
                <input type="hidden" id="photoId" name="photoId"/>
            </form>
            <img id="photoImg" alt="图片预览失败" src="#" style="border-radius:3px"/>
        </div>
    </div>
</div>
</body>
</html>