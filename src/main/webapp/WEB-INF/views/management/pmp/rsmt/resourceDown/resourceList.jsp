<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="dwz" uri="http://www.anycc.com/dwz"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>资源申请</title>
    <jsp:include page="/iframeInclude/pmp/dataTableHead.jsp"></jsp:include>
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
                        <div class="row">
                            <div class="col-md-3 col-sm-3">
                                <div class="form-group">
                                    <div class="input-icon">
                                        <input type="text" class="form-control" id="pmp_rsmt_resourceForView_resourceName" placeholder="资源名称">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3 col-sm-3">
                                <div class="form-group">
                                    <div class="input-icon">
                                        <%--<input type="text" class="form-control" id="pmp_rsmt_resourceForView_resourceType" placeholder="资源类别">--%>
                                        <dwz:dic themeName="资源类别" className="form-control" paramName="pmp_rsmt_resourceForView_resourceType">
                                            <option value="">---请选择资源类别---</option>
                                        </dwz:dic>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3 col-sm-3">
                                <div class="form-group">
                                    <div class="input-icon">
                                        <dwz:dic themeName="阶段名称" className="form-control" paramName="pmp_rsmt_resourceForView_stage">
                                            <option value="">---请选择阶段名称---</option>
                                        </dwz:dic>
                                        <%--<select class="form-control" id="pmp_rsmt_resourceForView_stage"></select>--%>
                                        <%--<input type="text" class="form-control" id="pmp_rsmt_resourceForView_stage" placeholder="阶段">--%>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3 col-sm-3">
                                <div class="form-group">
                                    <div class="input-icon">
                                        <input type="text" class="form-control" id="pmp_rsmt_resourceForView_projectName" placeholder="项目名称">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 col-sm-12">
                                <div class="" align="center">
                                    <button type="button" id="pmp_rsmt_resourceForView_btnReset" class="btn default">
                                        <i class="fa fa-history"></i>重置
                                    </button>
                                    <button type="submit" id="pmp_rsmt_resourceForView_btnSearch" class="btn btn-primary">
                                        <i class="fa fa-search"></i> 查询
                                    </button>
                                    <!-- <button type="button" id="pmp_rsmt_resourceForView_btnExport" class="btn btn-primary">
                                        <i class="fa fa-search"></i> 导出
                                    </button> -->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div hidden="hidden">
        <form id="pmp_rsmt_resourceForView_form" action="${pageContext.request.contextPath}/management/pmp/rsmt/resourceDown/tableExport/" method="get">
            <input id="pmp_rsmt_resourceForView_resourceNameFormInput" name="resourceName">
            <input id="pmp_rsmt_resourceForView_resourceTypeFormInput" name="resourceType">
            <input id="pmp_rsmt_resourceForView_stageIdFormInput" name="stageId">
            <input id="pmp_rsmt_resourceForView_projectNameFormInput" name="projectName">
        </form>
    </div>

    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <h5>资源列表</h5>
                    <div class="ibox-content">
                        <table id="pmp_rsmt_resourceForView_table" class="table table-striped table-bordered table-hover">
                            <thead>
                            <tr>
                                <th>资源名称</th>
                                <th>资源类别</th>
                                <th>项目名称</th>
                                <th>阶段</th>
                                <th>区域名称</th>
                                <th>公司名称</th>
                                <th>上传人</th>
                                <th>上传时间</th>
                                <th>下载次数</th>
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

    <div id="pmp_rsmt_resourceView_modal" class="modal fade" aria-hidden="true" tabindex="-1" data-backdrop="static" role="dialog" aria-labelledby="ModalLabel" data-width="480">
        <div class="modal-dialog" >
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">&times;</span>
                        <span class="sr-only">Close</span>
                    </button>
                    <h4 class="modal-title">申请</h4>
                </div>
                <div class="modal-body" style="height: 300px; overflow-y: auto;">
                    <form method="post" action="" class="form-horizontal">
                        <input type="hidden" class="form-control" >
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">申请原因<%--<span class="required">*</span>--%></label>
                                <div class="col-md-10">
                                    <textarea class="form-control" id="pmp_rsmt_resourceForView_remark" cols="10" maxlength="255"></textarea>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" id="pmp_rsmt_resourceForView_btnSave" onclick="" class="btn btn-primary">提交</button>
                    <button type="button" data-dismiss="modal" class="btn">取消</button>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/iframeInclude/foot.jsp"></jsp:include>
</body>
<script src="${pageContext.request.contextPath}/resources/js/pmp/rsmt/aede.pmp.rsmt.resourceForView.js"></script>
</html>
