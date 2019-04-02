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
		text:"编辑",
		fun:function(data) {
			window.location.href="new_reqrep_draft.html?id="+data.id+"&fromPage=0001&type="+data.reqRepType;
		}
}

var deleObj = {
		text:"删除",
		fun:function(data) {
			$.confirm({msg:"确定要删除么？",confirmClick:function(){
				var dto = {
						id:data.id
				}
				doJsonRequest("/reqrep/deleteReqRep", dto, function(data,status){
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

function initFun(data, key) {
	if(key == "reqRepTitle") {
		var title = handleLongString(data.reqRepTitle,42,"...");
		var url = "new_reqrep_draft.html?id="+data.id+"&fromPage=0001&type="+data.reqRepType;
		return "<div class='reportTitles' style='cursor:pointer;' target='_self' onclick='goPage("+url+")' title='"+data.reqRepTitle+"'>"+title+"</div>";
	}
    function goPage(str){
        window.location.href=str;
    }
}

var oppObj = [editObj,deleObj];


