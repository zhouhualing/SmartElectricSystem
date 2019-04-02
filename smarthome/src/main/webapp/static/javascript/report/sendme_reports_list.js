$("#reportType,#status").on("change",function(){
	doQuery();
})

/**
 * init method
 */
$(function(){
	$("#status option").eq(1).remove();
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		doQuery();
	})
	
});

var scanObj = {
		text:"查看",
		fun:function(data) {
			window.location.href="reapply_reports.html?status="+data.status+"&sendId="+data.id+"&id="+data.reportEntity.id
		}
}

var replayObj = {
		text:"回执",
		fun:function(data) {
			if(data.status == "0003") {
				$.alert("您已经回执过了。");
			} else {
				if(data.reportEntity.status != "0006") {
					$.alert("该请示报告不可回执。");
				} else {
					window.location.href="reapply_reports.html?sendId="+data.id+"&id="+data.reportEntity.id
				}
			}
		}
}

var editObj = {
		text:"编辑",
		fun:function(data) {
			window.location.href="new_reqrep_draft.html?id="+data.id
		}
}

var deleObj = {
		text:"删除",
		fun:function(data) {
			$.confirm({msg:"确定要删除么？",confirmClick:function(){
				var dto = {
						id:data.id
				}
				doJsonRequest("/report/deleteReport", dto, function(data,status){
					doQuery();
					if(data.result) {
						$.alert("删除成功。");
					} else {
						$.alert("删除失败。");
					}
				},{showWaitting:true})				
			},data:"1"})

		}
}

function initFun(datas,columnKey) {
	if(columnKey == "status") {
		if(datas.status == "0003") {
			return "已回执"
		}
		return "未回执"
	}
}

var oppObj = [scanObj];


