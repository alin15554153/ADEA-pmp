(function ($) {

    $.aede.pmp.ptmt.projectFollow = {
        projectTable: "",//项目表格
        init: function () {
            $.aede.pmp.ptmt.projectFollow.initBtnListener();
            $("#pmp_ptmt_project_btnSearch").click();

        },
        initBtnListener: function () {
            //加载查询按钮事件
            $("#pmp_ptmt_project_btnSearch").click(function () {
                $.aede.pmp.ptmt.projectFollow.initProjectTable();
                return false;
            });
            //加载重置按钮事件
            $("#pmp_ptmt_project_btnReset").click(function () {
                $("#pmp_ptmt_project_query_userName").val("");
                $("#pmp_ptmt_project_query_pname").val("");
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
                        url: $.aede.expand.getContextPath() + '/management/pmp/ptmt/projectFollow/queryProjectFollowList',
                        type: 'POST',
                        dataType: 'json',
                        data: function (d) {
                            var userName = $.trim($("#pmp_ptmt_project_query_userName").val());
                            var pname = $.trim($("#pmp_ptmt_project_query_pname").val());
                            if (!!userName) {
                                d.userName = userName;
                            }
                            if (!!pname) {
                                d.pname = pname;
                            }
                        }
                    },
                    columns: [
                        {
                            data: "name"
                        },
                        {
                            data: "username"
                        },
                        {
                            data: "content"
                        },
                        {
                            data: "follow_time"
                        }
                    ],
                    columnDefs: [
                        {
                            "targets": [2],
                            "render": function (data, event, full) {
                                var newData = data;
                                if (newData.length > 25) {
                                    newData = newData.substring(0, 25) + "...";
                                }
                                return "<span title=\"" + data + "\">" + newData + "</span>";
                            }
                        }
                    ]

                });
            } else {
                $.aede.pmp.ptmt.project.projectTable.ajax.reload();
            }
        },


    }


})(jQuery);
$(document).ready(function () {
    $.aede.pmp.ptmt.projectFollow.init();
});




