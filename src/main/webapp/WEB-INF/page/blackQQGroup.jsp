<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>猎狐管理员系统</title>
    <meta charset="UTF-8"/>
    <base href="<%=request.getContextPath()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/easy-ui/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/easy-ui/themes/icon-lib.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/easy-ui/themes/bootstrap/easyui.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/easy-ui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/easy-ui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">

        var contentUrl = "<%=request.getContextPath()%>";

        var states = {0: "待爬取", 1: "已爬取"};

        function init() {
            // 定义表格列
            var gridColumns = [[
                {field: 'groupId', title: 'ID', hidden: true},
                {field: 'groupNo', title: 'QQ群号', width: 200, align: 'center'},
                {field: 'myQQ', title: '我的QQ', width: 200, align: 'center'},
                {
                    field: 'myPassword', title: '我的密码', width: 200, align: 'center',
                    formatter: function (value) {
                        return value && value.replace(/./g, "*") || "";
                    }
                },
                {field: 'createDate', title: '创建日期', width: 200, align: 'center'},
                {
                    field: 'stateValue', title: '爬取状态', width: 200, align: 'center',
                    formatter: function (value) {
                        return states[value] || "";
                    }
                },
                {field: 'resultMessage', title: '爬取结果', width: 200, align: 'center'}
            ]];

            // 定义表格工具栏
            var gridToolbar = [{
                text: "查询",
                iconCls: 'icon-search',
                handler: function () {
                    $('#dataGrid').datagrid("reload");
                }
            }, '-', {
                text: "新增",
                iconCls: 'icon-add',
                handler: function () {
                    openEditDialog({title: "新增QQ群", action: "add"});
                }
            }, {
                text: "编辑",
                iconCls: 'icon-edit',
                handler: function () {
                    var data = $("#dataGrid").datagrid("getSelected");
                    if (data && data.groupId) {
                        openEditDialog({title: "编辑QQ群", action: "update", rowData: data});
                    }
                }
            }];

            // 初始化表格
            $('#dataGrid').datagrid({
                url: contentUrl + "/blackQQGroup/list",
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
                    if (data && data.groupId) {
                        openEditDialog({title: "编辑QQ群", action: "update", rowData: data});
                    }
                },
                onBeforeLoad: function (param) {
                    param.groupNo = $("#groupNoInput").val() || "";
                    param.myQQ = $("#myQQInput").val() || "";
                }
            });
        }

        // 打开编辑对话框
        function openEditDialog(data) {
            $("#editDialog").dialog({
                title: data.title,
                closed: false,
                onClose: function () {
                    $("#groupId").val("");
                    $("#editForm").form("reset");
                },
                buttons: [{
                    text: "确认",
                    width: 80,
                    iconCls: 'icon-ok',
                    handler: function () {
                        // 更新数据
                        editOk(data);
                    }
                }, {
                    text: "取消",
                    width: 80,
                    iconCls: 'icon-arrow_undo',
                    handler: function () {
                        $("#editDialog").window("close");
                    }
                }]
            });
            if (data.action == "update") {
                $("#editForm").form("load", data.rowData);
                $("#groupNo").textbox('disable');
            } else {
                $("#groupNo").textbox('enable');
            }
        }

        // 编辑完成执行的操作
        function editOk(data) {
            var url = contentUrl + "/blackQQGroup/" + data.action;
            $.messager.progress();
            $('#editForm').form('submit', {
                url: url,
                onSubmit: function (param) {
                    var isValid = $(this).form('validate');
                    if (!isValid) {
                        $.messager.progress('close');
                    }
                    return isValid;
                },
                success: function (result) {
                    $.messager.progress('close');
                    result = eval("(" + result + ")");
                    $.messager.alert('温馨提示', result.message, result.success ? "info" : "warning");
                    if (result.success) {
                        $("#editDialog").window("close");
                        $('#dataGrid').datagrid("reload");
                    }
                }
            });
        }
    </script>
</head>
<body class="easyui-layout" onload="init();">

<div data-options="region:'north',iconCls:'icon-home',border:false" style="height:50px;padding:12px">
    <input id="groupNoInput" class="easyui-textbox" data-options="prompt:'QQ群号'" style="width:260px"/>　
    <input id="myQQInput" class="easyui-textbox" data-options="prompt:'我的QQ'" style="width:260px;margin-left:20px;"/>
</div>

<div data-options="region:'center',iconCls:'icon-user',border:false">
    <div id="dataGrid" data-options="border:false">
    </div>
</div>

<div id="editDialog" class="easyui-dialog" style="width:380px;height:300px;padding:10px 20px"
     data-options="closed:true,iconCls:'icon-view',resizable:false,modal:true,title:'编辑属性'">
    <form id="editForm" method="post">
        <input type="hidden" id="groupId" name="groupId"/>
        <div style="margin: 10px">
            <input class="easyui-textbox" id="groupNo" type="text" name="groupNo"
                   data-options="width:280,label:'QQ群号：',required:true"/>
        </div>
        <div style="margin: 10px">
            <input class="easyui-textbox" id="myQQ" type="text" name="myQQ"
                   data-options="width:280,label:'我的QQ：',required:true"/>
        </div>
        <div style="margin: 10px">
            <input class="easyui-textbox" id="myPassword" type="password" name="myPassword"
                   data-options="width:280,label:'我的密码：',required:true,validateOnBlur:true"/>
        </div>
        <div style="margin: 10px">
            <input class="easyui-combobox" name="stateValue" data-options="width:280,required:true,label:'爬取状态：',
            editable:false,value:0,panelHeight:50,textField:'name',data:[{name: '待爬取', value: 0},{name: '已爬取', value: 1}]"/>
        </div>
    </form>
</div>
</body>
</html>