$("#reportType,#status").on("change",function(){
	doQuery();
})

var userId = "";


function initFlag(data) {
	if($("#reportInfoQuery").attr("queryId") == "reportInboxQuery") {
		if(data.completeDate != null) {
			if(data.flag == '2') {
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
	
	if($("#reportInfoQuery").attr("queryId") == "reportInboxMeQuery") {
		
//		86 400 000
		
		if(data.flag == '2') {
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
		$("#reportInfoQuery").attr("queryId","reportInboxAllQuery");
		$("#reportInfoQuery").empty();
		clickmedCommonTableToScan();
		doQuery();
	}

	if($(this).val() == "0002") {
		$("#reportInfoQuery").attr("queryId","reportInboxQuery");
		$("#reportInfoQuery").empty();
		oppObj = [approvalObj];
		clickmedCommonTableToScan();
		doQuery();
	}
	
	if($(this).val() == "0003") {
		$("#reportInfoQuery").attr("queryId","reportInboxMeQuery");
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
	getCurrenUserInfo(function(data){
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
		text:"认领",
		fun:function(data) {
			wf_claim(data,function(){
				$.alert("认领成功。");
				doQuery();
			})
		}
}

var approvalObj1 = {
		text:"办理",
		fun:function(data) {
			window.location.href="new_reqrep_examine.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}


var oppObj = [approvalObj];


