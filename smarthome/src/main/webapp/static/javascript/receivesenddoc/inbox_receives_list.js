//收文待办事项的js

$("#reportType,#status").on("change",function(){
	doQuery();
})

var userId = "";

var type="0001";
function initFlag(data) {

	if($("#reportInfoQuery").attr("queryId") == "receiveInboxQuery") {
		if(data.completeDate != null) {
			if(new Date().getTime() - data.completeDate < 0) {
	//			return "<span style ='color:blue'>!</span>";
				return "<img src='../../static/images/green.png'/>";
			} else {
	//			return "<span style ='color:red'>!</span>";
				return "<img src='../../static/images/red.png'/>";
			}
		} else {
			return " ";
		}
	}
	
	if($("#reportInfoQuery").attr("queryId") == "receiveInboxMeQuery") {
		
//		86 400 000
		
		
		if((new Date().getTime()) - (new Date(new Date(data.assignTime).format("yyyy-MM-dd 00:00:00")).getTime()) < 172800000) {
//			return "<span style ='color:blue'>!</span>";
			return "<img src='../../static/images/green.png'/>";
		} else {
//			return "<span style ='color:red'>!</span>";
			return "<img src='../../static/images/red.png'/>";
		}
	}
}

$("[name='changeRole']").on("change",function(){
	if($(this).val() == "0001") {
		$("#reportInfoQuery").attr("queryId","receiveInboxAllQuery");
		$("#reportInfoQuery").empty();
		clickmedCommonTableToScan();
		doQuery();
	}

	if($(this).val() == "0002") {
		$("#reportInfoQuery").attr("queryId","receiveInboxQuery");
		$("#reportInfoQuery").empty();
		oppObj = [approvalObj];
		clickmedCommonTableToScan();
		doQuery();
	}
	
	if($(this).val() == "0003") {
		$("#reportInfoQuery").attr("queryId","receiveInboxMeQuery");
		$("#reportInfoQuery").empty();
		oppObj = [approvalObj1];
		clickmedCommonTableToScan();
		doQuery();
	}
})

/**
 * init method
 */
$(function(){
	wf_showProcessOnTable("reportInfoQuery","查看流程图");
	getCurrenUserInfo(function(data){
		$("#assigneeUserCode").val(data.userCode);
		$("#createUserId").val(data.userId);
		doQuery();
	})
	
});


var editObj = {
		text:"处理",
		fun:function(data) {
			if(type=='0001' || type=='0002' || type=='0005' || type=='0006'){
				window.location.href="drafting_receives_show.html?id="+data.receiveId+"&taskId="+data.taskId+"&fromPage=pc"+"&type="+type;
			}else if(type=='0003' || type=='0007'){
				
			}
			
		}
}

var scanObj = {
		text:"查看",
		fun:function(data) {
			window.location.href="drafting_receives_show.html?id="+data.reportId+"&taskId="+data.taskId+"&fromPage=pc";
		}
}

var approvalObj = {
		text:"签批",
		fun:function(data) {
			window.location.href="inbox_receives_approval.html?id="+data.receiveId+"&taskId="+data.taskId+"&fromPage=pc";
		}
}

var approvalObj1 = {
		text:"办理",
		fun:function(data) {
			window.location.href="inbox_receives_meapproval.html?id="+data.receiveId+"&taskId="+data.taskId+"&fromPage=pc";
		}
}


var oppObj = [editObj];


