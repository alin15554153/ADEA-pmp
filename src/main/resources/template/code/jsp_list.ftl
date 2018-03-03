<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="zh-cn" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="zh-cn" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="zh-cn" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<!-- 公共的meta、css和js--> 
<jsp:include page="${urlPre}include/head.jsp"></jsp:include>
<!-- 当前页特有的title、css、script-->
<link rel="stylesheet" type="text/css" href="${r"${pageContext.request.contextPath}"}/resources/plugins/smoothConfirm/smoothConfirm.css" />
<link rel="stylesheet" type="text/css" href="${r"${pageContext.request.contextPath}"}/resources/plugins/bootstrap-switch/css/bootstrap3/bootstrap-switch.min.css" />
<!-- Confirm确认提示框插件 -->
<script type="text/javascript" src="${r"${pageContext.request.contextPath}"}/resources/plugins/smoothConfirm/jquery.smoothConfirm.js"></script>
<script type="text/javascript" src="${r"${pageContext.request.contextPath}"}/resources/assets/plugins/select2/select2_locale_zh-CN.js"></script>
<script src="${r"${pageContext.request.contextPath}"}/resources/assets/plugins/jquery-validation/dist/jquery.validate.js" type="text/javascript"></script>
<script src="${r"${pageContext.request.contextPath}"}/resources/assets/plugins/jquery-validation/localization/messages_zh.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPT -->
<script src="${r"${pageContext.request.contextPath}"}/resources/js/${urlJs}.dataTable.js" type="text/javascript"></script>
<script src="${r"${pageContext.request.contextPath}"}/resources/js/Toast.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPT -->
<title>${functionName}</title>
<style type="text/css">
    .toolbar {
    float: right;
    margin-bottom:10px;
}
</style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed">
	<!-- 页面头部 -->
	<jsp:include page="${urlPre}include/header.jsp"></jsp:include>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- 左侧导航菜单 -->
		<jsp:include page="${urlPre}include/sidebar.jsp"></jsp:include>
		<!-- BEGIN PAGE -->
		<div class="page-content">
			<!-- 标题和位置导航面包屑 -->
			<jsp:include page="${urlPre}include/breadcrumb.jsp"></jsp:include>
			<div class="clearfix"></div>
			<#if hasSubModule>
			<input id="viewBt" type="hidden" value='<shiro:hasPermission name="${className}:view"><a href="javascript:void(0);" onclick="$.rhip.${moduleName}.${subModuleName}.${instanceName}.dataTable.showView${className}Modal()" class="btn btn-sm btn-info">查看 <i class="fa fa-info"></i></a>  </shiro:hasPermission>'/>
			<input id="editBt" type="hidden" value='<shiro:hasPermission name="${className}:edit"><a href="javascript:void(0);" onclick="$.rhip.${moduleName}.${subModuleName}.${instanceName}.dataTable.showEdit${className}Modal()" class="btn btn-sm blue">编辑 <i class="fa fa-edit"></i></a>  </shiro:hasPermission>'/>
			<input id="deleteBt" type="hidden" value='<shiro:hasPermission name="${className}:delete"><a href="javascript:void(0);" onclick="$.rhip.${moduleName}.${subModuleName}.${instanceName}.dataTable.delete${className}()" id="deleteButton"class="btn btn-sm red">删除 <i class="fa fa-times"></i></a>  </shiro:hasPermission>'/>
			<#else>
			<input id="viewBt" type="hidden" value='<shiro:hasPermission name="${className}:view"><a href="javascript:void(0);" onclick="$.rhip.${moduleName}.${instanceName}.dataTable.showView${className}Modal()" class="btn btn-sm btn-info">查看 <i class="fa fa-info"></i></a>  </shiro:hasPermission>'/>
			<input id="editBt" type="hidden" value='<shiro:hasPermission name="${className}:edit"><a href="javascript:void(0);" onclick="$.rhip.${moduleName}.${instanceName}.dataTable.showEdit${className}Modal()" class="btn btn-sm blue">编辑 <i class="fa fa-edit"></i></a>  </shiro:hasPermission>'/>
			<input id="deleteBt" type="hidden" value='<shiro:hasPermission name="${className}:delete"><a href="javascript:void(0);" onclick="$.rhip.${moduleName}.${instanceName}.dataTable.delete${className}()" id="deleteButton"class="btn btn-sm red">删除 <i class="fa fa-times"></i></a>  </shiro:hasPermission>'/>
			</#if>	
			<div class="row">
				<div class="col-md-12">
					<div class="portlet box blue">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-search"></i>
								${functionName}
							</div>
							<div class="tools">
								<a href="javascript:;" class="collapse"></a>
							</div>
						</div>
						<div class="portlet-body form">
							<!-- BEGIN FORM-->
							<form id="${instanceName}QueryConditionForm"   class="horizontal-form">
								<div class="form-body">	
									<div class="row">	<!-- 如果查询条件过多 自己根据情况放到不同的row里面去-->				
									  <#list columns as column>
										<#if column.fieldName == indexName>
												<div class="col-md-4">
														<div class="form-group">
															<input type="text" id="${indexName}Input" class="form-control" placeholder="<#if column.comment != ''>${column.comment}<#else>${column.fieldName}</#if>">
														</div>		
												</div>
											<#break>
										</#if>
									   </#list>		
			   						</div>								
									<div class="row">
										<div class="col-md-12">
											<div class="" align="center">
												<shiro:hasPermission name="${className}:save">
													<button id="btnAdd" type="button" class="btn green">
														新增
														<i class="fa fa-plus"></i>
													</button>
													&nbsp;&nbsp;
												</shiro:hasPermission>
												
												<button id="btnReset" type="button" class="btn default">
													重置
													<i class="fa fa-undo"></i>
												</button>
												&nbsp;&nbsp;
												<button id="btnSearch" type="button" class="btn blue">
													查询
													<i class="fa fa-search"></i>
												</button>
												
											</div>
										</div>
									</div>
								 </div>
								</form>
							<!-- END FORM-->
						</div>
					</div>
				</div>
			</div>	
									<div class="row">
										<div class="col-md-12">
											<div class="table-responsive">
												<table id="${instanceName}Table" class="table table-striped table-hover table-bordered" >
													<thead>
														<tr>
														    <th><input id="selectAll"  type="checkbox" class="unif"></th>
															<#list columns as column>
															<th><#if column.comment != ''>
																${column.comment}
																<#else>
																${column.fieldName}
																</#if>
															</th>
															</#list>	
														</tr>
													</thead>
													<tbody>	
													</tbody>
												</table>
											</div>
										</div>
									</div>
									<!--/row-->
								</div>
		
		<!-- END PAGE -->
		<div id="${instanceName}Modal" class="modal fade" aria-hidden="true" tabindex="-1" data-backdrop="static" role="dialog" aria-labelledby="ModalLabel" data-width="480">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
						<h4 class="modal-title" id="${instanceName}ModalLabel">${functionName}</h4>
					</div>
					<!-- <div class="modal-body scroller" style="height:420px;"> -->
					<div class="modal-body" style="height: 420px; overflow-y: auto;">
						<!-- BEGIN FORM-->
						<form method="post" action="" class="form-horizontal">
							<div class="form-body">
							<div class="alert alert-danger display-hide">
										<button class="close" data-close="alert"></button>
										You have some form errors. Please check below.
									</div>
									<div class="alert alert-success display-hide">
										<button class="close" data-close="alert"></button>
										Your form validation is successful!
									</div>
									
							<#list columns as column>
								<div class="form-group">
									<label class="col-md-3 control-label">
										<#if column.comment != ''>
										${column.comment}
										<#else>
										${column.fieldName}
										</#if>
										 <#if !column.nullable>
										 <span class="required">*</span>
										 <#else>
										 <span class="required" style="color:#FFF">*</span>
										 </#if>
									</label>
									<div class="col-md-6">
									<div class="input-icon right">   
												<i class="fa"></i>   
												<#if column.size gt 100>
												<textarea id="${instanceName}Modal${column.fieldName?cap_first}Input" name="${column.fieldName}" class="form-control" maxlength="225" rows="3" placeholder=""></textarea>
												<#elseif column.size!=1 && column.javaType !='Date'>
												<input id="${instanceName}Modal${column.fieldName?cap_first}Input" name="${column.fieldName}" type="text" class="form-control" >                    
												<#elseif column.size == 1 && column.type =='CHAR'>
												<input id="${instanceName}Modal${column.fieldName?cap_first}Input" type="checkbox" class="toggle rhip-switch" name="rhip-switch" checked data-on-text="是" data-off-text="否">
												<#elseif column.javaType == 'Date'>
												此处放日历控件
												</#if>         
											</div>
									</div>
								</div>	
							</#list>							
							
								
															
							</div>
						</form>
						<!-- END FORM-->
					</div>
					<div class="modal-footer">
					    <input id="${instanceName}Modal${className}IdHiddenInput" name="id" type="hidden">
						<button type="button" data-dismiss="modal" class="btn">取消</button>
						<button type="button" id="${instanceName}ModalBtnSave" class="btn blue">保存</button>
					</div>
				</div>
			</div>
		</div>
		
	</div>
	<!-- END CONTAINER -->
	<jsp:include page="${urlPre}include/footer.jsp"></jsp:include>
</body>
<!-- END BODY -->
</html>