$("#reportType,#status").on("change",function(){
	doQuery();
})

var task = getURLParam("task");

/**
 * init method
 */
$(function(){
	$("#status option").eq(1).remove();
	if(task == 0){
		$(".row").append('<input type="hidden" name="status"  id="status" value="0002" class="form-control" placeholder=""/>');
		//委办局发起的请示报告
		getCurrenUserInfo(function(data){
			$("#createUserCode").val(data.userCode);
			$("#reportInfoQuery").attr("queryId","qsbgShowQuery");
			$("#reportInfoQuery").attr("conf","{title:'发出的请示报告',customBtn:true,createDelOpp:false,autoLoad:false,searchFiledName:'receiveTitle'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
	}else if(task == 1){
		$(".row").append('<input type="hidden" name="status"  id="status" value="0002" class="form-control" placeholder=""/>');
		//我发起的公文
		getCurrenUserInfo(function(data){
			$("#createUserCode").val(data.userCode);
			$("#reportInfoQuery").attr("queryId","receivedocShowQuery");
			$("#reportInfoQuery").attr("conf","{title:'发起的公文',customBtn:true,createDelOpp:false,autoLoad:false,searchFiledName:'receiveTitle'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
	}else if(task == 2){
		//审批中
		$(".row").append('<input type="hidden" name="status"  id="status" value="0002" class="form-control" placeholder=""/>');
		getCurrenUserInfo(function(data){
			$("#createUserCode").val(data.userCode);
			$("#reportInfoQuery").attr("queryId","receivedocShowspzQuery");
			$("#reportInfoQuery").attr("conf","{title:'审批中',customBtn:true,createDelOpp:false,autoLoad:false,searchFiledName:'receiveTitle'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
	}else if(task == 3){
		//已分发
		$(".row").append('<input type="hidden" name="status"  id="status" value="0006" class="form-control" placeholder=""/>');
		getCurrenUserInfo(function(data){
			$("#createUserCode").val(data.userCode);
			$("#reportInfoQuery").attr("queryId","receivedocShowyffQuery");
			$("#reportInfoQuery").attr("conf","{title:'已分发',customBtn:true,createDelOpp:false,autoLoad:false,searchFiledName:'receiveTitle'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
	}else if(task == 4){
		//已办结
		$(".row").append('<input type="hidden" name="status"  id="status" value="0007" class="form-control" placeholder=""/>');
		getCurrenUserInfo(function(data){
			$("#createUserCode").val(data.userCode);
			$("#reportInfoQuery").attr("queryId","receivedocShowybjQuery");
			$("#reportInfoQuery").attr("conf","{title:'已办结',customBtn:true,createDelOpp:false,autoLoad:false,searchFiledName:'receiveTitle'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
		
		
	}
});

var scanObj = {
		text:"查看",
		fun:function(data) {
			if(data.reportType == "5002"){
				window.location.href="new_xjlw_check.html?id="+data.id+"&type="+data.reportType;
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


