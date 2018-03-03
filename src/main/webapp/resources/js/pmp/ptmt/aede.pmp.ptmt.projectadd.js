var stage=0;
var resultArray = new Array();
var rpid;
var Num="";
var names="";   //参与人员名称字符串
var ids="";      //参与人员id字符串

(function($){
    $.aede.pmp.ptmt.projectadd = {
        projectstageTable:"", //项目阶段表格
        proposeManTable :"",//项目人员
		selectSingleManTable:"",//负责人、复核人
        validateForm: "",//表单验证
        uploader: "",
		singleFlag:"",
        init : function() {
			$("#pmp_ptmt_project_progress").noUiSlider({
				direction: (Metronic.isRTL() ? "rtl" : "ltr"),
				start: 0,
				connect: 'lower',
				range: {
					'min': 0,
					'1%':[1,1],
					'max': 100
				}
			});
			$("#pmp_ptmt_project_progress").Link('lower').to($("#pmp_ptmt_project_progress_span"));
        	$.aede.pmp.ptmt.projectadd.addProjectModal();
            //$.aede.pmp.ptmt.projectadd.initBtnListener();
            // 模式窗口表单验证
            $.aede.pmp.ptmt.projectadd.initModalFormValidate();
            $.aede.pmp.ptmt.projectadd.initSaveAddProjectBtnClickListener();
            $.aede.pmp.ptmt.projectadd.initProposeManClick();
            $.aede.pmp.ptmt.projectadd.initProposeManChose();
            $.aede.pmp.ptmt.projectadd.addProjectModal();
			$.aede.pmp.ptmt.projectadd.initDirectorClickListener();//负责人、复核人点击按钮事件
			$.aede.pmp.ptmt.projectadd.initSelectSingleManSearchBtnClickListener();//查询按钮点击事件
			$.aede.pmp.ptmt.projectadd.initSelectSingleManResetBtnClickListener();//重置按钮点击事件
			$.aede.pmp.ptmt.projectadd.initStatusChangeListener();//项目状态变化事件
        },
        // 初始化模式窗口
        initModal : function() {
            // 清空模式窗口表单元素值
            $.aede.pmp.ptmt.projectadd.cleanModalFormEleVal();
            // 清除表单验证样式
            $.aede.pmp.ptmt.projectadd.cleanModalFormValidateStyle();
            
            // 清空保存按钮点击事件
            $("#pmp_ptmt_project_modal_btnSave").unbind("click");
            //$("#pmp_ptmt_projectstage_modal_btnSave").unbind("click");
            // 显示保存按钮
            $('#pmp_ptmt_project_modal_btnSave').show();
            //$('#pmp_ptmt_projectstage_modal_btnSave').show();
            // 清除disabled样式
            $("#pmp_ptmt_project_modal form input").attr("disabled",false);
            $("#pmp_ptmt_project_modal form textarea").attr("disabled",false);
           // $("#pmp_ptmt_project_manager").attr("disabled",true);
            $("#pmp_ptmt_project_code").attr("disabled",true);
            //$("#pmp_ptmt_projectstage_modal form input").attr("disabled",false);
        },
       
        // 清空模式窗口表单元素值
        cleanModalFormEleVal : function() {
            // 清空隐藏域
            $("#pmp_ptmt_project_id").val("");
            //stage=0;//初始化
            //deleteNode();
            //closeAll();
            //resultArray.length=0;
            $("#pmp_ptmt_project_modal form")[0].reset();// 重置表单
        },
       
        
        //新增项目页
        addProjectModal:function(){
	 		//$("#pmp_ptmt_project_modal .modal-header .modal-title").html("新建项目");
	 		//$.aede.comm.addTab($.aede.expand.getContextPath() +"/management/pmp/ptmt/project/addinfoTab","新增项目","projec_addinfoTab");
	 		$.aede.pmp.ptmt.projectadd.initModal();
	 		$.aede.pmp.ptmt.projectadd.cleanModalFormEleVal();
	 		$.aede.pmp.ptmt.projectadd.initSaveAddProjectBtnClickListener();
	 		$("#pmp_ptmt_project_manager").val($.aede.expand.comm.loginUser.realName);
	 		//$("#pmp_ptmt_projectstage_uuid").val(Num);
	 		
	 		//$.aede.pmp.ptmt.projectadd.initProjectStageTable();
	 		
			//$("#pmp_ptmt_project_modal").modal("show");
			//$('#pmp_ptmt_project_stagetablediv').show();
			//$('#pmp_ptmt_project_resourcetablediv').hide();
	 	},
	 	
	 	
	 	// 初始化保存新建按钮的click事件
		initSaveAddProjectBtnClickListener : function() {
			$("#pmp_ptmt_project_modal_btnSave").bind("click", function() {
				if ($.aede.pmp.ptmt.projectadd.validateForm.form()) {
					var postData = $.aede.pmp.ptmt.projectadd.getModalFormEleVal();
					if (!!postData.id) {
						// 新建时，id应该为空
						$.aede.comm.toastr.error("项目的ID异常，无法新建！");
						return false;
					}
					$.ajax({
						type : "POST",
						async : false,// 同步请求
						url : $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/add",
						dataType : "json",
						data : postData,
						success : function(data) {
							if (data["responseCode"] == "1") {
								$.aede.comm.toastr.success("新建项目成功！");
								//关闭窗口
								setTimeout(function(){$.aede.comm.closeTab()},2000);
								//发送邮件
								// var mailperid = $("#pmp_ptmt_project_perid").val();
								// var proname = $("#pmp_ptmt_project_name").val();
								// if(mailperid != ""){
								// 	var mailperids = mailperid.split(",");
								// 	for(var k=0;k<mailperids.length;k++){//人员变更 2
								// 		$.aede.pmp.comm.mail.sendPTP("加入项目团队通告","你被拉入了"+proname,mailperids[k],2);
								// 	}
								// }
								
							} else {
								$.aede.comm.toastr.error(data["responseMsg"]);
							}
							return false;
						},
						error : function() {
							$.aede.comm.toastr.error("请求异常，新建失败！");
						}
					});
				} else {
					$.aede.comm.toastr.warning("请正确填写项目信息！");
				}
			});
		},
		
        //初始化模式窗口表单信息
        initModalFormEleValById : function(id) {
            $.ajax({
                type : "GET",
                async : false,// 同步请求
                url : $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + id + "/view",
                dataType : "json",
                success : function(data) {
                    if (data["responseCode"] == "1") {
                        var result = data["datas"];
                        $("#pmp_ptmt_project_name").val(result.name);
                        $("#pmp_ptmt_project_type").val(result.type);
                        $("#pmp_ptmt_project_code").val(result.code);
                        $("#pmp_ptmt_project_nature").val(result.nature);
                        $("#pmp_ptmt_projectstage_ctime").val(result.ctime);
                        $("#pmp_ptmt_project_manager").val(result.manager.realname);
                        $("#pmp_ptmt_project_cid").val(result.cid);
                        $("#pmp_ptmt_project_director").val(result.director);
                        $("#pmp_ptmt_project_checker").val(result.checker);
                        $("#pmp_ptmt_project_level").val(result.level);
                        $("#pmp_ptmt_project_progress").val(result.progress);
                        $("#pmp_ptmt_project_amt").val(result.amt);
                        $("#pmp_ptmt_project_status").val(result.status);
                        $("#pmp_ptmt_project_remark").val(result.remark);
                        $("#pmp_ptmt_project_id").val(result.id);
                        $("#pmp_ptmt_project_perid").val(result.perids);
                        var pernames = result.pernames;
                        $("#pmp_ptmt_project_pername").val(pernames);
                        //rpid = result.id;
                        //$.aede.pmp.ptmt.projectadd.initStageTab(rpid);
                        //$.aede.pmp.ptmt.projectadd.initProjectResourceTable();
                        
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
        
        //获取模式窗口表单元素值
        getModalFormEleVal: function(){
            var postData = {};
            //$.aede.expand.comm.loginUser.userId;
            postData['webUser.id'] = $.aede.expand.comm.loginUser.userId;
            postData['webOrganization.id'] = $.aede.expand.comm.loginUser.orgid;
            postData['webArea.id'] = $.aede.expand.comm.loginUser.areaCode;
            postData.name=$.trim($("#pmp_ptmt_project_name").val());
            postData.type=$.trim($("#pmp_ptmt_project_type").val());
            postData.code=$.trim($("#pmp_ptmt_project_code").val());
            postData.nature=$.trim($("#pmp_ptmt_project_nature").val());
            postData.ctime=$.trim($("#pmp_ptmt_project_ctime").val());
            //postData.manager=$.trim($("#pmp_ptmt_project_manager").val());
            //postData['manager.id'] = $.aede.expand.comm.loginUser.userId;
			var managerId= $.trim($("pmp_ptmt_project_manager_id").val());
			if(!managerId){
				managerId= $.aede.expand.comm.loginUser.userId;
			}
			postData['manager.id'] =managerId;
            postData.cid=$.trim($("#pmp_ptmt_project_cid").val());
            postData.director=$.trim($("#pmp_ptmt_project_director").val());
            postData.checker=$.trim($("#pmp_ptmt_project_checker").val());
            postData.level=$.trim($("#pmp_ptmt_project_level").val());
            postData.progress=$.trim($("#pmp_ptmt_project_progress_span").text());
            postData.amt=$.trim($("#pmp_ptmt_project_amt").val());
            postData.status=$.trim($("#pmp_ptmt_project_status").val());
            postData.remark=$.trim($("#pmp_ptmt_project_remark").val());
            //postData.uupid=$.trim($("#pmp_ptmt_projectstage_uuid").val());
            postData.perids=$.trim($("#pmp_ptmt_project_perid").val());
			postData.projectSource = $.trim($("#pmp_ptmt_project_source").val());
			postData.stakeholder = $.trim($("#pmp_ptmt_project_stakeholder").val());
			postData.signingProbability = $.trim($("#pmp_ptmt_project_signingProbability").val());

			postData.totalPackage=$.trim($("#pmp_ptmt_project_totalPackage").val());
			postData.subPackage=$.trim($("#pmp_ptmt_project_subPackage").val());
			postData.expectedTime=$.trim($("#pmp_ptmt_project_expectedTime").val());

			postData.assistanceUnit=$.trim($("#pmp_ptmt_project_assistanceUnit").val());
			postData.assistanceContent=$.trim($("#pmp_ptmt_project_assistanceContent").val());
			postData.signingBudget= $.trim($("#pmp_ptmt_project_signingBudget").val());


			var id = $.trim($("#pmp_ptmt_project_id").val());
            if (!!id) {// 项目的id
                postData.id = id;
            }
            return postData;
        },
		initStatusChangeListener:function(){
			$("#pmp_ptmt_project_status").change(function(){
				var status = $("#pmp_ptmt_project_status").val();
				if(status==1){
					$("#pmp_ptmt_project_signingProbability").attr("readonly",false);
				}else{
					if(status==4){
						$("#pmp_ptmt_project_signingProbability").val(0);
					}else if(status==2||status==3||status==5){
						$("#pmp_ptmt_project_signingProbability").val(100);
					}
					$("#pmp_ptmt_project_signingProbability").attr("readonly",true);
				}
			});
		},
       
        // 模式窗口表单验证
        initModalFormValidate : function() {
            var icon = "<i class='fa fa-times-circle'></i> ";
            $.aede.pmp.ptmt.projectadd.validateForm = $('#pmp_ptmt_project_modal form').validate({
                rules: {
                	name : {
						required : true,
						maxlength : 225
					},
					
					ctime : {
						required : true
					},
					stage : {
						required : true
					},
					progress : {
						maxlength : 18,
						number:true ,
						min:0,
						max:100
					},
					pmp_ptmt_project_level : {
						required : true
					},
					pmp_ptmt_project_type : {
						required : true
					},
					pmp_ptmt_project_nature : {
						required : true
					},
					director : {
						maxlength : 11
					},
					checker : {
						maxlength : 11
					},
					manager : {
						required : true,
						maxlength : 11
					},
					amt : {
						required : true,
						maxlength : 11,
						number:true ,
						min:0
					},
					pmp_ptmt_project_status : {
						required : true
					},
					remark : {
						maxlength : 225
					},
					source : {
						required: true
					},
					stakeholder:{
						required: true
					},
					signingProbability:{
						required:true
					}
                },
                messages: {
                	name : {
						required : "请填写项目名称！",
						maxlength : "项目名称长度不可超过225！"
					},
					
					ctime : {
						required : "请填写建立日期！"
					},
					stage : {
						required : "请填写当前阶段！"
					},
					progress : {
						maxlength : "进度长度不可超过18！",
						number : "请输入合法的数字！",
						min : "进度最低为0！",
						max : "进度最高为100%！"	
					},
					pmp_ptmt_project_level : {
						required : "请填写项目等级！"
					},
					pmp_ptmt_project_type : {
						required : "请填写项目类别！"
					},
					pmp_ptmt_project_nature : {
						required : "请填写项目性质！"
					},
					director : {
						maxlength : "负责人姓名长度不可超过11！"
					},
					checker : {
						maxlength : "复核人姓名长度不可超过11！"
					},
					manager : {
						required : "项目经理是必填项！",
						maxlength : "项目经理姓名长度不可超过11！"
					},
					amt : {
						required : "金额是必填项！",
						maxlength : "金额长度不可超过11！",
						number : "请输入合法的数字！",
						min : "金额不可为负！"
					},
					pmp_ptmt_project_status : {
						required : "状态是必填项！"
					},
					remark : {
						maxlength : "备注长度不可超过225！"
					},
					source : {
						required: "项目来源是必填项！"
					},
					stakeholder:{
						required: "干系人是必填项！"
					},
					signingProbability:{
						required:"签约概率是必填项！"
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
                validClass: "help-block m-b-none"
            });
            return false;
        },
        
        // 清除模式窗口表单验证样式
        cleanModalFormValidateStyle : function() {
            if (!!$.aede.pmp.ptmt.projectadd.validateForm) {
                // 清除表单验证样式
                $.aede.pmp.ptmt.projectadd.validateForm.resetForm();
                // XXX jQuery Validate resetFrom不能清除highlight添加的样式，所有需要手动清理
                $('#pmp_ptmt_project_modal form .form-group').removeClass('has-error');
                $('#pmp_ptmt_project_modal form .form-group').removeClass('has-success');
            }
        },
		// 提出人员选择单击事件
		initProposeManClick : function() {
			$("#pmp_ptmt_project_pername").bind("click", function() {
				$("#jail-detain-addPrsArrgInfo-proposeManModal").modal("show");
				// 人员查询按钮点击事件
				$.aede.pmp.ptmt.projectadd.initProposeManSearchBtnClickListener();
				// 人员重置按钮点击事件
				$.aede.pmp.ptmt.projectadd.initProposeManResetBtnClickListener();
				$.aede.pmp.ptmt.projectadd.initProposeManTable();
				$(".panel-body").remove();
				//$("#jail-detain-addPrsArrgInfo-proposeMan-namePanel").html("");
				ids = $("#pmp_ptmt_project_perid").val();
				names = $("#pmp_ptmt_project_pername").val();
				if(names != "" && ids != ""){
					var namesplit = names.split(",");
					var idsplit = ids.split(",");
					for(var p=0;p<namesplit.length;p++){
						$("#jail-detain-addPrsArrgInfo-proposeMan-namePanel").append("<div class='panel-body'>"+namesplit[p]+
								"<input type='hidden' name='perhidid' value='"+idsplit[p]+"'>"+	 // class='btn btn-info btn-xs
								"<button style='float:right' type='button' onclick='$(this).parent().remove();' class='btn btn-danger btn-xs'><i class='fa fa-trash-o'></i></button></div>");	
					}	
				}
			});
		},
		initDirectorClickListener:function(){
			$("#pmp_ptmt_project_director").bind("click",function(){
				$("#pmp-ptmt-addProject-selectSingleMan-modal").modal("show");
				$.aede.pmp.ptmt.projectadd.singleFlag=1;
				$.aede.pmp.ptmt.projectadd.initSelectSingleManTable();
			});
			$("#pmp_ptmt_project_checker").bind("click",function(){
				$("#pmp-ptmt-addProject-selectSingleMan-modal").modal("show");
				$.aede.pmp.ptmt.projectadd.singleFlag=2;
				$.aede.pmp.ptmt.projectadd.initSelectSingleManTable();
			});
			$("#pmp_ptmt_project_manager").bind("click",function(){
				$("#pmp-ptmt-addProject-selectSingleMan-modal").modal("show");
				$.aede.pmp.ptmt.projectadd.singleFlag=3;
				$.aede.pmp.ptmt.projectadd.initSelectSingleManTable();
			});
		},
		// 初始化提出人员表
		initProposeManTable : function() {
			if (!$.aede.pmp.ptmt.projectadd.proposeManTable) {
				// 初始化proposeManTable
				$.aede.pmp.ptmt.projectadd.proposeManTable = $("#jail-detain-addPrsArrgInfo-proposeManTable")
							.DataTable(
								{
									serverSide: true,
									bLengthChange : false,
									ajax : {
										url : $.aede.expand.getContextPath()
												+ '/management/pmp/ptmt/project/queryuserlist',
										type : 'POST',
										dataType : 'json',
										data : function(d) {
											var realname = $.trim($("#jail-detain-addPrsArrgInfo-proposeMan-policeName").val());
											d.search_LIKE_realname = realname;
										}
									},
									columns : [ {
										data : "realname"
									},{
										data : "organization.organname"
									}, {
										data : "id"
									} ],
									columnDefs : [
									         {
			                                    'targets': [2],
			                                    'render': function (data, type, full) {
			                                    	return "<a href=\"javascript:void(0);\" onclick=\"$.aede.pmp.ptmt.projectadd.nameChosed('"
													+full.realname+"','"+full.id+"')\" class=\"btn default btn-xs red-stripe\">选择</a>";
			                                    }
			                                }]

								});
			} else {
				$.aede.pmp.ptmt.projectadd.proposeManTable.ajax.reload();
			}
		},

		initSelectSingleManTable : function() {
			if (!$.aede.pmp.ptmt.projectadd.selectSingleManTable) {
				// 初始化proposeManTable
				$.aede.pmp.ptmt.projectadd.selectSingleManTable = $("#pmp-ptmt-addProject-selectSingleMan-dataTable")
					.DataTable(
						{
							serverSide: true,
							bLengthChange : false,
							ajax : {
								url : $.aede.expand.getContextPath()
								+ '/management/pmp/ptmt/project/queryuserlist',
								type : 'POST',
								dataType : 'json',
								data : function(d) {
									var realname = $.trim($("#pmp-ptmt-addProject-selectSingleMan-name").val());
									d.search_LIKE_realname = realname;
								}
							},
							columns : [ {
								data : "realname"
							},{
								data : "organization.organname"
							}, {
								data : "id"
							} ],
							columnDefs : [
								{
									'targets': [2],
									'render': function (data, type, full) {
										return "<a href=\"javascript:void(0);\" onclick=\"$.aede.pmp.ptmt.projectadd.nameSelected('"
											+full.realname+"','"+full.id+"')\" class=\"btn default btn-xs red-stripe\">选择</a>";
									}
								}]

						});
			} else {
				$.aede.pmp.ptmt.projectadd.selectSingleManTable.ajax.reload();
			}
		},
		// 单个人员选择
		nameChosed : function(name,id){
			names = "";
			ids = "";
			$(".panel-body").each(function(){
				names += $(this).text();
				ids += $(this).text();
			});
			if(names.indexOf(name) == -1){
				$("#jail-detain-addPrsArrgInfo-proposeMan-namePanel").append("<div class='panel-body'>"+name+
					"<input type='hidden' name='perhidid' value='"+id+"'>"+	 // class='btn btn-info btn-xs
					"<button style='float:right' type='button' onclick='$(this).parent().remove();' class='btn btn-danger btn-xs'><i class='fa fa-trash-o'></i></button></div>");
				return false;
			}else{
				return false;
			}
		},
		nameSelected:function(name,id){
			//负责人
			if($.aede.pmp.ptmt.projectadd.singleFlag==1){
				$("#pmp_ptmt_project_director").val(name);
			}else if($.aede.pmp.ptmt.projectadd.singleFlag==2){//复核人
				$("#pmp_ptmt_project_checker").val(name);
			}else if($.aede.pmp.ptmt.projectadd.singleFlag==3){//项目经理
				$("#pmp_ptmt_project_manager").val(name);
				$("#pmp_ptmt_project_manager_id").val(id);
			}
			$("#pmp-ptmt-addProject-selectSingleMan-modal").modal("hide");
		},
		// 人员确认选择
		initProposeManChose : function() {
			$("#jail-detain-addPrsArrgInfo-proposeMan-btnSure").bind("click", function() {
				//$("#jail-detain-addPrsArrgInfo-proposeManModal").hide();
				names = "";
				ids = "";
				$(".panel-body").each(function(){
					names += $(this).text()+",";
				});
				$("input[name='perhidid']").each(function(){   
					ids += $(this).val()+",";
			    });
				$("#pmp_ptmt_project_pername").val(names.substring(0,names.length-1));
				$("#pmp_ptmt_project_perid").val(ids.substring(0,ids.length-1));
			});
		},
		// 人员查询按钮点击事件
		initProposeManSearchBtnClickListener : function() {
			$("#jail-detain-addPrsArrgInfo-proposeMan-btnSearch").click(function() {
				$.aede.pmp.ptmt.projectadd.initProposeManTable();
				return false;
			});
		},
		// 人员重置按钮点击事件
		initProposeManResetBtnClickListener : function() {
			$("#jail-detain-addPrsArrgInfo-proposeMan-btnReset").click(function() {
				$("#jail-detain-addPrsArrgInfo-proposeManModal input").val("");
				$("#jail-detain-addPrsArrgInfo-proposeManModal-isPinYin").removeAttr("checked");
				return false;
			});
		},
		//负责人、复核人查询按钮点击事件
		initSelectSingleManSearchBtnClickListener:function(){
			$("#pmp-ptmt-addProject-selectSingleMan-btnSearch").click(function() {
				$.aede.pmp.ptmt.projectadd.initSelectSingleManTable();
				return false;
			});
		},
		//负责人、复核人重置按钮点击事件
		initSelectSingleManResetBtnClickListener:function(){
			$("#pmp-ptmt-addProject-selectSingleMan-btnReset").click(function() {
				$("#pmp-ptmt-addProject-selectSingleMan-modal input").val("");
				return false;
			});
		},
    }
})(jQuery);
$(document).ready(function () {
	ComponentsNoUiSliders.init();
    $.aede.pmp.ptmt.projectadd.init();
	laydate({
		elem: '#pmp_ptmt_project_expectedTime',
		event: 'focus'
	});
    laydate({
        elem: '#pmp_ptmt_project_ctime',
        event: 'focus'
    });


});

function GetDateT(date)
{
 var d,s;
 d = new Date(date);
 s = d.getFullYear() + "-"; 
 s = s + (d.getMonth() + 1) + "-";//取月份
 s += d.getDate() + " ";         //取日期
 return(s);  
}




