$("#reportType,#status").on("change",function(){
	doQuery();
})

/**
 * init method
 */
$(function(){
	getCurrenUserInfo(function(data){
		$("#assigneeUserCode").val(data.userCode);
		$("#createUserId").val(data.userId)
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
		text:"办理",
		fun:function(data) {
			window.location.href="new_reqrep_examine.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}


var oppObj = [approvalObj];


