
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="dwz" uri="http://www.anycc.com/dwz"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>项目统计</title>
<jsp:include page="/iframeInclude/pmp/dataTableHead.jsp"></jsp:include>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/plugins/jquery.combotree.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/js/plugins/jquery-easyui-1.4.3/themes/bootstrap/easyui.css">

<script src="${ctx}/resources/js/pmp/slas/aede.pmp.slas.proStatistics.js"
	type="text/javascript"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/plugins/highcharts/highcharts.js"></script>

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
						<form id="slas_proStatistics_list_query_form" role="form">
							<div class="form-body">
								<div class="row">
									<div class="col-md-3 col-sm-3">
										<div class="form-group">
											<div class="input-icon">
												<input id="slas_proStatistics_list_query_starttime" readOnly
												class="laydate-icon myform-control layer-date" placeholder="项目建立日期起" name="starttime">
											</div>
										</div>
									</div>
									<div class="col-md-3 col-sm-3">
										<div class="form-group">
											<div class="input-icon">
												<input id="slas_proStatistics_list_query_endtime" readOnly
												class="laydate-icon myform-control layer-date" placeholder="项目建立日期止" name="endtime">
											</div>
										</div>
									</div>
									<div class="col-md-3 col-sm-3">
										<div class="form-group">
											<div class="input-icon">
<!-- 												<select id="slas_proStatistics_list_query_company" -->
<!-- 													class="form-control"></select> -->
												<select id="slas_proStatistics_list_query_company"  class="easyui-combotree col-md-8 col-xs-8 col-sm-8"
 														multiple style="height: 30px"></select>
											</div>
										</div>
									</div>
									<div class="col-md-3 col-sm-3">
										<div class="form-group">
											<div class="input-icon">
												<dwz:dic themeName="项目类别" className="form-control"
													paramName="slas_proStatistics_list_query_type">
													<option value="">---请选择项目类别---</option>
												</dwz:dic>
											</div>
										</div>
									</div>
								</div>	
								<div class="row">	
									<div class="col-md-3 col-sm-3">
										<div class="form-group">
											<div class="input-icon">
												<dwz:dic themeName="阶段名称" className="form-control"
													paramName="slas_proStatistics_list_query_stage">
													<option value="">---请选择阶段名称---</option>
												</dwz:dic>
											</div>
										</div>
									</div>

								</div>
							</div>
							<div class="form-actions">
								<div class="row">
									<div class="col-md-12 col-sm-12">
										<div class="" align="center">
											<button type="button"
												id="slas_proStatistics_list_query_btnReset"
												class="btn default">
												<i class="fa fa-history"></i>重置
											</button>
											<button type="submit"
												id="slas_proStatistics_list_query_btnSearch"
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
        <form id="slas_proStatistics_forexport_form" action="${pageContext.request.contextPath}/management/pmp/slas/proStatistics/tableExport/" method="get">
            <input id="slas_proStatistics_forexport_starttime" name="exportstarttime">
            <input id="slas_proStatistics_forexport_endtime" name="exportendtime">
            <input id="slas_proStatistics_forexport_orgIds" name="exportOrgIds">
            <input id="slas_proStatistics_forexport_stage" name="exportStage">
            <input id="slas_proStatistics_forexport_type" name="exportType">
        </form>
    </div>
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>数据列表</h5>
						 <div class="ibox-tools">
							<a data-toggle="dropdown" href="javascript:;"
								class="btn btn-deepBlue" aria-expanded="false"> <i
								class="fa fa-cogs"></i> 更多操作 <i class="fa fa-angle-down"></i>
							</a>
							<ul class="dropdown-menu">
								<li><a id="slas_proStatistics_forexport_button" > <i
										class="fa fa-send-o"></i>导出
								</a></li>
<!-- 									<a type="button" id="slas_proStatistics_forexport_button" class="btn btn-primary"> -->
<!--                                         <i class="fa fa-search"></i> 导出 -->
<!--                                     </button>  -->
							</ul>
						</div> 
					</div>
					<div class="ibox-content">
						<table id="slas_proStatistics_list_table"
							class="table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th>所属地区</th>
									<th>所属公司</th>
									<th>项目类别</th>
									<th>项目阶段</th>
									<th>项目数量</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<div id="pmp_demo_colum_container"
						style="min-width: 800px; height: 400px"></div>
				</div>
			</div>
		</div>
	</div>


	<jsp:include page="/iframeInclude/foot.jsp"></jsp:include>

</body>
</html>

