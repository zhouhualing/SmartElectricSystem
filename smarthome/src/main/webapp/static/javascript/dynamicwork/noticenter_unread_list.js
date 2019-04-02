var businessType = getURLParam("businessType");
$("#businessType").val(businessType);
$("#status").val("0002");
$(function(){
	getCurrenUserInfo(function(data){
		$("#userId").val(data.userId);
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
	if(key == "todoTheme") {
		var title = handleLongString(data.todoTheme,135,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.todoTheme+"'>"+title+"</div>";
	}
}

function goPage(data) {
	window.location.href="dynamicwork_beSended.html?id="+data.businessId+"&fromId="+data.id+"&mark=007";
}

var oppObj = [editObj];

