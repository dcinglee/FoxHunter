<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>猎狐黑名单系统</title>
    <meta charset="UTF-8"/>
    <base href="<%=request.getContextPath()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/easy-ui/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/easy-ui/themes/icon-lib.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/easy-ui/themes/bootstrap/easyui.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/easy-ui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/ajaxfileupload.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/easy-ui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/script/util.js"></script>
    <script type="text/javascript">

        var contentUrl = "<%=request.getContextPath()%>";

        // 字典值
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
            // 定义表格列
            var gridColumns = [[
                {field: 'blacklistId', title: 'ID', hidden: true},
                {field: 'name', title: '名称', width: 200, align: 'left'},
                {field: 'nickName', title: '昵称', width: 200, align: 'left'},
                {field: 'enName', title: '英文名', width: 200, align: 'left'},
                {field: 'bornDate', title: '出生日期', width: 200, align: 'left'},
                {
                    field: 'levelValue', title: '风险级别', width: 200, align: 'left',
                    formatter: function (value) {
                        return types[value] || value;
                    }
                }, {
                    field: 'orgTypeValue', title: '组织类型', width: 200, align: 'left',
                    formatter: function (value) {
                        return orgTypes[value] || value;
                    }
                }, {
                    field: 'stateValue', title: '状态', width: 200, align: 'left',
                    formatter: function (value) {
                        return states[value] || value;
                    }
                }, {
                    field: 'fromTypeValue', title: '来源类型', width: 200, align: 'left',
                    formatter: function (value) {
                        return fromTypes[value] || value;
                    }
                },
                {field: 'fromInfo', title: '来源描述', width: 200, align: 'left'},
                {field: 'phoneNo', title: '电话号码', width: 200, align: 'left'},
                {field: 'qqNo', title: 'QQ号码', width: 200, align: 'left'},
                {field: 'microNo', title: '微信号码', width: 200, align: 'left'},
                {field: 'email', title: '邮箱', width: 200, align: 'left'},
                {field: 'homeUrl', title: '主页地址', width: 200, align: 'left'},
                {
                    field: 'graduteValue', title: '学历', width: 200, align: 'left',
                    formatter: function (value) {
                        return gradutes[value] || value;
                    }
                }, {
                    field: 'province', title: '省份', width: 200, align: 'left',
                    formatter: function (value) {
                        return (value && value.name) || "";
                    }
                }, {
                    field: 'city', title: '城市', width: 200, align: 'left',
                    formatter: function (value) {
                        return (value && value.name) || "";
                    }
                }, {
                    field: 'county', title: '区县', width: 200, align: 'left',
                    formatter: function (value) {
                        return (value && value.name) || "";
                    }
                },
                {field: 'address', title: '详细地址', width: 200, align: 'left'},
                {
                    field: 'addManager', title: '录入管理员', width: 200, align: 'left',
                    formatter: function (value) {
                        return (value && value.name) || "";
                    }
                },
                {field: 'createDate', title: '创建时间', width: 200, align: 'left'}
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
                    openEditDialog({title: "新增黑名单", action: "add"});
                }
            }, {
                text: "查看",
                iconCls: 'icon-image',
                handler: function () {
                    var data = $("#dataGrid").datagrid("getSelected");
                    if (data && data.blacklistId) {
                        openEditDialog({title: "查看黑名单", action: "view", data: data});
                    }
                }
            }];

            // 初始化主表格
            $('#dataGrid').datagrid({
                url: contentUrl + "/blacklist/list",
                fit: true,
                /*fitColumns: true,*/
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
                    if (data && data.blacklistId) {
                        openEditDialog({title: "查看黑名单", action: "view", data: data});
                    }
                },
                onBeforeLoad: function (param) {
                    param.name = $("#nameInput").val() || "";
                    param.phoneNo = $("#phoneNoInput").val() || "";
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
                    param.photoId = $("#fromPhotoId").val() || "";
                    if (!param.photoId) return false;
                }
            });
        }

        // 打开编辑/查看对话框
        function openEditDialog(options) {
            var buttons = [];
            if (options.action == "add") {
                buttons = [{
                    text: "继续录入",
                    width: 100,
                    iconCls: 'icon-bullet_go',
                    handler: function () {
                        options.option = "next";
                        editOk(options);
                    }
                }, {
                    text: "完成",
                    width: 100,
                    iconCls: 'icon-ok',
                    handler: function () {
                        options.option = "done";
                        editOk(options);
                    }
                }, {
                    text: "返回",
                    width: 100,
                    iconCls: 'icon-arrow_undo',
                    handler: function () {
                        $("#editDialog").window("close");
                        $("#dataGrid").datagrid("reload");
                    }
                }];
            } else {
                buttons = [{
                    text: "返回",
                    width: 100,
                    iconCls: 'icon-arrow_undo',
                    handler: function () {
                        $("#editDialog").window("close");
                    }
                }];
                options.data.provinceId = options.data.province && options.data.province.locationId || "";
                options.data.cityId = options.data.city && options.data.city.locationId || "";
                options.data.countyId = options.data.county && options.data.county.locationId || "";
                options.data.levelValue = options.data.levelValue || "";
                options.data.orgTypeValue = options.data.orgTypeValue || "";
                options.data.fromTypeValue = options.data.fromTypeValue || "";
                options.data.stateValue = options.data.stateValue || "";
                $("#editForm").form("load", options.data);
                if (options.data.fromPhoto) {
                    $("#fromPhotoId").val(options.data.fromPhoto.photoId);
                    $('#imgPreview').attr("src", contentUrl + options.data.fromPhoto.path).removeAttr("hidden");
                    $('#imgChooser').filebox("disable").filebox("setText", options.data.fromPhoto.fileName);
                    $("#addedGrid").datagrid("reload");
                }
            }
            $("#editDialog").dialog({
                title: options.title,
                closed: false,
                onClose: function () { // 清理控件。
                    $("#editForm").form("reset");
                    $("#blacklistId").val("");
                    $("#fromPhotoId").val("");
                    $("#addedGrid").datagrid("loadData", []);
                    $('#imgPreview').attr("src", "#").attr("hidden", "hidden");
                    $('#imgChooser').filebox({buttonText: '选择图片', onChange: previewImg})
                            .filebox("clear").filebox("enable");
                },
                buttons: buttons
            });
        }

        function editOk(options) {
            // 校验输入。
            $.messager.progress();
            var isValid = $("#editForm").form('validate');
            if (!isValid) {
                $.messager.progress('close');
                return isValid;
            }
            if (!$("#phoneNo").textbox("getValue")
                    && !$("#qqNo").textbox("getValue")
                    && !$("#microNo").textbox("getValue")) {
                $.messager.progress('close');
                $.messager.alert("温馨提示", "联系方式至少填一项！", "warning");
                return false;
            }
            // 提交数据。
            var imgChooser = $("#imgChooser");
            if (imgChooser.filebox('getValue') && !$("#fromPhotoId").val()) {
                var blacklist = {
                    blacklistId: $("#blacklistId").val() || "",
                    name: $("#name").textbox("getValue") || "",
                    nickName: $("#nickName").textbox("getValue") || "",
                    enName: $("#enName").textbox("getValue") || "",
                    bornDate: $("#bornDate").datebox("getValue") || "",
                    levelValue: $("#levelValue").combobox("getValue") || "",
                    orgTypeValue: $("#orgTypeValue").combobox("getValue") || "",
                    stateValue: $("#stateValue").combobox("getValue") || "",
                    fromTypeValue: $("#fromTypeValue").combobox("getValue") || "",
                    fromInfo: $("#fromInfo").textbox("getValue") || "",
                    phoneNo: $("#phoneNo").textbox("getValue") || "",
                    qqNo: $("#qqNo").textbox("getValue") || "",
                    microNo: $("#microNo").textbox("getValue") || "",
                    email: $("#email").textbox("getValue") || "",
                    homeUrl: $("#homeUrl").textbox("getValue") || "",
                    provinceId: $("#provinceId").combobox("getValue") || "",
                    cityId: $("#cityId").combobox("getValue") || "",
                    countyId: $("#countyId").combobox("getValue") || "",
                    address: $("#address").textbox("getValue") || ""
                };
                var fileInputId = $(":file[id^=filebox_file_id]").attr("id");
                $.ajaxFileUpload({
                    type: 'POST',
                    data: blacklist,
                    dataType: 'json',
                    url: contentUrl + "/blacklist/addWithImg",
                    fileElementId: fileInputId,
                    success: function (result) {
                        $.messager.progress('close');
                        if (result.success) {
                            if (options.option == "done") {
                                $("#editDialog").window("close");
                                $("#dataGrid").datagrid("reload");
                            } else {
                                var fromType = $("#fromTypeValue").combobox("getValue");
                                var fromInfo = $("#fromInfo").textbox("getValue");
                                $("#editForm").form("reset");
                                $("#fromTypeValue").combobox("setValue", fromType);
                                $("#frominfo").textbox("setValue", fromInfo);
                                $("#blacklistId").val("");
                                $("#addedGrid").datagrid("appendRow", result.data);
                                $("#fromPhotoId").val(result.data.fromPhoto.photoId);
                                imgChooser.filebox('button').linkbutton({text: '清除图片', onClick: clearImg});
                            }
                        } else if (!$("#fromPhotoId").val()) {
                            clearImg();
                        }
                        $.messager.alert('温馨提示', result.message, result.success ? "info" : "warning");
                    },
                    error: function () {
                        $.messager.progress('close');
                        $.messager.alert("温馨提示", "网络或服务出错！", "warning");
                        if (!$("#fromPhotoId").val()) {
                            clearImg();
                        }
                    }
                });
            } else { // 普通表单
                $('#editForm').form('submit', {
                    url: contentUrl + "/blacklist/add",
                    success: function (result) {
                        result = JSON.parse(result);
                        $.messager.progress('close');
                        if (result.success) {
                            if (options.option == "done") {
                                $("#editDialog").window("close");
                                $("#dataGrid").datagrid("reload");
                            } else {
                                var fromType = $("#fromTypeValue").combobox("getValue");
                                var fromInfo = $("#fromInfo").textbox("getValue");
                                $("#editForm").form("reset");
                                $("#fromTypeValue").combobox("setValue", fromType);
                                $("#frominfo").textbox("setValue", fromInfo);
                                $("#blacklistId").val("");
                                $("#addedGrid").datagrid("appendRow", result.data);
                                if (result.data.fromPhoto)
                                    $("#fromPhotoId").val(result.data.fromPhoto.photoId);
                            }
                        }
                        $.messager.alert('温馨提示', result.message, result.success ? "info" : "warning");
                    }
                });
            }
        }

        function previewImg() {
            var imgChooser = $("#imgChooser");
            if (imgChooser.filebox('getValue')) {
                imgChooser.filebox('button').linkbutton({text: '清除图片', onClick: clearImg});
            }
            $("#fromPhotoId").val("");
            var file = $(":file[id^=filebox_file_id]").get(0).files[0];
            if (!file) return true;
            var url = null;
            if (window.createObjectURL) {
                url = window.createObjectURL(file);
            } else if (window.URL) {
                url = window.URL.createObjectURL(file);
            } else if (window.webkitURL) {
                url = window.webkitURL.createObjectURL(file);
            } else {
                $.messager.alert("温馨提示", "您的浏览器不支持预览图片！", "warning");
                return true;
            }
            $('#imgPreview').attr("src", url).removeAttr("hidden");
        }

        //清除图片可重新选择
        function clearImg() {
            var imgChooser = $("#imgChooser");
            imgChooser.filebox('clear');
            imgChooser.filebox({buttonText: '选择图片', onChange: previewImg});
            $("#fromPhotoId").val("");
            $("#addedGrid").datagrid("loadData", []);
            $('#imgPreview').attr("src", "#").attr("hidden", "hidden");
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

<div data-options="region:'north',iconCls:'icon-home',border:false" style="height:50px;padding:8px">
    <table>
        <tr>
            <td>
                <input id="nameInput" class="easyui-textbox" data-options="prompt:'黑名单名称'" style="width:300px">
            </td>
            <td style="width: 15px"></td>
            <td>
                <input id="phoneNoInput" class="easyui-textbox" data-options="prompt:'手机号码'" style="width:300px">
            </td>
            <td></td>
        </tr>
    </table>
</div>

<div data-options="region:'center',iconCls:'icon-user',border:false">
    <div id="dataGrid" data-options="border:false">
    </div>
</div>

<div id="editDialog" class="easyui-dialog" hidden="hidden" style="width:800px;height:600px"
     data-options="closed:true,iconCls:'icon-view',resizable:true,maximizable:true,modal:true,title:'编辑属性'">
    <div class="easyui-layout" data-options="border:false,fit:true">
        <div data-options="region:'north',height:130,title:'当前图片已录入的黑名单',iconCls:'icon-flag_checked'">
            <div id="addedGrid" data-options="border:false">
            </div>
        </div>
        <div data-options="region:'center',width:350" style="padding:10px">
            　　<input id="imgChooser" class="easyui-filebox" style="width:350px" name="file"
                     data-options="buttonText:'选择图片',buttonIcon:'icon-picture',accept:'image/*',
                     prompt:'请选择 JPG 格式图片',onChange:previewImg"/>
            <br/><br/>
            <img id="imgPreview" hidden="hidden" src="#" alt="图片预览失败" style="border-radius:3px"/>
        </div>
        <div data-options="region:'east',width:320" style="width:320px;padding:10px">
            <form id="editForm" method="post">
                <input type="hidden" id="blacklistId" name="blacklistId"/>
                <input type="hidden" id="fromPhotoId" name="fromPhotoId"/>
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