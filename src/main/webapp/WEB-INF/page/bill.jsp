<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>猎狐提现系统</title>
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

        // 字典值
        var billStates = {1: "提现中", 2: "提现成功", 3: "提现失败"};
        var billCurrencyType = {1: "人民币", 2: "美元", 3: "英镑"};

        function init() {

            // 定义表格列
            var gridColumns = [[
                {field: 'billId', title: 'ID', hidden: true},
                {
                    field: 'custom', title: '客户名称', width: 300, align: 'left',
                    formatter: function (value) {
                        return (value && value.name) || "";
                    }
                },
                {field: 'billNo', title: '账单编号', width: 250, align: 'left'},
                {field: 'sum', title: '金额', width: 300, align: 'left'},
                {field: 'createTime', title: '创建时间', width: 200, align: 'left'},
                {
                    field: 'stateValue', title: '提现状态', width: 200, align: 'left',
                    formatter: function (value) {
                        return billStates[value] || value;
                    }
                },
                {
                    field: 'currencyTypeValue', title: '货币类型', width: 200, align: 'left',
                    formatter: function (value) {
                        return billCurrencyType[value] || value;
                    }
                }
            ]];

            // 定义表格工具栏
            var gridToolbar = [{
                text: "查询",
                iconCls: 'icon-search',
                handler: function () {
                    $('#dataGrid').datagrid("reload");
                }
            }];

            // 设置日期时间输入框的值
            $('#startTimeInput').datetimebox('setValue', 'yyyy-mm-dd 00:00:00');
            $('#endTimeInput').datetimebox('setValue', 'yyyy-mm-dd 23:59:59');

            // 初始化表格
            $('#dataGrid').datagrid({
                url: contentUrl + "/pay/list",
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
                onBeforeLoad: function (param) {
                    param.startTime = $("#startTimeInput").datetimebox("getValue") || "";
                    param.endTime = $("#endTimeInput").datetimebox("getValue") || "";
                    param.name = $("#nameInput").val() || "";
                    param.billNo = $("#billNoInput").val() || "";
                }
            });
        }

    </script>
</head>
<body class="easyui-layout" onload="init();">

<div data-options="region:'north',iconCls:'icon-home',border:false" style="height:80px">
    <form>
        <div style="margin: 10px 20px">
            <input class="easyui-datetimebox" type="text" id="startTimeInput" data-options="label:'开始时间：',editable:false,width:260"/>
            　　<input class="easyui-datetimebox" type="text" id="endTimeInput" data-options="label:'结束时间：',editable:false,width:260"/>
        </div>
        <div style="margin: 10px 20px">
            <input class="easyui-textbox" type="text" id="nameInput" data-options="label:'客户名称：',width:260"/>
            　　<input class="easyui-textbox" type="text" id="billNoInput" data-options="label:'账单编号：',width:260"/>
        </div>
    </form>
</div>

<div data-options="region:'center',iconCls:'icon-user',border:false">
    <div id="dataGrid" data-options="border:false">
    </div>
</div>

</body>
</html>