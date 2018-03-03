(function($){
    $.aede.pmp.slas.proBrowser = {
        dataTable : "",//资源表格
        validateForm: "",//表单验证
        initBtnListener:function(){
            //加载查询按钮事件
            $("#slas_proBrowser_list_query_btnSearch").click(function(){
                $.aede.pmp.slas.proBrowser.initDataTable();
                return false;
            });
            //加载重置按钮事件
            $("#slas_proBrowser_list_query_btnReset").click(function(){
                $("#slas_proBrowser_list_query_form")[0].reset();
                $("#slas_proBrowser_list_query_form select option").removeAttr("selected");
                $.aede.expand.comm.select.resetSelectTree("slas_proBrowser_list_query_company",$.aede.expand.comm.select.orgSelectPlaceholder);
            });
        },

        //初始化数据表格
        initDataTable : function(){
            if (!$.aede.pmp.slas.proBrowser.dataTable) {
            	
                // 初始化resourceTable
                $.aede.pmp.slas.proBrowser.dataTable = $("#slas_proBrowser_list_table").DataTable({
                    serverSide: true,
                    "iDisplayLength":50,
                    ajax: {
                        url: $.aede.expand.getContextPath() + '/management/pmp/ptmt/project/queryByPaging',
                        type: 'POST',
                        async: false,
                        dataType: 'json',
                        data: function(d) {
                            //处理首页图表单击查询
                            var p = $("#slas_proBrowser_hidden_query_p").val();
                            if (!!p) {
                                if (p.lastIndexOf("_") > 0) {
                                    d["search_LIKE_webOrganization.organname"] = p.split("_")[0];
                                    $("#slas_proBrowser_list_query_status").find("option:contains('"+p.split("_")[1]+"')").attr("selected", true);
                                } else {
                                    $("#slas_proBrowser_list_query_status").find("option:contains('"+p+"')").attr("selected", true);
                                    $("#slas_proBrowser_list_query_yw").find("option:contains('"+p+"')").attr("selected", true);
                                }

                            }
                            var starttime = $.trim($("#slas_proBrowser_list_query_starttime").val());
                            if(!!starttime){
                            	d.search_GTE_ctime = starttime;
                            }
                            var endtime = $.trim($("#slas_proBrowser_list_query_endtime").val());
                            if(!!endtime){
                            	d.search_LTE_ctime = endtime;
                            }
                            var orgIds=$.aede.expand.comm.select.getSelectTreeValues("slas_proBrowser_list_query_company");
                            if (!!orgIds && orgIds.length > 0) {
                                d["search_IN_webOrganization.id"] = orgIds.toString();
                            } else {
                                //如果机构查询为空，设置为当前机构及子机构
                                var curOrgId = $.aede.expand.comm.loginUser.orgid;
                                $("#slas_proBrowser_list_query_company").combotree("setValue", curOrgId);
                                orgIds=$.aede.expand.comm.select.getSelectTreeValues("slas_proBrowser_list_query_company");
                                d["search_IN_webOrganization.id"] = orgIds.toString();
                                $.aede.expand.comm.select.resetSelectTree("slas_proBrowser_list_query_company",$.aede.expand.comm.select.orgSelectPlaceholder);
                            }

                            var yw = $("#slas_proBrowser_list_query_yw").val();
                            if (!!yw) {
                                if (yw == 1) {
                                   d["search_LTE_projectstage.expendtime"] =  moment().format("YYYY-MM-DD");
                                } else {
                                    d["search_GTE_projectstage.expendtime"] =  moment().format("YYYY-MM-DD");
                                }
                            }

                            var status = $("#slas_proBrowser_list_query_status").val();
                            if (!!status) {
                                d.search_EQ_status = status;
                            }

                            var stage = $("#slas_proBrowser_list_query_stage").val();
                            if (!!stage) {
                                d["search_EQ_projectstage.sname"] = stage;
                            }

                            var type = $("#slas_proBrowser_list_query_type").val();
                            if (!!type) {
                                d["search_EQ_type"] = type;
                            }
                            d.search_EQ_delTag = "0";
                        },
                        dataSrc: function ( json ) {
                            return json.data;
                        }
                    },
                    columns: [
                        {
                            data: "webArea.name"
                        },
                        {
                            data: "webOrganization.organname"
                        },
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
                            data: "followList"
                        }
                        ,
                        {
                            data: "id"
                        }
                    ],
                    columnDefs: [
                     {
                     	"targets" : [1],
                     	"render" : function(data,event,full) {
                     		var newData=data;
							if(newData.length>13)
								newData=newData.substring(0,11)+"...";
                     		return "<span title=\""+data+"\">"+newData+"</span>";
                     	}
                     },{
                     	"targets" : [3],
                     	"render" : function(data,event,full) {
                     		var newData=data;
							if(newData.length>12)
								newData=newData.substring(0,10)+"...";
                            if(full.type==1){
                                return "<button type=\"button\" class=\"btn btn-info btn-xs\">自建</button> <span title=\"" + data + "\">" + newData + "</span>";
                            }else{
                                return "<span title=\"" + data + "\">" + newData + "</span>";
                            }
                     	}
                     },{
                        "targets": [5],
                        "render" : function (data,event,full) {
                            return '<span title=' + data + '%>' + data + '%<div class="progress progress-mini"><div style="width: ' + data + '%;" class="progress-bar"></div></div></span>';
                        }
                    },{
                        "targets":[10],
                            "render":function(data,event,full){
                            if(data.length>0){
                                var content = data[0].content;
                                if(content.length>10){
                                    content = content.substr(0,10)+"...";
                                }
                                var maxNum = 3;
                                var html = "<ul>";
                                    for (var i = 0; i < (data.length>=maxNum?maxNum:data.length); i++) {
                                        html += "<li>" + data[i].followTime + " " + data[i].username + " " + data[i].content + "</li>";
                                    }
                                    html+="</ul>"
                                return "<span data-toggle='tooltip' data-placement='right' data-html='true' title='"+html+"'>"+content +"</span>";
                            }
                            return "";
                            }
                    },{
                        "targets": [11],
                        "render" : function (data,event,full) {
                            return '<button type="button" class="btn btn-primary btn-xs" onclick="$.aede.pmp.ptmt.proquery.showProjectinfoModal(\''+data+'\');">查看</button>' ;
                        }
                    }],
                    "drawCallback":function(){
                        $('[data-toggle="tooltip"]').tooltip();
                    },
                    "createdRow": function ( row, data, index ) {

                        if (!!data.projectstage) {
                            var expendtime = data.projectstage.expendtime;
                            var curDate = moment().format("YYYY-MM-DD");
                            if (expendtime < curDate) {
                                $(row).css('font-weight',"bold").css("color","red");
                                var curTxt = $('td', row).eq(-3).text();
                                $('td', row).eq(-3).text(curTxt + "(已延误)")
                            }
                        }
                        /*if ( data[5].replace(/[\$,]/g, '') * 1 > 4000 ) {
                            $('td', row).eq(5).css('font-weight',"bold").css("color","red");
                        }*/
                    }
                });
            } else {
                $.aede.pmp.slas.proBrowser.dataTable.ajax.reload();
            }
        },
        initChartsData:function(){
            var result;
            $.ajax({
                type: "POST",
                async: false,// 同步请求
                url: $.aede.expand.getContextPath() + '/management/pmp/slas/proBrowser/initCharts',
                dataType: "json",
                data: {
                    "orgId": $.aede.expand.comm.loginUser.orgid
                },
                success: function (data) {
                    result = data;
                    return false;
                },
                error: function () {
                    $.aede.comm.toastr.error("请求异常，查看项目信息失败！");
                }
            });

            $.aede.pmp.slas.proBrowser.createColumnCharts(result);
            $.aede.pmp.slas.proBrowser.createPieCharts(result);
        },

        //创建饼图
        createPieCharts:function(data){

            var pieChartDatas=[];//饼图数据

            var statusData = {};

            for (var i=0;i<data.length;i++) {
                if (!!statusData[data[i].statusName]) {
                    statusData[data[i].statusName]++;
                } else {
                    statusData[data[i].statusName] = 1;
                }

            }
            for (var status in statusData) {
                pieChartDatas.push({
                    name: status,
                    y: statusData[status]
                });
            }
            $.aede.pmp.slas.proBrowser.initPieCharts(pieChartDatas,"项目信息");
        },

        //创建柱状图
        createColumnCharts: function(result) {
            var A = function (name,len) {
                this.name = name;
                var a = new Array();
                for(var i=0;i<len;i++){
                    a[i] = 0;
                }
                this.data = a;
            }

            var xData = [];
            var yData = [];
            var statusData = {};
            var statusArr = [];
            for (var i in result) {
                if(xData.indexOf(result[i].webOrganization.organname) < 0){
                    xData.push(result[i].webOrganization.organname);
                }
            }
            for (var name in result) {
                if(statusArr.indexOf(result[name].statusName) < 0){
                    var a = new A(result[name].statusName,xData.length);
                    a.data[xData.indexOf(result[name].webOrganization.organname)] = 1;
                    yData.push(a);
                    statusArr.push(result[name].statusName);

                }else{
                    if(yData[statusArr.indexOf(result[name].statusName)].data[xData.indexOf(result[name].webOrganization.organname)] == undefined){
                        yData[statusArr.indexOf(result[name].statusName)].data[xData.indexOf(result[name].webOrganization.organname)] = 1;
                    }else{
                        yData[statusArr.indexOf(result[name].statusName)].data[xData.indexOf(result[name].webOrganization.organname)]++;
                    }
                }
            }

            $.aede.pmp.slas.proBrowser.initColumnCharts(xData, yData, "项目信息");
        },

        /*加载饼图
         * @param datas:加载饼图的结果集
         * 		 dict :加载标题用 取自下拉文本
         * */
        initPieCharts:function(datas,dict){
            $('#slas_resStatistics_list_pie_container').highcharts({
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    height:300,
                    width: 600
                },
                title: {
                    text: dict+'统计图'
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            color: '#000000',
                            connectorColor: '#000000',
                            format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                        }
                    }
                },
                series: [{
                    type: 'pie',
                    name: '占比：',
                    data: datas
                }]
            });
        },

        initColumnCharts: function(xDatas, datas,dict) {
            $('#slas_resStatistics_list_colum_container').highcharts({
                chart: {
                    type: 'column'
                },
                title: {
                    text: dict+'统计图'
                },
                xAxis: {
                    categories: xDatas
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: '项目数量'
                    },
                    stackLabels: {
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                        }
                    }
                },
                legend: {
                    align: 'right',
                    x: -70,
                    verticalAlign: 'top',
                    y: 20,
                    floating: true,
                    backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColorSolid) || 'white',
                    borderColor: '#CCC',
                    borderWidth: 1,
                    shadow: false
                },
                tooltip: {
                    formatter: function() {
                        return '<b>'+ this.x +'</b><br/>'+
                            this.series.name +': '+ this.y +'<br/>'+
                            'Total: '+ this.point.stackTotal;
                    }
                },
                plotOptions: {
                    column: {
                        stacking: 'normal',
                        dataLabels: {
                            enabled: true,
                            color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
                        }
                    }
                },
                series: datas
            });
        },

        init : function() {
            $.aede.expand.comm.select.initOrgSelectByLoginUser("slas_proBrowser_list_query_company", false);//按照登录用户的行政区划过滤
            $.aede.pmp.slas.proBrowser.initBtnListener();
            $("#slas_proBrowser_list_query_status").find("option[text='"+$("#slas_proBrowser_hidden_query_p").val()+"']").attr("selected",true);
            $("#slas_proBrowser_list_query_yw").find("option[text='"+$("#slas_proBrowser_hidden_query_p").val()+"']").attr("selected",true);
            $("#slas_proBrowser_list_query_btnSearch").click();
           // $.aede.pmp.slas.proBrowser.initChartsData();
        }
    }
})(jQuery);

