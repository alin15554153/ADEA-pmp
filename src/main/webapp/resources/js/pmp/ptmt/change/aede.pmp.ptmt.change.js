(function ($) {
    $.aede.pmp.ptmt.change = {
        dataTable: "",//项目表格
        validateForm: "",//表单验证
        init: function () {
            $.aede.pmp.ptmt.change.initBtnListener();
            $("#ptmt_change_list_query_btnSearch").click();
        },
        //初始化按钮click事件
        initBtnListener: function () {
            //加载查询按钮事件
            $("#ptmt_change_list_query_btnSearch").click(function () {
                $.aede.pmp.ptmt.change.initDataTable();
                return false;
            });
            //加载重置按钮事件
            $("#ptmt_change_list_query_btnReset").click(function () {
                $("#ptmt_change_list_query_form")[0].reset();
            });
        },
        //初始化数据表格
        initDataTable: function () {
            if (!$.aede.pmp.ptmt.change.dataTable) {
                // 初始化resourceTable
                $.aede.pmp.ptmt.change.dataTable = $("#ptmt_change_list_table").DataTable({
                    serverSide: true,
                    "iDisplayLength": 50,
                    ajax: {
                        url: $.aede.expand.getContextPath() + '/management/pmp/ptmt/project/queryByPaging',
                        type: 'POST',
                        dataType: 'json',
                        data: function (d) {
                            var code = $.trim($("#ptmt_change_list_query_code").val());
                            if (!!code) {
                                d.search_LIKE_code = code;
                            }
                            var name = $.trim($("#ptmt_change_list_query_name").val());
                            if (!!name) {
                                d.search_LIKE_name = name;
                            }
                            d.search_EQ_delTag = "0";


                            var roleNames = $.aede.expand.comm.loginUser.roleNames;
                            var re1 = /.项目经理/;
                            var re2 = /.地区管理员/;
                            var re3 = /.集团管理员/;
                            if (re3.test(roleNames)) {

                            } else if (re2.test(roleNames)) {
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
                                if (newData.length > 27)
                                    newData = newData.substring(0, 25) + "...";
                                return "<span title=\"" + data + "\">" + newData + "</span>";
                            }
                        }, {
                            "targets": [3],
                            "render": function (data, type, full) {
                                return '<span title=' + data + '%>' + data + '%<div class="progress progress-mini"><div style="width: ' + data + '%;" class="progress-bar"></div></div></span>';
                            }
                        }, {
                            "targets": [8],
                            "render": function (data, type, full) {
                                return '<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.change.changeStage(\'' + data + '\');">阶段变更</button>';
                            }
                        }]
                });
            } else {
                $.aede.pmp.ptmt.change.dataTable.ajax.reload();
            }
        },
        //阶段变更按钮click事件
        changeStage: function (id) {
            var tabName = "阶段变更";
            var tabId = "ptmt_change_stage_page";
            var url = $.aede.expand.getContextPath() + "/management/pmp/ptmt/change/" + id + "/changePage";
            $.aede.comm.addTab(url, tabName, tabId);
        }
    }
    $.aede.comm.callBack = function () {
        $.aede.pmp.ptmt.change.initDataTable();
    }
})(jQuery);
$(function () {
    $.aede.pmp.ptmt.change.init();
});