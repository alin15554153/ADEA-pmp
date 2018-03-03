<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="dwz" uri="http://www.anycc.com/dwz"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>项目管理</title>
<jsp:include page="/iframeInclude/pmp/dataTableHead.jsp"></jsp:include>
<link href="${pageContext.request.contextPath}/resources/css/plugins/sweetalert/sweetalert.css"
	rel="stylesheet" type="text/css">
	<link href="${pageContext.request.contextPath}/resources/plugins/nouislider/jquery.nouislider.min.css"
		  rel="stylesheet" type="text/css">
	<link href="${pageContext.request.contextPath}/resources/plugins/nouislider/jquery.nouislider.pips.min.css"
		  rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/resources/css/components.css"
	rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/resources/js/plugins/sweetalert/sweetalert.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/plugins/validate/jquery.validate.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/plugins/validate/messages_zh.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/plugins/nouislider/jquery.nouislider.all.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/components-nouisliders.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/pmp/ptmt/aede.pmp.ptmt.projectadd.js"></script>

<!-- easyUI  控件 -->	
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/jquery.easyui.min.js"></script>


<script
	src="${pageContext.request.contextPath}/resources/js/Math.uuid.js"
	type="text/javascript"></script>
<!--webuploader-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/plugins/webuploader/webuploader.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/plugins/webuploader/webuploader.js"></script> 
    <script src="${pageContext.request.contextPath}/resources/js/pmp/rsmt/aede.pmp.rsmt.resourceUploader.js" type="text/javascript"></script>
<style type="text/css">
.myform-control {
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
    color: #555;
    display: block;
    font-size: 14px;
    height: 34px;
    line-height: 1.42857;
    padding: 6px 12px;
    transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s ease-in-out 0s;
    width: 100%;
}
</style>
</head>


<body class="gray-bg">
<input type="hidden" id="pmp_ptmt_project_manager_id" value="">

<div id="pmp_ptmt_project_modal" >
				<div class="wrapper wrapper-content">
					<form method="post" action="" class="form-horizontal" >
						<input type="hidden" class="form-control">
						<div class="form-body">
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">
										    项目编号</span>
										</label>
										<div class="col-md-4">
											<input type="text" class="form-control"
												id="pmp_ptmt_project_code" name="code">
										</div>
										<label class="col-md-2 control-label">项目名称<span class="required" >*</span></label>
										<div class="col-md-4">
											<input type="text" class="form-control" maxlength='225' 
												id="pmp_ptmt_project_name" name="name">
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
											paramName="pmp_ptmt_project_type" >
												<option value="">---请选择项目类别---</option>
											</dwz:dic>
											
										</div>
										<label class="col-md-2 control-label">项目性质<span class="required" >*</span></label>
										<div class="col-md-4">
											<dwz:dic themeName="项目性质" className="form-control"
											paramName="pmp_ptmt_project_nature">
												<option value="">---请选择项目性质---</option>
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
											<input id="pmp_ptmt_project_ctime" readOnly
												class="laydate-icon myform-control layer-date" name="ctime">
										</div>
										<label class="col-md-2 control-label">项目经理<span class="required" >*</span></label>
										<div class="col-md-4">
											<input type="text" class="form-control" 
												id="pmp_ptmt_project_manager" name="manager">
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">项目负责人</label>
										<div class="col-md-4">
											<input type="text" class="form-control" maxlength='11' 
												id="pmp_ptmt_project_director" name="director">
										</div>
										<label class="col-md-2 control-label">复核人</label>
										<div class="col-md-4">
											<input type="text" class="form-control" maxlength='11' 
												id="pmp_ptmt_project_checker" name="checker">
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
													 paramName="pmp_ptmt_project_level">
												<option value="">---请选择项目等级---</option>
											</dwz:dic>
										</div>
										<label class="col-md-2 control-label">项目状态<span class="required" >*</span></label>
										<div class="col-md-4">
											<dwz:dic themeName="项目状态" className="form-control"
											paramName="pmp_ptmt_project_status">
												<option value="">---请选择项目状态---</option>
											</dwz:dic>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">项目进度（%）</label>
                                        <div class="col-sm-12 col-md-4">
                                            <div class="noUi-control noUi-success" id="pmp_ptmt_project_progress">
                                            </div>
											<span class="example-val" id="pmp_ptmt_project_progress_span"></span>
										</div>
										<%--<div class="col-sm-12 col-md-4">--%>
											<%--<input type="text" class="form-control" name="progress"--%>
												<%--id="pmp_ptmt_project_progress">--%>
										<%--</div>--%>
										<label class="col-md-2 control-label">项目金额（万元）<span class="required" >*</span></label>
										<div class="col-md-4">
											<input type="text" class="form-control" name="amt" 
												id="pmp_ptmt_project_amt">
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">项目来源<span class="required" >*</span></label>
										<div class="col-sm-12 col-md-4">
											<input type="text" class="form-control" name="source"
												   id="pmp_ptmt_project_source">
										</div>
										<label class="col-md-2 control-label">干系人<span class="required" >*</span></label>
										<div class="col-md-4">
											<input type="text" class="form-control" name="stakeholder"
												   id="pmp_ptmt_project_stakeholder">
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">项目总包<span class="required" >*</span></label>
										<div class="col-md-4">
											<dwz:dic themeName="项目总包" className="form-control"
													 paramName="pmp_ptmt_project_totalPackage" >
												<option value="">---请选择项目总包---</option>
											</dwz:dic>
										</div>
										<label class="col-md-2 control-label">项目分包</label>
										<div class="col-md-4">
											<input type="text" class="form-control" name="stakeholder"
												   id="pmp_ptmt_project_subPackage">
										</div>
									</div>
								</div>
							</div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label class="col-md-2 control-label">协助单位</label>
                                        <div class="col-md-4">
                                            <input type="text" class="form-control"
                                                   id="pmp_ptmt_project_assistanceUnit">
                                        </div>
                                        <label class="col-md-2 control-label">协助内容</label>
                                        <div class="col-md-4">
                                            <input type="text" class="form-control"
                                                   id="pmp_ptmt_project_assistanceContent">
                                        </div>
                                    </div>
                                </div>
                            </div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">预计签约日期<span class="required" >*</span></label>
										<div class="col-md-4">
											<input id="pmp_ptmt_project_expectedTime" readOnly
												   class="laydate-icon myform-control layer-date" name="expectedTime">
										</div>
										<label class="col-md-2 control-label">签约概率（%）</label>
										<div class="col-sm-12 col-md-4">
											<input type="text" class="form-control" name="signingProbability" readonly
									   			id="pmp_ptmt_project_signingProbability" value="0">
										</div>
									</div>
								</div>
							</div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label class="col-md-2 control-label">签约预算（万元）</label>
                                        <div class="col-md-4">
                                            <input type="text" class="form-control" name="signingBudget"
                                                   id="pmp_ptmt_project_signingBudget ">
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
											id="pmp_ptmt_project_perid">
										<input type="text" class="form-control" name="proposeMan"
											id="pmp_ptmt_project_pername">	
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-md-2 control-label">备注</label>
										<div class="col-md-10">
											<textarea class="form-control" id="pmp_ptmt_project_remark"
												cols="5" maxlength="255"></textarea>
										</div>
									</div>
								</div>
							</div>
							
						</div>
					</form>
