<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>项目资源管理平台</title>
    <meta name="keywords" content="响应式后台">
    <meta name="description" content="基于Bootstrap3最新版本开发的扁平化主题，她采用了主流的左右两栏式布局，使用了Html5+CSS3等现代技术">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/favicon.ico">
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/resources/js/jquery.min.js?v=2.1.4"></script>
    <script src="${pageContext.request.contextPath}/resources/js/plugins/jquery.cookie.1.4.1/jquery.cookie.js"></script>
    <script type="text/javascript">
        function before_submit() {
            if ($("#ck_rmbUser").prop("checked")) {
                var username = $("#txt_userName").val();
                var password = $("#txt_password").val();
                $.cookie("rmbUser", "true", {expires: 7}); //存储一个带7天期限的cookie
                $.cookie("username", username, {expires: 7});
                $.cookie("password", password, {expires: 7});
            } else {
                $.cookie("rmbUser", "false", {expire: -1});
                $.cookie("username", "", {expires: -1});
                $.cookie("password", "", {expires: -1});
            }
        }
        ;
    </script>
</head>
<body>
<div class="main-signin">
    <div class="main-body-signin">
        <form class="form-signin" action="${pageContext.request.contextPath}/login" method="post">
            <h1 class="title"><img src="${pageContext.request.contextPath}/resources/imgs/logo.png" alt=""></h1>
            <h3 class="subtitle">项目资源管理平台</h3>
            <label class="sr-only">用户名</label>
            <input type="text" class="form-control" id="txt_userName" name="username" placeholder="用户名" required>
            <label class="sr-only">密码</label>
            <input type="password" class="form-control" id="txt_password" name="password" placeholder="密码" required>
            <div class="checkbox">
                <label>
                    <input type="checkbox" id="ck_rmbUser"/>记住用户名和密码
                </label>
            </div>
            <button class="btn btn-primary btn-block btn-login" type="submit" onclick="before_submit()">登录</button>
            <div align="center">
                <c:if test="${msg!=null }">
                    <p style="color: red; margin-left: 10px;">${msg }</p>
                    <script type="text/javascript">
                        $.cookie("rmbUser", "false", {expire: -1});
                        $.cookie("username", "", {expires: -1});
                        $.cookie("password", "", {expires: -1});
                    </script>
                </c:if>
            </div>
        </form>
    </div>
</div>
<div id="particles">
    <canvas id="Mycanvas"></canvas>
</div>
<!-- 全局js -->
<script type="text/javascript">
    $(document).ready(function () {
        if ($.cookie("rmbUser") == "true") {
            $("#ck_rmbUser").prop("checked", true);
            $("#txt_userName").val($.cookie("username"));
            $("#txt_password").val($.cookie("password"));
        }
    });
    //定义画布宽高和生成点的个数
    var WIDTH = window.innerWidth,
            HEIGHT = window.innerHeight,
            POINT = 50;
    var canvas = document.getElementById('Mycanvas');
    canvas.width = WIDTH,
            canvas.height = HEIGHT;
    var context = canvas.getContext('2d');
    context.strokeStyle = 'rgba(0,0,0,0.2)',
            context.strokeWidth = 1,
            context.fillStyle = 'rgba(0,0,0,0.1)';
    var circleArr = [];

    //线条：开始xy坐标，结束xy坐标，线条透明度
    function Line(x, y, _x, _y, o) {
        this.beginX = x,
                this.beginY = y,
                this.closeX = _x,
                this.closeY = _y,
                this.o = o;
    }
    //点：圆心xy坐标，半径，每帧移动xy的距离
    function Circle(x, y, r, moveX, moveY) {
        this.x = x,
                this.y = y,
                this.r = r,
                this.moveX = moveX,
                this.moveY = moveY;
    }
    //生成max和min之间的随机数
    function num(max, _min) {
        var min = arguments[1] || 0;
        return Math.floor(Math.random() * (max - min + 1) + min);
    }
    // 绘制原点
    function drawCricle(cxt, x, y, r, moveX, moveY) {
        var circle = new Circle(x, y, r, moveX, moveY)
        cxt.beginPath()
        cxt.arc(circle.x, circle.y, circle.r, 0, 2 * Math.PI)
        cxt.closePath()
        cxt.fill();
        return circle;
    }
    //绘制线条
    function drawLine(cxt, x, y, _x, _y, o) {
        var line = new Line(x, y, _x, _y, o)
        cxt.beginPath()
        cxt.strokeStyle = 'rgba(0,0,0,' + o + ')'
        cxt.moveTo(line.beginX, line.beginY)
        cxt.lineTo(line.closeX, line.closeY)
        cxt.closePath()
        cxt.stroke();

    }
    //每帧绘制
    function draw() {
        context.clearRect(0, 0, canvas.width, canvas.height);
        for (var i = 0; i < POINT; i++) {
            drawCricle(context, circleArr[i].x, circleArr[i].y, circleArr[i].r);
        }
        for (var i = 0; i < POINT; i++) {
            for (var j = 0; j < POINT; j++) {
                if (i + j < POINT) {
                    var A = Math.abs(circleArr[i + j].x - circleArr[i].x),
                            B = Math.abs(circleArr[i + j].y - circleArr[i].y);
                    var lineLength = Math.sqrt(A * A + B * B);
                    var C = 1 / lineLength * 7 - 0.009;
                    var lineOpacity = C > 0.03 ? 0.03 : C;
                    if (lineOpacity > 0) {
                        drawLine(context, circleArr[i].x, circleArr[i].y, circleArr[i + j].x, circleArr[i + j].y, lineOpacity);
                    }
                }
            }
        }
    }
    //初始化生成原点
    function init() {
        circleArr = [];
        for (var i = 0; i < POINT; i++) {
            circleArr.push(drawCricle(context, num(WIDTH), num(HEIGHT), num(15, 2), num(10, -10) / 40, num(10, -10) / 40));
        }
        draw();
    }
    //调用执行
    window.onload = function () {
        init();
        setInterval(function () {
            for (var i = 0; i < POINT; i++) {
                var cir = circleArr[i];
                cir.x += cir.moveX;
                cir.y += cir.moveY;
                if (cir.x > WIDTH) cir.x = 0;
                else if (cir.x < 0) cir.x = WIDTH;
                if (cir.y > HEIGHT) cir.y = 0;
                else if (cir.y < 0) cir.y = HEIGHT;
            }
            draw();
        }, 10);
    }
</script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js?v=3.3.6"></script>
</body>

</html>
