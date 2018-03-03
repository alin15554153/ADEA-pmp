var grid;
(function($){
	//项目角色管理
    $.aede.pmp.ptmt.project.rolemanager = {
    	roleTable :"",//项目角色列表
    	linkuserTable:"",
    	proUserTable:"",
    	validateForm: "",//表单验证
    	//初始化页面按钮
		initBtnListener:function(){
 			//加载查询按钮事件
 			$("#ptmt_project_rolemanager_btnSearch").bind("click",function(){
 				//初始化项目角色列表
 				 $.aede.pmp.ptmt.project.rolemanager.initRoleTable();
 				return false;
 			});
 			//加载重置按钮事件
 			$("#ptmt_project_rolemanager-btnReset").bind("click",function(){
 				$("#ptmt_project_rolemanager_query_rname").val("");
 			});
 		/*	$("#save").bind("click",function(){
 			    $.aede.comm.closeTab();
 			});*/
 			
	 	},
	 	 //初始化用户数据表格
        initLinkuserTable : function(){
            if (!$.aede.pmp.ptmt.project.rolemanager.linkuserTable) {
                // 初始化linkuserTable
                $.aede.pmp.ptmt.project.rolemanager.linkuserTable = $("#ptmt_project_linkuser_table").DataTable({
                    serverSide: true,
                    ajax: {
                        url: $.aede.expand.getContextPath() + '/management/pmp/ptmt/project/rolemanager/query',
                        type: 'POST',
                        async : false,// 同步请求
                        dataType: 'json',
                        data: function(d) {
                        	d.pid=$("#pid").val();
                            d.rid=$("#rid").val();
                        }
                    },
                    columns: [
                        {
                            data: "webUser.realname"
                        },
                        {
                            data: "mid"
                        }
                    ],
                    columnDefs: [{
                        "targets" : [1],
                        "sWidth" :"50px",
                        "render" : function(data,event,full) {
                            return '&nbsp;&nbsp;<button type="button" class="btn btn-danger btn-xs" onclick="$.aede.pmp.ptmt.project.rolemanager.deleteLinkedUser(\''+data+'\');">删除</button>';
                        }
                    }]
                });
            } else {
                $.aede.pmp.ptmt.project.rolemanager.linkuserTable.ajax.reload();
            }
        },
    	 //初始化项目角色数据表格
        initRoleTable : function(){
            if (!$.aede.pmp.ptmt.project.rolemanager.roleTable) {
                // 初始化roleTable
                $.aede.pmp.ptmt.project.rolemanager.roleTable = $("#ptmt_project_rolemanager_table").DataTable({
                    serverSide: true,
                    ajax: {
                        url: $.aede.expand.getContextPath() + '/management/pmp/ptmt/project/rolemanager/queryByPaging',
                        type: 'POST',
                        dataType: 'json',
                        data: function(d) {
                        	var myId = $.aede.expand.comm.loginUser.userId;
                        	var pid=$("#pid").val();

                            var roleNames = $.aede.expand.comm.loginUser.roleNames;
                            var re1 = /.项目经理/;
                            var re2 = /.地区管理员/;
                            var re3 = /.集团管理员/;
                            if (re3.test(roleNames)) {

                            } else   if (re2.test(roleNames)) {
                                d["search_EQ_project.webOrganization.id"] = $.aede.expand.comm.loginUser.orgid;
                            } else if (re1.test(roleNames)) {
                                d["search_EQ_project.manager.id"]=myId;
                            }


                        	d.search_EQ_pid=pid;
                            var rname = $.trim($("#ptmt_project_rolemanager_query_rname").val());
                            if (!!rname) {
                                d.search_LIKE_rname = rname;
                            }
                        }
                    },
                    columns: [
                        {
                            data: "project.name"
                        },
                        {
                            data: "rnames"
                        },
                        {
                            data: "rid"
                        }
                    ],
                    columnDefs: [{

						'aTargets' : [ 0],
						"sWidth" :"500px",
						'mRender'  : function ( data, type, full ) {
							var newData=data;
							if(newData.length>29)
								newData=newData.substring(0,29)+"...";
						     return  "<span title=\""+data+"\">"+newData+"</span>";
						}
					    
					},{
                        "targets" : [2],
                        "sWidth" :"150px",
                        "render" : function(data,event,full) {
                            return '<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.project.rolemanager.showModal(\''+data+'\');">编辑</button>' +
                                   '&nbsp;&nbsp;<button type="button" class="btn btn-danger btn-xs" onclick="$.aede.pmp.ptmt.project.rolemanager.deleteRole(\''+data+'\');">删除</button>'+
                                   '&nbsp;&nbsp;<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.project.rolemanager.linkUser(\''+full.pid+'\',\''+full.rid+'\');">用户</button>';
                        }
                    }]
                });
            } else {
                $.aede.pmp.ptmt.project.rolemanager.roleTable.ajax.reload();
            }
        },
        //关联用户
        linkUser:function(pid,rid){
        	$("#pid").val("");
        	$("#rid").val("");
        	//$("a[name='add']").unbind("click");
        	//显示【关联用户】模态窗
        	$("#ptmt_project_linkuser_modal").modal("show");
        	//初始化关联用户页面按钮
        	//$.aede.pmp.ptmt.project.rolemanager.initLinkUserBtnListener();
        	$("#pid").val(pid);
        	$("#rid").val(rid);
        	//初始化两个用户表格
        	$.aede.pmp.ptmt.project.rolemanager.initLinkuserTable();
        	$.aede.pmp.ptmt.project.rolemanager.initProUserTable();
    /*    	$("a[name='add']").bind("click",function(){
        		$("#ptmt_project_user_modal").modal("show");
        		//初始化用户表格
            	$.aede.pmp.ptmt.project.rolemanager.initProUserTable(pid,rid);
            	$("#ptmt_project_linkuser_modal").modal("hide");
			});*/
        },
        // 清空模式窗口
        cleanModal:function(){
        	// 清空保存按钮点击事件
            $("#ptmt_project_rolemanager_btnSave").unbind("click");
    	    // 清空模式窗口表单元素值
            $("#ptmt_project_rolemanager_hide_rid").val("");
            // 清空隐藏域
            $("#ptmt_project_rolemanager_modal form")[0].reset();// 重置表单
            $("#ptmt_project_rolemanager_project").removeAttr("disabled");
        	$(".selectpicker").selectpicker('val', '');//下拉多选赋值
            document.getElementById('ptmt_project_rolemanager_stage').innerHTML="";//清空原有下拉框值
        },
        // 模式窗口表单验证
        initModalFormValidate : function() {
            var icon = "<i class='fa fa-times-circle'></i> ";
            $.aede.pmp.ptmt.project.rolemanager.validateForm = $('#ptmt_project_rolemanager_modal form').validate({
                rules: {
                	ptmt_project_rolemanager_project:{
                		required:true,
                	},
                	ptmt_project_rolemanager_stage:{
                		required:true,
                	},
                	ptmt_project_rolemanager_rname :{
                		required:true,
                	},
                	rdescription:{
                		maxlength:125
                	} 
                },
                messages: {
                	ptmt_project_rolemanager_project:{
                		required:icon + "请选择项目",
                	},
                	ptmt_project_rolemanager_stage:{
                		required:icon + "请选择阶段",
                	},
                	ptmt_project_rolemanager_rname :{
                		required:icon + "请填写角色名称",
                		
                	},
                	rdescription:{
                		maxlength:icon + "长度不能超过125"
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
        // 清除模式窗口表单验证样式
        cleanModalFormValidateStyle : function() {
            if (!!$.aede.pmp.ptmt.project.rolemanager.validateForm) {
                // 清除表单验证样式
            	$.aede.pmp.ptmt.project.rolemanager.validateForm.resetForm();
                // XXX jQuery Validate resetFrom不能清除highlight添加的样式，所有需要手动清理
                $('#ptmt_project_rolemanager_modal .form-group').removeClass('has-error');
                $('#ptmt_project_rolemanager_modal .form-group').removeClass('has-success');
            }
        },
        //初始化模式窗口表单信息
        initModalFormEleValById : function(rid) {
            $.ajax({
                type : "GET",
                async : false,// 同步请求
                url : $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/rolemanager/" + rid + "/view",
                dataType : "json",
                success : function(data) {
                    if (data["responseCode"] == "1") {
                        var result = data["datas"];
                        $("#ptmt_project_rolemanager_hid_project").val(result.pid);
                        $("#ptmt_project_rolemanager_rname").val(result.rname);
                        $("#ptmt_project_rolemanager_hide_rid").val(result.rid);
                        $("#ptmt_project_rolemanager_rdescription").val(result.rdescription);
                        $("#ptmt_project_rolemanager_hide_stage").val(result.sid);
                    } else {
                        $.aede.comm.toastr.error(data["responseMsg"]);
                    }
                    return false;
                },
                error : function() {
                    $.aede.comm.toastr.error("请求异常，查询资源维护失败！");
                }
            });
        },
        //显示模态窗
    	showModal:function(rid){
    		// 清空模式窗口
    		$.aede.pmp.ptmt.project.rolemanager.cleanModal();
    		// 清除表单验证样式
    		$.aede.pmp.ptmt.project.rolemanager.cleanModalFormValidateStyle();
    		$("#ptmt_project_rolemanager_modal").modal("show");
    		if(!rid){
    			//新增
        		//初始化项目select
   			    $.aede.pmp.ptmt.project.rolemanager.projectSelect();
   				document.getElementById('ptmt_project_rolemanager_stage').innerHTML="";//清空原有下拉框值
				$("#ptmt_project_rolemanager_project").attr("disabled",true);
				var proj = $("#ptmt_project_rolemanager_project").val();
				$.aede.pmp.ptmt.project.rolemanager.select.getSelectData(proj);
				$('.selectpicker').selectpicker('refresh');
   			    //初始化保存按钮
			    $.aede.pmp.ptmt.project.rolemanager.initModalBtnClickListener();
    			$("#ptmt_project_rolemanager_modal .modal-header .modal-title").html("新增角色");
    		}else{
    			//编辑
    			//初始化项目select
    			$.aede.pmp.ptmt.project.rolemanager.projectSelect();
   			    document.getElementById('ptmt_project_rolemanager_stage').innerHTML="";//清空原有下拉框值
   				//初始化ModalForm信息
    			$.aede.pmp.ptmt.project.rolemanager.initModalFormEleValById(rid);
				var proj = $("#ptmt_project_rolemanager_hid_project").val();
				// document.getElementById("ptmt_project_rolemanager_project").value=proj;
				$("#ptmt_project_rolemanager_project").val(proj);
				$("#ptmt_project_rolemanager_project").attr("disabled",true);
				//$("#ptmt_project_rolemanager_project option[value='"+proj+"']").attr("selected", true);  
				//最后一个无效
				//$("#ptmt_project_rolemanager_project").attr("value",proj);
				$.aede.pmp.ptmt.project.rolemanager.select.getSelectData(proj);
    			var tags=$('#ptmt_project_rolemanager_hide_stage').val().split(",");
    			$(".selectpicker").selectpicker('val', tags);//下拉多选赋值
    			 $('.selectpicker').selectpicker('refresh');
   			    //初始化保存按钮
			    $.aede.pmp.ptmt.project.rolemanager.initModalBtnClickListener();
    			$("#ptmt_project_rolemanager_modal .modal-header .modal-title").html("编辑角色");
    		}
	 	},
	     //获取模式窗口表单元素值
        getModalFormEleVal: function(){
            var postData = {};
            postData.pid=$.trim($("#ptmt_project_rolemanager_project").val());
            postData.sid=$.trim($("#ptmt_project_rolemanager_stage").val());
            postData.rname=$.trim($("#ptmt_project_rolemanager_rname").val());
            postData.rdescription=$.trim($("#ptmt_project_rolemanager_rdescription").val());
            var rid = $.trim($("#ptmt_project_rolemanager_hide_rid").val());
            if (!!rid) {// 角色id
                postData.rid = rid;
            }

            return postData;
        },
	 	 //初始化模式窗口按钮单击事件
        initModalBtnClickListener : function() {
            $("#ptmt_project_rolemanager_btnSave").bind("click", function() {
                if ($.aede.pmp.ptmt.project.rolemanager.validateForm.form()) {
                    var postData = $.aede.pmp.ptmt.project.rolemanager.getModalFormEleVal();
                    if(!postData.rid){//新增
                    	if(!postData.sid){
                    		$.aede.comm.toastr.error("项目阶段不能为空！");
                    		return false;
                    	}
                        $.ajax({
                            type : "POST",
                            async : false,// 同步请求
                            url : $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/rolemanager/add",
                            dataType : "json",
                            data : postData,
                            success : function(data) {
                                if (data["responseCode"] == "1") {
                                    $.aede.comm.parentCallBack();
                                    $.aede.comm.toastr.success("新增项目角色成功！");
                                    // 隐藏模式窗口
                                    $('#ptmt_project_rolemanager_modal').modal('hide');
                                    // 查询项目角色信息列表
                                    $.aede.pmp.ptmt.project.rolemanager.initRoleTable();
                                } else {
                                    $.aede.comm.toastr.error(data["responseMsg"]);
                                }
                                return false;
                            },
                            error : function() {
                                $.aede.comm.toastr.error("请求异常，新增项目角色失败！");
                            }
                        });
                    }else{//编辑
                    	if(!postData.sid){
                    		$.aede.comm.toastr.error("项目阶段不能为空！");
                    		return false;
                    	}
                        $.ajax({
                            type : "POST",
                            async : false,// 同步请求
                            url : $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/rolemanager/"+postData.rid+"/update",
                            dataType : "json",
                            data : postData,
                            success : function(data) {
                                if (data["responseCode"] == "1") {
                                    $.aede.comm.parentCallBack();
                                    $.aede.comm.toastr.success("编辑项目角色成功！");
                                    // 隐藏模式窗口
                                    $('#ptmt_project_rolemanager_modal').modal('hide');
                                    // 查询项目角色信息列表
                                    $.aede.pmp.ptmt.project.rolemanager.initRoleTable();
                                } else {
                                    $.aede.comm.toastr.error(data["responseMsg"]);
                                }
                                return false;
                            },
                            error : function() {
                                $.aede.comm.toastr.error("请求异常，编辑项目角色失败！");
                            }
                        });
                    }
                
                }
            });
        },
	 	//查询项目下拉列表
	 	projectSelect:function(){
	 		$("#ptmt_project_rolemanager_project").empty();
	 		//$("#ptmt_project_rolemanager_project").append("<option value=''>"+"----请选择项目----"+"</option>");
	 		var myId = $.aede.expand.comm.loginUser.userId;
	 		$.ajax({  
	             type:"POST",  
	             url:$.aede.expand.getContextPath() + '/management/pmp/ptmt/project/rolemanager/'+myId+'/'+$("#pid").val()+'/queryProjectList2',
	             async : false,
	             dataType: 'json', 
	             success:function(fieldList){
	                 if(fieldList != null){
	                     for(var i = 0; i< fieldList.length; i++){
	                    	 $("#ptmt_project_rolemanager_project").append("<option value='"+fieldList[i].id+"'>"+fieldList[i].name+"</option>");
	                     }  
	                 }  
	             },  
	         });  
	 	},
	 	/*  
	 	//项目阶段级联查询
	 	cascading:function(){
	 		     $("#ptmt_project_rolemanager_stage").empty();
                	$("#ptmt_project_rolemanager_stage").append("<option value=''>"+"----请选择阶段----"+"</option>");
                	var proj = $("#ptmt_project_rolemanager_project").val();
	 				if(proj!=""){
	 					$.ajax({  
	 			             type:"POST",  
	 			             async : false, 
	 			             url:$.aede.expand.getContextPath() + "/management/pmp/ptmt/project/rolemanager/"+proj+"/findByPid",  
	 			             dataType: 'json', 
	 			             success:function(fieldList){
	 			                 if(fieldList != null){
	 			                     for(var i = 0; i< fieldList.length; i++){
	 			                    	 $("#ptmt_project_rolemanager_stage").append("<option value='"+fieldList[i].sid+"'>"+fieldList[i].sname+"</option>");
	 			                     }  
	 			                 }  
	 			             },  
	 			         });  
	 				}
	 	},*/
	 	//删除角色
	 	deleteRole:function(rid){
	 		swal({
                title: "您确定要删除这条记录吗?",
                text: "删除后将无法恢复，请谨慎操作！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "删除",
                cancelButtonText: "取消",
                closeOnCancel: false
            }, function(isConfirm){
                if (isConfirm) {
                    $.ajax({
                        url : $.aede.expand.getContextPath()+ "/management/pmp/ptmt/project/rolemanager/"+ rid + "/delete",
                        dataType : "json",
                        contentType: "application/json; charset=utf-8",
                        type : "POST",
                        async : false,// 同步请求
                        success : function(data, textStatus) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.parentCallBack();
                                $.aede.comm.toastr.success("删除项目角色成功");
                                $.aede.pmp.ptmt.project.rolemanager.initRoleTable();
                                return;
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                                return;
                            }
                        },
                        error : function(XMLHttpRequest, textStatus) {
                        	 $.aede.comm.toastr.error("请求异常，删除项目角色失败！");
                        }
                    });

                } else {
                    swal("取消删除！", "角色项目保存安全。", "success");
                }
            });
	 	},
	 	 //初始化用户数据表格
	 	initProUserTable : function(){
            if (!$.aede.pmp.ptmt.project.rolemanager.proUserTable) {
                // 初始化linkuserTable
                $.aede.pmp.ptmt.project.rolemanager.proUserTable = $("#ptmt_project_prouser_table").DataTable({
                    serverSide: true,
                    ajax: {
                        url: $.aede.expand.getContextPath() + '/management/pmp/ptmt/project/rolemanager/userQueryByPaging2',
                        type: 'POST',
                        async : false,// 同步请求
                        dataType: 'json',
                        data: function(d) {
                        	d.pid=$("#pid").val();
                        }
                    },
                    columns: [
                        {
                            data: "webUser.realname"
                        },
                  /*      {
                            data: "mdescription"
                        },*/
                        {
                            data: "mid"
                        }
                    ],
                    columnDefs: [{
                        "targets" : [1],
                        "sWidth" :"150px",
                        "render" : function(data,event,full) {
                            return '<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.project.rolemanager.selectMember(\''+data+'\',\''+$("#rid").val()+'\',\''+$("#pid").val()+'\');">选择</button>';
                        }
                    }]
                });
            } else {
                $.aede.pmp.ptmt.project.rolemanager.proUserTable.ajax.reload();
            }
        },
        selectMember :function(id,rid,pid){
        	if(id!=null&&id!=""){
        		   $.ajax({
                       type : "GET",
                       async : false,// 同步请求
                       url : $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/rolemanager/" + id + "/"+rid+"/selectMember",
                       dataType : "json",
                       success : function(data) {
                           if (data["responseCode"] == "1") {
                        	var pid= pid;
                        	$.aede.pmp.ptmt.project.rolemanager.initLinkuserTable();
                        	$.aede.pmp.ptmt.project.rolemanager.initProUserTable();
                           } else {
                               $.aede.comm.toastr.error(data["responseMsg"]);
                           }
                           return false;
                       },
                       error : function() {
                           $.aede.comm.toastr.error("请求异常，选择失败！");
                       }
                   });
        	}
        
        },
	 	//删除角色
        deleteLinkedUser:function(mid){
	 		swal({
                title: "您确定要删除这个成员和角色的关联吗?",
                text: "删除后将无法恢复，请谨慎操作！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "删除",
                cancelButtonText: "取消",
                closeOnCancel: false
            }, function(isConfirm){
                if (isConfirm) {
                    $.ajax({
                        url : $.aede.expand.getContextPath()+ "/management/pmp/ptmt/project/rolemanager/"+ mid + "/deleteLinkedUser",
                        dataType : "json",
                        contentType: "application/json; charset=utf-8",
                        type : "GET",
                        async : false,// 同步请求
                        success : function(data, textStatus) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("删除成员和角色的关联成功");
                                $.aede.pmp.ptmt.project.rolemanager.initLinkuserTable();
                                $.aede.pmp.ptmt.project.rolemanager.initProUserTable();
                                return;
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                                return;
                            }
                        },
                        error : function(XMLHttpRequest, textStatus) {
                            $.Toast.error("请求异常，删除成员和角色的关联失败！");
                        }
                    });

                } else {
                    swal("取消删除！", "成员和角色的关联保存安全。", "success");
                }
            });
	 	},
        //页面初始化
		init :function(){
			//初始化项目角色列表
			 $.aede.pmp.ptmt.project.rolemanager.initRoleTable();
			//初始化页面按钮
			 $.aede.pmp.ptmt.project.rolemanager.initBtnListener();
			//初始化验证
			 $.aede.pmp.ptmt.project.rolemanager.initModalFormValidate();
			$('.selectpicker').selectpicker({noneSelectedText:'请选择阶段'});
			$('#ptmt_project_rolemanager_project').change(function(){
				var proj = $("#ptmt_project_rolemanager_project").val();
				$.aede.pmp.ptmt.project.rolemanager.select.getSelectData(proj);
			});
		}
    },
    $.aede.pmp.ptmt.project.rolemanager.select = {
			getSelectData : function(proj){
			 $.ajax({  
		         url :  $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/rolemanager/"+proj+"/findByPid",
		         type : "post",
		         async:false,
		         success : function(result){
	        		 for(var i=0;i<result.length;i++){
	        			 $("#ptmt_project_rolemanager_stage").append('<option value="'+result[i].sid+'">'+result[i].stageName+'</option>');     
	        		 }
	        		 $('.selectpicker').selectpicker('refresh');
		             }
		        });
			}
	}
})(jQuery);
$(function(){
	$.aede.pmp.ptmt.project.rolemanager.init();
});