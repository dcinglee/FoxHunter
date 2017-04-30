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
        var gradutes = {1: "小学", 2: "初中", 3: "职高", 4: "中专", 5: "高中", 6: "大专", 7: "本科", 8: "本科学士", 9: "研究生", 10: "研究生硕士", 11: "研究生博士"};
        var fromTypes = {
            100: "地推", 101: "地推-商业区", 102: "地推-休闲区", 103: "地推-交通线", 104: "地推-住宅区", 105: "地推-办公区", 106: "地推-生产区",
            200: "网络", 201: "网络-QQ群", 202: "网络-微信群", 203: "网络-QQ空间", 204: "网络-朋友圈", 205: "网络-贴吧", 206: "网络-论坛", 207: "网络-知道", 208: "网络-广告",
            300: "推算", 301: "推算-智能算法", 302: "推算-直接关联", 303: "推算-人工推测",
            400: "举报", 401: "举报-广告推送", 402: "举报-宣传营销", 403: "举报-诈骗诱骗", 404: "举报-信息搔扰"
        };

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
                text: "审核图片",
                iconCls: 'icon-edit',
                handler: function () {
                    var data = $("#dataGrid").datagrid("getSelected");
                    if (data && data.photoId) {
                        openAuditDialog({title: "图片审核", action: "audit", rowData: data});
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
                        openAuditDialog({title: "图片审核", action: "audit", rowData: data});
                    }
                },
                onBeforeLoad: function (param) {
                    param.customName = $("#customNameInput").val() || "";
                    param.checkState = 3;
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
                    $('#dataGrid').datagrid("reload");
                    $("#photoImg").attr("src", "#");
                    $("#addedGrid").datagrid("loadData", []);
                },
                buttons: [{
                    text: "通过并录入",
                    width: 100,
                    iconCls: 'icon-add',
                    handler: function () {
                        // 通过并录入黑名单
                        passAddBlacklist();
                    }
                }, {
                    text: "下一张",
                    width: 100,
                    iconCls: 'icon-next_green',
                    handler: function () {
                        // 审核下一张图片
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
            if (data.action == "audit") {
                $("#auditForm").form("reset");
                $("#photoImg").attr("src", contentUrl + data.rowData.path);
                $("#auditForm").form("load", data.rowData);
                $("#addedGrid").datagrid("reload");
            }
        }

        // 通过并录入黑名单
        function passAddBlacklist() {
            // 校验输入。
            $.messager.progress();
            if (!$("#phoneNo").textbox("getValue")
                    && !$("#qqNo").textbox("getValue")
                    && !$("#microNo").textbox("getValue")) {
                $.messager.progress('close');
                $.messager.alert("温馨提示", "联系方式至少填一项！", "warning");
                return false;
            }
            $('#auditForm').form('submit', {
                url: contentUrl + "/photo/auditPass",
                onSubmit: function () {
                    var isValid = $("#auditForm").form('validate');
                    if (!isValid) {
                        $.messager.progress('close');
                    }
                    return isValid;
                },
                success: function (result) {
                    result = JSON.parse(result);
                    $.messager.progress('close');
                    if (result.success) {
                        var photo = result.data.fromPhoto || {};
                        $("#auditForm").form("reset");
                        $("#photoId").val(photo.photoId);
                        $("#addedGrid").datagrid("appendRow", result.data);
                        $("#auditInfo").textbox('setValue', photo.auditInfo || "");
                    }
                    $.messager.alert('温馨提示', result.message, result.success ? "info" : "warning");
                }
            });
        }

        // 继续处理下一张图片
        function nextPhoto() {
            var photoId = $("#photoId").val();
            if (!photoId) {
                return false;
            }
            Util.request(contentUrl + "/photo/next", {photoId: photoId, nextState: 3}, function (result) {
                if (result.success) {
                    $("#auditForm").form("reset");
                    $("#photoImg").attr("src", contentUrl + result.data.path);
                    $("#auditForm").form("load", result.data);
                    $("#addedGrid").datagrid("reload");
                } else {
                    $("#auditDialog").window("close");
                }
            });
        }

        // 当省份下拉框的值发生变化
        function provinceChange() {
            var province = $("#provinceId").combobox("getValue");
            if (province) {
                $("#cityId").combobox("clear").combobox({url: contentUrl + "/business/location/" + province});
                $("#countyId").combobox("clear");
            }
        }

        // 当城市下拉框的值发生变化
        function cityChange() {
            var city = $("#cityId").combobox("getValue");
            if (city) {
                $("#countyId").combobox("clear").combobox({url: contentUrl + "/business/location/" + city});
            }
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
     data-options="closed:true,bodyCls:'overflowDiv',iconCls:'icon-view',resizable:true,maximizable:true,modal:true,title:'编辑属性'">
    <div class="easyui-layout" style="width: 100%; height: 100%;">
        <div data-options="region:'north',height:130,title:'当前图片已录入的黑名单',iconCls:'icon-flag_checked'">
            <div id="addedGrid" data-options="border:false">
            </div>
        </div>
        <div id="photoPanel" data-options="region:'center',iconCls:'icon-photo'" style="padding:10px">
            <img id="photoImg" alt="图片预览失败" src="#" style="border-radius:3px"/>
        </div>
        <div data-options="region:'east',iconCls:'icon-edit',collapsible:false" style="width:320px;padding:10px">
            <form id="auditForm" method="post">
                <input type="hidden" id="photoId" name="photoId"/>
                <div style="margin: 5px">
                    <input id="auditInfo" class="easyui-textbox" type="text" name="auditInfo" data-options="label:'审核备注：',width:260"/>
                </div>
                <hr style="height:1px;border:none;border-top:1px solid #DDD;margin:8px 0"/>
                <div style="margin: 5px">
                    <input class="easyui-textbox" id="name" name="name" data-options="required:true,label:'名称：',width:260"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-textbox" id="nickName" name="nickName" data-options="label:'昵称：',width:260"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-textbox" id="enName" name="enName" data-options="label:'英文名：',width:260"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-datebox" id="bornDate" name="bornDate" data-options="label:'出生日期：',editable:false,width:260"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-combobox" id="levelValue" name="levelValue" data-options="required:true,editable:false,label:'风险级别：',
            width:260,value:1,panelHeight:100,textField:'name',url:'<%=request.getContextPath()%>/business/dictionary/blackListLevel'"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-combobox" id="orgTypeValue" name="orgTypeValue" data-options="required:true,label:'组织类型：',width:260,
            editable:false,value:1,panelHeight:100,textField:'name',url:'<%=request.getContextPath()%>/business/dictionary/blackListOrgType'"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-combobox" id="stateValue" name="stateValue" data-options="required:true,editable:false,label:'状态：',
            width:260,value:1,panelHeight:100,textField:'name',url:'<%=request.getContextPath()%>/business/dictionary/blackListState'"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-combobox" id="fromTypeValue" name="fromTypeValue" data-options="required:true,editable:false,label:'来源类型：',
            width:260,value:200,panelHeight:200,textField:'name',url:'<%=request.getContextPath()%>/business/dictionary/blackListFromType'"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-textbox" id="fromInfo" name="fromInfo" data-options="width:260,label:'来源描述：'"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-textbox" id="phoneNo" name="phoneNo" data-options="width:260,label:'电话号码：',validType:'length[5,20]'"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-textbox" id="qqNo" name="qqNo" data-options="width:260,label:'QQ号码：',validType:'length[5,20]'"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-textbox" id="microNo" name="microNo" data-options="width:260,label:'微信号码：',validType:'length[5,64]'"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-textbox" id="email" name="email" data-options="width:260,label:'邮箱：',validateOnBlur:true,validType:'email'"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-textbox" id="homeUrl" name="homeUrl" data-options="width:260,label:'主页地址：',validateOnBlur:true,validType:'url'"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-combobox" id="graduteValue" name="graduteValue" data-options="editable:false,label:'学历：',
            width:260,panelHeight:200,textField:'name',url:'<%=request.getContextPath()%>/business/dictionary/gradute'"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-combobox" id="provinceId" name="provinceId" data-options="editable:false,label:'省份：',
            width:260,panelHeight:200,textField:'name',valueField:'locationId',url:'<%=request.getContextPath()%>/business/location/0',onChange:provinceChange"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-combobox" id="cityId" name="cityId" data-options="editable:false,label:'城市：',
            width:260,panelHeight:200,textField:'name',valueField:'locationId',onChange:cityChange"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-combobox" id="countyId" name="countyId" data-options="editable:false,label:'区县：',
            width:260,panelHeight:200,textField:'name',valueField:'locationId'"/>
                </div>
                <div style="margin: 5px">
                    <input class="easyui-textbox" id="address" name="address" data-options="label:'详细地址：',width:260"/>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>