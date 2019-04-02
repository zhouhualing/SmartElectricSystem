$(function(){
	getCurrenUserInfo(function(data){
		$("#roleCode").val(data.roleCodes)
		$("#createUserCode").val(data.userCode);
		$("#userId").val(data.userId);
		doQuery();
	});
});
var handleObj = {
		text:"查看",
		fun:function(data) {
			goPage(data);
		}
}

function initFun(data, key) {
	if(key == "reason") {
		var title = handleLongString(data[key],135,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data[key]+"'>"+title+"</div>";
	}
}

function goPage(data) {
	window.location.href = "new_goutapply_check.html?id="+data.id;
}

var oppObj = [handleObj];


