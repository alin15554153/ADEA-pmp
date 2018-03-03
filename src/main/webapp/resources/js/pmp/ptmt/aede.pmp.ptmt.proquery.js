var stage=0;
var resultArray = new Array();
var rpid;
var limit=0;
(function($){
    $.aede.pmp.ptmt.proquery = {
        proqueryTable : "",//项目表格
        proqueryresourceTable: "",//项目资源表格
        init : function() {
            $.aede.pmp.ptmt.proquery.initBtnListener();
            $("#pmp_ptmt_proquery_btnSearch").click();
        },
        initBtnListener:function(){
            //加载查询按钮事件
            $("#pmp_ptmt_proquery_btnSearch").click(function(){
                $.aede.pmp.ptmt.proquery.initProjectTable();
                return false;
            });
            //加载重置按钮事件
            $("#pmp_ptmt_proquery_btnReset").click(function(){
                $("#pmp_ptmt_proquery_query_name").val("");
                $("#pmp_ptmt_proquery_query_code").val("");
            });
        },
        //初始化数据表格
        initProjectTable : function(){
            if (!$.aede.pmp.ptmt.proquery.proqueryTable) {
                // 初始化proqueryTable
                $.aede.pmp.ptmt.proquery.proqueryTable = $("#pmp_ptmt_proquery_table").DataTable({
                    serverSide: true,
                    "iDisplayLength":50,
                    ajax: {
                        url: $.aede.expand.getContextPath() + '/management/pmp/ptmt/proquery/queryByPaging',
                        type: 'POST',
                        dataType: 'json',
                        data: function(d) {
                            var name = $.trim($("#pmp_ptmt_proquery_query_name").val());
                            var code = $.trim($("#pmp_ptmt_proquery_query_code").val());
                            var type = $.trim($("#pmp_ptmt_proquery_query_type").val());
                            var status = $.trim($("#pmp_ptmt_proquery_query_status").val());
                            if (!!name) {
                                d.search_LIKE_name = name;
                            }
                            if (!!code) {
                                d.search_LIKE_code = code;
                            }
                            if(!!type){
                                d.search_EQ_type = type;
                            }
                            if(!!status){
                                d.search_EQ_status = status;
                            }
                            d.search_EQ_delTag = "0";
                        }
                    },
                    columns: [
                        {
                        	data: "code"
                        },
                        {
                            data: "name"
                        },
                        {
                            data: "webUser.realname"
                        },
                        {
                            data: "progress"
                        },
                        {
                            data: "levelName"
                        },
                        {
                            data: "amt"
                        },
                        {
                            data: "statusName"
                        },
                        {
                            data: "stageName"
                        },
                        {
                            data: "id"
                        }
                    ],
                    columnDefs: [
                                 {
                                 	"targets" : [1],
                                 	"render" : function(data,event,full) {
                                 		var newData=data;
										if(newData.length>27)
											newData=newData.substring(0,25)+"...";
                                        if(full.type==1){
                                            return "<button type=\"button\" class=\"btn btn-info btn-xs\">自建</button> <span title=\"" + data + "\">" + newData + "</span>";
                                        }else{
                                            return "<span title=\"" + data + "\">" + newData + "</span>";
                                        }
                                 	}
                                 },{
                                 	"targets" : [3],
                                 	"render" : function(data,event,full) {
                                        return '<span title=' + data + '%>' + data + '%<div class="progress progress-mini"><div style="width: ' + data + '%;" class="progress-bar"></div></div></span>';
                                    }
                                 },{
                                	 "targets" : [8],
                                	 "render" : function(data,event,full) {
                                		 return '<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.proquery.showProjectinfoModallimit(\''+data+'\');">查看</button>' ;
                                	 }
                                 }
                                 ]
                });
            } else {
                $.aede.pmp.ptmt.proquery.proqueryTable.ajax.reload();
            }
        },
       //初始化数据表格，资源
        initProjectResourceTable : function(){
        	$("#pmp_ptmt_proquery_resource_query_pid").val(rpid);
        	$("#pmp_ptmt_proquery_resource_query_stage").val(stage);
            if (!$.aede.pmp.ptmt.proquery.proqueryresourceTable) {
            	//ppid = $("#pmp_ptmt_proquery_id").val();
                // 初始化Table
                $.aede.pmp.ptmt.proquery.proqueryresourceTable = $("#pmp_ptmt_proquery_resourcetable").DataTable({
                    serverSide: true,
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
                                		 return '<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.proquery.showProjectResourceEditModal(\''+data+'\');">下载</button>' ;
                                	}
                                 }
                                 ]
                });
            } else {
                $.aede.pmp.ptmt.proquery.proqueryresourceTable.ajax.reload();
            }
        },
        
        // 初始化模式窗口
        initModal : function() {
            // 清空模式窗口表单元素值
            $.aede.pmp.ptmt.proquery.cleanModalFormEleVal();
            // 清除表单验证样式
            //$.aede.pmp.ptmt.proquery.cleanModalFormValidateStyle();
            
            // 清空保存按钮点击事件
            $("#pmp_ptmt_proquery_modal_btnSave").unbind("click");
            // 显示保存按钮
            $('#pmp_ptmt_proquery_modal_btnSave').show();
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
                        $("#pmp_ptmt_proquery_manager").val(result.manager);
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
                        var pernames = result.pernames;
                        $("#pmp_ptmt_proquery_pername").val(pernames);
                        rpid = result.id;
                        $.aede.pmp.ptmt.proquery.initStageTab(rpid);
                        $.aede.pmp.ptmt.proquery.initProjectResourceTable();
                        
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
        //显示项目查看
        showProjectinfoModallimit : function(id) {
        	$.aede.comm.addTab($.aede.expand.getContextPath() +"/management/pmp/ptmt/proquery/"+id+"/viewlistlimit","项目查看","projec_viewinfolimit");
        },
        //显示项目编辑模式窗口
        showProjectinfoModal : function(id) {
        	$.aede.comm.addTab($.aede.expand.getContextPath() +"/management/pmp/ptmt/proquery/"+id+"/viewlist","项目详情","projec_viewinfo");
        },
        //阶段tab加载
        initStageTab : function(id) {
            $.ajax({
                type : "GET",
                async : false,// 同步请求
                url : $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + id + "/querytab",
                dataType : "json",
                success : function(data) {
                    if (data.length > 0) {
                    	$('#tt').show();
                    	for(var i=0;i<data.length;i++){
                    		if(i==0){
                    			stage = data[0].sid;	
                    		}
                    		resultArray[i]=new Array(); 
                    		addTab($.aede.expand.comm.getDictName('阶段名称',data[i].sname),data[i].sseq+1);	
                    		resultArray[i][0]=data[i].sid;
                    		resultArray[i][1] = $.aede.expand.comm.getDictName('阶段名称',data[i].sname);
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
        }
       
    }
})(jQuery);
$(document).ready(function () {
    $.aede.pmp.ptmt.proquery.init();
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
			$.aede.pmp.ptmt.proquery.initProjectResourceTable(); 	
		}
	}); 
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



