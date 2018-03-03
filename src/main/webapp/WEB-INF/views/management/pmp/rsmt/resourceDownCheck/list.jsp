<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="dwz" uri="http://www.anycc.com/dwz"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>审核管理</title>
    <jsp:include page="/iframeInclude/pmp/dataTableHead.jsp"></jsp:include>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="row" hidden="hidden">
			<div class="col-md-12">
				<input type="text" id="pmp_rsmt_resourceDownCheck_resourceDownId" class="text" value="${resourceDownId}">
			</div>
		</div>
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
										<input type="text" class="form-control" id="pmp_rsmt_resourceDownCheck_resourceName" placeholder="资源名称">
									</div>
								</div>
							</div>
							<div class="col-md-3 col-sm-3">
								<div class="form-group">
									<div class="input-icon">
										<dwz:dic themeName="资源类别" className="form-control" paramName="pmp_rsmt_resourceDownCheck_resourceType">
											<option value="">---请选择资源类别---</option>
										</dwz:dic>
										<%--<input type="text" class="form-control" id="pmp_rsmt_resourceDownCheck_resourceType" placeholder="资源类别">--%>
									</div>
								</div>
							</div>
							<div class="col-md-3 col-sm-3">
								<div class="form-group">
									<div class="input-icon">
										<dwz:dic themeName="阶段名称" className="form-control" paramName="pmp_rsmt_resourceDownCheck_stage">
											<option value="">---请选择阶段名称---</option>
										</dwz:dic>
										<%--<select class="form-control" id="pmp_rsmt_resourceDownCheck_stage"></select>--%>
										<%--<input type="text" class="form-control" id="pmp_rsmt_resourceDownCheck_stage" placeholder="阶段">--%>
									</div>
								</div>
							</div>
							<div class="col-md-3 col-sm-3">
								<div class="form-group">
									<div class="input-icon">
										<input type="text" class="form-control" id="pmp_rsmt_resourceDownCheck_projectName" placeholder="项目名称">
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 col-sm-12">
								<div class="" align="center">
									<button type="button" id="pmp_rsmt_resourceDownCheck_btnReset" class="btn default">
										<i class="fa fa-history"></i>重置
									</button>
									<button type="submit" id="pmp_rsmt_resourceDownCheck_btnSearch" class="btn btn-primary">
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
					<h5>申请列表</h5>
                    <div class="ibox-content">
                        <table id="pmp_rsmt_resourceDownCheck_table" class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th>资源名称</th>
									<th>资源类别</th>
									<th>项目名称</th>
									<th>申请人</th>
									<th>申请时间</th>
									<th>申请原因</th>
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

	<div id="pmp_rsmt_resourceDownCheck_modal" class="modal fade" aria-hidden="true" tabindex="-1" data-backdrop="static" role="dialog" aria-labelledby="ModalLabel" data-width="480">
		<div class="modal-dialog" >
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title">审核</h4>
				</div>
				<div class="modal-body" style="height: 300px; overflow-y: auto;">
				<form method="post" action="" class="form-horizontal">
					<input type="hidden" class="form-control">
					<div class="form-body">
						<div class="form-group">
							<label class="col-md-2 control-label">审核状态<%--<span class="required">*</span>--%></label>
							<div class="col-md-10">
								<select class="form-control" id="pmp_rsmt_resourceDownCheck_checkStatus">
									<option value="1">通过</option>
									<option value="2">不通过</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">审核原因<%--<span class="required">*</span>--%></label>
							<div class="col-md-10">
								<textarea class="form-control" id="pmp_rsmt_resourceDownCheck_checkRemark" cols="5" maxlength="255"></textarea>
							</div>
						</div>
					</div>
				</form>
				</div>
				<div class="modal-footer">
				    <button type="button" id="pmp_rsmt_resourceDownCheck_btnSave" class="btn btn-primary">保存</button>
					<button type="button" data-dismiss="modal" class="btn">取消</button>
				</div>
			</div>
		</div>
	</div>

	<div id="pmp_rsmt_resourceDownCheck_preRemarkModal" class="modal fade" aria-hidden="true" tabindex="-1" data-backdrop="static" role="dialog" aria-labelledby="ModalLabel" data-width="480">
		<div class="modal-dialog" >
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title">过往审核意见</h4>
				</div>
				<div class="modal-body" style="height: 300px; overflow-y: auto;">
					<table class="table table-bordered" id="pmp_rsmt_resourceDownCheck_preRemarkTable">
						<thead>
							<tr>
								<th>审核人</th>
								<th>审核内容</th>
								<th>审核时间</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" data-dismiss="modal" class="btn">关闭</button>
				</div>
			</div>
		</div>
	</div>
    <jsp:include page="/iframeInclude/foot.jsp"></jsp:include>
</body>
<script src="${pageContext.request.contextPath}/resources/js/pmp/rsmt/aede.pmp.rsmt.resourceDownCheck.js"></script>
</html>
