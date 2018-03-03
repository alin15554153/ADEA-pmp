<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="dwz" uri="http://www.anycc.com/dwz" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>项目角色管理</title>
    <jsp:include page="/iframeInclude/pmp/dataTableHead.jsp"></jsp:include>
    <link href="${ctx}/resources/css/plugins/sweetalert/sweetalert.css"
          rel="stylesheet" type="text/css">
    <script src="${ctx}/resources/js/plugins/sweetalert/sweetalert.min.js"></script>
    <script
            src="${ctx}/resources/js/plugins/validate/jquery.validate.min.js"></script>
    <script src="${ctx}/resources/js/plugins/validate/messages_zh.min.js"></script>
    <script
            src="${ctx}/resources/js/pmp/ptmt/rolemanager/aede.pmp.ptmt.rolemanager.js"></script>
    <script type="text/javascript"
            src="${ctx}/resources/js/plugins/bootstrap-select/bootstrap-select.js"></script>
    <link rel="stylesheet" type="text/css"
          href="${ctx}/resources/js/plugins/bootstrap-select/bootstrap-select.css">
</head>

<body class="gray-bg">
<input type="hidden" id="pid" value="${pid}"/>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5><i class="fa fa-search"></i>查询条件</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link"> <i class="fa fa-chevron-up"></i>
                        </a>
                    </div>
                </div>
                <div class="ibox-content">
                    <div class="row">
                        <div class="col-md-3 col-sm-3">
                            <div class="form-group">
                                <div class="input-icon">
                                    <dwz:dic themeName="项目角色" className="form-control"
                                             paramName="ptmt_project_rolemanager_query_rname">
                                        <option value="">---请选择角色名称---</option>
                                    </dwz:dic>
                                    <!-- 			<input type="text" class="form-control"
                                        id="ptmt_project_rolemanager_query_rname" placeholder="角色名称"> -->
                                </div>
                            </div>
                        </div>
                        <div class="col-md-5 col-sm-5">
                            <div class="" align="center">
                                <button type="button" id="ptmt_project_rolemanager-btnReset"
                                        class="btn default">
                                    <i class="fa fa-history"></i>重置
                                </button>
                                <button type="submit" id="ptmt_project_rolemanager_btnSearch"
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
                    <h5>项目角色管理列表</h5>
                    <div class="ibox-tools">
                        <a href="javascript:;" onclick="$.aede.pmp.ptmt.project.rolemanager.showModal();"
                           class="btn btn-primary btn-xs" role="button">
                            <i class="fa fa-plus"></i> 新增</a>
                        <ul class="dropdown-menu">
                            <li><a href="table_data_tables.html#"> <i
                                    class="fa fa-send-o"></i>导出
                            </a></li>
                            <li><a href="table_data_tables.html#"> <i
                                    class="fa fa-print"></i>打印
                            </a></li>
                        </ul>
                    </div>
                </div>
                <div class="ibox-content">
                    <table id="ptmt_project_rolemanager_table"
                           class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>项目名称</th>
                            <th>角色名称</th>
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
<!-- 	<div class="" align="center" style="margin-top:30px;">
		<button type="button" id="save"
			class="btn default">
			<i class="fa fa-history"></i>确认
		</button>
	</div> -->
<div id="ptmt_project_rolemanager_modal" class="modal fade"
     aria-hidden="true" tabindex="-1" data-backdrop="static" role="dialog"
     aria-labelledby="ModalLabel" data-width="480">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="userModalLabel">角色</h4>
            </div>
            <div class="modal-body" style="height: 260px; overflow-y: auto;">
                <!-- BEGIN FORM-->
                <form method="post" action="" class="form-horizontal">
                    <!-- 隐藏id -->
                    <input id="ptmt_project_rolemanager_hide_rid" name="iid"
                           type="hidden">
                    <!-- 隐藏sid -->
                    <input id="ptmt_project_rolemanager_hide_stage" name="ssid"
                           type="hidden">
                    <div class="form-body">
                        <div class="form-group row">
                            <label class="col-md-2 control-label"> 项目 <span
                                    class="required">*</span>
                            </label>
                            <div class="col-md-10">
                                <!-- 隐藏pid -->
                                <input type="hidden" id="ptmt_project_rolemanager_hid_project"/>
                                <select class="form-control"
                                        name="ptmt_project_rolemanager_project"
                                        id="ptmt_project_rolemanager_project">
                                    <!-- onchange="$.aede.pmp.ptmt.project.rolemanager.cascading();" -->
                                    <!-- <option value="">----请选择项目----</option> -->
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label"> 阶段 <span
                                    class="required">*</span>
                            </label>
                            <div class="col-md-10">
                                <!-- <div class="input-icon right" id="exp"> -->
                                <select id="ptmt_project_rolemanager_stage"
                                        name="ptmt_project_rolemanager_stage" data-size="5"
                                        class="selectpicker bla bla bli form-control required"
                                        multiple data-live-search="true">
                                    <!-- data-width="auto"  -->
                                    <!-- 有这个属性在 form-control无法控制select宽度 -->
                                </select>
                                <!-- </div> -->
                                <!-- 	<select name="stage" class="form-control"
                                    id="ptmt_project_rolemanager_stage">
                                    <option value="">----请选择阶段----</option>
                                </select> -->
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label"> 角色名称 <span
                                    class="required">*</span>
                            </label>
                            <div class="col-md-10">
                                <dwz:dic themeName="项目角色" className="form-control"
                                         paramName="ptmt_project_rolemanager_rname">
                                    <option value="">---请选择项目角色---</option>
                                </dwz:dic>
                                <!-- <input name="rname" type="text" class="form-control"
                                    id="ptmt_project_rolemanager_rname" placeholder="角色名称"> -->
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label"> 角色说明 </label>
                            <div class="col-md-10">
									<textarea name="rdescription" class="form-control"
                                              id="ptmt_project_rolemanager_rdescription" placeholder="角色说明"></textarea>
                            </div>
                        </div>
                    </div>
                </form>
                <!-- END FORM-->
            </div>
            <div class="modal-footer">
                <button type="button" id="ptmt_project_rolemanager_btnSave"
                        class="btn btn-primary">保存
                </button>
                <button type="button" data-dismiss="modal" class="btn">取消</button>
            </div>
        </div>
    </div>
</div>
<div id="ptmt_project_linkuser_modal" class="modal fade"
     aria-hidden="true" tabindex="-1" data-backdrop="static" role="dialog"
     aria-labelledby="ModalLabel" data-width="480">
    <div class="modal-dialog modal-lg" style="width: 800px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="userModalLabel">关联项目成员</h4>
            </div>
            <div class="modal-body" style="height: 300px; overflow-y: auto;">
                <input type="hidden" id="pid"/> <input type="hidden" id="rid"/>
                <div class="row">
                    <div class="col-sm-6">
                        <table id="ptmt_project_linkuser_table"
                               class="table table-striped table-bordered table-hover"
                               width="100%">
                            <thead>
                            <tr>
                                <th>已选择目成员姓名</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div class="col-sm-6">
                        <table id="ptmt_project_prouser_table"
                               class="table table-striped table-bordered table-hover"
                               width="100%">
                            <thead>
                            <tr>
                                <th>未选择项目成员姓名</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/iframeInclude/foot.jsp"></jsp:include>
</body>

</html>