<!-- 				</div> -->
				<div class="modal-footer">
				<input id="pmp_ptmt_project_id" type="hidden"
						class="form-control">
					<button type="button" id="pmp_ptmt_project_modal_btnSave"
						 class="btn btn-primary">保存</button>
					<button type="button" onclick="$.aede.comm.closeTab()" class="btn">取消</button>
				</div>
<!-- 			</div> -->
		</div>
	</div>

	
<!-- 参与人员选择modal -->
<div class="modal fade" id="jail-detain-addPrsArrgInfo-proposeManModal" data-backdrop="static" >
	<div class="modal-dialog" style="width: 800px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title">人员选择</h4>
			</div>
			<div class="modal-body">
				<!-- dataTable表格  -->
				<div class="row">
					<div class="col-md-5">
						<input type="text"
							id="jail-detain-addPrsArrgInfo-proposeMan-policeName"
							class="form-control" placeholder="姓名">
					</div>
					<div class="col-md-1">
						<div class="md-checkbox-inline">
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-actions">
							<button type="button"
								id="jail-detain-addPrsArrgInfo-proposeMan-btnReset"
								class="btn default">
								<i class="fa fa-history"></i> 重置
							</button>
							<button type="submit"
								id="jail-detain-addPrsArrgInfo-proposeMan-btnSearch"
								class="btn green">
								<i class="fa fa-search"></i> 查询
							</button>
							<button type="button"
								id="jail-detain-addPrsArrgInfo-proposeMan-btnSure"
								class="btn green" data-dismiss="modal">
								<i class="fa fa-check"></i> 确认
							</button>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-8">
						<div class="widget box">
							<div class="widget-content">
								<table style="width: 100%;" 
									class="table table-striped table-bordered table-hover table-checkable nowrap"
									id="jail-detain-addPrsArrgInfo-proposeManTable">
									<thead>
										<tr>
											<th>姓名</th>
											<th>所属公司</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>

									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="col-md-4">
						<div class="widget box">
							<div class="widget-content">
								<div class="panel panel-default" style="margin-top: 5px"
									id="jail-detain-addPrsArrgInfo-proposeMan-namePanel">
									<div class="panel-heading">已选择人员</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- dataTable表格  -->
			</div>
		</div>
	</div>
</div>
<!-- 负责人、复核人选择modal -->
<div class="modal fade" id="pmp-ptmt-addProject-selectSingleMan-modal" data-backdrop="static" >
	<div class="modal-dialog" style="width: 800px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
				<h4 class="modal-title">人员选择</h4>
			</div>
			<div class="modal-body">
				<!-- dataTable表格  -->
				<div class="row">
					<div class="col-md-5">
						<input type="text"
							   id="pmp-ptmt-addProject-selectSingleMan-name"
							   class="form-control" placeholder="姓名">
					</div>
					<div class="col-md-3">
						<div class="md-checkbox-inline">
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-actions">
							<button type="button"
									id="pmp-ptmt-addProject-selectSingleMan-btnReset"
									class="btn default">
								<i class="fa fa-history"></i> 重置
							</button>
							<button type="submit"
									id="pmp-ptmt-addProject-selectSingleMan-btnSearch"
									class="btn green">
								<i class="fa fa-search"></i> 查询
							</button>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<div class="widget box">
							<div class="widget-content">
								<table style="width: 100%;"
									   class="table table-striped table-bordered table-hover table-checkable nowrap"
									   id="pmp-ptmt-addProject-selectSingleMan-dataTable">
									<thead>
									<tr>
										<th>姓名</th>
										<th>所属公司</th>
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
				<!-- dataTable表格  -->
			</div>
		</div>
	</div>
</div>
	<jsp:include page="/iframeInclude/foot.jsp"></jsp:include>
</body>
<script
	src="${pageContext.request.contextPath}/resources/js/pmp/rsmt/aede.pmp.rsmt.resourceDown.js"></script>
</html>
