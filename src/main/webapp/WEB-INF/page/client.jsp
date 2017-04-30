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
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/script/util.js"></script>
    <script type="text/javascript">

        var contentUrl = "<%=request.getContextPath()%>";

        var states = {1: "正常", 2: "锁定"};

        function init() {
            // 定义表格列
            var gridColumns = [[
                {field: 'clientId', title: 'ID', hidden: true},
                {field: 'name', title: '名称', width: 200, align: 'center'},
                {field: 'password', title: '密码', hidden: true},
                {field: 'phoneNo', title: '电话号码', width: 200, align: 'center'},
                {field: 'mail', title: '邮箱', width: 200, align: 'center'},
                {field: 'balance', title: '余额', width: 200, align: 'center'},
                {field: 'totalNum', title: '查询总条次', width: 200, align: 'center'},
                {
                    field: 'stateValue', title: '状态', width: 200, align: 'center',
                    formatter: function (value) {
                        return states[value] || "";
                    }
                },
                {field: 'notes', title: '备注信息', width: 200, align: 'center'},
                {field: 'createDate', title: '创建日期', width: 200, align: 'center'}
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
                    openEditDialog({title: "新增客户端", action: "add"});
                }
            }, {
                text: "编辑",
                iconCls: 'icon-edit',
                handler: function () {
                    var data = $("#dataGrid").datagrid("getSelected");
                    if (data && data.clientId) {
                        openEditDialog({title: "编辑客户端", action: "update", rowData: data});
                    }
                }
            }];

            // 初始化表格
            $('#dataGrid').datagrid({
                url: contentUrl + "/client/list",
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
                    if (data && data.clientId) {
                        openEditDialog({title: "编辑客户端", action: "update", rowData: data});
                    }
                },
                onBeforeLoad: function (param) {
                    param.name = $("#nameInput").val() || "";
                }
            });
        }

        // 打开编辑对话框
        function openEditDialog(data) {
            $("#editDialog").dialog({
                title: data.title,
                closed: false,
                onClose: function () {
                    $("#clientId").val("");
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
                $("#name").textbox("disable");
            } else {
                $("#name").textbox("enable");
            }
        }

        // 编辑完成执行的操作
        function editOk(data) {
            var url = contentUrl + "/client/" + data.action;
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

<div data-options="region:'north',iconCls:'icon-home',border:false" style="height:50px;padding:8px">
    <table>
        <tr>
            <td>
                <input id="nameInput" class="easyui-textbox" data-options="prompt:'客户端名称'" style="width:300px">
            </td>
        </tr>
    </table>
</div>

<div data-options="region:'center',iconCls:'icon-user',border:false">
    <div id="dataGrid" data-options="border:false">
    </div>
</div>

<div id="editDialog" class="easyui-dialog" style="width:380px;height:380px;padding:10px 20px"
     data-options="closed:true,iconCls:'icon-view',resizable:false,modal:true,title:'编辑属性'">
    <form id="editForm" method="post">
        <input type="hidden" id="clientId" name="clientId"/>
        <div style="margin: 10px">
            <input class="easyui-textbox" id="name" type="text" name="name"
                   data-options="width:280,label:'名称：',required:true"/>
        </div>
        <div style="margin: 10px">
            <input class="easyui-textbox" type="password" name="password"
                   data-options="width:280,label:'密码：',required:true,validateOnBlur:true"/>
        </div>
        <div style="margin: 10px">
            <input class="easyui-textbox" type="text" name="balance" data-options="width:280,label:'余额：',required:true"/>
        </div>
        <div style="margin: 10px">
            <input class="easyui-textbox" type="text" name="phoneNo" data-options="width:280,label:'电话号码：',required:true"/>
        </div>
        <div style="margin: 10px">
            <input class="easyui-textbox" type="text" name="mail" data-options="width:280,label:'邮箱：',validateOnBlur:true,validType:'email'"/>
        </div>
        <div style="margin: 10px">
            <input class="easyui-combobox" name="stateValue" data-options="width:280,required:true,editable:false,label:'状态：',
            value:1,panelHeight:100,textField:'name',url:'<%=request.getContextPath()%>/business/dictionary/clientState'"/>
        </div>
        <div style="margin: 10px">
            <input class="easyui-textbox" type="text" name="notes" data-options="width:280,label:'备注信息：'"/>
        </div>
    </form>
</div>
</body>
</html>