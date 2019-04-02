var task = getURLParam("task");
$(function(){
	if(task=='1'){
		doQuery();
	}else{
		getCurrenUserInfo(function(data){
			$("#userCode").val(data.userCode);
			doQuery();
		});
	}
});
var handleObj = {
		text:"查看",
		fun:function(data) {
			goPage(data);
		}
}

function initFun(data, key) {
	if(key == "title") {
		var title = handleLongString(data[key],1305,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data[key]+"'>"+title+"</div>";
	}
}

function goPage(data) {
	window.location.href = "/cmcp/view/meet/meet_mark_show.html?id="+data.businessKey;
}

var oppObj = [handleObj];

