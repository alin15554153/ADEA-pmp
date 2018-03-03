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
	src="${ctx}/resources/js/pmp/ptmt/rolemanager/aede.pmp.ptmt.rolemanagerList.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/plugins/bootstrap-select/bootstrap-select.js"></script>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/js/plugins/bootstrap-select/bootstrap-select.css">
</head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
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
									<input id="ptmt_project_rolemanager_query_rname" class="form-control" type="text" placeholder="项目名称"/> 
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
									<th>所有角色</th>
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
