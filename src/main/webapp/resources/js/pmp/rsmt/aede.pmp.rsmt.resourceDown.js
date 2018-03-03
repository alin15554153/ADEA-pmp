$(document).ready(function () {
	$.aede.pmp.rsmt.resourceDown.initBtnListener();
	//$.aede.pmp.rsmt.resourceDown.initStageSelect();
	$.aede.pmp.rsmt.resourceDown.initTable();
});

(function($) {
	$.aede.pmp.rsmt.resourceDown = {
		table:"",
		initTable : function() {
			if (!$.aede.pmp.rsmt.resourceDown.table) {
				$.aede.pmp.rsmt.resourceDown.table = $('#pmp_rsmt_resourceDown_table').DataTable({
					serverSide: true,
					"iDisplayLength":50,
					ajax: {
						url: $.aede.expand.getContextPath() + '/management/pmp/rsmt/resourceDown/findApplicationList',
						type: 'POST',
						dataType: 'json',
						data: function(d) {
							var resourceName = $.trim($("#pmp_rsmt_resourceDown_resourceName").val());//资源名称
							var resourceType = $.trim($("#pmp_rsmt_resourceDown_resourceType").val());//资源类别
							var stage = $.trim($("#pmp_rsmt_resourceDown_stage").val());//阶段
							var projectName = $.trim($("#pmp_rsmt_resourceDown_projectName").val());//项目名称
							var uid = $.aede.expand.comm.loginUser.userId;//当前登陆用户ID
							if (!!resourceName) {
								d["search_LIKE_resource.name"] = resourceName;
							}
							if (!!resourceType) {
								d["search_LIKE_resource.type"] = resourceType;
							}
							if (!!stage) {
								d["search_EQ_resource.projectStage.sname"] = stage;
							}
							if (!!projectName) {
								d["search_LIKE_resource.project.name"] = projectName;
							}
							d.search_EQ_uid = uid;
						}
					},
					columns: [
						{
							data: "resource.name"
						},
						{
							data: "resource.type"
						},
						{
							data: "resource.project.name"
						},
						{
							data: "atime"
						},
						{
							data: "aremark"
						},
						{
							data: "status"
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
								if(newData.length > 10){
									newData=newData.substring(0,11)+"...";
								}
								return "<span title=\""+data+"\">"+newData+"</span>";
							}
						},
						{
							"targets" : [1],
							"render" : function(data,event,full) {
								return $.aede.expand.comm.getDictName('资源类别',data);
							}
						},
						{
							"targets" : [2],
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
							"targets" : [4],
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
							"targets" : [5],
							"render" : function(data,event,full) {
								if (full.processType == "1"){//PM==>MG
									if (data == "1"){
										return "未审核";
									} else if (data == "4"){
										return "审核通过";
									} else if (data == "3" || data == "5"){
										return "未通过";
									} else if (data == "2") {
										return "审核中";
									} else {
										return "未知";
									}
								} else if (full.processType == "2"){//PM==>MG==>SMG
									if (data == "1"){
										return "未审核";
									} else if (data == "6"){
										return "审核通过";
									} else if (data == "3" || data == "5" || data == "7"){
										return "未通过";
									} else if (data == "2" || data == "4") {
										return "审核中";
									} else {
										return "未知";
									}
								} else {
									return "无须审核";
								}
							}
						},
						{
							"targets" : [6],
							"render" : function(data,event,full) {
								var resourceId = full.resource.id;
								var manager = full.resource.project.manager.id;
								var status = full.status;
								//var link = $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/"+full.resource.path+"/download";
								var link = $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/"+resourceId+"/dl";
								if (full.processType == "1"){//PM==>MG
									var html = "";
									if (status == "4" || status == "3" || status == "5"){
										html += "<button type='button' class='btn btn-primary btn-xs' onclick='$.aede.pmp.rsmt.resourceDown.showResultModal(\""+data+"\",\""+full.status+"\");'>查看结果</button>&nbsp;&nbsp;";
									}
									if(status == "4"){
										html += "<a type='button' class='btn btn-primary btn-xs btn-dl' href=" + link + " rid='"+resourceId+"' target='_blank' onclick='$.aede.pmp.rsmt.resourceDown.addDownloadTimes(\""+resourceId+"\");'>下载</a>";
									} else if (status == "3" || status == "5"){
										html += "<button type='button' class='btn btn-primary btn-xs' onclick='$.aede.pmp.rsmt.resourceDown.reSendApplication(\""+data+"\",\""+manager+"\");'>重新申请</button>";
									}
									return html;
								} else if (full.processType == "2"){//PM==>MG==>SMG
									var html = "";
									if (status == "6" || status == "3" || status == "5" || status == "7"){
										html += "<button type='button' class='btn btn-primary btn-xs' onclick='$.aede.pmp.rsmt.resourceDown.showResultModal(\""+data+"\",\""+full.status+"\");'>查看结果</button>&nbsp;&nbsp;";
									}
									if(status == "6"){
										html += "<a type='button' class='btn btn-primary btn-xs btn-dl' href=" + link + " rid='"+resourceId+"' target='_blank' onclick='$.aede.pmp.rsmt.resourceDown.addDownloadTimes(\""+resourceId+"\");'>下载</a>";
									} else if (status == "3" || status == "5" || status == "7"){
										html += "<button type='button' class='btn btn-primary btn-xs' onclick='$.aede.pmp.rsmt.resourceDown.reSendApplication(\""+data+"\",\""+manager+"\");'>重新申请</button>";
									}
									return html;
								} else {
									return null;
								}

							}
						}
					]
				});
			} else {
				$.aede.pmp.rsmt.resourceDown.table.ajax.reload();
			}
		},

		initBtnListener:function(){
			//加载查询按钮事件
			$("#pmp_rsmt_resourceDown_btnSearch").click(function(){
				$.aede.pmp.rsmt.resourceDown.initTable();
				return false;
			});
			//加载重置按钮事件
			$("#pmp_rsmt_resourceDown_btnReset").click(function(){
				$("#pmp_rsmt_resourceDown_resourceName").val("");
				$("#pmp_rsmt_resourceDown_resourceType").val("");
				$("#pmp_rsmt_resourceDown_stage").val("");
				$("#pmp_rsmt_resourceDown_projectName").val("");
			});
		},

		//查看结果按钮点击事件
		showResultModal:function(id, status){
			$("#pmp_rsmt_resourceDown_checkPersonName").val("");
			$("#pmp_rsmt_resourceDown_checkTime").val("");
			$("#pmp_rsmt_resourceDown_checkStatus").val("");
			$("#pmp_rsmt_resourceDown_checkRemark").val("");
			$.ajax({
				url : $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/findCheckById",
				dataType : "json",
				data : {"id":id},
				type : "POST",
				async : false,
				success : function(data, textStatus) {
					if (data.responseCode == "1") {
						var resourceDownCheck = data.datas;
						if(resourceDownCheck != null){
							$("#pmp_rsmt_resourceDown_checkPersonName").val(resourceDownCheck.sname);
							$("#pmp_rsmt_resourceDown_checkTime").val(resourceDownCheck.stime);
							if (status == "4" || status == "6") {
								$("#pmp_rsmt_resourceDown_checkStatus").val("审核通过");
							} else{
								$("#pmp_rsmt_resourceDown_checkStatus").val("未通过");
							}
							$("#pmp_rsmt_resourceDown_checkRemark").val(resourceDownCheck.sremark);
						}
					}
				},
				error : function(XMLHttpRequest, textStatus) {
					$.aede.comm.toastr.error("与后台交互失败！");
				}
			});
			$("#pmp_rsmt_resourceDown_modal").modal("show");

		},
		
		//重新申请下载(id:resourceDownID manager:项目经理ID)
		reSendApplication : function (id, manager) {
			//console.info(id);
			//console.info(manager);

			$("#pmp_rsmt_resourceDown_remark").val("");//申请内容
			$("#pmp_rsmt_resourceDown_btnSave").unbind("click");
			$("#pmp_rsmt_resourceDown_btnSave").click(function(){
				var remark = $("#pmp_rsmt_resourceDown_remark").val();
				$.ajax({
					url : $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/reSendApplication",
					dataType : "json",
					data : {"id":id,"remark":remark},
					type : "POST",
					async : false,
					success : function(data, textStatus) {
						if (data.responseCode == "1") {
							var resourceDownId = data.datas;//申请ID
							$.aede.comm.toastr.success("申请成功!");

							$("#pmp_rsmt_resourceDown_modal2").modal("hide");

							$.aede.pmp.rsmt.resourceDown.initTable();

							var myName = $.aede.expand.comm.loginUser.realName;
							var content = "【" + myName + "】申请资源 请审核";

							//$.aede.pmp.comm.mail.sendPTP("资源下载申请", content, manager, 4);//发送邮件

							$.aede.pmp.rsmt.resourceDown.sendMessageToPM(manager, content, resourceDownId);//发送消息

						}
					},
					error : function(XMLHttpRequest, textStatus) {
						$.aede.comm.toastr.error("与后台交互失败!");
					}
				});
			});
			$("#pmp_rsmt_resourceDown_modal2").modal("show");
		},
		
		//增加下载次数
		addDownloadTimes : function (rid) {
			//console.info(rid);
			$.ajax({
				url : $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/addDownloadTimes",
				dataType : "json",
				data : {"rid":rid},
				type : "POST",
				async : false,
				success : function(data, textStatus) {
				},
				error : function(XMLHttpRequest, textStatus) {
					$.aede.comm.toastr.error("与后台交互失败!");
				}
			});
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
						$("#pmp_rsmt_resourceDown_stage").html(str);
					}
				},
				error : function(XMLHttpRequest, textStatus) {
					$.aede.comm.toastr.error("与后台交互失败！");
				}
			});
		},

		//发送申请消息给项目经理(targetId:接收方ID message:发送的消息内容)
		sendMessageToPM : function (targetId, message, resourceDownId) {
			var messageParam = [];
			messageParam.push({
				"name" : "toUser.id",
				"value" : targetId
			});
			messageParam.push({
				"name" : "message",
				"value" : message
			});
			messageParam.push({
				"name" : "url",
				"value" : "/management/pmp/rsmt/resourceDownCheck/"+resourceDownId+"/list"
			});
			messageParam.push({
				"name" : "tabName",
				"value" : "资源申请审核"
			});
			messageParam.push({
				"name" : "tabId",
				"value" : "resourceTab"
			});
			$.aede.mq.sys.message.messageMt.sendMessagePTP(messageParam);
		}
	}
})(jQuery);