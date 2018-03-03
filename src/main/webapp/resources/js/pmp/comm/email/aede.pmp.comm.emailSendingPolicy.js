(function($) {
	// 邮件发送策略
	$.aede.pmp.comm.emailSendPolicy = {
		validateForm : "",// 表单验证
		// 初始化页面按钮
		initBtnListener : function() {
			$("#pmp_ptmt_project_emailSendPolicy_btnSave").bind("click",
					function() {
						$.aede.pmp.comm.emailSendPolicy.saveForm();
					});
		},
		// 初始化页面
		initForm : function() {
			$
					.ajax({
						type : "POST",
						async : false,// 同步请求
						url : $.aede.expand.getContextPath()
								+ "/management/pmp/comm/email/emailSendingPolicy/queryEmailSendPolicy",
						dataType : "json",
						success : function(data) {
							if (data["responseCode"] == "1") {
								$("#id").val(data.datas.id);
								$("#pmp_ptmt_project_emailSendPolicy_smtp_server")
										.val(data.datas.smtpServer);
								$("#pmp_ptmt_project_emailSendPolicy_port")
										.val(data.datas.port);
								$("#pmp_ptmt_project_emailSendPolicy_account")
										.val(data.datas.account);
								$("#pmp_ptmt_project_emailSendPolicy_password")
										.val(data.datas.password);
								if (data.datas.isPjchangeSend == 1) {
									document
											.getElementById("pmp_ptmt_project_emailSendPolicy_is_pjchange_send").checked = true;
								}
								if (data.datas.isPjmemchangeSend == 1) {
									document
											.getElementById("pmp_ptmt_project_emailSendPolicy_is_pjmemchange_send").checked = true;
								}
								if (data.datas.isRsapplySend == 1) {
									document
											.getElementById("pmp_ptmt_project_emailSendPolicy_is_rsapply_send").checked = true;
								}
								if (data.datas.isPjstageSend == 1) {
									document
											.getElementById("pmp_ptmt_project_emailSendPolicy_is_pjstage_send").checked = true;
								}
							} else {
								$.aede.comm.toastr.error(data["responseMsg"]);
							}
							return false;
						},
						error : function() {
							$.aede.comm.toastr.error("请求异常，加载页面失败！");
						}
					});
		},
		// 模式窗口表单验证
		initModalFormValidate : function() {
			var icon = "<i class='fa fa-times-circle'></i> ";
			$.aede.pmp.comm.emailSendPolicy.validateForm = $(
					'#pmp_ptmt_project_emailSendPolicy_form form')
					.validate(
							{
								rules : {
									smtp_server : {
										required : true,
									},
									port : {
										required : true,
									},
									account : {
										required : true,
										email :true,
									},
									password : {
										required : true,
									}
								},
								messages : {
									smtp_server : {
										required : icon + "请填写smtp服务器",
									},
									port : {
										required : icon + "请填写端口号",
									},
									account : {
										required : icon + "请填写邮箱账户",
										email  : icon + "请填写正确的邮箱账户",
									},
									password : {
										required : icon + "请填写邮箱密码",
									}
								},
								highlight : function(element) {
									$(element).closest('.form-group')
											.removeClass('has-success')
											.addClass('has-error');
								},
								success : function(element) {
									element.closest('.form-group').removeClass(
											'has-error')
											.addClass('has-success');
								},
								errorElement : "span",
								errorPlacement : function(error, element) {
									if (element.is(":radio")
											|| element.is(":checkbox")) {
										error.appendTo(element.parent()
												.parent().parent());
									} else {
										error.appendTo(element.parent());
									}
								},
								errorClass : "help-block m-b-none",
								validClass : "hel-pblock m-b-none"
							});
			return false;
		},
	    // 清除表单验证样式
        cleanModalFormValidateStyle : function() {
            if (!!$.aede.pmp.comm.emailSendPolicy.validateForm) {
                // 清除表单验证样式
            	$.aede.pmp.comm.emailSendPolicy.validateForm.resetForm();
                // XXX jQuery Validate resetFrom不能清除highlight添加的样式，所有需要手动清理
                $('#pmp_ptmt_project_emailSendPolicy_form .form-group').removeClass('has-error');
                $('#pmp_ptmt_project_emailSendPolicy_form .form-group').removeClass('has-success');
            }
        },
		// 获取表单元素值
		getModalFormEleVal : function() {
			var postData = {};
			postData.id=$("#id").val();
			postData.smtpServer = $.trim($(
					"#pmp_ptmt_project_emailSendPolicy_smtp_server").val());
			postData.port = $.trim($("#pmp_ptmt_project_emailSendPolicy_port")
					.val());
			postData.account = $.trim($(
					"#pmp_ptmt_project_emailSendPolicy_account").val());
			postData.password = $.trim($(
					"#pmp_ptmt_project_emailSendPolicy_password").val());
			if (document
					.getElementById("pmp_ptmt_project_emailSendPolicy_is_pjchange_send").checked == true) {
				postData.isPjchangeSend =1;

			}else{
				postData.isPjchangeSend =0;
			}
			if (document
					.getElementById("pmp_ptmt_project_emailSendPolicy_is_pjmemchange_send").checked == true) {
				postData.isPjmemchangeSend =1;

			}else{
				postData.isPjmemchangeSend =0;
			}
			if (document
					.getElementById("pmp_ptmt_project_emailSendPolicy_is_rsapply_send").checked == true) {
				postData.isRsapplySend =1;
			}else{
				postData.isRsapplySend =0;
			}
			if (document
					.getElementById("pmp_ptmt_project_emailSendPolicy_is_pjstage_send").checked == true) {
				postData.isPjstageSend =1;
			}else{
				postData.isPjstageSend =0;
			}
			return postData;
		},
		// 保存页面信息
		saveForm : function() {
			$.aede.pmp.comm.emailSendPolicy.cleanModalFormValidateStyle;
			if ($.aede.pmp.comm.emailSendPolicy.validateForm.form()) {
				var postData = $.aede.pmp.comm.emailSendPolicy
						.getModalFormEleVal();
				$.ajax({
							type : "POST",
							async : false,// 同步请求
							url : $.aede.expand.getContextPath()
									+ "/management/pmp/comm/email/emailSendingPolicy/update",
							dataType : "json",
							data : postData,
							success : function(data) {
								if (data["responseCode"] == "1") {
									$.aede.comm.toastr.success("保存邮件发送策略成功！");
								} else {
									$.aede.comm.toastr
											.error(data["responseMsg"]);
								}
								return false;
							},
							error : function() {
								$.aede.comm.toastr.error("请求异常，编辑邮件发送策略失败！");
							}
						});
			}

		},
		// 页面初始化
		init : function() {
			$.aede.pmp.comm.emailSendPolicy.initForm();
			$.aede.pmp.comm.emailSendPolicy.initModalFormValidate();
			$.aede.pmp.comm.emailSendPolicy.initBtnListener();
		}
	}
})(jQuery);
$(function() {
	$.aede.pmp.comm.emailSendPolicy.init();
});