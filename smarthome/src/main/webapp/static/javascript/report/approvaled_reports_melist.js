$("#reportType,#status").on("change",function(){
	doQuery();
})

/**
 * init method
 */
$(function(){
	getCurrenUserInfo(function(data){
		$("#createUserCode").val(data.userCode);
		$("#userCode").val(data.userCode);
		doQuery();
	})
	
});

var scanObj = {
		text:"查看",
		fun:function(data) {
			window.location.href="drafting_reports_show.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}

var approvalObj = {
		text:"签批",
		fun:function(data) {
			window.location.href="inbox_reports_approval.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}


var oppObj = [scanObj];


