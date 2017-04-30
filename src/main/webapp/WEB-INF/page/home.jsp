<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>猎狐管理员系统</title>
    <meta charset="UTF-8"/>
    <base href="<%=request.getContextPath()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/bootstrap/mycss/glyphicon.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/easy-ui/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/easy-ui/themes/icon-lib.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/easy-ui/themes/bootstrap/easyui.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/easy-ui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/public/easy-ui/locale/easyui-lang-zh_CN.js"></script>
    <style type="text/css">
        html, body {
            height: 100%;
        }

        .overflowDiv {
            overflow: hidden
        }

        .tree-file {
            background: url('<%=request.getContextPath()%>/public/easy-ui/themes/icon-lib/picture.png') no-repeat center center;
        }

    </style>
    <script type="text/javascript">

        var contentUrl = "<%=request.getContextPath()%>";

        function init() {
            $.ajax({
                url: contentUrl + "/manager/loginer",
                dataType: "json",
                success: function (data) {
                    if (data.success) {
                        window.loginer = data.data; // 绑定到window上
                        $("#helloSpan").html("欢迎" + data.data.name + "！");
                    }
                }
            });

            $('#menu-manager').tree({
                data: [{
                    elementId: "managerItem",
                    url: "/page/manager",
                    text: '系统管理员',
                    iconCls: "icon-user_suit"
                }],
                onClick: addTab
            });

            $('#menu-photo').tree({
                data: [{
                    elementId: "ptotoItem1",
                    url: "/page/photo",
                    text: '图片待审核',
                    iconCls: "icon-photo"
                }, {
                    elementId: "ptotoItem2",
                    url: "/page/photoChecked",
                    text: '图片已审核',
                    iconCls: "icon-photo"
                }, {
                    elementId: "ptotoItem3",
                    url: "/page/photoCheckedNo",
                    text: '审核未通过',
                    iconCls: "icon-photo"
                }],
                onClick: addTab
            });

            $('#menu-blacklist').tree({
                data: [{
                    elementId: "blacklistItem",
                    url: "/page/blacklist",
                    text: '黑名单管理',
                    iconCls: "icon-user_suit_black"
                },{
                    elementId: "blackQQGroupItem",
                    url: "/page/blackQQGroup",
                    text: '黑名QQ群',
                    iconCls: "icon-people"
                }],
                onClick: addTab
            });

            $('#menu-custom').tree({
                data: [{
                    elementId: "customItem",
                    url: "/page/custom",
                    text: '客户管理',
                    iconCls: "icon-user_suit"
                }],
                onClick: addTab
            });

            $('#menu-client').tree({
                data: [{
                    elementId: "clientItem",
                    url: "/page/client",
                    text: '客户端管理',
                    iconCls: "icon-computer"
                }],
                onClick: addTab
            });

            $('#menu-bill').tree({
                data: [{
                    elementId: "billItem",
                    url: "/page/bill",
                    text: '账单流水',
                    iconCls: "icon-money"
                }],
                onClick: addTab
            });
        }

        function addTab(node) {
            if (!node.text || !node.elementId || !node.url) {
                return true;
            }
            var name = node.text;
            var iframeId = node.elementId + "Iframe";
            var tabs = $("#center-tabs");
            var tab = tabs.tabs('getTab', name);
            if (tab) {
                tabs.tabs('select', name);
                return true;
            }
            tabs.tabs('add', {
                fit: true,
                bodyCls: "overflowDiv",
                iconCls: node.iconCls || "icon-page",
                closable: true,
                title: name,
                content: "<iframe id='" + iframeId + "' scrolling='no' frameborder=0><iframe>",
                onResize: function (width, height) {
                    var iframe = $("#" + iframeId);
                    iframe.width(width).height(height);
                }
            });
            var iframe = $("#" + iframeId);
            iframe.width(iframe.parent().width());
            iframe.height(iframe.parent().height());
            iframe.attr("src", contentUrl + node.url);
        }

        function exit() {
            $.ajax({
                url: contentUrl + "/manager/exit",
                success: function () {
                    window.location.href = contentUrl + "/index.html";
                },
                error: function () {
                    window.location.href = contentUrl + "/index.html";
                }
            });
        }

    </script>
</head>
<body class="easyui-layout" onload="init();">

<div data-options="region:'north',iconCls:'icon-home',border:false" style="height:65px;padding:3px 10px">
    <img height="58" style="float: left;vertical-align:middle" src="<%=request.getContextPath()%>/public/image/foxhunter.png"/>
    <h1 style="float: left;color: #0081c2;margin-left:8px;font-family: SimHei">猎狐管理员系统</h1>
    <div style="float: right">
        <br/><span id="helloSpan" style="text-align: right">您尚未登录！</span>
        <a href="#" class="easyui-linkbutton" onclick="exit()" data-options="iconCls:'icon-door_out',plain:true">安全退出</a>
    </div>
</div>

<div data-options="region:'west',iconCls:'icon-application_view_tile',title:'功能菜单'" style="width:250px;">
    <div id="west-accordion" class="easyui-accordion" data-options="border:0,fit:true,selected:0">
        <div title="审核管理" data-options="iconCls:'icon-photo'">
            <div id='menu-photo'></div>
        </div>
        <div title="黑名单管理" data-options="iconCls:'icon-mail'">
            <div id='menu-blacklist'></div>
        </div>
        <div title="客户管理" data-options="iconCls:'icon-user_suit'">
            <div id='menu-custom'></div>
        </div>
        <div title="提现管理" data-options="iconCls:'icon-wallet'">
            <div id='menu-bill'></div>
        </div>
        <div title="系统管理员" data-options="iconCls:'icon-client'">
            <div id='menu-manager'></div>
        </div>
        <div title="客户端管理" data-options="iconCls:'icon-computer'">
            <div id='menu-client'></div>
        </div>
        <div title="统计信息" data-options="iconCls:'icon-chart_bar'">
            <div id='menu-statistic'></div>
        </div>
    </div>
</div>

<div data-options="region:'center',iconCls:'icon-picture'">
    <div id="center-tabs" data-options="title:'工作区域',border:0,fit:true,tabWidth:'150px'" class="easyui-tabs">
        <div title=" 主 页 " data-options="iconCls:'icon-house'">
            <div class="easyui-layout" data-options="fit:true">
                <div data-options="region:'center',border:false,fit:true" style="text-align: center">
                    <div style="height:20%"></div>
                    <div style="color: #0081c2;font-family: SimHei;font-size: 50px">互联网金融黑名单方案</div>
                </div>
                <div data-options="region:'south',border:false,height:270" style="background-repeat:repeat-x;
                        background: url('<%=request.getContextPath()%>/public/image/homebg.png')">
                </div>
            </div>
        </div>
    </div>
</div>

<div data-options="region:'south',iconCls:'icon-note'" style="height:28px;text-align:center;padding:5px">
    <div style="color:#0081c2;font-family: SimHei;;font-size:14px">原形信息技术有限公司</div>
</div>

</body>
</html>