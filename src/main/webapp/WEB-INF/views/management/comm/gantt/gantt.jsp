<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="dwz" uri="http://www.anycc.com/dwz"%>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>甘特图</title>
    <link rel=stylesheet href="${pageContext.request.contextPath}/resources/js/ganttChart/platform.css" type="text/css">
	<link rel=stylesheet href="${pageContext.request.contextPath}/resources/js/ganttChart/dateField/jquery.dateField.css" type="text/css">
	
	<link rel=stylesheet href="${pageContext.request.contextPath}/resources/js/ganttChart/gantt.css" type="text/css">
	<link rel=stylesheet href="${pageContext.request.contextPath}/resources/js/ganttChart/print.css" type="text/css" media="print">
	
	<script src="http://libs.baidu.com/jquery/1.8.3/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/ganttChart/libs/jquery-ui.min.js "></script>

	<script src="${pageContext.request.contextPath}/resources/js/ganttChart/libs/jquery.livequery.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/ganttChart/libs/jquery.timers.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/ganttChart/libs/platform.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/ganttChart/libs/date.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/ganttChart/libs/i18nJs.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/ganttChart/dateField/jquery.dateField.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/ganttChart/JST/jquery.JST.js"></script>
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/js/ganttChart/libs/jquery.svg.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/ganttChart/libs/jquery.svg.min.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/ganttChart/libs/jquery.svgdom.1.8.js"></script>

	<script src="${pageContext.request.contextPath}/resources/js/ganttChart/ganttUtilities.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/ganttChart/ganttTask.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/ganttChart/ganttDrawerSVG.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/ganttChart/ganttGridEditor.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/ganttChart/ganttMaster.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/plugins/toastr/toastr.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/comm/aede.comm.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/comm/aede.expand.js"></script>
</head>

<body style="background:white" style="height:100%">
<input type="hidden" id="hd_pjId" value="${id }"/>
<div id="workSpace" style="padding:0px; overflow-y:auto; overflow-x:hidden;border:1px solid #e5e5e5;margin:0 5px"></div>

<div id="taZone" style="display:none;" class="noprint">
   <textarea rows="8" cols="150" id="ta">
    {"tasks":[
    {"id":"-1","name":"Gantt editor","code":"","level":0,"status":"STATUS_ACTIVE","canWrite":true,"start":1451577600000,"duration":21,"end":1461254400000,"startIsMilestone":true,"endIsMilestone":false,"collapsed":false,"assigs":[],"hasChild":true}
    ,{"id":-2,"name":"coding","code":"","level":1,"status":"STATUS_ACTIVE","canWrite":true,"start":1396994400000,"duration":10,"end":1398203999999,"startIsMilestone":false,"endIsMilestone":false,"collapsed":false,"assigs":[],"description":"","progress":0,"hasChild":true}
    ,{"id":-3,"name":"gantt part","code":"","level":2,"status":"STATUS_ACTIVE","canWrite":true,"start":1396994400000,"duration":2,"end":1397167199999,"startIsMilestone":false,"endIsMilestone":false,"collapsed":false,"assigs":[],"depends":"","hasChild":false}
    ,{"id":-4,"name":"editor part","code":"","level":2,"status":"STATUS_SUSPENDED","canWrite":true,"start":1397167200000,"duration":4,"end":1397685599999,"startIsMilestone":false,"endIsMilestone":false,"collapsed":false,"assigs":[],"depends":"3","hasChild":false}
    ,{"id":-5,"name":"testing","code":"","level":1,"status":"STATUS_SUSPENDED","canWrite":true,"start":1398981600000,"duration":6,"end":1399672799999,"startIsMilestone":false,"endIsMilestone":false,"collapsed":false,"assigs":[],"depends":"2:5","description":"","progress":0,"hasChild":true}
    ,{"id":-6,"name":"test on safari","code":"","level":2,"status":"STATUS_SUSPENDED","canWrite":true,"start":1398981600000,"duration":2,"end":1399327199999,"startIsMilestone":false,"endIsMilestone":false,"collapsed":false,"assigs":[],"depends":"","hasChild":false}
    ,{"id":-7,"name":"test on ie","code":"","level":2,"status":"STATUS_SUSPENDED","canWrite":true,"start":1399327200000,"duration":3,"end":1399586399999,"startIsMilestone":false,"endIsMilestone":false,"collapsed":false,"assigs":[],"depends":"6","hasChild":false}
    ,{"id":-8,"name":"test on chrome","code":"","level":2,"status":"STATUS_SUSPENDED","canWrite":true,"start":1399327200000,"duration":2,"end":1399499999999,"startIsMilestone":false,"endIsMilestone":false,"collapsed":false,"assigs":[],"depends":"6","hasChild":false}
    ],"selectedRow":0,"canWrite":false,"canWriteOnParent":false}  </textarea>

  <button onclick="loadGanttFromServer();">load</button>
