var columnData=[];//柱状图x轴数据
var chartDatas=new Array(); //x轴标
var i=0;
var tt=0;
(function($){
    $.aede.pmp.slas.proStatistics = {
        dataTable : "",//资源表格
        validateForm: "",//表单验证
        uploader: "",
        initBtnListener:function(){
            //加载查询按钮事件
            $("#slas_proStatistics_list_query_btnSearch").click(function(){
                $.aede.pmp.slas.proStatistics.initDataTable();
                //var columnChartDatas=[];//柱状图数据
               // columnChartDatas.push({
	        		//name: "项目数量",
	        		//data: columnData
	        	//});
                //$.aede.pmp.slas.proStatistics.initColumnChart(chartDatas,columnChartDatas,"项目信息","column");
                $.aede.pmp.slas.proStatistics.createColumnCharts(chartDatas);
                return false;
            });
            //加载重置按钮事件
            $("#slas_proStatistics_list_query_btnReset").click(function(){
                $("#slas_proStatistics_list_query_form")[0].reset();
                $.aede.expand.comm.select.resetSelectTree("slas_proStatistics_list_query_company",$.aede.expand.comm.select.orgSelectPlaceholder);
            });
        },
        //初始化数据表格
        initDataTable : function(){
        	columnData=[];//柱状图x轴数据
        	chartDatas=new Array(); //x轴标
        	i=0;
        	tt=0;
            if (!$.aede.pmp.slas.proStatistics.dataTable) {
            	
                // 初始化resourceTable
                $.aede.pmp.slas.proStatistics.dataTable = $("#slas_proStatistics_list_table").DataTable({
                    serverSide: true,
					"iDisplayLength":50,
                    ajax: {
                        url: $.aede.expand.getContextPath() + '/management/pmp/slas/proStatistics/queryByPaging',
                        type: 'POST',
                        async: false,
                        dataType: 'json',
                        data: function(d) {
                        	columnData=[];//柱状图x轴数据
                        	chartDatas=new Array(); //x轴标
                        	i=0;
                        	tt=0;
                        	var starttime = $.trim($("#slas_proStatistics_list_query_starttime").val());
                            if(!!starttime){
                            	d.starttime = starttime;
                            }
                            var endtime = $.trim($("#slas_proStatistics_list_query_endtime").val());
                            if(!!endtime){
                            	d.endtime = endtime;
                            }
                        	var orgIds=$.aede.expand.comm.select.getSelectTreeValues("slas_proStatistics_list_query_company");
                            if(!!orgIds){
                            	d.orgids = orgIds.join(",");
                            }
                        	var stage = $("#slas_proStatistics_list_query_stage").val();
                            if (!!stage) {
                                d.stage = stage;
                            }
                            var type = $("#slas_proStatistics_list_query_type").val();
                            if (!!type) {
                                d.type = type;
                            }
                        }
                       
                    },
                   /* "fnInitComplete": function(oSettings, json) {
                        alert( 'DataTables 初始化完毕' );
                     },*/
                    "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                        // 所有的a都加粗
                       /* if ( aData[4] == "A" )
                        {
                          $('td:eq(4)', nRow).html( '<b>A</b>' );
                        }*/
                      },
                      "fnDrawCallback": function( oSettings ) {
                    	  tt=1;
                        },
                    columns: [
                        {
                            data: "area"
                        },
                        {
                            data: "company"
                        },
                        {
                            data: "type"
                        },
                        {
                            data: "stage"
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
                        "targets": [1],
                        "render" : function(data,event,full) {
                        	if(tt==0){
                        		//columnData.push(Number(full.totalTimes));
                            	chartDatas[i]=full;
                            	i++;	
                        	}
                        	var newData = !!data?data:"未知";
							if(newData.length>32)
								newData=newData.substring(0,30)+"...";
                     		return "<span title=\""+data+"\">"+newData+"</span>";
                        }
                    }]
                });
            } else {
                $.aede.pmp.slas.proStatistics.dataTable.ajax.reload();
            }
        },
        /*加载柱状图和曲线图
         * @param datas:用于取类型（比如 男，女）
         * 		  columnChartdatas:加载柱状图或曲线图的结果集
         * 		  dict :加载标题用 取自下拉文本
         * 		  type:图表类型 柱状图或曲线图
         * */
	
    	//创建柱状图
		createColumnCharts: function(result) {
			var index = 0;
			var colors = ["#7cb5ec", "#e87e04", "#90ed7d", "#f7a35c", "#8085e9", "#f15c80", "#e4d354", "#2b908f", "#f45b5b", "#91e8e1"];
			var A = function (name,len,color) {
				this.name = name;
				var a = new Array();
				for(var i=0;i<len;i++){
					a[i] = 0;
				}
				this.color = color;
				this.data = a;
			}

			var xData = [];
			var yData = [];
			var statusData = {};
			var statusArr = [];
			for (var i in result) {
				if(xData.indexOf(result[i].company) < 0){
					xData.push(result[i].company);
				}
			}

			for (var name in result) {
				if(statusArr.indexOf(result[name].type) < 0){
					var a = new A(result[name].type,xData.length,colors[index]);
					a.data[xData.indexOf(result[name].company)] = Number(result[name].totalTimes);
					yData.push(a);
					statusArr.push(result[name].type);

				}else{
					if(yData[statusArr.indexOf(result[name].type)].data[xData.indexOf(result[name].company)] == undefined){
						yData[statusArr.indexOf(result[name].type)].data[xData.indexOf(result[name].company)] = Number(result[name].totalTimes);
					}else{
						yData[statusArr.indexOf(result[name].type)].data[xData.indexOf(result[name].company)]+= Number(result[name].totalTimes) ;
					}
				}
				index = index>colors.length?0:++index;
			}

			$.aede.pmp.slas.proStatistics.initColumnCharts(xData, yData, "地区项目状态信息");
		},
    	initColumnCharts: function(xDatas, datas,dict) {
			$('#pmp_demo_colum_container').highcharts({
				chart: {
					type: 'column'
				},
				title: {
					text: dict+'统计图'
				},
				credits: {
					enabled: false
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
						cursor: 'pointer',
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
        	$.aede.expand.comm.select.initOrgSelectByLoginUser("slas_proStatistics_list_query_company", false);//按照登录用户的行政区划过滤
            $.aede.pmp.slas.proStatistics.initBtnListener();
            $("#slas_proStatistics_list_query_btnSearch").click();
        }
    }
})(jQuery);

$(function(){
    $.aede.pmp.slas.proStatistics.init();
    laydate({
        elem: '#slas_proStatistics_list_query_starttime', 
        event: 'focus'
    });
    laydate({
        elem: '#slas_proStatistics_list_query_endtime', 
        event: 'focus'
    });
    $("#slas_proStatistics_forexport_button").click(function () {
    	var starttime = $.trim($("#slas_proStatistics_list_query_starttime").val());
        if(!!starttime){
        	$("#slas_proStatistics_forexport_starttime").val(starttime);
        }
        var endtime = $.trim($("#slas_proStatistics_list_query_endtime").val());
        if(!!endtime){
        	$("#slas_proStatistics_forexport_endtime").val(endtime);
        }
    	var orgIds=$.aede.expand.comm.select.getSelectTreeValues("slas_proStatistics_list_query_company");
        if(!!orgIds){
        	orgids = orgIds.join(",");
        	$("#slas_proStatistics_forexport_orgIds").val(orgids);
        }
    	var stage = $("#slas_proStatistics_list_query_stage").val();
        if (!!stage) {
        	$("#slas_proStatistics_forexport_stage").val(stage);
        }
        var type = $("#slas_proStatistics_list_query_type").val();
        if (!!type) {
        	$("#slas_proStatistics_forexport_type").val(type);
        }
        $("#slas_proStatistics_forexport_form").submit();
    });
});