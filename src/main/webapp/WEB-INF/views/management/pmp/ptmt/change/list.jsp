<%--
  Created by IntelliJ IDEA.
  User: Shibin
  Date: 2016/3/9
  Time: 8:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dwz" uri="http://www.anycc.com/dwz" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>项目变更</title>
    <jsp:include page="/iframeInclude/pmp/dataTableHead.jsp"></jsp:include>
    <script src="${pageContext.request.contextPath}/resources/js/pmp/ptmt/change/aede.pmp.ptmt.change.js"
            type="text/javascript"></script>

</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
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
                    <form id="ptmt_change_list_query_form" class="form-inline">
                        <div class="row">
                            <div class="col-md-4 col-sm-4">
                                <div class="form-group">
                                    <div class="input-icon">
                                        <input type="text" class="form-control"
                                               id="ptmt_change_list_query_code" placeholder="项目编号">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 col-sm-4">
                                <div class="form-group">
                                    <div class="input-icon">
                                        <input type="text" class="form-control"
                                               id="ptmt_change_list_query_name" placeholder="项目名称">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 col-sm-4">
                                <div class="form-group" align="center">
                                    <button type="button"
                                            id="ptmt_change_list_query_btnReset" class="btn default">
                                        <i class="fa fa-history"></i>重置
                                    </button>
                                    <button type="submit"
                                            id="ptmt_change_list_query_btnSearch" class="btn btn-primary">
                                        <i class="fa fa-search"></i> 查询
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>项目列表</h5>
                    <!-- <div class="ibox-tools">
                        <a data-toggle="dropdown" href="javascript:;" class="btn btn-deepBlue" aria-expanded="false">
                            <i class="fa fa-cogs"></i>
                                更多操作
                            <i class="fa fa-angle-down"></i>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a href="table_data_tables.html#">
                                    <i class="fa fa-send-o"></i>导出
                                </a>
                            </li>
                            <li>
                                <a href="table_data_tables.html#">
                                    <i class="fa fa-print"></i>打印
                                </a>
                            </li>
                        </ul>
                    </div> -->
                </div>
                <div class="ibox-content">
                    <table id="ptmt_change_list_table" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>项目编号</th>
                            <th>项目名称</th>
                            <th>项目创建人</th>
                            <th>项目进度</th>
                            <th>项目等级</th>
                            <th>金额(万元)</th>
                            <th>状态</th>
                            <th>阶段</th>
                            <th>操作</th>
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