</div>
<script>
var id=$("#hd_pjId").val();
$.ajax({
    type : "GET",
    async : false,// 同步请求
    url : $.aede.expand.getContextPath() + "/management/pmp/comm/gantt/" + id + "/queryGantt",
    dataType : "json",
    success : function(data) {
       	$("#ta").html(JSON.stringify(data));
    },
    error : function() {
        $.aede.comm.toastr.error("tab加载异常！");
    }
});
</script>

<style>
  .resEdit {
    padding: 15px;
  }

  .resLine {
    width: 95%;
    padding: 3px;
    margin: 5px;
    border: 1px solid #d0d0d0;
  }

  body {
    overflow: hidden;
  }

  .ganttButtonBar h1{
    color: #000000;
    font-weight: bold;
    font-size: 28px;
    margin-left: 10px;
  }

</style>

<script type="text/javascript">

var ge;  //this is the hugly but very friendly global var for the gantt editor
$(function() {

  //load templates
  $("#ganttemplates").loadTemplates();

  // here starts gantt initialization
  ge = new GanttMaster();
  var workSpace = $("#workSpace");
  workSpace.css({width:$(window).width() - 20,height:"200px"});
  //workSpace.css({width:$(window).width() - 20,height:$(window).height()});
  ge.init(workSpace);

  //inject some buttons (for this demo only)
  //overwrite with localized ones
  loadI18n();

  //simulate a data load from a server.
  loadGanttFromServer();


  //fill default Teamwork roles if any
  if (!ge.roles || ge.roles.length == 0) {
    setRoles();
  }

  //fill default Resources roles if any
  if (!ge.resources || ge.resources.length == 0) {
    setResource();
  }

  $(window).resize(function(){
    workSpace.css({width:$(window).width() - 20,height:$(window).height() - 100});
    workSpace.trigger("resize.gantt");
  })

});


function loadGanttFromServer(taskId, callback) {
  loadFromLocalStorage();
}


//-------------------------------------------  Create some demo data ------------------------------------------------------
function setRoles() {
  ge.roles = [
    {
      id:"tmp_1",
      name:"Project Manager"
    },
    {
      id:"tmp_2",
      name:"Worker"
    },
    {
      id:"tmp_3",
      name:"Stakeholder/Customer"
    }
  ];
}

function setResource() {
  var res = [];
  for (var i = 1; i <= 10; i++) {
    res.push({id:"tmp_" + i,name:"Resource " + i});
  }
  ge.resources = res;
}

function clearGantt() {
  ge.reset();
}

