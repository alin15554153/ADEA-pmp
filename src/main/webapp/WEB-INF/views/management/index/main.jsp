<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>主页</title>
<jsp:include page="/iframeInclude/pmp/dataTableHead.jsp"></jsp:include>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/plugins/highcharts/highcharts.js"></script>
<!-- 自定义js  -->
<script
	src="${pageContext.request.contextPath}/resources/js/pmp/comm/aede.pmp.comm.mail.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/pmp/comm/aede.pmp.main.js"></script>
</head>
<style type="text/css">
.myleft{text-align:left;}
</style>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<div class="ibox-title-font">
                    		待办事项
                    	</div>
					</div>
					<div class="ibox-content">
						<table id="adminmsgTable"
							style="border-top: 2px solid #E6E6E6; text-align: center; width: 100%;"
							class="table1 table-striped1 table-hover1 table-bordered1">
							<thead>
								<tr style="line-height: 40px">
									<th style="text-align: left; font-family: 微软雅黑; color: #465277">&nbsp;&nbsp;资源名称</th>
									<th
										style="text-align: center; font-family: 微软雅黑; color: #465277">资源类别</th>
									<th
										style="text-align: center; font-family: 微软雅黑; color: #465277">项目名称</th>
									<th
										style="text-align: center; font-family: 微软雅黑; color: #465277">申请人</th>
									<th
										style="text-align: center; font-family: 微软雅黑; color: #465277">申请时间</th>
									<th
										style="text-align: center; font-family: 微软雅黑; color: #465277">申请原因</th>
									<th
										style="text-align: center; font-family: 微软雅黑; color: #465277">操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<div class="ibox-title-font">
							项目信息统计
						</div>
					</div>
					<div class="ibox-content">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<div id="main_pie_container" style="min-width:520px;height:300px"></div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<div id="main_yw_pie_container" style="min-width:520px;height:300px"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<div class="ibox-title-font">
							项目信息统计
						</div>
					</div>
					<div class="ibox-content">
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div id="main_colum_container" style="min-width:800px;height:400px"></div>
								</div>
							</div>
						</div>
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
<!-- 	<script>
		$(document).ready(
				function() {
					WinMove();
					/* setTimeout(function () {
					    $.gritter.add({
					        title: '您有2条未读信息',
					        text: '请前往<a href="mailbox.html" class="text-warning">收件箱</a>查看今日任务',
					        time: 10000
					    });
					}, 2000); */

					$('.chart').easyPieChart({
						barColor : '#f8ac59',
						//                scaleColor: false,
						scaleLength : 5,
						lineWidth : 4,
						size : 80
					});

					$('.chart2').easyPieChart({
						barColor : '#1c84c6',
						//                scaleColor: false,
						scaleLength : 5,
						lineWidth : 4,
						size : 80
					});

					var data1 = [ [ 0, 4 ], [ 1, 8 ], [ 2, 5 ], [ 3, 10 ],
							[ 4, 4 ], [ 5, 16 ], [ 6, 5 ], [ 7, 11 ], [ 8, 6 ],
							[ 9, 11 ], [ 10, 30 ], [ 11, 10 ], [ 12, 13 ],
							[ 13, 4 ], [ 14, 3 ], [ 15, 3 ], [ 16, 6 ] ];
					var data2 = [ [ 0, 1 ], [ 1, 0 ], [ 2, 2 ], [ 3, 0 ],
							[ 4, 1 ], [ 5, 3 ], [ 6, 1 ], [ 7, 5 ], [ 8, 2 ],
							[ 9, 3 ], [ 10, 2 ], [ 11, 1 ], [ 12, 0 ],
							[ 13, 2 ], [ 14, 8 ], [ 15, 0 ], [ 16, 0 ] ];
					$("#flot-dashboard-chart").length
							&& $.plot($("#flot-dashboard-chart"), [ data1,
									data2 ], {
								series : {
									lines : {
										show : false,
										fill : true
									},
									splines : {
										show : true,
										tension : 0.4,
										lineWidth : 1,
										fill : 0.4
									},
									points : {
										radius : 0,
										show : true
									},
									shadowSize : 2
								},
								grid : {
									hoverable : true,
									clickable : true,
									tickColor : "#d5d5d5",
									borderWidth : 1,
									color : '#d5d5d5'
								},
								colors : [ "#1ab394", "#464f88" ],
								xaxis : {},
								yaxis : {
									ticks : 4
								},
								tooltip : false
							});
				});
	</script> -->
</body>

</html>
