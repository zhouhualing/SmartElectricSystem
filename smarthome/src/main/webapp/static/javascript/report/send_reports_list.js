$("#reportType,#status").on("change",function(){
	doQuery();
})

/**
 * init method
 */
$(function(){
	$("#status option").eq(1).remove();
	getCurrenUserInfo(function(data){
		$("#createUserCode").val(data.userCode);
		doQuery();
	})
	
});

var scanObj = {
		text:"查看",
		fun:function(data) {
			window.location.href="drafting_reports_show.html?id="+data.id
		}
}

var editObj = {
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


var oppObj = [editObj];


