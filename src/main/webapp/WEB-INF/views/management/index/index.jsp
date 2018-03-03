<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <title>项目管理系统</title>
    <meta name="keywords" content="响应式后台">
    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html"/>
    <![endif]-->
    <jsp:include page="../include/head.jsp"></jsp:include>
</head>

<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
<jsp:include page="../include/loginUser.jsp"></jsp:include>
<div id="wrapper">
    <!--左侧导航开始-->
    <jsp:include page="../include/sidebar.jsp"></jsp:include>
    <!--左侧导航结束-->
    <!--右侧部分开始-->
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row content-tabs">
            <button class="roll-nav roll-left J_tabMenu navbar-minimalize"><i class="fa fa-dedent"></i></button>
            <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i></button>
            <nav class="page-tabs J_menuTabs">
                <div class="page-tabs-content">
                    <a href="javascript:;" class="active J_menuTab" data-id="mainPage">首页</a>
                </div>
            </nav>
            <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
            </button>
            <div class="btn-group roll-nav roll-right">
                <button class="dropdown J_tabClose" data-toggle="dropdown">关闭操作<span class="caret"></span>
                </button>
                <ul role="menu" class="dropdown-menu dropdown-menu-right">
                    <li class="J_tabShowActive"><a>定位当前选项卡</a>
                    </li>
                    <li class="divider"></li>
                    <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                    </li>
                    <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                    </li>
                </ul>
            </div>
            <div class="btn-group roll-nav roll-right J_tabNotice">
                <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                    <i class="fa fa-envelope"></i><span class="label label-warning"
                                                        id="aede_mq_sp_remind_count">0</span>
                </a>
                <ul role="menu" class="dropdown-menu dropdown-menu-right dropdown-alerts" id="aede_mq_ul_newMessage">
                </ul>
            </div>
            <a href="${pageContext.request.contextPath}/logout" class="roll-nav roll-right J_tabExit"><i
                    class="fa fa fa-sign-out"></i> 退出</a>
        </div>
        <div class="row J_mainContent" id="content-main">
            <iframe class="J_iframe" name="iframe0" width="100%" height="100%"
                    src="${pageContext.request.contextPath}/management/commmon/index/main" frameborder="0"
                    data-id="mainPage" seamless></iframe>
        </div>
        <jsp:include page="../include/footer.jsp"></jsp:include>
    </div>
    <!--右侧部分结束-->
    <!--右侧边栏开始-->
    <%--<jsp:include page="../include/right-sidebar.jsp"></jsp:include>--%>
    <!--右侧边栏结束-->
    <!--mini聊天窗口开始-->
    <%-- <jsp:include page="../include/chatBox.jsp"></jsp:include> --%>
    <!--mini聊天窗口结束-->
</div>
</body>

</html>
