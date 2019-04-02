$("#reportType,#status").on("change",function(){
	doQuery();
	
})

/**
 * init method
 */
$(function(){
	getCurrenUserInfo(function(data){
		$("#createUserCode").val(data.userCode);
		doQuery();
	})
	
});


var editObj = {
		text:"查看详情",
		fun:function(data) {
			window.location.href="show_actionlist.html?id="+data.id+"&fromPage=0001"
		}
}

var deleObj = {
		text:"删除",
		fun:function(data) {
			$.confirm({msg:"确定要删除么？",confirmClick:function(){
				doJsonRequest("/actionList/deleteActionList", data.id, function(data,status){
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


