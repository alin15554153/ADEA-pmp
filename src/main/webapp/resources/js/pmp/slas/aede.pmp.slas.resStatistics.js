(function($){
    $.aede.pmp.slas.resStatistics = {
        dataTable : "",//资源表格
        validateForm: "",//表单验证
        uploader: "",
        initBtnListener:function(){
            //加载查询按钮事件
            $("#slas_resStatistics_list_query_btnSearch").click(function(){
                $.aede.pmp.slas.resStatistics.initDataTable();
                return false;
            });
            //加载重置按钮事件
            $("#slas_resStatistics_list_query_btnReset").click(function(){
                $("#slas_resStatistics_list_query_form")[0].reset();
                $.aede.expand.comm.select.resetSelectTree("slas_resStatistics_list_query_company",$.aede.expand.comm.select.orgSelectPlaceholder);
            });
        },
        //初始化数据表格
        initDataTable : function(){
            if (!$.aede.pmp.slas.resStatistics.dataTable) {
                // 初始化resourceTable
                $.aede.pmp.slas.resStatistics.dataTable = $("#slas_resStatistics_list_table").DataTable({
                    serverSide: true,
                    "iDisplayLength":50,
                    ajax: {
                        url: $.aede.expand.getContextPath() + '/management/pmp/slas/resStatistics/queryByPaging',
                        type: 'POST',
                        dataType: 'json',
                        data: function(d) {
                        	var starttime = $.trim($("#slas_resStatistics_list_query_starttime").val());
                            if(!!starttime){
                            	d.starttime = starttime;
                            }
                            var endtime = $.trim($("#slas_resStatistics_list_query_endtime").val());
                            if(!!endtime){
                            	d.endtime = endtime;
                            }
                        	var orgIds=$.aede.expand.comm.select.getSelectTreeValues("slas_resStatistics_list_query_company");
                            if(!!orgIds){
                            	d.orgids = orgIds.join(",");
                            }
                            var name = $("#slas_resStatistics_list_query_name").val();
                            var stage = $("#slas_resStatistics_list_query_stage").val();
                            if (!!name) {
                                d.name = name;
                            }
                            if (!!stage) {
                                d.stageName = stage;
                            }
                            d.delTag = "0";
                        },
                        dataSrc: function ( json ) {
                            $.aede.pmp.slas.resStatistics.createColumnCharts(json.data);

                            return json.data;
                        }
                    },
                    columns: [
                        {
                            data: "area"
                        },
                        {
                            data: "company"
                        },
                        {
                            data: "name"
                        },
                        {
                            data: "resCount"
                        },
                        {
                            data: "totalTimes"
                        }
                    ],
                    columnDefs: [{
                        "targets": [0],
                        "render" : function(data,event,full) {
                            return !!data?data:"未知";
                        }
                    },{
                     	"targets" : [1],
                     	"render" : function(data,event,full) {
                     		var newData=data;
							if(newData.length>27)
								newData=newData.substring(0,25)+"...";
                     		return "<span title=\""+data+"\">"+newData+"</span>";
                     	}
                     },{
                     	"targets" : [2],
                     	"render" : function(data,event,full) {
                     		var newData=data;
							if(newData.length>32)
								newData=newData.substring(0,30)+"...";
                     		return "<span title=\""+data+"\">"+newData+"</span>";
                     	}
                     },{
                        "targets": [3],
                        "render" : function(data,event,full) {
                            return !!data?data:"0";
                        }
                    },{
                        "targets": [4],
                        "render" : function(data,event,full) {
                            return !!data?data:"0";
                        }
                    }]
                });
            } else {
                $.aede.pmp.slas.resStatistics.dataTable.ajax.reload();
            }
        },


        //创建柱状图
        createColumnCharts:function(data){

            var result = data;
            /*$.ajax({
                type: "POST",
                async: false,// 同步请求
                url: $.aede.expand.getContextPath() + "/management/pmp/slas/resStatistics/queryResStatisticsByAreaAndCompany",
                dataType: "json",
                success: function (data) {
                    if (data["responseCode"] == "1") {
                        result = data["datas"];
                    } else {
                        $.aede.comm.toastr.error(data["responseMsg"]);
                    }
                    return false;
                },
                error: function () {
                    $.aede.comm.toastr.error("请求异常，查询资源统计信息失败！");
                }
            });*/

            var chartDatas=new Array();//项目名称

            var columnDataX=[];//柱状图x轴数据
            var columnDataY=[];//柱状图Y轴数据

            if (!!result && result.length>0) {
                for (var i=0;i<result.length;i++) {

                    chartDatas[i]=result[i].name;
                    columnDataX.push(!!result[i].resCount?parseInt(result[i].resCount):0);
                    columnDataY.push(!!result[i].totalTimes?parseInt(result[i].totalTimes):0);
                }
            }

            var columnChartDatas=[];//柱状图数据
            columnChartDatas.push({
                name: "资源数量",
                data: columnDataX
            });
            /*columnChartDatas.push({
                name: "资源下载总数",
                data: columnDataY
            });*/

            $.aede.pmp.slas.resStatistics.initColumnChart(chartDatas,columnChartDatas,"项目资源信息","column");



            /*var data = [];
            var colors = Highcharts.getOptions().colors;
            var categories = [];
            for (var i=0;i<result.length;i++) {
                categories[i] = result[i].name;
                data[i] = {
                    y: !!result[i].resCount?parseInt(result[i].resCount):0,
                    color: colors[i],
                    drilldown: {
                        name: "下载总数",
                        data: !!result[i].totalTimes?parseInt(result[i].totalTimes):0,
                        color: colors[i]
                    }
                }
            }

            // Build the data arrays
            var resData = [];
            var resDownloadData = [];
            for (var i = 0; i < data.length; i++) {
                // add resource count data
                resData.push({
                    name: categories[i],
                    y: data[i].y,
                    color: data[i].color
                });
                // add resource download count data
                var brightness = 0.2 - (i / data[i].drilldown.data) / 5 ;
                resDownloadData.push({
                    parent:data[i].drilldown.name,
                    name: categories[i],
                    y: data[i].drilldown.data,
                    color: Highcharts.Color(data[i].color).brighten(brightness).get()
                });
            }
            $.aede.pmp.slas.resStatistics.initDPieCharts(resData,resDownloadData, "项目资源信息","资源总数","下载总数");*/
        },

        /*加载柱状图和曲线图
         * @param datas:用于取类型（比如 男，女）
         * 		  columnChartdatas:加载柱状图或曲线图的结果集
         * 		  dict :加载标题用 取自下拉文本
         * 		  type:图表类型 柱状图或曲线图
         * */
        initColumnChart:function(chartDatas,columnChartdatas,dict,type){
            $('#slas_resStatistics_list_colum_container').highcharts({
                chart: {
                    type: type,
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false
                },
                title: {
                    text: dict+'统计图'
                },
                credits: {
                    enabled: false
                },
                xAxis: {
                    categories: chartDatas
                },
                yAxis: {
                    min: 0,
                    minTickInterval: 1,
                    title: { text: '峰值' },
                },
                series:  columnChartdatas
            });
        },

        initDPieCharts: function (inData,outData,dict,inDict,outDict) {
            // Create the chart
            $('#slas_resStatistics_list_pie_container').highcharts({
                chart: {
                    type: 'pie'
                },
                title: {
                    align: "left",
                    verticalAlign: "top",
                    text: dict + "统计图"
                },
                subtitle: {
                    align: "left",
                    verticalAlign: "top",
                    text:   inDict + "(内圈) 和 " +  outDict + "(外圈)",
                },
                yAxis: {
                    title: {
                        text: outDict
                    }
                },
                plotOptions: {
                    pie: {
                        shadow: false,
                        center: ['50%', '50%']
                    }
                },
                tooltip: {
                    valueSuffix: ''
                },
                series: [{
                    name: inDict,
                    data: inData,
                    size: '30%',
                    dataLabels: {
                        formatter: function() {
                            return this.y > 5 ? this.point.name : null;
                        },
                        color: 'white',
                        distance: -30
                    }
                }, {
                    name: outDict,
                    data: outData,
                    size: '80%',
                    innerSize: '60%',
                    dataLabels: {
                        formatter: function() {
                            // display only if larger than 1
                            return this.y > 1 ? '<b>'+ this.point.name +':</b> '+ '<b>'+ this.point.parent +':</b> '+this.y   : null;
                        }
                    }
                }]
            });

        },

        init : function() {
        	$.aede.expand.comm.select.initOrgSelectByLoginUser("slas_resStatistics_list_query_company", false);//按照登录用户的行政区划过滤
            $.aede.pmp.slas.resStatistics.initBtnListener();
            $("#slas_resStatistics_list_query_btnSearch").click();
        }
    }
})(jQuery);

