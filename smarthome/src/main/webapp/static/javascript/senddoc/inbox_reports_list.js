
var userId = "";

$(function(){
	var roleType =getURLParam("roleType");
	getCurrenUserInfo(function(data){
		$("#assigneeUserCode").val(data.userCode);
		$("#createUserId").val(data.userId);
		if(roleType=="follow"){
			roleType = "";
			$('input[name="changeRole"][value="0003"]').attr("checked", true);
			$("#reportInfoQuery").attr("queryId","senddocInboxMeQuery");
			$("#reportInfoQuery").empty();
			oppObj = [approvalObj1];     //配置操作按钮
			clickmedCommonTableToScan(); //表格初始化
		}
		doQuery();
	})
});

$("[name='changeRole']").on("change",function(){
	if($(this).val() == "0001") {
		$("#reportInfoQuery").attr("queryId","senddoctInboxAllQuery");
		$("#reportInfoQuery").empty();
		clickmedCommonTableToScan();
		doQuery();
	}
    //选择签批人时列表
	if($(this).val() == "0002") {
		$("#reportInfoQuery").attr("queryId","senddocInboxQuery");
		$("#reportInfoQuery").empty();
		oppObj = [approvalObj];      //配置操作按钮
		clickmedCommonTableToScan(); //表格初始化
		doQuery();
	}
	//选择跟件人时列表
	if($(this).val() == "0003") {
		$("#reportInfoQuery").attr("queryId","senddocInboxMeQuery");
		$("#reportInfoQuery").empty();
		oppObj = [approvalObj1];     //配置操作按钮
		clickmedCommonTableToScan(); //表格初始化
		doQuery();
	}
})

function initFlag(data) {
	if($("#reportInfoQuery").attr("queryId") == "senddocInboxQuery") {
		//判断是否过期
		if(data.completeDate != null) {
			if(new Date().getTime() - data.completeDate < 0) {
				return "<img src='../../static/images/green.png'/>";
			} else {
				return "<img src='../../static/images/red.png'/>";
			}
		} else {
			return " ";
		}
	}
	
	if($("#reportInfoQuery").attr("queryId") == "senddocInboxMeQuery") {
		if(data.flag == '2') {
			return "<img src='../../static/images/green.png'/>";
		} else {
			return "<img src='../../static/images/red.png'/>";
		}
	}
}


/*var approvalObj = {
		text:"签批",
		fun:function(data) {
			window.location.href="inbox_reports_approval.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}*/

var approvalObj1 = {
		text:"办理",
		fun:function(data) {
			window.location.href="new_reqrep_examine.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}


/*var oppObj = [approvalObj];*/


