(function($){
    $.aede.pmp.ptmt.changePage = {
        dataTable: "",
        validateForm: "",
        userId:"",
        //初始化
        init: function(){
            var id = $("#pmp_ptmt_change_changePage_project_hide_id").val();
            $.aede.pmp.ptmt.changePage.initPageFormVal(id);
            $.aede.pmp.ptmt.changePage.initAddShowModal(id);
        },
        //初始化项目基本信息
        initPageFormVal: function(id) {
            $.ajax({
                type : "GET",
                async : false,// 同步请求
                url : $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + id + "/view",
                dataType : "json",
                success : function(data) {
                    if (data["responseCode"] == "1") {
                        var result = data["datas"];
                        $("#pmp_ptmt_change_changePage_project_code").val(result.code);
                        $("#pmp_ptmt_change_changePage_project_name").val(result.name);
                        $("#pmp_ptmt_change_changePage_project_type").val($.aede.expand.comm.getDictName("项目类别", result.type));
                        $("#pmp_ptmt_change_changePage_project_nature").val($.aede.expand.comm.getDictName("项目性质", result.nature));
                        $("#pmp_ptmt_change_changePage_project_manager").val(result.manager.realname);
                        $.aede.pmp.ptmt.changePage.userId = result.manager.id;
                        $("#pmp_ptmt_change_changePage_project_ctime").val(result.ctime);
                        $("#pmp_ptmt_change_changePage_project_cid").val(result.webUser.realname);
                        $("#pmp_ptmt_change_changePage_project_director").val(result.director);
                        $("#pmp_ptmt_change_changePage_project_checker").val(result.checker);
                        $("#pmp_ptmt_change_changePage_project_level").val($.aede.expand.comm.getDictName("项目等级", result.level));
                        $("#pmp_ptmt_change_changePage_project_progress").val(!!result.progress ? result.progress + "%" : "");
                        $("#pmp_ptmt_change_changePage_project_amt").val(result.amt);
                        $("#pmp_ptmt_change_changePage_project_remark").val(result.remark);
                        $("#pmp_ptmt_change_changePage_project_hide_stage").val(!!result.projectstage ? result.projectstage.sname : null);

                    } else {
                        $.aede.comm.toastr.error(data["responseMsg"]);
                    }
                    return false;
                },
                error : function() {
                    $.aede.comm.toastr.error("请求异常，查询项目信息失败！");
                }
            });

            //初始化阶段变更数据列表
            $.aede.pmp.ptmt.changePage.initDataTable();
        },
        //初始化阶段变更数据列表
        initDataTable: function() {
            if (!$.aede.pmp.ptmt.changePage.dataTable) {
                // 初始化resourceTable
                $.aede.pmp.ptmt.changePage.dataTable = $("#pmp_ptmt_change_changePage_table").DataTable({
                    serverSide: true,
                    ajax: {
                        url: $.aede.expand.getContextPath() + '/management/pmp/ptmt/projectChangeLog/queryByPaging',
                        type: 'POST',
                        dataType: 'json',
                        data: function(d) {
                            var id = $.trim($("#pmp_ptmt_change_changePage_project_hide_id").val());
                            if (!!id) {
                                d["search_EQ_project.id"] = id;
                            }
                        }
                    },
                    columns: [
                        {
                            data: "webUser.realname"
                        },
                        {
                            data: "changeTime"
                        },
                        {
                            data: "stageName"
                        },
                        {
                            data: "changeReason"
                        }
                    ],
                    columnDefs: [{
                        "targets": [2],
                        "render": function (data, event, full) {
                            return "由“"+full.bfchangeTypeName+"”变更为“"+data+"”";
                        }
                    }]
                });
            } else {
                $.aede.pmp.ptmt.changePage.dataTable.ajax.reload();
            }
        },
        //初始化新增按钮click事件
        initAddShowModal : function (id) {
            $("#pmp_ptmt_change_changePage_btnShowModal").bind('click', function(){
                $("#pmp_ptmt_change_changePage_modal form")[0].reset();
                $.aede.pmp.ptmt.changePage.cleanModalFormValidateStyle();
                $.aede.pmp.ptmt.changePage.initValidateForm();

                $("#pmp_ptmt_change_changePage_modal_btnSave").unbind("click");
                $.aede.pmp.ptmt.changePage.initModalSaveBtn();

                $.aede.pmp.ptmt.changePage.initStageSelect(id);
                $("#pmp_ptmt_change_changePage_modal").modal("show");
            });
        },
        //初始化模式窗口保存按钮click事件
        initModalSaveBtn: function() {
            $("#pmp_ptmt_change_changePage_modal_btnSave").bind('click', function(){
                if ($.aede.pmp.ptmt.changePage.validateForm.form()) {
                    var postData = $.aede.pmp.ptmt.changePage.getModalFormEleVal();
                    $.ajax({
                        type : "POST",
                        async : false,// 同步请求
                        url : $.aede.expand.getContextPath() + "/management/pmp/ptmt/projectChangeLog/add",
                        dataType : "json",
                        data : postData,
                        success : function(data) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("新增阶段变更成功！");

                                //发送邮件
                                var content = $("#pmp_ptmt_change_changePage_project_name").val() + ":阶段变更为"+$("#pmp_ptmt_change_changePage_modal_type option:selected").text()+"阶段";
                                //阶段变更，参数3
                                //$.aede.pmp.comm.mail.sendPTP("阶段变更",content,$.aede.pmp.ptmt.changePage.userId,3);
                                //$.aede.pmp.comm.mail.sendAccToProject("阶段变更",content,$("#pmp_ptmt_change_changePage_project_hide_id").val(),3);


                                $("#pmp_ptmt_change_changePage_project_hide_stage").val($("#pmp_ptmt_change_changePage_modal_type option:selected").attr("rel"));
                                // 隐藏模式窗口
                                $('#pmp_ptmt_change_changePage_modal').modal('hide');
                                // 查询用户信息
                                $.aede.pmp.ptmt.changePage.initDataTable();
                                
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                            }
                            return false;
                        },
                        error : function() {
                            $.aede.comm.toastr.error("请求异常，新增阶段变更失败！");
                        }
                    });
                }
                return false;
            });
        },
        //获取模式窗口表单值
        getModalFormEleVal: function () {
            var postData = {};

            postData.changeUserid = $.aede.expand.comm.loginUser.userId;
            postData.sid=$.trim($("#pmp_ptmt_change_changePage_modal_type").val());
            postData.changeReason=$.trim($("#pmp_ptmt_change_changePage_modal_changeReason").val());
            postData.pid = $("#pmp_ptmt_change_changePage_project_hide_id").val();
            postData.bfchangeType = $.trim($("#pmp_ptmt_change_changePage_project_hide_stage").val());
            var id = $.trim($("#pmp_ptmt_change_changePage_modal_hide_id").val());
            if (!!id) {// 阶段变更id
                postData.id = id;
            }

            return postData;
        },
        //初始化模式窗口表单验证
        initValidateForm: function() {
            var icon = "<i class='fa fa-times-circle'></i> ";
            $.aede.pmp.ptmt.changePage.validateForm = $('#pmp_ptmt_change_changePage_modal form').validate({
                rules: {
                    type: "required",
                    changeReason: "required"
                },
                messages: {
                    type: icon + "请选择阶段类型",
                    changeReason: icon + "请输入变更理由"
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
            if (!!$.aede.pmp.ptmt.changePage.validateForm) {
                // 清除表单验证样式
                $.aede.pmp.ptmt.changePage.validateForm.resetForm();
                // XXX jQuery Validate resetFrom不能清除highlight添加的样式，所有需要手动清理
                $('#pmp_ptmt_change_changePage_modal form .form-group').removeClass('has-error');
            }
        },
        //初始化阶段下拉列表
        initStageSelect : function (id) {
            var curStage = $("#pmp_ptmt_change_changePage_project_hide_stage").val();
            $.ajax({
                url : $.aede.expand.getContextPath() + "/management/pmp/ptmt/projectChangeLog/"+id+"/initStageByProject",
                dataType : "json",
                type : "POST",
                async : false,
                success : function(data, textStatus) {
                    if (data.responseCode == "1") {
                        var list = data.datas;
                        var str = "<option value=''>---请选择阶段---</option>";
                        for (var i=0; i<list.length; i++) {
                            if (curStage != list[i].sname) {
                                var value = list[i].sid;
                                var text = list[i].stageName;
                                var rel = list[i].sname;
                                str += "<option rel='"+rel+"' value='"+value+"'>"+text+"</option>";
                            }
                        }
                        $("#pmp_ptmt_change_changePage_modal_type").html(str);
                    }
                },
                error : function(XMLHttpRequest, textStatus) {
                    $.aede.comm.toastr.error("与后台交互失败！");
                }
            });
        }
    }
})(jQuery);
$(function(){
    $.aede.pmp.ptmt.changePage.init();
});