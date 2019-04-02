
/**
 * init method
 */
$(function(){
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#createUserCode").val(data.userCode);
		doQuery();
	})
	
});


$("[name='changeRole']").on("change",function(){
	if($(this).val() == "0001") {
		$("#reportInfoQuery").attr("queryId","reportInboxAllQuery");
		$("#reportInfoQuery").empty();
		clickmedCommonTableToScan();
		doQuery();
	}

	if($(this).val() == "0002") {
		$("#reportInfoQuery").attr("queryId","receiveApprovaledQuery");
		$("#reportInfoQuery").empty();
		clickmedCommonTableToScan();
		doQuery();
	}
	
	if($(this).val() == "0003") {
		$("#reportInfoQuery").attr("queryId","receiveApprovaledMeQuery");
		$("#reportInfoQuery").empty();
		clickmedCommonTableToScan();
		doQuery();
	}
})

var scanObj = {
		text:"查看",
		fun:function(data) {
			window.location.href="drafting_receives_show.html?id="+data.receiveId+"&taskId="+data.taskId;
		}
}

var approvalObj = {
		text:"签批",
		fun:function(data) {
			window.location.href="inbox_reports_approval.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}


var oppObj = [scanObj];


