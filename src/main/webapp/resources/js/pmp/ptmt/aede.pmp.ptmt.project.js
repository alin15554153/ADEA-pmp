var stage = 0;
var resultArray = new Array();
var rpid;
var project_id = "";
(function ($) {

    $.aede.pmp.ptmt.project = {
        projectTable: "",//项目表格
        projectstageTable: "", //项目阶段表格
        validateFollow: "",
        //projectresourceTable: "",//项目资源表格
        //proposeManTable :"",//项目人员
        //validateForm: "",//表单验证
        //uploader: "",
        init: function () {
            $.aede.pmp.ptmt.project.initBtnListener();
            // 模式窗口表单验证
            //$.aede.pmp.ptmt.project.initModalFormValidate();
            $.aede.pmp.ptmt.project.initStageFormValidate();
            $.aede.pmp.ptmt.project.initFollowFormValidate();
            //$.aede.pmp.ptmt.project.initResourceFormValidate();
            $("#pmp_ptmt_project_btnSearch").click();
            //$.aede.pmp.ptmt.project.initSaveAddProjectBtnClickListener();
            //$.aede.pmp.ptmt.project.initProposeManClick();
            //$.aede.pmp.ptmt.project.initProposeManChose();
        },
        initBtnListener: function () {
            //加载查询按钮事件
            $("#pmp_ptmt_project_btnSearch").click(function () {
                $.aede.pmp.ptmt.project.initProjectTable();
                return false;
            });
            //加载重置按钮事件
            $("#pmp_ptmt_project_btnReset").click(function () {
                $("#pmp_ptmt_project_query_name").val("");
                $("#pmp_ptmt_project_query_code").val("");
            });
        },
        //初始化数据表格
        initProjectTable: function () {
            if (!$.aede.pmp.ptmt.project.projectTable) {
                // 初始化projectTable
                $.aede.pmp.ptmt.project.projectTable = $("#pmp_ptmt_project_table").DataTable({
                    serverSide: true,
                    "iDisplayLength": 50,
                    ajax: {
                        url: $.aede.expand.getContextPath() + '/management/pmp/ptmt/project/queryByPaging',
                        type: 'POST',
                        dataType: 'json',
                        data: function (d) {
                            var name = $.trim($("#pmp_ptmt_project_query_name").val());
                            var code = $.trim($("#pmp_ptmt_project_query_code").val());
                            var type = $.trim($("#pmp_ptmt_project_query_type").val());
                            var status = $.trim($("#pmp_ptmt_project_query_status").val());
                            var projectSource = $.trim($("#pmp_ptmt_project_query_projectSource").val());
                            if (!!name) {
                                d.search_LIKE_name = name;
                            }
                            if (!!code) {
                                d.search_LIKE_code = code;
                            }
                            if (!!type) {
                                d.search_EQ_type = type;
                            }
                            if (!!status) {
                                d.search_EQ_status = status;
                            }
                            if (!!projectSource) {
                                d.search_LIKE_projectSource = projectSource;
                            }
                            d.search_EQ_delTag = "0";


                            var roleNames = $.aede.expand.comm.loginUser.roleNames;
                            var re1 = /.项目经理/;
                            var re2 = /.地区管理员/;
                            var re3 = /.集团管理员/;
                            if (re3.test(roleNames)) {

                            } else   if (re2.test(roleNames)) {
                                d["search_EQ_webOrganization.id"] = $.aede.expand.comm.loginUser.orgid;
                            } else if (re1.test(roleNames)) {
                                d["search_EQ_manager.id"] = $.aede.expand.comm.loginUser.userId;
                            }

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
                            "targets": [1],
                            "render": function (data, event, full) {
                                var newData = data;
                                if (newData.length > 25)
                                    newData = newData.substring(0, 25) + "...";
                                if (full.type == 1) {
                                    return "<button type=\"button\" class=\"btn btn-info btn-xs\">自建</button> <span title=\"" + data + "\">" + newData + "</span>";
                                } else {
                                    return "<span title=\"" + data + "\">" + newData + "</span>";
                                }
                            }
                        }, {
                            "targets": [3],
                            "render": function (data) {
                                return '<span title=' + data + '%>' + data + '%<div class="progress progress-mini"><div style="width: ' + data + '%;" class="progress-bar"></div></div></span>';
                            }
                        }, {
                            "targets": [7],
                            "render": function (data, event, full) {
                                return data;
                            }
                        }, {
                            "targets": [8],
                            "render": function (data, event, full) {
                                return '<button type="button" class="btn btn-info btn-xs" onclick="$.aede.pmp.ptmt.project.initAddFollowModal(\'' + data + '\');">跟踪</button>' +
                                    '&nbsp;&nbsp;<button type="button" class="btn btn-warning btn-xs" onclick="$.aede.pmp.ptmt.project.showStageModal(\'' + data + '\');">阶段</button>' +
                                    '&nbsp;&nbsp;<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.project.showProjectEditModal(\'' + data + '\');">编辑</button>' +
                                    '&nbsp;&nbsp;<button type="button" class="btn btn-danger btn-xs" onclick="$.aede.pmp.ptmt.project.deleteProject(\'' + data + '\');">删除</button>';
                            }
                        }
                    ]

                });
            } else {
                $.aede.pmp.ptmt.project.projectTable.ajax.reload();
            }
        },
        //初始化数据表格，阶段
        initProjectStageTable: function () {
            $("#pmp_ptmt_project_id").val(project_id);
            if (!$.aede.pmp.ptmt.project.projectstageTable) {
                //var pid = $("#pmp_ptmt_projectstage_uuid").val();
                // 初始化projectTable
                $.aede.pmp.ptmt.project.projectstageTable = $("#pmp_ptmt_project_stagetable").DataTable({
                    serverSide: true,
                    ajax: {
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/queryByPagingWithPid",
                        type: 'POST',
                        dataType: 'json',
                        data: function (d) {
                            var pid = $("#pmp_ptmt_project_id").val();
                            ;
                            if (!!pid) {
                                d.search_EQ_pid = pid;
                            }
                            /*var pid = $("#pmp_ptmt_project_resource_query_pid").val();
                             var stage = $("#pmp_ptmt_project_resource_query_stage").val();
                             if (!!pid) {
                             d.search_EQ_pid = pid;
                             }
                             if (!!stage) {
                             d.search_EQ_sid = stage;
                             }*/
                        }
                    },
                    columns: [
                        {
                            data: "sseq"
                        },
                        {
                            data: "stageName"
                        },
                        {
                            data: "expbegintime"
                        },
                        {
                            data: "expendtime"
                        },
                        {
                            data: "sid"
                        }
                    ],
                    columnDefs: [
                        {
                            "targets": [2],
                            "render": function (data, event, full) {
                                return GetDateT(data);
                            }
                        }, {
                            "targets": [3],
                            "render": function (data, event, full) {
                                return GetDateT(data);
                            }
                        }, {
                            "targets": [4],
                            "render": function (data, event, full) {
                                return '<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.project.showProjectStageEditModal(\'' + data + '\');">编辑</button>';
                            }
                        }
                    ]
                });
            } else {
                $.aede.pmp.ptmt.project.projectstageTable.ajax.reload();
            }
        },

        // 初始化模式窗口
        initModal: function () {
            // 清空模式窗口表单元素值
            $.aede.pmp.ptmt.project.cleanModalFormEleVal();
            // 清除表单验证样式
            $.aede.pmp.ptmt.project.cleanModalFormValidateStyle();

            // 清空保存按钮点击事件
            //$("#pmp_ptmt_project_modal_btnSave").unbind("click");
            //$("#pmp_ptmt_projectstage_modal_btnSave").unbind("click");
            // 显示保存按钮
            //$('#pmp_ptmt_project_modal_btnSave').show();
            //$('#pmp_ptmt_projectstage_modal_btnSave').show();
            // 清除disabled样式
            $("#pmp_ptmt_project_modal form input").attr("disabled", false);
            $("#pmp_ptmt_project_modal form textarea").attr("disabled", false);
            //$("#pmp_ptmt_projectstage_modal form input").attr("disabled",false);
        },
        // 初始化stage模式窗口
        initStageModal: function () {
            // 清空模式窗口表单元素值
            $.aede.pmp.ptmt.project.cleanStageModalFormEleVal();
            // 清除表单验证样式
            $.aede.pmp.ptmt.project.cleanStageFormValidateStyle();
            // 清空保存按钮点击事件
            $("#pmp_ptmt_projectstage_modal_btnSave").unbind("click");
            // 显示保存按钮
            $('#pmp_ptmt_projectstage_modal_btnSave').show();
            // 清除disabled样式
            $("#pmp_ptmt_projectstage_modal form input").attr("disabled", false);
        },

        // 清空模式窗口表单元素值
        cleanModalFormEleVal: function () {
            // 清空隐藏域
            $("#pmp_ptmt_project_id").val("");
            /*stage=0;//初始化
             //deleteNode();
             closeAll();
             resultArray.length=0;*/
            $("#pmp_ptmt_project_modal form")[0].reset();// 重置表单
        },
        cleanStageModalFormEleVal: function () {
            // 清空隐藏域
            $("#pmp_ptmt_projectstage_sid").val("");
            $("#pmp_ptmt_projectstage_modal form")[0].reset();// 重置表单
        },

        //新增项目页
        addProjectModal: function () {
            //$("#pmp_ptmt_project_modal .modal-header .modal-title").html("新建项目");
            $.aede.comm.addTab($.aede.expand.getContextPath() + "/management/pmp/ptmt/project/addinfoTab", "新增项目", "projec_addinfoTab");
            /*$.aede.pmp.ptmt.project.initModal();
             Num="";
             for(var i=0;i<6;i++){
             Num+=Math.floor(Math.random()*10);
             }
             $.aede.pmp.ptmt.project.cleanModalFormEleVal();
             $("#pmp_ptmt_projectstage_uuid").val(Num);
             $("#pmp_ptmt_project_manager").val($.aede.expand.comm.loginUser.realName);
             $.aede.pmp.ptmt.project.initProjectStageTable();
             $.aede.pmp.ptmt.project.initSaveAddProjectBtnClickListener();
             $("#pmp_ptmt_project_modal").modal("show");
             $('#pmp_ptmt_project_stagetablediv').show();
             $('#pmp_ptmt_project_resourcetablediv').hide();*/
        },
        //显示阶段信息
        showStageModal: function (id) {
            $("#pmp_ptmt_project_modal .modal-header .modal-title").html("阶段信息");
            $("#pmp_ptmt_project_modal").modal("show");
            project_id = id;
            //$("#pmp_ptmt_project_id").val(project_id);
            $.aede.pmp.ptmt.project.initModal();
            $('#pmp_ptmt_project_stagetablediv').show();
            $.aede.pmp.ptmt.project.initProjectStageTable();

        },

        //新增阶段页
        addProjectStageModal: function () {

            $("#pmp_ptmt_projectstage_modal .modal-header .modal-title").html("新建阶段");
            $.aede.pmp.ptmt.project.initStageModal();
            $.aede.pmp.ptmt.project.initSaveAddProjectStageBtnClickListener();
            $("#pmp_ptmt_projectstage_modal").modal("show");
        },


        // 初始化保存新建按钮的click事件,项目阶段
        initSaveAddProjectStageBtnClickListener: function () {
            $("#pmp_ptmt_projectstage_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.ptmt.project.stageValidateForm.form()) {
                    var postData = $.aede.pmp.ptmt.project.getStageModalFormEleVal();
                    if (!!postData.sid) {
                        // 新建时，id应该为空
                        $.aede.comm.toastr.error("阶段的编号异常，无法新建！");
                        return false;
                    }
                    $.ajax({
                        type: "POST",
                        async: false,// 同步请求
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/addstage",
                        dataType: "json",
                        data: postData,
                        success: function (data) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("新建项目阶段成功！");
                                // 隐藏模式窗口
                                $('#pmp_ptmt_projectstage_modal').modal('hide');
                                // 查询风险源登记信息
                                $.aede.pmp.ptmt.project.initProjectStageTable();
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                            }
                            return false;
                        },
                        error: function () {
                            $.aede.comm.toastr.error("请求异常，新建失败！");
                        }
                    });
                } else {
                    $.aede.comm.toastr.warning("请正确填写项目阶段信息！");
                }
            });
        },
        // 初始化保存新建按钮的click事件,项目资源
        initSaveAddProjectResourceBtnClickListener: function () {
            $("#pmp_ptmt_projectresource_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.ptmt.project.validateresource.form()) {
                    $.aede.pmp.ptmt.project.uploader.upload();

                } else {
                    $.aede.comm.toastr.warning("请正确填写项目资源信息！");
                }
            });
        },
        //初始化模式窗口表单信息
        initModalFormEleValById: function (id) {
            $.ajax({
                type: "GET",
                async: false,// 同步请求
                url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + id + "/view",
                dataType: "json",
                success: function (data) {
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
                        rpid = result.id;
                        $.aede.pmp.ptmt.project.initStageTab(rpid);
                        $.aede.pmp.ptmt.project.initProjectResourceTable();

                    } else {
                        $.aede.comm.toastr.error(data["responseMsg"]);
                    }
                    return false;
                },
                error: function () {
                    $.aede.comm.toastr.error("请求异常，查询项目失败！");
                }
            });
        },
        //初始化模式窗口表单信息，项目阶段
        initStageModalFormEleValById: function (id) {
            $.ajax({
                type: "GET",
                async: false,// 同步请求
                url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + id + "/viewstage",
                dataType: "json",
                success: function (data) {
                    if (data["responseCode"] == "1") {
                        var result = data["datas"];
                        $("#pmp_ptmt_projectstage_sname").val(result.sname);
                        $("#pmp_ptmt_projectstage_sseq").val(result.sseq);
                        $("#pmp_ptmt_projectstage_expbegintime").val(result.expbegintime);
                        $("#pmp_ptmt_projectstage_expendtime").val(result.expendtime);
                        $("#pmp_ptmt_projectstage_type").val(result.type);
                        $("#pmp_ptmt_projectstage_sid").val(result.sid);
                    } else {
                        $.aede.comm.toastr.error(data["responseMsg"]);
                    }
                    return false;
                },
                error: function () {
                    $.aede.comm.toastr.error("请求异常，查询项目阶段失败！");
                }
            });
        },
        //初始化模式窗口表单信息，项目阶段
        initResourceModalFormEleValById: function (id) {
            $.ajax({
                type: "GET",
                async: false,// 同步请求
                url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + id + "/viewresource",
                dataType: "json",
                success: function (data) {
                    if (data["responseCode"] == "1") {
                        var result = data["datas"];
                        $("#pmp_ptmt_projectresource_type").val(result.type);
                        $("#pmp_ptmt_projectresource_id").val(result.id);
                        /*$("#pmp_ptmt_projectresource_name").val(fileData.fileNames);
                         $("#pmp_ptmt_projectresource_path").val(fileData.url);
                         $("#pmp_ptmt_projectresource_size").val(fileData.fileSize);*/
                        //
                        $("#pmp_ptmt_projectresource_path").val(result.path);
                        $("#pmp_ptmt_projectresource_name").val(result.name);
                        $("#pmp_ptmt_projectresource_size").val(result.size);
                        //var ttupload = "<tr id='tld'><td align='left'>" + "<a href='"+$.aede.expand.getContextPath()+"/upload/"+result.path+"'  style='text-decoration:none;' >"+ result.name+"</a></td></tr>";
                        //$("#pmp_ptmt_projectresource_updTablework").html(ttupload);
                    } else {
                        $.aede.comm.toastr.error(data["responseMsg"]);
                    }
                    return false;
                },
                error: function () {
                    $.aede.comm.toastr.error("请求异常，查询项目阶段失败！");
                }
            });
        },
        //显示项目编辑模式窗口
        showProjectEditModal: function (id) {
            //$("#pmp_ptmt_project_modal .modal-header .modal-title").html("项目");
            $.aede.comm.addTab($.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + id + "/editinfoTab", "编辑项目", "projec_editinfoTab");
            // 初始化模式窗口
            /*$.aede.pmp.ptmt.project.initModal();
             if (!!id) {
             $.aede.pmp.ptmt.project.initModalFormEleValById(id);
             //tab加载在数据加载里

             // 初始化保存编辑角色维护按钮的click事件(该按钮的其他点击事件已经在initModal时被清除)
             $.aede.pmp.ptmt.project.initModalBtnClickListener();
             $("#pmp_ptmt_project_modal").modal("show");
             $('#pmp_ptmt_project_stagetablediv').hide();
             } else {
             $.aede.comm.toastr.error("项目的id丢失，无法查询！");
             }*/
        },

        //显示编辑模式窗口，阶段
        showProjectStageEditModal: function (id) {
            $("#pmp_ptmt_projectstage_modal .modal-header .modal-title").html("项目阶段");
            // 初始化模式窗口
            $.aede.pmp.ptmt.project.initStageModal();
            if (!!id) {
                $.aede.pmp.ptmt.project.initStageModalFormEleValById(id);
                // 初始化保存编辑角色维护按钮的click事件(该按钮的其他点击事件已经在initModal时被清除)
                $.aede.pmp.ptmt.project.initStageModalBtnClickListener();
                $("#pmp_ptmt_projectstage_modal").modal("show");
            } else {
                $.aede.comm.toastr.error("项目阶段的id丢失，无法查询！");
            }
        },

        //初始化模式窗口按钮单击事件
        initModalBtnClickListener: function () {
            $("#pmp_ptmt_project_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.ptmt.project.validateForm.form()) {
                    var postData = $.aede.pmp.ptmt.project.getModalFormEleVal();
                    if (!postData.id) {
                        $.aede.comm.toastr.error("项目信息丢失，无法保存编辑结果！");
                        return false;
                    }
                    $.ajax({
                        type: "POST",
                        async: false,// 同步请求
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + postData.id + "/update",
                        dataType: "json",
                        data: postData,
                        success: function (data) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("编辑项目成功！");
                                // 隐藏模式窗口
                                $('#pmp_ptmt_project_modal').modal('hide');
                                // 查询用户信息
                                $.aede.pmp.ptmt.project.initProjectTable();
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                            }
                            return false;
                        },
                        error: function () {
                            $.aede.comm.toastr.error("请求异常，编辑项目失败！");
                        }
                    });
                }
            });
        },
        //初始化模式窗口按钮单击事件，项目阶段
        initStageModalBtnClickListener: function () {
            $("#pmp_ptmt_projectstage_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.ptmt.project.stageValidateForm.form()) {
                    var postData = $.aede.pmp.ptmt.project.getStageModalFormEleVal();
                    if (!postData.sid) {
                        $.aede.comm.toastr.error("项目阶段信息丢失，无法保存编辑结果！");
                        return false;
                    }
                    $.ajax({
                        type: "POST",
                        async: false,// 同步请求
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + postData.id + "/updatestage",
                        dataType: "json",
                        data: postData,
                        success: function (data) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("编辑项目阶段成功！");
                                // 隐藏模式窗口
                                $('#pmp_ptmt_projectstage_modal').modal('hide');
                                // 查询用户信息
                                $.aede.pmp.ptmt.project.initProjectStageTable();
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                            }
                            return false;
                        },
                        error: function () {
                            $.aede.comm.toastr.error("请求异常，编辑项目阶段失败！");
                        }
                    });
                }
            });
        },
        //初始化跟踪弹出窗
        initAddFollowModal: function (pid) {
            $("#pmp_ptmt_project_follow_pid").val(pid);
            $("#pmp_ptmt_project_follow_username").val($.aede.expand.comm.loginUser.realName);
            $("#pmp_ptmt_project_follow_time").val(moment(new Date()).format("YYYY-MM-DD"));
            $("#pmp_ptmt_project_follow_content").val("");
            $("#pmp_ptmt_project_follow_modal").modal("show");
            $.aede.pmp.ptmt.project.initSaveAddFollowBtnClickListener();
        },
        getFollowModalData: function () {
            var postData = {};
            var followTime = $.trim($("#pmp_ptmt_project_follow_time").val());
            var username = $.trim($("#pmp_ptmt_project_follow_username").val());
            var content = $.trim($("#pmp_ptmt_project_follow_content").val());
            var pid = $.trim($("#pmp_ptmt_project_follow_pid").val());
            if (!!pid) {
                postData.pid = pid;
            }
            if (!!followTime) {
                postData.followTime = followTime;
            }
            if (!!content) {
                postData.content = content;
            }
            if (!!username) {
                postData.username = username;
            }
            return postData;
        },
        initSaveAddFollowBtnClickListener: function () {
            $("#pmp_ptmt_project_follow_modal_btnSave").unbind("click");
            $("#pmp_ptmt_project_follow_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.ptmt.project.validateFollow.form()) {
                    var postData = $.aede.pmp.ptmt.project.getFollowModalData();
                    $.ajax({
                        type: "POST",
                        async: false,// 同步请求
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/projectFollow/addFollow",
                        dataType: "json",
                        data: postData,
                        success: function (data) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("项目跟踪成功！");
                                // 隐藏模式窗口
                                $('#pmp_ptmt_project_follow_modal').modal('hide');
                                // 查询项目跟踪
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                            }
                            return false;
                        },
                        error: function () {
                            $.aede.comm.toastr.error("请求异常，跟踪失败！");
                        }
                    });
                }
            });
        },
        //初始化模式窗口按钮单击事件，资源
        initResourceModalBtnClickListener: function () {
            $("#pmp_ptmt_projectresource_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.ptmt.project.initResourceFormValidate.form()) {
                    var postData = $.aede.pmp.ptmt.project.getResourceModalFormEleVal();
                    if (!postData.id) {
                        $.aede.comm.toastr.error("项目资源信息丢失，无法保存编辑结果！");
                        return false;
                    }
                    $.ajax({
                        type: "POST",
                        async: false,// 同步请求
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + postData.id + "/updateresource",
                        dataType: "json",
                        data: postData,
                        success: function (data) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("编辑项目阶段成功！");
                                // 隐藏模式窗口
                                $('#pmp_ptmt_projectresource_modal').modal('hide');
                                // 查询资源信息
                                //var id = $("#pmp_ptmt_project_id").val();
                                $.aede.pmp.ptmt.project.initProjectResourceTable();
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                            }
                            return false;
                        },
                        error: function () {
                            $.aede.comm.toastr.error("请求异常，编辑项目阶段失败！");
                        }
                    });
                }
            });
        },
        //获取模式窗口表单元素值
        getModalFormEleVal: function () {
            var postData = {};
            //$.aede.expand.comm.loginUser.userId;
            postData['webUser.id'] = $.aede.expand.comm.loginUser.userId;
            postData['webOrganization.id'] = $.aede.expand.comm.loginUser.orgid;
            postData['webArea.id'] = $.aede.expand.comm.loginUser.areaCode;
            postData.name = $.trim($("#pmp_ptmt_project_name").val());
            postData.type = $.trim($("#pmp_ptmt_project_type").val());
            postData.code = $.trim($("#pmp_ptmt_project_code").val());
            postData.nature = $.trim($("#pmp_ptmt_project_nature").val());
            postData.ctime = $.trim($("#pmp_ptmt_projectstage_ctime").val());
            //postData.manager=$.trim($("#pmp_ptmt_project_manager").val());
            postData.manager = $.aede.expand.comm.loginUser.userId;
            postData.cid = $.trim($("#pmp_ptmt_project_cid").val());
            postData.director = $.trim($("#pmp_ptmt_project_director").val());
            postData.checker = $.trim($("#pmp_ptmt_project_checker").val());
            postData.level = $.trim($("#pmp_ptmt_project_level").val());
            postData.progress = $.trim($("#pmp_ptmt_project_progress").val());
            postData.amt = $.trim($("#pmp_ptmt_project_amt").val());
            postData.status = $.trim($("#pmp_ptmt_project_status").val());
            postData.remark = $.trim($("#pmp_ptmt_project_remark").val());
            postData.uupid = $.trim($("#pmp_ptmt_projectstage_uuid").val());
            postData.perids = $.trim($("#pmp_ptmt_project_perid").val());
            var id = $.trim($("#pmp_ptmt_project_id").val());
            if (!!id) {// 项目的id
                postData.id = id;
            }
            return postData;
        },
        //获取stage模式窗口表单元素值
        getStageModalFormEleVal: function () {
            var postData = {};
            postData.sname = $.trim($("#pmp_ptmt_projectstage_sname").val());
            postData.sseq = $.trim($("#pmp_ptmt_projectstage_sseq").val());
            postData.expbegintime = $.trim($("#pmp_ptmt_projectstage_expbegintime").val());
            postData.expendtime = $.trim($("#pmp_ptmt_projectstage_expendtime").val());
            postData.type = $.trim($("#pmp_ptmt_projectstage_type").val());
            postData.pid = $.trim($("#pmp_ptmt_project_id").val());
            var sid = $.trim($("#pmp_ptmt_projectstage_sid").val());
            if (!!sid) {
                postData.sid = sid;
            }
            return postData;
        },
        //获取resource模式窗口表单元素值
        getResourceModalFormEleVal: function () {
            var postData = {};
            postData.type = $.trim($("#pmp_ptmt_projectresource_type").val());
            postData['project.id'] = $.trim($("#pmp_ptmt_project_id").val());
            postData.pid = $.trim($("#pmp_ptmt_project_id").val());
            postData.sid = stage;
            /* postData.path = $("#pmp_ptmt_projectresource_showWorkCardUpload").val();
             postData.name = $("#pmp_ptmt_projectresource_showWorkCardUploadname").val();
             postData.size = $("#pmp_ptmt_projectresource_showWorkCardUploadsize").val();*/
            postData.path = $("#pmp_ptmt_projectresource_path").val();
            postData.name = $("#pmp_ptmt_projectresource_name").val();
            postData.size = $("#pmp_ptmt_projectresource_size").val();
            var id = $.trim($("#pmp_ptmt_projectresource_id").val());
            if (!!id) {
                postData.id = id;
            }
            return postData;
        },
        // 模式窗口表单验证
        initModalFormValidate: function () {
            var icon = "<i class='fa fa-times-circle'></i> ";
            $.aede.pmp.ptmt.project.validateForm = $('#pmp_ptmt_project_modal form').validate({
                rules: {
                    name: {
                        required: true,
                        maxlength: 225
                    },
                    code: {
                        required: true
                    },
                    ctime: {
                        required: true
                    },
                    stage: {
                        required: true
                    },
                    progress: {
                        maxlength: 14
                    },
                    pmp_ptmt_project_level: {
                        required: true
                    },
                    pmp_ptmt_project_type: {
                        required: true
                    },
                    pmp_ptmt_project_nature: {
                        required: true
                    },
                    director: {
                        maxlength: 11
                    },
                    checker: {
                        maxlength: 11
                    },
                    manager: {
                        required: true,
                        maxlength: 11
                    },
                    amt: {
                        required: true,
                        maxlength: 11
                    },
                    pmp_ptmt_project_status: {
                        required: true
                    },
                    remark: {
                        maxlength: 225
                    }
                },
                messages: {
                    name: {
                        required: "请填写项目名称！",
                        maxlength: "项目名称长度不可超过225！"
                    },
                    code: {
                        required: "请填写项目编号！"
                    },
                    ctime: {
                        required: "请填写建立日期！"
                    },
                    stage: {
                        required: "请填写当前阶段！"
                    },
                    progress: {
                        maxlength: ""
                    },
                    pmp_ptmt_project_level: {
                        required: "请填写项目等级！"
                    },
                    pmp_ptmt_project_type: {
                        required: "请填写项目类别！"
                    },
                    pmp_ptmt_project_nature: {
                        required: "请填写项目性质！"
                    },
                    director: {
                        maxlength: "负责人姓名长度不可超过5！"
                    },
                    checker: {
                        maxlength: "复核人姓名长度不可超过5！"
                    },
                    manager: {
                        required: "项目经理是必填项！",
                        maxlength: "项目经理姓名长度不可超过5！"
                    },
                    amt: {
                        required: "金额是必填项！",
                        maxlength: "金额长度不可超过11！"
                    },
                    pmp_ptmt_project_status: {
                        required: "状态是必填项！"
                    },
                    remark: {
                        maxlength: "备注长度不可超过110！"
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
        // 表单验证,阶段
        initStageFormValidate: function () {
            var icon = "<i class='fa fa-times-circle'></i> ";
            jQuery.validator.methods.compareDate = function (value, element, param) {
                var startDate = jQuery(param).val();
                //var date1 = new Date(Date.parse(startDate.replace("-", "/")));
                //var date2 = new Date(Date.parse(value.replace("-", "/")));
                return startDate <= value;
            };
            $.aede.pmp.ptmt.project.stageValidateForm = $('#pmp_ptmt_projectstage_modal form').validate({
                rules: {
                    pmp_ptmt_projectstage_sname: {
                        required: true
                    },
                    sseq: {
                        required: true,
                        digits: true,
                        min: 0
                    },
                    expbegintime: {
                        required: true
                    },
                    expendtime: {
                        required: true,
                        compareDate: "#pmp_ptmt_projectstage_expbegintime"
                    }/*,
                     pmp_ptmt_projectstage_type : {
                     required : true
                     }*/
                },
                messages: {
                    pmp_ptmt_projectstage_sname: {
                        required: "请填写阶段名称！"
                    },
                    sseq: {
                        required: "请填写阶段序号！",
                        digits: "阶段序号必须为正整数！",
                        min: "阶段序号必须为正整数！"
                    },
                    expbegintime: {
                        required: "请填写阶段预计开始日期！"
                    },
                    expendtime: {
                        required: "请填写阶段预计结束日期！",
                        compareDate: "阶段预计结束日期不能小于阶段预计开始日期！"
                    }/*,
                     pmp_ptmt_projectstage_type : {
                     required : "请填写阶段类型！"
                     }*/
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
        // 表单验证,跟踪
        initFollowFormValidate: function () {
            var icon = "<i class='fa fa-times-circle'></i> ";
            jQuery.validator.methods.compareDate = function (value, element, param) {
                var startDate = jQuery(param).val();
                return startDate <= value;
            };
            $.aede.pmp.ptmt.project.validateFollow = $('#pmp_ptmt_project_follow_modal form').validate({
                rules: {
                    followTime: {
                        required: true
                    },
                    content: {
                        required: true
                    }
                },
                messages: {
                    mname: {
                        required: "请选择跟踪时间！",
                    },
                    mmanager: {
                        required: "请填写跟踪内容！"
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
        cleanModalFormValidateStyle: function () {
            if (!!$.aede.pmp.ptmt.project.validateForm) {
                // 清除表单验证样式
                $.aede.pmp.ptmt.project.validateForm.resetForm();
                // XXX jQuery Validate resetFrom不能清除highlight添加的样式，所有需要手动清理
                $('#pmp_ptmt_project_modal form .form-group').removeClass('has-error');
                $('#pmp_ptmt_project_modal form .form-group').removeClass('has-success');
            }
        },
        cleanStageFormValidateStyle: function () {
            if (!!$.aede.pmp.ptmt.project.stageValidateForm) {
                // 清除表单验证样式
                $.aede.pmp.ptmt.project.stageValidateForm.resetForm();
                // XXX jQuery Validate resetFrom不能清除highlight添加的样式，所有需要手动清理
                $('#pmp_ptmt_projectstage_modal form .form-group').removeClass('has-error');
                $('#pmp_ptmt_projectstage_modal form .form-group').removeClass('has-success');
            }
        },
        //删除项目
        deleteProject: function (id) {
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
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + id + "/delete",
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        type: "POST",
                        async: false,// 同步请求
                        success: function (data, textStatus) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("删除项目成功");
                                $.aede.pmp.ptmt.project.initProjectTable();
                                return;
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                                return;
                            }
                        },
                        error: function (XMLHttpRequest, textStatus) {
                            $.aede.comm.toastr.error("请求异常，删除项目失败！");
                        }
                    });

                } else {
                    swal("取消删除！", "保留数据。", "success");
                }
            });
        },

        //删除资源
        delProjectResource: function (id) {
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
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + id + "/deleteresource",
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        type: "POST",
                        async: false,// 同步请求
                        success: function (data, textStatus) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("删除资源成功");
                                //var id = $("#pmp_ptmt_project_id").val();
                                $.aede.pmp.ptmt.project.initProjectResourceTable();
                                return;
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                                return;
                            }
                        },
                        error: function (XMLHttpRequest, textStatus) {
                            $.aede.comm.toastr.error("请求异常，删除资源失败！");
                        }
                    });

                } else {
                    swal("取消删除！", "保留资源。", "success");
                }
            });
        },
        //
        initStageTab: function (id) {
            $.ajax({
                type: "GET",
                async: false,// 同步请求
                url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + id + "/querytab",
                dataType: "json",
                success: function (data) {
                    if (data.length > 0) {
                        $('#tt').show();
                        for (var i = 0; i < data.length; i++) {
                            if (i == 0) {
                                stage = data[0].sid;
                            }
                            resultArray[i] = new Array();
                            addTab($.aede.expand.comm.getDictName('阶段名称', data[i].sname), data[i].sseq + 1);
                            resultArray[i][0] = data[i].sid;
                            //resultArray[i][1]=data[i].sname;
                            resultArray[i][1] = $.aede.expand.comm.getDictName('阶段名称', data[i].sname);
                            $('#pmp_ptmt_project_resourcetablediv').show();
                        }
                    } else {
                        $('#tt').hide();
                        $('#pmp_ptmt_project_resourcetablediv').hide();
                    }
                    return false;
                },
                error: function () {
                    $.aede.comm.toastr.error("tab加载异常！");
                }
            });
        },
        //
        initFileInfo: function (fileData) {
            /*var updTableHtml="";
             $("#pmp_ptmt_projectresource_showWorkCardUpload").val(fileData.url);
             $("#pmp_ptmt_projectresource_showWorkCardUploadname").val(fileData.fileNames);
             $("#pmp_ptmt_projectresource_showWorkCardUploadsize").val(fileData.fileSize);
             updTableHtml += "<tr id='tld" + imgCount + "'><td align='left'>" + "<a href='"+$.aede.expand.getContextPath()+"/upload/"+fileData.url+"'  style='text-decoration:none;' >"+ fileData.fileNames+"</a></td></tr>";
             $("#pmp_ptmt_projectresource_updTablework").html(updTableHtml);*/
            $("#pmp_ptmt_projectresource_name").val(fileData.fileNames);
            $("#pmp_ptmt_projectresource_path").val(fileData.url);
            $("#pmp_ptmt_projectresource_size").val(fileData.fileSize);
            var postData = $.aede.pmp.ptmt.project.getResourceModalFormEleVal();
            if (!!postData.id) {
                // 新建时，id应该为空
                $.aede.comm.toastr.error("阶段的编号异常，无法新建！");
                return false;
            }
            $.ajax({
                type: "POST",
                async: false,// 同步请求
                url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/addresource",
                dataType: "json",
                data: postData,
                success: function (data) {
                    if (data["responseCode"] == "1") {
                        $.aede.comm.toastr.success("新建项目资源成功！");
                        // 隐藏模式窗口
                        $('#pmp_ptmt_projectresource_modal').modal('hide');
                        // 查询信息
                        //var id = $("#pmp_ptmt_project_id").val();
                        $.aede.pmp.ptmt.project.initProjectResourceTable();
                    } else {
                        $.aede.comm.toastr.error(data["responseMsg"]);
                    }
                    return false;
                },
                error: function () {
                    $.aede.comm.toastr.error("请求异常，新建失败！");
                }
            });
        },
        initdownResource: function () {

        },
        // 提出人员选择单击事件
        initProposeManClick: function () {
            $("#pmp_ptmt_project_pername").bind("click", function () {
                $("#jail-detain-addPrsArrgInfo-proposeManModal").modal("show");
                // 人员查询按钮点击事件
                $.aede.pmp.ptmt.project.initProposeManSearchBtnClickListener();
                // 人员重置按钮点击事件
                $.aede.pmp.ptmt.project.initProposeManResetBtnClickListener();
                $.aede.pmp.ptmt.project.initProposeManTable();
                $(".panel-body").remove();
            });
        },
        // 初始化提出人员表
        initProposeManTable: function () {
            if (!$.aede.pmp.ptmt.project.proposeManTable) {
                // 初始化proposeManTable
                $.aede.pmp.ptmt.project.proposeManTable = $("#jail-detain-addPrsArrgInfo-proposeManTable")
                    .DataTable(
                        {
                            serverSide: true,
                            bLengthChange: false,
                            ajax: {
                                url: $.aede.expand.getContextPath()
                                + '/management/pmp/ptmt/project/queryuserlist',
                                type: 'POST',
                                dataType: 'json',
                                data: function (d) {
                                    var realname = $.trim($("#jail-detain-addPrsArrgInfo-proposeMan-policeName").val());
                                    d.search_LIKE_realname = realname;
                                }
                            },
                            columns: [{
                                data: "realname"
                            }, {
                                data: "organization.organname"
                            }, {
                                data: "id"
                            }],
                            columnDefs: [
                                {
                                    'targets': [2],
                                    'render': function (data, type, full) {
                                        return "<a href=\"javascript:void(0);\" onclick=\"$.aede.pmp.ptmt.project.nameChosed('"
                                            + full.realname + "','" + full.id + "')\" class=\"btn default btn-xs red-stripe\">选择</a>";
                                    }
                                }]

                        });
            } else {
                $.aede.pmp.ptmt.project.proposeManTable.ajax.reload();
            }
        },
        // 单个人员选择
        nameChosed: function (name, id) {
            var names = "";
            var ids = "";
            $(".panel-body").each(function () {
                names += $(this).text();
                ids += $(this).text();
            });
            if (names.indexOf(name) == -1) {
                $("#jail-detain-addPrsArrgInfo-proposeMan-namePanel").append("<div class='panel-body'>" + name +
                    "<input type='hidden' name='perhidid' value='" + id + "'>" +	 // class='btn btn-info btn-xs
                    "<button style='float:right' type='button' onclick='$(this).parent().remove();' class='btn btn-danger btn-xs'><i class='fa fa-trash-o'></i></button></div>");
                return false;
            } else {
                return false;
            }
        },
        // 人员确认选择
        initProposeManChose: function () {
            $("#jail-detain-addPrsArrgInfo-proposeMan-btnSure").bind("click", function () {
                //$("#jail-detain-addPrsArrgInfo-proposeManModal").hide();
                var names = "";
                var ids = "";
                $(".panel-body").each(function () {
                    names += $(this).text() + ",";
                });
                $("input[name='perhidid']").each(function () {
                    ids += $(this).val() + ",";
                });
                $("#pmp_ptmt_project_pername").val(names.substring(0, names.length - 1));
                $("#pmp_ptmt_project_perid").val(ids.substring(0, ids.length - 1));
            });
        },
        // 人员查询按钮点击事件
        initProposeManSearchBtnClickListener: function () {
            $("#jail-detain-addPrsArrgInfo-proposeMan-btnSearch").click(function () {
                $.aede.pmp.ptmt.project.initProposeManTable();
                return false;
            });
        },
        // 人员重置按钮点击事件
        initProposeManResetBtnClickListener: function () {
            $("#jail-detain-addPrsArrgInfo-proposeMan-btnReset").click(function () {
                $("#jail-detain-addPrsArrgInfo-proposeManModal input").val("");
                $("#jail-detain-addPrsArrgInfo-proposeManModal-isPinYin").removeAttr("checked");
                return false;
            });
        }

    }
    $.aede.comm.callBack = function () {
        $.aede.pmp.ptmt.project.initProjectTable();
        // $.aede.comm.toastr.success("保存成功！");
    }

})(jQuery);
$(document).ready(function () {
    $.aede.pmp.ptmt.project.init();

    laydate({
        elem: '#pmp_ptmt_projectstage_expbegintime',
        event: 'focus'
    });
    laydate({
        elem: '#pmp_ptmt_projectstage_expendtime',
        event: 'focus'
    });
    laydate({
        elem: '#pmp_ptmt_project_follow_time',
        event: 'focus'
    });

    $('#tt').tabs({
        plain: true,
        border: false,
        //fit:true,
        onSelect: function (title) {
            for (var i = 0; i < resultArray.length; i++) {
                if (title == resultArray[i][1]) {
                    stage = resultArray[i][0];
                }
            }
            $.aede.pmp.ptmt.project.initProjectResourceTable();
        }
    });
});

function GetDateT(date) {
    var d, s;
    d = new Date(date);
    s = d.getFullYear() + "-";
    s = s + (d.getMonth() + 1) + "-";//取月份
    s += d.getDate() + " ";         //取日期

    return (s);
}

function addTab(title, url) {
    $('#tt').tabs('add', {
        title: title,
        closable: false
    });

}
function closeAll() { //关闭全部
    for (var i = 0; i < resultArray.length; i++) {
        $('#tt').tabs('close', resultArray[i][1]);
    }
}



