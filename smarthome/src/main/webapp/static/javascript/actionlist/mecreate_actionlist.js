var check = getURLParam("check");//1:我创建的 2：我发起的

/*
function initFlag(data) {
	if(data.flag == 0) {
		return "";
	} else {
		return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/>";
	}
}
*/
/**
 * init method
 */
$(function(){
	getCurrenUserInfo(function(data){
		if(check == 1){
			$("#collapseOne").append('<input type="hidden" name="createUserId"  id="userId" value="'+data.userId+'"/>');
		}else{
			$("#collapseOne").append('<input type="hidden" name="tobeCreatedUserId"  id="userId" value="'+data.userId+'"/>');
		}
		doQuery();
	});
});

function initFun(data, key) {
	if(key == "businessTitle") {
		var title = handleLongString(data.businessTitle,152,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.businessTitle+"'>"+title+"</div>";
	}
}

function goPage(data) {
	window.location.href="show_actionlist.html?appFlag=1&id="+data.id+"&check="+check;
}

var scanObj = {
		text:"查看",
		fun:function(data) {
			goPage(data);
		}
}
var oppObj = [scanObj];
