var stage = "";
var resultArray = new Array();
var rpid;
var Num = "";
var names = "";   //参与人员名称字符串
var ids = "";    //参与人员id字符串
(function ($) {
    $.aede.pmp.ptmt.projectedit = {
        projectresourceTable: "",//项目资源表格
        projectFollowTable: "",//项目跟踪表格
        selectSingleManTable: "",//负责人、复核人
        proposeManTable: "",//项目人员
        uploader: "",
        validateForm: "",
        validatemission: "",
        validateFollow: "",
        singleFlag: "",
        init: function () {
            //$.aede.pmp.ptmt.projectedit.initBtnListener();
            // 模式窗口表单验证
            $.aede.pmp.ptmt.projectedit.initModalFormValidate();
            $.aede.pmp.ptmt.projectedit.initResourceFormValidate();
            $.aede.pmp.ptmt.projectedit.initMissionFormValidate();
            $.aede.pmp.ptmt.projectedit.initFollowFormValidate();
            $.aede.pmp.ptmt.projectedit.showProjectEditModal();
            $.aede.pmp.ptmt.projectedit.initProposeManClick();
            $.aede.pmp.ptmt.projectedit.initProposeManChose();
            $.aede.pmp.ptmt.projectedit.initDirectorClickListener();//负责人、复核人点击按钮事件
            $.aede.pmp.ptmt.projectedit.initSelectSingleManSearchBtnClickListener();//查询按钮点击事件
            $.aede.pmp.ptmt.projectedit.initSelectSingleManResetBtnClickListener();//重置按钮点击事件
            $.aede.pmp.ptmt.projectedit.initStatusChangeListener();//项目状态变化事件
        },
        //初始化数据表格，资源
        initProjectResourceTable: function () {
            $("#pmp_ptmt_project_resource_query_pid").val(rpid);
            $("#pmp_ptmt_project_resource_query_stage").val(stage);
            if (!$.aede.pmp.ptmt.projectedit.projectresourceTable) {
                // 初始化Table
                $.aede.pmp.ptmt.projectedit.projectresourceTable = $("#pmp_ptmt_project_resourcetable").DataTable({
                    serverSide: true,
                    ajax: {
                        url: $.aede.expand.getContextPath() + "/management/pmp/rsmt/resource/queryByPaging",
                        type: 'POST',
                        dataType: 'json',
                        data: function (d) {
                            var pid = $("#pmp_ptmt_project_resource_query_pid").val();
                            var stage = $("#pmp_ptmt_project_resource_query_stage").val();
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
                            data: "typeName"
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
                            "targets": [3],
                            "render": function (data, event, full) {
                                return GetDateT(data);
                            }
                        }, {
                            "targets": [4],
                            "render": function (data, event, full) {
                                //var company =$.aede.expand.comm.loginUser.orgid;
                                //var procode =$("#pmp_ptmt_project_code").val();
                                var link = $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/" + data + "/dl";
                                return "<a type='button' class='btn btn-primary btn-xs' href=" + link + ">下载</a>" +
                                    '&nbsp;&nbsp;<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.projectedit.delProjectResource(\'' + data + '\');">删除</button>';
                            }
                        }
                    ]
                });
            } else {
                $.aede.pmp.ptmt.projectedit.projectresourceTable.ajax.reload();
            }
        },
        //初始化数据表格，任务
        initProjectMissionTable: function () {
            $("#pmp_ptmt_project_resource_query_pid").val(rpid);
            $("#pmp_ptmt_project_resource_query_stage").val(stage);
            if (!$.aede.pmp.ptmt.projectedit.projectmissionTable) {
                // 初始化Table
                $.aede.pmp.ptmt.projectedit.projectmissionTable = $("#pmp_ptmt_project_missiontable").DataTable({
                    serverSide: true,
                    ajax: {
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/mission/queryByPaging",
                        type: 'POST',
                        dataType: 'json',
                        data: function (d) {
                            var pid = $("#pmp_ptmt_project_resource_query_pid").val();
                            var stage = $("#pmp_ptmt_project_resource_query_stage").val();
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
                            data: "mname"
                        },
                        {
                            data: "mmanager"
                        },
                        {
                            data: "mstartdate"
                        },
                        {
                            data: "menddate"
                        },
                        {
                            data: "mprogress"
                        },
                        {
                            data: "id"
                        }
                    ],
                    columnDefs: [
                        {
                            "targets": [0],
                            "render": function (data, event, full) {
                                var newData = data;
                                if (newData.length > 17)
                                    newData = newData.substring(0, 15) + "...";
                                return "<span title=\"" + data + "\">" + newData + "</span>";
                            }
                        }, {
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
                                return !!data ? data + "%" : "";
                            }
                        }, {
                            "targets": [5],
                            "render": function (data, event, full) {
                                return '<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.projectedit.showProjectMissionEditModal(\'' + data + '\');">编辑</button>' +
                                    '&nbsp;&nbsp;<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.projectedit.delProjectMission(\'' + data + '\');">删除</button>';
                            }
                        }
                    ]
                });
            } else {
                $.aede.pmp.ptmt.projectedit.projectmissionTable.ajax.reload();
            }
        },

        initAddProjectFollowSaveBtnClickListener: function () {
            $("#pmp_ptmt_project_follow_modal_btnSave").unbind("click");
            $("#pmp_ptmt_project_follow_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.ptmt.projectedit.validateFollow.form()) {
                    var postData = $.aede.pmp.ptmt.projectedit.getFollowModalData();
                    $.ajax({
                        type: "POST",
                        async: false,// 同步请求
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/projectFollow/addFollow",
                        dataType: "json",
                        data: postData,
                        success: function (data) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("新建项目跟踪成功！");
                                // 隐藏模式窗口
                                $('#pmp_ptmt_project_follow_modal').modal('hide');
                                // 查询项目跟踪
                                $.aede.pmp.ptmt.projectedit.initProjectFollowTable();
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                            }
                            return false;
                        },
                        error: function () {
                            $.aede.comm.toastr.error("请求异常，新建失败！");
                        }
                    });
                }
            });
        },
        getFollowModalData: function () {
            var postData = {};
            var followTime = $.trim($("#pmp_ptmt_project_follow_time").val());
            var username = $.trim($("#pmp_ptmt_project_follow_username").val());
            var content = $.trim($("#pmp_ptmt_project_follow_content").val());
            var id = $.trim($("#pmp_ptmt_project_follow_hidId").val());
            if (!!id) {
                postData.id = id;
            }
            if (!!followTime) {
                postData.followTime = followTime;
            }
            if (!!content) {
                postData.content = content;
            }
            postData.pid = $("#pmp_ptmt_proedit_proid").val();
            if (!!username) {
                postData.username = username;
            }
            return postData;
        },
        initEditProjectFollowSaveBtnClickListener: function () {
            $("#pmp_ptmt_project_follow_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.ptmt.projectedit.validateFollow.form()) {
                    var postData = $.aede.pmp.ptmt.projectedit.getFollowModalData();
                    $.ajax({
                        type: "POST",
                        async: false,// 同步请求
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/projectFollow/" + postData.id + "/update",
                        dataType: "json",
                        data: postData,
                        success: function (data) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("编辑项目跟踪成功！");
                                // 隐藏模式窗口
                                $('#pmp_ptmt_project_follow_modal').modal('hide');
                                // 查询项目跟踪
                                $.aede.pmp.ptmt.projectedit.initProjectFollowTable();
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                            }
                            return false;
                        },
                        error: function () {
                            $.aede.comm.toastr.error("请求异常，编辑失败！");
                        }
                    });
                }
            });
        },

        //初始化跟踪表格
        initProjectFollowTable: function () {
            if (!$.aede.pmp.ptmt.projectedit.projectFollowTable) {
                // ³õÊ¼»¯Table
                $.aede.pmp.ptmt.projectedit.projectFollowTable = $("#pmp_ptmt_project_followtable").DataTable({
                    serverSide: true,
                    ajax: {
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/projectFollow/queryByPaging",
                        type: 'POST',
                        dataType: 'json',
                        data: function (d) {
                            var pid = $("#pmp_ptmt_project_resource_query_pid").val();
                            if (!!pid) {
                                d.search_EQ_pid = pid;
                            }
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
                    columnDefs: [
                        {
                            "targets": [3],
                            "render": function (data, event, full) {
                                return '<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.projectedit.showProjectFollowEditModal(\'' + data + '\');">编辑</button>' +
                                    '&nbsp;&nbsp;<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.projectedit.delProjectFollow(\'' + data + '\');">删除</button>';
                            }
                        }
                    ]
                });
            } else {
                $.aede.pmp.ptmt.projectedit.projectFollowTable.ajax.reload();
            }
        },
        //编辑跟踪窗口
        showProjectFollowEditModal: function (id) {
            $.ajax({
                type: "GET",
                async: false,// 同步请求
                url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/projectFollow/" + id + "/view",
                dataType: "json",
                success: function (data) {
                    if (data["responseCode"] == "1") {
                        var result = data["datas"];
                        $("#pmp_ptmt_project_follow_hidId").val(id);
                        $("#pmp_ptmt_project_follow_time").val(result.followTime);
                        $("#pmp_ptmt_project_follow_username").val(result.username);
                        $("#pmp_ptmt_project_follow_content").val(result.content);
                        $.aede.pmp.ptmt.projectedit.initEditProjectFollowSaveBtnClickListener();//保存编辑跟踪按钮点击事件
                        $("#pmp_ptmt_project_follow_modal").modal("show");
                    } else {
                        $.aede.comm.toastr.error(data["responseMsg"]);
                    }
                    return false;
                },
                error: function () {
                    $.aede.comm.toastr.error("请求异常，查询项目跟踪失败！");
                }
            });
        },
        // 初始化模式窗口
        initModal: function () {
            // 清空模式窗口表单元素值
            $.aede.pmp.ptmt.projectedit.cleanModalFormEleVal();
            // 清除表单验证样式
            $.aede.pmp.ptmt.projectedit.cleanModalFormValidateStyle();

            // 清空保存按钮点击事件
            $("#pmp_ptmt_project_modal_btnSave").unbind("click");
            //$("#pmp_ptmt_projectstage_modal_btnSave").unbind("click");
            // 显示保存按钮
            $('#pmp_ptmt_project_modal_btnSave').show();
            //$('#pmp_ptmt_projectstage_modal_btnSave').show();
            // 清除disabled样式
            $("#pmp_ptmt_project_modal form input").attr("disabled", false);
            $("#pmp_ptmt_project_modal form textarea").attr("disabled", false);
            // $("#pmp_ptmt_project_manager").attr("disabled", true);
            $("#pmp_ptmt_project_code").attr("disabled", true);
            //$("#pmp_ptmt_projectstage_modal form input").attr("disabled",false);
        },

        // 初始化resource模式窗口
        initResourceModal: function () {
            // 清空模式窗口表单元素值
            $.aede.pmp.ptmt.projectedit.cleanResourceModalFormEleVal();
            // 清除表单验证样式
            $.aede.pmp.ptmt.projectedit.cleanResourceFormValidateStyle();
            // 清空保存按钮点击事件
            $("#pmp_ptmt_projectresource_modal_btnSave").unbind("click");
            // 显示保存按钮
            $('#pmp_ptmt_projectresource_modal_btnSave').show();
            // 清除disabled样式
            $("#pmp_ptmt_projectresource_modal form input").attr("disabled", false);
        },
        // 初始化任务窗口
        initMissionModal: function () {
            // 清空模式窗口表单元素值
            $.aede.pmp.ptmt.projectedit.cleanMissionModalFormEleVal();
            // 清除表单验证样式
            $.aede.pmp.ptmt.projectedit.cleanMissionFormValidateStyle();
            // 清空保存按钮点击事件
            $("#pmp_ptmt_projectmission_modal_btnSave").unbind("click");
            $.aede.pmp.ptmt.projectedit.getSelectData(rpid);
            // 显示保存按钮
            $('#pmp_ptmt_projectmission_modal_btnSave').show();
            // 清除disabled样式
            $("#pmp_ptmt_projectmission_modal form input").attr("disabled", false);
        },

        // 清空模式窗口表单元素值
        cleanModalFormEleVal: function () {
            // 清空隐藏域
            $("#pmp_ptmt_project_id").val("");
            stage = "";//初始化
            //deleteNode();
            closeAll();
            resultArray.length = 0;
            $("#pmp_ptmt_project_modal form")[0].reset();// 重置表单
        },
        //清除资源表单元素值
        cleanResourceModalFormEleVal: function () {
            // 清空隐藏域
            $("#pmp_ptmt_projectresource_id").val("");
            $("#pmp_ptmt_projectresource_updTablework").html("");
            $("#pmp_ptmt_projectresource_modal form")[0].reset();// 重置表单
        },
        //清除任务表单元素值
        cleanMissionModalFormEleVal: function () {
            // 清空隐藏域
            $("#pmp_ptmt_projectmission_id").val("");
            $("#pmp_ptmt_projectmission_modal form")[0].reset();// 重置表单
            //getSelectData
            $(".selectpicker").selectpicker('val', '');//下拉多选赋值
            document.getElementById('pmp_ptmt_projectmission_mmanager').innerHTML = "";//清空原有下拉框值
        },

        //新增资源页
        addProjectResourceModal: function () {
            $("#pmp_ptmt_projectresource_modal .modal-header .modal-title").html("新建资源");
            $.aede.pmp.ptmt.projectedit.initResourceModal();
            $.aede.pmp.ptmt.projectedit.initSaveAddProjectResourceBtnClickListener();
            var company = $.aede.expand.comm.loginUser.orgid;
            var procode = $("#pmp_ptmt_project_code").val();
            $.aede.pmp.ptmt.projectedit.uploader = $.aede.pmp.rsmt.resourceUploader.initUploader(company, procode, "pmp_ptmt_projectresource_uploader", $.aede.pmp.ptmt.projectedit.initFileInfo);
            $("#pmp_ptmt_projectresource_modal").modal("show");
        },
        //新增任务页
        addProjectMissionModal: function () {
            $("#pmp_ptmt_projectresource_modal .modal-header .modal-title").html("新建任务");
            $.aede.pmp.ptmt.projectedit.initMissionModal();
            $.aede.pmp.ptmt.projectedit.initSaveAddProjectMissionBtnClickListener();
            $("#pmp_ptmt_projectmission_modal").modal("show");
        },
        //新增跟踪页
        addProjectFollowModal: function () {
            $("#pmp_ptmt_project_follow_username").val($.aede.expand.comm.loginUser.realName);
            $("#pmp_ptmt_project_follow_time").val(moment(new Date()).format("YYYY-MM-DD"));
            $("#pmp_ptmt_project_follow_hidId").val("");
            $("#pmp_ptmt_project_follow_content").val("");
            $.aede.pmp.ptmt.projectedit.initAddProjectFollowSaveBtnClickListener();//保存新增跟踪按钮点击事件
            $("#pmp_ptmt_project_follow_modal").modal("show");
        },
        // 初始化保存新建按钮的click事件,项目资源
        initSaveAddProjectResourceBtnClickListener: function () {
            $("#pmp_ptmt_projectresource_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.ptmt.projectedit.validateresource.form()) {
                    $.aede.pmp.ptmt.projectedit.uploader.upload();

                } else {
                    $.aede.comm.toastr.warning("请正确填写项目资源信息！");
                }
            });
        },
        //初始化保存新建按钮的click事件,项目任务
        initSaveAddProjectMissionBtnClickListener: function () {
            $("#pmp_ptmt_projectmission_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.ptmt.projectedit.validatemission.form()) {
                    var postData = $.aede.pmp.ptmt.projectedit.getMissionModalFormEleVal();
                    if (!!postData.id) {
                        // 新建时，id应该为空
                        $.aede.comm.toastr.error("任务的编号异常，无法新建！");
                        return false;
                    }
                    $.ajax({
                        type: "POST",
                        async: false,// 同步请求
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/mission/add",
                        dataType: "json",
                        data: postData,
                        success: function (data) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("新建项目任务成功！");
                                // 隐藏模式窗口
                                $('#pmp_ptmt_projectmission_modal').modal('hide');
                                // 查询风险源登记信息
                                $.aede.pmp.ptmt.projectedit.initProjectMissionTable();
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
                    $.aede.comm.toastr.warning("请正确填写项目任务信息！");
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
                        $("#pmp_ptmt_project_ctime").val(result.ctime);
                        $("#pmp_ptmt_project_manager").val(result.manager.realname);
                        $("#pmp_ptmt_project_hidemanagerid").val(result.manager.id);
                        $("#pmp_ptmt_project_cid").val(result.cid);
                        $("#pmp_ptmt_project_director").val(result.director);
                        $("#pmp_ptmt_project_checker").val(result.checker);
                        $("#pmp_ptmt_project_level").val(result.level);
                        var progress = result.progress;
                        var start = 0.00;
                        if (!!progress) {
                            start = progress;
                        }
                        $("#pmp_ptmt_project_progress").noUiSlider({
                            direction: (Metronic.isRTL() ? "rtl" : "ltr"),
                            start: start,
                            connect: 'lower',
                            range: {
                                'min': 0,
                                '1%': [1, 1],
                                'max': 100
                            }
                        });
                        $("#pmp_ptmt_project_progress").Link('lower').to($("#pmp_ptmt_project_progress_span"));
                        // $("#pmp_ptmt_project_progress").val(result.progress);
                        $("#pmp_ptmt_project_amt").val(result.amt);
                        if (result.status == 1) {
                            $("#pmp_ptmt_project_signingProbability").attr("readonly", false);
                        }
                        $("#pmp_ptmt_project_status").val(result.status);
                        $("#pmp_ptmt_project_remark").val(result.remark);
                        $("#pmp_ptmt_project_id").val(result.id);
                        $("#pmp_ptmt_project_perid").val(result.perids);
                        $("#pmp_ptmt_project_source").val(result.projectSource);
                        $("#pmp_ptmt_project_stakeholder").val(result.stakeholder);
                        $("#pmp_ptmt_project_signingProbability").val(result.signingProbability);
                        var pernames = result.pernames;
                        $("#pmp_ptmt_project_pername").val(pernames);


                        $("#pmp_ptmt_project_totalPackage").val(result.totalPackage);
                        $("#pmp_ptmt_project_subPackage").val(result.subPackage);
                        $("#pmp_ptmt_project_expectedTime").val(result.expectedTime);

                        $("#pmp_ptmt_project_assistanceUnit").val(result.assistanceUnit);
                        $("#pmp_ptmt_project_assistanceContent").val(result.assistanceContent);
                        $("#pmp_ptmt_project_signingBudget").val(result.signingBudget);


                        rpid = result.id;
                        $.aede.pmp.ptmt.projectedit.initStageTab(rpid);
                        $.aede.pmp.ptmt.projectedit.initProjectResourceTable();
                        $.aede.pmp.ptmt.projectedit.initProjectMissionTable();
                        $.aede.pmp.ptmt.projectedit.initProjectFollowTable();


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
        //初始化模式窗口表单信息，项目任务
        initMissionModalFormEleValById: function (id) {
            $.ajax({
                type: "GET",
                async: false,// 同步请求
                url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/mission/" + id + "/view",
                dataType: "json",
                success: function (data) {
                    if (data["responseCode"] == "1") {
                        var result = data["datas"];
                        $("#pmp_ptmt_projectmission_mname").val(result.mname);
                        var mmanager = result.mmanager;
                        var pers = "";
                        if (mmanager != "") {
                            var mmanagers = result.mmanager.split(",");
                            $(".selectpicker").selectpicker('val', mmanagers);//下拉多选赋值
                            $('.selectpicker').selectpicker('refresh');
                        }
                        //$("#pmp_ptmt_projectmission_mmanager").val(mmanagers);
                        $("#pmp_ptmt_projectmission_mstartdate").val(result.mstartdate);
                        $("#pmp_ptmt_projectmission_menddate").val(result.menddate);
                        $("#pmp_ptmt_projectmission_mprogress").val(result.mprogress);
                        $("#pmp_ptmt_projectmission_id").val(result.id);
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
            // 初始化模式窗口
            $.aede.pmp.ptmt.projectedit.initModal();
            var id = $("#pmp_ptmt_proedit_proid").val();
            if (!!id) {
                $.aede.pmp.ptmt.projectedit.initModalFormEleValById(id);
                //tab加载在数据加载里
                // 初始化保存编辑角色维护按钮的click事件(该按钮的其他点击事件已经在initModal时被清除)
                $.aede.pmp.ptmt.projectedit.initModalBtnClickListener();
                $('#pmp_ptmt_project_stagetablediv').hide();
            } else {
                $.aede.comm.toastr.error("项目的id丢失，无法查询！");
            }
        },
        //显示编辑模式窗口,资源
        showProjectResourceEditModal: function (id) {
            // 初始化模式窗口
            $.aede.pmp.ptmt.projectedit.initResourceModal();
            if (!!id) {
                $.aede.pmp.ptmt.projectedit.initResourceModalFormEleValById(id);
                // 初始化保存编辑角色维护按钮的click事件(该按钮的其他点击事件已经在initModal时被清除)
                $.aede.pmp.ptmt.projectedit.initResourceModalBtnClickListener();
                $("#pmp_ptmt_projectresource_modal").modal("show");
            } else {
                $.aede.comm.toastr.error("项目资源的id丢失，无法查询！");
            }
        },
        //显示编辑模式窗口,任务
        showProjectMissionEditModal: function (id) {
            // 初始化模式窗口
            $.aede.pmp.ptmt.projectedit.initMissionModal();
            if (!!id) {
                $.aede.pmp.ptmt.projectedit.initMissionModalFormEleValById(id);
                // 初始化保存编辑角色维护按钮的click事件(该按钮的其他点击事件已经在initModal时被清除)
                $.aede.pmp.ptmt.projectedit.initMissionModalBtnClickListener();
                $("#pmp_ptmt_projectmission_modal").modal("show");
            } else {
                $.aede.comm.toastr.error("项目任务的id丢失，无法查询！");
            }
        },

        //初始化模式窗口按钮单击事件,编辑项目保存
        initModalBtnClickListener: function () {
            $("#pmp_ptmt_project_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.ptmt.projectedit.validateForm.form()) {
                    var postData = $.aede.pmp.ptmt.projectedit.getModalFormEleVal();
                    if (!postData.id) {
                        $.aede.comm.toastr.error("项目信息丢失，无法保存编辑结果！");
                        return false;
                    }
                    postData.pid = postData.id;
                    $.ajax({
                        type: "POST",
                        async: false,// 同步请求
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/project/" + postData.id + "/update",
                        dataType: "json",
                        data: postData,
                        success: function (data) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("编辑项目成功！");
                                setTimeout(function () {
                                    $.aede.comm.closeTab()
                                }, 2000);
                                //发送邮件
                                // var alldatas = data["datas"].split("-");
                                // for (var i = 0; i < alldatas.length - 1; i++) {// old  del add
                                //     if (alldatas[i] != "") {
                                //         var idatas = alldatas[i].split(",");
                                //         if (idatas.length > 0) {
                                //             for (var k = 0; k < idatas.length; k++) {
                                //                 if (idatas[k] != "" && i == 0) {//old,项目信息变更 1
                                //                     $.aede.pmp.comm.mail.sendPTP("项目编辑通告", alldatas[3] + "更新了部分信息", idatas[k], 1);
                                //                 }
                                //                 if (idatas[k] != "" && i == 1) {//del 人员变更 2
                                //                     $.aede.pmp.comm.mail.sendPTP("移出项目团队通告", "你被移出了" + alldatas[3], idatas[k], 2);
                                //                 }
                                //                 if (idatas[k] != "" && i == 2) {//add 人员变更 2
                                //                     $.aede.pmp.comm.mail.sendPTP("加入项目团队通告", "你被拉入了" + alldatas[3], idatas[k], 2);
                                //                 }
                                //             }
                                //         }
                                //     }
                                // }
                                // //向项目经理发送，项目信息变更 1
                                // var managerid = $("#pmp_ptmt_project_hidemanagerid").val();
                                // $.aede.pmp.comm.mail.sendPTP("项目编辑通告", alldatas[3] + "更新了部分信息", managerid, 1);

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
        //初始化模式窗口按钮单击事件，资源
        initResourceModalBtnClickListener: function () {
            $("#pmp_ptmt_projectresource_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.ptmt.projectedit.initResourceFormValidate.form()) {
                    var postData = $.aede.pmp.ptmt.projectedit.getResourceModalFormEleVal();
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
                                $.aede.pmp.ptmt.projectedit.initProjectResourceTable();
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
        //初始化模式窗口按钮单击事件，任务
        initMissionModalBtnClickListener: function () {
            $("#pmp_ptmt_projectmission_modal_btnSave").bind("click", function () {
                if ($.aede.pmp.ptmt.projectedit.validatemission.form()) {
                    var postData = $.aede.pmp.ptmt.projectedit.getMissionModalFormEleVal();
                    if (!postData.id) {
                        $.aede.comm.toastr.error("项目任务信息丢失，无法保存编辑结果！");
                        return false;
                    }
                    $.ajax({
                        type: "POST",
                        async: false,// 同步请求
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/mission/" + postData.id + "/update",
                        dataType: "json",
                        data: postData,
                        success: function (data) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("编辑项目任务成功！");
                                // 隐藏模式窗口
                                $('#pmp_ptmt_projectmission_modal').modal('hide');
                                $.aede.pmp.ptmt.projectedit.initProjectMissionTable();
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
        initStatusChangeListener: function () {
            $("#pmp_ptmt_project_status").change(function () {
                var status = $("#pmp_ptmt_project_status").val();
                if (status == 1) {
                    $("#pmp_ptmt_project_signingProbability").attr("readonly", false);
                } else {
                    if (status == 4) {
                        $("#pmp_ptmt_project_signingProbability").val(0);
                    } else if (status == 2 || status == 3 || status == 5) {
                        $("#pmp_ptmt_project_signingProbability").val(100);
                    }
                    $("#pmp_ptmt_project_signingProbability").attr("readonly", true);
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
            postData.ctime = $.trim($("#pmp_ptmt_project_ctime").val());
            //postData.manager=$.trim($("#pmp_ptmt_project_manager").val());
            // postData['manager.id'] = $.aede.expand.comm.loginUser.userId;
            var managerId = $.trim($("#pmp_ptmt_project_manager_id").val());
            if (!managerId) {
                managerId = $.aede.expand.comm.loginUser.userId;
            }
            postData['manager.id'] = managerId;
            postData.cid = $.trim($("#pmp_ptmt_project_cid").val());
            postData.director = $.trim($("#pmp_ptmt_project_director").val());
            postData.checker = $.trim($("#pmp_ptmt_project_checker").val());
            postData.level = $.trim($("#pmp_ptmt_project_level").val());
            postData.progress = $.trim($("#pmp_ptmt_project_progress_span").text());
            postData.amt = $.trim($("#pmp_ptmt_project_amt").val());
            postData.status = $.trim($("#pmp_ptmt_project_status").val());
            postData.remark = $.trim($("#pmp_ptmt_project_remark").val());
            postData.uupid = $.trim($("#pmp_ptmt_projectstage_uuid").val());
            postData.perids = $.trim($("#pmp_ptmt_project_perid").val());
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
        //获取resource模式窗口表单元素值
        getResourceModalFormEleVal: function () {
            var postData = {};
            postData.type = $.trim($("#pmp_ptmt_projectresource_type").val());
            postData['projectedit.id'] = $.trim($("#pmp_ptmt_project_id").val());
            postData.pid = $.trim($("#pmp_ptmt_proedit_proid").val());
            if (!!stage) {
                postData.sid = stage;
            }
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
        //获取mission模式窗口表单元素值
        getMissionModalFormEleVal: function () {
            var postData = {};
            //postData['projectedit.id'] = $.trim($("#pmp_ptmt_project_id").val());
            postData.pid = $.trim($("#pmp_ptmt_proedit_proid").val());
            postData.sid = stage;
            postData.mname = $("#pmp_ptmt_projectmission_mname").val();
            var mmanager = $("#pmp_ptmt_projectmission_mmanager").val();
            var pers = "";
            if (mmanager != null) {
                for (var p = 0; p < mmanager.length; p++) {
                    pers += mmanager[p] + ",";
                }
            }
            pers = pers.substring(0, pers.length - 1);
            postData.mmanager = pers;
            postData.mstartdate = $("#pmp_ptmt_projectmission_mstartdate").val();
            postData.menddate = $("#pmp_ptmt_projectmission_menddate").val();
            postData.mprogress = $("#pmp_ptmt_projectmission_mprogress").val();
            var id = $("#pmp_ptmt_projectmission_id").val();
            if (!!id) {
                postData.id = id;
            }
            return postData;
        },

        // 模式窗口表单验证
        initModalFormValidate: function () {
            var icon = "<i class='fa fa-times-circle'></i> ";
            $.aede.pmp.ptmt.projectedit.validateForm = $('#pmp_ptmt_project_modal form').validate({
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
                        maxlength: 18,
                        number: true,
                        min: 0,
                        max: 100
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
                        maxlength: 11,
                        number: true,
                        min: 0
                    },
                    pmp_ptmt_project_status: {
                        required: true
                    },
                    remark: {
                        maxlength: 225
                    },
                    source: {
                        required: true
                    },
                    stakeholder: {
                        required: true
                    },
                    signingProbability: {
                        required: true
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
                        maxlength: "进度长度不可超过18！",
                        number: "请输入合法的数字！",
                        min: "进度最低为0！",
                        max: "进度最高为100%！"
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
                    },
                    source: {
                        required: "项目来源是必填项！"
                    },
                    stakeholder: {
                        required: "干系人是必填项！"
                    },
                    signingProbability: {
                        required: "签约概率是必填项！"
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

        // 表单验证,资源
        initResourceFormValidate: function () {
            var icon = "<i class='fa fa-times-circle'></i> ";//validateresource
            $.aede.pmp.ptmt.projectedit.validateresource = $('#pmp_ptmt_projectresource_modal form').validate({
                rules: {
                    pmp_ptmt_projectresource_type: {
                        required: true
                    }
                },
                messages: {
                    pmp_ptmt_projectresource_type: {
                        required: "请填写资源类别！"
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
        // 表单验证,任务
        initMissionFormValidate: function () {
            var icon = "<i class='fa fa-times-circle'></i> ";//validateresource
            jQuery.validator.methods.compareDate = function (value, element, param) {
                var startDate = jQuery(param).val();
                return startDate <= value;
            };
            $.aede.pmp.ptmt.projectedit.validatemission = $('#pmp_ptmt_projectmission_modal form').validate({
                rules: {
                    mname: {
                        required: true,
                        maxlength: 70
                    },
                    mmanager: {
                        required: true
                    },
                    mstartdate: {
                        required: true
                    },
                    menddate: {
                        required: true,
                        compareDate: "#pmp_ptmt_projectmission_mstartdate"
                    },
                    mprogress: {
                        //required : true
                    }
                },
                messages: {
                    mname: {
                        required: "请填写任务名称！",
                        maxlength: "任务名称长度不可超过70！"
                    },
                    mmanager: {
                        required: "请填写负责人！"
                    },
                    mstartdate: {
                        required: "请填写开始日期！"
                    },
                    menddate: {
                        required: "请填写结束日期！",
                        compareDate: "结束日期不能小于结束日期！"
                    },
                    mprogress: {
                        //required : true
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
        // 表单验证,跟踪
        initFollowFormValidate: function () {
            var icon = "<i class='fa fa-times-circle'></i> ";
            jQuery.validator.methods.compareDate = function (value, element, param) {
                var startDate = jQuery(param).val();
                return startDate <= value;
            };
            $.aede.pmp.ptmt.projectedit.validateFollow = $('#pmp_ptmt_project_follow_modal form').validate({
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
            if (!!$.aede.pmp.ptmt.projectedit.validateForm) {
                // 清除表单验证样式
                $.aede.pmp.ptmt.projectedit.validateForm.resetForm();
                // XXX jQuery Validate resetFrom不能清除highlight添加的样式，所有需要手动清理
                $('#pmp_ptmt_project_modal form .form-group').removeClass('has-error');
                $('#pmp_ptmt_project_modal form .form-group').removeClass('has-success');
            }
        },
        // 清除 资源 窗口表单验证样式
        cleanResourceFormValidateStyle: function () {
            if (!!$.aede.pmp.ptmt.projectedit.validateresource) {
                // 清除表单验证样式
                $.aede.pmp.ptmt.projectedit.validateresource.resetForm();
                // XXX jQuery Validate resetFrom不能清除highlight添加的样式，所有需要手动清理
                $('#pmp_ptmt_projectresource_modal form .form-group').removeClass('has-error');
                $('#pmp_ptmt_projectresource_modal form .form-group').removeClass('has-success');
            }
        },
        // 清除 任务 窗口表单验证样式
        cleanMissionFormValidateStyle: function () {
            if (!!$.aede.pmp.ptmt.projectedit.validatemission) {
                // 清除表单验证样式
                $.aede.pmp.ptmt.projectedit.validatemission.resetForm();
                // XXX jQuery Validate resetFrom不能清除highlight添加的样式，所有需要手动清理
                $('#pmp_ptmt_projectmission_modal form .form-group').removeClass('has-error');
                $('#pmp_ptmt_projectmission_modal form .form-group').removeClass('has-success');
            }
        },
        // 清除 跟踪 窗口表单验证样式
        cleanMissionFormValidateStyle: function () {
            if (!!$.aede.pmp.ptmt.projectedit.validateFollow) {
                // 清除表单验证样式
                $.aede.pmp.ptmt.projectedit.validateFollow.resetForm();
                // XXX jQuery Validate resetFrom不能清除highlight添加的样式，所有需要手动清理
                $('#pmp_ptmt_project_follow_modal form .form-group').removeClass('has-error');
                $('#pmp_ptmt_project_follow_modal form .form-group').removeClass('has-success');
            }
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
                                $.aede.pmp.ptmt.projectedit.initProjectResourceTable();
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
        //删除任务
        delProjectMission: function (id) {
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
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/mission/" + id + "/delete",
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        type: "POST",
                        async: false,// 同步请求
                        success: function (data, textStatus) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("删除任务成功");
                                $.aede.pmp.ptmt.projectedit.initProjectMissionTable();
                                return;
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                                return;
                            }
                        },
                        error: function (XMLHttpRequest, textStatus) {
                            $.aede.comm.toastr.error("请求异常，删除任务失败！");
                        }
                    });

                } else {
                    swal("取消删除！", "保留任务。", "success");
                }
            });
        },
        //删除跟踪
        delProjectFollow: function (id) {
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
                        url: $.aede.expand.getContextPath() + "/management/pmp/ptmt/projectFollow/" + id + "/delete",
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        type: "GET",
                        async: false,// 同步请求
                        success: function (data, textStatus) {
                            if (data["responseCode"] == "1") {
                                $.aede.comm.toastr.success("删除跟踪成功");
                                $.aede.pmp.ptmt.projectedit.initProjectFollowTable();
                                return;
                            } else {
                                $.aede.comm.toastr.error(data["responseMsg"]);
                                return;
                            }
                        },
                        error: function (XMLHttpRequest, textStatus) {
                            $.aede.comm.toastr.error("请求异常，删除任务失败！");
                        }
                    });

                } else {
                    swal("取消删除！", "保留任务。", "success");
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
                            addTabByTitle(data[i].stageName, data[i].sseq + 1);
                            resultArray[i][0] = data[i].sid;
                            //resultArray[i][1]=data[i].sname;
                            resultArray[i][1] = data[i].stageName;
                            $('#pmp_ptmt_project_resourcetablediv').show();
                        }
                    } else {
                        // $('#tt').hide();
                        // $('#pmp_ptmt_project_resourcetablediv').hide();
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
            if (!!fileData) {
                $("#pmp_ptmt_projectresource_name").val(fileData.fileNames);
                $("#pmp_ptmt_projectresource_path").val(fileData.url);
                $("#pmp_ptmt_projectresource_size").val(fileData.fileSize);
            } else {
                $.aede.comm.toastr.warning("请选择上传文件");
                return false;
            }

            var postData = $.aede.pmp.ptmt.projectedit.getResourceModalFormEleVal();
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
                        $.aede.pmp.ptmt.projectedit.initProjectResourceTable();
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
                $.aede.pmp.ptmt.projectedit.initProposeManSearchBtnClickListener();
                // 人员重置按钮点击事件
                $.aede.pmp.ptmt.projectedit.initProposeManResetBtnClickListener();
                $.aede.pmp.ptmt.projectedit.initProposeManTable();
                $(".panel-body").remove();
                ids = $("#pmp_ptmt_project_perid").val();
                names = $("#pmp_ptmt_project_pername").val();
                if (names != "" && ids != "") {
                    var namesplit = names.split(",");
                    var idsplit = ids.split(",");
                    for (var p = 0; p < namesplit.length; p++) {
                        $("#jail-detain-addPrsArrgInfo-proposeMan-namePanel").append("<div class='panel-body'>" + namesplit[p] +
                            "<input type='hidden' name='perhidid' value='" + idsplit[p] + "'>" +	 // class='btn btn-info btn-xs
                            "<button style='float:right' type='button' onclick='$(this).parent().remove();' class='btn btn-danger btn-xs'><i class='fa fa-trash-o'></i></button></div>");
                    }
                }
            });
        },
        initDirectorClickListener: function () {
            $("#pmp_ptmt_project_director").bind("click", function () {
                $("#pmp-ptmt-addProject-selectSingleMan-modal").modal("show");
                $.aede.pmp.ptmt.projectedit.singleFlag = 1;
                $.aede.pmp.ptmt.projectedit.initSelectSingleManTable();
            });
            $("#pmp_ptmt_project_checker").bind("click", function () {
                $("#pmp-ptmt-addProject-selectSingleMan-modal").modal("show");
                $.aede.pmp.ptmt.projectedit.singleFlag = 2;
                $.aede.pmp.ptmt.projectedit.initSelectSingleManTable();
            });
            $("#pmp_ptmt_project_manager").bind("click", function () {
                $("#pmp-ptmt-addProject-selectSingleMan-modal").modal("show");
                $.aede.pmp.ptmt.projectedit.singleFlag = 3;
                $.aede.pmp.ptmt.projectedit.initSelectSingleManTable();
            });
        },
        // 初始化提出人员表
        initProposeManTable: function () {
            if (!$.aede.pmp.ptmt.projectedit.proposeManTable) {
                // 初始化proposeManTable
                $.aede.pmp.ptmt.projectedit.proposeManTable = $("#jail-detain-addPrsArrgInfo-proposeManTable")
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
                                        return "<a href=\"javascript:void(0);\" onclick=\"$.aede.pmp.ptmt.projectedit.nameChosed('"
                                            + full.realname + "','" + full.id + "')\" class=\"btn default btn-xs red-stripe\">选择</a>";
                                    }
                                }]

                        });
            } else {
                $.aede.pmp.ptmt.projectedit.proposeManTable.ajax.reload();
            }
        },
        initSelectSingleManTable: function () {
            if (!$.aede.pmp.ptmt.projectedit.selectSingleManTable) {
                // 初始化proposeManTable
                $.aede.pmp.ptmt.projectedit.selectSingleManTable = $("#pmp-ptmt-addProject-selectSingleMan-dataTable")
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
                                    var realname = $.trim($("#pmp-ptmt-addProject-selectSingleMan-name").val());
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
                                        return "<a href=\"javascript:void(0);\" onclick=\"$.aede.pmp.ptmt.projectedit.nameSelected('"
                                            + full.realname + "','" + full.id + "')\" class=\"btn default btn-xs red-stripe\">选择</a>";
                                    }
                                }]

                        });
            } else {
                $.aede.pmp.ptmt.projectedit.selectSingleManTable.ajax.reload();
            }
        },
        // 单个人员选择
        nameChosed: function (name, id) {
            names = "";
            ids = "";
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
        nameSelected: function (name, id) {
            //负责人
            if ($.aede.pmp.ptmt.projectedit.singleFlag == 1) {
                $("#pmp_ptmt_project_director").val(name);
            } else if ($.aede.pmp.ptmt.projectedit.singleFlag == 2) {//复核人
                $("#pmp_ptmt_project_checker").val(name);
            } else if ($.aede.pmp.ptmt.projectedit.singleFlag == 3) {//项目经理
                $("#pmp_ptmt_project_manager").val(name);
                $("#pmp_ptmt_project_manager_id").val(id);
            }
            $("#pmp-ptmt-addProject-selectSingleMan-modal").modal("hide");
        },
        // 人员确认选择
        initProposeManChose: function () {
            $("#jail-detain-addPrsArrgInfo-proposeMan-btnSure").bind("click", function () {
                //$("#jail-detain-addPrsArrgInfo-proposeManModal").hide();
                names = "";
                ids = "";
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
                $.aede.pmp.ptmt.projectedit.initProposeManTable();
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
        },
        //负责人、复核人查询按钮点击事件
        initSelectSingleManSearchBtnClickListener: function () {
            $("#pmp-ptmt-addProject-selectSingleMan-btnSearch").click(function () {
                $.aede.pmp.ptmt.projectedit.initSelectSingleManTable();
                return false;
            });
        },
        //负责人、复核人重置按钮点击事件
        initSelectSingleManResetBtnClickListener: function () {
            $("#pmp-ptmt-addProject-selectSingleMan-btnReset").click(function () {
                $("#pmp-ptmt-addProject-selectSingleMan-modal input").val("");
                return false;
            });
        },
        getSelectData: function (rpid) {
            $.ajax({
                url: $.aede.expand.getContextPath() + "/management/pmp/comm/projectMember/" + rpid + "/findByPid",
                type: "post",
                async: false,
                success: function (result) {
                    for (var i = 0; i < result.length; i++) {
                        $("#pmp_ptmt_projectmission_mmanager").append('<option value="' + result[i].webUser.id + '">' + result[i].webUser.realname + '</option>');
                    }
                    $('.selectpicker').selectpicker('refresh');
                }
            });
        }

    }
    $.aede.comm.callBack = function () {
        $.aede.pmp.ptmt.project.initProjectTable();
    }

})(jQuery);
$(document).ready(function () {
    ComponentsNoUiSliders.init();
    $.aede.pmp.ptmt.projectedit.init();
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
            $.aede.pmp.ptmt.projectedit.initProjectResourceTable();
            $.aede.pmp.ptmt.projectedit.initProjectMissionTable();
            $.aede.pmp.ptmt.projectedit.initProjectFollowTable();
        }
    });
    laydate({
        elem: '#pmp_ptmt_project_ctime',
        event: 'focus'
    });
    laydate({
        elem: '#pmp_ptmt_projectmission_mstartdate',
        event: 'focus'
    });
    laydate({
        elem: '#pmp_ptmt_projectmission_menddate',
        event: 'focus'
    });
    laydate({
        elem: '#pmp_ptmt_project_follow_time',
        event: 'focus'
    });
    laydate({
        elem: '#pmp_ptmt_project_expectedTime',
        event: 'focus'
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

function addTabByTitle(title, url) {
    $('#tt').tabs('add', {
        title: title,
        //content:content,
        closable: false
    });
}

function closeAll() { //关闭全部
//    var tabs = $('#tt').tabs('allTabs');
//    $.each(tabs,function(i,n){
//        $(jq).tabs('close', n.title);
//    })
    for (var i = 0; i < resultArray.length; i++) {
        $('#tt').tabs('close', resultArray[i][1]);
    }
}



