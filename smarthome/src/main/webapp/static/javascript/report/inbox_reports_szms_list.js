/**
 * init method
 */
$(function(){
	getCurrenUserInfo(function(data){
		$("#createUserCode").val(data.userCode);
		doQuery();
	});
});


function initFlag(data) {
	//if($("#reportInfoQuery").attr("queryId") == "reportInboxQuery") {
		if(data.completeDate != null) {
			if(data.flag == '2') {
				return "<img src='../../static/images/green.png'/>";
			} else {
				return "<img src='../../static/images/red.png'/>";
			}
		} else {
			return " ";
		}
/*	}
	
	if($("#reportInfoQuery").attr("queryId") == "reportInboxMeQuery") {
		if(data.flag == '2') {
			return "<img src='../../static/images/green.png'/>";
		} else {
			return "<img src='../../static/images/red.png'/>";
		}
	}*/
}

function initFun(data, key) {
	if(key == "reqRepTitle"){
		var title = handleLongString(data.reqRepTitle,50,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.reqRepTitle+"'>"+title+"</div>";
	}
}

/*$("[name='changeRole']").on("change",function(){
	if($(this).val() == "0001") {
		$("#reportInfoQuery").attr("queryId","reportInboxAllQuery");
		$("#reportInfoQuery").empty();
		clickmedCommonTableToScan();
		doQuery();
	}

	if($(this).val() == "0002") {
		$("#reportInfoQuery").attr("queryId","reportInboxQuery");
		$("#reportInfoQuery").empty();
		oppObj = [approvalObj];
		wf_showProcessOnTable("reportInfoQuery","显示流程图")
		clickmedCommonTableToScan();
		doQuery();
	}
	
	if($(this).val() == "0003") {
		$("#reportInfoQuery").attr("queryId","reportInboxMeQuery");
		$("#reportInfoQuery").empty();
		oppObj = [approvalObj1];
		wf_showProcessOnTable("reportInfoQuery","显示流程图")
		clickmedCommonTableToScan();
		doQuery();
	}
})*/

/*var scanObj = {
		text:"查看",
		fun:function(data) {
			window.location.href="drafting_reports_show.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}
*/
var approvalObj = {
		text:"签批",
		fun:function(data) {
			goPage(data);
		}
}

/*var approvalObj1 = {
		text:"办理",
		fun:function(data) {
			window.location.href="new_reqrep_examine.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}*/


var oppObj = [approvalObj];

function goPage(data) {
	window.location.href="new_reqrep_examine.html?id="+data.reqRepId+"&taskId="+data.taskId+"&mark=002";
}
