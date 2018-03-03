(function ($) {
    $.aede.pmp= {
    	validateForm: "",//表单验证
		// 模式窗口表单验证
        initModalFormValidate : function() {
            var icon = "<i class='fa fa-times-circle'></i> ";
            $.aede.pmp.validateForm = $('#userInfo_form form').validate({
                rules: {
                	realname:{
                		required:true,
                		maxlength:24
                	},
                	phone:{
                		//required:true,
                		rangelength:[11,11]
                	},
                	email :{
                		//required:true,
                		email:true  
                	}
                },
                messages: {
                	realname:{
                		required:icon + "请填写真实姓名",
                		maxlength:icon + "长度不能超过24"
                	},
                	phone:{
                		//required:icon + "请填写电话",
                		rangelength:icon + "请填写正确的电话"
                	},
                	email :{
                		//required:icon + "请填写邮箱",
                		email:icon + "请填写正确的邮箱"
                		
                	}
                },
                highlight: function (element) {
                    $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
                },
                success: function (element) {
                    element.closest('.form-group').removeClass('has-error').addClass('has-success');
                },
                errorElement: "span",
                errorPlacement: function (error, element) {
                    if (element.is(":radio") || element.is(":checkbox")) {
                        error.appendTo(element.parent().parent().parent());
                    } else {
                        error.appendTo(element.parent());
                    }
                },
                errorClass: "help-block m-b-none",
                validClass: "hel-pblock m-b-none"
            });
            return false;
        },
		closeIframe: function(){
			var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	        parent.layer.close(index);
			
		},
	    view: function(){
	    	var id= $.aede.expand.comm.loginUser.userId;
	        $.ajax({
	            type : "GET",
	            async : false,// 同步请求
	            url : $.aede.expand.getContextPath() + "/management/pmp/comm/webUser/" + id + "/view",
	            dataType : "json",
	            success : function(data) {
	                if (data["responseCode"] == "1") {
	                    var result = data["datas"];
	                    $("#realname").val(result.realname);
	                    $("#phone").val(result.phone);
	                    $("#email").val(result.email);
	                } else {
	                    $.aede.comm.toastr.error(data["responseMsg"]);
	                }
	                return false;
	            },
	            error : function() {
	                $.aede.comm.toastr.error("请求异常，查询失败！");
	            }
	        });
	    },
	    saveUserInfo: function(){
	    	if($.aede.pmp.validateForm.form()) {
			swal({
	            title: "您正在修改个人资料！",
	           // text: "您正在修改密码！",
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "确定",
	            cancelButtonText: "取消",
	            closeOnCancel: false
	        }, function(isConfirm){
	            if (isConfirm) {
	            	var postData = {};
	            	postData.id=$.aede.expand.comm.loginUser.userId;
	    			postData.realname = $.trim($("#realname").val()); 
	    			postData.phone = $.trim($("#phone").val());
	    			postData.email = $.trim($("#email").val());
	    			$.ajax({
	    				type : "POST",
	    				async : false,// 同步请求
	    				url :$.aede.expand.getContextPath()  + "/management/pmp/comm/webUser/update",
	    				dataType : "json",
	    				data : postData,
	    				success : function(data) {
	    					if (data["responseCode"] == "1") {
	    						$.aede.comm.toastr.success("修改成功");
	    						//跳转至登陆页面
	    						//parent.window.location.href = $.aede.expand.getContextPath()+"/logout";
	    					} else {
	    						$.aede.comm.toastr.warning(data["responseMsg"]);
	    					}
	    					return false;
	    				},
	    				error : function() {
	    					$.aede.comm.toastr.error("请求异常，修改失败！");
	    				}
	    			});
	            } else {
	                swal("取消修改！", "密码没有修改。", "success");
	            }
	        });
	    }
	    },
	    init:function(){
	    	$.aede.pmp.initModalFormValidate();
	    	$.aede.pmp.view();
	    }
	    }
})(jQuery);
$(function(){
	$.aede.pmp.init();
});
