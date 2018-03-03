(function ($) {
    $.aede.pmp.main= {
    	adminmsgTable : "",// 待办事项
		init: function(){
			$.aede.pmp.main.initAdminmsgTable();
			$("a[name='more']").bind("click",function(){
				$.aede.comm.addTab($.aede.expand.getContextPath() +"/management/pmp/rsmt/resourceDownCheck/list","更多","moreTab");
			});
			$.aede.pmp.main.initChartsData();
		},
		table:"",
		initAdminmsgTable : function() {
			if (!$.aede.pmp.main.adminmsgTable) {
				$.aede.pmp.main.adminmsgTable = $('#adminmsgTable').DataTable({
					serverSide: true,
					dom : 'tr'  ,
					ajax: {
						url: $.aede.expand.getContextPath() + '/management/pmp/rsmt/resourceDownCheck/findUncheckedApplicationList',
						type: 'POST',
						dataType: 'json',
						data: function(d) {
							var myId = $.aede.expand.comm.loginUser.userId;

							var myRoleId = "";
							var roleNames = $.aede.expand.comm.loginUser.roleNames;
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
							"sClass":"myleft",
							"render" : function(data,event,full) {
								 var newData=data;
								 if(newData.length > 25){
									newData=newData.substring(0,25)+"...";
								 }
								 return "<div style='margin:5px 5px;'><span title=\""+data+"\" style='color:#666666'>"+newData+"</span></div>";
							}
						},
						{
							"targets" : [1],
							"render" : function(data,event,full) {
								 return "<div style='margin:5px 5px;'><span style='color:#666666'>"+data+"</span></div>";
							}
						},
						{
							"targets" : [2],
							"sClass":"myleft",
							"render" : function(data,event,full) {
								 var newData=data;
								 if(newData.length > 20){
									newData=newData.substring(0,20)+"...";
								 }
								 return "<div style='margin:5px 5px;'><span title=\""+data+"\" style='color:#666666'>"+newData+"</span></div>";
							}
						},
						{
							"targets" : [3],
							"render" : function(data,event,full) {
								 return "<div style='margin:5px 5px;'><span style='color:#666666'>"+data+"</span></div>";
							}
						},
						{
							"targets" : [4],
							"render" : function(data,event,full) {
								 return "<div style='margin:5px 5px;'><span style='color:#666666'>"+data+"</span></div>";
							}
						},
						{
							"targets" : [5],
							"sClass":"myleft",
							"render" : function(data,event,full) {
								 var newData=data;
								 if(newData.length > 10){
									newData=newData.substring(0,10)+"...";
								 }
								 return "<div style='margin:5px 5px;'><span title=\""+data+"\" style='color:#666666'>"+newData+"</span></div>";
							}
						},
						{
							"targets" : [6],
							"render" : function(data,event,full) {
								var preRemark = "<button type='button' class='btn btn-primary btn-xs' onclick='$.aede.pmp.main.showPreRemark(\""+data+"\");'>过往审核意见</button>&nbsp;&nbsp;";
								var checkHtml = "<button type='button' class='btn btn-primary btn-xs' onclick='$.aede.pmp.main.showCheckModal(\""+data+"\",\""+full.processType+"\",\""+full.orgId+"\");'>审核</button>";
								return preRemark + checkHtml;
							}
						}
					]
				});
			} else {
				$.aede.pmp.main.adminmsgTable.ajax.reload();
			}
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
							$.aede.pmp.main.initAdminmsgTable();
							if (processType == "1") {
								if (status == "2") {
									var myName = $.aede.expand.comm.loginUser.realName;
									var content = "项目经理【" + myName + "】已审核通过 请您审批";
									//$.aede.pmp.comm.mail.sendAreaAdmins("资源下载申请", content, orgId);
									$.aede.pmp.main.sendMessageToAreaAdmins(orgId, content, resourceDownId);//发送消息
								}
							}
							if (processType == "2") {
								if (status == "2") {
									var myName = $.aede.expand.comm.loginUser.realName;
									var content = "项目经理【" + myName + "】已审核通过 请您审批";
									//$.aede.pmp.comm.mail.sendAreaAdmins("资源下载申请", content, orgId);
									$.aede.pmp.main.sendMessageToAreaAdmins(orgId, content, resourceDownId);//发送消息
								} else if (status == "4") {
									var myName = $.aede.expand.comm.loginUser.realName;
									var content = "地区管理员【" + myName + "】已审核通过 请您审批";
									//$.aede.pmp.comm.mail.sendSuperAdmins("资源下载申请", content);
									$.aede.pmp.main.sendMessageToSuperAdmins(content, resourceDownId);//发送消息
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
		},

		//加载图表数据
		initChartsData:function(){
			var result;
			$.ajax({
				type: "POST",
				async: false,// 同步请求
				url: $.aede.expand.getContextPath() + '/management/pmp/slas/proBrowser/initCharts',
				dataType: "json",
				data: {
					"orgId": $.aede.expand.comm.loginUser.orgid
				},
				success: function (data) {
					result = data;
					return false;
				},
				error: function () {
					$.aede.comm.toastr.error("请求异常，查看项目信息失败！");
				}
			});
			
			$.aede.pmp.main.createColumnCharts(result);
			$.aede.pmp.main.createPieCharts(result);
		},

		//创建饼图
		createPieCharts:function(data){

			var index = 0;
			var colors = ["#7cb5ec", "#e87e04", "#90ed7d", "#f7a35c", "#8085e9", "#f15c80", "#e4d354", "#2b908f", "#f45b5b", "#91e8e1"];
			var pieChartDatas=[];//饼图数据

			var statusData = {};

			for (var i=0;i<data.length;i++) {
				if (!!statusData[data[i].statusName]) {
					statusData[data[i].statusName]++;
				} else {
					statusData[data[i].statusName] = 1;
				}

			}

			for (var status in statusData) {
				pieChartDatas.push({
					name: status,
					color: colors[index],
					y: statusData[status]
				});
				index = index>colors.length?0:++index;
			}

			$.aede.pmp.main.initPieCharts(pieChartDatas,"项目状态信息");

			pieChartDatas=[];//饼图数据
			statusData = {};
			index = 0;
			var yw_pieChartDatas = [];
			var y_count = 0;
			var w_count = 0;
			//初始化延误项目数据
			for (var i=0;i<data.length;i++) {
				if (!!data[i].projectstage) {
					var expendtime = data[i].projectstage.expendtime;
					var curDate = moment().format("YYYY-MM-DD");
					if (expendtime < curDate) {
						statusData['已延误'] = ++y_count;
					} else {
						statusData['未延误'] = ++w_count;
					}
				}
			}
			for (var status in statusData) {
				pieChartDatas.push({
					name: status,
					color: colors[index],
					y: statusData[status]
				});
				index = index>colors.length?0:++index;
			}
			$.aede.pmp.main.initYWPieCharts(pieChartDatas,"项目延误信息");
		},

		//创建柱状图
		createColumnCharts: function(result) {
			var index = 0;
			var colors = ["#7cb5ec", "#e87e04", "#90ed7d", "#f7a35c", "#8085e9", "#f15c80", "#e4d354", "#2b908f", "#f45b5b", "#91e8e1"];
			var A = function (name,len,color) {
				this.name = name;
				var a = new Array();
				for(var i=0;i<len;i++){
					a[i] = 0;
				}
				this.color = color;
				this.data = a;
			}

			var xData = [];
			var yData = [];
			var statusData = {};
			var statusArr = [];
			for (var i in result) {
				if(xData.indexOf(result[i].webOrganization.organname) < 0){
					xData.push(result[i].webOrganization.organname);
				}
			}

			for (var name in result) {
				if(statusArr.indexOf(result[name].statusName) < 0){
					var a = new A(result[name].statusName,xData.length,colors[index]);
					a.data[xData.indexOf(result[name].webOrganization.organname)] = 1;
					yData.push(a);
					statusArr.push(result[name].statusName);

				}else{
					if(yData[statusArr.indexOf(result[name].statusName)].data[xData.indexOf(result[name].webOrganization.organname)] == undefined){
						yData[statusArr.indexOf(result[name].statusName)].data[xData.indexOf(result[name].webOrganization.organname)] = 1;
					}else{
						yData[statusArr.indexOf(result[name].statusName)].data[xData.indexOf(result[name].webOrganization.organname)]++;
					}
				}
				index = index>colors.length?0:++index;
			}

			$.aede.pmp.main.initColumnCharts(xData, yData, "地区项目状态信息");
		},

		/*加载饼图
		 * @param datas:加载饼图的结果集
		 * 		 dict :加载标题用 取自下拉文本
		 * */
		initPieCharts:function(datas,dict){
			$('#main_pie_container').highcharts({
				chart: {
					plotBackgroundColor: null,
					plotBorderWidth: null,
					plotShadow: false,
					margin: [40, 40, 40, 40]
				},
				title: {
					text: dict+'统计图'
				},
				credits: {
					enabled: false
				},
				tooltip: {
					pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
				},
				plotOptions: {
					pie: {
						allowPointSelect: true,
						cursor: 'pointer',
						dataLabels: {
							enabled: false
						},
						showInLegend: true,
						events: {
							click: function (e) {
								var tabName = "项目一览【"+e.point.name+"】";
								var tabId = "ptmt_project_browse_page";
								var url = $.aede.expand.getContextPath() + "/management/pmp/slas/proBrowser/list?p=" + e.point.name;
								$.aede.comm.addTab(url,tabName,tabId);
							}
						}
					}
				},
				series: [{
					type: 'pie',
					name: '占比：',
					data: datas
				}]
			});
		},
		initYWPieCharts:function(datas,dict){
			$('#main_yw_pie_container').highcharts({
				chart: {
					plotBackgroundColor: null,
					plotBorderWidth: null,
					plotShadow: false,
					margin: [40, 40, 40, 40]
				},
				title: {
					text: dict+'统计图'
				},
				credits: {
					enabled: false
				},
				tooltip: {
					pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
				},
				plotOptions: {
					pie: {
						allowPointSelect: true,
						cursor: 'pointer',
						dataLabels: {
							enabled: false
						},
						showInLegend: true,
						events: {
							click: function (e) {
								var tabName = "项目一览【"+e.point.name+"】";
								var tabId = "ptmt_project_browse_page";
								var url = $.aede.expand.getContextPath() + "/management/pmp/slas/proBrowser/list?p=" + e.point.name;
								$.aede.comm.addTab(url,tabName,tabId);
							}
						}
					}
				},
				series: [{
					type: 'pie',
					name: '占比：',
					data: datas
				}]
			});
		},

		initColumnCharts: function(xDatas, datas,dict) {
			$('#main_colum_container').highcharts({
				chart: {
					type: 'column'
				},
				title: {
					text: dict+'统计图'
				},
				credits: {
					enabled: false
				},
				xAxis: {
					categories: xDatas
				},
				yAxis: {
					min: 0,
					title: {
						text: '项目数量'
					},
					stackLabels: {
						enabled: true,
						style: {
							fontWeight: 'bold',
							color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
						}
					}
				},
				legend: {
					align: 'right',
					x: -70,
					verticalAlign: 'top',
					y: 20,
					floating: true,
					backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColorSolid) || 'white',
					borderColor: '#CCC',
					borderWidth: 1,
					shadow: false
				},
				tooltip: {
					formatter: function() {
						return '<b>'+ this.x +'</b><br/>'+
							this.series.name +': '+ this.y +'<br/>'+
							'Total: '+ this.point.stackTotal;
					}
				},
				plotOptions: {
					column: {
						stacking: 'normal',
						cursor: 'pointer',
						dataLabels: {
							enabled: true,
							color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
						},
						events: {
							click: function (event) {
								var p = event.point.category + "_" + this.name;
								var tabName = "项目一览【"+p+"】";
								var tabId = "ptmt_project_browse_page";
								var url = $.aede.expand.getContextPath() + "/management/pmp/slas/proBrowser/list?p=" + p;
								$.aede.comm.addTab(url,tabName,tabId);
							}
						}
					}
				},
				series: datas
			});
		},
    }
})(jQuery);
$(document).ready(function () {
	$.aede.pmp.main.init();
});