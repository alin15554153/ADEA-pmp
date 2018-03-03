
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="dwz" uri="http://www.anycc.com/dwz"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>项目一览</title>
<jsp:include page="/iframeInclude/pmp/dataTableHead.jsp"></jsp:include>

<script type="text/javascript" src="${ctx}/resources/js/plugins/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/plugins/jquery.combotree.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/themes/bootstrap/easyui.css">

<script src="${ctx}/resources/js/pmp/slas/aede.pmp.slas.proBrowser.js"
		type="text/javascript"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/pmp/ptmt/aede.pmp.ptmt.proquery.js"></script>
<%--<script
		src="${pageContext.request.contextPath}/resources/js/pmp/ptmt/aede.pmp.ptmt.proquery.js"></script>--%>
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
	<div class="wrapper wrapper-content">
		<%--<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<div id="slas_resStatistics_list_pie_container"></div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<div id="slas_resStatistics_list_colum_container"
						 style="min-width: 550px; height: 300px"></div>
				</div>
			</div>

		</div>--%>
		<input type="hidden" value="${p}" id="slas_proBrowser_hidden_query_p" />
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
						<form id="slas_proBrowser_list_query_form" role="form">
							<div class="form-body">
								<div class="row">
									<div class="col-md-3 col-sm-3">
										<div class="form-group">
											<div class="input-icon">
												<input id="slas_proBrowser_list_query_starttime" readOnly
												class="laydate-icon myform-control layer-date" placeholder="项目建立日期起" name="starttime">
											</div>
										</div>
									</div>
									<div class="col-md-3 col-sm-3">
										<div class="form-group">
											<div class="input-icon">
												<input id="slas_proBrowser_list_query_endtime" readOnly
												class="laydate-icon myform-control layer-date" placeholder="项目建立日期止" name="endtime">
											</div>
										</div>
									</div>
									<div class="col-md-3 col-sm-3">
										<div class="form-group">
											<div class="input-icon">
												<select id="slas_proBrowser_list_query_yw"
													class="form-control" placeholder="延误状况">
													<option value="">---请选择延误状况---</option>
													<option value="1">已延误</option>
													<option value="2">未延误</option>
												</select>
											</div>
										</div>
									</div>
									<div class="col-md-3 col-sm-3">
										<div class="form-group">
											<div class="input-icon">
												<%--<select id="slas_proBrowser_list_query_company"
													class="form-control" placeholder="所属公司"></select>--%>
												<select id="slas_proBrowser_list_query_company" class="easyui-combotree col-md-8 col-xs-8 col-sm-8" multiple
														style="height: 30px"></select>
											</div>
										</div>
									</div>
									</div>
									<div class="row">
									<div class="col-md-3 col-sm-3">
										<div class="form-group">
											<div class="input-icon">
												<dwz:dic themeName="项目状态" className="form-control"
														 paramName="slas_proBrowser_list_query_status">
													<option value="">---请选择项目状态---</option>
												</dwz:dic>
											</div>
										</div>
									</div>
									<div class="col-md-3 col-sm-3">
										<div class="form-group">
											<div class="input-icon">
												<dwz:dic themeName="阶段名称" className="form-control"
													paramName="slas_proBrowser_list_query_stage">
													<option value="">---请选择阶段名称---</option>
												</dwz:dic>
											</div>
										</div>
									</div>
										<div class="col-md-3 col-sm-3">
											<dwz:dic themeName="项目类别" className="form-control"
													 paramName="slas_proBrowser_list_query_type" >
												<option value="">---请选择项目类别---</option>
											</dwz:dic>

										</div>
								</div>
							</div>
							<div class="form-actions">
								<div class="row">
									<div class="col-md-12 col-sm-12">
										<div class="" align="center">
											<button type="button"
												id="slas_proBrowser_list_query_btnReset"
												class="btn default">
												<i class="fa fa-history"></i>重置
											</button>
											<button type="button"
												id="slas_proBrowser_list_query_btnSearch"
												class="btn btn-primary">
												<i class="fa fa-search"></i> 查询
											</button>
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	<div hidden="hidden">
        <form id="slas_proBrowser_forexport_form" action="${pageContext.request.contextPath}/management/pmp/slas/proBrowser/tableExport/" method="get">
            <input id="slas_proBrowser_forexport_starttime" name="exportstarttime">
            <input id="slas_proBrowser_forexport_endtime" name="exportendtime">
            <input id="slas_proBrowser_forexport_yw" name="exportyw">
            <input id="slas_proBrowser_forexport_wyw" name="exportwyw">
            <input id="slas_proBrowser_forexport_company" name="exportcompany">
            <input id="slas_proBrowser_forexport_status" name="exportstatus">
            <input id="slas_proBrowser_forexport_stage" name="exportstage">
        </form>
    </div>
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5><i class="fa fa-list"></i>数据列表</h5>
						<div class="ibox-tools">
							<a data-toggle="dropdown" href="javascript:;"
							   class="btn btn-deepBlue" aria-expanded="false"> <i
									class="fa fa-cogs"></i> 导出操作 <i class="fa fa-angle-down"></i></a>
							<ul class="dropdown-menu">
								<li><a href="${pageContext.request.contextPath}/management/pmp/ptmt/project/export?type=1"> <i
										class="fa fa-send-o"></i>导出自建项目
								</a></li>
								<li><a href="${pageContext.request.contextPath}/management/pmp/ptmt/project/export?type=2"> <i
										class="fa fa-send-o"></i>导出外部项目
								</a></li>
						</div>
					</div>
					<div class="ibox-content">
						<table id="slas_proBrowser_list_table"
							class="table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th>所属地区</th>
									<th>所属公司</th>
									<th>项目编号</th>
									<th>项目名称</th>
									<th>项目创建人</th>
									<th>项目进度</th>
									<th>项目等级</th>
									<th>金额(万元)</th>
									<th>状态</th>
									<th>当前阶段</th>
									<th>项目进度</th>
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

