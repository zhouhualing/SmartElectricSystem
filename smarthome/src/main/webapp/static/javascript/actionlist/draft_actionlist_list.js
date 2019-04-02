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

function initFun(data, key) {
	if(key == "businessTitle") {
		var title = handleLongString(data.businessTitle,170,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.businessTitle+"'>"+title+"</div>";
	}
}

function goPage(data) {
	window.location.href="draft_actionlist.html?id="+data.id+"&fromPage=0001"
}

var editObj = {
		text:"编辑",
		fun:function(data) {
			goPage(data);
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


var oppObj = [editObj,deleObj];


