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
            src="${pageContext.request.contextPath}/resources/js/pmp/ptmt/aede.pmp.ptmt.project.js"></script>

    <!-- easyUI  控件 -->
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/themes/icon.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/jquery.easyui.min.js"></script>

    <!--webuploader-->
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/resources/css/plugins/webuploader/webuploader.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/js/plugins/webuploader/webuploader.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/pmp/rsmt/aede.pmp.rsmt.resourceUploader.js"
            type="text/javascript"></script>

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
                        <div class="col-md-4 col-sm-4">
                            <div class="form-group">
                                <div class="input-icon">
                                    <input type="text" class="form-control"
                                           id="pmp_ptmt_project_query_code" placeholder="项目编号">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 col-sm-4">
                            <div class="form-group">
                                <div class="input-icon">
                                    <input type="text" class="form-control"
                                           id="pmp_ptmt_project_query_name" placeholder="项目名称">
                                </div>
                            </div>
                        </div>

                        <div class="col-md-4 col-sm-4">
                            <div class="form-group">
                                <div class="input-icon">
                                    <input type="text" class="form-control"
                                           id="pmp_ptmt_project_query_projectSource" placeholder="项目来源">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 col-sm-4">
                            <dwz:dic themeName="项目类别" className="form-control"
                                     paramName="pmp_ptmt_project_query_type">
                                <option value="">---请选择项目类别---</option>
                            </dwz:dic>

                        </div>
                        <div class="col-md-4 col-sm-4">
                            <dwz:dic themeName="项目状态" className="form-control"
                                     paramName="pmp_ptmt_project_query_status">
                                <option value="">---请选择项目状态---</option>
                            </dwz:dic>

                        </div>
                        <div class="col-md-4 col-sm-4">
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
                    <h5><i class="fa fa-list"></i>项目列表</h5>
                    <div class="ibox-tools">
                        <a href="javascript:;" onclick="$.aede.pmp.ptmt.project.addProjectModal();"
                           class="btn btn-primary btn-xs" role="button">
                            <i class="fa fa-plus"></i> 新增</a>
                        <a data-toggle="dropdown" href="javascript:;"
                           class="btn btn-deepBlue" aria-expanded="false"> <i
                                class="fa fa-cogs"></i> 导出操作 <i class="fa fa-angle-down"></i></a>
                        <ul class="dropdown-menu">
                            <li><a href="${pageContext.request.contextPath}/management/pmp/ptmt/project/export?type=1">
                                <i
                                        class="fa fa-send-o"></i>导出自建项目
                            </a></li>
                            <li><a href="${pageContext.request.contextPath}/management/pmp/ptmt/project/export?type=2">
                                <i
                                        class="fa fa-send-o"></i>导出外部项目
                            </a></li>
                        </ul>
                    </div>
                </div>
                <div class="ibox-content">
                    <table id="pmp_ptmt_project_table"
                           class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>项目编号lgl</th>
                            <th>项目名称</th>
                            <th>项目创建人</th>
                            <th>项目进度</th>
                            <th>项目等级</th>
                            <th>金额(万元)</th>
                            <th>状态</
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

<div id="pmp_ptmt_project_modal" class="modal fade" aria-hidden="true"
     tabindex="-1" data-backdrop="static" role="dialog"
     aria-labelledby="ModalLabel" data-width="600">
    <div class="modal-dialog" style="display: inline-block; width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="userModalLabel">项目</h4>
            </div>
            <div class="modal-body" style="height: 300px; overflow-y: auto;">
                <form method="post" action="" class="form-horizontal">
                    <input type="hidden" class="form-control">
                    <div class="form-body">
                        <div class="row" id="pmp_ptmt_project_stagetablediv">
                            <div class="col-sm-12">
                                <div class="ibox float-e-margins">
                                    <div class="ibox-title">
                                        <h5>阶段列表和角色选择</h5>
                                        <div class="ibox-tools">
                                            <a href="javascript:;"
                                               onclick="$.aede.pmp.ptmt.project.addProjectStageModal();"
                                               class="btn btn-primary btn-xs" role="button">
                                                <i class="fa fa-plus"></i> 新增阶段</a>
                                        </div>
                                    </div>
                                    <div class="ibox-content">
                                        <table id="pmp_ptmt_project_stagetable" style="width: 100%;"
                                               class="table table-striped table-bordered table-hover">
                                            <thead>
                                            <tr>
                                                <th style="text-align: center;">序号</th>
                                                <th style="text-align: center;">阶段名称</th>
                                                <th style="text-align: center;">阶段预计开始日期</th>
                                                <th style="text-align: center;">阶段预计结束日期</th>
                                                <th style="text-align: center;">操作</th>
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
                </form>
            </div>
            <div class="modal-footer">
                <input id="pmp_ptmt_project_id" type="hidden"
                       class="form-control">
                <!-- 					<button type="button" id="pmp_ptmt_project_modal_btnSave" -->
                <!-- 						onclick="" class="btn btn-primary">保存</button> -->
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
            </div>
        </div>
    </div>
