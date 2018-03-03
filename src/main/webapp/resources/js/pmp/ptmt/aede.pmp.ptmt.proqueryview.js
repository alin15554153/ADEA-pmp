var stage=0;
var resultArray = new Array();//项目阶段
var rpid;//项目编号
var rsmt_resourceDown_myProjectIdsArray = new Array();//当前用户所属的项目ID集合
var rsmt_resourceDown_areaCode;//当前用户所属的区域
(function($){
    $.aede.pmp.ptmt.proqueryview = {
        proqueryresourceTable: "",//项目资源表格
        followTable:"",//项目跟踪表
        init : function() {
            //$.aede.pmp.ptmt.proqueryview.initBtnListener();
            //$("#pmp_ptmt_proquery_btnSearch").click();
        	$.aede.pmp.ptmt.proqueryview.showProjectinfoModal();
        },

       //初始化数据表格，资源
        initProjectResourceTable : function(){
        	$("#pmp_ptmt_proquery_resource_query_pid").val(rpid);
        	$("#pmp_ptmt_proquery_resource_query_stage").val(stage);
            if (!$.aede.pmp.ptmt.proqueryview.proqueryviewresourceTable) {
            	//ppid = $("#pmp_ptmt_proquery_id").val();
                // 初始化Table
                $.aede.pmp.ptmt.proqueryview.proqueryviewresourceTable = $("#pmp_ptmt_proquery_resourcetable").DataTable({
                    serverSide: true,
                    "iDisplayLength":50,
                    ajax: {
                        url: $.aede.expand.getContextPath() + "/management/pmp/rsmt/resource/queryByPaging",
                        type: 'POST',
                        dataType: 'json',
                        data: function(d) {
                        	var pid = $("#pmp_ptmt_proquery_resource_query_pid").val();
                        	var stage = $("#pmp_ptmt_proquery_resource_query_stage").val();
                            if (!!pid) {
								d.search_EQ_pid = pid;
                            }
                            if (!!stage) {
								d.search_EQ_sid = stage;
                            }
                        }
                    },
                    columns: [
                        {
                        	data: "name"
                        },      
                        {
                        	data: "type"
                        },
                        {
                        	data: "webUser.realname"
                        },
                        {
                        	data: "uploaddate"
                        },
                        {
                            data: "id"
                        }
                    ],
                    columnDefs: [
								{
                              		"targets" : [1],
                              		"render" : function(data,event,full) {
                              			return $.aede.expand.comm.getDictName('资源类别',data);
                              		}
                                 },{
                                 	"targets" : [3],
                                 	"render" : function(data,event,full) {
                                 		return GetDateT(data);
                                 	}
                                 },{
                                	 "targets" : [4],
                                	 "render" : function(data,event,full) {
                                		// return '<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.proqueryview.showProjectResourceEditModal(\''+data+'\');">下载</button>' ;
                                		 var respid = full.pid;//该资源所属项目ID
                                         var applyHtml = "<button type='button' class='btn btn-primary btn-xs' onclick='$.aede.pmp.ptmt.proqueryview.sendApplication(\"" + data + "\",\"" + full.project.webArea.id + "\");'>申请</button>";
                                         //var link = $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/"+full.path+"/download";
                                         var link = $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/"+data+"/dl";
                                         var downloadHtml = "<a type='button' class='btn btn-primary btn-xs' href=" + link + " target='_blank'>下载</a>";

                                         if (rsmt_resourceDown_myProjectIdsArray.indexOf(respid) < 0) {
                                             return applyHtml;
                                         } else {
                                             return downloadHtml;
                                         }
                                	 }
                                 }
                                 ]
                });
            } else {
                $.aede.pmp.ptmt.proqueryview.proqueryviewresourceTable.ajax.reload();
            }
        },

        initFollowTable:function(){
            if (!$.aede.pmp.ptmt.proqueryview.followTable) {
                $.aede.pmp.ptmt.proqueryview.followTable = $("#pmp_ptmt_proquery_followtable").DataTable({
                    serverSide: true,
                    ajax: {
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/projectFollow/queryByPaging",
                        type: 'POST',
                        dataType: 'json',
                        data: function(d) {
                            var pid = $("#pmp_ptmt_proquery_proid").val();
                                d.search_EQ_pid = pid;
                        }
                    },
                    columns: [
                        {
                            data: "content"
                        },
                        {
                            data: "username"
                        },
                        {
                            data: "followTime"
                        },
                        {
                            data: "id"
                        }
                    ],
                    columnDefs:[{
                       "targets":[3],
                        "render":function(data,event,full) {
                            return "";
                        }
                    }],
                });
            }else{
                $.aede.pmp.ptmt.proqueryview.followTable.ajax.reload();
            }
        },
        
        // 初始化模式窗口
        initModal : function() {
            // 清空模式窗口表单元素值
            $.aede.pmp.ptmt.proqueryview.cleanModalFormEleVal();
            // 清除表单验证样式
            //$.aede.pmp.ptmt.proqueryview.cleanModalFormValidateStyle();
            
            // 清空保存按钮点击事件
            //$("#pmp_ptmt_proquery_modal_btnSave").unbind("click");
            //$("#pmp_ptmt_proquerystage_modal_btnSave").unbind("click");
            // 显示保存按钮
            //$('#pmp_ptmt_proquery_modal_btnSave').show();
            //$('#pmp_ptmt_proquerystage_modal_btnSave').show();
            // 清除disabled样式
            $("#pmp_ptmt_proquery_modal form input").attr("disabled",true);
            $("#pmp_ptmt_proquery_modal form textarea").attr("disabled",true);
            $("#pmp_ptmt_proquery_type").attr("disabled",true);
            $("#pmp_ptmt_proquery_nature").attr("disabled",true);
            $("#pmp_ptmt_proquery_level").attr("disabled",true);
            $("#pmp_ptmt_proquery_status").attr("disabled",true);
        },
        // 清空模式窗口表单元素值
        cleanModalFormEleVal : function() {
            // 清空隐藏域
            $("#pmp_ptmt_proquery_id").val("");
            stage=0;//初始化
            closeAll();
            resultArray.length=0;
            $("#pmp_ptmt_proquery_modal form")[0].reset();// 重置表单
        },
        
        //初始化模式窗口表单信息
        initModalFormEleValById : function(id) {
            $.ajax({
                type : "GET",
                async : false,// 同步请求
                url : $.aede.expand.getContextPath() + "/management/pmp/ptmt/proquery/" + id + "/view",
                dataType : "json",
                success : function(data) {
                    if (data["responseCode"] == "1") {
                        var result = data["datas"];
                        $("#pmp_ptmt_proquery_name").val(result.name);
                        $("#pmp_ptmt_proquery_type").val(result.type);
                        $("#pmp_ptmt_proquery_code").val(result.code);
                        $("#pmp_ptmt_proquery_nature").val(result.nature);
                        $("#pmp_ptmt_proquerystage_ctime").val(result.ctime);
                        $("#pmp_ptmt_proquery_manager").val(result.manager.realname);
                        $("#pmp_ptmt_proquery_managerid").val(result.manager.id);
                        $("#pmp_ptmt_proquery_cid").val(result.cid);
                        $("#pmp_ptmt_proquery_director").val(result.director);
                        $("#pmp_ptmt_proquery_checker").val(result.checker);
                        $("#pmp_ptmt_proquery_level").val(result.level);
                        $("#pmp_ptmt_proquery_progress").val(result.progress);
                        $("#pmp_ptmt_proquery_amt").val(result.amt);
                        $("#pmp_ptmt_proquery_status").val(result.status);
                        $("#pmp_ptmt_proquery_remark").val(result.remark);
                        $("#pmp_ptmt_proquery_id").val(result.id);
                        $("#pmp_ptmt_proquery_perid").val(result.perids);
                        $("#pmp_ptmt_proquery_source").val(result.projectSource);
                        $("#pmp_ptmt_proquery_stakeholder").val(result.stakeholder);
                        $("#pmp_ptmt_proquery_signingProbability").val(result.signingProbability);
                        var pernames = result.pernames;
                        $("#pmp_ptmt_proquery_pername").val(pernames);
                        rpid = result.id;
                        $.aede.pmp.ptmt.proqueryview.initStageTab(rpid);
                        $.aede.pmp.ptmt.proqueryview.initProjectResourceTable();
                        $.aede.pmp.ptmt.proqueryview.initFollowTable();
                        
                    } else {
                        $.aede.comm.toastr.error(data["responseMsg"]);
                    }
                    return false;
                },
                error : function() {
                    $.aede.comm.toastr.error("请求异常，查询项目失败！");
                }
            });
        },
        //显示项目编辑模式窗口
        showProjectinfoModal : function() {
            //$("#pmp_ptmt_proquery_modal .modal-header .modal-title").html("项目详情");
            // 初始化模式窗口
            $.aede.pmp.ptmt.proqueryview.initModal();
            var id = $("#pmp_ptmt_proquery_proid").val();
            if (!!id) {
                $.aede.pmp.ptmt.proqueryview.initModalFormEleValById(id);
                //tab加载在数据加载里
                // 初始化保存编辑角色维护按钮的click事件(该按钮的其他点击事件已经在initModal时被清除)
                //$.aede.pmp.ptmt.proqueryview.initModalBtnClickListener();
                //$("#pmp_ptmt_proquery_modal").modal("show");
            } else {
                $.aede.comm.toastr.error("项目的id丢失，无法查询！");
            }
        },
        //阶段tab加载
        initStageTab : function(id) {
        	var limitquery = $("#pmp_ptmt_proquery_limit").val();
        	ajaxurl = $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + id + "/querytab";
        	if(limitquery == "1"){//限制阶段展示
        		ajaxurl = $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + id + "/querytablimit";
        	}
            $.ajax({
                type : "GET",
                async : false,// 同步请求
                url : ajaxurl, 
                dataType : "json",
                success : function(data) {
                    if (data.length > 0) {
                    	$('#tt').show();
                    	for(var i=0;i<data.length;i++){
                    		if(i==0){
                    			stage = data[0].sid;	
                    		}
                    		resultArray[i]=new Array(); 
                    		addTab(data[i].stageName,data[i].sseq+1);
                    		//addTab($.aede.expand.comm.getDictName('阶段名称',data[i].sname),data[i].sseq+1);	
                    		resultArray[i][0]=data[i].sid;
                    		resultArray[i][1] = data[i].stageName;
                    		//resultArray[i][1] = $.aede.expand.comm.getDictName('阶段名称',data[i].sname);
                    		$('#pmp_ptmt_proquery_resourcetablediv').show();
                    	}
                    } else {
                    	$('#tt').hide();
                    	$('#pmp_ptmt_proquery_resourcetablediv').hide();
                    }
                    return false;
                },
                error : function() {
                    $.aede.comm.toastr.error("tab加载异常！");
                }
            });
        },
        //获取当前用户所在的所有项目ID数组
        getMyProjectIds : function(){
            var userid = $.aede.expand.comm.loginUser.userId;
            $.ajax({
                url : $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/getMyProjectIds",
                dataType : "json",
                data : {"userid":userid},
                type : "POST",
                async : false,
                success : function(data, textStatus) {
                    if (data.responseCode == "1") {
                        if (data.datas != null && data.datas != "") {
                            rsmt_resourceDown_myProjectIdsArray = data.datas;
                        } else {
                            rsmt_resourceDown_myProjectIdsArray = new Array();
                        }
                    }
                },
                error : function(XMLHttpRequest, textStatus) {
                    $.aede.comm.toastr.error("与后台交互失败!");
                }
            });
        },
      //获取当前用户所属区域ID
        getMyAreaId : function(){
            var userid = $.aede.expand.comm.loginUser.userId;
            $.ajax({
                url : $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/getMyAreaId",
                dataType : "json",
                data : {"userid":userid},
                type : "POST",
                async : false,
                success : function(data, textStatus) {
                    if (data.responseCode == "1") {
                        if (data.datas != null && data.datas != "") {
                            rsmt_resourceDown_areaCode = data.datas;
                        } else {
                            rsmt_resourceDown_areaCode = "";
                        }
                    }
                },
                error : function(XMLHttpRequest, textStatus) {
                    $.aede.comm.toastr.error("与后台交互失败!");
                }
            });
        },
      //申请按钮点击事件(rid:资源ID areaId:项目区域ID)
        sendApplication : function (rid, areaId){
            $("#pmp_rsmt_resourceForView_remark").val("");//申请内容
            $("#pmp_rsmt_resourceForView_btnSave").unbind("click");
            $("#pmp_rsmt_resourceForView_btnSave").click(function(){
                var uid = $.aede.expand.comm.loginUser.userId;//用户ID
                var remark = $("#pmp_rsmt_resourceForView_remark").val();
                var processType = "";
                if (areaId == rsmt_resourceDown_areaCode) {
                    processType = "1";
                } else {
                    processType = "2";
                }
                $.ajax({
                    url : $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/sendApplication",
                    dataType : "json",
                    data : {"rid":rid,"uid":uid,"remark":remark,"processType":processType},
                    type : "POST",
                    async : false,
                    success : function(data, textStatus) {
                        if (data.responseCode == "1") {
                            $.aede.comm.toastr.success("申请成功!");
                            var manager = $("#pmp_ptmt_proquery_managerid").val();
                            var myName = $.aede.expand.comm.loginUser.realName;
							var content = "【" + myName + "】申请资源 请审核";
							//资源申请，参数4
							//$.aede.pmp.comm.mail.sendPTP("资源下载申请", content, manager,4);//发送邮件
							$.aede.pmp.ptmt.proqueryview.sendMessageToPM(manager, content);//发送消息
                            $("#pmp_rsmt_resourceView_modal").modal("hide");
                        }
                    },
                    error : function(XMLHttpRequest, textStatus) {
                        $.aede.comm.toastr.error("与后台交互失败!");
                    }
                });
            });
            $("#pmp_rsmt_resourceView_modal").modal("show");
        },

		//发送申请消息给项目经理(targetId:接收方ID message:发送的消息内容)
		sendMessageToPM : function (targetId, message) {
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
				"value" : "/management/pmp/rsmt/resourceDownCheck/list"
			});
			messageParam.push({
				"name" : "tabName",
				"value" : "资源申请审核"
			});
			messageParam.push({
				"name" : "tabId",
				"value" : ""
			});
			$.aede.mq.sys.message.messageMt.sendMessagePTP(messageParam);
		}
       
       
    }
})(jQuery);
$(document).ready(function () {
    $.aede.pmp.ptmt.proqueryview.init();
    $('#tt').tabs({ 
    	plain:true,
		border:false, 
		//fit:true,
		onSelect:function(title){ 
			for(var i = 0; i< resultArray.length; i++) {
			    if(title==resultArray[i][1]){
			    	stage = resultArray[i][0];
			    }
			 }
			$.aede.pmp.ptmt.proqueryview.initProjectResourceTable(); 	
		}
	}); 
    $.aede.pmp.ptmt.proqueryview.getMyProjectIds();
    $.aede.pmp.ptmt.proqueryview.getMyAreaId();
});

function GetDateT(date)
{
 var d,s;
 d = new Date(date);
 s = d.getFullYear() + "-"; 
 s = s + (d.getMonth() + 1) + "-";//取月份
 s += d.getDate() + " ";         //取日期
// s += d.getHours() + ":";       //取小时
// s += d.getMinutes() + ":";    //取分
// s += d.getSeconds();         //取秒
 return(s);  
}

function addTab(title, url){
		$('#tt').tabs('add',{
			title:title,
			//content:content,
			closable:false
		});
}
function closeAll(){ //关闭全部
	 for(var i = 0; i< resultArray.length; i++) {
	     $('#tt').tabs('close',resultArray[i][1]);
	 }
}



