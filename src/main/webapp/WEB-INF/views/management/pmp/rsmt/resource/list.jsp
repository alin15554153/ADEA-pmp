<%--
  Created by IntelliJ IDEA.
  User: Shibin
  Date: 2016/3/7
  Time: 12:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dwz" uri="http://www.anycc.com/dwz"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>资源维护</title>
    <jsp:include page="/iframeInclude/pmp/dataTableHead.jsp"></jsp:include>

    <link href="${ctx}/resources/css/plugins/sweetalert/sweetalert.css" rel="stylesheet" type="text/css">
    <script src="${ctx}/resources/js/plugins/sweetalert/sweetalert.min.js"></script>

    <script src="${ctx}/resources/js/plugins/validate/jquery.validate.min.js"></script>
    <script src="${ctx}/resources/js/plugins/validate/messages_zh.min.js"></script>

    <!--webuploader-->
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/plugins/webuploader/webuploader.css">
    <script type="text/javascript" src="${ctx}/resources/js/plugins/webuploader/webuploader.js"></script>

    <script src="${ctx}/resources/js/pmp/rsmt/aede.pmp.rsmt.resource.js" type="text/javascript"></script>
    <script src="${ctx}/resources/js/pmp/rsmt/aede.pmp.rsmt.resourceUploader.js" type="text/javascript"></script>

    <style>
        .webuploader-pick {
            position: relative;
            display: inline-block;
            cursor: pointer;
            background: #1ab394;
            padding: 5px 15px;
            color: #fff;
            text-align: center;
            font-size: 14px;
            border-radius: 3px;
            overflow: hidden;
        }
        .webuploader-pick-hover {
            background: #18a689;
        }

    </style>
</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>查询条件</h5>
                        <div class="ibox-tools">
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                        <form id="rsmt_resource_list_query_form" role="form">
                            <div class="form-body">
                                <div class="row">
                                    <div class="col-md-3 col-sm-3">
                                        <div class="form-group">
                                            <div class="input-icon">
                                                <input type="text" class="form-control"
                                                       id="rsmt_resource_list_query_name" placeholder="资源名称">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-3 col-sm-3">
                                        <div class="form-group">
                                            <div class="input-icon">
                                                <%--<input type="text" class="form-control"
                                                       id="rsmt_resource_list_query_type" placeholder="资源类别">--%>
                                                <dwz:dic themeName="资源类别" className="form-control"
                                                         paramName="rsmt_resource_list_query_type">
                                                    <option value="">---请选择资源类别---</option>
                                                </dwz:dic>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-3 col-sm-3">
                                        <div class="form-group">
                                            <div class="input-icon">
                                                <%--<input type="text" class="form-control"
                                                       id="rsmt_resource_list_query_stage" placeholder="阶段">--%>
                                                <%--<select class="form-control" id="rsmt_resource_list_query_stage"></select>--%>
                                                    <dwz:dic themeName="阶段名称" className="form-control"
                                                             paramName="rsmt_resource_list_query_stage">
                                                        <option value="">---请选择阶段---</option>
                                                    </dwz:dic>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-3 col-sm-3">
                                        <div class="form-group">
                                            <div class="input-icon">
                                                <input type="text" class="form-control"
                                                       id="rsmt_resource_list_query_project_name" placeholder="项目名称">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-actions">
                                <div class="row">
                                    <div class="col-md-12 col-sm-12">
                                        <div class="" align="center">
                                            <button type="button"
                                                    id="rsmt_resource_list_query_btnReset" class="btn default">
                                                <i class="fa fa-history"></i>重置
                                            </button>
                                            <button type="submit"
                                                    id="rsmt_resource_list_query_btnSearch" class="btn btn-primary">
                                                <i class="fa fa-search"></i> 查询
                                            </button>
                                        </div>
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
                        <h5>数据列表</h5>
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
                        <table id="rsmt_resource_list_table" class="table table-striped table-bordered table-hover">
                            <thead>
                            <tr>
                                <th>资源名称</th>
                                <th>资源类别</th>
                                <th>项目名称</th>
                                <th>阶段</th>
                                <th>上传人</th>
                                <th>上传时间</th>
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

    <div id="rsmt_resource_list_modal" class="modal fade" aria-hidden="true" tabindex="1" data-backdrop="static" role="dialog" aria-labelledby="ModalLabel" data-width="480">
        <div class="modal-dialog" >
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="userModalLabel">资源维护</h4>
                </div>
                <div class="modal-body" style="height: 200px; overflow-y: auto;">
                    <!-- BEGIN FORM-->
                    <form method="post" action="" class="form-horizontal">
                        <input id="rsmt_resource_list_modal_hide_id" name="id"  type="hidden" class="form-control" >
                        <input id="rsmt_resource_list_modal_hide_company" name="id"  type="hidden" class="form-control" >
                        <input id="rsmt_resource_list_modal_hide_procode" name="id"  type="hidden" class="form-control" >
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-4 control-label">
                                    资源类别
                                    <span class="required" style="color: red">*</span>
                                </label>
                                <div class="col-md-8">
                                    <%--<input type="text" class="form-control" name="type" id="rsmt_resource_list_modal_type" placeholder="资源类别">--%>
                                        <dwz:dic themeName="资源类别" className="form-control"
                                                 paramName="rsmt_resource_list_modal_type">
                                            <option value="">---请选择资源类别---</option>
                                        </dwz:dic>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">
                                    资源:
                                    <span class="required" style="color: red">*</span>
                                </label>
                                <div class="col-md-8">
                                    <input type="hidden" class="form-control" name="name" id="rsmt_resource_list_modal_name" placeholder="资源名称">
                                    <input type="hidden" class="form-control" name="path" id="rsmt_resource_list_modal_path" placeholder="资源名称">
                                    <div id="uploader" class="wu-example">

                                    </div>

                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- END FORM-->
                </div>
                <div class="modal-footer">
                    <button type="button" id="rsmt_resource_list_modal_btnSave" onclick="" class="btn btn-primary">保存</button>
                    <button type="button" data-dismiss="modal" class="btn">取消</button>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/iframeInclude/foot.jsp"></jsp:include>

</body>
</html>