</div>

<div id="pmp_ptmt_projectstage_modal" class="modal fade"
     aria-hidden="true" tabindex="-1" data-backdrop="static" role="dialog"
     aria-labelledby="ModalLabel" data-width="720">
    <div class="modal-dialog" style="width: 40%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="projectstageModalLabel">阶段</h4>
            </div>
            <div class="modal-body" style="height: 300px; overflow-y: auto;">
                <form method="post" action="" class="form-horizontal">
                    <input type="hidden" class="form-control"> <input
                        id="pmp_ptmt_projectstage_uuid" type="hidden"
                        class="form-control">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">阶段名称</label>
                            <div class="col-md-8">
                                <dwz:dic themeName="阶段名称" className="form-control"
                                         paramName="pmp_ptmt_projectstage_sname">
                                    <option value="">---请选择阶段名称---</option>
                                </dwz:dic>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">阶段序号</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" name="sseq"
                                       id="pmp_ptmt_projectstage_sseq">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">阶段预计开始日期</label>
                            <div class="col-md-8">
                                <input id="pmp_ptmt_projectstage_expbegintime" readOnly
                                       class="laydate-icon form-control layer-date" name="expbegintime">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">阶段预计结束日期</label>
                            <div class="col-md-8">
                                <input id="pmp_ptmt_projectstage_expendtime" readOnly
                                       class="laydate-icon form-control layer-date" name="expendtime">
                            </div>
                        </div>
                        <!-- 							<div class="form-group"> -->
                        <!-- 								<label class="col-md-4 control-label">阶段类型</label> -->
                        <!-- 								<div class="col-md-8"> -->
                        <%-- 								<dwz:dic themeName="阶段类型" className="form-control" --%>
                        <%-- 											paramName="pmp_ptmt_projectstage_type">  --%>
                        <!-- 												<option value="">---请选择阶段类型---</option> -->
                        <%--  								</dwz:dic>  --%>
                        <!-- 								</div> -->
                        <!-- 							</div> -->
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <input id="pmp_ptmt_projectstage_sid" type="hidden"
                       class="form-control">
                <button type="button" id="pmp_ptmt_projectstage_modal_btnSave"
                        onclick="" class="btn btn-primary">保存
                </button>
                <button type="button" data-dismiss="modal" class="btn">取消</button>
            </div>
        </div>
    </div>
</div>
<!-- 跟踪modal -->
<div id="pmp_ptmt_project_follow_modal" class="modal fade"
     aria-hidden="true" tabindex="-1" data-backdrop="static" role="dialog"
     aria-labelledby="ModalLabel" data-width="720">
    <div class="modal-dialog" style="width: 40%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="projectFollowModalLabel">跟踪</h4>
            </div>
            <div class="modal-body" style="height: 300px; overflow-y: auto;">
                <form method="post" action="" class="form-horizontal">
                    <input type="hidden" class="form-control">
                    <div class="form-body">
                        <input type="hidden" id="pmp_ptmt_project_follow_pid">
                        <div class="form-group">
                            <label class="col-md-4 control-label">跟踪日期<span class="required">*</span></label>
                            <div class="col-md-8">
                                <input id="pmp_ptmt_project_follow_time" readOnly
                                       class="laydate-icon myform-control layer-date" name="followTime">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">跟踪人<span class="required">*</span></label>
                            <div class="col-md-8">
                                <input id="pmp_ptmt_project_follow_username" class="form-control" name="username"
                                       readonly>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">跟踪内容<span class="required">*</span></label>
                            <div class="col-md-8">
									<textarea class="form-control" id="pmp_ptmt_project_follow_content"
                                              cols="5" maxlength="255"></textarea>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" id="pmp_ptmt_project_follow_modal_btnSave"
                        class="btn btn-primary">保存
                </button>
                <button type="button" data-dismiss="modal" class="btn">取消</button>
            </div>
        </div>
    </div>
</div>

<!-- 提出人员选择modal -->

<jsp:include page="/iframeInclude/foot.jsp"></jsp:include>
</body>
<script
        src="${pageContext.request.contextPath}/resources/js/pmp/rsmt/aede.pmp.rsmt.resourceDown.js"></script>
</html>
