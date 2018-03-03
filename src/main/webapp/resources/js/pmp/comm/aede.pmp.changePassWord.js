(function ($) {
    $.aede.pmp= {
	resetpwd: function(){
		var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
		
	},
    savepwd: function(){
    	if($("#plainPassword").val()==""){
    		$.aede.comm.toastr.warning("当前密码不可为空！");
    		return false;    	
    	}
    	if($("#newPassword").val()==""){
    		$.aede.comm.toastr.warning("新密码不可为空！");
    		return false;    	
    	}
    	if($("#rPassword").val()==""){
    		$.aede.comm.toastr.warning("确认密码不可为空！");
    		return false;    	
    	}
		swal({
            title: "您正在修改密码！",
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
    			postData.plainPassword = $.trim($("#plainPassword").val()); 
    			var  newPassword = $.trim($("#newPassword").val());
    			var  rPassword = $.trim($("#rPassword").val());
    			$.ajax({
    				type : "POST",
    				async : false,// 同步请求
    				url :$.aede.expand.getContextPath()  + "/management/pmp/comm/webUser/" + newPassword +"/"+rPassword+ "/savepwd",
    				dataType : "json",
    				data : postData,
    				success : function(data) {
    					if (data["responseCode"] == "1") {
    						$.aede.comm.toastr.success("修改成功");
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
    }
})(jQuery);
$(document).ready(function () {
});