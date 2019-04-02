$("#reportType,#status").on("change",function(){
	doQuery();
})

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
		$("#reportInfoQuery").attr("queryId","reportApprovaledQuery");
		$("#reportInfoQuery").empty();
		clickmedCommonTableToScan();
		doQuery();
	}
	
	if($(this).val() == "0003") {
		$("#reportInfoQuery").attr("queryId","reportApprovaledMeQuery");
		$("#reportInfoQuery").empty();
		clickmedCommonTableToScan();
		doQuery();
	}
})

var scanObj = {
		text:"查看",
		fun:function(data) {
			var fromPage = '0002';
			if($("[name='changeRole']:checked").val() == "0003") {
				fromPage = '0003';
			}
			
			window.location.href="drafting_reports_show.html?fromPage="+fromPage+"&id="+data.reportId+"&taskId="+data.taskId;
		}
}

var approvalObj = {
		text:"签批",
		fun:function(data) {
			window.location.href="inbox_reports_approval.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}


var oppObj = [scanObj];


