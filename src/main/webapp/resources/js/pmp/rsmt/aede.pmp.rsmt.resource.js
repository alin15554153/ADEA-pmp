(function ($) {
    $.aede.pmp.rsmt.resource = {
        resourceTable: "",//资源表格
        validateForm: "",//表单验证
        uploader: "",
        initBtnListener: function () {
            //加载查询按钮事件
            $("#rsmt_resource_list_query_btnSearch").click(function () {
                $.aede.pmp.rsmt.resource.initResourceTable();
                return false;
            });
            //加载重置按钮事件
            $("#rsmt_resource_list_query_btnReset").click(function () {
                $("#rsmt_resource_list_query_form")[0].reset();
            });
        },
        //初始化数据表格
        initResourceTable: function () {
            if (!$.aede.pmp.rsmt.resource.resourceTable) {
                // 初始化resourceTable
                $.aede.pmp.rsmt.resource.resourceTable = $("#rsmt_resource_list_table").DataTable({
                    serverSide: true,
                    "iDisplayLength":50,
                    ajax: {
                        url: $.aede.expand.getContextPath() + '/management/pmp/rsmt/resource/queryByPaging',
                        type: 'POST',
                        dataType: 'json',
                        data: function (d) {
                            var name = $.trim($("#rsmt_resource_list_query_name").val());
                            var type = $.trim($("#rsmt_resource_list_query_type").val());
                            var stage = $.trim($("#rsmt_resource_list_query_stage").val());
                            var projectName = $.trim($("#rsmt_resource_list_query_project_name").val());

                            if (!!name) {
                                d.search_LIKE_name = name;
                            }
                            if (!!type) {
                                d.search_EQ_type = type;
                            }
                            if (!!stage) {
                                d["search_EQ_projectStage.sname"] = stage;
                            }
                            if (!!projectName) {
                                d["search_LIKE_project.name"] = projectName;
                            }

                            d["search_EQ_project.delTag"] = 0;
                            d["search_EQ_project.manager.id"]= $.aede.expand.comm.loginUser.userId;

                        }
                    },
                    columns: [
                        {
                            data: "name"
                        },
                        {
                            data: "typeName"
                        },
                        {
                            data: "project.name"
                        },
                        {
                            data: "stageName"
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
                     	"targets" : [0],
                     	"render" : function(data,event,full) {
                     		var newData=data;
							if(newData.length>17)
								newData=newData.substring(0,15)+"...";
                     		return "<span title=\""+data+"\">"+newData+"</span>";
                     	}
                     },{
                     	"targets" : [2],
                     	"render" : function(data,event,full) {
                     		var newData=data;
							if(newData.length>27)
								newData=newData.substring(0,25)+"...";
                     		return "<span title=\""+data+"\">"+newData+"</span>";
                     	}
                     },{
                        "targets": [6],
                        "render": function (data, event, full) {
                            return '<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.rsmt.resource.showResourceEditModal(\'' + data + '\');">编辑</button>' +
                                '&nbsp;&nbsp;<button type="button" class="btn btn-danger btn-xs" onclick="$.aede.pmp.rsmt.resource.deleteResource(\'' + data + '\');">删除</button>';
                        }
                    }]
                });
            } else {
                $.aede.pmp.rsmt.resource.resourceTable.ajax.reload();
            }
        },

        // 初始化模式窗口
        initModal: function () {
            // 清空模式窗口表单元素值
            $.aede.pmp.rsmt.resource.cleanModalFormEleVal();
            // 清除表单验证样式
            $.aede.pmp.rsmt.resource.cleanModalFormValidateStyle();
            // 清空保存按钮点击事件
            $("#rsmt_resource_list_modal_btnSave").unbind("click");
            // 显示保存按钮
            $('#rsmt_resource_list_modal_btnSave').show();
            // 清除disabled样式
            $("#rsmt_resource_list_modal form input").attr("disabled", false);
            //$("#rsmt_resource_list_modal form select").select2("enable", true);
            $("#rsmt_resource_list_modal form textarea").attr("disabled", false);
            //$("#rsmt_resource_list_modal form select").select2("val", "");
        },
        // 清空模式窗口表单元素值
        cleanModalFormEleVal: function () {
            // 清空隐藏域
            $("#rsmt_resource_list_modal_hide_id").val("");
            $("#rsmt_resource_list_modal form")[0].reset();// 重置表单
        },

        //初始化模式窗口表单信息
        initModalFormEleValById: function (id) {
            $.ajax({
                type: "GET",
                async: false,// 同步请求
                url: $.aede.expand.getContextPath() + "/management/pmp/rsmt/resource/" + id + "/view",
                dataType: "json",
                success: function (data) {
                    if (data["responseCode"] == "1") {
                        var result = data["datas"];
                        $("#rsmt_resource_list_modal_name").val(result.name);
                        $("#rsmt_resource_list_modal_type").val(result.type);
                        $("#rsmt_resource_list_modal_path").val(result.path);
                        $("#rsmt_resource_list_modal_hide_id").val(result.id);
                        $("#rsmt_resource_list_modal_hide_company").val(result.belong);
                        $("#rsmt_resource_list_modal_hide_procode").val(result.project.code);

                    } else {
                        $.aede.comm.toastr.error(data["responseMsg"]);
                    }
                    return false;
                },
                error: function () {
                    $.aede.comm.toastr.error("请求异常，查询资源维护失败！");
                }
            });
        },

        initInfo: function (obj) {
        	if(!!obj){
        		$("#rsmt_resource_list_modal_name").val(obj.fileNames);
                $("#rsmt_resource_list_modal_path").val(obj.url);
        	}

            var postData = $.aede.pmp.rsmt.resource.getModalFormEleVal();

            if (!postData.id) {
                $.aede.comm.toastr.error("资源维护信息丢失，无法保存编辑结果！");
                return false;
            }

            $.ajax({
                type: "POST",
                async: false,// 同步请求
                url: $.aede.expand.getContextPath() + "/management/pmp/rsmt/resource/" + postData.id + "/update",
                dataType: "json",
                data: postData,
                success: function (data) {
                    if (data["responseCode"] == "1") {
                        $.aede.comm.toastr.success("编辑资源维护成功！");
                        // 隐藏模式窗口
                        $('#rsmt_resource_list_modal').modal('hide');
                        // 查询用户信息
                        $.aede.pmp.rsmt.resource.initResourceTable();
                    } else {
                        $.aede.comm.toastr.error(data["responseMsg"]);
                    }
                    return false;
                },
                error: function () {
                    $.aede.comm.toastr.error("请求异常，编辑资源维护失败！");
                }
            });


        },
        //显示资源编辑模式窗口
        showResourceEditModal: function (id) {
            $("#rsmt_resource_list_modal .modal-header .modal-title").html("资源维护");
            // 初始化模式窗口
            $.aede.pmp.rsmt.resource.initModal();
            if (!!id) {
                $.aede.pmp.rsmt.resource.initModalFormEleValById(id);
                // 初始化保存编辑角色维护按钮的click事件(该按钮的其他点击事件已经在initModal时被清除)
                $.aede.pmp.rsmt.resource.initModalBtnClickListener();

                var editoptions = {};
                editoptions.name = $.trim($("#rsmt_resource_list_modal_name").val());
                editoptions.path = $.trim($("#rsmt_resource_list_modal_path").val());
                var company = $("#rsmt_resource_list_modal_hide_company").val();
                var procode = $("#rsmt_resource_list_modal_hide_procode").val();
                $.aede.pmp.rsmt.resource.uploader = $.aede.pmp.rsmt.resourceUploader.initUploader(company,procode,"uploader", $.aede.pmp.rsmt.resource.initInfo, editoptions);

                //$.aede.pmp.rsmt.resource.initUploader();

                $("#rsmt_resource_list_modal").modal("show");


            } else {
                $.aede.comm.toastr.error("资源维护的id丢失，无法查询！");
            }
        },

        //初始化模式窗口按钮单击事件
        initModalBtnClickListener: function () {

            $("#rsmt_resource_list_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.rsmt.resource.validateForm.form()) {

                    $.aede.pmp.rsmt.resource.uploader.upload();
                }
                /* else {
                 $.aede.comm.toastr.warning("请正确填写资源维护信息！");
                 }*/
            });
        },
        //获取模式窗口表单元素值
        getModalFormEleVal: function () {
            var postData = {};
            postData.name = $.trim($("#rsmt_resource_list_modal_name").val());
            postData.type = $.trim($("#rsmt_resource_list_modal_type").val());
            postData.path = $.trim($("#rsmt_resource_list_modal_path").val());
            postData.uid = $.aede.expand.comm.loginUser.userId;
            var id = $.trim($("#rsmt_resource_list_modal_hide_id").val());
            if (!!id) {// 资源维护的id
                postData.id = id;
            }

            return postData;
        },
        // 模式窗口表单验证
        initModalFormValidate: function () {
            var icon = "<i class='fa fa-times-circle'></i> ";
            $.aede.pmp.rsmt.resource.validateForm = $('#rsmt_resource_list_modal form').validate({
                rules: {
                    type: "required"
                },
                messages: {
                    type: icon + "请选择资源类型"
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
        cleanModalFormValidateStyle: function () {
            if (!!$.aede.pmp.rsmt.resource.validateForm) {
                // 清除表单验证样式
                $.aede.pmp.rsmt.resource.validateForm.resetForm();
                // XXX jQuery Validate resetFrom不能清除highlight添加的样式，所有需要手动清理
                $('#rsmt_resource_list_modal form .form-group').removeClass('has-error');
            }
        },
        //删除资源
        deleteResource: function (id) {
            swal({
                title: "您确定要删除这条记录吗?",
                text: "删除后将无法恢复，请谨慎操作！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "删除",
                cancelButtonText: "取消",
                closeOnCancel: false
            }, function (isConfirm) {
                if (isConfirm) {
                    $.ajax({
                        url: $.aede.expand.getContextPath() + "/management/pmp/rsmt/resource/" + id + "/delete",
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        type: "POST",
                        async: false,// 同步请求
                        success: function (data, textStatus) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("删除资源成功");
                                $.aede.pmp.rsmt.resource.initResourceTable();
                                return;
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                                return;
                            }
                        },
                        error: function (XMLHttpRequest, textStatus) {
                            $.Toast.error("请求异常，删除资源失败！");
                        }
                    });

                } else {
                    swal("取消删除！", "成功。", "success");
                }
            });

        },

        init: function () {
            $.aede.pmp.rsmt.resource.initBtnListener();

            // 模式窗口表单验证
            $.aede.pmp.rsmt.resource.initModalFormValidate();

            $("#rsmt_resource_list_query_btnSearch").click();
        }
    }
})(jQuery);

$(function () {
    $.aede.pmp.rsmt.resource.init();
});