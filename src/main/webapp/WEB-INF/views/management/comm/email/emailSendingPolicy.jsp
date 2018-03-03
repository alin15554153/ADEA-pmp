<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="dwz" uri="http://www.anycc.com/dwz"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
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
	src="${ctx}/resources/js/pmp/comm/email/aede.pmp.comm.emailSendingPolicy.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/plugins/bootstrap-select/bootstrap-select.js"></script>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/js/plugins/bootstrap-select/bootstrap-select.css">
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<input id="id" type="hidden"/>
	<div id="pmp_ptmt_project_emailSendPolicy_form" >
		<form method="post" action="" class="form-horizontal">
			<input type="hidden" class="form-control">
			<div class="form-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<label class="col-md-3 control-label"><span
								class="required">*</span>smtp服务器： </label>
							<div class="col-md-3">
								<input type="text" class="form-control"
									id="pmp_ptmt_project_emailSendPolicy_smtp_server"
									name="smtp_server">
							</div>
							<label class="col-md-3 control-label"><span
								class="required">*</span>端口：</label>
							<div class="col-md-3">
								<input type="text" class="form-control"
									id="pmp_ptmt_project_emailSendPolicy_port" name="port">
							</div>

						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<label class="col-md-3 control-label"><span
								class="required">*</span>邮箱账户： </label>
							<div class="col-md-3">
								<input type="text" class="form-control"
									id="pmp_ptmt_project_emailSendPolicy_account" name="account">
							</div>
							<label class="col-md-3 control-label"><span
								class="required">*</span>邮箱密码：</label>
							<div class="col-md-3">
								<input type="password" class="form-control"
									id="pmp_ptmt_project_emailSendPolicy_password" name="password">
							</div>

						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<label class="col-md-3 control-label">项目信息变更是否发送邮件： </label>
							<div class="col-md-3">
								<input type="checkbox" class="form-control" style="width:20px;height:20px;"
									id="pmp_ptmt_project_emailSendPolicy_is_pjchange_send"
									name="is_pjchange_send" />
							</div>
							<label class="col-md-3 control-label">项目成员变更是否发送邮件：</label>
							<div class="col-md-3">
								<input type="checkbox" class="form-control" style="width:20px;height:20px;"
									id="pmp_ptmt_project_emailSendPolicy_is_pjmemchange_send"
									name="is_pjmemchange_send">
							</div>

						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<label class="col-md-3 control-label">资源申请是否发送邮件： </label>
							<div class="col-md-3">
								<input type="checkbox" class="form-control" style="width:20px;height:20px;"
									id="pmp_ptmt_project_emailSendPolicy_is_rsapply_send"
									name="is_rsapply_send">
							</div>
							<label class="col-md-3 control-label">阶段变更是否发送邮件：</label>
							<div class="col-md-3">
								<input type="checkbox" class="form-control" style="width:20px;height:20px;"
									id="pmp_ptmt_project_emailSendPolicy_is_pjstage_send"
									name="is_pjstage_send">
							</div>

						</div>
					</div>
				</div>
			</div>
		</form>
		<div style="text-align:center">
			<button type="button" id="pmp_ptmt_project_emailSendPolicy_btnSave" style="width:100px;"
				class="btn btn-primary">保存</button>
		</div>
	</div>
	</div>
	<jsp:include page="/iframeInclude/foot.jsp"></jsp:include>
</body>
</html>