function loadI18n() {
  GanttMaster.messages = {
    "CANNOT_WRITE":                  "CANNOT_WRITE",
    "CHANGE_OUT_OF_SCOPE":"NO_RIGHTS_FOR_UPDATE_PARENTS_OUT_OF_EDITOR_SCOPE",
    "START_IS_MILESTONE":"START_IS_MILESTONE",
    "END_IS_MILESTONE":"END_IS_MILESTONE",
    "TASK_HAS_CONSTRAINTS":"TASK_HAS_CONSTRAINTS",
    "GANTT_ERROR_DEPENDS_ON_OPEN_TASK":"GANTT_ERROR_DEPENDS_ON_OPEN_TASK",
    "GANTT_ERROR_DESCENDANT_OF_CLOSED_TASK":"GANTT_ERROR_DESCENDANT_OF_CLOSED_TASK",
    "TASK_HAS_EXTERNAL_DEPS":"TASK_HAS_EXTERNAL_DEPS",
    "GANTT_ERROR_LOADING_DATA_TASK_REMOVED":"GANTT_ERROR_LOADING_DATA_TASK_REMOVED",
    "ERROR_SETTING_DATES":"ERROR_SETTING_DATES",
    "CIRCULAR_REFERENCE":"CIRCULAR_REFERENCE",
    "CANNOT_DEPENDS_ON_ANCESTORS":"CANNOT_DEPENDS_ON_ANCESTORS",
    "CANNOT_DEPENDS_ON_DESCENDANTS":"CANNOT_DEPENDS_ON_DESCENDANTS",
    "INVALID_DATE_FORMAT":"INVALID_DATE_FORMAT",
    "TASK_MOVE_INCONSISTENT_LEVEL":"TASK_MOVE_INCONSISTENT_LEVEL",

    "GANTT_QUARTER_SHORT":"trim.",
    "GANTT_SEMESTER_SHORT":"sem."
  };
}



//-------------------------------------------  Get project file as JSON (used for migrate project from gantt to Teamwork) ------------------------------------------------------
function getFile() {
  $("#gimBaPrj").val(JSON.stringify(ge.saveProject()));
  $("#gimmeBack").submit();
  $("#gimBaPrj").val("");

  /*  var uriContent = "data:text/html;charset=utf-8," + encodeURIComponent(JSON.stringify(prj));
   neww=window.open(uriContent,"dl");*/
}


//-------------------------------------------  LOCAL STORAGE MANAGEMENT (for this demo only) ------------------------------------------------------
Storage.prototype.setObject = function(key, value) {
  this.setItem(key, JSON.stringify(value));
};


Storage.prototype.getObject = function(key) {
  return this.getItem(key) && JSON.parse(this.getItem(key));
};


function loadFromLocalStorage() {
  var ret;
  if (localStorage) {
    if (localStorage.getObject("teamworkGantDemo")) {
      ret = localStorage.getObject("teamworkGantDemo");
    }
  } else {
    $("#taZone").show();
  }
  if (!ret || !ret.tasks || ret.tasks.length == 0){
    ret = JSON.parse($("#ta").val());


    //actualiza data
    //莫名其妙用当前日期减去开始日期  先注释掉再说
    /*var offset=new Date().getTime()-ret.tasks[0].start;
    for (var i=0;i<ret.tasks.length;i++)
      ret.tasks[i].start=ret.tasks[i].start+offset;
	*/

  }
  ge.loadProject(ret);
  ge.checkpoint(); //empty the undo stack
}
//-------------------------------------------  Open a black popup for managing resources. This is only an axample of implementation (usually resources come from server) ------------------------------------------------------

</script>


