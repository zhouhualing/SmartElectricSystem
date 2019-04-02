$("#reportType,#status").on("change",function(){
	doQuery();
})

var task = getURLParam("task");

/**
 * init method
 */
$(function(){
	$("#status option").eq(1).remove();
	if(task == 1){
		//我发起的公文
		getCurrenUserInfo(function(data){
			$("#createUserCode").val(data.userCode);
			$("#reportInfoQuery").attr("queryId","senddocShowQuery");
			$("#reportInfoQuery").attr("conf","{title:'我发起的公文',customBtn:true,createDelOpp:false,autoLoad:false,searchFiledName:'reportTitle'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
	}else if(task == 2){
		//审批中
		getCurrenUserInfo(function(data){
			$("#createUserCode").val(data.userCode);
			$("#reportInfoQuery").attr("queryId","senddocShowspzQuery");
			$("#reportInfoQuery").attr("conf","{title:'审批中',customBtn:true,createDelOpp:false,autoLoad:false,searchFiledName:'reportTitle'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
	}else if(task == 3){
		//已分发
		getCurrenUserInfo(function(data){
			$("#createUserCode").val(data.userCode);
			$("#reportInfoQuery").attr("queryId","senddocShowyffQuery");
			$("#reportInfoQuery").attr("conf","{title:'已分发',customBtn:true,createDelOpp:false,autoLoad:false,searchFiledName:'reportTitle'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
	}else if(task == 4){
		//已办结
		getCurrenUserInfo(function(data){
			$("#createUserCode").val(data.userCode);
			$("#reportInfoQuery").attr("queryId","senddocShowybjQuery");
			$("#reportInfoQuery").attr("conf","{title:'已办结',customBtn:true,createDelOpp:false,autoLoad:false,searchFiledName:'reportTitle'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
		
		
	}
});

var scanObj = {
		text:"查看",
		fun:function(data) {
			if(data.reportType == "0009"){
				window.location.href="new_nfw_check.html?id="+data.id+"&type="+data.reportType+"&showCard=1";
			}else if(data.reportType == "5002"){
				window.location.href="../receivedoc/new_xjlw_check.html?id="+data.id+"&type="+data.reportType;
			}else{
				window.location.href="new_fwfh_check.html?id="+data.id+"&type="+data.reportType+"&showCard=1";
			}
			
		}
}

/*var editObj = {
		text:"分发",
		fun:function(data) {
			if(data.status =="0003"||data.status =="0006") {
				window.location.href="send_reports.html?id="+data.id
			} else {
				$.alert("完成或分发状态的可以分发。");
			}
			
		}
}

var deleObj = {
		text:"删除",
		fun:function(data) {
			$.confirm({msg:"确定要删除么？",confirmClick:function(){
				var dto = {
						id:data.id
				}
				doJsonRequest("/senddoc/deleteReport", dto, function(data,status){
					doQuery();
					if(data.result) {
						$.alert("删除成功。");
					} else {
						$.alert("删除失败。");
					}
				},{showWaitting:true})				
			},data:"1"})

		}
}*/


var oppObj = [scanObj];