$(function(){
    $.aede.pmp.slas.resStatistics.init();
    laydate({
        elem: '#slas_resStatistics_list_query_starttime', 
        event: 'focus'
    });
    laydate({
        elem: '#slas_resStatistics_list_query_endtime', 
        event: 'focus'
    });
    $("#slas_resStatistics_forexport_button").click(function () {
    	var starttime = $.trim($("#slas_resStatistics_list_query_starttime").val());
        if(!!starttime){
        	$("#slas_resStatistics_forexport_starttime").val(starttime);
        }
        var endtime = $.trim($("#slas_resStatistics_list_query_endtime").val());
        if(!!endtime){
        	$("#slas_resStatistics_forexport_endtime").val(endtime);
        }
    	var orgIds=$.aede.expand.comm.select.getSelectTreeValues("slas_resStatistics_list_query_company");
        if(!!orgIds){
        	$("#slas_resStatistics_forexport_company").val(orgIds.join(","));
        }
        var name = $("#slas_resStatistics_list_query_name").val();
        var stage = $("#slas_resStatistics_list_query_stage").val();
        if (!!name) {
        	$("#slas_resStatistics_forexport_name").val(name);
        }
        if (!!stage) {
        	$("#slas_resStatistics_forexport_stage").val(stage);
        }
        $("#slas_resStatistics_forexport_form").submit();
    });
});