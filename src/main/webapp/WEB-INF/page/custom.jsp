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

        var genders = {0: "保密", 1: "男", 2: "女"};
        var states = {1: "正常", 2: "锁定"};

        function init() {
            // 定义表格列
            var gridColumns = [[
                {field: 'managerId', title: 'ID', hidden: true},
                {field: 'name', title: '姓名', width: 200, align: 'left'},
                {field: 'password', title: '密码', hidden: true},
                {
                    field: 'genderValue', title: '性别', width: 200, align: 'left',
                    formatter: function (value) {
                        return genders[value] || "";
                    }
                },
                /*{field: 'bornDate', title: '出生日期', width: 200, align: 'left'},
                 {field: 'phoneNo', title: '电话号码', width: 200, align: 'left'},
                 {field: 'idCardNo', title: '身份证号', width: 200, align: 'left'},
                 {field: 'workCardNo', title: '工作证号', width: 200, align: 'left'},*/
                {field: 'allPhotoNum', title: '上传总数', width: 200, align: 'left'},
                {field: 'checkedPhotoNum', title: '审核通过数', width: 200, align: 'left'},
                {field: 'historyIncome', title: '历史收益总计', width: 200, align: 'left'},
                {field: 'income', title: '当前收益', width: 200, align: 'left'},
                {field: 'notes', title: '备注信息', width: 200, align: 'left'},
                {
                    field: 'stateValue', title: '状态', width: 200, align: 'left',
                    formatter: function (value) {
                        return states[value] || "";
                    }
                },
                {field: 'annotate', title: '管理员批注', width: 200, align: 'left'}
            ]];

            // 定义表格工具栏
            var gridToolbar = [{
                text: "查询",
                iconCls: 'icon-search',
                handler: function () {
                    $('#dataGrid').datagrid("reload");
                }
            }, '-', {
                text: "编辑",
                iconCls: 'icon-edit',
                handler: function () {
                    var data = $("#dataGrid").datagrid("getSelected");
                    if (data && data.customId) {
                        openEditDialog({title: "编辑客户", action: "update", rowData: data});
                    }
                }
            }];

            // 初始化表格
            $('#dataGrid').datagrid({
                url: contentUrl + "/custom/list",
                fit: true,
                fitColumns: true,
                rownumbers: true,
                singleSelect: true,
                striped: true,
                idField: "id",
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 50, 100],
                columns: gridColumns,
                toolbar: gridToolbar,
                onDblClickRow: function (index, data) {
                    if (data && data.customId) {
                        openEditDialog({title: "编辑客户", action: "update", rowData: data});
                    }
                },
                onBeforeLoad: function (param) {
                    param.name = $("#nameInput").val() || "";
                    param.phoneNo = $("#phoneNoInput").val() || "";
                }
            });
        }

        // 打开编辑对话框
        function openEditDialog(data) {
            $("#editDialog").dialog({
                title: data.title,
                closed: false,
                onClose: function () {
                    $("#customId").val("");
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
            }
        }

        // 编辑完成执行的操作
        function editOk(data) {
            var url = contentUrl + "/custom/" + data.action;
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
                <input id="nameInput" class="easyui-textbox" data-options="prompt:'客户姓名'" style="width:300px">
            </td>
            <td style="width: 15px"></td>
            <td>
                <input id="phoneNoInput" class="easyui-textbox" data-options="prompt:'电话号码'" style="width:300px">
            </td>
            <td></td>
        </tr>
    </table>
</div>

<div data-options="region:'center',iconCls:'icon-user',border:false">
    <div id="dataGrid" data-options="border:false">
    </div>
</div>

<div id="editDialog" class="easyui-dialog" style="width:340px;height:230px;padding:10px 20px"
     data-options="closed:true,iconCls:'icon-view',resizable:false,modal:true,title:'编辑属性'">
    <form id="editForm" method="post">
        <input type="hidden" id="customId" name="customId"/>
        <div style="margin: 10px">
            <input class="easyui-textbox" type="text" name="name" data-options="readonly:true,label:'名称：',width:260"/>
        </div>
        <div style="margin: 10px">
            <input class="easyui-combobox" type="text" name="stateValue" data-options="required:true,editable:false,label:'状态：',
            panelHeight:100,textField:'name',width:260,url:'<%=request.getContextPath()%>/business/dictionary/customState'"/>
        </div>
        <div style="margin: 10px">
            <input class="easyui-textbox" type="text" name="annotate" data-options="label:'管理员批注：',width:260,multiline:true"/>
        </div>
    </form>
</div>
</body>
</html>