<div id="gantEditorTemplates" style="display:none;">

  <div class="__template__" type="TASKSEDITHEAD"><!--
  <table class="gdfTable" cellspacing="0" cellpadding="0">
    <thead>
    <tr style="height:40px">
      <th class="gdfColHeader" style="width:35px;"></th>
      <th class="gdfColHeader" style="width:25px;"></th>
      <th class="gdfColHeader gdfResizable" style="width:120px;">阶段</th>
      <th class="gdfColHeader gdfResizable" style="width:80px;">开始日期</th>
      <th class="gdfColHeader gdfResizable" style="width:80px;">结束日期</th>
      <th class="gdfColHeader gdfResizable" style="width:60px;">持续天数</th>
    </tr>
    </thead>
  </table>
  --></div>

  <div class="__template__" type="TASKROW"><!--
  <tr taskId="(#=obj.id#)" class="taskEditRow" level="(#=level#)">
    <th class="gdfCell" align="center" style="cursor:pointer;"><span class="taskRowIndex">(#=obj.getRow()+1#)</span> </th>
    <td class="gdfCell noClip" align="center"><div class="taskStatus cvcColorSquare" status="(#=obj.status#)"></div></td>
    <td class="gdfCell indentCell" style="padding-left:(#=obj.level*10#)px;">
      <div class="(#=obj.isParent()?'exp-controller expcoll exp':'exp-controller'#)" align="center"></div>
      <input type="text" readOnly name="name" style="color:black" value="(#=obj.name#)">
    </td>
    <td class="gdfCell"><input type="text" readOnly style="color:black" name="start"  value="" ></td>
    <td class="gdfCell"><input type="text" readOnly style="color:black" name="end" value="" ></td>
    <td class="gdfCell"><input type="text" readOnly style="color:black" name="duration" value="" ></td>
  </tr>
  --></div>

  <div class="__template__" type="TASKEMPTYROW"><!--
  <tr class="taskEditRow emptyRow" >
    <th class="gdfCell" align="right"></th>
    <td class="gdfCell noClip" align="center"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell"></td>
  </tr>
  --></div>

  <div class="__template__" type="TASKBAR"><!--
  <div class="taskBox taskBoxDiv" taskId="(#=obj.id#)" >
    <div class="layout (#=obj.hasExternalDep?'extDep':''#)">
      <div class="taskStatus" status="(#=obj.status#)"></div>
      <div class="taskProgress" style="width:(#=obj.progress>100?100:obj.progress#)%; background-color:(#=obj.progress>100?'red':'rgb(153,255,51);'#);"></div>
      <div class="milestone (#=obj.startIsMilestone?'active':''#)" ></div>

      <div class="taskLabel"></div>
      <div class="milestone end (#=obj.endIsMilestone?'active':''#)" ></div>
    </div>
  </div>
  --></div>

  <div class="__template__" type="CHANGE_STATUS"><!--
    <div class="taskStatusBox">
      <div class="taskStatus cvcColorSquare" status="STATUS_ACTIVE" title="active"></div>
      <div class="taskStatus cvcColorSquare" status="STATUS_DONE" title="completed"></div>
      <div class="taskStatus cvcColorSquare" status="STATUS_FAILED" title="failed"></div>
      <div class="taskStatus cvcColorSquare" status="STATUS_SUSPENDED" title="suspended"></div>
      <div class="taskStatus cvcColorSquare" status="STATUS_UNDEFINED" title="undefined"></div>
    </div>
  --></div>


  <div class="__template__" type="TASK_EDITOR"><!--
  <div class="ganttTaskEditor">
  <table width="100%">
    <tr>
      <td>
        <table cellpadding="5">
          <tr>
            <td><label for="code">code/short name</label><br><input type="text" readOnly name="code" id="code" value="" class="formElements"></td>
           </tr><tr>
            <td><label for="name">name</label><br><input type="text" readOnly name="name" id="name" value=""  size="35" class="formElements"></td>
          </tr>
          <tr></tr>
            <td>
              <label for="description">description</label><br>
              <textarea rows="5" cols="30" id="description" name="description" class="formElements"></textarea>
            </td>
          </tr>
        </table>
      </td>
      <td valign="top">
        <table cellpadding="5">
          <tr>
          <td colspan="2"><label for="status">status</label><br><div id="status" class="taskStatus" status=""></div></td>
          <tr>
          <td colspan="2"><label for="progress">progress</label><br><input type="text" readOnly name="progress" id="progress" value="" size="3" class="formElements"></td>
          </tr>
          <tr>
          <td><label for="start">start</label><br><input type="text" readOnly name="start" id="start"  value="" class="date" size="10" class="formElements"><input type="checkbox" id="startIsMilestone"> </td>
          <td rowspan="2" class="graph" style="padding-left:50px"><label for="duration">dur.</label><br><input type="text" readOnly name="duration" id="duration" value=""  size="5" class="formElements"></td>
        </tr><tr>
          <td><label for="end">end</label><br><input type="text" readOnly name="end" id="end" value="" class="date"  size="10" class="formElements"><input type="checkbox" id="endIsMilestone"></td>
        </table>
      </td>
    </tr>
    </table>

  <h2>assignments</h2>
  <table  cellspacing="1" cellpadding="0" width="100%" id="assigsTable">
    <tr>
      <th style="width:100px;">name</th>
      <th style="width:70px;">role</th>
      <th style="width:30px;">est.wklg.</th>
      <th style="width:30px;" id="addAssig"><span class="teamworkIcon" style="cursor: pointer">+</span></th>
    </tr>
  </table>

  <div style="text-align: right; padding-top: 20px"><button id="saveButton" class="button big">save</button></div>
  </div>
  --></div>


  <div class="__template__" type="ASSIGNMENT_ROW"><!--
  <tr taskId="(#=obj.task.id#)" assigId="(#=obj.assig.id#)" class="assigEditRow" >
    <td ><select name="resourceId"  class="formElements" (#=obj.assig.id.indexOf("tmp_")==0?"":"disabled"#) ></select></td>
    <td ><select type="select" name="roleId"  class="formElements"></select></td>
    <td ><input type="text" readOnly name="effort" value="(#=getMillisInHoursMinutes(obj.assig.effort)#)" size="5" class="formElements"></td>
    <td align="center"><span class="teamworkIcon delAssig" style="cursor: pointer">d</span></td>
  </tr>
  --></div>


  <div class="__template__" type="RESOURCE_EDITOR"><!--
  <div class="resourceEditor" style="padding: 5px;">

    <h2>Project team</h2>
    <table  cellspacing="1" cellpadding="0" width="100%" id="resourcesTable">
      <tr>
        <th style="width:100px;">name</th>
        <th style="width:30px;" id="addResource"><span class="teamworkIcon" style="cursor: pointer">+</span></th>
      </tr>
    </table>

    <div style="text-align: right; padding-top: 20px"><button id="resSaveButton" class="button big">save</button></div>
  </div>
  --></div>


  <div class="__template__" type="RESOURCE_ROW"><!--
  <tr resId="(#=obj.id#)" class="resRow" >
    <td ><input type="text" readOnly name="name" value="(#=obj.name#)" style="width:100%;" class="formElements"></td>
    <td align="center"><span class="teamworkIcon delRes" style="cursor: pointer">d</span></td>
  </tr>
  --></div>


</div>
<script type="text/javascript">
  $.JST.loadDecorator("ASSIGNMENT_ROW", function(assigTr, taskAssig) {

    var resEl = assigTr.find("[name=resourceId]");
    for (var i in taskAssig.task.master.resources) {
      var res = taskAssig.task.master.resources[i];
      var opt = $("<option>");
      opt.val(res.id).html(res.name);
      if (taskAssig.assig.resourceId == res.id)
        opt.attr("selected", "true");
      resEl.append(opt);
    }


    var roleEl = assigTr.find("[name=roleId]");
    for (var i in taskAssig.task.master.roles) {
      var role = taskAssig.task.master.roles[i];
      var optr = $("<option>");
      optr.val(role.id).html(role.name);
      if (taskAssig.assig.roleId == role.id)
        optr.attr("selected", "true");
      roleEl.append(optr);
    }

    if(taskAssig.task.master.canWrite && taskAssig.task.canWrite){
      assigTr.find(".delAssig").click(function() {
        var tr = $(this).closest("[assigId]").fadeOut(200, function() {
          $(this).remove();
        });
      });
    }


  });
</script>

</body>
</html>
