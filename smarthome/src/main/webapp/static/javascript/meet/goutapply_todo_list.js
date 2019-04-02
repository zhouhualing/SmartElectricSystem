$(function(){
	getCurrenUserInfo(function(data){
		$("#currentUserCode").val(data.userCode);
		doQuery();
	});
});
var editObj = {
		text:"处理",
		fun:function(data) {
			goPage(data);
		}
};

function initFun(data, key) {
	if(key == "reason") {
		var title = handleLongString(data.reason,210,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.reason+"'>"+title+"</div>";
	}
}

function goPage(data) {
	window.location.href="new_goutapply_examine.html?id="+data.goutApplyId+"&type="+data.type+"&targetStep="+data.step;
}
var oppObj = [editObj];

