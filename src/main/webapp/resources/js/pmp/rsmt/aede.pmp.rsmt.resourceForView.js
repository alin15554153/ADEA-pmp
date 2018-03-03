$(document).ready(function () {
    $.aede.pmp.rsmt.resourceForView.initBtnListener();
    //$.aede.pmp.rsmt.resourceForView.initStageSelect();
    $.aede.pmp.rsmt.resourceForView.initResourceTable();
    $.aede.pmp.rsmt.resourceForView.getMyProjectIds();
    $.aede.pmp.rsmt.resourceForView.getMyAreaId();
    $("#pmp_rsmt_resourceForView_btnExport").click(function () {
        $("#pmp_rsmt_resourceForView_resourceNameFormInput").val($.trim($("#pmp_rsmt_resourceForView_resourceName").val()));
        $("#pmp_rsmt_resourceForView_resourceTypeFormInput").val($.trim($("#pmp_rsmt_resourceForView_resourceType").val()));
        $("#pmp_rsmt_resourceForView_stageIdFormInput").val($.trim($("#pmp_rsmt_resourceForView_stage").val()));
        $("#pmp_rsmt_resourceForView_projectNameFormInput").val($.trim($("#pmp_rsmt_resourceForView_projectName").val()));
        $("#pmp_rsmt_resourceForView_form").submit();
    });
});

var rsmt_resourceDown_myProjectIdsArray = new Array();//当前用户所属的项目ID集合
var rsmt_resourceDown_areaCode;//当前用户所属的区域

