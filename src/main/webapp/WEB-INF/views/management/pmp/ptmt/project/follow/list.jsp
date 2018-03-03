<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="dwz" uri="http://www.anycc.com/dwz" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>项目管理</title>
    <jsp:include page="/iframeInclude/pmp/dataTableHead.jsp"></jsp:include>
    <link href="${pageContext.request.contextPath}/resources/css/plugins/sweetalert/sweetalert.css"
          rel="stylesheet" type="text/css">
    <script src="${pageContext.request.contextPath}/resources/js/plugins/sweetalert/sweetalert.min.js"></script>
    <script
            src="${pageContext.request.contextPath}/resources/js/plugins/validate/jquery.validate.min.js"></script>
    <script
            src="${pageContext.request.contextPath}/resources/js/plugins/validate/messages_zh.min.js"></script>
    <script
            src="${pageContext.request.contextPath}/resources/js/pmp/ptmt/follow/aede.pmp.ptmt.project.follow.js"></script>

    <!-- easyUI  控件 -->
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/themes/icon.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/jquery.easyui.min.js"></script>


</head>
<body class="gray-bg">
<div class="wrapper wrapper-content  ">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5><i class="fa fa-search"></i>查询条件</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link">
                            <i class="fa fa-chevron-up"></i>
                        </a>
                    </div>
                </div>
                <div class="ibox-content">
                    <div class="row">
                        <div class="col-md-3 col-sm-3">
                            <div class="form-group">
                                <div class="input-icon">
                                    <input type="text" class="form-control"
                                           id="pmp_ptmt_project_query_pname" placeholder="项目名称">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-3">
                            <div class="form-group">
                                <div class="input-icon">
                                    <input type="text" class="form-control"
                                           id="pmp_ptmt_project_query_userName" placeholder="跟踪人">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-3">
                            <div class="" align="center">
                                <button type="button" id="pmp_ptmt_project_btnReset"
                                        class="btn default">
                                    <i class="fa fa-history"></i>重置
                                </button>
                                <button type="submit" id="pmp_ptmt_project_btnSearch"
                                        class="btn btn-primary">
                                    <i class="fa fa-search"></i> 查询
                                </button>
                            </div>
                        </div>

                    </div>

                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5><i class="fa fa-list"></i>项目跟踪列表</h5>

                </div>
                <div class="ibox-content">
                    <table id="pmp_ptmt_project_table"
                           class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>项目名称</th>
                            <th>跟踪人</th>
                            <th>跟踪进度</th>
                            <th>跟踪时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


<jsp:include page="/iframeInclude/foot.jsp"></jsp:include>
</body>
</html>
