<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="dwz" uri="http://www.anycc.com/dwz"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>项目查询</title>
<jsp:include page="/iframeInclude/pmp/dataTableHead.jsp"></jsp:include>
<!-- easyUI  控件 -->	
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/jquery.easyui.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/pmp/ptmt/aede.pmp.ptmt.proquery.js"></script>
</head>

<body class="gray-bg">
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
										<input type="text" class="form-control"
											id="pmp_ptmt_proquery_query_code" placeholder="项目编号">
									</div>
								</div>
							</div>
							<div class="col-md-3 col-sm-3">
								<div class="form-group">
									<div class="input-icon">
										<input type="text" class="form-control"
											id="pmp_ptmt_proquery_query_name" placeholder="项目名称">
									</div>
								</div>
							</div>
							<div class="col-md-3 col-sm-3">
								<dwz:dic themeName="项目类别" className="form-control"
										 paramName="pmp_ptmt_proquery_query_type" >
									<option value="">---请选择项目类别---</option>
								</dwz:dic>

							</div>
							<div class="col-md-3 col-sm-3">
								<dwz:dic themeName="项目状态" className="form-control"
										 paramName="pmp_ptmt_proquery_query_status" >
									<option value="">---请选择项目状态---</option>
								</dwz:dic>

							</div>
						</div>
						<div class="row">
							<div class="col-md-12 col-sm-12">
								<div class="" align="center">
									<button type="button" id="pmp_ptmt_proquery_btnReset"
										class="btn default">
										<i class="fa fa-history"></i>重置
									</button>
									<button type="submit" id="pmp_ptmt_proquery_btnSearch"
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
						<h5>项目列表</h5>
					</div>
					<div class="ibox-content">
						<table id="pmp_ptmt_proquery_table"
							class="table table-striped table-bordered table-hover">
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

		<div id="pmp_ptmt_proquery_modal" class="modal fade" aria-hidden="true"
		tabindex="-1" data-backdrop="static" role="dialog"
		aria-labelledby="ModalLabel" data-width="720">
		<div class="modal-dialog" style="display: inline-block; width: 100%;height: auto;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="userModalLabel">项目</h4>
				</div>
				<div class="modal-body" style=" overflow-y: auto;">
					<form method="post" action="" class="form-horizontal" >
						<input type="hidden" class="form-control">
						<div class="form-body">
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">
										    项目编号<span class="required" >*</span>
										</label>
										<div class="col-md-4">
											<input type="text" class="form-control"
												id="pmp_ptmt_proquery_code" name="code">
										</div>
										<label class="col-md-2 control-label">项目名称<span class="required" >*</span></label>
										<div class="col-md-4">
											<input type="text" class="form-control"
												id="pmp_ptmt_proquery_name" name="name">
										</div>

									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">项目类别<span class="required" >*</span></label>
										<div class="col-md-4">
											<dwz:dic themeName="项目类别" className="form-control"
 											paramName="pmp_ptmt_proquery_type" > 
											</dwz:dic>
											
										</div>
										<label class="col-md-2 control-label">项目性质<span class="required" >*</span></label>
										<div class="col-md-4">
											<dwz:dic themeName="项目性质" className="form-control"
 											paramName="pmp_ptmt_proquery_nature"> 
 											</dwz:dic>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">建立日期<span class="required" >*</span></label>
										<div class="col-md-4">
											<input id="pmp_ptmt_proquerystage_ctime" readOnly
												class="laydate-icon form-control layer-date" name="ctime">
										</div>
										<label class="col-md-2 control-label">项目经理<span class="required" >*</span></label>
										<div class="col-md-4">
											<input type="text" class="form-control"
												id="pmp_ptmt_proquery_manager" name="manager">
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">项目负责人</label>
										<div class="col-md-4">
											<input type="text" class="form-control"
												id="pmp_ptmt_proquery_director">
										</div>
										<label class="col-md-2 control-label">复核人</label>
										<div class="col-md-4">
											<input type="text" class="form-control"
												id="pmp_ptmt_proquery_checker">
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">项目等级<span class="required" >*</span></label>
										<div class="col-md-4">
											<dwz:dic themeName="项目等级" className="form-control"
											paramName="pmp_ptmt_proquery_level">
 											</dwz:dic> 
										</div>
										<label class="col-md-2 control-label">项目状态<span class="required" >*</span></label>
										<div class="col-md-4">
											<dwz:dic themeName="项目状态" className="form-control"
 											paramName="pmp_ptmt_proquery_status"> 
 											</dwz:dic> 
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">项目进度</label>
										<div class="col-md-4">
											<input type="text" class="form-control"
												id="pmp_ptmt_proquery_progress">
										</div>
										<label class="col-md-2 control-label">项目金额<span class="required" >*</span></label>
										<div class="col-md-4">
											<input type="text" class="form-control"
												id="pmp_ptmt_proquery_amt">
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="control-label col-md-2">参与人员</label>
										<div class="col-md-10">
										<input type="hidden" class="form-control" name="proposeid"
											id="pmp_ptmt_proquery_perid">
										<input type="text" class="form-control" name="proposeMan"
											id="pmp_ptmt_proquery_pername">	
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">备注</label>
										<div class="col-md-10">
											<textarea class="form-control" id="pmp_ptmt_proquery_remark"
												cols="5" maxlength="255"></textarea>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row" id="pmp_ptmt_proquery_resourcetablediv">
								<div class="col-sm-12">
								<input id="pmp_ptmt_proquery_resource_query_pid" type="hidden"
								class="form-control">
								<input id="pmp_ptmt_proquery_resource_query_stage" type="hidden"
								class="form-control">
									<div id="tt" class="easyui-tabs" style="width:965px;height:30px;">
									</div>
									<div class="ibox float-e-margins">
										
										<div class="ibox-title">
											<h5>项目资源</h5>
										</div>
										<div class="ibox-content">
											<table id="pmp_ptmt_proquery_resourcetable"
												class="table table-striped table-bordered table-hover">
												<thead>
													<tr>
														<th style="text-align: center;">资源名称</th>
														<th style="text-align: center;">资源类别</th>
														<th style="text-align: center;">上传人</th>
														<th style="text-align: center;">上传时间</th>
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
				<input id="pmp_ptmt_proquery_id" type="hidden"
						class="form-control">
<!-- 					<button type="button" id="pmp_ptmt_proquery_modal_btnSave" -->
<!-- 						onclick="" class="btn btn-primary">保存</button> -->
					<button type="button" data-dismiss="modal" class="btn">取消</button>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="/iframeInclude/foot.jsp"></jsp:include>
</body>
</html>