$(function(){
    $.aede.pmp.slas.proBrowser.init();
    laydate({
        elem: '#slas_proBrowser_list_query_starttime', 
        event: 'focus'
    });
    laydate({
        elem: '#slas_proBrowser_list_query_endtime', 
        event: 'focus'
    });
    $("#slas_proBrowser_forexport_button").click(function () {
    	var starttime = $.trim($("#slas_proBrowser_list_query_starttime").val());
        if(!!starttime){
        	$("#slas_proBrowser_forexport_starttime").val(starttime);
        }
        var endtime = $.trim($("#slas_proBrowser_list_query_endtime").val());
        if(!!endtime){
        	$("#slas_proBrowser_forexport_endtime").val(endtime);
        }
    	var orgIds=$.aede.expand.comm.select.getSelectTreeValues("slas_proBrowser_list_query_company");
        if (!!orgIds && orgIds.length > 0) {
            $("#slas_proBrowser_forexport_company").val(orgIds.toString());
        } else {
            //如果机构查询为空，设置为当前机构及子机构
            var curOrgId = $.aede.expand.comm.loginUser.orgid;
            $("#slas_proBrowser_list_query_company").combotree("setValue", curOrgId);
            orgIds=$.aede.expand.comm.select.getSelectTreeValues("slas_proBrowser_list_query_company");
            $("#slas_proBrowser_forexport_company").val(orgIds.toString());
        }
        var yw = $("#slas_proBrowser_list_query_yw").val();
        if (!!yw) {
            if (yw == 1) {
            	$("#slas_proBrowser_forexport_yw").val(moment().format("YYYY-MM-DD"));
            } else {
            	
            	$("#slas_proBrowser_forexport_yw").val(moment().format("YYYY-MM-DD"));
            }
        }
        var status = $("#slas_proBrowser_list_query_status").val();
        if (!!status) {
        	$("#slas_proBrowser_forexport_status").val(status);
        }
        var stage = $("#slas_proBrowser_list_query_stage").val();
        if (!!stage) {
        	$("#slas_proBrowser_forexport_stage").val(stage);
        }
        $("#slas_proBrowser_forexport_form").submit();
    });
});