(function ($) {
    $.aede.pmp.comm.mail= {
    	/**
    	 * 给单人发送邮件
    	 * @param title   标题
    	 * 	      content 邮件内容
    	 * 		  userId  发送人ID
    	 * 		  type    类型 1：项目信息变更  2：人员信息变更  3：项目阶段信息变更 4:资源申请
    	 * */
		sendPTP:function(title,content,userId,type){
  	 		$.ajax({
				type : "POST",
				async : false,// 同步请求
				url : $.aede.expand.getContextPath() + "/management/pmp/comm/mailSend/sendPTP",
				dataType : "json",
				data: {
                	"title":title,
                	"content":content,
                	"userId":userId,
                	"type":type
                },
				success : function(data) {
					//$.aede.comm.toastr.success("邮件发送成功！");
				},
				error : function() {
					$.aede.comm.toastr.error("邮件发送失败！");
				}
  	 		});
  	 	},

    	/**
    	 * 根据项目给成员发送邮件
    	 * @param title   	 标题
    	 * 	      content 	 邮件内容
    	 * 		  projectId  项目编号
    	 * 		  type    	 类型 1：项目信息变更  2：人员信息变更  3：项目阶段信息变更 4:资源申请
    	 * */
 		sendAccToProject:function(title,content,projectId,type){
  	 		$.ajax({
				type : "POST",
				async : false,// 同步请求
				url : $.aede.expand.getContextPath() + "/management/pmp/comm/mailSend/sendAccToProject",
				dataType : "json",
				data: {
                	"title":title,
                	"content":content,
                	"projectId":projectId,
                	"type":type
                },
				success : function(data) {
					//$.aede.comm.toastr.success("邮件发送成功！");
				},
				error : function() {
					$.aede.comm.toastr.error("邮件发送失败！");
				}
  	 		});
  	 	},
  	 	/*
    	 * 给当前机构的地区管理员发送邮件
    	 * */
  	 	sendAreaAdmins:function(title,content,orgId){
  	 		$.ajax({
				type : "POST",
				async : false,// 同步请求
				url : $.aede.expand.getContextPath() + "/management/pmp/comm/mailSend/sendAreaAdmins",
				dataType : "json",
				data: {
                	"title":title,
                	"content":content,
                	"orgId":orgId
                },
				success : function(data) {
					//$.aede.comm.toastr.success("邮件发送成功！");
				},
				error : function() {
					$.aede.comm.toastr.error("邮件发送失败！");
				}
  	 		});
  	 	},
  	 	/*
    	 * 给超级管理员发送邮件
    	 * */
  	 	sendSuperAdmins:function(title,content){
  	 		$.ajax({
				type : "POST",
				async : false,// 同步请求
				url : $.aede.expand.getContextPath() + "/management/pmp/comm/mailSend/sendSuperAdmins",
				dataType : "json",
				data: {
                	"title":title,
                	"content":content
                },
				success : function(data) {
					//$.aede.comm.toastr.success("邮件发送成功！");
				},
				error : function() {
					$.aede.comm.toastr.error("邮件发送失败！");
				}
  	 		});
  	 	}
    }
})(jQuery);