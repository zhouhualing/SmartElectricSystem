$("#reportType,#status").on("change",function(){
	doQuery();
})

/**
 * init method
 */
$(function(){
	$("#status option").eq(1).remove();
	doQuery();
});

var scanObj = {
		text:"查看",
		fun:function(data) {
			window.location.href="drafting_reports_show.html?id="+data.id
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


var oppObj = [scanObj];


