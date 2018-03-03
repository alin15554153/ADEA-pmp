<%--
  Created by IntelliJ IDEA.
  User: Shibin
  Date: 2016/3/9
  Time: 10:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="dwz" uri="http://www.anycc.com/dwz"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>阶段变更</title>
    <jsp:include page="/iframeInclude/pmp/dataTableHead.jsp"></jsp:include>
    <script src="${pageContext.request.contextPath}/resources/js/plugins/validate/jquery.validate.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/plugins/validate/messages_zh.min.js"></script>

    <script src="${pageContext.request.contextPath}/resources/js/pmp/ptmt/change/aede.pmp.ptmt.changePage.js"></script>

</head>
<body class="gray-bg">
    <input type="hidden" id="pmp_ptmt_change_changePage_project_hide_id" value="${id}">
    <input type="hidden" id="pmp_ptmt_change_changePage_project_hide_stage">
    <div class="wrapper wrapper-content">
        <div class="row">
            <div class="col-md-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>项目基本信息</h5>
                        <div class="ibox-tools">
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal">
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="control-label col-md-3">
                                            项目编号
                                        </label>
                                        <div class="col-md-9">
                                            <input id="pmp_ptmt_change_changePage_project_code" class="form-control" type="text" disabled>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="control-label col-md-3">
                                            项目名称
                                        </label>
                                        <div class="col-md-9">
                                            <input id="pmp_ptmt_change_changePage_project_name" class="form-control" type="text" disabled>
                                        </div>
                                    </div>
                                </div><div class="col-md-4">
                                <div class="form-group">
                                    <label class="control-label col-md-3">
                                        项目类型
                                    </label>
                                    <div class="col-md-9">
                                        <input id="pmp_ptmt_change_changePage_project_type" class="form-control" type="text" disabled>
                                    </div>
                                </div>
                            </div>
                            </div>
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="control-label col-md-3">
                                            项目性质
                                        </label>
                                        <div class="col-md-9">
                                            <input id="pmp_ptmt_change_changePage_project_nature" class="form-control" type="text" disabled>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="control-label col-md-3">
                                            项目经理
                                        </label>
                                        <div class="col-md-9">
                                            <input id="pmp_ptmt_change_changePage_project_manager" class="form-control" type="text" disabled>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="control-label col-md-3">
                                            建立日期
                                        </label>
                                        <div class="col-md-9">
                                            <input id="pmp_ptmt_change_changePage_project_ctime" class="form-control" type="text" disabled>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="control-label col-md-3">
                                            操作人
                                        </label>
                                        <div class="col-md-9">
                                            <input id="pmp_ptmt_change_changePage_project_cid" class="form-control" type="text" disabled>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="control-label col-md-3">
                                            申报人
                                        </label>
                                        <div class="col-md-9">
                                            <input id="pmp_ptmt_change_changePage_project_director" class="form-control" type="text" disabled>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="control-label col-md-3">
                                            复核人
                                        </label>
                                        <div class="col-md-9">
                                            <input id="pmp_ptmt_change_changePage_project_checker" class="form-control" type="text" disabled>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="control-label col-md-3">
                                            等级
                                        </label>
                                        <div class="col-md-9">
                                            <input id="pmp_ptmt_change_changePage_project_level" class="form-control" type="text" disabled>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="control-label col-md-3">
                                            进度
                                        </label>
                                        <div class="col-md-9">
                                            <input id="pmp_ptmt_change_changePage_project_progress" class="form-control" type="text" disabled>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="control-label col-md-3">
                                            金额
                                        </label>
                                        <div class="col-md-9">
                                            <input id="pmp_ptmt_change_changePage_project_amt" class="form-control" type="text" disabled>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label class="control-label col-md-1">
                                            备注
                                        </label>
                                        <div class="col-md-11">
                                            <textarea id="pmp_ptmt_change_changePage_project_remark" class="form-control" rows="3" disabled></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>变更履历</h5>
                        <div class="ibox-tools">
                            <a href="javascript:;" onclick="$.aede.comm.closeTab();"
                               class="btn btn-primary btn-xs" role="button">
                                <i class="fa fa-reply-all"></i> 返回</a>
                            <a href="javascript:;"  id="pmp_ptmt_change_changePage_btnShowModal"  class="btn btn-primary btn-xs" role="button">
                                <i class="fa fa-save"></i> 变更</a>
                            <!-- <a data-toggle="dropdown" href="javascript:;" class="btn btn-deepBlue" aria-expanded="false">
                                <i class="fa fa-cogs"></i>
                                更多操作
                                <i class="fa fa-angle-down"></i>
                            </a> -->
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
                        </div>
                    </div>
                    <div class="ibox-content">
                        <table class="table table-striped table-bordered table-hover"
                               id="pmp_ptmt_change_changePage_table">
                            <thead>
                            <tr>
                                <th>变更人</th>
                                <th>变更时间</th>
                                <th>变更状态</th>
                                <th>变更理由</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div id="pmp_ptmt_change_changePage_modal" class="modal fade"
         aria-hidden="true" tabindex="-1" data-backdrop="static" role="dialog"
         aria-labelledby="ModalLabel" data-width="720">
        <div class="modal-dialog" style="width: 40%">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                    </button>
                    <h4 class="modal-title" id="projectstageModalLabel">新增阶段变更</h4>
                </div>
                <div class="modal-body" style="height: 200px; overflow-y: auto;">
                    <form method="post" action="" class="form-horizontal">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-3 control-label">阶段</label>
                                <div class="col-md-9">
                                    <select type="text" class="form-control" name="type"
                                           id="pmp_ptmt_change_changePage_modal_type">
                                    </select>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label">变更理由</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" name="changeReason"
                                           id="pmp_ptmt_change_changePage_modal_changeReason">
                                </div>
                            </div>
                        </div>
                    </form>

                </div>
                <div class="modal-footer">
                    <input id="pmp_ptmt_change_changePage_modal_hide_id" type="hidden" class="form-control">
                    <button type="button" id="pmp_ptmt_change_changePage_modal_btnSave"
                            onclick="" class="btn btn-primary">保存</button>
                    <button type="button" data-dismiss="modal" class="btn">取消</button>
                </div>
            </div>
        </div>

    </div>

    <jsp:include page="/iframeInclude/foot.jsp"></jsp:include>
</body>
</html>
