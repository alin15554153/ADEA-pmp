(function($){
	//项目角色管理,第一个页面
    $.aede.pmp.ptmt.project.rolemanager = {
    	projectTable :"",//项目角色列表
    	//初始化页面按钮
		initBtnListener:function(){
 			//加载查询按钮事件
 			$("#ptmt_project_rolemanager_btnSearch").bind("click",function(){
 				//初始化项目角色列表
 				 $.aede.pmp.ptmt.project.rolemanager.initprojectTable();
 				return false;
 			});
 			//加载重置按钮事件
 			$("#ptmt_project_rolemanager-btnReset").bind("click",function(){
 				$("#ptmt_project_rolemanager_query_rname").val("");
 			});
		},
    	//初始化项目角色数据表格
        initprojectTable : function(){
            if (!$.aede.pmp.ptmt.project.rolemanager.projectTable) {
                // 初始化projectTable
                $.aede.pmp.ptmt.project.rolemanager.projectTable = $("#ptmt_project_rolemanager_table").DataTable({
                    serverSide: true,
                    "iDisplayLength":50,
                    ajax: {
                        url: $.aede.expand.getContextPath() + '/management/pmp/ptmt/project/rolemanager/queryList',
                        type: 'POST',
                        dataType: 'json',
                        data: function(d) {
                        	d.managerId=$.aede.expand.comm.loginUser.userId;
                        	d.pname= $("#ptmt_project_rolemanager_query_rname").val();
                        	
                        }
                    },
                    columns: [
                        {
                            data: "name"
                        },
                        {
                            data: "rnames"
                        },
                        {
                            data: "id"
                        }
                    ],
                    columnDefs: [{

						'aTargets' : [ 0 ],
						"sWidth" :"500px",
						'mRender'  : function ( data, type, full ) {
							var newData=data;
							if(newData.length>29)
								newData=newData.substring(0,29)+"...";
						     return  "<span title=\""+data+"\">"+newData+"</span>";
						}
					    
					},{

						'aTargets' : [ 1 ],
						"sWidth" :"500px",
						'mRender'  : function ( data, type, full ) {
							var newData=data;
							if(newData.length>29)
								newData=newData.substring(0,29)+"...";
						     return  "<span title=\""+data+"\">"+newData+"</span>";
						}
					    
					},{
                        "targets" : [2],
                        "sWidth" :"150px",
                        "render" : function(data,event,full) {
                        	var pid='"'+full.id+'"';
                            return '<button type=\'button\' class=\'btn btn-primary btn-xs\' onclick=\'$.aede.pmp.ptmt.project.rolemanager.jump('+pid+');\'>查看详情</button>' ;
                        }
                    }]
                });
            } else {
                $.aede.pmp.ptmt.project.rolemanager.projectTable.ajax.reload();
            }
        },
        //跳转页面
        jump :function(pid){
        	$.aede.comm.addTab($.aede.expand.getContextPath() +"/management/pmp/ptmt/project/rolemanager/"+pid+"/secList","查看详情","infoTab");
        },
        
        //页面初始化
		init :function(){
			 $.aede.pmp.ptmt.project.rolemanager.initBtnListener();
			//初始化项目角色列表
			 $.aede.pmp.ptmt.project.rolemanager.initprojectTable();
		}
    }
    $.aede.comm.callBack=function(){
    	 $.aede.pmp.ptmt.project.rolemanager.initprojectTable();
	}
})(jQuery);
$(function(){
	$.aede.pmp.ptmt.project.rolemanager.init();
});