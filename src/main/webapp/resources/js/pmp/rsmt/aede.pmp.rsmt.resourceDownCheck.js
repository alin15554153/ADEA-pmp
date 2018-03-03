$(document).ready(function () {
	$.aede.pmp.rsmt.resourceDownCheck.initBtnListener();
	//$.aede.pmp.rsmt.resourceDownCheck.initStageSelect();
	$.aede.pmp.rsmt.resourceDownCheck.initTable();
});
(function($) {
	$.aede.pmp.rsmt.resourceDownCheck = {
		table:"",
		initTable : function() {
			if (!$.aede.pmp.rsmt.resourceDownCheck.table) {
				var resourceName = $.trim($("#pmp_rsmt_resourceDownCheck_resourceName").val());//资源名称
				var resourceType = $.trim($("#pmp_rsmt_resourceDownCheck_resourceType").val());//资源类别
				var stage = $.trim($("#pmp_rsmt_resourceDownCheck_stage").val());//阶段
				var projectName = $.trim($("#pmp_rsmt_resourceDownCheck_projectName").val());//项目名称
				$.aede.pmp.rsmt.resourceDownCheck.table = $('#pmp_rsmt_resourceDownCheck_table').DataTable({
					serverSide: true,
					ajax: {
						url: $.aede.expand.getContextPath() + '/management/pmp/rsmt/resourceDownCheck/findUncheckedApplicationList',
						type: 'POST',
						dataType: 'json',
						data: function(d) {
							var resourceName = $.trim($("#pmp_rsmt_resourceDownCheck_resourceName").val());
							var resourceType = $.trim($("#pmp_rsmt_resourceDownCheck_resourceType").val());//资源类别
							var stage = $.trim($("#pmp_rsmt_resourceDownCheck_stage").val());//阶段
							var projectName = $.trim($("#pmp_rsmt_resourceDownCheck_projectName").val());//项目名称
							var resourceDownId = $.trim($("#pmp_rsmt_resourceDownCheck_resourceDownId").val());//申请ID

							var myId = $.aede.expand.comm.loginUser.userId;

							var myRoleId = "";
							var roleNames = $.aede.expand.comm.loginUser.roleNames;
							//console.info(roleNames);
							var re1 = /.项目经理/;
							var re2 = /.地区管理员/;
							var re3 = /.集团管理员/;
							if (re3.test(roleNames)) {
								myRoleId = "14004";
							} else if (re2.test(roleNames)) {
								myRoleId = "14003";
							} else if (re1.test(roleNames)) {
								myRoleId = "14002";
							}

							if (!!resourceName) {
								d.resourceName = resourceName;
							}
							if (!!resourceType) {
								d.resourceType = resourceType;
							}
							if (!!stage) {
								d.projectStage = stage;
							}
							if (!!projectName) {
								d.projectName = projectName;
							}
							if (!!resourceDownId) {
								d.resourceDownId = resourceDownId;
							}

							if (myId != "" && !!myId) {
								d.myId = myId;
							}
							if (myRoleId != "") {
								d.myRoleId = myRoleId;
							} else {
								d.myRoleId = '999';
							}
						}
					},
					columns: [
						{
							data: "resourceName"
						},
						{
							data: "resourceTypeName"
						},
						{
							data: "projectName"
						},
						{
							data: "userName"
						},
						{
							data: "atimeString"
						},
						{
							data: "aremark"
						},
						{
							data: "id"
						}
					],
					columnDefs: [
						{
							"targets" : [0],
							"render" : function(data,event,full) {
								var newData=data;
								//console.info(newData.length);
								if(newData.length > 15){
									newData=newData.substring(0,16)+"...";
								}
								return "<span title=\""+data+"\">"+newData+"</span>";
							}
						},
						/*{
						 "targets" : [1],
						 "render" : function(data,event,full) {
						 return $.aede.expand.comm.getDictName('资源类别',data);
						 }
						 },*/
						{
							"targets" : [2],
							"render" : function(data,event,full) {
								var newData=data;
								//console.info(newData.length);
								if(newData.length > 15){
									newData=newData.substring(0,16)+"...";
								}
								return "<span title=\""+data+"\">"+newData+"</span>";
							}
						},
						{
							"targets" : [5],
							"render" : function(data,event,full) {
								var newData=data;
								//console.info(newData.length);
								if(newData.length > 10){
									newData=newData.substring(0,11)+"...";
								}
								return "<span title=\""+data+"\">"+newData+"</span>";
							}
						},
						{
							"targets" : [6],
							"render" : function(data,event,full) {
								var preRemark = "<button type='button' class='btn btn-primary btn-xs' onclick='$.aede.pmp.rsmt.resourceDownCheck.showPreRemark(\""+data+"\");'>过往审核意见</button>&nbsp;&nbsp;";
								var checkHtml = "<button type='button' class='btn btn-primary btn-xs' onclick='$.aede.pmp.rsmt.resourceDownCheck.showCheckModal(\""+data+"\",\""+full.processType+"\",\""+full.orgId+"\");'>审核</button>";
								return preRemark + checkHtml;
							}
						}
					]
				});
			} else {
				$.aede.pmp.rsmt.resourceDownCheck.table.ajax.reload();
			}
		},

		initBtnListener:function(){
			//加载查询按钮事件
			$("#pmp_rsmt_resourceDownCheck_btnSearch").click(function(){
				$("#pmp_rsmt_resourceDownCheck_resourceDownId").val("");
				$.aede.pmp.rsmt.resourceDownCheck.initTable();
				return false;
			});
			//加载重置按钮事件
			$("#pmp_rsmt_resourceDownCheck_btnReset").click(function(){
				$("#pmp_rsmt_resourceDownCheck_resourceName").val("");
				$("#pmp_rsmt_resourceDownCheck_resourceType").val("");
				$("#pmp_rsmt_resourceDownCheck_stage").val("");
				$("#pmp_rsmt_resourceDownCheck_projectName").val("");
			});
		},

		//过往审核意见按钮点击事件
		showPreRemark:function(id){
			//清空表格
			$("#pmp_rsmt_resourceDownCheck_preRemarkTable tbody").html("");
			//获取审核信息列表
			$.ajax({
				url : $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDownCheck/showPreRemark",
				dataType : "json",
				data : {"id":id},
				type : "POST",
				async : false,
				success : function(data, textStatus) {
					if (data.responseCode == "1") {
						var checks = data.datas;//List<ResourceDownCheck>
						for (var i=0; i<checks.length; i++) {
							var trHtml = "<tr><td>"+checks[i].sname+"</td><td>"+checks[i].sremark+"</td><td>"+checks[i].stime+"</td></tr>";
							$("#pmp_rsmt_resourceDownCheck_preRemarkTable").append(trHtml);
						}
					}
				},
				error : function(XMLHttpRequest, textStatus) {
					$.aede.comm.toastr.error("与后台交互失败！");
				}
			});
			$("#pmp_rsmt_resourceDownCheck_preRemarkModal").modal("show");

		},

		//审核按钮点击事件
		showCheckModal:function(id, processType, orgId){
			//console.info(id);
			//console.info(processType);
			//console.info(orgId);
			$("#pmp_rsmt_resourceDownCheck_checkStatus").val("");
			$("#pmp_rsmt_resourceDownCheck_checkRemark").val("");
			$("#pmp_rsmt_resourceDownCheck_btnSave").unbind("click");
			$("#pmp_rsmt_resourceDownCheck_btnSave").click(function(){
				var statusOption = $("#pmp_rsmt_resourceDownCheck_checkStatus").val();
				var status = "1";
				var roleNames = $.aede.expand.comm.loginUser.roleNames;
				var re1 = /.项目经理/;
				var re2 = /.地区管理员/;
				var re3 = /.集团管理员/;
				if (re3.test(roleNames)) {
					if (statusOption == "1") {
						status = "6";
					} else {
						status = "7";
					}
				} else if (re2.test(roleNames)) {
					if (statusOption == "1") {
						status = "4";
					} else {
						status = "5";
					}
				} else if (re1.test(roleNames)) {
					if (statusOption == "1") {
						status = "2";
					} else {
						status = "3";
					}
				}
				var sid = $.aede.expand.comm.loginUser.userId;
				var remark = $("#pmp_rsmt_resourceDownCheck_checkRemark").val();
				$.ajax({
					url : $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDownCheck/checkApplication",
					dataType : "json",
					data : {"id":id,"sid":sid,"status":status,"remark":remark},
					type : "POST",
					async : false,
					success : function(data, textStatus) {
						if (data.responseCode == "1") {
							var resourceDownId = data.datas;
							$.aede.comm.toastr.success("审核成功");
							$("#pmp_rsmt_resourceDownCheck_modal").modal("hide");
							$.aede.pmp.rsmt.resourceDownCheck.initTable();
							if (processType == "1") {
								if (status == "2") {
									var myName = $.aede.expand.comm.loginUser.realName;
									var content = "项目经理【" + myName + "】已审核通过 请您审批";
									//$.aede.pmp.comm.mail.sendAreaAdmins("资源下载申请", content, orgId);
									$.aede.pmp.rsmt.resourceDownCheck.sendMessageToAreaAdmins(orgId, content, resourceDownId);//发送消息
								}
							}
							if (processType == "2") {
								if (status == "2") {
									var myName = $.aede.expand.comm.loginUser.realName;
									var content = "项目经理【" + myName + "】已审核通过 请您审批";
									//$.aede.pmp.comm.mail.sendAreaAdmins("资源下载申请", content, orgId);
									$.aede.pmp.rsmt.resourceDownCheck.sendMessageToAreaAdmins(orgId, content, resourceDownId);//发送消息
								} else if (status == "4") {
									var myName = $.aede.expand.comm.loginUser.realName;
									var content = "地区管理员【" + myName + "】已审核通过 请您审批";
									//$.aede.pmp.comm.mail.sendSuperAdmins("资源下载申请", content);
									$.aede.pmp.rsmt.resourceDownCheck.sendMessageToSuperAdmins(content, resourceDownId);//发送消息
								}
							}
						}
					},
					error : function(XMLHttpRequest, textStatus) {
						$.aede.comm.toastr.error("与后台交互失败！");
					}
				});
			});
			$("#pmp_rsmt_resourceDownCheck_modal").modal("show");

		},

		//初始化阶段下拉列表
		initStageSelect : function () {
			$.ajax({
				url : $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/findProjectStageList",
				dataType : "json",
				type : "POST",
				async : false,
				success : function(data, textStatus) {
					if (data.responseCode == "1") {
						var list = data.datas;
						var str = "<option value=''>---请选择阶段---</option>";
						for (var i=0; i<list.length; i++) {
							var value = list[i].sid;
							var text = list[i].sname;
							str += "<option value='"+value+"'>"+text+"</option>";
						}
						$("#pmp_rsmt_resourceDownCheck_stage").html(str);
					}
				},
				error : function(XMLHttpRequest, textStatus) {
					$.aede.comm.toastr.error("与后台交互失败！");
				}
			});
		},

		//发送申请消息给地区管理员(orgId:当前机构ID message:发送的消息内容)
		sendMessageToAreaAdmins : function(orgId, message,resourceDownId){
			var messagesParams={
				messageList:[]
			};
			//查找该项目所属区域的所有区域管理员
			$.ajax({
				url : $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDownCheck/findAreaAdmins",
				dataType : "json",
				data : {"orgId":orgId},
				type : "POST",
				async : false,
				success : function(data, textStatus) {
					var admins = data.datas;
					for (var i=0; i<admins.length; i++) {
						var messagesParam={
							"fromUser":{
								id:$.aede.expand.comm.getLoginUserId(),
								realname:$.aede.expand.comm.loginUser.realname
							},
							"toUser":{id:admins[i].id},
							"message":message,
							"url":"/management/pmp/rsmt/resourceDownCheck/"+resourceDownId+"/list",
							"tabName" : "资源申请审核",
							"tabId" : "resourceTab"
						};
						messagesParams.messageList.push(messagesParam);
					}
					$.aede.mq.sys.message.messageMt.sendMessages(messagesParams);
				},
				error : function(XMLHttpRequest, textStatus) {
					$.aede.comm.toastr.error("与后台交互失败！");
				}
			});
		},

		//发送申请消息给超级管理员(message:发送的消息内容)
		sendMessageToSuperAdmins : function(message, resourceDownId){
			var messagesParams={
				messageList:[]
			};
			//查找所有超级管理员
			$.ajax({
				url : $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDownCheck/findSuperAdmins",
				dataType : "json",
				type : "POST",
				async : false,
				success : function(data, textStatus) {
					var admins = data.datas;
					for (var i=0; i<admins.length; i++) {
						var messagesParam={
							"fromUser":{
								id:$.aede.expand.comm.getLoginUserId(),
								realname:$.aede.expand.comm.loginUser.realname
							},
							"toUser":{id:admins[i].id},
							"message":message,
							"url":"/management/pmp/rsmt/resourceDownCheck/"+resourceDownId+"/list",
							"tabName" : "资源申请审核",
							"tabId" : "resourceTab"
						};
						messagesParams.messageList.push(messagesParam);
					}
					$.aede.mq.sys.message.messageMt.sendMessages(messagesParams);
				},
				error : function(XMLHttpRequest, textStatus) {
					$.aede.comm.toastr.error("与后台交互失败！");
				}
			});
		}
	}
})(jQuery);