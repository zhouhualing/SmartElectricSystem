$("#reportType,#status").on("change",function(){
	doQuery();
})

var userId = "";


function initFlag(data) {
	if(data.flag == 0) {
		return "";
	} else {
		return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/>";
	}
}

/**
 * init method
 */
$(function(){
		
		doQuery();
	
});
var nowSearchObj = null;
function createAnalysis() {
	var objs = clickmedTables.reportInfoQuery.createSearchParam();
	nowSearchObj = objs;
	var obj = {
	    title:'饼图分析',
	    height:"500px",
	    width:"750px",
	    url:'supervise_approval.html?type=0001',
	    myCallBack:myCallBack,
	    cancelText:"关闭",
	    confirmBtn:false,
	    fun:true
	}
	new jqueryDialog(obj);
}

function myCallBack() {
	
}

var scanObj = {
		text:"督办",
		fun:function(data) {
			window.location.href="../../view/actionlist/show_actionlist.html?fromPage=0003&id="+data.id;
		}
}

var approvalObj = {
		text:"签批",
		fun:function(data) {
			window.location.href="inbox_reports_approval.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}

var approvalObj1 = {
		text:"办理",
		fun:function(data) {
			window.location.href="new_reqrep_examine.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}


var oppObj = [scanObj];


