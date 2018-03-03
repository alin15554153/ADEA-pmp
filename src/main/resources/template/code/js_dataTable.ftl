(function($) {
	// ${functionName}管理
	$.${urlJs}.dataTable = {
		${instanceName}Table : "",// ${functionName}列表
		validateForm : "",// 模式窗口表单验证
		selectIds : {
			//如果有下拉框在此处设置id eg provinceSelectId : "#${instanceName}ModalProvinceSelect",
		},
		modalSelectIds : {
		},
		// 初始化页面
		init : function() {
			// 重置按钮点击事件
			$.${urlJs}.dataTable.initResetBtnClickListener();
			// 查询按钮点击事件
			$.${urlJs}.dataTable.initSearchBtnClickListener();
			
			// 模式窗口显示出来后触发的事件(将滚动条回滚到顶端)
			$.${urlJs}.dataTable.initModalShownEventListener();
			
			// 新增按钮点击事件
			$.${urlJs}.dataTable.initAddBtnClickListener();
			// 模式窗口表单验证
			$.${urlJs}.dataTable.initModalFormValidate();
			// 回车提交表单
			$.${urlJs}.dataTable.initEnterListener();
			// switchChange事件
			$.${urlJs}.dataTable.initSwitchChangeListener();
			
			$("#btnSearch").click();			
		},
		//回车查询事件
		initEnterListener : function() {
		$('#${instanceName}QueryConditionForm input').keypress(function (e) {
            if (e.which == 13) {
            	$("#btnSearch").click();
                return false;
            }
        });
		},
		
		// 重置按钮点击事件
		initResetBtnClickListener : function() {
			$("#btnReset").click(function() {
				$("#${instanceName}QueryConditionForm")[0].reset();
				return false;
			});
		},
		// 查询按钮点击事件
		initSearchBtnClickListener : function() {
			$("#btnSearch").click(function() {
				$.${urlJs}.dataTable.init${className}Table();
				return false;
			});
		},
		// 新增按钮点击事件
		initAddBtnClickListener : function() {
			$("#btnAdd").click(function() {
				// 显示查看${className}信息窗口
				$.${urlJs}.dataTable.showAdd${className}Modal();
				return false;
			});
		},
		// switchChange事件
		initSwitchChangeListener : function(){
			$(".rhip-switch").on(
					'switchChange.bootstrapSwitch', function(event, state) {
					      state ? $(this).val(1) : $(this).val(0);
					    }
			);
		},
		// 初始化${functionName}列表
		init${className}Table : function() {
			
			if (!$.${urlJs}.dataTable.${instanceName}Table) {
				// 初始化${instanceName}Table
				$.${urlJs}.dataTable.${instanceName}Table = $("#${instanceName}Table").dataTable(
						{
							"oLanguage" : {
								"sUrl" : $.rhip.getContextPath() + "/resources/assets/plugins/data-tables/oLanguage.txt"
							},
							"dom":'l<"toolbar">rtip',
							"fnInitComplete":function(){             
								$(".toolbar").html($("#viewBt").val());
								$(".toolbar").append($("#editBt").val());
								$(".toolbar").append($("#deleteBt").val());
								$.${urlJs}.dataTable.initSelectAllClickListener();
							},
							"iDisplayLength" : 10,
							"aLengthMenu" : [ 10, 25, 50, 100 ],
							"sPaginationType" : "bootstrap",
							"bServerSide" : true,
							"sServerMethod" : "POST",
							"bProcessing" : true,
							"bStateSave" : true,
							"bSort" : false,
							"bFilter" : false,
							<#if hasSubModule>
							"sAjaxSource" : $.rhip.getContextPath() + '/management/${moduleName}/${subModuleName}/${instanceName}/queryByPaging',
							<#else>
							"sAjaxSource" : $.rhip.getContextPath() + '/management/${moduleName}/${instanceName}/queryByPaging',
							</#if>	
							"fnServerData" : function(sSource, aoData, fnCallback) {
								var ${indexName} = $("#${indexName}Input").val();
								if (!!${indexName}) {
									aoData.push({
										"name" : "${indexName}",
										"value" : ${indexName}
									});
								}
								$.ajax({
									"dataType" : 'json',
									"type" : "POST",
									"url" : sSource,
									"data" : aoData,
									"success" : fnCallback,
									"error" :  function(XMLHttpRequest, textStatus, errorThrown){
										$.Toast.error("与后台交互时发生错误："+textStatus);
								       }
								});
							},
							"aoColumns" :[
							{
								"mDataProp" : "id"
							},
							<#list columns as column>
							{
								"mDataProp" : "${column.fieldName}"
							}<#if column_has_next>,</#if>
							</#list>	
							],
							"aoColumnDefs" : [
									{
										'aTargets' : [ 0 ], 
										'mRender'  : function ( data, type, full ) {
										 return "<input type=\"checkbox\" class=\"unif\" name=\"id\" value=\""
										 +data+"\"><script>$(\".unif\").uniform();</script>" ; 
										 //动态生成的form元素没有被uniform初始化
										}
									}
									 ]
						});
			} else {
				// 重绘${instanceName}Table
				var oSettings = $.${urlJs}.dataTable.${instanceName}Table.fnSettings();
				oSettings._iDisplayStart = 0;
				$.${urlJs}.dataTable.${instanceName}Table.fnDraw(oSettings);
			}
		},
		// 初始化模式窗口
		init${className}Modal : function() {
			// 清空模式窗口表单元素值
			$.${urlJs}.dataTable.cleanModalFormEleVal();
			// 清除表单验证样式
			$.${urlJs}.dataTable.cleanModalFormValidateStyle();
			// 清空保存按钮点击事件
			$("#${instanceName}ModalBtnSave").unbind("click");
			// 显示保存按钮
			$('#${instanceName}ModalBtnSave').show();
			// 清除disabled样式
			$("#${instanceName}Modal form input:not(.rhip-switch)").attr("disabled",false);
			$("#${instanceName}Modal form select").select2("enable", true);
			$("#${instanceName}Modal form textarea").attr("disabled",false);
			$(".rhip-switch").bootstrapSwitch('disabled', false);
		},
		// 初始化模式窗口表单信息
		initModalFormEleValBy${className}Id:function(id){
			$.ajax({
				type : "GET",
				async : false,// 同步请求
				<#if hasSubModule>
					url : $.rhip.getContextPath() + "/management/${moduleName}/${subModuleName}/${instanceName}/" + id + "/view",
					<#else>
					url : $.rhip.getContextPath() + "/management/${moduleName}/${instanceName}/" + id + "/view",
				</#if>		
				dataType : "json",
				success : function(data) {
					if (data[$.Constants.respCode] == $.Constants.yes) {
						var ${instanceName} = data[$.Constants.datas];
						<#list columns as column>
							$("#${instanceName}Modal${column.fieldName?cap_first}Input").val(${instanceName}.${column.fieldName});
							<#if column.type == 'CHAR' && column.size == 1>
								if(${instanceName}.${column.fieldName}==0){ //0为false
									$("#${instanceName}Modal${column.fieldName?cap_first}Input").bootstrapSwitch('state', false);
								}else{
									$("#${instanceName}Modal${column.fieldName?cap_first}Input").bootstrapSwitch('state', true);
								}
							</#if>  
						</#list>

						$("#${instanceName}Modal${className}IdHiddenInput").val(${instanceName}.id);
					} else {
						$.Toast.error(data[$.Constants.respMsg]);
					}
					return false;
				},
				error : function() {
					$.Toast.error("请求异常，查询${functionName}失败！");
				}
			});
		},
		// 显示编辑${functionName}信息窗口
		showEdit${className}Modal : function() {
			var idArray=$.${urlJs}.dataTable.getIdArray();
			var len=idArray.length;
			if(1==len){
				$("#${instanceName}Modal .modal-header .modal-title").html("编辑${functionName}");
				// 初始化模式窗口
				$.${urlJs}.dataTable.init${className}Modal();
				if (!!idArray[0]) {
					$.${urlJs}.dataTable.initModalFormEleValBy${className}Id(idArray[0]);
					// 初始化保存编辑${functionName}按钮的click事件(该按钮的其他点击事件已经在init${className}Modal时被清除)
					$.${urlJs}.dataTable.initSaveEdit${className}BtnClickListener();
					$('#${instanceName}Modal').modal('show');
				} else {
					$.Toast.error("${functionName}的id丢失，无法查询！");
				}
			}else{
				if(0==len){
					$.Toast.warning("请选择您要编辑的记录！");
				}else{
					$.Toast.warning("请选择唯一的一条记录！");
				}		
			}
		},
		// 显示查看${functionName}信息窗口
		showView${className}Modal : function() {
			var idArray=$.${urlJs}.dataTable.getIdArray();
			var len=idArray.length;
			if(1==len){
				$("#${instanceName}Modal .modal-header .modal-title").html("查看${functionName}");
				$.${urlJs}.dataTable.init${className}Modal();// 初始化模式窗口
				
				$.${urlJs}.dataTable.initModalFormEleValBy${className}Id(idArray[0]);
				//隐藏保存按钮
				$('#${instanceName}ModalBtnSave').hide();
				$("#${instanceName}Modal form input:not(.rhip-switch)").attr("disabled","disabled");
				$("#${instanceName}Modal form select").select2("enable", false);
				$("#${instanceName}Modal form textarea").attr("disabled","disabled");
				$(".rhip-switch").bootstrapSwitch('disabled', true);
				
				$('#${instanceName}Modal').modal('show');
			}else{
				if(0==len){
					$.Toast.warning("请选择您要查看的记录！");
				}else{
					$.Toast.warning("请选择唯一的一条记录！");
				}		
			}
		},
		// 显示新建${functionName}信息窗口
		showAdd${className}Modal : function() {
			$("#${instanceName}Modal .modal-header .modal-title").html("新增${functionName}");
			$.${urlJs}.dataTable.init${className}Modal();// 初始化模式窗口
			// 初始化保存新建用户按钮的click事件
			$.${urlJs}.dataTable.initSaveAdd${className}BtnClickListener();
			// 显示模式窗口
			$("#${instanceName}Modal").modal("show");
		},
		// 初始化保存编辑${functionName}按钮的click事件
		initSaveEdit${className}BtnClickListener : function() {
			$("#${instanceName}ModalBtnSave").bind("click", function() {
				if ($.${urlJs}.dataTable.validateForm.form()) {
					var postData = $.${urlJs}.dataTable.getModalFormEleVal();
					if (!postData.id) {
						$.Toast.error("${functionName}信息丢失，无法保存编辑结果！");
						return false;
					}
					$.ajax({
						type : "POST",
						async : false,// 同步请求
						<#if hasSubModule>
						url : $.rhip.getContextPath() + "/management/${moduleName}/${subModuleName}/${instanceName}/"+postData.id+"/update", 
						<#else>
						url : $.rhip.getContextPath() + "/management/${moduleName}/${instanceName}/"+postData.id+"/update",
						</#if>	
						dataType : "json",
						data : postData,
						success : function(data) {
							if (data[$.Constants.respCode] == $.Constants.yes) {
								$.Toast.success("编辑${functionName}成功！");
								// 隐藏模式窗口
								$('#${instanceName}Modal').modal('hide');
								// 查询用户信息
								$.${urlJs}.dataTable.init${className}Table();
							} else {
								$.Toast.error(data[$.Constants.respMsg]);
							}
							return false;
						},
						error : function() {
							$.Toast.error("请求异常，编辑${functionName}失败！");
						}
					});
				} else {
					$.Toast.warning("请正确填写${functionName}信息！");
				}
			});
		},
		// 初始化保存新建${functionName}按钮的click事件
		initSaveAdd${className}BtnClickListener : function() {
			$("#${instanceName}ModalBtnSave").bind("click", function() {
				if ($.${urlJs}.dataTable.validateForm.form()) {
					var postData = $.${urlJs}.dataTable.getModalFormEleVal();
					if (!!postData.id) {
						// 新建时，id应该为空
						$.Toast.error("${functionName}的ID异常，无法新建！");
						return false;
					}
					$.ajax({
						type : "POST",
						async : false,// 同步请求
						<#if hasSubModule>
						url : $.rhip.getContextPath() + "/management/${moduleName}/${subModuleName}/${instanceName}/add",
						<#else>
						url : $.rhip.getContextPath() + "/management/${moduleName}/${instanceName}/add",
						</#if>	
						
						dataType : "json",
						data : postData,
						success : function(data) {
							if (data[$.Constants.respCode] == $.Constants.yes) {
								$.Toast.success("新建${functionName}成功！");
								// 隐藏模式窗口
								$('#${instanceName}Modal').modal('hide');
								// 查询${functionName}信息
								$.${urlJs}.dataTable.init${className}Table();
							} else {
								$.Toast.error(data[$.Constants.respMsg]);
							}
							return false;
						},
						error : function() {
							$.Toast.error("请求异常，新建${functionName}失败！");
						}
					});
				} else {
					$.Toast.warning("请正确填写${functionName}信息！");
				}
			});
		},
		// 清空模式窗口表单元素值
		cleanModalFormEleVal : function() {
			// 清空隐藏域
			$("#${instanceName}Modal${className}IdHiddenInput").val("");
			$("#${instanceName}Modal form")[0].reset();// 重置表单
			for ( var key in $.${urlJs}.dataTable.modalSelectIds) {
				if ($($.${urlJs}.dataTable.modalSelectIds[key]).length) {
					$($.${urlJs}.dataTable.modalSelectIds[key]).select2("val", "");
					$($.${urlJs}.dataTable.modalSelectIds[key]).select2("enable", true);
				}
			}
		},
		// 获取模式窗口表单元素值
		getModalFormEleVal : function() {
			var postData = {};
			<#list columns as column>
				postData.${column.fieldName}=$.trim($("#${instanceName}Modal${column.fieldName?cap_first}Input").val());
			</#list>
			var id = $.trim($("#${instanceName}Modal${className}IdHiddenInput").val());
			if (!!id) {// ${functionName}的id
				postData.id = id;
			}
			
			return postData;
		},
		// 模式窗口显示出来后触发的事件
		initModalShownEventListener : function() {
			$("#${instanceName}Modal").on("shown.bs.modal", function() {
				// 将滚动条会滚到顶端
				$("#${instanceName}Modal .modal-body").scrollTop(0);
			});
		},
		// 模式窗口表单验证
		initModalFormValidate : function() {
			$.${urlJs}.dataTable.validateForm = $('#${instanceName}Modal form').validate({
				errorElement : 'span', // default input error message container
				errorClass : 'help-block', // default input error message class
				focusInvalid : false, // do not focus the last invalid input
				rules : {//此处添加需要校验的字段的规则  参考http://jqueryvalidation.org/
				
				<#list columns as column>
					${column.fieldName} :{
					 <#if !column.nullable>
					 required : true,
					 </#if>
					 maxlength : ${column.size}
					}<#if column_has_next>,</#if>
			    </#list>
			
					
				},
				highlight : function(element) {
					// hightlight error inputs
					// set error class to the control group
					$(element).closest('.form-group').addClass('has-error');
				},
				unhighlight : function(element, errorClass, validClass) {
					$(element).closest('.form-group').removeClass('has-error');
				},
				success : function(label) {
					label.closest('.form-group').removeClass('has-error');
					label.remove();
				},
				errorPlacement : function(error, element) {
					error.insertAfter(element.closest('.form-group > div'));
				}
			});
			
			return false;
		},
		// 清除模式窗口表单验证样式
		cleanModalFormValidateStyle : function() {
			if (!!$.${urlJs}.dataTable.validateForm) {
				// 清除表单验证样式
				$.${urlJs}.dataTable.validateForm.resetForm();
				// XXX jQuery Validate resetFrom不能清除highlight添加的样式，所有需要手动清理
				$('#${instanceName}Modal form .form-group').removeClass('has-error');
			}
		},
	
		//全选
		initSelectAllClickListener : function(){
			$("#selectAll").click(function(){
				$.uniform.update($("input[name='id']").attr("checked",this.checked));
			});
		},
		
		getIdArray : function(){
			var idArray=new Array();
			 $("input[name='id']:checked").each(function(){   
				 idArray.push($(this).val());
		            });
			 return idArray;
		},
		
		// 删除${functionName}
		delete${className} : function() {
			var idArray=$.${urlJs}.dataTable.getIdArray();
			var len=idArray.length;
			var ids=new Array();
			for(var i=0;i<len;i++){
				id = idArray[i];
				ids.push(id);
			}				
			if (len>0) {
				$("#deleteButton").smoothConfirm("确定要删除选中的${functionName}？", {
					okVal : "删除",
					// 点击确认返回callback
					ok : function() {
						$.ajax({
							<#if hasSubModule>
							url : $.rhip.getContextPath() + "/management/${moduleName}/${subModuleName}/${instanceName}/delete",
							<#else>
							url : $.rhip.getContextPath() + "/management/${moduleName}/${instanceName}/delete",
							</#if>	
							
							dataType : "json",
							contentType: "application/json; charset=utf-8",
							data : JSON.stringify(ids), 
							type : "POST",
							async : false,// 同步请求
							success : function(data, textStatus) {
								if (data[$.Constants.respCode] == $.Constants.yes) {
									$.Toast.success("删除${functionName}成功！");
									// 查询${functionName}信息
									$.${urlJs}.dataTable.init${className}Table();
								} else {
									$.Toast.error(data[$.Constants.respMsg]);
								}
							},
							error : function(XMLHttpRequest, textStatus) {
								$.Toast.error("请求异常，删除${functionName}失败！");
							}
						});
					},
					// 点击取消返回callback
					cancel : function() {
						return false;
					}
				});
			} else {
				$.Toast.warning("请选择您要删除的记录！");
			}
			return false;
		}
	};
})(jQuery);

$(function() {
    $(".rhip-switch").bootstrapSwitch();
	$.${urlJs}.dataTable.init();	
});