(function($){
    $.aede.pmp.rsmt.resourceForView = {
        resourceTable : "",//资源表格

        initBtnListener:function(){
            //加载查询按钮事件
            $("#pmp_rsmt_resourceForView_btnSearch").click(function(){
                $.aede.pmp.rsmt.resourceForView.initResourceTable();
                return false;
            });
            //加载重置按钮事件
            $("#pmp_rsmt_resourceForView_btnReset").click(function(){
                $("#pmp_rsmt_resourceForView_resourceName").val("");
                $("#pmp_rsmt_resourceForView_resourceType").val("");
                $("#pmp_rsmt_resourceForView_stage").val("");
                $("#pmp_rsmt_resourceForView_projectName").val("");
            });
        },

        //初始化数据表格
        initResourceTable : function(){
            if (!$.aede.pmp.rsmt.resourceForView.resourceTable) {
                $.aede.pmp.rsmt.resourceForView.resourceTable = $("#pmp_rsmt_resourceForView_table").DataTable({
                    serverSide: true,
                    ajax: {
                        url: $.aede.expand.getContextPath() + '/management/pmp/rsmt/resourceDown/findResourceListBySql',
                        type: 'POST',
                        dataType: 'json',
                        data: function(d) {
                            var resourceName = $.trim($("#pmp_rsmt_resourceForView_resourceName").val());
                            var resourceType = $.trim($("#pmp_rsmt_resourceForView_resourceType").val());//资源类别
                            var stage = $.trim($("#pmp_rsmt_resourceForView_stage").val());//阶段
                            var projectName = $.trim($("#pmp_rsmt_resourceForView_projectName").val());//项目名称
                            var uid = $.aede.expand.comm.loginUser.userId;
                            if (!!resourceName) {
                                //d.search_LIKE_name = resourceName;
                                d.resourceName = resourceName;
                            }
                            if (!!resourceType) {
                                //d.search_LIKE_type = resourceType;
                                d.resourceType = resourceType;
                            }
                            if (!!stage) {
                                //d["search_EQ_project.stage"] = stage;
                                d.projectStage = stage;//这里是projectstagedivision的sname
                            }
                            if (!!projectName) {
                                //d["search_LIKE_project.name"] = projectName;
                                d.projectName = projectName;
                            }
                            d.uid = uid;
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
                            data: "projectName"
                        },
                        {
                            //data: "stageName"
                            data: "stageName"
                        },
                        {
                            data: "areaName"
                        },
                        {
                            data: "orgName"
                        },
                        {
                            data: "userName"
                        },
                        {
                            data: "dateString"
                        },
                        {
                            data: "times"
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
                                //console.info(newData.length);
                                if(newData.length > 10){
                                    newData=newData.substring(0,11)+"...";
                                }
                                return "<span title=\""+data+"\">"+newData+"</span>";
                            }
                        },
                        {
                            "targets" : [2],
                            "render" : function(data,event,full) {
                                var newData=data;
                                //console.info(newData.length);
                                if(newData.length > 10){
                                    newData=newData.substring(0,11)+"...";
                                }
                                return "<span title=\""+data+"\">"+newData+"</span>";
                            }
                        },
                        /*{
                            "targets" : [1],
                            "render" : function(data,event,full) {
                                return $.aede.expand.comm.getDictName('资源类别',data);
                            }
                        },
                        {
                            "targets" : [3],
                            "render" : function(data,event,full) {
                                return $.aede.expand.comm.getDictName('阶段名称',data);
                            }
                        },*/
                        /*{
                            "targets" : [7],
                            "render" : function(data,event,full) {
                                var newData=data;
                                //console.info(newData.length);
                                if(newData.length > 10){
                                    newData=newData.substring(0,11)+"...";
                                }
                                return "<span title=\""+data+"\">"+newData+"</span>";
                            }
                        },*/
                        {
                            "targets" : [9],
                            "render" : function(data,event,full) {
                                var pid = full.pid;//该资源所属项目ID
                                var applyHtml = "<button type='button' class='btn btn-primary btn-xs' onclick='$.aede.pmp.rsmt.resourceForView.sendApplication(\"" + data + "\",\"" + full.areaId + "\",\""+full.remark+"\");'>申请</button>";

                                //var link = $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/"+full.path+"/download";
                                var link = $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/"+data+"/dl";
                                var downloadHtml = "<a type='button' class='btn btn-primary btn-xs btn-dl' href=" + link + " target='_blank' rid='"+data+"' onclick='$.aede.pmp.rsmt.resourceForView.addDownloadTimes(\"" + data + "\");'>下载</a>";

                                if (rsmt_resourceDown_myProjectIdsArray.indexOf(pid) < 0) {
                                    return applyHtml;
                                } else {
                                    return downloadHtml;
                                }
                            }
                        }
                    ]
                });
            } else {
                $.aede.pmp.rsmt.resourceForView.resourceTable.ajax.reload();
            }
        },

        //申请按钮点击事件(rid:资源ID areaId:项目区域ID manager:项目经理ID)
        sendApplication : function (rid, areaId, manager){
            //console.info(rid);
            //console.info(areaId);
            //console.info(manager);
            //console.info($.aede.expand.comm.loginUser);
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
                            var resourceDownId = data.datas;//申请ID
                            $.aede.comm.toastr.success("申请成功!");
                            
                            $("#pmp_rsmt_resourceView_modal").modal("hide");

                            var myName = $.aede.expand.comm.loginUser.realName;
                            var content = "【" + myName + "】申请资源 请审核";

                            //$.aede.pmp.comm.mail.sendPTP("资源下载申请", content, manager, 4);//发送邮件

                            $.aede.pmp.rsmt.resourceForView.sendMessageToPM(manager, content, resourceDownId);//发送消息
                        }
                    },
                    error : function(XMLHttpRequest, textStatus) {
                        $.aede.comm.toastr.error("与后台交互失败!");
                    }
                });
            });
            $("#pmp_rsmt_resourceView_modal").modal("show");
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

        //增加下载次数
        addDownloadTimes : function (rid) {
            //console.info(rid);
            $.ajax({
                url : $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/addDownloadTimes",
                dataType : "json",
                data : {"rid":rid},
                type : "POST",
                async : false,
                success : function(data, textStatus) {
                    $.aede.pmp.rsmt.resourceForView.initResourceTable();
                },
                error : function(XMLHttpRequest, textStatus) {
                    $.aede.comm.toastr.error("与后台交互失败!");
                }
            });
        },

        //初始化阶段下拉列表
        initStageSelect : function () {
            $.ajax({
                url : $.aede.expand.getContextPath() + "/management/pmp/rsmt/resourceDown/findProjectStageList",
                dataType : "json",
                type : "POST",
                async : false,
                success : function(data, textStatus) {
                    if (data.responseCode == "1") {
                        var list = data.datas;
                        var str = "<option value=''>---请选择阶段---</option>";
                        for (var i=0; i<list.length; i++) {
                            var value = list[i].sid;
                            var text = list[i].sname;
                            str += "<option value='"+value+"'>"+text+"</option>";
                        }
                        $("#pmp_rsmt_resourceForView_stage").html(str);
                    }
                },
                error : function(XMLHttpRequest, textStatus) {
                    $.aede.comm.toastr.error("与后台交互失败！");
                }
            });
        },

        //发送申请消息给项目经理(targetId:接收方ID message:发送的消息内容)
        sendMessageToPM : function (targetId, message, resourceDownId) {
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
                "value" : "/management/pmp/rsmt/resourceDownCheck/"+resourceDownId+"/list"
            });
            messageParam.push({
                "name" : "tabName",
                "value" : "资源申请审核"
            });
            messageParam.push({
                "name" : "tabId",
                "value" : "resourceTab"
            });
            $.aede.mq.sys.message.messageMt.sendMessagePTP(messageParam);
        }
    }
})(jQuery);